package com.laetienda.db;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class Transaction {
	
	private EntityManager em;
	
	public Transaction(EntityManager em){
		
		if(em != null && em.isOpen() && !em.getTransaction().isActive()){
			this.em = em;
		}
	}
	
	public EntityManager getEm(){
		return this.em;
	}
	
	public boolean begin(EntityManager em) throws Exception {
		
		boolean result = false;
		
		try{
			em.clear();
			em.getTransaction().begin();
			result = true;
			
		}catch(IllegalStateException ex){
			em.clear();
			throw ex;
		}finally{
			
		}
		
		return result;
	}
	
	public boolean save(EntityManager em, Object entity) throws Exception {
		boolean result = false;
		
		try{
			em.persist(entity);
			em.getTransaction().commit();
			result = true;
		}catch(IllegalStateException ex){
			throw ex;
		}catch(RollbackException ex){
			
			try{
				em.getTransaction().rollback();
			}catch(IllegalStateException e){
				throw e;
			}finally{
				
			}
			throw ex;
			
		}finally{
			em.clear();
		}
		
		return result;
	}
}
