package com.clinital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinital.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// Optional<User> findByUsername(String email);

	// Boolean existsByUsername(String email);

	Boolean existsByEmail(String email);
	Optional<User> findUserByEmail(String email);
	User findByEmail(String email);

	@Query(value ="SELECT * FROM users WHERE id = :id",nativeQuery = true)
	User getById(Long id);
	
	@Query(value = "SELECT case when (u.email_verified=1)  then true else false end FROM users u WHERE u.email = ?1",nativeQuery=true)
    Boolean findEmailVerifiedByEmail(String email);



}
