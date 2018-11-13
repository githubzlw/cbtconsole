/**   
 *
 * @Title SaleProfitrateTest.java 
 * @Prject cbtconsole
 * @Package com.importExpress.test 
 * @Description TODO
 * @author cerong E-mail: saycjc@outlook.com  
 * @date 2018年4月24日 下午5:35:52 
 * @version V1.0   
 * Copyright © 2018 上海策融网络科技有限公司. All rights reserved.
 */ 
package com.importExpress.test;

import com.ibm.icu.text.SimpleDateFormat;
import com.importExpress.pojo.Saleprofitrate;
import com.importExpress.pojo.SaleprofitrateExample;
import com.importExpress.pojo.SaleprofitrateExample.Criteria;
import com.importExpress.service.SaleprofitrateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.fail;
/** 
* @author 作者 E-mail: saycjc@outlook.com
* @version 创建时间：2018年4月24日 下午5:35:52 
* 类说明 
*/

/**
 * @ClassName SaleProfitrateTest 
 * @Description TODO
 * @author cerong
 * @date 2018年4月24日 下午5:35:52  
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"classpath:applicationContext.xml"}) 
public class SaleProfitrateTest {
    @Autowired
    private SaleprofitrateService saleprofitrateService;
    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#countByExample(com.importExpress.pojo.SaleprofitrateExample)}.
     */
    @Test
    public void testCountByExample() {
        SaleprofitrateExample example = new SaleprofitrateExample();
        Criteria criteria = example.createCriteria();
        int count = saleprofitrateService.countByExample(example);
        System.out.println("条数："+count);
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#deleteByExample(com.importExpress.pojo.SaleprofitrateExample)}.
     */
    @Test
    public void testDeleteByExample() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#deleteByPrimaryKey(Integer)}.
     */
    @Test
    public void testDeleteByPrimaryKey() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#insert(com.importExpress.pojo.Saleprofitrate)}.
     */
    @Test
    public void testInsert() {

        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Saleprofitrate salePrifigeRate = new Saleprofitrate();
        salePrifigeRate.setGoodid("543335124010");
       /* salePrifigeRate.setUserid(2);
        salePrifigeRate.setSaleprice(new BigDecimal("13.43"));
        salePrifigeRate.setSourceprice(new BigDecimal("13.43"));
        salePrifigeRate.setSalesnum(2);
        salePrifigeRate.setUpdatatime(dt);
        salePrifigeRate.setCreattime(dt);*/
        int conunt = saleprofitrateService.insertSelective(salePrifigeRate);
//        saleprofitrateService.insertSelective(salePrifigeRate);
        System.out.println("插入条数："+ conunt);
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#insertSelective(com.importExpress.pojo.Saleprofitrate)}.
     */
    @Test
    public void testInsertSelective() {
        Saleprofitrate salePrifigeRate = new Saleprofitrate();
        salePrifigeRate.setGoodid("543335124010");
        int conunt = saleprofitrateService.insertSelective(salePrifigeRate);
        System.out.println("插入条数："+ conunt);
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#selectByExample(com.importExpress.pojo.SaleprofitrateExample)}.
     */
    @Test
    public void testSelectByExample() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#selectByPrimaryKey(Integer)}.
     */
    @Test
    public void testSelectByPrimaryKey() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#updateByExampleSelective(com.importExpress.pojo.Saleprofitrate, com.importExpress.pojo.SaleprofitrateExample)}.
     */
    @Test
    public void testUpdateByExampleSelective() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#updateByExample(com.importExpress.pojo.Saleprofitrate, com.importExpress.pojo.SaleprofitrateExample)}.
     */
    @Test
    public void testUpdateByExample() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#updateByPrimaryKeySelective(com.importExpress.pojo.Saleprofitrate)}.
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.importExpress.service.impl.SaleprofitrateServiceImpl#updateByPrimaryKey(com.importExpress.pojo.Saleprofitrate)}.
     */
    @Test
    public void testUpdateByPrimaryKey() {
        fail("Not yet implemented");
    }

}
 