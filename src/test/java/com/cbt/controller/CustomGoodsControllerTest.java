package com.cbt.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.controller
 * @date:2019/11/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class CustomGoodsControllerTest {

    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).build();
        loginFirst();
    }

    /**
     * 设置登录用户
     *
     * @throws Exception
     */
    private void loginFirst() throws Exception {
        //设置登录信息
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/userLogin/checkUserInfoss.do")
                    .param("userName", "ling")
                    .param("passWord", "123"))
                    //.andExpect(view().name("redirect:/website/main_menu.jsp"))
                    .andExpect(status().isOk()).andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryGoodsWeightList() throws Exception {
        MvcResult mRes = mockMvc.perform(get("/cutom/queryGoodsWeightList").param("pid", ""))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        System.err.println(mRes.getResponse().getContentAsString());
    }

    @Test
    public void syncLocalWeightToOnline() throws Exception {
        MvcResult mRes = mockMvc.perform(get("/cutom/syncLocalWeightToOnline")
                .param("goodsType", "Color:Blue@32161,Specification (length * width):S@324511")
                .param("pid", "37819281686")
                .param("id", "5")
                .param("volumeWeight", "0.65")
                .param("volumeWeight", "0.65"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        System.err.println(mRes.getResponse().getContentAsString());
    }
}
