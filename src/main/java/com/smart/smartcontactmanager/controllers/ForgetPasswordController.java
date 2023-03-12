package com.smart.smartcontactmanager.controllers;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.repository.UserRepository;
import com.smart.smartcontactmanager.service.EmailService;


@Controller
public class ForgetPasswordController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/forget")
	public String forgetPasswordPage() {
		
		
		return "forget-password";
	}
	
	@PostMapping("/otppage")
	public String verifyOtp(@RequestParam("email") String email,HttpSession session) {
		User user = userRepository.getUserbyUsername(email);
		if(user!=null) {
		Random random=new Random();
		int otp = random.nextInt(999999);
		System.out.println("Email :"+email);
		System.out.println("OTP is : "+otp);
		String to=email;
		String msg="<h1>To chage Password Your OTP is :"+otp+"<h1>";
		String subject="SCM : Change Password Verification";
		EmailService.sendEmail(msg,subject,to);
		session.setAttribute("myOtp", otp);
		session.setAttribute("email", email);
		session.setAttribute("message", new Message("An OTP has sent to your email id.", "alert-success"));
		return "verifyOtp";
		}
		else {
			session.setAttribute("message", new Message("No such user found ! Please signup", "alert-danger"));
			return "forget-password";
		}
	}
	
	@PostMapping("/verify")
	public String verify(@RequestParam("otp") Integer otp,HttpSession session) 
	{
		
		Integer myOtp=(int)session.getAttribute("myOtp");
		
		
		
	if(myOtp.equals(otp)) 
		{
			return "change-password";
		}
	else {
		session.setAttribute("message", new Message("Invalid OTP", "alert-danger"));
	}
		return "verifyOtp";
	}
	
	@GetMapping("/changepwd")
	public String changePasswordPage() {
			
			
		return "change-password";
		
	}
	
	@PostMapping("/processchangepwd")
	public String processChangePassword(@RequestParam("newPassword") String password, HttpSession session) 
	{
		System.out.println(password);
		System.out.println(session.getAttribute("email"));
		String uname = (String)session.getAttribute("email");
		User user = userRepository.getUserbyUsername(uname);
		user.setPassword(this.bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
		String to=(String)session.getAttribute("email");
		String subject="Password Changed";
		String msg="Your password has been changed successfully your current password is : "+password+ "if you didn't change this go and change your password immidiately.";
		EmailService.sendEmail(msg, subject, to);
		session.setAttribute("message", new Message("Password updated successfully !", "alert-success"));
		return "change-password";
	}
	
	
	

}
