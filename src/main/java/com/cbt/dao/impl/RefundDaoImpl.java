package com.cbt.dao.impl;

import com.cbt.dao.RefundDaoPlus;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.refund.bean.RefundBean;
import com.cbt.refund.bean.RefundBeanExtend;
import com.cbt.util.Util;
import com.cbt.website.bean.PaymentBean;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RefundDaoImpl implements RefundDaoPlus{

	@Override
	public int agreeRefund(int rid,String admuser) {
		String  sql = "update refund set status=1,agreetime = now(),agreepeople = ? "
				+ "where id = ? and status = 0 and valid = 1;";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int  row = 0;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, admuser);
//			stmt.setInt(2, rid);
//			row =stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(admuser));
			lstValues.add(String.valueOf(rid));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			row = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}
	@Override
	public int refuseRefund(int uid,int rid,String admuser,double appcount,String refuse,String appcurrency,int type) {
		int row = 0;
		double userBalance =0;
		double rate = 0.0;

		String sql = "update refund set status=-1,agreetime = now(),agreepeople = ?,"
				    + "endtime=now(),refuse=?,account=0 where valid = 1 and id = ? ;";

		String sql1	= "select available_m from user where id =?;";

		String sql4 = "select exchange_rate from exchange_rate where country = ? ";

//		6.3   再拒绝一条退款申请时，我们将原先的这条退款申请产生的余额变更记录   remark_id  置为0  用来标识      并判断能否取消
		String sql5= "update recharge_record set remark_id='0' where remark_id=? and type=8";

		String sql2 = "insert into recharge_record(recharge_record.userid,recharge_record.price,"
					+ "recharge_record.type,recharge_record.remark,"
					+ "recharge_record.remark_id,recharge_record.datatime,"
					+ "recharge_record.adminuser,recharge_record.usesign,"
					+ "recharge_record.currency,recharge_record.balanceAfter)"
					+ " values ( ?, ?, 10, '用户申请退款，操作员拒绝，回款导致金额变更', ?, now(), ?, 1, ?,? )";

		String sql3= "update user set available_m=?+available_m where id=?";

		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null,rs1 =null,rs2=null;
//		PreparedStatement stmt = null;
//		PreparedStatement stmt1 = null;
//		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
//		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
//		PreparedStatement stmt6 = null;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, admuser);
//			stmt.setString(2, refuse);
//			stmt.setInt(3, rid);
//			row =stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(admuser));
			lstValues.add(String.valueOf(refuse));
			lstValues.add(String.valueOf(rid));
			String runSql = DBHelper.covertToSQL(sql,lstValues);
			row = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

			//用户余额提现
			if(type==0&&row>0){
//				若管理员拒绝
//				2.3：查询汇率比   并计算应该回款的金额
				stmt3 =conn.prepareStatement(sql4);
				stmt3.setString(1, appcurrency);
				rs1 = stmt3.executeQuery();
				if(rs1.next()){
					rate = rs1.getDouble(1);
				}
				double rebackMoney =appcount/rate;
//				2.3 ：给用户回款
//				stmt4 = conn.prepareStatement(sql3);
//				stmt4.setDouble(1, rebackMoney);
//				stmt4.setInt(2, uid);
//				row+=stmt4.executeUpdate();

				List<String> lstValues1 = new ArrayList<String>();
				lstValues1.add(String.valueOf(rebackMoney));
				lstValues1.add(String.valueOf(uid));
				String runSql1 = DBHelper.covertToSQL(sql3,lstValues1);
				row += Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql1)));

//				stmt6 = conn.prepareStatement(sql5);
//				stmt6.setString(1, rid+"");
//				stmt6.executeUpdate();

				List<String> lstValues2 = new ArrayList<String>();
				lstValues2.add(String.valueOf(rid));
				String runSql2 = DBHelper.covertToSQL(sql5,lstValues2);
				SendMQ.sendMsg(new RunSqlModel(runSql2));

//				System.out.println(n);
//				2.5:查出用户余额，用于修改余额变更表
				stmt5 = conn.prepareStatement(sql1);
				stmt5.setInt(1, uid);
				rs2=stmt5.executeQuery();
				if(rs2.next()){
					userBalance = rs2.getDouble(1);
				}
//				2.4：添加余额变更记录
//				stmt1 = conn.prepareStatement(sql2);
//				stmt1.setInt(1, uid);
//				stmt1.setDouble(2, appcount);
//				stmt1.setInt(3, rid);
//				stmt1.setString(4, admuser);
//				stmt1.setString(5, appcurrency);
//				stmt1.setDouble(6, userBalance);
//				row +=stmt1.executeUpdate();
				List<String> lstValues3 = new ArrayList<String>();
				lstValues3.add(String.valueOf(uid));
				lstValues3.add(String.valueOf(appcount));
				lstValues3.add(String.valueOf(rid));
				lstValues3.add(String.valueOf(admuser));
				lstValues3.add(String.valueOf(appcurrency));
				lstValues3.add(String.valueOf(userBalance));

				String runSql3 = DBHelper.covertToSQL(sql2,lstValues3);
				row += Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql3)));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (stmt1 != null) {
//				try {
//					stmt1.close();
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}



	//根据退款状态查询退款记录^
	@Override
	public List<RefundBean> findByState(int state) {

		ArrayList<RefundBean> rfbList = new ArrayList<RefundBean>();
		String sql = "select r.id,r.userid,r.appcount,r.apptime,r.status,r.refusr,"
				+ "r.agreetime,r.endtime,r.currency as appcurrency,"
				+ " r.remark,u.name,u.available_m,u.email,u.currency as "
				+ "balanceCurrency from refund r, user u where r.userid=u.id  and r.valid=1 ";
		//判断state
		//1:所有退款
		//2:新申请退款
		//3:用户已取消
		//4:银行处理中
		//5:完结
		if(state==2){
			sql += " and r.status=0";
		} else if(state==3){
			sql += " and r.status=-2";
		} else if(state==4){
			sql += " and r.status=1";
		} else if(state==5){
			sql += " and r.status=2";
		}

		sql +=" order by r.apptime desc";

		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String agr = null;
				RefundBean rfb = new RefundBean();
				rfb.setId(rs.getInt("id"));
				rfb.setUserid(rs.getInt("userid"));
				rfb.setUsername(rs.getString("name"));
				rfb.setAppcount(rs.getDouble("appcount"));
				rfb.setApptime((rs.getString("apptime")).substring(0, 10));
				rfb.setAgreetime(rs.getString("agreetime"));
				rfb.setUserEmail(rs.getString("email"));
				if(rs.getString("endtime")==null){
					rfb.setEndtime("");
				} else {
					rfb.setEndtime((rs.getString("endtime")).substring(0, 19));
				}
				rfb.setBalance(rs.getDouble("available_m"));
				String agree = rs.getString("agreetime");
				int _status = rs.getInt("status");
				if(_status==0){
					agr = "还未同意退款";
				} else if(_status==-2){
					agr = "用户已取消";
				} else if(_status==2){
					agr = "已完结";
				} else if(_status==-1){
					agr = "拒绝  "+agree;
				}
				StringBuffer sb = new StringBuffer();
				sb.append(rs.getInt("id"));
				sb.append("////");
				sb.append(rs.getInt("userid"));
				sb.append("////");
				sb = sb.append(rs.getDouble("appcount"));
				if(_status==1){
					rfb.setRefundstate("银行处理中");
					String chkble = "<input type=checkbox name='str' id='chk' onclick='unSelectAll()' value='"+sb+"' />";
					rfb.setChkable(chkble);
				} else {

				if(_status==0){
					rfb.setRefundstate("新申请退款");
					String chkble = "<input type=checkbox name='str' id='chk' onclick='unSelectAll()' value='"+sb+"' />";
					rfb.setChkable(chkble);
				} else if(_status==-2) {
					rfb.setRefundstate("用户已取消");
					String chkble = "<input type=checkbox name='str' id='chk' onclick='unSelectAll()' value='"+sb+"' disabled=ture />";
					rfb.setChkable(chkble);
				} else if(_status==1) {
					rfb.setRefundstate("银行处理中");
					String chkble = "<input type=checkbox name='str' id='chk' onclick='unSelectAll()' value='"+sb+"' disabled=ture />";
					rfb.setChkable(chkble);
				} else if(_status==2){
					rfb.setRefundstate("完结");
					String chkble = "<input type=checkbox name='str' id='chk' onclick='unSelectAll()' value='"+sb+"' disabled=ture />";
					rfb.setChkable(chkble);
				}}
				rfb.setAgree(agr);
				String ssssss=rs.getString("remark");
				rfb.setRemark(ssssss);
				if(rs.getString("paypalname")=="付款时记录"){
					rfb.setPaypalname("");
				} else {
					rfb.setPaypalname(rs.getString("paypalname"));
				}
				rfb.setBalanceCurrency(rs.getString("balanceCurrency"));
				rfb.setCurrency(rs.getString("appcurrency"));
//				rfb.setAdminUid(rs.getString("adminid"));
				rfb.setRefuse(rs.getString("refuse"));
				rfbList.add(rfb);
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
		return rfbList;
	}

	//获取所有后台工作人员
	@Override
	public List<AdminUserBean> getAllAdmUser() {
		List<AdminUserBean> aubList = new ArrayList<AdminUserBean>();
		String sql = "select * from admuser where status = 1 and roleType=0";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				AdminUserBean admuser = new AdminUserBean();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("Email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoleType(rs.getInt("roleType"));
				admuser.setState(rs.getInt("status"));
				aubList.add(admuser);
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
		return aubList;
	}


	//完结退款项
	@Override
	public int finishRefund(int refundId) {
		int row=0;
		String sql = "update refund set status=2,endtime=now() where id=?";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		ResultSet rs = null;
//		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, refundId);
//			row=stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(refundId));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			row = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		}
		return row;
	}


	@Override
	public List<RefundBean> reportGet(String sdate,String edate,String reason,int page) {
		StringBuilder sb = new StringBuilder();
		List<RefundBean> list = new ArrayList<RefundBean>();
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		sb.append("select sql_calc_found_rows r.id,r.apptime,r.appcount,"
				+ "r.userid,r.remark,r.paypalname,r.currency,r.account,"
				+ "r.status,u.name "
				+ "from refund r,user u WHERE r.valid=1 and u.id=r.userid ");
		if(sdate!=null && !"".equals(sdate)){
			sb.append(" and r.apptime > '"+sdate+"'");
		}
		if(edate!=null && !"".equals(edate)){
			sb.append(" and r.apptime < '"+edate+"'");
		}
		if(reason!=null&&!reason.trim().isEmpty()&&!"0".equals(reason)){
			sb.append(" and r.refundtype="+reason);
		}
		sb.append(" order by apptime desc limit ?,40");

		String countsql = "select found_rows();";
		int count  = 0 ;
		try {
//			System.err.println("^^^^"+sb.toString());
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, (page-1)*40);
			rs= stmt.executeQuery();
			stmt2 = conn.prepareStatement(countsql);
			rs2= stmt2.executeQuery();
			if(rs2.next()){
				count = rs2.getInt("found_rows()");
				count = count%40 == 0 ? count / 40 : count / 40 + 1;
			}

			while(rs.next()){
				RefundBean rb = new RefundBean();
				rb.setApptime(rs.getString("apptime"));
				rb.setAppcount(rs.getDouble("appcount"));
				rb.setAccount(rs.getDouble("account"));
				rb.setUserid(rs.getInt("userid"));
				rb.setRemark(rs.getString("remark"));
				rb.setPaypalname(rs.getString("paypalname"));
				rb.setCurrency("USD");
				rb.setUsername(rs.getString("name"));
				rb.setCount(count);
				rb.setStatus(rs.getInt("status"));
				list.add(rb);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}



	@Override
	public List<RefundBeanExtend> searchAllRefundByUid(int userid) {
		ArrayList<RefundBeanExtend> rfbList = new ArrayList<RefundBeanExtend>();
		String sql = null;
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		if(userid!=0){
			sql = "SELECT rr.id,rr.price,rr.datatime,rr.currency,rr.balanceAfter,"
					+ "r.id rid,r.status,r.valid from recharge_record rr"
					+ "  LEFT JOIN refund r on rr.remark_id=r.id WHERE r.userid =?  and r.valid=1  "
					+ " order by r.apptime DESC";
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, userid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			rs = stmt.executeQuery();
			while (rs.next()) {
				RefundBeanExtend rfb = new RefundBeanExtend();
				rfb.setId(rs.getInt("rid"));
				rfb.setValid(rs.getInt("valid"));
				rfb.setRid(rs.getInt("id"));
				rfb.setStatus(rs.getInt("status"));
				rfb.setRprice(rs.getDouble("price"));
				rfb.setRdatatime((rs.getString("datatime")).substring(0, 19));
				rfb.setRcurrency((rs.getString("currency")));
				rfb.setRbalanceAfter((rs.getDouble("balanceAfter")));
				rfb.setCurrencyshow(Util.currencyChange(rfb.getRcurrency()));
				rfbList.add(rfb);
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
		return rfbList;
	}


	@Override
	public List<RefundBean> searchByPramer(Integer userid, String username,String appdate,
			String agreeTime,int status, int startpage,int type,int rid,String admin) {

		ArrayList<RefundBean> rfbList = new ArrayList<RefundBean>();

		String _sql = "select sql_calc_found_rows r.id,r.userid,r.appcount,"
					+ "r.status,r.refuse,r.apptime,r.account,r.type,r.orderid,r.payid,r.additionid,"
				    + "r.paypalstate,r.reasoncode,r.reasonnote,r.casetype,r.complainid,r.outcomecode,"
				    + "r.refundtype,"
//				    + "(select sum(money) from additional_balance ab where ab.comid=r.complainid) as addbalance,"
				    +" (select adus.admName  from admin_r_user as ad,admuser as adus where ad.adminid =adus.id and " +
					"ad.userid=r.userid limit 1) as adminuser, "
				    + "r.agreetime,r.endtime,r.currency as appcurrency,"
				    +"r.remark,r.feedback,r.paypalname,u.name,u.available_m,u.email "
				    + " from refund r, user u where r.userid=u.id "
				    + "and r.valid = 1 ";

		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null,rscount=null;
		PreparedStatement stmt = null,stmt2=null;
		int index = 1;
		if(rid!=0){
			_sql +=" and r.id=? ";
		}
		if(type!=-1){
			_sql +=" and r.type=? ";
		}
		if(userid!=0){
			_sql +=" and r.userid=?";
		}

		//根据参数拼接sql
		if(username!=null&&!"".equals(username)){
			_sql += " and u.name like ?";
		}
		if(appdate!=null&&!"".equals(appdate)){
			_sql += " and r.apptime like ?";
		}

		if(agreeTime!=null&&!"".equals(agreeTime)){
			_sql += " and date_format(r.agreetime, '%Y-%m-%d') = date_format(?, '%Y-%m-%d')";
		}

		//根据传过来的状态值拼接对应的sql
//		1.新申请退款
		if(0==status){
			_sql +=" and r.status = 0 ";
		}
//		2.用户已取消
		if(-2==status){
			_sql +=" and r.status = -2 ";
		}
//		3.银行处理中
		if(1==status){
			_sql +=" and r.status = 1 and ( (account>0 and type=0) or type>0 )";
		}
//		4.已完结
		if(2==status){
			_sql +=" and r.status = 2 ";
		}
	//5 拒绝
		if(-1==status){
			_sql +=" and r.status = -1 ";
		}
		if(3==status ||  -3==status || -4==status){
			_sql +=" and r.status = " + status + " ";
		}
		if(admin!=null){
			admin = admin.toLowerCase();
			if(!admin.equals("ling")&&!admin.equals("emma")){
				_sql +=" having adminuser='"+admin+"'";
			}
		}

		//6.3   因最新退款流程和以前状态值有区别，我们放弃5.30之前已经处理过的退款数据，从这之后开始算新流程。
		if(2==status){
			_sql +="  order by  r.endtime desc ";
		}else{
			_sql +="  order by  r.apptime desc ";
		}

		String countsql = "select found_rows();";
		_sql +=" limit ?,10";

//		System.out.println(_sql);
		int count = 0;
		try {
			System.err.println(_sql);
			stmt = conn.prepareStatement(_sql);
			if(rid!=0){
				stmt.setInt(index, rid);
				index++;
			}
			if(type!=-1){
				stmt.setInt(index, type);
				index++;
			}
			if(userid!=0){
				stmt.setInt(index, userid);
				index++;
			}

			//根据参数拼接sql
			if(username!=null&&!"".equals(username)){
				stmt.setString(index, "%"+username+"%");
				index++;
			}
			if(appdate!=null&&!"".equals(appdate)){
				stmt.setString(index, "%"+appdate+"%");
				index++;
			}

			if(agreeTime!=null&&!"".equals(agreeTime)){
				stmt.setString(index, agreeTime);
				index++;
			}
			stmt.setInt(index, (startpage-1)*10);
			stmt2 = conn.prepareStatement(countsql);
			rs = stmt.executeQuery();
			rscount = stmt2.executeQuery();
			if(rscount.next()){
				count = rscount.getInt("found_rows()");
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (rs.next()) {
				System.out.println(rs.getString("adminuser"));
				String agr = null;
				RefundBean rfb = new RefundBean();
				int _appStatus = rs.getInt("status");
				//状态 0-申请退款 1-销售同意退款 2-退款完结 3-管理员同意退款  -1-销售驳回退款 -2 -客户取消退款 -3-管理员拒绝退款 -4-退款失败
				if (_appStatus == 0) {
					agr = "申请退款";
				} else if (_appStatus == -4) {
					agr = "退款失败";
				} else if (_appStatus == -3) {
					agr = "管理员拒绝退款";
				} else if (_appStatus == -2) {
					agr = "客户取消退款";
				} else if (_appStatus == -1) {
					agr = "售驳回退款";
				} else if (_appStatus == 1) {
					agr = "销售同意退款";
				} else if (_appStatus == 2) {
					agr = "退款完结";
				} else if (_appStatus == 3) {
					agr = "管理员同意退款";
				}
				rfb.setStatus(_appStatus);
				rfb.setId(rs.getInt("id"));
				rfb.setUserid(rs.getInt("userid"));
				rfb.setUsername(rs.getString("name"));
				rfb.setAppcount(rs.getDouble("appcount"));
				int refundtype = rs.getInt("refundtype");
				String refundType =  "" ;
				if(refundtype==1){
					refundType = "产品无货";
				}else if(refundtype==2){
					refundType = "无法发货";
				}else if(refundtype==3){
					refundType = "交期延误";
				}else{
					refundType = refundtype==4?"客户申请":"其他原因";
					refundType = refundtype==0?"":"其他原因";
				}
				rfb.setRefundType(refundType);
				String apptime = rs.getString("apptime");
				rfb.setApptime(apptime);
				int ptype = rs.getInt("type");
				rfb.setType(ptype);
				if(ptype==1&&( _appStatus==0 || _appStatus==1)){
					rfb.setDelaytime(TypeUtils.getTimeAfter(df.parse(apptime), 9));
					if(!TypeUtils.isNew(apptime, 6)){
						rfb.setIsDelay(TypeUtils.isNew(apptime, 9)?1:2);
					}
				}
				rfb.setAgreetime(rs.getString("agreetime"));
				rfb.setFeedback(rs.getString("feedback"));
				rfb.setUserEmail(rs.getString("u.email"));
				rfb.setRefuse(rs.getString("refuse"));
				rfb.setAccount(rs.getDouble("account"));
				rfb.setOrderid(rs.getString("orderid"));
				rfb.setPayid(rs.getString("payid"));
				rfb.setAdditionid(rs.getInt("additionid"));
//				r.paypalstate,r.reasoncode,r.reasonnote,r.casetype
				rfb.setPaypalstate(rs.getInt("paypalstate"));
				rfb.setReasoncode(rs.getString("reasoncode"));
				rfb.setReasonnote(rs.getString("reasonnote"));
				rfb.setCasetype(rs.getString("casetype"));
				rfb.setComplainId(rs.getInt("complainid"));
				rfb.setOutcomeCode(rs.getString("outcomecode"));
//				rfb.setAdditionBanlance(rs.getDouble("addbalance"));
				if(rs.getString("endtime")==null){
					rfb.setEndtime("");
				} else {
					rfb.setEndtime(rs.getString("endtime"));
				}
				rfb.setBalance(rs.getDouble("available_m"));
				rfb.setAgreetime(rs.getString("agreetime"));

				rfb.setAgree(agr);
				String remark=rs.getString("remark");
				rfb.setRemark(remark==null?"":remark.trim());
				if(rs.getString("paypalname")=="付款时记录"){
					rfb.setPaypalname("");
				} else {
					rfb.setPaypalname(rs.getString("paypalname"));
				}
				rfb.setBalanceCurrency("USD");
				rfb.setCurrency(rs.getString("appcurrency"));
				rfb.setAdminUid(rs.getString("adminuser"));
				rfb.setCount(count);
				rfbList.add(rfb);
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
		return rfbList;
	}


	@Override
	public int addFeedback(int rid,String feedback) {
		int res=0;
		String sql = "update refund set feedback = ?  where  id = ? ";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, feedback);
//			stmt.setInt(2, rid);
//			res = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(feedback));
			lstValues.add(String.valueOf(rid));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			res = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}


	@Override
	public int addRemark(double account,int rid, String remark,String agreepeople,int additionId,int status,int resontype) {
		String sql = "update refund set account=?,remark=?,status=?,agreetime=now(),"
				+ "agreepeople=?,additionid=?,refundtype=? where id = ?";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int result = 0;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setDouble(1, account);
//			stmt.setString(2, remark);
//			stmt.setInt(3, status);
//			stmt.setString(4, agreepeople);
//			stmt.setInt(5, additionId);
//			stmt.setInt(6, resontype);
//			stmt.setInt(7, rid);
//			result = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(account));
			lstValues.add(String.valueOf(remark));
			lstValues.add(String.valueOf(status));
			lstValues.add(String.valueOf(agreepeople));
			lstValues.add(String.valueOf(additionId));
			lstValues.add(String.valueOf(resontype));
			lstValues.add(String.valueOf(rid));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public double getApplyRefund(int id) {
		String sql = "select r.userid, sum(r.account/e.exchange_rate)  as usdappcount "
				+ "from refund r,exchange_rate e where r.currency=e.country  and r.valid=1  "
				+ "and (r.status=0 or r.status=1 or r.status=2 or r.status=3 or r.status=4) and r.userid=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()){
				total = rs.getDouble("usdappcount");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return total;
	}
	@Override
	public double getApplyPaypal(int id) {
		String sql = "select r.userid, sum(r.account/e.exchange_rate)  as usdappcount "
				+ "from refund r,exchange_rate e where r.currency=e.country  "
				+ "and r.valid=1 and r.type=1 "
				+ "and r.status=0 and r.userid=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while(rs.next()){
				total = rs.getDouble("usdappcount");
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
	public double getRefundByUserid(int userid) {
		String sql = "select r.userid, sum(r.account/e.exchange_rate)  as usdappcount "
				+ "from refund r,exchange_rate e where r.currency=e.country  and r.valid=1  "
				+ "and (r.status=4) and r.userid=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while(rs.next()){
				total = rs.getDouble("usdappcount");
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
	public Map<String, String> getRefundByUserids(List<Integer> list){
		String sql = "select r.userid, sum(r.account/e.exchange_rate)  as usdappcount "
					+ "from refund r,exchange_rate e where r.currency=e.country   and r.valid=1 "
					+ "and (r.status=2) and r.userid in (";

		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			for(Integer l:list){
				sql = sql+l+",";
			}
			sql = sql.endsWith(",")?sql.substring(0, sql.length()-1):sql;
			sql = sql+") group by r.userid";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("userid"), rs.getString("usdappcount"));
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
	public Map<String, String> getApplyRefundByUserids(List<Integer> list) {
		String sql = "select r.userid, sum(r.account/e.exchange_rate)  as usdappcount "
				+ "from refund r,exchange_rate e where r.currency=e.country  and r.valid=1  "
				+ "and (r.status=0 or r.status=1) and r.userid in (";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			for(Integer l:list){
				sql = sql+l+",";
			}
			sql = sql.endsWith(",")?sql.substring(0, sql.length()-1):sql;
			sql = sql+") group by r.userid";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("userid"), rs.getString("usdappcount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return map;
	}
	@Override
	public Map<String, String> getApplyPaypalByUserids(List<Integer> list) {
		String sql = "select r.userid, sum(r.account/e.exchange_rate)  as usdappcount "
				+ "from refund r,exchange_rate e where r.currency=e.country  and r.valid=1  "
				+ " and r.type=1 and r.status=0 and r.userid in (";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			for(Integer l:list){
				sql = sql+l+",";
			}
			sql = sql.endsWith(",")?sql.substring(0, sql.length()-1):sql;
			sql = sql+") group by r.userid";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("userid"), rs.getString("usdappcount"));
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
	public int addRefundFromAppeal(int uid, Double appcount,
			String currency, String paypalname, String payid, String ordeid,
			int type) {
		String sql = "insert into refund(userid,appcount,account,apptime,currency,paypalname,type,orderid,payid)"
				+ "values(?,?,?,now(),?,?,?,?,?) ";

//		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
//		PreparedStatement stmt = null;
		try {

//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, uid);
//			stmt.setDouble(2, appcount);
//			stmt.setDouble(3, appcount);
//			stmt.setString(4, currency);
//			stmt.setString(5, paypalname);
//			stmt.setInt(6, type);
//			stmt.setString(7, ordeid);
//			stmt.setString(8, payid);
//
//			rs = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(uid));
			lstValues.add(String.valueOf(appcount));
			lstValues.add(String.valueOf(appcount));
			lstValues.add(String.valueOf(currency));
			lstValues.add(String.valueOf(paypalname));
			lstValues.add(String.valueOf(type));
			lstValues.add(String.valueOf(ordeid));
			lstValues.add(String.valueOf(payid));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}
	@Override
	public Map<String, String> getComplainRefundByUserids(String useridList) {
		Map<String, String>  map = new HashMap<String, String>();
		if(StrUtils.isNullOrEmpty(useridList)){
			return map;
		}
		String sql = "select *from refund where type=2 and userid in ("+useridList+")";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("userid"), "1");
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
		return map;
	}
	@Override
	public int insertRechangeRecord(int userid, double available, String remark,
			String modifyuser, int usersign,int type) {
		String sql0 = "select available_m from user where id=?";
		String sql="insert into recharge_record(userid,price,type,remark,"
				+ "datatime,adminuser,usesign,balanceAfter)"
				+ " values(?,?,?,?,now(),?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection2();
		int res=0;
		PreparedStatement stmt0 = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		double available_m = 0;
		try {
			stmt0 = conn.prepareStatement(sql0);
			stmt0.setInt(1, userid);
			rs = stmt0.executeQuery();
			while(rs.next()){
				available_m = rs.getDouble("available_m");
			}
			available_m = available_m+available;

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setDouble(2, available);
			stmt.setInt(3, type);
			if(usersign==0){
				stmt.setString(4, "add:"+remark);
			}else{
				stmt.setString(4, "deduction:"+remark);
			}

			stmt.setString(5, modifyuser);
			stmt.setInt(6, usersign);
			stmt.setDouble(7, available_m);
			res=stmt.executeUpdate();

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
			if (stmt0 != null) {
				try {
					stmt0.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	@Override
	public List<RefundBean> getRefundList(int page) {
			String sql = "select sql_calc_found_rows "
					+ "id,userid,appcount,apptime,"
					+ "payid,orderid,paypalname,currency "
					+ "from refund where userid=0 and valid =1 "
					+ "order by apptime desc limit ?,40";
			Connection conn = DBHelper.getInstance().getConnection2();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			PreparedStatement stmt2 = null;
			ResultSet rs2 = null;
			int count  = 0;
			List<RefundBean> list = new ArrayList<RefundBean>();
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, (page-1)*40);
				rs = stmt.executeQuery();
				stmt2 = conn.prepareStatement("select found_rows();");
				rs2 = stmt2.executeQuery();
				if(rs2.next()){
					count = rs2.getInt("found_rows()");
				}

				while(rs.next()){
					RefundBean bean = new RefundBean();
					bean.setCount(count);
					bean.setAppcount(rs.getDouble("appcount"));
					bean.setCurrency(rs.getString("currency"));
					bean.setPayid(rs.getString("payid"));
					bean.setApptime(rs.getString("apptime"));
					bean.setId(rs.getInt("id"));
					bean.setPaypalname(rs.getString("paypalname"));
					bean.setOrderid(rs.getString("orderid"));
					list.add(bean);
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
				DBHelper.getInstance().closeConnection(conn);
			}
			return list;
	}
	@Override
	public int updateRefund(int id, int userid, String orerid, String paypalname) {
		String sql = "update refund set userid=?,orderid=?,paypalname=? where id=?";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int rs = 0;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, userid);
//			stmt.setString(2, orerid);
//			stmt.setString(3, paypalname);
//			stmt.setInt(4, id);
//			rs = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(userid));
			lstValues.add(String.valueOf(orerid));
			lstValues.add(String.valueOf(paypalname));
			lstValues.add(String.valueOf(id));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}
	@Override
	public List<PaymentBean> getPayment(double appCount,String curreny,int page) {
		String sql = "select sql_calc_found_rows userid,orderid,orderdesc,payment_amount,"
				+ "payment_cc,createtime "
				+ "from payment where paystatus=1 and paytype=0 and payment_amount=? "
				+ "and payment_cc=? order by createtime desc limit ?,40 ";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		int count  = 0;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, appCount);
			stmt.setString(2, curreny);
			stmt.setInt(3, (page-1)*40);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			if(rs2.next()){
				count = rs2.getInt("found_rows()");
			}
			while(rs.next()){
				PaymentBean bean = new PaymentBean();
				bean.setCount(count);
				bean.setUserid(rs.getInt("userid"));
				bean.setOrderid(rs.getString("orderid"));
				bean.setOrderdesc(rs.getString("orderdesc"));
				bean.setPayment_amount(rs.getString("payment_amount"));
				bean.setPayment_cc(rs.getString("payment_cc"));
				bean.setCreatetime(rs.getString("createtime"));
				list.add(bean);
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public int confirmAndRemark(int id, int userid, String refundOrderNo, String remark, double account, int status, String admName) {

		int rs = 0;

		String sql;
		//查询退款表，更新退款状态
		if (status == 1) {
			sql = "update refund set orderid=?,account=?,status=?,remark=concat(ifnull(remark,''),?),agreetime =sysdate(),"
					+ "agreepeople = ? where id=? and userid = ?";
		} else {
			sql = "update refund set orderid=?,account=?,status=?,remark=concat(ifnull(remark,''),?) where id=? and userid = ?";
		}

		try {

			List<String> lstValues = new ArrayList<>();
			lstValues.add(refundOrderNo);
			lstValues.add(String.valueOf(account));
			lstValues.add(String.valueOf(status));
			lstValues.add("<br>" + remark);

			if (status == 1) {
				lstValues.add(admName);
				lstValues.add(String.valueOf(id));
				lstValues.add(String.valueOf(userid));
			} else {
				lstValues.add(String.valueOf(id));
				lstValues.add(String.valueOf(userid));
			}

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * @Title: getRefundOrderNo
	 * @Author: cjc
	 * @Despricetion:TODO 根据paypal账号获取可退款订单号
	 * @Date: 2018/5/21 14:14
	 * @Param: [paypalEmail]
	 * @Return: int
	 */
	@Override
	public List<com.cbt.bean.Payment> getRefundOrderNo(String paypalEmail) {

		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		List<com.cbt.bean.Payment> list = new ArrayList<>();
		String sql = "";
		ResultSet rs = null;
		sql = "select orderid from payment WHERE orderid in (SELECT orderNo FROM ipn_info where " +
				"payer_email = ? AND datediff(NOW(),createtime) < 90) AND paystatus=1 AND datediff(NOW(),createtime) < 90 " +
				"and paytype in(0,1,2) GROUP BY orderid ORDER BY createtime DESC";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, paypalEmail);
			rs = stmt.executeQuery();
			if (rs.next()) {
				com.cbt.bean.Payment payment = new com.cbt.bean.Payment();
				payment.setOrderid(rs.getString("orderid"));
				list.add(payment);
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
		return list;
	}

	@Override
	public boolean updateRefundState(int userId, int refundId, int state) {

//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int rs = 0;

		String sql = "update refund set status=? where id=? and userid = ?";

		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, state);
//			stmt.setInt(2, refundId);
//			stmt.setInt(3, userId);
//			rs = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(state));
			lstValues.add(String.valueOf(refundId));
			lstValues.add(String.valueOf(userId));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs > 0;
	}

	@Override
	public double queryComplaintTotalAmount(int userId) {
		String sql = "select sum(amount_refunded) as total from customer_dispute_refund where user_id = ? and status = 2";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return total;
	}

	@Override
	public int checkIsExistsApproval(int userId, String orderNo) {
		String querySql = "select count(1) from order_cancel_approval where user_id = ? and order_no = ? and deal_state < 4";
		Connection connAws = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int total = 0;
		try {
			stmt = connAws.prepareStatement(querySql);
			stmt.setInt(1, userId);
			stmt.setString(2, orderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(connAws);
		}
		return total;
	}

	@Override
	public int insertIntoPaymentByApproval(int userId,String orderNo) {
		String querySql = "insert into payment(userid,orderid,paymentid,payment_amount,payment_cc,orderdesc,username,"
				+ "paystatus,createtime,paySID,payflag,paytype,payment_other,paymentno,transaction_fee) " +
				"select userid,orderid,paymentid,(0 - payment_amount) as payment_amount,payment_cc,orderdesc,username," +
				"paystatus,createtime,paySID,payflag,paytype,payment_other,paymentno,transaction_fee from payment"
				+ " where orderid =? and userid = ? and paytype in(0,5) and paystatus = 1 ";
//		Connection connAws = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int total = 0;
		try {
//			stmt = connAws.prepareStatement(querySql);
//			stmt.setString(1, orderNo);
//			stmt.setInt(2, userId);
//			total = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(orderNo));
			lstValues.add(String.valueOf(userId));

			String runSql = DBHelper.covertToSQL(querySql,lstValues);
			total = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			DBHelper.getInstance().closePreparedStatement(stmt);
//			DBHelper.getInstance().closeConnection(connAws);
		}
		return total;
	}
}
