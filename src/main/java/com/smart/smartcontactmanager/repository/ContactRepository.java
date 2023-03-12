package com.smart.smartcontactmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;



public interface ContactRepository extends JpaRepository<Contact, Integer>
{
	@Query("from Contact as c where c.user.id= :id")
	public Page<Contact> getContactsById(@Param("id") int userId, Pageable pageable);
	
	public List<Contact> findByNameContainingAndUser(String name, User user);
}
