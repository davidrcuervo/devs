package com.laetienda.log.entities;

import com.laetienda.log.entities.TextIndex;
import java.io.Serializable;
import java.lang.String;
import java.util.Calendar;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Text
 *
 */

@Entity
@Table(name="texts@logger")
@NamedQuery(name="Text.findAll", query="SELECT t FROM Text t")
public class Text implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "logger_texts_id_seq", sequenceName = "logger_texts_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logger_texts_id_seq")
	@Column(name="id", updatable=false, unique=true, nullable=false)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created; 
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modified; 
	
	private String Text;
	
	//bi-directional many-to-one association to TextIndex
	@ManyToOne
	@JoinColumn(name="\"index\"", nullable=false)
	private TextIndex index;
		
	public Text() {
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

	public void setModified(Calendar modified) {
		this.modified = modified;
	}
	   
	public String getText() {
 		return this.Text;
	}

	public void setText(String Text) {
		this.Text = Text;
	}
	   
	public TextIndex getIdentifier() {
 		return this.index;
	}

	public void setIdentifier(TextIndex index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public TextIndex getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(TextIndex index) {
		this.index = index;
	}
}
