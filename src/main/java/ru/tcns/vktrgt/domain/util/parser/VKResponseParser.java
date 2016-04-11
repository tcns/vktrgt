package ru.tcns.vktrgt.domain.util.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.tcns.vktrgt.domain.external.vk.dict.GroupType;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timur on 3/26/16.
 */
public final class VKResponseParser {
    private VKResponseParser(){}

    public static String parseAuthResponse(String response) throws JSONException{
        JSONObject object = new JSONObject(response);
        return object.getString("access_token");
    }
    public static GroupUserResponse parseGroupUserResponse(String response) throws JSONException{
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        GroupUserResponse userResponse = new GroupUserResponse();
        userResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("users");
        ArrayList<Long> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i<jsonArray.length(); i++) {
            items.add(jsonArray.getLong(i));
        }
        userResponse.setUserIds(items);
        return userResponse;
    }

    public static GroupResponse parseGroupSearchResponse(String response) throws JSONException{
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("items");
        ArrayList<Group> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject jsonGroup = jsonArray.getJSONObject(i);
            Group group = getGroupFromJson(jsonGroup);
            items.add(group);
        }
        groupResponse.setGroups(items);
        return groupResponse;
    }

    public static List<Group> parseGroupGetByIdesponse(String response) throws JSONException{
        JSONObject object = new JSONObject(response);
        JSONArray jsonArray = object.getJSONArray("response");
        ArrayList<Group> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject jsonGroup = jsonArray.getJSONObject(i);
            Group group = getGroupFromJson(jsonGroup);
            if (group.getId()!=null) {
                items.add(group);
            }
        }
        return items;
    }

    private static Group getGroupFromJson(JSONObject jsonGroup) {
        Group group = new Group();
        if (jsonGroup.getInt("is_closed")== VKDicts.OPEN_GROUP) {
            try {
                group.setId(jsonGroup.getLong("gid"));
                group.setName(jsonGroup.getString("name"));
                group.setScreenName(jsonGroup.getString("screen_name"));
                group.setType(GroupType.get(jsonGroup.getString("type")));
                group.setPhoto50(jsonGroup.getString("photo"));
                group.setPhoto100(jsonGroup.getString("photo_medium"));
                group.setPhoto200(jsonGroup.getString("photo_big"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        return group;
    }

}
