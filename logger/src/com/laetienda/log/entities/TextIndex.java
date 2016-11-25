package com.laetienda.log.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.*;

/**
 * 
 * @author myself
 *
 */

@Entity
@Table(name="indexes@logger;cdflsl=/eiqy-")
@NamedQuery(name="TextIndex.findAll", query="SELECT i FROM TextIndex i")
public class TextIndex implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "logger_indexes_id_seq", sequenceName = "logger_indexes_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logger_indexes_id_seq")
	@Column(name="id", updatable=false, unique=true, nullable=false)
	private Integer id; 
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified;
	
	@OneToMany(mappedBy="index")
	private List<Text> texts;
		
	public TextIndex() {
		super();
	} 
	   
	public Integer getId() {
 		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	   
	public Calendar getCreated() {
 		return this.created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}
	   
	public Calendar getModified() {
 		return this.modified;
	}

	public void setModified(Calendar Modified) {
		this.modified = Modified;
	}
	
	public void setText(String text){
		texts = new ArrayList<Text>();
		List<String> lines = formatTextInList(text);
		
		for(String line : lines){
			Text temp = new Text();
			temp.setText(line);
			temp.setIndex(this);
			texts.add(temp);
		}
	}
	
	public String getText(){
		
		String result = new String("");
		
		if(texts != null && texts.size() > 0){
			for(Text temp : getTexts()){
				result += temp.getText(); 
			}
		}
		
		return result;
	}
	
	private List<String> formatTextInList(String text){
		
		int size = 254;
		List<String> textInList = new ArrayList<String>();
		
		for (int start = 0; start < text.length(); start += size) {
			textInList.add(text.substring(start, Math.min(text.length(), start + size)));
	    }
		
		return textInList;
	}

	/**
	 * @return the texts
	 */
	public List<Text> getTexts() {
		return texts;
	}

	/**
	 * @param texts the texts to set
	 */
	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}
}
