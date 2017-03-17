package com.laetienda.lang.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.laetienda.db.Transaction;
import com.laetienda.db.entities.EntityObject;

import javax.persistence.*;

@Entity
@Table(name="lang_languages")
@NamedQueries({
	@NamedQuery(name="Language.findAll", query="SELECT l FROM Language l ORDER BY l.id"),
	@NamedQuery(name="Language.findAllCount", query = "SELECT COUNT(l) FROM Language l"),
	@NamedQuery(name="Language.findByIdentifier", query="SELECT l FROM Language l WHERE l.identifier = :identifier"),
})

public class Language extends EntityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "langLanguages_id_seq", sequenceName = "langLanguages_id_seq", allocationSize=1)
	@GeneratedValue(generator = "langLanguages_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@Column(name="\"identifier\"", nullable= false, unique = true, length=254)
	private String identifier;
	
	@Column(name="\"english\"", nullable=true, unique=false, length=254)
	private String english;

	@Column(name="\"french\"", nullable=true, unique=false, length=254)
	private String french;
	
	@Column(name="\"spanish\"", nullable=true, unique=false, length=254)
	private String spanish;
	
	public Language(){
		super();
	}
	
	@PreUpdate
	@PrePersist
	public void updateTimeStamps(){
		Date date = new Date();
		modified = Calendar.getInstance();
		modified.setTime(date);
		
		if(created == null){
			created = Calendar.getInstance();
			created.setTime(date);
		}
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the created
	 */
	public Calendar getCreated() {
		return created;
	}

	/**
	 * @return the modified
	 */
	public Calendar getModified() {
		return modified;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public Language setIdentifier(String identifier, Transaction db) {
		
		this.identifier = identifier;
		
		if(identifier == null || identifier.isEmpty()){
			super.addError("identifier", "language entiry error : identifier is empty");
		}else{
			if(identifier.length() > 254)
				super.addError("identifier", "language entity error: identifier max lenghth is 254 characters");
			
			List<Language> temp = db.getEm().createNamedQuery("Language.findByIdentifier", Language.class).setParameter("identifier", identifier).getResultList();
			System.out.println("id: " + id);
			if(temp.size() > 0 && temp.get(0).getId() != id)
				super.addError("identifier", "language entity error: identifier already exists");
		}
		
		return this;
	}

	/**
	 * @return the English
	 */
	public String getEnglish() {
		return english;
	}

	/**
	 * @param english the English to set
	 */
	public Language setEnglish(String english) {
		this.english = english;
		
		if(english != null){
			
			if(english.length() > 254)
				super.addError("english", "language entity error: English max lenght is 254 characters");
		}
		
		return this;
	}

	/**
	 * @return the french
	 */
	public String getFrench() {
		return french;
	}

	/**
	 * @param french the french to set
	 */
	public Language setFrench(String french) {
		this.french = french;
		
		if(english != null){
			
			if(english.length() > 254)
				super.addError("french", "language entity error: French max lenght is 254 characters");
		}
		
		return this;
	}

	/**
	 * @return the Spanish
	 */
	public String getSpanish() {
		return spanish;
	}

	/**
	 * @param spanish the Spanish to set
	 */
	public Language setSpanish(String spanish) {
		this.spanish = spanish;
		
		if(english != null){
			
			if(english.length() > 254)
				super.addError("spanish", "language entity error: English max lenght is 254 characters");
		}
		
		return this;
	}
}
