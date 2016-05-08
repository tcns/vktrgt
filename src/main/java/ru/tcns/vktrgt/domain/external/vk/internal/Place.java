package ru.tcns.vktrgt.domain.external.vk.internal;

import org.springframework.data.annotation.Id;
import ru.tcns.vktrgt.anno.JsonEntity;

/**
 * Created by Тимур on 09.04.2016.
 */
@JsonEntity
public class Place {
    @Id
    private Integer id;
    private String title;
    private Double longitude;
    private Double latitude;
    private Integer type;
    private Integer country;
    private Integer city;
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
