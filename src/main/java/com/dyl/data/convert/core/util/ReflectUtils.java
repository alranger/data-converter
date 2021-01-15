package com.dyl.data.convert.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ReflectUtils {
    // 递归获取所有字段
    public static List<Field> getAllField(Object obj) {
        return getAllField(obj.getClass());
    }

    /**
     * 获取class的所有字段
     * @param clazz
     * @return
     */
    public static List<Field> getAllField(Class clazz){
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = clazz;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null  && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            //fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            Collections.addAll(fieldList, tempClass.getDeclaredFields());
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }

}
