package ru.tcns.vktrgt.web.rest.external.vk;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.config.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TIMUR on 15.08.2016.
 */
@RestController
@RequestMapping("/api")
public class TokenResource {
    class TokenRespone {
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        private String token;
    }
    @RequestMapping(value = "/vk/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenRespone> getVKToken(HttpServletRequest request) {
        String ans = "";
        if (request.getSession().getAttribute(Constants.VK_TOKEN)!=null) {
            ans = (String)request.getSession().getAttribute(Constants.VK_TOKEN);
        }
        TokenRespone tokenRespone = new TokenRespone();
        tokenRespone.setToken(ans);
        return new ResponseEntity<>(tokenRespone, HttpStatus.OK);
    }
}
