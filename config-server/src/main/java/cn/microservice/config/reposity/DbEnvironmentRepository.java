package cn.microservice.config.reposity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;

import com.alibaba.fastjson.JSON;

import cn.microservice.config.service.PersistenceService;

/**
 * 所有的环境拉取都是走的这个方法.
 *
 * @author zhangbo
 * @since 1.0.0
 */
public class DbEnvironmentRepository implements EnvironmentRepository {

    private static final Logger LOG = LoggerFactory.getLogger(DbEnvironmentRepository.class);

    @Autowired
    PersistenceService persistenceService;

    /**
     * 复写，读取环境走数据库方式.
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @param label
     *            label
     * @return 配置环境
     * @author zhangbo
     * @since 1.0.0
     */
    public Environment findOne(String application, String profile, String label) {
        LOG.info("读取配置application=[{}],profile=[{}],label=[{}]", application, profile, label);
        Properties properties = persistenceService.readProperties(application, profile, label);
        Environment result = new Environment(application, profile);
        result.setLabel(label);
        Map<String, String> propertyMap = new HashMap<>();
        for (Entry<Object, Object> entry : properties.entrySet()) {
            if (entry.getValue() != null) {
                parsePros(entry.getKey()
                        .toString(), entry.getValue(), propertyMap);
            }
        }
        PropertySource propertySource = new PropertySource(application + "_" + profile + "_" + label, propertyMap);
        result.add(propertySource);
        return result;
    }

    // 以[开头，以]结尾则认为是列表
    private static boolean isList(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof List) {
            return true;
        }
        Pattern p = Pattern.compile("^\\[.*\\]$");
        Matcher matcher = p.matcher(value.toString());
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    // 解析属性
    private static void parsePros(String key, Object value, Map<String, String> result) {
        if (isList(value)) {
            if (value instanceof String) {
                List<Object> tempCollection = JSON.parseArray(value.toString(), Object.class);
                parseStrProperties(key, tempCollection, result);
            }
        } else {
            result.put(key, value.toString());
        }
    }

    private static void parseStrProperties(String prefixOfKey, Object value, Map<String, String> resultMap) {
        if (value instanceof String || value instanceof Boolean || value instanceof Long || value instanceof Integer
                || value instanceof Float || value instanceof Double || value instanceof Short
                || value instanceof Character || value instanceof Byte) {
            resultMap.put(prefixOfKey, String.valueOf(value));
        } else if (value instanceof ArrayList<?>) {
            List<?> valueList = (ArrayList<?>) value;
            for (int i = 0; i < valueList.size(); i++) {
                parseStrProperties(prefixOfKey + "[" + i + "]", valueList.get(i), resultMap);
            }
        } else if (value instanceof LinkedHashMap<?, ?>) {
            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) value;
            for (Entry<?, ?> entry : map.entrySet()) {
                parseStrProperties(prefixOfKey + "." + entry.getKey()
                        .toString(), entry.getValue(), resultMap);
            }
        } else {
            LOG.error(String.format("properties属性解析时,key=%s 未知类型的value:%s", prefixOfKey, String.valueOf(value)));
        }
    }
}
