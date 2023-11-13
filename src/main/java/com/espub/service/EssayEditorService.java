package com.espub.service;

import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.espub.dao.EssayDao;
import com.espub.dao.UserDao;
import com.espub.dto.EssayRequest;
import com.espub.exception.NoPermissionException;
import com.espub.model.Essay;
import com.espub.model.User;

@Service
public class EssayEditorService 
{
	@Autowired
	private EssayDao essayDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private Authentication authentication;
	private Logger logger = LoggerFactory.getLogger(EssayEditorService.class);
	
	public ResponseEntity<String> addEssay(EssayRequest requestEssay)
	{
		String username = authentication.getName();
		Calendar date = new GregorianCalendar();
		User user = userDao.findByUsername(username).get();
		Essay essay = Essay.builder()
				.content(requestEssay.getContent())
				.publicationDate(date)
				.modificationDate(date)
				.user(user)
				.build();
		essay = essayDao.save(essay);
		logger.debug("New essay was added: {}", essay.getId());
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
	public ResponseEntity<String> deleteEssay(int id) throws NoSuchElementException, NoPermissionException
	{
		if (!essayDao.existsById(id))
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		Essay essay = essayDao.findById(id).get();
		GrantedAuthority adminRole = new GrantedAuthority()
				{
					private static final long serialVersionUID = -1930735709217377217L;
					@Override
					public String getAuthority() {
						return "ADMIN";
					}
				};
		if (authentication.getName() != essay.getUser().getUsername() && !authentication.getAuthorities().contains(adminRole))
			throw new NoPermissionException();
		essayDao.deleteById(id);
		logger.debug("Essay with id {} was deleted", id);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	public ResponseEntity<String> modifyEssay(EssayRequest essay, int id) throws NoSuchElementException, NoPermissionException
	{
		if (!essayDao.existsById(id))
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		Essay originalEssay = essayDao.findById(id).get();
		if (authentication.getName() != originalEssay.getUser().getUsername())
			throw new NoPermissionException();
		originalEssay.setContent(essay.getContent());
		originalEssay.setModificationDate(new GregorianCalendar());
		Essay newEssay = essayDao.save(originalEssay);
		logger.debug("Essay was modified: {}", newEssay.getId());
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
}
