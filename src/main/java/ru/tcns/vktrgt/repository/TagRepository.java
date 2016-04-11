package ru.tcns.vktrgt.repository;

import ru.tcns.vktrgt.domain.Tag;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Tag entity.
 */
public interface TagRepository extends MongoRepository<Tag,String> {

}
