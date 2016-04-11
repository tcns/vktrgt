package ru.tcns.vktrgt.repository;

import ru.tcns.vktrgt.domain.Blog;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Blog entity.
 */
public interface BlogRepository extends MongoRepository<Blog,String> {

}
