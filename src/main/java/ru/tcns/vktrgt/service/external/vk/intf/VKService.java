package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.api.vk.Common;

/**
 * Created by TIMUR on 29.04.2016.
 */
public interface VKService {
    String DOMAIN_PREFIX = "https://vk.com/";
    String URL_PREFIX = "https://api.vk.com/method/";
    String VERSION = "&v=5.52";
    String ACCESS_TOKEN = "&access_token=" + Common.getToken() + "&client_secret=" + Common.CLIENT_SECRET +
        VERSION;
    String RESPONSE_STRING = "response";
}
