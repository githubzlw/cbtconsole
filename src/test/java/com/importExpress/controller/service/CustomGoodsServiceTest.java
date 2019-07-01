package com.importExpress.controller.service;

import com.cbt.service.CustomGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
@Transactional
@Rollback
public class CustomGoodsServiceTest {

    @Autowired
    private CustomGoodsService customGoodsService;

    @Test
    public void insertIntoGoodsImgUpLogTest(){
        customGoodsService.insertIntoGoodsImgUpLog("111","111",1,"test");
    }
}
