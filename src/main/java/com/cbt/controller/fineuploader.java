package com.cbt.controller;

import com.cbt.bean.FileMeta;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.RandomUtil;
import com.cbt.util.SysParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

@Controller
@RequestMapping("/server")
public class fineuploader {
	
	private static Logger log = LoggerFactory.getLogger(fineuploader.class);
	private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	FileMeta fileMeta = null;
	
	/**
	 * 获取新文件名
	 * [规则：时间戳+4位随机数+原文件名+原文件扩展名]
	 * 修改不加原文件名，原文件名有可能有特殊字符和汉字
	 * @param
	 * @return
	 */
	private String getNewFileName(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String fileName2 = fileName.split(",")[0];
		return "" + System.currentTimeMillis() + RandomUtil.getRandom4() +fileName2.substring(fileName2.lastIndexOf("."));
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
		if (ftpConfig == null) {
			ftpConfig = GetConfigureInfo.getFtpConfig();
		}
		String localDiskPath = ftpConfig.getLocalDiskPath();
		String uploadPath =localDiskPath;// SysParamUtil.getParam("uploadPath");
		String saveFileUrl = uploadPath.endsWith("/")?uploadPath:uploadPath+"/invoice";
		saveFileUrl = saveFileUrl + "/" + System.currentTimeMillis() + "/";
		
		File headPath = new File(saveFileUrl);//获取文件夹路径
//		//System.out.println(getRootPath(multipartRequest));
        if(!headPath.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
        	headPath.mkdirs();
        }
		//获取上传的所有文件名
		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile mpf = null;
		String newFileName ="";
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
				//输出(保存)文件
//				FileCopyUtils.copy(("\r\n--"+mpf+"--\r\n").getBytes(), new FileOutputStream(getRootPath(multipartRequest)+"/upimg/" +newFileName));
				FileCopyUtils.copy((mpf).getBytes(), new FileOutputStream(saveFileUrl+newFileName));
				//获取后缀名
				map.put("success", true);
				
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("message", "File upload occured problem.");
			}
			//压入栈顶
			files.add(fileMeta);
		}
		map.put("names", newFileName);
		map.put("saveLoaction", saveFileUrl);
		return map;
	}
	
}
