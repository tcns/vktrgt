package ru.tcns.vktrgt.repository.external.vk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;

import java.util.List;

/**
 * Created by Тимур on 13.04.2016.
 */
public interface GroupRepository extends MongoRepository<Group, Long>, QueryDslPredicateExecutor<Group> {
    Page<Group> findByNameIgnoreCase(String name, Pageable pageable);
}
