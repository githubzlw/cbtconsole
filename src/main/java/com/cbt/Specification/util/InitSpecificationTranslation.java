package com.cbt.Specification.util;

import com.cbt.Specification.bean.GoodsDataInfo;
import com.cbt.jdbc.DBHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InitSpecificationTranslation {

	public static void main(String[] args) {
		// testQueryGoodsDataInfo();
		// testDetail();
		// testTypes();
		//insertTypes();
		// queryAllInfo();

		InitSpecification initSpc = new InitSpecification();
		initSpc.insertSpecification();

	}

	// 插入选择区的数据
	public static void insertTypes() {

		Long begin = System.currentTimeMillis();

		java.sql.Connection conn = DBHelper.getInstance().getConnection();
		List<String> typesSqls = testTypes();
		List<String> detailSqls = testDetail();
		String spcSql = "insert into specification_translation(enName,type,product_category_id) values(?,?,?)";
		String attrSql = "insert into specification_mapping(enName,product_category_id,specification_id) values(?,?,?)";
		try {
			for (String typeInfo : typesSqls) {
				String[] spcLst = typeInfo.split(":");
				HashMap<String, Object> spcMap = isExistSpcName(spcLst[0], spcLst[1], "specification_translation");
				if (!Boolean.valueOf(spcMap.get("isExist").toString())) {
					PreparedStatement spcPsmt = conn.prepareStatement(spcSql, Statement.RETURN_GENERATED_KEYS);
					spcPsmt.setString(1, spcLst[1]);
					spcPsmt.setInt(2, 1);
					spcPsmt.setString(3, spcLst[0]);
					spcPsmt.executeUpdate();
					ResultSet rs = spcPsmt.getGeneratedKeys();
					System.out.println("insert choice traslation (" + spcLst[0] + " : " + spcLst[1] + ") success");
					if (rs.next()) {
						if (spcLst.length == 3) {
							if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
								int id = rs.getInt(1);
								if (id != 0) {
									PreparedStatement attrPsmt = conn.prepareStatement(attrSql);
									attrPsmt.setString(1, spcLst[2]);
									attrPsmt.setString(2, spcLst[0]);
									attrPsmt.setInt(3, id);
									attrPsmt.executeUpdate();
									System.out.println("insert choice mapping (" + spcLst[0] + " , " + spcLst[1] + " : "
											+ spcLst[2] + ") success");
								}
							}
						}
					}
				} else {
					if (spcLst.length == 3) {
						if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
							int id = Integer.valueOf(spcMap.get("id").toString());
							if (id != 0) {
								PreparedStatement attrPsmt = conn.prepareStatement(attrSql);
								attrPsmt.setString(1, spcLst[2]);
								attrPsmt.setString(2, spcLst[0]);
								attrPsmt.setInt(3, id);
								attrPsmt.executeUpdate();
								System.out.println("insert  choice mapping (" + spcLst[0] + "," + spcLst[1] + " : "
										+ spcLst[2] + ") success");
							}
						}
					}
				}
			}

			for (String detailInfo : detailSqls) {
				String[] spcLst = detailInfo.split(":");
				HashMap<String, Object> attrMap = isExistSpcName(spcLst[0], spcLst[1], "specification_translation");
				if (!Boolean.valueOf(attrMap.get("isExist").toString())) {
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
									PreparedStatement attrPsmt = conn.prepareStatement(attrSql);
									attrPsmt.setString(1, spcLst[2]);
									attrPsmt.setString(2, spcLst[0]);
									attrPsmt.setInt(3, id);
									attrPsmt.executeUpdate();
									System.out.println("insert detail mapping (" + spcLst[0] + "," + spcLst[1] + " : "
											+ spcLst[2] + ") success");
								}
							}
						}
					}
				} else {
					if (spcLst.length == 3) {
						if (!isExistAttrEnName(spcLst[0], spcLst[2], "specification_mapping")) {
							int id = Integer.valueOf(attrMap.get("id").toString());
							if (id != 0) {
								PreparedStatement attrPsmt = conn.prepareStatement(attrSql);
								attrPsmt.setString(1, spcLst[2]);
								attrPsmt.setString(2, spcLst[0]);
								attrPsmt.setInt(3, id);
								attrPsmt.executeUpdate();
								System.out.println("insert detail mapping (" + spcLst[0] + "," + spcLst[1] + " : "
										+ spcLst[2] + ") success");
							}
						}
					}
				}
			}

			Long end = System.currentTimeMillis();
			System.out.println(DateFormatUtil.getWithMicroseconds(new Date(begin)));
			System.out.println(DateFormatUtil.getWithMicroseconds(new Date(end)));
			System.out.println("consume : " + (end - begin) / 1000 * 60.0 + "minutes");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}

	}

	private static HashMap<String, Object> isExistSpcName(String cid, String enName, String tabelName) {
		HashMap<String, Object> isMap = new HashMap<String, Object>();
		java.sql.Connection conn = DBHelper.getInstance().getConnection();
		try {
			String selectSql = "select id,enName from " + tabelName + " where product_category_id = '" + cid + "' "
					+ "and enName = '" + enName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectSql);

			isMap.put("isExist", false);
			while (rs.next()) {
				isMap.put("isExist", true);
				isMap.put("id", rs.getInt("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
		}
		return isMap;
	}

	private static boolean isExistAttrEnName(String cid, String enName, String tabelName) {
		boolean isExist = false;
		java.sql.Connection conn = DBHelper.getInstance().getConnection();
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

	private static List<String> testTypes() {

		List<String> sqls = new ArrayList<String>();
		List<GoodsDataInfo> goodsDataInfos = queryGoodsDataInfos();
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

	private static List<String> testDetail() {
		List<String> sqls = new ArrayList<String>();
		List<GoodsDataInfo> goodsDataInfos = queryGoodsDataInfos();
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

	private static List<GoodsDataInfo> queryGoodsDataInfos() {
		java.sql.Connection conn = DBHelper.getInstance().getConnection();
		List<GoodsDataInfo> goodsDataInfos = new ArrayList<GoodsDataInfo>();
		try {
			String selectSql = "select goodsdata.id,goodsdata_expand_ex.catid1,goodsdata.detail,goodsdata.types "
					+ "from goodsdata,goodsdata_expand_ex where goodsdata.url = goodsdata_expand_ex.url ";
			// selectSql += "and goodsdata_expand_ex.catid1 = '200000670' ";
			// selectSql += "limit 0,5";
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
