package com.smart.smartcontactmanager.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.ExcelToDatabase;
import com.smart.smartcontactmanager.repository.ContactRepository;
import com.smart.smartcontactmanager.repository.UserRepository;

@Service
public class ImportFromExcelService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	public void SaveExcelData(MultipartFile file,User user) 
	{
		try {
			List<Contact> contacts = ExcelToDatabase.importExcelFile(file.getInputStream(),user);
			
			
			this.contactRepository.saveAll(contacts);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
