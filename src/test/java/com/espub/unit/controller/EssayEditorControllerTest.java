package com.espub.unit.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.espub.controller.EssayEditorController;
import com.espub.model.Essay;
import com.espub.service.EssayEditorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EssayEditorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EssayEditorControllerTest 
{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	EssayEditorService essayEditorService;
	
	@Test
	void addNewEssayShouldReturnSuccess() throws Exception
	{
		Essay essay = new Essay();
		
		given(essayEditorService.addEssay(ArgumentMatchers.any())).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
		
		ResultActions response = mockMvc.perform(post("/essay/edit/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(essay)));
		
		response.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	@Test
	void deleteEssayShouldReturnSuccess() throws Exception
	{
		int existingId = 1;
		int nonExistedId = 2;
		given(essayEditorService.deleteEssay(existingId)).willReturn(new ResponseEntity<String>("success", HttpStatus.OK));
		given(essayEditorService.deleteEssay(nonExistedId)).willReturn(new ResponseEntity<String>("success", HttpStatus.BAD_REQUEST));
		
		ResultActions responseExisting = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", existingId)));
		ResultActions responseNonExisted = mockMvc.perform(delete(String.format("/essay/edit/delete/%d", nonExistedId)));	
		
		responseExisting.andExpect(MockMvcResultMatchers.status().isOk());
		responseNonExisted.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	@Test
	void updateEssayShouldReturnSuccess() throws Exception
	{
		Essay existingEssay = Essay.builder().content("first content").build();
		Essay nonExistedEssay = Essay.builder().content("second content").build();
		given(essayEditorService.updateEssay(existingEssay)).willReturn(new ResponseEntity<String>("The resource doesn't exist yet", HttpStatus.BAD_REQUEST));
		given(essayEditorService.updateEssay(nonExistedEssay)).willReturn(new ResponseEntity<String>("success", HttpStatus.CREATED));
		
		ResultActions responseExisting = mockMvc.perform(put("/essay/edit/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(existingEssay)));
		ResultActions responseNonExisted = mockMvc.perform(put("/essay/edit/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(nonExistedEssay)));
		
		responseExisting.andExpect(MockMvcResultMatchers.status().isBadRequest());
		responseNonExisted.andExpect(MockMvcResultMatchers.status().isCreated());
	}
}
