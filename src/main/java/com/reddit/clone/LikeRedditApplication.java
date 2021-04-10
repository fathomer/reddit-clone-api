package com.reddit.clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LikeRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(LikeRedditApplication.class, args);
	}

}
