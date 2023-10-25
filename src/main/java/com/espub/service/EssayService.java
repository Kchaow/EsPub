package com.espub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.espub.dao.EssayDao;
import com.espub.model.Essay;

@Service
public class EssayService 
{
	@Autowired
	private EssayDao essayDao;
	
	public ResponseEntity<List<Essay>> getAll()
	{
		List<Essay> essay = essayDao.findAll();
		return new ResponseEntity<>(essay, HttpStatus.OK);
	}
	public ResponseEntity<Essay> getById(int id)
	{
		Optional<Essay> essay = essayDao.findById(id);
		if (!essay.isEmpty())
		{
			return new ResponseEntity<>(essay.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
}
