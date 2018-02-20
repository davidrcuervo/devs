package com.laetienda.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="variables")
@NamedQueries({
	@NamedQuery(name="Variable.findall", query="SELECT v FROM Variable v")
})

public class Variable extends EntityObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(Variable.class);
	
	@Id
	@SequenceGenerator(name = "variable_id_seq", sequenceName = "variable_id_seq", allocationSize=1)
	@GeneratedValue(generator = "variable_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="object_id", unique=true, nullable=false, updatable=false, insertable=true)
	private Objeto objeto;
	
	@Column(name="\"name\"", unique=true, nullable=false, length=254)
	private String name;
	
	@Column(name="\"description\"", unique=true, nullable=false, length=254)
	private String description;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="variable", orphanRemoval=true)
	private List<Option> options = new ArrayList<Option>();
}
