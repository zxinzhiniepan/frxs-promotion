/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class OTCAssembleUtils {

    /*public static void assemble(Object sourcePojo, Object targetPojo) {
        if (targetPojo == null || sourcePojo == null) {
            return;
        }
        Map<String, Map> valueMaps = getFiledsInfo(sourcePojo);
        String[] dtoFields = getFiledName(targetPojo);
        for (String field : dtoFields) {
            Map<String, Object> valueMap = valueMaps.get(field);
            if (valueMap == null || valueMap.isEmpty()) {
                continue;
            }
            Class type = (Class) valueMap.get("type");
            Object value = valueMap.get("value");
            setFieldValue(field, value, type, targetPojo);
        }
    }*/

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    private static void setFieldValue(String fieldName, Object value, Class classes, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String setter = "set" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(setter, classes);
            method.invoke(o, new Object[]{value});
        } catch (Exception e) {
            return;
        }
    }

    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    private static Map<String, Map> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Map> mapList = new HashMap();
        Map<String, Object> infoMap;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            mapList.put(fields[i].getName(), infoMap);
        }
        return mapList;
    }
}
