package cn.microservice.config.service.impl;

import static cn.microservice.config.pojo.mysql.tables.Property.PROPERTY;

import java.awt.Label;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import cn.microservice.config.conf.ConfigServerProperties;
import cn.microservice.config.conf.SecurityUserProperties;
import cn.microservice.config.dao.PropertyMapper;
import cn.microservice.config.pojo.mysql.tables.pojos.Property;
import cn.microservice.config.service.PersistenceService;
import cn.microservice.config.utils.RestTemplate4SecurityUtil;
import cn.microservice.config.utils.YamlUtils;

/**
 * 配置内容的数据持久化操作实现.
 *
 * @author zhangbo
 * @since 1.0.0
 */
@Service
public class DbPersistenceServiceImpl implements PersistenceService {

    @Autowired
    PropertyMapper propertyMapper;
    @Autowired
    ConfigServerProperties configPros;
    @Autowired
    SecurityUserProperties securityUserPros;

    /**
     * 获取某个配置文件
     *
     * @param application
     *            - 应用名称
     * @param profile
     *            - 环境名称
     * @param label
     *            - 版本名称
     * @return 配置文件
     * @author zhangbo
     * @since 1.0.0
     */
    @Override
    public Properties readProperties(String application, String profile, String label) {
        Property property = propertyMapper.selectAll(application, profile, label, PROPERTY.ACTIVE.eq(true))
                .iterator()
                .next();
        return property != null ? YamlUtils.yamlToProperties(property.getProperty()) : null;
    }

    /**
     * 删除某个环境下所有项目所有版本的配置文件
     *
     * @param profile
     *            profile
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    @Transactional
    @Override
    public int deleteAllByProfile(String profile) {
        return propertyMapper.updateBatch(PROPERTY.ACTIVE, false, PROPERTY.PROFILE.eq(profile));
    }

    /**
     * 删除某个项目，所有版本在所有环境下的所有配置文件
     *
     * @param application
     *            application
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    @Transactional
    @Override
    public int deleteAllByApplication(String application) {
        return propertyMapper.updateBatch(PROPERTY.ACTIVE, false, PROPERTY.APPLICATION.eq(application));
    }

    /**
     * 删除某个项目某个环境下的所有配置文件
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    @Transactional
    @Override
    public int deleteAllByApplicationAndProfile(String application, String profile) {
        return propertyMapper.updateBatch(PROPERTY.ACTIVE, false, PROPERTY.PROFILE.eq(profile)
                .and(PROPERTY.APPLICATION.eq(application)));
    }

    /**
     * 删除某个项目某个版本下所有的配置文件
     * <p>
     * （由于Label只属于一个项目，所以该操作就是删除某个项目某个版本在所有环境下的配置）
     *
     * @param label
     *            label
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    @Transactional
    @Override
    public int deletePropertiesByLabel(Label label) {
        // propertyMapper.updateBatch(PROPERTY.ACTIVE,false,
        // PROPERTY.LABEL.eq(field));
        return 0;
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
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    @Transactional
    @Override
    public int deleteAllByApplicationAndProfileAndLabel(String application, String profile, String label) {
        return propertyMapper.updateBatch(PROPERTY.ACTIVE, false, PROPERTY.PROFILE.eq(profile)
                .and(PROPERTY.APPLICATION.eq(application))
                .and(PROPERTY.LABEL.eq(label)));
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
    @Transactional
    @Override
    public void saveProperties(String application, String profile, String label, Properties update) {
        deleteAllByApplicationAndProfileAndLabel(application, profile, label);
        if (!propertyMapper.insertOne(application, profile, label, YamlUtils.convertYamlString(update))) {
            throw new RuntimeException(String.format("保存[%s]-[%s] [%s]配置失败", application, profile, label));
        }
        asynchronismNotifyClient2BusRefresh(application);
    }

    // 异步处理，刷新所有的配置
    protected void asynchronismNotifyClient2BusRefresh(String application) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";
                if (securityUserPros.getName() != null && securityUserPros.getPassword() != null) {
                    url = String.format("http://%s:%s@localhost:%s/bus/refresh?destination=%s",
                            securityUserPros.getName(), securityUserPros.getPassword(), configPros.getPort(),
                            application);
                } else {
                    url = String.format("http://localhost:%s/bus/refresh?destination=%s", securityUserPros.getName(),
                            securityUserPros.getPassword(), configPros.getPort(), application);
                }
                RestTemplate restTemplate = RestTemplate4SecurityUtil.newRestTemplateInstance(url);
                restTemplate.postForObject(url, null, Integer.class);
            }
        }).start();
        // TODO 线程最好根据项目要求各自处理一下
    }

    /**
     * 修改环境名之后需要操作的配置持久化内容
     *
     * @param oldName
     *            - 原环境名
     * @param newName
     *            - 新环境名
     * @author zhangbo
     * @retur 是否修改成功
     * @since 1.0.0
     */
    @Transactional
    @Override
    public Boolean updateProfileName(String oldName, String newName) {
        // TODO Auto-generated method stub
        return false;
    }

}
