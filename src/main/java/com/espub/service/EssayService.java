package com.espub.service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.espub.dao.EssayDao;
import com.espub.dao.HistoryDao;
import com.espub.dao.UserDao;
import com.espub.dto.EssayResponse;
import com.espub.dto.EssayResponsePage;
import com.espub.model.Essay;
import com.espub.model.History;
import com.espub.model.User;
import com.espub.util.EssayPageSort;
import com.espub.util.EssayResponsePageWrapper;
import com.espub.util.EssayResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EssayService 
{
	Logger logger = Logger.getLogger(EssayService.class);
	@Autowired
	private EssayDao essayDao;
	@Autowired
	private HistoryDao historyDao;
	@Autowired
	private UserDao userDao;
	@Value("${essay.viewCounterCooldown}")
	private int viewCounterCooldown;
	@Value("${server.zoneId}")
	private ZoneId zoneId;
	
	public ResponseEntity<EssayResponsePage> getEssayPage(int offset, int limit, 
													EssayPageSort essayPageSort, 
													String category, 
													HttpServletRequest request)
	{
		PageRequest pageRequest;
		if (essayPageSort == null)
			pageRequest = PageRequest.of(offset, limit);
		else
			pageRequest = PageRequest.of(offset, limit, essayPageSort.getSortValue());
		
		Page<Essay> essayPage;
		if (category == null)
			essayPage = essayDao.findAll(pageRequest);
		else
			essayPage = essayDao.findAllByCategoryName(category, pageRequest);
		
		if (request.getHeader("If-None-Match") != null && request.getHeader("If-None-Match").equals(essayPage.hashCode() + ""))
			return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		ZoneId clientZoneId = null;
		if (request.getHeader("ZoneId") != null)
		{
			try
			{
				clientZoneId = ZoneId.of(request.getHeader("ZoneId"));
			}
			catch (Exception e)
			{
				logger.error(e.toString());
			}
		}
		EssayResponsePage essayResponsePage = EssayResponsePageWrapper.of(essayPage, clientZoneId);
		
		CacheControl cacheControl = CacheControl
				.noCache()
				.cachePublic();
		return ResponseEntity.status(HttpStatus.OK)
				.eTag(essayPage.hashCode() + "")
				.cacheControl(cacheControl)
				.body(essayResponsePage);
	}
	public ResponseEntity<EssayResponse> getById(int id,
										 Authentication authentication,
										 HttpServletRequest request) throws NoSuchElementException
	{
		Optional<Essay> essayOpt = essayDao.findById(id);
		if (essayOpt.isEmpty())
			throw new NoSuchElementException(String.format("Element with %d id doesn't exist", id));
		Essay essay = essayOpt.get();
		
		if (request.getHeader("If-None-Match") != null && request.getHeader("If-None-Match").equals(essay.hashCode() + ""))
			return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
		
		changeHistory(authentication, essay);
		
		ZoneId clientZoneId = null;
		if (request.getHeader("ZoneId") != null)
		{
			try
			{
				clientZoneId = ZoneId.of(request.getHeader("ZoneId"));
			}
			catch (Exception e)
			{
				logger.error(e.toString());
			}
		}
		EssayResponse essayResponse = EssayResponseWrapper.of(essay, clientZoneId);
		CacheControl cacheControl = CacheControl
				.noCache()
				.cachePublic();
		return ResponseEntity.status(HttpStatus.OK)
				.eTag(essay.hashCode() + "")
				.cacheControl(cacheControl)
				.body(essayResponse);
				
	}
	
	private void changeHistory(Authentication authentication, Essay essay)
	{
		if (authentication.isAuthenticated())
		{
			User user = userDao.findByUsername(authentication.getName()).get();
			if (historyDao.findByUserAndEssay(user, essay).isEmpty())
			{
				int views = essay.getViews();
				essay.setViews(++views);
				essay = essayDao.save(essay);
				History history = History.builder()
						.essay(essay)
						.user(user)
						.lastValidViewDate(ZonedDateTime.now(zoneId))
						.lastViewDate(ZonedDateTime.now(zoneId))
						.build();
				historyDao.save(history);
						
			}
			else if (isCooldownLeft(user, essay))
			{
				int views = essay.getViews();
				essay.setViews(++views);
				essay = essayDao.save(essay);
				History history = historyDao.findByUserAndEssay(user, essay).get();
				history.setLastValidViewDate(ZonedDateTime.now(zoneId));
				history.setLastViewDate(ZonedDateTime.now(zoneId));
				historyDao.save(history);
			}
			else
			{
				History history = historyDao.findByUserAndEssay(user, essay).get();
				history.setLastViewDate(ZonedDateTime.now(zoneId));
				historyDao.save(history);
			}
		}
	}
	
	private boolean isCooldownLeft(User user, Essay essay)
	{
		Optional<History> history = historyDao.findByUserAndEssay(user, essay);
		if (history.isEmpty()) return true;
		if (Duration.between(history.get().getLastValidViewDate(), ZonedDateTime.now(this.zoneId)).getSeconds() > viewCounterCooldown)
			return true;
		return false;
	}
}
