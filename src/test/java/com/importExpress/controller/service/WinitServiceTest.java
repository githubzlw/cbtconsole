package com.importExpress.controller.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cbt.winit.api.model.WarehouseWrap;
import com.cbt.winit.api.service.WinitService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class WinitServiceTest {
	@Autowired
	private WinitService winitService;

	/*@Test
	public void queryWarehouse() {
		List<WarehouseWrap> queryWarehouse = winitService.queryWarehouse();
		queryWarehouse.stream().forEach(w->System.err.println(w.toString()));
		
	}*/
	@Test
	public void queryInventory() {
		WarehouseWrap wrap = WarehouseWrap.builder().code("US0001").id("1000008").build();
		int syncCount = winitService.queryInventory(wrap);
		
		System.out.println("syncCount:"+syncCount);
		
	}

}
