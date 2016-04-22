package ru.tcns.vktrgt.domain.util.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.tcns.vktrgt.domain.external.vk.dict.GroupType;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.*;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.util.DateUtils;

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
        ArrayList<Long> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(jsonArray.getLong(i));
        }
        commonIDResponse.setIds(items);
        return commonIDResponse;
    }

    public static FriendsResponse parseFriendsResponse(String response) throws JSONException{
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        FriendsResponse friendsResponse = new FriendsResponse();
        friendsResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("items");
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i<jsonArray.length(); i++) {
            users.add(parseUser(jsonArray.getJSONObject(i)));
        }
        friendsResponse.setItems(users);
        return friendsResponse;
    }

    public static User parseUser (JSONObject from) {
        User user = new User();
        user.setCity(parseCity(from.optJSONObject("city")));
        user.setDomain(from.optString("domain"));
        user.setFirstName(from.optString("first_name"));
        user.setId(from.optLong("id"));
        user.setLastName(from.optString("last_name"));
        return user;
    }

    public static City parseCity (JSONObject from) {

        if (from!=null) {
            City city = new City();
            city.setId(from.optInt("id"));
            city.setTitle(from.optString("title"));
            return city;
        }
        return null;
    }

    public static GroupUserResponse parseGroupUserResponse(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        GroupUserResponse userResponse = new GroupUserResponse();
        userResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("users");
        ArrayList<Long> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(jsonArray.getLong(i));
        }
        userResponse.setUserIds(items);
        return userResponse;
    }

    public static GroupResponse parseGroupSearchResponse(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONObject jsonResponse = object.getJSONObject("response");
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setCount(jsonResponse.getInt("count"));
        JSONArray jsonArray = jsonResponse.getJSONArray("items");
        ArrayList<Group> items = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonGroup = jsonArray.getJSONObject(i);
            Group group = getGroupFromJson(jsonGroup);
            items.add(group);
        }
        groupResponse.setGroups(items);
        return groupResponse;
    }

    public static List<Group> parseGroupGetByIdResponse(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONArray jsonArray = object.optJSONArray("response");
        if (jsonArray!=null) {
            ArrayList<Group> items = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonGroup = jsonArray.getJSONObject(i);
                Group group = getGroupFromJson(jsonGroup);
                if (group.getId() != null && group.getId() != 0L) {
                    items.add(group);
                }
            }
            return items;
        }
        return null;
    }

    private static Group getGroupFromJson(JSONObject jsonGroup) {
        Group group = new Group();
        if (jsonGroup.getInt("is_closed") == VKDicts.OPEN_GROUP &&
            !jsonGroup.has("deactivated")) {
            try {
                group.setMembersCount(jsonGroup.optInt("members_count"));
                group.setName(jsonGroup.optString("name"));
                if (group.getMembersCount() > VKDicts.MEMBER_COUNT_THRESHOLD &&
                    !VKDicts.NAME_DELETED.equals(group.getName())) {
                    group.setId(jsonGroup.getLong("gid"));
                    group.setScreenName(jsonGroup.optString("screen_name"));
                    group.setType(GroupType.get(jsonGroup.getString("type")));
                    group.setPhoto50(jsonGroup.optString("photo"));
                    group.setPhoto100(jsonGroup.optString("photo_medium"));
                    group.setPhoto200(jsonGroup.optString("photo_big"));
                    group.setActivity(jsonGroup.optString("activity"));
                    group.setCity(jsonGroup.optInt("city"));
                    group.setContacts(parseContacts(jsonGroup.optJSONArray("contacts")));
                    group.setCountry(jsonGroup.optInt("country"));
                    group.setDescription(jsonGroup.getString("description"));
                    if (GroupType.EVENT.equals(group.getType())) {
                        group.setFinishDate(DateUtils.parseUnixTime("" + jsonGroup.optLong("finish_date")));
                        group.setStartDate(DateUtils.parseUnixTime("" + jsonGroup.optLong("start_date")));
                    } else {
                        group.setStartDate(DateUtils.parseString(String.valueOf(jsonGroup.optInt("start_date")), VKDicts.VK_DATE_FORMAT));
                    }
                    if (jsonGroup.has("fixed_post")) {
                        group.setFixedPost(jsonGroup.getLong("fixed_post"));
                    }
                    group.setMainAlbumId(jsonGroup.optLong("main_album_id"));
                    group.setMainSection(jsonGroup.optLong("main_section"));
                    group.setMarket(parseMarket(jsonGroup.optJSONObject("market")));
                    group.setPlace(parsePlace(jsonGroup.optJSONObject("place")));
                    group.setPublicDateLabel(jsonGroup.optString("public_date_label"));
                    group.setScreenName(jsonGroup.optString("screen_name"));
                    group.setSite(jsonGroup.optString("site"));
                    group.setStatus(jsonGroup.optString("status"));
                    group.setVerified(jsonGroup.optInt("verified"));
                    group.setWikiPage(jsonGroup.optString("wiki_page"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return group;
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


}
