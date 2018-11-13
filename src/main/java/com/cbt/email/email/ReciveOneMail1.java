package com.cbt.email.email;

import com.cbt.email.entity.EmailReceive;
import com.cbt.email.entity.EmailUser;
import com.cbt.email.entity.GuestBook;
import com.cbt.email.service.*;
import com.cbt.email.util.JsoupGetData;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 有一封邮件就需要建立一个ReciveMail对象
 */
public class ReciveOneMail1 {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReciveOneMail1.class);
	
	private MimeMessage mimeMessage = null;
	private Message iMessage = null;
	private String saveAttachPath = "F://ybFile"; // 附件下载后的存放目录
	private StringBuffer bodytext = new StringBuffer();// 存放邮件内容
	private String dateformat = "yy-MM-dd HH:mm"; // 默认的日前显示格式
	
	
	public ReciveOneMail1() {
	}

	/**
	 * 【设置附件存放路径】
	 */

	public void setAttachPath(String attachpath) {
		this.saveAttachPath = attachpath;
	}

	/**
	 * 【获得附件存放路径】
	 */
	public String getAttachPath() {
		return saveAttachPath;
	}

	/**
	 * 【设置日期显示格式】
	 */
	public void setDateFormat(String format) throws Exception {
		this.dateformat = format;
	}

	public ReciveOneMail1(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}
	public ReciveOneMail1(Message iMessage) {
		this.iMessage = iMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	/**
	 * 获得发件人的地址和姓名
	 */
	public String getFrom() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null)
			from = "";
		String personal = address[0].getPersonal();
		if (personal == null)
			personal = "";
		//String fromaddr = personal + from ;
		String fromaddr = from ;
		return fromaddr;
	}

	/**
	 * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
	 */
	public String getMailAddress(String type) throws Exception {
		String mailaddr = "";
		String addtype = type.toUpperCase();
		InternetAddress[] address = null;
		if (addtype.equals("TO") || addtype.equals("CC")
				|| addtype.equals("BCC")) {
			if (addtype.equals("TO")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.TO);
			} else if (addtype.equals("CC")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String email = address[i].getAddress();
					if (email == null)
						email = "";
					else {
						email = MimeUtility.decodeText(email);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
						personal = "";
					else {
						personal = MimeUtility.decodeText(personal);
					}
					 // 将个人描述信息与邮件地址连起来
                   // String compositeto = personal + "<" + email + ">";
					 String compositeto = personal + "<" + email;
					//String compositeto = email;
					//String compositeto = personal + email;
					mailaddr += "," + compositeto;
				}
				//mailaddr = mailaddr.substring(1);
			}else {//如果没有抄送人
				mailaddr = null;
			}
		} else {
			throw new Exception("Error emailaddr type!");
		}
		return mailaddr;
	}

	/**
	 * 获得邮件主题
	 */
	public String getSubject() throws MessagingException {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (Exception exce) {
		}
		return subject;
	}

	/**
	 * 获得邮件发送日期
	 */
	public Timestamp getSentDate() throws Exception {
		Date sentdate = mimeMessage.getSentDate();
		
		Timestamp tm = new Timestamp(sentdate.getTime());
		
		LOG.warn("邮件发送日期:"+tm);
		//SimpleDateFormat sf = new SimpleDateFormat(dateformat);
		//java.sql.Date sd = new java.sql.Date(sentdate.getTime());
		return tm;
	}

	/**
	 * 获得邮件正文内容
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	
	
	/**
	 　　*　解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件
	 　　*　主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 　　*/
	 public void getMailContent(Part part)throws Exception{
	  String contenttype=part.getContentType();
	  int nameindex=contenttype.indexOf("name");
	  boolean conname=false;
	  if(nameindex!=-1)conname=true;
	  if(part.isMimeType("text/plain")&&!conname){
	   bodytext.append((String)part.getContent());   
	  }
	  else if(part.isMimeType("multipart/*")){
	   Multipart multipart=(Multipart)part.getContent();
	   int counts=multipart.getCount();
	   for(int i=0;i<counts;i++){
	    getMailContent(multipart.getBodyPart(i));
	   }
	  }else if(part.isMimeType("message/rfc822")){
	   getMailContent((Part)part.getContent());
	  }
	  else{}
	 }
	
	
	public void getMailContent(Part part,StringBuffer htmlContent,Map<String,String> imageMap) throws Exception {
		String contentType = part.getContentType();
		int nameIndex = contentType.indexOf("name");
		if (part.isMimeType("text/html") && nameIndex==-1) {
			htmlContent.append((String)part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i),htmlContent,imageMap);
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent(),htmlContent,imageMap);
		} else if (contentType.contains("image/")) {
			String id="cid:"+part.getHeader("Content-ID");
		    
			
			//String id="cid:"+part.getHeader("Content-ID")[0].replace("<", "").replace(">", "");
	    	/* String root ="E:"+File.separator+"myproject"+File.separator+".metadata"
			 +File.separator+".plugins"+File.separator+"org.eclipse.wst.server.core"+File.separator+"tmp3"
	    			 +File.separator+"wtpwebapps"+File.separator+"NBEmail"+File.separator+"images"+File.separator;*/
			 
	    	 String root ="E:"+File.separator+"DevSoft"+File.separator+"tomcat7"
	    			 +File.separator+"webapps"+File.separator+"NBEmail"+File.separator+"images"+File.separator;
	    	 //String src=
	    	 String src ="image"+new Date().getTime() + ".jpg";
	    	 imageMap.put(id,src);
	    	 
	    	 src=root+src;
	    	 
	    	 File file = new File(root);
	    	 if(!file.exists()){
	    		 file.mkdirs();
	    	 }
	    	 File image = new File(src);
	    	 if(!image.exists()){
	    		 image.createNewFile();
	    	 }
	    	 DataOutputStream output=null;
	    	 try{
	    		 output= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(image)));
		         com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) part.getContent();
		         byte[] buffer = new byte[1024];
		         int bytesRead;
		         while ((bytesRead = test.read(buffer)) != -1) {
		            output.write(buffer, 0, bytesRead);
		         }
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    	 }finally{
	    		 if(output!=null){
	    			 try{
	    				 output.close();
	    			 }catch(Exception e){
	    	    		 e.printStackTrace();
	    	    	 }
	    		 }
	    	 }
	        
	        
	      }
		else {}
	}
	public void getMail(Part part,StringBuffer htmlContent,Map<String,String> imageMap) throws Exception {
		String contentType = part.getContentType();
		int nameIndex = contentType.indexOf("name");
		if (part.isMimeType("text/html") && nameIndex==-1) {
			htmlContent.append((String)part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMail(multipart.getBodyPart(i),htmlContent,imageMap);
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMail((Part) part.getContent(),htmlContent,imageMap);
		} else if (contentType.contains("image/")) {
			//String id1="cid:"+part.getHeader("Content-ID");
			
			
			String id1="cid:"+part.getHeader("Content-ID")[0].replace("<", "").replace(">", "");
			/* String root ="E:"+File.separator+"myproject"+File.separator+".metadata"
			 +File.separator+".plugins"+File.separator+"org.eclipse.wst.server.core"+File.separator+"tmp3"
	    			 +File.separator+"wtpwebapps"+File.separator+"NBEmail"+File.separator+"images"+File.separator;*/
			
			String root ="E:"+File.separator+"DevSoft"+File.separator+"tomcat7"
					+File.separator+"webapps"+File.separator+"NBEmail"+File.separator+"images"+File.separator;
			//String src=
			String src ="image"+new Date().getTime() + ".jpg";
			imageMap.put(id1,src);
			
			src=root+src;
			
			File file = new File(root);
			if(!file.exists()){
				file.mkdirs();
			}
			File image = new File(src);
			if(!image.exists()){
				image.createNewFile();
			}
			DataOutputStream output=null;
			try{
				output= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(image)));
				com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) part.getContent();
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = test.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(output!=null){
					try{
						output.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			
		}
		else {}
	}
	
	
	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 */
	public void getMailContent2(MimeMessage message) throws Exception {
		// 如果该邮件是组合型"multipart/*"则可能包含附件等  
        if(message.isMimeType("multipart/*"))  
        {  
            Multipart multipart = (Multipart)message.getContent();  
            int bodyCounts = multipart.getCount();  
            for(int i = 0; i < bodyCounts; i++)  
            {  
                BodyPart bodypart = multipart.getBodyPart(i);  
                // 如果该BodyPart对象包含附件，则应该解析出来  
                if(bodypart.getDisposition() != null)  
                {  
                    String filename = bodypart.getFileName();  
                    if(filename.startsWith("=?"))  
                    {  
                        // 把文件名编码成符合RFC822规范  
                        filename = MimeUtility.decodeText(filename);  
                    }  
                 
                }  
            }  
        }  

	}

	/**
	 * 124. * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false" 125.
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage
				.getHeader("Disposition-Notification-To");
		if (needreply != null) {
			replysign = true;
		}
		return replysign;
	}

	/**
	 * 获得此邮件的Message-ID
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = ((Message) mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		LOG.warn("flags's length: " + flag.length);
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isnew = true;
				System.out.println("seen Message.......");
				break;
			}
		}
		return isnew;
	}

	/**
	 * 判断此邮件是否包含附件
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		String contentType = part.getContentType();
		if (part.isMimeType("multipart/mixed")) {
		//if (part.isMimeType("multipart/mixed")) {
				Multipart mp = (Multipart) part.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					BodyPart mpart = mp.getBodyPart(i);
					String disposition = mpart.getDisposition();
					if ((disposition != null)
							&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
									.equals(Part.INLINE))))
						attachflag = true;
					else if (mpart.isMimeType("multipart/*")) {
						attachflag = isContainAttach((Part) mpart);
					} else {
						String contype = mpart.getContentType();
						if (contype.toLowerCase().indexOf("application") != -1)
							attachflag = true;
						if (contype.toLowerCase().indexOf("name") != -1)
							attachflag = true;
					}
				}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}
	
	

	/**
	 * 【保存附件】
	 */
	public String saveAttachMent(Part part,int eid) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					
					LOG.warn("if:"+fileName);
					if (fileName != null){
						if (fileName.toLowerCase().indexOf("gb2312") != -1) {
							fileName = MimeUtility.decodeText(fileName);
						}else {
							fileName = MimeUtility.decodeText(fileName);
							//fileName = new String(fileName.getBytes("ISO-8859-1"),"GB2312");
							//LOG.warn("转码:"+fileName);
						}
						//保存附件
						fileName = saveFile(fileName, mpart.getInputStream());
						/*if(eid != 0) {
							//记录收件附件信息到数据库（这里主要是在邮件正文中可能会有图片，在这里解析的时候会直接将图片
							//作为附件解析并下载，这个时候的暂时解决办法是将附件保存下来，之后在显示正文的时候，可以根据
							//对应的邮件去作相应的显示，当然此时的图片是没有链接的
		                     IEmailReceiveFileService ifs = new EmailReceiveFileServiceImpl();
		                     EmailFile file = new EmailFile();
		                     file.setFileName(fileName);
		                     file.setEid(eid);
		                     //获取文件名称后面的文件组后一个.的下标（后缀名）
		                     int index = fileName.lastIndexOf(".");
		                     //获取文件类型
		                     String sb = fileName.substring(index+1);
							 //设置附件文件类型
							 file.setType(sb);
							 //标记为正文中被解析为附件的内容(当附件存储了) 
							 file.setFlag(1);
		                     ifs.addFile(file);
						}
					}*/
					//检测并替换附件中的敏感关键字
					//checkFile(fileName);
					}
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(mpart,eid);
				} else {
					fileName = mpart.getFileName();
					LOG.warn("else:"+fileName);
					if (fileName != null){
						if (fileName.toLowerCase().indexOf("GB2312") != -1) {
							fileName = MimeUtility.decodeText(fileName);
						}else{
							fileName = new String(fileName.getBytes("ISO-8859-1"),"GB2312");
							LOG.warn("转码:"+fileName);
						}
						//保存附件
						fileName = saveFile(fileName, mpart.getInputStream());
						/*if(eid != 0) {
							//记录收件附件信息到数据库（这里主要是在邮件正文中可能会有图片，在这里解析的时候会直接将图片
							//作为附件解析并下载，这个时候的暂时解决办法是将附件保存下来，之后在显示正文的时候，可以根据
							//对应的邮件去作相应的显示，当然此时的图片是没有链接的
		                     IEmailReceiveFileService ifs = new EmailReceiveFileServiceImpl();
		                     EmailFile file = new EmailFile();
		                     file.setFileName(fileName);
		                     file.setEid(eid);
		                     //获取文件名称后面的文件组后一个.的下标（后缀名）
		                     int index = fileName.lastIndexOf(".");
		                     //获取文件类型
		                     String sb = fileName.substring(index+1);
							 //设置附件文件类型
							 file.setType(sb);
							 //标记为正文中被解析为附件的内容(当附件存储了) 
							 file.setFlag(1);
		                     ifs.addFile(file);
						}
					}
					//检测并替换附件中的敏感关键字
					//checkFile(fileName);
					 * }
*/				}
			}
				}		
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent(),eid);
		}
		return fileName;
			
	}

	

	/**
	 * 【真正的保存附件到指定目录里】
	 */
	private String saveFile(String fileName, InputStream in) throws Exception {
		//获取本机的系统名
		String osName = System.getProperty("os.name");
		String storedir = getAttachPath();
		String separator = "";
		if (osName == null)
			osName = "";
		if (osName.toLowerCase().indexOf("win") != -1) {//windows系统的磁盘目录
			separator = "\\";
			if (storedir == null || storedir.equals(""))
				storedir = "F://ybFile";
		} else {//其它系统的磁盘目录
			separator = "/";
			storedir = "/tmp";
		}
		File storefile = new File(storedir + separator + fileName);
		
		LOG.warn("storefile's path: " +storefile.toString() );
		for(int i=0;storefile.exists();i++){//如果存在同名的附件，则在后面添加数字区分
			 //获取文件名称后面的文件组后一个.的下标（后缀名）
            int index = fileName.lastIndexOf(".");
            String sb = fileName.substring(0,index)+i;
            LOG.warn("sb:"+sb);
			fileName = sb+fileName.substring(index);
			storefile = new File(storedir+separator+fileName);
		}
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(storefile));
			bis = new BufferedInputStream(in);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write(c);
				bos.flush();
			}
			//保存好附件之后，将附件信息记录到数据库
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception("文件保存失败!");
		} finally {
			bos.close();
			bis.close();
		}
		return fileName;
	}
	
	
	
	
	
	/**
	 * 【保存过滤附件】
	 */
	public String saveAttachMenta(Part part,String eid) throws Exception {
		String fileName = "";
		String x="";
		String num=eid;
		x=num+".zip";
		String storedir1=getAttachPath();
		Map<String, File> map1 = new HashMap<String, File>();
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					
					LOG.warn("if:"+fileName);
					if (fileName != null){
						if (fileName.toLowerCase().indexOf("gb2312") != -1) {
							fileName = MimeUtility.decodeText(fileName);
						}else {
							fileName = MimeUtility.decodeText(fileName);
							//fileName = new String(fileName.getBytes("ISO-8859-1"),"GB2312");
							//LOG.warn("转码:"+fileName);
						}
						//保存附件
                       InputStream in=mpart.getInputStream();
						
						String osName = System.getProperty("os.name");
						String storedir = getAttachPath();
						String separator = "";
						if (osName == null)
							osName = "";
						if (osName.toLowerCase().indexOf("win") != -1) {//windows系统的磁盘目录
							separator = "\\";
							if (storedir == null || storedir.equals(""))
								storedir = "F://ybFile";
						} else {//其它系统的磁盘目录
							separator = "/";
							storedir = "/tmp";
						}
						
						File storefile = new File(storedir + separator+fileName );
						
						LOG.warn("storefile's path: " +storefile.toString() );
						for(int m=0;storefile.exists();m++){//如果存在同名的附件，则在后面添加数字区分
							 //获取文件名称后面的文件组后一个.的下标（后缀名）
						
				            int index = fileName.lastIndexOf(".");
				            String sb = fileName.substring(0,index)+m;
				            LOG.warn("sb:"+sb);
							fileName = sb+fileName.substring(index);
							
							storefile = new File(storedir+separator+fileName);
						}
						
						BufferedOutputStream bos = null;
						BufferedInputStream bis = null;
						try {
							bos = new BufferedOutputStream(new FileOutputStream(storefile));
							bis = new BufferedInputStream(in);
							int c;
							while ((c = bis.read()) != -1) {
								bos.write(c);
								bos.flush();
							}
							//保存好附件之后，将附件信息记录到数据库
							
						} catch (Exception exception) {
							exception.printStackTrace();
							throw new Exception("文件保存失败!");
						} finally {
							bos.close();
							bis.close();
						}
						map1.put(fileName,storefile);
						/*if(eid != 0) {
							//记录收件附件信息到数据库（这里主要是在邮件正文中可能会有图片，在这里解析的时候会直接将图片
							//作为附件解析并下载，这个时候的暂时解决办法是将附件保存下来，之后在显示正文的时候，可以根据
							//对应的邮件去作相应的显示，当然此时的图片是没有链接的
		                     IEmailReceiveFileService ifs = new EmailReceiveFileServiceImpl();
		                     EmailFile file = new EmailFile();
		                     file.setFileName(fileName);
		                     file.setEid(eid);
		                     //获取文件名称后面的文件组后一个.的下标（后缀名）
		                     int index = fileName.lastIndexOf(".");
		                     //获取文件类型
		                     String sb = fileName.substring(index+1);
							 //设置附件文件类型
							 file.setType(sb);
							 //标记为正文中被解析为附件的内容(当附件存储了) 
							 file.setFlag(1);
		                     ifs.addFile(file);
						}*/
					}
					//检测并替换附件中的敏感关键字
					//checkFile(fileName);
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMenta(mpart,eid);
				} else {
					fileName = mpart.getFileName();
					LOG.warn("else:"+fileName);
					if (fileName != null){
						if (fileName.toLowerCase().indexOf("GB2312") != -1) {
							fileName = MimeUtility.decodeText(fileName);
						}else{
							fileName = new String(fileName.getBytes("ISO-8859-1"),"GB2312");
							LOG.warn("转码:"+fileName);
						}
						//保存附件
					
						InputStream in=mpart.getInputStream();
						
						String osName = System.getProperty("os.name");
						String storedir = getAttachPath();
						String separator = "";
						if (osName == null)
							osName = "";
						if (osName.toLowerCase().indexOf("win") != -1) {//windows系统的磁盘目录
							separator = "\\";
							if (storedir == null || storedir.equals(""))
								storedir = "F://ybFile";
						} else {//其它系统的磁盘目录
							separator = "/";
							storedir = "/tmp";
						}
						int num1=10000;
						x=num1+".zip";
						File storefile = new File(storedir + separator+fileName );
						//File storefile = new File(storedir + separator + fileName);
						
						LOG.warn("storefile's path: " +storefile.toString() );
						for(int n=0;storefile.exists();n++){//如果存在同名的附件，则在后面添加数字区分
							 //获取文件名称后面的文件组后一个.的下标（后缀名）
							
				            int index = fileName.lastIndexOf(".");
				            String sb = fileName.substring(0,index)+n;
				            LOG.warn("sb:"+sb);
							fileName = sb+fileName.substring(index);
							
							storefile = new File(storedir+separator+fileName);
						}
						BufferedOutputStream bos = null;
						BufferedInputStream bis = null;
						try {
							bos = new BufferedOutputStream(new FileOutputStream(storefile));
							bis = new BufferedInputStream(in);
							int c;
							while ((c = bis.read()) != -1) {
								bos.write(c);
								bos.flush();
							}
							//保存好附件之后，将附件信息记录到数据库
							
						} catch (Exception exception) {
							exception.printStackTrace();
							throw new Exception("文件保存失败!");
						} finally {
							bos.close();
							bis.close();
						}
						map1.put(fileName,storefile);
						/*if(eid != 0) {
							//记录收件附件信息到数据库（这里主要是在邮件正文中可能会有图片，在这里解析的时候会直接将图片
							//作为附件解析并下载，这个时候的暂时解决办法是将附件保存下来，之后在显示正文的时候，可以根据
							//对应的邮件去作相应的显示，当然此时的图片是没有链接的
		                     IEmailReceiveFileService ifs = new EmailReceiveFileServiceImpl();
		                     EmailFile file = new EmailFile();
		                     file.setFileName(fileName);
		                     file.setEid(eid);
		                     //获取文件名称后面的文件组后一个.的下标（后缀名）
		                     int index = fileName.lastIndexOf(".");
		                     //获取文件类型
		                     String sb = fileName.substring(index+1);
							 //设置附件文件类型
							 file.setType(sb);
							 //标记为正文中被解析为附件的内容(当附件存储了) 
							 file.setFlag(1);
		                     ifs.addFile(file);
						}*/
					}
					//检测并替换附件中的敏感关键字
					//checkFile(fileName);
				}
			}
			byte[] buffer = new byte[1024];  
			 
		       //生成的ZIP文件名为Demo.zip  
		 
		       String strZipName = storedir1+"//"+x;  
		       
		       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipName));  
		 
		       //需要同时下载的两个文件result.txt ，source.txt  
		       
		       //System.out.print(strs);
		       //File[] file1 = {new File("D:/13.jpg"),new File("D:/14.jpg"),new File("D:/16.jpg"),new File("D:/17.jpg"),new File("D:/19.jpg")};  
		     
		       Set<Entry<String, File>> set = map1.entrySet();// 取得键值对的对象set集合
				for (Entry<String, File> en : set) {// 遍历键值对的集合
					FileInputStream fis = new FileInputStream(en.getValue());  
					 
			           out.putNextEntry(new ZipEntry(en.getKey()));  
			 
			           int len;  
			 
			           //读入需要下载的文件的内容，打包到zip文件  
			 
			          while((len = fis.read(buffer))>0) {  
			 
			           out.write(buffer,0,len);   
			 
			          }  
			 
			           out.closeEntry();  
			 
			           fis.close();  
			           
			       }  
				out.close();
			
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMenta((Part) part.getContent(),eid);
		}
		
		
	
		
		return x;
	}
	
	
	
	
	
	
	
	
	
    
    
    
   
    /**     
     *  通过递归删除html标签     
     *   @param content - 包含HTML标签的内容      
     *   @author Jack, 2014-05-15.     
     *   @return 不带HTML标签的文本内容     
    */   
    public static String removeHtmlTag(String content) {
    	Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
    	Matcher m = p.matcher(content);
    	if (m.find()) {
    		content = content.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
    		content = removeHtmlTag(content);
    	}
    	return content;
    }
    
    
    /** 
     * 把html内容转为文本 
     * @param html 需要处理的html文本 
     * @param filterTags 需要保留的html标签样式 
     * @return 
     */  
    public static String trimHtml2Txt(String html){      
        html = html.replaceAll("\\<head>[\\s\\S]*?</head>(?i)", "");//去掉head  
        html = html.replaceAll("\\<!--[\\s\\S]*?-->", "");//去掉注释  
        html = html.replaceAll("\\<![\\s\\S]*?>", "");  
        html = html.replaceAll("\\<style[^>]*>[\\s\\S]*?</style>(?i)", "");//去掉样式  
        html = html.replaceAll("\\<script[^>]*>[\\s\\S]*?</script>(?i)", "");//去掉js  
        html = html.replaceAll("\\<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");//去掉word标签  
        html = html.replaceAll("\\<xml>[\\s\\S]*?</xml>(?i)", "");  
        html = html.replaceAll("\\<html[^>]*>|<body[^>]*>|</html>|</body>(?i)", "");  
        //html = html.replaceAll("\\\r\n|\n|\r", " ");//去掉换行  
        html = html.replaceAll("\\<br[^>]*>(?i)", "\n");
        html = html.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
                        
        html = html.replaceAll("\\</p>(?i)", "\n");  
        html = html.replaceAll("\\<[^>]+>", "");  
       // html = ExStringUtils.replaceEach(html,s_tags.toArray(new String[s_tags.size()]),tags.toArray(new String[tags.size()]));  
        html = html.replaceAll("\\ ", " ");  
       // html = html.replaceAll(" ", "");//去掉空格
       // html = html.replaceAll("\\s*","");//去掉所有空格包括空白字符
        html = html.replaceAll("&nbsp", " ");
        return html.trim();  
    }  
    
   
  
    /**
     * 方法描述:接收并处理邮件
     * author:lzj
     * date:2015年6月8日
     */
    public static void pop(EmailUser user) {
    	Date d3 = new Date();
         long l3 = d3.getTime();
         String username = user.getEmailAddress();
         String imapserver = "secure.emailsrvr.com";
         Properties props = new Properties(); 
         props.put("mail.imap.host",imapserver);
         props.put("mail.imap.auth.plain.disable","true");//不知道为什么，以plain方式登录出错，我就禁用它了
         props.put("mail.imap.port", 143);
         Session mailsession=Session.getInstance(props,null); 
         mailsession.setDebug(false);
         // 创建Session实例对象 
         Session session = Session.getInstance(props);
         IMAPFolder folder  = null;
         IMAPStore store  = null; 
         try {
        	 store = (IMAPStore) session.getStore("imap"); // 使用imap会话机制，连接服务器
        	 store.connect(imapserver, username, user.getEmailPWD());
             //store.connect(username, user.getEmailPWD()); 
           folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
	    	//folder =  store.getFolder("INBOX"); // 收件箱  
		    folder.open(Folder.READ_WRITE);
		    // 获取总邮件数  
		    int total = folder.getMessageCount();  
		    LOG.warn("--------------"+user.getUserName()+"共有邮件：" + total + " 封--------------"); 
	        Message[] messages = folder.getMessages();
	        int messageNumber = 0;  
	        ReciveOneMail1 pmm = null;
	        for (Message _message : messages) {  
	        	if(!_message.getFolder().isOpen()) { //判断是否open   
	        		_message.getFolder().open(Folder.READ_WRITE); //如果close
	        		
	        	}
	            Flags flags = _message.getFlags(); 
	            
	            if (flags.contains(Flags.Flag.SEEN)) {
	            	//已读的邮件不处理
	            	//LOG.warn("这是一封已读邮件");  
	            }else { 
	            	String path="E://email";
	            	
	                pmm = new ReciveOneMail1((MimeMessage) _message);
	                String title = pmm.getSubject();
	                if("Undelivered Mail Returned to Sender".equals(title)){}else{
	               Map<String, String> map = new HashMap<String, String>();
         		   // map.put("fileupload", value);
	                //每封邮件都有一个MessageNumber，可以通过邮件的MessageNumber在收件箱里面取得该邮件  
	                messageNumber = _message.getMessageNumber();
	             
	                	EmailReceive receive = new EmailReceive();
	                	
	                	receive.setTitle(title);
	                	//获取收件人
	                	String from=pmm.getFrom();
	 	            	//receive.setReceiver(pmm.getMailAddress("to"));
	 	            	receive.setEmail(pmm.getFrom());
	 	            	
	 	            	
	 	            	
	 	            	StringBuffer sb1 = new StringBuffer();
	 	            	Map<String,String> imageMap = new HashMap<String,String>();
	 	       		pmm.getMailContent((Part)_message,sb1,imageMap);
	 	       	    IEmailReceiveService erservice=new EmailReceiveServiceImpl();  
	 	       		String content1 = sb1.toString();
	 	       		Set<Entry<String, String>> set =imageMap.entrySet();
	 	       		Iterator<Entry<String, String>> it = set.iterator();
	 	       		while(it.hasNext()){
	 	       			Entry<String,String>entry = it.next();
	 	       			String cid=entry.getKey();
	 	       		String src="/cbtconsole/images/"+entry.getValue();
	 	   			content1=content1.replace(cid, src);
	 	       			
	 	       		}
	 	       	 String content=null;
	 	         String s = content1;
				 Boolean index1 = s.toLowerCase().contains("/NBEmail/images/".toLowerCase());
				 if(index1 !=false){
		 	       		content=content1;
		 	       			
		 	       		}else{
	 	       		StringBuffer sb2 = new StringBuffer();
	 	       		Map<String,String> imageMap2 = new HashMap<String,String>();
	 	       		pmm.getMail((Part)_message,sb2,imageMap2);
	 	       		
	 	       		String content2 = sb2.toString();
	 	       		Set<Entry<String, String>> set2 =imageMap2.entrySet();
	 	       		Iterator<Entry<String, String>> it2 = set2.iterator();
	 	       		while(it2.hasNext()){
	 	       			Entry<String,String>entry = it2.next();
	 	       			String cid=entry.getKey();
	 	       			String src="/NBEmail/images/"+entry.getValue();
	 	       			content2=content2.replace(cid, src);
	 	       		}
	 	       	         content=content2;
		 	       		}	
	 	       		
	 	       	receive.setContent(content);
	 	       pmm.getMailContent((Part)_message);
	 	      boolean containAttach = pmm.isContainAttach((Part)_message);
	 	     if(containAttach) {
	 	    	receive.setInclude(1);
	 	    	int number=100000;
            	String value=number+".eml";
                File storefile = new File(path,number+".eml");
				//LOG.warn("storefile's path: " + storefile.toString());
                String sb3=null;
				for(int i=0;storefile.exists();i++){//如果存在同名的附件，则在后面添加数字区分
					 //获取文件名称后面的文件组后一个.的下标（后缀名）
					number++;
		            int index = value.lastIndexOf(".");
		            //String sb = value.substring(0,index)+i;
		           sb3 = value.replace(value.substring(index-6), number+"");
		            //LOG.warn("sb:"+sb);
		            value = sb3+value.substring(index);
					storefile = new File(path,value);
				}
				mailReceiver(_message);
            	FileOutputStream out1=new FileOutputStream(storefile);
               _message.writeTo(out1);
            	out1.flush();
            	out1.close();
            	receive.setOriginalmessage(value);
            	 String email=JsoupGetData.getNumbers12(pmm.getFrom());
    	 	     Pattern p1=Pattern.compile("\\[[1-9]{3,6}"); 
    	 	        Matcher m1=p1.matcher(title);
    	 	       String ids1="";
    	 	       int id2=0;
    	 	        while(m1.find())
    	 	        { 
    	 	           ids1=m1.group();
    	 	        } 
    	 	        if(ids1!=null&&!"".equals(ids1)){
    	 	        ids1=ids1.replace("[", "");
    	 	       id2 = Integer.parseInt(ids1);
    	 	      if(id2!=0){
    		 	    	IGuestBookService service = new GuestBookServiceImpl();
    		 	    	GuestBook guest=service.getall(id2);
    		 	    	receive.setQuestionid(id2);
    		 	    	receive.setUserId(guest.getUserId());
    		 	    	receive.setAdminId(guest.getAdminid());
    		 	    	int num=erservice.add1(receive);
    		 	     }else{
    		 	    	IAdminUserService acservice=new AdminUserInfoServiceImpl();
    			 	      //客户id
    			 	       int id=acservice.getId(email);
    			 	       if(id!=0){
    			 	       //销售id
    			 	     int  userid=acservice.getid(id);
    			 	     receive.setAdminId(userid);
    			 	     receive.setUserId(id);
    			 	    receive.setQuestionid(0);
    			 	    int num=erservice.add1(receive);
    			 	       }else{
    			 	    	  receive.setAdminId(0);
    					 	    receive.setUserId(0);
    					 	    receive.setQuestionid(0);
    			 	   int num=erservice.add1(receive);
    			 	       } 
    		 	    	 
    		 	     }
    			  }else{
    	 	     
    	 	        
    	 	      String content3=pmm.getBodyText();
    	 	      Pattern p=Pattern.compile("\\[[1-9]{3,6}"); 
    	 	        Matcher m=p.matcher(content3);
    	 	       String ids="";
    	 	       int id1=0;
    	 	        while(m.find())
    	 	        { 
    	 	           ids=m.group();
    	 	        } 
    	 	        if(ids!=null&&!"".equals(ids)){
    	 	        ids=ids.replace("[", "");
    	 	       id1 = Integer.parseInt(ids);
    			  }
    	 	     if(id1!=0){
    	 	    	IGuestBookService service = new GuestBookServiceImpl();
    	 	    	GuestBook guest=service.getall(id1);
    	 	    	receive.setQuestionid(id1);
    	 	    	receive.setUserId(guest.getUserId());
    	 	    	receive.setAdminId(guest.getAdminid());
    	 	    	int num=erservice.add1(receive);
    	 	     }else{
    	 	    	IAdminUserService acservice=new AdminUserInfoServiceImpl();
    		 	      //客户id
    		 	       int id=acservice.getId(email);
    		 	       if(id!=0){
    		 	       //销售id
    		 	     int  userid=acservice.getid(id);
    		 	     receive.setAdminId(userid);
    		 	     receive.setUserId(id);
    		 	    receive.setQuestionid(0);
    		 	    int num=erservice.add1(receive);
    		 	       }else{
    		 	    	  receive.setAdminId(0);
    				 	    receive.setUserId(0);
    				 	    receive.setQuestionid(0);
    		 	   int num=erservice.add1(receive);
    		 	       } 
    	 	     }
    			  }
            	
            	
             }else{
	 	     String email=JsoupGetData.getNumbers12(pmm.getFrom());
	 	     Pattern p1=Pattern.compile("\\[[1-9]{3,6}"); 
	 	        Matcher m1=p1.matcher(title);
	 	       String ids1="";
	 	       int id2=0;
	 	        while(m1.find())
	 	        { 
	 	           ids1=m1.group();
	 	        } 
	 	        if(ids1!=null&&!"".equals(ids1)){
	 	        ids1=ids1.replace("[", "");
	 	       id2 = Integer.parseInt(ids1);
	 	      if(id2!=0){
		 	    	IGuestBookService service = new GuestBookServiceImpl();
		 	    	GuestBook guest=service.getall(id2);
		 	    	receive.setQuestionid(id2);
		 	    	receive.setUserId(guest.getUserId());
		 	    	receive.setAdminId(guest.getAdminid());
		 	    	int num=erservice.add(receive);
		 	     }else{
		 	    	IAdminUserService acservice=new AdminUserInfoServiceImpl();
			 	      //客户id
			 	       int id=acservice.getId(email);
			 	       if(id!=0){
			 	       //销售id
			 	     int  userid=acservice.getid(id);
			 	     receive.setAdminId(userid);
			 	     receive.setUserId(id);
			 	    receive.setQuestionid(0);
			 	    int num=erservice.add(receive);
			 	       }else{
			 	    	  receive.setAdminId(0);
					 	    receive.setUserId(0);
					 	    receive.setQuestionid(0);
			 	   int num=erservice.add(receive);
			 	       } 
		 	    	 
		 	     }
			  }else{
	 	     
	 	        
	 	      String content3=pmm.getBodyText();
	 	      Pattern p=Pattern.compile("\\[[1-9]{3,6}"); 
	 	        Matcher m=p.matcher(content3);
	 	       String ids="";
	 	       int id1=0;
	 	        while(m.find())
	 	        { 
	 	           ids=m.group();
	 	        } 
	 	        if(ids!=null&&!"".equals(ids)){
	 	        ids=ids.replace("[", "");
	 	       id1 = Integer.parseInt(ids);
			  }
	 	     if(id1!=0){
	 	    	IGuestBookService service = new GuestBookServiceImpl();
	 	    	GuestBook guest=service.getall(id1);
	 	    	receive.setQuestionid(id1);
	 	    	receive.setUserId(guest.getUserId());
	 	    	receive.setAdminId(guest.getAdminid());
	 	    	int num=erservice.add(receive);
	 	     }else{
	 	    	IAdminUserService acservice=new AdminUserInfoServiceImpl();
		 	      //客户id
		 	       int id=acservice.getId(email);
		 	       if(id!=0){
		 	       //销售id
		 	     int  userid=acservice.getid(id);
		 	     receive.setAdminId(userid);
		 	     receive.setUserId(id);
		 	    receive.setQuestionid(0);
		 	    int num=erservice.add(receive);
		 	       }else{
		 	    	  receive.setAdminId(0);
				 	    receive.setUserId(0);
				 	    receive.setQuestionid(0);
		 	   int num=erservice.add(receive);
		 	       } 
	 	    	 
	 	     }
			  }
	 	     
	                	
	 	       
	            
             }
	                }
	        }
	        }
	        Date d = new Date();
	         long l = d.getTime();
	         LOG.warn("收邮件花费的时间:"+(l-l3));
    	} catch(AuthenticationFailedException e) {
    		LOG.warn("网络异常，稍后会自动重新执行接收邮件！");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}finally {
    		 // 释放资源  
			try {
				if (folder != null)
				folder.close(true);
				if (store != null)  
		            store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			
			}
    	}
    	
    }
    private static void mailReceiver(Message msg)throws Exception{  
        // 发件人信息  
        Address[] froms = msg.getFrom();  
        if(froms != null) {  
            //System.out.println("发件人信息:" + froms[0]);  
            InternetAddress addr = (InternetAddress)froms[0];  
            //System.out.println("发件人地址:" + addr.getAddress());  
            //System.out.println("发件人显示名:" + addr.getPersonal());  
        }  
        //System.out.println("邮件主题:" + msg.getSubject());  
        // getContent() 是获取包裹内容, Part相当于外包装  
        Object o = msg.getContent();  
        if(o instanceof Multipart) {  
            Multipart multipart = (Multipart) o ;  
            reMultipart(multipart);  
        } else if (o instanceof Part){  
            Part part = (Part) o;   
            rePart(part);  
        } else {  
            //System.out.println("类型" + msg.getContentType());  
            //System.out.println("内容" + msg.getContent());  
        }  
    }  
      
    /** 
     * @param part 解析内容 
     * @throws Exception 
     */  
    private  static void rePart(Part part) throws MessagingException,  
            UnsupportedEncodingException, IOException, FileNotFoundException {  
        if (part.getDisposition() != null&&!"".equals(part.getDisposition())) {  
          try{
           // String strFileNmae = part.getFileName(); //MimeUtility.decodeText解决附件名乱码问题  
            String strFileNmae = MimeUtility.decodeText(part.getFileName()); //MimeUtility.decodeText解决附件名乱码问题  
            //System.out.println("发现附件: " +  MimeUtility.decodeText(part.getFileName()));  
           // System.out.println("内容类型: " + MimeUtility.decodeText(part.getContentType()));  
           // System.out.println("附件内容:" + part.getContent());  
            InputStream in = part.getInputStream();// 打开附件的输入流  
            // 读取附件字节并存储到文件中  
            FileOutputStream out = new FileOutputStream(strFileNmae);
            int data;  
            while((data = in.read()) != -1) {  
                out.write(data);  
            }  
            in.close();  
            out.close();
          }catch(Exception e){
          }
        } else {  
            if(part.getContentType().startsWith("text/plain")) {  
               // System.out.println("文本内容：" + part.getContent());  
            } else {  
                //System.out.println("HTML内容：" + part.getContent());  
            }  
        }  
    }  
      
    /** 
     * @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)） 
     * @throws Exception 
     */  
    private static void reMultipart(Multipart multipart) throws Exception {  
        //System.out.println("邮件共有" + multipart.getCount() + "部分组成");  
        // 依次处理各个部分  
        for (int j = 0, n = multipart.getCount(); j < n; j++) {  
            //System.out.println("处理第" + j + "部分");  
            Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,  
            // 也可能是另一个小包裹(MultipPart)  
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative  
            if (part.getContent() instanceof Multipart) {  
                Multipart p = (Multipart) part.getContent();// 转成小包裹  
                //递归迭代  
                reMultipart(p);  
            } else {  
                rePart(part);  
            }  
         }  
    }  
 
 
	
   
 } 
