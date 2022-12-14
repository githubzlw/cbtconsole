package com.cbt.ocr.ctrl;

import com.cbt.controller.EditorController;
import com.cbt.ocr.service.Distinguish_PictureService;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Category1688;
import com.cbt.pojo.CustomGoods;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.OrderInfoPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "Distinguish_Picture")
public class Distinguish_PictureContorller {
	private static final Log LOG = LogFactory.getLog(Distinguish_PictureContorller.class);

	@Autowired
	public Distinguish_PictureService distinguish_pictureService;

	/**
	 * 查询OCR识别错误图片
	 *
	 * @param request
	 * @param page
	 * @param
	 * @return
	 * @User zlc
	 */
	@RequestMapping(value = "FindCustomGoodsInfo")
	public String showDistinguish_Pircture(HttpServletRequest request, String page, String imgtype, String state, String Change_user) {
		//获取当前用户
		try {
			String sessionId = request.getSession().getId();
			String authJson = Redis.hget(sessionId, "userauth");
			String userJson = Redis.hget(sessionId, "admuser");
			int isdate = 1;
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			//初始的判断以及赋值
			if (StrUtils.isNullOrEmpty(page)) page = "1";
			int pageNO = Integer.parseInt(page);

			if ( user.getRoletype()!=0&&"1".equals(state)){
                Change_user=user.getAdmName();
            }else {
                Change_user="";
            }
			//查询出页面数据   custom_goods_md5 中符合条件的数据
			List<CustomGoods> customGoodsList = distinguish_pictureService.showDistinguish_Pircture(pageNO, imgtype, state, Change_user);
			if (StrUtils.isNullOrEmpty(state)) state = "0";
			//处理人员查询显示
			//TODO 需优化  查询时间过久
			List<Admuser> customGoodsList2 = distinguish_pictureService.showDistinguish_Pircture_operationUser();

			//给页面数据一个解释
			if (customGoodsList.size() == 0) {
				isdate = 0;
			}
			//分页的数据统计与计算
			int totalpage = 0;
			if (customGoodsList != null && !customGoodsList.isEmpty()) {
				totalpage = (Integer) customGoodsList.get(0).getCount();
				totalpage = totalpage % 35 == 0 ? totalpage / 35 : totalpage / 35 + 1;
			}
			//页面动态锁定信息
			request.setAttribute("username", user.getAdmName());
			request.setAttribute("imgtype", imgtype);
			request.setAttribute("Change_user", Change_user);
			request.setAttribute("state", state);
			request.setAttribute("currentPage", pageNO);
			request.setAttribute("totalpage", totalpage);
			request.setAttribute("customGoodsList", customGoodsList);
			request.setAttribute("customGoodsList2", customGoodsList2);
			request.setAttribute("isdate", isdate);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return "recognition_picture";
	}

	/****
	 * @User zlc
	 * 批发更新线上是否删除状态
	 * @param request
	 * @param mainMap
	 */
	@RequestMapping(value = "updateSomeis_delete")

	public String updateSomeDistinguish_Pircture_is_delete(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> mainMap, String userName, int type) throws Exception {
		List<Map<String, String>> bgList = (List<Map<String, String>>) mainMap.get("bgList");
		int ret = 0;
		try {
			if (type == 2) {
				//更新线上下架的图片状态位为1
				distinguish_pictureService.updateSomePirctu_risdelete_date(bgList);
				StringBuffer imgpath = new StringBuffer("");
				for (int i = 0; i < bgList.size(); i++) {
					String[] splt = bgList.get(i).get("id").split(",");
					imgpath = imgpath.append(splt[1] + ";" + splt[2] + "@");
				}
				try {
					ret = 1;
					//提供给蒋先伟    线上下架图片的信息列
					return "redirect:/editc/deleteEnInfoImgByParam?pidImgList=" + imgpath.substring(0, imgpath.length() - 1);

				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("pidImgList:" + imgpath.substring(0, imgpath.length() - 1) + ",updateSomeis_delete error:" + e.getMessage());
					System.err.println("pidImgList:" + imgpath.substring(0, imgpath.length() - 1) + ",updateSomeis_delete error:" + e.getMessage());
				}

			} else if (type == 1 || type == 3) {
				//修改图片状态位
				ret = distinguish_pictureService.updateSomePirctu_risdelete(bgList, userName, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	@RequestMapping(value = "recognition_date_details", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String recognition_date_details(HttpServletRequest request, String pid, String startTime, String endTime) throws Exception {
		int pageNum = 1;
		int pageSize = 50;
		String t = request.getParameter("pageNum");
		if (t != null && !"".equals(t)) {
			pageNum = Integer.parseInt(t);
		}
		t = request.getParameter("pageSize");
		if (t != null && !"".equals(t)) {
			pageSize = Integer.parseInt(t);
		}
		try {
			int startNum = pageNum * 50 - 50;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pid", pid);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			int count = distinguish_pictureService.FindRecognition_delete_count(map);

			System.out.println(count);
			map.put("startNum", startNum);
			map.put("endNum", pageSize);
			List<CustomGoods> customGoodsList = distinguish_pictureService.FindRecognition_delete_details(map);
			request.setAttribute("customGoodsList", customGoodsList);
			request.setAttribute("pageCount", startNum);
			request.setAttribute("startTime", startTime);
			request.setAttribute("endTime", endTime);
			request.setAttribute("pid", pid);
			OrderInfoPage oip = new OrderInfoPage();
			oip.setPageNum(pageNum);
			oip.setPageSize(50);
			oip.setPageSum(count);
			oip.setCkEndTime(endTime);
			oip.setCkStartTime(startTime);
			oip.setExpress_code(pid);
			request.setAttribute("oip", oip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "recognition_details";
	}

	/****
	 * @User zlc
	 * 批发更新线上无需删除操作
	 * @param request
	 * @param
	 */
	@RequestMapping(value = "updateSomeis")
	public void updateSomeDistinguish_Pircture(HttpServletRequest request, @RequestBody Map<String, Object> myArray, String userName) {
		List<Map<String, String>> maList = (List<Map<String, String>>) myArray.get("maList");
		distinguish_pictureService.updateSomePirctu_risdelete_s(maList, userName);
	}

	/**
	 * 查询最简单的一级类型数据显示到页面上
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "FindCategory")
	@ResponseBody
	public List<Category1688> FindCategory(HttpServletRequest request) {
		List<Category1688> ret = distinguish_pictureService.showCategory1688_type();
		return ret;
	}
	@RequestMapping(value = "deleteAllPriceByAdmname")

	public String deleteAllPriceByAdmname(@RequestParam("admName")String admName,HttpServletRequest request) {
		List<CustomGoods> customGoodsList = distinguish_pictureService.deleteAllPriceByAdmname(admName);
		List<Map<String, String>> bgList=new ArrayList<>();
		for (int i=0;i<customGoodsList.size();i++ ){
			Map<String,String> map=new HashMap<>();
			String len="/data";
			customGoodsList.get(i).setRemotepath("https://img.import-express.com"+customGoodsList.get(i).getRemotepath().substring(len.length(),customGoodsList.get(i).getRemotepath().length()));
			String value=customGoodsList.get(i).getId()+","+customGoodsList.get(i).getPid()+","+customGoodsList.get(i).getRemotepath();
			map.put("id",value);
			bgList.add(map);
		}
		StringBuffer imgpath = new StringBuffer("");
		for (int i = 0; i < bgList.size(); i++) {
			String[] splt = bgList.get(i).get("id").split(",");
			imgpath = imgpath.append(splt[1] + ";" + splt[2] + "@");
		}
		int ret=1;
		HttpSession session = request.getSession();
		try {
			 distinguish_pictureService.updateSomePirctu_risdelete_dateById(customGoodsList);
			session.setAttribute("pidImgList",imgpath.substring(0, imgpath.length() - 1));
			//提供给蒋先伟    线上下架图片的信息列
			return "redirect:/editc/deleteEnInfoImgByParam";

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("pidImgList:" + imgpath.substring(0, imgpath.length() - 1) + ",updateSomeis_delete error:" + e.getMessage());
			System.err.println("pidImgList:" + imgpath.substring(0, imgpath.length() - 1) + ",updateSomeis_delete error:" + e.getMessage());
			ret=0;
		}
		//更新线上下架的图片状态位为1

     return "";
	}
}