package ru.tcns.vktrgt.domain.external.vk.internal;

/**
 * Created by Тимур on 09.04.2016.
 */
public class Market {
    private Boolean enabled;
    private Double priceMin;
    private Double priceMax;
    private Long mainAlbumId;
    private Long contactId;
    private Long currencyId;
    private String currencyName;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public Long getMainAlbumId() {
        return mainAlbumId;
    }

    public void setMainAlbumId(Long mainAlbumId) {
        this.mainAlbumId = mainAlbumId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
