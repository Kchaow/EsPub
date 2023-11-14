package com.espub.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.espub.dao.ReactionTypeDao;
import com.espub.model.ReactionType;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReactionTypeRepositoryTest 
{
	@Autowired
	ReactionTypeDao reactionTypeDao;
	private ReactionType reactionType;
	@BeforeEach
	void setupComment()
	{
		reactionType = ReactionType.builder()
				.name("name")
				.build();
	}
	@Test
	void createRowShouldNotBeNull()
	{
		ReactionType returned = reactionTypeDao.save(reactionType);
		
		assertThat(returned).isNotNull();
		assertEquals(returned, reactionType);
	}
	@Test
	void updateRowShouldBeChanged()
	{
		ReactionType created = reactionTypeDao.save(reactionType);
		int id = created.getId();
		String newValue = "Another name";
		reactionType.setName(newValue);
		reactionTypeDao.save(reactionType);
		
		assertEquals(reactionTypeDao.findById(id).get().getName(), newValue);
	}
	@Test
	void findByIdRowShouldNotBeNull()
	{
		ReactionType created = reactionTypeDao.save(reactionType);
		
		assertEquals(reactionTypeDao.findById(created.getId()).isEmpty(), false);
	}
	@Test
	void deleteByIdRowExistsShouldReturnFalse()
	{
		ReactionType created = reactionTypeDao.save(reactionType);
		
		reactionTypeDao.deleteById(created.getId());
		
		assertEquals(reactionTypeDao.existsById(created.getId()), false);
	}
}
