package com.dyl.data.convert.module.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude={"parent", "children"})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class Area extends BaseEntity<Long> {
    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 名称
     */
    private String name;

    /**
     * 全名称
     */
    private String fullName;

    /**
     * 层级
     */
    private Integer grade;

    /**
     * 树路径
     */
    private String treePath;

    /**
     * 上级地区
     */
    @JsonIgnore
    private Area parent;

    /**
     * 下级地区
     */
    @JsonIgnore
    private Set<Area> children = new HashSet<>();

    public Area(Long id, String name, String fullName, Integer grade, String treePath, Area parent) {
        super.setId(id);
        this.name = name;
        this.fullName = fullName;
        this.grade = grade;
        this.treePath = treePath;
        this.parent = parent;
    }
}
