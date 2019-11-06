package com.importExpress.controller;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.jcys.util.HttpUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.importExpress.pojo.GoodsReview;
import com.importExpress.pojo.OrderShare;
import com.importExpress.pojo.TimingWarningInfo;
import com.importExpress.pojo.UserBean;
import com.importExpress.service.QueryUserService;
import com.importExpress.utli.*;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/queryuser")
public class QueryUserController {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(QueryUserController.class);

    @Autowired
	private QueryUserService queryUserService;

    @Autowired
    private IWarehouseService iWarehouseService;
	
	/**
	 * http://127.0.0.1:8086/cbtconsole/queryuser/randomlist.do
	 * 查找指定用户
	   	1.连线上数据库 随机找以下几类客户
			a.一周之前登陆的
			b.二周之前登陆的
			c.一个月之前登陆的
			d.三个月
			e.半年之前登陆的
		2.每个取5个（含一个第三方登陆）且购物车有商品 且一半以上有订单
			所有的含有一个dropship客户用户且必须有订单
			购物车不同交期
			不同的国家
			一键登录
	 */
	@RequestMapping("/randomlist")
	@ResponseBody
	public Map<String, Object> queryuser(Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resutlMap = new HashMap<String, Object>();
		try {
			List<String> list = queryUserService.queryUser();
			if (null == list || list.size() < 1) {
				resutlMap.put("status", false);
				resutlMap.put("message", "未找到对应数据!");
				return resutlMap;
			}
			resutlMap.put("status", true);
			resutlMap.put("date", list);
		} catch (Exception e){
			resutlMap.put("status", false);
			resutlMap.put("message", "内部异常!");
		}
		return resutlMap;
	}
	
	/**
	 * http://127.0.0.1:8086/cbtconsole/queryuser/randombypricelist.do?price=1000
	 * 查找指定用户
	   	总消费金额1000$以上随机客户
	 */
	@RequestMapping("/randombypricelist")
	@ResponseBody
	public Map<String, Object> queryUserByPrice(Integer price) {
		Map<String, Object> resutlMap = new HashMap<String, Object>();
		try {
			List<String> list = queryUserService.queryUserByPrice(price);
			if (null == list || list.size() < 1) {
				resutlMap.put("status", false);
				resutlMap.put("message", "未找到对应数据!");
				return resutlMap;
			}
			resutlMap.put("status", true);
			resutlMap.put("date", list);
		} catch (Exception e){
			resutlMap.put("status", false);
			resutlMap.put("message", "内部异常!");
		}
		return resutlMap;
	}
	
	/**
	 * 非精确对标的 转化为 精确对标操作页面 
	 * 		查询待对标数据
	 * 		http://127.0.0.1:8086/cbtconsole/queryuser/querymarkedlist.do?rows=10
	 **/
	@RequestMapping("/querymarkedlist")
	@ResponseBody
	public Map<String, Object> queryMarkedList(
			@RequestParam(value = "rows", defaultValue = "50", required = false) Integer rows,
			@RequestParam(value = "orderby", defaultValue = "asc", required = false) String orderby) {
		if (null != orderby && "desc".endsWith(orderby.toLowerCase())) {
			orderby = "desc";
		} else {
			orderby = "asc";
		}
		return queryUserService.queryMarkedList(rows, orderby);
	}
	
	/**
	 * 非精确对标的 转化为 精确对标操作页面 
	 * 		查询待对标数据
	 * 		http://127.0.0.1:8086/cbtconsole/queryuser/updatemarkedbyid.do?id=10&marked=1
	 * 
	 * @param id 需要更新的商品id
	 * @param marked 更新的值 1-对标；2-不对标
	 * 
	 **/
	@RequestMapping("/updatemarkedbyid")
	@ResponseBody
	public String updateMarkedById(long id, Integer marked) {
		try {
			queryUserService.updateMarkedById(id, marked);
			return "id:" + id + ", marked:" + marked + ". update success";
		} catch (Exception e) {
			System.out.println(e);
		}
		return "id:" + id + ", marked:" + marked + ". update error";
	}
	
	
	/**
	 * 查询对标商品数据 
	 * 		数据准备 创建临时数据 及对应数据处理 返回数据生成时间
	 * 		http://127.0.0.1:8086/cbtconsole/queryuser/createstandardgoodsform.do
	 * 
	 **/
	@RequestMapping("/createstandardgoodsform")
	@ResponseBody
	public Map<String, Object> createStandardGoodsForm() {
		try {
			return queryUserService.createStandardGoodsForm();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	/**
	 * 线上静态页查询临时表生成 
	 * 		数据准备 创建临时数据 及对应数据处理 返回数据生成时间
	 * 		http://127.0.0.1:8086/cbtconsole/queryuser/createstaticizeform.do?flag=
	 * @param flag 标记 1-生成临时表并返回时间；2-只返回时间
	 * 
	 **/
	@RequestMapping("/createstaticizeform")
	@ResponseBody
	public String createStaticizeForm(Integer flag) {
		try {
			return queryUserService.createStaticizeForm(flag);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * 查询对标商品数据 
	 * 		临时数据 生成时间
	 * 		及部分数据总数
	 * 		http://127.0.0.1:8086/cbtconsole/queryuser/querystandardgoodsformcreatetime.do
	 * 
	 **/
	@RequestMapping("/querystandardgoodsformcreatetime")
	@ResponseBody
	public Map<String, Object> queryStandardGoodsFormCreatetime() {
		try {
			return queryUserService.queryStandardGoodsFormCreatetime();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
     * 查询对标商品数据 列表，有分页，有条件
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/querystandardgoodsformlist.do
     *
     * rows 每页行数			   为空则默认20
     * page 当前页			   为空则默认1
     * flag 标记      0-默认值 全部对标商品；1-优势对标商品；2-劣势对标商品；
     * bmFlag 标记      0-默认值 ；1-人为对标；2-非人为对标；
     * valid 标记      0-默认值 全部对标商品；1-在线；2-软下架；
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/querystandardgoodsformlist.do")
    @ResponseBody
    public EasyUiJsonResult queryStandardGoodsFormList(HttpServletRequest request,
    		Integer flag, Integer bmFlag, Integer valid) {
    	flag = (null == flag || flag == 0)?null:flag;
    	bmFlag = (null == bmFlag || bmFlag == 0)?null:bmFlag;
    	valid = (null == valid || valid == 0)?null:valid;
        //返回数据
        EasyUiJsonResult json = new EasyUiJsonResult();
        //分页参数接收并处理
        String rowsStr = request.getParameter("rows");
        Integer rows = 20;
        if (!(rowsStr == null || "".equals(rowsStr) || "0".equals(rowsStr))) {
            rows = Integer.valueOf(rowsStr);//无该参数时查询默认值20
        }
        String pageStr = request.getParameter("page");
        Integer page = 1;
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            page = Integer.valueOf(pageStr);//无该参数时查询默认值1
        }
        // 查询
        Map<String, Object> map = queryUserService.queryStandardGoodsFormList(page, rows, flag, bmFlag, valid);
        // 查询结果处理 并返回
        if (map != null && map.size() > 0) {
            json.setSuccess(true);
            json.setRows(map.get("recordList"));
            json.setTotal(Integer.parseInt(map.get("totalCount").toString()));
        } else {
            json.setRows("");
            json.setSuccess(false);
        }
        return json;
    }
	
    /**
     * ly 2018/10/11 10:26
	 * 查询后台入口信息 （这里查询的是 后台更新记录（账号&密码均为：share））
	 * 		http://127.0.0.1:8086/cbtconsole/queryuser/queryAuthInfo.do?authId=1103
	 * 
	 **/
	@RequestMapping("/queryAuthInfo")
	@ResponseBody
	public AuthInfo queryAuthInfo(Integer authId) {
		try {
			return queryUserService.queryAuthInfo(authId);
		} catch (Exception e) {
			LOG.error("queryAuthInfo[{}]",e.getMessage());
		}
		return null;
	}

    /**
     * ly  2018/11/30 17:10
     * 查询测试账号余额
     * http://127.0.0.1:8086/cbtconsole/queryuser/queryAvailable.do?email=20180202@qq.com
     */
    @RequestMapping("/queryAvailable")
    @ResponseBody
    public Map<String, Object> queryAvailable(String email) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(email) || !checkTestAccounts(email)){
                result.put("status", false);
                result.put("message", "email账号为空或者非测试账号!");
                return result;
            }
            String available = queryUserService.queryAvailable(email);
            if (StringUtils.isBlank(available)){
                result.put("status", false);
                result.put("message", "未找到该用户信息!");
            } else {
                result.put("status", true);
                result.put("available", available);
            }
            return result;
        } catch (Exception e){
            result.put("status", false);
            result.put("message", "内部异常");
            LOG.error("QueryUserController.queryAvailable error, email:" + email + ", result:" + result, e);
        }
        return result;
    }

    /**
     * ly  2018/11/30 17:10
     * 更新测试账号余额
     * http://127.0.0.1:8086/cbtconsole/queryuser/updateAvailable.do?email=20180202@qq.com&available=12.0
     */
    @RequestMapping("/updateAvailable")
    @ResponseBody
    public Map<String, Object> updateAvailable(String email, Double available) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(email) || !checkTestAccounts(email) || null == available){
                result.put("status", false);
                result.put("message", "email账号为空或非测试账号 或 修改金额问题!");
                return result;
            }
            long count = queryUserService.updateAvailable(email, available);
            if (count < 1){
                result.put("status", false);
                result.put("message", "未找到该用户,修改失败");
            } else {
                result.put("status", true);
                result.put("message", "修改成功");
            }
            return result;
        } catch (Exception e){
            result.put("status", false);
            result.put("message", "内部异常");
            LOG.error("QueryUserController.updateAvailable error, email:" + email + ", result:" + result, e);
        }
        return result;
    }

    /**
     * 判断邮箱是否是测试账号(这里测试账号需要包含 test 或者 @qq 或者 qq.)
     * @param email 被检测的邮箱
     * @return 如果是测试账号则返回true, 否则返回false
     */
    public static boolean checkTestAccounts(String email){
        if (email.indexOf("@qq") != -1
                || email.indexOf("qq.") != -1
                || email.indexOf("test") != -1){
            return true;
        }
        return false;
    }

    /**
     * ly  2018/12/06 13:45
     * 创建/修改后台功能入口
     * http://127.0.0.1:8086/cbtconsole/queryuser/updateAuthInfo.do
     */
    @RequestMapping(value = "/updateAuthInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateAuthInfo(String authName, String url, Integer moduleType,
                                              @RequestParam(value = "authId", required = false) Integer authId,
                                              @RequestParam(value = "reMark", required = false) String reMark,
                                              @RequestParam(value = "urlFlag", required = false) String urlFlag,
                                              @RequestParam(value = "colorFlag", required = false) String colorFlag) {
        Map<String, Object> result = new HashMap<String, Object>();
        AuthInfo authInfo = new AuthInfo();
        try {
            //参数校验
            if (StringUtils.isBlank(authName) || StringUtils.isBlank(url) || null == moduleType){
                result.put("status", false);
                result.put("message", "入口名称或入口链接未输入!");
                return result;
            }
            if (authId != null){
                authInfo.setAuthId(authId);
            }
            authInfo.setAuthName(authName);
            authInfo.setModuleType(moduleType);
            if (StringUtils.isNotBlank(reMark)){
                authInfo.setReMark(reMark);
            }
            //新增/修改
            long count = queryUserService.updateAuthInfo(authInfo, url, urlFlag, colorFlag);
            if (count > 0){
                result.put("status", true);
                result.put("message", "新增/修改后台入口成功!");
            } else {
                result.put("status", false);
                result.put("message", "新增/修改后台入口失败!");
            }
        } catch (Exception e){
            result.put("status", false);
            result.put("message", "内部异常");
            LOG.error("QueryUserController.updateAvailable error, authInfo:" + authInfo, e);
        }
        return result;
    }

    /**
     * ly  2019/01/10 11:20
     * 商品下架审核 中 刷新 有销量 加过购物车 等商品的临时数据
     * http://127.0.0.1:8086/cbtconsole/queryuser/refreshNeedOffShelfData.do
     */
    @RequestMapping(value = "/refreshNeedOffShelfData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> refreshNeedOffShelfData() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            queryUserService.updateNeedOffShelfData();
            result.put("status", false);
            result.put("message", "临时数据刷新成功!");
        } catch (Exception e){
            result.put("status", false);
            result.put("message", "刷新内部异常!");
            LOG.error("QueryUserController.refreshNeedOffShelfData error", e);
        }
        return result;
    }

    /**
     * 同步所有未同步的实秤重量到产品库（调用蒋的接口）
     * http://127.0.0.1:8086/cbtconsole/queryuser/synGoodsWeight.do
     *
     */
    @RequestMapping(value = "synGoodsWeight", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> synGoodsWeight(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            String sessionId = request.getSession().getId();
			String userJson = Redis.hget(sessionId, "admuser");
			com.cbt.website.userAuth.bean.Admuser user = (com.cbt.website.userAuth.bean.Admuser) SerializeUtil.JsonToObj(userJson, com.cbt.website.userAuth.bean.Admuser.class);
            //查询待同步数据
            List<String> pidList = queryUserService.queryGoodsWeightNoSyn();
            if (null == pidList || pidList.size() == 0){
                result.put("message", "未找到要同步实秤重量到产品库的商品");
                result.put("state", "success");
                return result;
            }
            //调用蒋接口同步
            for (String pid : pidList) {
                // iWarehouseService.saveWeightFlag(pid, user.getId());
            }
            result.put("message", "success");
            return result;
        } catch (Exception e){
            result.put("message", "err");
            return result;
        }
    }

    /**
     * 查询未下单的指定用户
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/list.do
     *
     * rows 每页行数			   为空则默认20
     * page 当前页			   为空则默认1
     * userType 查询用户类别 		1-录入收货地址 但没下单的客户;2-有Wechat号 但没下单的客户
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public EasyUiJsonResult queryUserList(HttpServletRequest request, Integer userType) {
        //分页参数接收并处理
        String rowsStr = request.getParameter("rows");
        Integer rows = 20;
        if (!(rowsStr == null || "".equals(rowsStr) || "0".equals(rowsStr))) {
            rows = Integer.valueOf(rowsStr);//无该参数时查询默认值20
        }
        String pageStr = request.getParameter("page");
        Integer page = 1;
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            page = Integer.valueOf(pageStr);//无该参数时查询默认值1
        }
        //查询时间范围参数接收
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if (StringUtils.isNotBlank(startDate)) {
            startDate += " 00:00";
        } else {
            startDate = null;
        }
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59";
        } else {
            endDate = null;
        }
        // 查询
        return queryUserService.queryUserList(page, rows, userType, startDate, endDate);
    }

    /**
     * 查询未下单的指定用户 数据导出
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/listCsv.do
     *
     * userType 查询用户类别 		1-录入收货地址 但没下单的客户;2-有Wechat号 但没下单的客户...
     *
     */
    @RequestMapping(value = "/listCsv.do")
    @ResponseBody
    public Map<String, String> queryUserListCsv(HttpServletRequest request, Integer userType) {
        //查询时间范围参数接收
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if (StringUtils.isNotBlank(startDate)) {
            startDate += " 00:00";
        } else {
            startDate = null;
        }
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59";
        } else {
            endDate = null;
        }
        // 查询
        return queryUserService.queryUserListCsv(userType, startDate, endDate);
    }

    /**
     * 查询用户相关信息
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/queryUserOtherInfo.do
     *
     * id 用户id
     * userType 查询用户类别 		1-录入收货地址 但没下单的客户;2-有Wechat号 但没下单的客户
     */
    @RequestMapping(value = "/queryUserOtherInfo.do")
    @ResponseBody
    public Map<String, Object> queryUserOtherInfo(Integer id, Integer userType) {
        return queryUserService.queryUserOtherInfo(id, userType);
    }

    @RequestMapping("/updateNeedoffshellEditFlag")
    @ResponseBody
    public Map<String, Object> updateNeedoffshellEditFlag(String pids) {
        return queryUserService.updateNeedoffshellEditFlag(pids);
    }

    /**
     * 查询未下单的指定用户
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/goodsReviewList.do
     *
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/queryGoodsReviewList.do")
    @ResponseBody
    public EasyUiJsonResult queryGoodsReviewList(@RequestParam(value = "rows", defaultValue = "20", required = false) Integer rows,
                                            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                            @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                                            @RequestParam(value = "endDate", defaultValue = "", required = false) String endDate,
                                            @RequestParam(value = "goodsPid", defaultValue = "", required = false) String goodsPid,
                                            @RequestParam(value = "reviewRemark", defaultValue = "", required = false) String reviewRemark,
                                            @RequestParam(value = "type", defaultValue = "-1", required = false) Integer type,
                                            @RequestParam(value = "reviewFlag", defaultValue = "-1", required = false) Integer reviewFlag) {
        if (StringUtils.isNotBlank(startDate)) {
            startDate += " 00:00";
        } else {
            startDate = null;
        }
        if (StringUtils.isNotBlank(endDate)) {
            endDate += " 23:59";
        } else {
            endDate = null;
        }
        if (StringUtils.isBlank(goodsPid)) {
            goodsPid = null;
        }
        if (StringUtils.isBlank(reviewRemark)) {
            reviewRemark = null;
        } else {
            reviewRemark = "%" + reviewRemark + "%";
        }
        // 查询
        return queryUserService.queryGoodsReviewList(page, rows, goodsPid, reviewRemark, type, reviewFlag, startDate, endDate);
    }


    /**
     * 查询要分享的订单数据
     *
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/queryOrderShareList.do")
    @ResponseBody
    public EasyUiJsonResult queryOrderShareList(@RequestParam(value = "rows", defaultValue = "20", required = false) Integer rows,
                                                 @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                                 @RequestParam(value = "shopType", defaultValue = "", required = false) String shopType,
                                                 @RequestParam(value = "orderNo", defaultValue = "", required = false) String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            orderNo = null;
        }
        // 查询
        return queryUserService.queryOrderShareList(page, rows, shopType,orderNo);
    }

    /**
     * 查询要分享的订单数据
     *
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/lookShareableOrder")
    @ResponseBody
    public EasyUiJsonResult lookShareableOrder(@RequestParam(value = "rows", defaultValue = "20", required = false) Integer rows,
                                                 @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                                 @RequestParam(value = "orderNo", defaultValue = "", required = false) String orderNo,
                                                 @RequestParam(value = "userId", defaultValue = "0", required = false) int userId,
                                                 @RequestParam(value = "share", defaultValue = "-1", required = false) int share,
                                                 @RequestParam(value = "name", defaultValue = "", required = false) String name) {
        if (StringUtils.isBlank(orderNo)) {
            orderNo = null;
        }
        if (StringUtils.isBlank(name)) {
            name = null;
        }
        // 查询
        return queryUserService.lookShareableOrder(page, rows,orderNo,name,userId,share);
    }

    /**
     * 查询要分享的订单数据
     *
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/queryOrderShareSave.do")
    @ResponseBody
    public String queryOrderShareSave(HttpServletRequest request, HttpServletResponse response) {

        String msg = "成功";
        //店铺类型
        String shopType = request.getParameter("shopType");
        String orderNo = request.getParameter("orderNo");
        //get the detail data
        String ens = request.getParameter("equations");


        List<OrderShare> deviceReturns= StrUtils.getPersons(ens, OrderShare.class);
        try {
            this.queryUserService.SetShareByOrderno(orderNo);
            //本地27插入
//            queryUserService.insertOrderShare(deviceReturns,shopType,orderNo);
            //线上表插入
            this.insertOrderShare(deviceReturns,shopType,orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            msg="操作失败";
            return msg;
        }
        return msg;
    }

    private void insertOrderShare(List<OrderShare> pList,String shopType,String orderNo) {
        shopType = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(shopType);
        for(int i=0;i<pList.size();i++ ){
            String sql = "INSERT INTO order_share (shop_type , order_no, goods_price,goods_img,goods_pid,yourorder)" +
                    " values('" + shopType + "','" + orderNo + "','" + pList.get(i).getGoodsPrice() + "','"+pList.get(i).getGoodsImg()+ "','" + pList.get(i).getGoodsPid() + "','" + pList.get(i).getYourorder() + "')";
            NotifyToCustomerUtil.sendSqlByMq(sql);
        }

    }



    /**
     * 根据id查询对应评论数据
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/queryGoodsReviewById.do?id=
     *
     */
    @RequestMapping(value = "/queryGoodsReviewById.do")
    @ResponseBody
    public GoodsReview queryGoodsReviewById(Integer id) {
        if (id == null || id == 0){
            return null;
        }
        return queryUserService.queryGoodsReviewById(id);
    }

    /**
     * 后台 商品下架审核中 需要下架但在购物车 或 已买过的 人为确认是否下架更新
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/updateNeedoffshelfByPid.do?pid=&noShelfInfo=
     *
     */
    @RequestMapping(value = "/updateNeedoffshelfByPid.do")
    @ResponseBody
    public Map<String, String> updateNeedoffshelfByPid(String pid, String noShelfInfo) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            queryUserService.upNeedoffshelfByPid(pid, noShelfInfo);
            result.put("message", "已修改!");
        } catch (Exception e) {
            result.put("message", "内部异常, 修改失败!");
        }
        return result;
    }


    /**
     * 后台 商品下架审核中 根据pid查询同款信息
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/querySameGoodsInfoByPid.do?pid=
     *
     */
    @RequestMapping(value = "/querySameGoodsInfoByPid.do")
    @ResponseBody
    public Map<String, Object> querySameGoodsInfoByPid(String pid) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            return queryUserService.querySameGoodsInfoByPid(pid);
        } catch (Exception e) {
            result.put("state", false);
            result.put("message", "内部异常!");
        }
        return result;
    }


    /**
     * 后台 商品下架(添加到指定表 隔天定时下架)
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/insertNeedoffDownAll.do?pids=&reason=
     *
     */
    @RequestMapping(value = "/insertNeedoffDownAll.do")
    @ResponseBody
    public Map<String, String> insertNeedoffDownAll(HttpServletRequest request, String pids, Integer reason) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            String sessionId = request.getSession().getId();
            String userJson = Redis.hget(sessionId, "admuser");
            Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
            if (user == null || user.getId() == 0) {
                result.put("message", "请登陆后再操作!");
                return result;
            }

            List<String> pidList = Arrays.asList(pids.split(","));
            if (CollectionUtils.isEmpty(pidList)) {
                result.put("message", "未找到下架的pid!");
                return result;
            }
            queryUserService.insertNeedoffDownAll(pidList, reason, user.getId());
            result.put("state", "true");
            result.put("message", "已修改!");
        } catch (Exception e) {
            result.put("message", "内部异常, 修改失败!");
        }
        return result;
    }

    /**
     * 后台 商品下架审核中 使用的所有商品下架原因及对应中文
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/queryUnsellablereasonMaster.do
     *
     */
    @RequestMapping(value = "/queryUnsellablereasonMaster.do")
    @ResponseBody
    public Map<String, Object> queryUnsellablereasonMaster() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            return queryUserService.queryUnsellablereasonMaster();
        } catch (Exception e) {
            result.put("state", false);
            result.put("message", "内部异常!");
        }
        return result;
    }

    /**
     * 根据订单号判断用户下单的网站
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/getSiteTypeNum.do?orderNo=
     *
     *  返回 1-非kids网站(importx网站); 2-kids网站;
     */
    @RequestMapping(value = "/getSiteTypeNum.do")
    @ResponseBody
    public Integer getSiteTypeNum(@RequestParam(value = "orderNo", defaultValue = "", required = false) String orderNo) {
        return MultiSiteUtil.getSiteTypeNum(orderNo);
    }

    /**
     * 后台模拟等日志记录
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/insertLoginLog.do
     *
     *  userid 模拟登陆的用户id
     *  site 1-非kids网站(importx网站); 2-kids网站;
     */
    @RequestMapping(value = "/insertLoginLog")
    @ResponseBody
    public Map<String, Object> insertLoginLog(HttpServletRequest request, Integer userid, Integer site){
        Map<String, Object> result = new HashMap<String, Object>();
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            result.put("message", "请登陆后台后再操作!");
            result.put("state", "false");
            return result;
        }
        UserBean bean = queryUserService.insertLoginLog(userid, user.getId(), site);
        result.put("bean", bean);
        result.put("webSiteUrl", MultiSiteUtil.getWebSiteUrl(site));
        try{
            String encode = URLEncoder.encode(DESUtils.encode(userid + ""));
            result.put("encode", encode);
        }catch(Exception e){
            LOG.error(" 加密用户名错误 USERID:{}", userid);
        }
        result.put("state", "true");
        return result;
    }

    /**
     * 美国用户品类屏蔽 后台的用户管理 更新用户是否 未满足$70美国用户
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/updateUserCheckout.do?userid=&type=
     *
     * @param userid 用户id
     * @param type 0-未满足$70美国用户; 1-满足$70美国用户;
     *
     */
    @RequestMapping(value = "/updateUserCheckout")
    @ResponseBody
    public Map<String, Object> updateUserCheckout(Integer userid, Integer type){
        //放入MQ
        Channel channel =null ;
        try{
            channel = SendMQ.getChannel();
            SendMQ.sendAuthorizationFlagMqSql(channel,userid, type);
        }finally {
            SendMQ.closeChannel(channel);
        }
        return queryUserService.updateUserCheckout(userid, type);
    }


    @RequestMapping(value = "/refreshCheckout")
    @ResponseBody
    public Map<String, String> refreshCheckout() throws IOException, TimeoutException {
        //放入MQ
        Map<String, String> map = new HashMap<>();
        int isFlag = 0;
        Channel channel =null ;
        // String updateTime = "2019-11-04 00:00:00";
        String updateTime = null;
        try {
            List<Integer> list = queryUserService.queryAllCheckout(isFlag, updateTime);
            System.err.println("flag:" + isFlag + ",size:" + list.size());
            int count = 0;
            if (CollectionUtils.isNotEmpty(list)) {
                channel = SendMQ.getChannel();
                for (Integer userid : list) {
                    count++;
                    if (count % 5 == 0) {
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /*theadPool.execute(() -> {
                        SendMQ.sendAuthorizationFlagMqSql(userid, isFlag);
                        queryUserService.updateUserCheckout(userid, isFlag);
                    });*/

                    SendMQ.sendAuthorizationFlagMqSql(channel ,userid, isFlag);
                    queryUserService.updateUserCheckout(userid, isFlag);
                }
            }
            map.put("success", "true");
            map.put("message", "执行成功，size:" + list.size());
            list.clear();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", "false");
            map.put("message", e.getMessage());
        }finally {
            if(channel != null){
                channel.close();
            }
        }
        System.err.println(map);

        int zoneFlag = 1;
        try {
            List<Integer> list = queryUserService.queryAllCheckout(zoneFlag, updateTime);

            System.err.println("flag:" + zoneFlag + ",size:" + list.size());
            if (CollectionUtils.isNotEmpty(list)) {
                channel = SendMQ.getChannel();
                int count = 0;
                for (Integer userid : list) {
                    count++;
                    if (count % 5 == 0) {
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /*theadPool.execute(() -> {
                        SendMQ.sendAuthorizationFlagMqSql(userid, zoneFlag);
                        queryUserService.updateUserCheckout(userid, zoneFlag);
                    });*/
                    SendMQ.sendAuthorizationFlagMqSql(channel, userid, zoneFlag);
                    queryUserService.updateUserCheckout(userid, zoneFlag);
                }
            }
            map.put("success", "true");
            map.put("message", "执行成功，size:" + list.size());
            list.clear();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", "false");
            map.put("message", e.getMessage());
        }finally {
            if(channel != null){
                channel.close();
            }
        }
        return map;
    }

    /**
     * 给出公司的各种爬虫列表，并监控运行状态 监控
     * 		http://127.0.0.1:8086/cbtconsole/queryuser/queryTimingWarningInfo.do
     *
     *
     */
    @RequestMapping(value = "/queryTimingWarningInfo")
    @ResponseBody
    public List<TimingWarningInfo> queryTimingWarningInfo(Integer valid,
                                                          @RequestParam(value = "day", defaultValue = "-1", required = false) Integer day){
        return queryUserService.queryTimingWarningInfo(valid, day);
    }


    /**
     * 刷新指定索引的预警
     *      http://127.0.0.1:8086/cbtconsole/queryuser/refreshTimingWarningInfo.do
     *
     * @param index 刷新对应索引的所有预警数据, 不传值则刷新所有
     *
     */
    @RequestMapping(value = "refreshTimingWarningInfo", method = RequestMethod.GET)
    @ResponseBody
    public String refreshTimingWarningInfo(@RequestParam(value = "index", defaultValue = "0", required = false) Integer index) {
        return HttpUtil.doGet("http://192.168.1.48:18079/syncsku/timingWarning/test1.do?index=" + index, "success", 3);
    }

    /**
     * 刷新预警阀值
     *      http://127.0.0.1:8086/cbtconsole/queryuser/queryQuotaData.do
     *
     */
    @RequestMapping(value = "queryQuotaData", method = RequestMethod.GET)
    @ResponseBody
    public TimingWarningInfo queryQuotaData(Integer id) {
        return queryUserService.queryQuotaData(id);
    }

    /**
     * 更新预警阀值
     *      http://127.0.0.1:8086/cbtconsole/queryuser/udpateQuotaData.do
     *
     */
    @RequestMapping(value = "udpateQuotaData")
    @ResponseBody
    public String udpateQuotaData(TimingWarningInfo bean) {
        if (bean.getId() == null) {
            return "fail";
        }
        queryUserService.udpateQuotaData(bean);
        return "success";
    }

}