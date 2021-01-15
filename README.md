# dataconverter
用注解方式给对象属性赋值：

1、两个对象的某些字段名相同，类型不同时的用法：

例如：
source：

    /**
     * 类型（json）
     */
    private String type;

    /**
     * 标签（json）
     */
    private String tags;
    
 target:

    @ConverterField(target = BriefVO.class)
    private BriefVO type;

    @ConverterField(target = BriefVO.class)
    private List<BriefVO> tags;
 
2、字符串拼接，
例如：

source:

    /**
     * 地址
     */
    private String address;

    /**
     * 所在区域
     */
    private Area area;
    
 target:
 
    @ApiModelProperty("详细地址")
    @ConverterField(source = {"area.fullName", "address"}, target = Target.class)
    private String detailAddress;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("所在区域")
    private Area area;
