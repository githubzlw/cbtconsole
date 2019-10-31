package com.cbt.website.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

public class OrderwsDaoTest {

    private IOrderwsDao dao = new OrderwsDao();

    @Test
    public void addOrderDetails(){
        dao.addOrderDetails("881", "1", "9431534551274", "1431534551274", 1);

    }

}
