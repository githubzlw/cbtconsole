package com.cbt.controller;

import com.cbt.bean.StateName;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.util.Utility;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/cbt/address")
public class AddressController {
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(AddressController.class);
	
	@RequestMapping(value = "/getState", method = RequestMethod.POST)  
	@ResponseBody  
	public Map<String, Object> getState(String key) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			map.put("code", "0");
			if(!"2".equals(key) && !"29".equals(key) && !"35".equals(key)){
				IZoneServer zs = new ZoneServer();
				List<StateName> statelist =zs.getStateName();
				map.put("msg", statelist);
			}else{
				List<String> list=Utility.getState(key);
				map.put("msg", list);
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "1");
			map.put("msg", "Operation failure");
		}		
		return map;
	}
}