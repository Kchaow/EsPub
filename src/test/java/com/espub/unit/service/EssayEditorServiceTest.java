package com.espub.unit.service;

import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import com.espub.dao.EssayDao;
import com.espub.dao.UserDao;
import com.espub.dto.EssayRequest;
import com.espub.exception.NoPermissionException;
import com.espub.model.Essay;
import com.espub.model.User;
import com.espub.service.EssayEditorService;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class EssayEditorServiceTest 
{
	Logger logger = LoggerFactory.getLogger(EssayEditorServiceTest.class);
	@Mock
	EssayDao essayDao;
	@Mock
	GrantedAuthority adminRole;
	@Mock
	UserDao userDao;
	@InjectMocks
	EssayEditorService essayEditorService;
	Authentication authentication;
	
	@BeforeEach
	void setAuthentication() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		authentication = SecurityContextHolder.getContext().getAuthentication();
		ReflectionTestUtils.setField(essayEditorService, "zoneId", ZoneId.of("Europe/Moscow"));
	}
	
	@Test
	@WithMockUser
	void addEssayByAuthorizedUserShouldReturnSuccess() throws NoPermissionException
	{
		EssayRequest essayRequest = new EssayRequest("content");
		Essay essay = setupUserAndEssay();
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(essay.getUser()));
		when(essayDao.save(Mockito.any(Essay.class))).thenReturn(essay);
		
		ResponseEntity<String> response = essayEditorService.addEssay(essayRequest, authentication);
		
		assertAll(
				() -> assertEquals(response.getHeaders().get("Location").get(0), "essay/" + 10),
				() -> assertEquals(response.getBody(), "success"),
				() -> assertEquals(response.getStatusCode(), HttpStatus.CREATED)
				);
	}
	
	@Test
	void addEssayByNonAuthorizedUserShouldThrowNoPermissionException()
	{
		EssayRequest essayRequest = new EssayRequest("content");
		assertThrowsExactly(NoPermissionException.class, ()-> essayEditorService.addEssay(essayRequest, authentication) );	
	}
	
	@Test
	@WithMockUser
	void deleteExistingEssayByAuthorizedUserShouldReturnSuccess() throws NoSuchElementException, NoPermissionException
	{
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(true);
		Essay essay = setupUserAndEssay();
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		
		ResponseEntity<String> response = essayEditorService.deleteEssay(10, authentication);
		assertAll(
				() -> assertEquals(response.getBody(), "success"),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);
	}
	
	@Test
	@WithMockUser
	void deleteNonExistingEssayByAuthorizedUserShouldThrowNoSuchElementException() throws NoSuchElementException, NoPermissionException
	{
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(false);
		
		assertThrowsExactly(NoSuchElementException.class, ()-> essayEditorService.deleteEssay(10, authentication));	
	}
	
	@Test
	@WithMockUser
	void deleteExistingEssayByWrongUserShouldThrowNoPermissionException() throws NoSuchElementException, NoPermissionException
	{
		int id = 10;
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(true);
		User user = User.builder().username("anotherUser").build();
		Essay essay = Essay.builder().id(id).user(user).build();
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		
		assertThrowsExactly(NoPermissionException.class, ()-> essayEditorService.deleteEssay(10, authentication));	
	}
	
	@Test
	@WithMockUser(authorities = {"ADMIN"})
	void deleteExistingExistingEssayByAdminShouldReturnSuccess() throws NoSuchElementException, NoPermissionException
	{
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(true);
		Essay essay = setupUserAndEssay();
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		
		ResponseEntity<String> response = 
				essayEditorService.deleteEssay(10, authentication);
		assertAll(
				() -> assertEquals(response.getBody(), "success"),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);	
	}
	
	@Test
	void deleteExistingEssayByNonAuthorizedUserShouldThrowNoPermissionException() throws NoSuchElementException, NoPermissionException
	{
		assertThrowsExactly(NoPermissionException.class, ()-> essayEditorService.deleteEssay(10, authentication));	
	}
	
	@Test
	@WithMockUser
	void modifyExistingEssayByAuthorizedUserShouldReturnSuccess() throws NoSuchElementException, NoPermissionException
	{
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(true);
		Essay essay = setupUserAndEssay();
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(essayDao.save(Mockito.any(Essay.class))).thenReturn(essay);
		
		EssayRequest essayRequest = new EssayRequest("content");
		ResponseEntity<String> response = essayEditorService.modifyEssay(essayRequest, 10, authentication);
		
		assertAll(
				() -> assertEquals(response.getBody(), "success"),
				() -> assertEquals(response.getStatusCode(), HttpStatus.CREATED)
				);
	}
	
	@Test
	@WithMockUser
	void modifyExistingEssayByWrongAuthorizedUserShouldThrowNoPermissionException() throws NoSuchElementException, NoPermissionException
	{
		int id = 10;
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(true);
		User user = User.builder().username("anotherUser").build();
		Essay essay = Essay.builder().id(id).user(user).build();
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		
		EssayRequest essayRequest = new EssayRequest("content");
		
		assertThrowsExactly(NoPermissionException.class, ()-> essayEditorService.modifyEssay(essayRequest, 10, authentication));	
	}
	
	@Test
	void modifyExistingEssayByNonAuthorizedUserShouldThrowNoPermissionException() throws NoSuchElementException, NoPermissionException
	{
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(true);
		Essay essay = setupUserAndEssay();
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		
		EssayRequest essayRequest = new EssayRequest("content");
		
		assertThrowsExactly(NoPermissionException.class, ()-> essayEditorService.modifyEssay(essayRequest, 10, authentication));	
	}
	
	@Test
	@WithMockUser
	void modifyNonExistingEssayByAuthorizedUserShouldThrowNoSuchElementException() throws NoSuchElementException, NoPermissionException
	{
		when(essayDao.existsById(Mockito.anyInt())).thenReturn(false);
		
		EssayRequest essayRequest = new EssayRequest("content");
		
		assertThrowsExactly(NoSuchElementException.class, ()-> essayEditorService.modifyEssay(essayRequest, 10, authentication));	
	}
	
	private Essay setupUserAndEssay()
	{
		int id = 10;
		User user = User.builder().username("user").build();
		return Essay.builder().id(id).user(user).build();
	}
}
