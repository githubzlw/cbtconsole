package com.cbt.dao.impl;

import com.cbt.bean.*;
import com.cbt.dao.NewCloudGoodsDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Md5Util;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewCloudGoodsDaoImpl implements NewCloudGoodsDao {

	private static final Log LOG = LogFactory.getLog(NewCloudGoodsDaoImpl.class);

	@Override
	public List<CategoryBean> getCaterory() {
		List<CategoryBean> list = new ArrayList<CategoryBean>();
		String sql = "select  catid,category,count(cbm.pid) as total from custom_benchmark_ready_cloud cbm,ali_category alc "
				+ "where cbm.catid=alc.cid and cbm.catid !='0' group by cbm.catid order by category asc";

		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CategoryBean bean = new CategoryBean();
				bean.setCategoryName(rs.getString("category"));
				bean.setCid(rs.getString("catid"));
				bean.setTotal(rs.getInt("total"));
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getCaterory error :" + e.getMessage());
			LOG.error("getCaterory error :" + e.getMessage());
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
	public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean) {

		List<CategoryBean> list = new ArrayList<CategoryBean>();
		String sql = "select alc.cid,alc.category,alc.path,alc.lv,"
				+ "ifnull(cbm.total,0) as total from ali_category alc  left join "
				+ "(select catid,count(pid) as total  from custom_benchmark_ready_cloud where 1=1  ";

		if (queryBean.getValid() > -1) {
			sql = sql + " and valid = ?";
		}
		
		if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
			sql = sql + " and createtime >= ?";
		}
		if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
			sql = sql + " and createtime <= ?";
		}
		if (queryBean.getAdminId() > 0) {
			sql = sql + " and admin_id = ?";
		}
		if (queryBean.getIsEdited() > -1) {
			sql = sql + " and is_edited = ?";
		}
		
		sql += " group by catid) cbm on alc.cid=cbm.catid" + " order by alc.lv,alc.category asc ";

		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		System.err.println(sql);
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			
			if(queryBean.getValid() > -1) {
				stmt.setInt(index, queryBean.getValid());
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
			if (queryBean.getAdminId() > 0) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}

			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
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
	public List<CustomGoodsBean> getGoodsList(String catid, int page, String sttime, String edtime, int state) {

		String sql = "select  sql_calc_found_rows keyword,catid,img,pid,localpath,"
				+ "remotpath,url,enname,valid,publishtime,admin,goodsstate,updatetime "
				+ "from custom_goods where 1=1  ";
		if (catid != null && !catid.isEmpty()) {
			sql = sql + " and catid = ?";
		}
		if (sttime != null && !sttime.isEmpty()) {
			sql = sql + " and publishtime > ?";
		}
		if (edtime != null && !edtime.isEmpty()) {
			sql = sql + " and publishtime < ?";
		}
		if (state != 0) {
			sql = sql + " and goodsstate = ?";
		}

		sql = sql + " limit " + ((page - 1) * 40) + ",40";

		List<CustomGoodsBean> list = new ArrayList<CustomGoodsBean>();

		Connection conn = DBHelper.getInstance().getConnection();

		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		int count = 0;
		int index = 1;
		try {
			System.err.println(sql);
			stmt = conn.prepareStatement(sql);
			if (catid != null && !catid.isEmpty()) {
				stmt.setString(index, catid);
				index++;
			}
			if (sttime != null && !sttime.isEmpty()) {
				stmt.setString(index, sttime);
				index++;
			}
			if (edtime != null && !edtime.isEmpty()) {
				stmt.setString(index, edtime);
				index++;
			}
			if (state != 0) {
				stmt.setInt(index, state);
				index++;
			}

			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			if (rs2.next()) {
				count = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				CustomGoodsBean bean = new CustomGoodsBean();
				bean.setKeyword(rs.getString("keyword"));
				bean.setCatid(rs.getString("catid"));
				bean.setPid(rs.getString("pid"));
				bean.setEnname(rs.getString("enname"));
				bean.setLocalpath(rs.getString("localpath"));
				bean.setRemotpath(rs.getString("remotpath"));
				String url = rs.getString("url");

				if (StringUtils.indexOf(url, "www.local.com") > -1) {
					bean.setUrl("&source=D" + Md5Util.encoder(rs.getString("pid")) + "&item=" + rs.getString("pid"));
				} else {
					bean.setUrl("&source=D" + url + "&item=" + rs.getString("pid"));
				}
				// "&source=D"+Md5Util.encoder(rs.getString("pid"))+"&item="+rs.getString("pid")
				bean.setCount(count);
				;
				bean.setImg(rs.getString("img").split(",")[0].replace("[", "").replace("]", ""));
				// bean.setEninfo(rs.getString("eninfo"));
				// bean.setInfo(rs.getString("info"));
				bean.setGoodsState(rs.getInt("goodsstate"));
				bean.setPublishtime(rs.getString("publishtime"));
				bean.setAdmin(rs.getString("admin"));
				bean.setUpdatetime(rs.getString("updatetime"));
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getGoodsList error :" + e.getMessage());
			LOG.error("getGoodsList error :" + e.getMessage());
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
			if (stmt2 != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs2 != null) {
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
		String sql = "select * from custom_benchmark_ready_cloud where pid=? limit 1";
		Connection conn = type == 0 ? DBHelper.getInstance().getConnection() : DBHelper.getInstance().getConnection2();
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
	public List<CustomGoodsBean> getGoodsList(String pidList) {
		String sql = "select *from custom_goods where pid in (" + pidList + ")";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<CustomGoodsBean> list = new ArrayList<CustomGoodsBean>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CustomGoodsBean bean = new CustomGoodsBean();
				bean.setCatid(rs.getString("catid"));
				bean.setCatid1(rs.getString("catid1"));
				bean.setCreatetime(rs.getString("createtime"));
				bean.setDetail(rs.getString("detail"));
				bean.setEndetail(rs.getString("endetail"));
				bean.setEninfo(rs.getString("eninfo"));
				bean.setEntype(rs.getString("entype"));
				bean.setFeeprice(rs.getString("feeprice"));
				bean.setFprice(rs.getString("fprice"));
				bean.setImg(rs.getString("img"));
				bean.setInfo(rs.getString("info"));
				bean.setKeyword(rs.getString("keyword"));
				bean.setLocalpath(rs.getString("localpath"));
				bean.setMethod(rs.getString("method"));
				bean.setMorder(rs.getInt("morder"));
				bean.setName(rs.getString("name"));
				bean.setEnname(rs.getString("enname"));
				bean.setPid(rs.getString("pid"));
				bean.setPosttime(rs.getString("posttime"));
				bean.setPrice(rs.getString("price"));
				bean.setRemotpath(rs.getString("remotpath"));
				bean.setSku(rs.getString("sku"));
				bean.setSold(rs.getInt("sold"));
				bean.setSolds(rs.getInt("solds"));
				bean.setType(rs.getString("type"));
				bean.setUpdatetime(rs.getString("updatetime"));
				bean.setUrl(rs.getString("url"));
				bean.setValid(rs.getInt("valid"));
				bean.setVolum(rs.getString("volum"));
				bean.setWeight(rs.getString("weight"));
				bean.setWprice(rs.getString("wprice"));
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getGoodsList error :" + e.getMessage());
			LOG.error("getGoodsList error :" + e.getMessage());
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

		String upsql = "update custom_benchmark_ready_cloud set valid=1,keyword=?,eninfo=?,enname=?,"
				+ "weight=?,img=?,endetail=?,feeprice=?,revise_weight=?,final_weight=?, "
				+ "price=?,wprice=?,range_price=?,sku=?,createtime=now(),bm_flag=1,goodsstate=4,valid=1";
		if (bean.getIsEdited() == 1) {
			upsql += ",finalName=?";
		} else if (bean.getIsEdited() == 2) {
			upsql += ",finalName=?,infoReviseFlag=?,priceReviseFlag=?";
		}
		upsql += ",is_show_det_img_flag=?";
		upsql += " where pid=?";
		Connection conn = DBHelper.getInstance().getConnection2();
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
			} else {
				stmt2.setInt(i++, 0);
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
	public int publishList(List<CustomGoodsBean> list) {
		String sqlDrop = "DROP TABLE IF EXISTS  custom_goods_temporary";

		String sqlCreate = "CREATE temporary TABLE `custom_goods_temporary` (" + "`keyword` longtext COMMENT '置顶关键词',"
				+ "`catid` varchar(20) DEFAULT NULL COMMENT '置顶类别id',"
				+ "`catid1` varchar(20) DEFAULT NULL COMMENT '1688类别'," + "`pid` varchar(20) NOT NULL COMMENT '商品id',"
				+ "`img` varchar(5000) DEFAULT NULL COMMENT '商品图片'," + "`url` varchar(500) DEFAULT NULL COMMENT '商品链接',"
				+ "`enname` varchar(5000) DEFAULT NULL COMMENT '商品 名称英文',"
				+ "`price` double(16,2) DEFAULT NULL COMMENT '商品价格',"
				+ "`wprice` varchar(300) DEFAULT NULL COMMENT '商品批发价格',"
				+ "`volum` varchar(100) DEFAULT NULL COMMENT '商品体积',"
				+ "`weight` varchar(20) DEFAULT NULL COMMENT '商品重量'," + "`entype` text COMMENT '商品规格英文',"
				+ "`endetail` text COMMENT '商品明细英文'," + "`eninfo` longtext COMMENT '详情英文',"
				+ "`sku` longtext COMMENT '规格属性'," + "`morder` int(11) DEFAULT '1' COMMENT '最小订量',"
				+ "`sold` int(11) DEFAULT '0' COMMENT '销量'," + "`solds` int(11) DEFAULT NULL COMMENT '可售数量',"
				+ "`feeprice` varchar(20) DEFAULT NULL COMMENT '免邮运费',"
				+ "`method` varchar(100) DEFAULT NULL COMMENT '快递方式',"
				+ "`posttime` varchar(20) DEFAULT NULL COMMENT '快递时间',"
				+ "`fprice` varchar(20) DEFAULT NULL COMMENT '非免邮商品价格'," + "`valid` int(2) DEFAULT '0' COMMENT '数据有效性',"
				+ "`remotpath` varchar(500) DEFAULT NULL COMMENT '远程路径',"
				+ "`localpath` varchar(500) DEFAULT NULL COMMENT '本地路径',"
				+ "`createtime` datetime DEFAULT NULL COMMENT '入库时间',"
				+ "`updatetime` datetime DEFAULT NULL COMMENT '更新时间'," + "`updateflag` int(1) DEFAULT '0' ,"
				+ "KEY `pid` (`pid`) ," + "KEY `updateflag` (`updateflag`)" + ")ENGINE=MEMORY  DEFAULT CHARSET=utf8";

		String sqlInsert = "insert into custom_goods_temporary "
				+ "(keyword,catid,catid1,pid,img,url,enname,price,wprice,"
				+ "volum,weight,entype,endetail,eninfo,sku,morder,sold,solds,"
				+ "feeprice,method,posttime,fprice,remotpath,localpath," + "createtime,updatetime,updateflag,valid) "
				+ "values(?,?,?,?,?,?,?,?,?,?,,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),0,0)";

		String sqlUpdate = "update custom_goods c,custom_goods_temporary t set t.updateflag=1,"
				+ " c.keyword=t.keyword," + "c.catid=tcatid.," + "c.catid1=t.catid1," + "c.img=t.img," + "c.url=t.url,"
				+ "c.enname=t.enname," + "c.price=t.price," + "c.wprice=t.wprice," + "c.volum=t.volum,"
				+ "c.weight=t.weight," + "c.entype=t.entype," + "c.endetail=t.endetail," + "c.eninfo=t.eninfo,"
				+ "c.sku=t.sku," + "c.morder=t.morder," + "c.sold=t.sold," + "c.solds=t.solds,"
				+ "c.feeprice=t.feeprice," + "c.method=t.method," + "c.posttime=t.posttime," + "c.fprice=t.fprice,"
				+ "c.remotpath=t.remotpath," + "c.localpath=t.localpath," + "c.updatetime=t.updatetime,"
				+ "c.valid=t.valid" + "where  c.pid =t.pid";

		String sqlAdd = "insert into custom_goods(keyword,catid,catid1,pid,img,url,enname,price,wprice,"
				+ "volum,weight,entype,endetail,eninfo,sku,morder,sold,solds,"
				+ "feeprice,method,posttime,fprice,remotpath,localpath," + "createtime,updatetime,valid)"
				+ "(select keyword,catid,catid1,pid,img,url,enname,price,wprice,"
				+ "volum,weight,entype,endetail,eninfo,sku,morder,sold,solds,"
				+ "feeprice,method,posttime,fprice,remotpath,localpath,"
				+ "createtime,updatetime,valid from custom_goods_temporary where updateflag=0)";

		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmtDrop = null;
		PreparedStatement stmtCreate = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		PreparedStatement stmtAdd = null;
		int result = 0;
		try {
			stmtDrop = conn.prepareStatement(sqlDrop);
			stmtDrop.execute();

			stmtCreate = conn.prepareStatement(sqlCreate);
			stmtCreate.execute();

			stmtInsert = conn.prepareStatement(sqlInsert);
			for (CustomGoodsBean bean : list) {
				stmtInsert.setString(1, bean.getKeyword());
				stmtInsert.setString(2, bean.getCatid());
				stmtInsert.setString(3, bean.getCatid1());
				stmtInsert.setString(4, bean.getPid());
				stmtInsert.setString(5, bean.getImg());
				stmtInsert.setString(6, bean.getUrl());
				stmtInsert.setString(7, bean.getEnname());
				stmtInsert.setString(8, bean.getPrice());
				stmtInsert.setString(9, bean.getWprice());
				stmtInsert.setString(10, bean.getVolum());
				stmtInsert.setString(11, bean.getWeight());
				stmtInsert.setString(12, bean.getEntype());
				stmtInsert.setString(13, bean.getEndetail());
				stmtInsert.setString(14, bean.getEninfo());
				stmtInsert.setString(15, bean.getSku());
				stmtInsert.setInt(16, bean.getMorder());
				stmtInsert.setInt(17, bean.getSold());
				stmtInsert.setInt(18, bean.getSolds());
				stmtInsert.setString(19, bean.getFeeprice());
				stmtInsert.setString(20, bean.getMethod());
				stmtInsert.setString(21, bean.getPosttime());
				stmtInsert.setString(22, bean.getFprice());
				stmtInsert.setString(23, bean.getRemotpath());
				stmtInsert.setString(24, bean.getLocalpath());
				stmtInsert.addBatch();
			}

			stmtInsert.executeBatch();

			stmtUpdate = conn.prepareStatement(sqlUpdate);
			stmtUpdate.executeUpdate();

			stmtAdd = conn.prepareStatement(sqlAdd);
			stmtAdd.executeUpdate();

			stmtDrop.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmtDrop != null) {
				try {
					stmtDrop.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtCreate != null) {
				try {
					stmtCreate.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtInsert != null) {
				try {
					stmtInsert.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtUpdate != null) {
				try {
					stmtUpdate.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtInsert != null) {
				try {
					stmtInsert.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public String getGoodsInfo(String pid) {
		String sql = "select eninfo from custom_goods where valid=1 and pid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String result = "";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("eninfo");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getGoodsInfo error :" + e.getMessage());
			LOG.error("getGoodsInfo error :" + e.getMessage());
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
		return result;
	}

	@Override
	public int updateState(int state, String pid, int adminid) {
		String sql = "update custom_benchmark_ready_cloud set goodsstate=?,admin_id=?,publishtime=now(),valid=1";
		if (state == 4) {
			sql += ",bm_flag=1,is_edited='1'";
		}
		sql += " where pid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setInt(2, adminid);
			stmt.setString(3, pid);
			rs = stmt.executeUpdate();
			System.err.println(pid + ":" + sql);
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
	public boolean updateStateList(int state, String pids, int adminid) {
		Connection conn = DBHelper.getInstance().getConnection();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		PreparedStatement remoteStmt = null;

		String upSql = "update custom_benchmark_ready_cloud set valid=?,goodsstate=?,admin_id=?,"
				+ "publishtime=now(),updatetime=now() where pid = ?";
		String upRemoteSql = "update custom_benchmark_ready_cloud set valid=?,goodsstate=? where pid = ?";

		int rs = 0;
		int count = 0;

		try {
			String[] pidList = pids.split(",");
			conn.setAutoCommit(false);
			remoteConn.setAutoCommit(false);

			stmt = conn.prepareStatement(upSql);
			remoteStmt = remoteConn.prepareStatement(upRemoteSql);
			
			for (String pid : pidList) {
				if (pid == null || "".equals(pid)) {
					continue;
				} else {
					count++;
					remoteStmt.setInt(1, state == 4 ? 1 : 0);
					remoteStmt.setInt(2, state);
					remoteStmt.setString(3, pid);
					remoteStmt.addBatch();
					stmt.setInt(1, state == 4 ? 1 : 0);
					stmt.setInt(2, state);
					stmt.setInt(3, adminid);
					stmt.setString(4, pid);
					stmt.addBatch();

				}
			}
			rs = remoteStmt.executeBatch().length;
			if (rs == count) {
				rs = 0;
				rs = stmt.executeBatch().length;
				if (rs == count) {
					remoteConn.commit();
					conn.commit();
				} else {
					remoteConn.rollback();
					conn.rollback();
				}
			} else {
				remoteConn.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateStateList error :" + e.getMessage());
			LOG.error("updateStateList error :" + e.getMessage());
			try {
				remoteConn.rollback();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} finally {
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
		return rs == count ;
	}

	@Override
	public int updateValid(int valid, String pid) {
		String sql = "update custom_goods set valid=? where pid=? ";
		Connection conn = DBHelper.getInstance().getConnection2();
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
	public int updateValidList(int valid, String pids) {
		String sql = "update custom_benchmark_ready_cloud set valid=? and goodsstate =? where pid in ( " + pids + ")";
		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setInt(2, (valid == 0 ? 2 : 4));
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateValidList error :" + e.getMessage());
			LOG.error("updateValidList error :" + e.getMessage());
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
	public int insertRecord(String pid, String admin, int state, String record) {
		String sql = "insert into custom_goods_action_state "
				+ "(goodsstatus,admin,pid,updatetime,record) value (?,?,?,now(),?) ";

		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setString(2, admin);
			stmt.setString(3, pid);
			stmt.setString(4, record);
			rs = stmt.executeUpdate();

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
		return rs;
	}

	public int insertRecordList(List<String> pids, String admin, int state, String record) {
		String sql = "insert into custom_goods_action_state " + "(goodsstatus,admin,pid,updatetime,record) "
				+ "values (?,?,?,now(),?) ";

		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (String pid : pids) {
				stmt.setInt(1, state);
				stmt.setString(2, admin);
				stmt.setString(3, pid);
				stmt.setString(4, record);
				stmt.addBatch();
			}
			stmt.executeBatch();

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
		return rs;
	}

	@Override
	public List<CustomRecord> getRecordList(String pid, int page) {
		String sql = "select sql_calc_found_rows * from custom_goods_action_state where pid=? "
				+ "order by updatetime desc limit ?,40";
		List<CustomRecord> list = new ArrayList<CustomRecord>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		int count = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			stmt.setInt(2, (page - 1) * 40);
			rs = stmt.executeQuery();

			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			if (rs2.next()) {
				count = rs2.getInt("found_rows()");
			}

			while (rs.next()) {
				CustomRecord bean = new CustomRecord();
				bean.setGoodsPid(pid);
				bean.setUpdateTime(rs.getString("updatetime"));
				bean.setAdmin(rs.getString("admin"));
				// 2-产品下架 3-发布失败 4-发布成功
				int state = rs.getInt("goodsstatus");
				if (state == 2) {
					bean.setGoodsState("产品下架");
				} else if (state == 3) {
					bean.setGoodsState("发布失败");
				} else if (state == 4) {
					bean.setGoodsState("发布成功");
				}
				bean.setRecord(rs.getString("record"));
				bean.setCount(count);
				list.add(bean);
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
	public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean) {

		String sqlo = "select cbr.* "
				+ " from (select cbu.keyword,cbu.catid,cbu.pid,cbu.name,cbu.enname,cbu.localpath,cbu.remotpath,cbu.goodsstate,"
				+ "cbu.custom_main_image,cbu.bm_flag,cbu.valid,cbu.isBenchmark,cbu.source_pro_flag,cbu.source_used_flag,"
				+ "cbu.ocr_match_flag,cbu.priority_flag,cbu.updatetime,cbu.publishtime,adu.admName as admin,"
				+ "ifnull(adu.id,0) as admin_id,cbu.is_edited,cbu.is_sold_flag,cbu.is_add_car_flag,cbu.is_abnormal"
				+ " from custom_benchmark_ready_cloud cbu left join (select id,admName from admuser where status =1) adu "
				+ "on cbu.admin_id = adu.id where 1=1  ";
		String sqlb = ") cbr  where 1=1";
		if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
			sqlo = sqlo + " and cbu.catid  in(select cid from ali_category  where find_in_set(?,path))";
			sqlb = sqlb + " and catid  in(select cid from ali_category  where find_in_set(?,path))";
		}
		if (queryBean.getValid() > -1) {
			sqlo = sqlo + " and cbu.valid = ?";
			sqlb = sqlb + " and valid = ?";
		}
		if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
			sqlo = sqlo + " and cbu.createtime >= ?";
			sqlb = sqlb + " and createtime >= ?";
		}
		if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
			sqlo = sqlo + " and cbu.createtime <= ?";
			sqlb = sqlb + " and createtime <= ?";
		}
		if (queryBean.getAdminId() > 0) {
			sqlo = sqlo + " and cbu.admin_id = ?";
			sqlb = sqlb + " and admin_id = ?";
		}
		if (queryBean.getIsEdited() > -1) {
			sqlo = sqlo + " and cbu.is_edited = ?";
			sqlb = sqlb + " and is_edited = ?";
		}

		sqlo += sqlb  + " group by cbr.pid limit " + ((queryBean.getPage() - 1) * 40) + ",40";

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
			if (queryBean.getValid() > -1) {
				stmt.setInt(index, queryBean.getValid());
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
			if (queryBean.getAdminId() > 0) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}
			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
				index++;
			}

			// 二次设置
			if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
				stmt.setString(index, queryBean.getCatid());
				index++;
			}
			if (queryBean.getValid() > -1) {
				stmt.setInt(index, queryBean.getValid());
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
			if (queryBean.getAdminId() > 0) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}
			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
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
				bean.setUrl("&source=N" + Md5Util.encoder(rs.getString("pid")) + "&item=" + rs.getString("pid"));

				String name1688 = rs.getString("name");
				if (name1688 == null || "".equals(name1688)) {
					bean.setName("1688 商品url");
				} else {
					bean.setName(name1688);
				}

				String remotpath = rs.getString("remotpath");
				bean.setShowMainImage(remotpath + rs.getString("custom_main_image"));

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

		String sql = "select count(pid) from (select pid from custom_benchmark_ready_cloud where 1=1  ";
		if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
			System.err.println("catid:" + queryBean.getCatid());
			sql = sql + " and catid in(select cid from ali_category  where find_in_set(?,path))";
		}
		if (queryBean.getValid() > -1) {
			sql = sql + " and valid = ?";
		}
		if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
			sql = sql + " and createtime > ?";
		}
		if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
			sql = sql + " and createtime < ?";
		}

		if (queryBean.getAdminId() > 0) {
			sql = sql + " and admin_id = ?";
		}
		if (queryBean.getIsEdited() > -1) {
			sql = sql + " and is_edited = ?";
		}

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
			if (queryBean.getValid() > -1) {
				stmt.setInt(index, queryBean.getValid());
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
			if (!(queryBean.getAdminId() == 0)) {
				stmt.setInt(index, queryBean.getAdminId());
				index++;
			}

			if (queryBean.getIsEdited() > -1) {
				stmt.setInt(index, queryBean.getIsEdited());
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
		String btSql = "update custom_benchmark_ready_cloud set enname = ?,admin=?,goodsstate=?,admin_id=?,"
				+ "updatetime=now(),is_edited='1' where pid = ? ";
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(btSql);
			for (CustomGoodsBean cg : cgLst) {
				stmt.setString(1, cg.getEnname());
				stmt.setString(2, user.getAdmName());
				stmt.setInt(3, 5);
				stmt.setInt(4, user.getId());
				stmt.setString(5, cg.getPid());
				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
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
	public CustomGoodsPublish queryGoodsDetails(String pid, int type) {

		String sql = "select cbu.pid,cbu.enname,cbu.endetail,cbu.eninfo,cbu.feeprice,cbu.fprice,"
				+ "cbu.keyword,cbu.custom_main_image as main_img,cbu.price,cbu.final_weight,cbu.wprice,cbu.entype,cbu.sku,"
				+ "cbu.goodsstate,cbu.bm_flag,cbu.valid,cbu.isBenchmark,cbu.source_pro_flag,cbu.source_used_flag,"
				+ "cbu.ocr_match_flag,cbu.priority_flag,cbu.img,cbu.range_price,cbu.revise_weight,cbu.localpath,cbu.remotpath,"
				+ "cbu.is_sold_flag,cbu.is_add_car_flag,cbu.ali_pid,cbu.is_edited,cbu.updatetime,adu.admName as admin,"
				+ "ifnull(adu.id,0) as admin_id,cbu.is_abnormal,cbu.shop_id"
				+ " from custom_benchmark_ready_cloud cbu left join (select id,admName from admuser where status =1) adu "
				+ "on cbu.admin_id = adu.id where cbu.pid=?";
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
					// bean.setWprice(wprice.substring(1, wprice.length() -
					// 1).replace("$", "@"));
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
				bean.setShopId(rs.getString("shop_id"));
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
	public int saveEditDetalis(CustomGoodsPublish cgp, String adminName, int adminId, int type) {

		Connection conn = DBHelper.getInstance().getConnection();
		String upSql = "update custom_benchmark_ready_cloud set enname = ?,feeprice = ?,"
				+ "img = ?,endetail = ?,eninfo=?,goodsstate=?,keyword=?,";
		if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight()) || "0".equals(cgp.getReviseWeight()))) {
			upSql += "feeprice=?,revise_weight=?,final_weight=?,";
		}
		if (cgp.getRangePrice() == null || "".equals(cgp.getRangePrice())) {
			upSql += "price=?,wprice=?,range_price= null,";
		} else {
			upSql += "range_price=?,price=?,";//,sku=?
		}
		if (cgp.getValid() == 1) {
			upSql += "valid=?,";
		}
		if(StringUtils.isNotBlank(cgp.getSku())){//不管什么时候都可以修改sku的价格
			upSql += "sku=?,";
		}
		
		upSql += "admin=?,admin_id=?,updatetime=sysdate(),bm_flag=1,is_edited='2' where pid = ? ";
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
				stmt.setString(i++, cgp.getPrice());
			}
			if (cgp.getValid() == 1) {
				stmt.setInt(i++, cgp.getValid());
			}
			if(StringUtils.isNotBlank(cgp.getSku())){
				stmt.setString(i++, cgp.getSku());
				}
			stmt.setString(i++, adminName);
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
	public GoodsPictureQuantity queryPictureQuantityByPid(String pid) {
		String sql = "select id,pid,imgSize,typeSize,infoSize,img_original_size,"
				+ "type_original_size,info_original_size from custom_img_size where pid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		GoodsPictureQuantity quantity = new GoodsPictureQuantity();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				quantity.setId(rs.getInt("id"));
				quantity.setPid(rs.getString("pid"));
				quantity.setImgSize(rs.getInt("imgSize"));
				quantity.setImgOriginalSize(rs.getInt("img_original_size"));
				quantity.setTypeSize(rs.getInt("typeSize"));
				quantity.setTypeOriginalSize(rs.getInt("type_original_size"));
				quantity.setInfoSize(rs.getInt("infoSize"));
				quantity.setInfoOriginalSize(rs.getInt("info_original_size"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryPictureQuantityByPid error :" + e.getMessage());
			LOG.error("queryPictureQuantityByPid error :" + e.getMessage());
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
		return quantity;
	}

	@Override
	public int setGoodsValid(String pid, String adminName, int adminId, int type) {

		Connection conn = DBHelper.getInstance().getConnection();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		String upSql = "update custom_benchmark_ready_cloud set valid=?,admin=?,goodsstate=?,admin_id=?,"
				+ "updatetime=sysdate(),publishtime=sysdate()  where pid = ? ";
		PreparedStatement stmt = null;
		PreparedStatement remoteStmt = null;
		String upRemoteSql = "update custom_benchmark_ready_cloud set valid=?,goodsstate=? where pid = ?";
		int rs = 0;
		try {

			remoteConn.setAutoCommit(false);
			conn.setAutoCommit(false);
			remoteStmt = remoteConn.prepareStatement(upRemoteSql);
			remoteStmt.setInt(1, type == 1 ? 1 : 0);
			remoteStmt.setInt(2, type == 1 ? 4 : 2);
			remoteStmt.setString(3, pid);

			rs = remoteStmt.executeUpdate();
			if (rs > 0) {
				rs = 0;
				stmt = conn.prepareStatement(upSql);
				// type为-1 下架该商品 1 检查通过
				stmt.setInt(1, type == 1 ? 1 : 0);
				stmt.setString(2, adminName);
				stmt.setInt(3, type == 1 ? 4 : 2);
				stmt.setInt(4, adminId);
				stmt.setString(5, pid);

				rs = stmt.executeUpdate();
				if (rs > 0) {
					remoteConn.commit();
					conn.commit();
				} else {
					remoteConn.rollback();
					conn.rollback();
				}
			} else {
				remoteConn.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pid:" + pid + " setGoodsValid error :" + e.getMessage());
			LOG.error("pid:" + pid + " setGoodsValid error :" + e.getMessage());
		} finally {
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
	public boolean batchDeletePids(String[] pidLst) {

		Connection conn = DBHelper.getInstance().getConnection();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		String deSql = "delete from custom_benchmark_ready_cloud where pid = ?";
		PreparedStatement stmt = null;
		PreparedStatement remoteStmt = null;
		String deRemoteSql = "delete from custom_benchmark_ready_cloud where pid = ?";
		boolean rs = false;
		try {
			stmt = conn.prepareStatement(deSql);
			remoteStmt = remoteConn.prepareStatement(deRemoteSql);
			for (int i = 0; i < pidLst.length; i++) {
				stmt.setString(1, pidLst[i]);
				stmt.addBatch();
				remoteStmt.setString(1, pidLst[i]);
				remoteStmt.addBatch();
			}

			int[] countArr = stmt.executeBatch();
			if (countArr.length > 0) {
				return remoteStmt.executeBatch().length > 0;
			} else {
				return rs;
			}

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
	public int updateGoodsState(String pid, int goodsState) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String updateSql = "update custom_benchmark_ready_cloud set goodsstate =? where pid = ?";
		int count = 0;
		try {
			stmt = conn.prepareStatement(updateSql);

			stmt.setInt(1, goodsState);
			stmt.setString(2, pid);
			count = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateGoodsState error :" + e.getMessage());
			LOG.error("updateGoodsState error :" + e.getMessage());
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
		return count;
	}

	@Override
	public int publishTo28(CustomGoodsPublish bean) {

		String upsql = "update custom_benchmark_ready_cloud_newest set valid=1,keyword=?,eninfo=?,enname=?,"
				+ "weight=?,img=?,endetail=?,feeprice=?,revise_weight=?,final_weight=?, "
				+ "price=?,wprice=?,range_price=?,sku=?,createtime=now(),bm_flag=1,goodsstate=4";
		if (bean.getIsEdited() == 1) {
			upsql += ",finalName=?";
		} else if (bean.getIsEdited() == 2) {
			upsql += ",finalName=?,infoReviseFlag=?,priceReviseFlag=?";
		}
		upsql += ",is_show_det_img_flag=?";
		upsql += " where pid=?";
		Connection conn = DBHelper.getInstance().getConnection8();
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
			} else {
				stmt2.setInt(i++, 0);
			}
			stmt2.setString(i++, bean.getPid());
			result = stmt2.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pid:" + bean.getPid() + " publishTo28 error :" + e.getMessage());
			LOG.error("pid:" + bean.getPid() + " publishTo28 error :" + e.getMessage());
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
	public int updateBmFlagByPids(String[] pidLst, int adminid) {

		Connection conn = DBHelper.getInstance().getConnection();
		String upSql = "update custom_benchmark_ready_cloud set bm_flag =1,updatetime=sysdate() where pid = ?";
		PreparedStatement stmt = null;
		int rsCount = 0;
		try {
			stmt = conn.prepareStatement(upSql);
			for (int i = 0; i < pidLst.length; i++) {
				stmt.setString(1, pidLst[i]);
				stmt.addBatch();
			}
			int[] countArr = stmt.executeBatch();
			rsCount = countArr.length;
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
		return rsCount;
	}

	@Override
	public ShopManagerPojo queryByShopId(String shopId) {

		Connection conn = DBHelper.getInstance().getConnection();
		String queySql = "select id,shop_id,shop_name,shop_url,admuser from shop_manager where shop_id =? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ShopManagerPojo spmg = new ShopManagerPojo();
		try {
			stmt = conn.prepareStatement(queySql);
			stmt.setString(1, shopId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				spmg.setId(rs.getInt("id"));
				spmg.setShop_id(rs.getString("shop_id"));
				spmg.setShop_name(rs.getString("shop_name"));
				spmg.setShop_url(rs.getString("shop_url"));
				spmg.setAdmuser(rs.getString("admuser"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryByShopId error :" + e.getMessage());
			LOG.error("queryByShopId error :" + e.getMessage());
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
		return spmg;
	}

	@Override
	public boolean batchInsertSimilarGoods(String mainPid, String similarPids, int adminId,List<String> existPids) {

		Connection remoteConn = DBHelper.getInstance().getConnection2();
		Connection conn = DBHelper.getInstance().getConnection();
		String insertSql = "insert into similar_goods_relation(main_pid,similar_pid,admin_id) values(?,?,?)";
		PreparedStatement stmt = null;
		PreparedStatement remoteStmt = null;
		int count = 0;
		int rs = 0;
		try {
			
			String[] similarPidList = similarPids.split(";");

			conn.setAutoCommit(false);
			remoteConn.setAutoCommit(false);
			stmt = conn.prepareStatement(insertSql);
			remoteStmt = remoteConn.prepareStatement(insertSql);

			for (String similarpid : similarPidList) {
				if (similarpid == null || "".equals(similarpid)) {
					continue;
				} else if(existPids.contains(similarpid)){
					continue;
				}else{
					count++;
					stmt.setString(1, mainPid);
					stmt.setString(2, similarpid);
					stmt.setInt(3, adminId);
					stmt.addBatch();
					remoteStmt.setString(1, mainPid);
					remoteStmt.setString(2, similarpid);
					remoteStmt.setInt(3, adminId);
					remoteStmt.addBatch();
				}
			}
			rs = remoteStmt.executeBatch().length;

			if (rs == count) {
				rs = 0;
				rs = stmt.executeBatch().length;
				if (rs == count) {
					remoteConn.commit();
					conn.commit();
				} else {
					remoteConn.rollback();
					conn.rollback();
				}
			} else {
				remoteConn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("main_pid:" + mainPid + " batchInsertSimilarGoods error :" + e.getMessage());
			LOG.error("main_pid:" + mainPid + " batchInsertSimilarGoods error :" + e.getMessage());
			try {
				remoteConn.rollback();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(remoteConn);
		}
		return rs > 0 && rs == count;
	}

	@Override
	public List<SimilarGoods> querySimilarGoodsByMainPid(String mainPid) {

		Connection conn = DBHelper.getInstance().getConnection();
		String querySql = "select sgr.id,sgr.main_pid,sgr.similar_pid,sgr.admin_id,sgr.create_time,cbr.custom_main_image,cbr.remotpath "
				+ " from similar_goods_relation sgr left join custom_benchmark_ready_cloud cbr on cbr.pid = sgr.similar_pid"
				+ " where sgr.main_pid =? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<SimilarGoods> list = new ArrayList<SimilarGoods>();

		try {
			stmt = conn.prepareStatement(querySql);
			stmt.setString(1, mainPid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				SimilarGoods goods = new SimilarGoods();
				goods.setId(rs.getInt("id"));
				goods.setMainPid(rs.getString("main_pid"));
				goods.setSimilarPid(rs.getString("similar_pid"));
				String remotpath = rs.getString("remotpath");
				if (remotpath == null || "".equals(remotpath)) {
					goods.setSimilarGoodsImg(rs.getString("custom_main_image"));
				} else {
					goods.setSimilarGoodsImg(remotpath + rs.getString("custom_main_image"));
				}
				goods.setAdminId(rs.getInt("admin_id"));
				goods.setCreateTime(rs.getString("create_time"));
				list.add(goods);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("queryByShopId error :" + e.getMessage());
			LOG.error("queryByShopId error :" + e.getMessage());
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
	public List<String> queryOffShelfPids() {
		
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String querySql = "select pid from needoffshelf where update_flag < 2 "
				+ "and operateCount < 10 limit 300 ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> pids = new ArrayList<String>();

		try {
			stmt = conn28.prepareStatement(querySql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				pids.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("queryByShopId error :" + e.getMessage());
			LOG.error("queryByShopId error :" + e.getMessage());
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
			DBHelper.getInstance().closeConnection(conn28);
		}
		return pids;
	
	}

	@Override
	public void updateOffShelfByPid(String pid,int flag) {
		
		Connection conn28 = DBHelper.getInstance().getConnection5();
		String updateSql = "update needoffshelf set operateCount = operateCount +1 ,"
				+ "update_flag =?,update_time =sysdate() where pid = ? ";
		PreparedStatement stmt = null;

		try {
			stmt = conn28.prepareStatement(updateSql);
			stmt.setInt(1, flag);
			stmt.setString(2, pid);
			int rs= stmt.executeUpdate();
			if(rs > 0){
				System.out.println("pid:" + pid + " updateOffShelfByPid success!");
			}else{
				System.err.println("pid:" + pid + " updateOffShelfByPid error");
				LOG.error("pid:" + pid + " updateOffShelfByPid error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("pid:" + pid + " updateOffShelfByPid error :" + e.getMessage());
			LOG.error("pid:" + pid + " updateOffShelfByPid error :" + e.getMessage());
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
	}

}
