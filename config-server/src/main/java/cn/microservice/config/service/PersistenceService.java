package cn.microservice.config.service;

import java.awt.Label;
import java.util.Properties;

/**
 * 配置内容的持久化操作.
 *
 * @author zhangbo
 * @since 1.0.0
 */
public interface PersistenceService {
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
    Properties readProperties(String application, String profile, String label);

    /**
     * 删除某个环境下所有项目所有版本的配置文件
     *
     * @param profile
     *            profile
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    int deleteAllByProfile(String profile);

    /**
     * 删除某个项目，所有版本在所有环境下的所有配置文件
     *
     * @param application
     *            application
     * @return 删除成功数
     * @author zhangbo
     * @since 1.0.0
     */
    int deleteAllByApplication(String application);

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
    int deleteAllByApplicationAndProfile(String application, String profile);

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
    int deletePropertiesByLabel(Label label);

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
    int deleteAllByApplicationAndProfileAndLabel(String application, String profile, String label);

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
    void saveProperties(String application, String profile, String label, Properties update);

    /**
     * 修改环境名之后需要操作的配置持久化内容
     *
     * @param oldName
     *            - 原环境名
     * @param newName
     *            - 新环境名
     * @author zhangbo
     * @return 是否修改成功
     * @since 1.0.0
     */
    Boolean updateProfileName(String oldName, String newName);

}