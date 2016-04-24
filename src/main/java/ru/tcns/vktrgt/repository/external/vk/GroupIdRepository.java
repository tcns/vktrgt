package ru.tcns.vktrgt.repository.external.vk;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;

import java.util.List;

/**
 * Created by TIMUR on 16.04.2016.
 */
public interface GroupIdRepository extends MongoRepository <GroupIds, Integer> {
    @Query("{'id' : {'$gt' : ?0, '$lt' : ?1}}")
    List<GroupIds> findByIdBetween (Integer from, Integer to);
}
