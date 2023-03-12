package com.smart.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserbyUsername(username);
		if(user == null)
		{
			throw new UsernameNotFoundException("User Not Found");
		}
		CustomUserDetails userDetails=new CustomUserDetails(user);
		return userDetails;
	}

}
