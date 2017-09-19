package ru.tcns.vktrgt.web.rest;


import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;
import ru.tcns.vktrgt.domain.User;
import ru.tcns.vktrgt.service.SocialService;
import ru.tcns.vktrgt.service.UserService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Map;

@RestController
@RequestMapping("/social")
public class SocialController {
    private final Logger log = LoggerFactory.getLogger(SocialController.class);

    @Inject
    private SocialService socialService;

    @Inject
    private ProviderSignInUtils providerSignInUtils;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public RedirectView signUp(WebRequest webRequest,
                               @RequestParam(name = "code", required = false)
                               @CookieValue(name = "CSRF-TOKEN", required = false, defaultValue = "") String csrf) {
        try {
            Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
            ServletWebRequest request = (ServletWebRequest) webRequest;
            socialService.createSocialUser(connection, "ru");
            /*String url = request.getRequest().getScheme() + "://" +
                request.getRequest().getServerName() + ":"+request.getRequest().getServerPort()+
                "signin/" + connection.getKey().getProviderId();
            Request.Post(url)
                .bodyForm(Form.form().add("_csrf",  csrf).build())
                .execute().returnContent();*/
            return new RedirectView(URIBuilder.fromUri("/social-register.html")
                .build().toString(), true);
        } catch (Exception e) {
            log.error("Exception creating social user: ", e);
            return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
                .queryParam("success", "false")
                .build().toString(), true);
        }
    }
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public RedirectView signIn(WebRequest webRequest) {
        Map<String, String[]> requestMap =  webRequest.getParameterMap();
        try {
            if (requestMap.keySet().contains("error")) {
                throw new Exception("error");
            }
            return new RedirectView(URIBuilder.fromUri("/#/")
                .queryParam("success", "true")
                .build().toString(), true);
        } catch (Exception e) {
            log.error("Exception creating social user: ", e);
            return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
                .queryParam("success", "false")
                .queryParam("error", requestMap.get("error")[0])
                .build().toString(), true);
        }
    }
}
