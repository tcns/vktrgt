package ru.tcns.vktrgt.service.external.vk.impl;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.QGroup;
import ru.tcns.vktrgt.repository.external.vk.GroupRepository;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Тимур on 13.04.2016.
 */
@Service
public class GroupServiceImpl implements GroupService {


    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Inject
    GroupRepository groupRepository;

    @Override
    public Group findOne(Long id) {
        return groupRepository.findOne(id);
    }
    @Override
    public List<Group> saveAll(List<Group> group) {
        return groupRepository.save(group);
    }
    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public void deleteAll() {
        groupRepository.deleteAll();
    }

    @Override
    public void delete(Long id) {
        groupRepository.delete(id);
    }

    @Override
    public Page<Group> searchByName(String name, Boolean restrict, Pageable pageable) {
        if (!restrict) {
            Predicate predicate = QGroup.group.name.containsIgnoreCase(name.toLowerCase());
            return groupRepository.findAll(predicate, pageable);
        } else {
            return groupRepository.findByNameIgnoreCase(name.toLowerCase(), pageable);
        }

    }

    @Override
    public List<Group> searchByNames(List<String> names) {
        ArrayList<Long> ids = new ArrayList<>();
        for (String name: names) {
            try {
                Long val = Long.parseLong(name);
                ids.add(val);
            } catch (Exception ex){}

        }
        Predicate predicate = QGroup.group.screenName.in(names).or(
            QGroup.group.id.in(ids)
        );
        return Lists.newArrayList(groupRepository.findAll(predicate));
    }

}