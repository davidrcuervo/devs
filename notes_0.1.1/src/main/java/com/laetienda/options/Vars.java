package com.laetienda.options;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.laetienda.db.Db;
import com.laetienda.entities.Option;
import com.laetienda.entities.Variable;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Vars {
	private final static Logger log4j = LogManager.getLogger(Vars.class);
	
	private Db db;
	
	public Vars(Db db) {
		this.db = db;
	}
	
	public List<Option> options(String var){
		List<Option> result = new ArrayList<Option>();
		
		try {
			Variable variable = db.getEm().createNamedQuery("Variable.findByName", Variable.class).setParameter("name", var).getSingleResult();
			result = variable.getOptions();

		}catch(NoResultException | NonUniqueResultException ex) {
			log4j.error("Failed to get option from requested variable. $var: " + var, ex);
		}
			
		return result;
	}
}
