package com.reddit.clone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.clone.entity.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long>{
    Optional<Subreddit> findByName(String subredditName);
}
