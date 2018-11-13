package com.importExpress.test;

import com.importExpress.pojo.Saleprofitrate;
import com.importExpress.service.SaleprofitrateService;
import org.springframework.beans.factory.annotation.Autowired;

/** 
* @author 作者 E-mail: saycjc@outlook.com
* @version 创建时间：2018年4月25日 上午10:12:18 
* 类说明 
*/
public class SaleProfitrateMTest {
    
    @Autowired
    private SaleprofitrateService saleprofitrateService;
    public  void main(String[] args) {
        Saleprofitrate salePrifigeRate = new Saleprofitrate();
        salePrifigeRate.setGoodid("543335124010");
        int conunt = saleprofitrateService.insertSelective(salePrifigeRate);
        System.out.println("插入条数："+ conunt);
    }

}
 