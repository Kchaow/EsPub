package com.espub.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.espub.dao.CommentDao;
import com.espub.model.Comment;
import com.espub.model.Essay;
import com.espub.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest 
{
	@Autowired
	CommentDao commentDao;
	private Comment comment;
	@BeforeEach
	void setupComment()
	{
		comment = Comment.builder()
				.text("text")
				.user(new User())
				.essay(new Essay())
				.build();
	}
	@Test
	void createRowShouldNotBeNull()
	{
		Comment returned = commentDao.save(comment);
		
		assertThat(returned).isNotNull();
		assertEquals(returned, comment);
	}
	@Test
	void updateRowShouldBeChanged()
	{
		Comment created = commentDao.save(comment);
		int id = created.getId();
		String newValue = "Another text";
		comment.setText(newValue);
		commentDao.save(comment);
		
		assertEquals(commentDao.findById(id).get().getText(), newValue);
	}
	@Test
	void findByIdRowShouldNotBeNull()
	{
		Comment created = commentDao.save(comment);
		
		assertEquals(commentDao.findById(created.getId()).isEmpty(), false);
	}
	@Test
	void deleteByIdRowExistsShouldReturnFalse()
	{
		Comment created = commentDao.save(comment);
		
		commentDao.deleteById(created.getId());
		
		assertEquals(commentDao.existsById(created.getId()), false);
	}
}
