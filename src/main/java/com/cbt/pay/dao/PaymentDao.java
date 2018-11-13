package com.cbt.pay.dao;

import com.cbt.bean.Payment;
import com.cbt.bean.RechargeRecord;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.service.StrUtils;

import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao implements IPaymentDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PaymentDao.class);
	@Override
	public void addPayment(Payment pay) {
		// TODO Auto-generated method stub
		String sql = "insert payment(userid,orderid,paymentid,orderdesc,username,paystatus,payment_amount,payment_cc,createtime,paySID,payflag,paytype) values(?,?,?,?,?,?,?,?,now(),?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, pay.getUserid());
			stmt.setString(2, pay.getOrderid());
			stmt.setString(3, pay.getPaymentid());
			stmt.setString(4, pay.getOrderdesc());
			stmt.setString(5, pay.getUsername());
			stmt.setInt(6, pay.getPaystatus());
			stmt.setFloat(7, pay.getPayment_amount());
			stmt.setString(8, pay.getPayment_cc());
			stmt.setString(9, pay.getPaySID());
			stmt.setString(10, pay.getPayflag());
			stmt.setString(11, pay.getPaytype());
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public Payment getPayment(int userid, String orderid) {
		// TODO Auto-generated method stub
		String sql = "select * from payment where userid=? and orderid=? and paystatus=1";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Payment pay = new Payment();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, orderid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				pay.setOrderid(rs.getString("orderid"));
				pay.setOrderdesc(rs.getString("orderdesc"));
				pay.setPaystatus(rs.getInt("paystatus"));
				pay.setUsername(rs.getString("username"));
				pay.setPayment_amount(rs.getFloat("payment_amount"));
				pay.setPayment_cc(rs.getString("payment_cc"));
				pay.setPaymentid(rs.getString("paymentid"));
				pay.setCreatetime(rs.getString("createtime"));
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
		
		return pay;
	}
	
	@Override
	public int getPayment(String paymentid) {
		// TODO Auto-generated method stub
		String sql = "select id from payment where paymentid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int id = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, paymentid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt("id");
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
		
		return id;
	}

	@Override
	public void insertintoPaylog(Payment pay,String paymentDate,int paybtype) {
		// TODO Auto-generated method stub
		String sql = "insert paylog(userid,orderid,paySID,payment_amount,payment_cc,orderdesc,username,payType,paybtype,Operationtime,OperationDate) values(?,?,?,?,?,?,?,?,?,now(), date_format(now(),'%Y-%m-%d'))";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, pay.getUserid());
			stmt.setString(2, pay.getOrderid());
			stmt.setString(3, pay.getPaySID());
			stmt.setFloat(4, pay.getPayment_amount());
			stmt.setString(5, pay.getPayment_cc());
			stmt.setString(6, pay.getOrderdesc());
			stmt.setString(7, pay.getUsername());
			stmt.setInt(8, pay.getPaystatus());
			stmt.setInt(9, paybtype);
			//stmt.setString(9, paymentDate);
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
			LOG.warn("---------------"+pay.getUsername()+"insert into paylog1 success!!");
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public void insertintoPaylog(int userid,String orderid,String paySID,float payment_amount,String username,String paylogdesc,String paybtype) {
		// TODO Auto-generated method stub
		String sql = "insert paylog(userid,orderid,paySID,payment_amount,payment_cc,orderdesc,username,paybtype,payType,Operationtime,OperationDate) values(?,?,?,?,?,?,?,?,?,now(), date_format(now(),'%Y-%m-%d'))";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userid);
			stmt.setString(2, orderid);
			stmt.setString(3, paySID);
			stmt.setFloat(4, payment_amount);
			stmt.setString(5,"USD");
			stmt.setString(6, paylogdesc);
			stmt.setString(7, username);
			stmt.setString(8, paybtype);
			stmt.setString(9, "1");
			//stmt.setString(9, paymentDate);
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
			LOG.warn(username+"insert into paylog2 success!!");
			DBHelper.getInstance().closeConnection(conn);
		}
	}
	@Override
	public void insertintoPayconfirm(String orderid, String name) {
		// TODO Auto-generated method stub
		String sql = "insert paymentconfirm(orderid,confirmname,confirmtime) values(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderid);
			stmt.setString(2,name);
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public void addRechargeRecord(RechargeRecord rr) {
		String sql = "insert recharge_record(userid,price,type,remark,remark_id,datatime,usesign,currency) values(?,?,?,?,?,now(),?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, rr.getUserid());
			stmt.setDouble(2, rr.getPrice());
			stmt.setInt(3, rr.getType());
			stmt.setString(4, rr.getRemark());
			stmt.setString(5, rr.getRemark_id());
			stmt.setInt(6, rr.getUsesign());
			//ljj 4.7 添加插入货币字段
			stmt.setString(7, rr.getCurrency());
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public Payment getPayment(int userid, String orderid, String payflag) {
		// TODO Auto-generated method stub
		String sql = "select * from payment where userid=? and orderid=? and paystatus=1 and payflag=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Payment pay = new Payment();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, orderid);
			stmt.setString(3, payflag);
			rs = stmt.executeQuery();
			while (rs.next()) {
				pay.setOrderid(rs.getString("orderid"));
				pay.setOrderdesc(rs.getString("orderdesc"));
				pay.setPaystatus(rs.getInt("paystatus"));
				pay.setUsername(rs.getString("username"));
				pay.setPayment_amount(rs.getFloat("payment_amount"));
				pay.setPayment_cc(rs.getString("payment_cc"));
				pay.setPaymentid(rs.getString("paymentid"));
				pay.setCreatetime(rs.getString("createtime"));
				pay.setPayflag(rs.getString("payflag"));
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
		
		return pay;
	}

	@Override
	public String getPayLog(String orderNo, String sid) {
		String sql = "select payment_amount from paylog where orderid = ? and paySID = ? and  not exists (select id from payment where orderid=? and paySID=? )";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String payment_amount = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setString(2, sid);
			stmt.setString(3, orderNo);
			stmt.setString(4, sid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				payment_amount = rs.getString("payment_amount");
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
		
		return payment_amount;
	}

	@Override
	public void addPayments(List<Payment> pays) {
		String sql = "insert payment(userid,orderid,paymentid,orderdesc,username,paystatus,payment_amount,payment_cc,createtime,paySID,payflag,paytype,payment_other) values(?,?,?,?,?,?,?,?,now(),?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for(int i=0;i<pays.size();i++){
				Payment pay = pays.get(i);
				stmt.setInt(1, pay.getUserid());
				stmt.setString(2, pay.getOrderid());
				stmt.setString(3, pay.getPaymentid());
				stmt.setString(4, pay.getOrderdesc());
				stmt.setString(5, pay.getUsername());
				stmt.setInt(6, pay.getPaystatus());
				stmt.setFloat(7, pay.getPayment_amount());
				stmt.setString(8, pay.getPayment_cc());
				stmt.setString(9, pay.getPaySID());
				stmt.setString(10, pay.getPayflag());
				stmt.setString(11, pay.getPaytype());
				stmt.setFloat(12, pay.getPayment_other());
				stmt.addBatch();
			}
			int[] result = stmt.executeBatch();
			if (result.length > 0) {
				stmt.getGeneratedKeys();
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

	/**
	 * 获取订单支付信息
	 * 
	 * @param orderNo
	 */
	@Override
	public List<Payment> getOrdersPayList(String orderNo,int paystatus){
//		String sql = "SELECT DISTINCT p.orderid,p.orderdesc,p.paystatus,p.paymentid,"
//				+ "p.username,p.payment_cc,p.payment_amount,p.userid,p.createtime,"
//				+ "p.payflag,p.paytype,p.paySID,p2.paytype2,p2.paymentid2 "
//				+ "FROM payment p,"
//				+ "(select p1.paymentid as paymentid2,"
//				+ "p1.paytype as paytype2 from payment p1 where "
//				+ "p1.orderid=LEFT(?,16)) as p2 "
//				+ " WHERE p.paystatus = ? "
//				+ "and LEFT(p.orderid,17) = LEFT(?,17)";
		
		
		
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		int length = 16;
		List<Payment> payList = new ArrayList<Payment>();
		try {
			if(orderNo.length()>16){
				String sql2 = "select paymentid from payment where paystatus=1 and orderid=?  ";
				stmt2 = conn.prepareStatement(sql2);
				stmt2.setString(1, orderNo.substring(0, 17));
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					String paymentid = rs2.getString("paymentid");
					if(StrUtils.isNotNullEmpty(paymentid)&&paymentid.length()>5){
						length = 17;
						break;
					}
				}
			}
			
			String sql="SELECT DISTINCT p.orderid,p.orderdesc,p.paystatus,p.paymentid,p.username,"
					+ "p.payment_cc,p.payment_amount,p.userid,p.createtime,p.payflag,p.paytype,p.paySID ,"
					
					+ "(select  concat(p1.paymentid,',',p1.paytype) from payment p1 where p1.paystatus=1 and "
					+ "p1.payflag!='Y' and p1.paymentid!='' and "
					+ "LEFT(p1.orderid,"+length+")=LEFT(?,"+length+") limit 1)  as paymentidtype,"
					
					+ "(select sum(payment_amount) as payall from payment p4 where p4.paystatus=1 and "
					+ "LEFT(p4.orderid,"+length+")=LEFT(?,"+length+") and (p4.paytype<3 ) ) as payall,"
					
					+ "(select sum(payment_amount) as bpay from payment p6 where p6.paystatus=1 and "
					+ "LEFT(p6.orderid,"+length+")=LEFT(?,"+length+") and (p6.paytype=2 )) as bpay "
					
					+ "FROM payment p WHERE p.paystatus =? and LEFT(p.orderid,16) = LEFT(?,16)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setString(2, orderNo);
			stmt.setString(3, orderNo);
			stmt.setInt(4, paystatus);
			stmt.setString(5, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Payment pay = new Payment();
				String orderid = rs.getString("orderid");
				orderid = StrUtils.isNotNullEmpty(orderid)?orderid.trim():"";
				pay.setOrderid(orderid);
				String paytype =rs.getString("paytype"); 
				String paymentidtype = rs.getString("paymentidtype");
				String paymentid = rs.getString("paymentid");
				
				if("3".equals(paytype)||"4".equals(paytype)){
					pay.setOrderSplit("3".equals(paytype)?"2":"1");
//					System.err.println("paymentidtype:"+paymentidtype);
					if(StrUtils.isNotNullEmpty(paymentidtype)){
						String[] paymentidtypes = paymentidtype.split(",");
//						System.err.println("paymentidtypes[0]:"+paymentidtypes[0]);
//						System.err.println("paymentidtypes[1]:"+paymentidtypes[1]);
						paymentid = StrUtils.isNullOrEmpty(paymentid)?paymentidtypes[0]:paymentid;
						paytype = paymentidtypes.length>1?paymentidtypes[1]:paytype;
					}
				}else{
					pay.setOrderSplit("0");
				}
				pay.setPaymentid(paymentid);
				pay.setPaytype(paytype);
				pay.setOrderdesc(rs.getString("orderdesc"));
				pay.setPaystatus(rs.getInt("paystatus"));
				pay.setUsername(rs.getString("username"));
				pay.setPayment_amount(rs.getFloat("payment_amount"));
				pay.setPayment_cc(rs.getString("payment_cc"));
				pay.setUserid(rs.getInt("userid"));
				pay.setCreatetime(rs.getString("createtime"));
				pay.setPayflag(rs.getString("payflag"));
				pay.setPaySID(rs.getString("paySID"));
				pay.setPayAll(rs.getString("payall"));
				String bpay = rs.getString("bpay");
				pay.setBalancePay(StrUtils.isNullOrEmpty(bpay)?"0":bpay);
				payList.add(pay);
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
		
		return payList;
	}
}
