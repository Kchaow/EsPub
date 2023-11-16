package com.espub.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.espub.dao.UserDao;
import com.espub.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest 
{
	@Autowired
	UserDao userDao;
	private User user;
	@BeforeEach
	void setupComment()
	{
		user = User.builder()
				.password("encodedPassword")
				.salt("salt")
				.username("username")
				.profileImage("image")
				.status("status")
				.description("description")
				.build();
	}
	@Test
	void createRowShouldNotBeNull()
	{
		User returned = userDao.save(user);
		
		assertThat(returned).isNotNull();
		assertEquals(returned, user);
	}
	@Test
	void updateRowShouldBeChanged()
	{
		User created = userDao.save(user);
		int id = created.getId();
		String newValue = "Another name";
		user.setUsername(newValue);
		userDao.save(user);
		
		assertEquals(userDao.findById(id).get().getUsername(), newValue);
	}
	@Test
	void findByIdRowShouldNotBeNull()
	{
		User created = userDao.save(user);
		
		assertEquals(userDao.findById(created.getId()).isEmpty(), false);
	}
	@Test
	void deleteByIdRowExistsShouldReturnFalse()
	{
		User created = userDao.save(user);
		
		userDao.deleteById(created.getId());
		
		assertEquals(userDao.existsById(created.getId()), false);
	}
	@Test
	void existByUsernameShouldReturnTrue()
	{
		userDao.save(user);
		assertTrue(userDao.existsByUsername(user.getUsername()));
	}
	
	@Test
	void savingUserWithExistingNameShouldThrowException()
	{
		User user2 = User.builder().username("username").build();
		userDao.save(user);
		assertThrows(DataIntegrityViolationException.class, () -> userDao.save(user2));
	}
}
