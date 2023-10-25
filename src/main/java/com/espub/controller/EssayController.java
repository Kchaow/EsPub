package com.espub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("all")
	public ResponseEntity<List<Essay>> getAllEssay()
	{
		return essayService.getAll();
	}
	@GetMapping("{id}")
	public ResponseEntity<Essay> getEssay(int id)
	{
		return essayService.getById(id);
	}
}
