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

    public static CommonIDResponse parseCommonIDResponse(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONArray jsonArray = object.getJSONArray("response");
        CommonIDResponse commonIDResponse = new CommonIDResponse();
        commonIDResponse.setCount(jsonArray.length());
        ArrayList<Integer> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(jsonArray.getInt(i));
        }
        commonIDResponse.setItems(items);
        return commonIDResponse;
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
    public static FriendsResponse parseFriendsResponse(String response) throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        FriendsResponse friendsResponse = new FriendsResponse();
        friendsResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("items");
        ArrayList<User> users = new ArrayList<>();
        ResponseParser<User> userResponseParser = new ResponseParser<>(User.class);
        for (int i = 0; i<jsonArray.length(); i++) {
            users.add(userResponseParser.parseObject(jsonArray.getJSONObject(i)));
        }
        friendsResponse.setItems(users);
        return friendsResponse;
    }


    public static GroupResponse parseGroupSearchResponse(String response) throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("items");
        ArrayList<Group> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonGroup = jsonArray.getJSONObject(i);
            Group group = new ResponseParser<>(Group.class).parseObject(jsonGroup);
            items.add(group);
        }
        groupResponse.setItems(items);
        return groupResponse;
    }

    public static List<Group> parseGroupGetByIdResponse(String response) throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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

    public static Contact[] parseContacts(JSONArray json) {
        int len = 0;
        if (json != null) {
            len = json.length();
        }
        Contact[] contacts = new Contact[len];
        for (int i = 0; i<len; i++) {
            JSONObject object = json.getJSONObject(i);
            Contact contact = new Contact();
            contact.setDesc(object.optString("desc"));
            contact.setUserId(object.optLong("user_id"));
            contact.setEmail(object.optString("email"));
            contacts[i] = contact;
        }
        return contacts;
    }

    public static Market parseMarket(JSONObject from) {
        if (from!=null) {
            Market m = new Market();
            m.setContactId(from.optLong("contact_id"));
            m.setCurrencyId(from.optLong("currency_id"));
            m.setCurrencyName(from.optString("currency_name"));
            m.setEnabled(from.getInt("enabled") == 1);
            m.setMainAlbumId(from.optLong("main_album_id"));
            m.setPriceMax(from.optDouble("price_max"));
            m.setPriceMin(from.optDouble("price_min"));
            return m;
        }
        return null;

    }

    public static Place parsePlace(JSONObject from) {
        if (from != null) {
            Place place = new Place();
            place.setId(from.getLong("pid"));
            place.setAddress(from.optString("address"));
            place.setCity(from.optInt("city"));
            place.setCountry(from.optInt("country"));
            place.setLatitude(from.optDouble("latitude"));
            place.setLongitude(from.optDouble("longitude"));
            place.setTitle(from.optString("title"));
            place.setType(from.optString("type"));
            return place;
        }
        return null;

    }


    public static SubscriptionsResponse parseUserSubscriptions(String response) {
        JSONObject object = new JSONObject(response);
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
