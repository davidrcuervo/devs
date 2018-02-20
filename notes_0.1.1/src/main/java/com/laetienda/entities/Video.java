package com.laetienda.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.laetienda.db.Db;
import com.laetienda.db.DbException;

@Entity
@Table(name="multimedia_videos")
@NamedQueries({
	@NamedQuery(name="Video.findAll", query="SELECT v FROM Video v"),
	@NamedQuery(name="Video.findByUrl", query = "SELECT v FROM Video v WHERE v.url = :url"),
})

public class Video extends EntityObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "multimediaVideo_id_seq", sequenceName = "multimediaVideo_id_seq", allocationSize=1)
	@GeneratedValue(generator = "multimediaVideo_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"created\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@Column(name="\"name\"", nullable=true, unique=false, length=254)
	private String name;
	
	@Column(name="\"url\"", nullable=false, unique=true, length=254)
	private String url;
	
	@Column(name="\"description\"", nullable=true, unique=false, length=254)
	private String description;
	
	@Column(name="\"mp4\"", nullable=false, unique=true, length=254)
	private String mp4;
	
	@Column(name="\"webm\"", nullable=false, unique=true, length=254)
	private String webm;
	
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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		try{
			if(name.isEmpty()){
				this.name = getUrl();
			}else{
				this.name = name;
			}
		}catch(NullPointerException ex){
			this.name = getUrl();
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the mp4
	 */
	public String getMp4() {
		return mp4;
	}

	/**
	 * @param mp4 the mp4 to set
	 */
	public void setMp4(String mp4) {
		try{
			this.mp4 = mp4;
		}catch(NullPointerException ex){
			
		}
	}

	/**
	 * @return the webm
	 */
	public String getWebm() {
		return webm;
	}

	/**
	 * @param webm the webm to set
	 */
	public void setWebm(String webm) {
		try{
			this.webm = webm;
		}catch(NullPointerException ex){
			
		}
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the created
	 */
	public Calendar getCreated() {
		return created;
	}

	/**
	 * @return the modified
	 */
	public Calendar getModified() {
		return modified;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 * @throws DbException 
	 */
	public void setUrl(String url, Db db) {
		
		int counter = 0;
		List<Video> query;
		String temp = url;
		
		try{
			if(url != null && !url.isEmpty()){		
				do{
					this.url = URLEncoder.encode(temp, "UTF-8");
					query = db.getEm().createNamedQuery("Video.findByUrl", Video.class).setParameter("url", this.url).getResultList();
					counter++;
					temp = url + "_" + Integer.toString(counter);
				}while(query.size() > 0);
			}else{
				addError("video", "Make sure that you have selected a valid video file");
			}
			
		}catch(UnsupportedEncodingException ex){
			addError("video", "Internal error while updating URL in the database");
		}
		
	}

	public String getIdentifierName() {
		return null;
	}

	public EntityObject setIdentifierValue(Integer id) {
		return this;
	}
}
