package com.espub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.Essay;

@Repository
public interface CategoryDao extends JpaRepository<Essay, Integer> 
{
	
}
