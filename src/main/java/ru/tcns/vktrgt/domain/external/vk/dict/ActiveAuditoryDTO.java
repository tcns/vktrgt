package ru.tcns.vktrgt.domain.external.vk.dict;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class ActiveAuditoryDTO implements Serializable {
    private List<String> groups;
    private Integer maxDays;
    private Boolean countByAllGroups;
    private Boolean countLikes;
    private Boolean countReposts;
    private Boolean countComments;
    private Integer minCount;
    private String type;
    private List<String> postIds;
    private String taskInfo;

    public List<String> getGroups() {
        return groups;
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    public Boolean getCountByAllGroups() {
        return countByAllGroups;
    }

    public Boolean getCountLikes() {
        return countLikes;
    }

    public Boolean getCountReposts() {
        return countReposts;
    }

    public Boolean getCountComments() {
        return countComments;
    }

    public Integer getMinCount() {
        return minCount;
    }

    public String getType() {
        return type;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setMaxDays(Integer maxDays) {
        this.maxDays = maxDays;
    }

    public void setCountByAllGroups(Boolean countByAllGroups) {
        this.countByAllGroups = countByAllGroups;
    }

    public void setCountLikes(Boolean countLikes) {
        this.countLikes = countLikes;
    }

    public void setCountReposts(Boolean countReposts) {
        this.countReposts = countReposts;
    }

    public void setCountComments(Boolean countComments) {
        this.countComments = countComments;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }
}
