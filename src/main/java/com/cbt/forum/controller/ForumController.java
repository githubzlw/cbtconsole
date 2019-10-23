package com.cbt.forum.controller;


import com.cbt.forum.pojo.ForumClassification;
import com.cbt.forum.pojo.ForumDetails;
import com.cbt.forum.service.ForumService;
import com.cbt.bean.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.jfinal.plugin.redis.Redis;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/***
 * user zlc
 * 论坛管理后台
 */
@Controller
@RequestMapping(value = "Forum")
public class ForumController {
	//日志管理
	private final static org.slf4j.Logger LOG= LoggerFactory.getLogger(ForumController.class);
	@Autowired
	private ForumService forumService;

	@RequestMapping(value = "FindForum_speech")
	@ResponseBody
	public EasyUiJsonResult FindForum_speech(@RequestParam(value = "rows",defaultValue ="20",required = false)Integer rows,
	                                         @RequestParam(value = "page",defaultValue ="1",required = false)int page,
	                                         @RequestParam(value = "posttitle",defaultValue ="",required = false)String posttitle,
	                                         @RequestParam(value = "audit_user",defaultValue ="",required = false)String audit_user,
	                                         @RequestParam(value = "type",defaultValue ="0",required = false)int type,
	                                         @RequestParam(value = "reviewFlag",defaultValue ="0",required = false)Integer reviewFlag,
	                                         @RequestParam(value = "startDate",defaultValue ="",required = false)String startDate,
	                                         @RequestParam(value = "endDate",defaultValue ="",required = false)String endDate){
		if (StringUtils.isNotBlank(startDate)) {
			startDate += " 00:00";
		} else {
			startDate = null;
		}
		if (StringUtils.isNotBlank(endDate)) {
			endDate += " 23:59";
		} else {
			endDate = null;
		}
		if (StringUtils.isBlank(posttitle)) {
			posttitle = null;
		}
		if (StringUtils.isBlank(audit_user)) {
			audit_user = null;
		}
		return forumService.FidForumList(page,rows,posttitle,type,reviewFlag,startDate,endDate,audit_user);
	}

	/***
	 * 帖子分类
	 * @return
	 */
	@RequestMapping(value = "FindForum_Class")
	@ResponseBody
	public List<ForumClassification> FindForum_Class(){
		return  forumService.FidForumClass();
	}

	/***
	 *
	 * @param request
	 * @param type 更新类型
	 * @param state 帖子状态
	 * @return
	 */
	@RequestMapping(value = "editForum_State")
	@ResponseBody
	public JsonResult editForum_state(HttpServletRequest request,int id,int type,int state){
		JsonResult jsonResult=new JsonResult();
		jsonResult.setOk(true);
		try {
			int State=forumService.EditForum_State(id,type,state);
			if(State>0){
				//更新线上帖子状态
				/*
				String sql="";
				SendMQ.sendMsg(new RunSqlModel(sql));
				*/

				jsonResult.setOk(true);
			}
		}catch (Exception e){
			jsonResult.setOk(false);
			jsonResult.setMessage("state:"+state+"editForum_State执行报错"+e.getMessage());
			LOG.error("state:"+state+"editForum_State执行报错"+e.getMessage());
		}
		return jsonResult;
	}

	/***
	 * 随机生成发帖人名字
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "random_Post_Name")
	@ResponseBody
	public JsonResult random_Post_Name(HttpServletRequest request){
		JsonResult json=new JsonResult();
		json.setComment(forumService.FindForumUser());
		return json;
	}

	/***
	 * 删论坛帖子
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete_forum_details_post")
	@ResponseBody
	public JsonResult delete_forum_details_post(HttpServletRequest request,int id){
		JsonResult json=new JsonResult();
		try {
			int State=forumService.EditForum_details_State(id);
			if(State>0){
				//更新线上帖子状态
				/*
				String sql="";
				SendMQ.sendMsg(new RunSqlModel(sql));
				*/

				json.setOk(true);
			}
		}catch (Exception e){
			json.setOk(false);
			json.setMessage("id:"+id+"delete_forum_details_post执行报错"+e.getMessage());
			LOG.error("id:"+id+"delete_forum_details_post执行报错"+e.getMessage());
		}
		return json;
	}

	/***
	 *  查看单个帖子的详情信息
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "FidForumList_one")
	@ResponseBody
	public List<ForumDetails> FidForumList_one(HttpServletRequest request,int id){

		return forumService.FidForumList_one(id);
	}
	/***
	 * 审核人员查询
	 * @return
	 */
	@RequestMapping(value = "FindForum_audit_user")
	@ResponseBody
	public List<ForumClassification> FindForum_audit_user(){
		return  forumService.FidForumClass();
	}
}