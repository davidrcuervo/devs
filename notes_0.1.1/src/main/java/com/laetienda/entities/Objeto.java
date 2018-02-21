package com.laetienda.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="objetos")
@NamedQueries({
	@NamedQuery(name="Objeto.findall", query="SELECT o FROM Objeto o")
})

public class Objeto extends EntityObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(Objeto.class);
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	@JoinColumn(name="\"user_id\"", nullable=true, unique=false)
	private User owner;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"group_id\"", nullable=true, unique=false)
	private Group group;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"read_acl_id\"", nullable=true, unique=false)
	private AccessList write;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"write_acl_id\"", nullable=true, unique=false)
	private AccessList read;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"delete_acl_id\"", nullable=true, unique=false)
	private AccessList delete;
	
	public Objeto() {
		 
	}
	
	public Calendar getModified() {
		return modified;
	}
	
	public Objeto setModified() {
		setModified(Calendar.getInstance());
		return this;
	}

	public void setModified(Calendar modified) {
		this.modified = modified;
	}

	public Calendar getCreated() {
		return created;
	}

	public User getOwner() {
		return owner;
	}
	
	public Objeto setCreated() {
		setCreated(Calendar.getInstance());
		return this;
	}
	
	public void setCreated(Calendar created) {
		this.created = created;
	}

	public Objeto setOwner(User owner, Group group) {
		setOwner(owner);
		setGroup(group);
		return this;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public AccessList getWrite() {
		return write;
	}

	public void setWrite(AccessList write) {
		this.write = write;
	}

	public AccessList getRead() {
		return read;
	}

	public void setRead(AccessList read) {
		this.read = read;
	}

	public AccessList getDelete() {
		return delete;
	}

	public void setDelete(AccessList delete) {
		this.delete = delete;
	}

	public Integer getId() {
		return id;
	}

	public Objeto setPermisions(AccessList delete, AccessList write, AccessList read){
		this.setDelete(delete);
		this.setWrite(write);
		this.setRead(read);
		return this;
	}
	
	public Objeto getObjeto() {
		return this;
	}
}
