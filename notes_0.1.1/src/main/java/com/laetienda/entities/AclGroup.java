package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="groups_lists")
@NamedQueries({
	@NamedQuery(name="AclGroup.findall", query="SELECT a FROM AclGroup a")
})

public class AclGroup implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(AclUser.class);
	
	@Id
	@SequenceGenerator(name = "acl_group_id_seq", sequenceName = "acl_group_id_seq", allocationSize=1)
	@GeneratedValue(generator = "acl_group_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@ManyToOne
	private AccessList acl;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"group_id\"", unique=false, nullable=false)
	private Group group;
}
