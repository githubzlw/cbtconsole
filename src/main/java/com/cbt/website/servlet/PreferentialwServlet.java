package com.cbt.website.servlet;

import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.PreferentialWeb;
import com.cbt.common.CommonConstants;
import com.cbt.messages.service.MessagesService;
import com.cbt.pojo.Messages;
import com.cbt.util.SpringContextUtil;
import com.cbt.util.Utility;
import com.cbt.website.service.IPreferentialwServer;
import com.cbt.website.service.PreferentialwServer;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ylm
 * 批量优惠
 */
public class PreferentialwServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PreferentialwServlet.class);
	
//	private MessagesService messagesService = new MessagesServiceImpl();
	
	private MessagesService messagesService = null;
	
	public PreferentialwServlet() {
        super();
    }
    
    //获取批量优惠
   	protected void getPreferentials(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		String userid_s = request.getParameter("userid");
   		String type_s = request.getParameter("type");
   		String hdate = request.getParameter("hdate");
   		String cdate = request.getParameter("cdate");
   		String email = request.getParameter("email");
   		int adminid = Integer.parseInt(request.getParameter("adminid"));
   		
   		int page = Integer.parseInt(request.getParameter("page"));
   		int userid = 0;
   		if(Utility.getStringIsNull(userid_s)){
   			userid = Integer.parseInt(userid_s);
   		}
   		int type = -1;
   		if(Utility.getStringIsNull(type_s)){
   			type = Integer.parseInt(type_s);
   		}
   		IPreferentialwServer server = new PreferentialwServer();
   		List<PreferentialWeb> list = server.getPreferentials(type, userid,page,hdate,cdate,email,adminid);
   		int count = 0;
   		if(list!=null&&!list.isEmpty()){
   			count = list.get(0).getTotal();
   		}
//   	int count = server.getPreferentialsNumber(type, userid);
   		Map<String , Object> map = new HashMap<String, Object>();
   		map.put("pre", list);
   		map.put("count", count);
   		map.put("page", page);
   		PrintWriter out = response.getWriter();
   		out.print(JSONArray.fromObject(map));
   		out.flush();
   		out.close();
   	}
   
   	//保存优惠交互信息
   	protected void  savePainteracted(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		System.out.println("==发送邮件");
   		String ids = request.getParameter("ids");
   		String gids = request.getParameter("gids");
   		String prices = request.getParameter("prices");
   		String title = request.getParameter("title");
   		String sprices = request.getParameter("sprices");
   		String number = request.getParameter("number");
   		String email = request.getParameter("email");
   		String eend = request.getParameter("eend");
   		String username = request.getParameter("username");
   		String sessionid = request.getParameter("sessionid");
   		String content = request.getParameter("content");
   		String currency = request.getParameter("currency");
   		int confirm = Integer.parseInt(request.getParameter("confirm"));
   		int userid = Integer.parseInt(request.getParameter("userid"));
   		String imgs=  request.getParameter("img");
   		String question = request.getParameter("question");
   		IPreferentialwServer server = new PreferentialwServer();
   		int res = server.savePai(ids, gids, prices, title, sprices,number, userid,email,confirm,eend,sessionid,username,content,currency,imgs,question);
//   	uptimePainteracted(request, response);
//		response.sendRedirect("website/preferential.jsp?res="+res);
   		PrintWriter out = response.getWriter();
   		out.print(res);
   		out.flush();
   		out.close();
   	}
   	
   	/**
   	 * 批量优惠申请 帮客户修改价格
   	 * 2016-8-25 lyb
   	 */
   	protected void updatePriceById(HttpServletRequest request, HttpServletResponse response) throws IOException{
   		double price =Double.parseDouble(request.getParameter("price")==null || "".equals(request.getParameter("price"))?"0":request.getParameter("price"));
   		int id = Integer.parseInt(request.getParameter("id"));
   		String itemId = request.getParameter("itemId");
   		int number= Integer.parseInt(request.getParameter("number"));
   		double sPrice= Double.parseDouble(request.getParameter("sPrice"));

   		IPreferentialwServer server = new PreferentialwServer();
   		//取得数据库最新支付金额
   		double payPrice= server.getPreferentialsPayPrice(id);
   		double subPrice= new BigDecimal((sPrice*number)-(price*number)).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
   		double upPrice = payPrice-subPrice;
   		int i=0;
   		if(upPrice>0){
   			i = server.updatePriceById(price, id);
   	   		//更新购物车更改的价格
   	   		server.updateGoodsCarPrice(price, itemId);
   	   		//更新支付金额
   	   		server.updatePayPrice(itemId, upPrice);
   		}
   		PrintWriter out = response.getWriter();
   		out.print(i);
   		out.flush();
   		out.close();
   	}
   	
   //取消优惠交互信息
   	protected void  cancelPainteracted(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		int pid = Integer.parseInt(request.getParameter("pid"));
   		String cancel_reason = request.getParameter("cancel_reason");
   		IPreferentialwServer server = new PreferentialwServer();
   		int res = server.delPaprestrain(pid, cancel_reason);
   		PrintWriter out = response.getWriter();
   		out.print(res);
   		out.flush();
   		out.close();
   	}
   	
   	//修改优惠申请的失效时间
   	protected void  uptimePainteracted(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		
   		messagesService = SpringContextUtil.getBean("MessagesService",MessagesService.class);
   		
   		int id = Integer.parseInt(request.getParameter("id"));
   		String etime = request.getParameter("etime");
   		IPreferentialwServer server = new PreferentialwServer();
   		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		Date date=sdf.parse(etime);
		boolean eend = date.before(new Date());
   		int res = server.uptimePainteracted(id, etime);
   		PrintWriter out = response.getWriter();
   		if(res > 0){
   		    //同时将消息列表的信息改为已完结
		    Messages messages = new Messages();
		    messages.setType(CommonConstants.BATAPPLY);
		    messages.setEventid(id);
   			int rows = messagesService.deleteByPrimaryKey(messages);
   			LOG.info("消息列表修改状态："+rows);
   			if(eend){
   				res = 2;
   			}
   		}
   		out.print(res);
   		out.flush();
   		out.close();
   	}
   	
 	//获取申请邮费折扣
   	protected void  getPds(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		String uid = request.getParameter("userid");
   		String type_ = request.getParameter("type");
   		String email = request.getParameter("useremail");
   		int page = Integer.parseInt(request.getParameter("page"));
   		int userid = 0;
   		if(Utility.getStringIsNull(uid))userid = Integer.parseInt(uid);
   		int type = -1;
   		if(Utility.getStringIsNull(type_))type = Integer.parseInt(type_);
   		IPreferentialwServer server = new PreferentialwServer();
   		List<PostageDiscounts> list = server.getPostageD(userid, type,page,email);
   		int count = server.getPostageDNumber(userid, type);
   		if(list.size()>0){
   	   		list.get(0).setPage(page);
   	   		list.get(0).setCount(count);
   		}
   		PrintWriter out = response.getWriter();
   		out.print(JSONArray.fromObject(list));
   		out.flush();
   		out.close();
   	}
   	
   	//申请邮费折扣处理
   	protected void  upPostageD(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		String id = request.getParameter("id");
   		String type_ = request.getParameter("type");
   		String handleman = request.getParameter("handleman");
   		int pid = 0;
   		if(Utility.getStringIsNull(id))pid = Integer.parseInt(id);
   		int type = -1;
   		if(Utility.getStringIsNull(type_))type = Integer.parseInt(type_);
   		IPreferentialwServer server = new PreferentialwServer();
   		int res = server.upPostageD(pid, type, handleman);
   		PrintWriter out = response.getWriter();
   		out.print(res);
   		out.flush();
   		out.close();
   	}
   	
//   	@Override
//   	public void init(ServletConfig config) throws ServletException {
//   		ServletContext context = config.getServletContext();
//   		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
//   		messagesService = (MessagesService)ctx.getBean("MessagesService");
//   	}
   	
}
