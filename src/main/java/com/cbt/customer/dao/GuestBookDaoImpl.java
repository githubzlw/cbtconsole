package com.cbt.customer.dao;

import com.cbt.bean.GuestBookBean;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GuestBookDaoImpl implements IGuestBookDao {

	@Override
	public int addComment(GuestBookBean gbb) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "insert into guestbook(user_id,user_name,pid,pname,price,purl,pimg,content,create_time,status,online_url) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date now = new Date();
		try {
			Date parse = dateFormat.parse(dateFormat.format(now));
			stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, gbb.getUserId());
			stmt.setString(2, gbb.getUserName());
			stmt.setString(3, gbb.getPid());
			stmt.setString(4, gbb.getPname());
			stmt.setString(5, gbb.getPrice());
			stmt.setString(6, gbb.getPurl());
			stmt.setString(7, gbb.getPimg());
			stmt.setString(8, gbb.getContent());
			stmt.setTimestamp(9, new Timestamp(parse.getTime()));
			stmt.setInt(10, 0);// 默认是未回复
			stmt.setString(11, gbb.getOnlineUrl());//添加线上链接数据
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public List<GuestBookBean> findByPid(String pid, int userId, int start, int number) {
		List<GuestBookBean> gbbList = new ArrayList<GuestBookBean>();
		;
		String sql = "select * from guestbook where pid=? and user_id=? order by reply_time desc limit ?,?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			stmt.setInt(2, userId);
			stmt.setInt(3, start);
			stmt.setInt(4, number);
			rs = stmt.executeQuery();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
			while (rs.next()) {
				GuestBookBean gbb = new GuestBookBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setUserName(rs.getString("user_name"));
				gbb.setPid(rs.getString("pid"));
				gbb.setPname(rs.getString("pname"));
				gbb.setPrice(rs.getString("price"));
				gbb.setPurl(rs.getString("purl"));
				gbb.setPimg(rs.getString("pimg"));
				gbb.setContent(rs.getString("content"));
				gbb.setOnlineUrl(rs.getString("online_url"));//获取线上链接数据
				java.sql.Date create_time = rs.getDate("create_time");
				if (create_time != null) {
					gbb.setCreateTime(new Date(create_time.getTime()));
					gbb.setShowTime(dateFormat.format(gbb.getCreateTime()));
				}
				gbb.setReplyContent(rs.getString("reply_content"));
				java.sql.Date reply_time = rs.getDate("reply_time");
				if (reply_time != null) {
					gbb.setReplyTime(new Date(reply_time.getTime()));
				}
				gbb.setStatus(rs.getInt("status"));
				gbbList.add(gbb);
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
		return gbbList;
	}

	@Override
	public GuestBookBean findById(int id) {
		GuestBookBean gbb = null;
		String sql = "select * from guestbook where id=?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				gbb = new GuestBookBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setUserName(rs.getString("user_name"));
				gbb.setPid(rs.getString("pid"));
				gbb.setPname(rs.getString("pname"));
				gbb.setPrice(rs.getString("price"));
				gbb.setPurl(rs.getString("purl"));
				gbb.setPimg(rs.getString("pimg"));
				gbb.setContent(rs.getString("content"));
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setStatus(rs.getInt("status"));
				gbb.setOnlineUrl(rs.getString("online_url"));//获取线上链接数据
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
		return gbb;
	}

	@Override
	public List<GuestBookBean> findAll(int userId, String date, // 查询留言
			int state, String userName, String pname, int start, int end, String useremail, String timeFrom,
			String timeTo, int adminid,int type) {
		List<GuestBookBean> gbbList = new ArrayList<GuestBookBean>();
		// 新的sql
		String sql = "select distinct m.*,ifnull(n.eid,'0') eid from (select   g.picPath,g.id,g.user_id,g.user_name,g.pid,g.pname,g.price,g.purl,g.pimg,g.questionType,";
				if(type==0){ // 全部
					sql+="g.content,g.orderQuantity,g.customizationNeed,g.targetPrice";
				}
		        if(type==1){ //qustion
					sql+="REPLACE(REPLACE(g.content, CHAR(10), ''), CHAR(13), '') as content" ;
				}if(type==2){ //Request Business Discount
					sql+="CONCAT('orderQuantity: ',g.orderQuantity,';  targetPrice: ',g.targetPrice) as  content";
				}if(type==3){ //Request Customization
					sql+="CONCAT('orderQuantity: ',g.orderQuantity,';  customizationNeed: ',g.customizationNeed) as  content";
				}
		        sql+=",g.create_time,g.reply_content,"
				+ "g.reply_time,g.status,ifnull(u.email,'') useremail ,ifnull(u.businessName,'') as bname ,"
				+ "ifnull(ad.admName ,'')  admName ,ifnull(er.id,'0')eid,g.online_url from guestbook  g left join user  u  on  g.user_id=u.id "
				+ "left join admin_r_user  au ON  u.id=au.userid "
				+ "LEFT JOIN admuser ad ON au.adminid=ad.id "
				+ " left join email_receive er  on er.question_id=g.id)m "
				+ "left join (select question_id,max(id) eid from email_receive order by question_id)n on m.id=n.question_id where 1=1";

				

		if (userId != 0) {
			sql += " and m.user_id="+userId+"";
		}
		if (adminid != 0) {
			sql += " AND m.user_id IN (SELECT userid FROM admin_r_user aru,guestbook gb WHERE aru.userid = gb.user_id AND aru.adminid = "+adminid+")";
		}
		if (timeFrom != null && !"".equals(timeFrom)) {
			sql += " and m.create_time >='"+timeFrom+" 00:00:00'";
		}
		if (timeTo != null && !"".equals(timeTo)) {
			sql += " and m.create_time<='"+timeTo+" 23:59:59'";
		}
		/*if (date != null && !"".equals(date)) {
			sql += " and m.create_time = '"+date+"'";
		}*/
		if (state != -1) {
			sql += " and m.status="+state+"";
		}
		if (userName != null && !"".equals(userName)) {
			sql += " and m.user_name='"+userName+"'";
		}
		if (pname != null && !"".equals(pname)) {
			sql += " and m.pname like '%"+pname+"%'";
		}
		if (useremail != null && !useremail.equals("")) {
			sql += " and m.useremail='"+useremail+"'";
		}
		if(type>0){
			sql += " and m.questionType = "+type+" ";
		}
		sql += " and m.status < 2 " ;
		sql += " order by m.id desc limit "+start+", "+end+"";
		System.out.println(sql);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				GuestBookBean gbb = new GuestBookBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setUserName(rs.getString("user_name"));
				gbb.setPid(rs.getString("pid"));
				gbb.setPname(rs.getString("pname"));
				gbb.setBname(rs.getString("bname"));// 2.6 添加公司名称
				gbb.setPrice(rs.getString("price"));
				gbb.setPicPath(rs.getString("picPath"));
				//商品url链接跳转到线上
				//新url
 //             String uuid = Md5Util.encoder(rs.getString("purl"));
                gbb.setPurl(rs.getString("purl"));
//              gbb.setPurl("source=I"+uuid+"&item="+rs.getString("pid"));
//				gbb.setPurl(TypeUtils.encodeGoods(rs.getString("purl")));
				gbb.setPimg(rs.getString("pimg"));
				gbb.setOnlineUrl(rs.getString("online_url"));//获取线上链接数据
				if (rs.getString("content") != null) {
					gbb.setContent(rs.getString("content").replace("'", "").replace("\"", ""));
				}
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setReplyContent(rs.getString("reply_content"));
				gbb.setReplyTime(rs.getDate("reply_time"));
				gbb.setStatus(rs.getInt("status"));
				gbb.setEmail(rs.getString("useremail"));
				gbb.setAdmname(rs.getString("admName"));
				gbb.setEid(rs.getInt("eid"));
				gbb.setQuestionType(rs.getInt("questionType"));
				if(type==0){
					gbb.setOrderQuantity(rs.getString("orderQuantity"));
					gbb.setTargetPrice(rs.getString("targetPrice"));
					gbb.setCustomizationNeed(rs.getString("customizationNeed"));
				}
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	@Override
	public int replyReport(int id, String replyContent, String date) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		conn = DBHelper.getInstance().getConnection();
		ResultSet rs=null;
		try {
			sql="select reply_content from problem_report where id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				String old_reply_content=rs.getString("reply_content");
				replyContent=StringUtils.isStrNull(old_reply_content)?replyContent:rs.getString("reply_content").toString()+"<br>"+replyContent;
			}
			sql = "update problem_report set reply_content=?,reply_time=?,status=? where id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, replyContent);
			stmt.setString(2, date);
			stmt.setInt(3, 1);// 状态改为已回复
			stmt.setInt(4, id);
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

	@Override
	public int reply(int id, String replyContent, String date) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		conn = DBHelper.getInstance().getConnection();
		Connection conn2 = DBHelper.getInstance().getConnection2();
		ResultSet rs=null;
		try {
			sql="select reply_content from guestbook where id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				String old_reply_content=rs.getString("reply_content");
				replyContent=old_reply_content==null || "".equals(old_reply_content)?replyContent:rs.getString("reply_content").toString()+"<br>"+replyContent;
			}
			sql = "update guestbook set reply_content=?,reply_time=?,status=? where id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, replyContent);
			stmt.setString(2, date);
			stmt.setInt(3, 1);// 状态改为已回复
			stmt.setInt(4, id);
			result = stmt.executeUpdate();
			//将评论更新到aws库中以便回复在产品单页展示  王宏杰 2018-04-27
			sql = "update guestbook set reply_content=?,reply_time=?,status=? where status=0 and id=? ";
			stmt = conn2.prepareStatement(sql);
			stmt.setString(1, replyContent);
			stmt.setString(2, date);
			stmt.setInt(3, 1);// 状态改为已回复
			stmt.setInt(4, id);
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
			DBHelper.getInstance().closeConnection(conn2);
		}
		return result;
	}
	/**
	 * 方法描述:查看ID所有留言
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @return
	 */
	@Override
	public GuestBookBean getGuestBookBean(String gid){
//		String qustion = request.getParameter("qustion");
//		String name = request.getParameter("name");
//		String pname = request.getParameter("pname");
//		String email = request.getParameter("email");
//		String userId = request.getParameter("userId");
//		String purl = request.getParameter("purl");
		String sql;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		GuestBookBean g=new GuestBookBean();
		try{
			sql="select g.questionType,g.user_id,g.orderQuantity,g.targetPrice,g.user_name,g.pname,g.content,g.online_url,u.email,ad.email as sale_email  from guestbook g left join user u on g.user_id=u.id 	inner join admin_r_user a on u.id=a.userid inner join admuser ad on a.adminid=ad.id  where g.id='"+gid+"'";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				g.setUserId(rs.getInt("user_id"));
				g.setUserName(rs.getString("user_name"));
				g.setPname(rs.getString("pname"));
				g.setContent(rs.getString("content"));
				g.setPurl(rs.getString("online_url"));
				g.setEmail(rs.getString("email"));
				g.setQuestionType(rs.getInt("questionType"));
				g.setOrderQuantity(rs.getString("orderQuantity"));
				g.setTargetPrice(rs.getString("targetPrice"));
				g.setSale_email(rs.getString("sale_email"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return g;
	}

	@Override
	public int delreply(int id) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
//		sql = "delete from guestbook where id=?";
		sql = "update  guestbook  set  status = 2  where id=?";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);// 状态改为已回复
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

	@Override
	public int total(int userId, String date, int state, String userName, String pname, int start, int end,
			String timeFrom, String timeTo, int adminid,int type) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "select count(*) from guestbook  g left join user  u  on  g.user_id=u.id "
				+ "left join admin_r_user  au on  u.email=au.useremail where 1=1";	
		if (userId != 0) {
			sql += " and g.user_id="+userId+"";
		}
		if (adminid != 1 && adminid != 0) {
			sql += " AND g.user_id IN (SELECT userid FROM admin_r_user aru,guestbook gb WHERE aru.userid = gb.user_id AND aru.adminid = "+adminid+")";
		}
		if (timeFrom != null && !"".equals(timeFrom) && timeTo != null && !"".equals(timeTo)) {
			sql += " and g.create_time>='"+timeFrom+"' AND g.create_time<='"+timeTo+"'";
		}
		if (date != null && !"".equals(date)) {
			sql += " and g.create_time = '"+date+"'";
		}
		if (state != -1) {
			sql += " and g.status="+state+"";
		}
		if (userName != null && !"".equals(userName)) {
			sql += " and g.user_name='"+userName+"'";
		}
		if (pname != null && !"".equals(pname)) {
			sql += " and g.pname like '%"+pname+"%'";
		}
		if(type>0){
			sql +=" and g.questionType = "+type+" ";
		}
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
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

}
