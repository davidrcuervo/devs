package com.laetienda.lang.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

@Entity
@Table(name="lang_languages")
@NamedQueries({
	@NamedQuery(name="Language.findAll", query="SELECT l FROM Language l"),
	@NamedQuery(name="Language.findByIdentifier", query="SELECT l FROM Language l WHERE l.identifier = :identifier"),
})

public class Language implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "langLanguages_id_seq", sequenceName = "langLanguages_id_seq", allocationSize=1)
	@GeneratedValue(generator = "langLanguages_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@Column(name="\"identifier\"", unique = true, length=254)
	private String identifier;
	
	@Column(name="\"english\"", nullable=false, unique=false, length=254)
	private String english;

	@Column(name="\"french\"", nullable=false, unique=false, length=254)
	private String french;
	
	@Column(name="\"spanish\"", nullable=false, unique=false, length=254)
	private String spanish;

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
	 * @param created the created to set
	 */
	public void setCreated(Calendar created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Calendar getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Calendar modified) {
		this.modified = modified;
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
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the english
	 */
	public String getEnglish() {
		return english;
	}

	/**
	 * @param english the english to set
	 */
	public void setEnglish(String english) {
		this.english = english;
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
	public void setFrench(String french) {
		this.french = french;
	}

	/**
	 * @return the spanish
	 */
	public String getSpanish() {
		return spanish;
	}

	/**
	 * @param spanish the spanish to set
	 */
	public void setSpanish(String spanish) {
		this.spanish = spanish;
	}
	
	
}
