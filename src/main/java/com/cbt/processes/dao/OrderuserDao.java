package com.cbt.processes.dao;

import com.cbt.bean.*;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderuserDao implements IOrderuserDao {

	private static final Log LOG = LogFactory.getLog(OrderuserDao.class);
	
	@Override
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage, int page) {
		String sql = "select od.id detailID,od.orderid,goodsid,goodsprice ,od.userid,car_type,od.car_url,goodsname,car_img,goodsprice,yourorder,goodsfreight,od.remark,create_time,od.state,product_cost,od.actual_weight,od.actual_volume,yourorder,remaining_price,orderinfo.currency from  order_details od,orderinfo where orderinfo.order_no=od.orderid and od.userid=?";
		if(state != -2 && state != 2){
			sql = sql + " and orderinfo.state=?  and od.state not in(1,2)";
		}else if(state == 2){
			sql = sql + " and od.state=1";
		}
		sql = sql + "  and orderinfo.order_show = 0 and TIMESTAMPDIFF(MINUTE,orderinfo.create_time,now() )>1 order by od.orderid desc  limit ?, ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			if(state != -2 && state != 2){
				stmt.setInt(2, state);
				stmt.setInt(3, startpage);
				stmt.setInt(4, page);
			}else{
				stmt.setInt(2, startpage);
				stmt.setInt(3, page);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderDetailsBean odb = new OrderDetailsBean();
				odb.setId(rs.getInt("detailID"));
				odb.setOrderid(rs.getString("orderid"));
				String createtime1 = rs.getString("create_time");
				if(Utility.getStringIsNull(createtime1)){
					createtime1 = createtime1.split(" ")[0];
				}
				odb.setCreatetime(createtime1);
				odb.setState(rs.getInt("state"));
				odb.setGoodsprice(rs.getString("goodsprice"));
				odb.setActual_volume(rs.getString("actual_volume"));
				odb.setActual_weight(rs.getString("actual_weight"));
				odb.setYourorder(rs.getInt("yourorder"));
				odb.setGoods_freight(rs.getString("goodsfreight"));
				odb.setGoods_url(rs.getString("car_url"));
				odb.setGoods_img(rs.getString("car_img"));
				odb.setGoods_type(rs.getString("car_type"));
				odb.setRemark(rs.getString("remark"));
				odb.setGoodsname(rs.getString("goodsname"));
				/*spider.setFreight(rs.getString("goodsfreight"));
				spider.setImg_url(rs.getString("car_img"));
				spider.setName(rs.getString("goodsname"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("googs_price"));
				spider.setRemark(rs.getString("remark"));
				spider.setUrl(rs.getString("car_url"));
				odb.setSpider(spider);*/
				odb.setProduct_cost(rs.getString("product_cost"));
				odb.setRemaining_price(rs.getString("remaining_price"));
				odb.setCurrency(rs.getString("currency"));
				spiderlist.add(odb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return spiderlist;
	
	}
	
	@Override
	public List<OrderDetailsBean> getProductDetail(int userID, int state, int startpage, int page, String orderNo) {
		String sql = "select od.id detailID,od.orderid,goodsid,goodsprice ,od.userid,car_type,od.car_url,goodsname,car_img,goodsprice,yourorder,goodsfreight,od.remark,create_time,od.state,product_cost,od.actual_weight,od.actual_volume,yourorder,remaining_price,orderinfo.currency from  order_details od,orderinfo where orderinfo.order_no=od.orderid and od.userid=?";
		if(state != -2 && state != 2){
			sql = sql + " and orderinfo.state=?  and od.state not in(1,2)";
		}else if(state == 2){
			sql = sql + " and od.state=1";
		}
		sql = sql + "  and orderinfo.order_no=? order by od.orderid desc  limit ?, ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			if(state != -2 && state != 2){
				stmt.setInt(2, state);
				stmt.setString(3, orderNo);
				stmt.setInt(4, startpage);
				stmt.setInt(5, page);
			}else{
				stmt.setString(2, orderNo);
				stmt.setInt(3, startpage);
				stmt.setInt(4, page);
			}
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderDetailsBean odb = new OrderDetailsBean();
				odb.setId(rs.getInt("detailID"));
				odb.setOrderid(rs.getString("orderid"));
				String createtime1 = rs.getString("create_time");
				if(Utility.getStringIsNull(createtime1)){
					createtime1 = createtime1.split(" ")[0];
				}
				odb.setCreatetime(createtime1);
				odb.setState(rs.getInt("state"));
				odb.setGoodsprice(rs.getString("goodsprice"));
				odb.setActual_volume(rs.getString("actual_volume"));
				odb.setActual_weight(rs.getString("actual_weight"));
				odb.setYourorder(rs.getInt("yourorder"));
				odb.setGoods_freight(rs.getString("goodsfreight"));
				odb.setGoods_url(rs.getString("car_url"));
				odb.setGoods_img(rs.getString("car_img"));
				odb.setGoods_type(rs.getString("car_type"));
				odb.setRemark(rs.getString("remark"));
				odb.setGoodsname(rs.getString("goodsname"));
				/*spider.setFreight(rs.getString("goodsfreight"));
				spider.setImg_url(rs.getString("car_img"));
				spider.setName(rs.getString("goodsname"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("googs_price"));
				spider.setRemark(rs.getString("remark"));
				spider.setUrl(rs.getString("car_url"));
				odb.setSpider(spider);*/
				odb.setProduct_cost(rs.getString("product_cost"));
				odb.setRemaining_price(rs.getString("remaining_price"));
				odb.setCurrency(rs.getString("currency"));
				spiderlist.add(odb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return spiderlist;
	
	}
	
	
	@Override
	public int getOrderdNumber(int userID, int state){
		String sql = "select count(order_details.id) counts from order_details,orderinfo where orderinfo.order_no=order_details.orderid and order_details.userid=? and orderinfo.order_show = 0 ";
		if(state != -2 && state != 2){
			sql = sql + " and orderinfo.state=?   and order_details.state not in(1,2)";
		}else if(state == 2){
			sql = sql + " and order_details.state=1 order by order_details.orderid ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int counts = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			if(state != -2 && state != 2){
				stmt.setInt(2, state);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				counts = rs.getInt("counts");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return counts;
	}
	
	/*@Override
	public List<SpiderBean> getOrdersDetails(String orderNo) {
		String sql = "select goods_car.id,itemId,shopId,order_details.userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,order_details.remark,datatime from goods_car,order_details where goods_car.id=order_details.goodsid and orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setId(rs.getInt("id"));
				spider.setItemId(rs.getString("itemId"));
				spider.setShopId(rs.getString("shopId"));
				spider.setColor(rs.getString("googs_color"));
				spider.setFreight(rs.getString("freight"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("googs_price"));
				spider.setRemark(rs.getString("remark"));
				spider.setSeller(rs.getString("googs_seller"));
				spider.setSize(rs.getString("googs_size"));
				spider.setUrl(rs.getString("goods_url"));
				spiderlist.add(spider);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return spiderlist;
	}
	*/
	
	@Override
	public List<Integer[]> getOrdersIndividual(int userid) {
//		String sql ="select count(state) counts, state from orderinfo where user_id=? and( state = 0 or state = 5 or state = 3 or state=4 or state=1 )group by state ";
		String sql = "select count(order_details.state) counts, orderinfo.state from orderinfo,order_details where orderinfo.order_no = order_details.orderid and  order_details.state not in (1,2)  and user_id=? and orderinfo.state in (0,5,3,4,1) group by orderinfo.state ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Integer[]> list = new ArrayList<Integer[]>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer[] inte = {rs.getInt("state"),rs.getInt("counts")};
				list.add(inte);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public int getOrderNumber(int userID) {
		String sql = "select count(orderid) from orderInfo where user_id=? and state=4";
		  
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}
	@Override
	public Map<String, List<String>> getConfirmThePriceOf(int userId){
		String sql = "select order_no,orderinfo.state from orderinfo,order_details where orderinfo.order_no = order_details.orderid and user_id=? and ( orderinfo.state=5 or  orderinfo.state=7) and order_details.state!=2  group by order_no";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		Map<String, List<String>> listMap = new HashMap<String, List<String>>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			List<String> cList=new ArrayList<String>();
			List<String> aList=new ArrayList<String>();
			while(rs.next()){
				int state = rs.getInt("state");
				String string = rs.getString("order_no");
				if(state ==5){
					cList.add(string);
				}else{
					aList.add(string);
				}
			}
			listMap.put("advance", aList);
			listMap.put("ver", cList);
			aList=null;cList=null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return listMap;
	}
	@Override
	public Map<String, Object> getCtpoOrderInfo(String orderNo,int userId){
		String sql = "select oi.orderid,oi.order_no,oi.product_cost,od.goodsid,od.delivery_time,od.yourorder,od.goodsname,od.goodsprice,od.orderid,car_url,od.car_img,od.remark,od.goodsfreight,purchase_state,oi.foreign_freight,oi.currency,discount_amount,order_ac,(select img from goods_typeimg where od.goodsid= goods_id) goods_typeimg,car_type,service_fee,discount_ratio from orderinfo oi,order_details od  where oi.order_no=? and oi.state=5 and od.state!=2 and od.orderid=oi.order_no  and oi.user_id=?";
		String sql2= "select goodId,ropType,oldValue,newValue,status,dateline from order_change where orderNo=? and goodId=? and ropType!=6 and status=1 and del_state=0 order by id asc";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		PreparedStatement stmt2 = null;
		ResultSet rs2=null;
		Map<String, Object> resultMap=new HashMap<String,Object>();
		List<CtpoOrderBean> ctpoList=new ArrayList<CtpoOrderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, userId);
			rs = stmt.executeQuery();
			/*float totalProductCost=0;//总共支付的钱
*/			float productCost=0;//需要支付的钱
//			String jiaohuoriqi="";
			String jiaohuoriqi="";
			String foreign_freight = "0";
			double discount_amount = 0;
			String currency = "USD";
			double order_ac = 0;
			String service_fee = "0";
			while(rs.next()){
//				OrderDetailsBean temp = new OrderDetailsBean();
				discount_amount = rs.getDouble("discount_amount");
				order_ac = rs.getDouble("order_ac");
				int goodId = rs.getInt("od.goodsid");
				CtpoOrderBean ctpoOrderBean=new CtpoOrderBean();
				ctpoOrderBean.setId(rs.getInt("oi.orderid"));
				ctpoOrderBean.setOrderNo(rs.getString("od.orderid"));
				ctpoOrderBean.setGoodId(goodId);
				ctpoOrderBean.setGoodsName(rs.getString("od.goodsname"));
				ctpoOrderBean.setGoodsUrl(rs.getString("car_url"));
				ctpoOrderBean.setGoodsImg(rs.getString("car_img"));
				ctpoOrderBean.setGoodsRemark(rs.getString("od.remark"));
				ctpoOrderBean.setPurchase_state(rs.getInt("purchase_state"));
				currency = rs.getString("currency");
				ctpoOrderBean.setCurrency(currency);
				ctpoOrderBean.setImg_type(rs.getString("goods_typeimg"));
				ctpoOrderBean.setDiscount_amount(rs.getDouble("discount_amount"));
				ctpoOrderBean.setTypes(rs.getString("car_type"));
				String freight = rs.getString("od.goodsfreight");
				ctpoOrderBean.setDiscount_ratio(rs.getDouble("discount_ratio"));
				float goodsfreight=0;
				if(Utility.getStringIsNull(freight)){
					goodsfreight=Float.parseFloat(freight);
				}
				String ff = rs.getString("foreign_freight");
				if(Utility.getStringIsNull(ff)){
					foreign_freight = ff;
				}
				String service_f = rs.getString("service_fee");
				if(Utility.getStringIsNull(service_f)){
					service_fee = service_f;
				}
				ctpoOrderBean.setGoodsfreight(freight);
				float goodsPrice=0;
				String goodsPriceStr=rs.getString("od.goodsprice");
				if(Utility.getStringIsNull(goodsPriceStr)){
					goodsPrice=Float.parseFloat(goodsPriceStr);
				}
				ctpoOrderBean.setGoodsPrice(goodsPriceStr);
				/*String productCostStr = rs.getString("oi.product_cost");
				if(Utility.getStringIsNull(productCostStr)){
					totalProductCost=Float.parseFloat(productCostStr);
				}*/
				ctpoOrderBean.setDeliveryTime(rs.getString("od.delivery_time"));
				int yourorder = rs.getInt("od.yourorder");
				ctpoOrderBean.setYourOrder(yourorder);
				List<String> type1List=new ArrayList<String>();
				List<String> type2List=new ArrayList<String>();
				List<String> type3List=new ArrayList<String>();
				List<String> type5List=new ArrayList<String>();
				List<String> type7List=new ArrayList<String>();
				stmt2=conn.prepareStatement(sql2);
				stmt2.setString(1, orderNo);
				stmt2.setInt(2, goodId);
				rs2=stmt2.executeQuery();
				boolean isCancel=false;
//				float danj=goodsPrice;
				while (rs2.next()) {
					int ropType = rs2.getInt("ropType");
					String oldValue=rs2.getString("oldValue");
					String newValue=rs2.getString("newValue");
					switch (ropType) {
					case 1://价格变动
						type1List.add(oldValue);
						type1List.add(newValue);
						/*if(Utility.getStringIsNull(newValue)){
							Float danj=Float.parseFloat(newValue);
							goodsPrice=danj;
//							ctpoOrderBean.setGoodsPrice(danj+"");
						}*/
						ctpoOrderBean.setChange1(type1List);
						break;
					case 2://交期变动
//						if(!Utility.getStringIsNull(oldValue)){
//							oldValue=Utility.getDateFormatYMD().format(new Date());
//						}else if(oldValue.trim().equals("0")){
//							oldValue=Utility.getDateFormatYMD().format(new Date());
//						}
						type2List.add(oldValue);
						type2List.add(newValue);
						if(Utility.getStringIsNull(newValue)){
							
								jiaohuoriqi=newValue;
						}
						ctpoOrderBean.setChange2(type2List);
						break;
					case 3://最小定量
						type3List.add(oldValue);
						type3List.add(newValue);
						yourorder=Integer.parseInt(newValue);
						ctpoOrderBean.setChange3(type3List);
						break;
					case 4://取消商品
						ctpoOrderBean.setChange4(newValue);
						isCancel=true;
						break;
					case 5://交流信息
						SimpleDateFormat format = new SimpleDateFormat("hhaa,MM-dd ",Locale.ENGLISH);  
						String dateline = format.format(rs2.getTimestamp("dateline"));
						if(oldValue.equals("1")){
							type5List.add(dateline+"  Supplier:"+newValue);
						}else{
							type5List.add(dateline+"  Customer:"+newValue);
						}
						break;
					case 7://国内运费
						type7List.add(oldValue);
						type7List.add(newValue);
						ctpoOrderBean.setChange7(type7List);
						break;
					default:
						break;
					}
				}
				ctpoOrderBean.setChange5(type5List);
				if(!isCancel){
//					ctpoOrderBean.setYourOrder(yourorder);
//					ctpoOrderBean.setGoodsPrice(goodsPrice+"");
					productCost=productCost+(goodsPrice*yourorder+goodsfreight);
				}
				ctpoOrderBean.setCancel(isCancel);
				ctpoList.add(ctpoOrderBean);
			}
			resultMap.put("currency", currency);
			resultMap.put("ctpoList", ctpoList);
			resultMap.put("productCost", productCost);
			resultMap.put("discount_amount", discount_amount);
			resultMap.put("order_ac", order_ac);
			resultMap.put("foreign_freight", foreign_freight);
			resultMap.put("service_fee", service_fee);
			if(jiaohuoriqi!=null){
				resultMap.put("jiaoqiTime", jiaohuoriqi);
			}else{
				resultMap.put("jiaoqiTime", "");
			}
//			resultMap.put("totalProductCost",totalProductCost);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return resultMap;
	}
	
	@Override
	public int addForwarder(Forwarder forwarder) {
		String sql = "insert forwarder(order_no,express_no,logistics_name,new_state,transport_details,createtime) values(?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
		 
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, forwarder.getOrder_no());
				stmt.setString(2, forwarder.getExpress_no());
				stmt.setString(3, forwarder.getLogistics_name());
				stmt.setString(4, forwarder.getNew_state());
				stmt.setString(5, forwarder.getTransport_details());
//				Timestamp ts = new Timestamp(forwarder.getCreatetime().getTime());
				stmt.setString(6, forwarder.getCreatetime());
				res = stmt.executeUpdate();
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	@Override
	public Forwarder getForwarder(String orderNo) {
		String sql = "select id,express_no,logistics_name,new_state,transport_details,createtime from forwarder where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		Forwarder forwarder = null;
		ResultSet rs = null;
		try {
		 
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, orderNo);
				rs = stmt.executeQuery();
				while (rs.next()) {
					forwarder = new Forwarder();
					forwarder.setId(rs.getInt("id"));
					forwarder.setOrder_no(orderNo);
					forwarder.setLogistics_name(rs.getString("logistics_name"));
					forwarder.setNew_state(rs.getString("new_state"));
					forwarder.setTransport_details(rs.getString("transport_details"));
					forwarder.setCreatetime(rs.getString("createtime"));
				}
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return forwarder;
	}

	@Override
	public int saveEvaluate(Evaluate evaluate) {
		String sql = "insert evaluate(userid,order_no,service,products,evaluate,createtime) values(?,?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
		 
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, evaluate.getUserid());
			stmt.setString(2, evaluate.getOrderNo());
			stmt.setInt(3, evaluate.getService());
			stmt.setInt(4, evaluate.getProducts());
			stmt.setString(5, evaluate.getEvaluate());
			res = stmt.executeUpdate();
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public int upOrderState(String orderNo, int state) {
		String sql = "update orderinfo set state=?,arrive_time=now()  where order_no = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setString(2, orderNo);
			res = stmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}

	@Override
	public int cancelOrder(String orderNo, int cancel_obj) {
		String sql = "update orderinfo set state=-1 ,cancel_obj = ? where order_no = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, cancel_obj);
			stmt.setString(2, orderNo);
			res = stmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}

	@Override
	public int warehouse(int userid) {
		String sql = "select count(id) counts from order_details where userid=? and state=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("counts");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}


	@Override
	public int getDelOrder(String orderNo) {
		String sql = "select count(id) counts from order_details where orderid=? and state!=2";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				res = rs.getInt("counts");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return res;
	}

	
	@Override
	public Map<String, String> getOrderChangeState(String[] orderNo) {
		//(select sum(payment_amount) from payment where orderid='1426672037944' and paystatus=1)
		//select  sum(yourorder*goodsprice+ifnull(goodsfreight,0) ) from order_details  where orderid='1426672037944'
		//select count(id) from order_change where (roptype<6 or roptype=7) and del_state=0 and orderno='1429688620802'
		
		String chageSql = "select count(id) counts,orderno from order_change where (roptype<6 or roptype=7) and del_state=0 ";
		for (int i = 0; i < orderNo.length; i++) {
			chageSql +=  " and orderno = ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			stmt = conn.prepareStatement(chageSql);
			for (int i = 0; i < orderNo.length; i++) {
				stmt.setString(i+1, orderNo[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				int counts = rs.getInt("counts");
				if(counts!=0){
					map.put(rs.getString("orderno"), counts+"");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return map;
	}
	
	@Override
	public Map<String, String> getOrderChangeState1(String[] orderNo) {
		//(select sum(payment_amount) from payment where orderid='1426672037944' and paystatus=1)
		//select  sum(yourorder*goodsprice+ifnull(goodsfreight,0) ) from order_details  where orderid='1426672037944'
		//select count(id) from order_change where (roptype<6 or roptype=7) and del_state=0 and orderno='1429688620802'
		
		String sql = "select sum(payment_amount) sums,orderid from payment where paystatus=1 ";
		String odsql = "select  sum(yourorder*goodsprice+ifnull(goodsfreight,0) ) sums,orderid from order_details  where 1=1 ";
		for (int i = 0; i < orderNo.length; i++) {
			sql +=  " and orderid = ?";
			odsql +=  " and orderid = ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < orderNo.length; i++) {
				stmt.setString(i+1, orderNo[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("orderid"), rs.getString("sums"));
			}
			stmt = conn.prepareStatement(odsql);
			for (int i = 0; i < orderNo.length; i++) {
				stmt.setString(i+1, orderNo[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String orderid = rs.getString("orderid");
				String goodsPrice = rs.getString("sums");
				float goodsPrices=0;
				if(Utility.getStringIsNull(goodsPrice)){
					goodsPrices=Float.parseFloat(goodsPrice);
				}
				String payPrice = map.get(orderid);
				float payPrices=0;
				if(Utility.getStringIsNull(payPrice)){
					payPrices=Float.parseFloat(payPrice);
				}
				if(goodsPrices > payPrices){
					map.put(orderid, "PROCESSING");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return map;
	}

	@Override
	public float getTotalPrice(String goodsids) {
		String sql = "select sum(googs_price*googs_number) sums from goods_car where  id in ("+goodsids.replace("@", ",")+")";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		float total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				 total   =  (float)(Math.round( rs.getDouble("sums")*100))/100;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return total;
	}
	
	@Override
	public void updateOrderState(int userid, String orderid) {
		// TODO Auto-generated method stub
				String sql1="update orderinfo set state=5 where user_id="+userid+" and order_no="+orderid;
				String sql2="update order_details set state=0 where userid="+userid+" and orderid="+orderid;
				Connection conn = DBHelper.getInstance().getConnection();
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				try {
						stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
						stmt2 = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
						stmt.executeUpdate();
						stmt2.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					DBHelper.getInstance().closeConnection(conn);
				}
	}

	@Override
	public int saveQuestions(String orderid, String questions) {
		String sql = "insert advance_order(orderno,questions,createtime) values(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
		 
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, orderid);
				stmt.setString(2, questions);
				res = stmt.executeUpdate();
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public AdvanceOrderBean getOrders(String orderNo) {
//		String sql = "select goods_car.id,goodsprice,goods_url,goods_title,googs_img,googs_price,googs_number,googs_size,googs_color,freight,order_details.remark from goods_car,order_details where   goods_car.id=order_details.goodsid and order_details.orderid=?";
		String sql = "select id,goodsprice,car_url,goodsname,car_img,goodsprice,yourorder,car_type,goodsfreight,order_details.remark from  order_details where order_details.orderid=?";
		String sqls = "select  id,questions,answer,freight,tariffs,createtime from advance_order where orderno=?";
		  
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AdvanceOrderBean aOrderBean = new AdvanceOrderBean();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			List<SpiderBean> list = new ArrayList<SpiderBean>();
			rs = stmt.executeQuery();
			while (rs.next()) { 
				SpiderBean spider = new SpiderBean();
				spider.setId(rs.getInt("id"));
				spider.setFreight(rs.getString("goodsfreight"));
				spider.setImg_url(rs.getString("car_img"));
				spider.setName(rs.getString("goodsname"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("goodsprice"));
				spider.setRemark(rs.getString("remark"));
				spider.setTypes(rs.getString("car_type"));
				spider.setUrl(rs.getString("car_url"));
				list.add(spider); 
			}
			aOrderBean.setSpiderBean(list);
			stmt = conn.prepareStatement(sqls);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if(rs.next()){
				aOrderBean.setAnswer(rs.getString("answer"));
				aOrderBean.setCreatetime(rs.getString("createtime"));
				aOrderBean.setFreight(rs.getString("freight"));
				aOrderBean.setId(rs.getInt("id"));
				aOrderBean.setOrderNo(orderNo);
				aOrderBean.setQuestions(rs.getString("questions"));
				aOrderBean.setTariffs(rs.getString("tariffs"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return aOrderBean;
	}

	@Override
	public List<SpiderBean> getSpiders(String orderNo) {
//		String sql = "select * from goods_car where  id in (select goodsid from order_details where orderid=?)";
		String sql = "select  id, goodsname,yourorder,goodsprice,delivery_time from  order_details where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setId(rs.getInt("id"));
				spider.setName(rs.getString("goodsname"));
				spider.setNumber(rs.getInt("yourorder"));
				spider.setPrice(rs.getString("goodsprice"));
				spider.setDelivery_time(rs.getString("delivery_time"));
				spiderlist.add(spider);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return spiderlist;

	}
	@Override
	public List<ProductChangeBean> getProductChangeInfo(String orderNo, String flag) {
		
		String sql = "select distinct orderno,aliimg,aliurl,aliprice, aliname,goodid,od.goodsprice,od.goodsid as goodscarid,od.purchase_state,od.goodsname,changeflag,car_url,car_img,car_type,oi.currency ";
		sql = sql +"from changegooddata inner join order_details od on changegooddata.orderno=od.orderid ";
//		sql = sql +" and changegooddata.goodid=od.goodsdata_id  ";
//		sql = sql + " and od.state!=2 and changegooddata.goodscarid = od.goodsid ";
//		sql = sql + " and od.state!=2 ";
		sql = sql + " and od.state!=2 ";
		if("1".equals(flag)){
			sql = sql + " and changegooddata.goodscarid = od.goodsid ";
		}else{
			sql = sql + " and changegooddata.chagoodurl = od.car_url  and od.car_type=changegooddata.goodstype ";
		}
		sql = sql +"inner join orderinfo oi on od.orderid = oi.order_no ";
		sql = sql +"where orderno =? order by goodid asc";
		
		String sql1 = "select  cg.orderno, cg.aliimg,cg.aliurl,cg.aliprice,cg.aliname, cg.chagoodimg,cg.chagoodurl,cg.chagoodprice,cg.chagoodname,cg.changeflag,gd.pID,gd.name,gd.sPrice,cg.goodstype,cg.goodscarid,gd.id as gdid,gd.img as gdimg  ";
		//sql1 = sql1 +"from changegooddata cg left join goodsdata gd on cg.chagoodurl = gd.url ";
		sql1 = sql1 +"from changegooddata cg inner join goodsdata gd on cg.chagoodurl = gd.url ";
		sql1 = sql1+" where cg.delflag=0 and cg.aliurl=? and cg.goodscarid=? ";
		 
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null,rs1 = null;
		PreparedStatement stmt = null,stmt1 = null;
		List<ProductChangeBean> alList = new ArrayList<ProductChangeBean>();
		List<ProductChangeBean> chaList = new ArrayList<ProductChangeBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				
				ProductChangeBean pcb = new ProductChangeBean();
				pcb.setGoodId(rs.getString("goodid"));
				pcb.setAliImg(rs.getString("aliimg"));
				pcb.setAliUrl(rs.getString("aliurl"));
				pcb.setAliGoodsCarUrl(rs.getString("car_url"));
				if(rs.getString("car_img").indexOf(".jpg")>1){
					pcb.setAliGoodsCarImg((rs.getString("car_img")).substring(0,(rs.getString("car_img").indexOf(".jpg")+4)));
				}else{
					pcb.setAliGoodsCarImg((rs.getString("car_img")).substring(0,(rs.getString("car_img").indexOf(".png")+4)));
				}
				pcb.setAliPrice(rs.getString("aliprice"));
				pcb.setAliName(rs.getString("aliname"));
				pcb.setGoodsPrice(rs.getString("goodsprice"));
				pcb.setGoodsCarId(rs.getString("goodscarid"));
				pcb.setPurchaseState(rs.getString("purchase_state"));
				pcb.setGoodsName(rs.getString("goodsname"));
				pcb.setChangeFlag(rs.getInt("changeflag"));
				String[] type = rs.getString("car_type").split(",");
				String[] type1 = type[0].split("@");
				pcb.setGoodsType(type1[0]+",");
				if(type.length>1){
					String[] type2 = type[1].split("@");
					pcb.setGoodsType(type1[0]+","+type2[0]);
				}
				
				pcb.setCurrency(rs.getString("currency"));
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("aliurl"));
				stmt1.setString(2, rs.getString("goodscarid"));
				rs1 = stmt1.executeQuery();
				while(rs1.next()){
					ProductChangeBean pcb1 = new ProductChangeBean();
					if(rs1.getString("chagoodimg")!=null&&!rs1.getString("chagoodimg").isEmpty()){
						pcb1.setChangeImg(rs1.getString("chagoodimg"));
					}else{
						String gdimg = rs1.getString("gdimg");
						if(gdimg!=null && !gdimg.isEmpty()){
							gdimg = gdimg.replace("[", "").replace("]", "").trim();
							String[] gdimgs = gdimg.split(",\\s+");
							if(gdimgs[0].indexOf(".jpg")>1 || gdimgs[0].indexOf(".png")>1 || gdimgs[0].indexOf(".gif")>1){
								pcb1.setChangeImg(gdimgs[0]);
							}else{
								pcb1.setChangeImg(gdimgs[0]+"jpg");
							}
							
						}
					}
					
					pcb1.setChangePrice(rs1.getString("chagoodprice"));
					//goodsdata:pid
					pcb1.setChangePid(rs1.getString("pID"));
					pcb1.setChangeUrl(rs1.getString("chagoodurl"));
					//goodsdata:name
					pcb1.setChangeName(rs1.getString("chagoodname"));
					pcb1.setAliUrl(rs1.getString("aliurl"));
					pcb1.setChangeFlag(rs1.getInt("changeflag"));
					pcb1.setGoodsType(rs1.getString("goodstype"));
					pcb1.setGdId(rs1.getString("gdid"));
					pcb1.setGoodsCarId(rs1.getString("goodscarid"));
					
					chaList.add(pcb1);
				}
				alList.add(pcb);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		for(int i=0;i<alList.size();i++){
			alList.get(i).setChangeList(new ArrayList<ProductChangeBean>());
			for(int j=0;j<chaList.size();j++){
				if(alList.get(i).getAliUrl().equals(chaList.get(j).getAliUrl()) && alList.get(i).getGoodsCarId().equals(chaList.get(j).getGoodsCarId())){
					alList.get(i).getChangeList().add(chaList.get(j));
				}
			}
		}
		
		return alList;

	}
	
	@Override
	public List<ProductChangeBean> getPriceReductionOffer(String userId) {
		
		String sql = "select tpi.goods_car_id,tpi.goods_data_id,tpi.old_goods_title,tpi.old_goods_img,tpi.old_goods_url,tpi.old_goods_price,tpi.confirm_price ,gc.currency ";
		sql = sql+"from tbl_preshoppingcar_info tpi inner join goods_car gc on tpi.goods_car_id = gc.id and gc.state=0 ";
		sql = sql +"where tpi.flag=0 and tpi.userid= ? ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<ProductChangeBean> alList = new ArrayList<ProductChangeBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(userId));
			rs = stmt.executeQuery();
			while (rs.next()) {
				
				ProductChangeBean pcb = new ProductChangeBean();
				pcb.setAliImg(rs.getString("old_goods_img"));
				pcb.setAliUrl(rs.getString("old_goods_url"));
				pcb.setAliName(rs.getString("old_goods_title"));
				pcb.setAliPrice(rs.getString("old_goods_price"));
				pcb.setChangePrice(rs.getString("confirm_price"));
				pcb.setCurrency(rs.getString("currency"));
				pcb.setGoodId(rs.getString("goods_data_id"));
				pcb.setGoodsCarId(rs.getString("goods_car_id"));
				
				alList.add(pcb);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return alList;
	}
	
	
	
	@Override
	public int upQuestions(String orderid, String questions) {
		String sql = "update advance_order set questions=?  where orderno = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, questions);
			stmt.setString(2, orderid);
			res = stmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}

	@Override
	public String getAdvance(String orderid) {
		String sql = "select id from advance_order where orderno=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String res = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}

	@Override
	public int addAdvance(String orderid,String questions) {
		String sql = "insert advance_order (orderno,questions,createtime) values(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, questions);
			res = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
	 return res;
	}
	
	@Override
	public Map<String, Object> getDelOrderByOrderNoAndStatus(String orderNo, int goodId, int status) {
		String sql1 = "select remaining_price,discount_amount,currency,order_ac,product_cost  from orderinfo oi where order_no = ?";
		String sql = "select od.orderid,od.createtime,od.state,od.goodsprice,od.actual_volume,od.actual_weight,od.yourorder,goods_class,goodsid from order_details od where od.orderid=? and (state !=2 || goodsid=?)";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		PreparedStatement stmt1 = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderDetailsBean> odbList = new ArrayList<OrderDetailsBean>();
		try {

			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, orderNo);
			rs1 = stmt1.executeQuery();
			if(rs1.next()){
				String remaining = rs1.getString("remaining_price");
				map.put("remaining_price", Utility.getStringIsNull(remaining) ? remaining : "0");
				map.put("discount_amount", rs1.getDouble("discount_amount"));
				map.put("currency", rs1.getString("currency"));
				map.put("order_ac", rs1.getDouble("order_ac"));
				map.put("product_cost", rs1.getDouble("product_cost"));
			}
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodId);
			/*stmt.setInt(3, status);*/
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderDetailsBean odb = new OrderDetailsBean();
				odb.setGoodsid(rs.getInt("goodsid"));
				odb.setOrderid(rs.getString("orderid"));
				odb.setCreatetime(rs.getString("createtime"));
				odb.setState(rs.getInt("state"));
				String spriceString = rs.getString("goodsprice");
				/*if(Utility.getStringIsNull("spriceString")){
					spriceString = rs.getString("goodsprice");
				}*/
				odb.setGoodsprice(spriceString);
				odb.setActual_volume(rs.getString("actual_volume"));
				odb.setActual_weight(rs.getString("actual_weight"));
				odb.setYourorder(rs.getInt("yourorder"));
				odb.setGoods_class(rs.getInt("goods_class"));
				odbList.add(odb);
			}
			map.put("odb", odbList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return map;
	}

	@Override
	public int upOrderPurchase(int purchase_state, String orderno, int state) {
		String sql = "update orderinfo set details_number=details_number-1 ";
		if(state == 6){
			sql = "update orderinfo set state=?,arrive_time=now(),details_number=details_number-1 ";
		}
		if(purchase_state == 1){
			sql += ",purchase_number=purchase_number-1 ";
		}
		sql += "where order_no = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			int i = 1;
			if(state == 6){
				stmt.setInt(1, state);
				i++;
			}
			stmt.setString(i, orderno);
			res = stmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}

	@Override
	public int getOrderState(String orderNo) {
		String sql = "select state from orderinfo where order_no = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int counts = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if(rs.next()){
				counts = rs.getInt("state");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return counts;
	}

	/*@Override
	public Object[] getOrderDiscount(String orderNo) {
		String sql = "select id,price from discount_chenge where orderno = ? and state=0  order by id desc limit 1";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Object[] obj = new Object[2];
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if(rs.next()){
				 obj[0] = rs.getInt("id");
				 obj[1] = rs.getDouble("price");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return obj;
	}*/
	public int upOrderDiscount(int id) {
		String sql = "update   discount_chenge set state=1 where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int counts = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			counts = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		return counts;
	}

	public List<OrderDetailsBean> getIndividualOrdersDetails(String orderNo) {
//		String sql = "select order_details.id oid,goods_car.id gid,goods_car.userid,goods_title,googs_img,goods_url,googs_size,googs_color,googs_price,googs_number,freight,checkproduct_fee,Remark,norm_least,order_details.state,paytime,goods_car.delivery_time,Actual_price,Actual_freight,Actual_weight,Actual_volume from goods_car,order_details where goods_car.id=order_details.goodsid and orderid=?";
		
		String sql="select od.delivery_time,od.userid,od.id oid,goodsname,od.checkprice_fee,od.state,od.paytime,goodsid,car_img,car_url, od.goodsfreight ,od.goodsprice,od.yourorder,yourorder,goodsfreight,od.remark,delivery_time,od.delivery_time" +
				",od.Actual_price,od.Actual_freight,od.Actual_weight,freight_free,od.Actual_volume,od.goodsid,od.fileupload,purchase_state,purchase_time,purchase_confirmation,car_type" +
				",date_format(od.createtime,'%Y-%m-%d') as create_time,(select img from goods_typeimg where goodsid = goods_id) goods_typeimg from  order_details od where od.orderid=? and od.state!=2";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String gdsUrl=null;
		List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderDetailsBean odb = new OrderDetailsBean();
				odb.setUserid(rs.getInt("od.userid"));
				odb.setOrderid(orderNo);
				odb.setId(rs.getInt("oid"));
				odb.setCheckproduct_fee(rs.getInt("od.checkprice_fee"));
				odb.setState(rs.getInt("od.state"));
				odb.setPaytime(rs.getString("od.paytime"));
				odb.setYourorder(rs.getInt("od.yourorder"));
				odb.setGoodsprice(rs.getString("od.goodsprice"));
				odb.setGoodsid(rs.getInt("od.goodsid"));
				odb.setYourorder(rs.getInt("yourorder"));
				odb.setFreight(rs.getString("od.goodsfreight"));
				odb.setGoodsname(rs.getString("goodsname"));
				odb.setGoods_img(rs.getString("car_img"));
				odb.setGoods_url(rs.getString("car_url"));
				odb.setGoodsprice(rs.getString("od.goodsprice"));
				odb.setDelivery_time(rs.getString("delivery_time"));
				odb.setFreight_free(rs.getInt("freight_free"));
				odb.setGoods_type(rs.getString("car_type"));
//				SpiderBean spider = new SpiderBean();
				int carId = rs.getInt("goodsid");
				/*spider.setId(carId);
				spider.setName(rs.getString("gc.goods_title"));
				spider.setImg_url(rs.getString("gc.googs_img"));
				spider.setUrl(rs.getString("gc.goods_url"));*/
				gdsUrl = rs.getString("car_url");
				String strin = this.findgoods(gdsUrl);
				odb.setOldUrl(strin);
//				spider.setPrice(rs.getString("od.goodsprice"));
//				spider.setNumber(rs.getInt("gc.googs_number"));
//				spider.setFreight(rs.getString("gc.freight"));
//				spider.setDelivery_time(rs.getString("gc.delivery_time"));
//				spider.setFreight_free(rs.getInt("freight_free"));
//				spider.setTypes(rs.getString("goods_type"));
//				odb.setSpider(spider);
				odb.setActual_price(rs.getDouble("od.Actual_price"));
				odb.setActual_freight(rs.getDouble("od.Actual_freight"));
				odb.setActual_weight(rs.getString("od.Actual_weight"));
				odb.setActual_volume(rs.getString("od.Actual_volume"));
				//lzj start  备注 od.fileupload在sql中没有查询这个字段  我自己加上的这个字段
				odb.setFileupload(rs.getString("od.fileupload"));
				//lzj  end
				odb.setChange_price("");
				odb.setChange_delivery("");
				odb.setNewsourceurl("");
				odb.setIscancel(0);
				odb.setRemark(rs.getString("od.remark"));
				odb.setPurchase_confirmation(rs.getString("purchase_confirmation"));
				odb.setPurchase_state(rs.getInt("purchase_state"));
				odb.setPurchase_time(rs.getString("purchase_time"));
				odb.setCreatetime(rs.getString("create_time"));
				odb.setImg_type(rs.getString("goods_typeimg"));
				String sql2="select ropType,oldValue,newValue from order_change where (ropType=1 or ropType=2 or ropType=4 or ropType=6 or ropType=7) and orderNo=? and goodId=? and del_state=0";
				stmt2=conn.prepareStatement(sql2);
				stmt2.setString(1, orderNo);
				stmt2.setInt(2, carId);			
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					int ropType = rs2.getInt("ropType");
					String values=rs2.getString("newValue");
					if(ropType==1){
						odb.setChange_price(values);
					}
					if(ropType==2){
						odb.setChange_delivery(values);
					}
					if(ropType==6){
						odb.setNewsourceurl("price:"+rs2.getString("oldValue")+"<br/><a  onclick='fnRend(\""+values+"\")' >"+values+"</a>");
					}
					if(ropType==4){
						odb.setIscancel(1);
					}
					if(ropType==7){
						odb.setChange_freight(values);
					}
					if(ropType != 6){
						odb.setPurchase_state(2);
					}
				}
				spiderlist.add(odb);
/*				stmt = conn.prepareStatement(sql);
				stmt.setString(1, orderNo);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					OrderDetailsBean odb = new OrderDetailsBean();
					odb.setUserid(rs.getInt("userid"));
					odb.setOrderid(orderNo);
					odb.setId(rs.getInt("oid"));
					odb.setCheckproduct_fee(rs.getInt("checkproduct_fee"));
					odb.setState(rs.getInt("state"));
					odb.setPaytime(rs.getString("paytime"));
					SpiderBean spider = new SpiderBean();
					int carId = rs.getInt("gid");
					spider.setId(carId);
					spider.setName(rs.getString("goods_title"));
					spider.setImg_url(rs.getString("googs_img"));
					spider.setUrl(rs.getString("goods_url"));
					spider.setSize(rs.getString("googs_size"));
					spider.setColor(rs.getString("googs_color"));
					spider.setPrice(rs.getString("googs_price"));
					spider.setNumber(rs.getInt("googs_number"));
					spider.setFreight(rs.getString("freight"));
					spider.setRemark(rs.getString("remark"));
					spider.setNorm_least(rs.getString("norm_least"));
					String ssssssssss = rs.getString("goods_car.delivery_time");
					spider.setDelivery_time(ssssssssss);
					odb.setSpider(spider);
					odb.setActual_price(rs.getDouble("Actual_price"));
					odb.setActual_freight(rs.getDouble("Actual_freight"));
					odb.setActual_weight(rs.getString("Actual_weight"));
					odb.setActual_volume(rs.getString("Actual_volume"));
					odb.setChange_price("");
					odb.setChange_delivery("");
					String sql2="select ropType,`newValue` as vss from order_change where (ropType=1 or ropType=2) and orderNo=? and goodId=?";
					stmt2=conn.prepareStatement(sql2);
					stmt2.setString(1, orderNo);
					stmt2.setInt(2, carId);
					rs2 = stmt2.executeQuery();
					while (rs2.next()) {
						int ropType = rs2.getInt("ropType");
						String values=rs2.getString("vss");
						System.out.println(ropType+"===="+values);
						if(ropType==1){
							odb.setChange_price(values);
						}
						if(ropType==2){
							odb.setChange_delivery(values);
						}
					}
					spiderlist.add(odb);
*/			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return spiderlist;
	}
	
	public String findgoods(String gdsUrl){
		String gUrl = "";
		String sql="select distinct oc.newValue from order_change oc where oc.goodId in (select id from goods_car where goods_url = ?) and oc.ropType = 6";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,gdsUrl);
			rs = stmt.executeQuery();
			while(rs.next()){
				gUrl = gUrl+"<a href='"+rs.getString("newValue")+"' target='block'>"+rs.getString("newValue")+"</a><br/><br/>";
			}
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { e.printStackTrace(); }
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) { e.printStackTrace(); }
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return gUrl;
	}
	
	@Override
	public int updateOrderInfopr(String orderNo,int userId,String productCost,String remainingPrice){
		
		String sql0 = "select remaining_price from orderinfo where order_no = ? and user_id =? ";
		String sql = "update orderinfo set product_cost=?,remaining_price=?  where order_no = ? and user_id =? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt0 = null,stmt = null;
		int res = 0;
		ResultSet rs = null;
		try {
			stmt0 = conn.prepareStatement(sql0);
			stmt0.setString(1, orderNo);
			stmt0.setInt(2, userId);

			rs = stmt0.executeQuery();
			while (rs.next()) {
				double remaingPrice = rs.getDouble("remaining_price");
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, productCost);
				stmt.setDouble(2, remaingPrice+Double.parseDouble(remainingPrice));
				stmt.setString(3, orderNo);
				stmt.setInt(4, userId);
				res = stmt.executeUpdate();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt0 != null) {
				try {
					stmt0.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	@Override
	public int updateOrderDetail(String orderNo,int userId,String goodsId,String goodSprice,String name,int goodsCarId,int id,String remark
			,String goodsUrl,String goodsImg,String goodsType){
		
		String sql = "update order_details set goodsprice=?,goodsname=?,goodsid=?,remark=?,car_url=?,car_img=?,car_type=? where orderid = ? and userid =? and goodsdata_id=? and goodsid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, goodSprice);
				stmt.setString(2, name);
				stmt.setInt(3, id);
				stmt.setString(4, remark);
				stmt.setString(5, goodsUrl);
				stmt.setString(6, goodsImg);
				stmt.setString(7, goodsType);
				stmt.setString(8, orderNo);
				stmt.setInt(9, userId);
				stmt.setInt(10, Integer.valueOf(goodsId));
				stmt.setInt(11, goodsCarId);
				res = stmt.executeUpdate();
				
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	@Override
	public int updateChangeGood(String orderNo,String goodId){
		String sql = "update changegooddata set changeflag=1 where orderno = ? and goodid =? ";
		String sql1 = "update orderinfo set domestic_freight=1 where order_no = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null,stmt1=null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setString(2, goodId);

			
			res = stmt.executeUpdate();
			
			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, orderNo);

			
			res = stmt1.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	@Override
	public int updateProductSource(String goodsCarId,String goodsdataid,String goodsUrl,String goodsImgUrl,String goodsPrice,String name,String orderNo,int goodCarKey){
		
		//String sql0 = "select max(id) maxId from goods_car ";
		String sql = "update order_product_source set goodsid=?,goodsdataid=?,goods_url=?,goods_p_url=?,goods_img_url=?,goods_price=?,goods_p_price=?,goods_name=?,goods_p_name=?,purchase_state=13 ";
		sql = sql +"  where orderid = ? and goodsid =? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, goodCarKey);
			stmt.setString(2, goodsdataid);
			stmt.setString(3, goodsUrl);
			stmt.setString(4, goodsUrl);
			stmt.setString(5, goodsImgUrl);
			stmt.setString(6, goodsPrice);
			stmt.setString(7, goodsPrice);
			stmt.setString(8, name);
			stmt.setString(9, name);
			stmt.setString(10, orderNo);
			stmt.setString(11, goodsCarId);
			res = stmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	@Override
	public int saveGoodsCar(String goodsUrl,String goodsName,String goodsImg,String goodsType,int goodsId,String remark) {
		
		String sql = "insert into goods_car (goods_url,goods_title,googs_img,goods_type,goodsdata_id,remark) values(?,?,?,?,?,?) ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int keys=-1;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, goodsUrl);
			stmt.setString(2, goodsName);
			stmt.setString(3, goodsImg);
			stmt.setString(4, goodsType);
			stmt.setInt(5, goodsId);
			stmt.setString(6, remark);
			
			int result = stmt.executeUpdate();
			if (result == 1) {
				ResultSet rs=stmt.getGeneratedKeys();
				rs.next();
				keys=rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {  
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	 return keys;
	}
	
	
	
}
