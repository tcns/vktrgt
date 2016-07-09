package ru.tcns.vktrgt.web.rest.external;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.config.AuthFactory;
import ru.tcns.vktrgt.config.Constants;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by TIMUR on 19.06.2016.
 */
@Controller
public class CallbackResource {

    @Inject
    AuthFactory authFactory;

    @CrossOrigin
    @RequestMapping(value = "/Callback",
        method = RequestMethod.GET)
    public void getCallback(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        try {
            String baseUrl = String.format("%s://%s:%d",request.getScheme(),  request.getServerName(), request.getServerPort());
            String url = "https://oauth.vk.com/access_token?client_id=" + Constants.VK_CLIENT_ID +
                "&client_secret=" + Constants.VK_CLIENT_SECRET + "&redirect_uri=" + baseUrl + "/Callback&" +
                "code=" + code;
            Response response = Request.Get(url).execute();
            String content = response.returnContent().asString();
            JSONObject object = new JSONObject(content);
            String accessToken = object.getString("access_token");
            request.getSession().setAttribute(Constants.VK_TOKEN, accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
