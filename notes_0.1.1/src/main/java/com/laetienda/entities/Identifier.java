package com.laetienda.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="identifiers")
@NamedQueries({
	@NamedQuery(name="Identifier.findAll", query="SELECT i FROM Identifier i"),
	@NamedQuery(name="Identifier.findByName", query="SELECT i FROM Identifier i WHERE i.name = :name")
})

@Deprecated
public class Identifier extends EntityObject implements Serializable{
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "identifiers_id_seq", sequenceName = "identifiers_id_seq", allocationSize=1)
	@GeneratedValue(generator = "identifiers_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@Column(name="\"name\"", nullable=false, unique=true, length=254)
	private String name;
	
	@Column(name="\"value\"", nullable= false, unique = false)
	private Integer value;
	
	@Column(name="\"description\"", nullable=true, unique=false, length=254)
	private String description;
	
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
	
	@Override
	public String getIdentifierName(){
		return null;
	}

	public Integer getIdentifier() {
		return value;
	}

	public void setIdentifier(Integer identifier) {
		this.value = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public Calendar getCreated() {
		return created;
	}

	public Calendar getModified() {
		return modified;
	}

	@Override
	public EntityObject setIdentifierValue(Integer id) {
		// TODO Auto-generated method stub
		return this;
	}
}