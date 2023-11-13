package com.espub.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espub.model.Essay;
import com.espub.service.EssayService;

@RestController
@RequestMapping("essay")
public class EssayController 
{
	@Autowired
	EssayService essayService;
	Logger logger = LoggerFactory.getLogger(EssayController.class);
	
	@GetMapping("all")
	public ResponseEntity<List<Essay>> getAllEssay()
	{
		logger.debug("GetAllEssay method from EssayController received a request");
		return essayService.getAll();
	}
	@GetMapping("{id}")
	public ResponseEntity<Essay> getEssay(@PathVariable int id) throws NoSuchElementException
	{
		logger.debug("GetEssay method from EssayController received a request");
		return essayService.getById(id);
	}
}
