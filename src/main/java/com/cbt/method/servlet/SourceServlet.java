package com.cbt.method.servlet;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.method.service.OrderDetailsService;
import com.cbt.method.service.OrderDetailsServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 录入商品货源
 * @author whj
 *
 */
public class SourceServlet extends HttpServlet {
	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(SourceServlet.class);
	private static final Log SLOG = LogFactory.getLog("source");
	private static final long serialVersionUID = 1L;
	
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			LOG.info("----------------开始插件录入货源------------------");
			request.setCharacterEncoding("utf-8");
//			response.setCharacterEncoding("utf-8");
			OrderDetailsService ods=new OrderDetailsServiceImpl();
			String taobaospec=request.getParameter("taobaospec");
			String url=request.getParameter("taobaoID").toString().replace("|", "&");
			String taobaoprice=request.getParameter("taobaoprice");//淘宝价格 15.90
			int odid=Integer.parseInt(request.getParameter("ProductID").toString());//订单明细ID 产品ID ProductID  6603
			boolean allReplcae=Boolean.valueOf(request.getParameter("allReplcae"));
			String shop_info=request.getParameter("shop_id");
			String taobaoname=request.getParameter("taobaoname");
			String address=request.getParameter("address");
			String remark="";
			String buycountt="";
			String shop_url="";
			String shop_name="";
			//商品规格+采购数量
			if(taobaospec!=null){
//				taobaospec=new String(taobaospec.toString().getBytes("iso-8859-1"),"utf-8");
				taobaospec=taobaospec.indexOf(":")>-1?taobaospec.replace(":", "："):taobaospec;
				if(taobaospec.contains("(") || taobaospec.contains(")")){
					buycountt=taobaospec.split("\\(")[1].toString().split("\\)")[0];
					taobaospec=taobaospec.split("\\(")[0].toString();
				}
				if(taobaospec.indexOf(" ")>-1 && taobaospec.split(" ")[0].equals(taobaospec.split(" ")[1])){
					taobaospec=taobaospec.split(" ")[0];
				}
			}
			//商品名称
			if(taobaoname!=null){
//				taobaoname=new String(taobaoname.toString().getBytes("iso-8859-1"),"utf-8");
			}
			//发货地址
			if(address!=null){
//				address=new String(address.toString().getBytes("iso-8859-1"),"utf-8");
			}
			LOG.info("taobaoname:"+taobaoname);
			try{
				if(shop_info!=null){
					shop_info=new String(shop_info.toString().getBytes("iso-8859-1"),"utf-8");
					LOG.info("shop_info:"+shop_info);
					shop_url=shop_info;
//					shop_url=shop_url.split("\\//")[1].split("\\.")[0];
					shop_name="";
//					shop_name=shop_info.split(";")[0].split("=")[1];
				}
			}catch(Exception e){
				
			}
			//商品链接
			if (url.contains("1688.com")) {
				url = url.substring(0, url.indexOf(".html") + 5);
			}else if(url.contains("taobao")){
				System.out.println("url=="+url);
				String x=url.split("\\?")[0];
				String y[]=url.split("\\?")[1].split("&");
				for(int i=0;i<y.length;i++){
					if(y[i].contains("id")){
						url=x+"?"+y[i];
					}
				}
			}
			//采购备注
			if(request.getParameter("remark")!=null){
				remark=request.getParameter("remark").toString();
//				remark=new String(request.getParameter("remark").toString().getBytes("iso-8859-1"),"utf-8");
			}
			double price1;//采购价格
			String resource;
			price1 = Double.parseDouble(taobaoprice);
			OrderDetailsBean o=ods.getOrderDetails(odid);
			String yourOrder=String.valueOf(o.getYourorder());
//			if(preferential_price!=null && !"".equals(preferential_price) && !"undefined;".equals(preferential_price) && "1".equals(flag)){
//				int begin=0;
//				int end=0;
//				double price=0.00;
//				preferential_price=preferential_price.substring(0,preferential_price.length()-1);
//				int pg_id =ods.addPreferentialGoods(o.getGoods_url(),url,price1);//插入preferential_goods记录
//				if(preferential_price.indexOf(";")>-1){
//					String prices[]=preferential_price.split(";");
//					for(int i=0;i<prices.length && pg_id>0;i++){
//						JSONObject jsonObject = JSONObject.fromObject(prices[i]);
//						String begins=jsonObject.get("begin").toString();
//						begin=Integer.parseInt(begins!=null && !"".equals(begins)?begins:"0");
//						String ends=jsonObject.get("end").toString();
//						end=Integer.parseInt(ends!=null && !"".equals(ends)?ends:"0");
//						String pricess=jsonObject.get("price").toString();
//						price=Double.valueOf(pricess!=null && !"".equals(pricess)?pricess:"0");
//						//保存优惠批量价格
//						ods.addPreferentialPrice(pg_id,url,begin,end,price,o.getGoods_url());
//					}
//				}else{
//					JSONObject jsonObject = JSONObject.fromObject(preferential_price);
//					String begins=jsonObject.get("begin").toString();
//					begin=Integer.parseInt(begins!=null && !"".equals(begins)?begins:"0");
//					String ends=jsonObject.get("end").toString();
//					end=Integer.parseInt(ends!=null && !"".equals(ends)?ends:"0");
//					String prices=jsonObject.get("price").toString();
//					price=Double.valueOf(prices!=null && !"".equals(prices)?prices:"0");
//					//保存优惠批量价格
//					ods.addPreferentialPrice(pg_id,url,begin,end,price,o.getGoods_url());
//				}
//			}else if(preferential_price!=null && !"".equals(preferential_price) && !"undefined;".equals(preferential_price) && "2".equals(flag) && !StringUtils.isStrNull(sku_price)){
//				//保存优惠批量价格
//				ods.addPreferentialPriceForSku(o.getOrderid(),o.getGoodsid(),o.getGoods_url(),url,preferential_price,sku_price,price1);
//			}
			String type = taobaospec+"@type";
			int admid =Integer.valueOf(request.getParameter("admid")!=null?request.getParameter("admid"):"0");//adm.getId();
			int userid = o.getUserid();
			int goodsdataid = o.getGoodsdata_id();
			String goods_url = o.getGoods_url();
			String googs_img = o.getGoods_img();
			String goods_pricee = o.getGoodsprice();
			double goodsprice = Double.parseDouble(goods_pricee);
			String goods_title = o.getGoodsname();//--
			int googsnumber = o.getBuycount();
			String orderNo = o.getOrderid();
			int goodid = o.getGoodsid();
			int od_id = o.getId();
			String reason = remark;
			resource = url;
			buycountt =buycountt==null || "".equals(buycountt)?yourOrder:buycountt;
			String currency = "USD";//货币单位
			int buycount = Integer.parseInt(buycountt);
				ods.AddRecource(type,admid, userid, goodsdataid, goods_url,
						googs_img, goodsprice, goods_title, googsnumber, orderNo,
						od_id,goodid, price1, resource, buycount, reason, currency, taobaoname,allReplcae,shop_url,shop_name,address);
				Date date=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time=sdf.format(date);
				SLOG.info(time+"采购【"+admid+"】修改了订单【"+orderNo+"】商品【"+goodid+"】的货源链接为:"+resource);
			PrintWriter out;
			out = response.getWriter();
			out.print("ok");
			LOG.info("----------------成功插件录入货源------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private int min(int one, int two, int three) {
		  return (one = one < two ? one : two) < three ? one : three;
	}
	
	public float getSimilarityRatio(String str, String target) {
		  return 1 - (float)compare(str, target)/Math.max(str.length(), target.length());
	}
	
	private int compare(String str, String target) {
		  int d[][]; // 矩阵
		  int n = str.length();
		  int m = target.length();
		  int i; // 遍历str的
		  int j; // 遍历target的
		  char ch1; // str的
		  char ch2; // target的
		  int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		  if (n == 0) {
		   return m;
		  }
		  if (m == 0) {
		   return n;
		  }
		  d = new int[n + 1][m + 1];
		  for (i = 0; i <= n; i++) { // 初始化第一列
		   d[i][0] = i;
		  }

		  for (j = 0; j <= m; j++) { // 初始化第一行
		   d[0][j] = j;
		  }

		  for (i = 1; i <= n; i++) { // 遍历str
		   ch1 = str.charAt(i - 1);
		   // 去匹配target
		   for (j = 1; j <= m; j++) {
		    ch2 = target.charAt(j - 1);
		    if (ch1 == ch2) {
		     temp = 0;
		    } else {
		     temp = 1;
		    }

		    // 左边+1,上边+1, 左上角+temp取最小
		    d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
		   }
		  }
		  return d[n][m];
		 }

}
