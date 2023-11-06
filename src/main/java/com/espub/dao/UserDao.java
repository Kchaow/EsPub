package com.espub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>
{
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
