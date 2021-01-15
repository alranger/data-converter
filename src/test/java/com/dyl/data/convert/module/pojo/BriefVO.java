package com.dyl.data.convert.module.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简化返回entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BriefVO extends BaseEntity<Long> {

    private String name;

    private String code;

    public BriefVO(Long id, String name, String code) {
        super.setId(id);
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return "BriefVO{" +
                "id='" + super.getId() + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
