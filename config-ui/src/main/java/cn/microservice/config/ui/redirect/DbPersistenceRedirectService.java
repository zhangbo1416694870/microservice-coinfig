package cn.microservice.config.ui.redirect;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.didispace.scca.core.domain.Env;
import com.didispace.scca.core.domain.EnvRepo;
import com.didispace.scca.core.domain.Label;
import com.didispace.scca.core.service.PersistenceService;
import com.didispace.scca.core.service.UrlMakerService;

/**
 * 复写持久化服务，支持带账号密码的访问链接.
 *
 * @author zhangbo
 * @since 1.0.0
 */
public class DbPersistenceRedirectService implements PersistenceService {

    @Autowired
    private UrlMakerService urlMakerService;

    @Autowired
    private EnvRepo envRepo;

    /**
     * 获取某个配置文件.
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @param label
     *            label
     * @return 配置属性
     * @author zhangbo
     * @since 1.0.0
     */
    public Properties readProperties(String application, String profile, String label) {
        String url = urlMakerService.configServerBaseUrl(profile)
                + "/readProperties?application={1}&profile={2}&label={3}";
        Properties properties = newRestTemplateInstance(url).getForObject(url, Properties.class, application, profile,
                label);
        return properties;
    }

    /**
     * 删除某个环境下所有项目所有版本的配置文件
     *
     * @param profile
     *            profile
     * @author zhangbo
     * @since 1.0.0
     */
    public void deletePropertiesByEnv(String profile) {
        // 删除某个环境下的所有配置
        String url = urlMakerService.configServerBaseUrl(profile) + "/deletePropertiesByEnv?profile={1}";
        Integer rows = newRestTemplateInstance(url).getForObject(url, Integer.class, profile);
    }

    /**
     * 删除某个项目，所有版本在所有环境下的所有配置文件
     *
     * @param application
     *            application
     * @author zhangbo
     * @since 1.0.0
     */
    public void deletePropertiesByProject(String application) {
        // 删除某个项目所有环境下的配置
        for (Env env : envRepo.findAll()) {
            String url = urlMakerService.configServerBaseUrl(env.getName())
                    + "/deletePropertiesByProject?application={1}";
            Integer rows = newRestTemplateInstance(url).getForObject(url, Integer.class, application);
        }
    }

    /**
     * 删除某个项目某个环境下的所有配置文件
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @author zhangbo
     * @since 1.0.0
     */
    public void deletePropertiesByProjectAndEnv(String application, String profile) {
        String url = urlMakerService.configServerBaseUrl(profile)
                + "/deletePropertiesByProjectAndEnv?application={1}&profile={2}";
        Integer rows = newRestTemplateInstance(url).getForObject(url, Integer.class, application, profile);
    }

    /**
     * 删除某个项目某个版本下所有的配置文件
     * <p>
     * （由于Label只属于一个项目，所以该操作就是删除某个项目某个版本在所有环境下的配置）
     *
     * @param label
     *            label
     * @author zhangbo
     * @since 1.0.0
     */
    public void deletePropertiesByLabel(Label label) {
        // TODO
        // int rows = propertyRepo.deleteAllByLabel(label);
        // log.info("delete project [{}] label [{}] property rows {}",
        // label.getProject().getName(), label.getName(), rows);
    }

    /**
     * 删除某个配置文件
     *
     * @param application
     *            - 应用名称
     * @param profile
     *            - 环境名称
     * @param label
     *            - 版本名称
     * @author zhangbo
     * @since 1.0.0
     */
    public void deletePropertiesByProjectAndEnvAndLabel(String application, String profile, String label) {
        String url = urlMakerService.configServerBaseUrl(profile)
                + "/deletePropertiesByProjectAndEnvAndLabel?application={1}&profile={2}&label={3}";
        Integer rows = newRestTemplateInstance(url).getForObject(url, Integer.class, application, profile, label);
    }

    /**
     * 保存某个配置文件
     *
     * @param application
     *            - 应用名称
     * @param profile
     *            - 环境名称
     * @param label
     *            - 版本名称
     * @param update
     *            - 更新的全量配置内容
     * @author zhangbo
     * @since 1.0.0
     */
    public void saveProperties(String application, String profile, String label, Properties update) {
        String url = urlMakerService.configServerBaseUrl(profile)
                + "/saveProperties?application={1}&profile={2}&label={3}";
        Integer rows = newRestTemplateInstance(url).postForObject(url, update, Integer.class, application, profile,
                label);
    }

    /**
     * 修改环境名之后需要操作的配置持久化内容
     *
     * @param oldName
     *            - 原环境名
     * @param newName
     *            - 新环境名
     * @author zhangbo
     * @since 1.0.0
     */
    public void updateProfileName(String oldName, String newName) {
        String url = urlMakerService.configServerBaseUrl(oldName) + "/updateProfileName?oldName={1}&newName={2}";
        Integer rows = newRestTemplateInstance(url).getForObject(url, Integer.class, oldName, newName);
    }

    protected RestTemplate newRestTemplateInstance(String url) {
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
