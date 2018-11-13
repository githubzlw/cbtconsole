package com.cbt.controller;

import com.cbt.bean.BusiessBean;
import com.cbt.service.BusiessService;
import com.cbt.util.Utility;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/cbt/busiess")
public class busiessController {
	private static final Log LOG = LogFactory.getLog(busiessController.class);
	@Autowired
	private BusiessService busiessService;

	@RequestMapping(value = "/saveBusiess", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveBusiess(HttpServletRequest request, String company, String email, String ordervalue,
                                           String needs, String orderid, String userphone) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", 0);
		params.put("company", company);
		params.put("email", email);
		params.put("ordervalue", ordervalue);
		params.put("needs", needs);
		params.put("createtime", Utility.format(new Date(), Utility.datePattern1));
		params.put("userphone", userphone);
		int result = busiessService.saveBusiess(params);
		if (result == 1) {
			map.put("code", "0");
			map.put("msg", "Sent Successfully");
		} else {
			map.put("code", "1");
			map.put("msg", "Commit failed");
		}
		return map;
	}

	@RequestMapping(value = "/getBusiess", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBusiess(HttpServletRequest request, BusiessBean busiessBean,
                                          @RequestParam(value = "pagenum", defaultValue = "1") int pagenum) {
		Map<String, Object> map = new HashMap<String, Object>();
		int total = busiessService.getBusiessCountPage(busiessBean);
		List<BusiessBean> list = busiessService.getBusiessPage(busiessBean, pagenum);
		if (list != null && list.size() > 0) {
			map.put("code", "0");
			map.put("data", JSONArray.fromObject(list).toString());
			map.put("total", total);
			map.put("page", pagenum);
			map.put("pagesize", 20);
			map.put("status", busiessBean.getStatus());
		} else {
			map.put("code", "1");
			map.put("data", "null");
		}
		return map;
	}

	@RequestMapping(value = "/queryByChoice", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryByChoice(HttpServletRequest request, BusiessBean busiessBean,
                                             @RequestParam(value = "pagenum", defaultValue = "1") int pagenum, @RequestParam("type") int type) {
		Map<String, Object> map = new HashMap<String, Object>();
		int total = busiessService.queryByChoiceCountPage(busiessBean, type);
		List<BusiessBean> list = busiessService.queryByChoice(busiessBean, pagenum, type);
		if (list != null && list.size() > 0) {
			map.put("code", "0");
			map.put("data", JSONArray.fromObject(list).toString());
			map.put("total", total);
			map.put("page", pagenum);
			map.put("pagesize", 20);
			map.put("status", busiessBean.getStatus());
		} else {
			map.put("code", "1");
			map.put("data", "null");
		}
		return map;
	}

	@RequestMapping(value = "/changeBusiess", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeBusiess(HttpServletRequest request, @RequestParam("id") int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		int rows = busiessService.deleteBusiess(id);
		if (rows > 0) {
			map.put("code", "0");
		} else {
			map.put("code", "1");
		}
		return map;
	}
}