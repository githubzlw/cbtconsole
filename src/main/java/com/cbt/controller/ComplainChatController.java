package com.cbt.controller;


import com.cbt.FtpUtil.ContinueFTP2;
import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.bean.FileMeta;
import com.cbt.processes.service.SendEmail;
import com.cbt.service.IComplainChatService;
import com.cbt.service.IComplainService;
import com.cbt.util.DateFormatUtil;
import com.cbt.util.RandomUtil;
import com.cbt.util.Util;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.UploadByOkHttp;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/complainChat")
public class ComplainChatController {
	private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
	private static Logger log = LoggerFactory.getLogger(ComplainChatController.class);
	FileMeta fileMeta = null;
	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	private static String saveFileUrl ="/upimg/";
	@Autowired
	private IComplainChatService complainChatService;
	@Autowired
	private IComplainService complainService;
	@Autowired
	private SendMailFactory sendMailFactory;
	@RequestMapping(value = "/responseCustomer", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult responseCustomer(ComplainChat t, String dealAdmin, Integer dealAdminId, String userEmail, String orderNo, String urls,
                                       @RequestParam(value = "websiteType", defaultValue = "1", required = false) Integer websiteType //网站名
                                       ){
		JsonResult js = new JsonResult();
		String uuid = UUID.randomUUID().toString();
		if(t.getComplainid()!=0 && dealAdminId!=0 &&t.getChatText().trim()!=null){
			t.setUuid(uuid);
			int complainChatid=complainChatService.addChat(t);
			int result = complainService.responseCustomer(t, dealAdmin, dealAdminId,t.getComplainid());
			if(!"".equals(urls)){
					ComplainFile cf = new ComplainFile();
					cf.setComplainChatid(0);
					cf.setComplainid(t.getComplainid());
					cf.setFlag(1);
					cf.setImgUrl(urls);
					cf.setUuid(uuid);
					complainChatService.add(cf);
				}
				StringBuffer sb = new StringBuffer(t.getChatText());
				String contents = SendEmail.SetComplainContent("aaa", sb).toString();
				//SendEmail.send(SendEmail.FROM, SendEmail.PWD, userEmail, contents, "Reply to your support request", orderNo, 1);
				Map<String,Object> model = new HashedMap();
				String chatText= t.getChatText();
				model.put("chatText",chatText);
				model.put("name",userEmail);
				model.put("email",userEmail);
				model.put("websiteType",websiteType);
				sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "Reply to your support request", model, TemplateType.COMPLAINT);
				js.setOk(true);
				js.setMessage("回复成功");
		}else{
			js.setOk(false);
			js.setMessage("提交参数中，必要的字段为空"+t.getComplainid()+dealAdminId+t.getChatText());
		}
		return js;
	}
	
	/**
	 * 获取系统根路径
	 * (路径结尾不包括‘/’)
	 * @param request
	 * @return demo:
	 */
	private String getRootPath(HttpServletRequest request) {
		String prjPath = request.getSession().getServletContext().getRealPath("");
		prjPath = prjPath.replace("\\", "/");// 兼容window环境
		return prjPath.substring(0, prjPath.lastIndexOf("/"));
	}
	
	/**
	 * 获取新文件名
	 * [规则：时间戳+4位随机数+原文件名+原文件扩展名]
	 * @param file
	 * @return
	 */
	private String getNewFileName(MultipartFile file) {
		String fileName = file.getOriginalFilename();
        String fileName2 = fileName.split(",")[0];
        return "" + System.currentTimeMillis() + RandomUtil.getRandom4() + fileName2.substring(fileName2.lastIndexOf("."));
	}
	
	@RequestMapping("/uploads")
	@ResponseBody
	public Map<String,Object> upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		MultipartHttpServletRequest multipartRequest = null;
		if (resolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest)request;
		} else {
			map.put("data", "无文件上传");
			return map;
		}

		//获取上传的所有文件名
		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile mpf = null;
		String newFileName ="";

		String yearAndMonth = DateFormatUtil.formatDateToYearAndMonthString(LocalDateTime.now()).replace("-","");
        String preFilePath = getRootPath(multipartRequest) + "/upimg/"+ yearAndMonth + "/" + System.currentTimeMillis() + "/";

        String middleStr = "/importcsvimg/servicerequest/" + yearAndMonth + "/";
        String remotePath = UploadByOkHttp.SERVICE_LOCAL_IMPORT_PATH + middleStr;

        File preFile = new File(preFilePath);
        if(!preFile.exists()){
        	preFile.mkdirs();
        }
        String rsNames = "";
		while (itr.hasNext()) {
			//取出文件
			mpf = multipartRequest.getFile(itr.next());
			log.info(mpf.getOriginalFilename() + " 开始上传! 队列:" + files.size());
			//如果上传文件数大于10,则移除第一个文件
			if (files.size() >= 10)
				files.pop();
			//创建文件属性对象
			fileMeta = new FileMeta();
			String fileName= mpf.getOriginalFilename();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			fileMeta.setBytes(mpf.getBytes());

			try {
				// 保存文件名
				newFileName = getNewFileName(mpf);
				if(!newFileName.toLowerCase().contains("jpg") && !newFileName.toLowerCase().contains("png")){
					newFileName=newFileName+".jpg";
				}
				// 去除中文
				Pattern pat = Pattern.compile(REGEX_CHINESE);
				Matcher mat = pat.matcher(newFileName);
				newFileName=mat.replaceAll("");
				//输出(保存)文件
				FileCopyUtils.copy((mpf).getBytes(), new FileOutputStream(preFilePath +newFileName));
				//获取后缀名
				map.put("success", true);

				boolean isSu = UploadByOkHttp.uploadFile(new File(preFilePath + newFileName), remotePath, 0);
                if (!isSu) {
                    map.put("success", false);
                    map.put("message", "File upload occured problem.");
                }else{
                	rsNames +=  "," + middleStr + newFileName ;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("message", "File upload occured problem.");
			}
			//压入栈顶
			files.add(fileMeta);
			/*ContinueFTP2 f1 = new ContinueFTP2(Util.PIC_IP, Util.PIC_USER, Util.PIC_PASS, "21",
					"/stock_picture/2018-20/"+newFileName+"", preFilePath+"/"+newFileName);
			//远程上传到图片服务器
			f1.start();*/
		}
		map.put("names", rsNames.substring(1));
		// map.put("saveLoaction", saveFileUrl);
		map.put("saveLoaction", middleStr);
		return map;
	}
	
}
