package com.cbt.Specification.ctrl;

import com.cbt.bigpro.bean.AliCategoryPojo;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.GoodsDaoBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoodsPropertyServlet {

	public static void main(String[] args) {
		doQuery();
	}

	// 查询产品类别
	public static void doQuery() {
		String cids = "100003109,100003070,1501";
		List<AliCategoryPojo> aliCategoryPojos = getAliCategoryByCid(cids, 4);

		for (AliCategoryPojo aliCa : aliCategoryPojos) {
			int i = 0;
			List<GoodsDaoBean> goodsDaoBeans = queryByCatid(aliCa.getCid());
			if (goodsDaoBeans.size() > 0) {
				for (GoodsDaoBean goodsDaoBean : goodsDaoBeans) {
					i++;
					if (i < 3) {
						parseTypes(aliCa.getCid(), goodsDaoBean.getTypes());
						// System.out.println(goodsDaoBean.getTypes());
					}
				}
			}
		}
	}

	// 解析Types的数据，即产品的属性
	public static void parseTypes(String cid, String types) {

		if (!(types == null || "".equals(types)) && types.length() >= 1) {
			String nwtypes = types.substring(1, types.length() - 1);
			String[] lSTypes = nwtypes.split(",");
			if (lSTypes.length > 0) {
				for (String typeStr : lSTypes) {
					String[] dataSplit = typeStr.split("#");
					if (dataSplit.length > 0) {
						// for (String sp : dataSplit) {
						// System.out.println(sp.trim());
						// }
						// System.out.println();

						String[] typeNameLst = dataSplit[1].trim().split("=");
						String[] typeValueLst = dataSplit[2].trim().split("=");

//						if (typeNameLst.length > 0) {
//							String typeName = dataSplit[1].trim().substring(0, dataSplit[1].trim().length() - 2);
//							insertIntoSpecificationTranslation(null, typeName, cid);
//						}

						if (typeNameLst.length > 0 && typeValueLst.length > 0) {
							String typeName = dataSplit[1].trim().substring(0, dataSplit[1].trim().length() - 2);
							String typeValue = dataSplit[2].trim().substring(0, dataSplit[2].trim().length() - 1);
							insertIntoSpecificationMapping(null, typeName, typeValue, cid);
						}

					}
				}
			}
		}

	}

	// 插入到规格翻译表中
	public static void insertIntoSpecificationTranslation(String chName, String enName, String productCategoryId) {

		String[] typeLst = enName.split("=");
		String typeVal = "";
		if (typeLst.length > 0) {
			typeVal = typeLst[1];
		}
		if (!(typeVal == null || "".equals(typeVal)) && !isExistsTranslation(typeVal, productCategoryId)) {
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			String sql = "insert into specification_translation(chName,enName,product_category_id) values(?,?,?)";
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, chName);
				stmt.setString(2, typeVal);
				stmt.setString(3, productCategoryId);
				stmt.executeUpdate();
				System.out.println("insertIntoSpecificationTranslation successful-- enName:" + typeVal + ",cid:"
						+ productCategoryId);
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
	}

	// 根据enName和productCategoryId判断是否已经存在了
	public static boolean isExistsTranslation(String enName, String productCategoryId) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		String sql = "select * from specification_translation where enName=? and product_category_id=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, enName);
			stmt.setString(2, productCategoryId);
			rSet = stmt.executeQuery();
			if (rSet.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            DBHelper.getInstance().closeResultSet(rSet);
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return false;
	}

	// 插入到规格映射表
	public static void insertIntoSpecificationMapping(String chName, String translationEnName, String enName,
			String productCategoryId) {

		String[] typeNameLst = translationEnName.split("=");
		String typeName = "";
		if (typeNameLst.length > 0) {
			typeName = typeNameLst[1];
		}
		String[] typeValueLst = enName.split("=");
		String typeVal = "";
		if (typeValueLst.length > 0) {
			typeVal = typeValueLst[1];
		}
		if (!(typeName == null || "".equals(typeName))) {

			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			PreparedStatement nwStmt = null;
			ResultSet rSet = null;
			String sql = "select id,chName,enName,product_category_id from specification_translation "
					+ "where enName=? and product_category_id=?";
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, typeName);
				stmt.setString(2, productCategoryId);
				rSet = stmt.executeQuery();
				System.out.println("select specification_translation successful -- enName:" + typeName + ",cid:"
						+ productCategoryId);
				if (rSet.next() && !(typeVal == null || "".equals(typeVal))) {
					int specificationId = rSet.getInt("id");
					sql = "insert into specification_mapping(chName,enName,product_category_id,specification_id)"
							+ "values(?,?,?,?)";
					nwStmt = conn.prepareStatement(sql);
					nwStmt.setString(1, chName);
					nwStmt.setString(2, typeVal);
					nwStmt.setString(3, productCategoryId);
					nwStmt.setInt(4, specificationId);
					nwStmt.executeUpdate();
					System.out.println("insertIntoSpecification_mapping successful-- enName:" + typeVal + ",cid:"
							+ productCategoryId + ",specificationId:" + specificationId);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBHelper.getInstance().closeResultSet(rSet);
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (nwStmt != null) {
					try {
						nwStmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				DBHelper.getInstance().closeConnection(conn);
			}

		}

	}

	// 根据cids和lv查询产品
	public static List<AliCategoryPojo> getAliCategoryByCid(String cids, int lv) {
		String[] idlst = cids.split(",");
		if (idlst.length > 0) {
			String inSql = "";
			for (String id : idlst) {
				inSql += " or FIND_IN_SET('" + id + "',path)";
			}
			if (inSql.equals("")) {
				return null;
			}
			inSql = "(" + inSql.substring(4) + ")";
			String sql = "select id,cid,path,category,time from ali_category where lv=" + lv + " and " + inSql;
			System.out.println(sql);
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			ResultSet res = null;
			List<AliCategoryPojo> list = new ArrayList<AliCategoryPojo>();
			try {
				stmt = conn.prepareStatement(sql);
				res = stmt.executeQuery();
				while (res.next()) {
					AliCategoryPojo category = new AliCategoryPojo();
					category.setCategory(res.getString("category"));
					category.setCid(res.getString("cid"));
					category.setId(String.valueOf(res.getInt("id")));
					category.setPath(res.getString("path"));
					list.add(category);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			    DBHelper.getInstance().closeResultSet(res);
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
		} else {
			return null;
		}
	}

	public static List<GoodsDaoBean> queryByCatid(String catid) {

		if (!(catid == null || "".equals(catid))) {
			String sql = "select goodsdata.id,goodsdata.valid,goodsdata.time,goodsdata.cID,goodsdata.pID,goodsdata.name,goodsdata.url,"
					+ "goodsdata.oPrice,goodsdata.sPrice,goodsdata.wPrice,goodsdata.pUnit,goodsdata.gUnit,goodsdata.img,goodsdata.imgSize,"
					+ "goodsdata.mOrder,goodsdata.pTime,goodsdata.sellUnit,goodsdata.weight,goodsdata.width,goodsdata.perWeight,goodsdata.free,"
					+ "goodsdata.category,goodsdata.sID,goodsdata.title,goodsdata.sUrl,goodsdata.types,goodsdata.info,"
					+ "goodsdata.detail,goodsdata.sell,goodsdata.method,goodsdata.posttime,goodsdata.infourl,goodsdata.packages,"
					+ "goodsdata.bprice,goodsdata.sku,goodsdata.fprice,goodsdata.feeprice,goodsdata.dtime "
					+ "from goodsdata,goodsdata_expand_ex "
					+ "where goodsdata.url=goodsdata_expand_ex.url and goodsdata_expand_ex.catid1='" + catid + "'";

			Connection conn = DBHelper.getInstance().getConnection();
			ResultSet rs = null;
			PreparedStatement stmt = null;
			List<GoodsDaoBean> rsLst = new ArrayList<GoodsDaoBean>();
			try {
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				while (rs.next()) {
					GoodsDaoBean rsTree = new GoodsDaoBean();
					rsTree.setId(rs.getInt("id"));
					rsTree.setValid(rs.getString("valid"));
					rsTree.setTime(rs.getString("time"));
					rsTree.setcID(rs.getString("cID"));
					rsTree.setpID(rs.getString("pID"));
					rsTree.setName(rs.getString("name"));
					rsTree.setUrl(rs.getString("url"));
					rsTree.setoPrice(rs.getString("oPrice"));
					rsTree.setsPrice(rs.getString("sPrice"));
					rsTree.setwPrice(rs.getString("wPrice"));
					rsTree.setpUnit(rs.getString("pUnit"));
					rsTree.setgUnit(rs.getString("gUnit"));
					rsTree.setImg(rs.getString("img"));
					rsTree.setImgSize(rs.getString("imgSize"));
					rsTree.setmOrder(rs.getString("mOrder"));
					rsTree.setpTime(rs.getString("pTime"));
					rsTree.setSellUnit(rs.getString("sellUnit"));
					rsTree.setWeight(rs.getString("weight"));
					rsTree.setWidth(rs.getString("width"));
					rsTree.setPerWeight(rs.getString("perWeight"));
					rsTree.setFree(rs.getString("free"));
					rsTree.setCategory(rs.getString("category"));
					rsTree.setsID(rs.getString("sID"));
					rsTree.setTitle(rs.getString("title"));
					rsTree.setsUrl(rs.getString("sUrl"));
					rsTree.setTypes(rs.getString("types"));
					rsTree.setInfo(rs.getString("info"));
					rsTree.setDetail(rs.getString("detail"));
					rsTree.setSell(rs.getString("sell"));
					rsTree.setMethod(rs.getString("method"));
					rsTree.setPosttime(rs.getString("posttime"));
					rsTree.setInfourl(rs.getString("infourl"));
					rsTree.setPackages(rs.getString("packages"));
					rsTree.setBprice(rs.getString("bprice"));
					rsTree.setSku(rs.getString("sku"));
					rsTree.setfPrice(rs.getString("fprice"));
					rsTree.setFeePrice(rs.getString("feeprice"));
					rsTree.setDtime(rs.getInt("dtime"));
					rsLst.add(rsTree);
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
			return rsLst;
		} else {
			return null;
		}
	}

}
