package ru.tcns.vktrgt.repository.external.vk;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;

/**
 * Created by TIMUR on 16.04.2016.
 */
public interface GroupIdRepository extends MongoRepository <GroupIds, Integer> {
}
