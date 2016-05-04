package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import java.io.IOException;

/**
 * Created by TIMUR on 03.05.2016.
 */
public abstract class AbstractVKUserService implements VKUserService {
    protected CommonIDResponse getFollowers(int userId, int offset, int count) {
        try {
            String url = USERS_PREFIX + "getFollowers?user_id=" + userId + "&offset=" + offset
                + "&count=" + count;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = VKResponseParser.parseCommonResponseWithCount(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommonIDResponse();
    }

}
