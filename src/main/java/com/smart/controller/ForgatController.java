package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgatController {
	 Random random=new Random(1000);
	   
	@Autowired
	private EmailService emailService;
	 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	//email id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm() {
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email")String email,HttpSession session) {
    System.out.println("email"+email);
		
    int otp=random.nextInt(99999999);
     System.out.println("OTP"+otp);
     //write code for send otp
     
     String subject="OTP FROM SCM";
      String message=""
    		  +"<div style='border:1px solid #e2e2e2;padding:20px'>"
    		  +"<h1>"
    		  +otp
    		  +"</h1"
    		  +"</div>";
    		  
    		  
    		  //"OTP="+otp+"";
     String to=email;
    boolean flag= this.emailService.sendEmail(subject, message, to);
    if(flag) { 
    	
    	session.setAttribute("myOtp", otp);
       session.setAttribute("email",email);
    	return "verify_otp";
     
    }else {
    	
    	session.setAttribute("message", "check your email id something wrong");
        return "forgot_email_form";   
    }	
		
	}
	//verify otp handler
     @PostMapping("/verify-otp")
     public String verifyOtp(@RequestParam("otp")int otp,HttpSession session)
      {
        int myOtp=(int)session.getAttribute("myOtp");
        String email=(String)session.getAttribute("email");
    	if(myOtp==otp) {
    		
    		User user=this.userRepository.getUserByUserName(email);
            
        if(user==null) {
        	session.setAttribute("message", "user does not exist with this email !!");
            
        		return "forgot_email_form";
        	
        }else {	
          //send change password form
        	//return "password_change_form"; 
            
        }
        return "password_change_form"; 
    	}else {
           	session.setAttribute("message", "you have entered wrong otp");
        	return "verify_otp";
        
    	}
    		
        
     }//change password
     @PostMapping("/change-password")
     public String changePassword(@RequestParam("newpassword")String newpassword,HttpSession session) {
      
    	 String email=(String)session.getAttribute("email") ;	 
    	 User user=this.userRepository.getUserByUserName(email);
    	 user.setPassword(this.bcrypt.encode(newpassword));
       this.userRepository.save(user);
     return "redirect:/signin?change-password changed successfully";
     }
     
     
     
     
     
	public ForgatController() {
		
	}

	
}
