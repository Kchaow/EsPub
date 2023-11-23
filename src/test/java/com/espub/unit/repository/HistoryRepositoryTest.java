package com.espub.unit.repository;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.espub.dao.EssayDao;
import com.espub.dao.HistoryDao;
import com.espub.dao.UserDao;
import com.espub.model.Essay;
import com.espub.model.History;
import com.espub.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class HistoryRepositoryTest 
{
	@Autowired
	HistoryDao historyDao;
	@Autowired
	EssayDao essayDao;
	@Autowired
	UserDao userDao;
	History history;
	
	@BeforeEach
	void setupHistory()
	{
		history = History.builder()
				.lastValidViewDate(ZonedDateTime.now())
				.lastViewDate(ZonedDateTime.now())
				.build();
	}
	
	@Test
	void findByUserAndEssayShouldReturnEssay()
	{
		User user = new User();
		userDao.save(user);
		Essay essay = new Essay();
		essayDao.save(essay);
		history.setEssay(essay);
		history.setUser(user);
		historyDao.save(history);
		
		Optional<History> historyOpt = historyDao.findByUserAndEssay(user, essay);
		
		assertEquals(false, historyOpt.isEmpty());
	}

}
