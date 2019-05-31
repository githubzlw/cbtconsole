package com.importExpress.controller;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.ShopCarUserStatistic;
import com.importExpress.service.NewCustomersFollowService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/NewCustomersFollow")
public class NewCustomersFollowController {
    @Autowired
    private NewCustomersFollowService newCustomersFollowService;

    @RequestMapping("/CustomList")
    @ResponseBody
    public EasyUiJsonResult FindCustomList(@RequestParam(value = "allCus",defaultValue = "1")int allCus,@RequestParam(value = "follow",defaultValue = "-1")int follow,  HttpServletRequest request, HttpServletResponse response) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        ShopCarUserStatistic statistic = new ShopCarUserStatistic();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            if (!"0".equals(admuser.getRoletype())) {
                statistic.setFollowAdminId(admuser.getId());
            }
        }

        int startNum = 0;
        int limitNum = 30;
        String rowsStr = request.getParameter("rows");
        if (StringUtils.isNotBlank(rowsStr)) {
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (StringUtils.isNotBlank(pageStr)) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }


        String followIdStr = request.getParameter("followId");
        int followId = 0;
        if (StringUtils.isNotBlank(followIdStr)) {
            followId = Integer.parseInt(followIdStr);
        }

        String adminIdStr = request.getParameter("adminId");
        int adminId = 0;
        if (StringUtils.isNotBlank(adminIdStr)) {
            adminId = Integer.parseInt(adminIdStr);
        }

        String userIdStr = request.getParameter("userId");
        int userId = 0;
        if (StringUtils.isNotBlank(userIdStr)) {
            userId = Integer.parseInt(userIdStr);
        }

        String userEmail = request.getParameter("userEmail");
        if (StringUtils.isNotBlank(userEmail)) {
            statistic.setUserEmail(userEmail);
        }

        String isOrderStr = request.getParameter("isOrder");
        int isOrder = -1;
        if (StringUtils.isNotBlank(isOrderStr)) {
            isOrder = Integer.parseInt(isOrderStr);
        }

        double beginMoney = 0;
        String beginMoneyStr = request.getParameter("beginMoney");
        if (StringUtils.isNotBlank(beginMoneyStr)) {
            beginMoney = Double.parseDouble(beginMoneyStr);
        }

        double endMoney = -1;
        String endMoneyStr = request.getParameter("endMoney");
        if (StringUtils.isNotBlank(endMoneyStr)) {
            endMoney = Double.parseDouble(endMoneyStr);
        }

        int countryId = 0;
        String countryIdStr = request.getParameter("countryId");
        if (StringUtils.isNotBlank(countryIdStr)) {
            countryId = Integer.parseInt(countryIdStr);
        }
        statistic.setFollowAdminId(followId);
        statistic.setSaleId(adminId);
        statistic.setIsOrder(isOrder);
        statistic.setUserId(userId);
        statistic.setBeginMoney(beginMoney);
        statistic.setEndMoney(endMoney);
        statistic.setCountryId(countryId);
        statistic.setStartNum(startNum);
        statistic.setLimitNum(limitNum);
        statistic.setTotalPrice(allCus);
        statistic.setIsOrder(follow);
        json = this.newCustomersFollowService.FindCustomList(statistic,admuser);

        return json;
    }
    @RequestMapping("queryNewCustomByUserId")
    public ModelAndView queryNewCustomByUserId(@RequestParam("userEmail")String userEmail, @RequestParam("userId")int userId, @RequestParam(value = "page",defaultValue="1")int page, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("NewcustomCarByUsid");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (admuserJson == null) {
            mv.addObject("Message", "用户未登陆");
            return mv;
        }
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);


        List<ShopCarUserStatistic> list=this.newCustomersFollowService.queryNewCustomByUserId(userId,page);
        int total=this.newCustomersFollowService.queryNewCustomByUserIdCount(userId,page);

        mv.addObject("totalpage",(total+9)/10);
        mv.addObject("page",page);
        mv.addObject("pagesize",10);
        mv.addObject("total",total);
        mv.addObject("userId",userId);
        mv.addObject("userEmail",userEmail);
        mv.addObject("goodsList",list);
        mv.addObject("usname",admuser.getAdmName());
        mv.addObject("uspassword",admuser.getEmialpass());
        return mv;
    }
    @RequestMapping("queryAllSale")
    @ResponseBody
    public JsonResult queryAllSale() {
        JsonResult json = new JsonResult();
        try {
            List<ConfirmUserInfo> allAdms = this.newCustomersFollowService.queryAllSale();
            json.setOk(true);
            json.setData(allAdms);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("获取用户列表失败，原因 :" + e.getMessage());
        }
        return json;
    }

 @RequestMapping("queryCustomByUserId")
    @ResponseBody
    public JsonResult queryCustomByUserId(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String userid=request.getParameter("userid");
            json = this.newCustomersFollowService.queryCustomByUserId(userid);
        return json;
    }


}
