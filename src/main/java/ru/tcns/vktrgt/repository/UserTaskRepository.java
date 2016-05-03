package ru.tcns.vktrgt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import ru.tcns.vktrgt.domain.UserTask;

import java.util.List;

/**
 * Created by TIMUR on 02.05.2016.
 */
public interface UserTaskRepository extends MongoRepository<UserTask, String> {
    Page<UserTask> findByUserId (String userId, Pageable pageable);
}
