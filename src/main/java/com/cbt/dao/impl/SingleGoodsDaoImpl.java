package com.cbt.dao.impl;

import com.cbt.bean.*;
import com.cbt.dao.SingleGoodsDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.Admuser;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SingleGoodsDaoImpl implements SingleGoodsDao {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SingleGoodsDaoImpl.class);

    @Override
    public JsonResult saveGoods(String goodsUrl, int adminId, double goodsWeight, int drainageFlag, int goodsType, String aliPid, String aliPrice) {

        JsonResult json = new JsonResult();
        Connection conn28 = DBHelper.getInstance().getConnection6();
        PreparedStatement stmtQry = null;
        PreparedStatement stmtSql = null;
        ResultSet rset = null;
        int count = 0;
        String querySql = "select count(0) from single_goods_offers where goods_pid =?";
        String insertSql = "insert into single_goods_offers(goods_pid,good_url,set_weight,admin_id,drainage_flag,goods_type,ali_pid,ali_price)" +
                " values(?,?,?,?,?,?,?,?) ";

        try {
            String pid = goodsUrl.substring(goodsUrl.lastIndexOf("/") + 1, goodsUrl.indexOf(".html"));
            stmtQry = conn28.prepareStatement(querySql);
            stmtQry.setString(1, pid);

            rset = stmtQry.executeQuery();
            if (rset.next()) {
                count = rset.getInt(1);
            }
            if (count > 0) {
                json.setOk(false);
                json.setMessage("当前商品：" + goodsUrl + "，已经存在");
            } else {
                count = 0;
                stmtSql = conn28.prepareStatement(insertSql);
                stmtSql.setString(1, pid);
                stmtSql.setString(2, goodsUrl);
                stmtSql.setDouble(3, goodsWeight);
                stmtSql.setInt(4, adminId);
                stmtSql.setInt(5, drainageFlag);
                stmtSql.setInt(6, goodsType);
                stmtSql.setString(7, aliPid);
                stmtSql.setString(8, aliPrice);
                count = stmtSql.executeUpdate();
                if (count > 0) {
                    json.setOk(true);
                } else {
                    json.setOk(false);
                    json.setMessage("当前商品：" + goodsUrl + "，插入失败，请重试");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("goodsUrl:" + goodsUrl + ",saveGoods error :" + e.getMessage());
            json.setOk(false);
            json.setMessage("当前商品：" + goodsUrl + "，插入失败:" + e.getMessage());
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
            DBHelper.getInstance().closeConnection(conn28);
        }
        return json;
    }

    @Override
    public List<CustomOnlineGoodsBean> queryDealGoods() {

        List<CustomOnlineGoodsBean> goodList = new ArrayList<CustomOnlineGoodsBean>();
        Connection conn28 = DBHelper.getInstance().getConnection6();
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

        int rs = 0;
        Connection connOnline = DBHelper.getInstance().getConnection2();
        Connection conn27 = DBHelper.getInstance().getConnection();
        Connection conn28 = DBHelper.getInstance().getConnection7();
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

        String updateSql = "update same_goods_ready set sync_flag =?,sync_remark=? where pid = ?";
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
                stmt28Up.setInt(1, 2);
                stmt28Up.setString(2, "当前 pid已经存在！");
                stmt28Up.setString(3, goods.getPid());
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
                    stmt28Up.setInt(1, 1);
                    stmt28Up.setString(2, "success");
                    stmt28Up.setString(3, goods.getPid());
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

    @Override
    public boolean updateSameTypeGoodsError(String pid) {

        int rs = 0;
        Connection connOnline = DBHelper.getInstance().getConnection2();
        Connection conn28 = DBHelper.getInstance().getConnection6();
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

        Connection conn28 = DBHelper.getInstance().getConnection6();
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

        Connection conn28 = DBHelper.getInstance().getConnection6();
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
        Connection conn28 = DBHelper.getInstance().getConnection6();
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
    public List<SameTypeGoodsBean> queryForList(SingleQueryGoodsParam queryPm) {
        Connection conn28 = DBHelper.getInstance().getConnection6();
        String querySql = "select sgo.goods_pid,sgo.create_time,sgo.good_url,sgo.admin_id,sgo.goods_name,sgo.shop_id,"
                + "sgo.pic as img_1688,sgo.crawl_flag,sgo.set_weight,sgo.clear_flag,sgo.goods_type,ifnull(sgr.remotpath,'') as remotpath,"
                + "ifnull(sgr.enname,'') as express_name,ifnull(sgr.custom_main_image,'') as express_img,ifnull(sgr.valid,0) as valid,"
                + "ifnull(sgr.sync_flag,0) as sync_flag,ifnull(sgr.sync_remark,'') as sync_remark,sgo.drainage_flag from single_goods_offers sgo "
                + "left join useful_data.single_goods_ready sgr on sgo.goods_pid = sgr.pid where 1 = 1 ";
        if (queryPm.getAdmid() > 0) {
            querySql += " and sgo.admin_id = ?";
        }
        if (!(queryPm.getPid() == null || "".equals(queryPm.getPid()))) {
            querySql += " and sgo.goods_pid = ?";
        }
        if (!(queryPm.getSttime() == null || "".equals(queryPm.getSttime()))) {
            querySql += " and sgo.create_time >= ? ";
        }
        if (!(queryPm.getEdtime() == null || "".equals(queryPm.getEdtime()))) {
            querySql += " and sgo.create_time <= ?";
        }
        if (queryPm.getDrainageFlag() > 0) {
            querySql += " and sgo.drainage_flag = ?";
        }
        if (queryPm.getGoodsType() > -1) {
            querySql += " and sgo.goods_type = ?";
        }
        querySql += " order by sgo.create_time desc limit ?,?";
        PreparedStatement stmt = null;
        ResultSet rss = null;
        List<SameTypeGoodsBean> list = new ArrayList<SameTypeGoodsBean>();
        try {
            int count = 1;
            stmt = conn28.prepareStatement(querySql);
            if (queryPm.getAdmid() > 0) {
                stmt.setInt(count++, queryPm.getAdmid());
            }
            if (!(queryPm.getPid() == null || "".equals(queryPm.getPid()))) {
                stmt.setString(count++, queryPm.getPid());
            }
            if (!(queryPm.getSttime() == null || "".equals(queryPm.getSttime()))) {
                stmt.setString(count++, queryPm.getSttime());
            }
            if (!(queryPm.getEdtime() == null || "".equals(queryPm.getEdtime()))) {
                stmt.setString(count++, queryPm.getEdtime());
            }
            if (queryPm.getDrainageFlag() > 0) {
                stmt.setInt(count++, queryPm.getDrainageFlag());
            }
            if (queryPm.getGoodsType() > -1) {
                stmt.setInt(count++, queryPm.getGoodsType());
            }
            stmt.setInt(count++, queryPm.getStartNum());
            stmt.setInt(count++, queryPm.getLimitNum());
            rss = stmt.executeQuery();
            while (rss.next()) {

                SameTypeGoodsBean goods = new SameTypeGoodsBean();
                String pid = rss.getString("goods_pid");
                goods.setGoodsPid(pid);
                String goodsName = rss.getString("goods_name");
                if (goodsName == null || "".equals(goodsName)) {
                    goodsName = "1688 GoodsName";
                }
                goods.setGoodsName(goodsName);
                String remotpath = rss.getString("remotpath");
                String goodsImg = rss.getString("img_1688");
                if (!(goodsImg == null || "".equals(goodsImg))) {
                    if (goodsImg.indexOf("https:") > -1 || goodsImg.indexOf("http:") > -1) {
                        goods.setGoodsImg(goodsImg);
                    } else {
                        goods.setGoodsImg((remotpath == null ? "" : remotpath) + goodsImg);

                    }
                }
                goods.setGoodUrl(rss.getString("good_url"));
                String express_name = rss.getString("express_name");
                if (express_name == null || "".equals(express_name)) {
                    express_name = "goods en name";
                }
                goods.setExpressUrl("https://www.import-express.com/goodsinfo/" + express_name + "-"
                        + rss.getString("goods_pid") + ".html");
                goods.setAdminId(rss.getInt("admin_id"));
                goods.setCreateTime(rss.getString("create_time"));
                goods.setCrawlFlag(rss.getInt("crawl_flag"));
                goods.setClearFlag(rss.getInt("clear_flag"));
                goods.setValid(rss.getInt("valid"));
                goods.setSyncFlag(rss.getInt("sync_flag"));
                goods.setSyncRemark(rss.getString("sync_remark"));
                goods.setAveWeight(rss.getDouble("set_weight"));
                goods.setDrainageFlag(rss.getInt("drainage_flag"));
                goods.setGoodsType(rss.getInt("goods_type"));
                goods.setShopId(rss.getString("shop_id"));
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
    public int queryForListCount(SingleQueryGoodsParam queryPm) {
        Connection conn28 = DBHelper.getInstance().getConnection6();
        String querySql = "select count(0) from single_goods_offers where 1=1 ";
        if (queryPm.getAdmid() > 0) {
            querySql += " and admin_id = ?";
        }
        if (!(queryPm.getPid() == null || "".equals(queryPm.getPid()))) {
            querySql += " and goods_pid = ?";
        }
        if (!(queryPm.getSttime() == null || "".equals(queryPm.getSttime()))) {
            querySql += " and create_time >= ? ";
        }
        if (!(queryPm.getEdtime() == null || "".equals(queryPm.getEdtime()))) {
            querySql += " and create_time <= ?";
        }
        if (queryPm.getDrainageFlag() > 0) {
            querySql += " and drainage_flag = ?";
        }
        if (queryPm.getGoodsType() > -1) {
            querySql += " and goods_type = ?";
        }
        PreparedStatement stmt = null;
        ResultSet rss = null;
        int count = 0;
        try {
            int total = 1;
            stmt = conn28.prepareStatement(querySql);
            if (queryPm.getAdmid() > 0) {
                stmt.setInt(total++, queryPm.getAdmid());
            }
            if (!(queryPm.getPid() == null || "".equals(queryPm.getPid()))) {
                stmt.setString(total++, queryPm.getPid());
            }
            if (!(queryPm.getSttime() == null || "".equals(queryPm.getSttime()))) {
                stmt.setString(total++, queryPm.getSttime());
            }
            if (!(queryPm.getEdtime() == null || "".equals(queryPm.getEdtime()))) {
                stmt.setString(total++, queryPm.getEdtime());
            }
            if (queryPm.getDrainageFlag() > 0) {
                stmt.setInt(total++, queryPm.getDrainageFlag());
            }
            if (queryPm.getGoodsType() > -1) {
                stmt.setInt(total++, queryPm.getGoodsType());
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
    public boolean deleteGoodsByPid(String pid) {

        // 非物理删除数据
        Connection conn28 = DBHelper.getInstance().getConnection6();
        String delete28 = "delete from  single_goods_offers where goods_pid = '" + pid + "'";

        Statement stmt28 = null;
        int rs28 = 0;
        try {
            stmt28 = conn28.createStatement();
            rs28 = stmt28.executeUpdate(delete28);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + ",deleteGoodsByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + ",deleteGoodsByPid error :" + e.getMessage());
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
        return rs28 > 0;
    }

    @Override
    public List<Integer> queryNoDealGoods() {

        Connection conn28 = DBHelper.getInstance().getConnection6();
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

        Connection conn28 = DBHelper.getInstance().getConnection6();
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
    public List<SingleGoodsCheck> queryCrossBorderGoodsForList(SingleGoodsCheck goodsCheck) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String selectSql = "select a.goods_pid,a.kj_local_path,a.kj_remote_path,a.kj_eninfo,a.pic as main_img," +
                "a.pics as imgs,a.is_pass,a.is_update,a.catid,b.`name` as category_name,a.shop_id,a.set_main_img," +
                "ifnull(c.main_img,'') as shop_main_img " +
                "from kj_single_goods_offers a LEFT JOIN 1688_category b on a.catid = b.category_id " +
                " left join kj_single_shop_offers c on a.shop_id = c.shop_id " +
                "where  a.kj_unzip_flag = 1 and a.crawl_flag =2 ";

        if (StringUtils.isNotBlank(goodsCheck.getPid())) {
            selectSql += " and a.goods_pid = ?";
        }
        if (StringUtils.isNotBlank(goodsCheck.getCatid())) {
            selectSql += " and a.catid in(select category_id from 1688_category  where find_in_set(?,path))";
        }
        if (StringUtils.isNotBlank(goodsCheck.getShopId())) {
            selectSql += " and a.shop_id = ?";
        }
        if (goodsCheck.getIsPass() > -1) {
            selectSql += " and a.is_pass =?";
        }
        if (goodsCheck.getIsUpdate() > -1) {
            selectSql += " and a.is_update =?";
        }
        selectSql += " order by a.shop_id";
        if (goodsCheck.getLimitNum() > 0) {
            selectSql += " limit ?,?";
        }
        List<SingleGoodsCheck> list = new ArrayList<SingleGoodsCheck>();
        PreparedStatement stmt31 = null;
        ResultSet rs = null;
        try {
            stmt31 = conn31.prepareStatement(selectSql);
            int count = 1;
            if (StringUtils.isNotBlank(goodsCheck.getPid())) {
                stmt31.setString(count++, goodsCheck.getPid());
            }
            if (StringUtils.isNotBlank(goodsCheck.getCatid())) {
                stmt31.setString(count++, goodsCheck.getCatid());
            }
            if (StringUtils.isNotBlank(goodsCheck.getShopId())) {
                stmt31.setString(count++, goodsCheck.getShopId());
            }
            if (goodsCheck.getIsPass() > -1) {
                stmt31.setInt(count++, goodsCheck.getIsPass());
            }
            if (goodsCheck.getIsUpdate() > -1) {
                stmt31.setInt(count++, goodsCheck.getIsUpdate());
            }
            if (goodsCheck.getLimitNum() > 0) {
                stmt31.setInt(count++, goodsCheck.getStartNum());
                stmt31.setInt(count++, goodsCheck.getLimitNum());
            }
            rs = stmt31.executeQuery();
            while (rs.next()) {
                SingleGoodsCheck goods = new SingleGoodsCheck();
                goods.setPid(rs.getString("goods_pid"));
                goods.setShopMainImg(rs.getString("shop_main_img"));
                goods.setMainImgSet(rs.getString("set_main_img"));
                goods.setRemotePath(rs.getString("kj_remote_path"));
                goods.setLocalPath(rs.getString("kj_local_path"));
                goods.setMainImg(rs.getString("main_img"));
                goods.setIsPass(rs.getInt("is_pass"));
                goods.setIsUpdate(rs.getInt("is_update"));
                goods.setImgs(rs.getString("imgs"));
                goods.setKjEninfo(rs.getString("kj_eninfo"));
                goods.setCatid(rs.getString("catid"));
                goods.setCategoryName(rs.getString("category_name"));
                goods.setShopId(rs.getString("shop_id"));

                list.add(goods);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryCrossBorderGoodsForList error :" + e.getMessage());
            LOG.error("queryCrossBorderGoodsForList error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return list;
    }

    @Override
    public int queryCrossBorderGoodsForListCount(SingleGoodsCheck goodsCheck) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String selectSql = "select count(0) from kj_single_goods_offers where  kj_unzip_flag = 1 and crawl_flag =2 ";
        if (StringUtils.isNotBlank(goodsCheck.getPid())) {
            selectSql += " and goods_pid = ?";
        }
        if (StringUtils.isNotBlank(goodsCheck.getCatid())) {
            selectSql += " and catid in(select category_id from 1688_category  where find_in_set(?,path))";
        }
        if (StringUtils.isNotBlank(goodsCheck.getShopId())) {
            selectSql += " and shop_id = ?";
        }
        if (goodsCheck.getIsPass() > -1) {
            selectSql += " and is_pass = ?";
        }
        if (goodsCheck.getIsUpdate() > -1) {
            selectSql += " and is_update =?";
        }
        PreparedStatement stmt31 = null;
        ResultSet rs = null;
        int total = 0;
        try {
            stmt31 = conn31.prepareStatement(selectSql);
            int count = 1;
            if (StringUtils.isNotBlank(goodsCheck.getPid())) {
                stmt31.setString(count++, goodsCheck.getPid());
            }
            if (StringUtils.isNotBlank(goodsCheck.getCatid())) {
                stmt31.setString(count++, goodsCheck.getCatid());
            }
            if (StringUtils.isNotBlank(goodsCheck.getShopId())) {
                stmt31.setString(count++, goodsCheck.getShopId());
            }
            if (goodsCheck.getIsPass() > -1) {
                stmt31.setInt(count++, goodsCheck.getIsPass());
            }
            if (goodsCheck.getIsUpdate() > -1) {
                stmt31.setInt(count++, goodsCheck.getIsUpdate());
            }
            rs = stmt31.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryCrossBorderGoodsForListCount error :" + e.getMessage());
            LOG.error("queryCrossBorderGoodsForListCount error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return total;
    }

    @Override
    public int updateSingleGoodsCheck(SingleGoodsCheck goodsCheck) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String selectSql = "update kj_single_goods_offers set is_pass = ?,is_update = 1 where goods_pid = ?";
        PreparedStatement stmt31 = null;
        int total = 0;
        try {
            stmt31 = conn31.prepareStatement(selectSql);
            stmt31.setInt(1, goodsCheck.getIsPass());
            stmt31.setString(2, goodsCheck.getPid());
            total = stmt31.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("updateSingleGoodsCheck error :" + e.getMessage());
            LOG.error("updateSingleGoodsCheck error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return total;
    }

    @Override
    public int insertIntoSingleGoodsByIsCheck(String pid) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String selectSql = "insert into single_goods_offers(goods_name, price,shop_id,bargain_number, standard, sku," +
                "color, pic, pics, pics1,iDetailData, iDetailConfig, detail,detail_url, weight, weight_url,addtime, " +
                "serviceid, tag_name,catid, keywords, average_deliver_time,dl_flag,set_weight,goods_pid,good_url,crawl_flag) " +
                "select goods_name, price,shop_id,bargain_number, standard, sku," +
                "color, pic, pics, pics1,iDetailData, iDetailConfig, detail,detail_url, weight, weight_url,addtime," +
                "serviceid, tag_name,catid, keywords, average_deliver_time,dl_flag,set_weight,goods_pid,good_url,crawl_flag " +
                "from kj_single_goods_offers where goods_pid ='" + pid + "'";
        String updateSql = "update kj_single_goods_offers set is_update = 1,is_pass = 2 where goods_pid = '" + pid + "'";
        String updateOffer = "update kj_catid_bak a, single_goods_offers b set b.catid=a.catid " +
                " where a.pid = b.goods_pid and b.catid='999999' and b.goods_pid = '" + pid + "'";
        Statement stmt31 = null;
        int total = 0;
        try {
            conn31.setAutoCommit(false);
            stmt31 = conn31.createStatement();
            stmt31.addBatch(selectSql);
            stmt31.addBatch(updateSql);
            stmt31.addBatch(updateOffer);
            total = stmt31.executeBatch().length;
            if (total > 0) {
                conn31.commit();
            } else {
                conn31.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + ",insertIntoSingleGoodsByIsCheck error :" + e.getMessage());
            LOG.error("pid:" + pid + ",insertIntoSingleGoodsByIsCheck error :" + e.getMessage());
            try {
                conn31.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            DBHelper.getInstance().closeStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return total;
    }

    @Override
    public int batchUpdateSingleGoodsCheck(List<SingleGoodsCheck> goodsList) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String selectSql = "update kj_single_goods_offers set is_pass = ?,is_update = 1 where goods_pid = ?";
        PreparedStatement stmt31 = null;
        int total = 0;
        try {
            stmt31 = conn31.prepareStatement(selectSql);
            for(SingleGoodsCheck goodsCheck : goodsList){
                stmt31.setInt(1, goodsCheck.getIsPass());
                stmt31.setString(2, goodsCheck.getPid());
                stmt31.addBatch();
            }
            total = stmt31.executeBatch().length;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("updateSingleGoodsCheck error :" + e.getMessage());
            LOG.error("updateSingleGoodsCheck error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return total;
    }

    @Override
    public List<CategoryBean> queryCategoryList(SingleGoodsCheck queryPm) {

        List<CategoryBean> list = new ArrayList<CategoryBean>();
        Connection conn31 = DBHelper.getInstance().getConnection6();
        String selectSql = "select cat1688.*,ifnull(cbm.total,0) as total from (select category_id as cid,name as category," +
                "path,lv from 1688_category  where lv > 0) cat1688  left join " +
                " (select catid,count(catid) as total  from kj_single_goods_offers " +
                " where kj_unzip_flag = 1 and crawl_flag = 2 ";
        if (queryPm.getIsPass() > -1) {
            selectSql += " and is_pass = ? ";
        }
        if (queryPm.getIsUpdate() > -1) {
            selectSql += " and is_update = ? ";
        }
        if (StringUtils.isNotBlank(queryPm.getPid())) {
            selectSql += " and goods_pid = ? ";
        }
        selectSql += " group by catid) cbm on cat1688.cid=cbm.catid order by cat1688.lv,cat1688.category asc ";
        PreparedStatement stmt31 = null;
        ResultSet rs = null;
        try {
            stmt31 = conn31.prepareStatement(selectSql);
            int count = 1;
            if (queryPm.getIsPass() > -1) {
                stmt31.setInt(count++, queryPm.getIsPass());
            }
            if (queryPm.getIsUpdate() > -1) {
                stmt31.setInt(count++, queryPm.getIsUpdate());
            }
            if (StringUtils.isNotBlank(queryPm.getPid())) {
                stmt31.setString(count++, queryPm.getPid());
            }

            rs = stmt31.executeQuery();
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
            System.err.println("queryCategoryList error :" + e.getMessage());
            LOG.error("queryCategoryList error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return list;
    }

    @Override
    public int setMainImgByPid(String pid, String imgUrl) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String updateSql = "update kj_single_goods_offers set set_main_img = ? where goods_pid = ?";
        PreparedStatement stmt31 = null;
        int rs = 0;
        try {
            stmt31 = conn31.prepareStatement(updateSql);
            stmt31.setString(1, imgUrl);
            stmt31.setString(2, pid);

            rs = stmt31.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + pid + ",setMainImgByPid error :" + e.getMessage());
            LOG.error("pid:" + pid + ",setMainImgByPid error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return rs;
    }

    @Override
    public int setMainImgByShopId(String shopId, String imgUrl) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String updateSql = "update kj_single_shop_offers set main_img = ? where shop_id = ?";
        PreparedStatement stmt31 = null;
        int rs = 0;
        try {
            stmt31 = conn31.prepareStatement(updateSql);
            stmt31.setString(1, imgUrl);
            stmt31.setString(2, shopId);

            rs = stmt31.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("shopId:" + shopId + ",setMainImgByShopId error :" + e.getMessage());
            LOG.error("shopId:" + shopId + ",setMainImgByShopId error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closePreparedStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return rs;
    }

    @Override
    public List<String> queryIsExistsPidFromSingleOffers(List<SingleGoodsCheck> pidList) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String sqlBegin = "select goods_pid from single_goods_offers where goods_pid in(";
        StringBuffer updateSql = new StringBuffer("");
        Statement stmt31 = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<>();
        try {
            for (SingleGoodsCheck goodsCheck : pidList) {
                if(goodsCheck.getIsPass() == 0){
                    if (StringUtils.isNotBlank(goodsCheck.getPid())) {
                        updateSql.append(",'" + goodsCheck.getPid() + "'");
                    }
                }
            }
            String exSql = sqlBegin + updateSql.toString().substring(1) + ")";
            stmt31 = conn31.createStatement();
            System.err.println(exSql);
            rs = stmt31.executeQuery(exSql);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryIsExistsPidFromSingleOffers error :" + e.getMessage());
            LOG.error("queryIsExistsPidFromSingleOffers error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closeStatement(stmt31);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return list;
    }

    @Override
    public boolean deleteSingleOffersByPids(List<String> pidList) {

        Connection conn31 = DBHelper.getInstance().getConnection6();
        String sqlOffersBegin = "delete from single_goods_offers where goods_pid in(";
        String sqlReadyBegin = "delete from useful_data.single_goods_ready where pid in(";
        StringBuffer updateSql = new StringBuffer("");
        Statement stmt31 = null;
        int rs = 0;
        try {
            stmt31 = conn31.createStatement();
            for (String pid : pidList) {
                if (StringUtils.isNotBlank(pid)) {
                    updateSql.append(",'" + pid + "'");
                }
            }
            stmt31.addBatch(sqlOffersBegin + updateSql.toString().substring(1) + ")");
            stmt31.addBatch(sqlReadyBegin + updateSql.toString().substring(1) + ")");
            rs = stmt31.executeBatch().length;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("deleteSingleOffersByPids error :" + e.getMessage());
            LOG.error("deleteSingleOffersByPids error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closeStatement(stmt31);
            DBHelper.getInstance().closeConnection(conn31);
        }
        return rs > 0;
    }

    @Override
    public int queryOnlineGoodsCountByShopId(String shopId) {

        Connection conn27 = DBHelper.getInstance().getConnection();
        String sql = "select count(1) from custom_benchmark_ready where shop_id = ? and valid = 1 ";
        //System.err.println("select count(1) from custom_benchmark_ready where shop_id = '"+shopId+"' and valid = 1 ");
        PreparedStatement stmt27 = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt27 = conn27.prepareStatement(sql);
            stmt27.setString(1,shopId);
            rs = stmt27.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("queryOnlineGoodsCountByShopId error :" + e.getMessage());
            LOG.error("queryOnlineGoodsCountByShopId error :" + e.getMessage());
        } finally {
            DBHelper.getInstance().closeStatement(stmt27);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn27);
        }
        return count;
    }

}
