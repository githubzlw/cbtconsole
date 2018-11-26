package com.importExpress.mail;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.Map;

/**
 * @author luohao
 * @date 2018/10/23
 */
@Service
public class SendMailFactory {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger("maillog");

    @Autowired
    private SpringTemplateEngine thymeleafEngine;

    @Async
    public void sendMail(String TO, String BCC, String SUBJECT, String BODY) {

        SendMail mail;
        try {
            mail = new SendMailByAmazon();
            mail.sendMail(TO, BCC, SUBJECT, BODY);
        } catch (Exception e) {
            logger.error("SendMailByAmazon", e);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                logger.error("Thread.sleep", e);
            }
            try {
                mail = new SendMailByMailGun();
                mail.sendMail(TO, BCC, SUBJECT, BODY);
            } catch (Exception e1) {
                logger.error(" SendMailByMailGun faild:" + SUBJECT +" TO:"+TO);
                logger.error("SendMailByMailGun", e);
            }
        }
    }
    /**
     * @Title: sendMail
     * @Author:
     * @Despricetion:TODO
     * @Date: 2018/10/25 18:52
     * @Param: [TO 发往, BCC 秘密抄送, SUBJECT 邮件内容, model , templateType]
     * @Return: void
     */
    public void sendMail(String TO, String BCC, String SUBJECT, Map<String, Object> model, Enum<TemplateType> templateType) {

        sendMail(TO, BCC, SUBJECT, getHtmlContent(model, templateType));
    }

    private String getHtmlContent(Map<String, Object> model, Enum<TemplateType> templateType){

        String result="";
        if(model != null) {
            Context context = new Context();
            for (Map.Entry<String, Object> param : model.entrySet()) {
                context.setVariable(param.getKey(), param.getValue());
            }
            result=thymeleafEngine.process(templateType.toString(), context);
            logger.debug(result);
        }
        return result;
    }

}
