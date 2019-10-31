package com.cbt.method.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Md5Util;
import com.google.common.collect.Lists;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.mysql.jdbc.Statement;

public class OrderDetailsDao implements IOrderDetailsDao {

	public List<OrderDetailsMethodBean> getOrderDetailsByOrderNo(int userid,
                                                                 String goodsids) {
		List<OrderDetailsMethodBean> list = new ArrayList<OrderDetailsMethodBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		DecimalFormat df = new DecimalFormat("#0.###");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String orderList[] = goodsids.split(",");
		for (int i = 0; i < orderList.length; i++) {
			String orderid = orderList[i].split(":")[0];
			String goodsid = orderList[i].split(":")[1];
			String sql = "SELECT od.car_img,od.car_type,od.goodsprice,ifnull(od.yourorder,1) as yourorder,od.id,od.goodscatid,od.orderid,ifnull(od.seilUnit,'piece') as seilUnit FROM order_details od where  od.id ='"
					+ goodsid + "' and od.orderid='" + orderid + "'";
			try {
				stmt = conn.prepareStatement(sql.toString());
				rs = stmt.executeQuery();
				while (rs.next()) {
					OrderDetailsMethodBean o = new OrderDetailsMethodBean();
					o.setCarImg(rs.getString("car_img"));// 图片
					String types = rs.getString("car_type");
					String types1 = "";
					if (types != null && !"".equals(types)
							&& types.indexOf("<") > -1
							&& types.indexOf(">") > -1) {
						for (int j = 1; j < 4 && types.indexOf("<") > -1
								&& types.indexOf(">") > -1; j++) {
							types1 = types.substring(types.indexOf("<"),
									types.indexOf(">") + 1);
							types = types.replace(types1, "");
						}
					}
					o.setCarType(types);// 规格
					o.setGoodsPrice(df.format(Double.valueOf(rs
							.getString("goodsprice")) * 6.4934));// 价格
					o.setYourOrder(rs.getInt("yourorder"));// 数量
					o.setOdid(rs.getInt("id"));// id
					o.setGoodscatid(rs.getString("goodscatid"));
					o.setOrderid(rs.getString("orderid"));
					o.setSeilUnit(rs.getString("seilUnit"));
					list.add(o);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	public String getUserName(int id) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String admName = "";
		String sql = "select admName from admuser where id=?";
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				admName = rs.getString("admName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBHelper.getInstance().closeConnection(conn);
		return admName;
	}

	public String getItemid(String u) {
		if (u.length() < 12) {// http://aaa&123
			return "0";
		}
		String ret = "";
		Pattern p = Pattern.compile("\\d{2,}");// 这个2是指连续数字的最少个数
		String maxStr = "";
		Matcher m = p.matcher(u);
		int i = 0;
		while (m.find()) {
			String temp = m.group();
			int c = u.indexOf(temp);
			int len = c + m.group().length() + 5;
			if (len > u.length()) {
				len = c + m.group().length();
			}
			temp = u.substring(c - 4, len);
			if (temp.indexOf("?id=") != -1 || temp.indexOf("&id=") != -1
					|| temp.indexOf(".html") != -1) {
				if (m.group().length() > maxStr.length()) {
					maxStr = m.group();
				}
			}
			i++;
		}
		ret = maxStr;
		return ret;
	}

	// 录入货源
	public void AddRecource(String type, int admid, int userid,
							int goodsdataid, String goods_url, String googs_img,
							double goodsprice, String goods_title, int googsnumber,
							String orderNo, int od_id, int goodid, double price,
							String resource, int buycount, String reason, String currency,
							String pname, boolean allReplcae,String shop_url,String shop_name,String address) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int iib = 0;
		try {
			String itemid = getItemid(resource);
			if (itemid == null || "".equals(itemid)) {
				itemid = "0000";
			}
			String sql1 = "";
			if (allReplcae) {
				sql1 = "select * from order_details where orderid=? and car_url=?";
				stmt = conn.prepareStatement(sql1);
				stmt.setString(1, orderNo);
				stmt.setString(2, goods_url);
			} else {
				sql1 = "select * from order_details where id=?";
				stmt = conn.prepareStatement(sql1);
				stmt.setInt(1, od_id);
			}
			rs1 = stmt.executeQuery();
			while (rs1.next()) {
				od_id = rs1.getInt("id");
				goodid = rs1.getInt("goodsid");
				goodsdataid = rs1.getInt("goodsdata_id");
				goodsprice = rs1.getDouble("goodsprice");
				googsnumber = rs1.getInt("yourorder");
//				buycount = rs1.getInt("yourorder");
				// ============
				String sql = "delete from order_product_source where orderid='"+ orderNo + "' and goodsid=" + goodid + "";
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				// order_product_sourceon  DUPLICATE key update re_shipno=#{re_shipno},remark=#{remark},itemqty=#{counts};
				sql = "insert into order_product_source(adminid,userid,addtime,orderid,goodsid,goodsdataid,goods_url,goods_p_url,"
						+ "goods_img_url,goods_price,goods_p_price,goods_name,usecount,buycount,unquestionsRemarks,purchase_state,"
						+ "currency,goods_p_name,od_id,tb_1688_itemid,goodssourcetype,shop_id,shop_name,tborderid) values(?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,?,?,?,?,?,'重复1') ";
				stmt = conn.prepareStatement(sql);
				stmt.setDouble(1, admid);
				stmt.setInt(2, userid);
				stmt.setString(3, orderNo);
				stmt.setInt(4, goodid);
				stmt.setInt(5, goodsdataid);
				stmt.setString(6, goods_url);
				stmt.setString(7, resource);
				stmt.setString(8, googs_img);
				stmt.setDouble(9, goodsprice);
				stmt.setDouble(10, price);
				stmt.setString(11, goods_title);
				stmt.setInt(12, googsnumber);//销售数量
				stmt.setInt(13, googsnumber);//采购数量（客户下单数量）
//				stmt.setInt(13, buycount);//采购数量（1688产品单页输入的数量）
				stmt.setString(14, reason);
				stmt.setString(15, currency);
				stmt.setString(16, pname);
				stmt.setInt(17, od_id);
				stmt.setString(18, itemid);
				stmt.setString(19, type.split("@")[0].toString());
				stmt.setString(20, shop_url);
				stmt.setString(21, shop_name);
				stmt.executeUpdate();
				// goods_source
				String sqllll = "SELECT distinct od.goods_pid,od.car_urlMD5,gs.shop_id,gs.address FROM goods_source gs "
						+ "INNER JOIN order_details od ON gs.goods_pid=od.goods_pid AND gs.car_urlMD5=od.car_urlMD5 "
						+ "WHERE od.id='"+od_id+"' AND gs.goods_purl='"+resource+"'";
				stmt = conn.prepareStatement(sqllll);
				rs = stmt.executeQuery();
				if(rs.next()){
					sqllll = "update goods_source set goods_purl='" + resource
							+ "', goods_img_url=?,goods_price=?,buycount=?,"
							+ "updatetime=now(),shop_id='"+(StringUtils.isStrNull(shop_url)?rs.getString("shop_url"):shop_url)+"',address='"+(StringUtils.isStrNull(address)?rs.getString("address"):address)+"' where goods_pid='"+rs.getString("goods_pid")+"' and car_urlMD5='"+rs.getString("car_urlMD5")+"' and goods_purl=?";
					stmt = conn.prepareStatement(sqllll);
					stmt.setString(1, googs_img);
					stmt.setDouble(2, price);
					stmt.setInt(3, buycount);
					stmt.setString(4, resource);
				}else{
					sqllll="select goods_pid,car_urlMD5 from order_details where id='"+od_id+"'";
					stmt = conn.prepareStatement(sqllll);
					rs = stmt.executeQuery();
					if(rs.next()){
						sqllll = "insert into goods_source(goodsdataid,goods_url,goods_purl,goods_img_url,"
								+ "goods_price,goods_name,moq,goodssourcetype,buycount,del,updatetime,goods_pid,car_urlMD5,shop_id,address) "
								+ "values(?,?,?,?,?,?,1,1,?,0,now(),?,?,?,?)";
						stmt = conn.prepareStatement(sqllll);
						stmt.setInt(1, goodsdataid);
						stmt.setString(2, goods_url);
						stmt.setString(3, resource);
						stmt.setString(4, googs_img);
						stmt.setDouble(5, price);
						stmt.setString(6, pname);
						stmt.setInt(7, buycount);
						stmt.setString(8, rs.getString("goods_pid"));
						stmt.setString(9, rs.getString("car_urlMD5"));
						stmt.setString(10, StringUtils.isStrNull(shop_url)?"":shop_url);
						stmt.setString(11, (StringUtils.isStrNull(address)?"":address));
					}
				}
				stmt.executeUpdate();
				sql="update order_product_source set tb_1688_itemid=?,last_tb_1688_itemid=?,goods_p_url=?,last_goods_p_url=? where orderid=? and od_id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, itemid);
				stmt.setString(2, itemid);
				stmt.setString(3, resource);
				stmt.setString(4, resource);
				stmt.setString(5, orderNo);
				stmt.setInt(6, od_id);
				stmt.executeUpdate();
				//将产品表中的店铺ID添加到货源表中
				sqllll="SELECT c.shop_id FROM order_details od INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid WHERE od.orderid='"+orderNo+"' AND od.goodsid='"+goodid+"'";
				stmt = conn.prepareStatement(sqllll);
				rs=stmt.executeQuery();
				if(rs.next()){
					String old_shopid=rs.getString("shop_id");
					sqllll="update order_product_source set old_shopid='"+old_shopid+"' where orderid='"+orderNo+"' AND goodsid='"+goodid+"'";
					stmt = conn.prepareStatement(sqllll);
					stmt.executeUpdate();
				}
				//将店铺ID录入到28库中
				try{
					String shop_urls=shop_url;
					sql="select * from ali_info_data where shop_id='"+shop_urls+"'";
					stmt=conn.prepareStatement(sql);
					rs=stmt.executeQuery();
					if(!rs.next() && !StringUtils.isStrNull(address) && !StringUtils.isStrNull(shop_urls)){
						sql = "insert into ali_info_data (address,shop_id) value(?,?)";
						stmt=conn.prepareStatement(sql);
						stmt.setString(1, address);
						stmt.setString(2, shop_urls);
						stmt.executeUpdate();
					}
				}catch(Exception e){

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBHelper.getInstance().closeConnection(conn);

	}

	public OrderDetailsBean getOrderDetails(int id) {
		OrderDetailsBean o = new OrderDetailsBean();
		String sql = "select * from order_details where id=" + id + "";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				o.setUserid(rs.getInt("userid"));
				o.setGoodsid(rs.getInt("goodsid"));
				o.setGoodsdata_id(rs.getInt("goodsdata_id"));
				o.setGoodsname(rs.getString("goodsname"));
				o.setGoods_url(rs.getString("car_url"));
				o.setGoods_img(rs.getString("car_img"));
				o.setOrderid(rs.getString("orderid"));
				o.setBuycount(rs.getInt("yourorder"));
				o.setYourorder(rs.getInt("yourorder"));
				o.setGoodsprice(rs.getString("goodsprice"));
				o.setId(rs.getInt("id"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBHelper.getInstance().closeConnection(conn);
		return o;
	}

	public int getAdminid(int userid) {
		String sql = "select adminid from admin_r_user where userid=" + userid
				+ "";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int adminid = -1;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				adminid = rs.getInt("adminid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBHelper.getInstance().closeConnection(conn);
		return adminid;
	}

	@Override
	public void autoAddToCart(PreOrderList pol) {
		System.out.println("----------开始查询自动加入购物车的订单商品-----------------");
		// 首先查出当前的所有订单商品信息
		String sql = "SELECT DISTINCT od.orderid AS orderid,od.id AS odid,od.car_type AS sku,od.car_url AS url,od.goodsprice AS saleprice,od.yourorder AS qty FROM orderinfo oi INNER JOIN paymentconfirm p ON oi.order_no=p.orderno  LEFT JOIN order_details od ON oi.order_no=od.orderid LEFT JOIN order_product_source ops ON od.id=ops.`od_id` LEFT JOIN ali1688_preorder_list apl ON od.id=apl.od_id  WHERE  oi.`orderpaytime` > ADDDATE(CURDATE(),'-7')  AND apl.id IS NULL AND od.`state`=0 AND ops.id IS NULL";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				int odid = rs.getInt("odid");// odid
				String AliSku = rs.getString("sku");
				String AliUrl = rs.getString("url");
				String saleprice = rs.getString("saleprice");// 销售价格
				String qty = rs.getString("qty");// 购买数量
				String orderid = rs.getString("orderid");
				String sql1 = "SELECT distinct ops.goods_p_name as goods_p_name,od.car_type AS oldsku,ops.`goodssourcetype` AS 1688sku,ops.goods_p_url as url,ops.goods_p_price as price FROM order_product_source ops INNER JOIN order_details od ON ops.od_id=od.id AND ops.goods_url='"
						+ AliUrl
						+ "' AND LOCATE('1688',ops.goods_p_url) and locate('规格需要备注' ,ops.remark) <= 0 ";
				stmt1 = conn.prepareStatement(sql1.toString());
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					String oldsku = rs1.getString("oldsku");
					String sku = rs1.getString("1688sku");// 1688规格
					String url = rs1.getString("url");// 1688商品链接
					String goodsPName = rs1.getString("goods_p_name");
					String price = rs1.getString("price");// 采购价格
					if (AliSku.equals(oldsku)) {
						// 匹配成功
						String insql = "insert into ali1688_preorder_list (od_id,url,firstOption,secondOption,qty,price,salesprice,goods_p_name,orderid,createtime) values (?,?,?,?,?,?,?,?,?,now())";
						stmt2 = conn.prepareStatement(insql.toString());
						stmt2.setInt(1, odid);// odid
						// stmt2.setInt(2, sourceid);//货源ID
						stmt2.setString(2, url);// 1688商品链接
						if (sku != null && !sku.equals("")
								&& sku.indexOf("|") > -1) {
							String skus[] = sku.split("\\|");
							System.out.println("skus=" + skus[0] + "\t ="
									+ skus[1]);
							if (skus[0].equals(skus[1])) {
								System.out.println("没有颜色只有规格");
								stmt2.setString(3, "");// 图片规格
								stmt2.setString(4, skus[1]);// 规格
							} else {
								stmt2.setString(3, skus[0]);// 图片规格
								stmt2.setString(4, skus[1]);// 规格
							}
						} else {
							stmt2.setString(3, "");// 图片规格
							stmt2.setString(4, "");// 规格
						}
						stmt2.setString(5, qty);// 采购数量
						stmt2.setString(6, price);// 购买价格
						stmt2.setString(7, saleprice);// 销售价格
						stmt2.setString(8, goodsPName);
						stmt2.setString(9, orderid);
						int row = stmt2.executeUpdate();
						if (row > 0) {
							System.out
									.println("新增ali1688_preorder_list成功------------------odid【"
											+ odid + "】");
						} else {
							System.out
									.println("新增ali1688_preorder_list失败------------------odid【"
											+ odid + "】");
						}
						break;
					}
				}
			}
			if (rs != null) {
				rs.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (stmt3 != null) {
				stmt3.close();
			}
			if (conn != null) {
				conn.close();
			}
			DBHelper.getInstance().closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("----------结束查询自动加入购物车的订单商品-----------------");
	}

	@Override
	public int addPreferentialPriceForSku(final String orderid,final int goodsid,
										  final String goods_url,final String goods_p_url,final String begin,final String price,
										  final double goods_p_price) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int row = 0;
		String sql = "";
		String uuid = Md5Util.encoder(goods_url);
		String goods_pid = getItemid(goods_url);
		if (goods_pid == null || "".equals(goods_pid)) {
			goods_pid = "0000";
		}
		String goods_p_itemid = getItemid(goods_p_url);
		if (goods_p_itemid == null || "".equals(goods_p_itemid)) {
			goods_p_itemid = "0000";
		}
		try {
			sql = "select id from preferential_goods where goods_p_itemid=? and goods_pid=?";
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, goods_p_itemid);
			stmt.setString(2, goods_pid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				row = rs.getInt("id");
				sql = "update preferential_goods set price=?,createtime=now() where goods_p_itemid=? and goods_pid=?";
				stmt = conn.prepareStatement(sql.toString());
				stmt.setDouble(1, goods_p_price);
				stmt.setString(2, goods_p_itemid);
				stmt.setString(3, goods_pid);
				row = stmt.executeUpdate();
				this.addPreferentialPrice(row, goods_p_url,
						Integer.valueOf(begin), 0, Double.valueOf(price),
						goods_url);
			} else {
				sql = "INSERT INTO preferential_goods (goods_pid,goods_url,goods_p_url,goods_p_itemid,createtime,price,uuid) VALUES(?,?,?,?,now(),?,?)";
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, goods_pid);
				stmt.setString(2, goods_url);
				stmt.setString(3, goods_p_url);
				stmt.setString(4, goods_p_itemid);
				stmt.setDouble(5, goods_p_price);
				stmt.setString(6, uuid);
				row = stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					row = id;
				}
				this.addPreferentialPrice(row, goods_p_url,
						Integer.valueOf(begin), 0, Double.valueOf(price),
						goods_url);
			}
			new Thread(){
				Connection conn2 = DBHelper.getInstance().getConnection2();
				PreparedStatement stmt = null;
				ResultSet rs = null;
				ResultSet rs1 = null;
				int row = 0;
				String sql = "";
				public void run() {
					try{
						// 更新线上数据库
						String uuid = Md5Util.encoder(goods_url);
						String goods_pid = getItemid(goods_url);
						if (goods_pid == null || "".equals(goods_pid)) {
							goods_pid = "0000";
						}
						String goods_p_itemid = getItemid(goods_p_url);
						if (goods_p_itemid == null || "".equals(goods_p_itemid)) {
							goods_p_itemid = "0000";
						}
						sql = "select id from preferential_goods where goods_p_itemid=? and goods_pid=?";
						stmt = conn2.prepareStatement(sql.toString());
						stmt.setString(1, goods_p_itemid);
						stmt.setString(2, goods_pid);
						rs = stmt.executeQuery();
						if (rs.next()) {
							row = rs.getInt("id");
							sql = "update preferential_goods set price=?,createtime=now() where goods_p_itemid=? and goods_pid=?";
							List<String> lstValues = Lists.newArrayList();
							lstValues.add(String.valueOf(goods_p_price));
							lstValues.add(goods_p_itemid);
							lstValues.add(goods_pid);
							/*stmt = conn2.prepareStatement(sql.toString());
							stmt.setDouble(1, goods_p_price);
							stmt.setString(2, goods_p_itemid);
							stmt.setString(3, goods_pid);
							row = stmt.executeUpdate();*/
							String runSql = DBHelper.covertToSQL(sql, lstValues);
							row = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
							
							new OrderDetailsDao().addPreferentialPrice(row, goods_p_url,
									Integer.valueOf(begin), 0, Double.valueOf(price),
									goods_url);
						} else {
							
							sql = "select id from preferential_goods order id desc limit 1";
							stmt = conn2.prepareStatement(sql.toString());
							rs = stmt.executeQuery();
							int id = 0;
							if (rs.next()) {
								id = rs.getInt("id");
							}
							sql = "INSERT INTO preferential_goods (goods_pid,goods_url,goods_p_url,goods_p_itemid,createtime,price,uuid) VALUES(?,?,?,?,now(),?,?)";
							List<String> lstValues = Lists.newArrayList();
							
							lstValues.add(goods_pid);
							lstValues.add(goods_url);
							lstValues.add(goods_p_url);
							lstValues.add(goods_p_itemid);
							lstValues.add(String.valueOf(goods_p_price));
							lstValues.add(uuid);
							String runSql = DBHelper.covertToSQL(sql, lstValues);
							row = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
							
							/*stmt = conn2.prepareStatement(sql,
									Statement.RETURN_GENERATED_KEYS);
							stmt.setString(1, goods_pid);
							stmt.setString(2, goods_url);
							stmt.setString(3, goods_p_url);
							stmt.setString(4, goods_p_itemid);
							stmt.setDouble(5, goods_p_price);
							stmt.setString(6, uuid);
							row = stmt.executeUpdate();
							rs = stmt.getGeneratedKeys()*/;
							if (row > 0) {
								id = id+1;
								row = id;
							}
							new OrderDetailsDao().addPreferentialPrice(row, goods_p_url,
									Integer.valueOf(begin), 0, Double.valueOf(price),
									goods_url);
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally {
						try {
							if (stmt != null) {
								stmt.close();
							}
							if (rs != null) {
								rs.close();
							}
							if (rs1 != null) {
								rs1.close();
							}
						} catch (SQLException e) {

							e.printStackTrace();
						}
						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	/**
	 * 记录preferential_goods
	 *
	 * @param goods_url
	 *            ali链接
	 * @param goods_p_url
	 *            实际采购链接
	 * @param goods_p_price
	 *            采购价格
	 * @return 主键
	 */
	@Override
	public int addPreferentialGoods(final String goods_url,
									final String goods_p_url, final double goods_p_price) {
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "";
		ResultSet rs = null;
		String uuid = Md5Util.encoder(goods_url);
		String goods_pid = getItemid(goods_url);
		if (goods_pid == null || "".equals(goods_pid)) {
			goods_pid = "0000";
		}
		String goods_p_itemid = getItemid(goods_p_url);
		if (goods_p_itemid == null || "".equals(goods_p_itemid)) {
			goods_p_itemid = "0000";
		}
		try {
			sql = "select id from preferential_goods where goods_pid=? and goods_p_itemid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goods_pid);
			stmt.setString(2, goods_p_itemid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				row = rs.getInt("id");
				sql = "update preferential_goods set price=? where goods_pid=? and goods_p_itemid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setDouble(1, goods_p_price);
				stmt.setString(2, goods_pid);
				stmt.setString(3, goods_p_itemid);
				stmt.executeUpdate();
			} else {
				sql = "insert into preferential_goods (goods_pid,goods_url,goods_p_url,goods_p_itemid,createtime,price,uuid) values(?,?,?,?,now(),?,?)";
				stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, goods_pid);
				stmt.setString(2, goods_url);
				stmt.setString(3, goods_p_url);
				stmt.setString(4, goods_p_itemid);
				stmt.setDouble(5, goods_p_price);
				stmt.setString(6, uuid);
				row = stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					row = id;
				}
			}
			new Thread() {
				Connection conn2 = DBHelper.getInstance().getConnection2();
				PreparedStatement stmt = null;
				String sql = "";
				ResultSet rs = null;
				public void run() {
					try{
						String uuid = Md5Util.encoder(goods_url);
						String goods_pid = getItemid(goods_url);
						if (goods_pid == null || "".equals(goods_pid)) {
							goods_pid = "0000";
						}
						String goods_p_itemid = getItemid(goods_p_url);
						if (goods_p_itemid == null || "".equals(goods_p_itemid)) {
							goods_p_itemid = "0000";
						}
						sql = "select id from preferential_goods where goods_pid=? and goods_p_itemid=?";
						stmt = conn2.prepareStatement(sql);
						stmt.setString(1, goods_pid);
						stmt.setString(2, goods_p_itemid);
						rs = stmt.executeQuery();
						if (rs.next()) {
							sql = "update preferential_goods set price=? where goods_pid=? and goods_p_itemid=?";
							/*stmt = conn2.prepareStatement(sql);
							stmt.setDouble(1, goods_p_price);
							stmt.setString(2, goods_pid);
							stmt.setString(3, goods_p_itemid);
							stmt.executeUpdate();*/
							
							List<String> lstValues = Lists.newArrayList();
							lstValues.add(String.valueOf(goods_p_price));
							lstValues.add(goods_pid);
							lstValues.add(goods_p_itemid);
							String runSql = DBHelper.covertToSQL(sql, lstValues);
							SendMQ.sendMsg(new RunSqlModel(runSql));
						} else {
							sql = "insert into preferential_goods (goods_pid,goods_url,goods_p_url,goods_p_itemid,createtime,price,uuid) values(?,?,?,?,now(),?,?)";
							/*stmt = conn2.prepareStatement(sql);
							stmt.setString(1, goods_pid);
							stmt.setString(2, goods_url);
							stmt.setString(3, goods_p_url);
							stmt.setString(4, goods_p_itemid);
							stmt.setDouble(5, goods_p_price);
							stmt.setString(6, uuid);
							stmt.executeUpdate();*/
							
							List<String> lstValues = Lists.newArrayList();
							lstValues.add(goods_pid);
							lstValues.add(goods_url);
							lstValues.add(goods_p_url);
							lstValues.add(goods_p_itemid);
							lstValues.add(String.valueOf(goods_p_price));
							lstValues.add(uuid);
							String runSql = DBHelper.covertToSQL(sql, lstValues);
							SendMQ.sendMsg(new RunSqlModel(runSql));
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally {
						try {
							if (stmt != null) {
								stmt.close();
							}
							if (rs != null) {
								rs.close();
							}
							if (rs != null) {
								rs.close();
							}
						} catch (SQLException e) {

							e.printStackTrace();
						}
						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn);
			sql = "";
		}
		return row;
	}

	/**
	 * 记录货源批量优惠价格
	 *
	 * @param goods_url
	 *            销售商品ali链接
	 * @param goods_p_url
	 *            采购链接
	 * @param begin
	 *            批量采购起始数量
	 * @param end
	 *            批量采购结束数量
	 * @param price
	 *            批量采购价格
	 * @param price1
	 *            实际采购价格
	 * @return 影响数据库行数
	 * @author 王宏杰 2017-07-18
	 */
	@Override
	public int addPreferentialPrice(int pg_id,final String goods_p_url,final int begin,
									final int end,final double price,final String goods_url) {
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			String goods_p_itemid = getItemid(goods_p_url);
			if (goods_p_itemid == null || "".equals(goods_p_itemid)) {
				goods_p_itemid = "0000";
			}
			String goods_pid = getItemid(goods_url);
			if (goods_pid == null || "".equals(goods_pid)) {
				goods_pid = "0000";
			}
			sql = "select id from preferential_goods where goods_p_itemid=? and goods_pid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goods_p_itemid);
			stmt.setString(2, goods_pid);
			rs = stmt.executeQuery();
			if(rs.next()){
				int pgid=rs.getInt("id");
				sql="select a.id from preferential_goods_price a  where a.goods_p_itemid=? and a.begin=? and a.end=? and a.pgid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, goods_p_itemid);
				stmt.setInt(2, begin);
				stmt.setInt(3, end);
				stmt.setInt(4, pgid);
				rs = stmt.executeQuery();
				if(rs.next()){
					sql = "update preferential_goods_price set price=? where pgid=? and goods_p_itemid=? and begin=? and end=?";
					stmt = conn.prepareStatement(sql);
					stmt.setDouble(1, price);
					stmt.setInt(2, pgid);
					stmt.setString(3, goods_p_itemid);
					stmt.setInt(4, begin);
					stmt.setInt(5, end);
					row = stmt.executeUpdate();
				}else{
					sql = "insert into preferential_goods_price (pgid,goods_p_itemid,begin,end,price) values(?,?,?,?,?)";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(1, pgid);
					stmt.setString(2, goods_p_itemid);
					stmt.setInt(3, begin);
					stmt.setInt(4, end);
					stmt.setDouble(5, price);
					row = stmt.executeUpdate();
				}
			}
			new Thread(){
				Connection conn2 = DBHelper.getInstance().getConnection2();
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String sql = "";
				int pg_id=0;
				public void run() {
					try{
						String goods_p_itemid = getItemid(goods_p_url);
						if (goods_p_itemid == null || "".equals(goods_p_itemid)) {
							goods_p_itemid = "0000";
						}
						String goods_pid = getItemid(goods_url);
						if (goods_pid == null || "".equals(goods_pid)) {
							goods_pid = "0000";
						}
						// 更新线上数据库
						sql = "select a.pgid from preferential_goods_price a inner join preferential_goods b on a.pgid=b.id where a.goods_p_itemid=? and a.begin=? and a.end=?";
						stmt = conn2.prepareStatement(sql);
						stmt.setString(1, goods_p_itemid);
						stmt.setInt(2, begin);
						stmt.setInt(3, end);
						rs = stmt.executeQuery();
						if (rs.next()) {
							pg_id = rs.getInt("pgid");
							sql = "update preferential_goods_price set price=? where pgid=? and goods_p_itemid=? and begin=? and end=?";
							List<String> lstValues = Lists.newArrayList();
							lstValues.add(String.valueOf(price));
							lstValues.add(String.valueOf(pg_id));
							lstValues.add(goods_p_itemid);
							lstValues.add(String.valueOf(begin));
							lstValues.add(String.valueOf(end));
							/*stmt = conn2.prepareStatement(sql);
							stmt.setDouble(1, price);
							stmt.setInt(2, pg_id);
							stmt.setString(3, goods_p_itemid);
							stmt.setInt(4, begin);
							stmt.setInt(5, end);
							stmt.executeUpdate();*/
							String runSql = DBHelper.covertToSQL(sql, lstValues );
							SendMQ.sendMsg(new RunSqlModel(runSql));
							
						} else {
							sql = "select id from preferential_goods where goods_p_itemid=? and goods_pid=?";
							stmt = conn2.prepareStatement(sql);
							stmt.setString(1, goods_p_itemid);
							stmt.setString(2, goods_pid);
							rs = stmt.executeQuery();
							if (rs.next()) {
								pg_id = rs.getInt("id");
								sql = "insert into preferential_goods_price (pgid,goods_p_itemid,begin,end,price) values(?,?,?,?,?)";
								/*stmt = conn2.prepareStatement(sql);
								stmt.setInt(1, pg_id);
								stmt.setString(2, goods_p_itemid);
								stmt.setInt(3, begin);
								stmt.setInt(4, end);
								stmt.setDouble(5, price);
								stmt.executeUpdate();*/
								List<String> lstValues = Lists.newArrayList();
								lstValues.add(String.valueOf(pg_id));
								lstValues.add(goods_p_itemid);
								lstValues.add(String.valueOf(begin));
								lstValues.add(String.valueOf(end));
								lstValues.add(String.valueOf(price));
								String runSql = DBHelper.covertToSQL(sql, lstValues );
								SendMQ.sendMsg(new RunSqlModel(runSql));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally {
						try {
							if (stmt != null) {
								stmt.close();
							}
							if (rs != null) {
								rs.close();
							}
							if (rs != null) {
								rs.close();
							}
						} catch (SQLException e) {

							e.printStackTrace();
						}
						DBHelper.getInstance().closeConnection(conn2);
					}
				};
			}.start();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	// @Override
	// public int addPreferentialPrice(String goods_url,String
	// goods_p_url,double price1,int begin,int end,double price) {
	// Connection conn = DBHelper.getInstance().getConnection();
	// PreparedStatement stmt = null;
	// String uuid=Md5Util.encoder(goods_url);
	// ResultSet rs=null;
	// ResultSet rs1=null;
	// int row=0;
	// String sql="";
	// try{
	// sql="select distinct orderid,goodsid from order_details where car_url=?";
	// stmt = conn.prepareStatement(sql.toString());
	// stmt.setString(1, goods_url);
	// rs1=stmt.executeQuery();
	// while(rs1.next()){
	// orderid=rs1.getString("orderid");
	// goodsid=rs1.getInt("goodsid");
	// sql="select * from preferential_price where orderid=? and goodsid=? and uuid=? and begin=? and goods_p_url=?";
	// stmt = conn.prepareStatement(sql.toString());
	// stmt.setString(1, orderid);
	// stmt.setInt(2, goodsid);
	// stmt.setString(3, uuid);
	// stmt.setInt(4, begin);
	// stmt.setString(5, goods_p_url);
	// rs=stmt.executeQuery();
	// if(rs.next()){
	// sql="update preferential_price set begin=?,end=?,price=?,createtime=now() where orderid=? and goodsid=? and uuid=? and goods_p_url=? and begin=?";
	// stmt = conn.prepareStatement(sql.toString());
	// stmt.setInt(1, begin);
	// stmt.setInt(2, end);
	// stmt.setDouble(3, price);
	// stmt.setString(4, orderid);
	// stmt.setInt(5, goodsid);
	// stmt.setString(6, uuid);
	// stmt.setString(7, goods_p_url);
	// stmt.setInt(8, begin);
	// row=stmt.executeUpdate();
	// }else{
	// sql="INSERT INTO preferential_price (orderid,goodsid,goods_url,goods_p_url,BEGIN,END,price,createtime,uuid) VALUES(?,?,?,?,?,?,?,now(),?)";
	// stmt = conn.prepareStatement(sql.toString());
	// stmt.setString(1, orderid);
	// stmt.setInt(2, goodsid);
	// stmt.setString(3, goods_url);
	// stmt.setString(4, goods_p_url);
	// stmt.setInt(5, begin);
	// stmt.setInt(6, end);
	// stmt.setDouble(7, price);
	// stmt.setString(8, uuid);
	// row=stmt.executeUpdate();
	// }
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// try {
	// if(stmt!=null){
	// stmt.close();
	// }
	// if(rs!=null){
	// rs.close();
	// }
	// if(rs1!=null){
	// rs1.close();
	// }
	// } catch (SQLException e) {
	//
	// e.printStackTrace();
	// }
	// DBHelper.getInstance().closeConnection(conn);
	// }
	// return row;
	// }

	public int addPackgeParameters(String length1, String length2,
								   String length3, String weight, String bagenum, String orderid) {
		DecimalFormat df = new DecimalFormat("#0.###");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int row = 0;
		if (!"".equals(bagenum)) {
			try {
				String sql = "update shipping_package set sweight=?,svolume=?,volumeweight=?,sflag=? where shipmentno=?";
				stmt = conn.prepareStatement(sql.toString());
				stmt.setString(1, weight);// 重量
				stmt.setString(2, length1 + "*" + length2 + "*" + length3);// 体积
				Double volumeweight = Double.valueOf(length1)
						* Double.valueOf(length2) * Double.valueOf(length3)
						/ 5000;// 体积/5000
				System.out.println("volumeweight==" + volumeweight);
				stmt.setString(3, df.format(volumeweight));
				stmt.setString(4, "2");// 已完成测量
				stmt.setString(5, bagenum);
				row = stmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("更新shipping_package失败");
				e.printStackTrace();
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public List<Map<String,Object>> getOrderDetailByUser(int userid) {
		List<Map<String,Object>>  result = new ArrayList<Map<String,Object>>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select  distinct userid,orderid,goods_pid,car_img from  "
				+ "order_details  where state!=2 and userid=? "
				+ " order by id desc";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			Map<String,Object> map = new HashMap<String,Object>();
			while (rs.next()) {
				List<OrderDetailsBean> list = (List<OrderDetailsBean>)map.get(rs.getString("orderid"));
				OrderDetailsBean bean = new OrderDetailsBean();
				bean.setCar_img(rs.getString("car_img"));
				bean.setGoods_pid(rs.getString("goods_pid"));
				if(list == null) {
					list = new ArrayList<OrderDetailsBean>();
				}
				list.add(bean);
				map.put( rs.getString("orderid"), list);
			}
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
				if(org.apache.commons.lang.StringUtils.isBlank(entry.getKey())) {
					continue;
				}
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put( "orderid", entry.getKey());
				map2.put( "orderdetail", entry.getValue());
				result.add(map2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBHelper.getInstance().closeConnection(conn);
		return result;
	}
}
