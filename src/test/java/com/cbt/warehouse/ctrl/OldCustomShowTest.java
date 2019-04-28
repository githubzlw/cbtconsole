package com.cbt.warehouse.ctrl;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml","classpath:spring-mvc.xml","classpath:applicationContext-mail.xml","classpath:applicationContext-shiro.xml"})
public class OldCustomShowTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Before
    public void setup() {

        this.mockMvc = webAppContextSetup(this.wac).build();
    }
    @Test
    public void getOldCustomShowTest() throws Exception {
        mockMvc.perform((get("/OldCustomShow/getOldCustomShow")
                .param("email", "")
                .param("staTime", "")
                .param("enTime", "")
                .param("id", "")
                .param("cuName", "")
                .param("page", "1")

        ))
                .andExpect(status().isOk()).andDo(print());
        ;
    }
    @Test
    public void getAlladmTest() throws Exception {
        mockMvc.perform((get("/OldCustomShow/getAlladm")

        ))
                .andExpect(status().isOk()).andDo(print());
        ;
    }

    @Test
    public void getCusOrderTest() throws Exception {
        mockMvc.perform((get("/OldCustomShow/getCusOrder")
                .param("usid", "15725")
                .param("order", "")
                .param("page", "1")
        ))
                .andExpect(status().isOk()).andDo(print());
        ;
    }
}
