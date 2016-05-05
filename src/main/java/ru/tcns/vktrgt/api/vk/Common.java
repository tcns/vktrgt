package ru.tcns.vktrgt.api.vk;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;

/**
 * Created by timur on 3/29/16.
 */
@Service
public class Common {

    private static String token=null;
    private static  String CLIENT_ID="5385314";
    public static String CLIENT_SECRET="secret";
    private static String URL_PREFIX="https://oauth.vk.com/";


    public static String getToken() {
        if(token==null) {
            try {
                String s = "https://oauth.vk.com/authorize?client_id=5385314&v=5.52&grant_type=client_credentials&redirect_uri=localhost&scope=friends&response_type=token";
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
