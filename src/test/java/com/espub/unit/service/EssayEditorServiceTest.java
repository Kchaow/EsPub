package com.espub.unit.service;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.espub.dao.EssayDao;
import com.espub.model.Essay;
import com.espub.service.EssayEditorService;

@ExtendWith(MockitoExtension.class)
public class EssayEditorServiceTest 
{
	@Mock
	private EssayDao essayDao;
	@InjectMocks
	EssayEditorService essayEditorService;
	
	@Test
	void addEssayShouldReturnResponseEntity()
	{
		when(essayDao.save(Mockito.any(Essay.class))).thenReturn(new Essay());
		
		ResponseEntity<String> response = essayEditorService.addEssay(new Essay());
		assertAll(
				() -> assertEquals(response.getBody(), "success"),
				() -> assertEquals(response.getStatusCode(), HttpStatus.CREATED)
				);
	}
	@Test
	void deleteEssayShouldReturnResponseEntity()
	{
		int existingId = 1;
		int nonExistingId = 2;
		when(essayDao.existsById(existingId)).thenReturn(true);
		when(essayDao.existsById(nonExistingId)).thenReturn(false);
		
		ResponseEntity<String> responseExistingId = essayEditorService.deleteEssay(existingId);
		ResponseEntity<String> responseNonExistingId = essayEditorService.deleteEssay(nonExistingId);
		
		assertAll(
				() -> assertEquals(responseExistingId.getBody(), "success"),
				() -> assertEquals(responseExistingId.getStatusCode(), HttpStatus.OK)
				);
		assertAll(
				() -> assertEquals(responseNonExistingId.getBody(), String.format("Element with %d id doesn't exist", nonExistingId)),
				() -> assertEquals(responseNonExistingId.getStatusCode(), HttpStatus.BAD_REQUEST)
				);
	}
	@Test
	void updateEssayShouldReturnResponseEntity()
	{
		Essay existingEssay = Essay.builder().id(1).build();
		Essay nonExistingEssay = Essay.builder().id(2).build();
		when(essayDao.existsById(existingEssay.getId())).thenReturn(true);
		when(essayDao.existsById(nonExistingEssay.getId())).thenReturn(false);
		
		ResponseEntity<String> responseExistingId = essayEditorService.updateEssay(existingEssay);
		ResponseEntity<String> responseNonExistingId = essayEditorService.updateEssay(nonExistingEssay);
		
		assertAll(
				() -> assertEquals(responseExistingId.getBody(), "success"),
				() -> assertEquals(responseExistingId.getStatusCode(), HttpStatus.CREATED)
				);
		assertAll(
				() -> assertEquals(responseNonExistingId.getBody(), "The resource doesn't exist yet"),
				() -> assertEquals(responseNonExistingId.getStatusCode(), HttpStatus.BAD_REQUEST)
				);
	}
}
