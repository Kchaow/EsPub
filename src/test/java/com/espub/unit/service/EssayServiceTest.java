package com.espub.unit.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.espub.dao.EssayDao;
import com.espub.dao.HistoryDao;
import com.espub.dao.UserDao;
import com.espub.dto.EssayResponse;
import com.espub.dto.EssayResponsePage;
import com.espub.model.Essay;
import com.espub.model.History;
import com.espub.model.User;
import com.espub.service.EssayService;
import com.espub.util.EssayPageSort;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class EssayServiceTest 
{
	@Mock
	EssayDao essayDao;
	@Mock
	HistoryDao historyDao;
	@Mock
	UserDao userDao;
	@Mock
	HttpServletRequest httpServletRequest;
	@Mock
	Authentication authentication;
	@InjectMocks
	EssayService essayService;
	ZoneId zoneId = ZoneId.of("Europe/Moscow");
	
	@BeforeEach
	void setProperties()
	{
		ReflectionTestUtils.setField(essayService, "zoneId", zoneId);
		ReflectionTestUtils.setField(essayService, "viewCounterCooldown", 3600);
	}
	
	@Test
	void getEssayPageWithNoSortAndNoCategoryShouldReturnPage()
	{
		int essayCount = 10;
		PageImpl<Essay> pageImpl = new PageImpl<>(
				getListOfEssay(essayCount)
				);
		when(essayDao.findAll(Mockito.any(Pageable.class))).thenReturn(pageImpl);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponsePage> response = essayService.getEssayPage(0, 10, null, null, httpServletRequest);
		assertAll(
				() -> assertEquals(essayCount, response.getBody().getEssayResponseList().size()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
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
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponsePage> response = essayService.getEssayPage(0, 10, null, "category", httpServletRequest);
		assertAll(
				() -> assertEquals(essayCount, response.getBody().getEssayResponseList().size()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
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
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponsePage> response = essayService.getEssayPage(0, 10, essayPageSort, "category", httpServletRequest);
		assertAll(
				() -> assertEquals(essayCount, response.getBody().getEssayResponseList().size()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void getByIdShouldReturnEssay()
	{
		Essay essay = getListOfEssay(1).get(0);
		
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(authentication.isAuthenticated()).thenReturn(false);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponse> response = essayService.getById(0, authentication, httpServletRequest);
		assertAll(
				() -> assertEquals(essay.getPublicationDate(), response.getBody().getPublicationDate()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void getByUnknownIdShouldThrowNoSuchElementException()
	{
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		//when(authentication.isAuthenticated()).thenReturn(false);
		assertThrowsExactly(NoSuchElementException.class, () -> essayService.getById(0, authentication, httpServletRequest));
	}
	
	@Test
	void getNonModifiedEssayPageShouldReturnNotModified()
	{
		int essayCount = 10;
		PageImpl<Essay> pageImpl = new PageImpl<>(
				getListOfEssay(essayCount)
				);
		String pageImplHash = pageImpl.hashCode() + "";
		when(essayDao.findAll(Mockito.any(Pageable.class))).thenReturn(pageImpl);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(pageImplHash);
		
		ResponseEntity<EssayResponsePage> response = essayService.getEssayPage(0, 10, null, null, httpServletRequest);
		assertAll(
				() -> assertEquals(response.getBody(), null),
				() -> assertEquals(response.getStatusCode(), HttpStatus.NOT_MODIFIED)
				);
	}
	
	@Test
	void getModifiedEssayPageShouldReturnPage()
	{
		int essayCount = 10;
		PageImpl<Essay> pageImpl = new PageImpl<>(
				getListOfEssay(essayCount)
				);
		String pageImplHash = pageImpl.hashCode() + 456 + "";
		when(essayDao.findAll(Mockito.any(Pageable.class))).thenReturn(pageImpl);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(pageImplHash);
		
		ResponseEntity<EssayResponsePage> response = essayService.getEssayPage(0, 10, null, null, httpServletRequest);
		assertAll(
				() -> assertEquals(essayCount, response.getBody().getEssayResponseList().size()),
				() -> assertEquals( HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void getNonModifiedEssayShouldReturnNotModified()
	{
		Essay essay = getListOfEssay(1).get(0);
		String essayHash = essay.hashCode() + "";
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(essayHash);
		//when(authentication.isAuthenticated()).thenReturn(false);
		
		ResponseEntity<EssayResponse> response = essayService.getById(0, authentication, httpServletRequest);
		assertAll(
				() -> assertEquals(null, response.getBody()),
				() -> assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode())
				);
	}
	
	@Test
	void modifiedEssayShouldReturnEssay()
	{
		Essay essay = getListOfEssay(1).get(0);
		String essayHash = essay.hashCode() + 367 + "";
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(authentication.isAuthenticated()).thenReturn(false);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(essayHash);
		
		ResponseEntity<EssayResponse> response = essayService.getById(0, authentication,httpServletRequest);
		assertAll(
				() -> assertEquals(essay.getPublicationDate(), response.getBody().getPublicationDate()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void getEssayByAuthorizedFirstTimeShouldMakeHistoryAndIncreaseViews()
	{
		Essay essay = getListOfEssay(1).get(0);
		User user = new User();
		
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
		when(historyDao.findByUserAndEssay(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		when(historyDao.save(Mockito.any())).thenReturn(new History());
		when(authentication.getName()).thenReturn("user");
		when(authentication.isAuthenticated()).thenReturn(true);
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(essayDao.save(Mockito.any())).thenReturn(essay);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponse> response = essayService.getById(0, authentication, httpServletRequest);
		assertAll(
				() -> assertEquals(1, essay.getViews()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void getEssayByAuthorizedShouldIncreaseViews()
	{
		Essay essay = getListOfEssay(1).get(0);
		User user = new User();
		History history = History.builder()
				.lastValidViewDate(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, zoneId))
				.build();
		
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
		when(historyDao.findByUserAndEssay(Mockito.any(), Mockito.any())).thenReturn(Optional.of(history));
		when(historyDao.save(Mockito.any())).thenReturn(history);
		when(authentication.getName()).thenReturn("user");
		when(authentication.isAuthenticated()).thenReturn(true);
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(essayDao.save(Mockito.any())).thenReturn(essay);
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponse> response = essayService.getById(0, authentication, httpServletRequest);
		assertAll(
				() -> assertEquals(1, essay.getViews()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void getEssayByAuthorizedWhileCooldownShouldntIncreaseViews()
	{
		Essay essay = getListOfEssay(1).get(0);
		User user = new User();
		History history = History.builder()
				.lastValidViewDate(ZonedDateTime.now())
				.build();
		
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
		when(historyDao.findByUserAndEssay(Mockito.any(), Mockito.any())).thenReturn(Optional.of(history));
		when(historyDao.save(Mockito.any())).thenReturn(history);
		when(authentication.getName()).thenReturn("user");
		when(authentication.isAuthenticated()).thenReturn(true);
		when(essayDao.findById(Mockito.anyInt())).thenReturn(Optional.of(essay));
		when(httpServletRequest.getHeader("If-None-Match")).thenReturn(null);
		
		ResponseEntity<EssayResponse> response = essayService.getById(0, authentication, httpServletRequest);
		assertAll(
				() -> assertEquals(0, essay.getViews()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	private List<Essay> getListOfEssay(int size)
	{
		List<Essay> list = new ArrayList<>();
		for (int i = 0; i < size; i++)
		{
			Essay essay = Essay.builder()
					.content("" + i)
					.modificationDate(ZonedDateTime.now(
							zoneId))
					.publicationDate(ZonedDateTime.now(
							zoneId))
					.user(new User())
					.build();
			list.add(essay);
		}
		return list;
	}
	
	
	
}
