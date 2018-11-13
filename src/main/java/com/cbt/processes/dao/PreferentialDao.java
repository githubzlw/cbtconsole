package com.cbt.processes.dao;

import com.cbt.bean.*;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.util.Utility;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PreferentialDao implements IPreferentialDao{

	 
	
	public int getPreferential(int userid, String url){
		String sql = "select id from preferential_application where userid=? and goodsid=(select max(id) from goodsdata where url=?) ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, url);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("id");
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
	public int savePreferential(Preferential preferential,String url) {
		String sql = "insert preferential_application(pano,userid,goodsid,quantity,username,email,country,remark,createtime,sprice,effectivetime,syseffectivetime,sessionid,goods_unit,discount,discountedUnitPrice,totalprice,shipping,currency,url) values(?,?,(select max(id) from goodsdata where url=?),?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, preferential.getPano());
			stmt.setInt(2, preferential.getUserid());
			stmt.setString(3,url);
			stmt.setInt(4, preferential.getNumber());
			stmt.setString(5, preferential.getUsername());
			stmt.setString(6, preferential.getEmail());
			stmt.setString(7, preferential.getCountry());
			stmt.setString(8, preferential.getNote());
			stmt.setDouble(9, preferential.getSprice());
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 2);
			String tiemString = f.format(c.getTime());
			stmt.setString(10, tiemString);
			stmt.setString(11, tiemString);
			stmt.setString(12, preferential.getSessionid());
			stmt.setString(13, preferential.getpGoodsUnit());
			stmt.setString(14, preferential.getDiscount());
			stmt.setString(15, preferential.getDiscountedUnitPrice());
			stmt.setString(16, preferential.getTotalprice());
			stmt.setString(17, preferential.getShipping());
			stmt.setString(18, preferential.getCurrency());
			stmt.setString(19, url);
			result = stmt.executeUpdate();
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
		return result;
	}
	
	@Override
	public int savePreferential2(Preferential preferential,String url) {
		String sql = "insert preferential_application(pano,userid,goodsid,quantity,username,email,country,remark,createtime,sprice,effectivetime,syseffectivetime,sessionid,goods_unit,discount,discountedUnitPrice,totalprice,shipping,currency,url) values(?,?,(select max(id) from goodsdata where url=?),?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, preferential.getPano());
			stmt.setInt(2, preferential.getUserid());
			stmt.setString(3,url);
			stmt.setInt(4, preferential.getNumber());
			stmt.setString(5, preferential.getUsername());
			stmt.setString(6, preferential.getEmail());
			stmt.setString(7, preferential.getCountry());
			stmt.setString(8, preferential.getNote());
			stmt.setDouble(9, preferential.getSprice());
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 2);
			String tiemString = f.format(c.getTime());
			stmt.setString(10, tiemString);
			stmt.setString(11, tiemString);
			stmt.setString(12, preferential.getSessionid());
			stmt.setString(13, preferential.getpGoodsUnit());
			stmt.setString(14, preferential.getDiscount());
			stmt.setString(15, preferential.getDiscountedUnitPrice());
			stmt.setString(16, preferential.getTotalprice());
			stmt.setString(17, preferential.getShipping());
			stmt.setString(18, preferential.getCurrency());
			stmt.setString(19, url);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
		    while(rs.next()){
		    	result = rs.getInt(1);
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
		return result;
	}
	
	@Override
	public int getPreferentialNum(int userid) {
		String sql = "select count(id) conu from preferential_application where userid=? and state=0 and id in(select max(paid) from pa_interacted p   group by paid )";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("conu");
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
	public List<PreferentialWeb> getPreferentials(int userid, int state, int startpage, int page) {
		String sql = "select pa.id,pa.userid,pano,name,goodsid,quantity,url,img,mOrder,quantity,pa.sPrice,pa.state,remark,createtime,handletime,username,email,country,syseffectivetime,goods_unit,currency from goodsdata g,preferential_application pa where pa.url=g.url and pa.state=1 and userid=? order by handletime desc   limit ?, ?";
		if(state == 2){
			sql = "select pa.id,pa.userid,pano,name,goodsid,quantity,url,img,mOrder,quantity,pa.sPrice,pa.state,remark,createtime,handletime,username,email,country,syseffectivetime,goods_unit,currency from goodsdata g,preferential_application pa where pa.url=g.url and pa.state=0 and pa.id in (select paid from pa_interacted pai where pai.paid=pa.id) and userid=? order by handletime desc   limit ?, ?";
		}else if(state == 0){
			sql = "select pa.id,pa.userid,pano,name,goodsid,quantity,url,img,mOrder,quantity,pa.sPrice,pa.state,remark,createtime,handletime,username,email,country,syseffectivetime,goods_unit,currency from goodsdata g,preferential_application pa where pa.url=g.url and pa.state=0 and pa.id not in (select paid from pa_interacted pai where pai.paid=pa.id) and userid=? order by handletime desc   limit ?, ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		List<PreferentialWeb> oblist = new ArrayList<PreferentialWeb>();
		double price = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setInt(2, startpage);
			stmt.setInt(3, page);
			rs = stmt.executeQuery();
			PreferentialWeb preferentialWeb;
			List<PaInteracted> paInteracteds;
			PaInteracted paInteracted;
			Preferential preferential;
			String sql1 = "select id,price,quantity,type,createtime,sysid,message from pa_interacted where paid=? order by id desc";
			while (rs.next()) {
				paInteracteds = new ArrayList<PaInteracted>();
				preferentialWeb = new PreferentialWeb();
				preferential = new Preferential();
				int paid = rs.getInt("id");
				preferential.setCountry(rs.getString("country"));
				preferential.setCreatetime(rs.getString("createtime"));
				preferential.setEmail(rs.getString("email"));
				preferential.setGoodsid(rs.getInt("goodsid"));
				preferential.setHandletime(rs.getString("handletime"));
				preferential.setId(paid);
				preferential.setNote(rs.getString("remark"));
				preferential.setNumber(rs.getInt("quantity"));
				preferential.setPano(rs.getString("pano"));
				preferential.setState(rs.getInt("state"));
				preferential.setUserid(rs.getInt("userid"));
				preferential.setUsername(rs.getString("username"));
				preferential.setSprice(rs.getDouble("sPrice"));
				preferential.setpGoodsUnit(rs.getString("goods_unit"));
				preferential.setCurrency(rs.getString("currency"));
				preferentialWeb.setPreferential(preferential);
				String img = rs.getString("img");
				if(img.indexOf(",") > -1){
					img = img.substring(1,img.indexOf(","));
				}else{
					img = img.substring(1,img.indexOf("]"));
				}
				preferentialWeb.setImg(img);
				preferentialWeb.setUrl(rs.getString("url"));
				preferentialWeb.setmOrder(rs.getString("mOrder"));
				String effectivetime = rs.getString("syseffectivetime");
				if(!Utility.getStringIsNull(effectivetime)){
					effectivetime = rs.getString("effectivetime");
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
				Date date=sdf.parse(effectivetime);
				boolean eend = date.before(new Date());
				preferential.setEffectiveend(eend ? 1 : 0);
				preferentialWeb.setName(rs.getString("name"));
				stmt2=conn.prepareStatement(sql1);
				stmt2.setInt(1, paid);
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					int type = rs2.getInt("type");
					double paprice = rs2.getDouble("price");
					if(price == 0 && type == 0){
						price = paprice;
					}
					paInteracted = new PaInteracted();
					paInteracted.setCreatetime(rs2.getString("createtime"));
					paInteracted.setPrice(paprice);
					paInteracted.setQuantity(rs2.getInt("quantity"));
					paInteracted.setMessage(rs2.getString("message"));
					paInteracted.setSysid(rs2.getInt("sysid"));
					paInteracted.setType(rs2.getInt("type"));
					paInteracteds.add(paInteracted);
				}
				preferentialWeb.setPaInteracteds(paInteracteds);
				preferentialWeb.setPrice(price);
				oblist.add(preferentialWeb);
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
	public List<PreferentialWeb> getDiscounts(int userid) {
		//zlw add start
		String sql = "select pa.id,pa.userid,pano,name,goodsid,quantity,pa.url,img,mOrder,quantity,pa.sPrice,pa.state,remark,createtime,handletime,username,email,country,syseffectivetime,goods_unit,pa.discount,pa.discountedunitprice,pa.currency from goodsdata g,preferential_application pa where ( pa.url=g.url) and pa.state=0 and pa.id in (select paid from pa_interacted pai where pai.paid=pa.id) and userid=?";
		sql = sql + " union select pa.id,pa.userid,pano,name,goodsid,quantity,pa.url,img,mOrder,quantity,pa.sPrice,pa.state,remark,createtime,handletime,username,email,country,syseffectivetime,goods_unit,pa.discount,pa.discountedunitprice,pa.currency from goodsdata g,preferential_application pa where ( pa.url=g.url) and pa.state=0 and pa.id not in (select paid from pa_interacted pai where pai.paid=pa.id) and userid=? order by handletime desc";
		//zlw add end
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		List<PreferentialWeb> oblist = new ArrayList<PreferentialWeb>();
		double price = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			//zlw add start
			stmt.setInt(2, userid);
			//zlw add end
			rs = stmt.executeQuery();
			PreferentialWeb preferentialWeb;
			List<PaInteracted> paInteracteds;
			PaInteracted paInteracted;
			Preferential preferential;
			String sql1 = "select id,price,quantity,type,createtime,sysid,message from pa_interacted where paid=? order by id desc";
			while (rs.next()) {
				paInteracteds = new ArrayList<PaInteracted>();
				preferentialWeb = new PreferentialWeb();
				preferential = new Preferential();
				int paid = rs.getInt("id");
				preferential.setCountry(rs.getString("country"));
				preferential.setCreatetime(rs.getString("createtime").substring(0, 16));
				preferential.setEmail(rs.getString("email"));
				preferential.setGoodsid(rs.getInt("goodsid"));
				preferential.setHandletime(rs.getString("handletime"));
				preferential.setId(paid);
				preferential.setNote(rs.getString("remark"));
				preferential.setNumber(rs.getInt("quantity"));
				preferential.setPano(rs.getString("pano"));
				preferential.setState(rs.getInt("state"));
				preferential.setUserid(rs.getInt("userid"));
				preferential.setUsername(rs.getString("username"));
				preferential.setSprice(rs.getDouble("sPrice"));
				preferential.setpGoodsUnit(rs.getString("goods_unit"));
				preferential.setDiscount(rs.getString("discount"));
				preferential.setDiscountedUnitPrice(rs.getString("discountedunitprice"));
				preferential.setCurrency(rs.getString("currency"));
				preferentialWeb.setPreferential(preferential);
				String img = rs.getString("img");
				if(img.indexOf(",") > -1){
					img = img.substring(1,img.indexOf(","));
				}else{
					img = img.substring(1,img.indexOf("]"));
				}
				preferentialWeb.setImg(img);
				preferentialWeb.setUrl(rs.getString("url"));
				preferentialWeb.setmOrder(rs.getString("mOrder"));
				String effectivetime = rs.getString("syseffectivetime");
				if(!Utility.getStringIsNull(effectivetime)){
					effectivetime = rs.getString("effectivetime");
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
				Date date=sdf.parse(effectivetime);
				boolean eend = date.before(new Date());
				preferential.setEffectiveend(eend ? 1 : 0);
				preferentialWeb.setName(rs.getString("name"));
				stmt2=conn.prepareStatement(sql1);
				stmt2.setInt(1, paid);
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					int type = rs2.getInt("type");
					double paprice = rs2.getDouble("price");
					if(price == 0 && type == 0){
						price = paprice;
					}
					paInteracted = new PaInteracted();
					paInteracted.setCreatetime(rs2.getString("createtime").substring(0, 16));
					paInteracted.setPrice(paprice);
					paInteracted.setQuantity(rs2.getInt("quantity"));
					paInteracted.setMessage(rs2.getString("message"));
					paInteracted.setSysid(rs2.getInt("sysid"));
					paInteracted.setType(rs2.getInt("type"));
					paInteracteds.add(paInteracted);
				}
				preferentialWeb.setPaInteracteds(paInteracteds);
				preferentialWeb.setPrice(price);
				oblist.add(preferentialWeb);
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
	public int getPreferentials(int userid,int state) {
		String sql = "select count(id) coun from preferential_application pa where pa.state=? and userid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int count = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setInt(2, userid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				 count = rs.getInt("coun");
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
	public int savePai(int id, int gids, double prices) {
		String sql = "insert into pa_interacted(paid,gid,price,type,createtime) values(?,?,?,1,now())";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
				stmt.setInt(1, id);
				stmt.setInt(2, gids);
				stmt.setDouble(3, prices);
				stmt.addBatch();
			result = stmt.executeUpdate();
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
		return result;
	}

	@Override
	public int delPaprestrain(int pid,String cancel_reason) {
		String sql = "update preferential_application set state=2 ,cancel_reason=?,cancel_obj=1 where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(2, pid);
			stmt.setString(1, cancel_reason);
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
	public List<PreferentialWeb> getPreferentials(String uid) {
		String sql = "select pa.id,pa.userid,pano,quantity,url,img,mOrder,quantity,pa.sPrice,pa.state,remark,createtime,email,country,effectivetime,syseffectivetime,goods_unit from goodsdata g,preferential_application pa where pa.url=g.url and state!=2 and state!=1 and pa.id in (select pid from paprestrain where uid=? and state=0) ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		List<PreferentialWeb> oblist = new ArrayList<PreferentialWeb>();
		double price = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, uid);
			rs = stmt.executeQuery();
			PreferentialWeb preferentialWeb;
			List<PaInteracted> paInteracteds;
			PaInteracted paInteracted;
			Preferential preferential;
			String sql1 = "select id,price,quantity,type,createtime,sysid,message from pa_interacted where paid=? order by id desc";
			while (rs.next()) {
				paInteracteds = new ArrayList<PaInteracted>();
				preferentialWeb = new PreferentialWeb();
				preferential = new Preferential();
				int paid = rs.getInt("id");
				preferential.setCountry(rs.getString("country"));
				preferential.setCreatetime(rs.getString("createtime"));
				preferential.setEmail(rs.getString("email"));
				preferential.setId(paid);
				preferential.setNote(rs.getString("remark"));
				preferential.setNumber(rs.getInt("quantity"));
				preferential.setPano(rs.getString("pano"));
				preferential.setState(rs.getInt("state"));
				preferential.setUserid(rs.getInt("userid"));
				preferential.setSprice(rs.getDouble("sPrice"));
				preferential.setpGoodsUnit(rs.getString("goods_unit"));
				preferentialWeb.setPreferential(preferential);
				String effectivetime = rs.getString("syseffectivetime");
				if(!Utility.getStringIsNull(effectivetime)){
					effectivetime = rs.getString("effectivetime");
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
				Date date=sdf.parse(effectivetime);
				boolean eend = date.before(new Date());
				preferential.setEffectiveend(eend ? 1 : 0);
				
				String img = rs.getString("img");
				if(img.indexOf(",") > -1){
					img = img.substring(1,img.indexOf(","));
				}else{
					img = img.substring(1,img.indexOf("]"));
				}
				preferentialWeb.setImg(img);
				preferentialWeb.setUrl(rs.getString("url"));
				preferentialWeb.setmOrder(rs.getString("mOrder"));
				
				stmt2=conn.prepareStatement(sql1);
				stmt2.setInt(1, paid);
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					int type = rs2.getInt("type");
					double paprice = rs2.getDouble("price");
					if(price == 0 && type == 0){
						price = paprice;
					}
					paInteracted = new PaInteracted();
					paInteracted.setCreatetime(rs2.getString("createtime"));
					paInteracted.setPrice(paprice);
					paInteracted.setQuantity(rs2.getInt("quantity"));
					paInteracted.setMessage(rs2.getString("message"));
					paInteracted.setSysid(rs2.getInt("sysid"));
					paInteracted.setType(rs2.getInt("type"));
					paInteracteds.add(paInteracted);
				}
				preferentialWeb.setPaInteracteds(paInteracteds);
				preferentialWeb.setPrice(price);
				oblist.add(preferentialWeb);
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
	public List<SpiderBean> getGoodsdata(String uid, int userid, int type) {
		String sql = "select p.userid,goodsid,quantity,gUnit,g.pID,(select price from pa_interacted where paid=p.id and type=0 order by id desc limit 1) price,p.sprice,name,p.url,img,weight,width,perWeight,free,ptime,sellUnit,method,posttime,remark,method,posttime,goods_unit,p.currency  from goodsdata g,preferential_application p where (g.url=p.url) and syseffectivetime>now() and state=0 and p.id in (select pid from paprestrain where uid=? )";
		String[] uids = uid.split("@");
		if(type == 1){
//			sql = "select p.userid,goodsid,quantity,gUnit,g.pID,(select price from pa_interacted where paid=p.id and type=0 order by id desc limit 1) price,p.sprice,name,p.url,img,weight,width,perWeight,free,ptime,sellUnit,method,posttime,remark,method,posttime,goods_unit,p.currency  from goodsdata g,preferential_application p where (g.id=p.goodsid or g.url=p.url) and syseffectivetime>now() and state=0 and ";
			sql = "select distinct p.userid,goodsid,ifnull(pa.quantity,p.quantity) quantity,gUnit,g.pID,ifnull(pa.price,p.discountedunitprice) price,p.sprice,name,p.url,img,weight,width,perWeight,free,ptime,sellUnit,method,posttime,remark,method,posttime,goods_unit,p.currency  from goodsdata g,preferential_application p  left join pa_interacted pa on pa.paid=p.id where (g.url=p.url) and syseffectivetime>now() and state=0 and ";
			if(uids.length > 0){
				sql += " ( p.id=? ";
			}
			for (int i = 1; i < uids.length; i++) {
				sql += " or p.id=? ";
			}
			sql += " ) ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<SpiderBean> list = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			if(type == 1){
				for (int i = 0; i < uids.length; i++) {
					stmt.setInt(i + 1, Integer.parseInt(uids[i]));
				}
			}else{
				stmt.setString(1, uid);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spiderBean = new SpiderBean();
				spiderBean.setUserId(userid);
				spiderBean.setGoodsdata_id(rs.getInt("goodsid"));
				spiderBean.setNumber(rs.getInt("quantity"));
				spiderBean.setName(rs.getString("name"));
				spiderBean.setUrl(rs.getString("url"));
				String img = rs.getString("img");
				if(img.indexOf(",") > -1){
					img = img.substring(1,img.indexOf(","));
				}else{
					img = img.substring(1,img.indexOf("]"));
				}
				spiderBean.setImg_url(img);
				spiderBean.setWeight(rs.getString("perWeight"));
				spiderBean.setWidth(rs.getString("width"));
				spiderBean.setNorm_least(rs.getInt("quantity")+"");
				spiderBean.setPerWeight(rs.getString("weight"));
				spiderBean.setFreight_free(0);
				spiderBean.setDelivery_time(rs.getString("ptime"));
				spiderBean.setSeilUnit(rs.getString("sellUnit"));
				spiderBean.setFree_shopping_company(rs.getString("method"));
				spiderBean.setFree_sc_days(rs.getString("posttime"));
				spiderBean.setRemark(rs.getString("remark"));
				spiderBean.setPreferential(1);
				spiderBean.setItemId(rs.getString("g.pid"));
				double sprice = rs.getDouble("sprice");
				double price = rs.getDouble("price");
				int s = (int)((sprice-price)/sprice*100);
				spiderBean.setPrice(rs.getString("price"));
				spiderBean.setDeposit_rate(s < 0 ? 0 : s);
				spiderBean.setGoodsUnit(rs.getString("gUnit"));
				String volume = ParseGoodsUrl.calculateVolume(spiderBean.getNumber(), spiderBean.getWidth(), spiderBean.getSeilUnit(), spiderBean.getGoodsUnit());
				String weight = ParseGoodsUrl.calculateWeight(spiderBean.getNumber(),spiderBean.getPerWeight(), spiderBean.getSeilUnit(), spiderBean.getGoodsUnit());
				spiderBean.setBulk_volume(volume);
				spiderBean.setTotal_weight(weight);
				spiderBean.setColor("");
				spiderBean.setSize("");
				String guId = UUID.randomUUID().toString().replaceAll("-", "");
				spiderBean.setGuId(guId);
				spiderBean.setFree_shopping_company(rs.getString("method"));
				spiderBean.setFree_sc_days(rs.getString("posttime"));
				spiderBean.setCurrency(rs.getString("currency"));
				spiderBean.setpWprice("");
				spiderBean.setFreight("");
				list.add(spiderBean);
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
	public int getPauid(String uid, int userid) {
		String sql = "select id from paprestrain where suserid=? and uid=? and state=0";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(2, uid);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				res = 1;
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
	public int upPauid(String uid,int type) {
		String sql = "update paprestrain set state = 1 where uid=?";
		String[] uids = uid.split("@");
		if(type == 1){
			sql = "update paprestrain set state = 1 where ";
			if(uids.length > 0){
				sql += "  pid=? ";
			}
			for (int i = 1; i < uids.length; i++) {
				sql += " or pid=? ";
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			if(type == 1){
				for (int i = 0; i < uids.length; i++) {
					stmt.setInt(i + 1, Integer.parseInt(uids[i]));
				}
			}else{
				stmt.setString(1, uid);
			}
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
	public int upPA(String uid,int type) {
		String sql = "update preferential_application p set state = 1 , handletime = now(),cart_add = cart_add+1 where id in (select pid from paprestrain where uid=?)";
		String[] uids = uid.split("@");
		if(type == 1){
			sql = "update preferential_application p set state = 1 , handletime = now(),cart_add = cart_add+1 where ";
			if(uids.length > 0){
				sql += " p.id=? ";
			}
			for (int i = 1; i < uids.length; i++) {
				sql += " or p.id=? ";
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			if(type == 1){
				for (int i = 0; i < uids.length; i++) {
					stmt.setInt(i + 1, Integer.parseInt(uids[i]));
				}
			}else{
				stmt.setString(1, uid);
			}
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
	public int upPA(int pid) {
		String sql = "update preferential_application p set state = 1 , handletime = now(),cart_add = cart_add+1 where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, pid);
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
	public int savePostageD(PostageDiscounts pd) {
		String sql = "insert postage_discounts(userid,sessionid,name,email,phone,countryid,shopping_company,shopping_price,ip,coi,createtime,weight) values(?,?,?,?,?,?,?,?,?,?,now(),?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, pd.getUserid());
			stmt.setString(2,pd.getSessionid());
			stmt.setString(3, pd.getName());
			stmt.setString(4, pd.getEmail());
			stmt.setString(5, pd.getPhone());
			stmt.setInt(6, pd.getCountryid());
			stmt.setString(7, pd.getShopping_company());
			stmt.setString(8, pd.getShopping_price());
			stmt.setString(9, pd.getIp());
			stmt.setInt(10, pd.getCoi());
			stmt.setString(11, pd.getWeight());
			result = stmt.executeUpdate();
			if (result == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					result = rs.getInt(1);
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
		return result;
	}

	@Override
	public PostageDiscounts getPostageD(int userid,String sessionid) {
		String sql = "select id,userid,sessionid,name,email,phone,countryid,shopping_company,shopping_price,ip,coi,createtime from paprestrain where state=0 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		PostageDiscounts pd = new PostageDiscounts();
		try {
			if(userid != 0){
				sql += " and userid=? ";
			}else{
				sql += " and sessionid=? ";
			}
			stmt = conn.prepareStatement(sql);
			if(userid != 0){
				sql += " and userid=? ";
			}else{
				sql += " and sessionid=? ";
			}
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				pd.setId(rs.getInt("id"));
				pd.setUserid(userid);
				pd.setEmail(rs.getString("email"));
				pd.setName(rs.getString("name"));
				pd.setSessionid(rs.getString("sesisonid"));
				pd.setPhone(rs.getString("phone"));
				pd.setCoi(rs.getInt("coi"));
				pd.setCountryid(rs.getInt("coi"));
				pd.setShopping_company(rs.getString("shopping_company"));
				pd.setShopping_price(rs.getString("shopping_price"));
				pd.setIp(rs.getString("ip"));
				pd.setCreatetime(rs.getString("createtime"));
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
		
		return pd;
	}

	@Override
	public int upPostageD(PostageDiscounts pd) {
		String sql = "update postage_discounts set name=? ,email=?,phone=?,shopping_company=?,shopping_price=? , countryid=? , ip=? , coi=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pd.getName());
			stmt.setString(2, pd.getEmail());
			stmt.setString(3, pd.getPhone());
			stmt.setString(4, pd.getShopping_company());
			stmt.setString(5, pd.getShopping_price());
			stmt.setInt(6, pd.getCountryid());
			stmt.setString(7, pd.getIp());
			stmt.setInt(8, pd.getCoi());
			stmt.setInt(9, pd.getId());
			res = stmt.executeUpdate();
			if(res > 0)res=pd.getId();
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
	public int updateRemark(int id, String remark) {
		// TODO Auto-generated method stub
		String sql = "update preferential_application set remark=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, remark);
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
	public List<ClassDiscount> getClass_discount() {
		String sql = "select id,classname,price,deposit_rate,showname from class_discount ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<ClassDiscount> list = new ArrayList<ClassDiscount>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				ClassDiscount cd = new ClassDiscount();
				cd.setId(rs.getInt("id"));
				cd.setClassname(rs.getString("classname"));
				cd.setPrice(rs.getInt("price"));
				cd.setDeposit_rate(rs.getDouble("deposit_rate"));
				cd.setShowname(rs.getString("showname"));
				list.add(cd);
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

}
