package cn.microservice.config.controller;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.microservice.config.service.PersistenceService;

/**
 * 数据库存储配置文件控制层操作.
 *
 * @author zhangbos
 * @since 1.0.0
 */
@RestController
@RequestMapping("${spring.cloud.config.server.prefix:}")
public class DbPropertyController {
    @Autowired
    PersistenceService persistenceService;

    private static final Logger LOG = LoggerFactory.getLogger(DbPropertyController.class);

    /**
     * 读取配置.
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @param label
     *            label
     * @return java.util.Properties
     * @author zhangbo
     * @since 1.0.0
     */
    @RequestMapping(path = "/readProperties", method = RequestMethod.GET)
    public Properties readProperties(@RequestParam String application, @RequestParam String profile,
            @RequestParam String label) {
        return persistenceService.readProperties(application, profile, label);
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
    @RequestMapping(path = "/deletePropertiesByEnv", method = RequestMethod.GET)
    public int deletePropertiesByEnv(@RequestParam String profile) {
        int rows = persistenceService.deleteAllByProfile(profile);
        LOG.info("delete env [{}] property rows {}", profile, rows);
        return rows;
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
    @RequestMapping(path = "/deletePropertiesByProject", method = RequestMethod.GET)
    public int deletePropertiesByProject(@RequestParam String application) {
        int rows = persistenceService.deleteAllByApplication(application);
        LOG.info("delete project [{}] property rows {}", application, rows);
        return rows;
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
    @RequestMapping(path = "/deletePropertiesByProjectAndEnv", method = RequestMethod.GET)
    public int deletePropertiesByProjectAndEnv(@RequestParam String application, @RequestParam String profile) {
        int rows = persistenceService.deleteAllByApplicationAndProfile(application, profile);
        LOG.info("delete project [{}] in env [{}] property rows {}", application, profile, rows);
        return rows;
    }

    /**
     * 未完成.
     *
     * @param label
     *            删除某个分支下所有配置
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    @Transactional
    @RequestMapping(path = "/deletePropertiesByLabel", method = RequestMethod.GET)
    public int deletePropertiesByLabel(@RequestParam String label) {
        // TODO
        int rows = 0;
        // int rows = persistenceService.deleteAllByLabel(label);
        return rows;
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
    @RequestMapping(path = "/deletePropertiesByProjectAndEnvAndLabel", method = RequestMethod.GET)
    public int deletePropertiesByProjectAndEnvAndLabel(@RequestParam String application, @RequestParam String profile,
            @RequestParam String label) {
        int rows = persistenceService.deleteAllByApplicationAndProfileAndLabel(application, profile, label);
        LOG.info("delete {}-{}-{} property rows {}", application, profile, label, rows);
        return rows;
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
    @RequestMapping(path = "/saveProperties", method = RequestMethod.POST)
    public int saveProperties(@RequestParam String application, @RequestParam String profile,
            @RequestParam String label, @RequestBody Properties update) {
        persistenceService.saveProperties(application, profile, label, update);
        return 1;
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
    @Transactional
    @RequestMapping(path = "/updateProfileName", method = RequestMethod.GET)
    public int updateProfileName(@RequestParam String oldName, @RequestParam String newName) {
        Boolean succeess = persistenceService.updateProfileName(newName, oldName);
        return succeess ? 1 : 0;
    }
}
