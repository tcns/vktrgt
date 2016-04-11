package ru.tcns.vktrgt.api.vk;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;

/**
 * Created by timur on 3/29/16.
 */
public class Common {

    private static String token=null;
    private static  String CLIENT_ID="5385314";
    public static String CLIENT_SECRET="6u54vy2z5xAisi0lmDQ2";
    private static String URL_PREFIX="https://oauth.vk.com/";
    public static String getToken() {
        if(token==null) {
            try {
                String url = URL_PREFIX + "access_token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
                    + "&v=5.50&grant_type=client_credentials";
                Content content = Request.Get(url).execute().returnContent();
                String ans = content.asString();
                token = VKResponseParser.parseAuthResponse(ans);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return token;
    }
    public static String initialRequest() {
        try {
            String url = URL_PREFIX + "authorize?client_id=" + CLIENT_ID
                + "&v=5.50&response_type=code&scope=friends&redirect_uri=http://localhost";
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            token = VKResponseParser.parseAuthResponse(ans);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
