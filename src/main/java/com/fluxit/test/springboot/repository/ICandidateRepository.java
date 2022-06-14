package com.fluxit.test.springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fluxit.test.springboot.entity.Candidate;

public interface ICandidateRepository extends JpaRepository<Candidate, Integer> {
	
	@Query("SELECT c FROM Candidate c WHERE c.dni = ?1 OR (c.name LIKE ?2 AND c.surName LIKE ?3)")
	public Page<Candidate> findByDniOrNameAndSurName(int dni, String name, String surName, Pageable pageable);
}
