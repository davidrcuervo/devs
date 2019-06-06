package com.laetienda.db;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.entities.*;



public class Db {
	private final static Logger log = LogManager.getLogger(Db.class);
	
	private EntityManager em;
	
	public Db(EntityManager em){
		
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
	/**
	 * 
	 * @return boolean it returns <i>true</i> if it is able to update and commit into the database otherwise it returns <i>false</i>
	 * @throws DbException
	 */
	public boolean update() throws DbException {
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
			//em.clear();
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
/*	
	public void insert(EntityObject entity) throws DbException {

		insertNoCommit(entity);
		commit();
	}
	
	public void insertNoCommit(EntityObject entity) throws DbException{
		em.clear();
		
		try{
			
			if(entity.getErrors().size() > 0){
				//Better not to do anything if it has errors
			}else{
				
				em.getTransaction().begin();
				
				if(entity.getIdentifierName() != null){
					Identifier identifier = em.createNamedQuery("Identifier.findByName", Identifier.class).setParameter("name", entity.getIdentifierName()).getSingleResult();
					Integer id = identifier.getValue();
					id += 1;
					identifier.setValue(id);
					entity.setIdentifierValue(id);
				}
				
				em.persist(entity);
				
			}
			
		}catch(IllegalStateException  ex){
			throw new DbException("Failed to start db transaction", ex);
		}catch(IllegalArgumentException ex){
			throw new DbException("Failed to se arguments to query idenfier table object", ex);
		}catch(NoResultException ex){
			throw new DbException("No identifier found for object table", ex);
		}catch(EntityExistsException ex){
			throw new DbException("Object entity already exists", ex);
		}catch(TransactionRequiredException ex){
			throw new DbException("There is no active transaction", ex);
		}
	}
	
	public void commit() throws DbException{
		try{
			em.getTransaction().commit();
		}catch(IllegalStateException ex){
			throw new DbException("Failed to find db transaction", ex);
		}catch(RollbackException ex){
			rollback();
			throw new DbException("Failed to commit transaction", ex);
		}finally{
			em.clear();
		}
	}
*/	
	/**
	 * It inserts and instance of EntityObject into the database
	 * @param entity Object should be instance of EntityObject
	 * @throws DbException if it is not possible to commit into database
	 */
	public void insert(/*EntityManager em, EntityObject entity,*/ Objeto objeto) throws DbException {
		
		try{
			if(objeto.getErrors().size() > 0){
				
			}else{
				//em.clear();
				em.getTransaction().begin();
				em.persist(objeto);	
				//em.persist(entity);
				em.getTransaction().commit();
			}
			
		}catch(Exception ex){
			log.error("Failed to persist object in database. $exception: " + ex.getMessage());
			rollback();
			throw new DbException(ex.getMessage(), ex);
//		}catch(EntityExistsException ex){
//			em.clear();
//			throw new DbException(ex.getMessage(), ex);
//		}catch(IllegalArgumentException ex){
//			em.clear();
//			throw new DbException(ex.getMessage(), ex);
//		}catch(TransactionRequiredException ex){
//			em.clear();
//			throw new DbException(ex.getMessage(), ex);
//		}catch(RollbackException ex){
//			rollback();
//			throw new DbException(ex.getMessage(), ex);
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
			em.getTransaction().rollback();
			//rollback();
		}finally{
			
		}
		
		return result;
	}
	
	private void rollback() throws DbException{
		try{
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			
		}catch(IllegalStateException ex){
			throw new DbException("Invalid em state to rollback", ex);
		}catch(PersistenceException ex){
			throw new DbException("Unexpected error while rolling back transaction", ex);
		}finally{
			em.clear();
		}
	}
	/**
	 * 
	 * @param variable String
	 * @param Option String
	 * @return Option object com.laetienda.entities.Option
	 * @throws DbException
	 */
	public Option findOption(String variable, String  Option) throws DbException {
		Option result = null;
		try {
			Variable status = em.createNamedQuery("Variable.findByName", Variable.class).setParameter("name", variable).getSingleResult();
			result = em.createNamedQuery("Option.findByName", Option.class).setParameter("variable", status).setParameter("name", Option).getSingleResult();
		}catch(NoResultException | NonUniqueResultException ex) {
			throw new DbException("Failed to find option", ex);
		}
		
		return result;
	}
	
	/*
	public synchronized Integer getNextUid() throws DbException {
		Integer result = null;
		
		Option value = findOption("user uid", "uid");
		
		try {
			result = Integer.parseInt(value.getDescription());
			value.setDescription(Integer.toString(result + 1));
			update();
		}catch(NumberFormatException ex) {
			throw new DbException("The uid is not a valid value", ex);
		}
		
		return result;
	}
	*/
	
	@Deprecated
	public void insert(/*EntityManager em,*/ EntityObject entity) throws DbException {
		
		try{
			if(entity.getErrors().size() > 0){
				
			}else{
				//em.clear();
				em.getTransaction().begin();
				em.persist(entity);	
				//em.persist(entity);
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
			//em.clear();
		}
	}
}
