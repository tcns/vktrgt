package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.response.AudioResponse;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.VKErrorResponse;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import java.io.IOException;
import java.util.List;

/**
 * Created by TIMUR on 03.05.2016.
 */
public abstract class AbstractVKUserService implements VKUserService {
    protected CommonIDResponse getFollowers(int userId, int offset, int count) {
        try {
            String url = USERS_PREFIX + "getFollowers?user_id=" + userId + "&offset=" + offset
                + "&count=" + count + VERSION;
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

    protected AudioResponse getUserAudio(String userId, String token) throws VKException{
        try {
            String url = AUDIO_PREFIX + "get?owner_id=" + userId + "&count=6000&access_token=" + token + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            AudioResponse response = new ResponseParser<>(AudioResponse.class).parseResponseString(ans, RESPONSE_STRING);
            if (response!=null) {
                return response;
            } else {
                VKErrorResponse errorResponse = new ResponseParser<>(VKErrorResponse.class).parseResponseString(ans, ERROR_STRING);
                throw new VKException(errorResponse);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new AudioResponse();
    }

}
