package ru.tcns.vktrgt.domain.external.vk.dict;

import java.util.List;

public class ActiveAuditoryDTO {
    private final List<Integer> groups;
    private final Integer maxDays;
    private final Boolean countByAllGroups;
    private final Boolean countLikes;
    private final Boolean countReposts;
    private final Boolean countComments;
    private final Integer minCount;
    private final String type;
    private final List<Integer> postIds;

    public ActiveAuditoryDTO(List<Integer> groups,
                             Integer maxDays,
                             Boolean countByAllGroups,
                             Boolean countLikes,
                             Boolean countReposts,
                             Boolean countComments,
                             Integer minCount,
                             String type,
                             List<Integer> postIds) {
        this.groups = groups;
        this.postIds = postIds;
        this.maxDays = maxDays;
        this.countByAllGroups = countByAllGroups;
        this.countLikes = countLikes;
        this.countReposts = countReposts;
        this.countComments = countComments;
        this.minCount = minCount;
        this.type = type;
    }

    public List<Integer> getGroups() {
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

    public List<Integer> getPostIds() {
        return postIds;
    }
}
