package com.espub.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.ResponseEntity;

import com.espub.controller.EssayController;
import com.espub.model.Essay;
import com.espub.service.EssayService;

@WebMvcTest(controllers = EssayController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EssayControllerTest 
{
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	EssayService essayService;
	
	@Test
	void getAllEssayShouldReturnListEssay() throws Exception
	{
		Essay essay1 = Essay.builder().content("first content").build();
		Essay essay2 = Essay.builder().content("second content").build();
		List<Essay> list = new ArrayList<>();
		list.add(essay1);
		list.add(essay2);
		when(essayService.getAll()).thenReturn(new ResponseEntity<List<Essay>>(list,HttpStatus.OK));
		
		ResultActions response = mockMvc.perform(get("/essay/all"));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].content", CoreMatchers.is(essay1.getContent())))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].content", CoreMatchers.is(essay2.getContent())));
	}
	@Test
	void getEssayByIdShouldReturnEssay() throws Exception
	{
		int id = 1;
		Essay essay = Essay.builder().content("first content").build();
		when(essayService.getById(id)).thenReturn(new ResponseEntity<Essay>(essay,HttpStatus.OK));
		
		ResultActions response = mockMvc.perform(get(String.format("/essay/%d", id)));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(essay.getContent())));
	}
}
