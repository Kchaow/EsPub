package com.espub.unit.service;

import static org.mockito.Mockito.when;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

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

import com.espub.component.EssayPageSort;
import com.espub.dao.EssayDao;
import com.espub.model.Essay;
import com.espub.model.User;
import com.espub.service.EssayService;

@ExtendWith(MockitoExtension.class)
public class EssayServiceTest 
{
	@Mock
	private EssayDao essayDao;
	@InjectMocks
	private EssayService essayService;
	
	
	@Test
	void getEssayPageWithNoSortAndNoCategoryShouldReturnPage()
	{
		int essayCount = 10;
		PageImpl<Essay> pageImpl = new PageImpl<>(
				getListOfEssay(essayCount)
				);
		when(essayDao.findAll(Mockito.any(Pageable.class))).thenReturn(pageImpl);
		
		ResponseEntity<Page<Essay>> response = essayService.getEssayPage(0, 10, null, null);
		assertAll(
				() -> assertEquals(response.getBody().getTotalElements(), essayCount),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);
	}
	
	@Test
	void getEssayPageWithNoSortAndCategoryShouldReturnPage()
	{
		int essayCount = 10;
		PageImpl<Essay> pageImpl = new PageImpl<>(
				getListOfEssay(essayCount)
				);
		when(essayDao.findAllByCategoryName(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(pageImpl);
		
		ResponseEntity<Page<Essay>> response = essayService.getEssayPage(0, 10, null, "category");
		assertAll(
				() -> assertEquals(response.getBody().getTotalElements(), essayCount),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);
		
	}
	
	@Test
	void getEssayPageWithSortAndCategoryShouldReturnPage()
	{
		EssayPageSort essayPageSort = EssayPageSort.TIME;
		int essayCount = 10;
		PageImpl<Essay> pageImpl = new PageImpl<>(
				getListOfEssay(essayCount)
				);
		when(essayDao.findAllByCategoryName(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(pageImpl);
		
		ResponseEntity<Page<Essay>> response = essayService.getEssayPage(0, 10, essayPageSort, "category");
		assertAll(
				() -> assertEquals(response.getBody().getTotalElements(), essayCount),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);
	}
	
	@Test
	void getByIdShouldReturnEssay()
	{
		Essay essay = getListOfEssay(1).get(0);
		
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		
		ResponseEntity<Essay> response = essayService.getById(0);
		assertAll(
				() -> assertEquals(response.getBody(), essay),
				() -> assertEquals(response.getStatusCode(), HttpStatus.OK)
				);
	}
	
	@Test
	void getByUnknownIdShouldThrowNoSuchElementException()
	{
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		
		assertThrowsExactly(NoSuchElementException.class, () -> essayService.getById(0));
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
