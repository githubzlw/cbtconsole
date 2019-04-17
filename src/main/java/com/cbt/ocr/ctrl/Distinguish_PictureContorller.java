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
import java.util.HashMap;
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
	public String showDistinguish_Pircture(HttpServletRequest request,
	    String page,String pid,String imgtype,String state,String Change_user){
		//获取当前用户
		String sessionId = request.getSession().getId();
		String authJson = Redis.hget(sessionId, "userauth");
		String userJson = Redis.hget(sessionId, "admuser");
		int isdate=1;
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		//初始的判断以及赋值
		if (StrUtils.isNullOrEmpty(page))
			page="1";
		int pageNO=Integer.parseInt(page);
		//查询出页面数据   custom_goods_md5 中符合条件的数据
		List<CustomGoods> customGoodsList=distinguish_pictureService.showDistinguish_Pircture(pid,pageNO,imgtype,state,Change_user);
		//处理人员查询显示
		//TODO 需优化  查询时间多久
		List<CustomGoods> customGoodsList2=distinguish_pictureService.showDistinguish_Pircture_2();

		//给页面数据一个解释
		if(customGoodsList.size()==0){
			isdate=0;
		}
		//分页的数据统计与计算
		int totalpage = 0;
		if(customGoodsList!=null&&!customGoodsList.isEmpty()){
			totalpage = (Integer)customGoodsList.get(0).getCount();
			totalpage = totalpage%30==0?totalpage/30:totalpage/30+1;
		}

		//分类类别
		//TODO 需优化   查询时间多久
		List<Category1688> ret = distinguish_pictureService.showCategory1688_type();

		//页面动态锁定信息
		request.setAttribute("pid",pid);
		request.setAttribute("username",user.getAdmName());
		request.setAttribute("imgtype",imgtype);
		request.setAttribute("Change_user",Change_user);
		request.setAttribute("state",state);
		request.setAttribute("currentPage", pageNO);
		request.setAttribute("totalpage", totalpage);
		request.setAttribute("customGoodsList",customGoodsList);
		request.setAttribute("customGoodsList2",customGoodsList2);
		request.setAttribute("isdate",isdate);
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
	public int updateSomeDistinguish_Pircture_is_delete(HttpServletRequest request,@RequestBody Map<String,Object> mainMap,String type,String userName){
		List<Map<String, String>> bgList = (List<Map<String, String>>)mainMap.get("bgList");
		int type_=Integer.parseInt(type);
		int ret = distinguish_pictureService.updateSomePirctu_risdelete(bgList,type_,userName);
		return   ret ;
	}

	/**
	 * 查询最简单的一级类型数据显示到页面上
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "FindCategory")
	@ResponseBody
	public List<Category1688> FindCategory(HttpServletRequest request){
		/*System.out.println("查询分类开始......");
		List<Category1688> ret = distinguish_pictureService.showCategory1688_type();
		try {
		for (int i=0;i<ret.size();i++){
			if(distinguish_pictureService.queryDistinguish_PirctureCount("",ret.get(i).getCategoryid())>0){
				ret.get(i).setId(i);
				ret.get(i).setEnable(distinguish_pictureService.queryDistinguish_PirctureCount("",ret.get(i).getCategoryid()));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}*/
		return  null;
	}
}