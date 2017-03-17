package com.laetienda.db.entities;

import java.util.Calendar;

import javax.persistence.*;

@Entity
@Table(name="app")
@NamedQueries({
	@NamedQuery(name="App.findAll", query="SELECT a FROM App a"),
})
public class App {
	
	@Id
	@SequenceGenerator(name = "app_id_seq", sequenceName = "app_id_seq", allocationSize=1)
	@GeneratedValue(generator = "app_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@Column(name="\"description\"", nullable=true, unique=false, length=254)
	private String description;
}
