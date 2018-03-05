package com.laetienda.dap;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;

public class Ldif {
	final static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(Ldif.class);
	protected static String domain = new String();
	
	protected static void setDomain(String domain){
		 
		 String[] domainParts = domain.split("\\.");
		 
		 for(int c=0; c < (domainParts.length -1); c++){
			 Ldif.domain += String.format("dc=%s,", domainParts[c]);
		 }
		 
		 Ldif.domain += String.format("dc=%s", domainParts[domainParts.length - 1]);
		 log4j.debug("domain: " + Ldif.domain);
	}
	
	public static String getDomain(){
		return domain;
	}
	
	protected static final Dn PEOPLE_DN(){
		try{
			return new Dn("ou=People", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry PEOPLE_ENTRY(){
		try{
			return new DefaultEntry(PEOPLE_DN())
					.add("objectclass", "top")
					.add("objectclass", "organizationalUnit")
					.add("ou", "People")
					.add("description", "People who has been regestired in the domain");
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn SYSADMIN_DN(){
		try{
			return new Dn("uid=sysadmin,ou=People", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry SYSADMIN_ENTRY(String admuserpassword){
		try{
			return new DefaultEntry(SYSADMIN_DN())
					.add("objectclass", "person")
					.add("objectclass", "inetOrgPerson")
					.add("objectclass", "organizationalPerson")
					.add("objectclass", "top")
					.add("uid","sysadmin")
					.add("cn", "sysadmin")
					.add("sn", "sysadmin")
					.add("userpassword", admuserpassword)
					.add("ou", "People")
					.add("description", "Admin user for everything");
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn TOMCAT_USER_DN(){
		try{
			return new Dn("uid=tomcat,ou=People", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry TOMCAT_USER_ENTRY(String tomcatpassword){
		//System.out.println("$tomcatpassword: " + tomcatpassword);
		try{
			return new DefaultEntry(TOMCAT_USER_DN())
					.add("objectclass", "person")
					.add("objectclass", "inetOrgPerson")
					.add("objectclass", "organizationalPerson")
					.add("objectclass", "top")
					.add("uid","tomcat")
					.add("cn", "tomcat")
					.add("sn", "tomcat")
					.add("userpassword", tomcatpassword)
					.add("ou", "People")
					.add("description", "User for unauthenticated people");
		}catch(LdapException ex){
			return null;
		}
	}
	
	
	protected static final Dn GROUPS_DN(){
		try{
			return new Dn("ou=Groups", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry GROUPS_ENTRY(){
		try{
			return new DefaultEntry(GROUPS_DN())
					.add("objectclass", "top")
					.add("objectclass", "organizationalUnit")
					.add("ou", "Groups")
					.add("description", "Groups that will clasify people in the domain");
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn SYSADMINS_DN(){
		try{
			return new Dn("cn=sysadmins,ou=Groups", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry SYSADMINS_ENTRY(){
		try{
			return new DefaultEntry(SYSADMINS_DN())
					.add("objectclass", "top")
					.add("objectclass", "groupOfUniqueNames")
					.add("uniquemember","uid=sysadmin,ou=People," + domain)
					.add("ou", "Groups")
					.add("description", "Users with priviledges to do everything")
					.add("cn","sysadmins");
			
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn MANAGERS_DN(){
		try{
			return new Dn("cn=managers,ou=Groups", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry MAANGERS_ENTRY(){
		try{
			return new DefaultEntry(MANAGERS_DN())
					.add("objectclass", "top")
					.add("objectclass", "groupOfUniqueNames")
					.add("uniquemember","uid=sysadmin,ou=People," + domain)
					.add("ou", "Groups")
					.add("description", "Managers of the web site")
					.add("cn","managers");
			
		}catch(LdapException ex){
			return null;
		}
	}
	
	
	protected static final Dn ACI_TOMCAT_DN(){
		try{
			return new Dn("cn=aciTomcatControlAccess", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry ACI_TOMCAT_SUBENTRY(){
		try{
			return new DefaultEntry(ACI_TOMCAT_DN())
					.add("objectclass", "top")
					.add("objectclass", "subentry")
					.add("objectclass", "accessControlSubentry")
					.add("cn", "aciTomcatControlAccess")
					.add("subtreeSpecification", "{base \"ou=People\"}")
					.add("prescriptiveACI ",
							"{"
								+ "identificationTag \"aciTomcatEnableAddEntry\","
								+ "precedence 5,"
								+ "authenticationLevel simple,"
								+ "itemOrUserFirst userFirst: " 
								+ "{"
									+ "userClasses " 
									+ "{"
										+ "name { \"uid=tomcat,ou=people," + domain + "\" }" 
									+ "},"
									+ "userPermissions " 
									+ "{"
										+ "{"
											+ "protectedItems { entry, allUserAttributeTypesAndValues },"
											+ "grantsAndDenials " 
											+ "{ "
												+ "grantCompare,"
												+ "grantFilterMatch,"
												+ "grantReturnDN,"
												+ "denyModify,"
												+ "grantBrowse,"
												+ "grantAdd,"
												+ "grantRead" 
											+ "}"
										+ "}"
									+ "}"
								+ "}"
							+ "}"
							);			
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn SERVICES_DN() throws DapException {
		try{
			return new Dn("ou=services", domain);
		}catch(LdapInvalidDnException ex){
			throw new DapException("Failed to create SERVICES DN", ex);
		}
	}
	
	protected static final Entry SERVICES_ENTRY() throws DapException {
			try {
				return new DefaultEntry(SERVICES_DN())
						.add("objectclass", "top")
					    .add("objectclass", "organizationalunit")
					    .add("ou", "services");
			} catch (LdapException ex) {
				throw new DapException("", ex);
			}
	}
	
	protected static final Dn LDAP_DN() throws DapException {
		try{
			return new Dn("uid=ldap","ou=services", domain);
		}catch(LdapInvalidDnException ex){
			throw new DapException("Failed to create LDAP DN", ex);
		}
	}
	
	protected static final Entry LDAP_ENTRY() throws DapException {
			try {
				return new DefaultEntry(LDAP_DN())
						.add("objectClass", "top")
						.add("objectClass", "organizationalUnit")
						.add("objectClass", "krb5KDCEntry")
						.add("objectClass", "uidObject")
						.add("objectClass", "krb5Principal")
						.add("krb5KeyVersionNumber", "0")
						.add("krb5PrincipalName", "ldap/la-etienda.com@LA-ETIENDA.COM")
						.add("uid", "ldap")
						.add("userPassword", "randomKey")
						.add("ou", "TGT");
			} catch (LdapException ex) {
				throw new DapException("Failed to create SERVICES entry", ex);
			}
	}

	protected static final Dn KRBTGT_DN() throws DapException {
		try{
			return new Dn("uid=krbtgt","ou=services", domain);
		}catch(LdapInvalidDnException ex){
			throw new DapException("Failed to create KRBTGT DN", ex);
		}
	}
	
	protected static final Entry KRBTGT_ENTRY() throws DapException {
			try {
				return new DefaultEntry(KRBTGT_DN())
						.add("objectclass", "top")
						.add("objectClass", "organizationalUnit")
						.add("objectClass", "krb5KDCEntry")
						.add("objectClass", "uidObject")
						.add("objectClass", "krb5Principal")
						.add("krb5KeyVersionNumber", "0")
						.add("krb5PrincipalName", "krbtgt/la-etienda.com@LA-ETIENDA.COM")
						.add("uid", "krbtgt")
						.add("userPassword", "randomkey")
						.add("ou", "LDAP");

			} catch (LdapException ex) {
				throw new DapException("Failed to create KRBTGT entry", ex);
			}
	}
	
	protected static final Dn ACI_SYSADMIN_DN(){
		try{
			return new Dn("cn=aciSysadminControlAccess", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry ACI_SYSADMIN(){
		try{
			return new DefaultEntry(ACI_SYSADMIN_DN())
					.add("objectclass", "top")
					.add("objectclass", "subentry")
					.add("objectclass", "accessControlSubentry")
					.add("cn", "aciSysadminControlAccess")
					.add("subtreeSpecification", "{ }")
					.add("prescriptiveACI ", 
							"{"
							+ "identificationTag \"aciSysadminControlAccess\","
							+ "precedence 0,"
							+ "authenticationLevel none,"
							+ "itemOrUserFirst userFirst: " 
								+ "{"
									+ "userClasses " 
									+ "{"
										+ "userGroup { \"cn=sysadmins,ou=groups," + domain + "\" }" 
	        						+ "},"
	        						+ "userPermissions " 
	    							+ "{"
	    								+ "{"
	    									+ "protectedItems " 
	    									+ "{"
	    										+ "entry,"
	    										+ "allUserAttributeTypes,"
												+ "allUserAttributeTypesAndValues" 
	                						+ "},"
	                						+ "grantsAndDenials " 
	                						+ "{"
	                							+ "grantInvoke,"
	                							+ "grantExport,"
	                							+ "grantDiscloseOnError,"
	                							+ "grantFilterMatch,"
	                							+ "grantRename,"
	                							+ "grantRemove,"
	                							+ "grantModify,"
	                							+ "grantAdd,"
	                							+ "grantCompare,"
	                							+ "grantBrowse,"
	                							+ "grantReturnDN,"
	                							+ "grantImport,"
	                							+ "grantRead" 
	                						+ "}"
	            						+ "}"
	        						+ "}"
        						+ "}"
							+ "}"
							);
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn ACI_MANAGER_DN(){
		try{
			return new Dn("cn=aciManagerControlAccess", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry ACI_MANAGER(){
		try{
			return new DefaultEntry(ACI_MANAGER_DN())
					.add("objectclass", "top")
					.add("objectclass", "subentry")
					.add("objectclass", "accessControlSubentry")
					.add("cn", "aciManagerControlAccess")
					.add("subtreeSpecification", "{base \"ou=People\"}")
					.add("prescriptiveACI ", 
							"{"
								+ "identificationTag \"aciManagerControlAccess\","
								+ "precedence 0,"
								+ "authenticationLevel simple,"
								+ "itemOrUserFirst userFirst: " 
								+ "{"
									+ "userClasses " 
									+ "{"
										+ "userGroup { \"cn=managers,ou=groups," + domain + "\" }"
					        		+ "},"
					        		+ "userPermissions " 
					        		+ "{"
					        			+ "{"
					        				+ "protectedItems { entry, allUserAttributeTypesAndValues },"
					        				+ "grantsAndDenials " 
					        				+ "{"
					        					+ "grantCompare,"
					        					+ "grantFilterMatch,"
					        					+ "grantBrowse,"
					        					+ "grantReturnDN,"
					        					+ "grantModify,"
					        					+ "grantRead" 
					                		+ "}"
					            		+ "},"
					            		+ "{"
					                		+ "protectedItems " 
					                		+ "{"
					                    		+ "allAttributeValues { uid, userPassword }" 
				                    		+ "},"
				                    		+ "grantsAndDenials " 
				                    		+ "{"
						                    	+ "denyAdd,"
						                    	+ "denyRemove,"
						                    	+ "denyInvoke,"
						                    	+ "denyImport,"
						                    	+ "denyDiscloseOnError,"
						                    	+ "denyRename,"
						                    	+ "denyModify,"
						                    	+ "denyExport" 
						                	+ "}"
					                	+ "},"
					                	+ "{"
						                	+ "protectedItems " 
							                + "{"
							                    + "allAttributeValues " 
							                    + "{"
							                        + "cn,"
							                        + "sn,"
							                        + "mail" 
							                    + "}"
						                    + "},"
						                    + "grantsAndDenials " 
						                	+ "{"
						                    	+ "grantModify,"
						                    	+ "grantRemove,"
						                    	+ "grantAdd" 
						                	+ "}"
					                	+ "}"
				                	+ "}"
			                	+ "}"
					    	+ "}"
				    	);
		}catch(LdapException ex){
			return null;
		}
	}
	
	protected static final Dn ACI_USER_DN(){
		try{
			return new Dn("cn=aciUserControlAccess", domain);
		}catch(LdapInvalidDnException ex){
			return null;
		}
	}
	
	protected static final Entry ACI_USER(){
		try{
			return new DefaultEntry(ACI_USER_DN())
					.add("objectclass", "top")
					.add("objectclass", "subentry")
					.add("objectclass", "accessControlSubentry")
					.add("cn", "aciUserControlAccess")
					.add("subtreeSpecification", "{base \"ou=People\"}")
					.add("prescriptiveACI ",
							"{"
								+ "identificationTag \"aciUserControlAccess\","
								+ "precedence 0,"
								+ "authenticationLevel simple,"
								+ "itemOrUserFirst userFirst: " 
								+ "{"
									+ "userClasses { thisEntry },"
									+ "userPermissions " 
									+ "{"
										+ "{"
											+ "precedence 0,"
											+ "protectedItems " 
											+ "{"
												+ "allAttributeValues { uid }" 
                							+ "},"
                							+ "grantsAndDenials " 
            							+ "{"
	            							+ "denyAdd,"
	            							+ "denyRemove,"
	            							+ "grantReturnDN,"
	            							+ "grantRead,"
	            							+ "denyRename,"
	            							+ "denyModify" 
            							+ "}"
            						+ "},"
        						+ "{"
        							+ "precedence 0,"
        							+ "protectedItems " 
        							+ "{"
        								+ "allAttributeValues " 
        								+ "{"
        									+ "userPassword,"
        									+ "mail,"
        									+ "sn,"
        									+ "cn" 
                    					+ "}"
                					+ "},"
                					+ "grantsAndDenials " 
                					+ "{"
                						+ "grantInvoke,"
                						+ "grantExport,"
                						+ "grantDiscloseOnError,"
                						+ "grantFilterMatch,"
                						+ "grantRename,"
                						+ "grantModify,"
                						+ "grantRemove,"
                						+ "grantAdd,"
                						+ "grantCompare,"
                						+ "grantBrowse,"
                						+ "grantReturnDN,"
                						+ "grantImport,"
                						+ "grantRead" 
                					+ "}"
        						+ "},"
        						+ "{"
        							+ "precedence 0,"
        							+ "protectedItems { entry, allUserAttributeTypesAndValues },"
        							+ "grantsAndDenials " 
    								+ "{"
    									+ "grantCompare,"
    									+ "grantFilterMatch,"
    									+ "grantBrowse,"
    									+ "grantReturnDN,"
    									+ "grantModify,"
    									+ "grantRead" 
            						+ "}"
        						+ "}"
    						+ "}"
						+ "}"
					+ "}"
				);
		}catch(LdapException ex){
			return null;
		}
	}
	
	public static void main(String[] args) {
		Ldif.setDomain("dc=la-etienda,dc=com");
		String text = "{"
				+ "identificationTag \"aciTomcatEnableAddEntry\","
				+ "precedence 5,"
				+ "authenticationLevel simple,"
				+ "itemOrUserFirst userFirst: " 
				+ "{"
					+ "userClasses " 
					+ "{"
						+ "name { \"uid=2,ou=people," + domain + "\" }" 
					+ "},"
					+ "userPermissions " 
					+ "{"
						+ "{"
							+ "protectedItems { entry, allUserAttributeTypesAndValues },"
							+ "grantsAndDenials " 
							+ "{"
								+ "grantCompare,"
								+ "grantFilterMatch,"
								+ "grantReturnDN,"
								+ "denyModify,"
								+ "grantBrowse,"
								+ "grantAdd,"
								+ "grantRead" 
							+ "}"
						+ "}"
					+ "}"
				+ "}"
			+ "}";
		
		System.out.println(text);
	}
}