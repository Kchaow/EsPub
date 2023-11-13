package com.espub.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.espub.component.EssayPageSort;
import com.espub.model.Essay;
import com.espub.service.EssayService;

import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("essay")
public class EssayController 
{
	@Autowired
	EssayService essayService;
	Logger logger = LoggerFactory.getLogger(EssayController.class);
	
	@GetMapping
	public ResponseEntity<Page<Essay>> getEssayPage(@RequestParam @Min(0) int offset, 
												   @RequestParam @Min(1) int limit,
												   @RequestParam(required = false) EssayPageSort essayPageSort,
												   @RequestParam(required = false) String category)
	{
		logger.debug("GetEssayPage method from EssayController received a request");
		return essayService.getEssayPage(offset, limit, essayPageSort, category);
	}
	@GetMapping("{id}")
	public ResponseEntity<Essay> getEssay(@PathVariable int id) throws NoSuchElementException
	{
		logger.debug("GetEssay method from EssayController received a request");
		return essayService.getById(id);
	}
}
