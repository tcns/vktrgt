package ru.tcns.vktrgt.web.rest.external;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.config.AuthFactory;
import ru.tcns.vktrgt.config.Constants;
import ru.tcns.vktrgt.domain.external.vk.dict.VKErrorCodes;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by TIMUR on 19.06.2016.
 */
@Controller
public class CallbackResource {

    @Inject
    AuthFactory authFactory;
    @Inject
    private Environment env;
    @Inject
    private GroupService groupService;
    @CrossOrigin
    @RequestMapping(value = "/Callback",
        method = RequestMethod.GET)
    public void getCallback(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        try {
            String baseUrl = String.format("%s://%s:%d",request.getScheme(),  request.getServerName(), request.getServerPort());
            if (Arrays.asList(env.getActiveProfiles()).contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                baseUrl = "https://vktrgt.herokuapp.com";
            }
            String url = "https://oauth.vk.com/access_token?client_id=" + Constants.VK_CLIENT_ID +
                "&client_secret=" + Constants.VK_CLIENT_SECRET + "&redirect_uri=" + baseUrl + "/Callback&" +
                "code=" + code;
            Response response = Request.Get(url).execute();
            String content = response.returnContent().asString();
            JSONObject object = new JSONObject(content);
            String accessToken = object.getString("access_token");
            /*String testResponse = null;
            try {
                groupService.searchVk("test", accessToken);
            } catch (VKException ex) {
                if (ex.getVkErrorResponse().getErrorCode()== VKErrorCodes.VALIDATION_REQUIRED) {
                    Response response2 = Request.Get(ex.getVkErrorResponse().getRedirectUri()).execute();
                    System.out.println("redirect uri: " + ex.getVkErrorResponse().getRedirectUri());
                    String content2 = response2.returnContent().asString();
                    System.out.println(content2);
                    JSONObject object2 = new JSONObject(content2);
                    accessToken = object2.getString("access_token");
                }
            }*/
            request.getSession().setAttribute(Constants.VK_TOKEN, accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
