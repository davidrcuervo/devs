package com.laetienda.db.test;

import com.laetienda.db.*;
import com.laetienda.db.entities.Tabla;

public class Test {

	public static void main(String[] args) {
		
		try{
			Connection database = new Connection("db");
			Transaction db = new Transaction(database.getEm());
			db.begin();
			Tabla table = new Tabla();
			table.setName("row3");
			table.setDescription("column2");
			db.save(table);
			database.closeEm(db.getEm());
			database.close();
		
		}catch (Exception ex){
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}finally{
			System.out.println("Thank you.!!\nGood Bye");
		}
	}
}
