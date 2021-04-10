package com.reddit.clone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.dto.CommentsDto;
import com.reddit.clone.entity.Comment;
import com.reddit.clone.entity.NotificationEmail;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import com.reddit.clone.exception.PostNotFoundException;
import com.reddit.clone.repository.CommentRepository;
import com.reddit.clone.repository.PostRepository;
import com.reddit.clone.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

    // TODO: Construct POST URL
    private static final String POST_URL = "";

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilderService mailContentBuilder;
    private final MailService mailService;

    public Comment mapToEntity(CommentsDto commentsDto, Post post, User user) {
        return Comment.builder().text(commentsDto.getText()).post(post).user(user).createdDate(Instant.now()).build();
    }

    public CommentsDto mapToDto(Comment comment) {
        return CommentsDto.builder().id(comment.getId()).text(comment.getText()).postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate()).userName(comment.getUser().getUsername()).build();

    }

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = mapToEntity(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder
                .build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(
                new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }
}