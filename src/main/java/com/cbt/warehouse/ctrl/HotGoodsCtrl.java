package com.cbt.warehouse.ctrl;

import com.cbt.bean.CustomGoodsBean;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.util.Md5Util;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.pojo.HotSellingCategory;
import com.cbt.warehouse.pojo.HotSellingGoods;
import com.cbt.warehouse.service.HotGoodsService;
import com.cbt.warehouse.util.CheckGoodsValid;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hotGoods")
public class HotGoodsCtrl {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(HotGoodsCtrl.class);

    @Autowired
    private HotGoodsService hotGoodsService;

    /**
     * 查询所有热卖类别
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryForList.do")
    @ResponseBody
    public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        try {
            // 查询所有数据库表中的类别
            List<HotSellingCategory> categoryLst = hotGoodsService.queryForList();
            json.setOk(true);
            json.setData(categoryLst);
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("获取热卖区类别失败，原因：" + e.getMessage());
            LOG.error("获取热卖区类别失败，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 根据热卖类别id更新数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateCategory.do")
    @ResponseBody
    public JsonResult updateCategory(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String idStr = request.getParameter("id");

        if (idStr == null || "".equals(idStr)) {
            json.setOk(false);
            json.setMessage("获取id失败");
            return json;
        }

        String showName = request.getParameter("showName");
        if (showName == null || "".equals(showName)) {
            json.setOk(false);
            json.setMessage("获取展示名称失败");
            return json;
        }

        String sortingStr = request.getParameter("sorting");
        if (sortingStr == null || "".equals(sortingStr)) {
            json.setOk(false);
            json.setMessage("获取排序数失败");
            return json;
        }

        String flagStr = request.getParameter("flag");
        if (flagStr == null || "".equals(flagStr)) {
            flagStr = "0";
        }

        try {
            HotSellingCategory category = new HotSellingCategory();
            category.setShowName(showName);
            category.setId(Integer.valueOf(idStr));
            category.setSorting(Integer.valueOf(sortingStr));
            category.setIsOn(flagStr);
            category.setUpdateAdmid(user.getId());
            // 更新本地
            hotGoodsService.updateHotSellingCategory(category);

            final HotSellingCategory nwCategory = category;
            new Thread() {
                public void run() {
                    try {
                        SendMQ sendMQ = new SendMQ();
                        StringBuilder sql = new StringBuilder();
                        sql.append("update hot_selling_category set ");
                        if (StringUtil.isNotBlank(nwCategory.getShowName())) {
                            String showName = nwCategory.getShowName();
                            if (showName.contains("'")) {
                                showName = showName.replace("'", "\\'");
                            }
                            if (showName.contains("\"")) {
                                showName = showName.replace("\"", "\\\"");
                            }
                            sql.append("show_name='" + showName + "',");
                        }
                        if (nwCategory.getSorting() != 0) {
                            sql.append("sorting=" + nwCategory.getSorting() + ",");
                        }
                        if (StringUtil.isNotBlank(nwCategory.getIsOn())) {
                            sql.append("is_on='" + nwCategory.getIsOn() + "',");
                        }
                        sql.append("update_admid = " + nwCategory.getUpdateAdmid() + ",update_time =SYSDATE()");
                        sql.append(" where id = " + category.getId());
                        sendMQ.sendMsg(new RunSqlModel(sql.toString()));
                        sendMQ.closeConn();
                    } catch (Exception e) {
                        LOG.error("修改线上热卖区类别失败，原因：" + e.getMessage());
                    }
                }
            }.start();

            json.setOk(true);
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("修改热卖区类别失败，原因：" + e.getMessage());
            LOG.error("修改热卖区类别失败，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 热卖类别id查询所有热卖商品
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryGoodsByCategoryId.do")
    @ResponseBody
    public JsonResult queryGoodsByCategoryId(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null || "".equals(categoryIdStr)) {
            json.setOk(false);
            json.setMessage("获取类别id失败");
            return json;
        }

        try {
            List<HotSellingGoods> goodsList = hotGoodsService.queryByHotSellingCategory(Integer.valueOf(categoryIdStr), 0);

            for (HotSellingGoods goods : goodsList) {
                String range_price = StrUtils.object2Str(goods.getRangePrice());
                if (StringUtils.isBlank(range_price)) {
                    if (goods.getIsSoldFlag() > 0) {
                        if (StringUtils.isNotBlank(goods.getFeeprice())) {
                            List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
                                    StrUtils.object2Str(goods.getFeeprice()));
                            if (matchStrList != null && !matchStrList.isEmpty()) {
                                range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                                if (matchStrList.size() > 1) {
                                    range_price = range_price + "-" + StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                                }
                            } else {
                                range_price = StrUtils.object2Str(goods.getGoodsPrice());
                            }
                        }
                    } else {
                        List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",StrUtils.object2Str(goods.getWprice()));
                        if (matchStrList != null && !matchStrList.isEmpty()) {
                            range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                            if (matchStrList.size() > 1) {
                                range_price = range_price + "-" + StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                            }
                        } else {
                            range_price = StrUtils.object2Str(goods.getGoodsPrice());
                        }
                    }
                }
                goods.setShowPrice(range_price);
                goods.setGoodsUrl("https://www.import-express.com/goodsinfo/" + goods.getShowName() + (goods.getIsNewCloud() > 0 ? "-3" : "-1") + goods.getGoodsPid() + ".html");
            }

            json.setOk(true);
            json.setData(goodsList);
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("获取类别商品失败，原因：" + e.getMessage());
            LOG.error("获取类别商品失败，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 修改热卖类别下的热卖商品
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateGoods.do")
    @ResponseBody
    public JsonResult updateGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String idStr = request.getParameter("id");

        if (idStr == null || "".equals(idStr)) {
            json.setOk(false);
            json.setMessage("获取id失败");
            return json;
        }

        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null || "".equals(categoryIdStr)) {
            json.setOk(false);
            json.setMessage("获取类别id失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");
        if (goodsPid == null || "".equals(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取goodsPid失败");
            return json;
        }

        String amazonPriceStr = request.getParameter("amazonPrice");
        if (amazonPriceStr == null || "".equals(amazonPriceStr)) {
            json.setOk(false);
            json.setMessage("获取亚马逊价格失败");
            return json;
        }
        String asinCode = request.getParameter("asinCode");
        if (asinCode == null || "".equals(asinCode)) {
            json.setOk(false);
            json.setMessage("获取ASIN码失败");
            return json;
        }
        String profitMarginStr = request.getParameter("profitMargin");
        if (profitMarginStr == null || "".equals(profitMarginStr)) {
            json.setOk(false);
            json.setMessage("获取利润率失败");
            return json;
        }

        String flagStr = request.getParameter("flag");
        if (flagStr == null || "".equals(flagStr)) {
            flagStr = "0";
        }

        try {
            HotSellingGoods hsGoods = new HotSellingGoods();
            hsGoods.setId(Integer.valueOf(idStr));
            hsGoods.setHotSellingId(Integer.valueOf(categoryIdStr));
            if (!(amazonPriceStr == null || "".equals(amazonPriceStr))) {
                BigDecimal sPrice = new BigDecimal(Float.valueOf(amazonPriceStr));
                hsGoods.setAmazonPrice(sPrice.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }
            hsGoods.setUpdateAdmid(user.getId());
            hsGoods.setIsOn(flagStr);
            hsGoods.setAsinCode(asinCode);
            hsGoods.setProfitMargin(Double.valueOf(profitMarginStr));
            hsGoods.setGoodsPid(goodsPid);
            // 更新本地
            hotGoodsService.updateHotSellingGoods(hsGoods);

            final HotSellingGoods nwHsGoods = hsGoods;
            new Thread() {
                public void run() {
                    try {
                        SendMQ sendMQ = new SendMQ();
                        StringBuilder sql = new StringBuilder();
                        sql.append("update hot_selling_goods set ");
                        if (StringUtil.isNotBlank(nwHsGoods.getIsOn())) {
                            sql.append(" is_on='" + nwHsGoods.getIsOn() + "',");
                        }
                        if (nwHsGoods.getAmazonPrice() != 0) {
                            sql.append("amazon_price=" + nwHsGoods.getAmazonPrice() + ",");
                        }
                        if (StringUtil.isNotBlank(nwHsGoods.getAsinCode())) {
                            sql.append("asin_code='" + nwHsGoods.getAsinCode() + "',");
                        }
                        if (nwHsGoods.getProfitMargin() != 0) {
                            sql.append("profit_margin=" + nwHsGoods.getProfitMargin() + ",");
                        }
                        sql.append("update_admid=" + nwHsGoods.getUpdateAdmid() + ",");
                        sql.append("update_time = SYSDATE()");
                        sql.append(" where hot_selling_id =" + nwHsGoods.getHotSellingId() + " and id =" + nwHsGoods.getId() + "");


                        System.err.println(sql.toString());
                        sendMQ.sendMsg(new RunSqlModel(sql.toString()));
                        sendMQ.closeConn();
                    } catch (Exception e) {
                        LOG.error("修改类别商品失败，原因：" + e.getMessage());
                    }
                }
            }.start();

            final String pid = goodsPid;
            if ("1".equals(flagStr)) {

                new Thread() {
                    public void run() {
                        try {
                            // 插入到 正在使用的热卖商品表，供张立伟和王宏杰判断商品是否能够下架使用
                            hotGoodsService.insertHotGoodsUse(pid);
                        } catch (Exception e) {
                            LOG.error("pid:" + pid + ",插入到“正在使用的热卖商品表”失败，原因：" + e.getMessage());
                        }
                    }
                }.start();
            }

            json.setOk(true);
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("修改类别商品失败，原因：" + e.getMessage());
            LOG.error("修改类别商品失败，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 新增热卖类别下的热卖商品
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveGoods.do")
    @ResponseBody
    public JsonResult saveGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null || "".equals(categoryIdStr)) {
            json.setOk(false);
            json.setMessage("获取类别id失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");
        if (goodsPid == null || "".equals(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品Pid失败");
            return json;
        }

        String goodsName = request.getParameter("goodsName");
        if (goodsName == null || "".equals(goodsName)) {
            goodsName = "";
        }

        String showName = request.getParameter("showName");
        if (showName == null || "".equals(showName)) {
            json.setOk(false);
            json.setMessage("获取展示名称失败");
            return json;
        }

        String goodsUrl = request.getParameter("goodsUrl");
        if (goodsUrl == null || "".equals(goodsUrl)) {
            json.setOk(false);
            json.setMessage("获取商品链接失败");
            return json;
        }

        String goodsImg = request.getParameter("goodsImg");
        if (goodsImg == null || "".equals(goodsImg)) {
            json.setOk(false);
            json.setMessage("获取商品图片链接失败");
            return json;
        }

        String amazonPriceStr = request.getParameter("amazonPrice");
        String asinCode = request.getParameter("asinCode");

        String profitMarginStr = request.getParameter("profitMargin");
        if (profitMarginStr == null || "".equals(profitMarginStr)) {
            profitMarginStr = "0";
        }

        String flagStr = request.getParameter("flag");
        if (flagStr == null || "".equals(flagStr)) {
            flagStr = "0";
        }

        try {
            SendMQ sendMQ = new SendMQ();
            // 校检存在的goodsPid数据
            boolean isExists = hotGoodsService.checkExistsGoods(Integer.valueOf(categoryIdStr), goodsPid);
            if (isExists) {
                json.setOk(false);
                json.setMessage("已经存在此PID的商品");
                return json;
            }

            HotSellingGoods hsGoods = new HotSellingGoods();
            hsGoods.setHotSellingId(Integer.valueOf(categoryIdStr));

            hsGoods.setGoodsName(goodsName);
            hsGoods.setShowName(showName);
            hsGoods.setGoodsImg(goodsImg);
            hsGoods.setGoodsUrl(goodsUrl);
            hsGoods.setGoodsPid(goodsPid);


            if (!(amazonPriceStr == null || "".equals(amazonPriceStr))) {
                BigDecimal gPrice = new BigDecimal(Float.valueOf(amazonPriceStr));
                hsGoods.setAmazonPrice(gPrice.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }
            if (!(asinCode == null || "".equals(asinCode))) {
                hsGoods.setAsinCode(asinCode);
            } else {
                hsGoods.setAsinCode("");
            }

            hsGoods.setProfitMargin(Double.valueOf(profitMarginStr));
            hsGoods.setCreateAdmid(user.getId());
            hsGoods.setIsOn(flagStr);

            // 切换数据源到线上
//			DataSourceSelector.set("dataSource127hop");
//			// 执行成功后，插入数据到线上
//			hotGoodsService.insertHotSellingGoods(hsGoods);


            if (showName.contains("'")) {
                showName = showName.replace("'", "\\'");
            }
            if (showName.contains("\"")) {
                showName = showName.replace("\"", "\\\"");
            }
            sendMQ.sendMsg(new RunSqlModel("insert into hot_selling_goods (hot_selling_id,goods_pid,show_name," +
                    "goods_url,goods_img,goods_price,is_on,profit_margin,selling_price,wholesale_price_1,wholesale_price_2," +
                    "wholesale_price_3,wholesale_price_4,wholesale_price_5,create_admid,amazon_price,asin_code) values(" + hsGoods.getHotSellingId() + "," + hsGoods.getGoodsPid() + "," +
                    "'" + showName + "'," +
                    "'" + hsGoods.getGoodsUrl() + "','" + hsGoods.getGoodsImg() + "','" + hsGoods.getGoodsPrice() + "','" + hsGoods.getIsOn() + "'," +
                    "'" + hsGoods.getProfitMargin() + "','" + hsGoods.getSellingPrice() + "','" + hsGoods.getWholesalePrice_1() + "','" + hsGoods.getWholesalePrice_2() + "'," +
                    "'" + hsGoods.getWholesalePrice_3() + "','" + hsGoods.getWholesalePrice_4() + "','" + hsGoods.getWholesalePrice_5() + "'," +
                    "'" + hsGoods.getCreateAdmid() + "','" + hsGoods.getAmazonPrice() + "','" + hsGoods.getAsinCode() + "')"));
            json.setOk(true);
            json.setMessage("保存线上类别商品成功，请等待数据拉取");
            sendMQ.closeConn();
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("保存类别商品失败，原因：" + e.getMessage());
            LOG.error("保存类别商品失败，原因：" + e.getMessage());
        } finally {
            DataSourceSelector.restore();
        }
        return json;
    }

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title deleteGoodsByPid
     * @Description 删除热卖类别下的热卖商品
     */
    @RequestMapping(value = "/deleteGoodsByPid.do")
    @ResponseBody
    public JsonResult deleteGoodsByPid(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null || "".equals(categoryIdStr)) {
            json.setOk(false);
            json.setMessage("获取类别id失败");
            return json;
        }

        String goodsPid = request.getParameter("goodsPid");
        if (goodsPid == null || "".equals(goodsPid)) {
            json.setOk(false);
            json.setMessage("获取商品Pid失败");
            return json;
        }

        try {

            // 删除本地goodsPid
            hotGoodsService.deleteGoodsByPid(Integer.valueOf(categoryIdStr), goodsPid);
            SendMQ sendMQ = new SendMQ();
            sendMQ.sendMsg(new RunSqlModel("delete from hot_selling_goods where hot_selling_id = " + categoryIdStr + " and goods_pid = '" + goodsPid + "'"));
            sendMQ.closeConn();
            final String newGoodsPid = goodsPid;
            new Thread() {
                @Override
                public void run() {
                    DataSourceSelector.set("localDataSource");
                    hotGoodsService.deleteHotUseGoodsByPid(newGoodsPid);
                }
            }.start();

            json.setOk(true);
            json.setMessage("删除成功");
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("删除成功，原因：" + e.getMessage());
            LOG.error("删除成功，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 自动获取热卖类别和热卖商品
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/genCategoryAndGoods.do")
    @ResponseBody
    public JsonResult genHotCategoryAndGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        try {
            hotGoodsService.genHotCategoryAndGoods();
            List<HotSellingGoods> goodsLst = hotGoodsService.queryForByTmp();
            List<HotSellingGoods> leftGoodsLst = new ArrayList<HotSellingGoods>();
            List<HotSellingGoods> nwGoodsLst = new ArrayList<HotSellingGoods>();
            System.out.println("本次获取商品数量：" + goodsLst.size());
            int deleteNum = 0;
            if (goodsLst != null && goodsLst.size() > 0) {
                for (HotSellingGoods sGoods : goodsLst) {
                    // 判断商品链接有效
                    if (CheckGoodsValid.parseGoodsByUrl(sGoods.getGoodsUrl(), 3)) {
                        // 去掉无图商品
                        if (!(sGoods.getGoodsImg() == null && "".equals(sGoods.getGoodsImg()))) {
                            leftGoodsLst.add(sGoods);
                        } else {
                            nwGoodsLst.add(sGoods);
                        }
                    } else {
                        nwGoodsLst.add(sGoods);
                    }
                }
                System.out.println("本次判断无效商品数量：" + nwGoodsLst.size());
                /*
                 * if (nwGoodsLst.size() > 0) { // 无效商品存入，循环删除无效商品 for
                 * (HotSellingGoods nwGoods : nwGoodsLst) {
                 * hotGoodsService.deleteHotSellingGoodsTmp(nwGoods.getId()); }
                 * }
                 */
                if (leftGoodsLst.size() > 0) {
                    // 有效商品存入，循环获取商品的pid
                    for (HotSellingGoods lfGoods : leftGoodsLst) {
                        String urlStr = lfGoods.getGoodsUrl();
                        String pid = "";
                        if (urlStr.lastIndexOf("/") > 0) {
                            pid = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.lastIndexOf(".html"));
                        }
                        if (!"".equals(pid)) {
                            // hotGoodsService.updateHotSellingGoodsPid(lfGoods.getId(),
                            // pid);
                            lfGoods.setGoodsPid(pid);
                        }
                        // 判断商品图片链接是否小图，如果是，删除小图的后缀
                        int indexOfNum = lfGoods.getGoodsImg().indexOf(".jpg");
                        int lastIndexOfNum = lfGoods.getGoodsImg().lastIndexOf(".jpg");
                        if (indexOfNum > 0 && indexOfNum < lastIndexOfNum) {
                            lfGoods.setGoodsImg(lfGoods.getGoodsImg().substring(0, indexOfNum + ".jpg".length()));
                        }
                    }
                }
                // 删除无图片链接的商品
                /*
                 * deleteNum = hotGoodsService.deleteHotSellingGoodsByImgUrl();
                 * System.out.println("本次删除无图商品数量：" + deleteNum); int count =
                 * hotGoodsService.updateHotSellingGoodsValid();
                 * System.out.println("本次更新有效商品标识数量：" + count);
                 */

            }

            // 执行成功后，插入数据到线上
            insertIntoOnline(leftGoodsLst);
            json.setOk(true);
            json.setMessage("本次获取有效商品数:" + (goodsLst.size() - nwGoodsLst.size() - deleteNum) + ",请等待数据拉取");
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("自动获取热卖类别和热卖商品失败，原因：" + e.getMessage());
            LOG.error("自动获取热卖类别和热卖商品失败，原因：" + e.getMessage());
        }
        return json;
    }

    private void insertIntoOnline(List<HotSellingGoods> gdLst) throws Exception {
        try {
            SendMQ sendMQ = new SendMQ();
            List<HotSellingCategory> ctLst = hotGoodsService.queryInsertCategory();
            if (ctLst != null && ctLst.size() > 0) {
                for (HotSellingCategory ctgy : ctLst) {
                    if (ctgy.getCategoryName().indexOf("\'") >= 0) {
                        ctgy.setCategoryName(ctgy.getCategoryName().replace("\'", "&apos;"));
                        ctgy.setShowName(ctgy.getShowName().replace("\'", "&apos;"));
                    }
                }
                for (HotSellingCategory h : ctLst) {
                    sendMQ.sendMsg(new RunSqlModel("insert into hot_selling_category (category_id,category_name,show_name,is_on,sorting) values " +
                            "('" + h.getCategoryId() + "','" + h.getCategoryName() + "','" + h.getShowName() + "','" + h.getIsOn() + "','" + h.getSorting() + "')"));
                }
            }
            if (gdLst != null && gdLst.size() > 0) {
                int count = 0;
                List<HotSellingGoods> nwGdLst = new ArrayList<HotSellingGoods>();
                for (HotSellingGoods hsGd : gdLst) {
                    count++;
                    nwGdLst.add(hsGd);
                    if (count % 10 == 0) {
                        if (nwGdLst.size() > 0) {
                            for (HotSellingGoods g : nwGdLst) {
                                sendMQ.sendMsg(new RunSqlModel("insert into hot_selling_goods (hot_selling_id,goods_pid,goods_name,show_name,goods_url," +
                                        "goods_img,goods_price,profit_margin,selling_price,goods_path) values ('" + g.getHotSellingId() + "','" + g.getGoodsPid() + "','" + g.getGoodsName() + "','" + g.getShowName() + "'," +
                                        "'" + g.getGoodsUrl() + "','" + g.getGoodsImg() + "','" + g.getGoodsPrice() + "'," +
                                        "'" + g.getProfitMargin() + "','" + g.getSellingPrice() + "','')"));
                            }
                            nwGdLst.clear();
                        }
                    }
                }
            }
            // 更新线上关联关系
//			hotGoodsService.updateRelationship();
            sendMQ.sendMsg(new RunSqlModel("update hot_selling_goods,hot_selling_category set hot_selling_goods.hot_selling_id = hot_selling_category.id " +
                    "where find_in_set(hot_selling_category.category_id,hot_selling_goods.goods_path)>0 and hot_selling_goods.hot_selling_id=0;"));
            sendMQ.closeConn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据1688url获取1688商品信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryGoodsFrom1688.do")
    @ResponseBody
    public JsonResult queryGoodsFrom1688(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String urlStr = request.getParameter("url");
        if (urlStr == null || "".equals(urlStr)) {
            json.setOk(false);
            json.setMessage("获取url失败，请重试");
            return json;
        } else {
            if (urlStr.indexOf("?") > -1) {
                urlStr = urlStr.substring(0, urlStr.indexOf("?"));
            }
        }
        // https://detail.1688.com/offer/38455907337.html
        String regex = "https://detail.1688.com/offer/";
        String pid = "";
        if (urlStr.indexOf(regex) > -1) {
            pid = urlStr.substring(regex.length(), urlStr.length() - ".html".length());
        } else {
            pid = urlStr;
        }
        try {
            System.err.println(pid);
            CustomGoodsBean goods = hotGoodsService.queryFor1688Goods(pid);

            if (goods == null || goods.getId() == 0) {
                json.setOk(false);
                json.setMessage("未获取到商品，请确认商品存在或未下架");
            } else {
                // goods.setRemotpath(goods.getRemotpath().replace("https://www.import-express.com/productimg/importsvimg",
                // "http://img.import-express.com/importcsvimg"));
                goods.setImg(goods.getImg().split(",")[0].replace("[", "").replace("]", ""));
                if (goods.getIsNewCloud() == 0) {
                    goods.setUrl("https://www.import-express.com/product/detail?&source=D"
                            + Md5Util.encoder(goods.getPid()) + "&item=" + goods.getPid());
                } else if (goods.getIsNewCloud() == 1) {
                    goods.setUrl("https://www.import-express.com/product/detail?&source=N"
                            + Md5Util.encoder(goods.getPid()) + "&item=" + goods.getPid());
                }

                if (goods.getName() == null || "".equals(goods.getName())) {
                    goods.setName("");
                }
                json.setOk(true);
                json.setData(goods);
            }
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("获取1688商品信息失败，原因：" + e.getMessage());
            LOG.error("获取1688商品信息失败，原因：" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/useHotGoods.do")
    @ResponseBody
    public JsonResult useHotGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        String pids = request.getParameter("pids");
        if (pids == null || "".equals(pids)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String categoryId = request.getParameter("categoryId");
        if(StringUtils.isBlank(categoryId)){
            json.setOk(false);
            json.setMessage("获取热卖类别ID失败");
            return json;
        }

        String stateStr = request.getParameter("state");
        if (stateStr == null || "".equals(stateStr)) {
            json.setOk(false);
            json.setMessage("获取状态参数失败");
            return json;
        }
        try {
            Map<String, String> pidsMap = new HashMap<String, String>();
            String[] pidList = pids.split(",");
            for (String pid : pidList) {
                pidsMap.put(pid, stateStr);
            }
            if (pidsMap.size() > 0) {
                hotGoodsService.useHotGoodsByState(pidsMap,Integer.valueOf(categoryId),user.getId());
                SendMQ sendMQ = new SendMQ();
                for (String key : pidsMap.keySet()) {
                    String value = pidsMap.get(key);
                    String sql = "update hot_selling_goods set is_on = " + value + " where goods_pid = '" + key + "'";
                    System.err.println(sql);
                    sendMQ.sendMsg(new RunSqlModel(sql));
                }
                sendMQ.closeConn();
                json.setOk(true);

                final String[] newPids = pidList;
                new Thread() {
                    public void run() {
                        if (!(newPids == null || newPids.length == 0)) {
                            // 插入到 正在使用的热卖商品表，供张立伟和王宏杰判断商品是否能够下架使用
                            for (String pid : newPids) {
                                try {
                                    hotGoodsService.insertHotGoodsUse(pid);
                                } catch (Exception e) {
                                    LOG.error("pid:" + pid + ",插入到“正在使用的热卖商品表”失败，原因：" + e.getMessage());
                                }
                            }
                        }
                    }
                }.start();
            } else {
                json.setOk(false);
                json.setMessage("获取参数失败，请重试");
            }
        } catch (Exception e) {
            e.getStackTrace();
            json.setOk(false);
            json.setMessage("更新热卖商品是否启用失败，原因：" + e.getMessage());
            LOG.error("更新热卖商品是否启用失败，原因：" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/calculateProfitMargin.do")
    @ResponseBody
    public JsonResult calculateProfitMargin(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }
        String amazonPriceStr = request.getParameter("amazonPrice");
        if (amazonPriceStr == null || "".equals(amazonPriceStr)) {
            json.setOk(false);
            json.setMessage("获取亚马逊价格失败");
            return json;
        }
        try {
            CustomGoodsBean goods = hotGoodsService.queryFor1688Goods(pid);
            if (goods == null || StringUtils.isEmpty(goods.getPrice())) {
                json.setOk(false);
                json.setMessage("获取当前商品的价格失败");
                return json;
            } else {
                double price = Double.valueOf(goods.getPrice());
                double amazonPrice = Double.valueOf(amazonPriceStr);
                BigDecimal bdMargin = new BigDecimal((amazonPrice - price) / amazonPrice * 100);
                json.setOk(true);
                json.setData(bdMargin.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setMessage("计算利润率失败，原因：" + e.getMessage());
            LOG.error("计算利润率失败，原因：" + e.getMessage());
        }
        return json;
    }

}
