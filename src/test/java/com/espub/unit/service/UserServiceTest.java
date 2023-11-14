package com.espub.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.espub.dao.UserDao;
import com.espub.model.User;
import com.espub.service.UserService;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest 
{
	@Mock
	UserDao userDao;
	@InjectMocks
	UserService userService;
	
	@Test
	void loadUserByUsernameShouldReturnUserDetails()
	{
		String username = "username";
		User user = User.builder()
				.username(username)
				.password("password")
				.build();
		
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
		
		UserDetails userDetails = userService.loadUserByUsername(username);
		
		assertEquals(username, userDetails.getUsername());
	}
	
	@Test
	void loadUserByUnknownUsernameShouldThrowUsernameNotFoundException()
	{
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrowsExactly(UsernameNotFoundException.class,() -> userService.loadUserByUsername("username"));
	}
}
