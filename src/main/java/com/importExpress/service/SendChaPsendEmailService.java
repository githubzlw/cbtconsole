package com.importExpress.service;

import com.cbt.website.util.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SendChaPsendEmailService {
    /**
     * @Title: sendChaPsendEmail
     * @Author: cjc
     * @Despricetion:TODO 发送替换邮件
     * @Date: 2018/12/29 10:37:58
     * @Param: [request, response]
     * @Return: com.cbt.website.util.JsonResult
     */
    JsonResult sendChaPsendEmail(String emailInfo, String email, String copyEmail, String orderNo, String userId, String title , String reason3);
}
