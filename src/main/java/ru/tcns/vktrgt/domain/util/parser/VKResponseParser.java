package ru.tcns.vktrgt.domain.util.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.tcns.vktrgt.domain.external.vk.dict.GroupType;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.*;
import ru.tcns.vktrgt.domain.external.vk.response.*;
import ru.tcns.vktrgt.domain.util.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timur on 3/26/16.
 */
public final class VKResponseParser {
    private VKResponseParser() {
    }

    public static String parseAuthResponse(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        return object.getString("access_token");
    }

    public static List<User> parseUsersResponse(String response) throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JSONObject object = new JSONObject(response);
        JSONArray jsonArray = object.getJSONArray("response");
        List<User> users = new ArrayList<>();
        ResponseParser<User> userResponseParser = new ResponseParser<>(User.class);
        for (int i = 0; i<jsonArray.length(); i++) {
            users.add(userResponseParser.parseObject(jsonArray.getJSONObject(i)));
        }
        return users;
    }

    public static CommonIDResponse parseCommonResponseWithCount(String response) throws JSONException{
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        CommonIDResponse commonIDResponse = new CommonIDResponse();
        commonIDResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("items");
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 0; i<jsonArray.length(); i++) {
            items.add(jsonArray.getInt(i));
        }
        commonIDResponse.setItems(items);
        return commonIDResponse;
    }

    public static List<Group> parseGroupGetByIdResponse(String response) throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(response);
        JSONObject object = new JSONObject(response);
        JSONArray jsonArray = object.optJSONArray("response");
        if (jsonArray!=null) {
            ArrayList<Group> items = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonGroup = jsonArray.getJSONObject(i);
                Group group = new ResponseParser<>(Group.class).parseObject(jsonGroup);
                if (group.getId() != null && group.getId() != 0L) {
                    items.add(group);
                }
            }
            return items;
        }
        return null;
    }




    public static SubscriptionsResponse parseUserSubscriptions(String response) {
        JSONObject object = new JSONObject(response);
        System.out.println(response);
        JSONObject obj = object.optJSONObject("response");
        if (obj!=null) {
            SubscriptionsResponse subscriptionsResponse = new SubscriptionsResponse();
            JSONObject users =  obj.optJSONObject("users");
            if (users!=null) {
                JSONArray jsonArray = users.optJSONArray("items");
                if (jsonArray != null) {
                    ArrayList<Integer> items = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Integer id = jsonArray.getInt(i);
                        items.add(id);
                    }
                    subscriptionsResponse.setUsers(items);
                }
            }
            JSONObject groups =  obj.optJSONObject("groups");
            if (groups!=null) {
                JSONArray jsonArray = groups.optJSONArray("items");
                if (jsonArray != null) {
                    ArrayList<Integer> items = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Integer id = jsonArray.getInt(i);
                        items.add(id);
                    }
                    subscriptionsResponse.setGroups(items);
                }
            }
            return subscriptionsResponse;

        }
        return null;
    }
}
