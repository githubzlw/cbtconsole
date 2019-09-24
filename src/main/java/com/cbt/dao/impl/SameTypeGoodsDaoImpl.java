package com.cbt.dao.impl;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.bean.SameTypeGoodsBean;
import com.cbt.dao.SameTypeGoodsDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.Admuser;
import com.cbt.util.Md5Util;
import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SameTypeGoodsDaoImpl implements SameTypeGoodsDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SameTypeGoodsDaoImpl.class);

	@Override
	public JsonResult batchAddUrl(String mainUrl, String urls, int adminId, int typeFlag, double aveWeight) {

		JsonResult json = new JsonResult();
		Connection conn28 = DBHelper.getInstance().getConnection5();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmtQry = null;
		PreparedStatement stmtSql = null;
		PreparedStatement stmtRel = null;
		PreparedStatement remoteStmt = null;
		ResultSet rset = null;
		String insertSql = "insert into same_1688_goods_offers(goods_pid,good_url,source_flag,"
				+ "admin_id) values(?,?,?,?) ";
		String insertRelation = "insert into same_1688_goods_relation(main_pid,low_pid,type_flag,is_main,ave_weight)"
				+ " values(?,?,?,?,?)";

		int rs = 0;
		String[] urlList = urls.split(";");
		String querySql = "select goods_pid from same_1688_goods_offers where goods_pid in(";
		try {

			conn28.setAutoCommit(false);
			remoteConn.setAutoCommit(false);
			int total = 0;
			// 预处理url到pid
			List<String> existsPids = new ArrayList<String>();
			Map<String, String> pidMap = new HashMap<String, String>();
			String endSql = "";
			// 主图部分预处理
			if (mainUrl.indexOf("?") > -1) {
				mainUrl = mainUrl.substring(0, mainUrl.indexOf("?"));
			}
			String mainPid = mainUrl.substring(mainUrl.lastIndexOf("/") + 1, mainUrl.indexOf(".html"));
			pidMap.put(mainPid, mainUrl);
			endSql += ",'" + mainPid + "'";
			// 同款部分预处理
			for (String scUrl : urlList) {
				if (scUrl == null || "".equals(scUrl)) {
					continue;
				} else {
					if (scUrl.indexOf("?") > -1) {
						scUrl = scUrl.substring(0, scUrl.indexOf("?"));
					}
					String cldPid = scUrl.substring(scUrl.lastIndexOf("/") + 1, scUrl.indexOf(".html"));
					pidMap.put(cldPid, scUrl);
					endSql += ",'" + cldPid + "'";
				}
			}
			endSql = endSql.substring(1);

			stmtQry = conn28.prepareStatement(querySql + endSql + ")");
			rset = stmtQry.executeQuery();
			while (rset.next()) {
				existsPids.add(rset.getString(1));
			}

			List<String> existsUrls = new ArrayList<String>();
			if (!(pidMap == null || pidMap.size() == 0)) {

				stmtSql = conn28.prepareStatement(insertSql);

				// 主图部分插入数据
				if (!(existsPids == null || existsPids.size() == 0)) {
					if (existsPids.contains(mainPid)) {
						json.setOk(false);
						json.setMessage("主图商品已经存在");
						return json;
					} else {
						// 主pid插入
						stmtSql.setString(1, mainPid);
						stmtSql.setString(2, mainUrl);
						stmtSql.setInt(3, 1);
						stmtSql.setInt(4, adminId);
						stmtSql.addBatch();
						total++;
					}
				}

				// 同款部分插入数据
				for (String pid : pidMap.keySet()) {
					if (!(pid == null || "".equals(pid))) {
						if (existsPids.size() > 0) {
							if (existsPids.contains(pid)) {
								existsUrls.add(pidMap.get(pid));
								continue;
							} else {
								stmtSql.setString(1, pid);
								stmtSql.setString(2, pidMap.get(pid));
								stmtSql.setInt(3, 1);
								stmtSql.setInt(4, adminId);
								stmtSql.addBatch();
								total++;
							}
						} else {
							stmtSql.setString(1, pid);
							stmtSql.setString(2, pidMap.get(pid));
							stmtSql.setInt(3, 1);
							stmtSql.setInt(4, adminId);
							stmtSql.addBatch();
							total++;
						}
					}
				}
				rs = stmtSql.executeBatch().length;
				if (rs == total) {
					stmtRel = conn28.prepareStatement(insertRelation);
					remoteStmt = remoteConn.prepareStatement(insertRelation);
					rs = 0;

					for (String pidTp : pidMap.keySet()) {
						stmtRel.setString(1, mainPid);
						stmtRel.setString(2, pidTp);
						stmtRel.setInt(3, typeFlag);
						stmtRel.setInt(4, mainPid.equals(pidTp) ? 1 : 0);
						stmtRel.setDouble(5, aveWeight);
						stmtRel.addBatch();
						remoteStmt.setString(1, mainPid);
						remoteStmt.setString(2, pidTp);
						remoteStmt.setInt(3, typeFlag);
						remoteStmt.setInt(4, mainPid.equals(pidTp) ? 1 : 0);
						remoteStmt.setDouble(5, aveWeight);
						remoteStmt.addBatch();
					}
					rs = stmtRel.executeBatch().length;
					if (rs == total) {

						rs = 0;
						rs = remoteStmt.executeBatch().length;
						if (rs == total) {
							conn28.commit();
							remoteConn.commit();
							json.setOk(true);
							if (existsUrls.size() == 0) {
								json.setMessage("全部数据插入成功");
							} else {
								json.setMessage("部分插入成功，已存在数据：" + existsUrls.toString());
							}
						} else {
							conn28.rollback();
							remoteConn.rollback();
							json.setOk(false);
							json.setMessage("插入关系表失败");
						}

					} else {
						conn28.rollback();
						json.setOk(false);
						json.setMessage("插入关系表失败");
					}
				} else {
					json.setOk(false);
					json.setMessage("插入数据失败,插入数量少于输入数量,已回滚");
					conn28.rollback();
				}
			} else {
				json.setOk(false);
				json.setMessage("获取数据失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("batchAddUrl error :" + e.getMessage());
			LOG.error("batchAddUrl error :" + e.getMessage());
			try {
				conn28.rollback();
				remoteConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (stmtQry != null) {
				try {
					stmtQry.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtSql != null) {
				try {
					stmtSql.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtRel != null) {
				try {
					stmtRel.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (remoteStmt != null) {
				try {
					remoteStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			urlList = null;
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(remoteConn);
		}
		return json;
	}

	@Override
	public JsonResult batchAddTypeUrl(String mainUrl, String urls, int adminId, int typeFlag, double aveWeight) {

		JsonResult json = new JsonResult();
		Connection conn28 = DBHelper.getInstance().getConnection5();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmtQry = null;
		PreparedStatement stmtSql = null;
		PreparedStatement stmtRel = null;
		PreparedStatement remoteStmt = null;
		ResultSet rset = null;
		String insertSql = "insert into same_1688_goods_offers(goods_pid,good_url,source_flag,"
				+ "admin_id) values(?,?,?,?) ";
		String insertRelation = "insert into same_1688_goods_relation(main_pid,low_pid,type_flag,is_main,ave_weight) "
				+ "values(?,?,?,?,?)";

		int rs = 0;
		String[] urlList = urls.split(";");
		String querySql = "select goods_pid from same_1688_goods_offers where goods_pid in(";
		try {

			conn28.setAutoCommit(false);
			remoteConn.setAutoCommit(false);
			int total = 0;
			// 预处理url到pid
			List<String> existsPids = new ArrayList<String>();
			Map<String, String> pidMap = new HashMap<String, String>();
			String endSql = "";
			// 主图部分预处理
			if (mainUrl.indexOf("?") > -1) {
				mainUrl = mainUrl.substring(0, mainUrl.indexOf("?"));
			}
			String mainPid = mainUrl.substring(mainUrl.lastIndexOf("/") + 1, mainUrl.indexOf(".html"));
			// 同款部分预处理
			for (String scUrl : urlList) {
				if (scUrl == null || "".equals(scUrl)) {
					continue;
				} else {
					if (scUrl.indexOf("?") > -1) {
						scUrl = scUrl.substring(0, scUrl.indexOf("?"));
					}
					String cldPid = scUrl.substring(scUrl.lastIndexOf("/") + 1, scUrl.indexOf(".html"));
					pidMap.put(cldPid, scUrl);
					endSql += ",'" + cldPid + "'";
				}

			}
			endSql = endSql.substring(1);

			stmtQry = conn28.prepareStatement(querySql + endSql + ")");
			rset = stmtQry.executeQuery();
			while (rset.next()) {
				existsPids.add(rset.getString(1));
			}

			List<String> existsUrls = new ArrayList<String>();
			if (!(pidMap == null || pidMap.size() == 0)) {
				stmtSql = conn28.prepareStatement(insertSql);

				// 同款部分插入数据
				for (String pid : pidMap.keySet()) {
					if (!(pid == null || "".equals(pid))) {
						if (existsPids.size() > 0) {
							if (existsPids.contains(pid)) {
								existsUrls.add(pidMap.get(pid));
								continue;
							} else {
								stmtSql.setString(1, pid);
								stmtSql.setString(2, pidMap.get(pid));
								stmtSql.setInt(3, 1);
								stmtSql.setInt(4, adminId);
								stmtSql.addBatch();
								total++;
							}
						} else {
							stmtSql.setString(1, pid);
							stmtSql.setString(2, pidMap.get(pid));
							stmtSql.setInt(3, 1);
							stmtSql.setInt(4, adminId);
							stmtSql.addBatch();
							total++;
						}
					}
				}
				rs = stmtSql.executeBatch().length;
				if (rs == total) {
					stmtRel = conn28.prepareStatement(insertRelation);
					remoteStmt = conn28.prepareStatement(insertRelation);
					rs = 0;
					pidMap.remove(mainPid);
					for (String pidTp : pidMap.keySet()) {
						stmtRel.setString(1, mainPid);
						stmtRel.setString(2, pidTp);
						stmtRel.setInt(3, typeFlag);
						stmtRel.setInt(4, 0);
						stmtRel.setDouble(5, aveWeight);
						stmtRel.addBatch();
						remoteStmt.setString(1, mainPid);
						remoteStmt.setString(2, pidTp);
						remoteStmt.setInt(3, typeFlag);
						remoteStmt.setInt(4, 0);
						remoteStmt.setDouble(5, aveWeight);
						remoteStmt.addBatch();
					}
					rs = stmtRel.executeBatch().length;
					if (rs == total) {
						rs = 0;
						rs = remoteStmt.executeBatch().length;
						if (rs == total) {
							conn28.commit();
							remoteConn.commit();
							json.setOk(true);
							if (existsUrls.size() == 0) {
								json.setMessage("全部数据插入成功");
							} else {
								json.setMessage("部分插入成功，已存在数据：" + existsUrls.toString());
							}
						} else {
							conn28.rollback();
							remoteConn.rollback();
							json.setOk(false);
							json.setMessage("插入关系表失败,已回滚");
						}
					} else {
						conn28.rollback();
						json.setOk(false);
						json.setMessage("插入关系表失败,已回滚");
					}
				} else {
					json.setOk(false);
					json.setMessage("插入数据失败,已回滚");
					conn28.rollback();
				}
			} else {
				json.setOk(false);
				json.setMessage("获取数据失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("batchAddUrl error :" + e.getMessage());
			LOG.error("batchAddUrl error :" + e.getMessage());
			try {
				conn28.rollback();
				remoteConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (stmtSql != null) {
				try {
					stmtSql.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtRel != null) {
				try {
					stmtRel.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtRel != null) {
				try {
					stmtRel.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (remoteStmt != null) {
				try {
					remoteStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			urlList = null;
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(remoteConn);
		}
		return json;
	}

	@Override
	public List<CustomOnlineGoodsBean> queryDealGoods() {

		List<CustomOnlineGoodsBean> goodList = new ArrayList<CustomOnlineGoodsBean>();
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String querySql = "select id,pid,custom_main_image,entype,img,eninfo,img_down_flag"
				+ " from same_goods_ready where sync_flag = 0";
		PreparedStatement stmt = null;
		ResultSet rss = null;
		try {
			stmt = conn28.prepareStatement(querySql);
			rss = stmt.executeQuery();
			while (rss.next()) {
				CustomOnlineGoodsBean good = new CustomOnlineGoodsBean();
				good.setId(rss.getInt("id"));
				good.setPid(rss.getString("pid"));
				good.setCustomMainImage(rss.getString("custom_main_image"));
				good.setEntype(rss.getString("entype"));
				good.setImg(rss.getString("img"));
				good.setEninfo(rss.getString("eninfo"));
				good.setImgDownFlag(rss.getInt("img_down_flag"));
				goodList.add(good);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryDealGoods error :" + e.getMessage());
			LOG.error("queryDealGoods error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}

		return goodList;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean syncSingleGoodsToOnline(CustomOnlineGoodsBean goods) {

		throw new RuntimeException("already cancel method called");

	}

	@Override
	public boolean updateSameTypeGoodsError(String pid) {

		int rs = 0;
		Connection connOnline = DBHelper.getInstance().getConnection2();
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String updateSql = "update same_1688_goods_relation set del_flag =1 where low_pid = ?";
		PreparedStatement stmtOnline = null;
		PreparedStatement stmt28Up = null;
		try {

			connOnline.setAutoCommit(false);
			conn28.setAutoCommit(false);
			stmtOnline = connOnline.prepareStatement(updateSql);
			stmtOnline.setString(1, pid);
			stmt28Up = conn28.prepareStatement(updateSql);
			stmt28Up.setString(1, pid);
			rs = stmtOnline.executeUpdate();
			if (rs > 0) {
				rs = 0;
				rs = stmt28Up.executeUpdate();
				if (rs > 0) {
					connOnline.commit();
					conn28.commit();
				} else {
					conn28.rollback();
				}
			} else {
				connOnline.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("pid:" + pid + " updateSameTypeGoodsSuccess error :" + e.getMessage());
			LOG.error("pid:" + pid + " updateSameTypeGoodsSuccess error :" + e.getMessage());
		} finally {
			if (stmtOnline != null) {
				try {
					stmtOnline.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt28Up != null) {
				try {
					stmt28Up.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(connOnline);
			DBHelper.getInstance().closeConnection(conn28);
		}
		return rs > 0;
	}

	private void setSingleParam(PreparedStatement stmt, CustomOnlineGoodsBean goods, boolean no27) {

		try {
			int i = 1;
			stmt.setString(i++, goods.getAliFreight() == null ? "" : goods.getAliFreight());
			stmt.setString(i++, goods.getAliImg() == null ? "" : goods.getAliImg());
			stmt.setString(i++, goods.getAliMorder() == null ? "0" : goods.getAliMorder());
			if (no27) {
				stmt.setString(i++, goods.getAliName() == null ? "" : goods.getAliName());
			}
			stmt.setString(i++, goods.getAliPid() == null ? "" : goods.getAliPid());
			stmt.setString(i++, goods.getAliPrice() == null ? "" : goods.getAliPrice());

			stmt.setString(i++, goods.getAliSellunit() == null ? "" : goods.getAliSellunit());
			stmt.setInt(i++, goods.getAliSold());
			stmt.setString(i++, goods.getAliUnit() == null ? "" : goods.getAliUnit());
			stmt.setString(i++, goods.getAliWeight() == null ? "" : goods.getAliWeight());
			stmt.setInt(i++, goods.getBmFlag());

			stmt.setString(i++, goods.getCatid() == null ? "" : goods.getCatid());
			stmt.setString(i++, goods.getCatid1() == null ? "" : goods.getCatid1());
			stmt.setString(i++, goods.getCatidb() == null ? "" : goods.getCatidb());
			stmt.setString(i++, goods.getCatidParenta() == null ? "" : goods.getCatidParenta());
			stmt.setString(i++, goods.getCatidParentb() == null ? "" : goods.getCatidParentb());

			stmt.setString(i++, goods.getCatpath() == null ? "" : goods.getCatpath());
			stmt.setString(i++, goods.getCustomMainImage() == null ? "" : goods.getCustomMainImage());
			stmt.setString(i++, goods.getEndetail() == null ? "" : goods.getEndetail());
			stmt.setString(i++, goods.getEninfo() == null ? "" : goods.getEninfo());

			stmt.setString(i++, goods.getEnname() == null ? "" : goods.getEnname());

			stmt.setString(i++, goods.getEntype() == null ? "" : goods.getEntype());
			stmt.setString(i++, goods.getFeeprice() == null ? "" : goods.getFeeprice());
			if (no27) {
				stmt.setString(i++, goods.getFinalName() == null ? "" : goods.getFinalName());
			}
			stmt.setString(i++, goods.getFinalWeight() == null ? "" : goods.getFinalWeight());
			stmt.setString(i++, goods.getFprice() == null ? "" : goods.getFprice());
			if (no27) {
				stmt.setString(i++, goods.getFpriceStr() == null ? "" : goods.getFpriceStr());
			}
			stmt.setString(i++, goods.getImg() == null ? "" : goods.getImg());
			stmt.setInt(i++, goods.getImgCheck());
			if (no27) {
				stmt.setInt(i++, goods.getInfoReviseFlag());
			}
			stmt.setInt(i++, goods.getIsAddCarFlag());
			stmt.setInt(i++, goods.getIsBenchmark());
			stmt.setInt(i++, goods.getIsNewCloud());
			if (no27) {
				stmt.setInt(i++, goods.getIsShowDetImgFlag());
				stmt.setInt(i++, goods.getIsShowDetTableFlag());
			}
			stmt.setInt(i++, goods.getIsSoldFlag());

			stmt.setString(i++, goods.getKeyword() == null ? "" : goods.getKeyword());
			stmt.setString(i++, goods.getLocalPath() == null ? "" : goods.getLocalPath());
			stmt.setInt(i++, goods.getMorder());
			if (no27) {
				stmt.setString(i++, goods.getName() == null ? "" : goods.getName());
			}

			stmt.setInt(i++, goods.getOcrMatchFlag());
			stmt.setString(i++, goods.getOriginalCatid() == null ? "" : goods.getOriginalCatid());

			stmt.setString(i++, goods.getOriginalCatpath() == null ? "" : goods.getOriginalCatpath());
			stmt.setString(i++, goods.getPid() == null ? "" : goods.getPid());
			stmt.setDouble(i++, goods.getPrice());
			if (no27) {
				stmt.setInt(i++, goods.getPriceReviseFlag());
			}
			stmt.setInt(i++, goods.getPriorityFlag());
			if (no27) {
				stmt.setString(i++, goods.getPvids() == null ? "" : goods.getPvids());
			}
			stmt.setString(i++, goods.getRangePrice() == null ? "" : goods.getRangePrice());
			stmt.setString(i++, goods.getRemotPath() == null ? "" : goods.getRemotPath());
			stmt.setString(i++, goods.getReviseWeight() == null ? "" : goods.getReviseWeight());
			if (no27) {
				stmt.setString(i++, goods.getSellUnit() == null ? "" : goods.getSellUnit());
			}
			stmt.setString(i++, goods.getShopId() == null ? "" : goods.getShopId());
			stmt.setString(i++, goods.getSku() == null ? "" : goods.getSku());
			stmt.setInt(i++, goods.getSold());
			stmt.setInt(i++, goods.getSourceProFlag());
			stmt.setInt(i++, goods.getSourceUsedFlag());
			stmt.setInt(i++, goods.getValid());
			stmt.setString(i++, goods.getWeight() == null ? "" : goods.getWeight());
			if (no27) {
				stmt.setString(i++, goods.getWholesalePrice() == null ? "" : goods.getWholesalePrice());
			}

			stmt.setString(i++, goods.getWprice() == null ? "" : goods.getWprice());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("setSingleParam error :" + e.getMessage());
			LOG.error("setSingleParam error :" + e.getMessage());
		}

	}

	@Override
	public boolean updateGoodsImg(CustomOnlineGoodsBean goods) {

		Connection conn28 = DBHelper.getInstance().getConnection5();
		String updateSql = "update same_goods_ready set img_down_flag = ?,custom_main_image = ?,"
				+ "entype = ?,img = ?,eninfo = ?,remotpath = ? where id = ? and pid = ?";
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			stmt = conn28.prepareStatement(updateSql);
			stmt.setInt(1, 1);
			stmt.setString(2, goods.getCustomMainImage());
			stmt.setString(3, goods.getEntype());
			stmt.setString(4, goods.getImg());

			stmt.setString(5, goods.getEninfo());
			stmt.setString(6, goods.getRemotPath());
			stmt.setInt(7, goods.getId());
			stmt.setString(8, goods.getPid());

			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(
					"id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImg error :" + e.getMessage());
			LOG.error("id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImg error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}

		return rs > 0;
	}

	@Override
	public boolean updateGoodsImgError(CustomOnlineGoodsBean goods) {

		Connection conn28 = DBHelper.getInstance().getConnection5();
		String updateSql = "update same_goods_ready set img_down_flag = ? where id = ? and pid = ?";
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			stmt = conn28.prepareStatement(updateSql);
			stmt.setInt(1, 2);
			stmt.setInt(2, goods.getId());
			stmt.setString(3, goods.getPid());

			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(
					"id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImgError error :" + e.getMessage());
			LOG.error(
					"id:" + goods.getId() + ",pid:" + goods.getPid() + " updateGoodsImgError error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}

		return rs > 0;
	}

	@Override
	public CustomOnlineGoodsBean queryGoodsByPid(String pid) {
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String querySql = "select * from same_goods_ready where pid = ?";
		PreparedStatement stmt = null;
		ResultSet rss = null;
		CustomOnlineGoodsBean goods = new CustomOnlineGoodsBean();
		try {
			stmt = conn28.prepareStatement(querySql);
			stmt.setString(1, pid);
			rss = stmt.executeQuery();
			if (rss.next()) {

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
				// goods.setFlag(rss.getInt("flag"));
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
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("pid:" + pid + " queryGoodsByPid error :" + e.getMessage());
			LOG.error("pid:" + pid + " queryGoodsByPid error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}
		return goods;
	}

	@Override
	public List<SameTypeGoodsBean> queryForList(int type, int adminId, int start, int limitNum) {
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String querySql = "select sgp.main_pid as goods_pid,sgp.total_num,sgp.type_flag,sgp.is_on,sgp.ave_weight,"
				+ "ifnull(sgo.create_time,'') as create_time,ifnull(sgo.source_flag,0) as source_flag,"
				+ "ifnull(sgo.good_url,'') as good_url,ifnull(sgo.admin_id,0) as admin_id,"
				+ "(select count(same_goods_ready.pid) from same_goods_ready,same_1688_goods_relation  "
				+ "where same_1688_goods_relation.main_pid = sgp.main_pid and "
				+ " same_goods_ready.pid = same_1688_goods_relation.low_pid and same_goods_ready.sync_flag =1 "
				+ "and same_1688_goods_relation.del_flag =0 and same_1688_goods_relation.is_main =0)"
				+ " as success_num,ifnull(sgr.remotpath,'') as remotpath,"
				+ "ifnull(sgr.enname,'') as goods_name,ifnull(sgr.custom_main_image,'') as goods_img,"
				+ "ifnull(sgr.price,0) as goods_price,ifnull(sgr.morder,0) as min_order_num,"
				+ "ifnull(sgo.crawl_flag,0) as crawl_flag,ifnull(sgr.valid,0) as valid,"
				+ "ifnull(sgr.img_down_flag,0) as img_down_flag,ifnull(sgo.clear_flag,0) as clear_flag,ifnull(sgr.sync_flag,0) as sync_flag,"
				+ "ifnull(sgr.sync_remark,'') as sync_remark from (select main_pid,type_flag,is_on,ave_weight,"
				+ "count(low_pid) as total_num from same_1688_goods_relation where del_flag = 0"
				+ " GROUP BY main_pid) sgp left join same_1688_goods_offers sgo on sgp.main_pid =sgo.goods_pid "
				+ " left join same_goods_ready sgr on sgr.pid = sgp.main_pid where 1=1 ";
		if (type > 0) {
			querySql += " and sgp.type_flag = ?";
		}
		if (adminId > 0) {
			querySql += " and sgo.admin_id = ?";
		}
		querySql += " order by sgo.create_time desc limit ?,?";
		PreparedStatement stmt = null;
		ResultSet rss = null;
		List<SameTypeGoodsBean> list = new ArrayList<SameTypeGoodsBean>();
		try {
			int count = 1;
			stmt = conn28.prepareStatement(querySql);
			if (type > 0) {
				stmt.setInt(count++, type);
			}
			if (adminId > 0) {
				stmt.setInt(count++, adminId);
			}
			stmt.setInt(count++, start);
			stmt.setInt(count++, limitNum);
			rss = stmt.executeQuery();
			while (rss.next()) {

				SameTypeGoodsBean goods = new SameTypeGoodsBean();
				String pid = rss.getString("goods_pid");
				goods.setGoodsPid(pid);
				goods.setGoodsName(rss.getString("goods_name"));
				String remotpath = rss.getString("remotpath");
				String goodsImg = rss.getString("goods_img");
				if (goodsImg.indexOf("https:") > -1 || goodsImg.indexOf("http:") > -1) {
					goods.setGoodsImg(goodsImg);
				} else {
					goods.setGoodsImg(remotpath + goodsImg);
				}
				goods.setGoodUrl(rss.getString("good_url"));
				goods.setGoodsPrice(rss.getDouble("goods_price"));
				String expressUrl = "&source=D" + Md5Util.encoder(pid) + "&item=" + pid;
				goods.setExpressUrl("https://www.import-express.com/spider/detail?" + expressUrl);
				goods.setMinOrderNum(rss.getInt("min_order_num"));
				goods.setAdminId(rss.getInt("admin_id"));
				int totalNum = rss.getInt("total_num");
				if (totalNum > 0) {
					goods.setTotalNum(totalNum - 1);
				} else {
					goods.setTotalNum(0);
				}
				goods.setSuccessNum(rss.getInt("success_num"));
				goods.setCreateTime(rss.getString("create_time"));
				goods.setSourceFlag(rss.getInt("source_flag"));
				goods.setTypeFlag(rss.getInt("type_flag"));
				goods.setCrawlFlag(rss.getInt("crawl_flag"));
				goods.setClearFlag(rss.getInt("clear_flag"));
				goods.setValid(rss.getInt("valid"));
				goods.setImgDownFlag(rss.getInt("img_down_flag"));
				goods.setSyncFlag(rss.getInt("sync_flag"));
				goods.setSyncRemark(rss.getString("sync_remark"));
				goods.setIsOn(rss.getInt("is_on"));
				goods.setAveWeight(rss.getDouble("ave_weight"));
				list.add(goods);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryForList error :" + e.getMessage());
			LOG.error("queryForList error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}
		return list;
	}

	@Override
	public int queryForListCount(int type, int adminId) {
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String querySql = "select count(sgp.main_pid) from "
				+ " (select main_pid,type_flag,count(low_pid) as total_num from same_1688_goods_relation"
				+ "  where del_flag = 0 group by main_pid) sgp"
				+ " left join same_1688_goods_offers sgo on sgp.main_pid =sgo.goods_pid where 1=1 ";
		if (type > 0) {
			querySql += " and sgp.type_flag = ?";
		}
		if (adminId > 0) {
			querySql += " and sgo.admin_id = ?";
		}
		PreparedStatement stmt = null;
		ResultSet rss = null;
		int count = 0;
		try {
			stmt = conn28.prepareStatement(querySql);
			if (type > 0) {
				stmt.setInt(1, type);
			}
			if (adminId > 0) {
				stmt.setInt(2, adminId);
			}
			rss = stmt.executeQuery();
			if (rss.next()) {
				count = rss.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryForListCount error :" + e.getMessage());
			LOG.error("queryForListCount error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}
		return count;
	}

	@Override
	public List<SameTypeGoodsBean> queryListByMainPid(String mainPid) {
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String querySql = "select sgp.low_pid as goods_pid,sgp.del_flag,ifnull(sgo.create_time,'') as create_time,"
				+ "ifnull(sgo.source_flag,0) as source_flag,ifnull(sgo.good_url,'') as good_url,"
				+ "ifnull(sgo.admin_id,0) as admin_id,"
				+ "ifnull(sgr.enname,'') as goods_name,ifnull(sgr.custom_main_image,'') as goods_img,"
				+ "ifnull(sgr.price,0) as goods_price,ifnull(sgr.morder,0) as min_order_num,"
				+ "ifnull(sgo.crawl_flag,0) as crawl_flag,ifnull(sgr.valid,0) as valid,"
				+ "ifnull(sgr.img_down_flag,0) as img_down_flag,ifnull(sgo.clear_flag,0) as clear_flag,ifnull(sgr.sync_flag,0) as sync_flag,"
				+ "ifnull(sgr.sync_remark,'') as sync_remark,ifnull(sgr.remotpath,'') as remotpath "
				+ " from (select low_pid,del_flag from same_1688_goods_relation"
				+ " where main_pid =?) sgp left join same_1688_goods_offers sgo on sgp.low_pid =sgo.goods_pid "
				+ " left join same_goods_ready sgr on sgr.pid = sgp.low_pid ";
		PreparedStatement stmt = null;
		ResultSet rss = null;
		List<SameTypeGoodsBean> list = new ArrayList<SameTypeGoodsBean>();
		try {
			stmt = conn28.prepareStatement(querySql);
			stmt.setString(1, mainPid);
			rss = stmt.executeQuery();
			while (rss.next()) {

				SameTypeGoodsBean goods = new SameTypeGoodsBean();
				String pid = rss.getString("goods_pid");
				goods.setGoodsPid(pid);
				goods.setGoodsName(rss.getString("goods_name"));
				String remotpath = rss.getString("remotpath");
				String goodsImg = rss.getString("goods_img");
				if (goodsImg.indexOf("https:") > -1 || goodsImg.indexOf("http:") > -1) {
					goods.setGoodsImg(goodsImg);
				} else {
					goods.setGoodsImg(remotpath + goodsImg);
				}

				goods.setGoodUrl(rss.getString("good_url"));
				goods.setGoodsPrice(rss.getDouble("goods_price"));
				String expressUrl = "&source=D" + Md5Util.encoder(pid) + "&item=" + pid;
				goods.setExpressUrl("https://www.import-express.com/spider/detail?" + expressUrl);
				goods.setMinOrderNum(rss.getInt("min_order_num"));
				goods.setAdminId(rss.getInt("admin_id"));
				goods.setCreateTime(rss.getString("create_time"));
				goods.setSourceFlag(rss.getInt("source_flag"));
				goods.setCrawlFlag(rss.getInt("crawl_flag"));
				goods.setClearFlag(rss.getInt("clear_flag"));
				goods.setValid(rss.getInt("valid"));
				goods.setImgDownFlag(rss.getInt("img_down_flag"));
				goods.setSyncFlag(rss.getInt("sync_flag"));
				goods.setSyncRemark(rss.getString("sync_remark"));
				goods.setDelFlag(rss.getInt("del_flag"));
				list.add(goods);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("mainPid:" + mainPid + " queryListByMainPid error :" + e.getMessage());
			LOG.error("mainPid:" + mainPid + " queryListByMainPid error :" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}
		return list;
	}

	@Override
	public List<Admuser> queryAllAdmin() {
		List<Admuser> list = new ArrayList<Admuser>();
		String sql = "SELECT id,admName from admuser where status=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Admuser adm = new Admuser();
				adm.setId(rs.getInt("id"));
				adm.setAdmname(rs.getString("admName"));
				list.add(adm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public boolean deleteGoodsByMainPid(String mainPid) {

		// 非物理删除数据
		Connection conn28 = DBHelper.getInstance().getConnection5();
		Connection connOnline = DBHelper.getInstance().getConnection2();
		String delete28 = "update same_1688_goods_relation set del_flag =1,update_time=sysdate()"
				+ " where main_pid = '" + mainPid + "'";

		Statement stmt28 = null;
		Statement stmtOnline = null;
		int rs28 = 0;
		try {
			conn28.setAutoCommit(false);
			connOnline.setAutoCommit(false);
			stmt28 = conn28.createStatement();
			rs28 = stmt28.executeUpdate(delete28);
			if (rs28 > 0) {
				rs28 = 0;
				stmtOnline = connOnline.createStatement();
				rs28 = stmtOnline.executeUpdate(delete28);
				if (rs28 > 0) {
					conn28.commit();
					connOnline.commit();
				} else {
					conn28.rollback();
					connOnline.rollback();
				}
			} else {
				conn28.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("mainPid:" + mainPid + ",deleteGoodsByMainPid error :" + e.getMessage());
			LOG.error("mainPid:" + mainPid + ",deleteGoodsByMainPid error :" + e.getMessage());
		} finally {
			if (stmt28 != null) {
				try {
					stmt28.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtOnline != null) {
				try {
					stmtOnline.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(connOnline);
		}
		return rs28 > 0;
	}

	@Override
	public boolean deleteGoodsByPid(String mainPid, String pid) {

		// 非物理删除数据
		Connection conn28 = DBHelper.getInstance().getConnection5();
		Connection connOnline = DBHelper.getInstance().getConnection2();
		String delete28 = "update same_1688_goods_relation set del_flag =1,update_time=sysdate()" + " where low_pid = '"
				+ pid + "' and main_pid = '" + mainPid + "'";

		Statement stmt28 = null;
		Statement stmtOnline = null;
		int rs28 = 0;
		try {
			conn28.setAutoCommit(false);
			connOnline.setAutoCommit(false);
			stmt28 = conn28.createStatement();
			rs28 = stmt28.executeUpdate(delete28);
			if (rs28 > 0) {
				rs28 = 0;
				stmtOnline = connOnline.createStatement();
				rs28 = stmtOnline.executeUpdate(delete28);
				if (rs28 > 0) {
					conn28.commit();
					connOnline.commit();
				} else {
					conn28.rollback();
					connOnline.rollback();
				}
			} else {
				conn28.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("mainPid:" + mainPid + ",pid:" + pid + ",deleteGoodsByPid error :" + e.getMessage());
			LOG.error("mainPid:" + mainPid + ",pid:" + pid + ",deleteGoodsByPid error :" + e.getMessage());
		} finally {
			if (stmt28 != null) {
				try {
					stmt28.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtOnline != null) {
				try {
					stmtOnline.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(connOnline);
		}
		return rs28 > 0;
	}

	@Override
	public List<Integer> queryNoDealGoods() {

		Connection conn28 = DBHelper.getInstance().getConnection5();
		String selectSql = "select id from same_1688_goods_offers where crawl_flag > 1" + " and dl_flag =0 order by id";

		List<Integer> ids = new ArrayList<Integer>();
		Statement stmt28 = null;
		ResultSet rs = null;
		try {
			stmt28 = conn28.createStatement();
			rs = stmt28.executeQuery(selectSql);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryNoDealGoods error :" + e.getMessage());
			LOG.error("deleteGoodsByPid error :" + e.getMessage());
		} finally {
			if (stmt28 != null) {
				try {
					stmt28.close();
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
			DBHelper.getInstance().closeConnection(conn28);
		}
		return ids;
	}

	@Override
	public int batchUpdateDlFlag(List<Integer> ids) {

		Connection conn28 = DBHelper.getInstance().getConnection5();
		String updateSql = "update same_1688_goods_offers set dl_flag =1 where id = ?";

		PreparedStatement stmt28 = null;
		int rs = 0;
		try {
			conn28.setAutoCommit(false);
			stmt28 = conn28.prepareStatement(updateSql);
			for (int tempId : ids) {
				stmt28.setInt(1, tempId);
				stmt28.addBatch();
			}
			rs = stmt28.executeBatch().length;
			if (rs > 0) {
				conn28.commit();
			} else {
				conn28.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("batchUpdateDlFlag error :" + e.getMessage());
			LOG.error("batchUpdateDlFlag error :" + e.getMessage());
		} finally {
			if (stmt28 != null) {
				try {
					stmt28.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
		}
		return rs;
	}

	@Override
	public boolean replaceGoodsMainPid(String newPid, String oldPid) {

		Connection conn28 = DBHelper.getInstance().getConnection5();
		Connection remotrConn = DBHelper.getInstance().getConnection2();
		String updateLowSql = "update same_1688_goods_relation set  main_pid = '" + newPid + "',low_pid = '" + newPid
				+ "' where is_main =1 and low_pid = '" + oldPid + "' and main_pid = '" + oldPid + "'";
		String updateMainSql = "update same_1688_goods_relation set main_pid = '" + newPid
				+ "' where is_main =0 and main_pid = '" + oldPid + "'";

		Statement stmt28 = null;
		Statement remoteStmt = null;
		int rs = 0;
		try {

			conn28.setAutoCommit(false);
			remotrConn.setAutoCommit(false);
			stmt28 = conn28.createStatement();
			stmt28.addBatch(updateLowSql);
			stmt28.addBatch(updateMainSql);
			remoteStmt = remotrConn.createStatement();
			remoteStmt.addBatch(updateLowSql);
			remoteStmt.addBatch(updateMainSql);

			rs = stmt28.executeBatch().length;
			if (rs == 2) {
				rs = 0;
				rs = remoteStmt.executeBatch().length;
				if (rs == 2) {
					remotrConn.commit();
					conn28.commit();
				} else {
					remotrConn.rollback();
					conn28.rollback();
				}
			} else {
				conn28.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(
					"newPid:" + newPid + ",oldPid:" + oldPid + " replaceGoodsMainPid error :" + e.getMessage());
			LOG.error("newPid:" + newPid + ",oldPid:" + oldPid + " replaceGoodsMainPid error :" + e.getMessage());
			try {
				conn28.rollback();
				remotrConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (stmt28 != null) {
				try {
					stmt28.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (remoteStmt != null) {
				try {
					remoteStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(remotrConn);
		}
		return rs == 2;
	}

	@Override
	public boolean useGoodsByState(int state, String pids) {

		Connection conn28 = DBHelper.getInstance().getConnection5();
		Connection remotrConn = DBHelper.getInstance().getConnection2();
		String update28Sql = "update same_1688_goods_relation set  is_on = " + state + " where main_pid in(";

		Statement stmt28 = null;
		Statement remoteStmt = null;
		int rs = 0;
		String[] pidList = null;
		if (pids == null || "".equals(pids)) {
			System.err.println("PIDS获取失败!");
			return false;
		} else {
			pidList = pids.split(",");
			String tempPids = "";
			for (String pid : pidList) {
				if (pid == null || "".equals(pid)) {
					continue;
				} else {
					tempPids += ",'" + pid + "'";
				}
			}
			if (tempPids.length() == 0) {
				System.err.println("PIDS解析后无PID数据");
				return false;
			} else {
				update28Sql += tempPids.substring(1) + ")";
			}
		}
		try {

			System.err.println("update28Sql:" + update28Sql);
			conn28.setAutoCommit(false);
			remotrConn.setAutoCommit(false);
			stmt28 = conn28.createStatement();
			remoteStmt = remotrConn.createStatement();

			rs = stmt28.executeUpdate(update28Sql);
			if (rs > 0) {
				rs = 0;
				rs = remoteStmt.executeUpdate(update28Sql);
				if (rs > 0) {
					remotrConn.commit();
					conn28.commit();
				} else {
					remotrConn.rollback();
					conn28.rollback();
				}
			} else {
				conn28.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("state:" + state + " useGoodsByState error :" + e.getMessage());
			LOG.error("state:" + state + " useGoodsByState error :" + e.getMessage());
			try {
				conn28.rollback();
				remotrConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (stmt28 != null) {
				try {
					stmt28.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (remoteStmt != null) {
				try {
					remoteStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(remotrConn);
		}
		return rs > 0;
	}

}
