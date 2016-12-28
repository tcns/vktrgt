package ru.tcns.vktrgt.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;
import ru.tcns.vktrgt.domain.User;
import ru.tcns.vktrgt.service.SocialService;
import ru.tcns.vktrgt.service.UserService;

import javax.inject.Inject;
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
    public RedirectView signUp(WebRequest webRequest, @CookieValue(name = "NG_TRANSLATE_LANG_KEY", required = false, defaultValue = "\"ru\"") String langKey) {
        try {
            Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
            socialService.createSocialUser(connection, langKey.replace("\"", ""));
            return new RedirectView(URIBuilder.fromUri("/#/social-register/" + connection.getKey().getProviderId())
                .queryParam("success", "true")
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
            return new RedirectView(URIBuilder.fromUri("/#/social-register/")
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
