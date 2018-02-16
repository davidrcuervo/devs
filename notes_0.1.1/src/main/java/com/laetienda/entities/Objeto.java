package com.laetienda.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="objectos")
@NamedQueries({
	@NamedQuery(name="Objeto.findall", query="SELECT o FROM Objecto o")
})

public class Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(Objeto.class);
	
	@Id
	@SequenceGenerator(name = "objecto_id_seq", sequenceName = "objecto_id_seq", allocationSize=1)
	@GeneratedValue(generator = "objecto_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;

	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="\"user_id\"", nullable=false, unique=false)
	private User owner;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"group_id\"", nullable=false, unique=false)
	private Group group;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"read_acl_id\"", nullable=false, unique=false)
	private AccessList write;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"write_acl_id\"", nullable=false, unique=false)
	private AccessList read;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"delete_acl_id\"", nullable=false, unique=false)
	private AccessList delete;
	
	
	
}
