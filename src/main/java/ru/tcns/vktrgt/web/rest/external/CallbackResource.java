package ru.tcns.vktrgt.web.rest.external;

import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.config.AuthFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by TIMUR on 19.06.2016.
 */
@RestController
@RequestMapping("/")
public class CallbackResource {

    @Inject
    AuthFactory authFactory;
    @CrossOrigin
    @RequestMapping(value = "/Callback",
        method = RequestMethod.GET)
    public void getCallback(HttpServletRequest request) {
        String code = request.getParameter("code");
        authFactory.signal(code);
    }
}
