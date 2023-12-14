package com.smart.service;

import java.util.Properties;
/*
import javax.mail.internet.MimeMessage;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
*/
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	public boolean sendEmail(String subject,String message,String to) {
	//rest of the code
	
		boolean f =false;
		
	String from="sagardeore861@gmail.com";
	
	//variable for gmail;
	 String host="smtp.gmail.com";
	 
	//get the system properties
	 
	 Properties properties=System.getProperties();
	 System.out.println("propertirs"+properties);
     
	//setting important info to properties object
	 //host set
	 properties.put("mail.smtp.host", host);
	 properties.put("mail.smtp.port", "465");
	 properties.put("mail.smtp.ssl.enable", "true");
	 properties.put("mail.smtp.auth", "true");
	
	 //step 1 to get session object..
	 
	 Session session=Session.getInstance(properties,new Authenticator() {
       @Override
       protected PasswordAuthentication getPasswordAuthentication() {
    	   return new PasswordAuthentication("sagardeore861@gmail.com","rrbtlzbvchnphgbd");
       }
       });  
		session.setDebug(true);	 
		
		
		//step 2 compose msg 
		MimeMessage m= new MimeMessage(session);
		 
		try {
		m.setFrom(from);	
		m.addRecipient(Message.RecipientType.TO ,new InternetAddress(to));
		m.setSubject(subject);	
	//	m.setText(message);
		m.setContent(message, "text/html");
		
		//step 3 send the msg useing trensport class
		
		Transport.send(m);
		System.out.println("send success.....");
        f=true;		
		
		}catch(Exception e) {
		e.printStackTrace();
		}
		
		return f;
			
	}
	
	
	public EmailService() {}
}
