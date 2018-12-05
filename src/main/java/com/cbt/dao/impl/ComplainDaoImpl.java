package com.cbt.dao.impl;

import com.cbt.bean.Complain;
import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.bean.ComplainVO;
import com.cbt.dao.IComplainDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.page.Page;
import com.cbt.util.SqlSplitUtil;
import com.cbt.warehouse.util.StringUtil;
import com.google.common.collect.ArrayTable;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ComplainDaoImpl implements IComplainDao{

	@Override
	public Complain getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(Complain t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer add(Complain t) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ComplainVO getComplainByCid(Integer cid) {
		String sql="SELECT c.id cid,c.userid,c.userEmail,c.complainType,c.complainText,c.createTime,c.closeTime,c.ref_orderid,c.complainState,c.dealAdmin,c.dealAdminid"
				+ " from tb_complain c   "
				+ "where c.id=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ComplainVO rfb = new ComplainVO();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, cid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				rfb.setId(rs.getInt("cid"));
				rfb.setUserid((rs.getInt("userid")));
				rfb.setUserEmail(rs.getString("userEmail"));
				rfb.setComplainType(rs.getString("complainType"));
				rfb.setComplainText(rs.getString("complainText"));
				String stime=rs.getString("createTime");
				String etime=rs.getString("closeTime");
				if(stime!=""&&stime!=null){
				rfb.setCreatTime(stime.substring(0, 19));
				}
				if(etime!=""&&etime!=null){
					rfb.setCloseTime(etime.substring(0, 19));
				}
				rfb.setRefOrderId(rs.getString("ref_orderid"));
				rfb.setComplainState(rs.getInt("complainState"));
				rfb.setDealAdmin(rs.getString("dealAdmin"));
				rfb.setDealAdminid(rs.getInt("dealAdminid"));
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
		return rfb;
	}

	@Override
	public Integer afterResponse(Integer complainid, Integer chatid, Integer cfid) {
		String sql1 = "update tb_complain set complainState=1 where id = ?";
		String sql2 = "update tb_complain_file set complainChatid=? where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		int row =0;
		try {
			stmt = conn.prepareStatement(sql1);
			stmt.setInt(1, complainid);
			row+=stmt.executeUpdate();
			stmt2 = conn.prepareStatement(sql2);
			stmt2.setInt(1, chatid);
			stmt2.setInt(2, cfid);
			row+=stmt2.executeUpdate();
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
		return row;
	}

	@Override
	public Integer responseCustomer(ComplainChat t, String dealAdmin, Integer dealAdminId, Integer complainid) {
		// TODO Auto-generated method stub
		if(t.getChatAdminid()==0){
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int result = 0;
			//回复客户的投诉后 状态改为 沟通中1
			String sql3 = "UPDATE tb_complain SET dealAdmin=?,dealAdminid=?,complainState=1 WHERE id=?";
			conn = DBHelper.getInstance().getConnection2();
			try {
				stmt=conn.prepareStatement(sql3);
				stmt.setString(1, dealAdmin);
				stmt.setInt(2, dealAdminId);
				stmt.setInt(3, t.getComplainid());
				stmt.executeUpdate();
				sql3 = "update tb_complain_chat set readorno=1 where complainid ="+t.getComplainid()+" and flag=0";
				stmt=conn.prepareStatement(sql3);
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
		}else{
			return 0;
		}
	
	}

	@Override
	public List<ComplainVO> getCommunicatingByCidBG(Integer complainId) {
		String sql="SELECT cc.id,cc.complainid,cc.chatText,cc.chatAdmin,cc.chatTime,cc.flag,(select admName from admuser where id=( SELECT adminid from admin_r_user where userid=( SELECT userid from tb_complain where id=?) limit 1) limit 1) as admName,f.imgUrl"
				+ " from tb_complain_chat cc LEFT JOIN tb_complain_file f ON cc.id=f.complainChatid AND f.flag=1 WHERE cc.complainid = ? ORDER BY chatTime ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<ComplainVO> list = new ArrayList<ComplainVO>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, complainId);
			stmt.setInt(2, complainId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ComplainVO rfb = new ComplainVO();
				rfb.setCcchatid(rs.getInt(1));
				rfb.setId(rs.getInt(2));
				rfb.setCcchatText(rs.getString(3));
//				rfb.setCcchatAdmin(rs.getString(4));
				rfb.setCcchatAdmin(rs.getString("admName"));
				rfb.setCcchatTime(new java.util.Date(rs.getTimestamp(5).getTime()) );
				rfb.setCcflag(rs.getInt(6));
				rfb.setImgUrl(rs.getString("imgUrl"));
			//		rfb.setCfimgUrl(rs.getString(7));
				list.add(rfb);
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
	public Integer closeComplain(Integer complainid) {
		String sql1 = "update tb_complain set complainState=2,closeTime=now() where id = ?";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int row =0;
		try {
			stmt = conn.prepareStatement(sql1);
			stmt.setInt(1, complainid);
			row+=stmt.executeUpdate();
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
		return row;
	}

	@Override
	public List<ComplainFile> getImgsByCid(Integer complainId) {
		String sql="SELECT cf.imgUrl from tb_complain_file cf LEFT JOIN tb_complain c on c.id = cf.complainid WHERE c.id=? and cf.flag=0";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<ComplainFile> list = new ArrayList<ComplainFile>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, complainId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ComplainFile rfb = new ComplainFile();
				rfb.setImgUrl(rs.getString(1));
				list.add(rfb);
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
	public Page<ComplainVO> searchComplainByParam(Complain t, String username, Page page, String admName,int check) {
		int start= (page.getStartIndex()-1) *20;
		ArrayList<ComplainVO> rfbList = new ArrayList<ComplainVO>();
		StringBuilder sb = new StringBuilder("SELECT c.id,c.userid,c.userEmail,c.complainType,"
				+ "c.complainText,c.createTime,c.closeTime,c.ref_orderid,c.ref_goodsid,c.dispute_id,c.merchant_id,"
				+ "c.complainState,c.dealAdmin,c.dealAdminid,u.admName,"
				+" (SELECT COUNT(id) FROM tb_complain_chat WHERE complainid=c.id AND readorno=0 and flag=0) AS counts," +
				" (SELECT COUNT(id)+1 FROM tb_complain_chat WHERE complainid=c.id  and flag=0) AS customSum , "
				+ " (SELECT COUNT(id) FROM tb_complain_chat WHERE complainid=c.id and flag=1) AS salerSum "
				+ " from tb_complain c LEFT JOIN admin_r_user u on u.userid = c.userid  ");
		if(t.getComplainState() == 2){
			sb.append(" inner join tb_complain_chat b ON c.id=b.complainid ");
		}
		sb.append(" where 1=1");
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		PreparedStatement stmt1 = null;
		//ling,Sales1可以看所有的投诉
		if(admName!=null&&!"Ling".equalsIgnoreCase(admName) && !"Sales1".equalsIgnoreCase(admName) && !"kara".equalsIgnoreCase(admName) && !"emmaxie".equalsIgnoreCase(admName)
				&& !"admin1".equalsIgnoreCase(admName)){
			sb.append(" and u.admName='"+admName+"'" ); 
		}
		if(check == 1) {
			
			sb.append(" and (c.ref_goodsid='' or c.ref_goodsid is null)");
		}
		if(t.getUserid()!=0){
			sb.append(" and c.userid="+t.getUserid());
		}
		if(StringUtil.isNotBlank(t.getCreatTime())){
			 sb.append(" and c.createTime like \"%"+t.getCreatTime()+"%\"");
		}
		if(StringUtil.isNotBlank(t.getComplainType())){
			sb.append(" and c.complainType ='"+t.getComplainType()+"'");
		}
		if(t.getComplainState() == 2){
			sb.append(" and b.readorno=0 AND b.flag=0 ");
		}else if(t.getComplainState()!=-1){
			sb.append(" and c.complainState ="+t.getComplainState());
		}else {
			sb.append(" and 1=1 ");
		}
		sb.append(" order by c.createTime desc limit "+ start+","+page.getOnePageCount());
		String aaa = sb.toString();
		try {
			stmt = conn.prepareStatement(sb.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ComplainVO rfb = new ComplainVO();
				rfb.setId(rs.getInt("id"));
				rfb.setUserid((rs.getInt("userid")));
				rfb.setUserEmail(rs.getString("userEmail"));
				rfb.setComplainType(rs.getString("complainType"));
				rfb.setComplainText(rs.getString("complainText"));
				if(rs.getString("createTime")!=""&&rs.getString("createTime")!=null){
					rfb.setCreatTime(rs.getString("createTime").substring(0, 19));
				}
				if(rs.getString("closeTime")!="" &&rs.getString("closeTime")!=null){
					rfb.setCloseTime(rs.getString("closeTime").substring(0, 19));
				}
				String ref_orderid = rs.getString("ref_orderid");
				rfb.setRefOrderId(ref_orderid);
				if(StringUtils.isNotBlank(ref_orderid)) {
					rfb.setOrderIdList(Arrays.asList(ref_orderid.split(",")));
				}
				rfb.setDisputeId(rs.getString("dispute_id"));
				rfb.setMerchantId(rs.getString("merchant_id"));
				rfb.setRefGoodsId(rs.getString("ref_goodsid"));
				rfb.setComplainState(rs.getInt("complainState"));
				rfb.setDealAdmin(rs.getString("dealAdmin"));
				rfb.setDealAdminid(rs.getInt("dealAdminid"));
				rfb.setSaleAdmin(rs.getString("admName"));
				rfb.setCounts(rs.getInt("counts"));
				rfb.setCustomSum(rs.getInt("customSum"));
				rfb.setSalerSum(rs.getInt("salerSum"));
				rfbList.add(rfb);
			}
			String countSql=SqlSplitUtil.coutSqlCombine(aaa);
			stmt1 = conn.prepareStatement(countSql);
			rs1 = stmt1.executeQuery();
			int rowCount=0;
			while (rs1.next()) {
				rowCount=rs1.getInt(1);
			}
			page.setCountRecord(rowCount);
			page.setList(rfbList);
			page.calculate();
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
		return page;
	}
	@Override
	public int updateGoodsid(int id, String orderid, String goodsid) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int rs = 0;
		String sql = "update  tb_complain set ref_goodsid=?,ref_orderid=? where id=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodsid);
			stmt.setString(2, orderid);
			stmt.setInt(3, id);
			rs = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		DBHelper.getInstance().closeConnection(conn);
		return rs;
	}
	@Override
	public int updateDisputeid(int id,String disputeid,String merchantid) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int rs = 0;
		String sql = "update  tb_complain set dispute_id=?,merchant_id=? where id=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, disputeid);
			stmt.setString(2, merchantid);
			stmt.setInt(3, id);
			rs = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		DBHelper.getInstance().closeConnection(conn);
		return rs;
	}

	@Override
	public List<ComplainVO> getComplainByDisputeId(List<String> disputeIdList) {
		List<ComplainVO> rfbList = new ArrayList<ComplainVO>();
		StringBuilder sb = new StringBuilder("SELECT id,userid,complainText from tb_complain where dispute_id in (");

		for(String disputeId : disputeIdList) {
			sb.append("\"").append(disputeId).append("\",");
		}
		sb.append("\"0\"").append(")");
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
	
		try {
			stmt = conn.prepareStatement(sb.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ComplainVO rfb = new ComplainVO();
				rfb.setId(rs.getInt("id"));
				rfb.setUserid((rs.getInt("userid")));
				rfb.setComplainText(rs.getString("complainText"));
				rfb.setDisputeId(rs.getString("dispute_id"));
				rfb.setMerchantId(rs.getString("merchant_id"));
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
	public List<ComplainVO> getComplainByUserId(String userId) {
		List<ComplainVO> rfbList = new ArrayList<ComplainVO>();
		StringBuilder sb = new StringBuilder("SELECT id,userid,complainText "
				+ "from tb_complain where userid=? order by id desc limit 30");

		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
	
		try {
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ComplainVO rfb = new ComplainVO();
				rfb.setId(rs.getInt("id"));
				rfb.setUserid((rs.getInt("userid")));
				rfb.setComplainText(rs.getString("complainText"));
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
}
