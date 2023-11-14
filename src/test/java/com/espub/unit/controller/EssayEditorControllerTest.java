package com.espub.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import com.espub.controller.EssayEditorController;
import com.espub.dao.RoleDao;
import com.espub.dao.UserDao;
import com.espub.dto.EssayRequest;
import com.espub.exception.ControllerExceptionHandler;
import com.espub.service.AuthenticationService;
import com.espub.service.EssayEditorService;
import com.espub.service.JwtService;
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
