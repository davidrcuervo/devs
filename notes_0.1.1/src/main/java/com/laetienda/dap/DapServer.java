package com.laetienda.dap;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.directory.api.ldap.schema.extractor.SchemaLdifExtractor;
import org.apache.directory.api.ldap.schema.extractor.impl.DefaultSchemaLdifExtractor;
import org.apache.directory.api.ldap.schema.loader.LdifSchemaLoader;
import org.apache.directory.api.ldap.schema.manager.impl.DefaultSchemaManager;
import org.apache.directory.api.util.exception.Exceptions;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.api.ldap.model.schema.registries.SchemaLoader;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.api.CacheService;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.DnFactory;
import org.apache.directory.server.core.api.InstanceLayout;
import org.apache.directory.server.core.api.schema.SchemaPartition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.core.partition.ldif.LdifPartition;
import org.apache.directory.server.core.shared.DefaultDnFactory;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;

import org.apache.directory.server.i18n.I18n;


/**
 * 
 * From example on the web: <a href="https://issues.apache.org/jira/secure/attachment/12626473/EmbeddedADSVerTrunkV2.java">link</a>
 * 
 * @author myself
 * 
 */
public class DapServer {
	
	private DirectoryService service;
	private LdapServer server;

	protected DapServer(Properties settings) throws DapException{
		
		File workdir = new File(settings.getProperty("dapFolderPath"));
		
		if(!workdir.exists()){
			workdir.mkdirs();
		}
		
		initDirectoryService(workdir);
	}
	
	protected void startServer() throws DapException{
		
		try{
			service.startup();
			server = new LdapServer();
			int serverPort = 10389;
			server.setTransports(new TcpTransport(serverPort));
			server.setDirectoryService(service);
			
			try{
				server.start();
				System.out.println("Ldap Server is running");
			}catch(Exception ex){
				throw new DapException("Failed to start Ldap Server", ex);
			}
		}catch(Exception ex){
			throw new DapException("Failed to start Directory Service", ex);
		}
	}
	
	protected void stopServer(){
		System.out.println("Stopping DAP server");
		
		if(server != null && server.isStarted()){
			server.stop();
		}
		
		try{
			service.shutdown();
		}catch(Exception ex){
			System.err.println("Directory Service failed to shutdown");
		}
	}
	
	private void initSchemaPartition() throws DapException{
		
		try{
			InstanceLayout instanceLayout = service.getInstanceLayout();
			File schemaPartitionDirectory = new File(instanceLayout.getPartitionsDirectory(), "schema");
			
			if(schemaPartitionDirectory.exists()){
				
			}else{
				SchemaLdifExtractor extractor = new DefaultSchemaLdifExtractor(instanceLayout.getPartitionsDirectory());
				extractor.extractOrCopy();
			}
			
			SchemaLoader loader = new LdifSchemaLoader(schemaPartitionDirectory);
			SchemaManager schemaManager = new DefaultSchemaManager(loader);
			schemaManager.loadAllEnabled();
			
			List<Throwable> errors = schemaManager.getErrors();
			
			if(errors.size() != 0 ){
				throw new DapException(I18n.err(I18n.ERR_317, Exceptions.printErrors(errors)));
			}
			
			service.setSchemaManager(schemaManager);
			
			DnFactory dnFactory = new DefaultDnFactory(service.getSchemaManager(), service.getCacheService().getCache("schemaLdifPartitionCache"));
			service.setDnFactory(dnFactory);
			
			LdifPartition schemaLdifPartition = new LdifPartition(schemaManager, dnFactory);
			schemaLdifPartition.setPartitionPath(schemaPartitionDirectory.toURI());
			
			SchemaPartition schemaPartition = new SchemaPartition(schemaManager);
			schemaPartition.setWrappedPartition(schemaLdifPartition);
			service.setSchemaPartition(schemaPartition);
			
		}catch(IOException ex){
			//ex.printStackTrace();
			throw new DapException("DAP server failed to initiate Schema Partition", ex);
		}catch(LdapException ex){
			throw new DapException("DAP server failed to initiate Schema Partition", ex);
		}
	}
	
	protected JdbmPartition initAppPartition(Properties settings) throws DapException{
		SchemaManager schemaManager = service.getSchemaManager();
		String app_name = settings.getProperty("app_name");
		
		DnFactory dnFactory = new DefaultDnFactory(service.getSchemaManager(), service.getCacheService().getCache("appJdbmPartitionCache"));
		
		JdbmPartition appPartition = new JdbmPartition(schemaManager, dnFactory);
		appPartition.setId(app_name);
		appPartition.setCacheSize(1000);
		
		File partition = new File(service.getInstanceLayout().getPartitionsDirectory(), appPartition.getId());
		appPartition.setPartitionPath(partition.toURI());
		
		try{
			
			//appPartition.setSuffixDn( Ldif.CONTEXT_DN() );
			//appPartition.init(service);
			appPartition.setSchemaManager(schemaManager);
			appPartition.initialize();
			service.addPartition(appPartition);
			
			//appPartition.setContextEntry(Ldif.CONTEXT_ENTRY());
			
			
		}catch(LdapInvalidDnException ex){
			throw new DapException("Failed to add App partition to DAP server", ex);
		}catch(LdapException ex){
			throw new DapException("Failed to add App partition to DAP server", ex);
		}catch(Exception ex){
			throw new DapException("Failed to add App partition to Directory Service", ex);
		}
		
		return appPartition;
	}
	
	protected void buildAppPartition(Properties settings, JdbmPartition appPartition) throws DapException{
		//SchemaManager schemaManager = service.getSchemaManager();
		
		try{
			/*
			Dn entryDn = new Dn(schemaManager, "ou=people", dn);
			Entry entry = new DefaultEntry(schemaManager, entryDn)
					.add("objectclass", "top")
					.add("objectclass", "organizationalUnit")
					.add("ou", "people")
					.add("description", "Fictional example organizational unit");	
			System.out.println(entry.toString());
			appPartition.add( new AddOperationContext( null, entryDn, entry));
			*/
		}catch(Exception ex){
			throw new DapException("Failed to add entries to application partition", ex);
		}
	}
	
	private void initDirectoryService(File workdir) throws DapException{
		
		try{
			service = new DefaultDirectoryService();
			service.setInstanceLayout(new InstanceLayout(workdir));
			
			CacheService cacheService = new CacheService();
			cacheService.initialize(service.getInstanceLayout());
			
			service.setCacheService(cacheService);
			
			initSchemaPartition();
			
			DnFactory dnFactory = new DefaultDnFactory(service.getSchemaManager(), service.getCacheService().getCache("systemJdbmPartitionCache"));
			JdbmPartition systemPartition = new JdbmPartition(service.getSchemaManager(), dnFactory);
			systemPartition.setId("system");
			systemPartition.setPartitionPath(new File(service.getInstanceLayout().getPartitionsDirectory(), systemPartition.getId()).toURI());
			systemPartition.setSuffixDn(new Dn(ServerDNConstants.SYSTEM_DN));
			systemPartition.setSchemaManager(service.getSchemaManager());
			
			service.setSystemPartition(systemPartition);
			service.getChangeLog().setEnabled(false);
			service.setDenormalizeOpAttrsEnabled(true);
			
		
		}catch (Exception ex){
			throw new DapException("Exception while Initializing the directory service", ex);
		}
	}
	
	public static void main(String[] args) throws DapException{
		/*
		File directory = new File("/home/myself/git/eclipse/Web.opt");
		
		Properties settings = new Properties();
		settings.setProperty("app_name", "app");
		settings.setProperty("domain", "local.lan");
		
		String dapFolderPath = directory.getAbsolutePath() + File.separator + "var" + File.separator + "dap";
		settings.setProperty("dapFolderPath", dapFolderPath);
		
		DapServer dap = new DapServer(settings);
		dap.startServer();
		
		
		
		LdapConnection connection = new LdapNetworkConnection( "localhost", 10389 );
		connection.setTimeOut(0);
		
		try{
			connection.close();
		}catch(IOException ex){
			throw new DapException("Error while connecting to LDAP", ex);
		}
		*/
	}
}
