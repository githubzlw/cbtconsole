package com.cbt.dao.impl;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.dao.SyncPidToOnlineDao;
import com.cbt.jdbc.DBHelper;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SyncPidToOnlineDaoImpl implements SyncPidToOnlineDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SyncPidToOnlineDaoImpl.class);

	@Override
	public CustomOnlineGoodsBean queryGoodsInfoByPid(String goodsPid, String dealTable) {

		CustomOnlineGoodsBean good = new CustomOnlineGoodsBean();
		Connection conn28 = DBHelper.getInstance().getConnection8();
		String querySql = "select id,pid,custom_main_image,entype,img,eninfo,img_down_flag,localpath"
				+ " from ? where pid = ? ";
		PreparedStatement stmt = null;
		ResultSet rss = null;
		try {
			stmt = conn28.prepareStatement(querySql);
			stmt.setString(1, dealTable);
			stmt.setString(2, goodsPid);
			rss = stmt.executeQuery();
			if (rss.next()) {
				good.setId(rss.getInt("id"));
				good.setPid(rss.getString("pid"));
				good.setCustomMainImage(rss.getString("custom_main_image"));
				good.setEntype(rss.getString("entype"));
				good.setImg(rss.getString("img"));
				good.setEninfo(rss.getString("eninfo"));
				good.setImgDownFlag(rss.getInt("img_down_flag"));
				good.setLocalPath(rss.getString("localpath"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("dealTable:" + dealTable + ",goodsPid:" + goodsPid + ",queryGoodsInfoByPid error :"
					+ e.getMessage());
			LOG.error("dealTable:" + dealTable + ",goodsPid:" + goodsPid + ",queryGoodsInfoByPid error :"
					+ e.getMessage());
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
		return good;
	}

	@Override
	public String queryGoodsLocalPath(String crawlTable, String goodsPid) {
		String localPath = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select loc_mainpath from ? where goods_pid = ?";
		try {
			conn = DBHelper.getInstance().getConnection5();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, crawlTable);
			stmt.setString(2, goodsPid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				localPath = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryGoodsLocalPath error: " + e.getMessage());
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
		if (localPath == null) {
			localPath = "";
		}
		return localPath;
	}

	@Override
	public CustomOnlineGoodsBean queryGoodsByTableAndPid(String dealTable, String goodsPid) {
		Connection conn28 = DBHelper.getInstance().getConnection8();
		String querySql = "select * from ? where  pid = ?";
		PreparedStatement stmt = null;
		ResultSet rss = null;
		CustomOnlineGoodsBean goods = new CustomOnlineGoodsBean();
		try {
			stmt = conn28.prepareStatement(querySql);
			stmt.setString(1, dealTable);
			stmt.setString(2, goodsPid);
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
			System.err.println("dealTable:" + dealTable + ",goodsPid:" + goodsPid + ",queryGoodsByShopIdAndPid error :"
					+ e.getMessage());
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

	@SuppressWarnings("resource")
	@Override
	public boolean syncSingleGoodsToOnline(CustomOnlineGoodsBean goods, String dealTable) {

		int rs = 0;
		Connection connOnline = DBHelper.getInstance().getConnection2();
		Connection conn27 = DBHelper.getInstance().getConnection();
		Connection conn28 = DBHelper.getInstance().getConnection8();
		String querySql = "select count(1) from custom_benchmark_ready where pid = ?";
		String insertSql = "(ali_freight,ali_img,ali_morder,ali_name,ali_pid,"
				+ "ali_price,ali_sellunit,ali_sold,ali_unit,ali_weight,bm_flag,catid,catid1,catidb,catidparenta,"
				+ "catidparentb,catpath,custom_main_image,endetail,eninfo,"
				+ "enname,entype,feeprice,finalName,final_weight,fprice,fprice_str,img,img_check,infoReviseFlag,"
				+ "is_add_car_flag,isBenchmark,isNewCloud,is_show_det_img_flag,is_show_det_table_flag,"
				+ "is_sold_flag,keyword,localpath,morder,name,"
				+ "ocr_match_flag,originalcatid,originalcatpath,pid,price,"
				+ "priceReviseFlag,priority_flag,pvids,range_price,remotpath,"
				+ "revise_weight,sellunit,shop_id,sku,sold,"
				+ "source_pro_flag,source_used_flag,valid,weight,wholesale_price,wprice) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String insertSql27 = "insert into custom_benchmark_ready(ali_freight,ali_img,ali_morder,ali_pid,ali_price,"
				+ "ali_sellunit,ali_sold,ali_unit,ali_weight,bm_flag,"
				+ "catid,catid1,catidb,catidparenta,catidparentb,catpath,custom_main_image,endetail,eninfo,enname,"
				+ "entype,feeprice,final_weight,fprice,img,img_check,"
				+ "is_add_car_flag,isBenchmark,isNewCloud,is_sold_flag,keyword,"
				+ "localpath,morder,ocr_match_flag,originalcatid,originalcatpath,"
				+ "pid,price,priority_flag,range_price,remotpath,revise_weight,shop_id,sku,sold,source_pro_flag,"
				+ "source_used_flag,valid,weight,wprice) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String updateSql = "update ? set sync_flag =?,sync_remark=? where pid = ?";
		PreparedStatement stmtOnline = null;
		PreparedStatement stmt27 = null;
		PreparedStatement stmt28Up = null;
		PreparedStatement stmt28 = null;
		ResultSet rset = null;
		try {

			stmtOnline = connOnline.prepareStatement(querySql);
			stmtOnline.setString(1, goods.getPid());
			rset = stmtOnline.executeQuery();
			int count = 0;
			if (rset.next()) {
				count = rset.getInt(1);
			}
			// 线上存在直接更新同步
			if (count > 0) {
				stmt28Up = conn28.prepareStatement(updateSql);
				stmt28Up.setString(1, dealTable);
				stmt28Up.setInt(2, 2);
				stmt28Up.setString(3, "当前 pid已经存在！");
				stmt28Up.setString(4, goods.getPid());
				stmt28Up.executeUpdate();
				System.err.println("当前 pid:" + goods.getPid() + " 已经存在！！");
				LOG.error("当前 pid:" + goods.getPid() + " 已经存在！！");
				rs = 3;
			} else {
				// 同步Online
				connOnline.setAutoCommit(false);
				stmtOnline.clearParameters();
				stmtOnline = connOnline.prepareStatement("insert into custom_benchmark_ready" + insertSql);
				setSingleParam(stmtOnline, goods, true);
				count = stmtOnline.executeUpdate();
				if (count > 0) {
					// 更新同步
					conn28.setAutoCommit(false);
					stmt28Up = conn28.prepareStatement(updateSql);
					stmt28Up.setString(1, dealTable);
					stmt28Up.setInt(2, 1);
					stmt28Up.setString(3, "success");
					stmt28Up.setString(4, goods.getPid());
					int upCount = stmt28Up.executeUpdate();
					if (upCount > 0) {
						// 同步Online成功
						connOnline.commit();
						rs++;
						conn28.commit();
						// 同步27
						conn27.setAutoCommit(false);
						count = 0;
						stmt27 = conn27.prepareStatement(insertSql27);
						setSingleParam(stmt27, goods, false);
						count = stmt27.executeUpdate();
						if (count > 0) {
							// 同步27成功
							conn27.commit();
							rs++;
							// 同步28
							count = 0;
							stmt28 = conn28.prepareStatement("insert into custom_benchmark_ready_newest" + insertSql);
							setSingleParam(stmt28, goods, true);
							count = stmt28.executeUpdate();
							if (count > 0) {
								// 同步28成功
								conn28.commit();
								rs++;
							} else {
								// 同步28失败
								conn28.rollback();
								System.err.println("publishGoods 28 failure");
								LOG.error("publishGoods 28 failure");
							}
						} else {
							// 同步27失败
							conn27.rollback();
							System.err.println("publishGoods 27 failure");
							LOG.error("publishGoods 27 error");
						}
					} else {
						connOnline.rollback();
						conn28.rollback();
						System.err.println("update sync_flag failure");
						LOG.error("update sync_flag failure");
					}
				} else {
					// 同步Online失败
					connOnline.rollback();
					System.err.println("publishGoods Online failure");
					LOG.error("publishGoods Online failure");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("pid:" + goods.getPid() + " publishGoods error :" + e.getMessage());
			LOG.error("pid:" + goods.getPid() + " publishGoods error :" + e.getMessage());
		} finally {
			if (stmtOnline != null) {
				try {
					stmtOnline.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt27 != null) {
				try {
					stmt27.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt28 != null) {
				try {
					stmt28.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rset != null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(connOnline);
			DBHelper.getInstance().closeConnection(conn27);
			DBHelper.getInstance().closeConnection(conn28);
		}
		return rs == 3;
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

}
