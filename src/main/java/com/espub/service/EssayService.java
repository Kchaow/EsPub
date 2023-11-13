package com.espub.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.espub.component.EssayPageSort;
import com.espub.dao.EssayDao;
import com.espub.model.Essay;

@Service
public class EssayService 
{
	@Autowired
	private EssayDao essayDao;
	
	public ResponseEntity<Page<Essay>> getEssayPage(int offset, int limit, EssayPageSort essayPageSort, String category)
	{
		PageRequest pageRequest;
		if (essayPageSort == null)
			pageRequest = PageRequest.of(offset, limit);
		else
			pageRequest = PageRequest.of(offset, limit, essayPageSort.getSortValue());
		Page<Essay> essay;
		if (category == null)
			essay = essayDao.findAll(pageRequest);
		else
			essay = essayDao.findAllByCategoryName(category, pageRequest);
		return new ResponseEntity<>(essay, HttpStatus.OK);
	}
	public ResponseEntity<Essay> getById(int id) throws NoSuchElementException
	{
		Optional<Essay> essay = essayDao.findById(id);
		if (essay.isEmpty())
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		return new ResponseEntity<>(essay.get(), HttpStatus.OK);
	}
}
