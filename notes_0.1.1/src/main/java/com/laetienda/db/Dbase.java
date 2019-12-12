package com.laetienda.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.entities.Objeto;

public class Dbase {
	
	private final static Logger log = LogManager.getLogger(Dbase.class);
	
	public Dbase() {
		
	}
	
	public void insert(EntityManager em, Objeto o) throws DbException {
		
		try {
			if(!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			
			em.persist(o);
			
		}catch(PersistenceException | IllegalArgumentException e) {
			throw new DbException(e);
		}
	}
	
	public void commit(EntityManager em) throws DbException {
		try{
			em.getTransaction().commit();
		}catch(IllegalStateException e) {
			throw new DbException(e);
		}catch(RollbackException e) {
			log.error("Failed to commit database. $error: {}", e.getMessage());
			rollback(em);
			throw new DbException(e);
		}
	}
	
	public void rollback(EntityManager em) throws DbException {
		try{
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			
		}catch(Exception ex){
			throw new DbException(ex.getMessage(), ex);
		}
	}
}
