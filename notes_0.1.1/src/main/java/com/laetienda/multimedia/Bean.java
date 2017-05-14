package com.laetienda.multimedia;

import java.util.List;

import com.laetienda.db.Db;
import com.laetienda.entities.Video;

public class Bean {
	
	private Db db;
	
	public Bean(Db db){
		this.db = db;
	}
	
	public List<Video> getVideosList(){
				
		return 	db.getEm().createNamedQuery("Video.findAll", Video.class).getResultList();
	}

}
