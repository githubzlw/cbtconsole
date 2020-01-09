package com.cbt.dao.impl;

import com.cbt.bean.*;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Md5Util;
import com.cbt.website.bean.OrderProductSourceLogBean;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.CustomBenchmarkSkuNew;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.pojo.ShopMd5Bean;
import com.importExpress.pojo.SkuValPO;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.UpdateTblModel;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class CustomGoodsDaoImpl implements CustomGoodsDao {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CustomGoodsDaoImpl.class);

    @Override
    public List<CategoryBean> getCaterory() {
        List<CategoryBean> list = new ArrayList<CategoryBean>();
        String sql = "select  cbm.catid1,cat1688.name,count(cbm.pid) as total from custom_benchmark_ready cbm,1688_category cat1688 "
                + "where cbm.catid1=cat1688.category_id and cbm.catid1 !='0' group by cbm.catid1 order by cat1688.name asc";

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CategoryBean bean = new CategoryBean();
                bean.setCategoryName(rs.getString("name"));
                bean.setCid(rs.getString("catid1"));
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
        String sql = "select cat1688.*,ifnull(cbm.total,0) as total from (select category_id as cid,name as category,"
                + "path,lv from 1688_category  where lv > 0) cat1688  left join "
                + "(select catid1,count(pid) as total  from custom_benchmark_ready where 1=1 ";

        if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
            sql = sql + " and publishtime >= ?";
        }
        if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
            sql = sql + " and publishtime <= ?";
        }
        if (queryBean.getState() > 0) {
            sql = sql + " and goodsstate = ?";
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
        if (queryBean.getWeightCheck() > -1) {
            if (queryBean.getWeightCheck() == 0 || queryBean.getWeightCheck() < 6) {
                sql = sql + " and weight_check = " + queryBean.getWeightCheck();
            } else if (queryBean.getWeightCheck() % 2 == 0) {
                sql = sql + " and( weight_check = 10 or weight_check = 2 )";
            } else if (queryBean.getWeightCheck() % 3 == 0) {
                sql = sql + " and( weight_check = 15 or weight_check = 3 )";
            } else if (queryBean.getWeightCheck() % 5 == 0) {
                sql = sql + " and( weight_check = 5 or weight_check =10 or weight_check =15)";
            }
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
        if (queryBean.getSourceUsedFlag() > -1) {
            sql = sql + " and source_used_flag=? ";
        }
        if (queryBean.getOcrMatchFlag() > 0) {
            sql = sql + " and ocr_match_flag=? ";
        }
        if (queryBean.getRebidFlag() > 0) {
            sql = sql + " and rebid_flag=? ";
        }
        if (queryBean.getInfringingFlag() > -1) {
            sql = sql + " and infringing_flag=? ";
        }

        sql += " group by catid1) cbm on cat1688.cid=cbm.catid1" + " order by cat1688.lv,cat1688.category asc ";

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
            if (queryBean.getState() > 0) {
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
            if (queryBean.getSourceUsedFlag() > -1) {
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
            if (queryBean.getInfringingFlag() > -1) {
                stmt.setInt(index, queryBean.getInfringingFlag());
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
    public List<CategoryBean> queryStaticizeCateroryByParam() {

        List<CategoryBean> list = new ArrayList<CategoryBean>();
        String sql = "select cat1688.*,ifnull(cbm.total,0) as total from (select category_id as cid,name as category,"
                + "path,lv from 1688_category  where lv > 0) cat1688  left join "
                + "(select catid1,count(pid) as total  from staticize_goods_html_sel where catid1 is not null  "
                + " group by catid1) cbm on cat1688.cid=cbm.catid1 order by cat1688.lv,cat1688.category asc ";
        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        System.err.println(sql);
        try {
            stmt = conn.prepareStatement(sql);
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

        String sql = "select  sql_calc_found_rows keyword,catid1,img,pid,localpath,"
                + "remotpath,url,enname,valid,publishtime,admin,goodsstate,updatetime "
                + "from custom_goods where 1=1  ";
        if (catid != null && !catid.isEmpty()) {
            sql = sql + " and catid1 = ?";
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
                bean.setCatid(rs.getString("catid1"));
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
        String sql = "select * from custom_benchmark_ready where pid=? limit 1";
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
                bean.setShowMainImage(rs.getString("custom_main_image"));
                bean.setSellUnit(rs.getString("sellunit"));
                bean.setAliGoodsPid(rs.getString("ali_pid"));
                bean.setAliGoodsPrice(rs.getString("ali_price"));
                bean.setMatchSource(rs.getInt("matchSource"));
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
        String sql = "select pid,eninfo from custom_goods where catid1=?";
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
    public int publish(CustomGoodsPublish bean,int isOnline) {

        String upsql = "update custom_benchmark_ready set valid=1,keyword=?,eninfo=?,enname=?,"
                + "weight=?,img=?,endetail=?,revise_weight=?,final_weight=?, "
                + "price=?,wprice=?,range_price=?,sku=?,is_show_det_img_flag = ?,cur_time=now(),bm_flag=1,goodsstate=4";
        if (bean.getIsEdited() == 1) {
            upsql += ",finalName=?";
        } else if (bean.getIsEdited() == 2) {
            upsql += ",finalName=?,infoReviseFlag=?,priceReviseFlag=?";
        }
        if (StringUtils.isNotBlank(bean.getFeeprice())) {
            upsql += ",feeprice=?";
        }
        upsql += ",is_show_det_img_flag=?,entype=?,sellunit=?";
        upsql += ",ali_pid=?,ali_price=?,matchSource=?";
        upsql += " where pid=?";
        Connection conn = null;
        conn = DBHelper.getInstance().getConnection();
        String upLocalSql = "update custom_benchmark_ready set img=?,eninfo=?,custom_main_image=?,is_show_det_img_flag = ?,unsellableReason=45 where pid = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        int result = 0;
        try {
            if (isOnline == 1) {
                int i = 1;
                stmt2 = conn.prepareStatement(upsql);
                stmt2.setString(i++, bean.getKeyword());
                stmt2.setString(i++, bean.getEninfo());
                stmt2.setString(i++, bean.getEnname());
                stmt2.setString(i++, bean.getWeight());
                stmt2.setString(i++, bean.getImg());
                stmt2.setString(i++, bean.getEndetail());

                stmt2.setString(i++, bean.getReviseWeight());
                stmt2.setString(i++, bean.getFinalWeight());
                stmt2.setString(i++, bean.getPrice());
                stmt2.setString(i++, bean.getWprice());
                stmt2.setString(i++, bean.getRangePrice());
                stmt2.setString(i++, bean.getSku());
                stmt2.setInt(i++, bean.getIsShowDetImgFlag());
                if (bean.getIsEdited() == 1) {
                    stmt2.setString(i++, bean.getEnname());
                } else if (bean.getIsEdited() == 2) {
                    stmt2.setString(i++, bean.getEnname());
                    stmt2.setInt(i++, 1);
                    stmt2.setInt(i++, 1);
                }

                if (StringUtils.isNotBlank(bean.getFeeprice())) {
                    stmt2.setString(i++, bean.getFeeprice());
                }
                if (!(bean.getEninfo() == null || "".equals(bean.getEninfo()) || bean.getEninfo().length() < 10)) {
                    stmt2.setInt(i++, 1);
                } else {
                    stmt2.setInt(i++, 0);
                }
                stmt2.setString(i++, bean.getEntype());
                stmt2.setString(i++, bean.getSellUnit());
                stmt2.setString(i++, bean.getAliGoodsPid());
                stmt2.setString(i++, bean.getAliGoodsPrice());
                stmt2.setInt(i++, bean.getMatchSource());
                stmt2.setString(i++, bean.getPid());
                result = stmt2.executeUpdate();
            } else {
                stmt = conn.prepareStatement(upLocalSql);
                stmt.setString(1, bean.getImg());
                stmt.setString(2, bean.getEninfo());
                stmt.setString(3, bean.getShowMainImage().replace(bean.getRemotpath(),""));
                stmt.setInt(4, bean.getIsShowDetImgFlag());
                stmt.setString(5, bean.getPid());
                result = stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("publish error :" + e.getMessage());
            LOG.error("publish error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closePreparedStatement(stmt2);
            DBHelper.getInstance().closeResultSet(rs);
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

        //线上没有当前表custom_goods，该方法应该不需要
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
        String sql = "update custom_goods_edit a,custom_benchmark_ready b set b.goodsstate=?,a.admin_id=?,b.valid=1";
        if (state == 4) {
            sql += ",b.bm_flag=1,a.is_edited=1,a.publish_time=now()";
        }else if(state == 2){
            sql += ",a.off_time=now()";
        }
        sql += " where a.pid = b.pid and b.pid =? ";
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
    public boolean updateStateList(int state, String pids, int adminid, String reason) {
        Connection conn = DBHelper.getInstance().getConnection();
        // Connection remoteConn = DBHelper.getInstance().getConnection2();
        Connection conn28 = DBHelper.getInstance().getConnection8();
        PreparedStatement stmt = null;
        // PreparedStatement remoteStmt = null;
        PreparedStatement stmt28 = null;

        String upSql = "update custom_goods_edit a,custom_benchmark_ready b set b.valid=?,b.goodsstate=?,a.admin_id=?";
        if (state == 4) {
            upSql += ",a.publish_time=now()";
        }else if(state == 2){
            upSql += ",a.off_time=now(),a.off_reason=?,b.unsellableReason = 6";
        }
        upSql += " where a.pid = b.pid and b.pid =? ";
        // String upRemoteSql = "update custom_benchmark_ready set valid=?,goodsstate=?,cur_time = NOW() where pid = ?";
        String up28Sql = "update custom_benchmark_ready_newest set valid=?,goodsstate=?,cur_time = NOW() where pid = ?";

        int rs = 0;
        int count = 0;

        try {
            String[] pidList = pids.split(",");
            conn.setAutoCommit(false);
            // remoteConn.setAutoCommit(false);
            conn28.setAutoCommit(false);

            stmt = conn.prepareStatement(upSql);
            // remoteStmt = remoteConn.prepareStatement(upRemoteSql);
            stmt28 = conn28.prepareStatement(up28Sql);

            for (String pid : pidList) {
                if (pid == null || "".equals(pid)) {
                    continue;
                } else {
                    count++;
//                    remoteStmt.setInt(1, state == 4 ? 1 : 0);
//                    remoteStmt.setInt(2, state);
//                    remoteStmt.setString(3, pid);
//                    remoteStmt.addBatch();
                    stmt.setInt(1, state == 4 ? 1 : 0);
                    stmt.setInt(2, state);
                    stmt.setInt(3, adminid);
                    if(state == 2){
                        stmt.setString(4, reason);
                        stmt.setString(5, pid);
                    }else{
                        stmt.setString(4, pid);
                    }
                    stmt.addBatch();

                    stmt28.setInt(1, state == 4 ? 1 : 0);
                    stmt28.setInt(2, state);
                    stmt28.setString(3, pid);
                    stmt28.addBatch();
                }
            }
            /*rs = remoteStmt.executeBatch().length;
            if (rs == count) {
                rs = 0;
                rs = stmt.executeBatch().length;
                if (rs == count) {
                    rs = 0;
                    rs = stmt28.executeBatch().length;
                    if (rs == count) {
                        remoteConn.commit();
                        conn.commit();
                        conn28.commit();
                    } else {
                        remoteConn.rollback();
                        conn.rollback();
                        conn28.rollback();
                    }
                } else {
                    remoteConn.rollback();
                    conn.rollback();
                }
            } else {
                remoteConn.rollback();
            }*/


            rs = stmt.executeBatch().length;
            if (rs == count) {
                rs = 0;
                rs = stmt28.executeBatch().length;
                if (rs == count) {
                    conn.commit();
                    conn28.commit();
                } else {
                    conn.rollback();
                    conn28.rollback();
                }
            } else {
                conn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updateStateList error :" + e.getMessage());
            LOG.error("updateStateList error :" + e.getMessage());
            try {
                //remoteConn.rollback();
                conn.rollback();
                conn28.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            // DBHelper.getInstance().closePreparedStatement(remoteStmt);
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn);
            // DBHelper.getInstance().closeConnection(remoteConn);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return rs == count;
    }

    @Override
    public int updateValid(int valid, String pid) {
        String sql = "update custom_goods set valid=? where pid=? ";
//        Connection conn = DBHelper.getInstance().getConnection2();
        int rs = 0;
        PreparedStatement stmt = null;
        try {
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, valid);
//            stmt.setString(2, pid);
//            rs = stmt.executeUpdate();

            List<String> lstValues = new ArrayList<String>();
            lstValues.add(String.valueOf(valid));
            lstValues.add(String.valueOf(pid));

            String runSql = DBHelper.covertToSQL(sql,lstValues);
            rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updateValid error :" + e.getMessage());
            LOG.error("updateValid error :" + e.getMessage());
        } finally {
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            DBHelper.getInstance().closeConnection(conn);
        }
        return rs;
    }

    @Override
    public int updateValidList(int valid, String pids) {
        String sql = "update custom_benchmark_ready set valid=? and goodsstate =? where pid in ( " + pids + ")";
        int rs = 0;
        try {
            List<String> lstValues = new ArrayList<>();
            lstValues.add(String.valueOf(valid));
            lstValues.add(String.valueOf(valid == 0 ? 2 : 4));

            String runSql = DBHelper.covertToSQL(sql,lstValues);
            rs=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updateValidList error :" + e.getMessage());
            LOG.error("updateValidList error :" + e.getMessage());
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

        String sqlo = "select cbr.*,c8gd.* "
                + " from (select cbu.keyword,cbu.catid1,cbu.pid,cbu.enname,cbu.localpath,cbu.remotpath,cbu.goodsstate,cbu.custom_main_image,"
                + "cbu.bm_flag,cbu.valid,cbu.isBenchmark,cbu.source_pro_flag,cbu.source_used_flag,cbu.ocr_match_flag,cbu.infringing_flag,"
                + "cbu.priority_flag,cbu.rebid_flag,cbu.updatetime,cbu.publishtime,adu.admName as admin,ifnull(adu.id,0) as admin_id,"
                + "cbu.is_edited,cbu.is_sold_flag,cbu.is_add_car_flag,cbu.is_abnormal"
                + " from custom_benchmark_ready cbu left join (select id,admName from admuser where status =1) adu "
                + "on cbu.admin_id = adu.id where 1=1  ";
        String sqlb = ") cbr left join  (select pid,name,img,ali_name,ali_img,ali_pid "
                + "from custom_1688_goods where pid in(select pid from custom_benchmark_ready where 1=1  ";
        String sqle = ")) c8gd on cbr.pid = c8gd.pid where 1=1";
        if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
            sqlo = sqlo + " and cbu.catid1  in(select category_id from 1688_category  where find_in_set(?,path))";
            sqlb = sqlb + " and catid1  in(select category_id from 1688_category  where find_in_set(?,path))";
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
        if (queryBean.getWeightCheck() > -1) {
            if (queryBean.getWeightCheck() == 0 || queryBean.getWeightCheck() < 6) {
                sqlo = sqlo + " and cbu.weight_check = " + queryBean.getWeightCheck();
                sqlb = sqlb + " and weight_check = " + queryBean.getWeightCheck();
            } else if (queryBean.getWeightCheck() % 2 == 0) {
                sqlo = sqlo + " and ( cbu.weight_check = 10 or cbu.weight_check = 2 )";
                sqlb = sqlb + " and ( weight_check = 10 or weight_check = 2 )";
            } else if (queryBean.getWeightCheck() % 3 == 0) {
                sqlo = sqlo + " and ( cbu.weight_check = 15 or cbu.weight_check = 3 )";
                sqlb = sqlb + " and ( weight_check = 15 or weight_check = 3 )";
            } else if (queryBean.getWeightCheck() % 5 == 0) {
                sqlo = sqlo + " and ( cbu.weight_check = 15 or cbu.weight_check = 10 or cbu.weight_check = 5 )";
                sqlb = sqlb + " and ( weight_check = 15 or weight_check = 10 or weight_check = 5 )";
            }

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
        if (queryBean.getSourceUsedFlag() > -1) {
            sqlo = sqlo + " and cbu.source_used_flag = ?";
            sqlb = sqlb + " and source_used_flag = ?";
        }
        if (queryBean.getOcrMatchFlag() > 0) {
            sqlo = sqlo + " and cbu.ocr_match_flag = ?";
            sqlb = sqlb + " and ocr_match_flag = ?";
        }
        if (queryBean.getRebidFlag() > 0) {
            sqlo = sqlo + " and cbu.rebid_flag = ?";
            sqlb = sqlb + " and rebid_flag = ?";
        }
        if (queryBean.getInfringingFlag() > -1) {
            sqlo = sqlo + " and cbu.infringing_flag = ?";
            sqlb = sqlb + " and infringing_flag = ?";
        }

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
            if (queryBean.getSourceUsedFlag() > -1) {
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
            if (queryBean.getInfringingFlag() > -1) {
                stmt.setInt(index, queryBean.getInfringingFlag());
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
            if (queryBean.getSourceUsedFlag() > -1) {
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
            if (queryBean.getInfringingFlag() > -1) {
                stmt.setInt(index, queryBean.getInfringingFlag());
                index++;
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                CustomGoodsPublish bean = new CustomGoodsPublish();
                bean.setKeyword(rs.getString("keyword"));
                bean.setCatid(rs.getString("catid1"));
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
                bean.setRebidFlag(rs.getInt("rebid_flag"));
                bean.setInfringingFlag(rs.getInt("infringing_flag"));

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

        String sql = "select count(pid) from (select pid from custom_benchmark_ready where 1=1  ";
        if (!(queryBean.getCatid() == null || queryBean.getCatid().isEmpty() || "0".equals(queryBean.getCatid()))) {
            System.err.println("catid:" + queryBean.getCatid());
            sql = sql + " and catid1 in(select category_id from 1688_category  where find_in_set(?,path))";
        }
        if (queryBean.getSttime() != null && !queryBean.getSttime().isEmpty()) {
            sql = sql + " and publishtime > ?";
        }
        if (queryBean.getEdtime() != null && !queryBean.getEdtime().isEmpty()) {
            sql = sql + " and publishtime < ?";
        }

        if (queryBean.getState() > 0) {
            sql = sql + " and goodsstate = ?";
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

        if (queryBean.getWeightCheck() > -1) {
            if (queryBean.getWeightCheck() == 0 || queryBean.getWeightCheck() < 6) {
                sql = sql + " and weight_check = " + queryBean.getWeightCheck();
            } else if (queryBean.getWeightCheck() % 2 == 0) {
                sql = sql + " and( weight_check = 10 or weight_check = 2 )";
            } else if (queryBean.getWeightCheck() % 3 == 0) {
                sql = sql + " and( weight_check = 15 or weight_check = 3 )";
            } else if (queryBean.getWeightCheck() % 5 == 0) {
                sql = sql + " and( weight_check = 5 or weight_check =10 or weight_check =15)";
            }
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
        if (queryBean.getSourceUsedFlag() > -1) {
            sql = sql + " and source_used_flag=? ";
        }
        if (queryBean.getOcrMatchFlag() > 0) {
            sql = sql + " and ocr_match_flag=? ";
        }
        if (queryBean.getRebidFlag() > 0) {
            sql = sql + " and rebid_flag=? ";
        }
        if (queryBean.getInfringingFlag() > -1) {
            sql = sql + " and infringing_flag=? ";
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
            if (queryBean.getSourceUsedFlag() > -1) {
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
            if (queryBean.getInfringingFlag() > -1) {
                stmt.setInt(index, queryBean.getInfringingFlag());
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
    public int queryStaticizeGoodsInfosCount() {
        String sql = "SELECT COUNT(1) FROM staticize_goods_html_sel where catid1 is not null ";

        Connection conn = DBHelper.getInstance().getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            stmt = conn.prepareStatement(sql);
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
        String btSql = "update custom_goods_edit a,custom_benchmark_ready b set b.enname = ?,b.goodsstate=?,a.admin_id=?,"
                + "b.is_edited='1',a.update_count = a.update_count + 1 where a.pid = b.pid and b.pid = ? ";
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

        String sql = "select ifnull(cbn.ocrcontent1,'') as ocrSizeInfo1,ifnull(cbn.ocrcontent2,'') as ocrSizeInfo2,ifnull(cbn.ocrcontent3,'') as ocrSizeInfo3,"
                + "cbu.pid,cbu.enname,cbu.endetail,cbu.eninfo,cbu.feeprice,cbu.fprice,"
                + "cbu.keyword,cbu.custom_main_image,cbu.price,cbu.final_weight,cbu.wprice,cbu.entype,cbu.sku,"
                + "cbu.goodsstate,cbu.bm_flag,cbu.valid,cbu.isBenchmark,cbu.source_pro_flag,cbu.source_used_flag,cbu.rebid_flag,"
                + "cbu.ocr_match_flag,cbu.priority_flag,cbu.img,cbu.range_price,cbu.revise_weight,cbu.localpath,cbu.remotpath,"
                + "cbu.is_sold_flag,cbu.is_add_car_flag,cbu.ali_pid,cbu.is_edited,cbu.updatetime,adu.admName as admin,"
                + "ifnull(adu.id,0) as admin_id,cbu.is_abnormal,cbu.shop_id,cbu.publishtime,cbu.catid1,cbu.infringing_flag,cbu.sellunit," +
                "cbu.ali_price,cbu.matchSource"
                + " from custom_benchmark_ready cbu left join (select id,admName from admuser where status =1) adu "
                + " on cbu.admin_id = adu.id "
                + " left join needfindsizechart cbn on cbn.pid=cbu.pid "
                + " where cbu.pid=?";
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
                //bean.setFeeprice(rs.getString("feeprice"));
                //bean.setFeeprice(rs.getString("feeprice"));

                String feeprice = rs.getString("feeprice");
                if (StringUtils.isBlank(feeprice)) {
                    bean.setFeeprice("");
                } else {
                    bean.setFeeprice(feeprice.replace("[", "").replace("]", "").replace("$", "@"));
                }

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
                bean.setShowMainImage(rs.getString("custom_main_image"));
                bean.setSku(rs.getString("sku"));
                bean.setEntype(rs.getString("entype"));
                bean.setRemotpath(rs.getString("remotpath"));

                bean.setOcrSizeInfo1(rs.getString("ocrSizeInfo1"));
                bean.setOcrSizeInfo2(rs.getString("ocrSizeInfo2"));
                bean.setOcrSizeInfo3(rs.getString("ocrSizeInfo3"));

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
                bean.setAliGoodsPrice(rs.getString("ali_price"));
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
                bean.setRebidFlag(rs.getInt("rebid_flag"));
                bean.setShopId(rs.getString("shop_id"));

                bean.setPublishtime(rs.getString("publishtime") == null ? "" : rs.getString("publishtime"));
                bean.setCatid1(rs.getString("catid1"));
                bean.setInfringingFlag(rs.getInt("infringing_flag"));
                bean.setSellUnit(rs.getString("sellunit"));
                bean.setMatchSource(rs.getInt("matchSource"));
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
        String upSql = "update custom_benchmark_ready set enname = ?,"
                + "img = ?,endetail = ?,eninfo=?,goodsstate=?,keyword=?,";
        if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight()) || "0".equals(cgp.getReviseWeight()))) {
            upSql += "revise_weight=?,final_weight=?,";
        }
        if (StringUtils.isNotBlank(cgp.getRangePrice())) {
            upSql += "range_price=?,sku=?,";
        } else if (StringUtils.isNotBlank(cgp.getFeeprice())) {
            upSql += "price=?,feeprice=?,";
        } else if (StringUtils.isNotBlank(cgp.getWprice())) {
            upSql += "price=?,wprice=?,";
        }
        if (StringUtils.isNotBlank(cgp.getType())) {
            upSql += "entype=?,";
        }
        if (StringUtils.isNotBlank(cgp.getSellUnit())) {
            upSql += "sellunit=?,";
        }
        if (adminId != 63) {
            upSql += "admin=?,admin_id=?,";
        }
        upSql += "updatetime=sysdate(),bm_flag=1,is_edited='2' where pid = ? ";
        PreparedStatement stmt = null;
        int rs = 0;
        int i = 1;
        try {
            stmt = conn.prepareStatement(upSql);
            stmt.setString(i++, cgp.getEnname());
            //stmt.setString(i++, cgp.getFeeprice());
            stmt.setString(i++, cgp.getImg());
            stmt.setString(i++, cgp.getEndetail());
            stmt.setString(i++, cgp.getEninfo());
            // type为1需要发布的，否则是待发布
            stmt.setInt(i++, type == 1 ? 4 : 5);
            stmt.setString(i++, cgp.getKeyword());
            if (!(cgp.getReviseWeight() == null || "".equals(cgp.getReviseWeight())
                    || "0".equals(cgp.getReviseWeight()))) {
                //stmt.setString(i++, cgp.getFeeprice());
                stmt.setString(i++, cgp.getReviseWeight());
                stmt.setString(i++, cgp.getReviseWeight());
            }

            if (StringUtils.isNotBlank(cgp.getRangePrice())) {
                stmt.setString(i++, cgp.getRangePrice());
                stmt.setString(i++, cgp.getSku());
            } else if (StringUtils.isNotBlank(cgp.getFeeprice())) {
                stmt.setString(i++, cgp.getPrice());
                stmt.setString(i++, cgp.getFeeprice());
            } else if (StringUtils.isNotBlank(cgp.getWprice())) {
                stmt.setString(i++, cgp.getPrice());
                stmt.setString(i++, cgp.getWprice());
            }

            if (StringUtils.isNotBlank(cgp.getType())) {
                stmt.setString(i++, cgp.getType());
            }
            if (StringUtils.isNotBlank(cgp.getSellUnit())) {
                stmt.setString(i++, cgp.getSellUnit());
            }
            if (adminId != 63) {
                stmt.setString(i++, adminName);
                stmt.setInt(i++, adminId);
            }
            stmt.setString(i++, cgp.getPid());
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("saveEditDetalis error :" + e.getMessage());
            LOG.error("saveEditDetalis error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
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
    public int setGoodsValid(String pid, String adminName, int adminId, int type, int reason,String remark) {

        Connection conn = DBHelper.getInstance().getConnection();
        Connection conn28 = DBHelper.getInstance().getConnection8();
        String upSql = "update custom_goods_edit a,custom_benchmark_ready b " +
                "set b.valid=?,b.goodsstate=?,b.unsellableReason = ?,a.admin_id=?,a.update_count=a.update_count + 1 ";
        if (type == 1) {
            upSql += ",a.publish_time=sysdate()";
        } else {
            upSql += ",a.off_time=sysdate(),a.off_reason=?";
        }
        upSql += "  where a.pid = b.pid and b.pid = ? ";
        PreparedStatement stmt = null;
        PreparedStatement stmt28 = null;
        String up28Sql = "update custom_benchmark_ready_newest set valid=?,goodsstate=?,unsellableReason = ?,flag=? where pid = ?";
        int rs = 0;
        try {
            conn.setAutoCommit(false);
            rs = 0;
            stmt = conn.prepareStatement(upSql);
            int count27 = 1;
            // type为-1 下架该商品 1 检查通过
            stmt.setInt(count27++, type == 1 ? 1 : 0);
            stmt.setInt(count27++, type == 1 ? 4 : 2);
            stmt.setInt(count27++, reason);
            stmt.setInt(count27++, adminId);
            if (type != 1) {
                stmt.setString(count27++, remark);
            }
            stmt.setString(count27++, pid);

            rs = stmt.executeUpdate();
            if (rs > 0) {
                conn.commit();
                //rs = 0;
                stmt28 = conn28.prepareStatement(up28Sql);
                stmt28.setInt(1, type == 1 ? 1 : 0);
                stmt28.setInt(2, type == 1 ? 4 : 2);
                stmt28.setInt(3, reason);
                stmt28.setInt(4, (reason > 0 || type == 1) ? 0 : 2);
                stmt28.setString(5, pid);
                //rs = stmt28.executeUpdate();
                stmt28.executeUpdate();
            } else {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + " setGoodsValid error :" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsValid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return rs;
    }

    @Override
    public int setGoodsValid2(String pid, String adminName, int adminId, int type, int reason,String remark) {

        Connection conn = DBHelper.getInstance().getConnection();
        Connection conn28 = DBHelper.getInstance().getConnection8();
        String upSql = "update custom_goods_edit a,custom_benchmark_ready b " +
                "set b.valid=?,b.goodsstate=?,b.unsellableReason = ?,a.admin_id=?,a.update_count=a.update_count + 1 ";
        if (type == 1) {
            upSql += ",a.publish_time=sysdate()";
        } else {
            upSql += ",a.off_time=sysdate(),a.off_reason=?";
        }
        upSql += "  where a.pid = b.pid and b.pid = ? ";
        PreparedStatement stmt = null;
        PreparedStatement stmt28 = null;
        String up28Sql = "update custom_benchmark_ready_newest set valid=?,goodsstate=?,unsellableReason = ?,flag=? where pid = ?";
        int rs = 0;
        try {
            conn.setAutoCommit(false);
            rs = 0;
            stmt = conn.prepareStatement(upSql);
            int count27 = 1;
            // type为-1 下架该商品 1 检查通过
            stmt.setInt(count27++, type == 1 ? 1 : 2);
            stmt.setInt(count27++, type == 1 ? 4 : 2);
            stmt.setInt(count27++, reason);
            stmt.setInt(count27++, adminId);
            if (type < 1) {
                stmt.setString(count27++, remark);
            }
            stmt.setString(count27++, pid);

            rs = stmt.executeUpdate();
            if (rs > 0) {
                conn.commit();
                //rs = 0;
                stmt28 = conn28.prepareStatement(up28Sql);
                stmt28.setInt(1, type == 1 ? 1 : 2);
                stmt28.setInt(2, type == 1 ? 4 : 2);
                stmt28.setInt(3, reason);
                stmt28.setInt(4, (reason > 0 || type == 1) ? 0 : 2);
                stmt28.setString(5, pid);
                //rs = stmt28.executeUpdate();
                stmt28.executeUpdate();
            } else {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + " setGoodsValid error :" + e.getMessage());
            LOG.error("pid:" + pid + " setGoodsValid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return rs;
    }

    @Override
    public boolean batchDeletePids(String[] pidLst) {

        throw new RuntimeException("already cancel method called");
    }

    @Override
    public int updateGoodsState(String pid, int goodsState) {

        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        String updateSql = "update custom_goods_edit a,custom_benchmark_ready b set b.valid = ?, b.goodsstate =?";
        if (goodsState == 4) {
            updateSql += ",a.publish_time =now()";
        }
        updateSql += " where a.pid = b.pid and  a.pid = ?";
        int count = 0;
        try {
            stmt = conn.prepareStatement(updateSql);

            stmt.setInt(1, goodsState == 4 ? 1 : 0);
            stmt.setInt(2, goodsState);
            stmt.setString(3, pid);
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

        String upsql = "update custom_benchmark_ready_newest set valid=1,keyword=?,eninfo=?,enname=?,"
                + "weight=?,img=?,endetail=?,revise_weight=?,final_weight=?, "
                + "price=?,wprice=?,range_price=?,sku=?,cur_time=now(),bm_flag=1,goodsstate=4,fprice_str=?";
        if (bean.getIsEdited() == 1) {
            upsql += ",finalName=?";
        } else if (bean.getIsEdited() == 2) {
            upsql += ",finalName=?,infoReviseFlag=?,priceReviseFlag=?";
        }
        if(StringUtils.isNotBlank(bean.getFeeprice())){
            upsql += ",feeprice=?";
        }
        upsql += ",is_show_det_img_flag=?,entype=?,sellunit=?";
        upsql += ",ali_pid=?,ali_price=?,matchSource=?";
        if(bean.getWeightIsEdit() > 0){
            upsql += ",source_pro_flag = 7 ";
        }
        if(StringUtils.isNotBlank(bean.getSizeInfoEn())){
            upsql += ",size_info_en = ? ";
        }
        upsql += " where pid=?";
        Connection conn = DBHelper.getInstance().getConnection8();
        ResultSet rs = null;
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

            stmt2.setString(i++, bean.getReviseWeight());
            stmt2.setString(i++, bean.getFinalWeight());
            stmt2.setString(i++, bean.getPrice());
            stmt2.setString(i++, bean.getWprice());
            stmt2.setString(i++, bean.getRangePrice());
            stmt2.setString(i++, bean.getSku());
            stmt2.setString(i++, bean.getFpriceStr());
            if (bean.getIsEdited() == 1) {
                stmt2.setString(i++, bean.getEnname());
            } else if (bean.getIsEdited() == 2) {
                stmt2.setString(i++, bean.getEnname());
                stmt2.setInt(i++, 1);
                stmt2.setInt(i++, 1);
            }
            if(StringUtils.isNotBlank(bean.getFeeprice())){
                stmt2.setString(i++, bean.getFeeprice());
            }
            if (!(bean.getEninfo() == null || "".equals(bean.getEninfo()) || bean.getEninfo().length() < 10)) {
                stmt2.setInt(i++, 1);
            } else {
                stmt2.setInt(i++, 0);
            }
            stmt2.setString(i++, bean.getEntype());
            stmt2.setString(i++, bean.getSellUnit());
            stmt2.setString(i++, bean.getAliGoodsPid());
            stmt2.setString(i++, bean.getAliGoodsPrice());
            stmt2.setInt(i++, bean.getMatchSource());
            if(StringUtils.isNotBlank(bean.getSizeInfoEn())){
                stmt2.setString(i++, bean.getSizeInfoEn());
            }
            stmt2.setString(i++, bean.getPid());
            result = stmt2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + bean.getPid() + " publishTo28 error :" + e.getMessage());
            LOG.error("pid:" + bean.getPid() + " publishTo28 error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closePreparedStatement(stmt2);
            DBHelper.getInstance().closeConnection(conn);
        }
        return result;
    }

    @Override
    public int updateBmFlagByPids(String[] pidLst, int adminid) {

        Connection conn = DBHelper.getInstance().getConnection();
        String upSql = "update custom_benchmark_ready set bm_flag =1,updatetime=sysdate() where pid = ?";
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
    public boolean batchInsertSimilarGoods(String mainPid, String similarPids, int adminId, List<String> existPids) {

        //该表没有操作similar_goods_relation 2018-02-09
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
                } else if (existPids.contains(similarpid)) {
                    continue;
                } else {
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
                + " from similar_goods_relation sgr left join custom_benchmark_ready cbr on cbr.pid = sgr.similar_pid"
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
        String querySql = "select pid,reason from needoffshelf where update_flag < 2 " + "and operateCount < 10 ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> pids = new ArrayList<String>();

        try {
            stmt = conn28.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("pid") + "@" + rs.getInt("reason"));
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
	public Set<String> queryHotSellingGoods() {
    	Set<String> pids = new HashSet<String>();
    	Connection conn = DBHelper.getInstance().getConnection2(); //online
        String querySql = "SELECT goods_pid FROM hot_selling_goods"; //热卖区商品
        
        Connection conn2 = DBHelper.getInstance().getConnection(); //27
        String querySql2 = "SELECT pid FROM custom_benchmark_ready WHERE is_edited=3"; //永不下架商品
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("goods_pid"));
            }
            stmt = conn2.prepareStatement(querySql2);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("pid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryByShopId error :" + e.getMessage());
            LOG.error("queryByShopId error :" + e.getMessage());
            throw new RuntimeException("queryHotSellingGoods error");
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
        return pids;
	}
    
    @Override
	public Set<String> queryShelvesGoods() {
    	Set<String> pids = new HashSet<String>();
    	Connection conn = DBHelper.getInstance().getConnection(); //27
        String querySql = "SELECT pid FROM custom_benchmark_ready b WHERE unsellableReason IN(1,2,3,5) AND ( b.path_catid like '311,%'  or b.path_catid like '312,%' or b.path_catid like '10165,%' or b.path_catid like '10166,%' or b.path_catid like '54,%' or b.path_catid like '1038378,%' )"; //精品商品 服装 珠宝 鞋子类
        Connection conn2 = DBHelper.getInstance().getConnection8(); //28
        String querySql2 = "SELECT pid FROM cross_border.custom_benchmark_ready_newest b WHERE unsellableReason IN(1,2,3,5) AND b.shop_id in(SELECT shop_id FROM alidata.shop_url_bak WHERE shop_type = 1) ";//精品店铺
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("pid"));
            }
            stmt = conn2.prepareStatement(querySql2);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("pid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryByShopId error :" + e.getMessage());
            LOG.error("queryByShopId error :" + e.getMessage());
            throw new RuntimeException("queryShelvesGoods error");
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
        return pids;
	}

    @Override
    public void updateOffShelfByPid(String pid, int flag) {

        Connection conn28 = DBHelper.getInstance().getConnection5();
        String updateSql = "update needoffshelf set operateCount = operateCount +1 ,"
                + "update_flag =?,update_time =sysdate() where pid = ? ";
        PreparedStatement stmt = null;

        try {
            stmt = conn28.prepareStatement(updateSql);
            stmt.setInt(1, flag);
            stmt.setString(2, pid);
            int rs = stmt.executeUpdate();
            if (rs > 0) {
                System.out.println("pid:" + pid + " updateOffShelfByPid success!");
            } else {
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

    @Override
    public List<String> queryPermanentGoods() {
        Connection conn = DBHelper.getInstance().getConnection(); //27
        String querySql = "SELECT pid FROM custom_benchmark_ready WHERE is_edited=3";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> pids = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                pids.add(rs.getString("pid"));
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
            DBHelper.getInstance().closeConnection(conn);
        }
        return pids;
    }

    @Override
    public boolean updateCustomBenchmarkSkuNew(String pid, List<CustomBenchmarkSkuNew> insertList) {
        Connection conn27 = DBHelper.getInstance().getConnection();
        // Connection connAws = DBHelper.getInstance().getConnection2();
        Connection conn28 = DBHelper.getInstance().getConnection8();


        int count = 0;
        String updateSql = "update custom_benchmark_sku set wprice = ?, act_sku_cal_price = ?, " +
                "act_sku_multi_currency_cal_price = ?, act_sku_multi_currency_display_price = ?," +
                "sku_cal_price = ?, sku_multi_currency_cal_price = ?,final_weight  = ?," +
                "sku_multi_currency_display_price = ? where pid = ? and sku_prop_ids=?";

        PreparedStatement stmt27 = null;
        PreparedStatement stmt28 = null;
        // PreparedStatement stmtAws = null;

        try {
            conn27.setAutoCommit(false);
            // connAws.setAutoCommit(false);
            conn28.setAutoCommit(false);

            stmt27 = conn27.prepareStatement(updateSql);
            stmt28 = conn28.prepareStatement(updateSql);
            // stmtAws = connAws.prepareStatement(updateSql);
            SkuValPO skuValPO;
            for (CustomBenchmarkSkuNew skuNew : insertList) {
                stmt27.setString(1, skuNew.getWprice());
                skuValPO = skuNew.getSkuVal();
                stmt27.setDouble(2, skuValPO.getActSkuCalPrice());
                stmt27.setDouble(3, skuValPO.getActSkuMultiCurrencyCalPrice());
                stmt27.setDouble(4, skuValPO.getActSkuMultiCurrencyDisplayPrice());
                stmt27.setDouble(5, skuValPO.getSkuCalPrice());
                stmt27.setDouble(6, skuValPO.getSkuMultiCurrencyCalPrice());
                stmt27.setString(7, skuNew.getFinalWeight());
                stmt27.setDouble(8, skuValPO.getActSkuMultiCurrencyDisplayPrice());
                stmt27.setString(9, pid);
                stmt27.setString(10, skuNew.getSkuPropIds());
                stmt27.addBatch();

                stmt28.setString(1, skuNew.getWprice());
                stmt28.setDouble(2, skuValPO.getActSkuCalPrice());
                stmt28.setDouble(3, skuValPO.getActSkuMultiCurrencyCalPrice());
                stmt28.setDouble(4, skuValPO.getActSkuMultiCurrencyDisplayPrice());
                stmt28.setDouble(5, skuValPO.getSkuCalPrice());
                stmt28.setDouble(6, skuValPO.getSkuMultiCurrencyCalPrice());
                stmt28.setString(7, skuNew.getFinalWeight());
                stmt28.setDouble(8, skuValPO.getActSkuMultiCurrencyDisplayPrice());
                stmt28.setString(9, pid);
                stmt28.setString(10, skuNew.getSkuPropIds());
                stmt28.addBatch();

//                stmtAws.setString(1, skuNew.getWprice());
//                stmtAws.setDouble(2, skuValPO.getActSkuCalPrice());
//                stmtAws.setDouble(3, skuValPO.getActSkuMultiCurrencyCalPrice());
//                stmtAws.setDouble(4, skuValPO.getActSkuMultiCurrencyDisplayPrice());
//                stmtAws.setDouble(5, skuValPO.getSkuCalPrice());
//                stmtAws.setDouble(6, skuValPO.getSkuMultiCurrencyCalPrice());
//                stmtAws.setString(7, skuNew.getFinalWeight());
//                stmtAws.setDouble(8, skuValPO.getActSkuMultiCurrencyDisplayPrice());
//                stmtAws.setString(9, pid);
//                stmtAws.setString(10, skuNew.getSkuPropIds());
//                stmtAws.addBatch();
            }

            count = stmt28.executeBatch().length;
            if (count > 0) {
                count = 0;
                count = stmt27.executeBatch().length;
                if (count > 0) {
                    // connAws.commit();
                    conn28.commit();
                    conn27.commit();
                } else {
                    // connAws.rollback();
                    conn28.rollback();
                    conn27.rollback();
                }
            } else {
                // connAws.rollback();
                conn28.rollback();
            }

            /*count = stmtAws.executeBatch().length;
            if (count > 0) {
                count = 0;

            } else {
                // connAws.rollback();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + " updateCustomBenchmarkSkuNew error :" + e.getMessage());
            LOG.error("pid:" + pid + " updateCustomBenchmarkSkuNew error :" + e.getMessage());
            /*try {
                // connAws.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
            try {
                conn28.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                conn27.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt27);
            DBHelper.getInstance().closePreparedStatement(stmt28);
            // DBHelper.getInstance().closePreparedStatement(stmtAws);
            DBHelper.getInstance().closeConnection(conn27);
            // DBHelper.getInstance().closeConnection(connAws);
            DBHelper.getInstance().closeConnection(conn28);
        }

        return count > 0;
    }

    @Override
    public boolean updateGoodsFlag(String pid, int moq, String rangePrice, String priceContent, int isSoldFlag, int benchmarkingFlag) {
        throw new RuntimeException("already cancel method called");
    }

    @Override
    public boolean setGoodsFlagByPid(GoodsEditBean editBean) {

        Connection conn27 = DBHelper.getInstance().getConnection();
        //Connection connOnline = DBHelper.getInstance().getConnection2();
        String updateSql = "update custom_benchmark_ready set updatetime = now()";
        if(editBean.getBenchmarking_flag() > 0){
            updateSql += ",benchmarking_flag=?";
        }
        if(editBean.getDescribe_good_flag() > 0){
            updateSql += ",source_used_flag=2";
        }
        if(editBean.getNever_off_flag() > 0){
            updateSql += ",is_edited='3'";
        }
        if(editBean.getUgly_flag() > 0){
            updateSql += ",ugly_flag=?";
        }
        if(editBean.getWeight_flag() > 0){
            updateSql += ",weight_flag=?";
        }
        if(editBean.getUniqueness_flag() > 0){
            updateSql += ",uniqueness_flag=?";
        }
        updateSql += " where pid = ? ";
        PreparedStatement stmt = null;
        //PreparedStatement stmtOnline = null;

        int rs = 0;
        try {
            //conn27.setAutoCommit(false);
            //connOnline.setAutoCommit(false);
            stmt = conn27.prepareStatement(updateSql);
            int count = 1;

            if(editBean.getBenchmarking_flag() > 0){
                stmt.setInt(count++, editBean.getBenchmarking_flag());
            }
            if(editBean.getUgly_flag() > 0){
                stmt.setInt(count++, editBean.getUgly_flag());
            }
            if(editBean.getWeight_flag() > 0){
                stmt.setInt(count++, editBean.getWeight_flag());
            }
            if(editBean.getUniqueness_flag() > 0){
                stmt.setInt(count++, editBean.getUniqueness_flag());
            }
            stmt.setString(count++, editBean.getPid());
            rs = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + editBean.getPid() + " setGoodsFlagByPid error :" + e.getMessage());
            LOG.error("pid:" + editBean.getPid() + " setGoodsFlagByPid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeConnection(conn27);
            //DBHelper.getInstance().closeConnection(connOnline);
        }
        return rs > 0;
    }

    @Override
    public boolean checkIsHotGoods(String pid) {

        Connection conn27 = DBHelper.getInstance().getConnection();
        String querySql = "select count(1) from hot_goods_use where goods_pid = ? ";
        PreparedStatement stmt = null;

        ResultSet rs = null;
        int count = 0;
        try {
            stmt = conn27.prepareStatement(querySql);
            stmt.setString(1, pid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + " checkIsHotGoods error :" + e.getMessage());
            LOG.error("pid:" + pid + " checkIsHotGoods error :" + e.getMessage());
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
            DBHelper.getInstance().closeConnection(conn27);
        }
        return count > 0;
    }

    @Override
    public Map<String, Integer> queryUnsellableReason() {

        Connection conn28 = DBHelper.getInstance().getConnection8();
        String querySql = "select pid, unsellableReason from custom_benchmark_ready_newest "
                + "where (cur_time BETWEEN date_sub(NOW(),interval 2 day)  AND date_sub(NOW(),interval 1 day)) "
                + "and unsellableReason = 3";
        PreparedStatement stmt = null;

        Map<String, Integer> map = new HashMap<String, Integer>();
        ResultSet rs = null;
        try {
            stmt = conn28.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("pid"), rs.getInt("unsellableReason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryUnsellableReason error :" + e.getMessage());
            LOG.error("queryUnsellableReason error :" + e.getMessage());
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
        return map;
    }

    @Override
    public Map<String, Integer> querySoldUnsellableReason() {

        Connection conn28 = DBHelper.getInstance().getConnection8();
//        String querySql = "select pid, unsellableReason from custom_benchmark_ready_newest "
//                + "where (cur_time BETWEEN date_sub(NOW(),interval 2 day)  AND date_sub(NOW(),interval 1 day)) "
//                + "and unsellableReason = 3";
        String querySql = "SELECT a.pid, b.unsellableReason"
                + " FROM alidata.online_goods_verify_sold a LEFT JOIN cross_border.custom_benchmark_ready_newest b"
                + " ON a.pid = b.pid"
                + " WHERE a.cur_time IS NOT NULL";
        PreparedStatement stmt = null;

        Map<String, Integer> map = new HashMap<String, Integer>();
        ResultSet rs = null;
        try {
            stmt = conn28.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String pid = rs.getString("pid");
                Integer unsellableReason = rs.getInt("unsellableReason");
                if (pid != null && !"".equals(pid) && unsellableReason != null && !"".equals(unsellableReason)) {
                    map.put(rs.getString("pid"), rs.getInt("unsellableReason"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryUnsellableReason error :" + e.getMessage());
            LOG.error("queryUnsellableReason error :" + e.getMessage());
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
        return map;
    }

    @Override
    public boolean updateOnlineUnsellableReason(String pid, int flag) {

        //该表线上没有不需要操作custom_benchmark_ready
        Connection connOnline = DBHelper.getInstance().getConnection2();
        String querySql = "update custom_benchmark_ready set unsellableReason = ?,cur_time = now() where pid = ? ";
        PreparedStatement stmt = null;
        int count = 0;
        try {
            stmt = connOnline.prepareStatement(querySql);
            stmt.setInt(1, flag);
            stmt.setString(2, pid);
            count = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + " updateOnlineUnsellableReason error :" + e.getMessage());
            LOG.error("pid:" + pid + " updateOnlineUnsellableReason error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBHelper.getInstance().closeConnection(connOnline);
        }
        return count > 0;
    }

    @Override
    public boolean updateOnlineSoldUnsellableReason(String pid, int flag, UpdateTblModel model) {
        Connection conn = DBHelper.getInstance().getConnection(); // 本地
        Connection conn28 = DBHelper.getInstance().getConnection8(); // 28
        // 更新27和线上
        String upSql = "update custom_benchmark_ready set unsellableReason = ?,cur_time=sysdate() where pid = ?";
        // 清除28上更新标志
        String up28Sql = "UPDATE alidata.online_goods_verify_sold SET cur_time = NULL WHERE	pid = ?";
        // 在28上面保存通过mq更新线上数据的记录
        String insertSqlString = "INSERT INTO `alidata`.`mq_update_log` (`pid`, `message`, `createtime`) VALUES (?, ?, NOW())";
        PreparedStatement stmt = null;
        PreparedStatement stmt28 = null;
        int rs = 0;
        try {
            conn.setAutoCommit(false);
            conn28.setAutoCommit(false);
            stmt28 = conn28.prepareStatement(insertSqlString);
            stmt28.setString(1, pid);
            stmt28.setString(2, JSONObject.fromObject(model).toString());
            rs = stmt28.executeUpdate();
            if (rs >= 0) {
                rs = 0;
                stmt28 = conn28.prepareStatement(up28Sql);
                stmt28.setString(1, pid);
                rs = stmt28.executeUpdate();
                if (rs >= 0) {
                    rs = 0;
                    stmt = conn.prepareStatement(upSql);
                    stmt.setInt(1, flag);
                    stmt.setString(2, pid);
                    rs = stmt.executeUpdate();
                    if (rs > 0) {
                        conn.commit();
                        conn28.commit();
                    } else {
                        conn.rollback();
                        conn28.rollback();
                    }
                } else {
                    conn28.rollback();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + " updateOnlineSoldUnsellableReason error :" + e.getMessage());
            LOG.error("pid:" + pid + " updateOnlineSoldUnsellableReason error :" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
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
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return rs > 0;
    }

    @Override
    public String getWordSizeInfoByPid(String pid) {

        Connection connOnline = DBHelper.getInstance().getConnection2();
        String querySql = "select size_info_en from custom_benchmark_ready_size_info  where del_flag = 0 and  goods_pid = ? ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String wordSizeInfo = "";
        try {
            stmt = connOnline.prepareStatement(querySql);
            stmt.setString(1, pid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                wordSizeInfo = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + " getWordSizeInfoByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + " getWordSizeInfoByPid error :" + e.getMessage());
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
            DBHelper.getInstance().closeConnection(connOnline);
        }
        return wordSizeInfo;
    }

    @Override
    public boolean deleteWordSizeInfoByPid(String pid) {

        String updateSql = "update  custom_benchmark_ready_size_info  set del_flag = 1 where  goods_pid = ? ";
        int count = 0;
        try {


            List<String> lstValues = new ArrayList<>();
            lstValues.add(pid);
            String runSql = DBHelper.covertToSQL(updateSql,lstValues);
            count=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + " deleteWordSizeInfoByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + " deleteWordSizeInfoByPid error :" + e.getMessage());
        } finally {

        }
        return count > 0;
    }

    @Override
    public List<OrderProductSourceLogBean> queryOrderProductSourceLog() {
        ArrayList<OrderProductSourceLogBean> list = new ArrayList<OrderProductSourceLogBean>();
        Connection conn27 = DBHelper.getInstance().getConnection();
        String querySql = "SELECT id, od_id, adminid, userid, addtime, orderid, confirm_userid, confirm_time, goodsid, usecount, buycount, del, tb_1688_itemid, purchase_state, remark, updatetime, ops_id, cur_time FROM order_product_source_log WHERE cur_time IS NULL";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderProductSourceLogBean bean;
        try {
            stmt = conn27.prepareStatement(querySql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                bean = new OrderProductSourceLogBean();
                bean.setId(rs.getString("id"));
                bean.setOdId(rs.getString("od_id"));
                bean.setAdminid(rs.getString("adminid"));
                bean.setUserid(rs.getString("userid"));
                bean.setAddtime(rs.getString("addtime"));
                bean.setOrderid(rs.getString("orderid"));
                bean.setConfirmUserid(rs.getString("confirm_userid"));
                bean.setConfirmTime(rs.getString("confirm_time"));
                bean.setGoodsid(rs.getString("goodsid"));
                bean.setUsecount(rs.getString("usecount"));
                bean.setBuycount(rs.getString("buycount"));
                bean.setDel(rs.getString("del"));
                bean.setTb1688Itemid(rs.getString("tb_1688_itemid"));
                bean.setPurchaseState(rs.getString("purchase_state"));
                bean.setRemark(rs.getString("remark"));
                bean.setUpdatetime(rs.getString("updatetime"));
                bean.setOpsId(rs.getString("ops_id"));
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryOrderProductSourceLog error :" + e.getMessage());
            LOG.error("queryOrderProductSourceLog error :" + e.getMessage());
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
            DBHelper.getInstance().closeConnection(conn27);
        }
        return list;
    }

    @Override
    public int updateOrderProductSourceLog(OrderProductSourceLogBean bean, RunSqlModel model) {
        Connection conn = DBHelper.getInstance().getConnection(); //27
        PreparedStatement stmt = null;
        //更新本地数据
        String upSql = "UPDATE order_product_source_log SET cur_time = NOW() WHERE id = ?";
        // 在27上面保存通过mq更新线上数据的记录
        String insertSqlLog = "INSERT INTO `alidata`.`mq_update_log` (`pid`, `message`, `createtime`) VALUES (?, ?, NOW())";
        //数据同步到线上（目前表中数据无更新操作）
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("INSERT IGNORE INTO order_product_source_log (id, od_id, adminid, userid, addtime, orderid, confirm_userid, confirm_time, goodsid, usecount, buycount, del, tb_1688_itemid, purchase_state, remark, updatetime, ops_id) VALUES ")
                .append("('")
                .append(bean.getId()).append("', '")
                .append(bean.getOdId()).append("', '")
                .append(bean.getAdminid()).append("', '")
                .append(bean.getUserid()).append("', '")
                .append(bean.getAddtime()).append("', '")
                .append(bean.getOrderid()).append("', ");
        if (null == bean.getConfirmUserid() || "null".equals(bean.getConfirmUserid())) {
            insertSql.append("null").append(", ");
        } else {
            insertSql.append("'").append(bean.getConfirmUserid()).append("', ");
        }
        if (null == bean.getConfirmTime() || "null".equals(bean.getConfirmTime())) {
            insertSql.append("null").append(", ");
        } else {
            insertSql.append("'").append(bean.getConfirmTime()).append("', ");
        }
        insertSql.append("'").append(bean.getGoodsid()).append("', '")
                .append(bean.getUsecount()).append("', '")
                .append(bean.getBuycount()).append("', '")
                .append(bean.getDel()).append("', '")
                .append(bean.getTb1688Itemid()).append("', '")
                .append(bean.getPurchaseState()).append("', ");
        if (null == bean.getRemark() || "null".equals(bean.getRemark())) {
            insertSql.append("null").append(", ");
        } else {
            insertSql.append("'").append(bean.getRemark()).append("', ");
        }
        insertSql.append("'").append(bean.getUpdatetime()).append("', '")
                .append(bean.getOpsId()).append("')");
        model.setSql(insertSql.toString());

        int rs = 0;
        try {
            //提交方式
            conn.setAutoCommit(false);
            //更新27同步标志
            rs = 0;
            stmt = conn.prepareStatement(upSql);
            stmt.setString(1, bean.getId());
            rs = stmt.executeUpdate();
            //保存记录
            if (rs > 0) {
                stmt = conn.prepareStatement(insertSqlLog);
                stmt.setString(1, bean.getId());
                stmt.setString(2, JSONObject.fromObject(model).toString());
                rs = stmt.executeUpdate();
                if (rs > 0) {
                    conn.commit();
                } else {
                    conn.rollback();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updateOrderProductSourceLog error :" + e.getMessage());
            LOG.error("updateOrderProductSourceLog error :" + e.getMessage());
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
    public List<String> queryInfringingGoodsByLimit(int limitNum) {
        List<String> list = new ArrayList<String>(limitNum + 10);
        Connection conn27 = DBHelper.getInstance().getConnection();
        String querySql = "select pid from goods_list_search where infringingflag =1 and is_update = 0 limit ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn27.prepareStatement(querySql);
            stmt.setInt(1, limitNum);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryInfringingGoodsByLimit error :" + e.getMessage());
            LOG.error("queryInfringingGoodsByLimit error :" + e.getMessage());
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
            DBHelper.getInstance().closeConnection(conn27);
        }
        return list;
    }

    @Override
    public boolean updateInfringingGoodsByPid(String pid) {
        Connection conn = DBHelper.getInstance().getConnection();
        Statement stmt = null;
        String updateSqlReady = "update custom_benchmark_ready set infringing_flag = 1 where pid = '" + pid + "'";
        String updateSqlSource = "update goods_list_search set is_update = 1 where pid = '" + pid + "'";
        int rs = 0;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            stmt.addBatch(updateSqlReady);
            stmt.addBatch(updateSqlSource);
            rs = stmt.executeBatch().length;
            if (rs > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("updateOrderProductSourceLog error :" + e.getMessage());
            LOG.error("updateOrderProductSourceLog error :" + e.getMessage());
            try {
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
        }
        return rs > 0;
    }

    @Override
    public boolean setNoBenchmarking(String pid, double finalWeight) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        Connection conn31 = DBHelper.getInstance().getConnection6();
        Connection conn27 = DBHelper.getInstance().getConnection();
        // Connection connAws = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt28 = null;
        PreparedStatement stmt31 = null;
        PreparedStatement stmt27 = null;
        PreparedStatement stmtAws = null;
        String updateSql28 = "update custom_benchmark_ready_newest set ali_weight='',bm_flag=2,isBenchmark=3 where pid = ?";
        String updateSql27 = "update custom_benchmark_ready set ali_weight='',bm_flag=2,isBenchmark=3 where pid = ?";
        // String updateSqlAws = "update custom_benchmark_ready set ali_weight='',bm_flag=2,isBenchmark=3 where pid = ?";
        String updateSql31 = "replace into single_goods_offers_child(good_url,goods_pid,set_weight,change_mark," +
                "crawl_flag,service_ip) values(?,?,?,1,0,'')";
        int rs = 0;
        try {
            stmt28 = conn28.prepareStatement(updateSql28);
            stmt28.setString(1, pid);

            stmt27 = conn27.prepareStatement(updateSql27);
            stmt27.setString(1, pid);

            // stmtAws = connAws.prepareStatement(updateSqlAws);
            // stmtAws.setString(1, pid);

            stmt31 = conn31.prepareStatement(updateSql31);
            stmt31.setString(1, "https://detail.1688.com/offer/" + pid + ".html");
            stmt31.setString(2, pid);
            stmt31.setDouble(3, finalWeight);

            // rs = stmtAws.executeUpdate();
            boolean isSuccess = GoodsInfoUpdateOnlineUtil.setNoBenchmarkingMongoDb(pid);
            if (isSuccess) {
                rs = 1;
                stmt28.executeUpdate();
                stmt27.executeUpdate();
                stmt31.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",setNoBenchmarking error :" + e.getMessage());
            LOG.error("pid:" + pid + ",setNoBenchmarking error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closePreparedStatement(stmt27);
            DBHelper.getInstance().closePreparedStatement(stmtAws);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(conn31);
            DBHelper.getInstance().closeConnection(conn27);
            // DBHelper.getInstance().closeConnection(connAws);
        }
        return rs > 0;
    }

    
    @Override
    public boolean upCustomerReady(String pid,String aliPid,String aliPrice,int bmFlag, int isBenchmark,String edName,String rwKeyword,int flag) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        Connection conn31 = DBHelper.getInstance().getConnection6();
        Connection conn27 = DBHelper.getInstance().getConnection();
        PreparedStatement stmt28 = null;
        PreparedStatement stmt31 = null;
        PreparedStatement stmt27 = null;
        String updateSql28 = "update custom_benchmark_ready_newest set ali_pid=?,ali_price=?,bm_flag=?,isBenchmark=?,finalName=?,rw_keyword=? where pid = ?";
        String updateSql27 = "update custom_benchmark_ready set ali_pid=?,ali_price=?,bm_flag=?,isBenchmark=?,finalName=?,rw_keyword=? where pid = ?";
        String updateSql31 = "replace into single_goods_offers_child(good_url,goods_pid,change_mark," +
                "crawl_flag,service_ip) values(?,?,1,0,'')";
        int rs = 0;
        try {
            stmt28 = conn28.prepareStatement(updateSql28);
            stmt28.setString(1, aliPid);
            stmt28.setString(2, aliPrice);
            stmt28.setInt(3, bmFlag);
            stmt28.setInt(4, isBenchmark);
            stmt28.setString(5, edName);
            stmt28.setString(6, rwKeyword);
            stmt28.setString(7, pid);

            stmt27 = conn27.prepareStatement(updateSql27);
            stmt27.setString(1, aliPid);
            stmt27.setString(2, aliPrice);
            stmt27.setInt(3, bmFlag);
            stmt27.setInt(4, isBenchmark);
            stmt27.setString(5, edName);
            stmt27.setString(6, rwKeyword);
            stmt27.setString(7, pid);
            
            if(flag==1){
	            stmt31 = conn31.prepareStatement(updateSql31);
	            stmt31.setString(1, "https://detail.1688.com/offer/" + pid + ".html");
	            stmt31.setString(2, pid);
            }

            // 线上MongoDB
            boolean isSuccess = GoodsInfoUpdateOnlineUtil.setCustomerReadyMongoDb(pid,aliPid,aliPrice,bmFlag,isBenchmark,edName,rwKeyword);
            if (isSuccess) {
                rs = 1;
                stmt28.executeUpdate();
                stmt27.executeUpdate();
                if(flag==1){
                	stmt31.executeUpdate();
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",upCustomerReady error :" + e.getMessage());
            LOG.error("pid:" + pid + ",upCustomerReady error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            if(flag==1){
            	DBHelper.getInstance().closePreparedStatement(stmt31);
            	DBHelper.getInstance().closeConnection(conn31);
            }
            DBHelper.getInstance().closePreparedStatement(stmt27);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(conn27);
        }
        return rs > 0;
    }
    
    
    
    @Override
    public boolean setNeverOff(String pid) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        Connection conn27 = DBHelper.getInstance().getConnection();
        PreparedStatement stmt28 = null;
        PreparedStatement stmt27 = null;
        String updateSql27 = "update custom_benchmark_ready set is_edited='3' where pid = ?";
        String updateSql28 = "update custom_benchmark_ready_newest set is_edited='3' where pid = ?";
        int rs = 0;
        try {
            conn28.setAutoCommit(false);
            conn27.setAutoCommit(false);
            stmt28 = conn28.prepareStatement(updateSql28);
            stmt28.setString(1, pid);

            stmt27 = conn27.prepareStatement(updateSql27);
            stmt27.setString(1, pid);

            rs = stmt27.executeUpdate();
            if (rs > 0) {
                rs = 0;
                rs = stmt28.executeUpdate();
                if (rs > 0) {
                    conn27.commit();
                    conn28.commit();
                } else {
                    DBHelper.getInstance().rollbackConnection(conn27);
                    DBHelper.getInstance().rollbackConnection(conn28);
                }
            } else {
                DBHelper.getInstance().rollbackConnection(conn27);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DBHelper.getInstance().rollbackConnection(conn27);
            DBHelper.getInstance().rollbackConnection(conn28);
            System.out.println("pid:" + pid + ",setNeverOff error :" + e.getMessage());
            LOG.error("pid:" + pid + ",setNeverOff error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closePreparedStatement(stmt27);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(conn27);
        }
        return rs > 0;
    }


    @Override
	public String getCatxs(String catId,String factoryPrice){
		String sql = "select case when '"+factoryPrice+"' <= node_p1+0.0 then adjustment_coefficient_1 "+
				"when '"+factoryPrice+"' > node_p1+0.0 and '"+factoryPrice+"' <= node_p2+0.0 then adjustment_coefficient_2 "+
				"when '"+factoryPrice+"' > node_p2+0.0 and '"+factoryPrice+"' <= node_p3+0.0 then adjustment_coefficient_3 "+
				"else adjustment_coefficient_4 end as adjustment_coefficient "+
				"from cate_increase_price_rate_adjustment_coefficient "+
				"where cate_id = '"+catId+"' ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String  result = "";
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getString("adjustment_coefficient");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

    @Override
    public int checkSkuGoodsOffers(String pid) {
        Connection conn31 = DBHelper.getInstance().getConnection6();
        PreparedStatement stmt31 = null;
        String querySql = "select count(1) from sku_goods_offers where goods_pid = ?";
        ResultSet rs = null;
        int count = 0;
        try {
            stmt31 = conn31.prepareStatement(querySql);
            stmt31.setString(1, pid);
            rs = stmt31.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",checkSkuGoodsOffers error :" + e.getMessage());
            LOG.error("pid:" + pid + ",checkSkuGoodsOffers error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return count;
    }

    @Override
    public int updateSkuGoodsOffers(String pid, double finalWeight) {
        Connection conn31 = DBHelper.getInstance().getConnection6();
        PreparedStatement stmt31 = null;
        String updateSql = "update sku_goods_offers set clear_flag=0,crawl_flag=2,set_weight= ? where goods_pid = ?";
        int count = -1;
        try {
            stmt31 = conn31.prepareStatement(updateSql);
            stmt31.setDouble(1, finalWeight);
            stmt31.setString(2, pid);
            count = stmt31.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",updateSkuGoodsOffers error :" + e.getMessage());
            LOG.error("pid:" + pid + ",updateSkuGoodsOffers error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return count;
    }

    @Override
    public int updateSourceProFlag(String pid, String finalWeight) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        PreparedStatement stmt28 = null;
        String querySql = "update custom_benchmark_ready_newest set source_pro_flag = 7,final_weight = ? where pid = ?";
        int count = -1;
        try {
            stmt28 = conn28.prepareStatement(querySql);
            stmt28.setString(1, finalWeight);
            stmt28.setString(2, pid);
            count = stmt28.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",updateSourceProFlag error :" + e.getMessage());
            LOG.error("pid:" + pid + ",updateSourceProFlag error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return count;
    }

    @Override
    public int insertIntoSingleOffersChild(String pid, double finalWeight) {
        Connection conn31 = DBHelper.getInstance().getConnection6();
        PreparedStatement stmt31 = null;
        String updateSql = "replace into single_goods_offers_child(good_url,goods_pid,set_weight,change_mark," +
                "crawl_flag,service_ip,create_time) values(?,?,?,1,0,'',now())";
        int count = -1;
        try {
            stmt31 = conn31.prepareStatement(updateSql);
            stmt31.setString(1, "https://detail.1688.com/offer/" + pid + ".html");
            stmt31.setString(2, pid);
            stmt31.setDouble(3, finalWeight);
            count = stmt31.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",insertIntoSingleOffersChild error :" + e.getMessage());
            LOG.error("pid:" + pid + ",insertIntoSingleOffersChild error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return count;
    }

    @Override
    public List<CustomBenchmarkSkuNew> querySkuByPid(String pid) {
        Connection conn27 = DBHelper.getInstance().getConnection();
        PreparedStatement stmt27 = null;
        String querySql = "select wprice, sku_attr, sku_prop_ids, act_sku_cal_price, act_sku_multi_currency_cal_price, " +
                "act_sku_multi_currency_display_price,avail_quantity, inventory, is_activity, " +
                "sku_cal_price, sku_multi_currency_cal_price, sku_multi_currency_display_price," +
                "create_time,update_time,flag,spec_id,sku_id,final_weight,is_sold_flag " +
                "from custom_benchmark_sku where pid = ?";
        ResultSet rs = null;
        int count = 0;
        List<CustomBenchmarkSkuNew> list = new ArrayList<>();
        try {
            stmt27 = conn27.prepareStatement(querySql);
            stmt27.setString(1, pid);
            rs = stmt27.executeQuery();

            while (rs.next()) {
                CustomBenchmarkSkuNew benchmarkSkuNew = new CustomBenchmarkSkuNew();
                benchmarkSkuNew.setWprice(rs.getString("wprice"));
                benchmarkSkuNew.setSkuAttr(rs.getString("sku_attr"));
                benchmarkSkuNew.setSkuPropIds(rs.getString("sku_prop_ids"));
                SkuValPO skuVal = new SkuValPO();
                skuVal.setActSkuCalPrice(rs.getDouble("act_sku_cal_price"));
                skuVal.setActSkuMultiCurrencyCalPrice(rs.getDouble("act_sku_multi_currency_cal_price"));
                skuVal.setActSkuMultiCurrencyDisplayPrice(rs.getDouble("act_sku_multi_currency_display_price"));
                skuVal.setAvailQuantity(rs.getInt("avail_quantity"));
                skuVal.setInventory(rs.getInt("inventory"));
                skuVal.setIsActivity(rs.getBoolean("is_activity"));
                skuVal.setSkuCalPrice(rs.getDouble("sku_cal_price"));
                skuVal.setSkuMultiCurrencyCalPrice(rs.getDouble("sku_multi_currency_cal_price"));
                skuVal.setSkuMultiCurrencyDisplayPrice(rs.getDouble("sku_multi_currency_display_price"));
                benchmarkSkuNew.setSkuVal(skuVal);
                benchmarkSkuNew.setCreateTime(rs.getDate("create_time"));
                benchmarkSkuNew.setUpdateTime(rs.getDate("update_time"));
                benchmarkSkuNew.setFlag(rs.getInt("flag"));
                benchmarkSkuNew.setSpecId(rs.getString("spec_id"));
                benchmarkSkuNew.setFinalWeight(rs.getString("final_weight"));
                benchmarkSkuNew.setIsSoldFlag(rs.getString("is_sold_flag"));
                list.add(benchmarkSkuNew);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",querySkuByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + ",querySkuByPid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt27);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn27);
        }
        return list;
    }

    @Override
    public int deleteSkuByPid(String pid) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        //该表线上没有不需要操作 custom_benchmark_sku
        Connection connAws = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt28 = null;
        PreparedStatement stmtAws = null;
        String deleteSql = "delete from custom_benchmark_sku where pid = ?";
        int count = 0;
        try {
            connAws.setAutoCommit(false);
            conn28.setAutoCommit(false);
            stmtAws = connAws.prepareStatement(deleteSql);
            stmt28 = conn28.prepareStatement(deleteSql);

            stmtAws.setString(1,pid);
            stmt28.setString(1,pid);

            count = stmtAws.executeUpdate();
            if(count > 0 ){
                count = stmt28.executeUpdate();
                if(count > 0){
                    connAws.commit();
                    conn28.commit();
                }else{
                    connAws.rollback();
                    conn28.rollback();
                }
            }else{
                connAws.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",deleteSkuByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + ",deleteSkuByPid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmtAws);
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(connAws);
        }
        return count;
    }

    @Override
    public int insertIntoSkuToOnline(List<CustomBenchmarkSkuNew> insertList) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        //该表线上不存在不需要操作custom_benchmark_sku
        Connection connAws = DBHelper.getInstance().getConnection2();
        PreparedStatement stmt28 = null;
        PreparedStatement stmtAws = null;

        String insertSql = "insert into custom_benchmark_sku(wprice,pid, sku_attr, sku_prop_ids, act_sku_cal_price, " +
                "act_sku_multi_currency_cal_price, " +
                "act_sku_multi_currency_display_price,avail_quantity, inventory, is_activity, " +
                "sku_cal_price, sku_multi_currency_cal_price, sku_multi_currency_display_price," +
                "create_time,update_time,flag,spec_id,sku_id,final_weight,is_sold_flag)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int count = 0;
        List<CustomBenchmarkSkuNew> list = new ArrayList<>();
        try {
            connAws.setAutoCommit(false);
            conn28.setAutoCommit(false);
            stmtAws = connAws.prepareStatement(insertSql);
            stmt28 = conn28.prepareStatement(insertSql);

            for (CustomBenchmarkSkuNew benchmarkSkuNew : insertList) {
                int upNum = 1;
                stmt28.setString(upNum++, benchmarkSkuNew.getWprice());
                stmt28.setString(upNum++, benchmarkSkuNew.getPid());
                stmt28.setString(upNum++, benchmarkSkuNew.getSkuAttr());
                stmt28.setString(upNum++, benchmarkSkuNew.getSkuPropIds());
                SkuValPO skuVal = benchmarkSkuNew.getSkuVal();
                stmt28.setDouble(upNum++, skuVal.getActSkuCalPrice());
                stmt28.setDouble(upNum++, skuVal.getActSkuMultiCurrencyCalPrice());
                stmt28.setDouble(upNum++, skuVal.getActSkuMultiCurrencyDisplayPrice());
                stmt28.setInt(upNum++, skuVal.getAvailQuantity());
                stmt28.setInt(upNum++, skuVal.getInventory());
                stmt28.setBoolean(upNum++, skuVal.getIsActivity());
                stmt28.setDouble(upNum++, skuVal.getSkuCalPrice());
                stmt28.setDouble(upNum++, skuVal.getSkuMultiCurrencyCalPrice());
                stmt28.setDouble(upNum++, skuVal.getSkuMultiCurrencyDisplayPrice());
                stmt28.setDate(upNum++, (Date) benchmarkSkuNew.getCreateTime());
                stmt28.setDate(upNum++, (Date) benchmarkSkuNew.getUpdateTime());
                stmt28.setInt(upNum++, benchmarkSkuNew.getFlag());
                stmt28.setString(upNum++, benchmarkSkuNew.getSpecId());
                stmt28.setString(upNum++, benchmarkSkuNew.getSkuId());
                stmt28.setString(upNum++, benchmarkSkuNew.getFinalWeight());
                stmt28.setString(upNum++, benchmarkSkuNew.getIsSoldFlag());
                stmt28.addBatch();

                upNum = 1;
                stmtAws.setString(upNum++, benchmarkSkuNew.getWprice());
                stmtAws.setString(upNum++, benchmarkSkuNew.getPid());
                stmtAws.setString(upNum++, benchmarkSkuNew.getSkuAttr());
                stmtAws.setString(upNum++, benchmarkSkuNew.getSkuPropIds());
                stmtAws.setDouble(upNum++, skuVal.getActSkuCalPrice());
                stmtAws.setDouble(upNum++, skuVal.getActSkuMultiCurrencyCalPrice());
                stmtAws.setDouble(upNum++, skuVal.getActSkuMultiCurrencyDisplayPrice());
                stmtAws.setInt(upNum++, skuVal.getAvailQuantity());
                stmtAws.setInt(upNum++, skuVal.getInventory());
                stmtAws.setBoolean(upNum++, skuVal.getIsActivity());
                stmtAws.setDouble(upNum++, skuVal.getSkuCalPrice());
                stmtAws.setDouble(upNum++, skuVal.getSkuMultiCurrencyCalPrice());
                stmtAws.setDouble(upNum++, skuVal.getSkuMultiCurrencyDisplayPrice());
                stmtAws.setDate(upNum++, (Date) benchmarkSkuNew.getCreateTime());
                stmtAws.setDate(upNum++, (Date) benchmarkSkuNew.getUpdateTime());
                stmtAws.setInt(upNum++, benchmarkSkuNew.getFlag());
                stmtAws.setString(upNum++, benchmarkSkuNew.getSpecId());
                stmtAws.setString(upNum++, benchmarkSkuNew.getSkuId());
                stmtAws.setString(upNum++, benchmarkSkuNew.getFinalWeight());
                stmtAws.setString(upNum++, benchmarkSkuNew.getIsSoldFlag());
                stmtAws.addBatch();
            }
            count = stmtAws.executeBatch().length;
            if (count > 0) {
                count = stmt28.executeBatch().length;
                if (count > 0) {
                    connAws.commit();
                    conn28.commit();
                } else {
                    connAws.rollback();
                    conn28.rollback();
                }
            } else {
                connAws.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insertIntoSkuToOnline error :" + e.getMessage());
            LOG.error("insertIntoSkuToOnline error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closePreparedStatement(stmtAws);
            DBHelper.getInstance().closeConnection(connAws);
        }
        return count;
    }

    @Override
    public int updatePromotionFlag(String pid) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        PreparedStatement stmt28 = null;

        String updateSql = "update custom_benchmark_ready_newest set promotion_flag = 1 where pid = ?";
        int count = 0;
        try {
            stmt28 = conn28.prepareStatement(updateSql);
            stmt28.setString(1, pid);
            count = stmt28.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",updatePromotionFlag error :" + e.getMessage());
            LOG.error("pid:" + pid + ",updatePromotionFlag error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return count;
    }

    @Override
    public int updatePidEnInfo(CustomGoodsPublish gd) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        PreparedStatement stmt28 = null;

        String updateSql = "update custom_benchmark_ready_newest set eninfo = ? where pid = ?";
        int count = 0;
        try {
            stmt28 = conn28.prepareStatement(updateSql);
            stmt28.setString(1, gd.getEninfo());
            stmt28.setString(2, gd.getPid());
            count = stmt28.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + gd.getPid() + ",updatePidEnInfo error :" + e.getMessage());
            LOG.error("pid:" + gd.getPid() + ",updatePidEnInfo error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return count;
    }


    @Override
    public int checkShopGoodsImgIsMarkByParam(ShopMd5Bean shopMd5Bean) {
        Connection conn28 = DBHelper.getInstance().getConnection5();
        PreparedStatement stmt28 = null;
        ResultSet rs = null;
        String querySql = "select count(0) from shop_goods_img_delete where shop_id = ? and SUBSTRING_INDEX(img, '/', -1) = ?";
        int count = 0;
        try {
            stmt28 = conn28.prepareStatement(querySql);
            stmt28.setString(1, shopMd5Bean.getShopId());
            stmt28.setString(2, shopMd5Bean.getMd5Img());
            rs = stmt28.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("shopId:" + shopMd5Bean.getShopId() + ",checkShopGoodsImgIsMarkByParam error :" + e.getMessage());
            LOG.error("shopId:" + shopMd5Bean.getShopId() + ",checkShopGoodsImgIsMarkByParam error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeResultSet(rs);
        }
        return count;
    }

    @Override
    public int setNewAliPidInfo(String pid, String aliPid, String aliPrice) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        PreparedStatement stmt28 = null;

        String updateSql = "update custom_benchmark_ready_newest set ali_pid = ?, ali_price = ?,bm_flag = 1 ,isBenchmark = 1 where pid = ?";
        int count = 0;
        try {
            stmt28 = conn28.prepareStatement(updateSql);
            stmt28.setString(1, aliPid);
            stmt28.setString(2, aliPrice);
            stmt28.setString(3, pid);
            count = stmt28.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",setNewAliPidInfo error :" + e.getMessage());
            LOG.error("pid:" + pid + ",setNewAliPidInfo error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return count;
    }

    @Override
    public Map<String, String> queryNewAliPriceByAliPid(String aliPid) {
        Connection conn28 = DBHelper.getInstance().getConnection5();
        PreparedStatement stmt28 = null;
        ResultSet rs = null;
        String querySql = "select goods_pid,new_price,new_time from  goods_price_historys where goods_pid = ? order by new_time desc limit 1";
        Map<String, String> map = new HashMap<>();
        try {
            stmt28 = conn28.prepareStatement(querySql);
            stmt28.setString(1, aliPid);
            rs = stmt28.executeQuery();
            if(rs.next()){
                map.put("new_price",rs.getString("new_price"));
                map.put("new_time",rs.getString("new_time"));
                map.put("goods_pid",rs.getString("goods_pid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("aliPid:" + aliPid + ",queryNewAliPriceByAliPid error :" + e.getMessage());
            LOG.error("aliPid:" + aliPid + ",queryNewAliPriceByAliPid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeResultSet(rs);
        }
        return map;
    }

    @Override
    public int updateVolumeWeight(String pid, String newWeight) {
        Connection conn28 = DBHelper.getInstance().getConnection8();
        PreparedStatement stmt28 = null;

        String updateSql = "update custom_benchmark_ready_newest set volume_weight = ? where pid = ?";
        int count = 0;
        try {
            stmt28 = conn28.prepareStatement(updateSql);
            stmt28.setString(1, newWeight);
            stmt28.setString(2, pid);
            count = stmt28.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("pid:" + pid + ",updateVolumeWeight error :" + e.getMessage());
            LOG.error("pid:" + pid + ",updateVolumeWeight error",e);
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt28);
            DBHelper.getInstance().closeConnection(conn28);
        }
        return count;
    }

}
