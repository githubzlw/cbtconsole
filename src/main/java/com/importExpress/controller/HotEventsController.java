package com.importExpress.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.HotEventsGoods;
import com.importExpress.pojo.HotEventsInfo;
import com.importExpress.service.HotEventsService;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.OKHttpUtils;
import com.importExpress.utli.UserInfoUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotEvents")
public class HotEventsController {
    private final static Logger logger = LoggerFactory.getLogger(HotEventsController.class);
    private static OKHttpUtils okHttpUtils = new OKHttpUtils();

    @Autowired
    private HotEventsService hotEventsService;
    @Autowired
    private CustomGoodsService customGoodsService;


    /**
     * 列表页面查询数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("hotEvents");
        Admuser admuser = UserInfoUtils.getUserInfo(request);
        try {

            if (admuser == null || admuser.getId() == 0) {
                mv.addObject("isShow", 0);
                mv.addObject("message", "请登录后操作");
                return mv;
            }

            HotEventsInfo eventsInfoQuery = new HotEventsInfo();
            eventsInfoQuery.setIsOn(-1);
            List<HotEventsInfo> infoList = hotEventsService.queryForInfoList(eventsInfoQuery);
            HotEventsGoods eventsGoodsQuery = new HotEventsGoods();
            eventsGoodsQuery.setIsOn(-1);
            List<HotEventsGoods> goodsList = hotEventsService.queryForGoodsList(eventsGoodsQuery);
            genEventsListInfo(infoList, goodsList);
            goodsList.clear();

            mv.addObject("isShow", 1);
            mv.addObject("list", infoList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("queryForList error:", e);
        }
        return mv;
    }

    /**
     * 插入Events数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/insertIntoHotEventsInfo")
    @ResponseBody
    public JsonResult insertIntoHotEventsInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }
            String eventsId = request.getParameter("eventsId");
            String isOn = request.getParameter("isOn");
            String infoImg = request.getParameter("infoImg");
            String infoLink = request.getParameter("infoLink");
            String childName1 = request.getParameter("childName1");
            String childLink1 = request.getParameter("childLink1");
            String childName2 = request.getParameter("childName2");
            String childLink2 = request.getParameter("childLink2");
            String childName3 = request.getParameter("childName3");
            String childLink3 = request.getParameter("childLink3");
            if (StringUtils.isBlank(infoImg) || StringUtils.isBlank(infoLink) || StringUtils.isBlank(childName1) || StringUtils.isBlank(childLink1)
                    || StringUtils.isBlank(childName2) || StringUtils.isBlank(childLink2) || StringUtils.isBlank(childName3) || StringUtils.isBlank(childLink3)) {
                json.setOk(false);
                json.setMessage("获取数据异常请确认全部输入");
                return json;
            }


            HotEventsInfo info = new HotEventsInfo();
            info.setAdminId(admuser.getId());
            info.setImgUrl(infoImg);
            info.setLink(infoLink);
            info.setChildLink1(childLink1);
            info.setChildName1(childName1);
            info.setChildLink2(childLink2);
            info.setChildName2(childName2);
            info.setChildLink3(childLink3);
            info.setChildName3(childName3);
            info.setIsOn(1);

            if (StringUtils.isNotBlank(eventsId)) {
                info.setId(Integer.valueOf(eventsId));
                if (StringUtils.isNotBlank(isOn)) {
                    info.setIsOn(Integer.valueOf(isOn));
                }
                hotEventsService.updateIntoHotEventsInfo(info);
            } else {
                hotEventsService.insertIntoHotEventsInfo(info);
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertIntoHotEventsInfo error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }

    /**
     * 插入或者更新events下的商品
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/insertIntoHotEventsGoods")
    @ResponseBody
    public JsonResult insertIntoHotEventsGoods(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            String eventsId = request.getParameter("eventsId");
            String pid = request.getParameter("pid");
            if (StringUtils.isBlank(eventsId) || StringUtils.isBlank(pid)) {
                json.setOk(false);
                json.setMessage("获取数据失败");
                return json;
            }

            HotEventsGoods eventsGoods = new HotEventsGoods();
            eventsGoods.setEventsId(Integer.valueOf(eventsId));
            eventsGoods.setPid(pid);
            eventsGoods.setAdminId(admuser.getId());
            hotEventsService.insertIntoHotEventsGoods(eventsGoods);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertIntoHotEventsGoods error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }


    /**
     * 更新events数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updateIntoHotEventsInfo")
    @ResponseBody
    public JsonResult updateIntoHotEventsInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateIntoHotEventsInfo error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/updateIntoHotEventsGoods")
    @ResponseBody
    public JsonResult updateIntoHotEventsGoods(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateIntoHotEventsGoods error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/deleteIntoHotEventsInfo")
    @ResponseBody
    public JsonResult deleteIntoHotEventsInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteIntoHotEventsInfo error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }

    /**
     * 删除商品数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/deleteIntoHotEventsGoods")
    @ResponseBody
    public JsonResult deleteIntoHotEventsGoods(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }
            String eventsId = request.getParameter("eventsId");
            String pid = request.getParameter("pid");
            if (StringUtils.isBlank(eventsId) || StringUtils.isBlank(pid)) {
                json.setOk(false);
                json.setMessage("获取删除数据失败");
                return json;
            }

            HotEventsGoods eventsGoods = new HotEventsGoods();
            eventsGoods.setEventsId(Integer.valueOf(eventsId));
            eventsGoods.setPid(pid);
            hotEventsService.deleteIntoHotEventsGoods(eventsGoods);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteIntoHotEventsGoods error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }


    /**
     * 生成电商网站显示数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/genOnlineData")
    @ResponseBody
    public JsonResult genOnlineData(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            Admuser admuser = UserInfoUtils.getUserInfo(request);
            if (admuser == null || admuser.getId() == 0) {
                json.setOk(false);
                json.setMessage("请登录后操作");
                return json;
            }

            HotEventsInfo eventsInfoQuery = new HotEventsInfo();
            eventsInfoQuery.setIsOn(1);
            List<HotEventsInfo> infoList = hotEventsService.queryForInfoList(eventsInfoQuery);
            HotEventsGoods eventsGoodsQuery = new HotEventsGoods();
            eventsGoodsQuery.setIsOn(-1);
            List<HotEventsGoods> goodsList = hotEventsService.queryForGoodsList(eventsGoodsQuery);
            genEventsListInfo(infoList, goodsList);
            goodsList.clear();

            // infoList.stream().c
            Collections.sort(infoList, Comparator.comparing(HotEventsInfo::getId));

            File file = GoodsInfoUpdateOnlineUtil.writeDataToLocal("hotEvents.json", infoList.toString());
            boolean isSuccess = okHttpUtils.postFileNoParam(GoodsInfoUpdateOnlineUtil.ONLINE_EVENTS_URL, file);
            if (isSuccess) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("上传文件失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("genOnlineData error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }


    private void genEventsListInfo(List<HotEventsInfo> infoList, List<HotEventsGoods> goodsList) {
        // 分组
        Map<Integer, List<HotEventsGoods>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(HotEventsGoods::getEventsId));

        // 遍历生成数据
        CustomGoodsPublish goods;
        for (HotEventsInfo eventsInfo : infoList) {
            if (goodsMap.containsKey(eventsInfo.getId())) {
                for (HotEventsGoods childGoods : goodsMap.get(eventsInfo.getId())) {
                    goods = customGoodsService.queryGoodsDetails(childGoods.getPid(), 0);
                    childGoods.setEnName(goods.getEnname());
                    if (goods.getShowMainImage().contains("http:") || goods.getShowMainImage().contains("https:")) {
                        childGoods.setMainImg(goods.getShowMainImage().replace("http:","https:"));
                    } else {
                        childGoods.setMainImg(goods.getRemotpath().replace("http:","https:") + goods.getShowMainImage());
                    }
                    childGoods.setOnlineUrl(GoodsInfoUtils.genOnlineUrl(goods));
                    if (goods.getRangePrice() == null || "".equals(goods.getRangePrice()) || "[]".equals(goods.getRangePrice().trim())) {
                        if (Integer.valueOf(goods.getIsSoldFlag()) > 0) {
                            if (StringUtils.isBlank(goods.getFeeprice()) || "[]".equals(goods.getFeeprice().trim())) {
                                childGoods.setPrice(goods.getPrice().trim());
                            }else{
                                if (goods.getFeeprice().indexOf(",") > -1) {
                                    String[] prices = goods.getFeeprice().split(",");
									String[] price2 = prices[0].replace(" ", "").split("\\$");
									childGoods.setPrice(price2[1].replace("[", "").trim());
                                }
                            }
                        }else{
                            if (StringUtils.isBlank(goods.getWprice()) || "[]".equals(goods.getWprice().trim())) {
                                childGoods.setPrice(goods.getPrice().trim());
                            }else{
                                if (goods.getWprice().indexOf(",") > -1) {
                                    String[] prices = goods.getWprice().split(",");
									String[] price2 = prices[0].replace(" ", "").split("\\$");
									childGoods.setPrice(price2[1].replace("[", "").trim());
                                }
                            }
                        }
                    }else{
                        if(goods.getRangePrice().contains("-")){
                            childGoods.setPrice(goods.getRangePrice().split("-")[1].trim());
                        }else{
                            childGoods.setPrice(goods.getRangePrice().trim());
                        }
                    }
                    childGoods.setUnit(goods.getSellUnit());
                }
                eventsInfo.setGoodsList(goodsMap.get(eventsInfo.getId()));
            }
        }
        goodsMap.clear();
    }
}
