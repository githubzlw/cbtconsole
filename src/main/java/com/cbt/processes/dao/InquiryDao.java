package com.cbt.processes.dao;

import com.cbt.bean.InquiryDetail;
import com.cbt.bean.SpiderBean;
import com.cbt.jdbc.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InquiryDao implements IInquiry {

	@Override
	public int saveInquiry(int userid, String payno, double server_price,int  pay_state) {
		String sql = "insert inquiry(userid,payno,server_price,pay_state,createtime) values(?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userid);
			stmt.setString(2, payno);
			stmt.setDouble(3, server_price);
			stmt.setInt(4, pay_state);
			res = stmt.executeUpdate();
			 if (res == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					res = rs.getInt(1);
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
		return res;
	}

	@Override
	public List<InquiryDetail> getInquiry(int state, int userid ) {
		String sql = "select inquiry_details.id detailid,inquiry.id,pay_state,payno,createtime,goodsid,price,deliverytime,inquiry_details.state,goods_url,googs_img,goods_title,googs_number,remark from inquiry,inquiry_details,goods_car where inquiry.payno =inquiry_details.inquiryid and goods_car.id = inquiry_details.goodsid and inquiry.state=0 ";
		if(userid != 0){
			sql += " and inquiry.userid=?";
		}
		if(state != -1){
			sql += " and inquiry_details.state=?";
		}
		sql += " order by payno desc";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<InquiryDetail> inquirylist = new ArrayList<InquiryDetail>();
		try {
			stmt = conn.prepareStatement(sql);
			int i = 1;
			if(userid != 0){
				stmt.setInt(1, userid);
				i = 2;
			}
			if(state != -1){
				stmt.setInt(i, state);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				InquiryDetail inquiry = new InquiryDetail();
				inquiry.setId(rs.getInt("detailid"));
				inquiry.setInquiryId(rs.getInt("inquiry.id"));
				inquiry.setPayno(rs.getString("payno"));
				inquiry.setPrice(rs.getDouble("price"));
				inquiry.setState(rs.getInt("state"));
				inquiry.setUserid(userid);
				inquiry.setPay_state(rs.getInt("pay_state"));
				inquiry.setCreatetime(rs.getTimestamp("createtime"));
				inquiry.setDeliverytime(rs.getTimestamp("deliverytime"));
				SpiderBean spider = new SpiderBean();
				spider.setUrl(rs.getString("goods_url"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setRemark(rs.getString("remark"));
				inquiry.setSpiderBean(spider);
				inquirylist.add(inquiry);
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
		return inquirylist;
	}

	@Override
	public List<InquiryDetail> getInquiry(String inqId) {
		String sql = "select inquiry_details.id detailid,inquiry.userid,payno,createtime,goodsid,price,deliverytime,inquiry_details.state,goods_url,googs_img,goods_title,googs_number,remark from inquiry,inquiry_details,goods_car where inquiry.payno =inquiry_details.inquiryid and inquiry.state=0 and goods_car.id = inquiry_details.goodsid and inquiry_details.inquiryid=?";
	 
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<InquiryDetail> inquirylist = new ArrayList<InquiryDetail>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, inqId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				InquiryDetail inquiry = new InquiryDetail();
				inquiry.setId(rs.getInt("detailid"));
				inquiry.setPayno(rs.getString("payno"));
				inquiry.setPrice(rs.getDouble("price"));
				inquiry.setState(rs.getInt("state"));
				inquiry.setUserid(rs.getInt("userid"));
				inquiry.setCreatetime(rs.getTimestamp("createtime"));
				inquiry.setDeliverytime(rs.getTimestamp("deliverytime"));
				SpiderBean spider = new SpiderBean();
				spider.setUrl(rs.getString("goods_url"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setRemark(rs.getString("remark"));
				inquiry.setSpiderBean(spider);
				inquirylist.add(inquiry);
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
		return inquirylist;
	}
	
	@Override
	public int upInquiryPay(String payno,int state) {
		String sql = "update inquiry set pay_state=?  where payno = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setString(2, payno);
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
	public int saveInquiryDetail(List<String> goodsid, String inquiryid) {
		String sql = "insert inquiry_details(goodsid ,inquiryid ) values(?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int[] res = null ;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < goodsid.size(); i++) {
				stmt.setInt(1, Integer.parseInt(goodsid.get(i)));
				stmt.setString(2, inquiryid);
				stmt.addBatch();  
			}
			res = stmt.executeBatch();
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
		return res.length>0?1:0;
	}



	@Override
	public int upInquiryDetail(int id, int state, double price) {
		String sql = "update inquiry_details set state=?  ,price=? , deliverytime=now() where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setDouble(2, price);
			stmt.setInt(3, id);
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
	public int getInquiryNumber(int userid) {
		String sql = "select count(inquiry_details.id) counts from inquiry_details,inquiry where inquiry.payno =inquiry_details.inquiryid and inquiry.state=0 and  userid=? ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt("counts");
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
	public int delInquiry(String payno) {
		String sql = "update inquiry set state=1   where payno = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, payno);
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
	public List<String> getInquiryExist(String[] goodsId) {
		String sql = "select goodsid from inquiry_details where goodsid = ? ";
		for (int i = 0; i < goodsId.length-1; i++) {
			sql +=" or goodsid=?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> res = new ArrayList<String>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodsId[0]);
			res.add(goodsId[0]);
			for (int i = 1; i < goodsId.length; i++) {
				stmt.setString(i+1, goodsId[i]);
				res.add(goodsId[i]);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				res.remove(rs.getString("goodsid"));
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

}
