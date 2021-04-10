package com.reddit.clone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.Subreddit;
import com.reddit.clone.entity.User;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
