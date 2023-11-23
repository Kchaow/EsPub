package com.espub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.Essay;
import com.espub.model.History;
import com.espub.model.User;

@Repository
public interface HistoryDao extends JpaRepository<History, Integer>
{
	Optional<History> findByUserAndEssay(User user, Essay essay);
}
