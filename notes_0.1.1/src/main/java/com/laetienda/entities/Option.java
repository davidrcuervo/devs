package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="options")
@NamedQueries({
	@NamedQuery(name="Option.findall", query="SELECT o FROM Option o")
})

public class Option extends EntityObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(Option.class);
	
	@Id
	@SequenceGenerator(name = "option_id_seq", sequenceName = "option_id_seq", allocationSize=1)
	@GeneratedValue(generator = "option_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="object_id", unique=true, nullable=false, updatable=false, insertable=true)
	private Objeto objeto;
	
	@ManyToOne
	private Variable variable;
	
}
