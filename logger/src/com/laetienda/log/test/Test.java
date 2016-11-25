package com.laetienda.log.test;

import com.laetienda.db.Connection;
import com.laetienda.db.Transaction;
import javax.persistence.EntityManager;

public class Test {

	public static void main(String[] args) {
		
		try{
			Connection database = new Connection("logger");
			Transaction db = new Transaction(database.getEm());
			EntityManager em = database.getEm();
			db.begin(em);
			db.save(entity);
			database.closeEm(em);
			database.closeEm(db.getEm());
			database.close();
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
