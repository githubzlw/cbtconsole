package com.importExpress.utli;


import com.importExpress.mail.MailBean;
import com.importExpress.mail.TemplateType;
import com.importExpress.mail.WelcomeMailTemplateBean;
import org.junit.Test;
import org.testng.Assert;


public class SendMQTest {

    @Test
    /**
     * 不带返回值
     */
    public void testSendMsg() {
        SendMQ.sendMsg(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='111' WHERE PID='1'"));
    }

    @Test
    /**
     * 更新成功1条
     */
    public void sendMsgByRPC() {
        Assert.assertEquals("1", SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='222' WHERE PID='1'")));
    }

    @Test
    /**
     * 更新成功0条
     */
    public void sendMsgByRPC2() {
        Assert.assertEquals("0", SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='222' WHERE PID='xxxxxxxx'")));
    }

    @Test
    /**
     * 更新成功多条
     */
    public void sendMsgByRPC3() {
        Assert.assertEquals("2", SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET SHOP_NO='333' WHERE PID='3'")));
    }

    @Test
    /**
     * 更新失败情况
     */
    public void sendMsgByRPC4() {
        Assert.assertEquals("-1", SendMQ.sendMsgByRPC(new RunSqlModel("UPDATE TEMP_TEST SET xxxxxx='333' WHERE PID='3'")));
    }

    @Test
    public void sendMail() throws Exception {
        MailBean mailBean = new MailBean();
        mailBean.setTest(true);
        mailBean.setSiteEnum(SiteEnum.KIDS);
        mailBean.setTo("luohao@kairong.com");
        mailBean.setTemplateType(TemplateType.WELCOME);

        WelcomeMailTemplateBean welcomeBean = new WelcomeMailTemplateBean();
        welcomeBean.setActivationCode("aabbcc");
        welcomeBean.setFrom("kairong");
        welcomeBean.setName("luohao");
        welcomeBean.setPass("pass");
        welcomeBean.setMailBean(mailBean);
        SendMQ.sendMail(welcomeBean);
    }
}
