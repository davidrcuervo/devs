package multimedia;

import java.util.List;

import com.laetienda.db.Transaction;

public class Bean {
	
	Transaction db;
	
	public Bean(Transaction db){
		this.db = db;
	}
	
	public List<com.laetienda.multimedia.entities.Video> getVideosList(){
				
		return 	db.getEm().createNamedQuery("Video.findAll", com.laetienda.multimedia.entities.Video.class).getResultList();
	}

}
