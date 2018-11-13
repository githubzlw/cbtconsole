package com.cbt.customer.servlet;

import com.cbt.bean.GuestBookBean;
import com.cbt.bean.Reply;
import com.cbt.common.CommonConstants;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.customer.service.IReplyService;
import com.cbt.customer.service.ReplyService;
import com.cbt.messages.service.MessagesService;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Messages;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SpringContextUtil;
import com.cbt.util.WebCookie;
import com.cbt.website.util.EasyUiJsonResult;

import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GuestBookServlet extends HttpServlet {
	private final static int PAGESIZE = 30;
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GuestBookServlet.class);
	
	private MessagesService messagesService = null;
	
	/**
	 * 方法描述:添加一条留言记录
	 * author:lizhanjun 
	 * date:2015年4月21日
	 * @param request
	 * @param response
	 */
	public void addGuestBook(HttpServletRequest request, HttpServletResponse response) {
		IGuestBookService ibs = new GuestBookServiceImpl();
/*		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("userInfo");*/
		String[] user =  WebCookie.getUser(request);
		String pimg = request.getParameter("pimg");
		String purl = request.getParameter("purl");
		String onlineUrl = request.getParameter("onlineUrl");
		try {
			PrintWriter out = response.getWriter();
			if(user != null && user.length > 0){//已经登陆过的用户
				String pid = request.getParameter("pid");
				String content = request.getParameter("content");
				String pname = request.getParameter("pname");
				String price = request.getParameter("price");
				GuestBookBean gbb = new GuestBookBean();
				gbb.setPid(pid);
				gbb.setPurl(purl);
				gbb.setOnlineUrl(onlineUrl);
				if(pimg != null) {
					gbb.setPimg(pimg.split(",")[0].replace("[", "").replace("]", ""));
				}
				gbb.setContent(content);
				gbb.setPname(pname);
				gbb.setUserId(Integer.parseInt(user[0]));
				gbb.setUserName(user[1]);
				gbb.setPrice(price);
				int count = ibs.addComment(gbb);
				if(count > 0) {
					out.print(count);
				}
			}else {//没有登陆跳转到登陆的页面
				out.print(-1);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * 方法描述:根据留言id查询出对应的所有回答
	 * author:lizhanjun
	 * date:2015年4月21日
	 * @param request
	 * @param response
	 */
	public void findByGBid(HttpServletRequest request, HttpServletResponse response) {
		IGuestBookService ibs = new GuestBookServiceImpl();
		IReplyService irs = new ReplyService();
		String str = request.getParameter("guestbookId");
		if(str != null && !"".equals(str)) {
			int gbid = Integer.parseInt(str);
			GuestBookBean gbb = ibs.findById(gbid);
			if(gbb != null) {
				List<Reply> reps = irs.findByGBid(gbid);
				request.setAttribute("gbb", gbb);
				request.setAttribute("reps", reps);
				try {
					request.getRequestDispatcher("cbt/guestBookandReply.jsp").forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				} 			}
		}
	}
	
	/**
	 * 方法描述:查询所有的留言
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @throws ParseException 
	 */
	public void findAll(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		String str = request.getParameter("page");
		String date = request.getParameter("createTime");
		String  type= request.getParameter("questionType");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		IGuestBookService ibs = new GuestBookServiceImpl();
		String parse =  null;
		String timeFrom =  null;
		String timeTo = null;
		String time1= request.getParameter("timeFrom");
		String time2= request.getParameter("timeTo");
		if(time1 !=null && time1!=""){
			request.setAttribute("timeFrom", time1);
			timeFrom =time1;
		}
		if(time2 != null && time2!=""){
			request.setAttribute("timeTo", time2);
			timeTo =time2;
		}
		//用户问题类型
		int qtype =  StrUtils.isNotNullEmpty(type)==true?Integer.parseInt(type):0;
		
		//获取登录用户
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		int adminid = adm.getId();
		//临时增加Sales1账号查看所有客户留言权限
		if(adm.getAdmName().equalsIgnoreCase("Sales1")){
			adminid =1;
		}
		try {
			if(date != null && !"".equals(date)) {
				request.setAttribute("date", date);
				parse =date;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String s = request.getParameter("status");
		int state = -1;
		if(s != null && !"".equals(s)) {
			state = Integer.parseInt(s);
		}
//		request.setAttribute("state", state);
		
		String su = request.getParameter("userId");
		int userId = 0;
		if(su != null && !"".equals(su)) {
			userId = Integer.parseInt(su);
//			request.setAttribute("userId", userId);//whj 输入条件查询时userid没有保留到文本框中
		}
		String userName = request.getParameter("userName");
		if(userName != null && !"".equals(userName)) {
//			request.setAttribute("userName", userName);
		}
		String pname = request.getParameter("pname");
		if(pname != null && !"".equals(pname)) {
//			request.setAttribute("pname", pname);
		}
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
//			request.setAttribute("useremail", useremail);
		}
		int page = 1;
		if(str==null){
			str="1";
		}
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		//int end = page * pageSize;
		List<GuestBookBean> findAll = ibs.findAll(userId, parse, state, userName, pname, start, PAGESIZE,useremail,timeFrom,timeTo,adminid,qtype);
		for(GuestBookBean gBean : findAll){
			String ppName = gBean.getPname();
			if(ppName != null && !"".equals(ppName.trim())){
				ppName = ppName.trim().replaceAll("\'", "\\\'").replaceAll("\"", "\\\"");
				gBean.setPname(ppName);
			}
		}
		
		int total = ibs.total(userId, parse, state, userName, pname, start, PAGESIZE,timeFrom,timeTo,adminid,qtype);
//		SplitPage.buildPager(request, total, PAGESIZE, page);
		/*for (GuestBookBean gb : findAll) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			try {
				gb.setCreateTime(dateFormat.parse(dateFormat.format(gb.getCreateTime())));
				System.out.println(gb.getCreateTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}*/
		
		//进行商品线上链接的替换
		for(GuestBookBean gBookBean : findAll){
			gBookBean.setUserInfos("<a href='/cbtconsole/website/user.jsp?userid="+gBookBean.getUserId()+"'></a>");
			if(gBookBean.getBname()!=null && !"".equals(gBookBean.getBname())){
				gBookBean.setEmail(gBookBean.getEmail()+"<br><span style='color:red'>BusinessName:</span><span style='color:green'>"+gBookBean.getBname()+"</span>");
			}else{
				gBookBean.setEmail(gBookBean.getEmail()+"<br>");
			}
			if(gBookBean.getEid()!=0){
				gBookBean.setStatusinfo("<button onclick=\"reply("+gBookBean.getId()+")\">回复</button><button onclick=\"replyrecord("+gBookBean.getId()+","+adminid+")\">回复记录</button><button onclick=\"delreply("+gBookBean.getId()+")\">删除</button>");
			}else{
				gBookBean.setStatusinfo("<button onclick=\"reply("+gBookBean.getId()+")\">回复</button><button onclick=\"delreply("+gBookBean.getId()+")\">删除</button>");
			}
			gBookBean.setPname("<span title='"+gBookBean.getPname()+"'></span><a href='"+gBookBean.getPurl()+"'  target='_blank'>"+gBookBean.getPname().substring(0,gBookBean.getPname().length()/3)+"...</a>");
			if(gBookBean.getOnlineUrl() == null || "".endsWith(gBookBean.getOnlineUrl())){
				String onlineUrl = TypeUtils.encodeGoods(gBookBean.getPurl());//使用工具解析商品链接
				gBookBean.setOnlineUrl("https://www.import-express.com/spider/getSpider?" + onlineUrl);
			}
		}
		
//		request.setAttribute("type", qtype);
//		request.setAttribute("gbbs", findAll);
//		request.setAttribute("adminid", adminid);
		json.setRows(findAll);
		json.setTotal(total);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		return json;
	}
	
	/**
	 * 方法描述:回复留言
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @param request
	 * @param response
	 */
	public void reply(HttpServletRequest request, HttpServletResponse response) {
		String gbid = request.getParameter("id");
		String content = request.getParameter("replyContent");
		IGuestBookService ibs = new GuestBookServiceImpl();
		messagesService = SpringContextUtil.getBean("MessagesService",MessagesService.class);
		try {
			PrintWriter out = response.getWriter();
			int id  = 0;
			String qustion="";
			String name="";
			String pname="";
			String email="";
			String purl="";
			String userId="";
			String sale_email="";
			if(gbid != null && !"".equals(gbid)) {
				id = Integer.parseInt(gbid);
				GuestBookBean g=ibs.getGuestBookBean(gbid);
				int  questionType = g.getQuestionType();
				if(questionType==2){
					qustion = "orderQuantity: "+g.getOrderQuantity()+"; targetPrice: "+g.getTargetPrice() ;
				}else if(questionType==3){
					qustion ="orderQuantity: "+g.getOrderQuantity()+"; customizationNeed: "+g.getCustomizationNeed();
				}else{
					qustion=g.getContent();
				}

				name=g.getUserName();
				pname=g.getPname();
				email=g.getEmail()==null?"":g.getEmail();
				userId=String.valueOf(g.getUserId());
				purl=g.getPurl();
				sale_email=g.getSale_email();
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			Date now = new Date();
			dateFormat.setLenient(false);
			String date = dateFormat.format(now);
			int count = ibs.reply(id, content,date,name,qustion,pname,email,Integer.parseInt(userId),purl,sale_email,"");
			String res = date.toString();
			if(count > 0) {
				//同时将消息列表的信息改为已完结
				Messages messages = new Messages();
				messages.setType(CommonConstants.PROPAGEMESSAGE);
				messages.setEventid(id);
				int rows = messagesService.deleteByPrimaryKey(messages);
				LOG.info("消息列表修改状态："+rows);
				out.println(res);
				out.flush();
				out.close();
			}else {
				out.println(-1);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void delreply(HttpServletRequest request, HttpServletResponse response) {
		IGuestBookService ibs = new GuestBookServiceImpl();
		String gbid = request.getParameter("id");
		int id  = 0;
		if(gbid != null && !"".equals(gbid)) {
			id = Integer.parseInt(gbid);
		}
		//逻辑性删除留言信息   state ->2 
		int count = ibs.delreply(id);
		try {
			PrintWriter PrOut = response.getWriter();
			PrOut.println(count);
			PrOut.flush();
			PrOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*public void addReply(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		String gbid = request.getParameter("guestbookId");
		UserBean user = (UserBean) session.getAttribute("userInfo");
		if(user == null) {//没有登陆跳转到登陆的页面
			try {
				request.setAttribute("gbid", gbid);
				request.getRequestDispatcher("/cbt/geton.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {//已经登陆过的用户
			String content = request.getParameter("replyContent");
			Reply rep = new Reply();
			int id  = 0;
			if(gbid != null && !"".equals(gbid)) {
				id = Integer.parseInt(gbid);
			}
			rep.setUserId(user.getId());
			rep.setUserName(user.getName());
			rep.setGuestbookId(id);
			rep.setReplyContent(content);
			int count = irs.addReply(rep);
			if(count > 0) {
				//request.setAttribute("gbb", gbb);
				try {
					String str = AppConfig.ip+"/AbstractServlet?action=findByGBid&className=GuestBookServlet&guestbookId="+id+"";
					response.sendRedirect(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}*/
}
