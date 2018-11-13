package com.importExpress.mail;

public interface SendMail {

    void sendMail(String TO, String BCC, String SUBJECT, String BODY) throws Exception;
}
