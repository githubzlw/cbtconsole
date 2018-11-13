package com.cbt.dao.impl;

import com.cbt.bean.*;
import com.cbt.dao.ProductOfflineDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Md5Util;
import com.cbt.website.userAuth.bean.Admuser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductOfflineDaoImpl implements ProductOfflineDao {
	private static final Log LOG = LogFactory.getLog(ProductOfflineDaoImpl.class);

	@Override
	public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean) {

		List<CategoryBean> list = new ArrayList<CategoryBean>();
		String sql = "select alc.cid,alc.category,alc.path,alc.lv,"
				+ "ifnull(cbm.total,0) as total from ali_category alc  left join "
				+ "(select catid,count(pid) as total  from custom_benchmark_offline where 1=1 ";

		if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
			sql = sql + " and publishtime >= ?";
		}
		if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
			sql = sql + " and publishtime <= ?";
		}
		if (queryBean.getState() > 0) {
			sql = sql + " and goodsstate = ?";
		}else{
			sql = sql + " and goodsstate < 6";
		}
		if (queryBean.getAdminId() > 0) {
			sql = sql + " and admin_id = ?";
		}
		if (queryBean.getIsEdited() > -1) {
			sql = sql + " and is_edited = ?";
		}
		if (queryBean.getIsAbnormal() > -1) {
			sql = sql + " and is_abnormal = ?";
		}
		if (queryBean.getIsBenchmark() > -1) {
			sql = sql + " and isBenchmark=? ";
		}
		if (queryBean.getBmFlag() > 0) {
			sql = sql + " and bm_flag=? ";
		}
		if (queryBean.getSourceProFlag() > 0) {
			sql = sql + " and source_pro_flag=? ";
		}
		if (queryBean.getSoldFlag() > 0) {
			sql = sql + " and is_sold_flag=? ";
		}
		if (queryBean.getPriorityFlag() > 0) {
			sql = sql + " and priority_flag=? ";
		}
		if (queryBean.getAddCarFlag() > 0) {
			sql = sql + " and is_add_car_flag=? ";
		}
		if (queryBean.getSourceUsedFlag() > 0) {
			sql = sql + " and source_used_flag=? ";
		}
		if (queryBean.getOcrMatchFlag() > 0) {
			sql = sql + " and ocr_match_flag=? ";
		}
//		if (queryBean.getRebidFlag() > 0) {
//			sql = sql + " and rebid_flag=? ";
//		}

		sql += " group by catid) cbm on alc.cid=cbm.catid order by alc.lv,alc.category asc ";

		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		System.err.println(sql);
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
				stmt.setString(index, queryBean.getSttime());
				index++;
			}
			if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
				stmt.setString(index, queryBean.getEdtime());
				index++;
			}
			if (queryBean.getState() != 0) {
				stmt.setInt(index, queryBean.getState());
				index++;
			}
			if (!(queryBean.getAdminId() == 0)) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}

			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
				index++;
			}
			if (queryBean.getIsAbnormal() > -1) {
				stmt.setInt(index, queryBean.getIsAbnormal());
				index++;
			}
			if (queryBean.getIsBenchmark() > -1) {
				stmt.setInt(index, queryBean.getIsBenchmark());
				index++;
			}
			if (queryBean.getBmFlag() > 0) {
				stmt.setInt(index, queryBean.getBmFlag());
				index++;
			}
			if (queryBean.getSourceProFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceProFlag());
				index++;
			}
			if (queryBean.getSoldFlag() > 0) {
				stmt.setInt(index, queryBean.getSoldFlag());
				index++;
			}
			if (queryBean.getPriorityFlag() > 0) {
				stmt.setInt(index, queryBean.getPriorityFlag());
				index++;
			}
			if (queryBean.getAddCarFlag() > 0) {
				stmt.setInt(index, queryBean.getAddCarFlag());
				index++;
			}
			if (queryBean.getSourceUsedFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceUsedFlag());
				index++;
			}
			if (queryBean.getOcrMatchFlag() > 0) {
				stmt.setInt(index, queryBean.getOcrMatchFlag());
				index++;
			}
			if (queryBean.getRebidFlag() > 0) {
				stmt.setInt(index, queryBean.getRebidFlag());
				index++;
			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				CategoryBean bean = new CategoryBean();
				bean.setCategoryName(rs.getString("category"));
				bean.setCid(rs.getString("cid"));
				bean.setPath(rs.getString("path"));
				bean.setLv(rs.getInt("lv"));
				bean.setTotal(rs.getInt("total"));
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryCateroryByParam error :" + e.getMessage());
			LOG.error("queryCateroryByParam error :" + e.getMessage());
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

		return list;
	}

	@Override
	public CustomGoodsPublish getGoods(String pid, int type) {
		String sql = "select * from custom_benchmark_offline where pid=? limit 1";
		Connection conn = type == 0 ? DBHelper.getInstance().getConnection() : DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		CustomGoodsPublish bean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				bean = new CustomGoodsPublish();
				bean.setCatid(rs.getString("catid"));
				bean.setCatid1(rs.getString("catid1"));
				// bean.setCreatetime(rs.getString("createtime"));
				// bean.setDetail(rs.getString("detail"));
				bean.setEndetail(rs.getString("endetail"));
				bean.setEninfo(rs.getString("eninfo"));
				bean.setEntype(rs.getString("entype"));
				bean.setFeeprice(rs.getString("feeprice"));
				bean.setFprice(rs.getString("fprice"));
				bean.setImg(rs.getString("img"));
				// bean.setInfo(rs.getString("info"));
				bean.setKeyword(rs.getString("keyword"));
				bean.setLocalpath(rs.getString("localpath"));
				// bean.setMethod("ePacket");
				bean.setMorder(rs.getInt("morder"));
				// bean.setName(rs.getString("name"));
				bean.setEnname(rs.getString("enname"));
				bean.setPid(rs.getString("pid"));
				// bean.setPosttime("9-15");
				bean.setPrice(rs.getString("price"));
				bean.setRemotpath(rs.getString("remotpath"));
				bean.setSku(rs.getString("sku"));
				bean.setSold(rs.getInt("sold"));
				// bean.setSolds(rs.getInt("solds"));
				// bean.setType(rs.getString("type"));
				// bean.setUpdatetime(rs.getString("updatetime"));
				bean.setUrl("https://detail.1688.com/offer/" + rs.getString("pid") + ".html");
				bean.setValid(rs.getInt("valid"));
				// bean.setVolum(rs.getString("volum"));
				bean.setWeight(rs.getString("weight"));
				bean.setFinalWeight(rs.getString("final_weight"));
				bean.setWprice(rs.getString("wprice"));
				bean.setReviseWeight(rs.getString("revise_weight"));
				bean.setRangePrice(rs.getString("range_price"));
				// bean.setLastPrice(rs.getString("lastprice"));
				bean.setIsEdited(rs.getInt("is_edited"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getGoods error :" + e.getMessage());
			LOG.error("getGoods error :" + e.getMessage());
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
			if (type == 0) {
				DBHelper.getInstance().closeConnection(conn);
			} else {
				DBHelper.getInstance().closeConnection(conn);
			}
		}
		return bean;
	}

	@Override
	public List<CustomGoodsBean> getGoodsListByCatid(String catid) {
		String sql = "select pid,eninfo from custom_goods where catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<CustomGoodsBean> list = new ArrayList<CustomGoodsBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CustomGoodsBean bean = new CustomGoodsBean();
				bean.setPid(rs.getString("pid"));
				bean.setEninfo(rs.getString("eninfo"));
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getGoodsListByCatid error :" + e.getMessage());
			LOG.error("getGoodsListByCatid error :" + e.getMessage());
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
		return list;
	}

	@Override
	public int updateInfo(CustomGoodsBean bean) {
		String sql = "update custom_goods set keyword=?,enname=?,price=?,weight=?,sku=?,"
				+ "endetail=?, eninfo=?,lastprice=? where pid=? ";

		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getKeyword());
			stmt.setString(2, bean.getEnname());
			stmt.setDouble(3, Double.valueOf(bean.getPrice()));
			stmt.setString(4, bean.getWeight());
			stmt.setString(5, bean.getSku());
			stmt.setString(6, bean.getEndetail());
			// stmt.setString(6, bean.getMethod());
			// stmt.setString(7, bean.getFeeprice());
			// stmt.setString(8, bean.getPosttime());
			stmt.setString(7, bean.getEninfo());
			stmt.setString(8, bean.getLastPrice());
			stmt.setString(9, bean.getPid());
			rs = stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateInfo error :" + e.getMessage());
			LOG.error("updateInfo error :" + e.getMessage());
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
		return rs;
	}

	@Override
	public int updateInfoList(List<CustomGoodsBean> list) {
		String sql = "update custom_goods set eninfo=? where pid=? ";

		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (CustomGoodsBean bean : list) {
				stmt.setString(1, bean.getEninfo());
				stmt.setString(2, bean.getPid());
				stmt.addBatch();
			}
			stmt.executeBatch();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateInfoList error :" + e.getMessage());
			LOG.error("updateInfoList error :" + e.getMessage());
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
		return rs;
	}

	@Override
	public int publish(CustomGoodsPublish bean) {

		String upsql = "update custom_benchmark_offline set valid=1,keyword=?,eninfo=?,enname=?,"
				+ "weight=?,img=?,endetail=?,feeprice=?,revise_weight=?,final_weight=?, "
				+ "price=?,wprice=?,range_price=?,sku=?,createtime=now()";
		if (bean.getIsEdited() == 1) {
			upsql += ",finalName=?";
		} else if (bean.getIsEdited() == 2) {
			upsql += ",finalName=?,infoReviseFlag=?,priceReviseFlag=?";
		}
		if (!(bean.getEninfo() == null || "".equals(bean.getEninfo()) || bean.getEninfo().length() < 10)) {
			upsql += ",is_show_det_img_flag=?";
		}
		upsql += " where pid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		int result = 0;
		try {
			int i = 1;
			stmt2 = conn.prepareStatement(upsql);
			stmt2.setString(i++, bean.getKeyword());
			stmt2.setString(i++, bean.getEninfo());
			stmt2.setString(i++, bean.getEnname());
			stmt2.setString(i++, bean.getWeight());
			stmt2.setString(i++, bean.getImg());
			stmt2.setString(i++, bean.getEndetail());
			stmt2.setString(i++, bean.getFeeprice());
			stmt2.setString(i++, bean.getReviseWeight());
			stmt2.setString(i++, bean.getFinalWeight());
			stmt2.setString(i++, bean.getPrice());
			stmt2.setString(i++, bean.getWprice());
			stmt2.setString(i++, bean.getRangePrice());
			stmt2.setString(i++, bean.getSku());
			if (bean.getIsEdited() == 1) {
				stmt2.setString(i++, bean.getEnname());
			} else if (bean.getIsEdited() == 2) {
				stmt2.setString(i++, bean.getEnname());
				stmt2.setInt(i++, 1);
				stmt2.setInt(i++, 1);
			}
			if (!(bean.getEninfo() == null || "".equals(bean.getEninfo()) || bean.getEninfo().length() < 10)) {
				stmt2.setInt(i++, 1);
			}
			stmt2.setString(i++, bean.getPid());
			result = stmt2.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("publish error :" + e.getMessage());
			LOG.error("publish error :" + e.getMessage());
		} finally {
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
	public int updateState(int state, String pid, int adminid) {
		String sql = "update custom_benchmark_offline set goodsstate=?,admin_id=?,publishtime=now(),valid=1"
				+ " where pid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setInt(2, adminid);
			stmt.setString(3, pid);
			rs = stmt.executeUpdate();
			System.err.println(sql + ":" + pid);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateState error :" + e.getMessage());
			LOG.error("updateState error :" + e.getMessage());
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
		return rs;
	}


	@Override
	public int updateValid(int valid, String pid) {
		String sql = "update custom_goods set valid=? where pid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setString(2, pid);
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateValid error :" + e.getMessage());
			LOG.error("updateValid error :" + e.getMessage());
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
		return rs;
	}


	@Override
	public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean) {

		String sqlo = "select cbr.*,c8gd.* "
				+ " from (select cbu.keyword,cbu.catid,cbu.pid,cbu.enname,cbu.localpath,cbu.remotpath,cbu.goodsstate,cbu.custom_main_image,"
				+ "cbu.bm_flag,cbu.valid,cbu.isBenchmark,cbu.source_pro_flag,cbu.source_used_flag,cbu.ocr_match_flag,"
				+ "cbu.priority_flag,cbu.updatetime,cbu.publishtime,adu.admName as admin,ifnull(adu.id,0) as admin_id,"
				+ "cbu.is_edited,cbu.is_sold_flag,cbu.is_add_car_flag,cbu.is_abnormal"
				+ " from custom_benchmark_offline cbu left join (select id,admName from admuser where status =1) adu "
				+ "on cbu.admin_id = adu.id where 1=1 ";
		String sqlb = ") cbr left join  (select pid,name,img,ali_name,ali_img,ali_pid "
				+ "from custom_1688_goods where pid in(select pid from custom_benchmark_offline where 1=1 ";
		String sqle = ")) c8gd on cbr.pid = c8gd.pid where 1=1";
		if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
			sqlo = sqlo + " and cbu.catid  in(select cid from ali_category  where find_in_set(?,path))";
			sqlb = sqlb + " and catid  in(select cid from ali_category  where find_in_set(?,path))";
		}
		if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
			sqlo = sqlo + " and cbu.publishtime >= ?";
			sqlb = sqlb + " and publishtime >= ?";
		}
		if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
			sqlo = sqlo + " and cbu.publishtime <= ?";
			sqlb = sqlb + " and publishtime <= ?";
		}
		if (queryBean.getState() > 0) {
			sqlo = sqlo + " and cbu.goodsstate = ?";
			sqlb = sqlb + " and goodsstate = ?";
		}else{
			sqlo = sqlo + " and cbu.goodsstate < 6";
			sqlb = sqlb + " and goodsstate < 6";
		}
		if (queryBean.getAdminId() > 0) {
			sqlo = sqlo + " and cbu.admin_id = ?";
			sqlb = sqlb + " and admin_id = ?";
		}
		if (queryBean.getIsEdited() > -1) {
			sqlo = sqlo + " and cbu.is_edited = ?";
			sqlb = sqlb + " and is_edited = ?";
		}
		if (queryBean.getIsAbnormal() > -1) {
			sqlo = sqlo + " and cbu.is_abnormal = ?";
			sqlb = sqlb + " and is_abnormal = ?";
		}
		if (queryBean.getIsBenchmark() > -1) {
			sqlo = sqlo + " and cbu.isBenchmark = ?";
			sqlb = sqlb + " and isBenchmark = ?";
		}
		if (queryBean.getBmFlag() > 0) {
			sqlo = sqlo + " and cbu.bm_flag = ?";
			sqlb = sqlb + " and bm_flag = ?";
		}
		if (queryBean.getSourceProFlag() > 0) {
			sqlo = sqlo + " and cbu.source_pro_flag = ?";
			sqlb = sqlb + " and source_pro_flag = ?";
		}
		if (queryBean.getSoldFlag() > 0) {
			sqlo = sqlo + " and cbu.is_sold_flag = ?";
			sqlb = sqlb + " and is_sold_flag = ?";
		}
		if (queryBean.getPriorityFlag() > 0) {
			sqlo = sqlo + " and cbu.priority_flag = ?";
			sqlb = sqlb + " and priority_flag = ?";
		}
		if (queryBean.getAddCarFlag() > 0) {
			sqlo = sqlo + " and cbu.is_add_car_flag = ?";
			sqlb = sqlb + " and is_add_car_flag = ?";
		}
		if (queryBean.getSourceUsedFlag() > 0) {
			sqlo = sqlo + " and cbu.source_used_flag = ?";
			sqlb = sqlb + " and source_used_flag = ?";
		}
		if (queryBean.getOcrMatchFlag() > 0) {
			sqlo = sqlo + " and cbu.ocr_match_flag = ?";
			sqlb = sqlb + " and ocr_match_flag = ?";
		}
//		if (queryBean.getRebidFlag() > 0) {
//			sqlo = sqlo + " and cbu.rebid_flag = ?";
//			sqlb = sqlb + " and rebid_flag = ?";
//		}

		sqlo += sqlb + sqle + " group by cbr.pid limit " + ((queryBean.getPage() - 1) * 40) + ",40";

		List<CustomGoodsPublish> list = new ArrayList<CustomGoodsPublish>();
		Connection conn = DBHelper.getInstance().getConnection();

		ResultSet rs = null;
		PreparedStatement stmt = null;
		int index = 1;
		try {
			System.err.println(sqlo);
			stmt = conn.prepareStatement(sqlo);
			if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
				stmt.setString(index, queryBean.getCatid());
				index++;
			}
			if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
				stmt.setString(index, queryBean.getSttime());
				index++;
			}
			if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
				stmt.setString(index, queryBean.getEdtime());
				index++;
			}
			if (queryBean.getState() != 0) {
				stmt.setInt(index, queryBean.getState());
				index++;
			}
			if (queryBean.getAdminId() > 0) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}
			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
				index++;
			}
			if (queryBean.getIsAbnormal() > -1) {
				stmt.setInt(index, queryBean.getIsAbnormal());
				index++;
			}
			if (queryBean.getIsBenchmark() > -1) {
				stmt.setInt(index, queryBean.getIsBenchmark());
				index++;
			}
			if (queryBean.getBmFlag() > 0) {
				stmt.setInt(index, queryBean.getBmFlag());
				index++;
			}
			if (queryBean.getSourceProFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceProFlag());
				index++;
			}
			if (queryBean.getSoldFlag() > 0) {
				stmt.setInt(index, queryBean.getSoldFlag());
				index++;
			}
			if (queryBean.getPriorityFlag() > 0) {
				stmt.setInt(index, queryBean.getPriorityFlag());
				index++;
			}
			if (queryBean.getAddCarFlag() > 0) {
				stmt.setInt(index, queryBean.getAddCarFlag());
				index++;
			}
			if (queryBean.getSourceUsedFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceUsedFlag());
				index++;
			}
			if (queryBean.getOcrMatchFlag() > 0) {
				stmt.setInt(index, queryBean.getOcrMatchFlag());
				index++;
			}
			if (queryBean.getRebidFlag() > 0) {
				stmt.setInt(index, queryBean.getRebidFlag());
				index++;
			}

			// 二次设置
			if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
				stmt.setString(index, queryBean.getCatid());
				index++;
			}
			if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
				stmt.setString(index, queryBean.getSttime());
				index++;
			}
			if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
				stmt.setString(index, queryBean.getEdtime());
				index++;
			}
			if (queryBean.getState() != 0) {
				stmt.setInt(index, queryBean.getState());
				index++;
			}
			if (queryBean.getAdminId() > 0) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}
			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
				index++;
			}
			if (queryBean.getIsAbnormal() > -1) {
				stmt.setInt(index, queryBean.getIsAbnormal());
				index++;
			}
			if (queryBean.getIsBenchmark() > -1) {
				stmt.setInt(index, queryBean.getIsBenchmark());
				index++;
			}
			if (queryBean.getBmFlag() > 0) {
				stmt.setInt(index, queryBean.getBmFlag());
				index++;
			}
			if (queryBean.getSourceProFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceProFlag());
				index++;
			}
			if (queryBean.getSoldFlag() > 0) {
				stmt.setInt(index, queryBean.getSoldFlag());
				index++;
			}
			if (queryBean.getPriorityFlag() > 0) {
				stmt.setInt(index, queryBean.getPriorityFlag());
				index++;
			}
			if (queryBean.getAddCarFlag() > 0) {
				stmt.setInt(index, queryBean.getAddCarFlag());
				index++;
			}
			if (queryBean.getSourceUsedFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceUsedFlag());
				index++;
			}
			if (queryBean.getOcrMatchFlag() > 0) {
				stmt.setInt(index, queryBean.getOcrMatchFlag());
				index++;
			}
			if (queryBean.getRebidFlag() > 0) {
				stmt.setInt(index, queryBean.getRebidFlag());
				index++;
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				CustomGoodsPublish bean = new CustomGoodsPublish();
				bean.setKeyword(rs.getString("keyword"));
				bean.setCatid(rs.getString("catid"));
				bean.setPid(rs.getString("pid"));
				bean.setEnname(rs.getString("enname"));
				bean.setLocalpath(rs.getString("localpath"));
				bean.setRemotpath(rs.getString("remotpath"));
				bean.setPublishtime(rs.getString("publishtime"));
				bean.setAdmin(rs.getString("admin"));
				bean.setCanEdit(rs.getInt("admin_id"));
				bean.setUpdatetime(rs.getString("updatetime"));
				bean.setUrl("&source=D" + Md5Util.encoder(rs.getString("pid")) + "&item=" + rs.getString("pid"));

				String name1688 = rs.getString("name");
				if (name1688 == null || "".equals(name1688)) {
					bean.setName("1688 商品url");
				} else {
					bean.setName(name1688);
				}

				String remotpath = rs.getString("remotpath");
				String main_image = rs.getString("img");
				if (!(main_image == null || "".equals(main_image))) {
					String indexImage = main_image.split(",")[0].replace("[", "").replace("]", "");
					// 判断图片是否是下架，含有http的，如https://cbu01.alicdn.com/img/ibank/2015/924/654/2546456429_577040841.60x60.jpg
					if (indexImage.indexOf("http") >= 0) {
						if (indexImage.indexOf("60x60") >= 0) {
							bean.setImg(indexImage.replace("60x60", "200x200"));
						} else {
							bean.setImg(indexImage);
						}
					} else {
						// 统一400图片
						if (indexImage.indexOf("60x60") >= 0) {
							bean.setImg(remotpath + indexImage.replace("60x60", "400x400"));
						} else {
							bean.setImg(remotpath + indexImage);
						}
					}
				} else {
					bean.setImg("");
				}

				bean.setShowMainImage(remotpath + rs.getString("custom_main_image"));
				String aliPid = rs.getString("ali_pid");
				if (aliPid == null || "".equals(aliPid)) {
					aliPid = "0";
				}
				bean.setAliGoodsPid(aliPid);
				String ali_name = rs.getString("ali_name");
				if (ali_name == null || "".equals(ali_name)) {
					ali_name = "aliexpress goods url";
				} else {
					ali_name = ali_name.replaceAll("\\\\", "/").replace("100%", "").replace("%", "");
				}

				bean.setAliGoodsName(ali_name);
				bean.setAliGoodsUrl("https://www.aliexpress.com/item/" + ali_name + "/" + aliPid + ".html");
				bean.setAliGoodsImgUrl(rs.getString("ali_img"));

				bean.setValid(rs.getInt("valid"));

				bean.setGoodsState(rs.getInt("goodsstate"));
				bean.setIsEdited(rs.getInt("is_edited"));
				bean.setIsBenchmark(rs.getInt("isBenchmark"));
				bean.setIsAbnormal(rs.getString("is_abnormal"));

				bean.setBmFlag(rs.getInt("bm_flag"));
				bean.setSourceProFlag(rs.getInt("source_pro_flag"));
				bean.setSoldFlag(rs.getInt("is_sold_flag"));

				bean.setPriorityFlag(rs.getInt("priority_flag"));
				bean.setAddCarFlag(rs.getInt("is_add_car_flag"));
				bean.setSourceUsedFlag(rs.getInt("source_used_flag"));
				bean.setOcrMatchFlag(rs.getInt("ocr_match_flag"));
				//bean.setRebidFlag(rs.getInt("rebid_flag"));
				//bean.setImgDownFlag(rs.getInt("img_down_flag"));

				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryGoodsInfos error :" + e.getMessage());
			LOG.error("queryGoodsInfos error :" + e.getMessage());
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
		return list;
	}

	@Override
	public int queryGoodsInfosCount(CustomGoodsQuery queryBean) {

		String sql = "select count(pid) from (select pid from custom_benchmark_offline where 1=1  ";
		if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
			System.err.println("catid:" + queryBean.getCatid());
			sql = sql + " and catid in(select cid from ali_category  where find_in_set(?,path))";
		}
		if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
			sql = sql + " and publishtime > ?";
		}
		if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
			sql = sql + " and publishtime < ?";
		}

		if (queryBean.getState() > 0) {
			sql = sql + " and goodsstate = ?";
		}else{
			sql = sql + " and goodsstate < 6";
		}
		if (queryBean.getAdminId() > 0) {
			sql = sql + " and admin_id = ?";
		}
		if (queryBean.getIsEdited() > -1) {
			sql = sql + " and is_edited = ?";
		}
		if (queryBean.getIsAbnormal() > -1) {
			sql = sql + " and is_abnormal = ?";
		}
		if (queryBean.getIsBenchmark() > -1) {
			sql = sql + " and isBenchmark=? ";
		}
		if (queryBean.getBmFlag() > 0) {
			sql = sql + " and bm_flag=? ";
		}
		if (queryBean.getSourceProFlag() > 0) {
			sql = sql + " and source_pro_flag=? ";
		}
		if (queryBean.getSoldFlag() > 0) {
			sql = sql + " and is_sold_flag=? ";
		}
		if (queryBean.getPriorityFlag() > 0) {
			sql = sql + " and priority_flag=? ";
		}
		if (queryBean.getAddCarFlag() > 0) {
			sql = sql + " and is_add_car_flag=? ";
		}
		if (queryBean.getSourceUsedFlag() > 0) {
			sql = sql + " and source_used_flag=? ";
		}
		if (queryBean.getOcrMatchFlag() > 0) {
			sql = sql + " and ocr_match_flag=? ";
		}
//		if (queryBean.getRebidFlag() > 0) {
//			sql = sql + " and rebid_flag=? ";
//		}

		sql += " ) custom_benchmark";
		Connection conn = DBHelper.getInstance().getConnection();

		ResultSet rs = null;
		PreparedStatement stmt = null;
		int count = 0;

		try {
			System.err.println(sql);

			stmt = conn.prepareStatement(sql);
			int index = 1;
			if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
				stmt.setString(index, queryBean.getCatid());
				index++;
			}
			if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
				stmt.setString(index, queryBean.getSttime());
				index++;
			}
			if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
				stmt.setString(index, queryBean.getEdtime());
				index++;
			}
			if (queryBean.getState() != 0) {
				stmt.setInt(index, queryBean.getState());
				index++;
			}
			if (!(queryBean.getAdminId() == 0)) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}

			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
				index++;
			}
			if (queryBean.getIsAbnormal() > -1) {
				stmt.setInt(index, queryBean.getIsAbnormal());
				index++;
			}
			if (queryBean.getIsBenchmark() > -1) {
				stmt.setInt(index, queryBean.getIsBenchmark());
				index++;
			}
			if (queryBean.getBmFlag() > 0) {
				stmt.setInt(index, queryBean.getBmFlag());
				index++;
			}
			if (queryBean.getSourceProFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceProFlag());
				index++;
			}
			if (queryBean.getSoldFlag() > 0) {
				stmt.setInt(index, queryBean.getSoldFlag());
				index++;
			}
			if (queryBean.getPriorityFlag() > 0) {
				stmt.setInt(index, queryBean.getPriorityFlag());
				index++;
			}
			if (queryBean.getAddCarFlag() > 0) {
				stmt.setInt(index, queryBean.getAddCarFlag());
				index++;
			}
			if (queryBean.getSourceUsedFlag() > 0) {
				stmt.setInt(index, queryBean.getSourceUsedFlag());
				index++;
			}
			if (queryBean.getOcrMatchFlag() > 0) {
				stmt.setInt(index, queryBean.getOcrMatchFlag());
				index++;
			}
			if (queryBean.getRebidFlag() > 0) {
				stmt.setInt(index, queryBean.getRebidFlag());
				index++;
			}

			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryGoodsInfosCount error :" + e.getMessage());
			LOG.error("queryGoodsInfosCount error :" + e.getMessage());
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
		return count;
	}

	@Override
	public void batchSaveEnName(Admuser user, List<CustomGoodsBean> cgLst) {
		Connection conn = DBHelper.getInstance().getConnection();
		String btSql = "update custom_benchmark_offline set enname = ?,goodsstate=?,admin_id=?,"
				+ "updatetime=now(),is_edited='1' where pid = ? ";
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(btSql);
			for (CustomGoodsBean cg : cgLst) {
				stmt.setString(1, cg.getEnname());
				stmt.setInt(2, 5);
				stmt.setInt(3, user.getId());
				stmt.setString(4, cg.getPid());
				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("batchSaveEnName error :" + e.getMessage());
			LOG.error("batchSaveEnName error :" + e.getMessage());
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
	public CustomGoodsPublish queryGoodsDetails(String pid, int type) {

		String sql = "select cbu.pid,cbu.enname,cbu.endetail,cbu.eninfo,cbu.feeprice,cbu.fprice,"
				+ "cbu.keyword,cbu.custom_main_image as main_img,cbu.price,cbu.final_weight,cbu.wprice,cbu.entype,cbu.sku,"
				+ "cbu.goodsstate,cbu.bm_flag,cbu.valid,cbu.isBenchmark,cbu.source_pro_flag,cbu.source_used_flag,"
				+ "cbu.ocr_match_flag,cbu.priority_flag,cbu.img,cbu.range_price,cbu.revise_weight,cbu.localpath,cbu.remotpath,"
				+ "cbu.is_sold_flag,cbu.is_add_car_flag,cbu.ali_pid,cbu.is_edited,cbu.updatetime,adu.admName as admin,"
				+ "ifnull(adu.id,0) as admin_id,cbu.is_abnormal"
				+ " from custom_benchmark_offline cbu left join (select id,admName from admuser where status =1) adu "
				+ "on cbu.admin_id = adu.id where cbu.pid=? and cbu.goodsstate < 6";
		Connection conn = type == 0 ? DBHelper.getInstance().getConnection() : DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		CustomGoodsPublish bean = null;
		try {
			System.err.println(sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				bean = new CustomGoodsPublish();
				bean.setCanEdit(rs.getInt("admin_id"));
				bean.setAdmin(rs.getString("admin"));
				bean.setEndetail(rs.getString("endetail"));
				bean.setEninfo(rs.getString("eninfo"));
				bean.setFeeprice(rs.getString("feeprice"));
				bean.setFprice(rs.getString("fprice"));
				bean.setKeyword(rs.getString("keyword"));
				bean.setLocalpath(rs.getString("localpath"));
				bean.setEnname(rs.getString("enname"));
				bean.setRangePrice(rs.getString("range_price"));
				bean.setReviseWeight(rs.getString("revise_weight"));
				bean.setFinalWeight(rs.getString("final_weight"));
				bean.setPid(rs.getString("pid"));
				bean.setPrice(rs.getString("price"));
				bean.setImg(rs.getString("img"));
				bean.setShowMainImage(rs.getString("main_img"));
				bean.setSku(rs.getString("sku"));
				bean.setEntype(rs.getString("entype"));
				bean.setRemotpath(rs.getString("remotpath"));

				bean.setUrl("https://detail.1688.com/offer/" + rs.getString("pid") + ".html");
				String wprice = rs.getString("wprice");
				if (wprice == null || "".equals(wprice)) {
					bean.setWprice("");
				} else {
					//bean.setWprice(wprice.substring(1, wprice.length() - 1).replace("$", "@"));
					bean.setWprice(wprice.replace("[", "").replace("]", "").replace("$", "@"));
				}
				String aliPid = rs.getString("ali_pid");
				if (aliPid == null || "".equals(aliPid)) {
					aliPid = "0";
				}
				bean.setAliGoodsPid(aliPid);
				bean.setAliGoodsUrl("https://www.aliexpress.com/item/aligoods/" + aliPid + ".html");
				bean.setUpdatetime(rs.getString("updatetime"));

				bean.setGoodsState(rs.getInt("goodsstate"));
				bean.setIsEdited(rs.getInt("is_edited"));
				bean.setIsBenchmark(rs.getInt("isBenchmark"));
				bean.setIsAbnormal(rs.getString("is_abnormal"));

				bean.setBmFlag(rs.getInt("bm_flag"));
				bean.setSourceProFlag(rs.getInt("source_pro_flag"));
				bean.setSoldFlag(rs.getInt("is_sold_flag"));

				bean.setPriorityFlag(rs.getInt("priority_flag"));
				bean.setAddCarFlag(rs.getInt("is_add_car_flag"));
				bean.setSourceUsedFlag(rs.getInt("source_used_flag"));
				bean.setOcrMatchFlag(rs.getInt("ocr_match_flag"));
				//bean.setRebidFlag(rs.getInt("rebid_flag"));
				//bean.setImgDownFlag(rs.getInt("img_down_flag"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryGoodsDetails error :" + e.getMessage());
			LOG.error("queryGoodsDetails error :" + e.getMessage());
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
			if (type == 0) {
				DBHelper.getInstance().closeConnection(conn);
			} else {
				DBHelper.getInstance().closeConnection(conn);
			}
		}
		return bean;
	}
	
	

	@Override
	public int saveEditDetalis(CustomGoodsPublish cgp, int adminId, int type) {

		Connection conn = DBHelper.getInstance().getConnection();
		String upSql = "update custom_benchmark_offline set enname = ?,feeprice = ?,"
				+ "img = ?,endetail = ?,eninfo=?,goodsstate=?,keyword=?,";
		if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight()) || "0".equals(cgp.getReviseWeight()))) {
			upSql += "feeprice=?,revise_weight=?,final_weight=?,";
		}
		if (cgp.getRangePrice() == null || "".equals(cgp.getRangePrice())) {
			upSql += "price=?,wprice=?,";
		} else {
			upSql += "range_price=?,sku=?,";
		}
		upSql += "admin_id=?,updatetime=sysdate(),is_edited='2' where pid = ? ";
		PreparedStatement stmt = null;
		int rs = 0;
		int i = 1;
		try {
			stmt = conn.prepareStatement(upSql);
			stmt.setString(i++, cgp.getEnname());
			stmt.setString(i++, cgp.getFeeprice());
			stmt.setString(i++, cgp.getImg());
			stmt.setString(i++, cgp.getEndetail());
			stmt.setString(i++, cgp.getEninfo());
			// type为1需要发布的，否则是待发布
			stmt.setInt(i++, type == 1 ? 4 : 5);
			stmt.setString(i++, cgp.getKeyword());
			if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight())
					|| "0".equals(cgp.getReviseWeight()))) {
				stmt.setString(i++, cgp.getFeeprice());
				stmt.setString(i++, cgp.getReviseWeight());
				stmt.setString(i++, cgp.getReviseWeight());
			}
			if (cgp.getRangePrice() == null || "".equals(cgp.getRangePrice())) {
				stmt.setString(i++, cgp.getPrice());
				stmt.setString(i++, cgp.getWprice());
			} else {
				stmt.setString(i++, cgp.getRangePrice());
				stmt.setString(i++, cgp.getSku());
			}
			stmt.setInt(i++, adminId);
			stmt.setString(i++, cgp.getPid());
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("saveEditDetalis error :" + e.getMessage());
			LOG.error("saveEditDetalis error :" + e.getMessage());
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
		return rs;
	}
	
	@Override
	public int replaceDetalisImgToLocal(CustomGoodsPublish cgp, int adminId) {

		Connection conn = DBHelper.getInstance().getConnection();
		String upSql = "update custom_benchmark_offline set custom_main_image = ?,entype = ?,"
				+ "img = ?,eninfo=?,remotpath=?,localpath = ?,admin_id=?,updatetime=sysdate(),"
				+ "is_edited='2' where pid = ?";
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			int i = 1;
			stmt = conn.prepareStatement(upSql);
			stmt.setString(i++, cgp.getShowMainImage());
			stmt.setString(i++, cgp.getEntype());
			stmt.setString(i++, cgp.getImg());
			stmt.setString(i++, cgp.getEninfo());
			stmt.setString(i++, cgp.getLocalpath());
			stmt.setString(i++, cgp.getLocalpath());
			stmt.setInt(i++, adminId);
			stmt.setString(i++, cgp.getPid());
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("replaceDetalisImgToLocal error :" + e.getMessage());
			LOG.error("replaceDetalisImgToLocal error :" + e.getMessage());
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
		return rs;
	}

	@Override
	public boolean batchDeletePids(String[] pidLst) {

		Connection conn = DBHelper.getInstance().getConnection();
		StringBuffer deSql = new StringBuffer("delete from custom_benchmark_offline where pid in(?");
		PreparedStatement stmt = null;
		boolean rs = false;
		for (int j = 0; j < pidLst.length - 1; j++) {
			deSql.append(",?");
		}
		deSql.append(")");
		try {
			System.err.println("deSql:" + deSql.toString());
			stmt = conn.prepareStatement(deSql.toString());
			for (int i = 0; i < pidLst.length; i++) {
				stmt.setString(i + 1, pidLst[i]);
			}

			rs = stmt.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("batchDeletePids error :" + e.getMessage());
			LOG.error("batchDeletePids error :" + e.getMessage());
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
		return rs;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean publishGoods(String pid,int adminId) {

		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn28 = DBHelper.getInstance().getConnection8();
		String querySql = "select * from custom_benchmark_offline where pid = ?";
		PreparedStatement stmt = null;
		ResultSet rss =null;
		boolean isSc = false;
		try {
			stmt = conn.prepareStatement(querySql);
			stmt.setString(1, pid);
			rss = stmt.executeQuery();
			if(rss.next()){
				CustomOnlineGoodsBean goods = new CustomOnlineGoodsBean();
				goods.setAliFreight(rss.getString("ali_freight"));
				goods.setAliImg(rss.getString("ali_img"));
				goods.setAliMorder(rss.getString("ali_morder"));
				goods.setAliName(rss.getString("ali_name"));
				goods.setAliPid(rss.getString("ali_pid"));
				goods.setAliPrice(rss.getString("ali_price"));
				goods.setAliSellunit(rss.getString("ali_sellunit"));
				goods.setAliSold(rss.getInt("ali_sold"));
				goods.setAliUnit(rss.getString("ali_unit"));
				goods.setAliWeight(rss.getString("ali_weight"));
				goods.setBmFlag(rss.getInt("bm_flag"));
				goods.setCatid(rss.getString("catid"));
				goods.setCatid1(rss.getString("catid1"));
				goods.setCatidb(rss.getString("catidb"));
				goods.setCatidParenta(rss.getString("catidparenta"));
				goods.setCatidParentb(rss.getString("catidparentb"));
				goods.setCatpath(rss.getString("catpath"));
				goods.setCreatetime(rss.getString("createtime"));
				goods.setCurTime(rss.getString("cur_time"));
				goods.setCustomMainImage(rss.getString("custom_main_image"));
				goods.setEndetail(rss.getString("endetail"));
				goods.setEninfo(rss.getString("eninfo"));
				goods.setEnname(rss.getString("enname"));
				goods.setEntype(rss.getString("entype"));
				goods.setFeeprice(rss.getString("feeprice"));
				goods.setFinalName(rss.getString("finalName"));
				goods.setFinalWeight(rss.getString("final_weight"));
				//goods.setFlag(rss.getInt("flag"));
				goods.setFprice(rss.getString("fprice"));
				goods.setFpriceStr(rss.getString("fprice_str"));
				goods.setGoodsState(rss.getInt("goodsstate"));
				goods.setId(rss.getInt("id"));
				goods.setImg(rss.getString("img"));
				goods.setImgCheck(rss.getInt("img_check"));
				goods.setInfoReviseFlag(rss.getInt("infoReviseFlag"));
				goods.setIsAddCarFlag(rss.getInt("is_add_car_flag"));
				goods.setIsBenchmark(rss.getInt("isBenchmark"));
				goods.setIsNewCloud(rss.getInt("isNewCloud"));
				goods.setIsShowDetImgFlag(rss.getInt("is_show_det_img_flag"));
				goods.setIsShowDetTableFlag(rss.getInt("is_show_det_table_flag"));
				goods.setIsSoldFlag(rss.getInt("is_sold_flag"));
				goods.setKeyword(rss.getString("keyword"));
				goods.setLocalPath(rss.getString("localpath"));
				goods.setMorder(rss.getInt("morder"));
				goods.setName(rss.getString("name"));
				goods.setOcrMatchFlag(rss.getInt("ocr_match_flag"));
				goods.setOriginalCatid(rss.getString("originalcatid"));
				goods.setOriginalCatpath(rss.getString("originalcatpath"));
				goods.setPid(rss.getString("pid"));
				goods.setPrice(rss.getDouble("price"));
				goods.setPriceReviseFlag(rss.getInt("priceReviseFlag"));
				goods.setPriorityFlag(rss.getInt("priority_flag"));
				goods.setPvids(rss.getString("pvids"));
				goods.setRangePrice(rss.getString("range_price"));
				goods.setRemotPath(rss.getString("remotpath"));
				goods.setReviseWeight(rss.getString("revise_weight"));
				goods.setSellUnit(rss.getString("sellunit"));
				goods.setShopId(rss.getString("shop_id"));
				goods.setSku(rss.getString("sku"));
				goods.setSold(rss.getInt("sold"));
				goods.setSourceProFlag(rss.getInt("source_pro_flag"));
				goods.setSourceUsedFlag(rss.getInt("source_used_flag"));
				goods.setValid(rss.getInt("valid"));
				goods.setWeight(rss.getString("weight"));
				goods.setWholesalePrice(rss.getString("wholesale_price"));
				goods.setWprice(rss.getString("wprice"));	
				
				if(goods.getGoodsState() == 6){
					System.err.println("pid:" + pid + " goodsState is 6");
					return false;
				}
				
				String insertSql = "insert into custom_benchmark_ready_newest("
						+ "ali_freight,ali_img,ali_morder,ali_name,ali_pid,ali_price,"
						+ "ali_sellunit,ali_sold,ali_unit,ali_weight,bm_flag,catid,catid1,"
						+ "catidb,catidparenta,catidparentb,catpath,createtime,cur_time,"
						+ "custom_main_image,endetail,eninfo,enname,entype,feeprice,finalName,"
						+ "final_weight,fprice,fprice_str,img,img_check,"
						+ "infoReviseFlag,is_add_car_flag,isBenchmark,isNewCloud,is_show_det_img_flag,"
						+ "is_show_det_table_flag,is_sold_flag,keyword,localpath,morder,name,"
						+ "ocr_match_flag,originalcatid,originalcatpath,pid,price,priceReviseFlag,"
						+ "priority_flag,pvids,range_price,remotpath,revise_weight,sellunit,shop_id,"
						+ "sku,sold,source_pro_flag,source_used_flag,valid,weight,wholesale_price,wprice) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),sysdate(),?,"
						+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?)";
				int i=1;
				stmt = conn28.prepareStatement(insertSql);
				stmt.setString(i++, goods.getAliFreight() == null ? "" :  goods.getAliFreight());
				stmt.setString(i++, goods.getAliImg() == null ? "" :  goods.getAliImg());
				stmt.setString(i++, goods.getAliMorder() == null ? "" :  goods.getAliMorder());
				stmt.setString(i++, goods.getAliName() == null ? "" :  goods.getAliName());
				stmt.setString(i++, goods.getAliPid() == null ? "" :  goods.getAliPid());
				stmt.setString(i++, goods.getAliPrice() == null ? "" :  goods.getAliPrice());
				stmt.setString(i++, goods.getAliSellunit() == null ? "" :  goods.getAliSellunit());
				stmt.setInt(i++, goods.getAliSold());
				stmt.setString(i++, goods.getAliUnit() == null ? "" :  goods.getAliUnit());
				stmt.setString(i++, goods.getAliWeight() == null ? "" :  goods.getAliWeight());
				stmt.setInt(i++, goods.getBmFlag());
				stmt.setString(i++, goods.getCatid() == null ? "" :  goods.getCatid());
				stmt.setString(i++, goods.getCatid1() == null ? "" :  goods.getCatid1());
				stmt.setString(i++, goods.getCatidb() == null ? "" :  goods.getCatidb());
				stmt.setString(i++, goods.getCatidParenta() == null ? "" :  goods.getCatidParenta());
				stmt.setString(i++, goods.getCatidParentb() == null ? "" :  goods.getCatidParentb());
				stmt.setString(i++, goods.getCatpath() == null ? "" :  goods.getCatpath());
				//stmt.setString(i++, goods.getCreatetime());
				//stmt.setString(i++, goods.getCurTime());
				stmt.setString(i++, goods.getCustomMainImage() == null ? "" :  goods.getCustomMainImage());
					
				stmt.setString(i++, goods.getEndetail() == null ? "" :  goods.getEndetail());
				stmt.setString(i++, goods.getEninfo() == null ? "" :  goods.getEninfo());
				stmt.setString(i++, goods.getEnname() == null ? "" :  goods.getEnname());
				stmt.setString(i++, goods.getEntype() == null ? "" :  goods.getEntype());
				stmt.setString(i++, goods.getFeeprice() == null ? "" :  goods.getFeeprice());
				stmt.setString(i++, goods.getFinalName() == null ? "" :  goods.getFinalName());
				stmt.setString(i++, goods.getFinalWeight() == null ? "" :  goods.getFinalWeight());
				stmt.setString(i++, goods.getFprice() == null ? "" :  goods.getFprice());
				stmt.setString(i++, goods.getFpriceStr() == null ? "" :  goods.getFpriceStr());
				stmt.setString(i++, goods.getImg() == null ? "" :  goods.getImg());
				stmt.setInt(i++, goods.getImgCheck());
				stmt.setInt(i++, goods.getInfoReviseFlag());
				stmt.setInt(i++, goods.getIsAddCarFlag());
				stmt.setInt(i++, goods.getIsBenchmark());
				stmt.setInt(i++, goods.getIsNewCloud());
				stmt.setInt(i++, goods.getIsShowDetImgFlag());
				stmt.setInt(i++, goods.getIsShowDetTableFlag());
				stmt.setInt(i++, goods.getIsSoldFlag());	
				stmt.setString(i++, goods.getKeyword() == null ? "" :  goods.getKeyword());
				stmt.setString(i++, goods.getLocalPath() == null ? "" :  goods.getLocalPath());
				
				stmt.setInt(i++, goods.getMorder());
				stmt.setString(i++, goods.getName() == null ? "" :  goods.getName());
				stmt.setInt(i++, goods.getOcrMatchFlag());
				stmt.setString(i++, goods.getOriginalCatid() == null ? "" :  goods.getOriginalCatid());
				stmt.setString(i++, goods.getOriginalCatpath() == null ? "" :  goods.getOriginalCatpath());
				stmt.setString(i++, goods.getPid() == null ? "" :  goods.getPid());
				stmt.setDouble(i++, goods.getPrice());
				stmt.setInt(i++, goods.getPriceReviseFlag());
				stmt.setInt(i++, goods.getPriorityFlag());
				stmt.setString(i++, goods.getPvids() == null ? "" :  goods.getPvids());
				stmt.setString(i++, goods.getRangePrice() == null ? "" :  goods.getRangePrice());
				stmt.setString(i++, goods.getRemotPath() == null ? "" :  goods.getRemotPath());
				stmt.setString(i++, goods.getReviseWeight() == null ? "" :  goods.getReviseWeight());
				stmt.setString(i++, goods.getSellUnit() == null ? "" :  goods.getSellUnit());
				stmt.setString(i++, goods.getShopId() == null ? "" :  goods.getShopId());
				stmt.setString(i++, goods.getSku() == null ? "" :  goods.getSku());
				stmt.setInt(i++, goods.getSold());
				stmt.setInt(i++, goods.getSourceProFlag());
				stmt.setInt(i++, goods.getSourceUsedFlag());
				stmt.setInt(i++, goods.getValid());
				
				stmt.setString(i++, goods.getWeight() == null ? "" :  goods.getWeight());
				stmt.setString(i++, goods.getWholesalePrice() == null ? "" :  goods.getWholesalePrice());
				stmt.setString(i++, goods.getWprice() == null ? "" :  goods.getWprice());
				
				int count = stmt.executeUpdate();
				if(count > 0){
					stmt.clearParameters();
					String updateSql = "update custom_benchmark_offline set goodsstate =6,"
							+ "admin_id=? where pid = ?";
					stmt = conn.prepareStatement(updateSql);
					stmt.setInt(1, adminId);
					stmt.setString(2, pid);
					isSc = stmt.executeUpdate() > 0;		
				}else{
					isSc = false;
				}		
			}else{
				isSc = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("publishGoods error :" + e.getMessage());
			LOG.error("publishGoods error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn28);
		}
		return isSc;
	}



}
