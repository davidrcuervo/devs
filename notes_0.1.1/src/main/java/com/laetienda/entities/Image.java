package com.laetienda.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.laetienda.db.entities.EntityObject;

@Entity
@Table(name="multimedia_images")
@NamedQueries({
	@NamedQuery(name="Image.findAll", query="SELECT i FROM Image i"),
})

public class Image extends EntityObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "multimediaImage_id_seq", sequenceName = "multimediaImage_id_seq", allocationSize=1)
	@GeneratedValue(generator = "multimediaImage_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
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
}
