package ru.tcns.vktrgt.web.rest.external.vk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.config.Constants;
import ru.tcns.vktrgt.domain.SocialUserConnection;
import ru.tcns.vktrgt.repository.SocialUserConnectionRepository;
import ru.tcns.vktrgt.service.SocialService;
import ru.tcns.vktrgt.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @Autowired
    SocialUserConnectionRepository connectionRepository;
    @Autowired
    UserService userService;
    @RequestMapping(value = "/vk/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenRespone> getVKToken(HttpServletRequest request) {
        String ans = "";
        if (request.getSession().getAttribute(Constants.VK_TOKEN)!=null) {
            ans = (String)request.getSession().getAttribute(Constants.VK_TOKEN);
        }
        List<SocialUserConnection> map = connectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(userService.getUserWithAuthorities().getLogin(), "vkontakte");
        if (map != null && map.get(0) != null) {
            ans = map.get(0).getAccessToken();
        }
        TokenRespone tokenRespone = new TokenRespone();
        tokenRespone.setToken(ans);
        return new ResponseEntity<>(tokenRespone, HttpStatus.OK);
    }
    @RequestMapping(value = "/vk/token-standalone", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenRespone> getVKTokenStandalone(HttpServletRequest request) {
        String ans = "";
        if (request.getSession().getAttribute(Constants.VK_TOKEN_STANDALONE)!=null) {
            ans = (String)request.getSession().getAttribute(Constants.VK_TOKEN_STANDALONE);
        }
        TokenRespone tokenRespone = new TokenRespone();
        tokenRespone.setToken(ans);
        return new ResponseEntity<>(tokenRespone, HttpStatus.OK);
    }
}
