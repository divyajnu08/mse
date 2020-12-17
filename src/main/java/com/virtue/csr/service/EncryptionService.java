package com.virtue.csr.service;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
		
		private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);
		
		private SecretKeySpec secretKey;
		
		@Value("${encryption.randomKey}")
		private String randomKey;
		
		@PostConstruct
		private void setKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
			byte[] key=null;
			MessageDigest sha = null;
            key = randomKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
		}
		
		public String encrypt(String strToEncrypt) 
	    {
	        try
	        {
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        } 
	        catch (Exception e) 
	        {
	            log.error("Error while encrypting: ",e);
	        }
	        return null;
	    }
		
		public  String decrypt(String strToDecrypt) 
	    {
	        try
	        {
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        } 
	        catch (Exception e) 
	        {
	            log.error("Error while decrypting: ",e);
	        }
	        return null;
	    }
}


