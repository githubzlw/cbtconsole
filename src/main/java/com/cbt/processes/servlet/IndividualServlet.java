package com.cbt.processes.servlet;

import com.alibaba.fastjson.JSON;
import com.cbt.bean.CollectionBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.PreferentialWeb;
import com.cbt.bean.UserBean;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.OrderDao;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.*;
import com.cbt.util.WebCookie;
import com.cbt.website.service.IMessageServer;
import com.cbt.website.service.MessageServer;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 个人中心
 */
public class IndividualServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(IndividualServlet.class);
    public IndividualServlet() {
        super();
    }

    /**
     * 个人中心显示的数量
     */
	protected void getCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(request);
	    IMessageServer mess = new MessageServer();
		
		if(userinfo != null){ 
			int userid = Integer.parseInt(userinfo[0]);
			//未读系统信息
			int mes = mess.getMessageSize(userid);
			//购物车数量
			String cartNumber = "0";
			/*Cookie[] c =  request.getCookies();
			for (Cookie cookie2 : c) {
				if(cookie2.getName().equals("cartNumber")){
					cartNumber = cookie2.getValue();
				}
			}*/
			//修改中
			Cookie cookie = WebCookie.getCookieByName(request, "cartNumber") ;
			if(cookie != null){
				cartNumber = cookie.getValue();
			}
			//未付款产品 
			//产品到达我们仓库
			//产品运送中
			//历史订单
			IOrderServer oser = new OrderServer();
			int[] goodState = oser.getOrdersIndividual(userid);
			request.setAttribute("message", mes);
			request.setAttribute("cart", cartNumber);
			request.setAttribute("goodState", goodState);
			//账户余额
			IUserServer userServer = new UserServer();
			double[] balance = userServer.getBalance(userid);
			request.setAttribute("balance", balance[0]);
			request.setAttribute("create", balance[1]);
			//批量优惠申请
			IPreferentialServer preferentialServer = new PreferentialServer();
			int preNum = preferentialServer.getPreferentialNum(userid);
			request.setAttribute("preNum", preNum);
			//获取用户中的货币 2015.12.04 sj cancel
			/*String currency1 = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency1)){
				currency1 = "USD";
			}*/
			Map<String, List<String>> confirmThePriceOf = oser.getConfirmThePriceOf(userid);
			LOG.warn(JSON.toJSONString(confirmThePriceOf));
			request.setAttribute("ctpoNum", confirmThePriceOf);
//			request.setAttribute("currency", currency1);//2015.12.04 sj cancel

			//zlw add start
			String currency = WebCookie.cookie(request, "currency");
			int userID = Integer.parseInt(userinfo[0]);
			IOrderDao idao = new OrderDao();
			//如果某 “未支付订单”发生于 “最近一次支付订单” 之前 30分钟内，就在未支付订单列表里面 隐藏此订单。
			idao.updateOrderShowFlag(userID);
			//连续出现两个 “未支付订单”，而他们的间隔 在 10分钟以内，就隐藏掉 早的一个未支付订单
			idao.updateUnpaidOrderShowFlag(userID);
			
			List<OrderBean> orderBeans=idao.getOrders(userID);
			List<OrderBean> unpaidOrder=new ArrayList<OrderBean>();
			int orderVerCount = 0;
			int unpaidCount = 0;
			for(int i=0;i<orderBeans.size();i++){
				if(orderBeans.get(i).getForeign_freight() == null 
					|| "".equals(orderBeans.get(i).getForeign_freight())){
					orderBeans.get(i).setForeign_freight("0");
				}
				if(orderBeans.get(i).getActual_ffreight() == null 
						|| "".equals(orderBeans.get(i).getActual_ffreight())){
						orderBeans.get(i).setActual_ffreight("0");
				}
				if(orderBeans.get(i).getState()==5){
					orderVerCount++;
				}
				if(orderBeans.get(i).getState()==0 && orderBeans.get(i).getPayFlag()==0){
					unpaidCount++;
					unpaidOrder.add(orderBeans.get(i));
				}
			}
			ISpiderServer is = new SpiderServer();
			List<CollectionBean> collection =is.getCollection(userID);
			int collectCount = collection.size();
	   		IPreferentialServer server = new PreferentialServer();
	   		List<PreferentialWeb> res = server.getDiscounts(userid);
	   		//取得客户经理信息
	   		UserBean userBean = userServer.getUserInfo(userid);
	   		
	   		//降价优惠查询
	   		int priceReductionCount = idao.getPriceReduction(userID);
	   		request.setAttribute("priceReductionCount", priceReductionCount);
	   		
	   		//取得businessName
	   		IUserDao userDao = new UserDao();
	   		UserBean bean = userDao.getUserFromId(userID);
	   		request.setAttribute("businessName", bean.getBusinessName());
	   		
	   		request.setAttribute("userBean", userBean);
	   		
	   		//货币
	   		request.setAttribute("currency", currency);
	   		//申请优惠
	   		request.setAttribute("pre", res);
	   		
			//订单信息
			request.setAttribute("orders", orderBeans);
			//未支付订单信息
			request.setAttribute("unpaidOrder", unpaidOrder);
			
			//未支付数
			request.setAttribute("count", unpaidCount);
			//收藏数目
			request.setAttribute("collectCount", collectCount);
			//订单确认数目
			request.setAttribute("orderVerCount", orderVerCount);
			
			//退款申请HomTU start
//			RefundService rfserver= new RefundServiceImpl();
//			Double sumApp1 = rfserver.getAppCount1(userid);
//			Double sumApp2 = rfserver.getAppCount2(userid);
//			RefundBean refund = rfserver.getLoadRefund(userid);
//			request.setAttribute("refund", refund);
//			request.setAttribute("sumapp1",sumApp1);//未同意放款，可取消
//			request.setAttribute("sumapp2",sumApp2);//同意放款，不可取消
			//页面加载
			request.setAttribute("message", "<script>Fnload()</script>");
			//HomTU end
			
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/Personal.jsp");
			//zlw add end
			
			homeDispatcher.forward(request, response);
		}else{
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}
	}

	
	
	
}
