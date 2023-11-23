package com.espub.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.espub.controller.EssayController;
import com.espub.dto.EssayResponsePage;
import com.espub.exception.ControllerExceptionHandler;
import com.espub.service.EssayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EssayControllerTest 
{	
	Logger logger = LoggerFactory.getLogger(EssayControllerTest.class);
	MockMvc mockMvc;
	@Autowired
	EssayController essayController;
	@Autowired
    ObjectMapper objectMapper;	
	@MockBean
	EssayService essayService;
	
	@BeforeEach
	void setupMockMvc()
	{
		mockMvc = MockMvcBuilders.standaloneSetup(essayController)
	            .setControllerAdvice(new ControllerExceptionHandler())
	            .build();
	}
	
	@Test
	void getEssayPageShouldReturnEssayResponsePage() throws JsonProcessingException, Exception
	{
		
		int pageCount = 100;
		EssayResponsePage essayResponsePage =
				EssayResponsePage.builder()
				.pageNumber(pageCount)
				
				.build();
		
		when(essayService.getEssayPage(Mockito.anyInt(),
									   Mockito.anyInt(),
									   Mockito.any(),
									   Mockito.any(),
									   Mockito.any(HttpServletRequest.class)))
		.thenReturn(new ResponseEntity<>(essayResponsePage, HttpStatus.OK));
		
		ResultActions response = mockMvc.perform(get("/essay?offset=0&limit=10")
				.header("ZoneId", "Asia/Vladivostok")
				);
		
		logger.info(response.andReturn().getResponse().getContentAsString());
		EssayResponsePage responseObject = objectMapper.readValue(
				response.andReturn().getResponse().getContentAsString(), 
				EssayResponsePage.class
				);
		
		response.andExpect(MockMvcResultMatchers.status().isOk());
		assertEquals(essayResponsePage.getPageNumber(), responseObject.getPageNumber());
	}
}
