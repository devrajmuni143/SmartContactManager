package com.smart.smartcontactmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.repository.ContactRepository;
import com.smart.smartcontactmanager.repository.UserRepository;

@RestController
public class SearchController {

	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> serachName(@PathVariable("query") String query,Principal principal){
		System.out.println(query);
		User user = this.userRepository.getUserbyUsername(principal.getName());
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
	
	
	
	
}
