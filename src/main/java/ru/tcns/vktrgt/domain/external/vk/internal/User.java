package ru.tcns.vktrgt.domain.external.vk.internal;

/**
 * Created by TIMUR on 22.04.2016.
 */
public class User implements Comparable<User> {
    private Long id;
    private String firstName;
    private String lastName;
    private String domain;
    private City city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
