package com.espub.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.espub.dao.CategoryDao;
import com.espub.dao.EssayDao;
import com.espub.model.Category;
import com.espub.model.Essay;
import com.espub.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EssayRepositoryTest 
{
	@Autowired
	EssayDao essayDao;
	@Autowired
	CategoryDao categoryDao;
	private Essay essay;
	@BeforeEach
	void setupEssay()
	{
		List<Category> list = new ArrayList<>();
		list.add(new Category());
		essay = Essay.builder()
				.content("Content")
				.publicationDate(new GregorianCalendar())
				.user(new User())
				.category(list)
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
	@Test
	void findAllByCategoryNameShouldReturnEssayWithCorrectCategory()
	{
		int[] ids = setCoupleCategory(3);
		String secondCategoryName = "1";
		
		List<Category> firstList = new ArrayList<>();
		List<Category> secondList = new ArrayList<>();
		List<Category> thirdList = new ArrayList<>();
		
		firstList.add(categoryDao.findById(ids[0]).get());
		
		secondList.add(categoryDao.findById(ids[0]).get());
		secondList.add(categoryDao.findById(ids[1]).get());
		
		thirdList.add(categoryDao.findById(ids[0]).get());
		thirdList.add(categoryDao.findById(ids[1]).get());
		thirdList.add(categoryDao.findById(ids[2]).get());
		
		Essay firstEssay = Essay.builder()
				.category(firstList)
				.build();
		Essay secondEssay = Essay.builder()
				.category(secondList)
				.build();
		Essay thirdEssay = Essay.builder()
				.category(thirdList)
				.build();
		
		essayDao.save(firstEssay);
		essayDao.save(secondEssay);
		essayDao.save(thirdEssay);
		
		Pageable pageRequest = PageRequest.of(0, 10);
		assertEquals(2, essayDao.findAllByCategoryName(secondCategoryName, pageRequest).getTotalElements());
	}
	
	private int[] setCoupleCategory(int count)
	{
		int[] ids = new int[count];
		for (int i = 0; i < count; i++)
		{
			Category category = Category.builder()
					.name(i + "")
					.build();
			ids[i] = categoryDao.save(category).getId();
		}
		return ids;
	}
}
