package com.cbt.Specification.util;

import com.cbt.Specification.bean.GoodsDataInfo;
import com.cbt.jdbc.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InitSpecification {

	// 插入选择区和详情区的数据
	public void insertSpecification() {

		Long begin = System.currentTimeMillis();

		List<GoodsDataInfo> goodsDataInfos = queryGoodsDataInfos();
		List<String> typesSqls = getTypes(goodsDataInfos);
		List<String> detailSqls = getDetail(goodsDataInfos);
		String spcSql = "insert into specification_translation(enName,type,product_category_id) values(?,?,?)";
		String attrSql = "insert into specification_mapping(enName,product_category_id,specification_id) values(?,?,?)";
		try {
			for (String typeInfo : typesSqls) {
				String[] spcLst = typeInfo.split(":");
				int[] checkedLst = isExistSpcName(spcLst[0], spcLst[1], "specification_translation");
				if (checkedLst[0] == 0) {
					insertChoiceTraslation(spcSql, spcLst, attrSql);
				} else {
					if (spcLst.length == 3) {
						if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
							int id = checkedLst[1];
							if (id != 0) {
								insertChoiceMapping(attrSql, spcLst, id);
							}
						} else{
							System.out.println("specification_mapping("+spcLst[0]+","+spcLst[1]+":"+spcLst[2]+")is exist");
						}
					}
				}
			}

			for (String detailInfo : detailSqls) {
				String[] spcLst = detailInfo.split(":");
				int[] checkedLst = isExistSpcName(spcLst[0], spcLst[1], "specification_translation");
				if (checkedLst[0] == 0) {
					insertDetailTraslation(spcSql, spcLst, attrSql);
				} else {
					if (spcLst.length == 3) {
						if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
							int id = checkedLst[1];
							if (id != 0) {
								insertDetailMapping(attrSql, spcLst, id);
							}
						} else{
							System.out.println("specification_mapping("+spcLst[0]+","+spcLst[1]+":"+spcLst[2]+")is exist");
						}
					}
				}
			}

			Long end = System.currentTimeMillis();
			System.out.println(DateFormatUtil.getWithMicroseconds(new Date(begin)));
			System.out.println(DateFormatUtil.getWithMicroseconds(new Date(end)));
			System.out.println("consume : " + (end - begin) / (1000 * 1000 * 60.0) + "hour");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(goodsDataInfos.size());
		}

	}

	private void insertChoiceTraslation(String spcSql, String[] spcLst, String attrSql) {
		Connection conn = DBHelper.getInstance().getConnection();
		try {

			PreparedStatement spcPsmt = conn.prepareStatement(spcSql, Statement.RETURN_GENERATED_KEYS);
			spcPsmt.setString(1, spcLst[1]);
			spcPsmt.setInt(2, 2);
			spcPsmt.setString(3, spcLst[0]);
			spcPsmt.executeUpdate();
			ResultSet rs = spcPsmt.getGeneratedKeys();
			System.out.println("insert detail traslation (" + spcLst[0] + " : " + spcLst[1] + ") success");
			if (rs.next()) {
				if (spcLst.length == 3) {
					if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
						int id = rs.getInt(1);
						if (id != 0) {
							insertChoiceMapping(attrSql, spcLst, id);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}

	}

	private void insertChoiceMapping(String attrSql, String[] spcLst, int id) {

		Connection conn = DBHelper.getInstance().getConnection();
		try {
			PreparedStatement attrPsmt = conn.prepareStatement(attrSql);
			attrPsmt.setString(1, spcLst[2]);
			attrPsmt.setString(2, spcLst[0]);
			attrPsmt.setInt(3, id);
			attrPsmt.executeUpdate();
			System.out
					.println("insert detail mapping (" + spcLst[0] + "," + spcLst[1] + " : " + spcLst[2] + ") success");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}

	}

	private void insertDetailTraslation(String spcSql, String[] spcLst, String attrSql) {
		Connection conn = DBHelper.getInstance().getConnection();
		try {

			PreparedStatement spcPsmt = conn.prepareStatement(spcSql, Statement.RETURN_GENERATED_KEYS);
			spcPsmt.setString(1, spcLst[1]);
			spcPsmt.setInt(2, 2);
			spcPsmt.setString(3, spcLst[0]);
			spcPsmt.executeUpdate();
			ResultSet rs = spcPsmt.getGeneratedKeys();
			System.out.println("insert detail traslation (" + spcLst[0] + " : " + spcLst[1] + ") success");
			if (rs.next()) {
				if (spcLst.length == 3) {
					if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
						int id = rs.getInt(1);
						if (id != 0) {
							insertDetailMapping(attrSql, spcLst, id);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}

	}

	private void insertDetailMapping(String attrSql, String[] spcLst, int id) {

		Connection conn = DBHelper.getInstance().getConnection();
		try {
			PreparedStatement attrPsmt = conn.prepareStatement(attrSql);
			attrPsmt.setString(1, spcLst[2]);
			attrPsmt.setString(2, spcLst[0]);
			attrPsmt.setInt(3, id);
			attrPsmt.executeUpdate();
			System.out
					.println("insert detail mapping (" + spcLst[0] + "," + spcLst[1] + " : " + spcLst[2] + ") success");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}

	}

	private int[] isExistSpcName(String cid, String enName, String tabelName) {
		int[] checkedLst = new int[2];
		Connection conn = DBHelper.getInstance().getConnection();
		try {
			String selectSql = "select id,enName from " + tabelName + " where product_category_id = '" + cid + "' "
					+ "and enName = '" + enName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectSql);

			checkedLst[0] = 0;
			while (rs.next()) {
				checkedLst[0] = 1;
				checkedLst[1] = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}
		return checkedLst;
	}

	private boolean isExistAttrEnName(String cid, String enName, String tabelName) {
		boolean isExist = false;
		Connection conn = DBHelper.getInstance().getConnection();
		try {
			String selectSql = "select enName from " + tabelName + " where product_category_id = '" + cid + "' "
					+ "and enName = '" + enName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectSql);
			while (rs.next()) {
				isExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}
		return isExist;

	}

	private List<String> getTypes(List<GoodsDataInfo> goodsDataInfos) {

		List<String> sqls = new ArrayList<String>();
		for (GoodsDataInfo gInfo : goodsDataInfos) {
			if (!(gInfo.getCid() == null || "".equals(gInfo.getCid()))) {

				if (!(gInfo.getTypes() == null || "".equals(gInfo.getTypes()))) {

					String typesStr = gInfo.getTypes().substring(1, gInfo.getTypes().length() - 1);
					String[] typesLst = typesStr.split(",");
					String typeName = "";
					String valueName = "";
					for (String types : typesLst) {
						String[] spcLst = types.split("#");
						for (int i = 1; i < spcLst.length - 1; i++) {
							String[] spcAndAttr = spcLst[i].split("=");
							if (spcAndAttr.length == 2) {
								if (spcAndAttr[0].trim().equals("type")) {
									typeName = spcAndAttr[1];
									continue;
								} else if (spcAndAttr[0].trim().equals("value")) {
									valueName = spcAndAttr[1];
									continue;
								}
							}
						}
						if (typeName.indexOf("+") != -1) {
							typeName = typeName.substring(0, typeName.indexOf("+"));
						}
						if (typeName.indexOf(":") != -1) {
							typeName = typeName.substring(0, typeName.indexOf(":"));
						}
						if (valueName.indexOf("+") != -1) {
							valueName = valueName.substring(0, valueName.indexOf("+"));
						}
						sqls.add(gInfo.getCid() + ":" + typeName.trim() + ":" + valueName.trim());
					}

				}

			}
		}
		return sqls;

	}

	private List<String> getDetail(List<GoodsDataInfo> goodsDataInfos) {
		List<String> sqls = new ArrayList<String>();
		for (GoodsDataInfo gInfo : goodsDataInfos) {
			if (!(gInfo.getCid() == null || "".equals(gInfo.getCid()))) {

				if (!(gInfo.getDetail() == null || "".equals(gInfo.getDetail()))) {

					String detailStr = gInfo.getDetail().substring(1, gInfo.getDetail().length() - 1);
					String[] detailLst = detailStr.split(",");
					for (String detail : detailLst) {
						String[] spcLst = detail.split(":");
						if (spcLst.length == 2) {
							String[] spcNameLst = spcLst[0].split("=");
							if (spcNameLst.length == 2) {
								sqls.add(gInfo.getCid() + ":" + spcNameLst[1].trim() + ":" + spcLst[1].trim());
							}
						}
					}
				}
			}
		}

		return sqls;

	}

	private List<GoodsDataInfo> queryGoodsDataInfos() {
		Connection conn = DBHelper.getInstance().getConnection();
		List<GoodsDataInfo> goodsDataInfos = new ArrayList<GoodsDataInfo>();
		try {
			String selectSql = "select goodsdata.id,goodsdata_expand_ex.catid1,goodsdata.detail,goodsdata.types "
					+ "from goodsdata,goodsdata_expand_ex where goodsdata.url = goodsdata_expand_ex.url "
					+ "order by goodsdata.id desc";
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectSql);
				while (rs.next()) {
					GoodsDataInfo gInfo = new GoodsDataInfo();
					gInfo.setGoodsid(rs.getInt("id"));
					gInfo.setCid(rs.getString("catid1"));
					gInfo.setDetail(rs.getString("detail"));
					gInfo.setTypes(rs.getString("types"));
					goodsDataInfos.add(gInfo);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBHelper.getInstance().closeConnection(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsDataInfos;

	}

}
