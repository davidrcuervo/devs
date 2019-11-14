package com.laetienda.app;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;		
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.install.InstallerException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;

/**
 * 
 * @author I849921
 * 
 */
public class Aes {
	
	private final static Logger log = LogManager.getLogger(Aes.class);
	
	/**
	 * 
	 * @param textToHash Text to be hashed that will be written in text file or database field
	 * @param hashPhrase Phrase used to hash text, it is required otherwise it will throw exception 
	 * @return
	 * @throws AppException
	 */
	public String encrypt(String textToHash, String hashPhrase) throws AppException {
	    byte[] ivBytes;
	    String password=hashPhrase;

	    /*you can give whatever you want for password. This is for testing purpose*/
	    SecureRandom random = new SecureRandom();
	    byte bytes[] = new byte[20];
	    random.nextBytes(bytes);
	    byte[] saltBytes = bytes;
	    byte[] encryptedTextBytes;
	    byte[] buffer;
	    
	    try {
	    	if(textToHash == null || textToHash.isBlank()) {
	    		throw new InstallerException("text to cypher can't be empty");
	    	}
	    	
	    	if(hashPhrase == null || hashPhrase.isBlank()) {
	    		throw new InstallerException("Hash phrasee can't be empty");
	    	}
	    	
	    	// Derive the key
		    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),saltBytes,65556,256);
		    SecretKey secretKey = factory.generateSecret(spec);
		    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		    
		    //encrypting the word
		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, secret);
		    AlgorithmParameters params = cipher.getParameters();
		    ivBytes =   params.getParameterSpec(IvParameterSpec.class).getIV();
			encryptedTextBytes = cipher.doFinal(textToHash.getBytes("UTF-8"));

			//prepend salt and vi
			buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
			System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
			System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
			System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);
		} catch (	IllegalBlockSizeException | 
					BadPaddingException | 
					UnsupportedEncodingException | 
					NoSuchAlgorithmException | 
					InvalidKeySpecException | 
					NoSuchPaddingException | 
					InvalidKeyException | 
					InvalidParameterSpecException e
				) 
	    {
			throw new InstallerException("Failed to encrypt text", e);
		}
	    
	    return new Base64().encodeToString(buffer);
	}
	
	/**
	 * 
	 * @param encryptedText
	 * @param hashPhrasse
	 * @return
	 * @throws AppException
	 */
	public String decrypt(String encryptedText, String hashPhrasse) throws AppException {
	    log.debug("$encryptedText: " + encryptedText);
		
	    byte[] decryptedTextBytes = null;
	    
	    try {
		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    
		    //strip off the salt and iv
		    ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(encryptedText));
		    byte[] saltBytes = new byte[20];
		    buffer.get(saltBytes, 0, saltBytes.length);
		    byte[] ivBytes1 = new byte[cipher.getBlockSize()];
		    buffer.get(ivBytes1, 0, ivBytes1.length);
		    byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes1.length];
		  
		    buffer.get(encryptedTextBytes);
		    
		    // Deriving the key
		    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		    PBEKeySpec spec = new PBEKeySpec(hashPhrasse.toCharArray(), saltBytes, 65556, 256);
		    SecretKey secretKey = factory.generateSecret(spec);
		    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes1));
		    
		    
		    decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
	    } catch (	IllegalBlockSizeException | 
	    			BadPaddingException | 
	    			NoSuchAlgorithmException | 
	    			NoSuchPaddingException | 
	    			InvalidKeySpecException | 
	    			InvalidKeyException | 
	    			InvalidAlgorithmParameterException e
    			) 
	    {
	        throw new InstallerException("Failed to decrypt text", e);
	    }
	   
	    return new String(decryptedTextBytes);
	  }
	
	
	public static void main(String[] args) {
	    
		final String USERNAME = "tomcat";
		String password;
		/*
		//Example of hashing a password
	    Aes en=new Aes();
	    String encryptedWord;
		try {
			encryptedWord = en.encrypt(password, USERNAME);
			System.out.println("Encrypted word is : " + encryptedWord);
		} catch (AppException e1) {
			log.error(e1.getMessage(), e1.getParent());
		} 
	    */
		
		
		//Example of decipher text 
	    Aes de =new Aes();
	    try {
	    	password = de.decrypt("8MMvevWMY3qQ3O+u4wYTqCfQ/B4nQbKY91iMDtjwdDYUld8jqrs3HgktwNyXUwKPPlPanQ==", USERNAME);
			System.out.println("Decrypted word is : " + password);
		} catch (AppException e) {
			log.error(e.getMessage(), e.getParent());
		}
		
	  }
}
