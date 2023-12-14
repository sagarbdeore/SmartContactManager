package com.smart.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "home-smart contact manager");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "about -smart contact manager");
		return "about";
	}

	@RequestMapping("/signup/")
	public String signup(Model model) {
		model.addAttribute("title", "Register-smart contact manager");
		model.addAttribute("user", new User());
		return "signup";

	}

	// handler for register user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {
			if (!agreement) {
				System.out.println("you have not agrred  term and condition");
				throw new Exception("you have not agreed term and condition");
			}
			if(result1.hasErrors()) {
				System.out.println("inside result1 haserror");
				System.out.println("error"+result1.toString());
				model.addAttribute("user", user);
				return"signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
		    user.setPassword(passwordEncode.encode(user.getPassword()));

		System.out.println("Agreement" + agreement);
			System.out.println("USER" + user);
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			model.addAttribute("session", session);
			session.setAttribute("message", new Message("successfully registerd!!", "alert-success"));
			// return "normal/user_dashboard";
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			model.addAttribute("session", session);
			session.setAttribute("message", new Message("something went wrong!!" + e.getMessage(), "alert-danger"));

			return "signup";
		}
	}
//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "login page");
		return "login";
	}
	
	
	public HomeController() {
		// TODO Auto-generated constructor stub
	}

}
