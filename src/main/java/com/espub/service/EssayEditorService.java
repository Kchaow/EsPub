package com.espub.service;

import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.espub.dao.EssayDao;
import com.espub.dto.EssayRequest;
import com.espub.exception.NoPermissionException;
import com.espub.model.Essay;

@Service
public class EssayEditorService 
{
	@Autowired
	private EssayDao essayDao;
	@Autowired
	private Authentication authentication;
	private Logger logger = LoggerFactory.getLogger(EssayEditorService.class);
	
	public ResponseEntity<String> addEssay(Essay essay)
	{
		Essay newEssay = essayDao.save(essay);
		logger.debug("New essay was added: {}", newEssay.getId());
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
	public ResponseEntity<String> deleteEssay(int id) throws NoSuchElementException
	{
		if (!essayDao.existsById(id))
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		essayDao.deleteById(id);
		logger.debug("Essay with id {} was deleted", id);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	public ResponseEntity<String> modifyEssay(EssayRequest essay, int id) throws NoSuchElementException, NoPermissionException
	{
		if (!essayDao.existsById(id))
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		Essay originalEssay = essayDao.findById(id).get();
		if (authentication.getName() != originalEssay.getUser().getUsername())
			throw new NoPermissionException();
		originalEssay.setContent(essay.getContent());
		originalEssay.setModificationDate(new GregorianCalendar());
		Essay newEssay = essayDao.save(originalEssay);
		logger.debug("Essay was modified: {}", newEssay.getId());
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
}
