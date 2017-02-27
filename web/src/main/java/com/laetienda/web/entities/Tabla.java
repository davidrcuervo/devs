package com.laetienda.web.entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

@Entity
@Table(name="tabla")
public class Tabla implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "tabla_id_seq", sequenceName = "tabla_id_seq", allocationSize=1)
	@GeneratedValue(generator = "tabla_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"description\"", nullable=false, unique=true, length=254)
	private String description;
	
	@Column(name="\"name\"", nullable=false, unique=true, length=254)
	private String name;

	public Tabla() {
		super();
	}   
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
