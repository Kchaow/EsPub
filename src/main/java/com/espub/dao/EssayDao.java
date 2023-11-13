package com.espub.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.Essay;

@Repository
public interface EssayDao extends JpaRepository<Essay, Integer> 
{
	Page<Essay> findAllByCategoryName(String name, Pageable pageable);
}
