package ru.tcns.vktrgt.domain.external.vk.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.anno.JsonField;
import ru.tcns.vktrgt.anno.JsonIgnore;
import ru.tcns.vktrgt.domain.external.vk.dict.GroupType;

import java.util.Date;
import java.util.List;

/**
 * Created by timur on 3/28/16.
 */
@Document(collection = "vk_groups")
@JsonEntity
public class Group {
    @Id
    private Integer id;

    @Indexed
    private String name;

    private String screenName;

    private Integer isClosed;

    private String type;

    @JsonField(name = "photo_50")
    private String photo50;

    @JsonField(name = "photo_100")
    private String photo100;

    @JsonField(name = "photo_200")
    private String photo200;

    @Field
    private City city;

    @Field
    private Country country;
    @Field
    private Place place;

    private String description;

    private String wikiPage;

    private Integer membersCount;

    private String counters;

    private Integer startDate;

    private Integer finishDate;

    private String publicDateLabel;

    private String activity;

    private String status;

    @Field
    private List<Contact> contacts;

    private String links;

    private Integer fixedPost;

    private Integer verified;

    private String site;

    private Integer mainAlbumId;

    private Integer mainSection;
    @Field
    private Market market;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Integer isClosed) {
        this.isClosed = isClosed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
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

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Integer finishDate) {
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

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public Integer getFixedPost() {
        return fixedPost;
    }

    public void setFixedPost(Integer fixedPost) {
        this.fixedPost = fixedPost;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getMainAlbumId() {
        return mainAlbumId;
    }

    public void setMainAlbumId(Integer mainAlbumId) {
        this.mainAlbumId = mainAlbumId;
    }

    public Integer getMainSection() {
        return mainSection;
    }

    public void setMainSection(Integer mainSection) {
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
