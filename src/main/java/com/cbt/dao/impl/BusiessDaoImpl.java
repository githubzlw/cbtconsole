package com.cbt.dao.impl;

import com.cbt.bean.BusiessBean;
import com.cbt.dao.BusiessDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.warehouse.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BusiessDaoImpl implements BusiessDao {

	private static final Logger logger = Logger.getLogger(BusiessDaoImpl.class);

	@Override
	/**
	 *
	 */
	public int saveBusiess(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "insert into busiess(company,email,ordervalue,needs,userid,createtime,userphone) values(?,?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, params.get("company").toString());
			stmt.setString(2, params.get("email").toString());
			stmt.setString(3, params.get("ordervalue").toString());
			stmt.setString(4, params.get("needs").toString());
			stmt.setInt(5, Integer.parseInt(params.get("userid").toString()));
			stmt.setString(6, params.get("createtime").toString());
			stmt.setString(7, params.get("userphone").toString());
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
	public int getBusiessCountPage(BusiessBean busiessBean) {
		// TODO Auto-generated method stub
		Integer id = busiessBean.getId();
		String company = busiessBean.getCompany();
		String createtime = busiessBean.getCreatetime();
		String email = busiessBean.getEmail();
		String needs = busiessBean.getNeeds();
		String ordervalue = busiessBean.getOrdervalue();
		Integer userid = busiessBean.getUserid();
		Integer status = busiessBean.getStatus(); // status =2 未分配 =3 未处理 =4 全部
		Integer adminid = busiessBean.getAdminid();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlIf = new StringBuffer();
		sql.append("select count(distinct *) as count from busiess where 1=1 ");
		if (id != null) {
			sqlIf.append(" and id=" + id);
		}
		if (email != null && !"".equals(email)) {
			sqlIf.append(" and email='" + email + "'");
		}

		if (createtime != null && !"".equals(createtime)) {
			sqlIf.append(" and createtime='" + createtime + "'");
		}

		if (company != null && !"".equals(company)) {
			sqlIf.append(" and company='" + company + "'");
		}

		if (needs != null && !"".equals(needs)) {
			sqlIf.append(" and needs='" + needs + "'");
		}
		if (ordervalue != null && !"".equals(ordervalue)) {
			sqlIf.append(" and ordervalue='" + ordervalue + "'");
		}
		if (userid != null) {
			sqlIf.append(" and userid='" + userid + "'");
		}
		if (adminid != null && adminid != 1 && adminid != 83 && adminid != 84) {
			if (status == 3) {
				sql.append(" and busiess.status=0");
				sql.append(
						" and busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid="
								+ adminid + ")");
				sql.append(sqlIf);
			} else {
				sql.append(
						" and busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid="
								+ adminid + ")");
				sql.append(sqlIf);
			}
		} else if (adminid != null && (adminid == 1 || adminid == 83 || adminid == 84)) {
			if (status == 2) {
				sql.append(
						" and busiess.email not in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid)");
				sql.append(sqlIf);
			} else if (status == 3) {
				sql.append(" and busiess.status=0");
				sql.append(sqlIf);
			} else if (status == 4) {
				sql.append(sqlIf);
			}
		}

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int total = 0;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("count");
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
	public List<BusiessBean> getBusiessPage(BusiessBean busiessBean, int pagenum) {
		// TODO Auto-generated method stub
		Integer id = busiessBean.getId();
		String company = busiessBean.getCompany();
		String createtime = busiessBean.getCreatetime();
		String email = busiessBean.getEmail();
		String needs = busiessBean.getNeeds();
		String ordervalue = busiessBean.getOrdervalue();
		Integer userid = busiessBean.getUserid();
		Integer status = busiessBean.getStatus(); // status =2 未分配 =3 未处理 =4 全部
		Integer adminid = busiessBean.getAdminid();

		int state=busiessBean.getState();
		String startdate=busiessBean.getStartdate();
		String enddate=busiessBean.getEnddate();
		String admName=busiessBean.getAdmName();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlIf = new StringBuffer();
		if (id != null) {
			sqlIf.append(" and id=" + id);
		}

		if (email != null && !"".equals(email)) {
			sqlIf.append(" and busiess.email='" + email + "'");
		}

		if (StringUtil.isNotBlank(createtime)) {
			sqlIf.append(" and createtime='" + createtime + "'");
		}
		if(StringUtil.isNotBlank(startdate)){
			sqlIf.append(" and createtime >='" + startdate + " 00:00:00'");
		}

		if(StringUtil.isNotBlank(enddate)){
			sqlIf.append(" and createtime <='" + startdate + " 23:59:59'");
		}

		if(state != -1){
			sqlIf.append(" and status = "+state+"");
		}

		if (company != null && !"".equals(company)) {
			sqlIf.append(" and company='" + company + "'");
		}

		if (needs != null && !"".equals(needs)) {
			sqlIf.append(" and needs='" + needs + "'");
		}

		if (ordervalue != null && !"".equals(ordervalue)) {
			sqlIf.append(" and ordervalue='" + ordervalue + "'");
		}

		if (userid != null) {
			sqlIf.append(" and userid='" + userid + "'");
		}
		sqlIf.append(" order by createtime desc limit " + ((pagenum - 1) * 20) + "," + 20);

		if (adminid != null && adminid != 1 && adminid != 83 && adminid != 84) {
			if (status == 3) {
				sql.append(
						"select distinct busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(" and busiess.status=0");
				sql.append(
						" and busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.useremail = bu.email and aru.adminid="
								+ adminid + ")");
				sql.append(sqlIf);
			} else {
				sql.append(
						"select distinct busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(
						" and busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.useremail = bu.email and aru.adminid="
								+ adminid + ")");
				sql.append(sqlIf);
			}
		} else if (adminid != null && (adminid == 1 || adminid == 83 || adminid == 84)) {
			if (status == 2) {
				sql.append(
						"select distinct busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(
						" and busiess.email not in (select aru.useremail from admin_r_user aru,busiess bu where aru.useremail = bu.email");
				if(!"-1".equals(admName)){
					sql.append(" aru.adminid="+admName+"");
				}
				sql.append(")").append(sqlIf);
			} else if (status == 3) {
				sql.append(
						"select distinct busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(" and busiess.status=0");
				if(!"-1".equals(admName)){
					sql.append(" admin_r_user.adminid="+admName+" ");
				}
				sql.append(sqlIf);
			} else if (status == 4) {
				sql.append(
						"select distinct busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				if(!"-1".equals(admName)){
					sql.append(" admin_r_user.adminid="+admName+" ");
				}
				sql.append(sqlIf);
			}
		}

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<BusiessBean> list = new ArrayList<BusiessBean>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				BusiessBean busiessBeanpage = new BusiessBean();
				busiessBeanpage.setId(rs.getInt("id"));
				busiessBeanpage.setCompany(rs.getString("company"));
				busiessBeanpage.setCreatetime(rs.getString("createtime"));
				busiessBeanpage.setEmail(rs.getString("email"));
				busiessBeanpage.setNeeds(rs.getString("needs"));
				busiessBeanpage.setOrdervalue(rs.getString("ordervalue"));
				busiessBeanpage.setUserid(rs.getInt("userid"));
				busiessBeanpage.setUserphone(rs.getString("userphone"));
				busiessBeanpage.setAdmName(rs.getString("admName"));
				busiessBeanpage.setStatus(rs.getByte("status"));
				busiessBeanpage.setUpdatetime(rs.getTimestamp("updatetime"));
				// start add by yang_tao for 关联服装定制信息字段添加_2017年1月7日
				busiessBeanpage.setCustomizedId(rs.getInt("customizedId"));
				// end add by yang_tao for 关联服装定制信息字段添加_2017年1月7日
				busiessBeanpage.setFeedbackId(rs.getInt("feedbackId"));
				list.add(busiessBeanpage);
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

	/**
	 * 根据id获得唯一一条数据
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BusiessBean getById(int id) {
		String sql = "select id,company,email,ordervalue,needs,userid,createtime,userphone,status,updatetime,userip,customizedId from busiess where id="
				+ id + " limit 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		BusiessBean bean = new BusiessBean();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getInt("id"));
				bean.setCompany(rs.getString("company"));
				bean.setEmail(rs.getString("email"));
				bean.setOrdervalue(rs.getString("ordervalue"));
				bean.setNeeds(rs.getString("needs"));
				bean.setUserid(rs.getInt("userid"));
				bean.setCreatetime(rs.getString("createtime"));
				bean.setUserphone(rs.getString("userphone"));
				bean.setStatus(rs.getByte("status"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdatetime(rs.getTimestamp("userip"));
				bean.setCustomizedId(rs.getInt("customizedId"));
			}
		} catch (Exception e) {
			logger.error(e);
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
		return bean;
	}

	/**
	 * @author zhulangui 更改商业询盘状态为已完结
	 */
	@Override
	public int deleteBusiess(int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "UPDATE busiess set status = 1 , updatetime=now() where id= ? ";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
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
	public int queryByChoiceCountPage(BusiessBean busiessBean, int type) {
		// TODO Auto-generated method stub
		Integer id = busiessBean.getId();
		String company = busiessBean.getCompany();
		String createtime = busiessBean.getCreatetime();
		String email = busiessBean.getEmail();
		String needs = busiessBean.getNeeds();
		String ordervalue = busiessBean.getOrdervalue();
		Integer userid = busiessBean.getUserid();
//		Integer status = busiessBean.getStatus(); // status =2 未分配 =3 未处理 =4 全部
//		Integer adminid = busiessBean.getAdminid();
		int state=busiessBean.getState();
		String startdate=busiessBean.getStartdate();
		String enddate=busiessBean.getEnddate();
		Integer adminid=Integer.parseInt(busiessBean.getAdmName());
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlIf = new StringBuffer();
//		if(adminid==56){
//			adminid=1;
//		}
		// type = 1 Busiess询盘 其他的暂未使用
		if(type !=0){
			if(type == 1){
				sqlIf.append(" and busiess.customizedId is null and busiess.feedbackId is null ");
			} else if(type == 2){
				sqlIf.append(" and busiess.customizedId is not null ");
			} else if(type == 3){
				sqlIf.append(" and busiess.feedbackId is not null ");
			}
		}
		if (id != null) {
			sqlIf.append(" and id=" + id);
		}

		if (email != null && !"".equals(email)) {
			sqlIf.append(" and busiess.email='" + email + "'");
		}

		if (createtime != null && !"".equals(createtime)) {
			sqlIf.append(" and createtime='" + createtime + "'");
		}
		if(StringUtil.isNotBlank(startdate)){
			sqlIf.append(" and busiess.createtime >='" + startdate + " 00:00:00'");
		}

		if(StringUtil.isNotBlank(enddate)){
			sqlIf.append(" and busiess.createtime <='" + enddate + " 23:59:59'");
		}
		if (company != null && !"".equals(company)) {
			sqlIf.append(" and company='" + company + "'");
		}

		if (needs != null && !"".equals(needs)) {
			sqlIf.append(" and needs='" + needs + "'");
		}

		if (ordervalue != null && !"".equals(ordervalue)) {
			sqlIf.append(" and ordervalue='" + ordervalue + "'");
		}

		if (userid != null) {
			sqlIf.append(" and userid='" + userid + "'");
		}
		//分配的账户
		if (adminid != 0) {
			sqlIf.append(" and ( busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid=\'")
					.append(adminid)
					.append("\')");
			sqlIf.append(" or busiess.userid in (select aru.userid from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid=\'")
					.append(adminid)
					.append("\'))");
		}
		//状态 -1：全部；0：待办；1-完成；2-未布置
		sql.append("select count(1) as count from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
		if(state == 0 || state == 1){
			sql.append(" and busiess.status = "+state+"");
		} else if (state == 2){
			sql.append(" and busiess.userid not in (select DISTINCT aru.userid from admin_r_user aru,busiess bu where aru.userid = bu.userid)");
			sql.append(" and busiess.email not in (select DISTINCT aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid)");
		}
		sql.append(sqlIf);

		// status=3 待办；status=2 未布置；status=4 所有；
		/*if (adminid != null && adminid != 1 && adminid != 0) {
			if (status == 3) {
				sql.append(
						"select count(distinct busiess.id) as count from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(" and busiess.status=0");
				sql.append(sqlIf);
			} else {
				sql.append(
						"select count(busiess.id) as count from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(sqlIf);
			}
		} else if (adminid != null && (adminid == 1 || adminid == 0)) {
			if (status == 2) {
				sql.append(
						"select count(busiess.id) as count from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(
						" and busiess.email not in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid");
				if(!"-1".equals(admName)){
					sql.append(" and aru.adminid="+admName+"");
				}
				sql.append(")").append(sqlIf);
			} else if (status == 3) {
				sql.append(
						"select count(busiess.id) as count from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				if(!"-1".equals(admName)){
					sql.append(" and admin_r_user.adminid="+admName+"");
				}
				sql.append(" and busiess.status=0");
				sql.append(sqlIf);
			} else if (status == 4) {
				sql.append(
						"select count(busiess.id) as count from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				if(!"-1".equals(admName)){
					sql.append(" and admin_r_user.adminid="+admName+"");
				}
				sql.append(sqlIf);
			}
		}*/

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int total = 0;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt("count");
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
	public List<BusiessBean> queryByChoice(BusiessBean busiessBean, int pagenum, int type) {
		// TODO Auto-generated method stub
		Integer id = busiessBean.getId();
		String company = busiessBean.getCompany();
		String createtime = busiessBean.getCreatetime();
		String email = busiessBean.getEmail();
		String needs = busiessBean.getNeeds();
		String ordervalue = busiessBean.getOrdervalue();
		Integer userid = busiessBean.getUserid();
//		Integer status = busiessBean.getStatus(); // status =2 未分配 =3 未处理 =4 全部
//		Integer adminid = busiessBean.getAdminid();
		int state=busiessBean.getState();
		String startdate=busiessBean.getStartdate();
		String enddate=busiessBean.getEnddate();
		Integer adminid=Integer.parseInt(busiessBean.getAdmName());
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlIf = new StringBuffer();
//		if(adminid==56){
//			adminid=1;
//		}
		// type = 1 Busiess询盘 其他的暂未使用
		if(type !=0){
			if(type == 1){
				sqlIf.append(" and busiess.customizedId is null and busiess.feedbackId is null ");
			} else if(type == 2){
				sqlIf.append(" and busiess.customizedId is not null ");
			} else if(type == 3){
				sqlIf.append(" and busiess.feedbackId is not null ");
			}
		}
		if (id != null) {
			sqlIf.append(" and id=" + id);
		}

		if (email != null && !"".equals(email)) {
			sqlIf.append(" and busiess.email='" + email + "'");
		}

		if (createtime != null && !"".equals(createtime)) {
			sqlIf.append(" and createtime='" + createtime + "'");
		}
		if(StringUtil.isNotBlank(startdate)){
			sqlIf.append(" and busiess.createtime >='" + startdate + " 00:00:00'");
		}

		if(StringUtil.isNotBlank(enddate)){
			sqlIf.append(" and busiess.createtime <='" + enddate + " 23:59:59'");
		}

		if (company != null && !"".equals(company)) {
			sqlIf.append(" and company='" + company + "'");
		}

		if (needs != null && !"".equals(needs)) {
			sqlIf.append(" and needs='" + needs + "'");
		}

		if (ordervalue != null && !"".equals(ordervalue)) {
			sqlIf.append(" and ordervalue='" + ordervalue + "'");
		}

		if (userid != null) {
			sqlIf.append(" and userid='" + userid + "'");
		}
		//分配的账户
		if (adminid != null && adminid != 1 && adminid != 0) {
			sqlIf.append(" and ( busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid=\'")
					.append(adminid)
					.append("\')");
			sqlIf.append(" or busiess.userid in (select aru.userid from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid=\'")
					.append(adminid)
					.append("\'))");
		}
		//分页
		sqlIf.append(" order by createtime desc limit " + ((pagenum - 1) * 20) + "," + 20);

		//状态 -1：全部；0：待办；1-完成；2-未布置
		sql.append("select busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
		if(state == 0 || state == 1){
			sql.append(" and busiess.status = "+state+"");
		} else if (state == 2){
			sql.append(" and busiess.email not in (select DISTINCT aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid)");
			sql.append(" and busiess.userid not in (select DISTINCT aru.userid from admin_r_user aru,busiess bu where aru.userid = bu.userid)");
		}
		sql.append(sqlIf);

		/*if (adminid != null && adminid != 1 && adminid != 83) {
			if (status == 3) {
				sql.append(
						"select busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(" and busiess.status=0");
				sql.append(
						" and busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid="
								+ adminid + ")");
				sql.append(sqlIf);
			} else {
				sql.append(
						"select busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(
						" and busiess.email in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid and aru.adminid="
								+ adminid + ")");
				sql.append(sqlIf);
			}
		} else if (adminid != null && (adminid == 1 || adminid == 83)) {
			if (status == 2) {
				sql.append(
						"select busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				sql.append(
						" and busiess.email not in (select aru.useremail from admin_r_user aru,busiess bu where aru.userid = bu.userid");
				if(!"-1".equals(admName)){
					sql.append(" and aru.adminid="+admName+"");
				}
				sql.append(")").append(sqlIf);
			} else if (status == 3) {
				sql.append(
						"select busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				if(!"-1".equals(admName)){
					sql.append(" and admin_r_user.adminid="+admName+"");
				}
				sql.append(" and busiess.status=0");
				sql.append(sqlIf);
			} else if (status == 4) {
				sql.append(
						"select busiess.*,admuser.admName from busiess left join admin_r_user on busiess.userid=admin_r_user.userid left join admuser on admuser.id=admin_r_user.adminid where 1=1 ");
				if(!"-1".equals(admName)){
					sql.append(" and admin_r_user.adminid="+admName+"");
				}
				sql.append(sqlIf);
			}
		}*/

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<BusiessBean> list = new ArrayList<BusiessBean>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				BusiessBean busiessBeanpage = new BusiessBean();
				busiessBeanpage.setId(rs.getInt("id"));
				busiessBeanpage.setCompany(rs.getString("company"));
				busiessBeanpage.setCreatetime(rs.getString("createtime"));
				busiessBeanpage.setEmail(rs.getString("email"));
				busiessBeanpage.setNeeds(rs.getString("needs"));
				busiessBeanpage.setOrdervalue(rs.getString("ordervalue"));
				busiessBeanpage.setUserid(rs.getInt("userid"));
				busiessBeanpage.setUserphone(rs.getString("userphone"));
				busiessBeanpage.setAdmName(rs.getString("admName"));
				busiessBeanpage.setStatus(rs.getByte("status"));
				busiessBeanpage.setUpdatetime(rs.getTimestamp("updatetime"));
				// start add by yang_tao for 关联服装定制信息字段添加_2017年1月7日
				busiessBeanpage.setCustomizedId(rs.getInt("customizedId"));
				// end add by yang_tao for 关联服装定制信息字段添加_2017年1月7日
				busiessBeanpage.setFeedbackId(rs.getInt("feedbackId"));
				list.add(busiessBeanpage);
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
