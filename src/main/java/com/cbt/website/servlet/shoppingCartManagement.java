package com.cbt.website.servlet;

import com.alibaba.fastjson.JSONObject;
import com.cbt.common.CommonConstants;
import com.cbt.messages.service.MessagesService;
import com.cbt.pojo.Messages;
import com.cbt.shopcar.service.impl.GoodCarServiceImpl;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SpringContextUtil;
import com.cbt.util.WebTool;
import com.cbt.website.dao.shoppingCartDao;
import com.cbt.website.dao.shoppingCartDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.ibm.icu.math.BigDecimal;
import com.importExpress.main.CalcFreight;
import net.minidev.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class shoppingCartManagement extends HttpServlet {


	private static final long serialVersionUID = -8913847387499385358L;

	private MessagesService messagesService = null;
	
	public shoppingCartManagement() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		String action=request.getParameter("action");
		
	}
	
	
	public void getAllUserShopCarList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
		Integer currenPage = Integer.parseInt(request.getParameter("currenPage"));
		String isorder = request.getParameter("isorder");
		Integer pageSize = 40;
		Integer begin = (currenPage - 1) * pageSize;
		shoppingCartDao shopDao = new shoppingCartDaoImpl();
		
		String sessionId = request.getSession().getId();
		String admuserJson = Redis.hget(sessionId, "admuser");
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		
		String statu = request.getParameter("status");
		int status = -1;
		if(statu!=null && !"".equals(statu)){
			status=Integer.parseInt(statu);
		}
		
		List<Object[]> list= shopDao.getAllUserShopCarList(Integer.parseInt(userid),admuser,begin,pageSize,Integer.parseInt(isorder),status);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(list));

		out.flush();
		out.close();
	}
	
	public void getShopCarList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
		
		shoppingCartDao shopDao = new shoppingCartDaoImpl();
		List<Object[]> Carlist= shopDao.getShopCarListFromUser(Integer.parseInt(userid),null);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(Carlist));
		out.flush();
		out.close();
	}
	
	//计算利率
	public void calcrate(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
		String caridList = request.getParameter("caridList");
		String priceList = request.getParameter("priceList");
		
		//累加选择货源的价格
		double newgoodprice = 0.0;
		String[] split = priceList.split(",");
		for (String string : split) {
			newgoodprice += Double.parseDouble(string);
		}
		
		shoppingCartDao shopDao = new shoppingCartDaoImpl();
		List<Object[]> Carlist= shopDao.getSelectShopCarListFromUser(Integer.parseInt(userid),caridList);
		int countryid = 0;
		Object[] obj = null;
		double totalFright = 0.0;//运费累加
		double totalprice = 0.0;//产品费累加
		double weight = 0.0;//重量累加
		DecimalFormat df = new DecimalFormat("#0.00");
		for (int i = 0; i < Carlist.size(); i++) {
			obj = Carlist.get(i);
			totalFright += (Double) obj[3];
			totalprice += (Double) obj[2] * (Integer) obj[1];
			weight += Double.parseDouble((String)obj[4]);
			countryid = (Integer) obj[6];
		}
		//原产品总工厂价格+总运费价格
		double oldsumPrice = new BigDecimal(totalFright+totalprice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println("原总价："+df.format(oldsumPrice));
		
		//计算新运费
		CalcFreight calcFreight = new CalcFreight();
		double newFreight = calcFreight.getFreight(Integer.parseInt(userid), countryid, weight);
		double newtotalprice = new BigDecimal(newgoodprice + newFreight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("oldprice", oldsumPrice);
		resMap.put("newprice", newtotalprice);
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		out.print(JSONObject.toJSONString(resMap));
		out.flush();
		out.close();
	}
	
	
	/**
	 * 确认营销
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void confirmRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
		String caridList = request.getParameter("caridList");
		String countList = request.getParameter("countList");
		String oldprice = request.getParameter("oldprice");
		String newprice = request.getParameter("newprice");
		String reduPrice = request.getParameter("reduPrice");
		Map<String, Object> resMap = new HashMap<String, Object>();
		shoppingCartDaoImpl shopDao = new shoppingCartDaoImpl();
		//复制购物车数据到新表
		//int res = shopDao.copyGoodsCarData(Integer.parseInt(userid), caridList, countList, oldprice, newprice, reduPrice);
		//删除未选择营销的购物车产品
		int res = shopDao.deleteUnMarketData(Integer.parseInt(userid), caridList, countList, oldprice, newprice, reduPrice);
		if (res != 1) {
			resMap.put("state", "false");
			resMap.put("message", "数据操作异常！");
		}else{
			
			shopDao.sendGoodcarPreEmail(Integer.parseInt(userid), caridList, oldprice, newprice, reduPrice);
			
			
		}
	}
	
	
	/**
	 * 购物车商品推送修改价格
	 */
	public void updateGoodsCarPrice(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int res =0 ;
		String caridStr = request.getParameter("carid");
		String priceStr = request.getParameter("price");
		String userid = request.getParameter("userid");
		
		if(!"".equals(caridStr)){
			String[] carid = caridStr.split(",");
			String[] price = priceStr.split(",");
			List<Integer> listcarid = new ArrayList<Integer>();
			List<Double> listprice = new ArrayList<Double>();
			for(int i=0;i<carid.length;i++){
				listcarid.add(Integer.parseInt(carid[i]));
				listprice.add(Double.parseDouble(price[i]));
			}
			shoppingCartDao shopDao = new shoppingCartDaoImpl();
			res = shopDao.updateGoodsCarPrice(listcarid, listprice);
			if (res >0) {
				GoodCarServiceImpl goodCarServiceImpl = new GoodCarServiceImpl();
				goodCarServiceImpl.upRedisData(Integer.parseInt(userid));
			}
		}
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.write(""+res);
		out.close();
	}
	
	public void getPurchasedSupply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
		shoppingCartDao shopDao = new shoppingCartDaoImpl();
		List<Object[]> list= shopDao.getPurchasedSupplyFromUserId(Integer.parseInt(userid));
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(list));
		out.flush();
		out.close();
	}
	
	/**
	 * 给客户发送购物车营销邮件
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void sendMailToUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid");
		String caridStr = request.getParameter("caridList");
		String priceStr = request.getParameter("priceList");
		String seleteStr = request.getParameter("selecList");
		String newpriceStr = request.getParameter("newpriceList");
		String emailtitle = request.getParameter("emailtitle");
		String emailremark = request.getParameter("emailremark");
		String goodscarid = request.getParameter("goodscarid");
		messagesService = SpringContextUtil.getBean("MessagesService",MessagesService.class);
		String ids[] = goodscarid.split(",");
		shoppingCartDao shopDao = new shoppingCartDaoImpl();
		String sessionId = request.getSession().getId();
		String admuserJson = Redis.hget(sessionId, "admuser");
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String admName=admuser.getAdmName();
		String[] carids=caridStr.split(",");
		for(int i=0;i<carids.length;i++){
			shopDao.saveEmailSentMessage(userid,carids[i],admName);
		}
		String res = shopDao.showEmail(Integer.parseInt(userid),caridStr,seleteStr,newpriceStr,emailtitle,emailremark,1);
		String result = "{\"status\":true,\"message\":\""+res+"\"}";
		WebTool.writeJson(result, response);
		//同时将消息列表的信息改为已完结 
		Messages messages = new Messages();
		messages.setType(CommonConstants.SHOPCARMARKET);
		for(int i=0;i<ids.length;i++){
		    messages.setEventid(Integer.parseInt(ids[i]));
			int row = messagesService.deleteByPrimaryKey(messages);
		}
	}
	
	/**
	 * 拼接邮件内容并返回给页面展示
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void showEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid");
		String caridStr = request.getParameter("caridList");//购物车id
		String priceStr = request.getParameter("priceList");//选择货源原价
		String seleteStr = request.getParameter("selecList");
		String newpriceStr = request.getParameter("newpriceList");//新价格
		shoppingCartDao shopDao = new shoppingCartDaoImpl();
		String emailStr = shopDao.showEmail(Integer.parseInt(userid),caridStr,seleteStr,newpriceStr,null, null, 0);
		String result = "{\"message\":\""+emailStr+"\"}";
		WebTool.writeJson(result, response);
	}
	
	public void updateWeight(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int carid = Integer.parseInt(request.getParameter("carid"));
		double weight = Double.parseDouble(request.getParameter("weight"));
		int number = Integer.parseInt(request.getParameter("number"));
		shoppingCartDao sc = new shoppingCartDaoImpl();
		int str =sc.updateWeight(carid, weight, number) ;
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(str);
		out.close();
	}
	
}
