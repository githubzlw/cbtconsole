package com.importExpress.controller.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.importExpress.service.OverseasWarehouseStockService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class OverseasWarehouseStockServiceTest {
	@Autowired
	private OverseasWarehouseStockService service;
	
	@Test
	public void testReduceOrderStock() {
		String orderno = "2191218K637_1_H";
		String shipno = "9200190221828141009887";
		String remark = "订单orderno/odid已出运-"+shipno+",释放其占用的库存orderStock";
		service.reduceOrderStock(orderno, 0, remark);
		
	}

}
