package com.espub.service;

import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.espub.dao.EssayDao;
import com.espub.dto.EssayResponse;
import com.espub.dto.EssayResponsePage;
import com.espub.model.Essay;
import com.espub.util.EssayPageSort;
import com.espub.util.EssayResponsePageWrapper;
import com.espub.util.EssayResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EssayService 
{
	@Autowired
	private EssayDao essayDao;
	
	public ResponseEntity<EssayResponsePage> getEssayPage(int offset, int limit, 
													EssayPageSort essayPageSort, 
													String category, 
													ZoneId zoneId,
													HttpServletRequest request)
	{
		PageRequest pageRequest;
		if (essayPageSort == null)
			pageRequest = PageRequest.of(offset, limit);
		else
			pageRequest = PageRequest.of(offset, limit, essayPageSort.getSortValue());
		
		Page<Essay> essayPage;
		if (category == null)
			essayPage = essayDao.findAll(pageRequest);
		else
			essayPage = essayDao.findAllByCategoryName(category, pageRequest);
		
		if (request.getHeader("If-None-Match") != null && request.getHeader("If-None-Match").equals(essayPage.hashCode() + ""))
			return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		
		EssayResponsePage essayResponsePage = EssayResponsePageWrapper.of(essayPage, zoneId);
		
		CacheControl cacheControl = CacheControl
				.noCache()
				.cachePublic();
		return ResponseEntity.status(HttpStatus.OK)
				.eTag(essayPage.hashCode() + "")
				.cacheControl(cacheControl)
				.body(essayResponsePage);
	}
	public ResponseEntity<EssayResponse> getById(int id,
										 ZoneId zoneId,
										 HttpServletRequest request) throws NoSuchElementException
	{
		Optional<Essay> essay = essayDao.findById(id);
		if (essay.isEmpty())
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		
		if (request.getHeader("If-None-Match") != null && request.getHeader("If-None-Match").equals(essay.hashCode() + ""))
			return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		
		EssayResponse essayResponse = EssayResponseWrapper.of(essay.get(), zoneId);
		CacheControl cacheControl = CacheControl
				.noCache()
				.cachePublic();
		return ResponseEntity.status(HttpStatus.OK)
				.eTag(essay.hashCode() + "")
				.cacheControl(cacheControl)
				.body(essayResponse);
				
	}
}
