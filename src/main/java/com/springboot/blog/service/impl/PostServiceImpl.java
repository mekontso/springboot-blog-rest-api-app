package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.stereotype.Service;

@Service // make a class as service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository; // constructor based injection

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // Convert Dto to entity
        Post post = new Post(postDto.getTitle(), postDto.getDescription(), postDto.getContent());
        //persist entity
        post = postRepository.save(post); // save inDB
        // Convert entity to dto
        return new PostDto(post.getId(), post.getTitle(), postDto.getDescription(), post.getContent());
    }
}
