package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="user_lists")
@NamedQueries({
	@NamedQuery(name="AclUser.findall", query="SELECT a FROM AclUser a")
})

public class AclUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(AclUser.class);
	
	@Id
	@SequenceGenerator(name = "acl_user_id_seq", sequenceName = "acl_user_id_seq", allocationSize=1)
	@GeneratedValue(generator = "acl_user_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"user_id\"", unique=false, nullable=false)
	private User user;
	
	@ManyToOne
	private AccessList acl;
	
}
