package com.espub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espub.model.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer>
{
	Optional<Role> findByName(String name);
}
