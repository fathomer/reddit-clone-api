package com.reddit.clone.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.clone.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}