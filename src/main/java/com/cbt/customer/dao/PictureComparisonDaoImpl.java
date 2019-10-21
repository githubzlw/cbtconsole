package com.cbt.customer.dao;

import ceRong.tools.bean.DorpDwonBean;

import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.TypeUtils;
import com.cbt.util.StrUtils;
import com.cbt.util.Util;
import com.cbt.warehouse.util.StringUtil;

import com.importExpress.utli.RunBatchSqlModel;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class PictureComparisonDaoImpl implements IPictureComparisonDao{
	private String chineseChar = "([\\一-\\龥]+)";
	
//	@Override
//	public List<GoodsFarBean> findByAliPicture(int maxC) {
//		
//		int maxid=0;
//		
//		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
//		String sql2 = "select id, url ,img from goodsdata_far  where id >"+maxC+"  and url like '%www.aliexpress.com%' and img is not null order by id  limit 10";
//		
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		PreparedStatement stmt2 = null;
//		ResultSet rs = null;
//		conn = DBHelper.getInstance().getConnection();
//		try {
//			stmt = conn.prepareStatement(sql2);
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				GoodsFarBean gfb = new GoodsFarBean();
//				gfb.setpId(rs.getInt("id"));
//				gfb.setUrl(rs.getString("url"));
//				gfb.setImg(rs.getString("img").replace("[", "").replace("]", ""));
//				gsfList.add(gfb);
//				maxid = rs.getInt("id");
//			}
//			stmt2 = conn.prepareStatement("update goodsindex set maxCount="+(maxid));
//			stmt2.executeUpdate();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (stmt2 != null) {
//				try {
//					stmt2.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
//		}
//		return gsfList;
//	}
	
	@Override
	public List<GoodsFarBean> findByAliPicture(int maxC) {
		
		int maxid=0;
		
		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
//		String sql2 = "select id,file from imgfile  where id >"+maxC+" order by id  limit 20";
		String sql2 = "select gcid,imgpath from importex.goodsdatacheckcache  where gcid >"+maxC+" and tbflag=1 order by gcid  limit 100";
		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql2);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsFarBean gfb = new GoodsFarBean();
				gfb.setpId(rs.getInt("gcid"));
				gfb.setImg(rs.getString("imgpath"));
				gsfList.add(gfb);
				maxid = rs.getInt("gcid");
			}
			stmt2 = conn.prepareStatement("update goodsindex set maxCount="+(maxid));
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
		return gsfList;
	}
	
	@Override
	public List<GoodsFarBean> getLireSearchCondition() {
		
		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
		String sql = "select a.img,a.url,a.catid,b.catname,a.file from importex.lire_search_condition a left join tbl_index b on a.catid=b.catid ";
		sql = sql +" where a.flag = 1 and a.img is not null and a.url is not null and a.catid is not null and a.file is not null ";
		sql = sql +" and b.catname='clothing' limit 2 ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsFarBean gfb = new GoodsFarBean();
				
				gfb.setImg(rs.getString("img"));
				gfb.setUrl(rs.getString("url"));
				gfb.setCatId(rs.getInt("catid"));
				gfb.setCatName(rs.getString("catname"));
				gfb.setImgPath(rs.getString("file"));
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public List<GoodsFarBean> getImgFile() {
		
		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
		String sql = "select a.imgurl,a.url,a.catid,b.catname,a.file from importex.imgfile a left join tbl_index b on a.catid=b.catid ";
		sql = sql +" where a.upload = 0  ";
//		sql = sql +" where a.upload = 0 and a.imgurl is not null and a.url is not null and a.catid is not null and a.file is not null  ";
		sql = sql +" and b.catname='clothing' limit 10 ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsFarBean gfb = new GoodsFarBean();
				
				gfb.setImg(rs.getString("imgurl"));
				gfb.setUrl(rs.getString("url"));
				gfb.setCatId(rs.getInt("catid"));
				gfb.setCatName(rs.getString("catname"));
				gfb.setImgPath(rs.getString("file"));
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public List<GoodsFarBean> findByTbPicture(int maxC) {
		
		int maxid=0;
		
		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
		//TODO
//		String sql2 = "select id,tbimg,tbimg1,tbimg2,tbimg3 from goodsdatacheck  where id >"+maxC+"  and alcat like '%Jewelry%'  and tburl is not null order by id  limit 100";
		String sql2 = "select gcid,tbimg from importex.goodsdatacheckcache  where gcid >"+maxC+" and tbflag=1 order by gcid  limit 100";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql2);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsFarBean gfb = new GoodsFarBean();
				gfb.setTbPId(rs.getInt("gcid"));
				gfb.setTbimg(rs.getString("tbimg"));
//				gfb.setTbimg1(rs.getString("tbimg1"));
//				gfb.setTbimg2(rs.getString("tbimg2"));
//				gfb.setTbimg3(rs.getString("tbimg3"));
				gsfList.add(gfb);
				maxid = rs.getInt("gcid");
			}
			stmt2 = conn.prepareStatement("update goodsindex set tbMaxCount="+(maxid));
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
		return gsfList;
	}
	
	@Override
	public List<GoodsFarBean> findByTbStyUrl(int maxC) {
		
		int maxid=0;
		
		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
		//TODO
		String sql2 = "select id,tburl,tburl1,tburl2,tburl3 from goodsdatacheck  where id >"+maxC+"  and alcat like '%Jewelry%'  and tburl is not null order by id  limit 100";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql2);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsFarBean gfb = new GoodsFarBean();
				gfb.setTbPId(rs.getInt("id"));
				gfb.setTburl(rs.getString("tburl"));
				gfb.setTburl1(rs.getString("tburl1"));
				gfb.setTburl2(rs.getString("tburl2"));
				gfb.setTburl3(rs.getString("tburl3"));
				gsfList.add(gfb);
				maxid = rs.getInt("id");
			}
			stmt2 = conn.prepareStatement("update goodsindex set tbStyMaxCount="+(maxid));
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
		return gsfList;
	}
	
	
	@Override
	public List<GoodsFarBean> findByCpPicture(int maxC) {
		
		int maxid=0;
		
		List<GoodsFarBean> gsfList = new ArrayList<GoodsFarBean>();
		//TODO
//		String sql2 = "select id,tbimg,tbimg1,tbimg2,tbimg3 from goodsdatacheck  where id >"+maxC+" and alcat like '%Jewelry%'  and tburl is not null order by id  limit 100";
		String sql2 = "select gcid from importex.goodsdatacheckcache  where gcid >"+maxC+" and tbflag=1 order by gcid  limit 100";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql2);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsFarBean gfb = new GoodsFarBean();
				gfb.setTbPId(rs.getInt("gcid"));
//				gfb.setTbimg(rs.getString("tbimg"));
//				gfb.setTbimg1(rs.getString("tbimg1"));
//				gfb.setTbimg2(rs.getString("tbimg2"));
//				gfb.setTbimg3(rs.getString("tbimg3"));
				gsfList.add(gfb);
				maxid = rs.getInt("gcid");
			}
			stmt2 = conn.prepareStatement("update goodsindex set cpMaxCount="+(maxid));
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
		return gsfList;
	}
	
	@Override
	public List<GoodsCheckBean> findOrdersPurchaseInfo(int selled, String orderNo, String categoryId1, int start, int end){
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		String sql ="select distinct(oi.order_no),oi.user_id,u.email,oi.product_cost,oi.currency,oi.create_time as createtime";
		sql = sql +" from orderinfo as oi inner join order_details as od on oi.order_no = od.orderid  ";
		sql = sql +" inner join goodsdatacheck gdc on od.car_url = gdc.url  ";
		sql = sql +" left join user u on oi.user_id = u.id  ";
		sql = sql +" where oi.order_no=? ";
		sql = sql +" order by oi.order_no desc limit ?, ? ";
		String sql1 ="select distinct od.orderid order_no,od.userid,CAST(IF(IFNULL(oi.exchange_rate,0)<=0,6.3,oi.exchange_rate) AS DECIMAL(10,2)) AS exchange_rate," +
				"'USD' currency,od.id as od_id,od.goodsid as goodscarid,gdc.url,gdc.imgpath ,gdc.id as id,gdc.goodsname,gdc.url ";
		sql1 = sql1 +" as url,gdc.imgurl,gdc.imgpath,gdc.tbimg,gdc.tburl,od.goodsprice as price,gdc.tbimg1,gdc.tburl1,gdc.tbimg2,gdc.tburl2,gdc.tbimg3, ";
		sql1 = sql1+" gdc.tburl3,gdc.tbimg4,gdc.tburl4,gdc.tbimg5,gdc.tburl5,gdc.tbprice,gdc.tbprice1,gdc.tbprice2,gdc.tbprice3,gdc.tbprice4,gdc.tbprice5,gdc.tbname,gdc.tbname1,gdc.tbname2,gdc.tbname3,gdc.tbname4,gdc.tbname5, ";
		sql1 = sql1 +" od.yourorder as usecount,ops.purchase_state purchasestate,gdc.id as gooddataid,gdc.goodsnamecn   ";
		sql1 = sql1 +" from order_details od   ";
		sql1 = sql1 +" inner join orderinfo oi on od.orderid = oi.order_no  ";
		//关联采购人员
		if(categoryId1!=null && !"".equals(categoryId1) && !"18".equals(categoryId1) && !"1".equals(categoryId1)){
			sql1 = sql1 +" inner join goods_distribution gd on od.orderid=gd.orderid and od.id=gd.odid   ";
		}
		sql1 = sql1 +"left join goodsdatacheck gdc on od.car_url = gdc.url   ";
		sql1 = sql1 +"left join order_product_source ops on od.id = ops.od_id   ";
		sql1 = sql1 +"where od.orderid= ? and od.state!=2 and gdc.url is not null ";
		if(categoryId1!=null && !"".equals(categoryId1)&& !"18".equals(categoryId1) && !"1".equals(categoryId1)){
			sql1 = sql1+" and gd.admuserid= ? ";
		}
		sql1 = sql1+" order by od.car_url,od.id ";
		
		String sql2= "select distinct goods_url,goods_purl,goods_img_url,order_details.car_img,goods_price,goods_name,goodsid as goodscarid ";
		sql2= sql2+" from order_details,goods_source where goods_url =car_url  and  order_details.orderid= ? and order_details.state<2 ";
		if(selled==2){
			sql2= sql2+" and change_flag=1 ";
		}
		sql2=sql2+" order by updatetime desc";
		String sql3 = "select DISTINCT order_details.car_url,goodsdataid,tbimg,tburl,tbprice,tbname  from order_details ,  goodsdatacheckhelp ";
		sql3=sql3+" where  order_details.goodsdata_id=goodsdatacheckhelp.goodsdataid ";
		sql3=sql3+" and order_details.orderid=?  and order_details.state<2 ";
		String sql4 = "select DISTINCT car_url as url,new_img,new_url,new_price from order_details,lire_search_data  ";
		sql4=sql4+" where source_url= car_url ";
		sql4=sql4+" and order_details.orderid=? and order_details.state<2 ";
		String sql5 = "select DISTINCT od.car_url as url,tu.p_url as new_img,tu.1688_url as new_url,tu.price as new_price,tu.name as new_name from order_details od,tb_1688_url tu,goodsdatacheck gdc ";
		sql5=sql5+" where od.car_url = gdc.url and gdc.tburl= tu.tb_url ";
		sql5=sql5+" and od.orderid=? and od.state<2 ";
		//taobao货源list
		List<GoodsCheckBean> pictureList = new ArrayList<GoodsCheckBean>();
		//1688货源list
		List<GoodsCheckBean> aliSourceList = new ArrayList<GoodsCheckBean>();
		//预览辅图list1
		List<GoodsCheckBean> ylFtList = new ArrayList<GoodsCheckBean>();
		//预览本地lire搜索1
		List<GoodsCheckBean> ylLocList = new ArrayList<GoodsCheckBean>();
		//预览神器搜索
		List<GoodsCheckBean> ylSqList = new ArrayList<GoodsCheckBean>();
		Connection conn = null;
		PreparedStatement stmt = null,stmt1 = null,stmt2 = null,stmt3 = null,stmt4 = null,stmt5 = null;
		ResultSet rs = null,rs1 = null,rs2 = null,rs3 = null,rs4 = null,rs5 = null;
		conn = DBHelper.getInstance().getConnection();
		SimpleDateFormat dfw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println("time start ="+dfw.format(new Date()));// new Date()为获取当前系统时间
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, start);
			stmt.setInt(3, end);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setOrderNo(rs.getString("order_no"));
				gfb.setUserName(rs.getString("user_id"));
				gfb.setEmail(rs.getString("email"));
				gfb.setCreatetime(rs.getString("createtime"));
				gfb.setProductCost(rs.getString("product_cost"));
				gfb.setCurrency(rs.getString("currency"));
				gsfList.add(gfb);
			}
			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1,orderNo);
			if(categoryId1!=null && !"".equals(categoryId1)&& !"18".equals(categoryId1) && !"1".equals(categoryId1)){
				stmt1.setString(2,categoryId1);
			}
			rs1 = stmt1.executeQuery();
			while(rs1.next()){
				GoodsCheckBean gfb1 = new GoodsCheckBean();
				gfb1.setOrderNo(rs1.getString("order_no"));
				gfb1.setpId(rs1.getInt("id"));
				gfb1.setGoodsName(rs1.getString("goodsname"));
				gfb1.setGoodsnamecn(rs1.getString("goodsnamecn"));
				gfb1.setUrl(rs1.getString("url"));
				gfb1.setImgpath(rs1.getString("imgpath"));
				gfb1.setPrice(rs1.getString("price"));
				gfb1.setTbImg(rs1.getString("tbimg"));
				gfb1.setTbImg1(rs1.getString("tbimg1"));
				gfb1.setTbImg2(rs1.getString("tbimg2"));
				gfb1.setTbImg3(rs1.getString("tbimg3"));
				gfb1.setTbImg4(rs1.getString("tbimg4"));
				gfb1.setTbImg5(rs1.getString("tbimg5"));
				gfb1.setTbUrl(rs1.getString("tburl"));
				gfb1.setTbprice(rs1.getString("tbprice"));
				gfb1.setTbUrl1(rs1.getString("tburl1"));
				gfb1.setTbprice1(rs1.getString("tbprice1"));
				gfb1.setTbUrl2(rs1.getString("tburl2"));
				gfb1.setTbprice2(rs1.getString("tbprice2"));
				gfb1.setTbUrl3(rs1.getString("tburl3"));
				gfb1.setTbUrl4(rs1.getString("tburl4"));
				gfb1.setTbUrl5(rs1.getString("tburl5"));
				gfb1.setTbprice3(rs1.getString("tbprice3"));
				gfb1.setTbprice4(rs1.getString("tbprice4"));
				gfb1.setTbprice5(rs1.getString("tbprice5"));
				gfb1.setTbName(rs1.getString("tbname"));
				gfb1.setTbName1(rs1.getString("tbname1"));
				gfb1.setTbName2(rs1.getString("tbname2"));
				gfb1.setTbName3(rs1.getString("tbname3"));
				gfb1.setTbName4(rs1.getString("tbname4"));
				gfb1.setTbName5(rs1.getString("tbname5"));
				gfb1.setUseCount(rs1.getInt("usecount"));
				gfb1.setGoodscarid(rs1.getInt("goodscarid"));
				gfb1.setGoodsDataId(rs1.getInt("gooddataid"));
				gfb1.setOrder_details_id(rs1.getInt("od_id"));
				gfb1.setExchange_rate(rs1.getString("exchange_rate"));
				if(selled==2){
					if(rs1.getInt("purchasestate")==12){
						pictureList.add(gfb1);
					}
				}else{
					pictureList.add(gfb1);
				}
			}
				
			stmt2 = conn.prepareStatement(sql2);
			stmt2.setString(1,orderNo);
			rs2 = stmt2.executeQuery();
			while(rs2.next()){
				GoodsCheckBean gfb2 = new GoodsCheckBean();
				gfb2.setAliSourceOrderNo(orderNo);
				gfb2.setAligSourceUrl(rs2.getString("goods_url"));
				gfb2.setAliSourceUrl(rs2.getString("goods_purl"));
				gfb2.setAliSourceImgUrl(StringUtils.isStrNull(rs2.getString("goods_img_url"))?rs2.getString("car_img"):rs2.getString("goods_img_url"));
				gfb2.setAliSourcePrice(rs2.getString("goods_price"));
				gfb2.setAliSourceName(rs2.getString("goods_name"));
				gfb2.setGoodscarid(rs2.getInt("goodscarid"));
				aliSourceList.add(gfb2);
			}
			stmt3 = conn.prepareStatement(sql3);
			stmt3.setString(1,orderNo);
			rs3 = stmt3.executeQuery();
			while(rs3.next()){
				GoodsCheckBean gfb3 = new GoodsCheckBean();
				gfb3.setGoodsDataId(rs3.getInt("goodsdataid"));
				gfb3.setyLTbimg(rs3.getString("tbimg"));
				gfb3.setyLTburl(rs3.getString("tburl"));
				gfb3.setyLTbprice(rs3.getString("tbprice"));
				ylFtList.add(gfb3);
			}
			stmt4 = conn.prepareStatement(sql4);
			stmt4.setString(1,orderNo);
			rs4 = stmt4.executeQuery();
			while(rs4.next()){
				GoodsCheckBean gfb4 = new GoodsCheckBean();
				gfb4.setUrl(rs4.getString("url"));
				gfb4.setyLLocimg(rs4.getString("new_img"));
				gfb4.setyLLocurl(rs4.getString("new_url"));
				gfb4.setyLLocprice(rs4.getString("new_price"));
				ylLocList.add(gfb4);
			}
			stmt5 = conn.prepareStatement(sql5);
			stmt5.setString(1,orderNo);
			rs5 = stmt5.executeQuery();
			while(rs5.next()){
				GoodsCheckBean gfb5 = new GoodsCheckBean();
				gfb5.setUrl(rs5.getString("url"));
				gfb5.setyLLocimg(rs5.getString("new_img"));
				gfb5.setyLLocurl(rs5.getString("new_url"));
				gfb5.setyLLocprice(rs5.getString("new_price").replace("¥", ""));
				gfb5.setGoodsName(rs5.getString("new_name"));
				ylSqList.add(gfb5);
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
			if (rs3 != null) {
				try {
					rs3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt3 != null) {
				try {
					stmt3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs4 != null) {
				try {
					rs4.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt4 != null) {
				try {
					stmt4.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt5 != null) {
				try {
					stmt5.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		for(int i=0; i<gsfList.size(); i++){
			gsfList.get(i).setPictureList(new ArrayList<GoodsCheckBean>());
			gsfList.get(i).setAliSourceList(new ArrayList<GoodsCheckBean>());
			gsfList.get(i).setYlFtList(new ArrayList<GoodsCheckBean>());
			gsfList.get(i).setYlLocList(new ArrayList<GoodsCheckBean>());
			gsfList.get(i).setSqLocList(new ArrayList<GoodsCheckBean>());
			for(int k=0;k<pictureList.size();k++){
				pictureList.get(k).setYlLocList(new ArrayList<GoodsCheckBean>());
				pictureList.get(k).setSqLocList(new ArrayList<GoodsCheckBean>());
				if(gsfList.get(i).getOrderNo().equals(pictureList.get(k).getOrderNo())){
					for(int b=0;b<aliSourceList.size();b++){
						//1688商品
						if(pictureList.get(k).getUrl()!=null && aliSourceList.get(b).getAligSourceUrl()!=null 
								&& pictureList.get(k).getUrl().equals(aliSourceList.get(b).getAligSourceUrl())
								&& pictureList.get(k).getOrderNo().equals(aliSourceList.get(b).getAliSourceOrderNo())
								&& pictureList.get(k).getGoodscarid()==aliSourceList.get(b).getGoodscarid() ){
							//1688商品
							gsfList.get(i).getAliSourceList().add(aliSourceList.get(b));
						}
					}
					//辅图预览一张
					for(int c=0;c<ylFtList.size();c++){
						if(pictureList.get(k).getGoodsDataId()==ylFtList.get(c).getGoodsDataId()){
							gsfList.get(i).getYlFtList().add(ylFtList.get(c));
						}
					}
					//本地lire搜索图片预览一张
					for(int d=0;d<ylLocList.size();d++){
						if(pictureList.get(k).getUrl().equals(ylLocList.get(d).getUrl())){
//							gsfList.get(i).getYlLocList().add(ylLocList.get(d));
							pictureList.get(k).getYlLocList().add(ylLocList.get(d));
						}
					}
					//神器搜索图片预览一张
					for(int e=0;e<ylSqList.size();e++){
						if(pictureList.get(k).getUrl().equals(ylSqList.get(e).getUrl())){
							pictureList.get(k).getSqLocList().add(ylSqList.get(e));
						}
					}
					//taobao商品
					gsfList.get(i).getPictureList().add(pictureList.get(k));
					//辅图同一商品不同规格去重
					this.distinctByOfferid(gsfList.get(i).getYlFtList());
					//1688同一商品不同规格去重
					this.distinctByUrl(gsfList.get(i).getYlLocList());
				}
			}
		}
		System.out.println("time end ="+dfw.format(new Date()));// new Date()为获取当前系统时间
		return gsfList;
	}
	
	//辅图去掉重复产品数据
	private List<GoodsCheckBean> distinctByOfferid(List<GoodsCheckBean> ylFtList){
		
		for(int i=0; i<ylFtList.size(); i++){
			for(int j=ylFtList.size()-1; j>i; j--){
				if(ylFtList.get(i).getGoodsDataId()==ylFtList.get(j).getGoodsDataId()
						&& ylFtList.get(i).getyLTburl().equals(ylFtList.get(j).getyLTburl())){
					ylFtList.remove(ylFtList.get(j));
				}
			}
		}
		return ylFtList;
	}
	
	//1688货源去掉重复产品数据
	private List<GoodsCheckBean> distinctByUrl(List<GoodsCheckBean> getYlLocList){
		
		for(int i=0; i<getYlLocList.size(); i++){
			for(int j=getYlLocList.size()-1; j>i; j--){
				if(getYlLocList.get(i).getUrl().equals(getYlLocList.get(j).getUrl())
						&& getYlLocList.get(i).getyLLocimg().equals(getYlLocList.get(j).getyLLocimg())){
					getYlLocList.remove(getYlLocList.get(j));
				}
			}
		}
		return getYlLocList;
	}
	
	@Override
	public List<GoodsCheckBean> findTaobaoFtInfo(int goodsDataId, String aliUrl, String orderNo){
		
		List<GoodsCheckBean> tbFtList = new ArrayList<GoodsCheckBean>();
		String sql ="select tbimg,tburl,tbprice,tbname,tbimg1,tburl1,tbprice1,tbname1,tbimg2,tburl2,tbprice2,tbname2,tbimg3,tburl3,tbprice3,tbname3 from goodsdatacheckhelp ";
		sql = sql +"where goodsdataid= "+goodsDataId;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setOrderNo(orderNo);
				gfb.setAligSourceUrl(aliUrl);
				gfb.setGoodsDataId(goodsDataId);
				gfb.setTbImg(rs.getString("tbimg"));
				gfb.setTbUrl(rs.getString("tburl"));
				gfb.setTbprice(rs.getString("tbprice"));
				gfb.setTbName(rs.getString("tbname"));
				gfb.setTbImg1(rs.getString("tbimg1"));
				gfb.setTbUrl1(rs.getString("tburl1"));
				gfb.setTbprice1(rs.getString("tbprice1"));
				gfb.setTbName1(rs.getString("tbname1"));
				gfb.setTbImg2(rs.getString("tbimg2"));
				gfb.setTbUrl2(rs.getString("tburl2"));
				gfb.setTbprice2(rs.getString("tbprice2"));
				gfb.setTbName2(rs.getString("tbname2"));
				gfb.setTbImg3(rs.getString("tbimg3"));
				gfb.setTbUrl3(rs.getString("tburl3"));
				gfb.setTbprice3(rs.getString("tbprice3"));
				gfb.setTbName3(rs.getString("tbname3"));
				
				tbFtList.add(gfb);
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
		
		return tbFtList;
	}
	
	@Override
	public List<GoodsCheckBean> findLireSearchData(String aliUrl, int goodDataId, String orderNo, String flag){
		
		List<GoodsCheckBean> tbFtList = new ArrayList<GoodsCheckBean>();
		String sql ="";
		if("1".equals(flag)){
			sql ="select new_img,new_url,score,new_price,new_name from lire_search_data ";
			sql = sql +" where source_url=? ";
		}else{
//			sql ="select p_url as new_img,1688_url as new_url, 1 as score, price as new_price,name as new_name from tb_1688_url ";
//			sql = sql +" where ali_url=? ";
			sql ="select p_url as new_img,1688_url as new_url, 1 as score, tb_1688_url.price as new_price,name as new_name from tb_1688_url ,goodsdatacheck ";
			sql = sql +" where tb_1688_url.tb_url=goodsdatacheck.tburl and goodsdatacheck.url=? ";
		}

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, aliUrl);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				//新图片路径
				gfb.setNewImg(rs.getString("new_img"));
				//新url
				gfb.setNewUrl(rs.getString("new_url"));
				//相似度
				gfb.setScore(rs.getDouble("score"));
				//新价格
				gfb.setNewPrice(rs.getString("new_price"));
				//新名字
				gfb.setNewName(rs.getString("new_name"));
				gfb.setOrderNo(orderNo);
				gfb.setGoodsDataId(goodDataId);
				gfb.setAligSourceUrl(aliUrl);
				
				tbFtList.add(gfb);
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
		
		return tbFtList;
	}
	
	
	
	@Override
	public List<GoodsCheckBean> findGoodsDataCheck(int selled, String imgCheck, String categoryId1, int start, int end){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		String sql = "select goodsdatacheck.id as id,goodsname,goodsdatacheck.url as url,imgurl,imgpath,tbimg,tburl,price,tbimg1,tburl1,tbimg2,tburl2,tbimg3,tburl3,tbprice,tbprice1,tbprice2,tbprice3,tbname,tbname1,tbname2,tbname3,";
		sql = sql +  "least(imgcheck0,imgcheck1,imgcheck2,imgcheck3) as minImgCheck,least(tbprice,tbprice1,tbprice2,tbprice3) as minPrice,style0,style1,style2,style3,goodsstyle,imgcheck0,imgcheck1,imgcheck2,imgcheck3,goodsstyle, ";

		sql = sql +"substring(price,1, LOCATE('-',price)-1) as priceHead,substring(price,LOCATE('-',price)+1,LENGTH(price)-LOCATE('-',price))  as priceTail from goodsdatacheck  ";
		
		if(!"".equals(categoryId1) && categoryId1 != null){
			sql = sql + " inner join goodsdata_expand_ex as ge on ge.url=goodsdatacheck.url ";
		}
		sql = sql +  "where 1=1 and delflag=1 "; 
		//TODO
		sql = sql +"and alcat like '%Jewelry%' and tburl is not null ";
		
		if(!"".equals(categoryId1) && categoryId1 != null){
			sql = sql + "and ge.catid1 = "+categoryId1+" ";
		}
		if(selled!=0){
			sql =sql +" and selled > "+selled;
		}else{
			sql =sql +" and selled >= "+1;
		}
		//TODO
		sql = sql +" having ( (tbprice/6.2 > price*0.15 and  price not  like '%-%' )  || (  tbprice /6.2> priceHead*0.15 and price  like '%-%' )) ";
		//sql = sql +" having ( (tbprice/6.2 > price*1.2 and  price not  like '%-%' )  || (  tbprice /6.2> priceHead*1.2 and price  like '%-%' )) ";
		//sql = sql +"and ( (least(tbprice,tbprice1,tbprice2) /6.2>price and price not  like '%-%' )  || ( least(tbprice,tbprice1,tbprice2)/6.2 >priceTail and price  like '%-%' )) and minImgCheck<=10 ";
		sql = sql +"and ( (least(tbprice,tbprice1,tbprice2) /6.2<=price and price not  like '%-%' )  || ( least(tbprice,tbprice1,tbprice2)/6.2 <=priceTail and price  like '%-%' )) and minImgCheck<=10 ";
		
		//sql = sql +" having ( (least(tbprice,tbprice1,tbprice2) /6.2>price*1.2 and price not  like '%-%' )  || ( least(tbprice,tbprice1,tbprice2)/6.2 >priceTail*1.2 and price  like '%-%' )) and minImgCheck<=10 ";
		
		if("1".equals(imgCheck)){
			sql = sql +  " having minImgCheck<5 ";
		}else if("2".equals(imgCheck)){
			sql = sql +  " having minImgCheck>5 and minImgCheck<10 ";
		}else if("3".equals(imgCheck)){
			sql = sql +  " having minImgCheck>10 ";
		}
		sql = sql +  " order by id  limit ?, ? ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, end);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setpId(rs.getInt("id"));
				gfb.setGoodsName(rs.getString("goodsname"));
				gfb.setUrl(rs.getString("url"));
				gfb.setImgpath(rs.getString("imgpath"));
				gfb.setPrice(rs.getString("price"));
//				gfb.setImgurl(rs.getString("imgurl").replace("[", "").replace("]", ""));
				gfb.setTbImg(rs.getString("tbimg"));
				gfb.setTbUrl(rs.getString("tburl"));
				gfb.setTbprice(rs.getString("tbprice"));
				gfb.setTbImg1(rs.getString("tbimg1"));
				gfb.setTbUrl1(rs.getString("tburl1"));
				gfb.setTbprice1(rs.getString("tbprice1"));
				gfb.setTbImg2(rs.getString("tbimg2"));
				gfb.setTbUrl2(rs.getString("tburl2"));
				gfb.setTbprice2(rs.getString("tbprice2"));
				gfb.setTbImg3(rs.getString("tbimg3"));
				gfb.setTbUrl3(rs.getString("tburl3"));
				gfb.setTbprice3(rs.getString("tbprice3"));
				gfb.setMinImgCheck(rs.getInt("minImgCheck"));
				gfb.setMinPrice(rs.getString("minPrice"));
				gfb.setTbName(rs.getString("tbname"));
				gfb.setTbName1(rs.getString("tbname1"));
				gfb.setTbName2(rs.getString("tbname2"));
				gfb.setTbName3(rs.getString("tbname3"));
				gfb.setStyle0(rs.getString("style0"));
				gfb.setStyle1(rs.getString("style1"));
				gfb.setStyle2(rs.getString("style2"));
				gfb.setStyle3(rs.getString("style3"));
				gfb.setAlStyle(rs.getString("goodsstyle"));
				gfb.setImgCheck0(rs.getInt("imgcheck0"));
				gfb.setImgCheck1(rs.getInt("imgcheck1"));
				gfb.setImgCheck2(rs.getInt("imgcheck2"));
				gfb.setImgCheck3(rs.getInt("imgcheck3"));
				gsfList.add(gfb);
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

		
		//规格取得
		for(int i=0; i<gsfList.size(); i++){
			//taobao规格取得
			String jsStyle0 = gsfList.get(i).getStyle0(); 
			//ali规格取得
			List<GoodsCheckBean> aliStyleList = this.getAliStyleInfo(gsfList.get(i).getAlStyle());
			if(aliStyleList!=null){
				gsfList.get(i).setAliStyleList(new ArrayList<GoodsCheckBean>());
				for(int j=0; j<aliStyleList.size();j++){
					gsfList.get(i).getAliStyleList().add(aliStyleList.get(j));
				}
			}
			
			if(!"[]".equals(jsStyle0) && jsStyle0 !=null && !"[null]".equals(jsStyle0)){
				String str = "{data:"+jsStyle0+"}";
				List<GoodsCheckBean> styList = this.getStyleInfo(str,0);
				gsfList.get(i).setStyImgList(new ArrayList<GoodsCheckBean>());
				gsfList.get(i).setStyValueList(new ArrayList<GoodsCheckBean>());
				for(int j = 0; j< styList.size(); j++){
					gsfList.get(i).getStyImgList().add(styList.get(j));
					gsfList.get(i).getStyValueList().add(styList.get(j));
				}
			}
			String jsStyle1 = gsfList.get(i).getStyle1(); 
			if(!"[]".equals(jsStyle1) && jsStyle1 !=null && !"[null]".equals(jsStyle1)){
				String str = "{data:"+jsStyle1+"}";
				List<GoodsCheckBean> styList = this.getStyleInfo(str,1);
				gsfList.get(i).setStyImgList1(new ArrayList<GoodsCheckBean>());
				gsfList.get(i).setStyValueList1(new ArrayList<GoodsCheckBean>());
				for(int j = 0; j< styList.size(); j++){
					gsfList.get(i).getStyImgList1().add(styList.get(j));
					gsfList.get(i).getStyValueList1().add(styList.get(j));
				}
			}
			String jsStyle2 = gsfList.get(i).getStyle2(); 
			if(!"[]".equals(jsStyle2) && jsStyle2 !=null && !"[null]".equals(jsStyle2)){
				String str = "{data:"+jsStyle2+"}";
				List<GoodsCheckBean> styList = this.getStyleInfo(str,2);
				gsfList.get(i).setStyImgList2(new ArrayList<GoodsCheckBean>());
				gsfList.get(i).setStyValueList2(new ArrayList<GoodsCheckBean>());
				for(int j = 0; j< styList.size(); j++){
					gsfList.get(i).getStyImgList2().add(styList.get(j));
					gsfList.get(i).getStyValueList2().add(styList.get(j));
				}
			}
			String jsStyle3 = gsfList.get(i).getStyle3(); 
			if(!"[]".equals(jsStyle3)  && jsStyle3 !=null && !"[null]".equals(jsStyle3)){
				String str = "{data:"+jsStyle3+"}";
				List<GoodsCheckBean> styList = this.getStyleInfo(str,3);
				gsfList.get(i).setStyImgList3(new ArrayList<GoodsCheckBean>());
				gsfList.get(i).setStyValueList3(new ArrayList<GoodsCheckBean>());
				for(int j = 0; j< styList.size(); j++){
					gsfList.get(i).getStyImgList3().add(styList.get(j));
					gsfList.get(i).getStyValueList3().add(styList.get(j));
				}
			}
		}
		return gsfList;
	}
	
	@Override
	public List<GoodsCheckBean> findLireData(int selled, String imgCheck, String categoryId1, int start, int end){
		
//		String sql = "select distinct(b.url),b.file,b.url,b.imgurl,a.goods_price  ";
//		sql = sql +" ,tbimg,tburl,tbname,tbprice,tbimg1,tburl1,tbname1,tbprice1,tbimg2,tburl2,tbname2,tbprice2,tbimg3,tburl3,tbname3,tbprice3 ";
//		sql = sql + " from  importex.ali_search_cache_datas a inner join importex.imgfile b on a.goods_url = b.url and b.upload=1 ";
//		sql = sql + " inner join importex.goodsdatacheckcache gdc on a.goods_url=gdc.url ";
//		sql = sql + " limit ?, ? ";
		String sql = "select distinct(a.goods_url),gdc.id,a.goods_url as url,a.goods_img as imgurl,a.goods_price   ";
		sql = sql +" ,tbimg,tburl,tbname,tbprice,tbimg1,tburl1,tbname1,tbprice1,tbimg2,tburl2,tbname2,tbprice2,tbimg3,tburl3,tbname3,tbprice3 ";
		sql = sql + " ,gdc.imgcheck0,gdc.score,gdc.tbpath,gdc.score1,gdc.tbpath1 from  importex.ali_search_cache_datas a  ";
		sql = sql + " inner join importex.goodsdatacheckcache gdc on a.goods_url=gdc.url and gdc.delflag=0 and gdc.tbflag=1  ";
		sql = sql +" where gdc.imgcheck0>6 and gdc.fcthflag=0 and gdc.ceddflag=0 ";
		sql = sql +" and gdc.tbpath is not null	";
		sql = sql +" order by gdc.gcid ";
		sql = sql + " limit ?, ? ";
		
		
		String sql1 = "select source_url,new_img,new_url,new_price,score from importex.lire_search_cache_data   ";
		sql1 = sql1 +"where source_url =? ";
		sql1 = sql1 + " order by score asc limit 4 ";
		 
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null,rs1 = null;
		PreparedStatement stmt = null,stmt1 = null;
		List<GoodsCheckBean> alList = new ArrayList<GoodsCheckBean>();
		List<GoodsCheckBean> chaList = new ArrayList<GoodsCheckBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, end);
			rs = stmt.executeQuery();
			while (rs.next()) {
				
				GoodsCheckBean pcb = new GoodsCheckBean();
//				pcb.setAliSourceImgUrl(rs.getString("file"));
				pcb.setAliSourceImgUrl(rs.getString("imgurl"));
				pcb.setAligSourceUrl(rs.getString("url"));
				pcb.setAliSourcePrice(rs.getString("goods_price"));
				
				pcb.setTbImg(rs.getString("tbimg"));
				pcb.setTbUrl(rs.getString("tburl"));
				pcb.setTbName(rs.getString("tbname"));
				pcb.setTbprice(rs.getString("tbprice"));
				pcb.setTbImg1(rs.getString("tbimg1"));
				pcb.setTbUrl1(rs.getString("tburl1"));
				pcb.setTbName1(rs.getString("tbname1"));
				pcb.setTbprice1(rs.getString("tbprice1"));
				pcb.setTbImg2(rs.getString("tbimg2"));
				pcb.setTbUrl2(rs.getString("tburl2"));
				pcb.setTbName2(rs.getString("tbname2"));
				pcb.setTbprice2(rs.getString("tbprice2"));
				pcb.setTbImg3(rs.getString("tbimg3"));
				pcb.setTbUrl3(rs.getString("tburl3"));
				pcb.setTbName3(rs.getString("tbname3"));
				pcb.setTbprice3(rs.getString("tbprice3"));
				pcb.setImgCheck0(rs.getInt("imgcheck0"));
				pcb.setScore(rs.getDouble("score"));
				pcb.setTbPath(rs.getString("tbpath"));
				pcb.setScore1(rs.getDouble("score1"));
				pcb.setTbPath1(rs.getString("tbpath1"));
				
				
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("url"));
				rs1 = stmt1.executeQuery();
				while(rs1.next()){
					GoodsCheckBean pcb1 = new GoodsCheckBean();
					
					pcb1.setAligSourceUrl(rs1.getString("source_url"));
					pcb1.setImgurl(rs1.getString("new_img"));
					pcb1.setUrl(rs1.getString("new_url"));
					pcb1.setPrice(rs1.getString("new_price"));
					pcb1.setdScore(rs1.getDouble("score"));
					
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
			alList.get(i).setPictureList(new ArrayList<GoodsCheckBean>());
			for(int j=0;j<chaList.size();j++){
				if(alList.get(i).getAligSourceUrl().equals(chaList.get(j).getAligSourceUrl())){
					alList.get(i).getPictureList().add(chaList.get(j));
				}
			}
		}
		
		return alList;
	}
	
	
	
	public List<GoodsCheckBean> getAliStyleInfo(String aliStyle){
		String types = aliStyle;
		List<GoodsCheckBean> aliStyList = new ArrayList<GoodsCheckBean>();
		if(types!=null&&!types.isEmpty()){
			types = types.replace("[", "").replace("]", "").trim();
//			ArrayList<TypeBean> typeList = new ArrayList<TypeBean>();
			String[] types_s = types.split(",\\s+");
			GoodsCheckBean bean = null;
			String[] tems = null;
			String tem = null;
			for(int i=0;i<types_s.length;i++){
				if(!types_s[i].isEmpty()){
					bean = new GoodsCheckBean();
					String[] type = types_s[i].split("\\+#\\s+");
					for(int j=0;j<type.length;j++){
						if(Pattern.compile("(id=)").matcher(type[j]).find()){
							tems = type[j].split("id=");
							tem = tems.length>1?tems[1]:"";
							//bean.setId(tem);
							tem = null;
							tems = null;
						}else if(Pattern.compile("(type=)").matcher(type[j]).find()){
							tems = type[j].split("type=");
							tem = tems.length>1?tems[1]:"";
							bean.setAliStyType(tem);
							tems = null;
							tem = null;
						}else if(Pattern.compile("(value=)").matcher(type[j]).find()){
							tems = type[j].split("value=");
							tem = tems.length>1?tems[1]:"";
							bean.setAliStyValue(tem);
							tem = null;
							tems = null;
						}else if(Pattern.compile("(img=)").matcher(type[j]).find()){
							tems = type[j].split("img=");
							tem = tems.length>1?tems[1]:"";
							bean.setAliStyImg(tem);
							tem = null;
							tems = null;
						}
					}
					aliStyList.add(bean);
				}
			}
		}
		return aliStyList;
	}
	
	public List<GoodsCheckBean> getStyleInfo(String jsStyle, int flag){
		
		List<GoodsCheckBean> styList = new ArrayList<GoodsCheckBean>();
        try {
        	
	        JSONObject json= new JSONObject(jsStyle);  
	        JSONArray jsonArray=json.getJSONArray("data"); 
	        
        	for(int i=0;i<jsonArray.length();i++){  
	        	GoodsCheckBean gfb = new GoodsCheckBean();
	            JSONObject jObject=(JSONObject) jsonArray.get(i);
	            	  if(flag == 0){
	  	            	gfb.setStyImg((String) jObject.get("img"));
	  		            gfb.setStyType((String) jObject.get("type"));
	  		            gfb.setStyValue((String) jObject.get("value"));
	  	            }else if(flag == 1){
	  	            	gfb.setStyImg1((String) jObject.get("img"));
	  	            	gfb.setStyType1((String) jObject.get("type"));
	  		            gfb.setStyValue1((String) jObject.get("value"));
	  	            }else if(flag == 2){
	  	            	gfb.setStyImg2((String) jObject.get("img"));
	  	            	gfb.setStyType2((String) jObject.get("type"));
	  		            gfb.setStyValue2((String) jObject.get("value"));
	  	            }else if(flag == 3){
	  	            	gfb.setStyImg3((String) jObject.get("img"));
	  	            	gfb.setStyType3((String) jObject.get("type"));
	  		            gfb.setStyValue3((String) jObject.get("value"));
	  	            }
	  	            styList.add(gfb);
	          
	        }
        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return styList;
	}
	
	@Override
	public List<GoodsCheckBean> findGoodsDataCheckCount(int selled, String imgCheck, String categoryId1){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		String sql = "select goodsdatacheck.id as id,goodsname,goodsdatacheck.url as url,imgurl,imgpath,tbimg,tburl,price,tbimg1,tburl1,tbimg2,tburl2,tbimg3,tburl3,tbprice,tbprice1,tbprice2,tbprice3,tbname,tbname1,tbname2,tbname3,";
		sql = sql +  "least(imgcheck0,imgcheck1,imgcheck2,imgcheck3) as minImgCheck,least(tbprice,tbprice1,tbprice2,tbprice3) as minPrice,style0,style1,style2,style3,goodsstyle,imgcheck0,imgcheck1,imgcheck2,imgcheck3, ";

		sql = sql +"substring(price,1, LOCATE('-',price)-1) as priceHead,substring(price,LOCATE('-',price)+1,LENGTH(price)-LOCATE('-',price))  as priceTail from goodsdatacheck  ";
		
		if(!"".equals(categoryId1) && categoryId1 != null){
			sql = sql + " inner join goodsdata_expand_ex as ge on ge.url=goodsdatacheck.url ";
		}
		sql = sql +  "where 1=1 "; 
		//TODO
		sql = sql +"and alcat like '%Jewelry%' and tburl is not null ";
		
		if(!"".equals(categoryId1) && categoryId1 != null){
			sql = sql + "and ge.catid1 = "+categoryId1+" ";
		}
		if(selled!=0){
			sql =sql +" and selled > "+selled;
		}else{
			sql =sql +" and selled > "+1;
		}
		
		sql = sql +" having ( (tbprice/6.2 > price*0.15 and  price not  like '%-%' )  || (  tbprice /6.2> priceHead*0.15 and price  like '%-%' )) ";
		sql = sql +"and ( (least(tbprice,tbprice1,tbprice2) /6.2<price*0.8 and price not  like '%-%' )  || ( least(tbprice,tbprice1,tbprice2)/6.2 <priceTail*0.8 and price  like '%-%' )) and minImgCheck <=10 ";
		
		if("1".equals(imgCheck)){
			sql = sql +  " having minImgCheck<5 ";
		}else if("2".equals(imgCheck)){
			sql = sql +  " having minImgCheck>5 and minImgCheck<10 ";
		}else if("3".equals(imgCheck)){
			sql = sql +  " having minImgCheck>10 ";
		}
		sql = sql +  " order by id";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setpId(rs.getInt("id"));
				gfb.setGoodsName(rs.getString("goodsname"));
				gfb.setUrl(rs.getString("url"));
				gfb.setImgpath(rs.getString("imgpath"));
				gfb.setPrice(rs.getString("price"));
//				gfb.setImgurl(rs.getString("imgurl").replace("[", "").replace("]", ""));
				gfb.setTbImg(rs.getString("tbimg"));
				gfb.setTbUrl(rs.getString("tburl"));
				gfb.setTbprice(rs.getString("tbprice"));
				gfb.setTbImg1(rs.getString("tbimg1"));
				gfb.setTbUrl1(rs.getString("tburl1"));
				gfb.setTbprice1(rs.getString("tbprice1"));
				gfb.setTbImg2(rs.getString("tbimg2"));
				gfb.setTbUrl2(rs.getString("tburl2"));
				gfb.setTbprice2(rs.getString("tbprice2"));
				gfb.setTbImg3(rs.getString("tbimg3"));
				gfb.setTbUrl3(rs.getString("tburl3"));
				gfb.setTbprice3(rs.getString("tbprice3"));
				gfb.setMinImgCheck(rs.getInt("minImgCheck"));
				gfb.setMinPrice(rs.getString("minPrice"));
				gfb.setTbName(rs.getString("tbname"));
				gfb.setTbName1(rs.getString("tbname1"));
				gfb.setTbName2(rs.getString("tbname2"));
				gfb.setTbName3(rs.getString("tbname3"));
				gfb.setStyle0(rs.getString("style0"));
				gfb.setStyle1(rs.getString("style1"));
				gfb.setStyle2(rs.getString("style2"));
				gfb.setStyle3(rs.getString("style3"));
				gfb.setAlStyle(rs.getString("goodsstyle"));
				gfb.setImgCheck0(rs.getInt("imgcheck0"));
				gfb.setImgCheck1(rs.getInt("imgcheck1"));
				gfb.setImgCheck2(rs.getInt("imgcheck2"));
				gfb.setImgCheck3(rs.getInt("imgcheck3"));
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public int findCount(int selled,String imgCheck,String categoryId1){
		
		String sql = "select count(*) as num  from importex.goodsdatacheckcache ";
		sql = sql +  " where delflag=0 and  tbflag=1 and imgcheck0>6 and fcthflag=0 and ceddflag=0 and tbpath is not null ";
		int num=0;

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				num=rs.getInt("num");
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
		return num;
	}
	
	@Override
	public int findResoultCount(){
		
//		String sql = "select count(*) as num  from importex.goodsdatacheckcache gdc where gdc.delflag=1 and gdc.imgcheck0>=6 and gdc.imgcheck0<7 and gdc.fcthflag=1  and ceddflag=1 ";
		String sql = "select count(*) as num  from importex.goodsdatacheckcache gdc where gdc.delflag=1 ";
		int num=0;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				num=rs.getInt("num");
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
		return num;
	}
	
	
//	@Override
//	public List<GoodsCheckBean> findTbGoodsDataCheck(int selled,String imgCheck,String sourceTbl,int start,int end){
//		
//		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
//		
////		String sql = "select goods_pid,imgurl,tburl,tburl1,tburl2,tburl3,tburl4,tburl5,";
////		sql= sql+"tbimg,tbimg1,tbimg2,tbimg3,tbimg4,tbimg5, ";
////		sql= sql+"shop_id,shop_id1,shop_id2,shop_id3,shop_id4,shop_id5 ";
////		if("".equals(imgCheck) || null!=imgCheck){
////			sql= sql+"from goodsdatacheckhot01 where goods_pid =? ";
////		}else{
////			sql= sql+"from goodsdatacheckhot01 where (result!=1 and result1!=1 and result2!=1 and result3!=1 and result4!=1 and result5!=1) and delflag=1 and ali_flag=0 ";
////			sql= sql+"limit ?, ? ";
////		}
//		
//		String sql = "select a.goods_pid,a.imgurl,a.tburl,a.tburl1,a.tburl2,a.tburl3,a.tburl4,a.tburl5,a.tburl6,a.tburl7,a.tburl8,a.tburl9,a.tburl10,a.tburl11,";
//		sql= sql+"a.tbimg,a.tbimg1,a.tbimg2,a.tbimg3,a.tbimg4,a.tbimg5,a.tbimg6,a.tbimg7,a.tbimg8,a.tbimg9,a.tbimg10,a.tbimg11, ";
//		sql= sql+"a.shop_id,a.shop_id1,a.shop_id2,a.shop_id3,a.shop_id4,a.shop_id5, a.shop_id6,a.shop_id7,a.shop_id8,a.shop_id9,a.shop_id10,a.shop_id11, ";
//		sql= sql+"a.moq_price,a.moq_price1,a.moq_price2,a.moq_price3,a.moq_price4,a.moq_price5, a.moq_price6,a.moq_price7,a.moq_price8,a.moq_price9,a.moq_price10,a.moq_price11, ";
//		sql= sql+"a.categoryid,a.categoryid1,a.categoryid2,a.categoryid3,a.categoryid4,a.categoryid5,a.categoryid6,a.categoryid7,a.categoryid8,a.categoryid9,a.categoryid10,a.categoryid11, ";
//		sql= sql+"a.imgcheck0,a.imgcheck1,a.imgcheck2,a.imgcheck3,a.imgcheck4,a.imgcheck5,a.imgcheck6,a.imgcheck7,a.imgcheck8,a.imgcheck9,a.imgcheck10,a.imgcheck11 ";
////		sql= sql+"from core_cache_data a,ali_info_data b ";
//		sql= sql+"from "+sourceTbl+" a,ali_info_data b ";
//		sql= sql+"where a.goods_pid=b.goods_pid and a.imgflag=b.img_flag ";
//		
//		if("".equals(imgCheck) || null!=imgCheck){
//			sql= sql+"and b.goods_pid =? ";
//		}else{
//			sql= sql+"and b.mark_flag in(2,3) and b.flag=3 and b.handle_flag=0 ";
//			sql= sql+"limit ?, ? ";
//		}
//		
//
//		
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		conn = DBHelper.getInstance().getConnection();
//		try {
//			stmt = conn.prepareStatement(sql);
//			if("".equals(imgCheck) || null!=imgCheck){
//				stmt.setString(1, imgCheck);
//			}else{
//				stmt.setInt(1, start);
//				stmt.setInt(2, end);
//			}
//			
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				GoodsCheckBean gfb = new GoodsCheckBean();
//				gfb.setGoodsPid(rs.getString("goods_pid"));
//				gfb.setImgpath(rs.getString("imgurl"));
//				gfb.setTbImg(rs.getString("tbimg"));
//				gfb.setTbImg1(rs.getString("tbimg1"));
//				gfb.setTbImg2(rs.getString("tbimg2"));
//				gfb.setTbImg3(rs.getString("tbimg3"));
//				gfb.setTbImg4(rs.getString("tbimg4"));
//				gfb.setTbImg5(rs.getString("tbimg5"));
//				gfb.setTbImg6(rs.getString("tbimg6"));
//				gfb.setTbImg7(rs.getString("tbimg7"));
//				gfb.setTbImg8(rs.getString("tbimg8"));
//				gfb.setTbImg9(rs.getString("tbimg9"));
//				gfb.setTbImg10(rs.getString("tbimg10"));
//				gfb.setTbImg11(rs.getString("tbimg11"));
//				gfb.setTbUrl(rs.getString("tburl"));
//				gfb.setTbUrl1(rs.getString("tburl1"));
//				gfb.setTbUrl2(rs.getString("tburl2"));
//				gfb.setTbUrl3(rs.getString("tburl3"));
//				gfb.setTbUrl4(rs.getString("tburl4"));
//				gfb.setTbUrl5(rs.getString("tburl5"));
//				gfb.setTbUrl6(rs.getString("tburl6"));
//				gfb.setTbUrl7(rs.getString("tburl7"));
//				gfb.setTbUrl8(rs.getString("tburl8"));
//				gfb.setTbUrl9(rs.getString("tburl9"));
//				gfb.setTbUrl10(rs.getString("tburl10"));
//				gfb.setTbUrl11(rs.getString("tburl11"));
//				gfb.setShopId(rs.getString("shop_id"));
//				gfb.setShopId1(rs.getString("shop_id1"));
//				gfb.setShopId2(rs.getString("shop_id2"));
//				gfb.setShopId3(rs.getString("shop_id3"));
//				gfb.setShopId4(rs.getString("shop_id4"));
//				gfb.setShopId5(rs.getString("shop_id5"));
//				gfb.setShopId6(rs.getString("shop_id6"));
//				gfb.setShopId7(rs.getString("shop_id7"));
//				gfb.setShopId8(rs.getString("shop_id8"));
//				gfb.setShopId9(rs.getString("shop_id9"));
//				gfb.setShopId10(rs.getString("shop_id10"));
//				gfb.setShopId11(rs.getString("shop_id11"));
//				gfb.setMoqPrice(rs.getString("moq_price"));
//				gfb.setMoqPrice1(rs.getString("moq_price1"));
//				gfb.setMoqPrice2(rs.getString("moq_price2"));
//				gfb.setMoqPrice3(rs.getString("moq_price3"));
//				gfb.setMoqPrice4(rs.getString("moq_price4"));
//				gfb.setMoqPrice5(rs.getString("moq_price5"));
//				gfb.setMoqPrice6(rs.getString("moq_price6"));
//				gfb.setMoqPrice7(rs.getString("moq_price7"));
//				gfb.setMoqPrice8(rs.getString("moq_price8"));
//				gfb.setMoqPrice9(rs.getString("moq_price9"));
//				gfb.setMoqPrice10(rs.getString("moq_price10"));
//				gfb.setMoqPrice11(rs.getString("moq_price11"));
//				gfb.setCategoryid(rs.getString("categoryid"));
//				gfb.setCategoryid1(rs.getString("categoryid1"));
//				gfb.setCategoryid2(rs.getString("categoryid2"));
//				gfb.setCategoryid3(rs.getString("categoryid3"));
//				gfb.setCategoryid4(rs.getString("categoryid4"));
//				gfb.setCategoryid5(rs.getString("categoryid5"));
//				gfb.setCategoryid6(rs.getString("categoryid6"));
//				gfb.setCategoryid7(rs.getString("categoryid7"));
//				gfb.setCategoryid8(rs.getString("categoryid8"));
//				gfb.setCategoryid9(rs.getString("categoryid9"));
//				gfb.setCategoryid10(rs.getString("categoryid10"));
//				gfb.setCategoryid11(rs.getString("categoryid11"));
//				gfb.setImgCheck(rs.getInt("imgcheck0"));
//				gfb.setImgCheck1(rs.getInt("imgcheck1"));
//				gfb.setImgCheck2(rs.getInt("imgcheck2"));
//				gfb.setImgCheck3(rs.getInt("imgcheck3"));
//				gfb.setImgCheck4(rs.getInt("imgcheck4"));
//				gfb.setImgCheck5(rs.getInt("imgcheck5"));
//				gfb.setImgCheck6(rs.getInt("imgcheck6"));
//				gfb.setImgCheck7(rs.getInt("imgcheck7"));
//				gfb.setImgCheck8(rs.getInt("imgcheck8"));
//				gfb.setImgCheck9(rs.getInt("imgcheck9"));
//				gfb.setImgCheck10(rs.getInt("imgcheck10"));
//				gfb.setImgCheck11(rs.getInt("imgcheck11"));
//				
//				gsfList.add(gfb);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
//		}
//		return gsfList;
//	}
	
	@Override
	public List<GoodsCheckBean> findTbGoodsDataCheck(int selled, String imgCheck, String sourceTbl, int start, int end){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		
		String sql= "select CONCAT(b.remotpath,b.custom_main_image) img ,b.pid from new_core_goods_ready a,custom_benchmark_ready_newest b where a.pid=b.pid and b.valid=1 order by b.id limit ?, ? ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection8();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, end);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setGoodsPid(rs.getString("pid"));
				gfb.setImgpath(rs.getString("img"));
				
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	
	
	@Override
	public List<GoodsCheckBean> findYLGoodsDataCheck(int selled, String noBenchFlag, String sourceTbl, int start, int end){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		
		
//		String sql = "select a.goods_pid,a.imgurl,a.tburl,a.tburl1,a.tburl2,a.tburl3,a.tburl4,a.tburl5,a.tburl6,a.tburl7,a.tburl8,a.tburl9,a.tburl10,a.tburl11,";
//		sql= sql+"a.tbimg,a.tbimg1,a.tbimg2,a.tbimg3,a.tbimg4,a.tbimg5,a.tbimg6,a.tbimg7,a.tbimg8,a.tbimg9,a.tbimg10,a.tbimg11, ";
//		sql= sql+"a.shop_id,a.shop_id1,a.shop_id2,a.shop_id3,a.shop_id4,a.shop_id5, a.shop_id6,a.shop_id7,a.shop_id8,a.shop_id9,a.shop_id10,a.shop_id11, ";
//		sql= sql+"a.moq_price,a.moq_price1,a.moq_price2,a.moq_price3,a.moq_price4,a.moq_price5, a.moq_price6,a.moq_price7,a.moq_price8,a.moq_price9,a.moq_price10,a.moq_price11, ";
//		sql= sql+"a.categoryid,a.categoryid1,a.categoryid2,a.categoryid3,a.categoryid4,a.categoryid5,a.categoryid6,a.categoryid7,a.categoryid8,a.categoryid9,a.categoryid10,a.categoryid11, ";
//		sql= sql+"a.goods_moq,a.goods_moq1,a.goods_moq2,a.goods_moq3,a.goods_moq4,a.goods_moq5,a.goods_moq6,a.goods_moq7,a.goods_moq8,a.goods_moq9,a.goods_moq10,a.goods_moq11, ";
//		sql= sql+" b.price_min as ali_price,a.priority_flag,a.source_pro_flag,b.goods_sellunits,c.types,c.sku,b.goods_weight ";
//		sql= sql+"from alicachedatanew1_1 a inner join ali_goods_source_new_hx b on a.goods_pid=b.goods_pid ";
//		sql= sql+"inner join ali_goods_source_new_ex_hx c on a.goods_pid=c.goods_pid where a.tbflag=1 and a.user_name='"+noBenchFlag+"'  ";
		

		String sql = "";
		//amazon
		if(selled==1){
			sql = "select '' as catid1,  a.goods_pid,a.imgurl,a.tburl,a.tburl1,a.tburl2,a.tburl3,a.tburl4,a.tburl5,a.tburl6,a.tburl7,a.tburl8,a.tburl9,a.tburl10,a.tburl11,";
			sql= sql+"a.tbimg,a.tbimg1,a.tbimg2,a.tbimg3,a.tbimg4,a.tbimg5,a.tbimg6,a.tbimg7,a.tbimg8,a.tbimg9,a.tbimg10,a.tbimg11, ";
			sql= sql+"a.shop_id,a.shop_id1,a.shop_id2,a.shop_id3,a.shop_id4,a.shop_id5, a.shop_id6,a.shop_id7,a.shop_id8,a.shop_id9,a.shop_id10,a.shop_id11, ";
			sql= sql+"a.moq_price,a.moq_price1,a.moq_price2,a.moq_price3,a.moq_price4,a.moq_price5, a.moq_price6,a.moq_price7,a.moq_price8,a.moq_price9,a.moq_price10,a.moq_price11, ";
			sql= sql+"a.categoryid,a.categoryid1,a.categoryid2,a.categoryid3,a.categoryid4,a.categoryid5,a.categoryid6,a.categoryid7,a.categoryid8,a.categoryid9,a.categoryid10,a.categoryid11, ";
			sql= sql+"a.goods_moq,a.goods_moq1,a.goods_moq2,a.goods_moq3,a.goods_moq4,a.goods_moq5,a.goods_moq6,a.goods_moq7,a.goods_moq8,a.goods_moq9,a.goods_moq10,a.goods_moq11, ";
			sql= sql+"a.goods_price as ali_price,a.priority_flag,a.source_pro_flag,'' as goods_sellunits,'' as goods_weight, ";
			sql= sql+"a.goods_name0,a.goods_name1,a.goods_name2,a.goods_name3,a.goods_name4,a.goods_name5,a.goods_name6,a.goods_name7,a.goods_name8,a.goods_name9,a.goods_name10,a.goods_name11, ";
			sql= sql+"a.min_moq0,a.min_moq1,a.min_moq2,a.min_moq3,a.min_moq4,a.min_moq5,a.min_moq6,a.min_moq7,a.min_moq8,a.min_moq9,a.min_moq10,a.min_moq11,a.url ";
			sql= sql+"from alicachedatanew1_1 a  ";
			sql= sql+" where a.tbflag=1  ";
			
			if(!"".equals(sourceTbl) && null!=sourceTbl){
				sql= sql +" and a.goods_pid=? ";
			}else{
				sql= sql+" and a.user_name='"+noBenchFlag+"'  and a.shelf_flag=2";
				sql= sql +" and a.flag=0  ";//
			}
			sql= sql +" limit ?, ?";
			//电子产品
		}else if(selled==4){
			sql = "select '' as catid1, a.goods_pid,a.imgurl,a.tburl,a.tburl1,a.tburl2,a.tburl3,a.tburl4,a.tburl5,a.tburl6,a.tburl7,a.tburl8,a.tburl9,a.tburl10,a.tburl11,";
			sql= sql+"a.tbimg,a.tbimg1,a.tbimg2,a.tbimg3,a.tbimg4,a.tbimg5,a.tbimg6,a.tbimg7,a.tbimg8,a.tbimg9,a.tbimg10,a.tbimg11, ";
			sql= sql+"a.shop_id,a.shop_id1,a.shop_id2,a.shop_id3,a.shop_id4,a.shop_id5, a.shop_id6,a.shop_id7,a.shop_id8,a.shop_id9,a.shop_id10,a.shop_id11, ";
			sql= sql+"a.moq_price,a.moq_price1,a.moq_price2,a.moq_price3,a.moq_price4,a.moq_price5, a.moq_price6,a.moq_price7,a.moq_price8,a.moq_price9,a.moq_price10,a.moq_price11, ";
			sql= sql+"a.categoryid,a.categoryid1,a.categoryid2,a.categoryid3,a.categoryid4,a.categoryid5,a.categoryid6,a.categoryid7,a.categoryid8,a.categoryid9,a.categoryid10,a.categoryid11, ";
			sql= sql+"a.goods_moq,a.goods_moq1,a.goods_moq2,a.goods_moq3,a.goods_moq4,a.goods_moq5,a.goods_moq6,a.goods_moq7,a.goods_moq8,a.goods_moq9,a.goods_moq10,a.goods_moq11, ";
			sql= sql+"a.goods_price as ali_price,a.priority_flag,a.source_pro_flag,'' as goods_sellunits,'' as goods_weight, ";
			sql= sql+"a.goods_name0,a.goods_name1,a.goods_name2,a.goods_name3,a.goods_name4,a.goods_name5,a.goods_name6,a.goods_name7,a.goods_name8,a.goods_name9,a.goods_name10,a.goods_name11, ";
			sql= sql+"a.min_moq0,a.min_moq1,a.min_moq2,a.min_moq3,a.min_moq4,a.min_moq5,a.min_moq6,a.min_moq7,a.min_moq8,a.min_moq9,a.min_moq10,a.min_moq11,a.url ";
			sql= sql+"from alicachedatanew1_1 a ";
			sql= sql+" where a.tbflag=1  ";
			
			if(!"".equals(sourceTbl) && null!=sourceTbl){
				sql= sql +" and a.goods_pid=? ";
			}else{
				sql= sql+" and a.user_name='"+noBenchFlag+"'  and a.shelf_flag=4";
				sql= sql +" and a.flag=0  ";//
			}
			sql= sql +" limit ?, ?";
			//速卖通
		}else{
			sql = "select b.catid1, a.goods_pid,a.imgurl,a.tburl,a.tburl1,a.tburl2,a.tburl3,a.tburl4,a.tburl5,a.tburl6,a.tburl7,a.tburl8,a.tburl9,a.tburl10,a.tburl11,";
			sql= sql+"a.tbimg,a.tbimg1,a.tbimg2,a.tbimg3,a.tbimg4,a.tbimg5,a.tbimg6,a.tbimg7,a.tbimg8,a.tbimg9,a.tbimg10,a.tbimg11, ";
			sql= sql+"a.shop_id,a.shop_id1,a.shop_id2,a.shop_id3,a.shop_id4,a.shop_id5, a.shop_id6,a.shop_id7,a.shop_id8,a.shop_id9,a.shop_id10,a.shop_id11, ";
			sql= sql+"a.moq_price,a.moq_price1,a.moq_price2,a.moq_price3,a.moq_price4,a.moq_price5, a.moq_price6,a.moq_price7,a.moq_price8,a.moq_price9,a.moq_price10,a.moq_price11, ";
			sql= sql+"a.categoryid,a.categoryid1,a.categoryid2,a.categoryid3,a.categoryid4,a.categoryid5,a.categoryid6,a.categoryid7,a.categoryid8,a.categoryid9,a.categoryid10,a.categoryid11, ";
			sql= sql+"a.goods_moq,a.goods_moq1,a.goods_moq2,a.goods_moq3,a.goods_moq4,a.goods_moq5,a.goods_moq6,a.goods_moq7,a.goods_moq8,a.goods_moq9,a.goods_moq10,a.goods_moq11, ";
			sql= sql+"b.price_min as ali_price,a.priority_flag,a.source_pro_flag,b.goods_sellunits,b.goods_weight, ";
			sql= sql+"a.goods_name0,a.goods_name1,a.goods_name2,a.goods_name3,a.goods_name4,a.goods_name5,a.goods_name6,a.goods_name7,a.goods_name8,a.goods_name9,a.goods_name10,a.goods_name11, ";
			sql= sql+"a.min_moq0,a.min_moq1,a.min_moq2,a.min_moq3,a.min_moq4,a.min_moq5,a.min_moq6,a.min_moq7,a.min_moq8,a.min_moq9,a.min_moq10,a.min_moq11,a.url ";
			sql= sql+"from alicachedatanew1_1 a inner join ali_goods_source_new_hx b on a.goods_pid=b.goods_pid ";
			sql= sql+" where a.tbflag=1  ";
			
			if(!"".equals(sourceTbl) && null!=sourceTbl){
				sql= sql +" and a.goods_pid=? ";
			}else{
				sql= sql+" and a.user_name='"+noBenchFlag+"'  and a.shelf_flag=1";
//				sql= sql +" and a.flag=0   and a.catid_flag = 0";//
				sql= sql +" and a.flag=0  ";//
			}
				
			sql= sql +" limit ?, ?";
		}
		
		String sql1=" select level from supplier_scoring  where shop_id=? ";
		
		Connection conn = null;
		PreparedStatement stmt = null,stmt1 = null;
		ResultSet rs = null,rs1 = null;
		conn = DBHelper.getInstance().getConnection5();
		try {
			stmt = conn.prepareStatement(sql);
			if(!"".equals(sourceTbl) && null!=sourceTbl){
				stmt.setString(1, sourceTbl);
				stmt.setInt(2, start);
				stmt.setInt(3, end);
			}else{
				stmt.setInt(1, start);
				stmt.setInt(2, end);
			}
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setCatid1(rs.getString("catid1"));
				gfb.setGoodsPid(rs.getString("goods_pid"));
				gfb.setUrl(rs.getString("url"));
				gfb.setImgpath(rs.getString("imgurl"));
				gfb.setTbImg(rs.getString("tbimg"));
				gfb.setTbImg1(rs.getString("tbimg1"));
				gfb.setTbImg2(rs.getString("tbimg2"));
				gfb.setTbImg3(rs.getString("tbimg3"));
				gfb.setTbImg4(rs.getString("tbimg4"));
				gfb.setTbImg5(rs.getString("tbimg5"));
				gfb.setTbImg6(rs.getString("tbimg6"));
				gfb.setTbImg7(rs.getString("tbimg7"));
				gfb.setTbImg8(rs.getString("tbimg8"));
				gfb.setTbImg9(rs.getString("tbimg9"));
				gfb.setTbImg10(rs.getString("tbimg10"));
				gfb.setTbImg11(rs.getString("tbimg11"));
				gfb.setTbUrl(rs.getString("tburl"));
				gfb.setTbUrl1(rs.getString("tburl1"));
				gfb.setTbUrl2(rs.getString("tburl2"));
				gfb.setTbUrl3(rs.getString("tburl3"));
				gfb.setTbUrl4(rs.getString("tburl4"));
				gfb.setTbUrl5(rs.getString("tburl5"));
				gfb.setTbUrl6(rs.getString("tburl6"));
				gfb.setTbUrl7(rs.getString("tburl7"));
				gfb.setTbUrl8(rs.getString("tburl8"));
				gfb.setTbUrl9(rs.getString("tburl9"));
				gfb.setTbUrl10(rs.getString("tburl10"));
				gfb.setTbUrl11(rs.getString("tburl11"));
				gfb.setShopId(rs.getString("shop_id"));
				gfb.setShopId1(rs.getString("shop_id1"));
				gfb.setShopId2(rs.getString("shop_id2"));
				gfb.setShopId3(rs.getString("shop_id3"));
				gfb.setShopId4(rs.getString("shop_id4"));
				gfb.setShopId5(rs.getString("shop_id5"));
				gfb.setShopId6(rs.getString("shop_id6"));
				gfb.setShopId7(rs.getString("shop_id7"));
				gfb.setShopId8(rs.getString("shop_id8"));
				gfb.setShopId9(rs.getString("shop_id9"));
				gfb.setShopId10(rs.getString("shop_id10"));
				gfb.setShopId11(rs.getString("shop_id11"));
				//根据供应商打分表，查出每个工厂的情况
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id1"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel1(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id2"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel2(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id3"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel3(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id4"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel4(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id5"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel5(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id6"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel6(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id7"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel7(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id8"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel8(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id9"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel9(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id10"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel10(rs1.getString("level"));
				}
				
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("shop_id11"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					gfb.setShopLevel11(rs1.getString("level"));
				}
				
				gfb.setMoqPrice(rs.getString("moq_price"));
				gfb.setMoqPrice1(rs.getString("moq_price1"));
				gfb.setMoqPrice2(rs.getString("moq_price2"));
				gfb.setMoqPrice3(rs.getString("moq_price3"));
				gfb.setMoqPrice4(rs.getString("moq_price4"));
				gfb.setMoqPrice5(rs.getString("moq_price5"));
				gfb.setMoqPrice6(rs.getString("moq_price6"));
				gfb.setMoqPrice7(rs.getString("moq_price7"));
				gfb.setMoqPrice8(rs.getString("moq_price8"));
				gfb.setMoqPrice9(rs.getString("moq_price9"));
				gfb.setMoqPrice10(rs.getString("moq_price10"));
				gfb.setMoqPrice11(rs.getString("moq_price11"));
				gfb.setCategoryid(rs.getString("categoryid"));
				gfb.setCategoryid1(rs.getString("categoryid1"));
				gfb.setCategoryid2(rs.getString("categoryid2"));
				gfb.setCategoryid3(rs.getString("categoryid3"));
				gfb.setCategoryid4(rs.getString("categoryid4"));
				gfb.setCategoryid5(rs.getString("categoryid5"));
				gfb.setCategoryid6(rs.getString("categoryid6"));
				gfb.setCategoryid7(rs.getString("categoryid7"));
				gfb.setCategoryid8(rs.getString("categoryid8"));
				gfb.setCategoryid9(rs.getString("categoryid9"));
				gfb.setCategoryid10(rs.getString("categoryid10"));
				gfb.setCategoryid11(rs.getString("categoryid11"));
				gfb.setGoodsSold(rs.getString("goods_moq"));
				gfb.setGoodsSold1(rs.getString("goods_moq1"));
				gfb.setGoodsSold2(rs.getString("goods_moq2"));
				gfb.setGoodsSold3(rs.getString("goods_moq3"));
				gfb.setGoodsSold4(rs.getString("goods_moq4"));
				gfb.setGoodsSold5(rs.getString("goods_moq5"));
				gfb.setGoodsSold6(rs.getString("goods_moq6"));
				gfb.setGoodsSold7(rs.getString("goods_moq7"));
				gfb.setGoodsSold8(rs.getString("goods_moq8"));
				gfb.setGoodsSold9(rs.getString("goods_moq9"));
				gfb.setGoodsSold10(rs.getString("goods_moq10"));
				gfb.setGoodsSold11(rs.getString("goods_moq11"));

//				gfb.setGoodsPrice(String.valueOf(Double.valueOf(StrUtils.matchStr(rs.getString("ali_price").split("-")[0], "(\\d+\\.\\d+)"))*Util.EXCHANGE_RATE));test
				gfb.setGoodsPrice(String.valueOf(Double.valueOf(rs.getString("ali_price").split("-")[0])*Util.EXCHANGE_RATE));
				gfb.setPriorityFlag(rs.getInt("priority_flag"));
				gfb.setSourceProFlag(rs.getInt("source_pro_flag"));
				gfb.setpUtil(rs.getString("goods_sellunits"));
//				gfb.setTypes(rs.getString("types"));
//				gfb.setSku(rs.getString("sku"));
				gfb.setGoodsWeight(rs.getString("goods_weight"));
				
				gfb.setGoodsName0(rs.getString("goods_name0"));
				gfb.setGoodsName1(rs.getString("goods_name1"));
				gfb.setGoodsName2(rs.getString("goods_name2"));
				gfb.setGoodsName3(rs.getString("goods_name3"));
				gfb.setGoodsName4(rs.getString("goods_name4"));
				gfb.setGoodsName5(rs.getString("goods_name5"));
				gfb.setGoodsName6(rs.getString("goods_name6"));
				gfb.setGoodsName7(rs.getString("goods_name7"));
				gfb.setGoodsName8(rs.getString("goods_name8"));
				gfb.setGoodsName9(rs.getString("goods_name9"));
				gfb.setGoodsName10(rs.getString("goods_name10"));
				gfb.setGoodsName11(rs.getString("goods_name11"));
				gfb.setMinMoq0(rs.getString("min_moq0"));
				gfb.setMinMoq1(rs.getString("min_moq1"));
				gfb.setMinMoq2(rs.getString("min_moq2"));
				gfb.setMinMoq3(rs.getString("min_moq3"));
				gfb.setMinMoq4(rs.getString("min_moq4"));
				gfb.setMinMoq5(rs.getString("min_moq5"));
				gfb.setMinMoq6(rs.getString("min_moq6"));
				gfb.setMinMoq7(rs.getString("min_moq7"));
				gfb.setMinMoq8(rs.getString("min_moq8"));
				gfb.setMinMoq9(rs.getString("min_moq9"));
				gfb.setMinMoq10(rs.getString("min_moq10"));
				gfb.setMinMoq11(rs.getString("min_moq11"));
				
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public List<GoodsCheckBean> getErrorInfo(String userId, String timeFrom, String timeTo, int start, int end,int valid){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		
//		String sql=" select user_id,url,create_time from error_info  ";
		String sql= " select b.email,a.user_id,a.url,a.create_time,b.is_test from error_info a ";
			   sql= sql+"LEFT JOIN user b on a.user_id=b.id ";
			  // sql= sql+"and b.email  not  like  'test%' and user_id<>1128 and  b.email  not  like  '%qq.com' and  b.email  not  like  '%163.com' ";
			  // sql= sql+"and b.email  not  like  'Xielulu1026%'   and b.email  not  like  'lifangha740%'  ";
			  // sql= sql+"and b.email  not  like  'jackluo666@aliyun.com'   and b.email  not  like  'Jennyblack1982@hotmail.com'   ";
			   sql= sql+" where (b.is_test IS NULL OR b.is_test =0) ";
			   if(!"".equals(userId) && userId!=null){
				   if ("-1".equals(userId)) {
						sql+=" and a.user_id !=0 ";
					}else {
						sql =sql+" and a.user_id = '"+userId+"' ";	
					}
			   }
			   if(!"".equals(timeFrom) && timeFrom!=null){
				   sql =sql+" and a.create_time >= '"+timeFrom+"' ";
			   }
			   if(!"".equals(timeTo) && timeTo!=null){
				   sql =sql+" and a.create_time <= '"+timeTo+"' ";
			   }
			   
			   sql= sql+"order by a.create_time desc LIMIT "+start+","+end+"";
			   if (valid==10) {
//				   sql= " SELECT c.*,COUNT(c.pid) as count,d.unsellableReason_name,d.cur_time FROM (SELECT a.*,b.email,b.is_test from error_info as a LEFT JOIN `user` as b "
//				   		+ "ON a.user_id=b.id  "
//				   		+ "  ) as c LEFT JOIN (SELECT f.unsellableReason,f.pid,f.cur_time,um.unsellableReason_name FROM custom_benchmark_ready as f LEFT JOIN unsellablereason_master as um ON f.unsellableReason=um.unsellableReason_id) AS d on c.pid=d.pid  ";
//				   sql= sql+" JOIN user b on a.user_id=b.id LEFT JOIN custom_benchmark_ready as cbr ON a.pid=cbr.pid ";
				  // sql= sql+"and b.email  not  like  'test%' and user_id<>1128 and  b.email  not  like  '%qq.com' and  b.email  not  like  '%163.com' ";
				   //sql= sql+"and b.email  not  like  'Xielulu1026%'   and b.email  not  like  'lifangha740%'  ";
				   //sql= sql+"and b.email  not  like  'jackluo666@aliyun.com'   and b.email  not  like  'Jennyblack1982@hotmail.com'   ";
				  // sql= sql+"and b.email  not  like  '789@222.com'  where 1=1 ";
//					sql+=" where c.valid=10  AND (c.is_test IS NULL OR c.is_test =0) and d.unsellableReason_name is NOT NULL";
					
					
					sql="SELECT l.* ,d.unsellableReason_name,d.cur_time FROM (SELECT * FROM "
                  +"(SELECT c.id,c.create_time,c.ip,c.pid,c.url,c.user_id,c.valid,c.email,c.is_test,COUNT(c.pid) as count FROM"
                  +"(SELECT a.id,a.create_time,a.ip,a.pid,a.url,a.user_id,a.valid,b.email,b.is_test from error_info  a "
                  +" LEFT JOIN `user`  b "
				   	+"	ON a.user_id=b.id  "
				   	+"	  )  c WHERE c.valid=10 AND pid regexp '^[0-9]+$'  AND (c.is_test IS NULL OR c.is_test =0)";
				   	if(!"".equals(userId) && userId!=null){
						   
						   if ("-1".equals(userId)) {
								sql+=" and c.user_id !=0 ";
							}else {
								sql =sql+" and c.user_id = '"+userId+"' ";	
							}
					   }
					   if(!"".equals(timeFrom) && timeFrom!=null){
						   sql =sql+" and c.create_time >= '"+timeFrom+"' ";
					   }
					   if(!"".equals(timeTo) && timeTo!=null){
						   sql =sql+" and c.create_time <= '"+timeTo+"' ";
					   }
				   	sql+= " GROUP BY c.pid ) h WHERE h.count >2) as l " 

                      +" LEFT JOIN (SELECT f.unsellableReason,f.pid,f.cur_time,um.unsellableReason_name FROM custom_benchmark_ready as f LEFT JOIN "
                   +" unsellablereason_master as um ON f.unsellableReason=um.unsellableReason_id where um.unsellableReason_name is NOT NULL ) AS d on l.pid=d.pid  where d.unsellableReason_name is NOT NULL"
					+"  ORDER BY count DESC, l.create_time DESC LIMIT "+start+","+end+"";
					
//					if(!"".equals(userId) && userId!=null){
//						   
//						   if ("-1".equals(userId)) {
//								sql+=" and c.user_id !=0 ";
//							}else {
//								sql =sql+" and c.user_id = '"+userId+"' ";	
//							}
//					   }
//					   if(!"".equals(timeFrom) && timeFrom!=null){
//						   sql =sql+" and c.create_time >= '"+timeFrom+"' ";
//					   }
//					   if(!"".equals(timeTo) && timeTo!=null){
//						   sql =sql+" and c.create_time <= '"+timeTo+"' ";
//					   }
//					   sql+=" GROUP BY c.pid ORDER BY count DESC, c.create_time DESC LIMIT "+start+","+end+"";
				}
            
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setEmail(rs.getString("email"));
				gfb.setUserName(rs.getString("user_id"));
				gfb.setUrl(rs.getString("url"));
				gfb.setCreatetime(rs.getString("create_time"));
				if (valid ==10) {
				gfb.setAddress(rs.getString("count"));
				gfb.setAliCatName(rs.getString("unsellableReason_name"));
				gfb.setAligSourceUrl(rs.getString("cur_time"));
				gfb.setAliStyImg(rs.getString("ip"));
				}
				
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public List<GoodsCheckBean> findSamplInfo(int selled, String cid, String shopId, int start, int end){
		Connection conn = DBHelper.getInstance().getConnection();
//		Connection alidata = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		StringBuffer sql=new StringBuffer();
		try{
			sql.append("select c.shop_id,sid.shop_guarantee,sid.address,sid.sale_service,s.level,count(distinct c.pid) as pId" +
					" from custom_benchmark_ready c left join supplier_scoring s on c.shop_id=s.shop_id " +
					" left join shop_info_data sid on c.shop_id=sid.shop_id " +
					" where c.valid=1 ");
			if(StringUtil.isNotBlank(shopId)){
				sql.append(" and c.shop_id='"+shopId+"'");
			}
			sql.append(" group by c.shop_id ORDER BY COUNT(DISTINCT c.pid) DESC  limit "+start+",40");
			stmt=conn.prepareStatement(sql.toString());
			rs=stmt.executeQuery();
			while(rs.next()){
				GoodsCheckBean g=new GoodsCheckBean();
				g.setShopId(rs.getString("shop_id"));
				g.setShopGuarantee(rs.getString("shop_guarantee"));
				g.setAddress(rs.getString("address"));
				g.setSaleService(rs.getString("sale_service"));
				g.setShopLevel(rs.getString("level"));
				g.setpId(rs.getInt("pId"));
				gsfList.add(g);
			}
			for(GoodsCheckBean g:gsfList){
				String authorizedFlag="未授权";
				String sql1="SELECT shop_type as shopId,authorized_flag as shopUrl FROM shop_url_bak WHERE shop_id='"+g.getShopId()+"'";
				stmt=conn.prepareStatement(sql1);
				rs=stmt.executeQuery();
				if(rs.next()){
					String shopUrl=rs.getString("shopUrl");
					if(StringUtil.isNotBlank(shopUrl) && "1".equals(shopUrl)){
						authorizedFlag="已授权";
					}
				}
				g.setAuthorizedFlag(authorizedFlag);
				//查询店铺采样过的商品数量
				sql1=" SELECT COUNT(od.id) as counts FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no" +
						"   INNER JOIN custom_benchmark_ready c ON od.goods_pid=c.pid" +
						"  WHERE oi.isDropshipOrder=3 AND oi.state BETWEEN 1 AND 5 AND od.state<2 " +
						"   AND c.shop_id='"+g.getShopId()+"'";
				stmt=conn.prepareStatement(sql1);
				rs=stmt.executeQuery();
				if(rs.next()){
					g.setSamplCount(rs.getInt("counts"));
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
//			DBHelper.getInstance().closeConnection(alidata);
		}
		return gsfList;
	}
	
	
	@Override
	public List<GoodsCheckBean> findLireImgInfo(int selled, String cid, String shopId, int start, int end, int flag){
		
//		upPidValid();
		Connection conn = DBHelper.getInstance().getConnection();
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		String sql = "select sor_pid,res_pid1,res_pid2,res_pid3,res_pid4,res_pid5,res_pid6,img_path, ";
			sql= sql+"yl_price,yl_price1,yl_price2,yl_price3,yl_price4,yl_price5,yl_price6, ";
			sql= sql+"pro_con,pro_con1,pro_con2,pro_con3,pro_con4,pro_con5,pro_con6, ";
			sql= sql+"shop_quality,shop_quality1,shop_quality2,shop_quality3,shop_quality4,shop_quality5,shop_quality6, ";
			sql= sql+"pro_score,pro_score1,pro_score2,pro_score3,pro_score4,pro_score5,pro_score6, ";
			sql= sql+"deep_factory,deep_factory1,deep_factory2,deep_factory3,deep_factory4,deep_factory5,deep_factory6, ";
			sql= sql+"valid,valid1,valid2,valid3,valid4,valid5,valid6, ";
			sql= sql+"pro_sold,pro_sold1,pro_sold2,pro_sold3,pro_sold4,pro_sold5,pro_sold6,";
			sql= sql+"(select b.valid from custom_benchmark_ready b where lire_img_custom.res_pid1=b.pid ) as customer_valid1,";
			sql= sql+"(select b.valid from custom_benchmark_ready b where lire_img_custom.res_pid2=b.pid ) as customer_valid2,";
			sql= sql+"(select b.valid from custom_benchmark_ready b where lire_img_custom.res_pid3=b.pid ) as customer_valid3,";
			sql= sql+"(select b.valid from custom_benchmark_ready b where lire_img_custom.res_pid4=b.pid ) as customer_valid4,";
			sql= sql+"(select b.valid from custom_benchmark_ready b where lire_img_custom.res_pid5=b.pid ) as customer_valid5,";
			sql= sql+"(select b.valid from custom_benchmark_ready b where lire_img_custom.res_pid6=b.pid ) as customer_valid6,";
			sql= sql+"(select level from supplier_scoring  where shop_id=lire_img_custom.shop_id1 ) as shop_name1,";
			sql= sql+"(select level from supplier_scoring  where shop_id=lire_img_custom.shop_id2 ) as shop_name2,";
			sql= sql+"(select level from supplier_scoring  where shop_id=lire_img_custom.shop_id3 ) as shop_name3,";
			sql= sql+"(select level from supplier_scoring  where shop_id=lire_img_custom.shop_id4 ) as shop_name4,";
			sql= sql+"(select level from supplier_scoring  where shop_id=lire_img_custom.shop_id5 ) as shop_name5,";
			sql= sql+"(select level from supplier_scoring  where shop_id=lire_img_custom.shop_id6 ) as shop_name6,";
			sql= sql+"(select quality_avg from supplier_scoring  where shop_id=lire_img_custom.shop_id1 ) as quality_shop1,";
			sql= sql+"(select quality_avg from supplier_scoring  where shop_id=lire_img_custom.shop_id2 ) as quality_shop2,";
			sql= sql+"(select quality_avg from supplier_scoring  where shop_id=lire_img_custom.shop_id3 ) as quality_shop3,";
			sql= sql+"(select quality_avg from supplier_scoring  where shop_id=lire_img_custom.shop_id4 ) as quality_shop4,";
			sql= sql+"(select quality_avg from supplier_scoring  where shop_id=lire_img_custom.shop_id5 ) as quality_shop5,";
			sql= sql+"(select quality_avg from supplier_scoring  where shop_id=lire_img_custom.shop_id6 ) as quality_shop6,";
			sql= sql+"(select ROUND(avg(quality),0) from supplier_product  where goods_pid=lire_img_custom.res_pid1 ) as quality_product1,";
			sql= sql+"(select ROUND(avg(quality),0) from supplier_product  where goods_pid=lire_img_custom.res_pid2 ) as quality_product2,";
			sql= sql+"(select ROUND(avg(quality),0) from supplier_product  where goods_pid=lire_img_custom.res_pid3 ) as quality_product3,";
			sql= sql+"(select ROUND(avg(quality),0) from supplier_product  where goods_pid=lire_img_custom.res_pid4 ) as quality_product4,";
			sql= sql+"(select ROUND(avg(quality),0) from supplier_product  where goods_pid=lire_img_custom.res_pid5 ) as quality_product5,";
			sql= sql+"(select ROUND(avg(quality),0) from supplier_product  where goods_pid=lire_img_custom.res_pid6 ) as quality_product6, ";
			sql= sql+"(select is_edited from custom_goods_edit  where pid=lire_img_custom.res_pid1 ) as is_edit1,";
			sql= sql+"(select is_edited from custom_goods_edit  where pid=lire_img_custom.res_pid2 ) as is_edit2,";
			sql= sql+"(select is_edited from custom_goods_edit  where pid=lire_img_custom.res_pid3 ) as is_edit3,";
			sql= sql+"(select is_edited from custom_goods_edit  where pid=lire_img_custom.res_pid4 ) as is_edit4,";
			sql= sql+"(select is_edited from custom_goods_edit  where pid=lire_img_custom.res_pid5 ) as is_edit5,";
			sql= sql+"(select is_edited from custom_goods_edit  where pid=lire_img_custom.res_pid6 ) as is_edit6";
			
			sql= sql+" from  lire_img_custom where flag=2 limit ?, ?";
			
			String path ="";
			if(flag>0){
				path = "http://192.168.1.28:83";
			}else{
				path = "http://117.144.21.74:83";
			}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, end);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				//pid
				gfb.setGoodsName0(rs.getString("sor_pid"));
				gfb.setGoodsName1(rs.getString("res_pid1"));
				gfb.setGoodsName2(rs.getString("res_pid2"));
				gfb.setGoodsName3(rs.getString("res_pid3"));
				gfb.setGoodsName4(rs.getString("res_pid4"));
				gfb.setGoodsName5(rs.getString("res_pid5"));
				gfb.setGoodsName6(rs.getString("res_pid6"));
				//imgurl
				String sourceImg = path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("sor_pid")+".jpg";
				gfb.setSourceImg(sourceImg);
				gfb.setTbImg1(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid1")+".jpg");
				gfb.setTbImg2(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid2")+".jpg");
				gfb.setTbImg3(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid3")+".jpg");
				gfb.setTbImg4(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid4")+".jpg");
				gfb.setTbImg5(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid5")+".jpg");
				gfb.setTbImg6(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid6")+".jpg");
				//价格
				gfb.setPrice(rs.getString("yl_price"));
				gfb.setTbprice(rs.getString("yl_price1"));
				gfb.setTbprice1(rs.getString("yl_price2"));
				gfb.setTbprice2(rs.getString("yl_price3"));
				gfb.setTbprice3(rs.getString("yl_price4"));
				gfb.setTbprice4(rs.getString("yl_price5"));
				gfb.setTbprice5(rs.getString("yl_price6"));
				//卖家在我司有多少产品
				gfb.setProCon(rs.getInt("pro_con"));
				gfb.setProCon1(rs.getInt("customer_valid1"));
				gfb.setProCon2(rs.getInt("customer_valid2"));
				gfb.setProCon3(rs.getInt("customer_valid3"));
				gfb.setProCon4(rs.getInt("customer_valid4"));
				gfb.setProCon5(rs.getInt("customer_valid5"));
				gfb.setProCon6(rs.getInt("customer_valid6"));
				//是否人为编辑过
				gfb.setShopQuality(rs.getInt("shop_quality"));
				gfb.setShopQuality1(rs.getInt("is_edit1"));
				gfb.setShopQuality2(rs.getInt("is_edit2"));
				gfb.setShopQuality3(rs.getInt("is_edit3"));
				gfb.setShopQuality4(rs.getInt("is_edit4"));
				gfb.setShopQuality5(rs.getInt("is_edit5"));
				gfb.setShopQuality6(rs.getInt("is_edit6"));
				
				//该卖家质量水准、
				gfb.setShopId1(rs.getString("shop_name1"));
				gfb.setShopId2(rs.getString("shop_name2"));
				gfb.setShopId3(rs.getString("shop_name3"));
				gfb.setShopId4(rs.getString("shop_name4"));
				gfb.setShopId5(rs.getString("shop_name5"));
				gfb.setShopId6(rs.getString("shop_name6"));
				
				//商品评分、
				gfb.setProScore(rs.getInt("pro_score"));
				gfb.setProScore1(rs.getInt("quality_product1"));
				gfb.setProScore2(rs.getInt("quality_product2"));
				gfb.setProScore3(rs.getInt("quality_product3"));
				gfb.setProScore4(rs.getInt("quality_product4"));
				gfb.setProScore5(rs.getInt("quality_product5"));
				gfb.setProScore6(rs.getInt("quality_product6"));
				
				//1688深度验厂、
				gfb.setDeepFactory(rs.getInt("deep_factory"));
				gfb.setDeepFactory1(rs.getInt("quality_shop1"));
				gfb.setDeepFactory2(rs.getInt("deep_factory2"));
				gfb.setDeepFactory3(rs.getInt("deep_factory3"));
				gfb.setDeepFactory4(rs.getInt("deep_factory4"));
				gfb.setDeepFactory5(rs.getInt("deep_factory5"));
				gfb.setDeepFactory6(rs.getInt("deep_factory6"));
				//销量
				gfb.setProSold(rs.getInt("pro_sold"));
				gfb.setProSold1(rs.getInt("quality_product1"));
				gfb.setProSold2(rs.getInt("quality_product2"));
				gfb.setProSold3(rs.getInt("quality_product3"));
				gfb.setProSold4(rs.getInt("quality_product4"));
				gfb.setProSold5(rs.getInt("quality_product5"));
				gfb.setProSold6(rs.getInt("quality_product6"));
				
//				if(rs.getInt("valid")!=1){
//					gfb.setSourceImg("");
//				}
//				if(rs.getInt("valid1")!=1){
//					gfb.setTbImg1("");
//				}
//				if(rs.getInt("valid2")!=1){
//					gfb.setTbImg2("");
//				}
//				if(rs.getInt("valid3")!=1){
//					gfb.setTbImg3("");
//				}
//				if(rs.getInt("valid4")!=1){
//					gfb.setTbImg4("");
//				}
//				if(rs.getInt("valid5")!=1){
//					gfb.setTbImg5("");
//				}
//				if(rs.getInt("valid6")!=1){
//					gfb.setTbImg6("");
//				}
				gsfList.add(gfb);
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
		return gsfList;
	}

	@Override
	public List<GoodsCheckBean> findImgDb(int selled, String cid, String pId, int start, int end, int flag){

		Connection conn = DBHelper.getInstance().getConnection();
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		String sql = "select sor_pid,res_pid1,res_pid2,res_pid3,res_pid4,res_pid5,res_pid6,img_path ";

		sql= sql+" from  lire_img_custom_db ";
		sql= sql+" where 1=1 ";
		if(StringUtil.isNotBlank(pId)){
			sql= sql+" and sor_pid=? ";
		}
		sql= sql+"  limit ?, ?";

		String path ="";
		if(flag>0){
			path = "http://192.168.1.28:83";
		}else{
			path = "http://117.144.21.74:83";
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(StringUtil.isNotBlank(pId)){
				stmt.setString(1,pId);
				stmt.setInt(2, start);
				stmt.setInt(3, end);
			}else{
				stmt.setInt(1, start);
				stmt.setInt(2, end);
			}


			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				//pid
				gfb.setGoodsName0(rs.getString("sor_pid"));
				gfb.setGoodsName1(rs.getString("res_pid1"));
				gfb.setGoodsName2(rs.getString("res_pid2"));
				gfb.setGoodsName3(rs.getString("res_pid3"));
				gfb.setGoodsName4(rs.getString("res_pid4"));
				gfb.setGoodsName5(rs.getString("res_pid5"));
				gfb.setGoodsName6(rs.getString("res_pid6"));
				String imgurl="\\OnlineGoodsMain\\OnlineGoodsMain1";
				String sourceImg = path+imgurl.replace("\\","/")+"/"+rs.getString("sor_pid")+".jpg";
				gfb.setSourceImg(sourceImg);
				gfb.setTbImg1(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid1")+".jpg");
				gfb.setTbImg2(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid2")+".jpg");
				gfb.setTbImg3(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid3")+".jpg");
				gfb.setTbImg4(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid4")+".jpg");
				gfb.setTbImg5(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid5")+".jpg");
				gfb.setTbImg6(path+rs.getString("img_path").replace("\\","/")+"/"+rs.getString("res_pid6")+".jpg");

				gsfList.add(gfb);
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
		return gsfList;
	}


	public int upPidValid() {
		
		String sql ="update lire_img_custom a,lire_goods b set a.valid=0 where a.sor_pid=b.pid";
		String sql1 ="update lire_img_custom a,lire_goods b set a.valid1=0 where a.res_pid1=b.pid";
		String sql2 ="update lire_img_custom a,lire_goods b set a.valid2=0 where a.res_pid2=b.pid";
		String sql3 ="update lire_img_custom a,lire_goods b set a.valid3=0 where a.res_pid3=b.pid";
		String sql4 ="update lire_img_custom a,lire_goods b set a.valid4=0 where a.res_pid4=b.pid";
		String sql5 ="update lire_img_custom a,lire_goods b set a.valid5=0 where a.res_pid5=b.pid";
		String sql6 ="update lire_img_custom a,lire_goods b set a.valid6=0 where a.res_pid6=b.pid";
			
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null,stmt1 = null,stmt2 = null,stmt3 = null,stmt4 = null,stmt5 = null,stmt6 = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt1 = conn.prepareStatement(sql1);
			stmt2 = conn.prepareStatement(sql2);
			stmt3 = conn.prepareStatement(sql3);
			stmt4 = conn.prepareStatement(sql4);
			stmt5 = conn.prepareStatement(sql5);
			stmt6 = conn.prepareStatement(sql6);
			res = stmt.executeUpdate();
			stmt1.executeUpdate();
			stmt2.executeUpdate();
			stmt3.executeUpdate();
			stmt4.executeUpdate();
			stmt5.executeUpdate();
			stmt6.executeUpdate();
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
		//更新ali信息
//		updateAliInfo(tbGoodBean.getGoodId());
	 return res;
	}
	
	
	@Override
	public List<GoodsCheckBean> getOneShopInFo(String flag){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		
//		String sql = "select count(a.shop_id) as shopCount,a.shop_id as sourceShopid,a.goods_pid as sourceGoodspid, "
//				+ " CONCAT(b.shop_id,',',b.shop_id1,',',b.shop_id2,',',b.shop_id3,',',b.shop_id4,',',b.shop_id5, "
//				+ " ',',b.shop_id6,',',b.shop_id7,',',b.shop_id8,',',b.shop_id9,',',b.shop_id10,',',b.shop_id11) as shop_ids "
//				+ " from ali_info_data a, alicachedatanew1_1 b where a.shelf_flag=1 and a.goods_pid=b.goods_pid "
//				+ " group by a.shop_id HAVING count(a.shop_id) =1 ";
		
		String sql = "select a.shop_id as sourceShopid,a.goods_pid as sourceGoodspid, "
				+ " CONCAT(b.shop_id,',',b.shop_id1,',',b.shop_id2,',',b.shop_id3,',',b.shop_id4,',',b.shop_id5, "
				+ " ',',b.shop_id6,',',b.shop_id7,',',b.shop_id8,',',b.shop_id9,',',b.shop_id10,',',b.shop_id11) as shop_ids "
				+ " from ali_info_data a, alicachedatanew1_1 b where a.goods_pid=b.goods_pid "
				+ " and a.shop_id in (select shop_id  from ali_info_data where shelf_flag=1 group by shop_id HAVING count(shop_id) =1 ) ";
			
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection5();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setShopId(rs.getString("sourceShopid"));
				gfb.setGoodsPid(rs.getString("sourceGoodspid"));
				gfb.setShopId1(rs.getString("shop_ids"));
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	
	
	public GoodsCheckBean getSamplePriceCount(String ylbbPid){
		
		GoodsCheckBean gfb = new GoodsCheckBean();
		
		String sql = "SELECT SUM(ops.buycount) as buycount,SUM(ops.goods_p_price*ops.buycount+5) as buyPrice FROM order_product_source ops  "
				+" INNER JOIN orderinfo oi ON ops.orderid=oi.order_no "
				+" INNER JOIN order_details od ON oi.order_no=od.orderid "
				+" WHERE oi.isDropshipOrder=3 AND oi.state>0 AND oi.state<6 AND od.state<2 "
				+" AND ops.purchase_state IN (3,4,6,8) AND od.goods_pid IN (?) ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ylbbPid);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				gfb.setBuyCount(rs.getInt("buyCount"));
				gfb.setBuyPrice(rs.getString("buyPrice"));
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
		return gfb;
	}
	
	
	
	@Override
	public List<GoodsCheckBean> findWinPic(String goodsPid){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		
		String sql = "select goods_pid,img from  ali_goods_source_new_ex_hx where goods_pid=? ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection5();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodsPid);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				
				String imgs = rs.getString("img").replace("[", "").replace("]", "");
				String[] imgArry = imgs.split(",");
				for(int i=1;i<imgArry.length;i++){
					GoodsCheckBean gfb = new GoodsCheckBean();
					
					gfb.setAliSourceImgUrl(imgArry[i]);
					gfb.setImgurl(imgArry[i].replace("https://ae01.alicdn.com",""));
					gfb.setImgpath("https://ae01.alicdn.com");
					
					gsfList.add(gfb);
				}
				
//				GoodsCheckBean gfb = new GoodsCheckBean();
//				gfb.setGoodsPid(rs.getString("goods_pid"));
//				gsfList.add(gfb);
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
		return gsfList;
	}
	
	
	@Override
	public String getSourceTbl(String aliPid){
		
		String sql = "select source_tbl from ali_info_data where goods_pid="+aliPid+" limit 1";
		String sourceTbl="";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				sourceTbl=rs.getString("source_tbl");
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
		return sourceTbl;
	}
	/**
	 * 根据工厂ID查询核心商品
	 */
		@Override
		public List<AliInfoDataBean> fingGoodsByShopId(String shop_id, int start,int end) {
		List<AliInfoDataBean> gsfList = new ArrayList<AliInfoDataBean>();
		String sql="select c.shop_id,c.ali_pid,c.pid,c.enname,c.remotpath,c.custom_main_image,c.entype,c.wholesale_price from custom_benchmark_ready c where c.valid=1 and c.samplingStatus in (0,1) and c.shop_id='"+shop_id+"' limit "+start+",40";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AliInfoDataBean a = new AliInfoDataBean();
				a.setGoods_pid(rs.getString("pid"));
				a.setShop_id(rs.getString("shop_id"));
				a.setGoods_name(rs.getString("enname"));
				a.setImg_1688(rs.getString("remotpath")+rs.getString("custom_main_image"));
				a.setUrl_1688("https://detail.1688.com/offer/"+rs.getString("pid")+".html");
				a.setGoods_url("https://www.aliexpress.com/item/a/"+rs.getString("ali_pid")+".html");
				a.setPrice_1688(rs.getString("wholesale_price"));
				String types = StrUtils.object2Str(rs.getString("entype"));
				if(!com.cbt.common.StringUtils.isStrNull(types)){
					a.setEntype(Util.getTypeList(types,rs.getString("remotpath")));
				}
				gsfList.add(a);
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
		return gsfList;
		}
		

	
	@Override
	public List<GoodsCheckBean> findGoodsDataCheck1(String cid, String path, int start, int end){
		
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		String sql = "select id,goodsname,url,imgurl,imgpath,tbimg,tburl,price,tbimg1,tburl1,tbimg2,tburl2,tbimg3,tburl3,tbprice,tbprice1,tbprice2,tbprice3,";
		sql = sql +  "least(imgcheck0,imgcheck1,imgcheck2,imgcheck3) as minImgCheck from goodsdatacheck ";
		sql = sql +  " where (imgcheck0<=10 ||  imgcheck1<=10 ||  imgcheck2<=10 ||  imgcheck3<=10) order by id  limit ?, ? ";
//		sql = sql +  " where floor(imgcheck0/10)+floor(imgcheck1/10)+floor(imgcheck2/10)+floor(imgcheck3/10) < 4 order by id  limit ?, ? ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, end);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GoodsCheckBean gfb = new GoodsCheckBean();
				gfb.setpId(rs.getInt("id"));
				gfb.setGoodsName(rs.getString("goodsname"));
				gfb.setUrl(rs.getString("url"));
				gfb.setImgpath(rs.getString("imgpath"));
				gfb.setPrice(rs.getString("price"));
//				gfb.setImgurl(rs.getString("imgurl").replace("[", "").replace("]", ""));
				gfb.setTbImg(rs.getString("tbimg"));
				gfb.setTbUrl(rs.getString("tburl"));
				gfb.setTbprice(rs.getString("tbprice"));
				gfb.setTbImg1(rs.getString("tbimg1"));
				gfb.setTbUrl1(rs.getString("tburl1"));
				gfb.setTbprice1(rs.getString("tbprice1"));
				gfb.setTbImg2(rs.getString("tbimg2"));
				gfb.setTbUrl2(rs.getString("tburl2"));
				gfb.setTbprice2(rs.getString("tbprice2"));
				gfb.setTbImg3(rs.getString("tbimg3"));
				gfb.setTbUrl3(rs.getString("tburl3"));
				gfb.setTbprice3(rs.getString("tbprice3"));
				gfb.setMinImgCheck(rs.getInt("minImgCheck"));
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public List<CategoryBean> getCategoryInfo(){
		
		List<CategoryBean> gsfList = new ArrayList<CategoryBean>();
		String sql = "select cid,path,category from ali_category where (LENGTH(path) - LENGTH(REPLACE(path,',',''))) = 0 and cid<>0 ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CategoryBean gfb = new CategoryBean();
				gfb.setCid(rs.getString("cid"));
				gfb.setPath(rs.getString("path"));
				gfb.setCategoryName(rs.getString("category"));
				gsfList.add(gfb);
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
		return gsfList;
	}
	
	@Override
	public List<CategoryBean> getCategoryInfo1(String cid){
		
		List<CategoryBean> gsfList = new ArrayList<CategoryBean>();
		String sql = "select cid,path,category from ali_category where  path like '%"+cid+"%'  and (LENGTH(path) - LENGTH(REPLACE(path,',',''))) = 1 ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CategoryBean gfb = new CategoryBean();
				gfb.setCid(rs.getString("cid"));
				gfb.setPath(rs.getString("path"));
				gfb.setCategoryName(rs.getString("category"));
				gsfList.add(gfb);
				
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
		return gsfList;
	}
	
	
	@Override
	public int getMaxCount() {
		
		String sql ="select maxCount from goodsindex";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int updateLireConditionFlag(List<GoodsFarBean> beanList) {
		
		String sql ="update importex.lire_search_condition set flag=0 where url=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			for(int i=0;i<beanList.size();i++){
				stmt.setString(1, beanList.get(i).getUrl());
				res = stmt.executeUpdate();
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
		return res;
	}
	
	@Override
	public int updateImgFileUpload(List<GoodsFarBean> beanList) {
		
		String sql ="update importex.imgfile set upload=1 where url=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			for(int i=0;i<beanList.size();i++){
				stmt.setString(1, beanList.get(i).getUrl());
				res = stmt.executeUpdate();
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
		return res;
	}
	
	@Override
	public int getTbMaxCount() {
		
		String sql ="select tbMaxCount from goodsindex";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("tbMaxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbStyMaxCount() {
		
		String sql ="select tbStyMaxCount from goodsindex";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("tbStyMaxCount");
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
		return mCount;
	}
	
	@Override
	public int getCpMaxCount() {
		
		String sql ="select cpMaxCount from goodsindex";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("cpMaxCount");
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
		return mCount;
	}
	
	@Override
	public int getGoodsCheckCount(int selled,String similarityId,String categoryId1) {
		
		String sql ="select count(*) as maxCount from goodsdatacheck ";
		//TODO
		sql = sql +"where alcat like '%Jewelry%' and tburl is not null ";
		
//		if(!"".equals(categoryId1) && categoryId1 != null){
//			sql = sql + "and ge.catid1 = "+categoryId1+" ";
//		}
		if(selled!=0){
			sql =sql +" and selled > "+selled;
		}
		if("1".equals(similarityId)){
			sql = sql +  " and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<5 ";
		}else if("2".equals(similarityId)){
			sql = sql +  " and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>5 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<10 ";
		}else if("3".equals(similarityId)){
			sql = sql +  " and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>10 ";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getOrderPurchaseCount(int selled,String similarityId,String categoryId1) {
		
		
		String sql ="select count(distinct(oi.order_no)) as maxCount ";
		sql = sql +"from orderinfo as oi inner join order_details as od on oi.order_no = od.orderid  ";
		sql = sql +"inner join goods_car gc on od.goodsid =gc.id inner join goodsdatacheck gdc on gc.goods_url = gdc.url  ";
		sql = sql +"left join payment as p on oi.order_no=p.orderid  ";
		sql = sql +"left join user u on oi.user_id = u.id  ";
		
//		if(selled!=0){
//			sql =sql +" and selled > "+selled;
//		}
//		if("1".equals(similarityId)){
//			sql = sql +  " and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<5 ";
//		}else if("2".equals(similarityId)){
//			sql = sql +  " and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>5 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<10 ";
//		}else if("3".equals(similarityId)){
//			sql = sql +  " and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>10 ";
//		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	
	@Override
	public int getTbgooddataCount() {
		
//		String sql = "select count(*) as maxCount ";
//		sql= sql+"from goodsdatacheckhot01 where (result!=1 and result1!=1 and result2!=1 and result3!=1 and result4!=1 and result5!=1) and delflag=1 and ali_flag=0 ";
		
		String sql = "select count(*) as maxCount ";
		sql= sql+" from core_cache_data a,ali_info_data b where a.goods_pid=b.goods_pid  ";
		sql= sql+" and a.imgflag=b.img_flag and b.mark_flag in(2,3) and b.flag=3 and b.handle_flag=0 ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getYlbbGooddataCount(String userName,int flag) {
		
//		String sql = "select count(*) as maxCount ";
//		sql= sql+" from core_cache_data a,ali_info_data b where a.goods_pid=b.goods_pid  ";
//		sql= sql+" and a.imgflag=b.img_flag and b.mark_flag in(2,3) and b.flag=3 and b.handle_flag=0 ";
		
//		String sql = "select count(*) as maxCount from alicachedatanew1_1 a, ali_goods_source_new_hx b where a.goods_pid=b.goods_pid and a.tbflag=1 and a.flag=0 ";
		
//		String sql = "select count(*) as maxCount from alicachedatanew1_1 where tbflag=1 and flag=0 ";
		String sql = "";
		//amazon
		if(flag==1){
			sql = "select count(*) as maxCount from alicachedatanew1_1 a where a.tbflag=1 and a.flag=0 and a.user_name='"+userName+"' and a.shelf_flag=2 ";
		}else if(flag==4){
			sql = "select count(*) as maxCount from alicachedatanew1_1 a where a.tbflag=1 and a.flag=0 and a.user_name='"+userName+"' and a.shelf_flag=4 ";
		}else{
			sql = "select count(*) as maxCount from alicachedatanew1_1 a inner join ali_goods_source_new_hx b on a.goods_pid=b.goods_pid where a.tbflag=1 and a.flag=0 and a.user_name='"+userName+"' and a.shelf_flag=1 ";
		}
		
		
		Connection conn = DBHelper.getInstance().getConnection5();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	
	@Override
	public int getErrorInfoCount(String userId,String timeFrom,String timeTo,int valid) {
		
		String sql = "select count(1) as maxCount from error_info a ";
		sql =sql +"LEFT JOIN user b on a.user_id=b.id ";
	    //sql= sql+"and b.email  not  like  'test%' and user_id<>1128 and  b.email  not  like  '%qq.com' and  b.email  not  like  '%163.com' ";
	   // sql= sql+"and b.email  not  like  'Xielulu1026%'   and b.email  not  like  'lifangha740%'  ";
	   // sql= sql+"and b.email  not  like  'jackluo666@aliyun.com'   and b.email  not  like  'Jennyblack1982@hotmail.com'   ";
	    //sql= sql+"and b.email  not  like  '789@222.com'  where 1=1 ";
		sql+=" where (b.is_test IS NULL OR b.is_test =0)  ";
		if(!"".equals(userId) && userId!=null){
			if ("-1".equals(userId)) {
				sql+=" and a.user_id !=0 ";
			}else {
				sql =sql+" and a.user_id = '"+userId+"' ";	
			}
		   }
		   if(!"".equals(timeFrom) && timeFrom!=null){
			   sql =sql+" and a.create_time >= '"+timeFrom+"' ";
		   }
		   if(!"".equals(timeTo) && timeTo!=null){
			   sql =sql+" and a.create_time <= '"+timeTo+"' ";
		   }
		   if (valid==10) {
			   sql="SELECT count(1) as maxCount FROM (SELECT * FROM "
		                  +"(SELECT c.id,c.create_time,c.ip,c.pid,c.url,c.user_id,c.valid,c.email,c.is_test,COUNT(c.pid) as count FROM"
		                  +"(SELECT a.id,a.create_time,a.ip,a.pid,a.url,a.user_id,a.valid,b.email,b.is_test from error_info  a "
		                  +" LEFT JOIN `user`  b "
						   	+"	ON a.user_id=b.id  "
						   	+"	  )  c WHERE c.valid=10 AND pid regexp '^[0-9]+$'  AND (c.is_test IS NULL OR c.is_test =0)";
						   	if(!"".equals(userId) && userId!=null){
								   
								   if ("-1".equals(userId)) {
										sql+=" and c.user_id !=0 ";
									}else {
										sql =sql+" and c.user_id = '"+userId+"' ";	
									}
							   }
							   if(!"".equals(timeFrom) && timeFrom!=null){
								   sql =sql+" and c.create_time >= '"+timeFrom+"' ";
							   }
							   if(!"".equals(timeTo) && timeTo!=null){
								   sql =sql+" and c.create_time <= '"+timeTo+"' ";
							   }
						   	sql+= " GROUP BY c.pid ) h WHERE h.count >2) as l " 

		                      +" LEFT JOIN (SELECT f.unsellableReason,f.pid,f.cur_time,um.unsellableReason_name FROM custom_benchmark_ready as f LEFT JOIN "
		                   +" unsellablereason_master as um ON f.unsellableReason=um.unsellableReason_id where um.unsellableReason_name is NOT NULL ) AS d on l.pid=d.pid  where d.unsellableReason_name is NOT NULL";
							
							
		   }
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	
	@Override
	public int getLireImgCount(String userName,int flag) {
		
//		String sql = "select count(*) as maxCount ";
//		sql= sql+" from core_cache_data a,ali_info_data b where a.goods_pid=b.goods_pid  ";
//		sql= sql+" and a.imgflag=b.img_flag and b.mark_flag in(2,3) and b.flag=3 and b.handle_flag=0 ";
		
//		String sql = "select count(*) as maxCount from alicachedatanew1_1 a, ali_goods_source_new_hx b where a.goods_pid=b.goods_pid and a.tbflag=1 and a.flag=0 ";
		
//		String sql = "select count(*) as maxCount from alicachedatanew1_1 where tbflag=1 and flag=0 ";
		String sql = "select count(*) as maxCount from lire_img_custom where flag=2";
//		//amazon
//		if(flag==1){
//			sql = "select count(*) as maxCount from alicachedatanew1_1 a where a.tbflag=1 and a.flag=0 and a.user_name='"+userName+"' and a.shelf_flag=2 ";
//		}else if(flag==4){
//			sql = "select count(*) as maxCount from alicachedatanew1_1 a where a.tbflag=1 and a.flag=0 and a.user_name='"+userName+"' and a.shelf_flag=4 ";
//		}else{
//			sql = "select count(*) as maxCount from alicachedatanew1_1 a inner join ali_goods_source_new_hx b on a.goods_pid=b.goods_pid where a.tbflag=1 and a.flag=0 and a.user_name='"+userName+"' and a.shelf_flag=1 ";
//		}
		
		
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	
	
	@Override
	public int getFactoryCount(String cid,String shopId) {
		
		String sql = "select count(distinct shop_id) as maxCount from custom_benchmark_ready where valid=1";

		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getShelfCount() {
		
		String sql = "select (select count(ali_info_data.1688_pid) sl  from ali_info_data,ali_info_data_all where  ali_info_data.1688_pid=ali_info_data_all.1688_pid  and ali_info_data.shelf_flag=1 and ali_info_data_all.shelf_flag=0 )  core_sum, "
				+ " (select count(*) from ali_info_data_all where shelf_flag=1 and nobench_flag=2 and source_pro_flag=1 and result=0) shop_sum "
				+ "from ali_info_data limit 1";
		
		Connection conn = DBHelper.getInstance().getConnection5();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("core_sum")+rs.getInt("shop_sum");
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
		return mCount;
	}
	
	@Override
	public int getTbCount1(String categoryId) {
		
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>=0 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=5";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount2(String categoryId) {
		
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>5 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=10";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount3(String categoryId) {
		
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where price  not like '%-%'  and tbprice/6.2>price*0.8  and alcat like '%Jewelry%' and tburl is not null and   least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>=0 and  least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=5 ";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount4(String categoryId) {
		
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where price  not like '%-%'  and tbprice/6.2>price  and alcat like '%Jewelry%' and tburl is not null and   least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>=0 and  least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=5 ";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount5(String categoryId) {
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where  price  not like '%-%'  and tbprice/6.2<price*0.3 and alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>=0 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=5";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount6(String categoryId) {
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where  price  not like '%-%'  and tbprice/6.2<price*0.2 and alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>=0 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=5";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount7(String categoryId) {
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where  price  not like '%-%'  and tbprice/6.2<price*0.1 and alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)>=0 and least(imgcheck0,imgcheck1,imgcheck2,imgcheck3)<=5";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount8(String categoryId) {
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where  price  not like '%-%'  and alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1)>=0 and least(imgcheck0,imgcheck1)<=5 and least(tbprice,tbprice1)/6.2> price*0.8";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount9(String categoryId) {
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where  price  not like '%-%'  and alcat like '%Jewelry%' and tburl is not null and least(imgcheck0,imgcheck1,imgcheck2)>=0 and least(imgcheck0,imgcheck1,imgcheck2)<=5 and  least(tbprice,tbprice1,tbprice1)/6.2> price*0.8";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int getTbCount10(String categoryId) {
		//TODO
		String sql ="select count(*) as maxCount from goodsdatacheck where  price  like '%-%'  and alcat like '%Jewelry%' and tburl is not null";
		Connection conn = DBHelper.getInstance().getConnection();
		int mCount=0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				mCount = rs.getInt("maxCount");
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
		return mCount;
	}
	
	@Override
	public int updateImgUrlValid(int id) {
		
		String sql = "update goodsdata_far set valid='9' where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
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
	public int updateStyle(int id,String style,String styJosn) {
		
		String sql ="";
		if("1".equals(style)){
			sql = "update goodsdatacheck set style0=? where id = ?";
		}else if("2".equals(style)){
			sql = "update goodsdatacheck set style1=? where id = ?";
		}else if("3".equals(style)){
			sql = "update goodsdatacheck set style2=? where id = ?";
		}else if("4".equals(style)){
			sql = "update goodsdatacheck set style3=? where id = ?";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,styJosn);
			stmt.setInt(2, id);
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
	public int insertChangeGoodsLog(String pUrl, String goodsType, String name, String price, String goodsCarId,String admName) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql="";
		String orderid="";
		String old_odid="";
		String old_goodsid="";
		String old_url="";
		String old_goodsPrice="";
		String new_url=pUrl;
		String new_goodsPrice="";
		int row=0;
		try{
			//查询订单号和新的odid
			sql="select orderno,goodid,chagoodprice from changegooddata where  chagoodurl = ? and goodscarid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pUrl);
			stmt.setString(2, goodsCarId);
			rs=stmt.executeQuery();
			if(rs.next()){
				orderid=rs.getString("orderno");
				new_goodsPrice=rs.getString("chagoodprice");
			}
			if(StringUtil.isNotBlank(orderid) && StringUtil.isNotBlank(goodsCarId)){
				//查询替换前的信息
				sql="select car_url,yourorder,goodsprice,goodsid,id from order_details where orderid=? and goodsid=?";
				stmt=conn.prepareStatement(sql);
				stmt.setString(1,orderid);
				stmt.setString(2,goodsCarId);
				rs=stmt.executeQuery();
				if(rs.next()){
					old_goodsid=rs.getString("goodsid");
					old_odid=rs.getString("id");
					old_url=rs.getString("car_url");
					old_goodsPrice=rs.getString("goodsprice");
				}
			}
//			if(StringUtil.isNotBlank(new_odid)){
//				//查询替换后的order_details信息
//				sql="select car_url,yourorder,goodsprice,goodsid from order_details where id=?";
//				stmt=conn.prepareStatement(sql);
//				stmt.setString(1,new_odid);
//				rs=stmt.executeQuery();
//				if(rs.next()){
//					new_goodsid=rs.getString("goodsid");
//					new_goodsPrice=rs.getString("goodsprice");
//				}
//			}
			sql="insert into ChangeGoodsLog(orderid,old_odid,old_goodsid,old_url,old_goodsPrice,new_url,new_goodsPrice,admName,createtime) values" +
					"(?,?,?,?,?,?,?,?,now())";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,orderid);
			stmt.setString(2,old_odid);
			stmt.setString(3,old_goodsid);
			stmt.setString(4,old_url);
			stmt.setString(5,old_goodsPrice);
			stmt.setString(6,new_url);
			stmt.setString(7,new_goodsPrice);
			stmt.setString(8,admName);
			row=stmt.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public int updateChangeType(String pUrl,String goodsType,String name,String price,String goodsCarId) {
		
		String sql ="update changegooddata set goodstype=?,chagoodname=?,chagoodprice=? where chagoodurl = ? and goodscarid=?";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,goodsType);
			stmt.setString(2, name);
			stmt.setString(3,price);
			stmt.setString(4, pUrl);
			stmt.setString(5, goodsCarId);
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
	public int updateSourceFlag(String url,int flag) {
		
		String sql = "";
		String sql2 = "";
		if(flag==0){
			//无货
			sql ="update importex.ali_search_cache_datas set source_flag=0,source_time=now() where goods_url = ?";
		}else if(flag ==1 ){
			//有货
			sql ="update importex.ali_search_cache_datas set source_flag=1,source_time=now() where goods_url = ?";
			//保存货源表
			sql2 ="insert into goods_source_cache(url,goods_url,goods_img,goods_price)  "
						  +"select url,tburl,tbimg,tbprice from importex.goodsdatacheckcache "
						  +" where url = ? ";
		}
		//有处理完成标志
		String sql1 ="update importex.goodsdatacheckcache set delflag=1 where url = ?";

		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null,stmt1 = null,stmt2 = null;
		int res = 0,res1 = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,url);
			res = stmt.executeUpdate();
			
			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1,url);
			res1 = stmt1.executeUpdate();
			if(flag ==1 ){
				stmt2 = conn.prepareStatement(sql2);
				stmt2.setString(1,url);
				stmt2.executeUpdate();
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
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	
	
	@Override
	public int updateLireFlag(String pid) {
		
		String sql ="update lire_img_custom set flag=1 where sor_pid = ?";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,pid);
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
	public int updateTbName(String goodsId,String tbName1,String tbName2,String tbName3,String tbName4) {
		
		String sql ="update goodsdataCheck set tbname=?,tbname1=?,tbname2=?,tbname3=? where id = ?";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,tbName1);
			stmt.setString(2, tbName2);
			stmt.setString(3,tbName3);
			stmt.setString(4, tbName4);
			stmt.setInt(4, Integer.parseInt(goodsId));
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
	public int updateSimilarity(int pId,int difference,int flag) {

		String sql = "";
		if(flag==0){
			sql = "update importex.goodsdatacheckcache set imgcheck0=? where gcid = ?";
		}else if(flag==1){
			sql = "update importex.goodsdatacheckcache set imgcheck1=? where gcid = ?";
		}else if(flag==2){
			sql = "update importex.goodsdatacheckcache set imgcheck2=? where gcid = ?";
		}else if(flag==3){
			sql = "update importex.goodsdatacheckcache set imgcheck3=? where gcid = ?";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, difference);
			stmt.setInt(2, pId);
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
	public int cancelChangeGoodData(String orderNo) {

		String sql = "update changegooddata set delflag = 1 where orderno = ? ";

		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
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
	public int updateCgdEmailFlag(String orderNo) {

		String sql = "update orderinfo set packag_style = 2 where order_no = ? ";
//		String sql2 = "update orderinfo set packag_style = 2 where order_no = ? ";


		Connection conn = DBHelper.getInstance().getConnection();
//		Connection conn1 = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null,stmt1 = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			res = stmt.executeUpdate();
//			stmt1 = conn1.prepareStatement(sql2);
//			stmt1.setString(1, orderNo);
//			res = stmt1.executeUpdate();
			SendMQ sendMQ=new SendMQ();
			sendMQ.sendMsg(new RunSqlModel("update orderinfo set packag_style = 2 where order_no ='"+orderNo+"' "));
			sendMQ.closeConn();

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
//			DBHelper.getInstance().closeConnection(conn1);
		}
		return res;
	}
	
	@Override
	public int insertOnlineChange(String orderNo) {

		List<ChangeGoodBean> gsfList = new ArrayList<ChangeGoodBean>();
		String sql = "select * from changegooddata where orderno = ? ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ChangeGoodBean gfb = new ChangeGoodBean();
				gfb.setOrderno(rs.getString("orderno"));
				gfb.setGoodsCarId(rs.getString("goodscarid"));
				gfb.setGoodid(rs.getString("goodid"));
				gfb.setAliname(rs.getString("aliname"));
				gfb.setAliimg(rs.getString("aliimg"));
				gfb.setAliurl(rs.getString("aliurl"));
				gfb.setAliprice(rs.getString("aliprice"));
				gfb.setChagoodname(rs.getString("chagoodname"));
				gfb.setChagoodimg(rs.getString("chagoodimg"));
				gfb.setChagoodurl(rs.getString("chagoodurl"));
				gfb.setChagoodprice(rs.getString("chagoodprice"));
				gfb.setDelFlag(rs.getInt("delflag"));
				gfb.setChangeFlag(rs.getInt("changeflag"));
				gfb.setGoodsType(rs.getString("goodstype"));
				gfb.setEmailsendFlag(rs.getInt("emailsendflag"));
				gsfList.add(gfb);
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
		
		
		String sql1 = "insert into changegooddata (orderno,goodscarid,goodid,aliname,aliimg,aliurl,aliprice,chagoodname,chagoodimg,chagoodurl,chagoodprice,delflag,changeflag,goodstype,emailsendflag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		String sqlDel ="delete from changegooddata where orderno=? ";
//		Connection conn1 = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt0=null,stmt1 = null;
		int res = 0;
		try {
//			stmt0 = conn1.prepareStatement(sqlDel);
//			stmt0.setString(1, orderNo);
//			res = stmt0.executeUpdate();
			SendMQ sendMQ=new SendMQ();
			sendMQ.sendMsg(new RunSqlModel("delete from changegooddata where orderno='"+orderNo+"'"));
			sendMQ.closeConn();

//			for(int i=0; i<gsfList.size(); i++){
//				stmt1 = conn1.prepareStatement(sql1);
//				stmt1.setString(1, gsfList.get(i).getOrderno());
//				stmt1.setInt(2, Integer.valueOf(gsfList.get(i).getGoodsCarId()));
//				stmt1.setInt(3, Integer.valueOf(gsfList.get(i).getGoodid()));
//				stmt1.setString(4, gsfList.get(i).getAliname());
//				stmt1.setString(5, gsfList.get(i).getAliimg());
//				stmt1.setString(6, gsfList.get(i).getAliurl());
//				stmt1.setString(7, gsfList.get(i).getAliprice());
//				stmt1.setString(8, gsfList.get(i).getChagoodname());
//				stmt1.setString(9, gsfList.get(i).getChagoodimg());
//				stmt1.setString(10, gsfList.get(i).getChagoodurl());
//				stmt1.setString(11, gsfList.get(i).getChagoodprice());
//				stmt1.setInt(12, Integer.valueOf(gsfList.get(i).getDelFlag()));
//				stmt1.setInt(13, Integer.valueOf(gsfList.get(i).getChangeFlag()));
//				stmt1.setString(14, gsfList.get(i).getGoodsType());
//				stmt1.setInt(15, Integer.valueOf(gsfList.get(i).getEmailsendFlag()));
//				res = stmt1.executeUpdate();
//			}

			// 添加新数据
			List<String> sqlList = new ArrayList<String>();
			StringBuffer sqlStr;
			for (int i=0; i<gsfList.size(); i++) {
				sqlStr = new StringBuffer();
				sqlStr.append("insert into changegooddata (orderno,goodscarid,goodid,aliname,aliimg,aliurl,aliprice,chagoodname,chagoodimg,chagoodurl,chagoodprice,delflag,changeflag,goodstype,emailsendflag) values(")
						.append("\'").append(gsfList.get(i).getOrderno()).append("\', ")
						.append("\'").append(gsfList.get(i).getGoodsCarId()).append("\', ")
						.append("\'").append(gsfList.get(i).getGoodid()).append("\', ")
						.append("\'").append(SendMQ.repCha(gsfList.get(i).getAliname())).append("\', ")
						.append("\'").append(gsfList.get(i).getAliimg()).append("\', ")
						.append("\'").append(gsfList.get(i).getAliurl()).append("\', ")
						.append("\'").append(gsfList.get(i).getAliprice()).append("\', ")
						.append("\'").append(SendMQ.repCha(gsfList.get(i).getChagoodname())).append("\', ")
						.append("\'").append(gsfList.get(i).getChagoodimg()).append("\', ")
						.append("\'").append(gsfList.get(i).getChagoodurl()).append("\', ")
						.append("\'").append(gsfList.get(i).getChagoodprice()).append("\', ")
						.append("\'").append(gsfList.get(i).getDelFlag()).append("\', ")
						.append("\'").append(gsfList.get(i).getChangeFlag()).append("\', ")
						.append("\'").append(SendMQ.repCha(gsfList.get(i).getGoodsType())).append("\', ")
						.append("\'").append(gsfList.get(i).getEmailsendFlag()).append("\');");
				sqlList.add(sqlStr.toString());

			}
			RunBatchSqlModel sqlModel = new RunBatchSqlModel();//存储待更新线上数据 mq方式更新
			sqlModel.setSqls(sqlList);
			SendMQ.sendMqSql(sqlModel);


			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt0 != null) {
//				try {
//					stmt0.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (stmt != null) {
//				try {
//					stmt1.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	
	@Override
	public int saveTbGood(TbGoodBean tbGoodBean) {
		String sql = "insert goodsdatacheckhot01_1(goods_pid,url,shop_id_temp,tbflag) values(?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, Integer.valueOf(tbGoodBean.getGoodId()));
			stmt.setString(1, tbGoodBean.getGoodId());
			stmt.setString(2, tbGoodBean.getTbUrl());
			stmt.setString(3, tbGoodBean.getShopId());
			stmt.setInt(4, 0);
//			stmt.setString(5, tbGoodBean.getTbFlag());
//			stmt.setString(6, tbGoodBean.getTbImg());
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
	public int saveDbYlGood(TbGoodBean tbGoodBean) {
		String sql = "insert ali_info_data(goods_pid,1688_url,1688_pid,shop_id,1688_price,1688_cat,mark_flag,bm_flag,priority_flag,source_pro_flag,1688_img,product_flag) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getGoodId());
			stmt.setString(2, tbGoodBean.getYlUrl());
			stmt.setString(3, tbGoodBean.getYlPid());
			stmt.setString(4, tbGoodBean.getShopId());
			stmt.setString(5, tbGoodBean.getYlPrice());
			stmt.setString(6, tbGoodBean.getCatId());
			stmt.setInt(7, 1);
			stmt.setInt(8, 1);
			stmt.setInt(9, tbGoodBean.getPriorityFlag());
			stmt.setInt(10, tbGoodBean.getSourceProFlag());
			stmt.setString(11, tbGoodBean.getYlImg());
			stmt.setInt(12, tbGoodBean.getPidSource());
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
		//更新ali信息
		updateAliInfo(tbGoodBean.getGoodId());
	 return res;
	}
	
	@Override
	public int saveDbLireGood(TbGoodBean tbGoodBean) {
		String sql = "";
		if(tbGoodBean.getDelFlag()==6){
			sql = "insert lire_same(pid_src,pid_same) values(?,?)";
		}else{
			sql = "insert lire_goods(pid,flag) values(?,?)";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			if(tbGoodBean.getDelFlag()==6){
				stmt.setString(1, tbGoodBean.getGoodId());
				stmt.setString(2, tbGoodBean.getYlPid());
			}else{
				stmt.setString(1, tbGoodBean.getGoodId());
				stmt.setInt(2, tbGoodBean.getDelFlag());
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
	
	
	/**更新数据alidata
	 * @author abc
	 * @param
	 * @param
	 */
	public int updateAliInfo(String pid){
		int result = 0;
		Connection conn = DBHelper.getInstance().getConnection5();
		String updateSql = "update ali_info_data a ,ali_goods_source_new_hx b "
						+"set a.goods_name=b.goods_name,a.goods_price=b.goods_price,a.price_min=b.price_min,a.goods_img=b.goods_img,"
						+" a.goods_url=CONCAT('https://www.aliexpress.com/item/2017-Spring-Oxfords-Shoes-For-Women-Platform-Lace-Up-Star-reepers-Women-s-Oxfords-Shoes-Casual/',b.goods_pid,'.html'),"
//						+" a.goods_width=b.goods_width,a.goods_method=b.goods_method,a.goods_posttime=b.goods_posttime,"
						+"a.goods_weight=b.goods_weight,a.goods_sellunits=b.goods_sellunits,a.pUtil=b.pUtil,a.goods_freight=b.goods_freight,"
						+"a.goods_moq=b.goods_moq,a.goods_sold=b.goods_sold, "
						+"a.ali_catid1=b.catid1,a.ali_catid2=b.catid2,a.ali_catid3=b.catid3,a.ali_catid4=b.catid4,a.ali_catid5=b.catid5,a.ali_catid6=b.catid6 "
						+"where a.goods_pid = b.goods_pid and b.goods_pid=?";
		PreparedStatement errorstmt = null;
		try {
			errorstmt = conn.prepareStatement(updateSql);
			errorstmt.setString(1, pid);
			result = errorstmt.executeUpdate();
			//更新amazon
			if(result == 0){
				updateAmazonInfo(pid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				errorstmt.close();
				DBHelper.getInstance().closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return result ;
	}
	
	public int updateAmazonInfo(String pid){
		int result = 0;
		Connection conn = DBHelper.getInstance().getConnection5();
		String updateSql = "update ali_info_data a ,alicachedatanew1_1 b "
						+"set a.goods_price=b.goods_price "
						+"where a.goods_pid = b.goods_pid and a.goods_pid=?";
		PreparedStatement errorstmt = null;
		try {
			errorstmt = conn.prepareStatement(updateSql);
			errorstmt.setString(1, pid);
			result = errorstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				errorstmt.close();
				DBHelper.getInstance().closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return result ;
	}
	

	@Override
	public int insertLireSearchData(List<GoodsCheckBean> cgbList){
		String sql = "insert lire_search_data(offer_id,source_img,source_url,new_img,new_url,score,new_price,new_name,type_id,type_name) values(?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			for(int i=0; i<cgbList.size(); i++){
				//产品Id
				stmt.setLong(1, cgbList.get(i).getOfferId());
				//原图片路径
				stmt.setString(2, cgbList.get(i).getSourceImg());
				//原url
				stmt.setString(3, cgbList.get(i).getSourceUrl());
				//新图片路径
				stmt.setString(4, cgbList.get(i).getNewImg());
				//新url
				stmt.setString(5, cgbList.get(i).getNewUrl());
				//相似度
				stmt.setDouble(6, cgbList.get(i).getScore());
				//新价格
				stmt.setString(7, cgbList.get(i).getNewPrice());
				//新名字
				stmt.setString(8, cgbList.get(i).getNewName());
				//类别Id
				stmt.setInt(9, cgbList.get(i).getCatId());
				//类别名
				stmt.setString(10, cgbList.get(i).getCatName());
				res = stmt.executeUpdate();
				System.out.println("lire_search_data.insert="+i);
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
	 return res;
	}
	
	
	@Override
	public int saveChangeGood(ChangeGoodBean chaGoodBean) {
		//delChangeGood(chaGoodBean);
		upChangeDataGood(chaGoodBean);
		String sql = "insert into changegooddata (orderno,goodid,aliname,aliimg,aliurl,aliprice,chagoodname,chagoodimg,chagoodurl,chagoodprice,goodscarid) values(?,?,?,?,?,?,?,?,?,?,?) ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, chaGoodBean.getOrderno());
			stmt.setInt(2, Integer.valueOf(chaGoodBean.getGoodid()));
			stmt.setString(3, chaGoodBean.getAliname());
			stmt.setString(4, chaGoodBean.getAliimg());
			stmt.setString(5, chaGoodBean.getAliurl());
			stmt.setString(6, chaGoodBean.getAliprice());
			stmt.setString(7, chaGoodBean.getChagoodname());
			stmt.setString(8, chaGoodBean.getChagoodimg());
			stmt.setString(9, chaGoodBean.getChagoodurl());
			stmt.setString(10, chaGoodBean.getChagoodprice());
			stmt.setInt(11, Integer.valueOf(chaGoodBean.getGoodsCarId()));
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
	public TbGoodBean getTbGood(String goodId) {
		String sql = "select goods_pid from goodsdatacheckhot01_1 where goods_pid = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		TbGoodBean tbGoodBean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodId);
			rs = stmt.executeQuery();
			if(rs.next()){
				tbGoodBean = new TbGoodBean();
				tbGoodBean.setGoodId(goodId);
//				tbGoodBean.setTbUrl(rs.getString("tburl"));
//				tbGoodBean.setTbPrice(rs.getString("tbprice"));
//				tbGoodBean.setTbName(rs.getString("tbname"));
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
		return tbGoodBean;
	}
	
	@Override
	public TbGoodBean getDbYlGood(String goodId) {
		String sql = "select goods_pid from ali_info_data where goods_pid = ?";
		Connection conn = DBHelper.getInstance().getConnection5();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		TbGoodBean tbGoodBean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodId);
			rs = stmt.executeQuery();
			if(rs.next()){
				tbGoodBean = new TbGoodBean();
				tbGoodBean.setGoodId(goodId);
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
		return tbGoodBean;
	}
	
	@Override
	public int getDbLireGood(TbGoodBean tbGoodBean) {
		
		String sql = "";
		if(tbGoodBean.getDelFlag()==6){
			sql = "select pid_same from lire_same where pid_same = ?";
		}else{
			sql = "select pid from lire_goods where pid = ?";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int count =0;
//		TbGoodBean tbGoodBean = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(tbGoodBean.getDelFlag()==6){
				stmt.setString(1, tbGoodBean.getYlPid());
			}else{
				stmt.setString(1, tbGoodBean.getGoodId());
			}
			
			rs = stmt.executeQuery();
			if(rs.next()){
//				tbGoodBean = new TbGoodBean();
//				if(tbGoodBean.getDelFlag()==6){
//					tbGoodBean.setGoodId(rs.getString("pid_same"));
//				}else{
//					tbGoodBean.setGoodId(rs.getString("pid"));
//				}
				count=1;
				
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
		return count;
	}
	
	
	@Override
	public ChangeGoodBean getChangeGoodData(String orderNo, String chaGoodUrl) {
		String sql = "select orderno from changegooddata where orderno = ? and chagoodurl =? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ChangeGoodBean chaGoodBean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setString(2, chaGoodUrl);
			rs = stmt.executeQuery();
			if(rs.next()){
				chaGoodBean = new ChangeGoodBean();
				chaGoodBean.setOrderno(orderNo);
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
		return chaGoodBean;
	}
	
	
	@Override
	public GoodsCheckBean getGoodsDataCheck(int pId, String tbId) {
		String sql = "select id,tburl,tbprice,tbname,tbimg,tburl1,tbprice1,tbname1,tbimg1,tburl2,tbprice2,tbname2,tbimg2,tburl3,tbprice3,tbname3,tbimg3 from goodsdatacheck where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		GoodsCheckBean goodsCheckBean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, pId);
			rs = stmt.executeQuery();
			if(rs.next()){
				goodsCheckBean = new GoodsCheckBean();
				goodsCheckBean.setpId(pId);
				if("tb1".equals(tbId)){
					goodsCheckBean.setTbUrl(rs.getString("tburl"));
					goodsCheckBean.setTbprice(rs.getString("tbprice"));
					goodsCheckBean.setTbName(rs.getString("tbname"));
					goodsCheckBean.setTbImg(rs.getString("tbimg"));
				}else if("tb2".equals(tbId)){
					goodsCheckBean.setTbUrl1(rs.getString("tburl1"));
					goodsCheckBean.setTbprice1(rs.getString("tbprice1"));
					goodsCheckBean.setTbName1(rs.getString("tbname1"));
					goodsCheckBean.setTbImg1(rs.getString("tbimg1"));
				}else if("tb3".equals(tbId)){
					goodsCheckBean.setTbUrl2(rs.getString("tburl2"));
					goodsCheckBean.setTbprice2(rs.getString("tbprice2"));
					goodsCheckBean.setTbName2(rs.getString("tbname2"));
					goodsCheckBean.setTbImg2(rs.getString("tbimg2"));
				}else if("tb4".equals(tbId)){
					goodsCheckBean.setTbUrl3(rs.getString("tburl3"));
					goodsCheckBean.setTbprice3(rs.getString("tbprice3"));
					goodsCheckBean.setTbName3(rs.getString("tbname3"));
					goodsCheckBean.setTbImg3(rs.getString("tbimg3"));
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
		return goodsCheckBean;
	}
	
	@Override
	public List<ChangeGoodBean> findChangeGoodsInfo(String orderNo, int flag){
		
		List<ChangeGoodBean> gsfList = new ArrayList<ChangeGoodBean>();
		String sql = "select orderno,goodid,goodscarid,aliname,aliimg,aliurl,aliprice,chagoodname,chagoodimg,chagoodurl,chagoodprice,GREATEST(aliprice,chagoodprice) as maxprice,od.car_type as goods_type,od.yourorder,od.userid,u.email ";
		sql = sql +",chagoodimg,goodstype  ";
//		if(flag!=1){
//			sql = sql +",gd.img as gdimg  ";
//		}
		
		//sql = sql +"from changegooddata inner join goods_car gc on changegooddata.goodscarid=gc.id  inner join  order_details od on changegooddata.orderno = od.orderid and changegooddata.goodscarid=od.goodsid ";
		sql = sql +"from changegooddata  inner join  order_details od on changegooddata.orderno = od.orderid and changegooddata.goodscarid=od.goodsid ";
		sql = sql +"inner join user u on od.userid=u.id  ";
//		if(flag!=1){
//			sql = sql +"inner join goodsdata gd on changegooddata.chagoodurl = gd.url  ";
//		}
		sql = sql +" where delflag=0 and orderno in ("+orderNo+")";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
			
				ChangeGoodBean changeGoodBean = new ChangeGoodBean();
				changeGoodBean.setOrderno(rs.getString("orderno"));
				changeGoodBean.setGoodid(rs.getString("goodid"));
				changeGoodBean.setGoodsCarId(rs.getString("goodscarid"));
				changeGoodBean.setChagoodurl(rs.getString("chagoodurl"));
				changeGoodBean.setChagoodname(rs.getString("chagoodname"));
				changeGoodBean.setAliimg(rs.getString("aliimg"));
				changeGoodBean.setAliname(rs.getString("aliname"));
				changeGoodBean.setAliprice(rs.getString("aliprice"));
				changeGoodBean.setChagoodprice(rs.getString("chagoodprice"));
				String maxprice = rs.getString("maxprice");
				double mp = Double.parseDouble(maxprice);
				changeGoodBean.setMaxprice(new BigDecimal(mp).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
				changeGoodBean.setGoodsType(rs.getString("goods_type"));
				changeGoodBean.setQuantity(rs.getString("yourorder"));
				changeGoodBean.setUserId(rs.getString("userid"));
				changeGoodBean.setEmail(rs.getString("email"));
				
//				if(flag!=1){
//					if(rs.getString("chagoodimg")!=null&&!rs.getString("chagoodimg").isEmpty()){
//						changeGoodBean.setChagoodimg(rs.getString("chagoodimg"));
//					}else{
//						String gdimg = rs.getString("gdimg");
//						if(gdimg!=null && !gdimg.isEmpty()){
//							gdimg = gdimg.replace("[", "").replace("]", "").trim();
//							String[] gdimgs = gdimg.split(",\\s+");
//							if(gdimgs[0].indexOf(".jpg")>1 || gdimgs[0].indexOf(".png")>1 || gdimgs[0].indexOf(".gif")>1){
//								changeGoodBean.setChagoodimg(gdimgs[0]);
//							}else{
//								changeGoodBean.setChagoodimg(gdimgs[0]+"jpg");
//							}
//						}
//					}
//				}else{
					changeGoodBean.setChagoodimg(rs.getString("chagoodimg"));
//				}
				
				changeGoodBean.setGoodstype1(rs.getString("goodstype"));
				
				gsfList.add(changeGoodBean);
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
		return gsfList;
	}
	
	@Override
	public List<GoodsCheckBean> searchDownLoadInfo(List<CustomOrderBean> picIdList, String type, int goodsDataId, String aliUrl, String orderNo){
		
		//更新图片路径
		this.upDownLoadImgPath(picIdList);
		
		List<GoodsCheckBean> tbFtList = new ArrayList<GoodsCheckBean>();
		String sql ="select imgPath,offerId,unitPrice,subject from download_csv where MATCH(imageList)  AGAINST(? IN BOOLEAN MODE) ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		
		try {
			for(int i=0; i<picIdList.size();i++){
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, picIdList.get(i).getImg());
				rs = stmt.executeQuery();
				while (rs.next()) {
					GoodsCheckBean gfb = new GoodsCheckBean();
					gfb.setOrderNo(orderNo);
					gfb.setAligSourceUrl(aliUrl);
					gfb.setGoodsDataId(goodsDataId);
//					gfb.setTbImg("http://192.168.0.18"+rs.getString("imgPath")+"/"+picIdList.get(i).getImg()+".jpg");
					gfb.setTbImg("http://192.168.1.26"+rs.getString("imgPath"));
					gfb.setTbUrl("https://detail.1688.com/offer/"+rs.getString("offerId")+".html");
					gfb.setTbprice(rs.getString("unitPrice"));
//					String subject = 
					gfb.setTbName(rs.getString("subject").replace("\"", ""));
					gfb.setScore(picIdList.get(i).getScore());
					
					
					tbFtList.add(gfb);
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
		
		return tbFtList;
	}
	
	@Override
	public List<ceRong.tools.bean.GoodsCheckBean> getDownLoadCsvInfo(List<ceRong.tools.bean.CustomOrderBean> picIdList){
		
//		//更新图片路径
//		this.upDownLoadImgPath(picIdList);
		
		List<ceRong.tools.bean.GoodsCheckBean> tbFtList = new ArrayList<ceRong.tools.bean.GoodsCheckBean>();
		String sql ="select offerId,unitPrice,subject from download_csv where MATCH(imageList)  AGAINST(? IN BOOLEAN MODE) ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		
		try {
			for(int i=0; i<picIdList.size();i++){
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, picIdList.get(i).getImgName());
				rs = stmt.executeQuery();
				while (rs.next()) {
					ceRong.tools.bean.GoodsCheckBean gfb = new ceRong.tools.bean.GoodsCheckBean();
					//产品Id
					gfb.setOfferId(rs.getLong("offerId"));
					//原图片路径
					gfb.setSourceImg(picIdList.get(i).getSourceImg());
					//原url
					gfb.setSourceUrl(picIdList.get(i).getSourceUrl());
					//新图片路径
					gfb.setNewImg(picIdList.get(i).getNewImg().replace("\\", "/"));
					//新url
					gfb.setNewUrl("https://detail.1688.com/offer/"+rs.getString("offerId")+".html");
					//相似度
					gfb.setScore(picIdList.get(i).getScore());
					//新价格
					gfb.setNewPrice(rs.getDouble("unitPrice"));
					//新名字
					gfb.setNewName(rs.getString("subject").replace("\"", ""));
					//类别Id
					gfb.setCatId(picIdList.get(i).getCatId());
					//类别名
					gfb.setCatName(picIdList.get(i).getCatName());
					
					tbFtList.add(gfb);
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
		System.out.println("tbFtList.size="+tbFtList.size());
		return tbFtList;
	}
	
	//大类索引取得
	public List<DorpDwonBean> getLargeIndexInfo(){
		
		String sql = "select indexName,indexNameCn from importcsv.index_large ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		List<DorpDwonBean> logisticsList = new ArrayList<DorpDwonBean>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DorpDwonBean bean = new DorpDwonBean();
				bean.setIndexName(rs.getString("indexName"));
				bean.setIndexNameCn(rs.getString("indexNameCn"));
//				bean.setCreateTime(rs.getString("createTime"));
//				bean.setNameTime(rs.getString("indexName")+" "+rs.getString("createTime"));
				logisticsList.add(bean);
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
		return logisticsList;
	}
	
	
	
	
	public int upDownLoadImgPath(List<CustomOrderBean> picIdList) {
		
		String sql ="update  download_csv,( select  offerId from  download_csv   where MATCH(imageList)  AGAINST(?   IN BOOLEAN MODE)   )  b    set download_csv.imgPath  = ? ";
		sql = sql +"where download_csv.offerId=b.offerId";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			for(int i=0; i<picIdList.size();i++){
				
				stmt.setString(1, picIdList.get(i).getImg());
				stmt.setString(2, picIdList.get(i).getPurl());
				res = stmt.executeUpdate();
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
	 return res;
	}
	
	@Override
	public int updateDownLoadInfo(List<CustomOrderBean> picIdList) {
		
		String sql ="update  download_csv,( select  offerId from  download_csv   where MATCH(imageList)  AGAINST(?   IN BOOLEAN MODE)   )  b    set download_csv.imgPath  = ? ";
		sql = sql +"where download_csv.offerId=b.offerId";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			for(int i=0; i<picIdList.size();i++){
				
				stmt.setString(1, picIdList.get(i).getImg());
				stmt.setString(2, picIdList.get(i).getPurl());
				res = stmt.executeUpdate();
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
	 return res;
	}
	
	
	@Override
	public GoodsCheckBean getChangeGoodData(List<Map<String,String>> listData) {
//		this.updateOrderProduceSource(orderNo,goodscarid,goodsId,od_id,importUrl);
//		this.updateOrderInfoFlag(orderNo);
//		this.updateGoodsSourceChaFlag(importUrl,changeFlag);

		

		Connection conn = DBHelper.getInstance().getConnection();
//		Connection conn1 = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt0 = null,stmt = null,stmt1 = null,stmt2 = null,stmt3 = null;
		GoodsCheckBean goodsCheckBean = null;
		
		PreparedStatement stmtfind = null;
		PreparedStatement stmtProduceSource = null;
		ResultSet rsProduceSource = null;
		int res = 0;
		try {
			
			String sqlDel ="delete from changegooddata where orderno=? ";
			stmt0 = conn.prepareStatement(sqlDel);
			stmt0.setString(1, (String)listData.get(0).get("orderNo"));
			res = stmt0.executeUpdate();
			
	        for(int i=0; i<listData.size(); i++){
	        	Map<String,String> map = listData.get(i);
	    		String orderNo = (String)map.get("orderNo");
	    		String importUrl = (String)map.get("importUrl");
	    		String goodsIdItem = (String)map.get("goodsId");
	    		//String changeFlag = (String)TypeUtils.modefindUrl(map.get("changeFlag"),1);
	    		String changeFlag = (String)map.get("changeFlag");
	    		String priceRmb = (String)map.get("priceRmb");
	    		//orderNos = (String)map.get("orderNo");
	    		
	    		//商品id
	    		int goodsId = Integer.valueOf(goodsIdItem.split("#")[0]);
	    		String goodsdataId = goodsIdItem.split("#")[1];
	    		int od_id = Integer.parseInt(goodsIdItem.split("#")[2]);
	    		
	    		int goodscarid = Integer.valueOf(goodsdataId);
	        
	        //getChangeGoodData(edit,orderNo,importUrl,Integer.valueOf(goodsId),changeFlag,Integer.valueOf(goodsdataId),od_id);
	        //getChangeGoodData(List<Map<String,String>> listData,String orderNo,String importUrl,int goodsId,String changeFlag,int goodscarid,int od_id) 
	    		//HomTU  ADD 
				String sqlfind = "select count(id) as sum from order_product_source where orderid='"+orderNo+"' and goodsid="+goodscarid;
				stmtfind = conn.prepareStatement(sqlfind);
				rsProduceSource = stmtfind.executeQuery();
				if(rsProduceSource.next()){
					res = rsProduceSource.getInt("sum");
				}
				if(res==0){
					String sqlProduceSource="delete from order_product_source where orderid='"+orderNo+"' and goodsid='"+goodscarid+"'";
					stmt1 = conn.prepareStatement(sqlProduceSource);
					stmt1.executeUpdate();
					sqlProduceSource="insert into order_product_source(orderid,goodsid,goodsdataid,goods_url,purchase_state,od_id,tborderid) values(?,?,?,?,12,?,'重复2') ";
					stmtProduceSource = conn.prepareStatement(sqlProduceSource);
					stmtProduceSource.setString(1,orderNo);
					stmtProduceSource.setInt(2,goodscarid);
					stmtProduceSource.setInt(3,goodsId);
					stmtProduceSource.setString(4,importUrl);
					stmtProduceSource.setInt(5,od_id);
					stmtProduceSource.executeUpdate();
				} else {
					String sqlProduceSource= "update order_product_source set purchase_state=12,od_id=? "
							+ "where orderid='"+orderNo+"' and goodsid="+goodscarid+" and del=0";
					stmtProduceSource = conn.prepareStatement(sqlProduceSource);
					stmtProduceSource.setInt(1,od_id);
					stmtProduceSource.executeUpdate();
				}
				
				//更新有替换产品标识
				String sqlChange = "update orderinfo set packag_style = 1 where order_no = ? ";
				stmt1 = conn.prepareStatement(sqlChange);
				stmt1.setString(1, orderNo);
				res = stmt1.executeUpdate();
				//更新有替换产品标识线上
//				String sqlChangeOnLine = "update orderinfo set packag_style = 1 where order_no = ? ";
//				stmt4 = conn1.prepareStatement(sqlChangeOnLine);
//				stmt4.setString(1, orderNo);
//				res = stmt4.executeUpdate();
				SendMQ sendMQ=new SendMQ();
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set packag_style = 1 where order_no ='"+orderNo+"' "));
				sendMQ.closeConn();

				//更新货源表替换产品标识
				String sqlGoods = "update goods_source set change_flag = 1 where goods_url = ? and goods_purl=? ";
				stmt2 = conn.prepareStatement(sqlGoods);
				stmt2.setString(1, importUrl);
				stmt2.setString(2, changeFlag);
				res = stmt2.executeUpdate();
				
				//changegooddata 插入
				String sql = "";
				if("tb1".equals(changeFlag) || "tb2".equals(changeFlag) || "tb3".equals(changeFlag) || "tb4".equals(changeFlag)){
					sql = sql +"select  gk.id,gk.url,gk.price,gk.goodsname,gk.imgpath,gk.tburl,gk.tbprice,gk.tbname,gk.tbimg,gk.tburl1,gk.tbprice1,gk.tbname1,gk.tbimg1, ";
					sql = sql +"gk.tburl2,gk.tbprice2,gk.tbname2,gk.tbimg2,gk.tburl3,gk.tbprice3,gk.tbname3,gk.tbimg3 ";
					sql = sql +"from goodsdatacheck gk ";
					sql = sql +"where gk.id ="+goodsId;
				}else{
					sql = sql +"select  gk.id,gk.url,gk.price,gk.goodsname,gk.imgpath,gk.tburl,gk.tbprice,gk.tbname,gk.tbimg,gk.tburl1,gk.tbprice1,gk.tbname1,gk.tbimg1, ";
					sql = sql +"gk.tburl2,gk.tbprice2,gk.tbname2,gk.tbimg2,gk.tburl3,gk.tbprice3,gk.tbname3,gk.tbimg3, ";
					sql = sql +"gs.goods_purl,gs.goods_price,gs.goods_name as chagoodsname,gs.goods_img_url ";
					sql = sql +"from  goods_source gs left join goodsdatacheck gk ";
					sql = sql +"on gs.goods_url = gk.url ";
					sql = sql +"where gs.goods_url =? ";
					sql = sql +"and gs.goods_purl =? ";
					sql = sql +"and gk.id =? ";
				}
				stmt = conn.prepareStatement(sql);
				if(!"tb1".equals(changeFlag) && !"tb2".equals(changeFlag) && !"tb3".equals(changeFlag) && !"tb4".equals(changeFlag)){
					stmt.setString(1, importUrl);
					stmt.setString(2, changeFlag);
					stmt.setInt(3, goodsId);
				}
				rs = stmt.executeQuery();
				if(rs.next()){
					goodsCheckBean = new GoodsCheckBean();
					//aliInfo
					goodsCheckBean.setUrl(rs.getString("url"));
					goodsCheckBean.setPrice(rs.getString("price"));
					goodsCheckBean.setGoodsName(rs.getString("goodsname"));
					goodsCheckBean.setImgpath(rs.getString("imgpath"));
					if("tb1".equals(changeFlag)){
						goodsCheckBean.setTbUrl(rs.getString("tburl"));
						goodsCheckBean.setTbprice(rs.getString("tbprice"));
						goodsCheckBean.setTbName(rs.getString("tbname"));
						goodsCheckBean.setTbImg(rs.getString("tbimg"));
					}else if("tb2".equals(changeFlag)){
						goodsCheckBean.setTbUrl1(rs.getString("tburl1"));
						goodsCheckBean.setTbprice1(rs.getString("tbprice1"));
						goodsCheckBean.setTbName1(rs.getString("tbname1"));
						goodsCheckBean.setTbImg1(rs.getString("tbimg1"));
					}else if("tb3".equals(changeFlag)){
						goodsCheckBean.setTbUrl2(rs.getString("tburl2"));
						goodsCheckBean.setTbprice2(rs.getString("tbprice2"));
						goodsCheckBean.setTbName2(rs.getString("tbname2"));
						goodsCheckBean.setTbImg2(rs.getString("tbimg2"));
					}else if("tb4".equals(changeFlag)){
						goodsCheckBean.setTbUrl3(rs.getString("tburl3"));
						goodsCheckBean.setTbprice3(rs.getString("tbprice3"));
						goodsCheckBean.setTbName3(rs.getString("tbname3"));
						goodsCheckBean.setTbImg3(rs.getString("tbimg3"));
					}else{
						goodsCheckBean.setGoodsPurl(rs.getString("goods_purl"));
						goodsCheckBean.setGoodsPrice(rs.getString("goods_price"));
						goodsCheckBean.setChaGoodsName(rs.getString("chagoodsname"));
						goodsCheckBean.setGoodsImgUrl(rs.getString("goods_img_url"));
					}
					
				}
				
    			ChangeGoodBean chgood = new ChangeGoodBean();
    			//订单no
    			chgood.setOrderno(orderNo);
    			//商品id
    			chgood.setGoodid(String.valueOf(goodsId));
    			//goodscarid
    			chgood.setGoodsCarId(goodsdataId);
    			//aliname
    			chgood.setAliname(goodsCheckBean.getGoodsName());
    			//aliimg
    			chgood.setAliimg(goodsCheckBean.getImgpath());
    			//aliurl
    			chgood.setAliurl(goodsCheckBean.getUrl());
    			//alipriceRMB
    			chgood.setAliprice(priceRmb);
    			if("tb1".equals(changeFlag)){
    				chgood.setChagoodname(goodsCheckBean.getTbName());
    				chgood.setChagoodimg(goodsCheckBean.getTbImg());
    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl(),1));
    				chgood.setChagoodprice(goodsCheckBean.getTbprice());
    			}else if("tb2".equals(changeFlag)){
    				chgood.setChagoodname(goodsCheckBean.getTbName1());
    				chgood.setChagoodimg(goodsCheckBean.getTbImg1());
    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl1(),1));
    				chgood.setChagoodprice(goodsCheckBean.getTbprice1());
    			}else if("tb3".equals(changeFlag)){
    				chgood.setChagoodname(goodsCheckBean.getTbName2());
    				chgood.setChagoodimg(goodsCheckBean.getTbImg2());
    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl2(),1));
    				chgood.setChagoodprice(goodsCheckBean.getTbprice2());
    			}else if("tb4".equals(changeFlag)){
    				chgood.setChagoodname(goodsCheckBean.getTbName3());
    				chgood.setChagoodimg(goodsCheckBean.getTbImg3());
    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getTbUrl3(),1));
    				chgood.setChagoodprice(goodsCheckBean.getTbprice3());
    			}else{
    				chgood.setChagoodname(goodsCheckBean.getChaGoodsName());
    				chgood.setChagoodimg(goodsCheckBean.getGoodsImgUrl());
    				chgood.setChagoodurl(TypeUtils.modefindUrl(goodsCheckBean.getGoodsPurl(),1));
    				chgood.setChagoodprice(String.valueOf(goodsCheckBean.getGoodsPrice()));
    				
    			}
	    		
//	    		upChangeDataGood(chgood);
	    		String sql2 = "insert into changegooddata (orderno,goodid,aliname,aliimg,aliurl,aliprice,chagoodname,chagoodimg,chagoodurl,chagoodprice,goodscarid) values(?,?,?,?,?,?,?,?,?,?,?) ";
	    		
				stmt3 = conn.prepareStatement(sql2);
				stmt3.setString(1, chgood.getOrderno());
				stmt3.setInt(2, Integer.valueOf(chgood.getGoodid()));
				stmt3.setString(3, chgood.getAliname());
				stmt3.setString(4, chgood.getAliimg());
				stmt3.setString(5, chgood.getAliurl());
				stmt3.setString(6, chgood.getAliprice());
				stmt3.setString(7, chgood.getChagoodname());
				stmt3.setString(8, chgood.getChagoodimg());
				stmt3.setString(9, chgood.getChagoodurl());
				stmt3.setString(10, chgood.getChagoodprice());
				stmt3.setInt(11, Integer.valueOf(chgood.getGoodsCarId()));
				res = stmt3.executeUpdate();
	    			
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rsProduceSource != null) {
				try {
					rsProduceSource.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfind != null) {
				try {
					stmtfind.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtProduceSource != null) {
				try {
					stmtProduceSource.close();
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
			if (stmt3 != null) {
				try {
					stmt3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
//			if (stmt4 != null) {
//				try {
//					stmt4.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			DBHelper.getInstance().closeConnection(conn);
//			DBHelper.getInstance().closeConnection(conn1);
		}
		return goodsCheckBean;
	}
	
	
	@Override
	public int upTbGood(TbGoodBean tbGoodBean) {
		
		String sql ="";
//		if(tbGoodBean.getDelFlag()==0){
//			sql = "update tbgooddata set tburl=?,tbprice=?,tbname=?,delflag=?,tbflag=?,tbimg=? where goodid=?";
//		}else{
			sql = "update goodsdatacheckhot01_1 set goods_pid=?,url=?,shop_id_temp=?,tbflag=0 where goods_pid=?";
//		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
//			if(tbGoodBean.getDelFlag()==0){
//				stmt.setString(1, tbGoodBean.getTbUrl());
//				stmt.setString(2, tbGoodBean.getTbPrice());
//				stmt.setString(3, tbGoodBean.getTbName());
//				stmt.setInt(4, tbGoodBean.getDelFlag());
//				stmt.setString(5, tbGoodBean.getTbFlag());
//				stmt.setString(6, tbGoodBean.getTbImg());
//				stmt.setInt(7, Integer.valueOf(tbGoodBean.getGoodId()));
//			}else{
//				stmt.setInt(1, tbGoodBean.getDelFlag());
//				stmt.setInt(2, Integer.valueOf(tbGoodBean.getGoodId()));
				stmt.setString(1, tbGoodBean.getGoodId());
				stmt.setString(2, tbGoodBean.getTbUrl());
				stmt.setString(3, tbGoodBean.getShopId());
				stmt.setString(4, tbGoodBean.getGoodId());
//			}

			
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
	public int upDbYlGood(TbGoodBean tbGoodBean) {
		
		String sql ="";
			sql = "update ali_info_data set 1688_url=?,1688_pid=?,shop_id=?,1688_price=?,1688_cat=?,1688_img=? where goods_pid=?";
			
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getYlUrl());
			stmt.setString(2, tbGoodBean.getYlPid());
			stmt.setString(3, tbGoodBean.getShopId());
			stmt.setString(4, tbGoodBean.getYlPrice());
			stmt.setString(5, tbGoodBean.getCatId());
			stmt.setString(6, tbGoodBean.getYlImg());
			stmt.setString(7, tbGoodBean.getGoodId());
			
			
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
		//更新ali信息
		updateAliInfo(tbGoodBean.getGoodId());
	 return res;
	}
	
	@Override
	public int upDbLireGood(TbGoodBean tbGoodBean) {
		
		String sql ="update lire_goods set flag=? where pid=?";
			
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, tbGoodBean.getDelFlag());
			stmt.setString(2, tbGoodBean.getGoodId());
			
			
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
		//更新ali信息
//		updateAliInfo(tbGoodBean.getGoodId());
	 return res;
	}
	
	
	
	
	@Override
	public int updateSourceAliFlag(TbGoodBean tbGoodBean) {
		
		String sql ="";
		//对标
		if(tbGoodBean.getDelFlag()==1){
			sql = "update alicachedatanew1_1 set flag=1,time=now() where goods_pid=?";
		}else{
			//无对标
			sql = "update alicachedatanew1_1 set flag=2,time=now() where goods_pid=?";
		}
			
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getGoodId());

			
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
	public int updateAliFlag(TbGoodBean tbGoodBean) {
		
		String sql ="";
//		if(tbGoodBean.getDelFlag()==1){
			sql = "update ali_info_data set 1688_url=?,1688_pid=?,shop_id=?,1688_price=?,1688_cat=?,cat_flag=0,handle_flag=1,bm_flag=1,img_check=10 where goods_pid=?";
//		}else{
//			sql = "update ali_info_data set ali_flag=2 where goods_pid=?";
//		}
			
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getYlUrl());
			stmt.setString(2, tbGoodBean.getYlPid());
			stmt.setString(3, tbGoodBean.getShopId());
			stmt.setString(4, tbGoodBean.getYlPrice());
			stmt.setString(5, tbGoodBean.getCatId());
			stmt.setString(6, tbGoodBean.getGoodId());

			
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
	public int updateAliInfoFlag(TbGoodBean tbGoodBean) {
		
		String sql ="";
		//精准对标
		if(tbGoodBean.getDelFlag()==1){
			//判断pid是否包含字母，如果包含就是亚马逊商品
//			if(StrUtils.judgeContainsStr(tbGoodBean.getGoodId())){
//				sql = "update ali_info_data set bm_flag=1,mark_flag=1,product_flag=1,lot_unit='"+tbGoodBean.getLotUnit()+"',user_name='"+tbGoodBean.getUserName()+"' where goods_pid=?";
//			}else{
				sql = "update ali_info_data set bm_flag=1,mark_flag=1,lot_unit='"+tbGoodBean.getLotUnit()+"',user_name='"+tbGoodBean.getUserName()+"' where goods_pid=?";
//			}
			
			//近似对标
		}else if(tbGoodBean.getDelFlag()==2){
			//判断pid是否包含字母，如果包含就是亚马逊商品
//			if(StrUtils.judgeContainsStr(tbGoodBean.getGoodId())){
//				sql = "update ali_info_data set bm_flag=1,mark_flag=2,product_flag=1,1688_weight='"+tbGoodBean.getYlWeight()+"',user_name='"+tbGoodBean.getUserName()+"' where goods_pid=?";
//			}else{
				sql = "update ali_info_data set bm_flag=1,mark_flag=2,1688_weight='"+tbGoodBean.getYlWeight()+"',user_name='"+tbGoodBean.getUserName()+"' where goods_pid=?";
//			}
			
			//无对标
		}else if(tbGoodBean.getDelFlag()==3){
			sql = "update ali_info_data set bm_flag=1,mark_flag=3 where goods_pid=?";
		}
			
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getGoodId());

			
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
	public int updateYlFlag(TbGoodBean tbGoodBean) {
		
		String sql = "update ali_info_data set bm_flag=1,mark_flag=1,remark=0,rebid_flag=1,1688_url=?,1688_pid=?,source_ylpid=? where goods_pid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getYlUrl());
			stmt.setString(2, tbGoodBean.getYlPid());
			stmt.setString(3, tbGoodBean.getSourceYlpid());
			stmt.setString(4, tbGoodBean.getGoodId());

			
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
	public List<Map<String, String>> getDataForDel(String startid){
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		String sql = "select  id,price,wprice,range_price,pid,wholesale_price,final_weight,"
				+ "catpath,morder,ali_price,ali_pid,ali_sellunit,ali_unit,ali_weight,weight,revise_weight"
				+ "  from  custom_benchmark_ready_newest where  id>"+startid+" limit 1000";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBHelper.getInstance().getConnection5();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				Map<String,String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("price", rs.getString("price"));
				map.put("wprice", rs.getString("wprice"));
				map.put("range_price", rs.getString("range_price"));
				map.put("pid", rs.getString("pid"));
				map.put("ali_price", rs.getString("ali_price"));
				map.put("ali_pid", rs.getString("ali_pid"));
				map.put("ali_sellunit", rs.getString("ali_sellunit"));
				map.put("ali_unit", rs.getString("ali_unit"));
				map.put("wholesale_price", rs.getString("wholesale_price"));
				map.put("final_weight", rs.getString("final_weight"));
				map.put("catpath", rs.getString("catpath"));
				map.put("morder", rs.getString("morder"));
				map.put("ali_weight", rs.getString("ali_weight"));
				map.put("weight", rs.getString("weight"));
				map.put("revise_weight", rs.getString("revise_weight"));
				result.add(map);
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
		return result;
	}
	
	@Override
	public int updateRebidFlag(TbGoodBean tbGoodBean) {
		
		String sql ="";
		if(tbGoodBean.getDelFlag()==4){
			//手工录入
			sql = "update custom_benchmark_ready set rebid_flag=1 where pid=? ";
		}else if(tbGoodBean.getDelFlag()==1){
			//精准对标
			sql = "update custom_benchmark_ready set rebid_flag=2 where pid=? ";
		}else if(tbGoodBean.getDelFlag()==2){
			//近似对标
			sql = "update custom_benchmark_ready set rebid_flag=3 where pid=? ";
		}else if(tbGoodBean.getDelFlag()==3){
			//无对标
			sql = "update custom_benchmark_ready set rebid_flag=4 where pid=? ";
		}

		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getSourceYlpid());
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
	public int validationDateUpdate(String flag) {
		
		String sql ="update ali_info_data a, cross_border.custom_benchmark_ready_newest b ";
		sql = sql+"set a.shelf_flag=2 where a.1688_pid=b.pid and b.valid=0 ";

		Connection conn = DBHelper.getInstance().getConnection5();
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
	public int updatePolymerizationShopId(String sourceShopId, String sourceGoodsPid,String newShopId) {
		
		
		String sql = "select count(shop_id) as shopCon,shop_id from ali_info_data where shop_id=? and shelf_flag=1 group by shop_id HAVING shopCon>1 ";
			
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection5();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, newShopId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String newShopIdTemp = rs.getString("shop_id");
				updateNewShopId(sourceShopId,sourceGoodsPid,newShopIdTemp);
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
		return 0;
	}
	
	public int updateNewShopId(String sourceShopId,String sourceGoodsPid,String newShopIdTemp) {
		
		String sql ="update ali_info_data set shop_id=?,source_shop_id=? where goods_pid=? ";

		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, newShopIdTemp);
			stmt.setString(2, sourceShopId);
			stmt.setString(3, sourceGoodsPid);
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
	public int delForGoodsPid(TbGoodBean tbGoodBean) {
		
		String sql = "delete from ali_info_data where goods_pid=?";
		
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbGoodBean.getGoodId());
			
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
	public int upChangeGood(ChangeGoodBean chaGoodBean) {
		
		String sql ="update changegooddata set chagoodname=?,chagoodimg=?,chagoodurl=?,chagoodprice=?,aliname=?,delflag=0 where orderno=? and chagoodurl=? ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, chaGoodBean.getChagoodname());
			stmt.setString(2, chaGoodBean.getChagoodimg());
			stmt.setString(3, chaGoodBean.getChagoodurl());
			stmt.setString(4, chaGoodBean.getChagoodprice());
			stmt.setString(5, chaGoodBean.getAliname());
			stmt.setString(6, chaGoodBean.getOrderno());
			stmt.setString(7, chaGoodBean.getChagoodurl());
			
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
	

	public static int delChangeGood(String orderNo) {
		
		String sql ="delete from changegooddata where orderno=? ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			//stmt.setString(2, chaGoodBean.getChagoodurl());
			
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
	
	public int upChangeDataGood(ChangeGoodBean chaGoodBean) {
		
		String sql ="update changegooddata set chagoodname=?,chagoodimg=?,chagoodurl=?,chagoodprice=?,aliname=?,delflag=1 where orderno=? and chagoodurl=? and goodscarid=? ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, chaGoodBean.getChagoodname());
			stmt.setString(2, chaGoodBean.getChagoodimg());
			stmt.setString(3, chaGoodBean.getChagoodurl());
			stmt.setString(4, chaGoodBean.getChagoodprice());
			stmt.setString(5, chaGoodBean.getAliname());
			stmt.setString(6, chaGoodBean.getOrderno());
			stmt.setString(7, chaGoodBean.getChagoodurl());
			stmt.setInt(8, Integer.parseInt(chaGoodBean.getGoodsCarId()));
			
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
	
	//HomTU  ADD 2015-12-29
	public void updateOrderProduceSource(String orderNo,int goodscarid,int goodsId,int od_id,String importUrl){
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmtfind = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			String sqlfind = "select count(id) as sum from order_product_source where orderid='"+orderNo+"' and goodsid="+goodscarid;
			stmtfind = conn.prepareStatement(sqlfind);
			rs = stmtfind.executeQuery();
			if(rs.next()){
				res = rs.getInt("sum");
			}
			if(res==0){
				String sql="delete from order_product_source where orderid='"+orderNo+"' and goodsid='"+goodscarid+"'";
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				sql="insert into order_product_source(orderid,goodsid,goodsdataid,goods_url,purchase_state,od_id,tborderid) values(?,?,?,?,12,?,'重复3') ";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,orderNo);
				stmt.setInt(2,goodscarid);
				stmt.setInt(3,goodsId);
				stmt.setString(4,importUrl);
				stmt.setInt(5,od_id);
				stmt.executeUpdate();
			} else {
				String sql= "update order_product_source set purchase_state=12,od_id=? "
						+ "where orderid='"+orderNo+"' and goodsid="+goodscarid+" and del=0";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1,od_id);
				stmt.executeUpdate();
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
			if (stmtfind != null) {
				try {
					stmtfind.close();
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
	}

	//更新有替换产品标识
	public int updateOrderInfoFlag(String orderNo) {

		String sql = "update orderinfo set packag_style = 1 where order_no = ? ";

		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
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
	
	//更新货源表替换产品标识
	public int updateGoodsSourceChaFlag(String url,String pUrl) {

		String sql = "update goods_source set change_flag = 1 where goods_url = ? and goods_purl=? ";

		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.setString(2, pUrl);
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
	public int getUserIdByOrder(String orderNo) {
		// TODO Auto-generated method stub
		String sql = "select user_id from orderinfo where order_no = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("user_id");
			}
		}catch (Exception e) {
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
	public String getAliexpressHistory(String ali_pid) {
		
		String sql = "select new_price from goods_price_historys  where goods_pid =?";
		
		
		String newPrice = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBHelper.getInstance().getConnection8();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ali_pid);
			rs = stmt.executeQuery();
			while(rs.next()){
				newPrice = rs.getString("new_price");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			newPrice = null;
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
		return newPrice;
	}
	
	@Override
	public void insertDelFlag_New(Map<String, String> map){
		String sql0 = "select count(1) as n,del_flag from a_custom_for_delflag where pid=?";
		
		String sql1 = "update a_custom_for_delflag set "
				+ " del_flag=? "
				+ "where pid=? ";

		Connection conn = null;
		PreparedStatement stmt0 = null;
		ResultSet rs0 = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBHelper.getInstance().getConnection8();
			stmt0 = conn.prepareStatement(sql0);
			stmt0.setString(1, map.get("pid"));
			rs0 = stmt0.executeQuery();
			if(rs0.next()){
				
				if(rs0.getInt("n") > 0){
					String delFlag = rs0.getString("del_flag");
					//有数据更新
					stmt = conn.prepareStatement(sql1);
					stmt.setString(1, delFlag+map.get("del_flag"));
					stmt.setString(2, map.get("pid"));
					stmt.executeUpdate();
					}
					
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
			if (stmt0 != null) {
				try {
					stmt0.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs0 != null) {
				try {
					rs0.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public int delNotSoldCategory(String catid1) {
		String sql1 = "";
		String  sql2= "";
		
		 sql1 ="insert into no_sold_category (catid1) values(?)";
		//有处理完成标志
		 sql2 ="update alicachedatanew1_1 a,ali_goods_source_new_hx b set a.catid_flag=1 where a.goods_pid=b.goods_pid and b.catid1=?";

		
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt1 = null,stmt2 = null;
		int res = 0,res1 = 0;
		try {
			
			stmt2 = conn.prepareStatement(sql2);
			stmt2.setString(1,catid1);
			res1 = stmt2.executeUpdate();
			
			if(res1>0){
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1,catid1);
				res = stmt1.executeUpdate();
			}
			
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
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	/**
	 * 查询所有不售卖的分类
	 */
	@Override
	public List<Map<String, Object>> getNoSoldCategory() {
		List<Map<String, Object>> listmaps = new ArrayList<Map<String, Object>>();
		String sql ="select DISTINCT n.catid1,a.category from no_sold_category n , ali_category a where n.catid1 =a.cid";
	
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection5();
		
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("catid1", rs.getString("catid1"));
				map.put("catname", rs.getString("category"));
				listmaps.add(map);
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
		
		return listmaps;
	}

	@Override
	public int updateNoSoldCategoryToSold(List<String> lists) {
		String sql ="update alicachedatanew1_1 a,ali_goods_source_new_hx b set a.catid_flag=0 where a.goods_pid=b.goods_pid and b.catid1=?";
		String sql2 = "delete from no_sold_category where catid1 = ?";
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement(sql2);
			for(int i=0;i<lists.size();i++){
				stmt.setString(1, lists.get(i));
				res = stmt.executeUpdate();
			}
			for (String catid : lists) {
				stmt2.setString(1, catid);
				res = stmt2 .executeUpdate();
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
		return res;
	}

	@Override
	public void updateTime(int id,int flag) {
		String sql = null;
		String sql2 = null;
		if(flag==1){
				
				sql ="update  update_data_time set update_data_time = now() where id=?";
				sql2 = "insert into update_data_time (update_data_time) values (now())";
		}else{
			
				sql ="update  update_data_time set shop_aggregation_time = now() where id=? ";
				sql2 = "insert into update_data_time (shop_aggregation_time) values (now())";
		}
		
		
		Connection conn = DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null,stmt2=null;
		try {
			stmt = conn.prepareStatement(sql);
			if(id!=0){
				stmt.setInt(1, id+1);	
			}
			int i = stmt.executeUpdate();
			if(i==0){
				stmt2= conn.prepareStatement(sql2);
				stmt2.executeUpdate();
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
		
	}

	@Override
	public Map<String, Object> selectupdateTime() {
		//此处查询的情况,分别查询两种时间,
		String sql = "select * from update_data_time where update_data_time is not null order by id desc limit 1 ";
		String sql2  = "select * from update_data_time where shop_aggregation_time is not null order by id desc limit 1";
		Connection conn = null;
		PreparedStatement stmt = null,stmt2=null;
		ResultSet rs = null,rs2=null;
		conn = DBHelper.getInstance().getConnection5();
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement(sql2);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			while (rs.next()){
				map.put("id1",rs.getObject("id"));
				String tsStr = rs.getTimestamp("update_data_time")+"";    
				if(org.apache.commons.lang.StringUtils.isBlank(tsStr)){
					map.put("time1",null );
				}else{
					Date  ts1 = sdf.parse(tsStr);  
					map.put("time1",sdf.format(ts1) );
				}
			}
			while (rs2.next()){
				map.put("id2",rs2.getObject("id"));
				String tsStr2 = rs2.getTimestamp("shop_aggregation_time")+"";    
				if(org.apache.commons.lang.StringUtils.isBlank(tsStr2)){
					map.put("time2", null);
				}else{
					Date  ts2 = sdf.parse(tsStr2);
					map.put("time2", sdf.format(ts2));
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

	
	
}
