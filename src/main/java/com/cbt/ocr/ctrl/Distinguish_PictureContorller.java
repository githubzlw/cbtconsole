package com.cbt.ocr.ctrl;

import com.alibaba.fastjson.JSON;
import com.cbt.ocr.service.Distinguish_PictureService;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.CustomGoods;
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
	 * @param request
	 * @param page
	 * @param pid
	 * @param shopid
	 * @param isdelete
	 * @return
	 */
	@RequestMapping(value = "FindCustomGoodsInfo")
	public String showDistinguish_Pircture(HttpServletRequest request,String page,String pid,String  shopid,String  isdelete){

		if (StrUtils.isNullOrEmpty(page))
			page="1";
		if (StrUtils.isNullOrEmpty(isdelete))
			isdelete="2";

		int pageNO=Integer.parseInt(page);
		int isdeleteNo=Integer.parseInt(isdelete);

		System.out.println("pid,shopid,isdeleteNo,pageNO"+pid+","+shopid+","+isdeleteNo+","+pageNO+",");
		List<CustomGoods> customGoodsList=distinguish_pictureService.showDistinguish_Pircture(pid,shopid,isdeleteNo,pageNO);

		int totalpage = 0;
		if(customGoodsList!=null&&!customGoodsList.isEmpty()){
			totalpage = (Integer)customGoodsList.get(0).getCount();
			totalpage = totalpage%40==0?totalpage/40:totalpage/40+1;
		}

		request.setAttribute("pid",pid);
		request.setAttribute("shopid",shopid);
		request.setAttribute("isdeleteNo",isdeleteNo);
		request.setAttribute("currentPage", pageNO);
		request.setAttribute("totalpage", totalpage);
		request.setAttribute("customGoodsList",customGoodsList);


	return "recognition_picture";
	}

	/****
	 * * 单条更新线上是否删除状态
	 * @param
	 */
/*	@RequestMapping(value = "updateis_delete")
	@ResponseBody
	public String updateDistinguish_Pircture_is_delete(HttpServletRequest request,int id,int ocrneeddelete){
		System.out.println("id+ocrneeddelete"+id+","+ocrneeddelete);
		int	ret = distinguish_pictureService.updatePirctu_risdelete(id,ocrneeddelete);
		String json = JSON.toJSONString(ret);
		return json;
	}*/
	/****
	 * 批发更新线上是否删除状态
	 * @param request
	 * @param mainMap
	 */
	@RequestMapping(value = "updateSomeis_delete")
	@ResponseBody
	public String updateSomeDistinguish_Pircture_is_delete(HttpServletRequest request,@RequestBody Map<String,Object> mainMap){
		List<Map<String, String>> bgList = (List<Map<String, String>>)mainMap.get("bgList");
		int ret = distinguish_pictureService.updateSomePirctu_risdelete(bgList);
		String  json = JSON.toJSONString(ret);
		return   json ;
	}
}