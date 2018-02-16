package com.laetienda.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="grupos")
@NamedQueries({
	@NamedQuery(name="Group.findall", query="SELECT g FROM Group g")
})
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(Group.class);
	
	@Id
	@SequenceGenerator(name = "group_id_seq", sequenceName = "group_id_seq", allocationSize=1)
	@GeneratedValue(generator = "group_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="object_id", unique=true, nullable=false, updatable=false, insertable=true)
	private Objeto objeto;
	
	@Column(name="\"name\"", nullable=false, unique=true, length=254)
	private String name;
	
	@Column(name="\"description\"", nullable=true, unique=false, length=254)
	private String description;
}
