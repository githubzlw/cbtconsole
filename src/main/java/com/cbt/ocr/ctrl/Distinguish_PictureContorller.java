package com.cbt.ocr.ctrl;

import com.alibaba.fastjson.JSON;
import com.cbt.ocr.service.Distinguish_PictureService;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "Distinguish_Picture")
public class Distinguish_PictureContorller {

	@Autowired
	public Distinguish_PictureService  distinguish_pictureService;


	/**
	 * 查询OCR识别错误图片
	 * @User  zlc
	 * @param request
	 * @param page
	 * @param pid
	 * @param
	 * @return
	 */
	@RequestMapping(value = "FindCustomGoodsInfo")
	public String showDistinguish_Pircture(HttpServletRequest request,String page,String pid,String type,String type2){
		//获取当前用户
		String sessionId = request.getSession().getId();
		String authJson = Redis.hget(sessionId, "userauth");
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		//初始的判断以及赋值
		if (StrUtils.isNullOrEmpty(page))
			page="1";
		int pageNO=Integer.parseInt(page);
		//查询出页面数据   custom_goods_md5 中符合条件的数据
		List<CustomGoods> customGoodsList=distinguish_pictureService.showDistinguish_Pircture(pid,pageNO,type);
		List<Category1688> ret = distinguish_pictureService.showCategory1688_type();

		int totalpage = 0;
		if(customGoodsList!=null&&!customGoodsList.isEmpty()){
			totalpage = (Integer)customGoodsList.get(0).getCount();
			totalpage = totalpage%40==0?totalpage/40:totalpage/40+1;
		}

		request.setAttribute("pid",pid);
		request.setAttribute("username",user.getAdmName());
		request.setAttribute("type",type);
		request.setAttribute("currentPage", pageNO);
		request.setAttribute("totalpage", totalpage);
		request.setAttribute("customGoodsList",customGoodsList);
		request.setAttribute("ret",ret);


	return "recognition_picture";
	}
	/****
	 * @User  zlc
	 * 批发更新线上是否删除状态
	 * @param request
	 * @param mainMap
	 */
	@RequestMapping(value = "updateSomeis_delete")
	@ResponseBody
	public String updateSomeDistinguish_Pircture_is_delete(HttpServletRequest request,@RequestBody Map<String,Object> mainMap,String type,String userName){
		List<Map<String, String>> bgList = (List<Map<String, String>>)mainMap.get("bgList");
		int ret = distinguish_pictureService.updateSomePirctu_risdelete(bgList,type,userName);
		String  json = JSON.toJSONString(ret);
		return   json ;
	}

	/**
	 * 查询最简单的一级类型数据显示到页面上
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "FindCategory")
	@ResponseBody
	public List<Category1688> FindCategory(HttpServletRequest request){
		List<Category1688> ret = distinguish_pictureService.showCategory1688_type();
		return  ret ;
	}
}