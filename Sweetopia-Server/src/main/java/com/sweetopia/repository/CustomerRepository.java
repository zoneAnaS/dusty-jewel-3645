package com.sweetopia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetopia.entity.User;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<User, Long>{

	public Optional<User> findByEmailAndUserPassword(String userName, String userPassword);
	public Optional<User> findByEmail(String email);
	
}
