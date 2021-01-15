package com.dyl.data.convert.module.pojo;

import com.dyl.data.convert.core.converter.ConverterField;
import com.dyl.data.convert.core.converter.Target;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CompanyVO extends BaseEntity<Long> {

        /**
         * 名称
         */
        private String name;

        /**
         * 类型
         */
        @ConverterField(target = BriefVO.class)
        private BriefVO type;

        /**
         * 标签
         */
        @ConverterField(target = BriefVO.class)
        private List<BriefVO> tags;

        /**
         * 官网url前缀
         */
        private String websitePrefix;

        /**
         * 官网url主体
         */
        private String website;

        /**
         * 官网url
         */
        @ConverterField(source = {"websitePrefix", "website"}, target = Target.class)
        private String websiteUrl;

        /**
         * 地址
         */
        private String address;

        /**
         * 所在区域
         */
        private Area area;

        /**
         * 详细地址
         */
        @ConverterField(source = {"area.fullName", "address"}, target = Target.class)
        private String detailAddress;

        /**
         * map测试
         */
        @ConverterField(target = Map.class)
        private Map<String, String> dict;
}
