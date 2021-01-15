package com.dyl.data.convert.core.converter;

public enum DataType {
    STRING("STRING"),
    SINGLE("SINGLE"),  // 单个对象
    LIST("LIST"),
    SET("SET"),
    MAP("MAP")
    ;

    private String code;

    public String getCode() {
        return code;
    }

    DataType(String code) {
        this.code = code;
    }
}
