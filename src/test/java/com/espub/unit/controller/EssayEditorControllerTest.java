package com.espub.unit.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.espub.controller.EssayEditorController;
import com.espub.dao.RoleDao;
import com.espub.dao.UserDao;
import com.espub.dto.EssayRequest;
import com.espub.exception.ControllerExceptionHandler;
import com.espub.exception.NoPermissionException;
import com.espub.model.Essay;
import com.espub.model.Role;
import com.espub.model.User;
import com.espub.service.AuthenticationService;
import com.espub.service.EssayEditorService;
import com.espub.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled("Я хуй знает")
//@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EssayEditorControllerTest
{
	
	//@Autowired
	MockMvc mockMvc;
//	@Autowired
//	@Autowired
//	ControllerExceptionHandler controllerExceptionHandler;
	@Autowired
    ObjectMapper objectMapper;
	@Autowired
	RoleDao roleDao;
	@Autowired
	UserDao userDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	JwtService jwtService;
	
	RestTemplate restTemplate;
	
	@Mock
	EssayEditorService essayEditorService;
	@Mock
	EssayEditorController essayEditorController;
	
//	@InjectMocks
//	EssayEditorController EssayEditorController;
	
	EssayRequest essayRequest = new EssayRequest("content");
	EssayRequest badEssayRequest;
	
	Authentication authentication;
	
//	@BeforeAll
//	void setMockMvc()
//	{
//		mockMvc = MockMvcBuilders.standaloneSetup(essayEditorController)
//	            .setControllerAdvice(controllerExceptionHandler)
//	            .build();
//	}
//	
	@BeforeEach
	void setAuthentication()
	{
		authentication = SecurityContextHolder.getContext().getAuthentication();
		mockMvc = MockMvcBuilders.standaloneSetup(essayEditorController)
		         .setControllerAdvice(new ControllerExceptionHandler())
		        .build();
	}
	
//	@Test
//	@WithMockUser
//	void addNewEssayByAuthorizedUserShouldReturnCreated() throws Exception
//	{
//		when(essayEditorController.addNewEssay(Mockito.any(EssayRequest.class), Mockito.any(Authentication.class));
//		ResultActions response = mockMvc.perform(post("/essay")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(essayRequest)));
//		response.andExpect(
//				MockMvcResultMatchers.status().isCreated()
//				);
//
//	}
	
//	@Test
//	@WithMockUser
//	void addNewEmptyEssayByAuthorizedUserShouldReturnBadRequest() throws Exception
//	{
//		given(essayEditorService.addEssay(ArgumentMatchers.any(EssayRequest.class),
//				ArgumentMatchers.any(Authentication.class)))
//			.willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
//		ResultActions response = mockMvc.perform(post("/essay")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(badEssayRequest)));
//		response.andExpect(
//				MockMvcResultMatchers.status().isBadRequest()
//				);
//	}
//	
//	@Test
//	void addNewEssayByNonAuthorizedUserShouldReturnForbidden() throws Exception
//	{
//		when(essayEditorService.addEssay(Mockito.any(EssayRequest.class), 
//				Mockito.any(Authentication.class))).thenThrow(NoPermissionException.class);
//		ResultActions response = mockMvc.perform(post("/essay")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(essayRequest)));
//		response.andExpect(
//				MockMvcResultMatchers.status().isForbidden()
//				);
//	}
	
	
	
//	@Nested
//	class RequestByAdmin
//	{
//		String token;
//		@BeforeEach
//		public void setUpDatabase()
//		{
//			String name = "admin";
//			String password = "password";
//			Role role = Role.builder()
//					.name("ADMIN")
//					.build();
//			roleDao.save(role);
//			List<Role> roleList = new ArrayList<>();
//			roleList.add(role);
//			User user = User.builder()
//					.username(name)
//					.password(passwordEncoder.encode(password))
//					.role(roleList)
//					.build();
//			userDao.save(user);
//			token = jwtService.generateToken(user);
//		}
//		
//		@AfterEach
//		public void clearDatabase()
//		{
//			userDao.deleteAll(); //Какого хуя
//			roleDao.deleteAll(); //блять
//		}
//		
//		
//		
//		@Test
//		void addNewEssayByAdminShouldReturnSuccess() throws Exception
//		{
//			Essay essay = new Essay();
//			
//			given(essayEditorService.addEssay(ArgumentMatchers.any())).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
//			
//			ResultActions response = mockMvc.perform(post("/essay/edit/add")
//					.header("Authorization", "Bearer " + token)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(essay)));
//			
//			response.andExpect(MockMvcResultMatchers.status().isCreated());
//		}
//		@Test
//		void deleteEssayByAdminShouldReturnSuccess() throws Exception
//		{
//			int existingId = 1;
//			int nonExistedId = 2;
//			given(essayEditorService.deleteEssay(existingId)).willReturn(new ResponseEntity<String>("success", HttpStatus.OK));
//			given(essayEditorService.deleteEssay(nonExistedId)).willReturn(new ResponseEntity<String>("success", HttpStatus.BAD_REQUEST));
//			
//			ResultActions responseExisting = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", existingId)).header("Authorization", "Bearer " + token));
//			ResultActions responseNonExisted = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", nonExistedId)).header("Authorization", "Bearer " + token));	
//			
//			responseExisting.andExpect(MockMvcResultMatchers.status().isOk());
//			responseNonExisted.andExpect(MockMvcResultMatchers.status().isBadRequest());
//		}
//		@Test
//		void updateEssayByAdminShouldReturnSuccess() throws Exception
//		{
//			Essay existingEssay = Essay.builder().content("first content").build();
//			Essay nonExistedEssay = Essay.builder().content("second content").build();
//			given(essayEditorService.updateEssay(existingEssay)).willReturn(new ResponseEntity<String>("The resource doesn't exist yet", HttpStatus.BAD_REQUEST));
//			given(essayEditorService.updateEssay(nonExistedEssay)).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
//			
//			ResultActions responseExisting = mockMvc.perform(put("/essay/edit/update")
//					.header("Authorization", "Bearer " + token)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(existingEssay)));
//			ResultActions responseNonExisted = mockMvc.perform(put("/essay/edit/update")
//					.header("Authorization", "Bearer " + token)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(nonExistedEssay)));
//			
//			responseExisting.andExpect(MockMvcResultMatchers.status().isBadRequest());
//			responseNonExisted.andExpect(MockMvcResultMatchers.status().isCreated());
//		}
//		
//	}
	
//	@Nested
//	class RequestByUser
//	{
//		String token;
//		@BeforeEach
//		public void setUpDatabase()
//		{
//			String name = "user";
//			String password = "password";
//			Role role = Role.builder()
//					.name("USER")
//					.build();
//			roleDao.save(role);
//			List<Role> roleList = new ArrayList<>();
//			roleList.add(role);
//			User user = User.builder()
//					.username(name)
//					.password(passwordEncoder.encode(password))
//					.role(roleList)
//					.build();
//			userDao.save(user);
//			token = jwtService.generateToken(user);
//		}
//		
//		@AfterEach
//		public void clearDatabase()
//		{
//			userDao.deleteAll(); //Какого хуя
//			roleDao.deleteAll(); //блять
//		}
		
//		@Test
//		void addNewEssayByUserShouldReturnSuccess() throws Exception
//		{
//			Essay essay = new Essay();
//			
//			given(essayEditorService.addEssay(ArgumentMatchers.any())).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
//			
//			ResultActions response = mockMvc.perform(post("/essay/edit/add")
//					.header("Authorization", "Bearer " + token)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(essay)));
//			
//			response.andExpect(MockMvcResultMatchers.status().isForbidden());
//		}
//		@Test
//		void deleteEssayByUserShouldReturnSuccess() throws Exception
//		{
//			int existingId = 1;
//			int nonExistedId = 2;
//			given(essayEditorService.deleteEssay(existingId)).willReturn(new ResponseEntity<String>("success", HttpStatus.OK));
//			given(essayEditorService.deleteEssay(nonExistedId)).willReturn(new ResponseEntity<String>("success", HttpStatus.BAD_REQUEST));
//			
//			ResultActions responseExisting = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", existingId)).header("Authorization", "Bearer " + token));
//			ResultActions responseNonExisted = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", nonExistedId)).header("Authorization", "Bearer " + token));	
//			
//			responseExisting.andExpect(MockMvcResultMatchers.status().isForbidden());
//			responseNonExisted.andExpect(MockMvcResultMatchers.status().isForbidden());
//		}
//		@Test
//		void updateEssayByUserShouldReturnSuccess() throws Exception
//		{
//			Essay existingEssay = Essay.builder().content("first content").build();
//			Essay nonExistedEssay = Essay.builder().content("second content").build();
//			given(essayEditorService.updateEssay(existingEssay)).willReturn(new ResponseEntity<String>("The resource doesn't exist yet", HttpStatus.BAD_REQUEST));
//			given(essayEditorService.updateEssay(nonExistedEssay)).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
//			
//			ResultActions responseExisting = mockMvc.perform(put("/essay/edit/update")
//					.header("Authorization", "Bearer " + token)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(existingEssay)));
//			ResultActions responseNonExisted = mockMvc.perform(put("/essay/edit/update")
//					.header("Authorization", "Bearer " + token)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(nonExistedEssay)));
//			
//			responseExisting.andExpect(MockMvcResultMatchers.status().isForbidden());
//			responseNonExisted.andExpect(MockMvcResultMatchers.status().isForbidden());
//		}
//	}
	

}
