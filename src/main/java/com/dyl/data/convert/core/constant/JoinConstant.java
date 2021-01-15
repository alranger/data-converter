package com.dyl.data.convert.core.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyl on 2020/7/16 14:53
 */
public class JoinConstant {

    public static final String SPOT = ".";

    /**
     * The constant UNDER_LINE.
     */
    public final static String HORIZONTAL_LINE = "-";

    /**
     * The constant UNDER_LINE.
     */
    public final static String UNDER_LINE = "_";

    /**
     * 左圆括号（英文）
     */
    public final static String LEFT_PARENTHESES_EN = "(";

    /**
     * 右圆括号（英文）
     */
    public final static String RIGHT_PARENTHESES_EN = ")";

    /**
     * 左圆括号（中文）
     */
    public final static String LEFT_PARENTHESES_ZH = "（";

    /**
     * 右圆括号（中文）
     */
    public final static String RIGHT_PARENTHESES_ZH = "）";

    /**
     * 左方括号（英文）
     */
    public final static String LEFT_SQUARE_BRACKET_EN = "[";

    /**
     * 右方括号（英文）
     */
    public final static String RIGHT_SQUARE_BRACKET_EN = "]";

    /**
     * 左方括号（中文）
     */
    public final static String LEFT_SQUARE_BRACKET_ZH = "【";

    /**
     * 右方括号（中文）
     */
    public final static String RIGHT_SQUARE_BRACKET_ZH= "】";

    /**
     * 左花括号（英文）
     */
    public final static String LEFT_CURLY_BRACE_EN = "{";

    /**
     * 右花括号（英文）
     */
    public final static String RIGHT_CURLY_BRACE_EN = "}";

    /**
     * 左花括号（中文）
     */
    public final static String LEFT_CURLY_BRACE_ZH = "{";

    /**
     * 右花括号（中文）
     */
    public final static String RIGHT_CURLY_BRACE_ZH= "}";

    // 将常量值放到list中
    public  final static List<Object> list = new ArrayList<>();;

    // private final static Map<String, Object> map = new HashMap<>();   // 将常量属性和值放到map中


    static {
        JoinConstant obj = new JoinConstant();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String typeName = field.getType().getName();
            if("java.lang.String".equals(typeName)){
                field.setAccessible(true);
                try {
                    list.add(field.get(obj));
                    // map.put(field.getName(), field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(list);
    }

}
