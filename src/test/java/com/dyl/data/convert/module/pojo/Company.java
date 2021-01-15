package com.dyl.data.convert.module.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity<Long> {

    /**
     * 名称
     */
    private String name;

    /**
     * 类型（json）
     */
    private String type;

    /**
     * 标签（json）
     */
    private String tags;

    /**
     * 官网url前缀
     */
    private String websitePrefix;

    /**
     * 官网url主体
     */
    private String website;

    /**
     * 地址
     */
    private String address;

    /**
     * 所在区域
     */
    private Area area;

    private BriefVO dict;

    public Company(Long id, String name, String type, String tags, String websitePrefix, String website, String address, Area area, BriefVO dict) {
        super.setId(id);
        this.name = name;
        this.type = type;
        this.tags = tags;
        this.websitePrefix = websitePrefix;
        this.website = website;
        this.address = address;
        this.area = area;
        this.dict = dict;
    }
}
