package com.importExpress.mail;

import com.cbt.util.SysParamUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static String configPath= SysParamUtil.getParam("mail.save");

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYMMdd_HHmmss_SSS");

    @Async
    public void sendMail(String TO, String BCC, String SUBJECT, String BODY, Integer siteType) {

        SendMail mail;
        try {
            mail = new SendMailByAmazon(siteType);
            mail.sendMail(TO, BCC, SUBJECT, BODY);
        } catch (Exception e) {
            logger.error("SendMailByAmazon", e);
            /*try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                logger.error("Thread.sleep", e);
            }
            try {
                mail = new SendMailByMailGun(siteType);
                mail.sendMail(TO, BCC, SUBJECT, BODY);
            } catch (Exception e1) {
                logger.error(" SendMailByMailGun faild:" + SUBJECT +" TO:"+TO);
                logger.error("SendMailByMailGun", e);
            }*/
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
        // websiteType 1-importx网站; 2-kids网站; 3-pets网站;
        Integer siteType = (model.get("websiteType") != null && Integer.valueOf(model.get("websiteType").toString()) == 1)?1:2;
        sendMail(TO, BCC, SUBJECT, getHtmlContent(model, templateType), siteType);
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
            saveHtml(templateType.toString(), result);
        }
        return result;
    }

    private void saveHtml(String preFileName,String content){

        try {
            String strTime = LocalDateTime.now().format(formatter);
            String fileName = preFileName.replace("/", "_").replace("emailTemplate_","") + "_" + strTime + ".html";
            Path path = Paths.get(configPath,fileName);
            Path write = Files.write(path, content.getBytes());
            logger.info("save to html,path:[{}]",write);
        } catch (IOException e) {
            logger.error("saveHtml",e);
        }
    }

}
