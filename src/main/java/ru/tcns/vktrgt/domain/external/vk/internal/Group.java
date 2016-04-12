package ru.tcns.vktrgt.domain.external.vk.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ru.tcns.vktrgt.domain.external.vk.dict.GroupType;

import java.util.Date;

/**
 * Created by timur on 3/28/16.
 */
@Document(collection = "group")
public class Group {
    @Id
    private Long id;

    private String name;

    private String screenName;

    private Boolean isClosed;

    private GroupType type;

    private String photo50;

    private String photo100;

    private String photo200;

    private String city;

    private String country;
    @DBRef
    private Place place;

    private String description;

    private String wikiPage;

    private Integer membersCount;

    private String counters;

    private Date startDate;

    private Date finishDate;

    private String publicDateLabel;

    private String activity;

    private String status;

    private String contacts;

    private String links;

    private String fixedPost;

    private String verified;

    private String site;

    private String mainAlbumId;

    private String mainSection;
    @DBRef
    private Market market;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public String getPhoto50() {
        return photo50;
    }

    public void setPhoto50(String photo50) {
        this.photo50 = photo50;
    }

    public String getPhoto100() {
        return photo100;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    public String getPhoto200() {
        return photo200;
    }

    public void setPhoto200(String photo200) {
        this.photo200 = photo200;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWikiPage() {
        return wikiPage;
    }

    public void setWikiPage(String wikiPage) {
        this.wikiPage = wikiPage;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public String getCounters() {
        return counters;
    }

    public void setCounters(String counters) {
        this.counters = counters;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getPublicDateLabel() {
        return publicDateLabel;
    }

    public void setPublicDateLabel(String publicDateLabel) {
        this.publicDateLabel = publicDateLabel;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getFixedPost() {
        return fixedPost;
    }

    public void setFixedPost(String fixedPost) {
        this.fixedPost = fixedPost;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMainAlbumId() {
        return mainAlbumId;
    }

    public void setMainAlbumId(String mainAlbumId) {
        this.mainAlbumId = mainAlbumId;
    }

    public String getMainSection() {
        return mainSection;
    }

    public void setMainSection(String mainSection) {
        this.mainSection = mainSection;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return id.equals(group.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
