package com.cbt.controller;

import com.cbt.common.DynamicsIP;
import com.cbt.util.AppConfig;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/dyipc")
public class DyIPController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DyIPController.class);
	//获取订单号
	@RequestMapping("/get")
	@ResponseBody
	public String getIP(HttpServletRequest request,
			HttpServletResponse response) {
		DynamicsIP ip = new DynamicsIP();
		String myIP = ip.getMyIP();
		System.err.println(">>>>>>>>>>>>>>New DIP:"+myIP);
		AppConfig.ips = AppConfig.ips.replaceAll("(\\d+\\.\\d+\\.\\d+\\.\\d+)", myIP);
		AppConfig.ip = AppConfig.ips;
		AppConfig.path = AppConfig.ips;
		
		AppConfig.product= AppConfig.product.replaceAll("(\\d+\\.\\d+\\.\\d+\\.\\d+)", myIP);
		ServletContext sc = request.getServletContext();
		sc.setAttribute("path", AppConfig.ip);
		
		System.err.println(">>>>>>>>>>>>>>AppConfig.ip:"+AppConfig.ip);
		
		return "<a href='"+AppConfig.ip+"'>"+AppConfig.ip+"</a>";
	}
	
}