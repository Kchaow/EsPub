package com.espub.service;

import java.nio.file.NoSuchFileException;

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
	
	public ResponseEntity<String> addEssay(Essay essay)
	{
		essayDao.save(essay);
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
	public ResponseEntity<String> deleteEssay(int id)
	{
		try 
		{
			if (!essayDao.existsById(id))
				throw new NoSuchFileException(String.format("Element with %d id doesn't exist", id));
			essayDao.deleteById(id);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		catch (NoSuchFileException e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(String.format("Element with %d id doesn't exist", id), HttpStatus.BAD_REQUEST);
		}
	}
	public ResponseEntity<String> updateEssay(Essay essay)
	{
		if (essayDao.existsById(essay.getId()))
		{
			essayDao.save(essay);
			return new ResponseEntity<>("success", HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("The resource doesn't exist yet", HttpStatus.BAD_REQUEST);
	}
}
