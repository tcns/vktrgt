package ru.tcns.vktrgt.service;

import ru.tcns.vktrgt.domain.Blog;
import ru.tcns.vktrgt.web.rest.dto.BlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Blog.
 */
public interface BlogService {

    /**
     * Save a blog.
     * @return the persisted entity
     */
    public BlogDTO save(BlogDTO blogDTO);

    /**
     *  get all the blogs.
     *  @return the list of entities
     */
    public Page<Blog> findAll(Pageable pageable);

    /**
     *  get the "id" blog.
     *  @return the entity
     */
    public BlogDTO findOne(String id);

    /**
     *  delete the "id" blog.
     */
    public void delete(String id);
}
