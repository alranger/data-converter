package com.dyl.data.convert.core.converter;


import com.alibaba.fastjson.JSON;
import com.dyl.data.convert.core.constant.JoinConstant;
import com.dyl.data.convert.core.util.BeanUtil;
import com.dyl.data.convert.core.util.JsonUtils;
import com.dyl.data.convert.core.util.ReflectUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 解析ConverterField
 */
public class ConverterFieldAnalysis {

    // 连接符号
    private static List<Object> joinConsts = JoinConstant.list;

    /**
     *  字段名称相同，类型不同的赋值
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T convertFormat(T source, T target){
        if(source == null){
            return null;
        }
        List<Field> fields = ReflectUtils.getAllField(target);
        for (Field field : fields) {
            ConverterField annotation = field.getAnnotation(ConverterField.class);
            if(null == annotation){
                continue;
            }
            try {
                String[] sources = annotation.source();
                PropertyDescriptor targetPd = new PropertyDescriptor(field.getName(), target.getClass());
                if(null != sources && sources.length > 0){
                    StringBuilder sb = sourceAnalysis(source, target, sources, annotation);
                    Method writeMethod = targetPd.getWriteMethod();
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(target, sb.toString());
                }else {
                    PropertyDescriptor sourcePd = new PropertyDescriptor(field.getName(), source.getClass());
                    targetAnalysis(source, target, sourcePd, targetPd, annotation);
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
                continue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return target;
    }

    /**
     * 对注解中source()的处理
     * @param source
     * @param target
     * @param sources
     * @param annotation
     * @param <T>
     * @return
     */
    private static <T> StringBuilder sourceAnalysis(T source, T target, String[] sources, ConverterField annotation){
        StringBuilder sb = new StringBuilder();
        try {
            if(annotation.origin() == OriginType.SELF){
                source = target;
            }
            for (String sourceField : sources) {
                // entity.attribute
                if (sourceField.indexOf(JoinConstant.SPOT) > 0) {
                    String[] attributes = StringUtils.split(sourceField, JoinConstant.SPOT);
                    T attributeSource = source;
                    for (String attribute : attributes) {
                        PropertyDescriptor sourcePd = new PropertyDescriptor(attribute, attributeSource.getClass());
                        Object sourceAttributeValue = getSourceValue(sourcePd, attributeSource);
                        if (null == sourceAttributeValue) {
                            return new StringBuilder();
                        }
                        if (getDataType(sourcePd) == DataType.SINGLE) {
                            attributeSource = (T) sourceAttributeValue;
                            continue;
                        }
                        if (getDataType(sourcePd) == DataType.STRING) {
                            if(StringUtils.isBlank((String) sourceAttributeValue)){
                                return new StringBuilder();
                            }
                            sb.append(sourceAttributeValue);
                        }
                    }
                } else {  // 属性
                    if(joinConsts.contains(sourceField)){   // 包含常见的连接符，如（"-", "(", ")" 等）
                        sb.append(sourceField);
                    }else{
                        PropertyDescriptor sourceFieldPd = new PropertyDescriptor(sourceField, source.getClass());
                        Object sourceFieldValue = getSourceValue(sourceFieldPd, source);
                        if (getDataType(sourceFieldPd) == DataType.STRING) {
                            if(StringUtils.isBlank((String) sourceFieldValue)){
                                return new StringBuilder();
                            }
                            sb.append(sourceFieldValue);
                        }
                    }
                }
            }

        }catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 对注解中target()的处理
     * @param source
     * @param target
     * @param sourcePd
     * @param targetPd
     * @param annotation
     * @param <T>
     */
    private static <T> void targetAnalysis(T source, T target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd, ConverterField annotation){
        Object sourceValue = getSourceValue(sourcePd, source);
        if (sourceValue == null ) {
            return;
        }
        if(sourceValue instanceof String && StringUtils.isBlank((String)sourceValue)){
            return;
        }
        try {
            if (targetPd.getName().equals(sourcePd.getName()) && targetPd.getPropertyType() != sourcePd.getPropertyType()) {
                Class<?> targetClass = annotation.target();
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                DataType targetType = getDataType(targetPd);

                if (sourceValue instanceof String) {
                    if (JsonUtils.isJson((String) sourceValue)) {
                        String value = (String) sourceValue;
                        switch (targetType) {
                            case SINGLE:
                                writeMethod.invoke(target, JsonUtils.toObject(value, targetClass));
                                break;
                            case LIST:
                                writeMethod.invoke(target, JSON.parseArray(value, targetClass));
                                break;
                            case SET:
                                writeMethod.invoke(target, new HashSet<>(JSON.parseArray(value, targetClass)));
                                break;
                            case MAP:
                                writeMethod.invoke(target, (Map) JSON.parse(value));
                                break;
                            default:
                        }
                    }
                    return;
                }
                if (sourceValue instanceof List) {
                    List<?> value = (List<?>) sourceValue;
                    switch (targetType) {
                        case LIST:
                            writeMethod.invoke(target, BeanUtil.listToList(value, targetClass));
                            break;
                        case SET:
                            writeMethod.invoke(target, new HashSet<>(BeanUtil.listToList(value, targetClass)));
                            break;
                        case MAP:
                            writeMethod.invoke(target, BeanUtil.objectsToMaps((List<T>) sourceValue));
                            break;
                        case SINGLE:
                            if (value.size() == 1) {
                                writeMethod.invoke(target, BeanUtil.beanToBean(value.get(0), targetClass));
                            }
                            break;
                        case STRING:
                            break;
                        default:
                    }
                    return;
                }
                if (sourceValue instanceof Set) {
                    List<?> value = new ArrayList<>((Set<?>) (sourceValue));
                    switch (targetType) {
                        case LIST:
                            writeMethod.invoke(target, BeanUtil.listToList(value, targetClass));
                            break;
                        case SET:
                            writeMethod.invoke(target, new HashSet<>(BeanUtil.listToList(value, targetClass)));
                            break;
                        case SINGLE:
                            if (value.size() == 1) {
                                writeMethod.invoke(target, BeanUtil.beanToBean(value.get(0), targetClass));
                            }
                            break;
                        case MAP:
                            writeMethod.invoke(target, BeanUtil.objectsToMaps((List<T>) sourceValue));
                            break;
                        case STRING:
                            break;
                        default:
                    }
                    return;
                }
                if (sourceValue instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) sourceValue;
                    switch (targetType) {
                        case LIST:
                            List<?> list = map.values().stream().collect(Collectors.toList());
                            writeMethod.invoke(target, list);
                            break;
                        case SET:
                            List<?> listValue = map.values().stream().collect(Collectors.toList());
                            writeMethod.invoke(target, new HashSet<>(listValue));
                            break;
                        case SINGLE:
                            writeMethod.invoke(target, BeanUtil.mapToObject((Map<String, Object>) sourceValue, targetClass));
                            break;
                        case STRING:
                            writeMethod.invoke(target, JsonUtils.toJson(sourceValue));
                            break;
                        default:
                    }
                    return;
                }
                DataType sourceType = getDataType(sourcePd);
                if (sourceType == DataType.SINGLE) {
                    // 以下是单个对象
                    switch (targetType) {
                        case SINGLE:
                            writeMethod.invoke(target, BeanUtil.beanToBean(sourceValue, targetClass));
                            break;
                        case STRING:
                            writeMethod.invoke(target, JsonUtils.toJson(sourceValue));
                        case MAP:
                            writeMethod.invoke(target, BeanUtil.beanToMap(sourceValue));
                        default:
                    }
                    return;
                }
            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 获取source字段的Value
     * @param sourcePd
     * @param source
     * @param <T>
     * @return
     */
    private static <T> Object getSourceValue(PropertyDescriptor sourcePd, T source){
        try {
            Method readMethod = sourcePd.getReadMethod();
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            return readMethod.invoke(source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取字段所属类型
     * @param pd
     * @return
     */
    private static DataType getDataType(PropertyDescriptor pd){
        JavaType javaType = TypeFactory.defaultInstance().constructType(pd.getPropertyType());
        if(String.class.isAssignableFrom(javaType.getRawClass())){
            return DataType.STRING;
        }else if (List.class.isAssignableFrom(javaType.getRawClass())) {
            return DataType.LIST;
        } else if (Set.class.isAssignableFrom(javaType.getRawClass())) {
            return DataType.SET;
        } else if (Map.class.isAssignableFrom(javaType.getRawClass())) {
            return DataType.MAP;
        }

        return DataType.SINGLE;
    }


}
