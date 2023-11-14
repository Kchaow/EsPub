package com.espub.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.espub.dao.ReactionDao;
import com.espub.model.Essay;
import com.espub.model.Reaction;
import com.espub.model.ReactionType;
import com.espub.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReactionRepositoryTest 
{
	@Autowired
	ReactionDao reactionDao;
	private Reaction reaction;
	@BeforeEach
	void setupComment()
	{
		reaction = Reaction.builder()
				.user(new User())
				.reactionType(new ReactionType())
				.essay(new Essay())
				.build();
	}
	@Test
	void createRowShouldNotBeNull()
	{
		Reaction returned = reactionDao.save(reaction);
		
		assertThat(returned).isNotNull();
		assertEquals(returned, reaction);
	}
	@Test
	void updateRowShouldBeChanged()
	{
		Reaction created = reactionDao.save(reaction);
		int id = created.getId();
		Essay newValue = Essay.builder().content("new content").build();
		reaction.setEssay(newValue);
		reactionDao.save(reaction);
		
		assertEquals(reactionDao.findById(id).get().getEssay(), newValue);
	}
	@Test
	void findByIdRowShouldNotBeNull()
	{
		Reaction created = reactionDao.save(reaction);
		
		assertEquals(reactionDao.findById(created.getId()).isEmpty(), false);
	}
	@Test
	void deleteByIdRowExistsShouldReturnFalse()
	{
		Reaction created = reactionDao.save(reaction);
		
		reactionDao.deleteById(created.getId());
		
		assertEquals(reactionDao.existsById(created.getId()), false);
	}
}
