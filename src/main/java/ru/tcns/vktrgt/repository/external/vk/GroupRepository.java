package ru.tcns.vktrgt.repository.external.vk;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;

/**
 * Created by Тимур on 13.04.2016.
 */
public interface GroupRepository extends MongoRepository<Group, Long> {

}
