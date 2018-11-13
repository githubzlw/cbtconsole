package com.importExpress.mail;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.MediaType;

/**
 * @author luohao
 * @date 2018/10/23
 */
public class SendMailByMailGun implements SendMail {

    private final static String MAILGUN_DOMAIN_NAME = "mg.import-express.com";
    private final static String MAILGUN_API_KEY = "key-5af11fc491becf8970b5c8eb45bbf6af";
    private final static String MAIL_GUN_ADDRESS = "Import-Express.com<admin@importx.com>";
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SendMailByMailGun.class);
    private static HostnameVerifier hv = (urlHostName, session) -> {
        logger.info("Warning: URL Host: " + urlHostName + " vs. "
                + session.getPeerHost());
        return true;
    };


    protected SendMailByMailGun() {

    }

    public static void main(String[] args) {
        try {
            final String BODY = String.join(
                    System.getProperty("line.separator"),
                    "<h1>Amazon SES SMTP Email Test</h1>",
                    "<p>This email was sent with Amazon SES using the ",
                    "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
                    " for <a href='https://www.java.com'>Java</a>."
            );
            new SendMailByMailGun().sendMail("luohao518@163.com", "", "Mail Gun test (SMTP interface accessed using Java)", BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMail(String TO, String BCC, String SUBJECT, String BODY) throws Exception {
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", MAILGUN_API_KEY));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/" + MAILGUN_DOMAIN_NAME + "/messages");
        FormDataMultiPart formData = new FormDataMultiPart();
        formData.field("from", MAIL_GUN_ADDRESS);
        formData.field("to", TO);
        if (StringUtils.isNotBlank(BCC)) {
            formData.field("bcc", BCC);
        }
        formData.field("subject", SUBJECT);
        formData.field("html", BODY);
        int res = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, formData).getStatus();
        if (res != 200) {
            logger.error("Send email faild:" + SUBJECT);
            throw new RuntimeException("Send email faild");
        }
    }

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
