package com.dyl.data.convert.module;

import com.dyl.data.convert.core.util.BeanUtil;

import com.dyl.data.convert.module.pojo.Area;
import com.dyl.data.convert.module.pojo.BriefVO;
import com.dyl.data.convert.module.pojo.Company;
import com.dyl.data.convert.module.pojo.CompanyVO;
import org.junit.jupiter.api.Test;

public class ConvertTest {

    @Test
    public void getCompany(){
        Company company = createCompany();
        CompanyVO companyVO = BeanUtil.beanToBean(company, CompanyVO.class);
        System.out.println(companyVO);
    }

    private Company createCompany(){
        Area area = createArea();
        /*Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "dyl");
        map.put("code", "alranger");*/
        BriefVO dict = new BriefVO(1L, "dyl",  "alranger" );
        Company company = new Company(1L, "京东集团", "{\"id\":67,\"name\":\"集团\",\"code\":\"1\"}", "[{\"id\":75,\"name\":\"上市公司\",\"code\":\"2\"},{\"id\":76,\"name\":\"互联网\",\"code\":\"3\"}]", "https://", "www.jd.com", "亦庄经济技术开发区科创十一街18号院", area, dict);
        return company;
    }


    private Area createArea(){
        Area area = new Area(1L, "北京市", "北京市", 0, ",", null);
        return area;
    }
}
