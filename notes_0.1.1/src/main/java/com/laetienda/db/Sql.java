package com.laetienda.db;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.ClassNotFoundException;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class Sql {
	
	private Properties settings;
	private Connection connection;
	private String query;
	private Statement stm;
	private FileWriter fileWriter;
	private FileReader fileReader;
	private CopyManager copyManager;
	
	public static void main (String[] args){
		
		File directory = new File("/home/myself/git/eclipse/Web.opt");
		
		try{
			Sql sql = new Sql(directory);
			File file = new File(directory.getAbsolutePath() + File.separator + "var" + File.separator + "lang" + File.separator + "tabla.csv");
			String query = "COPY tabla TO STDOUT";
			sql.queryToCsv(query, file);
		}catch(SqlException ex){
			ex.getMessage();
			ex.getQuery();
			ex.printStackTrace();
		}
	}
	
	public Sql(File directory) throws SqlException{
		
		settings = loadSettings(directory);
	}
	
	public void query(String query) throws SqlException{
		
		this.query = query;
		start();
		
		try{
			stm = connection.createStatement();
			stm.execute(query);
			
		}catch (SQLException ex){
			throw new SqlException("Exception caught while executing query", query, ex);
		}finally{
			close();
		}
	}
	
	public void queryToCsv(String query, File file) throws SqlException {
		
		this.query = query;
		start();
		
		try{
			fileWriter = new FileWriter(file);
			copyManager.copyOut(query, fileWriter);
		}catch(IOException ex){
			throw new SqlException("Filed to read/write the file or connection to database failed", query, ex);
		}catch(SQLException ex){
			throw new SqlException("Filed query the database", query, ex);
		}finally{
			close();
		}
	}
	
	public void csvToTable(String query, File file) throws SqlException {
		this.query = query;
		start();
		
		try{
			fileReader = new FileReader(file);
			copyManager.copyIn(query, fileReader);
		}catch(FileNotFoundException ex){
			throw new SqlException("Failed to read/write the file or connection to database failed", query, ex);
		}catch(IOException ex){
			throw new SqlException("Failed to read the file or to connect to database", query, ex);
		}catch(SQLException ex){
			throw new SqlException("Failed to execute query", query, ex);
		}finally{
			close();
		}
	}
	
	private void start() throws SqlException{
		
		String jdbcUrl =  "jdbc:postgresql://" 
				+ settings.getProperty("db_host") + ":" 
				+ settings.getProperty("db_port") + "/" 
				+ settings.getProperty("database");
		
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(jdbcUrl, settings.getProperty(JDBC_USER), settings.getProperty(JDBC_PASSWORD));
			copyManager = new CopyManager((BaseConnection) connection);
		}catch (ClassNotFoundException ex){
			throw new SqlException("Class not found. $class: org.postgresql.Driver", query, ex);
		}catch (SQLException ex){
			throw new SqlException("Exception caught while connection to the database", query, ex);
		}
	}
	
	private void close(){
		
		try{
			if(fileReader != null){
				fileReader.close();
			}
			
			if(fileWriter != null){
				fileWriter.close();
			}
			
			if(stm != null){
				stm.close();
			}
			
			if(connection != null){
				connection.close();
			}	
			
		}catch (IOException ex){
			System.out.println("Failed to close file");
		}catch (SQLException ex){
			System.out.println("Failed to close connection. $query: " + query);
		}finally{
			fileWriter = null;
			fileReader = null;
			copyManager = null;
			stm = null;
			connection = null;
		}
	}
	
	private Properties loadSettings(File directory) throws SqlException{
		
		Properties defaults = new Properties();
		
		defaults.setProperty(JDBC_URL, "jdbc:postgresql://db.la-etienda.lan:5432/db");
		defaults.setProperty(JDBC_USER, "db");
		defaults.setProperty(JDBC_PASSWORD, "www.myself.com");
		
		Properties settings = new Properties(defaults);
		
		FileInputStream conf;
		
		String path = directory.getAbsolutePath() 
				+ File.separator + "etc"
				+ File.separator + "database"
				+ File.separator + "conf.xml";
		
		try{
			conf = new FileInputStream(new File(path));
			settings.loadFromXML(conf);
		}catch(Exception ex){
			throw new SqlException("Exception caught while loading conf file from .xml file", null, ex);
		}finally{
			
		}
		
		return settings;
	}
}
