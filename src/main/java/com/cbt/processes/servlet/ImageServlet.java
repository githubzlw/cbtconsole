package com.cbt.processes.servlet;

import com.cbt.util.RandomValidate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 生成验证码
 */
public class ImageServlet {
	private static final long serialVersionUID = 1L;
       
    public ImageServlet() {
        super();
    }
 

	protected void getRegImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
	        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expire", 0);
	        RandomValidate randomValidateCode = new RandomValidate();
	        try {
	            randomValidateCode.getRandcode(request, response,"reg");//输出图片方法
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	protected void getLoginImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
	        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expire", 0);
	        RandomValidate randomValidateCode = new RandomValidate();
	        try {
	            randomValidateCode.getRandcode(request, response,"login");//输出图片方法
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
