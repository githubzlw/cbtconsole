package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.GoodsDaoBean;
import com.cbt.parse.daoimp.IGoodsDao;
import com.google.common.collect.Lists;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**goodsdata.sql
 * @author abc
 *set session transaction isolation level read uncommitted;
 */
public class GoodsDao implements IGoodsDao{
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsDao.class);
	/**添加一条数据信息
	 * @param 
	 * @return
	 */
	@Override
	public  int addSourceData(GoodsDaoBean bean) {
		int result = 0;
		
		String sqlupdate = "update goods_data_change set goods_name=?,goods_url=?,"
				+ "goods_sellprice=?,goods_orignalprice=?,goods_unitforsell=?,"
				+ "goods_unitforcurrency=?,goods_info=?,"
				+ "goods_uuid=?,goods_valid=?,goods_img=?,goods_imgsize=?,"
				+ "goods_sold=?,goods_minorder=?,goods_shopname=?,"
				+ "goods_shopid=?,goods_freeflag=?,goods_type=?,goods_detail=?,create_time =now() "
				+ "where goods_pid=?";
		
		String sqlinsert = "insert goods_data_change(goods_name,goods_url,"
				+ "goods_sellprice,goods_orignalprice,"
				+ "goods_unitforsell,goods_unitforcurrency,goods_info,"
				+ "goods_uuid,goods_valid,goods_img,goods_imgsize,"
				+ "goods_sold,goods_minorder,goods_shopname,"
				+ "goods_shopid,goods_freeflag,goods_type,goods_detail,"
				+ "goods_pid,create_time )"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
		
		
		PreparedStatement stmtsqlupdate = null;
		PreparedStatement stmtsqlinsert = null;
		try {
			List<String> lstValues = Lists.newArrayList();
			lstValues.add(bean.getName());
			lstValues.add(bean.getUrl());
			lstValues.add(bean.getsPrice());
			lstValues.add(bean.getoPrice());
			lstValues.add(bean.getgUnit());
			lstValues.add(bean.getpUnit());
			lstValues.add(bean.getInfourl());
			lstValues.add(bean.getUuid());
			lstValues.add(bean.getValid());
			lstValues.add(bean.getImg());
			lstValues.add(bean.getImgSize());
			lstValues.add(bean.getSell());
			lstValues.add(bean.getmOrder());
			lstValues.add(bean.getsName());
			lstValues.add(bean.getsID());
			lstValues.add(bean.getFree());
			lstValues.add(bean.getTypes());
			lstValues.add(bean.getDetail());
			lstValues.add(bean.getpID());
			
			String runSql = DBHelper.covertToSQL(sqlupdate, lstValues );
			result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
			
			if(result == 0){
				runSql = DBHelper.covertToSQL(sqlinsert, lstValues );
				result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmtsqlinsert != null) {
				try {
					stmtsqlinsert.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtsqlupdate != null) {
				try {
					stmtsqlupdate.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	
	/**
	 * @param 
	 * @return
	 */
	@Override
	public  int updatevalid(String valid,String url) {
		int result = 0;
		String sql = "update goodsdata_write set time =now(),valid=? where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valid);
			stmt.setString(2, url);
			result = stmt.executeUpdate();
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
	
	
	
	/**查询数据
	 * @return
	 */
	@Override
	public GoodsDaoBean queryData(String table,String url) {
		long st = new Date().getTime();
		String sql = "select valid,time,cID,pID,name,url,oPrice,sPrice,wPrice,pUnit,gUnit,img,imgSize,mOrder,"
				+ "pTime,sellUnit,weight,width,perWeight,free,category,sID,title,sUrl,types,info,"
				+ "detail,sell,method,posttime,infourl,packages,bprice,sku,fprice,feeprice,dtime  from "+table+" where url=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		GoodsDaoBean rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			if(rs.next()){
				rsTree = new GoodsDaoBean();
				rsTree.setValid(rs.getString("valid"));
				rsTree.setTime(rs.getString("time"));
				rsTree.setcID(rs.getString("cID"));
				rsTree.setpID(rs.getString("pID"));
				rsTree.setName( rs.getString("name"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setoPrice( rs.getString("oPrice"));
				rsTree.setsPrice( rs.getString("sPrice"));
				rsTree.setwPrice( rs.getString("wPrice"));
				rsTree.setpUnit( rs.getString("pUnit"));
				rsTree.setgUnit( rs.getString("gUnit"));
				rsTree.setImg(rs.getString("img"));
				rsTree.setImgSize( rs.getString("imgSize"));
				rsTree.setmOrder( rs.getString("mOrder"));
				rsTree.setpTime( rs.getString("pTime"));
				rsTree.setSellUnit(rs.getString("sellUnit"));
				rsTree.setWeight( rs.getString("weight"));
				rsTree.setWidth( rs.getString("width"));
				rsTree.setPerWeight( rs.getString("perWeight"));
				rsTree.setFree( rs.getString("free"));
				rsTree.setCategory( rs.getString("category"));
				rsTree.setsID( rs.getString("sID"));
				rsTree.setTitle( rs.getString("title"));
				rsTree.setsUrl(rs.getString("sUrl"));
				rsTree.setTypes( rs.getString("types"));
				rsTree.setInfo(rs.getString("info"));
				rsTree.setDetail( rs.getString("detail"));
				rsTree.setSell(rs.getString("sell"));
				rsTree.setMethod( rs.getString("method"));
				rsTree.setPosttime( rs.getString("posttime"));
				rsTree.setInfourl(rs.getString("infourl"));
				rsTree.setPackages(rs.getString("packages"));
				rsTree.setBprice( rs.getString("bprice"));
				rsTree.setSku(rs.getString("sku"));
				rsTree.setfPrice(rs.getString("fprice"));
				rsTree.setFeePrice(rs.getString("feeprice"));
				rsTree.setDtime(rs.getInt("dtime"));
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
		LOG.warn("search goods from sql:"+(new Date().getTime()-st));
		return rsTree;
	}
	
	/**查询商品数据
	 * @return
	 */
	@Override
	public GoodsDaoBean queryPid(String pid,String cid) {
		String sql = "select id,valid,time,cID,pID,name,url,oPrice,sPrice,wPrice,pUnit,gUnit,img,imgSize,mOrder,"
				+ "pTime,sellUnit,weight,width,perWeight,free,category,sID,title,sUrl,types,info,"
				+ "detail,sell,method,posttime,infourl,packages,bprice,sku,fprice,feeprice,dtime from goodsdata where cID=? and pID=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		GoodsDaoBean rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cid);
			stmt.setString(2, pid);
			rs = stmt.executeQuery();
			if(rs.next()){
				rsTree = new GoodsDaoBean();
				rsTree.setId(rs.getInt("id"));
				rsTree.setValid(rs.getString("valid"));
				rsTree.setTime(rs.getString("time"));
				rsTree.setcID(rs.getString("cID"));
				rsTree.setpID(rs.getString("pID"));
				rsTree.setName( rs.getString("name"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setoPrice( rs.getString("oPrice"));
				rsTree.setsPrice( rs.getString("sPrice"));
				rsTree.setwPrice( rs.getString("wPrice"));
				rsTree.setpUnit( rs.getString("pUnit"));
				rsTree.setgUnit( rs.getString("gUnit"));
				rsTree.setImg(rs.getString("img"));
				rsTree.setImgSize( rs.getString("imgSize"));
				rsTree.setmOrder( rs.getString("mOrder"));
				rsTree.setpTime( rs.getString("pTime"));
				rsTree.setSellUnit(rs.getString("sellUnit"));
				rsTree.setWeight( rs.getString("weight"));
				rsTree.setWidth( rs.getString("width"));
				rsTree.setPerWeight( rs.getString("perWeight"));
				rsTree.setFree( rs.getString("free"));
				rsTree.setCategory( rs.getString("category"));
				rsTree.setsID( rs.getString("sID"));
				rsTree.setTitle( rs.getString("title"));
				rsTree.setsUrl(rs.getString("sUrl"));
				rsTree.setTypes( rs.getString("types"));
				rsTree.setInfo(rs.getString("info"));
				rsTree.setDetail( rs.getString("detail"));
				rsTree.setSell(rs.getString("sell"));
				rsTree.setMethod( rs.getString("method"));
				rsTree.setPosttime( rs.getString("posttime"));
				rsTree.setInfourl(rs.getString("infourl"));
				rsTree.setPackages(rs.getString("packages"));
				rsTree.setBprice( rs.getString("bprice"));
				rsTree.setSku(rs.getString("sku"));
				rsTree.setfPrice(rs.getString("fprice"));
				rsTree.setFeePrice(rs.getString("feeprice"));
				rsTree.setDtime(rs.getInt("dtime"));
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
		return rsTree;
	}
	
	@Override
	public ArrayList<String> getImages(String car_id) {
		ArrayList<String> list=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String sql="select chagoodimg from changegooddata where goodscarid='"+car_id+"'";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				String chagoodimg=rs.getString("chagoodimg");
				list.add(chagoodimg);
			}
		}catch(Exception e){
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
		return list;
	}
}
