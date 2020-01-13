package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.GoodsInfoUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.controller
 * @date:2020/1/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class PublishGoodsToOnlineThreadTest {

    private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
    @Autowired
    private CustomGoodsService customGoodsService;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }


    @Test
    public void publishTest() throws ExecutionException, InterruptedException {
        String pid = "1006741705";
        PublishGoodsToOnlineThread pbCallable = new PublishGoodsToOnlineThread(pid, customGoodsService, ftpConfig, 0, 1);

        FutureTask futureTask = new FutureTask(pbCallable);
        Thread thread = new Thread(futureTask);
        thread.start();
        if (!futureTask.isDone()) {
            System.out.println("task has not finished!");
        }
        System.out.println(futureTask.get());
        Assert.assertTrue("执行异常", (Boolean) futureTask.get());
    }


    @Test
    public void checkImgTest() {
        String pid = "564259575142";
        CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
        boolean isCheckImg = GoodsInfoUtils.checkOffLineImg(goods, 0);
        Assert.assertTrue("检查图片异常", isCheckImg);
    }

}