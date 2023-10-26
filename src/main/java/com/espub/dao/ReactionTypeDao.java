package com.espub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.ReactionType;

@Repository
public interface ReactionTypeDao extends JpaRepository<ReactionType, Integer>
{
	
}
