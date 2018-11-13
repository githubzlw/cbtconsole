package com.cbt.warehouse.ctrl;

import com.cbt.bean.CustomGoodsBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.GroupBuyGoodsBean;
import com.cbt.bean.ShopGoodsInfo;
import com.cbt.service.SingleGoodsService;
import com.cbt.util.Md5Util;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.pojo.GroupBuyManageBean;
import com.cbt.warehouse.service.GroupBuyService;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/groupBuy")
public class GroupBuyManageCtrl {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GroupBuyManageCtrl.class);
    private List<com.cbt.pojo.Admuser> adminList = null;

    @Autowired
    private GroupBuyService groupBuyService;

    @Autowired
    private SingleGoodsService sgGsService;

    /**
     * 团购商品分页查询
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryForList")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        int startNum = 0;
        int limitNum = 20;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String idStr = request.getParameter("id");
        int id = 0;
        if (StringUtils.isNotBlank(idStr)) {
            id = Integer.valueOf(idStr);
        }
        String sttime = request.getParameter("sttime");
        if (sttime == null || "".equals(sttime)) {
            sttime = "";
        } else {
            sttime += " 00:00:00";
        }
        String edtime = request.getParameter("edtime");
        if (edtime == null || "".equals(edtime)) {
            edtime = "";
        } else {
            edtime += " 23:59:59";
        }
        String admidStr = request.getParameter("admid");
        int admid = 0;
        if (!(admidStr == null || "".equals(admidStr) || "0".equals(admidStr))) {
            admid = Integer.valueOf(admidStr);
        }
        String typeStr = request.getParameter("type");
        int type = -1;
        if (StringUtils.isNotBlank(typeStr)) {
            type = Integer.valueOf(typeStr);
        }
        try {
            // 查询所有数据库表中的类别
            List<GroupBuyManageBean> infos = groupBuyService.getGroupBuyInfos(id, sttime, edtime, type, startNum, limitNum);
            int total = groupBuyService.getGroupBuyInfosCount(id, sttime, edtime, type);
            if (infos.size() > 0) {
                dealRalationAdmin(infos);
            }
            json.setSuccess(true);
            json.setRows(infos);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("获取团购商品信息失败，原因：" + e.getMessage());
            LOG.error("获取团购商品信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    /**
     * 根据ID查询团购商品信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryInfoById")
    @ResponseBody
    public JsonResult queryInfoById(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String idStr = request.getParameter("id");
        int id = 0;
        if (StringUtils.isBlank(idStr)) {
            json.setOk(false);
            json.setMessage("获取ID失败");
        } else {
            id = Integer.valueOf(idStr);
        }
        try {
            GroupBuyManageBean info = groupBuyService.queryInfoById(id);
            json.setOk(true);
            json.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("获取团购商品信息失败，原因：" + e.getMessage());
            LOG.error("获取团购商品信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    /**
     * 检查最近一次添加的此商品是否在活动期内
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkExistsLatest")
    @ResponseBody
    public JsonResult checkExistsLatest(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
        }

        String activeBeginTime = request.getParameter("activeBeginTime");
        if (StringUtils.isBlank(activeBeginTime)) {
            json.setOk(false);
            json.setMessage("获取团购开始时间失败");
            return json;
        } else {
            activeBeginTime += " 00:00:00";
        }

        String effectiveDayStr = request.getParameter("effectiveDay");
        if (StringUtils.isBlank(effectiveDayStr)) {
            json.setOk(false);
            json.setMessage("获取团购天数失败");
            return json;
        }


        try {
            GroupBuyManageBean info = groupBuyService.queryLatestDateInfoByPid(pid);
            json.setOk(true);
            if (!(info == null || info.getId() == 0)) {
                //如果存在PID录入的商品，判断新的起始日期是否在上一个日期内
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date oldBeginDate = sdf.parse(info.getActiveBeginTime());
                Date oldEndDate = sdf.parse(info.getActiveEndTime());
                //新的开始日期
                Date nowBeginDate = sdf.parse(activeBeginTime);

                //新的结束日期
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(oldBeginDate);
                rightNow.add(Calendar.DAY_OF_YEAR, Integer.valueOf(effectiveDayStr));// 开始日期加上有效时间作为结束日期
                Date nowEndDate = rightNow.getTime();


                if (oldBeginDate.getTime() <= nowBeginDate.getTime() && nowBeginDate.getTime() <= oldEndDate.getTime()) {
                    json.setTotal(1L);
                    json.setData("当前PID：" + pid + "，存在活动的数据，最近一次活动编号：[" + info.getId() + "]，开始时间：" + info.getActiveBeginTime() + "，有效期：" + info.getEffectiveDay() + "天");
                } else if (oldBeginDate.getTime() <= nowEndDate.getTime() && nowEndDate.getTime() <= oldEndDate.getTime()) {
                    json.setTotal(1L);
                    json.setData("当前PID：" + pid + "，存在活动的数据，最近一次活动编号：[" + info.getId() + "]，开始时间：" + info.getActiveBeginTime() + "，有效期：" + info.getEffectiveDay() + "天");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("获取团购商品信息失败，原因：" + e.getMessage());
            LOG.error("获取团购商品信息失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/queryGoodsFrom1688")
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
            CustomGoodsBean goods = groupBuyService.queryFor1688Goods(pid);

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


    /**
     * 插入团购商品信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/insertGroupBuyInfos")
    @ResponseBody
    public JsonResult insertGroupBuyInfos(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        GroupBuyManageBean groupInfo = new GroupBuyManageBean();
        GroupBuyGoodsBean goods = new GroupBuyGoodsBean();
        goods.setAdminId(user.getId());
        goods.setIsMain(1);

        groupInfo.setAdminId(user.getId());

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        } else {
            goods.setPid(pid);
        }

        String finalPriceNeedNumStr = request.getParameter("finalPriceNeedNum");
        if (StringUtils.isBlank(finalPriceNeedNumStr)) {
            json.setOk(false);
            json.setMessage("获取最终价要求人数失败");
            return json;
        } else {
            groupInfo.setFinalPriceNeedNum(Integer.valueOf(finalPriceNeedNumStr));
        }

        String finalPriceStr = request.getParameter("finalPrice");
        if (StringUtils.isBlank(finalPriceStr)) {
            json.setOk(false);
            json.setMessage("获取最终价失败");
            return json;
        } else {
            groupInfo.setFinalPrice(Double.valueOf(finalPriceStr));
        }

        String initVirtualNumStr = request.getParameter("initVirtualNum");
        if (StringUtils.isBlank(initVirtualNumStr)) {
            json.setOk(false);
            json.setMessage("获取起始虚拟人数失败");
            return json;
        } else {
            groupInfo.setInitVirtualNum(Integer.valueOf(initVirtualNumStr));
        }

        String effectiveDayStr = request.getParameter("effectiveDay");
        if (StringUtils.isBlank(effectiveDayStr)) {
            json.setOk(false);
            json.setMessage("获取团购天数失败");
            return json;
        } else {
            groupInfo.setEffectiveDay(Integer.valueOf(effectiveDayStr));
        }

        String activeBeginTime = request.getParameter("activeBeginTime");
        if (StringUtils.isBlank(activeBeginTime)) {
            json.setOk(false);
            json.setMessage("获取团购开始时间失败");
            return json;
        } else {
            activeBeginTime += " 00:00:00";
            groupInfo.setActiveBeginTime(activeBeginTime);

            try {
                // 进行日期转换,计算结束日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(sdf.parse(activeBeginTime));
                rightNow.add(Calendar.DAY_OF_YEAR, groupInfo.getEffectiveDay());// 开始日期加上有效时间作为结束日期
                SimpleDateFormat sdfLt = new SimpleDateFormat("yyyy-MM-dd");
                groupInfo.setActiveEndTime(sdfLt.format(rightNow.getTime()) + " 23:59:59");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        String activeDesc = request.getParameter("activeDesc");
        if (StringUtils.isBlank(activeDesc)) {
            json.setOk(false);
            json.setMessage("获取拼团描述失败");
            return json;
        } else {
            groupInfo.setActiveDesc(activeDesc);
        }

        String isOnStr = request.getParameter("isOn");
        int isOn = 0;
        if (StringUtils.isNotBlank(isOnStr)) {
            isOn = 1;
        }
        groupInfo.setIsOn(isOn);
        goods.setIsOn(isOn);

        String groupTypeStr = request.getParameter("groupType");
        int groupType = 0;
        if (StringUtils.isNotBlank(groupTypeStr)) {
            groupType = Integer.valueOf(groupTypeStr);
        }
        groupInfo.setType(groupType);

        try {
            CustomGoodsPublish existsGoods = groupBuyService.queryFor1688Goods(pid);
            goods.setShopId(existsGoods.getShopId());
            int gbId = groupBuyService.insertGroupBuyInfos(groupInfo);
            if (gbId > 0) {

                goods.setGbId(gbId);
                List<GroupBuyGoodsBean> infos = new ArrayList<>();
                infos.add(goods);
                boolean isSuccess = groupBuyService.insertGroupBuyShopGoods(infos);
                if (isSuccess) {
                    json.setOk(true);
                } else {
                    json.setOk(false);
                    json.setMessage("插入团购商品信息失败，请重试");
                }
            } else {
                json.setOk(false);
                json.setMessage("插入团购商品信息失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("插入团购商品信息失败，原因：" + e.getMessage());
            LOG.error("插入团购商品信息失败，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 更新团购商品信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateGroupBuyInfos")
    @ResponseBody
    public JsonResult updateGroupBuyInfos(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("获取登录用户信息失败");
            return json;
        }

        GroupBuyManageBean groupInfo = new GroupBuyManageBean();

        String idStr = request.getParameter("id");
        if (StringUtils.isBlank(idStr) || "0".equals(idStr)) {
            json.setOk(false);
            json.setMessage("获取ID失败");
            return json;
        } else {
            groupInfo.setId(Integer.valueOf(idStr));
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        } else {
            groupInfo.setPid(pid);
        }

        String finalPriceNeedNumStr = request.getParameter("finalPriceNeedNum");
        if (StringUtils.isBlank(finalPriceNeedNumStr)) {
            json.setOk(false);
            json.setMessage("获取最终价要求人数失败");
            return json;
        } else {
            groupInfo.setFinalPriceNeedNum(Integer.valueOf(finalPriceNeedNumStr));
        }

        String finalPriceStr = request.getParameter("finalPrice");
        if (StringUtils.isBlank(finalPriceStr)) {
            json.setOk(false);
            json.setMessage("获取最终价失败");
            return json;
        } else {
            groupInfo.setFinalPrice(Double.valueOf(finalPriceStr));
        }

        String initVirtualNumStr = request.getParameter("initVirtualNum");
        if (StringUtils.isBlank(initVirtualNumStr)) {
            json.setOk(false);
            json.setMessage("获取起始虚拟人数失败");
            return json;
        } else {
            groupInfo.setInitVirtualNum(Integer.valueOf(initVirtualNumStr));
        }

        String effectiveDayStr = request.getParameter("effectiveDay");
        if (StringUtils.isBlank(effectiveDayStr)) {
            json.setOk(false);
            json.setMessage("获取团购天数失败");
            return json;
        } else {
            groupInfo.setEffectiveDay(Integer.valueOf(effectiveDayStr));
        }

        String activeBeginTime = request.getParameter("activeBeginTime");
        if (StringUtils.isBlank(activeBeginTime)) {
            json.setOk(false);
            json.setMessage("获取团购开始时间失败");
            return json;
        } else {
            activeBeginTime += " 00:00:00";
            groupInfo.setActiveBeginTime(activeBeginTime);

            try {
                // 进行日期转换,计算结束日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(sdf.parse(activeBeginTime));
                rightNow.add(Calendar.DAY_OF_YEAR, groupInfo.getEffectiveDay());// 开始日期加上有效时间作为结束日期
                SimpleDateFormat sdfLt = new SimpleDateFormat("yyyy-MM-dd");
                groupInfo.setActiveEndTime(sdfLt.format(rightNow.getTime()) + " 23:59:59");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        String activeDesc = request.getParameter("activeDesc");
        if (StringUtils.isBlank(activeDesc)) {
            json.setOk(false);
            json.setMessage("获取拼团描述失败");
            return json;
        } else {
            groupInfo.setActiveDesc(activeDesc);
        }

        String isOnStr = request.getParameter("isOn");
        int isOn = 0;
        if (StringUtils.isNotBlank(isOnStr) && "1".equals(isOnStr)) {
            isOn = 1;
        }
        groupInfo.setIsOn(isOn);

        String groupTypeStr = request.getParameter("groupType");
        int groupType = 0;
        if (StringUtils.isNotBlank(groupTypeStr)) {
            groupType = Integer.valueOf(groupTypeStr);
        }
        groupInfo.setType(groupType);

        try {
            boolean isSuccess = groupBuyService.updateGroupBuyInfos(groupInfo);
            if (isSuccess) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("更新团购商品信息失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("更新团购商品信息失败，原因：" + e.getMessage());
            LOG.error("更新团购商品信息失败，原因：" + e.getMessage());
        }
        return json;
    }

    /**
     * 删除团购商品信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/deleteGroupBuyInfos")
    @ResponseBody
    public JsonResult deleteGroupBuyInfos(HttpServletRequest request, HttpServletResponse response) {

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
        if (StringUtils.isBlank(idStr)) {
            json.setOk(false);
            json.setMessage("获取ID失败");
            return json;
        }

        try {
            boolean isSuccess = groupBuyService.deleteGroupBuyInfos(Integer.valueOf(idStr));
            if (isSuccess) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("删除团购商品信息失败，请重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("删除团购商品失败，原因：" + e.getMessage());
            LOG.error("删除团购商品失败，原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/chooseShopGoods")
    public ModelAndView chooseShopGoods(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("groupBuyShopGoodsAdd");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取店铺ID失败");
            return mv;
        }
        String gbId = request.getParameter("gbId");
        if (StringUtils.isBlank(gbId) || "0".equals(gbId)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取团购ID失败");
            return mv;
        }
        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取当前商品PID失败");
            return mv;
        }
        try {
            List<ShopGoodsInfo> goodsList = groupBuyService.queryShopGoodsByShopIdAndPid(shopId, pid);

            List<ShopGoodsInfo> existInfos = groupBuyService.queryShopGoodsFromGroupBuy(Integer.valueOf(gbId));

            genGoodsData(goodsList);
            for (ShopGoodsInfo spGoods : goodsList) {
                //判断是否有存在的商品
                if (!existInfos.isEmpty()) {
                    for (ShopGoodsInfo exInfo : existInfos) {
                        if (spGoods.getPid().equals(exInfo.getPid())) {
                            spGoods.setOnlineFlag(1);
                            break;
                        }
                    }
                }
            }

            Collections.sort(goodsList, new Comparator<ShopGoodsInfo>() {
                @Override
                public int compare(ShopGoodsInfo o1, ShopGoodsInfo o2) {
                    // 返回值为int类型，大于0表示正序，小于0表示逆序
                    return o2.getOnlineFlag() - o1.getOnlineFlag();
                }
            });
            mv.addObject("shopId", shopId);
            mv.addObject("gbId", gbId);
            mv.addObject("isShow", 1);
            mv.addObject("goodsList", goodsList);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取数据失败原因：" + e.getMessage());
            LOG.error("获取数据失败，原因：" + e.getMessage());
        }
        return mv;
    }


    @RequestMapping(value = "/addShopGoods")
    @ResponseBody
    public JsonResult addShopGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String shopId = request.getParameter("shopId");
        if (StringUtils.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }
        String gbId = request.getParameter("gbId");
        if (StringUtils.isBlank(gbId) || "0".equals(gbId)) {
            json.setOk(false);
            json.setMessage("获取团购ID失败");
            return json;
        }
        String infos = request.getParameter("infos");
        if (StringUtils.isBlank(infos)) {
            json.setOk(false);
            json.setMessage("获取选中商品失败");
            return json;
        }
        try {
            JSONArray jsonArray = JSONArray.fromObject(infos);// 把String转换为json
            List<ShopGoodsInfo> selectInfos = (List<ShopGoodsInfo>) JSONArray.toCollection(jsonArray, ShopGoodsInfo.class);
            if (selectInfos.isEmpty()) {
                json.setOk(false);
                json.setMessage("转换数据失败，请重试");
                return json;
            }

            GroupBuyManageBean info = groupBuyService.queryInfoById(Integer.valueOf(gbId));

            List<ShopGoodsInfo> existInfos = groupBuyService.queryShopGoodsFromGroupBuy(Integer.valueOf(gbId));
            List<GroupBuyGoodsBean> insertInfos = new ArrayList<GroupBuyGoodsBean>();
            List<GroupBuyGoodsBean> deleteInfos = new ArrayList<GroupBuyGoodsBean>();
            if (!existInfos.isEmpty()) {
                //判断已存在但本次未选中的商品，进行删除操作
                boolean isDelete = true;
                for (ShopGoodsInfo exInfo : existInfos) {
                    for (ShopGoodsInfo slInfo : selectInfos) {
                        if (slInfo.getPid().equals(exInfo.getPid())) {
                            isDelete = false;
                            break;
                        }
                    }
                    if (isDelete) {
                        if (!exInfo.getPid().equals(info.getPid())) {
                            GroupBuyGoodsBean gbBean = new GroupBuyGoodsBean();
                            gbBean.setAdminId(user.getId());
                            gbBean.setPid(exInfo.getPid());
                            gbBean.setShopId(shopId);
                            deleteInfos.add(gbBean);
                        }
                    }
                    isDelete = true;
                }
                if (!deleteInfos.isEmpty()) {
                    groupBuyService.deleteGroupBuyShopGoods(deleteInfos);
                }

                //判断选中的商品是否已经存在了，存在则不进行插入
                boolean isInsert = true;
                for (ShopGoodsInfo slInfo : selectInfos) {
                    for (ShopGoodsInfo exInfo : existInfos) {

                        if (slInfo.getPid().equals(exInfo.getPid())) {
                            isInsert = false;
                            break;
                        }
                    }
                    if (isInsert) {
                        GroupBuyGoodsBean gbBean = new GroupBuyGoodsBean();
                        gbBean.setAdminId(user.getId());
                        gbBean.setPid(slInfo.getPid());
                        gbBean.setShopId(shopId);
                        gbBean.setGbId(Integer.valueOf(gbId));
                        gbBean.setIsOn(info.getIsOn());
                        insertInfos.add(gbBean);
                    }
                    isInsert = true;
                }
                existInfos.clear();
                deleteInfos.clear();
            } else {
                for (ShopGoodsInfo slInfo : selectInfos) {
                    GroupBuyGoodsBean gbBean = new GroupBuyGoodsBean();
                    gbBean.setAdminId(user.getId());
                    gbBean.setPid(slInfo.getPid());
                    gbBean.setShopId(shopId);
                    gbBean.setGbId(Integer.valueOf(gbId));
                    gbBean.setIsOn(info.getIsOn());
                    insertInfos.add(gbBean);
                }
            }
            if (!insertInfos.isEmpty()) {
                groupBuyService.insertGroupBuyShopGoods(insertInfos);
            }
            insertInfos.clear();
            selectInfos.clear();
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("执行失败原因：" + e.getMessage());
            LOG.error("执行失败原因：" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/showShopGoods")
    public ModelAndView showShopGoods(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView("groupBuyShopGoodsShow");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "请登录后操作");
            return mv;
        }

        String gbId = request.getParameter("gbId");
        if (StringUtils.isBlank(gbId) || "0".equals(gbId)) {
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取店铺ID失败");
            return mv;
        }
        try {
            List<ShopGoodsInfo> infos = groupBuyService.queryShopGoodsFromGroupBuy(Integer.valueOf(gbId));

            if (!infos.isEmpty()) {
                genGoodsData(infos);
            }
            mv.addObject("isShow", 1);
            mv.addObject("infos", infos);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isShow", 0);
            mv.addObject("message", "获取数据失败原因：" + e.getMessage());
            LOG.error("获取数据失败，原因：" + e.getMessage());
        }
        return mv;
    }


    /**
     * 解析生成需要的data数据
     *
     * @param infos
     */
    private void genGoodsData(List<ShopGoodsInfo> infos) {
        for (ShopGoodsInfo spGoods : infos) {
            String range_price = StrUtils.object2Str(spGoods.getRangePrice());
            if (StringUtils.isBlank(range_price)) {
                List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
                        StrUtils.object2Str(spGoods.getWprice()));
                if (matchStrList != null && !matchStrList.isEmpty()) {
                    range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
                    if (matchStrList.size() > 1) {
                        range_price = range_price + "-"
                                + StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
                    }
                } else {
                    range_price = StrUtils.object2Str(spGoods.getPrice());
                }
            }
            spGoods.setShowPrice(range_price);
            // 完整图片路径
            if (!(spGoods.getRemotePath() == null || "".equals(spGoods.getRemotePath()))) {
                spGoods.setImgUrl(spGoods.getRemotePath() + spGoods.getImgUrl());
            }
            spGoods.setGoodsUrl("&source=D" + Md5Util.encoder(spGoods.getPid()) + "&item=" + spGoods.getPid());

            // 检查是否存在详情图片
            if (spGoods.getEnInfo() == null || "".equals(spGoods.getEnInfo())) {
                spGoods.setEnInfoNum(0);
            } else {
                // jsoup解析eninfo数据
                Document tempDoc = Jsoup.parseBodyFragment(spGoods.getEnInfo());
                Elements imgLst = tempDoc.getElementsByTag("img");
                if (!(imgLst == null || imgLst.size() == 0)) {
                    spGoods.setEnInfoNum(imgLst.size());
                }
                tempDoc = null;
            }
        }
    }

    private void dealRalationAdmin(List<GroupBuyManageBean> infos) {
        if (adminList == null || adminList.size() == 0) {
            adminList = sgGsService.queryAllAdmin();
        }
        for (GroupBuyManageBean good : infos) {
            if (good.getAdminId() > 0) {
                for (com.cbt.pojo.Admuser admin : adminList) {
                    if (good.getAdminId() == admin.getId()) {
                        good.setAdminName(admin.getAdmname());
                        break;
                    }
                }
            } else {
                continue;
            }
        }
    }

}
