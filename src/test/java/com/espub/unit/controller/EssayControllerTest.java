package com.espub.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.espub.controller.EssayController;
import com.espub.exception.ControllerExceptionHandler;
import com.espub.service.EssayService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled("Не реализовано")
//@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EssayControllerTest 
{	
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
	
//	@Test
//	void getEssayPageShouldReturnEssayResponsePage() throws JsonProcessingException, Exception
//	{
//		
//		int pageCount = 100;
//		EssayResponsePage essayResponsePage =
//				EssayResponsePage.builder()
//				.pageNumber(pageCount)
//				.build();
//		
//		when(essayService.getEssayPage(Mockito.anyInt(),
//									   Mockito.anyInt(),
//									   Mockito.any(EssayPageSort.class),
//									   Mockito.anyString(),
//									   Mockito.isNotNull(),
//									   Mockito.any(HttpServletRequest.class)))
//		.thenReturn(new ResponseEntity<>(essayResponsePage, HttpStatus.OK));
//		
//		ResultActions response = mockMvc.perform(get("/essay?offset=0&limit=10")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.createObjectNode().put("zoneId", "Asia/Vladivostok").asText()
//						)
//				);
//		
//		response.andExpect(MockMvcResultMatchers.status().isOk());
//		
//		response.andReturn().getResponse().getContentAsString()
//	}
	
//	@Test
//	void getAllEssayShouldReturnListEssay() throws Exception
//	{
//		Essay essay1 = Essay.builder().content("first content").build();
//		Essay essay2 = Essay.builder().content("second content").build();
//		List<Essay> list = new ArrayList<>();
//		list.add(essay1);
//		list.add(essay2);
//		when(essayService.getAll()).thenReturn(new ResponseEntity<List<Essay>>(list,HttpStatus.OK));
//		
//		ResultActions response = mockMvc.perform(get("/essay/all"));
//		
//		response.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$[0].content", CoreMatchers.is(essay1.getContent())))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[1].content", CoreMatchers.is(essay2.getContent())));
//	}
//	@Test
//	void getEssayByIdShouldReturnEssay() throws Exception
//	{
//		int id = 1;
//		Essay essay = Essay.builder().content("first content").build();
//		when(essayService.getById(id)).thenReturn(new ResponseEntity<Essay>(essay,HttpStatus.OK));
//		
//		ResultActions response = mockMvc.perform(get(String.format("/essay/%d", id)));
//		
//		response.andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(essay.getContent())));
//	}
	
	
}
