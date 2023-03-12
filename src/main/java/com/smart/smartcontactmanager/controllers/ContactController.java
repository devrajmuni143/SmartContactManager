package com.smart.smartcontactmanager.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.ExcelToDatabase;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.repository.ContactRepository;
import com.smart.smartcontactmanager.repository.UserRepository;
import com.smart.smartcontactmanager.service.ImportFromExcelService;

@Controller
@RequestMapping("/user")
public class ContactController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ContactRepository contactRepository;
	@Autowired
	ImportFromExcelService service;

	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println(userName);
		User user = userRepository.getUserbyUsername(userName);
		model.addAttribute("user", user);

		model.addAttribute("contact", new Contact());

	}

	@GetMapping("/add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "user/add-contact-form";
	}

	@PostMapping("/save-contact")
	public String saveContact(@ModelAttribute Contact contact, @RequestParam(value = "imageFile") MultipartFile file,
			Model model, Principal principal, HttpSession session) throws IOException {

		try {

//				getting user From userdao
			User user = userRepository.getUserbyUsername(principal.getName());

//				 creating custom image name
			String photoname = user.getId() + "-" + file.getOriginalFilename();
			contact.setImage(photoname);
			contact.setUser(user);

			System.out.println(user);
			System.out.println(contact);

//				uploading image to the contact_images folder

			File filepath = new ClassPathResource("static/contact_images").getFile();

			Path path = Paths.get(filepath.getAbsolutePath() + File.separator + photoname);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);// //saving contacts to user
			user.getContacts().add(contact);
			User result = this.userRepository.save(user);
			if (result != null) {
				session.setAttribute("message", new Message("Contact Saved Successfully !", "success"));
			} else {
				throw new Exception("Something Went Wrong");
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message(e.getMessage(), "danger"));
		}

		return "user/add-contact-form";
	}
	
	@GetMapping("/import")
	public String importContacts() {
		
		return "user/import_contacts_Excel";
	} 
	
	@PostMapping("/import-excel")
	public String getContactsFromExcel(@RequestParam("file") MultipartFile file,HttpSession session, Model model, Principal principal) {
		String userName = principal.getName();
		User user = this.userRepository.getUserbyUsername(userName);
		System.out.println(user);
		System.out.println(file.getOriginalFilename());
		if(ExcelToDatabase.isExcelFormat(file)) 
		{
			this.service.SaveExcelData(file, user);
			session.setAttribute("message", new Message("Contact uploaded Succssfully .", "alert-success"));
		}
		else {
			session.setAttribute("message", new Message("Upload excel file only .", "alert-danger"));
	}

		return "user/index";
	} 

	@GetMapping("/view-contact/{page}")
	public String viewContacts(@PathVariable("page") int page, Model model, Principal principal) {
		String userName = principal.getName();
		User user = this.userRepository.getUserbyUsername(userName);
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactRepository.getContactsById(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", contacts.getTotalPages());
		model.addAttribute("title", "Contacts");

		return "user/view-contact";
	}

	@GetMapping("/contact-details/{cid}")
	public String viewContactDetails(@PathVariable("cid") int cid, Model model, Principal principal,
			HttpSession session) {
		User user = this.userRepository.getUserbyUsername(principal.getName());
		Optional<Contact> optional = this.contactRepository.findById(cid);
		Contact contact = optional.get();
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("title", "Contact Details");
			model.addAttribute("contact", contact);
		} else {
			session.setAttribute("message", new Message("You are unauthorized for this request !", "danger"));
		}
		return "user/view-contact-details";
	}

	@GetMapping("/delete-contact/{cid}")
	public String deleteContact(@PathVariable("cid") int cid, Model model, Principal principal, HttpSession session) {
		try {
			User user = this.userRepository.getUserbyUsername(principal.getName());
			Contact contact = this.contactRepository.getById(cid);
			System.out.println(user);
			System.out.println(contact);
			if (user.getId() == contact.getUser().getId()) {
				System.out.println(contact.getImageFile());
				if (contact.getImageFile() != "default.jpg") {
					String photo = contact.getImageFile();
					File file = new ClassPathResource("static/contact_images/").getFile();
					File photoFile = new File(file, photo);
					photoFile.delete();
					System.out.println(photo + "deleted Successfully");
			}
			this.contactRepository.deleteById(cid);
			session.setAttribute("message", new Message("Contact Deleted Successfully ", "success"));
			
		}
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something Went Wrong", "danger"));
		}

		return "redirect:/user/view-contact/0";
	}

	@GetMapping("update-contact/{cid}")
	public String updateContactPage(@PathVariable("cid") int cid, Model model, Principal principal) {

		try {
			User user = this.userRepository.getUserbyUsername(principal.getName());
			Contact contact = this.contactRepository.getById(cid);
			if (user.getId() == contact.getUser().getId()) {
				model.addAttribute("contact", contact);
				model.addAttribute("title", "Update Contact");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "user/update-contact";
	}
	
	
	@PostMapping("/process-update")
	public String updateContact(@ModelAttribute("contact") Contact contact,@RequestParam("imageFile") MultipartFile file, Principal principal,Model model, HttpSession session) {
		try {
			if(!file.isEmpty()) {
//			delete old photo
				
			Contact oldContact=this.contactRepository.getById(contact.getcId());
			String oldImage = oldContact.getImageFile();
			File deleteFilePath = new ClassPathResource("static/contact_images/").getFile();
			File deleFile = new File(deleteFilePath, oldImage);
			deleFile.delete();
			
//			Upload new Photo
			
			File updateFile = new ClassPathResource("static/contact_images/").getFile();
			String newImage=contact.getcId()+"-"+file.getOriginalFilename();
			Path path = Paths.get(updateFile.getAbsolutePath()+File.separator+newImage);
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			contact.setImage(newImage);
			
			
			}
			else {
				
				String newImage=contact.getcId()+"-"+file.getOriginalFilename();
				contact.setImage(newImage);
			}
			
			User user = this.userRepository.getUserbyUsername(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Contact Updated Successfully !","success"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/view-contact/0";
	}
}
