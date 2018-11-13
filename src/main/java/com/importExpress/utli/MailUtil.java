package com.importExpress.utli;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MailUtil {


	private final static String MAILGUN_DOMAIN_NAME;
	private final  static String MAILGUN_API_KEY;
	public final static String MAIL_GUN_ADDRESS;

	private static final Log MAILLOG = LogFactory.getLog(MailUtil.class);
	
	static{
		ResourceBundle resource = ResourceBundle.getBundle("resource",Locale.getDefault());
		MAILGUN_DOMAIN_NAME = resource.getString("MAILGUN_DOMAIN_NAME");
		MAILGUN_API_KEY = resource.getString("MAILGUN_API_KEY");
		//MAIL_GUN_ADDRESS = "Import-Express@" + MAILGUN_DOMAIN_NAME;
		MAIL_GUN_ADDRESS = "admin@importx.com";
//		System.out.println(MAILGUN_DOMAIN_NAME);
//		System.out.println(MAILGUN_API_KEY);
	}
	
	
	/**
	 * 
	 * @param subject 标题
	 * @param body 内容
	 * @param to 收件人
	 * @param cc 抄送人
	 * @param bcc 密送人
	 * @param filepath 附件路径列表
	 * @param trycount 重试次数
	 * @return int
	 */
	public static int sendMailByMailgun(String subject, String body, String to, String cc, String bcc, List<String> filepath, int trycount) {
		try {
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);  
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("api", MAILGUN_API_KEY));
			WebResource webResource = client.resource("https://api.mailgun.net/v3/" + MAILGUN_DOMAIN_NAME + "/messages");
			FormDataMultiPart formData = new FormDataMultiPart();
			formData.field("from", MAIL_GUN_ADDRESS);
			formData.field("to", to);
			if (cc!=null) {
				formData.field("cc", cc);
			}
			if (bcc!=null) {
				formData.field("bcc", bcc);
			}
			formData.field("subject", subject);
			//formData.field("text", body);
			System.out.println(body);
			formData.field("html", body);
			if (filepath!=null) {
				for (int i = 0; i < filepath.size(); i++) {
					File file = new File(filepath.get(i));
					formData.bodyPart(new FileDataBodyPart("attachment", file, MediaType.TEXT_PLAIN_TYPE));
				}
			}
			int res = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, formData).getStatus();
			//状态码,200:OK;400:Bad Request;具体参阅com.sun.jersey.api.client.ClientResponse
			if (res != 200 && trycount > 0) {
			    Thread.sleep(3000);
				sendMailByMailgun(subject,body, to, cc, bcc, filepath, trycount-1);
			}
		} catch (Exception e) {
            MAILLOG.error(String.format("发送邮件失败 ToMail:[%s],title:[%s]",to,subject) ,e);
			return -1;
		}
		return 0;
	}
	
	static HostnameVerifier hv = new HostnameVerifier() {  
        public boolean verify(String urlHostName, SSLSession session) {  
            System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
                               + session.getPeerHost());  
            return true;  
        }  
    };  
      
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
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
  
        public boolean isServerTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public boolean isClientTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
    }
	
}
