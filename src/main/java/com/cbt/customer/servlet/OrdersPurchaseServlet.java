package com.cbt.customer.servlet;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.ChangeGoodBean;
import com.cbt.bean.CustomOrderBean;
import com.cbt.bean.GoodsCheckBean;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import com.cbt.parse.bean.TypeBean;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.pay.service.IOrderServer;
import com.cbt.pay.service.OrderServer;
import com.cbt.pojo.Admuser;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.UUIDUtil;
import com.cbt.website.bean.GoodsSource;
import com.cbt.website.dao.GoodsSourceDaoImp;
import com.cbt.website.dao.IGoodsSourceDao;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import net.sf.json.JSONObject;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrdersPurchaseServlet extends HttpServlet {
	
	private final static int PAGESIZE = 5;
	private static final long serialVersionUID = 1L;
	
	//新采购信息查询
	public void findOrdersPurchaseInfo(HttpServletRequest request, HttpServletResponse response) {
		IOrderServer os = new OrderServer();
		//订单号
		String orderNo = request.getParameter("orderNo");
		//销量
		String su = request.getParameter("selled");
		int selled = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("selled", su);
			selled = Integer.parseInt(su);
		}
		String cid = request.getParameter("cid");
		//大分类
		String categoryId = request.getParameter("categoryId");
		//小分类
		String categoryId1 = request.getParameter("purchaseId");
		
		String str = request.getParameter("page");
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		os.initCheckData(orderNo);
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得分类
		List<CategoryBean> categoryList=ips.getCategoryInfo();
		request.setAttribute("categoryList", categoryList);
		
		//取得二级分类
		if("".equals(cid)||cid==null){
			cid=categoryId;
		}
		List<CategoryBean> categoryList1=new ArrayList<CategoryBean>();
		if(!"".equals(cid) && cid!=null){
			categoryList1=ips.getCategoryInfo1(cid);
		}
		request.setAttribute("categoryList1", categoryList1);
		//根据分类查结果
		List<GoodsCheckBean> goodsCheckBeans = ips.findOrdersPurchaseInfo(selled,orderNo,categoryId1,start, PAGESIZE);

//		for(int i=0;i<goodsCheckBeans.size();i++){
//			//淘宝价格1
//			String tbPrice=goodsCheckBeans.get(i).getTbprice();
//			//淘宝价格2
//			String tbPrice1=goodsCheckBeans.get(i).getTbprice1();
//			//淘宝价格3
//			String tbPrice2=goodsCheckBeans.get(i).getTbprice2();
//			
//			//淘宝1个产品相似度值
//			int imgcheck0=goodsCheckBeans.get(i).getImgCheck0();
//			//淘宝2个产品相似度值
//			int imgcheck1=goodsCheckBeans.get(i).getImgCheck1();
//			//淘宝3个产品相似度值
//			int imgcheck2=goodsCheckBeans.get(i).getImgCheck2();
//			
//			//红框选择： 
//			//如果 第2个的相似度值 小于等于  第1个 （也就是比第一个更像),而且价格比第1个低，就选 第2个
//			if(imgcheck1 <= imgcheck0 && Double.valueOf(tbPrice1) < Double.valueOf(tbPrice)){
//				goodsCheckBeans.get(i).setTbFlag1("1");
//			}
//			//如果 第3个 比 第1个 更相似，而且 价格比第1个低 也比 第2个低， 就选 第3个。
//			else if(imgcheck2 < imgcheck0 && Double.valueOf(tbPrice2) < Double.valueOf(tbPrice) && Double.valueOf(tbPrice2) < Double.valueOf(tbPrice1)){
//				goodsCheckBeans.get(i).setTbFlag2("1");
//				
//			}else{
//				//默认选第1个 的价格
//				goodsCheckBeans.get(i).setTbFlag("1");
//			}
//				
//		}
//		int orderPurchaseCount = ips.getOrderPurchaseCount(selled,orderNo,categoryId1);
//		SplitPage.buildPager(request, orderPurchaseCount, PAGESIZE, page);
		double exchange_rate = 6.3;
		DecimalFormat df=new DecimalFormat("#0.##");
		for(int i=0;i<goodsCheckBeans.size();i++){
			//获取汇率
//			 Map<String, Double> maphl = Currency.getMaphl(request);
//			 exchange_rate = maphl.get("RMB");
//			 exchange_rate = exchange_rate/maphl.get(goodsCheckBeans.get(i).getCurrency());
			 List<GoodsCheckBean> pictureList = goodsCheckBeans.get(i).getPictureList();
			exchange_rate=Double.parseDouble(pictureList.get(0).getExchange_rate());
			if(exchange_rate<=6.3){
				exchange_rate=6.3;
			}
			 for(int j=0;j<pictureList.size();j++){
				 if(!"".equals(pictureList.get(j).getPrice()) && pictureList.get(j).getPrice()!=null){
					 pictureList.get(j).setPriceRmb(df.format(Double.parseDouble(pictureList.get(j).getPrice())*exchange_rate));
				 }
				 
//					//获取汇率
//				 Map<String, Double> maphl1 = Currency.getMaphl(request);
//				 double exchange_rate1 = maphl1.get(goodsCheckBeans.get(i).getCurrency());
//				 exchange_rate1 = exchange_rate1/maphl1.get("RMB");
//				 //原商品价格
////				 pictureList.get(j).setGoodsName(df.format(Double.parseDouble(pictureList.get(j).getPriceRmb())*exchange_rate1));
//				 pictureList.get(j).setGoodsName(df.format(246.75*exchange_rate1));
				 
			 }
			 

			 
		}
		request.setAttribute("gbbs", goodsCheckBeans);
		request.setAttribute("cid", cid);
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("categoryId1", categoryId1);
		request.setAttribute("similarityId", orderNo);
		request.setAttribute("page", page);

		try {
			request.getRequestDispatcher("/website/ordersPurchase.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//保存替代商品信息
	public void pageSaveChangeGoodData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		
		String data = request.getParameter("data");
		JSONObject json=JSONObject.fromObject(data);

        List<Map<String,String>> edit=(List<Map<String, String>>) json.getJSONArray("list");
        if(edit!=null && edit.size()<=0){
        	return;
        }
        String orderNos = (String)edit.get(0).get("orderNo");
//        PictureComparisonDaoImpl.delChangeGood(orderNoDel);
//        String orderNos = "";
//        orderNos = (String)edit.get(0).get("orderNo");
        IPictureComparisonService ips = new PictureComparisonServiceImpl();
        GoodsCheckBean goodsCheckBean= ips.getChangeGoodData(edit);
//        for(int i=0; i<edit.size(); i++){
//        	Map<String,String> map = edit.get(i);
//    		String orderNo = (String)map.get("orderNo");
//    		String importUrl = (String)map.get("importUrl");
//    		String goodsIdItem = (String)map.get("goodsId");
//    		//String changeFlag = (String)TypeUtils.modefindUrl(map.get("changeFlag"),1);
//    		String changeFlag = (String)map.get("changeFlag");
//    		String priceRmb = (String)map.get("priceRmb");
//    		orderNos = (String)map.get("orderNo");
//    		
//    		//商品id
//    		String goodsId = goodsIdItem.split("#")[0];
//    		String goodsdataId = goodsIdItem.split("#")[1];
//    		int od_id = Integer.parseInt(goodsIdItem.split("#")[2]);
//    		
//    		//GoodsCheckBean goodsCheckBean= ips.getChangeGoodData(edit,orderNo,importUrl,Integer.valueOf(goodsId),changeFlag,Integer.valueOf(goodsdataId),od_id);
//    		
//    		PrintWriter out = response.getWriter();
//    		
//    		if(goodsCheckBean != null){
//    			ChangeGoodBean chgood = new ChangeGoodBean();
//    			//订单no
//    			chgood.setOrderno(orderNo);
//    			//商品id
//    			chgood.setGoodid(goodsId);
//    			//goodscarid
//    			chgood.setGoodsCarId(goodsdataId);
//    			//aliname
//    			chgood.setAliname(goodsCheckBean.getGoodsName());
//    			//aliimg
//    			chgood.setAliimg(goodsCheckBean.getImgpath());
//    			//aliurl
//    			chgood.setAliurl(goodsCheckBean.getUrl());
//    			//alipriceRMB
//    			chgood.setAliprice(priceRmb);
//    			if("tb1".equals(changeFlag)){
//    				chgood.setChagoodname(goodsCheckBean.getTbName());
//    				chgood.setChagoodimg(goodsCheckBean.getTbImg());
//    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl(),1));
//    				chgood.setChagoodprice(goodsCheckBean.getTbprice());
//    			}else if("tb2".equals(changeFlag)){
//    				chgood.setChagoodname(goodsCheckBean.getTbName1());
//    				chgood.setChagoodimg(goodsCheckBean.getTbImg1());
//    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl1(),1));
//    				chgood.setChagoodprice(goodsCheckBean.getTbprice1());
//    			}else if("tb3".equals(changeFlag)){
//    				chgood.setChagoodname(goodsCheckBean.getTbName2());
//    				chgood.setChagoodimg(goodsCheckBean.getTbImg2());
//    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl2(),1));
//    				chgood.setChagoodprice(goodsCheckBean.getTbprice2());
//    			}else if("tb4".equals(changeFlag)){
//    				chgood.setChagoodname(goodsCheckBean.getTbName3());
//    				chgood.setChagoodimg(goodsCheckBean.getTbImg3());
//    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl3(),1));
//    				chgood.setChagoodprice(goodsCheckBean.getTbprice3());
//    			}else{
//    				chgood.setChagoodname(goodsCheckBean.getChaGoodsName());
//    				chgood.setChagoodimg(goodsCheckBean.getGoodsImgUrl());
//    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getGoodsPurl(),1));
//    				chgood.setChagoodprice(goodsCheckBean.getGoodsPrice());
//    				
//    			}
//    			
//    			int tbg = ips.saveChangeGoodData(chgood);
//    			out.print(tbg);
//    			out.flush();
//    			out.close();
//    		}else{
//    			out.print(0);
//    			out.flush();
//    			out.close();
//    		}
//    		
//        }
       
		request.getSession().setAttribute("orderNos", "\""+orderNos+"\"");
        
//		HttpSession session = request.getSession(); 
//		//订单no
//		String orderNo = request.getParameter("orderNo");
//		String orderNop="\""+orderNo+"\"";
//		String orderNos = "";
//		if((String) session.getAttribute("orderNos")!=null){
//			orderNos = (String) session.getAttribute("orderNos")+","+orderNop;
//		}else{
//			orderNos = orderNop;
//		}
//		request.getSession().setAttribute("orderNos", orderNos);
		
//		//原连接
//		String importUrl = request.getParameter("importUrl");
//		//商品id
//		String goodsId = (request.getParameter("goodsId")).split("#")[0];
//		String goodsdataId = (request.getParameter("goodsId")).split("#")[1];
//		int od_id = Integer.parseInt((request.getParameter("goodsId")).split("#")[2]);
//		//替代商品标识
//		String changeFlag = request.getParameter("changeFlag");
//		//alirmb
//		String priceRmb = request.getParameter("priceRmb");
//		
//		IPictureComparisonService ips = new PictureComparisonServiceImpl();
//		
//		GoodsCheckBean goodsCheckBean= ips.getChangeGoodData(orderNo,importUrl,Integer.valueOf(goodsId),changeFlag,Integer.valueOf(goodsdataId),od_id);
//		
//		PrintWriter out = response.getWriter();
//		
//		if(goodsCheckBean != null){
//			ChangeGoodBean chgood = new ChangeGoodBean();
//			//订单no
//			chgood.setOrderno(orderNo);
//			//商品id
//			chgood.setGoodid(goodsId);
//			//goodscarid
//			chgood.setGoodsCarId(goodsdataId);
//			//aliname
//			chgood.setAliname(goodsCheckBean.getGoodsName());
//			//aliimg
//			chgood.setAliimg(goodsCheckBean.getImgpath());
//			//aliurl
//			chgood.setAliurl(goodsCheckBean.getUrl());
//			//alipriceRMB
//			chgood.setAliprice(priceRmb);
//			if("tb1".equals(changeFlag)){
//				chgood.setChagoodname(goodsCheckBean.getTbName());
//				chgood.setChagoodimg(goodsCheckBean.getTbImg());
//				chgood.setChagoodurl(goodsCheckBean.getTbUrl());
//				chgood.setChagoodprice(goodsCheckBean.getTbprice());
//			}else if("tb2".equals(changeFlag)){
//				chgood.setChagoodname(goodsCheckBean.getTbName1());
//				chgood.setChagoodimg(goodsCheckBean.getTbImg1());
//				chgood.setChagoodurl(goodsCheckBean.getTbUrl1());
//				chgood.setChagoodprice(goodsCheckBean.getTbprice1());
//			}else if("tb3".equals(changeFlag)){
//				chgood.setChagoodname(goodsCheckBean.getTbName2());
//				chgood.setChagoodimg(goodsCheckBean.getTbImg2());
//				chgood.setChagoodurl(goodsCheckBean.getTbUrl2());
//				chgood.setChagoodprice(goodsCheckBean.getTbprice2());
//			}else if("tb4".equals(changeFlag)){
//				chgood.setChagoodname(goodsCheckBean.getTbName3());
//				chgood.setChagoodimg(goodsCheckBean.getTbImg3());
//				chgood.setChagoodurl(goodsCheckBean.getTbUrl3());
//				chgood.setChagoodprice(goodsCheckBean.getTbprice3());
//			}else{
//				chgood.setChagoodname(goodsCheckBean.getChaGoodsName());
//				chgood.setChagoodimg(goodsCheckBean.getGoodsImgUrl());
//				chgood.setChagoodurl(goodsCheckBean.getGoodsPurl());
//				chgood.setChagoodprice(goodsCheckBean.getGoodsPrice());
//				
//			}
//			
//			int tbg = ips.saveChangeGoodData(chgood);
//			out.print(tbg);
//			out.flush();
//			out.close();
//		}else{
//			out.print(0);
//			out.flush();
//			out.close();
//		}
		
	}
	
	//查询替代商品
	public void findChangeGoodsInfo(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		//订单no
		String orderNos = (String) session.getAttribute("orderNos");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		List<ChangeGoodBean> cgbList= ips.findChangeGoodsInfo(orderNos,1);
		for(int i=0;i<cgbList.size();i++){
			String[] type = cgbList.get(i).getGoodsType().split(",");
			String[] type1 = type[0].split("@");
			cgbList.get(i).setGoodsType(type1[0]+",");
			if(type.length>1){
				String[] type2 = type[1].split("@");
				cgbList.get(i).setGoodsType(type1[0]+","+type2[0]);
			}
			
		}
		request.setAttribute("cgbList", cgbList);
		
		session.removeAttribute("orderNos");
		try {
			request.getRequestDispatcher("/website/changegoods.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//无对应替代商品信息更新
	public void cancelChangeGoodData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		  
		//订单no
		String orderNo = request.getParameter("orderNo");
		
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		
		PrintWriter out = response.getWriter();
		
		if(!"".equals(orderNo)){
			
			int tbg = ips.cancelChangeGoodData(orderNo);
			out.print(tbg);
			out.flush();
			out.close();
		}else{
			out.print(0);
			out.flush();
			out.close();
		}
		
	}
	
	//有货商品保存
	public void addOrderProductSource(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		
		PurchaseServer purchaseServer = new PurchaseServerImpl();
		
		int admid =0;
		//
		int userid = Integer.parseInt(request.getParameter("userid"));
		//ali商品id
		int goodsdataid = Integer.parseInt(request.getParameter("aligoodsid"));
		//aliurl
		String goods_url = request.getParameter("aliurl");
		//选中商品img
		String googs_img = request.getParameter("pimg");
		//ali价格
		Double goodsprice = Double.parseDouble(request.getParameter("alisprice"));
		//ali名字
		String goods_title = request.getParameter("aliname");
		//yourorder
		int googsnumber = Integer.parseInt(request.getParameter("useCount"));
		String orderNo = request.getParameter("orderNo");
		//carid
		int goodid = Integer.parseInt(request.getParameter("carid"));
		//选中价格
		Double price = Double.parseDouble(request.getParameter("pprice"));
		//选中url
		String purl = request.getParameter("purl");
		//yourorder
		int buycount = Integer.parseInt(request.getParameter("useCount"));
		String reason = "";
		String currency = request.getParameter("currency");
		//选中商品名字
		String pname = request.getParameter("pname");
		//商品的type
		String cGoodstypee = request.getParameter("cGoodstypee");
		int od_id = Integer.parseInt(request.getParameter("od_id"));
		purchaseServer.AddRecource("",admid, userid, goodsdataid, goods_url,
				googs_img, goodsprice, goods_title, googsnumber, orderNo,
				od_id,goodid, price, purl, buycount,reason,currency,pname,cGoodstypee,"","","","");
	}
	
	//替代商品保存到云服务器
	public void saveGoodsData(HttpServletRequest request, HttpServletResponse response) {
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		if(user == null){
			return;
		}
		String data = request.getParameter("data");
		JSONObject json=JSONObject.fromObject(data);
        List<Map<String,String>> edit=(List<Map<String, String>>) json.getJSONArray("list");
        IPictureComparisonService ips = new PictureComparisonServiceImpl();
	        
        for(int k=0; k<edit.size(); k++){
        	Map<String,String> map = edit.get(k);
    		String changeUrl = (String)map.get("changeUrl");
    		String enName = (String)map.get("enName");
    		String price = (String)map.get("price");
    		String goodsType = (String)map.get("goodsType");
    		String quantity = (String)map.get("quantity");
    		String goodsCarId = (String)map.get("goodsCarId");
    		
    		
    		ArrayList<TypeBean> type = new ArrayList<TypeBean>();
    		String[] goodsType1 = goodsType.split(",");
    		for(int i=0;i<goodsType1.length;i++){
    			TypeBean typeBean = new TypeBean();
    			if(goodsType1[i].split(":").length>1){
				    typeBean.setType(goodsType1[i].split(":")[0]);
				    typeBean.setValue(goodsType1[i].split(":")[1]);
				    type.add(typeBean);
			    }
    		}
    		int tbg = ips.updateChangeType(changeUrl,goodsType,enName,price,goodsCarId);
    		//插入goodsdata
    		ParseGoodsUrl.parseGoodsWbsite(changeUrl,enName,price,type,goodsCarId);
    		//插入替换商品日志信息
	        ips.insertChangeGoodsLog(changeUrl,goodsType,enName,price,goodsCarId,user.getAdmName());
    		try {
			Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
//		//changeUrl
//		String changeUrl = request.getParameter("changeUrl");
//		//enName
//		String enName = request.getParameter("enName");
//		//price
//		String price = request.getParameter("price");
//		//goodsType
//		String goodsType = request.getParameter("goodsType");
//		//订量
//		String quantity = request.getParameter("quantity");
//		String goodsCarId = request.getParameter("goodsCarId");
//		
//		ArrayList<TypeBean> type = new ArrayList<TypeBean>();
//		String[] goodsType1 = goodsType.split(",");
//		for(int i=0;i<goodsType1.length;i++){
//			TypeBean typeBean = new TypeBean();
//			typeBean.setType(goodsType1[i].split(":")[0]);
//			typeBean.setValue(goodsType1[i].split(":")[1]);
//			type.add(typeBean);
//		}
//		
//		
//		IPictureComparisonService ips = new PictureComparisonServiceImpl();
//		int tbg = ips.updateChangeType(changeUrl,goodsType,enName,price,goodsCarId);
//		
//		//插入goodsdata
//		ParseGoodsUrl.parseGoodsWbsite(changeUrl,enName,price,type);
		
//		try {
//			request.getRequestDispatcher("/website/changegoods.jsp").forward(request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();	
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	//下一步跳转发信画面
	public void nextChangeGoodsInfo(HttpServletRequest request, HttpServletResponse response) {
		
		//订单no
		String orderNo = request.getParameter("orderNo");

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		List<ChangeGoodBean> cgbList= ips.findChangeGoodsInfo("\""+orderNo+"\"",2);
		for(int i=0;i<cgbList.size();i++){
			String[] type = cgbList.get(i).getGoodsType().split(",");
			String[] type1 = type[0].split("@");
			cgbList.get(i).setGoodsType(type1[0]+",");
			if(type.length>1){
				String[] type2 = type[1].split("@");
				cgbList.get(i).setGoodsType(type1[0]+","+type2[0]);
			}
			
		}
//		if(cgbList!=null){
		int userId = ips.getUserIdByOrder(orderNo);
			IUserDao userDao = new UserDao();
			String[] adminEmail = userDao.getAdminUser(0, null, userId);
			if(adminEmail==null){
				request.setAttribute("sendEmail", "");
			}else{
				request.setAttribute("sendEmail", adminEmail[0]);
			}
			
//		}

		
		request.setAttribute("cgbList", cgbList);
		
		
		try {
			request.getRequestDispatcher("/website/clientletter.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//给客户发邮件
	public void sendEmail(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		//email
		String email = request.getParameter("email");
		//userId
		String userId = request.getParameter("userId");
		//orderNo
		String orderNo = request.getParameter("orderNo");
		//获取自动登录路径
		String uuid = UUIDUtil.getEffectiveUUID(0, email);
		String path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
		//内容
		String content = "Unfortunately, we are not able to buy the following items for you.  We are suggesting alternatives for your review.<br />";
		content = content+"<a style='color: #0070C0' href='"+AppConfig.ip_email+path+"'>Click here for details.</a>";
		//标题
		String title = "Product Substitution Suggestions – ImportExpress Order#"+orderNo;

		IUserDao userDao = new UserDao();
		String[] adminEmail = userDao.getAdminUser(0, null, Integer.parseInt(userId));
		if (adminEmail != null) {
		   SendEmail.send(adminEmail[0], adminEmail[1], email,content,title,orderNo, 1);
		}
	}
	
	public void addSource(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		IGoodsSourceDao dao  =new GoodsSourceDaoImp();
		String url = req.getParameter("hdaliUrl");
		String id = req.getParameter("hdaliPid");
		String purl = TypeUtils.modefindUrl(req.getParameter("rlpurl"),1);
//		String pName = req.getParameter("rlname");
		String pPrice = req.getParameter("rlprice");
		String rlpimg = req.getParameter("rlpimg");
		System.out.println("==changeswe"+purl);
//		String checkImg = dao.getGoodsDataImg(purl);
//		if("".equals(checkImg)){
			//goodsdata取得img
			GoodsBean parseGoodsWbsite = ParseGoodsUrl.parseGoodsw(purl, 0);
			String[] imgAry = parseGoodsWbsite.getImgSize();
			String goodsImage ="";
			if(!parseGoodsWbsite.getpImage().isEmpty()){
				goodsImage =parseGoodsWbsite.getpImage().get(0);
			}else{
				goodsImage=rlpimg;
			}
			
//		}
		
//		try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String pImg = dao.getGoodsDataImg(purl);
		String pImg = "";
		if(imgAry != null && imgAry.length >1){
			pImg = goodsImage+imgAry[1];
		}else{
			pImg = goodsImage;
		}
		
		System.out.println("pImg=="+pImg);
//		String pImg = req.getParameter("img");
		if(url!=null&&!url.isEmpty()&&purl!=null&&!purl.isEmpty()){
			
			int queryExsis = dao.queryExsis(purl,url);
			if(queryExsis==0){
				GoodsSource bean = new GoodsSource();
				bean.setGoodsId(Integer.valueOf(id));
				bean.setGoodsImg(pImg);
//				bean.setGoodsName(pName);
				bean.setGoodsPrice(pPrice);
				bean.setGoodsPurl(purl);
				bean.setGoodsUrl(url);
				bean.setOrderDesc(50);
				bean.setSourceType("1688");
				dao.add(bean);
			}else{
				dao.updateChaImg(purl,url,pImg);
			}
//			dao.delGoodsDataImg(purl);
		}
		//dao.delGoodsDataImg(purl);
	}
	
	//查询淘宝辅图
	public void taobaoFtSearch(HttpServletRequest request, HttpServletResponse response) {
		
		
		int goodsDataId = Integer.valueOf(request.getParameter("goodId"));
		String aliUrl = request.getParameter("aliUrl");
		String orderNo = request.getParameter("orderNo");
		

		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		List<GoodsCheckBean> cgbList= ips.findTaobaoFtInfo(goodsDataId,aliUrl,orderNo);
		
		request.setAttribute("cgbList", cgbList);
		request.setAttribute("orderNo", orderNo);
		
		try {
			request.getRequestDispatcher("/website/taobaoft_info.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//查询本地相似图片
	public void localhostPtSearch(HttpServletRequest request, HttpServletResponse response) {
		
		
		String img = request.getParameter("imgpath");
		String dirFileName = "";
		try {
			dirFileName = this.grtImgToLocal(img.trim());
		}catch (Exception e) {
			//
			System.out.println("------图片保存失败！");

		}
		
		findImg(request,response,dirFileName);

		try {
			request.getRequestDispatcher("/website/taobaoft_info.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//查询本地相似图片
	public void localhostPtSearchs(HttpServletRequest request, HttpServletResponse response) {
		
		
		int goodsDataId = Integer.valueOf(request.getParameter("goodId"));
//		String aliUrl = request.getParameter("aliUrl");
		String orderNo = request.getParameter("orderNo");
		
		
		String aliUrl = request.getParameter("aliUrl");
		String flag = request.getParameter("flag");
//		String dirFileName = "";
//		try {
//			dirFileName = this.grtImgToLocal(img.trim());
//		}catch (Exception e) {
//			//
//			System.out.println("------图片保存失败！");
//
//		}
//		
//		findImg(request,response,dirFileName);
		
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		List<GoodsCheckBean> cgbList= ips.findLireSearchData(aliUrl,goodsDataId,orderNo,flag);
		
		request.setAttribute("cgbList", cgbList);
		request.setAttribute("orderNo", orderNo);
		try {
			request.getRequestDispatcher("/website/locImage_search.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 方法描述:上传图片搜索
	 */
	public void findImg(HttpServletRequest request, HttpServletResponse response, String dirFileName) {
		
		int goodsDataId = Integer.valueOf(request.getParameter("goodId"));
		String aliUrl = request.getParameter("aliUrl");
		String orderNo = request.getParameter("orderNo");
		String index = request.getParameter("index");
		
		// upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹
		ServletContext sctx = request.getSession()
				.getServletContext();
//		String searchImgPath = sctx.getRealPath("/searchupload");
		
		
//		String filename = request.getParameter("searchFile");   
//		File file = new File(filename );
//		String path = file.getAbsolutePath();
//        final String path = "F:\\imgTb\\300008\\taobao\\taobao2.jpg";
		//上传的搜索图片路径
        String path = dirFileName;
//        String path = "E:\\myproject\\localPhotos\\";
        //imgIndex路径
        String imgIndexPath = "";
        if("colthing".equals(index)){
            imgIndexPath = sctx.getRealPath("/clothingIndex");
        }else if("jewelry".equals(index)){
        	imgIndexPath = sctx.getRealPath("/jewelryIndex");
        }else if("watch".equals(index)){
        	imgIndexPath = sctx.getRealPath("/watchesIndex");
        }
        
//        List<CustomOrderBean> picIdList = new ArrayList<CustomOrderBean>();
        List<CustomOrderBean> ls4=null;
        List<CustomOrderBean> ls3=null;
        List<CustomOrderBean> ls2=null;
        List<CustomOrderBean> ls1=null;
        String imgPath="D:\\clothingIndex";
//        final JPanel frame = topPane;
//        Thread t = new Thread() {
//            public void run() {
                try {
//            		IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex1")));
//            		IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex2")));
//            		IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex3")));
//            		IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex4")));
//            		List<IndexReader>  lisrd= new  ArrayList<IndexReader>();
//            		lisrd.add(reader1);
//            		lisrd.add(reader2);
//            		lisrd.add(reader3);
//            		lisrd.add(reader4);
//                	//TODO 1
//                	for(int z=1;z<11;z++){
//                		syncTableInfo csn = new syncTableInfo();
//                		csn.setImgPath(imgIndexPath+z);
//                		csn.setUploadImgPath(path+z+".jpg"); 		
//                 		for(int j=0;j<lisrd.size();j++){
//                			SyncTableData data = new SyncTableData(csn,lisrd.get(j));
//                			Thread t1 = new Thread(data);
//                			t1.start();
//                	}
                	
            			
                	//TODO 2
	                	CallThread test = new CallThread();
//	            		IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath+"1")));
//	            		IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath+"2")));
//	            		IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath+"3")));
//	            		IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath+"4")));
	            		List<IndexReader>  lisrd= new  ArrayList<IndexReader>();
//	            		lisrd.add(reader1);
//	            		lisrd.add(reader2);
//	            		lisrd.add(reader3);
//	            		lisrd.add(reader4);
	                    // 创建一个线程池
	                   // ExecutorService pool = Executors.newFixedThreadPool(4);
//	                	for(int z=1;z<11;z++){
//                		syncTableInfo csn = new syncTableInfo();
//                		csn.setImgPath(imgIndexPath+z);
//                		csn.setUploadImgPath(path+z+".jpg"); 	
	                    
//                 		for(int j=0;j<lisrd.size();j++){
//                			SyncTableData data = new SyncTableData(csn,lisrd.get(j));
//                			Thread t1 = new Thread(data);
//                			t1.start();
//                 		}
	                    // 创建两个有返回值的任务
//	                    Callable c1 = test.new MyCallable(imgIndexPath+"1",path,reader1);
//	                    Callable c2 = test.new MyCallable(imgIndexPath+"2",path,reader2);
//	                    Callable c3 = test.new MyCallable(imgIndexPath+"3",path,reader3);
//	                    Callable c4 = test.new MyCallable(imgIndexPath+"4",path,reader4);
//
//	                    // 执行任务并获取Future对象
//	                    Future f1 = pool.submit(c1);
//	                    Future f2 = pool.submit(c2);
//	                    Future f3 = pool.submit(c3);
//	                    Future f4 = pool.submit(c4);
//
//	                    ls4=(List<CustomOrderBean>)f4.get();
//	                    ls3=(List<CustomOrderBean>)f3.get();
//	                    ls2=(List<CustomOrderBean>)f2.get();
//	                    ls1=(List<CustomOrderBean>)f1.get();
//
//	                    ls1.addAll(ls2);
//	                    ls1.addAll(ls3);
//	                    ls1.addAll(ls4);
//
//	                    System.out.println("返回列表大小f4："+ls4.size());
//	                    System.out.println("返回列表大小f3："+ls3.size());
//	                    System.out.println("返回列表大小f2："+ls2.size());
//	                    System.out.println("返回总列表大小："+ls1.size());
//	                    pool.shutdown();
                	//TODO 3
                	//词搜索
//                		IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex1")));
//                		IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex2")));
//                		IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex3")));
//                		IndexReader reader4 = DirectoryReader.open(FSDirectory.open(new File("D:\\clothingIndex4")));
//                		MultiReader multiReader = new MultiReader(reader1,reader2,reader3,reader4);
//                	    
//                	    IndexSearcher indexSearcher = LuceneUtils.getIndexSearcher(multiReader);
//                	    Query query = new TermQuery(new Term(  "contents","volatile"));
//                	    List<Document> list = LuceneUtils.query(indexSearcher, query);
//                	    if(null == list || list.size() <= 0) {
//                	      System.out.println("No results.");
//                	      return;
//                	    }
//                	    for(Document doc : list) {
//                	      String path1 = doc.get("path");
//                	      //String content = doc.get("contents");
//                	      System.out.println("path:" + path1);
//                	      //System.out.println("contents:" + content);
//                	    }
                	    
                	
                	
                	//TODO 4
//                        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(imgIndexPath)));
//                        int numDocs = reader.numDocs();
//                        System.out.println("numDocs = " + numDocs);
//                        int numResults = 50;
//                        try {
//                        	//图片显示结果数目
//                            numResults = 10;
//                        } catch (Exception e) {
//                            // nothing to do ...
//                        }
//                        
//                        ImageSearcher searcher = ImageSearcherFactory.createColorLayoutImageSearcher(numResults);
//                        searcher = ImageSearcherFactory.createCEDDImageSearcher(numResults);
//                      
//                        ImageSearchHits hits = searcher.search(ImageIO.read(new FileInputStream(path)), reader);
//                        
//                        for (int i = 0; i < hits.length(); i++) {
//                            try {
//                            	DecimalFormat df=new DecimalFormat("#0.##");
//                            	//分数
//                            	float fraction=hits.score(i);
//                            	String strScore = df.format(100-fraction)+"%";
//                                String fileIdentifier = hits.doc(i).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
//                    
//                                int pos = fileIdentifier.lastIndexOf("\\");
//                                String strFilePath = fileIdentifier.substring(10,pos);
//                                fileIdentifier = fileIdentifier.substring(pos+1,fileIdentifier.length()-4);
//                                
//                                strFilePath = strFilePath.replace("\\", "/");
//                                CustomOrderBean gbb = new CustomOrderBean();
//                                gbb.setPurl(strFilePath);
//                                gbb.setImg(fileIdentifier);
//                                gbb.setScore(strScore);
//                                picIdList.add(gbb);
//                				
//                				
//                            } catch (Exception ex) {
//                                Logger.getLogger("global").log(Level.SEVERE, null, ex);
//                            }
//                        }
//                        reader.close();
//                	}

                    
                } catch (Exception e) {
                    // Nothing to do here ....
                } finally {
                	
                }
                
        String type = ""; 
        IPictureComparisonService ips = new PictureComparisonServiceImpl();
        List<GoodsCheckBean> cgbList= ips.searchDownLoadInfo(ls1,type,goodsDataId,aliUrl,orderNo);
//		List<GoodsCheckBean> cgbList= ips.searchDownLoadInfo(picIdList,type,goodsDataId,aliUrl,orderNo);
		request.setAttribute("cgbList", cgbList);
		request.setAttribute("orderNo", orderNo);
		
		try {
//			request.getRequestDispatcher("/website/imgSearch.jsp").forward(request, response);
			request.getRequestDispatcher("/website/image_search.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
        
	}
	
	
	/**
	 * 判断远程文件是否存在
	 * @param imgUrl
	 * @return
	 */
	public static boolean isNetFileAvailable(String imgUrl) {
		InputStream netFileInputStream = null;
		try {
			URL url = new URL(imgUrl);
			URLConnection urlConn = url.openConnection();
			netFileInputStream = urlConn.getInputStream();
			if (null != netFileInputStream) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (netFileInputStream != null)
					netFileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将远程文件保存到本地
	 * @param img_url
	 * @return
	 * @throws Exception
	 */
	public String grtImgToLocal(String img_url) throws Exception {
		if(img_url.contains(".jpg")){
			img_url = img_url.substring(0,img_url.indexOf(".jpg")+4);
		} else if(img_url.contains(".png")){
			img_url = img_url.substring(0,img_url.indexOf(".png")+4);
		}
		URL url = new URL(img_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(25 * 1000);
		InputStream inStream = conn.getInputStream();
		byte[] data = readInputStream(inStream);
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		
//		File imageFile = new File("img_import.jpg");
		File imageFile = new File(fileName+".jpg");
		FileOutputStream outStream = new FileOutputStream("E:\\myproject\\localPhotos" + "\\" + imageFile);
		outStream.write(data);
		outStream.close();
		return "E:\\myproject\\localPhotos" + "\\" + imageFile;
	}
	
	/**
	 * 数据流文件
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
	
	
}
