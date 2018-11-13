package com.cbt.share.ctrl;

import com.cbt.share.pojo.SharePojo;
import com.cbt.share.service.ShareService;
import com.cbt.share.util.Ip2Number;
import com.cbt.share.util.ShareUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.List;

@Controller
@RequestMapping("/share")
public class ShareCtrl {
	@Autowired
	private ShareService service;
	
	
	//获取ip对应的国家
	@RequestMapping(value = "/getIpCorrespondenceState", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String getIpCorrespondenceState(HttpServletRequest request) {
		//获得ip地址
		Integer ip = 0;
		String ipStr= "";
		try {
			ipStr = request.getParameter("ip");
			if("".equals(ipStr) || ipStr==null){
				ipStr = ShareUtil.getIpAddr(request);
			}
			ip = Ip2Number.ip2Int(ipStr);
		} catch (UnknownHostException e) {
			return "";
		}
		
		//是否有缓存
		@SuppressWarnings("unchecked")
		List<SharePojo> allIpAddress = (List<SharePojo>) request.getSession().getAttribute("allIpAddress");
		
		if(allIpAddress == null){
			allIpAddress = service.getAllIpAddress();
			request.getSession().setAttribute("allIpAddress",allIpAddress);
		}
		String addr = "";
		//取出ip对应国家
		int index = ShareUtil.halfSearch(allIpAddress, ip);
		System.out.println("这般"+ipStr+"------"+ip+"--------"+allIpAddress.get(index).getAddr());
		
		for(int i=0; i<allIpAddress.size(); i++){
			SharePojo sp = allIpAddress.get(i);
			if(ip>=sp.getIp1() && ip<=sp.getIp2()){
				addr = sp.getAddr();
				System.out.println(ipStr+"------"+ip+"--------"+addr);
				break;
			}
		}
		return addr;
	}
}
