package com.importExpress.utli;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.testng.Assert;

import static org.junit.Assert.*;

public class SendMQTest {

    @Test
    public void testSendMsg() {
        SendMQ.sendMsg(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='111' WHERE PID='1'"));
    }

    @Test
    public void sendMsgByRPC() {
        Assert.assertEquals("1",SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='222' WHERE PID='1'")));
    }

    @Test
    public void sendMsgByRPC2() {
        Assert.assertEquals("0",SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='222' WHERE PID='xxxxxxxx'")));
    }

    @Test
    public void sendMsgByRPC3() {
        Assert.assertEquals("2",SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='333' WHERE PID='3'")));
    }

    @Test
    public void sendMsgByRPC4() {
        Assert.assertEquals("-1",SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET xxxxxx='333' WHERE PID='3'")));
    }
}
