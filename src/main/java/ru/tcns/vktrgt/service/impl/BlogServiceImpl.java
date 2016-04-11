package ru.tcns.vktrgt.service.impl;

import ru.tcns.vktrgt.service.BlogService;
import ru.tcns.vktrgt.domain.Blog;
import ru.tcns.vktrgt.repository.BlogRepository;
import ru.tcns.vktrgt.web.rest.dto.BlogDTO;
import ru.tcns.vktrgt.web.rest.mapper.BlogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Blog.
 */
@Service
public class BlogServiceImpl implements BlogService{

    private final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);
    
    @Inject
    private BlogRepository blogRepository;
    
    @Inject
    private BlogMapper blogMapper;
    
    /**
     * Save a blog.
     * @return the persisted entity
     */
    public BlogDTO save(BlogDTO blogDTO) {
        log.debug("Request to save Blog : {}", blogDTO);
        Blog blog = blogMapper.blogDTOToBlog(blogDTO);
        blog = blogRepository.save(blog);
        BlogDTO result = blogMapper.blogToBlogDTO(blog);
        return result;
    }

    /**
     *  get all the blogs.
     *  @return the list of entities
     */
    public Page<Blog> findAll(Pageable pageable) {
        log.debug("Request to get all Blogs");
        Page<Blog> result = blogRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one blog by id.
     *  @return the entity
     */
    public BlogDTO findOne(String id) {
        log.debug("Request to get Blog : {}", id);
        Blog blog = blogRepository.findOne(id);
        BlogDTO blogDTO = blogMapper.blogToBlogDTO(blog);
        return blogDTO;
    }

    /**
     *  delete the  blog by id.
     */
    public void delete(String id) {
        log.debug("Request to delete Blog : {}", id);
        blogRepository.delete(id);
    }
}
