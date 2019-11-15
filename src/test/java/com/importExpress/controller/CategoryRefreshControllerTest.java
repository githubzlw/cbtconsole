package com.importExpress.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: 修改 applicationContext-base.xml 中为测试环境配置 dev/jdbc.properties
 * @date:2019/11/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class CategoryRefreshControllerTest {
    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;

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

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).build();
        loginFirst();
    }

    @Test
    public void queryAllCategoryByParamTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/category/queryAllCategoryByParam").param("cid", "0"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void getCidInfoTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/category/getCidInfo").param("cid", null))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void getCidInfo311Test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/category/getCidInfo").param("cid", "311"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void updateCategoryInfoTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/category/updateCategoryInfo")
                .param("cid", null)
                .param("oldCid", null)
                .param("parentCid", null)
                .param("chName", null)
                .param("enName", null))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void updateCategoryInfoChNameTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/category/updateCategoryInfo")
                .param("cid", "311")
                .param("oldCid", "")
                .param("parentCid", "")
                .param("chName", "童装-1")
                .param("enName", ""))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void changeCategoryDataTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/category/changeCategoryData")
                .param("oidCid", "311")
                .param("curCid", "1037649")
                .param("newCid", "1037011"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        System.err.println(mvcResult.getResponse().getContentAsString());
    }

}
