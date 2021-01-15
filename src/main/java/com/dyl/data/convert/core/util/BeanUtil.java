package com.dyl.data.convert.core.util;

import com.dyl.data.convert.core.converter.ConverterFieldAnalysis;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

public  class BeanUtil {

    /**
     * 将model转化为VO
     * @param <T>
     * @param source
     * @param clazz
     * @return
     */
    public static <T> T beanToBean(Object source, Class<T> clazz, String... ignoreProperties)  {
        if(source == null){
            return null;
        }
        T target = null;
        try {
            target = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //BeanUtils.copyProperties(source, target, ignoreProperties);
        ConverterFieldAnalysis.convertFormat(source, target);
        return target;
    }

    /**
    * 将对象装换为map
    * @param bean
    * @return
            */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将map转为对象
     * @param map
     * @param t
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> t){
        try {
            T instance = t.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(instance, map);
            return instance;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     * @param objList
     * @return
     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps,Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0,size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }


    public static <T, S> List<T> listToList(List<S> objList, Class<T> clazz, String... ignoreProperties)  {
        List<T> list = Lists.newArrayList();
        if (null != objList && !objList.isEmpty()) {
            objList.stream().forEach(source -> list.add(beanToBean(source, clazz, ignoreProperties)));
        }
        return list;
    }
}
