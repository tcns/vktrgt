package ru.tcns.vktrgt.service.external.vk.intf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;

import java.util.List;

/**
 * Created by Тимур on 13.04.2016.
 */
public interface GroupService {
    Group findOne(Long id);

    Group save(Group group);
    List<Group> saveAll(List<Group> group);

    Page<Group> findAll(Pageable pageable);
    List<Group> findAll();

    void delete(Long id);
    void deleteAll();
    Page<Group> searchByName(String name, Boolean restrict, Pageable pageable);


}
