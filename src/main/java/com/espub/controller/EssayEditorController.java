package com.espub.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espub.model.Essay;
import com.espub.service.EssayEditorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("essay/edit")
public class EssayEditorController 
{
	@Autowired
	EssayEditorService essayEditorService;
	Logger logger = LoggerFactory.getLogger(EssayEditorController.class);
	
	@PostMapping("add")
	public ResponseEntity<String> addNewEssay(@RequestBody @Valid Essay essay)
	{
		logger.debug("AddNewEssay method from EssayEditorController received a request");
		return essayEditorService.addEssay(essay);
	}
	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> deleteEssay(@PathVariable int id) throws NoSuchElementException
	{
		logger.debug("DeleteEssay method from EssayEditorController received a request");
		return essayEditorService.deleteEssay(id);
	}
	@PutMapping("update")
	public ResponseEntity<String> updateEssay(@RequestBody Essay essay)
	{
		logger.debug("UpdateEssay method from EssayEditorController received a request");
		return essayEditorService.updateEssay(essay);
	}
	
}
