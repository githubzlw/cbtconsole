package com.importExpress.controller;

import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.parse.service.StrUtils;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.QueAns;
import com.importExpress.service.QuestionAndAnswerService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.WebSiteEnum;
import org.apache.commons.lang.StringUtils;
import com.importExpress.utli.UserMessageUtil;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 后台提问回答功能
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/question")
public class QueAnsController {
	private final static int PAGESIZE = 60;
	private final static String URl_HEADER = "http://127.0.0.1:8083";
	
	@SuppressWarnings("unused")
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(QueAnsController.class);

	@Autowired
	private QuestionAndAnswerService questionAndAnswerService;

	@Autowired
	private UserMessageUtil userMessageUtil;

	/**
	 * 后台查询所有提问和回复信息
	 * @return
	 */
	@RequestMapping(value={"/questionlist"},method = {RequestMethod.POST,RequestMethod.GET})
    public String list(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		//产品id
		String goodsPid = request.getParameter("pid");
		goodsPid = StringUtils.isBlank(goodsPid) ? null : goodsPid;
		//产品名称
		String goodsName = request.getParameter("goodsName");
		goodsName = StringUtils.isBlank(goodsName) ? null : goodsName;
		//开始日期
		String startdate = request.getParameter("startdate");
		startdate = StringUtils.isBlank(startdate) ? null : startdate + " 00:00:00";
		//结束日期
		String enddate = request.getParameter("enddate");
		enddate = StringUtils.isBlank(enddate) ? null : enddate + " 23:59:59";
		//回复状态
		String strRepalyFlag = request.getParameter("replayflag");
		int replyFlag = StrUtils.isNum(strRepalyFlag) ? Integer.valueOf(strRepalyFlag) : 0;
		//产品单页是否显示
		String strReplyStatus = request.getParameter("replyStatus");
		int replyStatus = StrUtils.isNum(strReplyStatus) ? Integer.valueOf(strReplyStatus) : 0;
		//页码
		String strPage = request.getParameter("page");
		int page  = StrUtils.isNum(strPage) ? Integer.valueOf(strPage) : 1;
		page = page < 1 ? 1 : page;
		//负责人
		String steAdm = request.getParameter("adminid");
		int adminId = StrUtils.isNum(steAdm) ? Integer.valueOf(steAdm) : 0;
		/*if(!"ling".equals(user.getAdmName().toLowerCase())){
			adminId = StrUtils.isNum(steAdm) ? Integer.valueOf(steAdm) : 0;
		}*/

		try{
			List<QueAns> list = questionAndAnswerService.findByQuery(goodsPid, goodsName, adminId, replyFlag,replyStatus,startdate,enddate, (page-1) * PAGESIZE);
			int total = questionAndAnswerService.getCountByQuery(goodsPid, goodsName, adminId, replyFlag,replyStatus,startdate,enddate);

			request.setAttribute("total", total);
			request.setAttribute("resultList", list);
			int totalPage = total % PAGESIZE == 0 ? total / PAGESIZE : total / PAGESIZE + 1 ;
			request.setAttribute("resultList", list);
			request.setAttribute("page", page);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("replyStatus", replyStatus);
			request.setAttribute("adminId", adminId);
			request.setAttribute("replyFlag", replyFlag);
			request.setAttribute("startdate", request.getParameter("startdate"));
			request.setAttribute("enddate", request.getParameter("enddate"));
			//回复人信息
			UserDao dao = new UserDaoImpl();
			List<ConfirmUserInfo> all = dao.getAll();
			request.setAttribute("admList", all);
		}catch (Exception e){
			e.printStackTrace();
		}

		return "question";
    }
	
	
//	//去提问详情页面
//	@RequestMapping("/goEdit")
//    public String goEdit(Model model,Integer questionid){
//		QueAns queAns = questionAndAnswerService.selectByPrimaryKey(questionid);
//		String purl = queAns.getPurl();
//		queAns.setPurl(URl_HEADER+purl);
//		model.addAttribute("queAns", queAns);
//		return "queAnsDetail";
//	}
	/**
	 * 变更提问回复是否在产品单页显示
	 * @Title changeIsShow 
	 * @Description TODO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return int
	 * 王宏杰 2018-05-03
	 */
	@RequestMapping("/changeIsShow")
	@ResponseBody
    public int changeIsShow(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int row=0;
		try{
			String pid=request.getParameter("pid");
			String type=request.getParameter("type");
			row=questionAndAnswerService.changeIsShow(pid,type);

			SendMQ.sendMsg(new RunSqlModel("update question_answer set isShow='"+type+"' where questionid='"+pid+"'"));

		}catch(Exception e){
			e.printStackTrace();
		}
		return row;
	}
	/**
	 * 该条问答影响同店铺下其他同类别产品
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/influenceShop")
	@ResponseBody
    public int influenceShop(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String strqid = request.getParameter("qid");
		String shop_id = request.getParameter("shop_id");
		String reply_content = request.getParameter("reply_content");
		String state = request.getParameter("state");
		int qid = StrUtils.isNum(strqid) ? Integer.valueOf(strqid) : 0;
		if(qid == 0 || com.cbt.common.StringUtils.isStrNull(state)){
			return -1;
		}
		int updateReplyContent = questionAndAnswerService.influenceShop(qid,shop_id,state);
		if("1".equals(state)){
			SendMQ.sendMsg(new RunSqlModel("update question_answer set reply_status=2 where questionid='"+qid+"'"));
		}else if("2".equals(state)){
			SendMQ.sendMsg(new RunSqlModel("update question_answer set reply_status=1 where questionid='"+qid+"'"));
		}else if("3".equals(state)){
			SendMQ.sendMsg(new RunSqlModel("update question_answer set isShow=2,shop_id='"+shop_id+"' where questionid='"+qid+"'"));
		}else if("4".equals(state)){
			SendMQ.sendMsg(new RunSqlModel("update question_answer set isShow=1 where questionid='"+qid+"'"));
		}

		return updateReplyContent;
	}

	/**
	 * 产品单页问答删除操作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteQuestion")
	@ResponseBody
	public int deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String strqid = request.getParameter("qid");
		int qid = StrUtils.isNum(strqid) ? Integer.valueOf(strqid) : 0;
		if(qid == 0){
			return -1;
		}
		int updateReplyContent = questionAndAnswerService.deleteQuestion(qid);
		if(updateReplyContent>0){
			SendMQ.sendMsg(new RunSqlModel("update question_answer set is_delete=1 where questionid='"+qid+"'"));
		}

		return updateReplyContent;
	}

	/**
	 * 后台产品单页提问邮件回复客户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sendEmail")
	@ResponseBody
	public int sendEmail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		IGuestBookService ibs = new GuestBookServiceImpl();
		String strqid = request.getParameter("qid");
		int qid = StrUtils.isNum(strqid) ? Integer.valueOf(strqid) : 0;
		String replyContent = request.getParameter("rcontent");
		String url=request.getParameter("url");
		String purl=request.getParameter("purl");
		if(StringUtils.isBlank(replyContent) || qid == 0){
			return -1;
		}
		replyContent = replyContent.trim();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		dateFormat.setLenient(false);
		String date = dateFormat.format(now);
		String Website="0";
		String reg=".*kidsproductwholesale.*";  //判断是否童装网站
		String regPet=".*import.*";  //判断是否主站
		boolean isValid=purl.matches(reg);
		boolean isValidPet=purl.matches(regPet);
		if (isValid){
			Website="1";
		}
		if (!isValid && !isValidPet){
			Website="3";
		}
		//给客户发送邮件
		QueAns q=questionAndAnswerService.getQueAnsinfo(qid);
		int updateReplyContent=questionAndAnswerService.replyReportQes(q.getQuestionid(),replyContent, date, q.getEmail(), q.getQuestion_content(), q.getEmail(), Integer.valueOf(q.getUserid()), q.getSale_email(),url,Website);
		return 1;
	}
	/**回复提问
	 * @date 2018年3月26日
	 * @author user4
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception  
	 */
	@RequestMapping("/edit")
	@ResponseBody
    public int queAnwEdit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		IGuestBookService ibs = new GuestBookServiceImpl();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class); 
		int adminid = user.getId();
		String userId = request.getParameter("userId");
		String pid = request.getParameter("pid");
		String strqid = request.getParameter("qid");
		String isShow = request.getParameter("isShow");
		String purl=request.getParameter("purl");
		//影响同店铺标识 1影响
		String shop_flag=request.getParameter("shop_flag");
		String shop_id=request.getParameter("shop_id");
		int qid = StrUtils.isNum(strqid) ? Integer.valueOf(strqid) : 0;
		String replyContent = request.getParameter("rcontent");
		String url=request.getParameter("url");
		if(StringUtils.isBlank(replyContent) || qid == 0){
			return -1;
		}
		if(StringUtils.isBlank(userId) || "0".equals(userId) || StringUtils.isBlank(pid) || "0".equals(pid)){
			return -1;
		}
		replyContent = replyContent.trim();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		dateFormat.setLenient(false);
		String date = dateFormat.format(now);

		int updateReplyContent =questionAndAnswerService.updateReplyContent(qid, adminid, replyContent,isShow,shop_id);
		if(updateReplyContent > 0){
			SendMQ.sendMsg(new RunSqlModel("update question_answer set reply_content='"+replyContent+"',reply_status=2,reply_name='"+adminid+"',reply_time=now(),shop_id='"+shop_id+"' where questionid='"+qid+"'"));
			//影响同店铺
			if("2".equals(shop_flag)){
				int row=questionAndAnswerService.influenceShop(qid,shop_id,"3");
				if(row>0){
					SendMQ.sendMsg(new RunSqlModel(" update question_answer set isShow=2 where questionid='"+qid+"'"));
				}
			}
		}
		AtomicReference<String> website= new AtomicReference<>("1");

		Arrays.stream(WebSiteEnum.values()).forEach(e->{
			if(purl.contains(e.getName().toLowerCase())){
				website.set(String.valueOf(e.getCode()));
			}
		});

		//给客户发送邮件
		QueAns q=questionAndAnswerService.getQueAnsinfo(qid);
		questionAndAnswerService.replyReportQes(q.getQuestionid(),replyContent, date, q.getEmail(), q.getQuestion_content(), q.getEmail(), Integer.parseInt(q.getUserid()), q.getSale_email(),url, website.get());
		// questionAndAnswerService.replyReportQes(q.getQuestionid(),replyContent, date, q.getEmail(), q.getQuestion_content(), q.getEmail(), Integer.valueOf(q.getUserid()), q.getSale_email(),url,Website);
//		}

		// 发送消息给客户
		userMessageUtil.sendMessage(Integer.parseInt(userId), pid, 2, q.getQuestion_content(), purl,
				q.getQuestion_content(), replyContent);
		return updateReplyContent;
	}
	@RequestMapping("/remark")
	@ResponseBody
	public int queAnwRemark(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class); 
		int adminid = user.getId();
		String strqid = request.getParameter("qid");
		int qid = StrUtils.isNum(strqid) ? Integer.valueOf(strqid) : 0;
		String remark = request.getParameter("remark");
		//0-通过审核  1审核不通过并备注原因   2,3修改审核不通过的备注
		String strRemarkFlag = request.getParameter("remarkflag");
		int remarkFlag = StrUtils.isNum(strRemarkFlag) ? Integer.valueOf(strRemarkFlag) : -1;
		if(remarkFlag != 0 ){
			if(StringUtils.isBlank(remark) || qid == 0 || remarkFlag < 0){
				return -1;
			}
		}
		remark = remark.trim();
		int isReview = remarkFlag == 0 ? 1 : 2;

		SendMQ.sendMsg(new RunSqlModel(" update question_answer set is_review='"+isReview+"',review_remark='"+remark+"',update_time=now() where id='"+qid+"'"));

		int updateRemark=questionAndAnswerService.updateRemark(qid, adminid, isReview, remark);
		return updateRemark;
	}
	
	
//
//	@RequestMapping("/updateAll")
//	public String updateAll(Integer[] ids,String pid,String pname,String replyName,String startdate,
//			String enddate,Integer isshow,Integer replyStatus) throws Exception{
//		questionAndAnswerService.updateAll(ids);
//		//TODO 带条件的更改
//		//?pid=${pid}&pname=${pname}&replyName=${replyName}&replyName=${replyName}&isshow=${isshow}&startdate=${startdate}&enddate=${enddate}
//		return "redirect:/question/questionlist?pid="+pid+"&pname="+pname+"&replyName="+replyName+
//				"&isshow="+isshow+"&replyStatus="+replyStatus+"&startdate="+startdate+"&enddate="+enddate;
//	 }
//	
//	@RequestMapping("/deleteAll")
//	public String deleteAll(Integer[] ids,String pid,String pname,String replyName,String startdate,
//			String enddate,Integer isshow,Integer replyStatus) throws Exception{
//		questionAndAnswerService.deleteAll(ids);
//		return "redirect:/question/questionlist?pid="+pid+"&pname="+pname+"&replyName="+replyName+
//				"&isshow="+isshow+"&replyStatus="+replyStatus+"&startdate="+startdate+"&enddate="+enddate;
//	}
//	
}