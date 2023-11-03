package com.espub.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.espub.dao.UserDao;
import com.espub.model.User;

@Service
public class UserService implements UserDetailsService
{
	@Autowired
	UserDao userDao;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		Optional<User> user = userDao.findByUsername(username);
		return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
