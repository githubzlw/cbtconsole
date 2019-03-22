package com.cbt.feedback.ctrl;

import com.alibaba.fastjson.JSON;
import com.cbt.feedback.bean.ErrorFeedback;
import com.cbt.feedback.service.ErrorFeedbackService;
import com.cbt.parse.service.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 问题监控异常信息反馈
 * @author admin
 *
 */
@Controller
@RequestMapping("errorFeedbackController")
public class ErrorFeedbackController {

	
	@Autowired
	private ErrorFeedbackService errorFeedbackService ;
	
	/**
	 * 按条件查询问题信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showErrorFb")
	public String showErrorFb(HttpServletRequest request, HttpServletResponse response){
		String   stype = request.getParameter("type");            //模块类别
		String  belongto  = request.getParameter("belongTo");        //负责人
		String  startTime = request.getParameter("startTime");      //开始时间
		String  endTime  = request.getParameter("endTime");        //结束时间
		String  delFlag = request.getParameter("delFlag");        //是否已处理标识

		String  pagen =   request.getParameter("page");

	    if(StrUtils.isNullOrEmpty(pagen)){
	    	pagen = "1";
	    }
	    if(StrUtils.isNullOrEmpty(delFlag)){
	    	  delFlag ="2";        //默认为2
	    }
	    if(StrUtils.isNullOrEmpty(stype)){
	    	 stype ="0";        //默认为2
	    }
	    belongto = belongto==""||belongto==null?null:belongto;
	    if(belongto!=null){
	    	try {
				belongto=new String(belongto.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    startTime = startTime==""||startTime==null?null:startTime;
	    endTime = endTime==""||endTime==null?null:endTime;
		int  deFlag = Integer.parseInt(delFlag);
		int  type = Integer.parseInt(stype);
		int  page = Integer.parseInt(pagen);
	    List<ErrorFeedback>    list = errorFeedbackService.showErrorFb(type,belongto,startTime,endTime,deFlag,page);
	    
	    int totalpage = 0;
		if(list!=null&&!list.isEmpty()){
			totalpage = (Integer)list.get(0).getCount();
			totalpage = totalpage%40==0?totalpage/40:totalpage/40+1; 
		}
		
		request.setAttribute("deFlag", deFlag);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("belongto", belongto);
		request.setAttribute("type", type);
	    request.setAttribute("currentPage", page);
	    request.setAttribute("totalpage", totalpage);
		request.setAttribute("list", list);
		return  "errorFeedback"; 
	}
	
	
	/**
	 * 插入异常问题信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("InsertErrorInfo")
	@ResponseBody
	public  String  InsertErrorInfo(HttpServletRequest request, HttpServletResponse response){
		String  content ="";
		int   positionFlag = 0;
		String  belongTo ="";
		int  type  = 0;
		String createtime ="";
		String logLocation ="";
		errorFeedbackService.InsertErrorInfo(content,createtime,belongTo,type,logLocation,positionFlag);
		
		return null; 
	}
	
	
	/**
	 * 更新单条状态 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateErrorFlag")
	@ResponseBody
	public  String   updateErrorFlag(HttpServletRequest request, HttpServletResponse response){
		int id = Integer.parseInt(request.getParameter("id"));
		String remark = request.getParameter("remark");
		remark= remark==null||remark==""?null:remark;
		int	ret = errorFeedbackService.updateErrorFlag(id,remark);
		String json = JSON.toJSONString(ret);
		return json ;
	}
	
	
	/**
	 * 批量更新状态
	 * 
	 */
	@RequestMapping("updateSomeErrorFlag")
	@ResponseBody
	public  String   updateSomeErrorFlag(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> mainMap){
		List<Map<String, String>> bgList = (List<Map<String, String>>)mainMap.get("bgList");
		for(Map<String,String>  map :bgList){
			 if(StrUtils.isNullOrEmpty(map.get("remark").toString())){
				  map.put("remark", "已修复");
			 }
		}
		int ret = errorFeedbackService.updateSomeErrorFlag(bgList);
		String  json = JSON.toJSONString(ret);
		return   json ; 
	}
	
}
