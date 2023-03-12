package com.smart.smartcontactmanager.controllers;


import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.repository.ContactRepository;
import com.smart.smartcontactmanager.repository.UserRepository;
import com.smart.smartcontactmanager.service.EmailService;



@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ContactRepository contactRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println(userName);
		User user = userRepository.getUserbyUsername(userName);
		model.addAttribute("user", user);

		model.addAttribute("contact", new Contact());

	}

	@RequestMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "Dashboard");

		return "/user/index";
	}

	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		try {
			User user = this.userRepository.getUserbyUsername(principal.getName());
			model.addAttribute("user",user);
			model.addAttribute("title","Profile");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return "user/profile";
	}

	@GetMapping("/settings")
	public String settings(Model model) {
		model.addAttribute("title", "Settings");
		return "user/settings";
	}
	
	@GetMapping("/change-password")
	public String changePassword(Model model) {
		model.addAttribute("title", "Change Password");
		return "user/change-password";
	}
	
	@PostMapping("/process-changePwd")
	public String processChangePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword ,Model model, Principal principal, HttpSession session) throws InterruptedException {
		User user = userRepository.getUserbyUsername(principal.getName());
		if(this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) 
		{
			user.setPassword(bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Password Changed Successfully", "success"));
			String to=principal.getName();
			String subject="SCM:Password Changed";
			String msg="<h1>Your password has been changed successfully, your current password is : "+newPassword+"</h1>"
					+"<br><h1> if you didn't change this go and change your password immidiately.</h1>";
			EmailService.sendEmail(msg, subject, to);
		}
		else {
			session.setAttribute("message", new Message("You have entered a Incorrect Old Password !", "danger"));
			
		}
		
		return "redirect:/user/change-password";
	}
	

	

}
