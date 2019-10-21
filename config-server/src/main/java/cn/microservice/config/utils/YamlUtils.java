package cn.microservice.config.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * 
 * yml和properties互转
 *
 * @author zb
 * @since 1.0.0
 */
public class YamlUtils {

    /**
     * Yaml转Properties格式
     *
     * @param yamlStr
     *            yml String
     * @return java.util.Properties
     */
    public static Properties yamlToProperties(String yamlStr) {
        Properties props = new Properties();
        yamlToProperties(props, new Yaml().loadAs(yamlStr, Map.class), null);
        return props;
    }

    /**
     * Yaml转Properties格式
     *
     * @param file
     *            yml file
     * @return java.util.Properties
     */
    public static Properties yamlToProperties(File file) {
        Properties props = new Properties();
        try {
            yamlToProperties(props, new Yaml().loadAs(new FileInputStream(file), Map.class), null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("properties 文件异常");
        }
        return props;
    }

    /**
     * Yaml转Properties格式.
     *
     * @param props
     *            待转java.util.Properties
     * @param map
     *            key value键值对
     * @param path
     *            key的path；map=[name=spring-user],path=spring.application, the
     *            result in Properties is spring.application.name=spring-user
     * @author zhangbo
     * @since 1.0.0
     */
    public static void yamlToProperties(Properties props, Map<String, Object> map, String path) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.hasLength(path)) {
                key = path + "." + key;
            }
            Object val = entry.getValue();
            if (val instanceof Map) {
                yamlToProperties(props, (Map<String, Object>) val, key);
            } else if (val instanceof List) {
                int index = 0;
                for (Object v : (List) val) {
                    props.put(key + "[" + index + "]", v == null ? "" : v.toString());
                    index++;
                }
            } else {
                props.put(key, val == null ? "" : val.toString());
            }
        }
    }

    /**
     * Properties转Yaml格式
     *
     * @param props
     *            java.util.Properties
     * @return Yaml格式的Map
     */
    public static Map<String, Object> propertiesToYamlMap(Properties props) {
        Map<String, Object> map = new TreeMap<>();
        for (Object key : props.keySet()) {
            List<String> keyList = Arrays.asList(((String) key).split("\\."));
            String value = props.getProperty((String) key);
            if (keyList.size() > 1) {
                Map<String, Object> valueMap = createTree(keyList, map);
                valueMap.put(keyList.get(keyList.size() - 1), value);
            } else {
                map.put(keyList.get(0), value);
            }
        }
        return map;
    }

    private static Map<String, Object> createTree(List<String> keys, Map<String, Object> map) {
        Map<String, Object> valueMap = (Map<String, Object>) map.get(keys.get(0));
        if (valueMap == null) {
            valueMap = new HashMap<>();
        }
        map.put(keys.get(0), valueMap);
        Map<String, Object> out = valueMap;
        if (keys.size() > 2) {
            out = createTree(keys.subList(1, keys.size()), valueMap);
        }
        return out;
    }

    /**
     * 转换为yml格式的字符串.
     *
     * @param map
     *            ymlMap
     * @return yml String
     * @author zhangbo
     * @since 1.0.0
     */
    public static String convertYamlString(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        convertYamlString(sb, map, 0);
        return sb.toString();
    }

    /**
     * 转换为yml格式的字符串.
     *
     * @param properties
     *            java.util.Properties
     * @return yml格式的string
     * @author zhangbo
     * @since 1.0.0
     */
    public static String convertYamlString(Properties properties) {
        return convertYamlString(propertiesToYamlMap(properties));
    }

    private static void convertYamlString(StringBuffer sb, Map<String, Object> map, int count) {
        Set<String> set = map.keySet();
        for (Object key : set) {
            Object value = map.get(key);
            for (int i = 0; i < count; i++) {
                sb.append("    ");
            }
            if (value instanceof Map) {
                sb.append(key + ":\n");
                convertYamlString(sb, (Map) value, count + 1);
            } else if (value instanceof List) {
                for (Object obj : (List) value) {
                    for (int i = 0; i < count; i++) {
                        System.out.print("    ");
                        sb.append("    ");
                    }
                    sb.append("    - " + transfer2SingleQuotes(obj.toString()) + "\n");
                }
            } else {
                sb.append(key + ": " + transfer2SingleQuotes(value.toString()) + "\n");
            }
        }
    }

    private static String transfer2SingleQuotes(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }
        return addSingleQuotes(deleteDoubleQuotes(value));
    }

    private static String deleteDoubleQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String addSingleQuotes(String value) {
        if (value.startsWith("\'") && value.endsWith("\'")) {
            return value;
        } else if (value.indexOf("'") != -1) {
            return value;
        }
        return "'" + value + "'";
    }
}