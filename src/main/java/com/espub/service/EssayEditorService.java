package com.espub.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.espub.dao.EssayDao;
import com.espub.model.Essay;

@Service
public class EssayEditorService 
{
	@Autowired
	private EssayDao essayDao;
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
	public ResponseEntity<String> updateEssay(Essay essay)
	{
		Essay newEssay = essayDao.save(essay);
		logger.debug("New essay was modified: {}", newEssay.getId());
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
}
