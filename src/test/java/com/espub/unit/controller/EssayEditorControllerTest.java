package com.espub.unit.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.espub.dao.RoleDao;
import com.espub.dao.UserDao;
import com.espub.model.Essay;
import com.espub.model.Role;
import com.espub.model.User;
import com.espub.service.AuthenticationService;
import com.espub.service.EssayEditorService;
import com.espub.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EssayEditorControllerTest
{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private ObjectMapper objectMapper;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	JwtService jwtService;
	
	@MockBean
	EssayEditorService essayEditorService;
	
	
	@Nested
	class RequestByAdmin
	{
		String token;
		@BeforeEach
		public void setUpDatabase()
		{
			String name = "admin";
			String password = "password";
			Role role = Role.builder()
					.name("ADMIN")
					.build();
			roleDao.save(role);
			List<Role> roleList = new ArrayList<>();
			roleList.add(role);
			User user = User.builder()
					.username(name)
					.password(passwordEncoder.encode(password))
					.role(roleList)
					.build();
			userDao.save(user);
			token = jwtService.generateToken(user);
		}
		
		@AfterEach
		public void clearDatabase()
		{
			userDao.deleteAll(); //Какого хуя
			roleDao.deleteAll(); //блять
		}
		
		@Test
		void addNewEssayByAdminShouldReturnSuccess() throws Exception
		{
			Essay essay = new Essay();
			
			given(essayEditorService.addEssay(ArgumentMatchers.any())).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
			
			ResultActions response = mockMvc.perform(post("/essay/edit/add")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(essay)));
			
			response.andExpect(MockMvcResultMatchers.status().isCreated());
		}
		@Test
		void deleteEssayByAdminShouldReturnSuccess() throws Exception
		{
			int existingId = 1;
			int nonExistedId = 2;
			given(essayEditorService.deleteEssay(existingId)).willReturn(new ResponseEntity<String>("success", HttpStatus.OK));
			given(essayEditorService.deleteEssay(nonExistedId)).willReturn(new ResponseEntity<String>("success", HttpStatus.BAD_REQUEST));
			
			ResultActions responseExisting = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", existingId)).header("Authorization", "Bearer " + token));
			ResultActions responseNonExisted = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", nonExistedId)).header("Authorization", "Bearer " + token));	
			
			responseExisting.andExpect(MockMvcResultMatchers.status().isOk());
			responseNonExisted.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
		@Test
		void updateEssayByAdminShouldReturnSuccess() throws Exception
		{
			Essay existingEssay = Essay.builder().content("first content").build();
			Essay nonExistedEssay = Essay.builder().content("second content").build();
			given(essayEditorService.updateEssay(existingEssay)).willReturn(new ResponseEntity<String>("The resource doesn't exist yet", HttpStatus.BAD_REQUEST));
			given(essayEditorService.updateEssay(nonExistedEssay)).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
			
			ResultActions responseExisting = mockMvc.perform(put("/essay/edit/update")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(existingEssay)));
			ResultActions responseNonExisted = mockMvc.perform(put("/essay/edit/update")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(nonExistedEssay)));
			
			responseExisting.andExpect(MockMvcResultMatchers.status().isBadRequest());
			responseNonExisted.andExpect(MockMvcResultMatchers.status().isCreated());
		}
		
	}
	
	@Nested
	class RequestByUser
	{
		String token;
		@BeforeEach
		public void setUpDatabase()
		{
			String name = "user";
			String password = "password";
			Role role = Role.builder()
					.name("USER")
					.build();
			roleDao.save(role);
			List<Role> roleList = new ArrayList<>();
			roleList.add(role);
			User user = User.builder()
					.username(name)
					.password(passwordEncoder.encode(password))
					.role(roleList)
					.build();
			userDao.save(user);
			token = jwtService.generateToken(user);
		}
		
		@AfterEach
		public void clearDatabase()
		{
			userDao.deleteAll(); //Какого хуя
			roleDao.deleteAll(); //блять
		}
		
		@Test
		void addNewEssayByUserShouldReturnSuccess() throws Exception
		{
			Essay essay = new Essay();
			
			given(essayEditorService.addEssay(ArgumentMatchers.any())).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
			
			ResultActions response = mockMvc.perform(post("/essay/edit/add")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(essay)));
			
			response.andExpect(MockMvcResultMatchers.status().isForbidden());
		}
		@Test
		void deleteEssayByUserShouldReturnSuccess() throws Exception
		{
			int existingId = 1;
			int nonExistedId = 2;
			given(essayEditorService.deleteEssay(existingId)).willReturn(new ResponseEntity<String>("success", HttpStatus.OK));
			given(essayEditorService.deleteEssay(nonExistedId)).willReturn(new ResponseEntity<String>("success", HttpStatus.BAD_REQUEST));
			
			ResultActions responseExisting = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", existingId)).header("Authorization", "Bearer " + token));
			ResultActions responseNonExisted = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", nonExistedId)).header("Authorization", "Bearer " + token));	
			
			responseExisting.andExpect(MockMvcResultMatchers.status().isForbidden());
			responseNonExisted.andExpect(MockMvcResultMatchers.status().isForbidden());
		}
		@Test
		void updateEssayByUserShouldReturnSuccess() throws Exception
		{
			Essay existingEssay = Essay.builder().content("first content").build();
			Essay nonExistedEssay = Essay.builder().content("second content").build();
			given(essayEditorService.updateEssay(existingEssay)).willReturn(new ResponseEntity<String>("The resource doesn't exist yet", HttpStatus.BAD_REQUEST));
			given(essayEditorService.updateEssay(nonExistedEssay)).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
			
			ResultActions responseExisting = mockMvc.perform(put("/essay/edit/update")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(existingEssay)));
			ResultActions responseNonExisted = mockMvc.perform(put("/essay/edit/update")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(nonExistedEssay)));
			
			responseExisting.andExpect(MockMvcResultMatchers.status().isForbidden());
			responseNonExisted.andExpect(MockMvcResultMatchers.status().isForbidden());
		}
	}
	

}
