package com.cbt.warehouse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.warehouse.pojo.OrderSnapshot;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderSnapshotDaoImpl implements OrderSnapshotDao {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderSnapshotDaoImpl.class);

	@SuppressWarnings("resource")
	@Override
	public boolean syncOfflineToOnline() {

		Connection conn = DBHelper.getInstance().getConnection();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		ResultSet rsSet = null;
		// 获取数据SQL
		String querySql = "select  odss.*,cbr.endetail as goods_details,cbr.eninfo as goods_eninfo,cbr.remotpath,"
				+ "gdc.goodsUnit as goods_unit  "
				+ "from (select id,od_id,userid,goodsid,goodsname,car_type,car_url,car_img,orderid,dropshipid,"
				+ "yourorder,goodsprice,delivery_time,goods_class,car_urlMD5,goods_pid,"
				+ "down_img_url from order_snapshot  where 1=1 and  sync_flag=0) odss "
				+ "left join custom_benchmark_ready cbr  on odss.goods_pid = cbr.pid and cbr.goodsstate = 4 "
				+ "left join goods_car gdc on odss.goodsid = gdc.id";
		PreparedStatement stmt = null;
		PreparedStatement remoteStmt = null;
		// 插入线上SQL
		String inRemoteSql = "insert into order_snapshot(od_id,userid,goodsid,goodsname,car_type,car_url,"
				+ "car_img,orderid,dropshipid,yourorder,goodsprice,delivery_time,"
				+ "goods_class,car_urlMD5,goods_pid,goods_details,goods_eninfo,remotpath,goods_unit) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// 更新本地flag SQL
		String upSql = "update order_snapshot set sync_flag=1 where id=?";
		boolean rs = false;
		try {
			stmt = conn.prepareStatement(querySql);

			List<OrderSnapshot> orderSnapshots = new ArrayList<OrderSnapshot>();
			rsSet = stmt.executeQuery();
			while (rsSet.next()) {
				OrderSnapshot odsh = new OrderSnapshot();
				odsh.setId(rsSet.getInt("id"));
				odsh.setOdId(rsSet.getInt("od_id"));
				odsh.setUserid(rsSet.getInt("userid"));
				odsh.setGoodsid(rsSet.getInt("goodsid"));
				odsh.setGoodsname(rsSet.getString("goodsname"));
				odsh.setGoods_type(rsSet.getString("car_type"));
				odsh.setGoods_url(rsSet.getString("car_url"));
				String down_img_url = rsSet.getString("down_img_url");
				if (down_img_url == null || "".equals(down_img_url)) {
					odsh.setGoods_img(rsSet.getString("car_img"));
				} else {
					odsh.setGoods_img(down_img_url);
				}
				odsh.setOrderid(rsSet.getString("orderid"));
				odsh.setDropshipid(rsSet.getString("dropshipid"));
				odsh.setYourorder(rsSet.getInt("yourorder"));
				odsh.setGoodsprice(rsSet.getString("goodsprice"));
				odsh.setDelivery_time(rsSet.getString("delivery_time"));
				odsh.setGoods_class(rsSet.getInt("goods_class"));
				odsh.setCar_urlMD5(rsSet.getString("car_urlMD5"));
				odsh.setGoods_pid(rsSet.getString("goods_pid"));
				odsh.setGoods_details(rsSet.getString("goods_details"));
				odsh.setGoods_eninfo(rsSet.getString("goods_eninfo"));
				odsh.setRemotpath(rsSet.getString("remotpath"));
				odsh.setGoodsUnit(rsSet.getString("goods_unit"));
				orderSnapshots.add(odsh);
			}

			if (orderSnapshots.size() > 0) {
				System.out.println("--syncOfflineToOnline orderSnapshots size:" + orderSnapshots.size());
				remoteStmt = remoteConn.prepareStatement(inRemoteSql);
				stmt = conn.prepareStatement(upSql);
				for (OrderSnapshot odspst : orderSnapshots) {
					remoteStmt.clearParameters();
					int k = 1;
					remoteStmt.setInt(k++, odspst.getOdId());
					remoteStmt.setInt(k++, odspst.getUserid());
					remoteStmt.setInt(k++, odspst.getGoodsid());
					remoteStmt.setString(k++, odspst.getGoodsname());
					remoteStmt.setString(k++, odspst.getGoods_type());
					remoteStmt.setString(k++, odspst.getGoods_url());
					remoteStmt.setString(k++, odspst.getGoods_img());
					remoteStmt.setString(k++, odspst.getOrderid());
					remoteStmt.setString(k++, odspst.getDropshipid());
					remoteStmt.setInt(k++, odspst.getYourorder());
					remoteStmt.setString(k++, odspst.getGoodsprice());
					remoteStmt.setString(k++, odspst.getDelivery_time());
					remoteStmt.setInt(k++, odspst.getGoods_class());
					remoteStmt.setString(k++, odspst.getCar_urlMD5());
					remoteStmt.setString(k++, odspst.getGoods_pid());
					remoteStmt.setString(k++, odspst.getGoods_details() == null ? "" : odspst.getGoods_details());

					// eninfo（详情图片）的处理，替换本地链接
					String eninfo = odspst.getGoods_eninfo();
					if (eninfo == null || "".equals(eninfo)) {
						eninfo = "";
					} else {
						if(eninfo.contains("192.168.1.28")){
							eninfo = eninfo.replace("http://192.168.1.28:81/editimg/",
									"http://img1.import-express.com/importcsvimg/");
						}if(eninfo.contains("192.168.1.34")){
							eninfo = eninfo.replace("http://192.168.1.34:81/editimg/",
									"http://img1.import-express.com/importcsvimg/");
						}
					}
					remoteStmt.setString(k++, eninfo);
					remoteStmt.setString(k++, odspst.getRemotpath());
					remoteStmt.setString(k++, odspst.getGoodsUnit());
					int count = remoteStmt.executeUpdate();
					if (count > 0) {
						rs = true;
						stmt.clearParameters();
						stmt.setInt(1, odspst.getId());
						count = stmt.executeUpdate();
						if (count == 0) {
							System.err.println("orderNo: " + odspst.getOrderid() + ",goodsId:" + odspst.getOdId()
									+ " update sync_flag error");
							LOG.error("orderNo: " + odspst.getOrderid() + ",goodsId:" + odspst.getOdId()
									+ " update sync_flag error");
						}
					}
				}
			} else {
				rs = true;
				System.out.println("--syncOfflineToOnline orderSnapshots size:0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("syncOfflineToOnline error :" + e.getMessage());
			LOG.error("syncOfflineToOnline error :" + e.getMessage());
		} finally {
			if (rsSet != null) {
				try {
					rsSet.close();
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
			if (remoteStmt != null) {
				try {
					remoteStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(remoteConn);
		}
		return rs;
	}

	@Override
	public int queryMaxIdFromOrderSnapshot() {

		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rsSet = null;
		PreparedStatement stmt = null;
		String querySql = "select max(od_id) from order_snapshot where 1=1";
		int maxId = 0;
		try {
			stmt = conn.prepareStatement(querySql);
			rsSet = stmt.executeQuery();
			if (rsSet.next()) {
				maxId = rsSet.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryMaxIdFromOrderSnapshot error :" + e.getMessage());
			LOG.error("queryMaxIdFromOrderSnapshot error :" + e.getMessage());
		} finally {
			if (rsSet != null) {
				try {
					rsSet.close();
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
		return maxId;
	}

	@Override
	public List<OrderSnapshot> queryForListByMaxId(int maxId) {

		List<OrderSnapshot> orderSnapshots = new ArrayList<OrderSnapshot>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rsSet = null;
		PreparedStatement stmt = null;
		String querySql = "select id as od_id,userid,goodsid,goodsname,car_type,car_url,car_img,orderid,"
				+ "dropshipid,yourorder,goodsprice,delivery_time,goods_class,car_urlMD5,goods_pid "
				+ "from order_details where 1=1 and id > ?";
		try {

			stmt = conn.prepareStatement(querySql);
			stmt.setInt(1, maxId);

			rsSet = stmt.executeQuery();
			while (rsSet.next()) {
				OrderSnapshot odsh = new OrderSnapshot();
				odsh.setOdId(rsSet.getInt("od_id"));
				odsh.setUserid(rsSet.getInt("userid"));
				odsh.setGoodsid(rsSet.getInt("goodsid"));
				odsh.setGoodsname(rsSet.getString("goodsname"));
				odsh.setGoods_type(rsSet.getString("car_type"));
				odsh.setGoods_url(rsSet.getString("car_url"));
				odsh.setGoods_img(rsSet.getString("car_img"));
				odsh.setOrderid(rsSet.getString("orderid"));
				odsh.setDropshipid(rsSet.getString("dropshipid"));
				odsh.setYourorder(rsSet.getInt("yourorder"));
				odsh.setGoodsprice(rsSet.getString("goodsprice"));
				odsh.setDelivery_time(rsSet.getString("delivery_time"));
				odsh.setGoods_class(rsSet.getInt("goods_class"));
				odsh.setCar_urlMD5(rsSet.getString("car_urlMD5"));
				odsh.setGoods_pid(rsSet.getString("goods_pid"));
				orderSnapshots.add(odsh);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryForListByMaxId error :" + e.getMessage());
			LOG.error("queryForListByMaxId error :" + e.getMessage());
		} finally {
			if (rsSet != null) {
				try {
					rsSet.close();
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
		return orderSnapshots;
	}

	@Override
	public boolean batchInsertOrderSnapshot(List<OrderSnapshot> orderSnapshots) {

		Connection localConn = DBHelper.getInstance().getConnection();
		ResultSet rsSet = null;
		PreparedStatement localStmt = null;
		String inLocalSql = "insert into order_snapshot(od_id,userid,goodsid,goodsname,car_type,car_url,"
				+ "car_img,orderid,dropshipid,yourorder,"
				+ "goodsprice,delivery_time,goods_class,car_urlMD5,goods_pid,down_img_url) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		boolean is = false;

		try {

			if (orderSnapshots.size() > 0) {
				localConn.setAutoCommit(false);
				localStmt = localConn.prepareStatement(inLocalSql);
				int count = 0;
				for (OrderSnapshot odspst : orderSnapshots) {

					count++;
					int k = 1;
					localStmt.setInt(k++, odspst.getOdId());
					localStmt.setInt(k++, odspst.getUserid());
					localStmt.setInt(k++, odspst.getGoodsid());
					localStmt.setString(k++, odspst.getGoodsname());
					localStmt.setString(k++, odspst.getGoods_type());
					localStmt.setString(k++, odspst.getGoods_url());
					localStmt.setString(k++, odspst.getGoods_img());
					localStmt.setString(k++, odspst.getOrderid());
					localStmt.setString(k++, odspst.getDropshipid());
					localStmt.setInt(k++, odspst.getYourorder());
					localStmt.setString(k++, odspst.getGoodsprice());
					localStmt.setString(k++, odspst.getDelivery_time());
					localStmt.setInt(k++, odspst.getGoods_class());
					localStmt.setString(k++, odspst.getCar_urlMD5());
					localStmt.setString(k++, odspst.getGoods_pid());
					localStmt.setString(k++, odspst.getDown_img_url() == null ? "" : odspst.getDown_img_url());
					localStmt.addBatch();

				}
				int[] arrT = localStmt.executeBatch();
				if (count == arrT.length) {
					is = true;
					localConn.commit();
				} else {
					localConn.rollback();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("batchInsertOrderSnapshot error :" + e.getMessage());
			LOG.error("batchInsertOrderSnapshot error :" + e.getMessage());
		} finally {
			if (rsSet != null) {
				try {
					rsSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			DBHelper.getInstance().closeConnection(localConn);
		}
		return is;
	}

	@Override
	public boolean updateOrderSnapshot(OrderSnapshot orderSnapshot) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String upSql = "update order_snapshot set down_img_url=? where od_id=?";
		int count = 0;
		try {

			stmt = conn.prepareStatement(upSql);
			stmt.setString(1, orderSnapshot.getDown_img_url());
			stmt.setInt(2, orderSnapshot.getOdId());
			count = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("updateOrderSnapshot error :" + e.getMessage());
			LOG.error("updateOrderSnapshot error :" + e.getMessage());
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
		return count > 0;
	}

	@Override
	public boolean insertOrderSnapshot(OrderSnapshot orderSnapshot) {

		Connection localConn = DBHelper.getInstance().getConnection();
		ResultSet rsSet = null;
		PreparedStatement localStmt = null;
		String inLocalSql = "insert into order_snapshot(od_id,userid,goodsid,goodsname,car_type,car_url,"
				+ "car_img,orderid,dropshipid,yourorder,"
				+ "goodsprice,delivery_time,goods_class,car_urlMD5,goods_pid,down_img_url) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		boolean is = false;

		try {
			localStmt = localConn.prepareStatement(inLocalSql);
			int k = 1;
			localStmt.setInt(k++, orderSnapshot.getOdId());
			localStmt.setInt(k++, orderSnapshot.getUserid());
			localStmt.setInt(k++, orderSnapshot.getGoodsid());
			localStmt.setString(k++, orderSnapshot.getGoodsname());
			localStmt.setString(k++, orderSnapshot.getGoods_type());
			localStmt.setString(k++, orderSnapshot.getGoods_url());
			localStmt.setString(k++, orderSnapshot.getGoods_img());
			localStmt.setString(k++, orderSnapshot.getOrderid());
			localStmt.setString(k++, orderSnapshot.getDropshipid());
			localStmt.setInt(k++, orderSnapshot.getYourorder());
			localStmt.setString(k++, orderSnapshot.getGoodsprice());
			localStmt.setString(k++, orderSnapshot.getDelivery_time());
			localStmt.setInt(k++, orderSnapshot.getGoods_class());
			localStmt.setString(k++, orderSnapshot.getCar_urlMD5());
			localStmt.setString(k++, orderSnapshot.getGoods_pid());
			localStmt.setString(k++, orderSnapshot.getDown_img_url() == null ? "" : orderSnapshot.getDown_img_url());
			is = localStmt.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("insertOrderSnapshot error :" + e.getMessage());
			LOG.error("insertOrderSnapshot error :" + e.getMessage());
		} finally {
			if (rsSet != null) {
				try {
					rsSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			DBHelper.getInstance().closeConnection(localConn);
		}
		return is;
	}

	@Override
	public String queryExistsImgUrlByPidAndType(String pid, String type) {
		String imgUrl = "";
		if (pid == null || "".equals(pid) || type == null || "".equals(type)) {
			return imgUrl;
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String qySql = "select down_img_url from order_snapshot where goods_pid=? and  car_type=?";
		ResultSet rs = null;

		try {

			stmt = conn.prepareStatement(qySql);
			stmt.setString(1, pid);
			stmt.setString(2, type);
			rs = stmt.executeQuery();
			if (rs.next()) {
				imgUrl = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryExistsImgUrlByPidAndType error :" + e.getMessage());
			LOG.error("queryExistsImgUrlByPidAndType error :" + e.getMessage());
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
		return imgUrl == null ? "" : imgUrl;
	}

}
