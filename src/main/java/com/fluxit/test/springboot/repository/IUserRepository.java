package com.fluxit.test.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fluxit.test.springboot.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	
	@Query("SELECT DISTINCT u FROM User u WHERE u.userName = ?1")
	public Optional<User> findByUserName(String userName);
}
