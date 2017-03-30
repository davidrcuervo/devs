package com.laetienda.db;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import com.laetienda.db.entities.*;
import com.laetienda.db.exceptions.*;

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
	
	public EntityObject find(Class<?> entityClass, String strId) throws DbException{
		int id = -1;
		
		try{
			id = Integer.parseInt(strId);
			
		}catch(NumberFormatException ex){
			//throw new DbException("The string ID is not a parsable integer. $Exception: " + ex.getMessage(), ex);
		}
		
		return find(entityClass, id);
	}
	
	public EntityObject find(Class<?> entityClass, Integer id) throws DbException{
		EntityObject result;
		
		try{
			result = (EntityObject)em.find(entityClass, id);
		}catch (IllegalArgumentException ex){
			throw new DbException("The class does not denote an entity type or the id is is not a valid type for that entity's primary key or is null" ,ex);
			
		}catch (ClassCastException ex){
			throw new DbException("The code has attempted to cast an object to a subclass of which it is not an instance", ex);
		}
		
		return result;
	}
	
	public boolean begin(EntityManager em) throws DbException {
		
		boolean result = false;
		
		try{
			em.getTransaction().begin();
			result = true;
			
		}catch(IllegalStateException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}
		
		return result;
	}
	
	public boolean update(EntityManager em) throws DbException {
		boolean result = false;
		
		try{
			em.getTransaction().begin();
			em.getTransaction().commit();
			result = true;
		}catch(IllegalStateException ex){
			throw new DbException(ex.getMessage(), ex);
		}catch(RollbackException ex){
			rollback();
		}finally{
			em.clear();
		}
		
		return result;
	}
	
	public void insert(EntityObject entity) throws DbException {
		insert(getEm(), entity);
	}
	
	public void insert(EntityManager em, EntityObject entity) throws DbException {
				
		try{
			if(entity.getErrors().size() > 0){
				
			}else{
				em.clear();
				em.getTransaction().begin();
				em.persist(entity);
				em.getTransaction().commit();
			}
			
		}catch(IllegalStateException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(EntityExistsException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(IllegalArgumentException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(TransactionRequiredException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(RollbackException ex){
			rollback();
			throw new DbException(ex.getMessage(), ex);
		}finally{
			em.clear();
		}
	}
	
	public boolean remove(EntityObject entity) throws DbException{
		boolean result = false;
		
		try{
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
			result = true;
		}catch(IllegalArgumentException ex){
			em.clear();
			throw new DbException("The entity to remove is not an entity or is a detached entity", ex);
		}catch(IllegalStateException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(TransactionRequiredException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(RollbackException ex){
			rollback();
		}finally{
			em.clear();
		}
		
		return result;
	}
	
	private void rollback() throws DbException{
		try{
			em.getTransaction().rollback();
			
		}catch(IllegalStateException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}catch(PersistenceException ex){
			em.clear();
			throw new DbException(ex.getMessage(), ex);
		}finally{
			em.clear();
		}
	}
}
