package ru.tcns.vktrgt.domain.external.vk.internal;

import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.anno.JsonIgnore;

import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 22.04.2016.
 */
@JsonEntity
public class User implements Comparable<User> {
    private Integer id;
    private String firstName;
    private String lastName;
    private String domain;
    private Integer sex;
    private String bdate;
    private Country country;
    private String homeTown;
    private String mobilePhone;
    private String homePhone;
    private City city;
    private List<Relative> relatives;
    private Integer relation;
    private User relationPartner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public int compareTo(User o) {
        return this.getId().compareTo(o.getId());
    }

    public List<Relative> getRelatives() {
        return relatives;
    }

    public void setRelatives(List<Relative> relatives) {
        this.relatives = relatives;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public User getRelationPartner() {
        return relationPartner;
    }

    public void setRelationPartner(User relationPartner) {
        this.relationPartner = relationPartner;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }
}
