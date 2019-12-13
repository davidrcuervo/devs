package com.laetienda.install;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.app.AppException;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.db.Installer;

/**
 * 
 * @author MySelf
 * This class has the basic configuration required to run the web framework.
 *
 */
public class Instalador {
	
	private final static Logger log = LogManager.getLogger(Instalador.class);
	
	private static final Options OPTIONS = new Options()
			.addOption(new Option("u", "user", true, "Admin user DN with priviledges to create domain settings."))
			.addOption(new Option("h", "help", false, "Help"))
			.addOption(new Option("p", "password", true, "Password of the admin user to connecto to LDAP"))
			.addOption(new Option("r", "rootPassword", true, "Password of root operative system"));
	
	private File directory;
	private CommandLine line;
	
	/**
	 * 
	 * @param directory Requires application directory, it will look at configuration files in etc folder.
	 */
	public Instalador(File directory){
		this.directory = directory;
	}
	
	/**
	 * @param args 
	 * @throws InstallerException Exception thrown when option does not exist
	 */
	public void parseCommand(String[] args) throws InstallerException{
		
		try{
			CommandLineParser parser = new DefaultParser();
			line = parser.parse(OPTIONS, args);
			
			if(line.hasOption("help")){
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("Install options", OPTIONS);
			}
		}catch(ParseException ex){
			throw new InstallerException("Error while parsing arguments", ex);
		}
	}
	
	public void dap() throws DapException{
	
		if(line.hasOption("user") && line.hasOption("password")){
			log.debug("$user: " + line.getOptionValue("user"));
			
			com.laetienda.dap.Installer installer = new com.laetienda.dap.Installer(directory);
			
			if(line.hasOption("rootPassword")){
				installer.setRootPassword(line.getOptionValue("rootPassword"));
			}
			
			try {
				installer.setConnection();
				installer.bind(line.getOptionValue("user"), line.getOptionValue("password"));
				installer.install();
				installer.unBind();
				log.info("Apache Active directory has been installed succesfully");
			}catch(DapException ex) {
				log.error("Apache Active Directory (DAP) failed to install.");
				throw ex;
			}finally {
				installer.closeConnection();
			}
//			installer.install(line.getOptionValue("user"), line.getOptionValue("password"));
		}else{
			throw new DapException("Username or password missing. User \"--help\" for more information");
		}	
	}
	

	public void database() throws AppException {
		
		log.info("Installing database structure");
		DapManager dap = null;
		DbManager dbManager = null;
		LdapConnection ldap = null;
		Db db = null;
		
		try {
			dap = new DapManager(directory);
			dbManager = new DbManager(directory);
			dbManager.setCreateDatabaseVariable();
			dbManager.open();
						
	//		Installer dbInstaller = new Installer(new File(directory.getAbsolutePath()));
			Installer dbInstaller = new Installer();
			db = dbManager.createTransaction();
			ldap = dap.createLdap(line.getOptionValue("user"), line.getOptionValue("password"));		
			dbInstaller.run(ldap, db.getEm());
			log.info("Database has been installed succesfully");
		}catch(DapException | DbException e) {
			throw e;
		}finally {
			dbManager.closeTransaction(db);
			dbManager.close();
			dap.closeConnection(ldap);
		}
	}
}
