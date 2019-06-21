package com.importExpress.controller;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import net.sf.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * *****************************************************************************************
 *
 * @ClassName PaysuccessinfoControllerTest
 * @Author: cjc
 * @Descripeion PaysuccessinfoController test
 * @Date： 2019/6/21 11:06:43
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       11:06:432019/6/21     cjc                       初版
 * ******************************************************************************************
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})

public class PaysuccessinfoControllerTest {
    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void getListInfo() throws Exception {

        mockMvc.perform(get("/paySuccessInfo/getListInfo")
                .param("page", "1")
                .param("rows", "10"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
