package com.cbt.controller;

import com.cbt.bean.LevelPageBean;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.LevelPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *用户等级操作
 *@author abc
 *@date 2016年11月14日
 *
 */
@Controller
@RequestMapping(value = "/level")
public class LevelPageController {
	@Autowired
	private LevelPageService levelPageService;
	
	/**获取列表
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView getList(HttpServletRequest req, HttpServletResponse resp){
		String strPage = req.getParameter("page");
		if(!StrUtils.isMatch(strPage, "(\\d+)")){
			strPage = "1";
		}
		int page = Integer.valueOf(strPage);
		
		ModelAndView mv  = new ModelAndView("levelpage");
		List<LevelPageBean> list = levelPageService.getList(page);
		mv.addObject("list", list);
		int count = list == null || list.isEmpty() ? 0 : list.get(0).getCount();
		count = count % 40 == 0 ? count / 40 : count / 40 + 1;
		mv.addObject("totalpage", count);
		mv.addObject("currentpage", strPage);
		
		
		return mv;
	}
	
	
	/**更新
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/uplevel",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String updateLevelPage(HttpServletRequest req, HttpServletResponse resp){
		String strId = req.getParameter("id");
		strId = !StrUtils.isMatch(strId, "(\\d+)") ? "0" : strId;
		int id = Integer.valueOf(strId);
		
		String name = req.getParameter("name");
		String page = req.getParameter("page");
		String strValid = req.getParameter("valid");
		strValid = !StrUtils.isMatch(strValid, "(\\d)") ? "0" : strValid;
		int valid = Integer.valueOf(strValid);
		String catid = req.getParameter("catid");
		if(!StrUtils.isMatch(catid, "(\\d+)")){
			catid = "0";
		}
		LevelPageBean bean = new LevelPageBean();
		bean.setId(id);
		bean.setCatid(catid);
		bean.setName(name);
		bean.setPage(page);
		bean.setValid(valid);
		System.err.println(bean.toString());
		int result = id == 0 ? levelPageService.insert(bean):levelPageService.update(bean);
		
		return result>0 ? "success" : "fail";
	}
	
	
	
	/**前台application刷新
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/refresh",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String refresh(HttpServletRequest req, HttpServletResponse resp){
		String site = req.getParameter("site");
		String url  = "https://www.import-express.com/app/rlevel";
		if("kids".equals(site)) {
			url  = "https://www.kidsproductwholesale.com/app/rlevel";
		}else if("pets".equals(site)) {
			url  = "https://www.lovelypetsupply.com/app/rlevel";
		}
		DownloadMain.getContentClient(url, null);
		return "刷新成功";
	}
	
	
	
	

}
