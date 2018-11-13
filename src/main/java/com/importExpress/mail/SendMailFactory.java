package com.importExpress.mail;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SendMailFactory.class);

    @Autowired
    private SpringTemplateEngine thymeleafEngine;

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
                logger.error("SendMailByMailGun", e);
            }
        }
    }

    public void sendMail(String TO, String BCC, String SUBJECT, Map<String, String> model, Enum<TemplateType> templateType) {

        sendMail(TO, BCC, SUBJECT, getHtmlContent(model, templateType));
    }

    private String getHtmlContent(Map<String, String> model, Enum<TemplateType> templateType){

        String result="";
        if(model != null) {
            Context context = new Context();
            for (Map.Entry<String, String> param : model.entrySet()) {
                context.setVariable(param.getKey(), param.getValue());
            }
            result=thymeleafEngine.process(templateType.toString(), context);
        }
        return result;
    }
}
