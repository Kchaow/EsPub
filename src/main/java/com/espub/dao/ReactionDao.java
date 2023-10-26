package com.espub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.Reaction;

@Repository
public interface ReactionDao extends JpaRepository<Reaction, Integer>
{

}
