package com.dyl.data.convert.core.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于字段赋值的处理
 * 目前：source与target不共存，先判断source,有source则不处理target
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConverterField {

    /**
     * 主要用于处理字段名相同，类型不同的数据
     *  字段类型（最直接的类型）
     *  example:
     *  1、 @ConverterField(target = BasicUser.class)
     *      private List<BasicUser> leaders;
     *  2、 @ConverterField(target = BriefVO.class)
     *      private BriefVO type;
     */
    Class<?> target();

    /**
     * 用于处理数据拼接，获取对象字段用"." 分割，如entity.name
     * 数据来源
     * 当注解有source时； target= Target.class 是默认空值
     * @return
     */
    String[] source() default {};

    /**
     * 数据的来源对象，目前用于source()数据处理, field的值可以来自于自身的entity
     * @return
     */
    OriginType origin() default OriginType.OPPOSITE;

}
