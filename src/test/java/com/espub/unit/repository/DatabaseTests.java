package com.espub.unit.repository;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.espub.dao.CategoryDao;
import com.espub.dao.CommentDao;
import com.espub.dao.EssayDao;
import com.espub.dao.ReactionDao;
import com.espub.dao.ReactionTypeDao;
import com.espub.dao.UserDao;
import com.espub.model.Category;
import com.espub.model.Comment;
import com.espub.model.Essay;
import com.espub.model.Reaction;
import com.espub.model.ReactionType;
import com.espub.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DatabaseTests 
{	
	@Nested
	class EssayRepositoryTests
	{
		@Autowired
		EssayDao essayDao;
		private Essay essay;
		@BeforeEach
		void setupEssay()
		{
			essay = Essay.builder()
					.content("Content")
					.publicationDate(new GregorianCalendar())
					.user(new User())
					.modificationDate(new GregorianCalendar()).build();
		}
		@Test
		void createRowShouldNotBeNull()
		{
			Essay returned = essayDao.save(essay);
			
			assertThat(returned).isNotNull();
			assertEquals(returned, essay);
		}
		@Test
		void updateRowShouldBeChanged()
		{
			Essay created = essayDao.save(essay);
			int id = created.getId();
			String newValue = "Another content";
			essay.setContent(newValue);
			essayDao.save(essay);
			
			assertEquals(essayDao.findById(id).get().getContent(), newValue);
		}
		@Test
		void findByIdRowShouldNotBeNull()
		{
			Essay created = essayDao.save(essay);
			
			assertEquals(essayDao.findById(created.getId()).isEmpty(), false);
		}
		@Test
		void deleteByIdRowExistsShouldReturnFalse()
		{
			Essay created = essayDao.save(essay);
			
			essayDao.deleteById(created.getId());
			
			assertEquals(essayDao.existsById(created.getId()), false);
		}
	}
	@Nested
	class CategoryRepositoryTests
	{
		@Autowired
		CategoryDao categoryDao;
		private Category category;
		@BeforeEach
		void setupCategory()
		{
			List<Essay> list = new ArrayList<>();
			list.add(new Essay());
			category = Category.builder()
					.name("Name")
					.essay(list)
					.build();
		}
		@Test
		void createRowShouldNotBeNull()
		{
			Category returned = categoryDao.save(category);
			
			assertThat(returned).isNotNull();
			assertEquals(returned, category);
		}
		@Test
		void updateRowShouldBeChanged()
		{
			Category created = categoryDao.save(category);
			int id = created.getId();
			String newValue = "Another name";
			category.setName(newValue);
			categoryDao.save(category);
			
			assertEquals(categoryDao.findById(id).get().getName(), newValue);
		}
		@Test
		void findByIdRowShouldNotBeNull()
		{
			Category created = categoryDao.save(category);
			
			assertEquals(categoryDao.findById(created.getId()).isEmpty(), false);
		}
		@Test
		void deleteByIdRowExistsShouldReturnFalse()
		{
			Category created = categoryDao.save(category);
			
			categoryDao.deleteById(created.getId());
			
			assertEquals(categoryDao.existsById(created.getId()), false);
		}
	}
	@Nested
	class CommentRepositoryTests
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
	@Nested
	class ReactionRepositoryTests
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
	@Nested
	class ReactionTypeRepositoryTests
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
	@Nested
	class UserRepositoryTests
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
	}
}
