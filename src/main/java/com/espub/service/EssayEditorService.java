package com.espub.service;

import java.util.NoSuchElementException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	@Value("${server.zoneId}")
	private ZoneId zoneId;
	private Logger logger = LoggerFactory.getLogger(EssayEditorService.class);
	
	public ResponseEntity<String> addEssay(EssayRequest requestEssay, Authentication authentication) throws NoPermissionException
	{
		if (authentication == null || !authentication.isAuthenticated())
			throw new NoPermissionException();
		String username = authentication.getName();
		User user = userDao.findByUsername(username).get();
		Essay essay = Essay.builder()
				.content(requestEssay.getContent())
				.publicationDate(ZonedDateTime.now(zoneId))
				.modificationDate(ZonedDateTime.now(zoneId))
				.user(user)
				.build();
		essay = essayDao.save(essay);
		logger.debug("New essay was added: {}", essay.getId());
		ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED)
				.header("Location", "essay/" + essay.getId())
				.body("success");
		return response;
	}
	public ResponseEntity<String> deleteEssay(int id, Authentication authentication) throws NoSuchElementException, NoPermissionException
	{
		if (authentication == null)
			throw new NoPermissionException();
		if (!essayDao.existsById(id))
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		Essay essay = essayDao.findById(id).get();
		String essayOwnerUsername = essay.getUser().getUsername();
		String roleName = "ADMIN";
		if (!authentication.getName().equals(essayOwnerUsername) 
				&& authentication.getAuthorities().stream().filter((x) -> x.getAuthority().equals(roleName)).count() == 0)
			throw new NoPermissionException();
		essayDao.deleteById(id);
		logger.debug("Essay with id {} was deleted", id);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	public ResponseEntity<String> modifyEssay(EssayRequest essay, int id, Authentication authentication) throws NoSuchElementException, NoPermissionException
	{
		if (!essayDao.existsById(id))
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		Essay originalEssay = essayDao.findById(id).get();
		String essayOwnerUsername = originalEssay.getUser().getUsername();
		if (authentication == null || !authentication.getName().equals(essayOwnerUsername) )
			throw new NoPermissionException();
		originalEssay.setContent(essay.getContent());
		originalEssay.setModificationDate(ZonedDateTime.now(zoneId));
		Essay newEssay = essayDao.save(originalEssay);
		logger.debug("Essay was modified: {}", newEssay.getId());
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
}
