package com.cbt.report.ctrl;

import com.cbt.report.vo.UserBehaviorBean;
import com.cbt.report.vo.UserBehaviorDetails;
import com.cbt.report.vo.UserBehaviorTotal;
import com.cbt.util.Redis;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/behaviorStatistics")
public class UserBehaviorStatisticsController {

    private IOrderwsDao dao = new OrderwsDao();
    //private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "/getStatistics")
    @ResponseBody
    public JsonResult getStatistics(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtils.isBlank(admuserJson)) {
            json.setOk(false);
            json.setMessage("未登录，请重新登录后操作");
            return json;
        }
        String beginDate = request.getParameter("beginDate");
        if (StringUtils.isBlank(beginDate)) {
            json.setOk(false);
            json.setMessage("获取开始时间失败");
            return json;
        } else {
            beginDate += " 00:00:00";
        }
        String endDate = request.getParameter("endDate");
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59:59";
        }
        String ipFlagStr = request.getParameter("ipFlag");
        int ipFlag = 0;
        if(StringUtils.isNotBlank(ipFlagStr)){
            ipFlag = Integer.valueOf(ipFlagStr);
        }
        List<UserBehaviorBean> list = new ArrayList<UserBehaviorBean>(10);
        try {

            // 1.注册客户数量,过滤测试账号
            UserBehaviorBean register = new UserBehaviorBean();
            register.setTypeDesc("注册用户数量");
            register.setTypeFlag(1);
            int registerStatisticsNum = dao.statisticsRegisterUser(beginDate, endDate, ipFlag);
            register.setStatisticsNum(registerStatisticsNum);
            list.add(register);

            // 2.当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
            UserBehaviorBean addFirstAddress = new UserBehaviorBean();
            addFirstAddress.setTypeDesc("第一次录入收货地址的用户");
            addFirstAddress.setTypeFlag(2);
            int addFirstAddressNum = dao.statisticsAddFirstAddress(beginDate, endDate, ipFlag);
            addFirstAddress.setStatisticsNum(addFirstAddressNum);
            list.add(addFirstAddress);

            // 3.1本日有添加购物车的客户数量 （未注册的客户）
            UserBehaviorBean addCarWithNoRegisterUser = new UserBehaviorBean();
            addCarWithNoRegisterUser.setTypeDesc("有添加购物车的客户数量 （未注册的客户）");
            addCarWithNoRegisterUser.setTypeFlag(3);
            addCarWithNoRegisterUser.setIsShow(0);
            addCarWithNoRegisterUser.setIsExport(0);
            int addCarWithNoRegisterUserNum = dao.statisticsAddCarWithNoRegisterUser(beginDate, endDate, ipFlag);
            addCarWithNoRegisterUser.setStatisticsNum(addCarWithNoRegisterUserNum);
            list.add(addCarWithNoRegisterUser);

            // 3.2本日有添加购物车的客户数量 （有注册过的客户）
            UserBehaviorBean addCarWithHasRegisterUser = new UserBehaviorBean();
            addCarWithHasRegisterUser.setTypeDesc("有添加购物车的客户数量（有注册过的客户）");
            addCarWithHasRegisterUser.setTypeFlag(4);
            addCarWithHasRegisterUser.setIsShow(0);
            addCarWithHasRegisterUser.setIsExport(0);
            int addCarWithHasRegisterUserNum = dao.statisticsAddCarWithHasRegisterUser(beginDate, endDate, ipFlag);
            addCarWithHasRegisterUser.setStatisticsNum(addCarWithHasRegisterUserNum);
            list.add(addCarWithHasRegisterUser);

            // 3.3本日有添加购物车的客户数量 （老客户 （以前购买过））
            UserBehaviorBean addCarWithOldUser = new UserBehaviorBean();
            addCarWithOldUser.setTypeDesc("有添加购物车的客户数量（老客户，以前购买过）");
            addCarWithOldUser.setTypeFlag(5);
            addCarWithOldUser.setIsShow(0);
            addCarWithOldUser.setIsExport(0);
            int addCarWithOldUserNum = dao.statisticsAddCarWithOldUser(beginDate, endDate, ipFlag);
            addCarWithOldUser.setStatisticsNum(addCarWithOldUserNum);
            list.add(addCarWithOldUser);

            // 3.4本日有添加购物车的客户数量 （所有客户）
            UserBehaviorBean addCarWithAllUser = new UserBehaviorBean();
            addCarWithAllUser.setTypeDesc("有添加购物车的客户数量（所有客户）");
            addCarWithAllUser.setTypeFlag(6);
            addCarWithAllUser.setIsShow(0);
            addCarWithAllUser.setIsExport(0);
            addCarWithAllUser.setStatisticsNum(
                    addCarWithNoRegisterUserNum + addCarWithHasRegisterUserNum + addCarWithOldUserNum);
            list.add(addCarWithAllUser);

            // 4当日下单的 总客户数量 （点击数字后 显示 每人的用户ID 和邮箱）（过滤掉被取消的订单）（如果有拆单的情况，只 count
            // 母订单）
            // （过滤掉付款金额为0的订单，过滤掉 未付款已取消，过滤掉 等待付款）
            UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
            makeOrderAllUser.setTypeDesc("下单的总客户数量");
            makeOrderAllUser.setTypeFlag(7);
            int makeOrderAllUserNum = dao.statisticsMakeOrderAllUser(beginDate, endDate, ipFlag);
            makeOrderAllUser.setStatisticsNum(makeOrderAllUserNum);
            list.add(makeOrderAllUser);

            // 5当日下单的 新客户数量
            UserBehaviorBean makeOrderNewUser = new UserBehaviorBean();
            makeOrderNewUser.setTypeDesc("下单的新客户数量");
            makeOrderNewUser.setTypeFlag(8);
            int makeOrderNewUserNum = dao.statisticsMakeOrderNewUser(beginDate, endDate, ipFlag);
            makeOrderNewUser.setStatisticsNum(makeOrderNewUserNum);
            list.add(makeOrderNewUser);

            // 6当日付款按钮的用户数量 （同一用户只算一次， 注意过滤掉测试账号）
            UserBehaviorBean payOrderUser = new UserBehaviorBean();
            payOrderUser.setTypeDesc("付款按钮的用户数量");
            payOrderUser.setTypeFlag(9);
            int payOrderUserNum = dao.statisticsPayOrderUser(beginDate, endDate, ipFlag);
            payOrderUser.setStatisticsNum(payOrderUserNum);
            list.add(payOrderUser);

            // 7当日产品单页浏览总次数
            UserBehaviorBean RecentView = new UserBehaviorBean();
            RecentView.setTypeDesc("产品单页浏览总次数");
            RecentView.setTypeFlag(10);
            int recentViewNum = dao.statisticsRecentView(beginDate, endDate, ipFlag);
            RecentView.setStatisticsNum(recentViewNum);
            list.add(RecentView);

            // 8Pay按钮点击独特人数
            UserBehaviorBean payLogInfo = new UserBehaviorBean();
            payLogInfo.setTypeDesc("Pay按钮点击独特人数");
            payLogInfo.setTypeFlag(11);
            int payLogNum = dao.queryUserPayLog(beginDate, endDate, ipFlag);
	        payLogInfo.setStatisticsNum(payLogNum);
            list.add(payLogInfo);
            //9 点击添加购物车按钮数量
            UserBehaviorBean addToOrder = new UserBehaviorBean();
            addToOrder.setTypeDesc("Add to order 按钮点击次数");
            addToOrder.setTypeFlag(12);
            payLogNum = dao.queryBehaviorRecord(beginDate, endDate, ipFlag);
            addToOrder.setStatisticsNum(payLogNum);
            addToOrder.setIsShow(0);
            list.add(addToOrder);
            json.setOk(true);
            json.setData(list);

        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("查询错误,原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "/getStatisticsDatails")
    @ResponseBody
    public EasyUiJsonResult getStatisticsDatails(HttpServletRequest request, HttpServletResponse response) {
        EasyUiJsonResult json = new EasyUiJsonResult();

        String beginDate = request.getParameter("beginDate");
        if (StringUtils.isBlank(beginDate)) {
            json.setSuccess(false);
            json.setMessage("获取开始时间失败");
            return json;
        } else {
            beginDate += " 00:00:00";
        }
        String endDate = request.getParameter("endDate");
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59:59";
        }

        String typeStr = request.getParameter("typeFlag");
        String ipFlagStr = request.getParameter("ipFlag");
        int ipFlag = 0;
        if(StringUtils.isNotBlank(ipFlagStr)){
            ipFlag = Integer.valueOf(ipFlagStr);
        }
        String pageStr = request.getParameter("page");
        int page = 1;
        if (StringUtils.isNotBlank(pageStr)) {
            page = Integer.valueOf(pageStr);
        }
        String rowStr = request.getParameter("rows");
        int row = 20;
        if (StringUtils.isNotBlank(rowStr)) {
            row = Integer.valueOf(rowStr);
        }
        int total = 0;
        String totalStr = request.getParameter("total");
        if (StringUtils.isNotBlank(totalStr)) {
            total = Integer.valueOf(totalStr);
        }
        try {

            List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
            if ("1".equals(typeStr)) {
                // 1.注册客户数量,过滤测试账号
                list = dao.queryRegisterUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("2".equals(typeStr)) {
                // 2.当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
                list = dao.queryAddFirstAddressDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("3".equals(typeStr)) {
                // 3.1本日有添加购物车的客户数量 （未注册的客户）
                list = dao.queryAddCarWithNoRegisterUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("4".equals(typeStr)) {
                // 3.2本日有添加购物车的客户数量 （有注册过的客户）
                list = dao.queryAddCarWithHasRegisterUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("5".equals(typeStr)) {
                // 3.3本日有添加购物车的客户数量 （老客户 （以前购买过））
                list = dao.queryAddCarWithOldUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("6".equals(typeStr)) {
                // 3.4本日有添加购物车的客户数量 （所有客户）
                list = dao.queryAddCarWithOldUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("7".equals(typeStr)) {
                // 4当日下单的 总客户数量 （点击数字后 显示 每人的用户ID 和邮箱）（过滤掉被取消的订单）（如果有拆单的情况，只
                // count
                // 母订单）
                // （过滤掉付款金额为0的订单，过滤掉 未付款已取消，过滤掉 等待付款）
                list = dao.queryMakeOrderAllUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("8".equals(typeStr)) {
                // 5当日下单的 新客户数量
                list = dao.queryMakeOrderNewUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("9".equals(typeStr)) {
                // 6当日付款按钮的用户数量 （同一用户只算一次， 注意过滤掉测试账号）
                list = dao.queryPayOrderUserDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            } else if ("10".equals(typeStr)) {
                //产品单页浏览次数统计（同一用户一天访问多次同一个产品只算一次）
                list = dao.queryUserRecentView(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            }else if ("11".equals(typeStr)) {
                //Pay按钮点击独特人数
                list = dao.queryUserPayLogDetails(beginDate, endDate, (page - 1) * 20, row,ipFlag);
            }

            json.setSuccess(true);
            json.setRows(list);
            json.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("查询错误,原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping(value = "/getStatisticsByEveryDay")
    @ResponseBody
    public EasyUiJsonResult getStatisticsByEveryDay(HttpServletRequest request, HttpServletResponse response) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        String beginDateStr = request.getParameter("beginDate");

        String endDateStr = request.getParameter("endDate");
        if (StringUtils.isBlank(beginDateStr)) {
            json.setSuccess(false);
            json.setMessage("获取开始时间失败");
            return json;
        } else {
            beginDateStr += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endDateStr)) {
            endDateStr += " 23:59:59";
        }
        String ipFlagStr = request.getParameter("ipFlag");
        int ipFlag = 0;
        if(StringUtils.isNotBlank(ipFlagStr)){
            ipFlag = Integer.valueOf(ipFlagStr);
        }

        String typeStr = request.getParameter("typeFlag");
        try {

            List<String> dateList = genDateList(beginDateStr, endDateStr);
            List<UserBehaviorBean> list = new ArrayList<UserBehaviorBean>();
            if (dateList.isEmpty()) {
                json.setSuccess(false);
                json.setRows(list);
                return json;
            }
            if ("1".equals(typeStr)) {
                // 1.注册客户数量,过滤测试账号
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }

            } else if ("2".equals(typeStr)) {
                // 2.当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddFirstAddress(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("3".equals(typeStr)) {
                // 3.1本日有添加购物车的客户数量 （未注册的客户）
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithNoRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("4".equals(typeStr)) {
                // 3.2本日有添加购物车的客户数量 （有注册过的客户）
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithHasRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("5".equals(typeStr)) {
                // 3.3本日有添加购物车的客户数量 （老客户 （以前购买过））
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithOldUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("6".equals(typeStr)) {
                // 3.4本日有添加购物车的客户数量 （所有客户）
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int noRegisterUser = dao.statisticsAddCarWithNoRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    int hasRegisterUser = dao.statisticsAddCarWithHasRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    int oldUser = dao.statisticsAddCarWithOldUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(noRegisterUser + hasRegisterUser + oldUser);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("7".equals(typeStr)) {
                // 4当日下单的 总客户数量 （点击数字后 显示 每人的用户ID 和邮箱）（过滤掉被取消的订单）（如果有拆单的情况，只
                // count
                // 母订单）
                // （过滤掉付款金额为0的订单，过滤掉 未付款已取消，过滤掉 等待付款）
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsMakeOrderAllUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("8".equals(typeStr)) {
                // 5当日下单的 新客户数量
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsMakeOrderNewUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("9".equals(typeStr)) {
                // 6当日付款按钮的用户数量 （同一用户只算一次， 注意过滤掉测试账号）
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsPayOrderUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            }else if ("12".equals(typeStr)) {
                // 6当日付款按钮的用户数量 （同一用户只算一次， 注意过滤掉测试账号）
                for (int i = 1; i <= dateList.size() / 2; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.queryBehaviorRecord(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            }
            json.setSuccess(true);
            json.setRows(list);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("查询错误,原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/exportStatisticsExcel")
    @ResponseBody
    public void exportStatisticsExcel(HttpServletRequest request, HttpServletResponse response) {

        String beginDate = request.getParameter("beginDate");

        String endDate = request.getParameter("endDate");

        String typeStr = request.getParameter("typeFlag");
        String ipFlagStr = request.getParameter("ipFlag");
        int ipFlag = 0;
        if(StringUtils.isNotBlank(ipFlagStr)){
            ipFlag = Integer.valueOf(ipFlagStr);
        }
        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate) && StringUtils.isNotBlank(typeStr)) {
            OutputStream ouputStream = null;
	        beginDate += " 00:00:00";
	        endDate += " 23:59:59";
            try {

                String typeDesc = "";
                String typeEnDesc = "";
                List<UserBehaviorDetails> list = new ArrayList<UserBehaviorDetails>();
                if ("1".equals(typeStr)) {
                    // 1.注册客户数量,过滤测试账号
                    typeDesc = "注册客户";
                    typeEnDesc = "Registered";
                    list = dao.queryRegisterUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("2".equals(typeStr)) {
                    // 2.当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
                    typeDesc = "录入收货地址的客户";
                    typeEnDesc = "EnterTheReceivingAddress";
                    list = dao.queryAddFirstAddressDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("3".equals(typeStr)) {
                    // 3.1本日有添加购物车的客户数量 （未注册的客户）
                    typeDesc = "有添加购物车的客户(未注册)";
                    typeEnDesc = "AddShoppingCart(unregistered)";
                    list = dao.queryAddCarWithNoRegisterUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("4".equals(typeStr)) {
                    typeDesc = "有添加购物车的客户(已注册)";
                    typeEnDesc = "AddShoppingCart(registeredAndFirst)";
                    // 3.2本日有添加购物车的客户数量 （有注册过的客户）
                    list = dao.queryAddCarWithHasRegisterUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("5".equals(typeStr)) {
                    typeDesc = "有添加购物车的客户(老客户)";
                    typeEnDesc = "AddShoppingCart(registeredOld)";
                    // 3.3本日有添加购物车的客户数量 （老客户 （以前购买过））
                    list = dao.queryAddCarWithOldUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("6".equals(typeStr)) {
                    typeDesc = "有添加购物车的客户(所有客户)";
                    typeEnDesc = "AddShoppingCart(All)";
                    // 3.4本日有添加购物车的客户数量 （所有客户）
                    list = dao.queryAddCarWithOldUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("7".equals(typeStr)) {
                    // 4当日下单的 总客户数量 （点击数字后 显示 每人的用户ID 和邮箱）（过滤掉被取消的订单）（如果有拆单的情况，只
                    // count 母订单） （过滤掉付款金额为0的订单，过滤掉 未付款已取消，过滤掉 等待付款）
                    typeDesc = "下单的客户";
                    typeEnDesc = "MakeOrder(All)";
                    list = dao.queryMakeOrderAllUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("8".equals(typeStr)) {
                    // 5当日下单的 新客户数量
                    typeDesc = "下单的新客户";
                    typeEnDesc = "MakeOrder(New)";
                    list = dao.queryMakeOrderNewUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("9".equals(typeStr)) {
                    // 6当日付款按钮的用户数量 （同一用户只算一次， 注意过滤掉测试账号）
                    typeDesc = "点击付款按钮的客户";
                    typeEnDesc = "PayOrder";
                    list = dao.queryPayOrderUserDetails(beginDate, endDate, 0, 0, ipFlag);
                } else if ("10".equals(typeStr)) {
                    //产品单页浏览次数统计（同一用户一天访问多次同一个产品只算一次）
                    typeDesc = "产品单页浏览次数最多的50个商品";
                    typeEnDesc = "GoodsView";
                    list = dao.queryUserRecentView(beginDate, endDate, 0, 0, ipFlag);
                }else if ("11".equals(typeStr)) {
	                //Pay按钮点击独特人数
	                typeDesc = "Pay按钮点击独特人数";
	                typeEnDesc = "Pay_log";
	                list = dao.queryUserPayLogDetails(beginDate, endDate, 0, 0, ipFlag);
                }

                if (StringUtils.isNotBlank(endDate)) {
                    typeDesc = endDate.replace("-", "").substring(0, 8) + "-" + typeDesc;
                    typeEnDesc = endDate.replace("-", "").substring(0, 8) + "-" + typeEnDesc;
                } else {
                    typeDesc = "today-" + typeDesc;
                    typeEnDesc = "today-" + typeEnDesc;
                }
                if (StringUtils.isNotBlank(beginDate)) {
                    typeDesc = beginDate.replace("-", "").substring(0, 8) + "-" + typeDesc;
                    typeEnDesc = beginDate.replace("-", "").substring(0, 8) + "-" + typeEnDesc;
                }
                HSSFWorkbook wb = null;
                if ("10".equals(typeStr)) {
                    wb = genUserRecentView(list, typeDesc);
                }else  if ("11".equals(typeStr)) {
	                wb = genUserPayLog(list, typeDesc);
                } else {
                    wb = genUserInfoExcel(list, typeDesc);
                }
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition",
                        "attachment;filename=" + typeEnDesc + "-UserInfo.xls");
                response.setCharacterEncoding("utf-8");
                ouputStream = response.getOutputStream();
                wb.write(ouputStream);
                ouputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ouputStream != null) {
                        ouputStream.flush();
                        ouputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private HSSFWorkbook genUserPayLog(List<UserBehaviorDetails> list, String title) {
	    // 第一步，创建一个webbook，对应一个Excel文件
	    HSSFWorkbook wb = new HSSFWorkbook();
	    // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	    HSSFSheet sheet = wb.createSheet(title);
	    // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	    HSSFRow row = sheet.createRow((int) 0);
	    // 第四步，创建单元格，并设置值表头 设置表头居中
	    HSSFCellStyle style = wb.createCellStyle();
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

	    HSSFCell cell = row.createCell(0);
	    cell.setCellValue("日期");
	    cell.setCellStyle(style);
	    cell = row.createCell(1);
	    cell.setCellValue("邮箱");
	    cell.setCellStyle(style);
	    cell = row.createCell(2);
	    cell.setCellValue("订单号");
	    cell.setCellStyle(style);
	    cell = row.createCell(3);
	    cell.setCellValue("金额($)");
	    cell.setCellStyle(style);

	    for (int i = 0; i < list.size(); i++) {
		    row = sheet.createRow((int) i + 1);
		    UserBehaviorDetails usBh = (UserBehaviorDetails) list.get(i);
		    // 第四步，创建单元格，并设置值
		    row.createCell(0).setCellValue(usBh.getCreateTime());
		    row.createCell(1).setCellValue(usBh.getEmail());
		    row.createCell(2).setCellValue(usBh.getPid());
		    row.createCell(3).setCellValue(usBh.getOrderAmount());
	    }
	    return wb;
    }

    private HSSFWorkbook genUserRecentView(List<UserBehaviorDetails> list, String title) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(title);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("产品pid");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("浏览次数");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) i + 1);
            UserBehaviorDetails usBh = (UserBehaviorDetails) list.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(usBh.getPid());
            row.createCell(1).setCellValue(usBh.getUserId());
        }
        return wb;
    }

    private HSSFWorkbook genUserInfoExcel(List<UserBehaviorDetails> list, String title) {

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(title);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("邮箱");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("时间");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) i + 1);
            UserBehaviorDetails usBh = (UserBehaviorDetails) list.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(usBh.getUserId());
            row.createCell(1).setCellValue(usBh.getEmail());
            row.createCell(2).setCellValue(usBh.getCreateTime());
        }
        return wb;
    }

    @RequestMapping(value = "/getStatisticsFigure")
    @ResponseBody
    public JsonResult getStatisticsFigure(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        String beginDateStr = request.getParameter("beginDate");

        String endDateStr = request.getParameter("endDate");

        String ipFlagStr = request.getParameter("ipFlag");
        int ipFlag = 0;
        if(StringUtils.isNotBlank(ipFlagStr)){
            ipFlag = Integer.valueOf(ipFlagStr);
        }
        String typeStr = request.getParameter("typeFlag");
        try {

            List<String> dateList = genDateList(beginDateStr, endDateStr);
            if (dateList.isEmpty()) {
                json.setOk(false);
                json.setMessage("获取时间错误");
                return json;
            }
            List<UserBehaviorBean> list = new ArrayList<UserBehaviorBean>();
            if ("1".equals(typeStr)) {
                // 1.注册客户数量,过滤测试账号

                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }

            } else if ("2".equals(typeStr)) {
                // 2.当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddFirstAddress(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("3".equals(typeStr)) {
                // 3.1本日有添加购物车的客户数量 （未注册的客户）
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithNoRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("4".equals(typeStr)) {
                // 3.2本日有添加购物车的客户数量 （有注册过的客户）
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithHasRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("5".equals(typeStr)) {
                // 3.3本日有添加购物车的客户数量 （老客户 （以前购买过））
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithOldUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("6".equals(typeStr)) {
                // 3.4本日有添加购物车的客户数量 （所有客户）
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsAddCarWithOldUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("7".equals(typeStr)) {
                // 4当日下单的 总客户数量 （点击数字后 显示 每人的用户ID 和邮箱）（过滤掉被取消的订单）（如果有拆单的情况，只
                // count
                // 母订单）
                // （过滤掉付款金额为0的订单，过滤掉 未付款已取消，过滤掉 等待付款）
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsMakeOrderAllUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("8".equals(typeStr)) {
                // 5当日下单的 新客户数量
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsMakeOrderNewUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            } else if ("9".equals(typeStr)) {
                // 6当日付款按钮的用户数量 （同一用户只算一次， 注意过滤掉测试账号）
                for (int i = 0; i < dateList.size() - 1; i++) {
                    UserBehaviorBean makeOrderAllUser = new UserBehaviorBean();
                    int statisticsNum = dao.statisticsPayOrderUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                    makeOrderAllUser.setStatisticsNum(statisticsNum);
                    makeOrderAllUser.setRecordDate(dateList.get((i - 1) * 2).substring(0, 10));
                    list.add(makeOrderAllUser);
                }
            }

            json.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("查询错误,原因:" + e.getMessage());
        }
        return json;
    }

    private List<String> genDateList(String beginDateStr, String endDateStr) {
        Date beginDate = null;
        Date endDate = null;
        List<String> dateList = new ArrayList<String>();
        try {

            if (StringUtils.isBlank(beginDateStr)) {
                return dateList;
            } else {
                beginDate = format.parse(beginDateStr);
                dateList.add(beginDateStr);
                dateList.add(format.format(beginDate) + " 23:59:59");
                if (StringUtils.isNotBlank(endDateStr)) {
                    endDate = format.parse(endDateStr);
                } else {
                    endDate = new Date();
                }
                Calendar calenderTemp = Calendar.getInstance();
                while (beginDate.getTime() < endDate.getTime()) {
                    calenderTemp.setTime(beginDate);
                    calenderTemp.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
                    beginDate = calenderTemp.getTime();
                    dateList.add(format.format(beginDate) + " 00:00:00");
                    dateList.add(format.format(beginDate) + " 23:59:59");
                }
                //dateList.add(format.format(endDate) + " 00:00:00");
                //dateList.add(endDateStr);
                calenderTemp = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            beginDate = null;
            endDate = null;
        }
        return dateList;
    }


    @RequestMapping("/exportStatisticsAllExcel")
    @ResponseBody
    public void exportStatisticsAllExcel(HttpServletRequest request, HttpServletResponse response) {

        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String ipFlagStr = request.getParameter("ipFlag");
        int ipFlag = 0;
        if(StringUtils.isNotBlank(ipFlagStr)){
            ipFlag = Integer.valueOf(ipFlagStr);
        }
        List<UserBehaviorTotal> list = new ArrayList<UserBehaviorTotal>();

        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            OutputStream ouputStream = null;
            try {

                String typeEnDesc = beginDate + "@" + endDate;
                List<String> dateList = genDateList(beginDate, endDate);
                if (!dateList.isEmpty()) {

                    for (int i = 1; i <= dateList.size() / 2 ; i++) {

                        UserBehaviorTotal behaviorTotal = new UserBehaviorTotal();

                        behaviorTotal.setQueryTime(beginDate);

                        int totalUser = 0;
                        // 1.注册客户数量,过滤测试账号
                        int statisticsNum = dao.statisticsRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        behaviorTotal.setRegisterUserNum(statisticsNum);
                        behaviorTotal.setQueryTime(dateList.get((i - 1) * 2).substring(0, 10));

                        // 2.当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
                        statisticsNum = dao.statisticsAddFirstAddress(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        behaviorTotal.setFirstAddAdressNum(statisticsNum);

                        //3.1本日有添加购物车的客户数量 （未注册的客户）
                        statisticsNum = dao.statisticsAddCarWithNoRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        behaviorTotal.setAddCarNoRegisterNum(statisticsNum);
                        totalUser += statisticsNum;

                        //3.2本日有添加购物车的客户数量 （有注册过的客户）
                        statisticsNum = dao.statisticsAddCarWithHasRegisterUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        totalUser += statisticsNum;

                        //3.3本日有添加购物车的客户数量 （老客户 （以前购买过））
                        statisticsNum = dao.statisticsAddCarWithOldUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        totalUser += statisticsNum;

                        behaviorTotal.setAddCarAllUserNum(totalUser);

                        //4当日下单的 总客户数量 （点击数字后 显示 每人的用户ID 和邮箱）（过滤掉被取消的订单）（如果有拆单的情况，只
                        // count 母订单）（过滤掉付款金额为0的订单，过滤掉 未付款已取消，过滤掉 等待付款）
                        statisticsNum = dao.statisticsMakeOrderAllUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        behaviorTotal.setMarkOrderTotalNum(statisticsNum);


                        // 5当日下单的 新客户数量
                        statisticsNum = dao.statisticsMakeOrderNewUser(dateList.get((i - 1) * 2), dateList.get((i - 1) * 2 + 1), ipFlag);
                        behaviorTotal.setMarkOrderNewNum(statisticsNum);

                        list.add(behaviorTotal);
                    }
                    HSSFWorkbook wb = genTotalStatisticsExcel(list, typeEnDesc);
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-disposition",
                            "attachment;filename=" + typeEnDesc + "-TotalStatistics.xls");
                    response.setCharacterEncoding("utf-8");
                    ouputStream = response.getOutputStream();
                    wb.write(ouputStream);
                    ouputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ouputStream != null) {
                        ouputStream.flush();
                        ouputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private HSSFWorkbook genTotalStatisticsExcel(List<UserBehaviorTotal> list, String title) {

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(title);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("时间");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("有添加购物车的客户数量(未注册的客户)");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("有添加购物车的客户数量(有注册过的客户)");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("注册客户数量");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("第一次录入收货地址");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("下单的总客户数量");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("下单的新客户数量");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(list.get(i).getQueryTime());
            row.createCell(1).setCellValue(list.get(i).getAddCarNoRegisterNum());
            row.createCell(2).setCellValue(list.get(i).getAddCarAllUserNum());
            row.createCell(3).setCellValue(list.get(i).getRegisterUserNum());
            row.createCell(4).setCellValue(list.get(i).getFirstAddAdressNum());
            row.createCell(5).setCellValue(list.get(i).getMarkOrderTotalNum());
            row.createCell(6).setCellValue(list.get(i).getMarkOrderNewNum());
        }
        return wb;
    }


}
