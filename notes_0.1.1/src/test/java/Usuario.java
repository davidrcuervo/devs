import com.laetienda.entities.User;
import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.app.AppException;
import com.laetienda.app.GeneralException;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.dap.Dap;
//import com.laetienda.dap.Ldif;

import java.io.File;

//import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
//import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Usuario {
	private final static Logger log = LogManager.getLogger(Usuario.class);
	
	User user;
//	User tomcat;
	DbManager dbManager;
	DapManager dapManager;
//	Dn baseDn;
	
	public Usuario(User user, DbManager dbManager, DapManager dapManager) throws AppException {
		
//		try {
			setUser(user);
			setDbManager(dbManager);
			setDapManager(dapManager);
//			this.tomcat = dapManager.getTomcat();
//			this.baseDn = new Dn(Ldif.getDomain());
//		} catch (LdapInvalidDnException e) {
//			log.error("Failed to create Usuario object. $exception: " + e.getMessage());
//			throw new DapException(e);
//		}
	}
	
	public Usuario setUser(User user) {
		this.user = user;
		return this;
	}
	
	public Usuario setDbManager(DbManager dbManager) {
		this.dbManager = dbManager;
		return this;
	}

	public Usuario setDapManager(DapManager dapManager) {
		this.dapManager = dapManager;
		return this;
	}
	
	public Usuario save() throws AppException {
		if(user.getErrors().size() > 0) {
			throw new GeneralException("User has errors and can't be persisted");
		}
		
		Db db = null;
		Dap dap = null;
		
		try {
			db = dbManager.createTransaction();
			db.insert(user);
			dap = dapManager.createDap();
			dap.insertUser(user);
		}catch (DbException e) {
			log.error("Failed to persist user. $excpetion: " + e.getMessage());
			throw e;
		}catch(DapException e) {
			user.addError("user", "Internal error. Failed to add user");
			
			try {
				db.remove(user);
			}catch(DbException ex) {
				log.fatal("Failed to remove user from DB that was not able to be saved in ldap directory. $exception: " + e.getMessage());
				throw ex;
			}
		}finally {
			dbManager.closeTransaction(db);
			dapManager.closeConnection(dap);
		}
		return this;
	}
	
	public Usuario update() {
		
		return this;
	}
	
	public Usuario delete() {
		
		return this;
	}
	
	public static void main(String[] args) {
		
		
		File directory = new File("C:\\Users\\i849921\\git\\devs\\web\\target\\classes");
		DapManager dapManager = null;
		DbManager dbManager = null;
		Usuario usuario;
		System.out.println("here1");
		
		User testUser = new User();
		try {
			System.out.println("here2");
			
			dapManager = new DapManager(new File(directory.getAbsolutePath()));
			System.out.println("here2");
			dbManager = new DbManager(new File(directory.getAbsolutePath())).open();
			System.out.println("here3");
			usuario = new Usuario(testUser, dbManager, dapManager);
			System.out.println("here4");
			usuario.save();
			System.out.println("User has been saved succesfully");
		} catch (AppException e) {
			System.err.println("error1");
			e.getParent().printStackTrace();			
		}finally {
			dbManager.close();
		}
	}
}
