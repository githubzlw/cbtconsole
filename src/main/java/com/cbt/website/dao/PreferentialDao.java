package com.cbt.website.dao;

import com.cbt.bean.PaInteracted;
import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.Preferential;
import com.cbt.bean.PreferentialWeb;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreferentialDao implements WebsitePreferentialDao {

	@Override
	public Preferential getPreferential(Preferential preferential) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PreferentialWeb> getPreferentials(int type, int userid, int page, int endpage, String hdate, String cdate, String email, int adminid) {
		//String Besql ="update   admin_r_user  inner join user  on admin_r_user.userid=user.id  set  admin_r_user.useremail=user.email";
		/*
		String sq1 = "select sql_calc_found_rows aru.admName,pa.id, ifnull(aru.userid, pa.userid) userid,pa.sessionid,pano,ifnull(aru.useremail, pa.username) username,g.name,goodsid,quantity,pa.url, "
				+ " pre_img,img,mOrder,quantity,pa.sPrice,pa.state,remark,pa.createtime,handletime,pa.email, (select country from zone where pa.country=id ) country,pa.goods_types,effectivetime, "
				+ " syseffectivetime,goods_unit,discountedunitprice,pa.currency,pa.itemId,user.email from  "
				+" preferential_application pa inner join  goodsdata g  on pa.url = g.url  left join admin_r_user aru on (pa.email=aru.useremail or (pa.userid = aru.userid  and pa.userid !=0))  left join user on user.id=pa.userid where 1=1 ";
		*/
		String sq1 = "select sql_calc_found_rows aru.admName,pa.id, ifnull(aru.userid, pa.userid) userid,pa.sessionid,pano,ifnull(aru.useremail, pa.username) username,ifnull(goodsname, '') as name,goodsid,quantity,pa.url, "
				+ " pre_img,quantity,pa.sPrice,pa.state,remark,pa.createtime,handletime,pa.email, (select country from zone where pa.country=id ) country,pa.goods_types,effectivetime, "
				+ " syseffectivetime,goods_unit,discountedunitprice,pa.currency,pa.itemId,user.email from  "
				+" preferential_application pa   left join admin_r_user aru on pa.userid = aru.userid   left join user on user.id=pa.userid where 1=1 and pa.userid != 0 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		//PreparedStatement stmt4 = null;
		List<PreferentialWeb> oblist = new ArrayList<PreferentialWeb>();
		if(adminid !=1){
			sq1+=" and  aru.adminid = ? ";
		}else if(adminid ==1){
			if(type==-2){ //未分配
				sq1+=" and  aru.adminid is null";
			}
		}
		try {
			if(userid != 0){
				sq1 += " and pa.userid=?";
			}
			if(type > -1){
				sq1 += " and pa.state=?";
			}
			if(!hdate.equals("")){
				sq1 += " and date_format(createtime,'%Y-%m-%d')=?";
			}
			if(!cdate.equals("")){
				sq1 += " and date_format(createtime,'%Y-%m-%d')=?";
			}
			if(email!=null&&!email.equals("")){
				sq1 += " and pa.email=? ";
			}
			
			sq1 += " 	group by  pa.id order by pa.createtime desc limit ?, ?";
			//stmt4=conn.prepareStatement(Besql);
			//stmt4.executeUpdate(Besql);//同步信息
			System.out.println("sq1==>"+sq1);
			stmt = conn.prepareStatement(sq1);
			stmt3 = conn.prepareStatement("select found_rows();");
			int i = 1;
			if(adminid !=1){
				stmt.setInt(i, adminid);
				i += 1;
			}
			if(userid != 0){
				stmt.setInt(i, userid);
				i += 1;
			}
			if(type > -1){
				stmt.setInt(i, type);
				i += 1;
			}
			if(!hdate.equals("")){
				stmt.setString(i, hdate);
				i += 1;
			}
			if(!cdate.equals("")){
				stmt.setString(i, cdate);
				i += 1;
			}
			if(email!=null&&!email.equals("")){ 
				stmt.setString(i, email);
				i += 1;
			}
			stmt.setInt(i, page);
			stmt.setInt(i+1, endpage);
			rs = stmt.executeQuery();
			rs3 = stmt3.executeQuery();
			PreferentialWeb preferentialWeb;
			List<PaInteracted> paInteracteds;
			PaInteracted paInteracted;
			Preferential preferential;
		
			String strpa="";
			int total = 0;
			while(rs3.next()){
				total = rs3.getInt("found_rows()");
			}
			paInteracteds = new ArrayList<PaInteracted>();
			
			while (rs.next()) {
				preferentialWeb = new PreferentialWeb();
				preferentialWeb.setTotal(total);
				preferential = new Preferential();
				int paid = rs.getInt("id");
				preferential.setCountry(rs.getString("country"));
				preferential.setCreatetime(rs.getString("createtime"));
				String email_res = rs.getString("email");
				email_res = Utility.getStringIsNull(email_res) ? email_res : rs.getString("user.email");
				preferential.setEmail(email_res);
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
				preferential.setPrice(rs.getString("discountedunitprice"));
				String imgString = rs.getString("pre_img");
				preferentialWeb.setImg(imgString);
				String effectivetime = rs.getString("syseffectivetime");
				if(!Utility.getStringIsNull(effectivetime)){
					effectivetime = rs.getString("effectivetime");
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
				Date date=sdf.parse(effectivetime);
				boolean eend = date.before(new Date());
				preferential.setEffectiveend(eend ? 1 : 0);
				preferential.setEffectivetime(effectivetime);
				preferential.setSessionid(rs.getString("sessionid"));
				preferential.setCurrency(rs.getString("currency"));
				preferential.setAdmname(rs.getString("admName"));
				preferential.setGoods_types(rs.getString("goods_types"));
				preferential.setItemId(rs.getString("itemId"));
				preferentialWeb.setPreferential(preferential);
				/*String img = rs.getString("img");
				if(img.indexOf(",") > -1){
					img = img.substring(1,img.indexOf(","));
				}else{
					img = img.substring(1,img.indexOf("]"));
				}
				preferentialWeb.setImg(Utility.getStringIsNull(imgString) ? imgString : img);*/
				preferentialWeb.setUrl(rs.getString("url"));
				//preferentialWeb.setmOrder(rs.getString("mOrder"));
				preferentialWeb.setUsername(rs.getString("username"));
				preferentialWeb.setName(rs.getString("name"));
				preferentialWeb.setPaid(paid);
				
				if(strpa.length()==0)
				{
					strpa=Integer.toString(paid);
				}
				else
				{
					strpa=strpa+','+Integer.toString(paid);
				}
				oblist.add(preferentialWeb);
			
			}
			if(Utility.getStringIsNull(strpa)){
				//开始查辅助信息
				//String sqlpa = "select paid, id,price,quantity,type,createtime,sysid,message from pa_interacted where paid in ("+strpa+") order by id desc";
				String sqlpa = "select paid, id,price,quantity,type,createtime,sysid,message from pa_interacted where paid in ("+strpa+") order by paid,createtime desc";
			//	sqlpa.replace("?", strpa);
				System.out.println("sql2==>"+sqlpa);
				stmt2=conn.prepareStatement(sqlpa);
				//stmt2.setString(1, strpa);
			
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					paInteracted = new PaInteracted();
					paInteracted.setCreatetime(rs2.getString("createtime"));
					paInteracted.setPrice(rs2.getDouble("price"));
					paInteracted.setQuantity(rs2.getInt("quantity"));
					paInteracted.setMessage(rs2.getString("message"));
					paInteracted.setSysid(rs2.getInt("sysid"));
					paInteracted.setType(rs2.getInt("type"));
					paInteracted.setPaid(rs2.getInt("paid"));
					paInteracteds.add(paInteracted);
				}
			}
			
			
			List<PaInteracted> pas = new ArrayList<PaInteracted>();
			for (int icnt = 0; icnt < oblist.size(); icnt++) {
				for (int icnt1 = 0; icnt1 < paInteracteds.size(); icnt1++) {
					/*if( paInteracteds.get(icnt1).getPaid()==oblist.get(icnt).getPaid())
					{
						//pas.add(paInteracteds.get(icnt1));
						//oblist.get(icnt).addPaInteracteds(paInteracteds.get(icnt1));
						pas.add(paInteracteds.get(icnt1));
						oblist.get(icnt).setPaInteracteds(pas);
					}*/
					
					if (paInteracteds.get(icnt1).getPaid() == oblist.get(icnt).getPaid()) {
						pas.add(paInteracteds.get(icnt1));
						oblist.get(icnt).setPaInteracteds(pas);
					} else {
						pas = new ArrayList<PaInteracted>();
					}
				}
				if(oblist.get(icnt).getPaInteracteds() == null){
					oblist.get(icnt).setPaInteracteds(pas);
				}
				// oblist.get(icnt).setPaInteracteds(pas);
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
			if (rs2 != null) {
				try {
					rs2.close();
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
			if (stmt3 != null) {
				try {
					stmt3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
	}
		
		return oblist;
	}
	
	@Override
	public int getPreferentialsNumber(int type,int userid) {
		String sql = "select count(pa.id) coun from preferential_application pa where 1=1 ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			if(userid != 0){
				sql += " and userid=?";
			}
			if(type != -1){
				sql += " and state=?";
			}
			sql += " order by pa.userid desc ";
			stmt = conn.prepareStatement(sql);
			int i = 1;
			if(userid != 0){
				stmt.setInt(i, userid);
				i += 1;
			}
			if(type != -1){
				stmt.setInt(i, type);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				 res = rs.getInt("coun");
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
	public double getPreferentialsPayPrice(int id) {
		String sql = "select pay_price from preferential_application pa where pa.id=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				 res = rs.getDouble("pay_price");
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
	
	
	
public static void main(String[] args) {

	Date deta1 = new Date();
	Date deta2 = new Date();
	boolean flag = deta1.before(deta2);
	System.out.println(flag);
}
	@Override
	public int upPreferential(Preferential preferential) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int savePai(List<Object[]> list,String uid,int userid,String sessionid) {
			String sql = "insert into pa_interacted(paid,gid,price,type,createtime) values(?,?,?,0,now())";
			Connection conn = DBHelper.getInstance().getConnection();
			Connection conn2 = DBHelper.getInstance().getConnection2();
			PreparedStatement stmt = null;
			PreparedStatement stmt2 = null;
			PreparedStatement ps1 = null;
			PreparedStatement ps2 = null;

			int result = 0;
			try {
				stmt = conn.prepareStatement(sql,  PreparedStatement.RETURN_GENERATED_KEYS);
				ps1 = conn2.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < list.size(); i++) {
					stmt.setInt(1, Integer.parseInt(list.get(i)[0].toString()));
					stmt.setInt(2, Integer.parseInt(list.get(i)[1].toString()));
					stmt.setDouble(3, Double.parseDouble(list.get(i)[2].toString()));
					stmt.addBatch();
					
					ps1.setInt(1, Integer.parseInt(list.get(i)[0].toString()));
					ps1.setInt(2, Integer.parseInt(list.get(i)[1].toString()));
					ps1.setDouble(3, Double.parseDouble(list.get(i)[2].toString()));
					ps1.addBatch();
				}
				int[] results = stmt.executeBatch();
				ps1.executeBatch();
				
				String sql1 = "insert into paprestrain(pid,uid,suserid,sessionid,createtime) values(?,?,?,?,now())";
				stmt2 = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS);
				ps2 = conn2.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < list.size(); i++) {
					stmt2.setInt(1, Integer.parseInt(list.get(i)[0].toString()));
					stmt2.setString(2, uid);
					stmt2.setInt(3, userid);
					stmt2.setString(4, sessionid);
					stmt2.addBatch();
					
					ps2.setInt(1, Integer.parseInt(list.get(i)[0].toString()));
					ps2.setString(2, uid);
					ps2.setInt(3, userid);
					ps2.setString(4, sessionid);
					ps2.addBatch();
				}
				stmt2.executeBatch();
				ps2.executeBatch();
				
				result = results.length;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ps1 != null) {
					try {
						ps1.close();
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
				if (ps2 != null) {
					try {
						ps2.close();
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
				DBHelper.getInstance().closeConnection(conn2);
			}
			return result;
		}

	@Override
	public int savePaprestrain(String uid, int userid) {
		String sql = "insert paprestrain(uid,suserid,createtime) values(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(2, userid);
			stmt.setString(1, uid);
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
	public int delPaprestrain(int pid,String cancel_reason) {
		String sql = "update preferential_application set state=2 ,cancel_reason=?,cancel_obj=2 where id=?";
		Connection conn = DBHelper.getInstance().getConnection2();
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
	public int upPaconfirm(String[] uid) {
		String sql = "update preferential_application set confirm = 1 where  ";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			sql += " id=? ";
			for (int i = 1; i < uid.length; i++) {
				sql += " or id=? ";
			}
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < uid.length; i++) {
				stmt.setInt(i+1, Integer.parseInt(uid[i]));
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
	public int uptimePainteracted(int pid, String time) {
		String sql = "update preferential_application set syseffectivetime = DATE_ADD(NOW(),INTERVAL 1 MONTH) where  id = ?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
				stmt.setInt(1, pid);
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
	public List<PostageDiscounts> getPostageD(int userid, int type, int page, int endpage, String email) {
		String sql = "select admuser.admName,pd.id,pd.userid,sessionid,name,pd.email,phone,(select country from zone where pd.countryid=id ) country,shopping_company,shopping_price,ip,coi,createtime,handleman,handletime,state,weight from postage_discounts pd "
				    + " left join admin_r_user on pd.email=admin_r_user.useremail left join admuser on admuser.id=admin_r_user.adminid"
					+ " where state!=2 ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<PostageDiscounts> list = new ArrayList<PostageDiscounts>();
		try {
			if(userid != 0){
				sql+= " and userid=? ";
			}
			if(type != -1){
				sql+= " and state=? ";
			}
			if(!email.equals("")&&email!=null){
				sql+= " and email=?";
			}
			sql += " order by id desc limit ?, ? ";
			stmt = conn.prepareStatement(sql);
			int i=1;
			if(userid != 0){
				stmt.setInt(i, userid);
				i++;
			}
			if(type != -1){
				stmt.setInt(i, type);
				i++;
			}
			if(!email.equals("")&&email!=null){
				stmt.setString(i, email);
				i++;
			}
			stmt.setInt(i, page);
			stmt.setInt(i+1, endpage);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PostageDiscounts pd = new PostageDiscounts();
				pd.setId(rs.getInt("id"));
				pd.setUserid(rs.getInt("userid"));
				pd.setEmail(rs.getString("email"));
				pd.setName(rs.getString("name"));
				pd.setCountry(rs.getString("country"));
				pd.setSessionid(rs.getString("sessionid"));
				pd.setPhone(rs.getString("phone"));
				pd.setCoi(rs.getInt("coi"));
				pd.setCountryid(rs.getInt("coi"));
				pd.setShopping_company(rs.getString("shopping_company"));
				pd.setShopping_price(rs.getString("shopping_price"));
				pd.setIp(rs.getString("ip"));
				pd.setCreatetime(rs.getString("createtime"));
				pd.setHandleman(rs.getString("handleman"));
				pd.setHandletime(rs.getString("handletime"));
				pd.setHandlestate(rs.getInt("state"));
				pd.setWeight(rs.getString("weight"));
				pd.setAdmname(rs.getString("admName"));
				list.add(pd);
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
	public int upPostageD(int id, int type, String handleman) {
		String sql = "update postage_discounts set handleman=?,handletime=now(),state=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, handleman);
			stmt.setInt(2, type);
			stmt.setInt(3, id);
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
	public int getPostageDNumber(int userid, int type) {
		String sql = "select count(id) coun from postage_discounts where state!=2 ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			if(userid != 0){
				sql+= " and userid=? ";
			}
			if(type != -1){
				sql+= " and state=? ";
			}
			stmt = conn.prepareStatement(sql);
			int i=1;
			if(userid != 0){
				stmt.setInt(i, userid);
				i++;
			}
			if(type != -1){
				stmt.setInt(i, type);
			}
			rs = stmt.executeQuery();
			rs = stmt.executeQuery();
			if (rs.next()) {
				 res = rs.getInt("coun");
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
	public int updatePriceById(double price, int id){
		//8.23 修改价格 同时修改状态为 已处理
//		String sql = "update preferential_application set discountedunitprice=?,state=1 where id=?";
		String sql = "update preferential_application set discountedunitprice=?,totalprice=(quantity*?)  where id=?";
		Connection con = DBHelper.getInstance().getConnection();
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement ps1=null,ps2=null;
		int i =0;
		try {
			ps1 = con.prepareStatement(sql);
			ps1.setDouble(1, price);
			ps1.setDouble(2, price);
			ps1.setInt(3, id);
			
			ps2 = conn.prepareStatement(sql);
			ps2.setDouble(1, price);
			ps2.setDouble(2, price);
			ps2.setInt(3, id);
			i = ps1.executeUpdate();
			i+= ps2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(ps1!=null){
				try {
					ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps2!=null){
				try {
					ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		DBHelper.getInstance().closeConnection(con);
		return i;
	}
	
	@Override
	public int updateGoodsCarPrice(double price, String itemId){
		//4.10 修改价格
		String sql = "update goods_car set googs_price=?  where preferential=?";
		Connection con = DBHelper.getInstance().getConnection();
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement ps1=null,ps2=null;
		int i =0;
		try {
			ps1 = con.prepareStatement(sql);
			ps1.setDouble(1, price);
			ps1.setString(2, itemId);
			
			ps2 = conn.prepareStatement(sql);
			ps2.setDouble(1, price);
			ps2.setString(2, itemId);
			i = ps1.executeUpdate();
			i+= ps2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(ps1!=null){
				try {
					ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps2!=null){
				try {
					ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		DBHelper.getInstance().closeConnection(con);
		return i;
	}
	
	@Override
	public int updatePayPrice(String itemId, double payPrice){
		//8.23 修改价格 同时修改状态为 已处理
//		String sql = "update preferential_application set discountedunitprice=?,state=1 where id=?";
		String sql = "update preferential_application set pay_price=?  where itemId=?";
		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement ps1=null;
		int i =0;
		try {
			ps1 = con.prepareStatement(sql);
			ps1.setDouble(1, payPrice);
			ps1.setString(2, itemId);

			List<String> lstValues = new ArrayList<>(2);
			lstValues.add( String.valueOf(payPrice));
			lstValues.add( itemId);
			String runSql = DBHelper.covertToSQL(sql,lstValues);
			SendMQ sendMQ = new SendMQ();
			sendMQ.sendMsg(new RunSqlModel(runSql));
			sendMQ.closeConn();

			i = ps1.executeUpdate();
			++i;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(ps1!=null){
				try {
					ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(con);
		return i;
	}
	
	
	
}
