package ru.tcns.vktrgt.domain.external.vk.dict;

/**
 * Created by timur on 3/28/16.
 */
public enum GroupType {
    PAGE, EVENT, GROUP;


    public static GroupType get(String str) {
        return GroupType.valueOf(str.toUpperCase());
    }
}
