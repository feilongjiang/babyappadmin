package com.example.happybaby.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.relational.core.sql.In;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String UnderlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 命名转为驼峰命名
     *
     * @param para
     *
     */

    public static String headerToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("");
        for (int i = 0; i < a.length; i++) {
            if (i == 0) a[i] = a[i].toUpperCase();
            result.append(a[i]);
        }
        return result.toString();
    }


    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i)) && i != 0) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Map转实体类共通方法
     *
     * @param type 实体类class
     * @param map  map
     * @return Object
     * @throws Exception
     */
    public static Object convertMap(Class type, Map<String, Object> map) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        Object obj = type.newInstance();
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            String filedType = descriptor.getPropertyType().getTypeName();
            if (map.containsKey(propertyName)) {
                Object value = map.get(propertyName);
                if (value == "") {
                    value = null;
                }
               if (value != null && filedType.equals("java.lang.Long")) {
                    descriptor.getWriteMethod().invoke(obj, Long.parseLong(value.toString()));
                } else {
                    descriptor.getWriteMethod().invoke(obj, value);
                }
            }
        }
        return obj;
    }

    /**
     * 实体类转Map共通方法
     *
     * @param bean 实体类
     * @return Map
     * @throws Exception
     */
    public static Map convertBean(Object bean) throws Exception {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}
