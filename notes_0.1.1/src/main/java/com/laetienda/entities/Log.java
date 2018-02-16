package com.laetienda.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Calendar;
import java.io.IOException;
import javax.persistence.*;
import com.laetienda.logger.*;

/**
 * Entity implementation class for Entity: Log
 *
 */
@Entity
@Table(name="logs@logger")

@Deprecated
public class Log extends EntityObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "logger_logs_id_seq", sequenceName = "logger_logs_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logger_logs_id_seq")
	@Column(name="id", updatable=false, unique=true, nullable=false)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@Column(name="\"user\"", nullable=false, unique=false, length=254)
	private String user;
	
	@Column(name="\"program\"", nullable=false, unique=false, length=254)
	private String program;
	
	@Column(name="\"method\"", nullable=true, unique=false, length=254)
	private String method;
	
	@Column(name="\"line\"", nullable=true, unique=false)
	private Integer line;
	
	@Column(name="\"level\"", nullable=false, unique=false, length=254)
	private String level;
	
	
	//uni-directional one-to-one association to TextIndex
	@OneToOne
	@JoinColumn(name="\"log\"")
	private TextIndex message;

	public Log() {
		super();
		created = Calendar.getInstance();
		modified = Calendar.getInstance();
	}   
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	
	public Calendar getCreated() {
		return this.created;
	}
	
	
	public String getCreatedDateInString(){
		return super.getDate(getCreated());
	}
	
	public Calendar getModified() {
		return this.modified;
	}
	
	public String getModifiedDateInString(){
		return super.getDate(getModified());
	}
	
	/**
	 * @return name of user who is sending the log.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param name of user who is sending the log. length = 254
	 * @throws IOException when is null, or it is empty or it contains more than 254 characters.
	 */
	public void setUser(String username) throws LoggerException {
		
		if(username != null && !username.isEmpty() && username.length() < 254){
			this.user = username;
		}else{
			throw new LoggerException("Name of user is null or empty or it contains more than 254 characters");
		}
	}
	/**
	 * @return the program
	 * 
	 */
	public String getProgram() {
		return program;
	}
	/**
	 * @param Name of program/script that is adding the log
	 * @throws IOException when is null, or it is empty or it contains more than 254 characters.
	 */
	public void setProgram(String program) throws LoggerException {
		
		if(program != null && !program.isEmpty() && program.length() < 254){
			this.program = program;
		}else{
			throw new LoggerException("Name of program is null or empty or it contains more than 254 characters");
		}
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the line
	 */
	public Integer getLine() {
		return line;
	}
	
	/**
	 * @param line the line to set
	 */
	public void setLine(Integer line) {
		this.line = line;
	}
	
	/**
	 * 
	 * @param line
	 */
	public void setLine(String line){
		
		try{
			setLine(Integer.parseInt(line));
		}catch(NumberFormatException ex){
			setLine(0);
		}
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) throws LoggerException{
		boolean flag = false;
		
		for(String temp : LoggerManager.getLevels()){
			//System.out.println("level: " + temp);
			if(temp.toUpperCase().equals(level.toUpperCase())){
				flag = true;
				break;
			}
		}
		
		if(flag){
			this.level = level;
		}else{
			throw new LoggerException("The log level does not exist. $level: " + level);
		}
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message.getText();
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = new TextIndex();
		this.message.setText(message);
	}
	@Override
	public String getIdentifierName() {
		return null;
	}
	@Override
	public EntityObject setIdentifierValue(Integer id) {
		return this;
	}
}
