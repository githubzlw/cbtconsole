package com.importExpress.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.HotEventsGoods;
import com.importExpress.pojo.HotEventsInfo;
import com.importExpress.service.HotEventsService;
import com.importExpress.utli.UserInfoUtils;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotEvents")
public class HotEventsController {
    private final static Logger logger = LoggerFactory.getLogger(HotEventsController.class);

    @Autowired
    private HotEventsService hotEventsService;
    @Autowired
    private CustomGoodsService customGoodsService;


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
            Map<Integer, List<HotEventsGoods>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(HotEventsGoods::getEventsId));
            goodsList.clear();

            CustomGoodsPublish goods;
            for (HotEventsInfo eventsInfo : infoList) {
                if (goodsMap.containsKey(eventsInfo.getId())) {
                    for (HotEventsGoods childGoods :  goodsMap.get(eventsInfo.getId())){
                        goods = customGoodsService.queryGoodsDetails(childGoods.getPid(), 0);
                        childGoods.setEnName(goods.getEnname());
                        if(goods.getShowMainImage().contains("http:") || goods.getShowMainImage().contains("https:")){
                            childGoods.setMainImg(goods.getShowMainImage());
                        }else{
                            childGoods.setMainImg(goods.getRemotpath() +  goods.getShowMainImage());
                        }

                    }
                    eventsInfo.setGoodsList(goodsMap.get(eventsInfo.getId()));
                }
            }
            goodsMap.clear();

            mv.addObject("isShow", 1);
            mv.addObject("list", infoList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("queryForList error:", e);
        }
        return mv;
    }

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

            hotEventsService.insertIntoHotEventsInfo(info);


            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertIntoHotEventsInfo error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }

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


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteIntoHotEventsGoods error:", e);
            json.setOk(false);
            json.setMessage("操作失败：" + e.getMessage());
        }
        return json;
    }


}
