package com.smart.smartcontactmanager.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.repository.UserRepository;
import com.smart.smartcontactmanager.service.EmailService;

@Controller
public class MyController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model)	{
		model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model)	{
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String signin(Model model)	{
		model.addAttribute("title","Login - Smart Contact Manager");
		
		return "signin";
	}
	
	@RequestMapping(value = "/register-user", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user, @RequestParam(value = "agreement", defaultValue = "false")boolean agreement,Model model,HttpSession session)	{
		try { 
			if(!agreement) {
				System.out.println("You have not agreed the terms and conditions.");
				throw new Exception("You have not agreed the terms and conditions.");
			}
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User result = this.userRepository.save(user);
			System.out.println("User"+result);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("User Registerd Successfully","alert-success"));
			String to=user.getEmail();
			String subject="SCM - Registration Successful";
			String msg="<h1>Congrts..! You have been successfully registered with Smart Contact Manager with UserId: "+user.getEmail()+"</h1>";
			EmailService.sendEmail(msg, subject, to);
			return "signup";
		} catch (Exception e) {
			session.setAttribute("message", new Message(e.getMessage(),"alert-danger"));
			model.addAttribute("user",user);
			return "signup";
		}
	}
	
	
}
