package com.importExpress.controller.service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cbt.warehouse.service.InventoryService;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class InventoryServiceTest {
	@Autowired
	private InventoryService inventoryService;
	@Test
	public void updateBarcode() {
		Map<String,Object> map = new HashMap<>();
		map.put("inid", 50);
		map.put("beforeBarcode", "SHCR001001004");
		map.put("afterBarcode", "SHCR003004001");
		map.put("remaining", 1);
		map.put("admid",1);
		int updateBarcode = inventoryService.updateBarcode(map);
		System.out.println(updateBarcode == 2);
	}

}
