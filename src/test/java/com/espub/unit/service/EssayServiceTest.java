package com.espub.unit.service;

import static org.mockito.Mockito.when;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.espub.dao.EssayDao;
import com.espub.model.Essay;
import com.espub.model.User;
import com.espub.service.EssayService;


//Нихуя недоделано
@ExtendWith(MockitoExtension.class)
public class EssayServiceTest 
{
	@Mock
	private EssayDao essayDao;
	@InjectMocks
	private EssayService essayService;
	
	@Test
	void getEssayPageShouldReturnResponseEntityWithPage()
	{
		List<Essay> list = new ArrayList<>();
		PageImpl<Essay> pageImpl = new PageImpl<>(list);
		when(essayDao.findAll(Mockito.any(Pageable.class))).thenReturn(pageImpl);
		
		ResponseEntity<Page<Essay>> response = essayService.getEssayPage(0, 10,null, null);
		assertAll(
				() -> assertEquals(response.getBody(), list),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);
	}
	@Test
	void getByIdShouldReturnResponseEntityWithEssay()
	{
		int existingId = 1;
		int nonExistentId = 2;
		Essay essay = Essay.builder()
				.id(1)
				.content("Content of first essay")
				.modificationDate(new GregorianCalendar(2023, 9, 12))
				.publicationDate(new GregorianCalendar(2021, 4, 9))
				.user(new User())
				.build();
		
		when(essayDao.findById(existingId)).thenReturn(Optional.of(essay));
		when(essayDao.findById(nonExistentId)).thenReturn(Optional.empty());
		
		ResponseEntity<Essay> existingIdResponse = essayService.getById(existingId);
		ResponseEntity<Essay> nonExistingResponse = essayService.getById(nonExistentId);
		
		assertAll(
				() -> assertEquals(existingIdResponse.getBody(), essay),
				() -> assertEquals(existingIdResponse.getStatusCode(), HttpStatus.OK)
				);
		assertAll(
				() -> assertEquals(nonExistingResponse.getBody(), null),
				() -> assertEquals(nonExistingResponse.getStatusCode(), HttpStatus.BAD_REQUEST)
				);
		
	}
	
	private List<Essay> getListOfEssay(int size)
	{
		List<Essay> list = new ArrayList<>();
		for (int i = 0; i < size; i++)
		{
			Essay essay = Essay.builder()
					.content("" + i)
					.modificationDate(new GregorianCalendar(2023, 9, 12))
					.publicationDate(new GregorianCalendar(2021, 4, 9))
					.user(new User())
					.build();
			list.add(essay);
		}
		return list;
	}
	
}
