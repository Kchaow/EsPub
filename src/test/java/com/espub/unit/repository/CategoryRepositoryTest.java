package com.espub.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.espub.dao.CategoryDao;
import com.espub.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTest
{
	@Autowired
	CategoryDao categoryDao;
	private Category category;
	@BeforeEach
	void setupCategory()
	{
		category = Category.builder()
				.name("Name")
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
