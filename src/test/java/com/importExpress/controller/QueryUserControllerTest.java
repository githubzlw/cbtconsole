package com.importExpress.controller;

import com.importExpress.service.QueryUserService;
import com.importExpress.utli.SendMQ;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.controller
 * @date:2019/10/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class QueryUserControllerTest {


    @Autowired
    private QueryUserService queryUserService;


    @Test
    public void testGo() {

        // Executor theadPool = Executors.newCachedThreadPool();
        Map<String, String> map = new HashMap<>();
        int isFlag = 1;
        try {
            List<Integer> list = queryUserService.queryAllCheckout(isFlag);
            int count = 0;
            if (CollectionUtils.isNotEmpty(list)) {
                for (Integer userid : list) {
                    count++;
                    if (count % 5 == 0) {
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /*theadPool.execute(() -> {
                        SendMQ.sendAuthorizationFlagMqSql(userid, isFlag);
                        queryUserService.updateUserCheckout(userid, isFlag);
                    });*/
                    SendMQ.sendAuthorizationFlagMqSql(userid, isFlag);
                    queryUserService.updateUserCheckout(userid, isFlag);
                }
            }
            map.put("success", "true");
            map.put("message", "执行成功，size:" + list.size());
            list.clear();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", "false");
            map.put("message", e.getMessage());
        }
        System.err.println(map);

        int zoneFlag = 0;
        try {
            List<Integer> list = queryUserService.queryAllCheckout(zoneFlag);
            if (CollectionUtils.isNotEmpty(list)) {
                int count = 0;
                for (Integer userid : list) {
                    count++;
                    if (count % 5 == 0) {
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /*theadPool.execute(() -> {
                        SendMQ.sendAuthorizationFlagMqSql(userid, zoneFlag);
                        queryUserService.updateUserCheckout(userid, zoneFlag);
                    });*/
                    SendMQ.sendAuthorizationFlagMqSql(userid, zoneFlag);
                    queryUserService.updateUserCheckout(userid, zoneFlag);
                }
            }
            map.put("success", "true");
            map.put("message", "执行成功，size:" + list.size());
            list.clear();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", "false");
            map.put("message", e.getMessage());
        }
        System.err.println(map);
    }
}
