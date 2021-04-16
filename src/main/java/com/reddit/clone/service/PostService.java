package com.reddit.clone.service;

import java.util.List;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.dto.PostRequest;
import com.reddit.clone.dto.PostResponse;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.Subreddit;
import com.reddit.clone.entity.User;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.exception.SubredditNotFoundException;
import com.reddit.clone.repository.CommentRepository;
import com.reddit.clone.repository.PostRepository;
import com.reddit.clone.repository.SubredditRepository;
import com.reddit.clone.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        return mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(mapToEntity(postRequest, subreddit, authService.getCurrentUser()));
    }

    PostResponse mapToDto(Post post) {
        return PostResponse.builder().id(post.getPostId()).postName(post.getPostName())
                .description(post.getDescription()).url(post.getUrl()).userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName()).commentCount(commentRepository.findByPost(post).size()).
                duration(post.getCreatedDate() != null ? TimeAgo.using(post.getCreatedDate().toEpochMilli()) : null).
                voteCount(post.getVoteCount()).
                build();
    }

    private Post mapToEntity(PostRequest postRequest, Subreddit subreddit, User user) {
        return Post.builder().postName(postRequest.getPostName()).url(postRequest.getUrl())
                .description(postRequest.getDescription()).user(user).subreddit(subreddit).createdDate(java.time.Instant.now()).voteCount(0).
                build();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user).stream().map(this::mapToDto).collect(Collectors.toList());
    }
}