package com.cbt.controller;

import com.cbt.bean.UserGradeBean;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.UserGradeService;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *用户等级操作
 *@author abc
 *@date 2016年11月14日
 *
 */
@Controller
@RequestMapping(value = "/grade")
public class UserGradeController {
	@Autowired
	private UserGradeService userGradeService;
	
	/**get
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/get",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getGrades(){
		List<UserGradeBean> grades = userGradeService.getGrades();
		return JSONArray.toJSONString(grades).toString();
	}
	
	/**get
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/update",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String updateGrades(String  gid,String uid){
		if(gid==null||gid.isEmpty()||!StrUtils.isMatch(gid, "(\\d+)")){
			return "0";
		}
		if(uid==null||uid.isEmpty()||!StrUtils.isMatch(uid, "(\\d+)")){
			return "0";
		}
		
		int updateGrade = userGradeService.updateUserGrade(Integer.valueOf(gid), Integer.valueOf(uid));
		
		return updateGrade>0?"1":"0";
	}
	
	/**获取折扣列表
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/dislist",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView getDiscount(){
		ModelAndView mv  = new ModelAndView("gradediscount");
		List<UserGradeBean> gradeDiscount = userGradeService.getGradeDiscount();
		mv.addObject("list", gradeDiscount);
		
		return mv;
	}
	/**删除
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/delete",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String deleteDiscount(String gid){
		if(gid==null||gid.isEmpty()||!StrUtils.isMatch(gid, "(\\d+)")){
			return "0";
		}
		int updateGradeDiscount = userGradeService.updateGradeDiscount(Integer.valueOf(gid), 0, 0);
		
		return updateGradeDiscount>0?"1":"0";
	}
	/**删除
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/updiscount",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String updateDiscount(String gid,String discount){
		if(gid==null||gid.isEmpty()||!StrUtils.isMatch(gid, "(\\d+)")){
			return "0";
		}
		if(discount==null||discount.isEmpty()||!StrUtils.isMatch(discount, "(\\d+)")){
			discount = "0";
		}
		Integer intGid = Integer.valueOf(gid);
		double discountd = Double.valueOf(discount)/100;
		int result = 0;
		if(userGradeService.isExsis(intGid)>0){
			result = userGradeService.updateGradeDiscount(Integer.valueOf(gid),discountd , 1);
		}else{
			result = userGradeService.addDicount(intGid, discountd);
		}
		return result>0?"1":"0";
	}
	
	
	
	/**前台application刷新
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	@RequestMapping(value="/refresh",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String refresh(){
		String url  = "https://www.import-express.com/app/rgraded";
		DownloadMain.getContentClient(url, null);
		return "刷新成功";
	}
	
	
	
	

}
