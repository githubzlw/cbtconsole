package com.importExpress.controller;

import com.importExpress.pojo.CustomizedBean;
import com.importExpress.service.CustomizedService;
import net.sf.json.JSONObject;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 服装定制控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/customized")
public class CustomizedController {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CustomizedController.class);

	@Autowired
	private CustomizedService customizedService;
	
	

	
	/**
	 * 服装定制详情
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	@RequestMapping("/getCustomized") 
	public void getCustomized(HttpServletRequest request, HttpServletResponse response) throws Throwable{
		
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String customizedId = request.getParameter("customizedId");
		if(null != customizedId && !"".equals(customizedId)){
			CustomizedBean bean = customizedService.get(Integer.parseInt(customizedId));
			JSONObject json = JSONObject.fromObject(bean);
			PrintWriter out = response.getWriter();
			out.print(json);
			out.close();
		}
	}
	
	/**
	 * 文件下载
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/download") 
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String filePath = request.getParameter("filePath");
		if(null != filePath){
			String filename;
			if(filePath.lastIndexOf("/") > -1){
				filename = filePath.substring(filePath.lastIndexOf("/") + 1);
			}else if(filePath.lastIndexOf("\\") > -1){
				filename = filePath.substring(filePath.lastIndexOf("\\") + 1);
			}else{
				filename = filePath;
			}
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			
			File file = new File(filePath);
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			InputStream fis = null;
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				fis = new FileInputStream(file.getAbsolutePath());
				bis = new BufferedInputStream(fis);
				bos = new BufferedOutputStream(out);
				int bytesRead = 0;
				byte[] buffer = new byte[5 * 1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.flush();
			}catch(Exception e){
				logger.error("",e);
			}finally {
				if(bis != null)
					bis.close();
				if(bos != null)
					bos.close();
				if(out != null)
					out.close();
				if(fis != null)
					fis.close();
			}
		}
	}
	
}