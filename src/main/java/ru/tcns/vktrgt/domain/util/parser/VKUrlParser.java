package ru.tcns.vktrgt.domain.util.parser;

import ru.tcns.vktrgt.domain.external.vk.dict.VKUrlDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TIMUR on 30.04.2016.
 */
public class VKUrlParser {
    public static final VKUrlDto parseUrl (String url) {
        String regex = "^(https?:\\/\\/)?(vk.com)\\/([a-zA-Z]*)(-?\\d*)(_)?(\\d*)(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        matcher.find();
        VKUrlDto vkUrlDto = new VKUrlDto();
        try {
            vkUrlDto.setElementId(matcher.group(6));
            vkUrlDto.setOwnerId(matcher.group(4));
            vkUrlDto.setType(matcher.group(3));
        } catch (Exception e){}

        return vkUrlDto;
    }
    public static final String getName (String url) {
        String regex = "^([\\w:\\/.]*\\/)?(id\\d*|[\\w\\d_]*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        matcher.find();
        String res = "";
        try {
            res = matcher.group(2);

        } catch (Exception e){}
        return res;
    }
}
