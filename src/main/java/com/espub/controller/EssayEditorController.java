package com.espub.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espub.dto.EssayRequest;
import com.espub.exception.NoPermissionException;
import com.espub.model.Essay;
import com.espub.service.EssayEditorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("essay")
public class EssayEditorController 
{
	@Autowired
	EssayEditorService essayEditorService;
	Logger logger = LoggerFactory.getLogger(EssayEditorController.class);
	
	@PostMapping
	public ResponseEntity<String> addNewEssay(@RequestBody @Valid EssayRequest essay)
	{
		logger.debug("AddNewEssay method from EssayEditorController received a request");
		return essayEditorService.addEssay(essay);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEssay(@PathVariable int id) throws NoSuchElementException, NoPermissionException
	{
		logger.debug("DeleteEssay method from EssayEditorController received a request");
		return essayEditorService.deleteEssay(id);
	}
	@PatchMapping("{id}")
	public ResponseEntity<String> updateEssay(@RequestBody @Valid EssayRequest essay, @PathVariable int id) throws NoSuchElementException, NoPermissionException
	{
		logger.debug("UpdateEssay method from EssayEditorController received a request");
		return essayEditorService.modifyEssay(essay, id);
	}
	
}
