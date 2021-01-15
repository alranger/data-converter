package com.dyl.data.convert.core.converter;

/**
 * 数据来源
 */
public enum OriginType {
    OPPOSITE("OPPOSITE"), // 对方
    SELF("SELF"),    // 自身
    ;

    private String code;

    public String getCode() {
        return code;
    }

    OriginType(String code) {
        this.code = code;
    }
}
