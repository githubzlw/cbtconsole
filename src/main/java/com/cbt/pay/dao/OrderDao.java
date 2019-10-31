package com.cbt.pay.dao;

import com.cbt.bean.Address;
import com.cbt.bean.Eightcatergory;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.TypeUtils;
import com.cbt.util.Utility;
import com.google.common.collect.Lists;
import com.importExpress.service.impl.SendMQServiceImpl;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderDao implements IOrderDao{

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderDao.class);

/*	@Override
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage,int page) {
		String sql = "select order_details.id detailID,order_details.orderid,goods_car.id,goodsprice,itemId,shopId,order_details.userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,remark,createtime,order_details.state,product_cost,order_details.actual_weight,order_details.actual_volume,yourorder from goods_car,order_details,orderinfo where orderinfo.order_no=order_details.orderid and goods_car.id=order_details.goodsid and order_details.userid=?";
		if(state != -2 && state != 2){
			sql = sql + " and orderinfo.state=?  and order_details.state not in(1,2)";
		}else if(state == 2){
			sql = sql + " and order_details.state=1";
		}
		sql = sql + "  order by order_details.orderid desc  limit ?, ?";
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
				odb.setCreatetime(rs.getString("createtime"));
				odb.setState(rs.getInt("state"));
				odb.setGoodsprice(rs.getString("goodsprice"));
				odb.setActual_volume(rs.getString("actual_volume"));
				odb.setActual_weight(rs.getString("actual_weight"));
				odb.setYourorder(rs.getInt("yourorder"));
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
				odb.setSpider(spider);
				odb.setProduct_cost(rs.getString("product_cost"));
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

	}*/



	@Override
	public int addOrderDetail(List<OrderDetailsBean> orderdetails) {
		// TODO Auto-generated method stub
		String sql = "insert order_details(goodsid,orderid,delivery_time,checkprice_fee,checkproduct_fee,state,fileupload,"
				+ "yourorder,userid,goodsname,goodsprice,goodsfreight,goodsdata_id,remark,goods_class,extra_freight,car_url,"
				+ "car_img,car_type,freight_free,od_bulk_volume,od_total_weight) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		int re = 0;
		try {
			for(int i=0;i<orderdetails.size();i++){
				List<String> lstValues = Lists.newArrayList();
				
				lstValues.add(String.valueOf(orderdetails.get(i).getGoodsid()));
				lstValues.add(orderdetails.get(i).getOrderid());
				lstValues.add(orderdetails.get(i).getDelivery_time());
				lstValues.add(String.valueOf(orderdetails.get(i).getCheckprice_fee()));
				lstValues.add(String.valueOf(orderdetails.get(i).getCheckproduct_fee()));
				lstValues.add(String.valueOf(orderdetails.get(i).getState()));
				lstValues.add(orderdetails.get(i).getFileupload());
				lstValues.add(String.valueOf(orderdetails.get(i).getYourorder()));
				lstValues.add(String.valueOf(orderdetails.get(i).getUserid()));
				lstValues.add(orderdetails.get(i).getGoodsname());
				lstValues.add(orderdetails.get(i).getGoodsprice());
				lstValues.add(orderdetails.get(i).getFreight());
				lstValues.add(String.valueOf(orderdetails.get(i).getGoodsdata_id()));
				lstValues.add(orderdetails.get(i).getRemark());
				lstValues.add(String.valueOf(orderdetails.get(i).getGoods_class()));
				lstValues.add(String.valueOf(orderdetails.get(i).getExtra_freight()));
				lstValues.add(orderdetails.get(i).getGoods_url());
				lstValues.add(orderdetails.get(i).getGoods_img());
				lstValues.add(orderdetails.get(i).getGoods_type());
				int freight_free = orderdetails.get(i).getFreight_free();
				lstValues.add(String.valueOf(freight_free));
				lstValues.add(orderdetails.get(i).getOd_bulk_volume());
				lstValues.add(String.valueOf(orderdetails.get(i).getOd_total_weight()));
				String runSql = DBHelper.covertToSQL(sql, lstValues);
				re = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
				
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
		}
		return re;
	}

	@Override
	public String getMaxOrderno(String userid) {
		// TODO Auto-generated method stub
		String sql ="select max(orderid) from order_details where  userid="+userid;
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String maxno="";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				maxno=rs.getString("max(orderid)");
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
		return maxno;
	}

	@Override
	public List<OrderDetailsBean> getOrderDetail(int userid, String itemid) {
		// TODO Auto-generated method stub
		String sql ="select * from order_details where userid=? and goodsid in(?)";
		Connection conn = DBHelper.getInstance().getConnection();
		List<OrderDetailsBean> odb = new ArrayList<OrderDetailsBean>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, itemid);
			rs = stmt.executeQuery();
			while(rs.next()){
				OrderDetailsBean temp = new OrderDetailsBean();
				temp.setGoodsid(rs.getInt("goodsid"));
				temp.setDelivery_time(rs.getString("delivery_time"));
				temp.setYourorder(rs.getInt("yourorder"));
				temp.setGoodsname(rs.getString("goodsname"));
				temp.setGoodsprice(rs.getString("goodsprice").replace(" ", ""));
				temp.setOrderid(rs.getString("orderid"));
				temp.setState(rs.getInt("state"));
				temp.setFreight(rs.getString("goodsfreight"));
				odb.add(temp);
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
		return odb;
	}

	@Override
	public int addAddress(com.cbt.bean.Address add) {
		// TODO Auto-generated method stub
		String sql = "insert address(userid,address,country,phone_number,zip_code,address2,statename,recipients,createtime,street) values(?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int keys=-1;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, add.getUserid());
			stmt.setString(2, add.getAddress());
			stmt.setString(3, add.getCountry());
			stmt.setString(4, add.getPhone_number());
			stmt.setString(5, add.getZip_code());
			stmt.setString(6, add.getAddress2());
			stmt.setString(7, add.getStatename());
			stmt.setString(8,add.getRecipients());
			stmt.setString(9, add.getCreatetime());
			stmt.setString(10, add.getStreet());
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

	@Override
	public List<Address> getUserAddr(int userid) {
		// TODO Auto-generated method stub
        String sql ="select address.*,zone.country as countryname from address,zone where userid="+userid+
                " and zone.id=address.country order by delflag ASC,createtime DESC";
		Connection conn = DBHelper.getInstance().getConnection2();
		List<Address> address = new ArrayList<Address>();
		Address defAddress = new Address();
		int flag=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				Address temp = new Address();
				if(rs.getInt("id") == rs.getInt("defaultaddress")){
					defAddress.setRecipients(rs.getString("recipients"));
					defAddress.setAddress(rs.getString("address"));
					defAddress.setCountry(rs.getString("country"));
					defAddress.setPhone_number(rs.getString("phone_number"));
					defAddress.setZip_code(rs.getString("zip_code"));
					defAddress.setUserid(rs.getInt("userid"));
					defAddress.setId(rs.getInt("id"));
					defAddress.setCountryname(rs.getString("countryname"));
					defAddress.setAddress2(rs.getString("address2"));
					defAddress.setStatename(rs.getString("statename"));
					defAddress.setRecipients(rs.getString("recipients"));
					defAddress.setDefaultaddress(rs.getInt("defaultaddress"));
					defAddress.setStreet(rs.getString("street"));
                    defAddress.setDelflag(rs.getString("delflag"));
					flag=1;
					continue;
				}
				temp.setRecipients(rs.getString("recipients"));
				temp.setAddress(rs.getString("address"));
				temp.setCountry(rs.getString("country"));
				temp.setPhone_number(rs.getString("phone_number"));
				temp.setZip_code(rs.getString("zip_code"));
				temp.setUserid(rs.getInt("userid"));
				temp.setId(rs.getInt("id"));
				temp.setCountryname(rs.getString("countryname"));
				temp.setAddress2(rs.getString("address2"));
				temp.setStatename(rs.getString("statename"));
				temp.setRecipients(rs.getString("recipients"));
				temp.setDefaultaddress(rs.getInt("defaultaddress"));
				temp.setStreet(rs.getString("street"));
                temp.setDelflag(rs.getString("delflag"));
				address.add(temp);
			}
			if(flag == 1){
				address.add(0, defAddress);
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
		return address;
	}

	@Override
	public int existUserAddr(int userid) {
		// TODO Auto-generated method stub
		String sql ="select id from address where userid=? and (ISNULL(address.delflag) or address.delflag='0') order by createtime";
		Connection conn = DBHelper.getInstance().getConnection();
		int flag=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("id");
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
		return flag;
	}

	@Override
	public int addOrderInfo(List<OrderBean> OrderBean,int addressid,int odcount) {
		// TODO Auto-generated method stub
		String sql = "insert orderinfo(order_no,user_id,product_cost,state,address_id,delivery_time,service_fee,"
				+ "ip,mode_transport,create_time,details_number,pay_price_three,foreign_freight,pay_price,"
				+ "pay_price_tow,currency,actual_ffreight,discount_amount,order_ac,actual_lwh,actual_weight,"
				+ "actual_weight_estimate,extra_freight,packag_number,orderRemark,cashback,exchange_rate) "
				+ "values(?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,0,?)";
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			for(int i=0;i<OrderBean.size();i++){
				List<String> lstValues = Lists.newArrayList();
				
				lstValues.add(OrderBean.get(i).getOrderNo());
				lstValues.add(String.valueOf(OrderBean.get(i).getUserid()));
				lstValues.add(OrderBean.get(i).getProduct_cost());
				lstValues.add(String.valueOf(OrderBean.get(i).getState()));
				lstValues.add(String.valueOf(addressid));
				lstValues.add(String.valueOf(OrderBean.get(i).getDeliveryTime()));
				lstValues.add(OrderBean.get(i).getService_fee());
				lstValues.add(OrderBean.get(i).getIp());
				lstValues.add(OrderBean.get(i).getMode_transport());
				lstValues.add(String.valueOf(OrderBean.get(i).getDetails_number()));
				lstValues.add(OrderBean.get(i).getPay_price_three());
				lstValues.add(OrderBean.get(i).getForeign_freight());
				lstValues.add(OrderBean.get(i).getPay_price()+"");
				lstValues.add(OrderBean.get(i).getPay_price_tow()+"");
				lstValues.add(OrderBean.get(i).getCurrency());
				lstValues.add(OrderBean.get(i).getActual_ffreight());
				lstValues.add(String.valueOf(OrderBean.get(i).getDiscount_amount()));
				lstValues.add(String.valueOf(OrderBean.get(i).getOrder_ac()));
				lstValues.add(OrderBean.get(i).getActual_lwh());
				lstValues.add(OrderBean.get(i).getActual_weight());
				lstValues.add(String.valueOf(OrderBean.get(i).getActual_weight_estimate()));
				lstValues.add(String.valueOf(OrderBean.get(i).getExtra_freight()));
				lstValues.add(OrderBean.get(i).getOrderRemark());
				lstValues.add(OrderBean.get(i).getExchange_rate());
				String runSql = DBHelper.covertToSQL(sql, lstValues);
				rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
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
		}
		return rs;
	}

	@Override
	public String getExchangeRate() {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String exchangeRate = "6.3";
		try {
			String sql="select rmb_rate from exchange_rate_daily order by id desc limit 1";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				exchangeRate=rs.getString("rmb_rate");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return exchangeRate;
	}

	@Override
	public List<OrderBean> getOrderInfo(int userid, String order_no) {
		String sql ="select order_no,foreign_freight,product_cost,state,create_time,remaining_price,currency,service_fee,pay_price,discount_amount,order_ac,extra_freight,pay_price_tow,actual_ffreight,(select count(orderid) from orderinfo where user_id=o.user_id)ordernum from orderinfo o where user_id=? and order_no=?";
		if(order_no.indexOf(",") > -1){
			order_no = "'" + order_no.replaceAll(",", "','") + "'";
			sql ="select order_no,foreign_freight,product_cost,state,create_time,remaining_price,currency,service_fee,pay_price,discount_amount,order_ac,extra_freight,pay_price_tow,actual_ffreight,(select count(orderid) from orderinfo where user_id=o.user_id)ordernum from orderinfo o where user_id=? and order_no in ("+order_no+")";
		}

		Connection conn = DBHelper.getInstance().getConnection();
		List<OrderBean> oblist = new ArrayList<OrderBean>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			if(order_no.indexOf(",") == -1){
				stmt.setString(2, order_no);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				OrderBean temp = new OrderBean();
				temp.setOrderNo(rs.getString("order_no"));
				String foreign_freight_ = rs.getString("foreign_freight");
				String product_cost_ = rs.getString("product_cost");
				temp.setProduct_cost(product_cost_);
				temp.setState(rs.getInt("state"));
				temp.setForeign_freight(foreign_freight_);
				temp.setCreatetime(rs.getString("create_time"));
				temp.setRemaining_price(rs.getDouble("remaining_price"));
				temp.setCurrency(rs.getString("currency"));
				temp.setDiscount_amount(rs.getDouble("discount_amount"));
				temp.setOrder_ac(rs.getDouble("order_ac"));
				temp.setExtra_freight(rs.getDouble("extra_freight"));
				temp.setActual_ffreight(rs.getString("actual_ffreight"));
				String pay_price=rs.getString("pay_price");
				if(pay_price != null && !"".equals(pay_price)){
					temp.setPay_price(new BigDecimal(pay_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				String service_fee = rs.getString("service_fee");
				temp.setOrderNumber(rs.getInt("ordernum") == 1 ? true : false);
				temp.setService_fee(service_fee);
				temp.setPay_price_tow(rs.getString("pay_price_tow"));
				oblist.add(temp);
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
		return oblist;
	}

	@Override
	public List<OrderBean> getOrders(int userid) {

		String sql ="select o.*,ifnull((select  max(paystatus )   paystatus  from payment  where orderid=o.order_no group by orderid ),'N') paystatus,f.order_no as express_no";
		sql = sql +" , (select count(*) from changegooddata  where orderno=o.order_no and delflag=0 ) as chaOrderNo, ";
		sql = sql +" case when TIMESTAMPDIFF(MINUTE,o.create_time,now())<=1 then 1 else 0 end as payflag ";
		sql = sql + " from orderinfo o left join forwarder f on o.order_no = f.order_no ";
		sql = sql + " where o.user_id=? and o.state in (0,1,2,3,5) and o.order_show = 0 order by o.orderid desc";
		Connection conn = DBHelper.getInstance().getConnection();
		List<OrderBean> oblist = new ArrayList<OrderBean>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while(rs.next()){
				OrderBean temp = new OrderBean();
				temp.setOrderNo(rs.getString("order_no"));
				temp.setForeign_freight(rs.getString("foreign_freight"));
				String product_cost_ = rs.getString("product_cost");
				double fp = Double.parseDouble(product_cost_);
				temp.setProduct_cost(new BigDecimal(fp).setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
				temp.setState(rs.getInt("state"));
				temp.setCreatetime(rs.getString("create_time").substring(0, 16));
				temp.setRemaining_price(rs.getDouble("remaining_price"));
				String pay_price=rs.getString("pay_price");
				if(pay_price != null && !"".equals(pay_price)){
					temp.setPay_price(new BigDecimal(pay_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				temp.setMode_transport(rs.getString("mode_transport"));
				temp.setServer_update(rs.getInt("server_update"));
				temp.setCurrency(rs.getString("currency"));
				temp.setActual_ffreight(rs.getString("actual_ffreight"));
				temp.setPaystatus(rs.getString("paystatus"));
				temp.setExpressNo(rs.getString("express_no"));
				temp.setDeliveryTime(rs.getInt("delivery_time"));
				temp.setService_fee(rs.getString("service_fee"));
				temp.setChaOrderNo(rs.getString("chaOrderNo"));
				//Subsitution Confirmed标识
				temp.setDomestic_freight(rs.getString("domestic_freight"));

				//免邮费免邮判断
				if(rs.getString("mode_transport").indexOf("@0@all") != -1
						||rs.getString("mode_transport").indexOf("@0.0@all") != -1) {
					temp.setFree_shipping(1);
				}else{
					temp.setFree_shipping(0);
				}
				//未支付订单先放到已支付订单里，1分钟内如果没支付在 放回未支付订单
				temp.setPayFlag(rs.getInt("payflag"));

				oblist.add(temp);
			}

		} catch (Exception e) {
			System.out.println("Has Error User ID:"+userid);
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
		return oblist;
	}

	@Override
	public int updateOrderShowFlag(int userid) {
		// TODO Auto-generated method stub
		String sql = "select   max(pm.createtime) as paymentTime  from orderinfo o ";
		sql = sql + "inner join payment pm on o.order_no = pm.orderid ";
		sql = sql +" where o.user_id= "+userid ;
		sql =sql +" and o.state=5 ";

		String sql1 ="update orderinfo o set o.order_show =1 ";
		sql1 = sql1 +" where o.user_id= "+userid ;
		sql1 =sql1 +" and o.state=0 and TIMESTAMPDIFF(MINUTE,o.create_time,?)<=30 and create_time<? ";


		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null,stmt1 = null;
		ResultSet rs = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("paymentTime"));
				stmt1.setString(2, rs.getString("paymentTime"));
				res = stmt1.executeUpdate();
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
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}


	@Override
	public int updateUnpaidOrderShowFlag(int userid) {

		String sql = "select max(create_time) as createTime from  orderinfo where user_id ="+userid+" and state=0  and   order_show=0";

		String sql1 ="update orderinfo set order_show=1 where user_id ="+userid+" ";
		sql1 = sql1 +" and state=0   and   order_show=0 and TIMESTAMPDIFF(MINUTE,create_time,?)<=10 and create_time<? ";

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null,stmt1 = null;
		ResultSet rs = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("createTime"));
				stmt1.setString(2, rs.getString("createTime"));
				res = stmt1.executeUpdate();
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
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}


	@Override
	public int updateGoodscarState(int userid, String itemid) {
		// TODO Auto-generated method stub
		String sql = "update goods_car set state=1 where id in (select goodsid from order_details where  orderid = ?)";
		if(itemid.indexOf(",") > -1){
			itemid = "'" + itemid.replaceAll(",", "','") + "'";
			sql = "update goods_car set state=1 where id in (select goodsid from order_details where  orderid in ("+itemid+"))";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			if(itemid.indexOf(",") == -1){
				stmt.setString(1, itemid);
			}
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
	public int updateGoodscarState(String itemid) {
		// TODO Auto-generated method stub
		String sql = "update goods_car set state=1 where id in ("+itemid.replace("@", ",")+")";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
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
	public void updateOrderState(int userid, String orderid,String pay_price_three) {
		// TODO Auto-generated method stub
		String sql1="update orderinfo set state=5,pay_price_three=? where user_id=? and order_no=?";
		String sql2="update order_details set state=0 where userid=? and orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, pay_price_three);
			stmt.setInt(2, userid);
			stmt.setString(3, orderid);
			stmt.executeUpdate();
			stmt2 = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
			stmt2.setInt(1, userid);
			stmt2.setString(2, orderid);
			stmt2.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public int updateUserAddress(int id,String address,String country,String phonenumber,String zipcode,String address2,String statename, String recipients, String street) {
		// TODO Auto-generated method stub
		String sql="update address set address=?,country=?,phone_number=?, zip_code=?,address2=?,statename=?,modifytime=? ";
		if(recipients != null){
			sql =sql+",recipients=? ";
		}
		if(street != null){
			sql =sql+" ,street=?";
		}
		sql =sql+"where id=?" ;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, address);
			stmt.setString(2, country);
			stmt.setString(3, phonenumber);
			stmt.setString(4, zipcode);
			stmt.setString(5, address2);
			stmt.setString(6, statename);
			stmt.setString(7, Utility.format(new Date(), Utility.datePattern1));
			int i = 8;
			if(recipients != null){
				stmt.setString(i, recipients);
				i++;
			}
			if(street != null){
				stmt.setString(i, street);
				i++;
			}
			stmt.setInt(i, id);
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
	public List<Eightcatergory> getHomefurnitureProduct(String catergory) {
		// TODO Auto-generated method stub
		String sql ="select row,catergory,minorder,unit,price,url,imgurl,productname from eightcatergory  where valid=1 and ";
		String[] catas = catergory.split("@");
		if(catergory.indexOf("@") > -1){
			sql += "  catergory = ?";
			for (int i = 1; i < catas.length; i++) {
				sql += " or catergory = ? ";
			}
		}else{
			sql += " catergory=? ";
		}

		sql += "order by id asc";
		Connection conn = DBHelper.getInstance().getConnection();
		List<Eightcatergory> home = new ArrayList<Eightcatergory>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(catergory.indexOf("@") > -1){
				for (int i = 0; i < catas.length; i++) {
					stmt.setString(i+1, catas[i]);
				}
			}else{
				stmt.setString(1, catergory);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				Eightcatergory temp=new Eightcatergory();
				temp.setId(rs.getInt("row"));
				temp.setCatergory(rs.getString("catergory"));
				temp.setMinorder(rs.getInt("minorder"));
				temp.setUnit(rs.getString("unit"));
				temp.setPrice(rs.getFloat("price"));
				String url = rs.getString("url");
				temp.setUrl(TypeUtils.encodeGoods(url));
				temp.setImgurl(rs.getString("imgurl"));
				temp.setProductname(rs.getString("productname"));
				home.add(temp);
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
		return home;
	}
	@Override
	public void updateGoodscarStateAgain(int userid, String itemid) {
		// TODO Auto-generated method stub
		String sql = "update goods_car set state=1 where id in("+itemid+") and userid="+userid;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();

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
	public void delUserAddressByid(int id) {
		// TODO Auto-generated method stub
		String sql = "update address set delflag=1 where id="+id;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
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
	public void setDefault(int id, int userid) {
		// TODO Auto-generated method stub
		String sql = "update address set defaultaddress="+id+" where userid="+userid;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
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
	public Address getUserAddrById(int id) {
		// TODO Auto-generated method stub
		String sql ="select address.*,zone.country as countryname from address,zone where address.id="+id+" and zone.id=address.country and (ISNULL(address.delflag) or address.delflag='0') order by createtime";
		Connection conn = DBHelper.getInstance().getConnection();
		Address temp = new Address();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				temp.setRecipients(rs.getString("recipients"));
				temp.setAddress(rs.getString("address"));
				temp.setCountry(rs.getString("country"));
				temp.setPhone_number(rs.getString("phone_number"));
				temp.setZip_code(rs.getString("zip_code"));
				temp.setUserid(rs.getInt("userid"));
				temp.setId(rs.getInt("id"));
				temp.setCountryname(rs.getString("countryname"));
				temp.setAddress2(rs.getString("address2"));
				temp.setStatename(rs.getString("statename"));
				temp.setRecipients(rs.getString("recipients"));
				temp.setDefaultaddress(rs.getInt("defaultaddress"));
				temp.setStreet(rs.getString("street"));
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
		return temp;
	}

	@Override
	public int getAddressCountByUserId(int userid) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String sql ="select count(*) count from address where (ISNULL(address.delflag) or address.delflag='0') and userid="+userid;
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int total=0;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				total=rs.getInt("count");
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
	public int getPriceReduction(int userid) {

		String sql ="select count(*) count from tbl_preshoppingcar_info tpi ";
		sql = sql +" inner join goods_car gc on tpi.userid=gc.userid and tpi.goods_data_id=gc.goodsdata_id and tpi.goods_car_id= gc.id and gc.state=0 ";
		sql = sql +" where tpi.flag=0 and tpi.userid="+userid;
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int total=0;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				total=rs.getInt("count");
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


	//合并订单
	public void mergeOrder(String orderid,int userid){
		String newOrderNo=System.currentTimeMillis()+"";
		String sql1 = "insert  into orderinfo(user_id,order_no,product_cost,state,create_time) select userid,'"+newOrderNo+"',sum(ROUND(yourorder*goodsprice,2)),'0',now() from order_details where orderid in("+orderid+")";
		String sql2 = "update order_details set orderid='"+newOrderNo+"' where orderid in("+orderid+")";
		String sql3 = "select id from address where userid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		try {
//			stmt3 = conn.prepareStatement(sql3);
//			rs = stmt3.executeQuery();
//			while(rs.next()){
//
//			}
			stmt1 = conn.prepareStatement(sql1);
			stmt1.executeUpdate();
			stmt2 = conn.prepareStatement(sql2);
			stmt2.executeUpdate();
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
			if (stmt1 != null) {
				try {
					stmt1.close();
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
	}

	@Override
	public void updateOrderPayPrice(int userid, String order_no, String pay_price, String ipnAddress, double discount_amount, double product_cost,double remaining_price) {
		// TODO Auto-generated method stub
		String sql1="update orderinfo set pay_price=?,pay_price_tow = actual_ffreight";
		if(ipnAddress != null){
			sql1=sql1+", ipnaddress='"+ipnAddress+"'";
		}
		if(discount_amount >= 0){
			sql1=sql1+", discount_amount=?";
		}
		if(product_cost >= 0){
			sql1=sql1+", product_cost=?";
		}
		if(remaining_price >= 0){
			sql1=sql1+", remaining_price=?";
		}
		sql1=sql1+" where user_id=? and order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, pay_price);
			int  i = 2;
			if(discount_amount >= 0){
				stmt.setDouble(i, discount_amount);
				i++;
			}
			if(product_cost >= 0){
				stmt.setDouble(i, product_cost);
				i++;
			}
			if(remaining_price >= 0){
				stmt.setDouble(i, remaining_price);
				i++;
			}
			stmt.setInt(i, userid);
			stmt.setString(i+1, order_no);
			int result=stmt.executeUpdate();
			LOG.debug("---------updateOrderPayPrice:"+result);
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
	public int getOrderid(){
		String sql ="select MAX(orderid) as orderid from orderinfo";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int orderid=0;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				orderid=rs.getInt("orderid");
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
		return orderid;
	}

	@Override
	public int addOrderAddress(Map<String,Object> map) {
		// TODO Auto-generated method stub
		String sql = "insert into order_address(AddressID,orderNo,Country,statename,address,address2,"
				+ "phoneNumber,zipcode,Adstatus,street,recipients) values(?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int keys=-1;
		try {
			String querySql = "select id from order_address order by id desc limit 1";
			stmt = conn.prepareStatement(querySql);
			rs = stmt.executeQuery();
			int id = 0;
			if(rs.next()) {
				id = rs.getInt("id");
			}
			List<String> lstValues = Lists.newArrayList();
			int addressid=Utility.getInt(map.get("addressid").toString());
			String orderno=Utility.formatObject(map.get("orderno"));
			String country=Utility.formatObject(map.get("country"));
			String statename=Utility.formatObject(map.get("statename"));
			String address=Utility.formatObject(map.get("address"));
			String address2=Utility.formatObject(map.get("address2"));
			String phoneNumber=Utility.formatObject(map.get("phoneNumber"));
			String zipcode=Utility.formatObject(map.get("zipcode"));
			String street=Utility.formatObject(map.get("street"));
			String recipients=Utility.formatObject(map.get("recipients"));
			lstValues.add(String.valueOf(addressid));
			lstValues.add(orderno);
			lstValues.add(country);
			lstValues.add(statename);
			lstValues.add(address);
			lstValues.add(address2);
			lstValues.add(phoneNumber);
			lstValues.add(zipcode);
			lstValues.add("0");
			lstValues.add(street);
			lstValues.add(recipients);
			
			String runSql = DBHelper.covertToSQL(sql, lstValues);
			int result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
			if (result == 1) {
				keys=id+1;
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return keys;
	}

	@Override
	public int addOrderAddress(List<Map<String,Object>> maps) {
		// TODO Auto-generated method stub
		String sql = "insert into order_address(AddressID,orderNo,Country,statename,address,address2,phoneNumber,"
				+ "zipcode,Adstatus,street,recipients) values(?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int keys=-1;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map = maps.get(i);
				int addressid=Utility.getInt(map.get("addressid").toString());
				String orderno=Utility.formatObject(map.get("orderno"));
				String country=Utility.formatObject(map.get("country"));
				String statename=Utility.formatObject(map.get("statename"));
				String address=Utility.formatObject(map.get("address"));
				String address2=Utility.formatObject(map.get("address2"));
				String phoneNumber=Utility.formatObject(map.get("phoneNumber"));
				String zipcode=Utility.formatObject(map.get("zipcode"));
				String street=Utility.formatObject(map.get("street"));
				String recipients=Utility.formatObject(map.get("recipients"));
				stmt.setInt(1, addressid);
				stmt.setString(2, orderno);
				stmt.setString(3, country);
				stmt.setString(4, statename);
				stmt.setString(5, address);
				stmt.setString(6, address2);
				stmt.setString(7, phoneNumber);
				stmt.setString(8, zipcode);
				stmt.setString(9, "0");
				stmt.setString(10, street);
				stmt.setString(11, recipients);
				stmt.addBatch();
			}
			int[] result = stmt.executeBatch();
			if (result.length > 1) {
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

	@Override
	public Address getOrderAddress(String orderid) {
		// TODO Auto-generated method stub
		String sql="select a.*,z.id as new_zid from order_address a left join zone z on REPLACE(a.country,' ','')=REPLACE(z.country,' ','')  where a.orderNo=?";
		String sql1="select * from zone where id=?";
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection conn = DBHelper.getInstance().getConnection();
		Address temp = new Address();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderid);
			rs=stmt.executeQuery();
			int countryid=0;
			while(rs.next()){
				temp.setAddress(StringUtils.isStrNull(rs.getString("address"))?"":rs.getString("address"));
				temp.setAddress2(StringUtils.isStrNull(rs.getString("address2"))?"":rs.getString("address2"));
				temp.setPhone_number(StringUtils.isStrNull(rs.getString("phoneNumber"))?"":rs.getString("phoneNumber"));
				temp.setZip_code(StringUtils.isStrNull(rs.getString("zipcode"))?"":rs.getString("zipcode"));
				temp.setStatename(StringUtils.isStrNull(rs.getString("statename"))?"":rs.getString("statename"));
				temp.setCountry(StringUtils.isStrNull(rs.getString("Country"))?"":rs.getString("Country"));
				temp.setRecipients(StringUtils.isStrNull(rs.getString("recipients"))?"":rs.getString("recipients"));
				countryid=Integer.parseInt(StringUtils.isStrNull(rs.getString("new_zid"))?rs.getString("Country"):rs.getString("new_zid"));
				temp.setStreet(StringUtils.isStrNull(rs.getString("street"))?"":rs.getString("street"));
			}
			stmt1 = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt1.setInt(1, countryid);
			rs1=stmt1.executeQuery();
			while(rs1.next()){
				temp.setCountryname(StringUtils.isStrNull(rs1.getString("country"))?"":rs1.getString("country"));
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
		return temp;
	}

	public int updateOrderAddress(Address address, String orderid) {
		// TODO Auto-generated method stub
		String sql="update order_address set Country=?,statename=?,address=?,address2=?,phoneNumber=?,zipcode=?,recipients=?,street=? where orderNo=?";
		int result=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, address.getCountry());
			stmt.setString(2, address.getStatename());
			stmt.setString(3, address.getAddress());
			stmt.setString(4, address.getAddress2());
			stmt.setString(5, address.getPhone_number());
			stmt.setString(6, address.getZip_code());
			stmt.setString(7, address.getRecipients());
			stmt.setString(8, address.getStreet());
			stmt.setString(9, orderid);
			result=stmt.executeUpdate();
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
		return result;
	}
	
	@Override
	public void updateOnlineOrderAddress(Address addr, String orderid) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("update order_address set Country=\"").append(addr.getCountry())
				.append("\",statename=\"").append(addr.getStatename())
				.append("\",address=\"").append(addr.getAddress())
				.append("\",address2=\"").append(addr.getAddress2())
				.append("\",phoneNumber=\"").append(addr.getPhone_number())
				.append("\",zipcode=\"").append(addr.getZip_code())
				.append("\",recipients=\"").append(addr.getRecipients())
				.append("\",street=\"").append(addr.getStreet())
				.append("\" where orderNo=\"").append(orderid)
				.append("\"");
		SendMQServiceImpl sendMQ = new SendMQServiceImpl();
		sendMQ.runSqlOnline(orderid, sBuffer.toString());
	}

	@Override
	public String getOrderProductcost(String orderid) {
		// TODO Auto-generated method stub
		String sql1="select ROUND(sum(yourorder*goodsprice),2) as sum from order_details where orderid in(?)";
		String result="";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderid);
			rs=stmt.executeQuery();
			if(rs.next()){
				result=rs.getString("sum");
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
		return result;
	}

	@Override
	public void updateOrderinfo(String product_cost,String service_fee,String orderid) {
		// TODO Auto-generated method stub
		String sql1="update orderinfo set product_cost=?,service_fee=? where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, product_cost);
			stmt.setString(2, service_fee);
			stmt.setString(3, orderid);
			stmt.executeUpdate();
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
	public int delGoods(String goodsid, String orderid) {
		// TODO Auto-generated method stub
		String sql="delete from order_details where goodsid=? and orderid=?";
		String sql1="update orderinfo set details_number=details_number -1 where order_no=?";
		int result=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, goodsid);
			stmt.setString(2, orderid);
			result=stmt.executeUpdate();
			stmt1 = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt1.setString(1, orderid);
			stmt1.executeUpdate();
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
		return result;
	}

	@Override
	public String getOrderNo() {
		Connection conn = DBHelper.getInstance().getConnection2();
		java.sql.CallableStatement stmt = null;
		String orderNo = "";
		try {
			stmt = conn.prepareCall("{call GetOrderIndex()}");

			ResultSet rest = stmt.executeQuery();

			// getXxx(index)中的index 需要和上面registerOutParameter的index对应
			if(rest.next()){
				orderNo = rest.getString("UUID");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
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
		return orderNo;
	}

	@Override
	public String initCheckData(String orderNos) {
		String[] strl=    orderNos.split(",");
		java.sql.CallableStatement stmt = null;
		Connection conn = DBHelper.getInstance().getConnection();
		try {
			for(int icnt=0;icnt<strl.length;icnt++)
			{
				stmt = conn.prepareCall("{call initCheckData(?)}");
				stmt.setString(1, strl[icnt]);
				stmt.execute();
				stmt.close();
				stmt=null;
			}
		} catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return "";
	}
	public static void main(String[] args) {
		OrderDao orderDao = new OrderDao();
		orderDao.initCheckData("1");
	}

	@Override
	public void updateOrderStatePayPrice(int userid,String orderid,String pay_price_three,String pryprice,String ipnAddress,double order_ac) {
		LOG.debug("---------updateOrderPayPrice-ipnAddress:"+ipnAddress);
		// TODO Auto-generated method stub
		String sql1="update orderinfo set remaining_price=0, pay_price=?,pay_price_tow = if( mode_transport like('%product' ),0,foreign_freight),state=5,pay_price_three=?,order_ac=?";
		if(ipnAddress != null){
			sql1=sql1+", ipnaddress=?";
		}
		sql1=sql1+" where user_id=? and order_no=?";
		String sql2="update order_details set state=0 where userid=? and orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, pryprice);
			stmt.setString(2, pay_price_three);
			stmt.setDouble(3, order_ac);
			int i = 4;
			if(ipnAddress != null){stmt.setString(i, ipnAddress);i++;}
			stmt.setInt(i, userid);
			stmt.setString(i+1, orderid);
			int result=stmt.executeUpdate();
			stmt2 = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
			stmt2.setInt(1, userid);
			stmt2.setString(2, orderid);
			stmt2.executeUpdate();
			LOG.debug("---------updateOrderPayPrice:"+result);
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
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public void updateOrderStatePayPrice(int userid,List<String[]> orderinfo,String ipnAddress) {
		LOG.debug("---------updateOrderPayPrice-ipnAddress:"+ipnAddress);
		// TODO Auto-generated method stub
		String sql1="update orderinfo set remaining_price=0, pay_price=?,pay_price_tow = foreign_freight,state=5,pay_price_three=?";
		if(ipnAddress != null){
			sql1=sql1+", ipnaddress=? ";
		}
		sql1=sql1+" where user_id=? and order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			String orderids = "";
			stmt = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);

			for (int i = 0; i < orderinfo.size(); i++) {
				stmt.setString(1, orderinfo.get(i)[1]);
				stmt.setString(2, orderinfo.get(i)[2]);
				int j = 3;
				if(ipnAddress != null){
					stmt.setString(3, ipnAddress);
					j ++;
				}
				stmt.setInt(j, userid);
				stmt.setString(j+1, orderinfo.get(i)[0]);
				stmt.addBatch();
				orderids += orderinfo.get(i)[0] + ",";
			}
			int[] result=stmt.executeBatch();
			orderids.substring(0,orderids.length()-1);
			orderids = "'" + orderids.replaceAll(",", "','") + "'";
			String sql2="update order_details set state=0 where userid=? and orderid in ("+orderids+")";
			stmt2 = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
			stmt2.setInt(1, userid);
			stmt2.executeUpdate();
			LOG.debug("---------updateOrderPayPrice:"+result);
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
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public int saveOrder_discount(String orderno, int discounttype,
								  double price, String discountinfo) {
		String sql = "insert order_discount(orderno,discounttype,price,discountinfo,createtime) values(?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderno);
			stmt.setInt(2, discounttype);
			stmt.setDouble(3, price);
			stmt.setString(4, discountinfo);
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
	public void updateChangeDelFlag(String orderNo,int goodsCarid){

		String sql="update changegooddata set delflag=1 where orderno=? and goodscarid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodsCarid);
			int result=stmt.executeUpdate();
			LOG.debug("---------server_update:"+result);
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
	public int upOrderExpress(String orderno, String mode_transport,
							  String actual_ffreight) {
		// TODO Auto-generated method stub
		String sql = "update orderinfo set mode_transport=?,actual_ffreight=? where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, mode_transport);
			stmt.setString(2, actual_ffreight);
			stmt.setString(3, orderno);
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
	public int upOrderExpress(String orderno, String mode_transport,
							  String actual_ffreight, String remaining_price, String pay_price_tow, String service_fee, double pay_price) {
		// TODO Auto-generated method stub
		String sql = "update orderinfo set mode_transport=?,actual_ffreight=?,remaining_price=?,pay_price_tow=?,foreign_freight=?,service_fee=?,pay_price=? where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, mode_transport);
			stmt.setString(2, actual_ffreight);
			stmt.setString(3, remaining_price);
			stmt.setString(4, pay_price_tow);
			stmt.setString(5, actual_ffreight);
			stmt.setString(6, service_fee);
			stmt.setDouble(7, pay_price);
			stmt.setString(8, orderno);
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
	public int upOrderService_fee(String orderno, String service_fee) {
		String sql = "update orderinfo set service_fee=? where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, service_fee);
			stmt.setString(2, orderno);
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
	public int cancelOrder(String orderid) {
		String sql = "update orderinfo set state=-1 where order_no=?";
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt1 = null;
		int res = 0;
		try {
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(orderid);
			String runSql = DBHelper.covertToSQL(sql, lstValues);
			res = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

			stmt1 = conn1.prepareStatement(sql);
			stmt1.setString(1, orderid);
			res = stmt1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn1);
		}
		return res;
	}
}
