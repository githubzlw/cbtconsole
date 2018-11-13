package com.cbt.paypal.util;


import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lhao
 * @date 2018/4/20
 */
public final class PropertyUtils {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
    private static final String RESOURCE_FILE = "../../../../resourc.eproperties";
    private static final String PAYPAL_FILE = "../../../../paypal.properties";
    /**
     * 缓存配置文件里的值集合
     */
    private static List<Map<String, String>> lstValue = new CopyOnWriteArrayList<Map<String,String>>();

    /*
     * 缓存配置文件的值
     */
    static {
        lstValue.add(getStringStringHashMap(PropertyFileEnum.RESOURCE_PROPERTIES));
        lstValue.add(getStringStringHashMap(PropertyFileEnum.PAYPAL_PROPERTIES));
    }

    private PropertyUtils() {
        //禁止实例化
    }

    private static Map<String, String> getStringStringHashMap(PropertyFileEnum propertyFileEnum) {

        Properties propertiesResource = null;

        try {
            switch (propertyFileEnum) {
                case RESOURCE_PROPERTIES:
                    propertiesResource = PropertiesLoaderUtils.loadAllProperties(RESOURCE_FILE);
                    break;
                case PAYPAL_PROPERTIES:
                    propertiesResource = PropertiesLoaderUtils.loadAllProperties(PAYPAL_FILE);
                    break;
                default:
                    throw new IllegalStateException("no support type");
            }
        } catch (IOException ioe) {
            logger.error("！！！！！！！！！！！！！！读取配置文件失败", ioe);
        }
        Assert.notNull(propertiesResource);
        Map<String, String> map = new ConcurrentHashMap<String, String>(50);
        Enumeration<Object> keys = propertiesResource.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            map.put(key, propertiesResource.getProperty(key));
        }
        logger.info("配置文件" + propertyFileEnum + "加载：" + map.toString());
        return map;
    }

    /**
     * 从缓存中获取值
     */
    public static String getValueFromResourceFile(String key) {
        return getValue(PropertyFileEnum.RESOURCE_PROPERTIES,key);
    }

    /**
     * 从缓存中获取值
     */
    public static String getValueFromPaypelFile(String key) {
        return getValue(PropertyFileEnum.PAYPAL_PROPERTIES,key);
    }
    /**
     * 从缓存中获取值
     */
    public static String getValue(PropertyFileEnum propName, String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is null");
        }
        switch (propName) {
            case RESOURCE_PROPERTIES:
                return lstValue.get(0).get(key);
            case PAYPAL_PROPERTIES:
                return lstValue.get(1).get(key);
            default:
                throw new IllegalStateException("no support type");
        }
    }

    public enum PropertyFileEnum {
        RESOURCE_PROPERTIES, PAYPAL_PROPERTIES
    }


}