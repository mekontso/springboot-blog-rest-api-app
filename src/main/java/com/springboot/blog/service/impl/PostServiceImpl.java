package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // make a class as service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository; // constructor based injection

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // Convert Dto to entity
        Post post = mapToEntity(postDto);
        //persist entity
        post = postRepository.save(post); // save inDB
        // Convert entity to dto
        return mapToDTO(post);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        var posts = postRepository.findAll(pageable); // page object
        var postsDto = posts.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        return new PostResponse(postsDto,pageNo, pageSize,posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
    }

    @Override
    public PostDto getPostById(Long id) {
        // get the post, return if found or throw exception
        return mapToDTO(postRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Post","id",String.valueOf(id))));
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        // search post in db
        var post = postRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Post","id",String.valueOf(id)));
        // update fields
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        // update db
        postRepository.save(post);
        return mapToDTO(post);
    }

    @Override
    public void deletePostById(Long id) {
        // search post in db
        var post = postRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Post","id",String.valueOf(id)));
        postRepository.delete(post);
    }

    /**
     * Converts post to post DTO
     * @param post object to convert
     * @return post DTO
     */
    private PostDto mapToDTO(Post post){
        return new PostDto(post.getId(),post.getTitle(),post.getDescription(),post.getContent());
    }

    /**
     * Converts post DTO to post
     * @param postDto post dto to convert
     * @return post converted
     */
    private Post mapToEntity(PostDto postDto){
        return new Post(postDto.getTitle(), postDto.getDescription(), postDto.getContent());
    }
}
