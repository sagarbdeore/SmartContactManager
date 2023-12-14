package com.smart.controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
 
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder ;
	
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private ContactRepository contactRepository;
  
  @ModelAttribute
  public void addCommData(Model model,Principal principal) {
	  String userName=principal.getName();
	  
	  User user=userRepository.getUserByUserName(userName);
	  model.addAttribute("user", user);
  }
  
	
	@RequestMapping("/index")
  public String dashboard(Model model,Principal principal) {
		model.addAttribute("title", "User Dashboard");	
	 return "normal/user_dashboard";
 }
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
 //processing add contact form
	
    @PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage")MultipartFile file,
			Principal principal,HttpSession session) {
  try {
    	String name=principal.getName();
       User user=this.userRepository.getUserByUserName(name);
     //processsing and uploading file
       if(file.isEmpty()) {
    	   //System.out.println("empty");
    	   contact.setImage("contact.png");
       }
       else {
    	   contact.setImage(file.getOriginalFilename());
    	  File saveFile=new ClassPathResource("static/img").getFile();   
         Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
    	  Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING ) ;              
        System.out.println("file is uploded");     
       }
       user.getContacts().add(contact);
       contact.setUser(user);
      
       this.userRepository.save(user);
    	//System.out.println("added to data base");
       //message success
    	session.setAttribute("message", new Message("Your Contact is added!! Add more..","success"));
     }  catch(Exception e) {
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			//message error
			session.setAttribute("message",new Message("some went wrong!! try again","danger"));
			
		}
    	return"normal/add_contact_form";		
	}
    
    
    //show contact handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page")Integer page,Model m,Principal principal) {
    	m.addAttribute("title"," Show user contact");
    	
    	String userName=principal.getName();
    	User user=this.userRepository.getUserByUserName(userName);
       Pageable pageable=PageRequest.of(page,3); 
    	Page<Contact>contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);
            	
    	m.addAttribute("contacts", contacts);
    	m.addAttribute("currentPage", page);
    	m.addAttribute("totalPages", contacts.getTotalPages());
        return"normal/show_contacts";	
    	
    }
    //showing particular contact detail
    @RequestMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId")Integer cId,Model model, Principal principal) {
        Optional <Contact>contactOptional=this.contactRepository.findById(cId);
        Contact contact=contactOptional.get();
    	
    	String userName=principal.getName();
    	User user=this.userRepository.getUserByUserName(userName);
    	 
    	if(user.getId()==contact.getUser().getId()) {
    	model.addAttribute("contact",contact);
    	model.addAttribute("title", contact.getName());
      }
    	return "normal/contact_detail";	
    }
    //delete handler
    
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid")Integer cId,Model model,HttpSession session) {
      
    	Optional<Contact>contactOptional=this.contactRepository.findById(cId);
    	Contact contact=contactOptional.get();
    	
   //Contact contact=contactOptional.get(); 
   
    contact.setUser(null); 	
   this.contactRepository.delete(contact);
   //session.setAttribute("message", new Message("contact deleted successfully","success"));
   
   return"redirect:/user/show-contacts/0";
    }
    //update form handler
    @PostMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid")Integer cid,Model m) {
    	
    	m.addAttribute("title", "update Contact");
    	
    	Contact contact=this.contactRepository.findById(cid).get();
    	
    	m.addAttribute("contact", contact);
    	return "normal/update_form";
    }
    
    //update contact handler
    @RequestMapping(value="/process-update",method=RequestMethod.POST)
    public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model m,HttpSession session,Principal principal) {
     try {//image
    	 Contact oldContactDetail=this.contactRepository.findById(contact.getcId()).get();
    	 if(!file.isEmpty()) {
    		 //delete old photo
    	File deleteFile=new ClassPathResource("static/img").getFile();
    	File file1=new File(deleteFile,oldContactDetail.getImage()); 
    	file1.delete();
    	//update new phota
     File saveFile=new ClassPathResource("static/img").getFile();   
      Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
    Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING ) ;              
    	    
    contact.setImage(file.getOriginalFilename());		 
    	 }
    	 else {
    	 contact.setImage(oldContactDetail.getImage())	;
    	 
    		 
    	 }
    	 User user=this.userRepository.getUserByUserName(principal.getName());
    	 contact.setUser(user);
    	this.contactRepository.save(contact);
        	   
        	 
     }catch(Exception e) {
        	   e.printStackTrace();           
     }
     
     return "redirect:/user/"+contact.getcId()+"/contact";
    }
      //your profile handler
    @GetMapping("/profile")
    public String yourprofile(Model model) {
    	model.addAttribute("title", "Profile page");
    	return "normal/profile";
    }
    //open setting handler
    @GetMapping("/settings")
    public String openSettings() {
    	
    	return "normal/settings";
    	
    }
    //change password handler
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Principal principal,HttpSession session){
       
    	String userName=principal.getName();
    	User currentUser=this.userRepository.getUserByUserName(userName);
    	if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
    	{ //change the password
    		currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
    		this.userRepository.save(currentUser);
    		session.setAttribute("message",new Message("your password is successfully changed..","success" ));
    	}else {
    		//error
    		session.setAttribute("message",new Message("Please enter correct old password ..","danger" ));
    	    return "redirect:/user/settings";
    		
    		
    	}
    	return "redirect:/user/index";
    }
      
    public UserController() {
		// TODO Auto-generated constructor stub
	}

}
