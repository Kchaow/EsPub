package com.espub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espub.model.Essay;
import com.espub.service.EssayEditorService;

@RestController
@RequestMapping("essay/edit")
public class EssayEditorController 
{
	@Autowired
	EssayEditorService essayEditorService;
	
	@GetMapping("add")
	public ResponseEntity<String> addNewEssay(@RequestBody Essay essay)
	{
		return essayEditorService.addEssay(essay);
	}
	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> deleteEssay(@PathVariable int id)
	{
		return essayEditorService.deleteEssay(id);
	}
	@PutMapping("update")
	public ResponseEntity<String> updateEssay(@RequestBody Essay essay)
	{
		return essayEditorService.updateEssay(essay);
	}
	
}
