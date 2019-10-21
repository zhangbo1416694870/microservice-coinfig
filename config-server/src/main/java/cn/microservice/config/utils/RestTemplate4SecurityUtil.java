package cn.microservice.config.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * 解析带账号密码的链接，使得能够访问带security的服务.
 *
 * @author zhangbo
 * @since 1.0.0
 */
public class RestTemplate4SecurityUtil {
    /***
     * new RestTemplate instance for requesting,which need spring boot security
     * 
     * @param url
     *            url
     * @return RestTemplate instance
     * @author unknown
     */
    public static RestTemplate newRestTemplateInstance(String url) {
        RestTemplate restTemplate = new RestTemplate();
        Pattern p = Pattern.compile("://.*:.*@");
        Matcher m = p.matcher(url);
        if (m.find()) {
            String userNamePasswordString = url.substring(url.indexOf("://") + 3, url.indexOf("@"));
            String[] userNameAndPasswordArray = userNamePasswordString.split(":");
            String userName = userNameAndPasswordArray[0];
            String password = userNameAndPasswordArray[1];
            restTemplate.getInterceptors()
                    .add(new BasicAuthorizationInterceptor(userName, password));
        }
        return restTemplate;
    }
}
