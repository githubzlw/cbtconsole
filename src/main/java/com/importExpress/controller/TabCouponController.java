package com.importExpress.controller;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.util.Md5Util;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.CouponRedisBean;
import com.importExpress.pojo.TabCouponNew;
import com.importExpress.pojo.TabCouponRules;
import com.importExpress.pojo.TabCouponType;
import com.importExpress.service.TabCouponService;
import com.importExpress.utli.SearchFileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/coupon")
public class TabCouponController {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TabCouponController.class);
	
	@Autowired
	private TabCouponService tabCouponService;

	/**
     * 查询对标商品数据 列表，有分页，有条件
     * 		http://127.0.0.1:8086/cbtconsole/coupon/list.do
     *
     * rows 每页行数			   为空则默认20
     * page 当前页			   为空则默认1
     * typeCode 券的所属分类码 		目前分为三种：折扣券；代金券；满减券
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public EasyUiJsonResult queryTabCouponList(HttpServletRequest request, String typeCode, Integer valid, Integer timeTo, Integer couponSite) {
    	if (StringUtils.isBlank(typeCode) || "0".equals(typeCode)) {
    		typeCode = null;
		}
		if(valid == -1){
    	    valid = null;
        }
        if(timeTo == 0){
            timeTo = null;
        }
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
        Map<String, Object> map = tabCouponService.queryTabCouponList(page, rows, typeCode, valid, timeTo, couponSite);
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
     * 查询可用的卷类别
     * 		http://127.0.0.1:8086/cbtconsole/coupon/typecodelist.do
     * 
     * @return 返回可用卷类别集合
     **/
    @RequestMapping(value = "/typecodelist.do")
    @ResponseBody
    public List<TabCouponType> queryTabCouponTypeCodeList() {
    	List<TabCouponType> list = null;
    	try {
    		// 查询
    		list = tabCouponService.queryTabCouponTypeCodeList();
		} catch (Exception e) {
			
		}
        // 查询结果处理 并返回
        return list;
    }
	
    /**
     * 查询卷规则
     * 		http://127.0.0.1:8086/cbtconsole/coupon/ruleslist.do
     * 
     * @return 返回可用卷规则集合
     **/
    @RequestMapping(value = "/ruleslist.do")
    @ResponseBody
    public List<TabCouponRules> queryTabCouponRulesList() {
    	List<TabCouponRules> list = null;
    	try {
    		// 查询
    		list = tabCouponService.queryTabCouponRulesList();
		} catch (Exception e) {
			
		}
        // 查询结果处理 并返回
        return list;
    }
	
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * 新增折扣卷
     * 		http://127.0.0.1:8086/cbtconsole/coupon/addcoupon.do
     * 
     * @return 返回可用卷规则集合
     **/
    @RequestMapping(value = "/addcoupon.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> addCoupon(HttpServletRequest request,
                                         String couponCode, //卷码
                                         @RequestParam(value = "userids", defaultValue = "", required = false) String userids, //满减卷关联用户的id
                                         Integer type, //卷类别
                                         Integer shareFlag, //是否用于社交分享
                                         @RequestParam(value = "valueLeft", defaultValue = "0", required = false) Integer valueLeft, //慢减卷最低消费金额
                                         @RequestParam(value = "valueRight", defaultValue = "0", required = false) Integer valueRight, //满减卷优惠金额
                                         String describe, //卷描述
                                         @RequestParam(value = "count", defaultValue = "0", required = false) Integer count, //卷数量
                                         @RequestParam(value = "couponWebsiteType", defaultValue = "0", required = false) Integer couponWebsiteType, //优惠卷所在网站
                                         @RequestParam(value = "websiteType", defaultValue = "1", required = false) Integer websiteType, //网站名
                                         String fromTime,//领取开始时间
                                         String toTime//领取截止时间
    		) {
    	Map<String, String> resultMap = new HashMap<String, String>();
    	// 获取登录用户信息 
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (null == user) {
        	// 用户未登陆
        	resultMap.put("message", "用户未登陆,请登陆后重试!");
        	resultMap.put("code", "1");
        	resultMap.put("state", "false");
        	return resultMap;
		}
        //登陆用户id
        Integer userId = user.getId();
        //用于权限校验 直接给销售$6美元以下现金券的发放权限 超过$6的需要管理员权限
        if (valueRight >= 6){
            if (!"0".endsWith(user.getRoletype())){
                resultMap.put("message", "发放6$及超过6$的优惠卷需要管理员权限!");
                resultMap.put("code", "4");
                resultMap.put("state", "false");
                return resultMap;
            }
        }
        //数据解析
        Date fromDate = null;
        Date toDate = null;
    	try {
    		fromDate = simpleDateFormat.parse(fromTime);
    		toDate = simpleDateFormat.parse(toTime);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	//数据校验
        if (StringUtils.isBlank(couponCode) || StringUtils.isBlank(fromTime) || StringUtils.isBlank(toTime)
        		|| null == fromDate || null == toDate) {
			//数据问题
        	resultMap.put("message", "表单数据问题!");
        	resultMap.put("state", "false");
        	resultMap.put("code", "2");
        	return resultMap;
		}
        String value = valueLeft + "-" + valueRight;
    	String shareid = "";
    	if (shareFlag == 1){
            shareid = Md5Util.encoder(String.valueOf(System.currentTimeMillis())).substring(0, 6).toLowerCase();
    	    value += "-1-" + shareid;
        }
        //关联用户id
        List<String> useridList = new ArrayList<String>();
        if (shareFlag != 1 && StringUtils.isNotBlank(userids)){
            List<String> idList = Arrays.asList(userids.replace("，", ",").split(","));
            for (String userid : idList) {
                if (StringUtils.isNotBlank(userid) && StringUtils.isNumeric(userid.trim()) && !useridList.contains(userid.trim())){
                    useridList.add(userid.trim());
                }
            }
        }
        if (useridList.size() > count){
            resultMap.put("message", "关联的用户id数量多于创建的优惠卷数量!");
            resultMap.put("state", "false");
            return resultMap;
        }
        //表单数据 (String id, String count, String leftCount, String describe, String value, String from, String to, String type, String valid)
        CouponRedisBean couponRedis = new CouponRedisBean(couponCode, count.toString(), count.toString(),
        		describe, value, new Long(fromDate.getTime()).toString(), new Long(toDate.getTime()).toString(),
        		type.toString(), "1");
        //String id, Integer count, Integer leftCount, String describe, String value, Date from, Date to, int type, int valid, Integer userid
        TabCouponNew tabCouponNew = new TabCouponNew(couponCode, count, count, describe,
        		value, fromDate, toDate, type, 1, userId, shareFlag, websiteType, couponWebsiteType);
        try {
            resultMap = tabCouponService.addCoupon(couponRedis, tabCouponNew, useridList);
        	if (shareFlag == 1) {
        	    String shareUrl = "/coupon/shareCoupon?couponcode=" + couponCode + "&shareid=" + shareid;
                resultMap.put("shareUrl", SearchFileUtils.importexpressPath + shareUrl
                            + "<br /><br />" + "https://www.kidsproductwholesale.com" + shareUrl);
            }
        	resultMap.put("state", "true");
		} catch (Exception e) {
			System.out.println(e);
			resultMap.put("message", "保存异常" + e);
			resultMap.put("code", "3");
        	resultMap.put("state", "false");
		}
        return resultMap;
    }
    
    /**
     * 查询卷码是否重复
     * 		http://127.0.0.1:8086/cbtconsole/coupon/checkcouponcode.do?couponCode=
     * 
     * @return 是否可用
     **/
    @RequestMapping(value = "/checkcouponcode.do")
    @ResponseBody
    public Boolean checkCouponCode(String couponCode) {
    	List<TabCouponRules> list = null;
    	try {
    		return tabCouponService.checkCouponCode(couponCode);
		} catch (Exception e) {
			
		}
        return false;
    }

    /**
     * 自动生成一张卷码
     * 		http://127.0.0.1:8086/cbtconsole/coupon/querycouponcode.do
     *
     * @return 是否可用
     **/
    @RequestMapping(value = "/querycouponcode.do")
    @ResponseBody
    public String querycouponcode(String couponCode) {
    	try {
    		return tabCouponService.querycouponcode();
		} catch (Exception e) {
            LOG.error("querycouponcode error", e);
        }
        return "";
    }

    /**
     * 根据折扣卷码查询折扣卷信息
     * 		http://127.0.0.1:8086/cbtconsole/coupon/queryTabCouponOne.do?couponCode=
     * @return
     **/
    @RequestMapping(value = "/queryTabCouponOne.do")
    @ResponseBody
    public TabCouponNew queryTabCouponOne(String couponCode) {
        try {
            TabCouponNew tabCouponNew = tabCouponService.queryTabCouponOne(couponCode);
            return tabCouponNew;
        } catch (Exception e) {
            LOG.error("queryTabCouponOne 查询折扣卷异常", e);
        }
        return null;
    }
    /**
     * 删除折扣卷（附带删除关联的相关用户中的折扣卷数据 调用齐庆接口）
     * 		http://127.0.0.1:8086/cbtconsole/coupon/delCoupon.do?couponCode=
     * @return
     **/
    @RequestMapping(value = "/delCoupon.do")
    @ResponseBody
    public Map<String, String> delCoupon(String couponCode) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            if (StringUtils.isBlank(couponCode)){
                result.put("state", "false");
                result.put("message", "折扣卷码问题");
                return result;
            }
            result = tabCouponService.delCoupon(couponCode);
        } catch (Exception e) {
            LOG.error("queryTabCouponOne 删除扣卷异常, couponCode " + couponCode, e);
            result.put("state", "false");
            result.put("message", "删除折扣卷异常");
        }
        return result;
    }

    /**
     * 折扣卷码关联用户id
     * 		http://127.0.0.1:8086/cbtconsole/coupon/addCouponUser.do?couponCode=&userids=
     * @param couponCode 折扣卷id
     * @param userids 关联的用户id
     * @return
     **/
    @RequestMapping(value = "/addCouponUser.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> addCouponUser(String couponCode, String userids,
                                             @RequestParam(value = "websiteType", defaultValue = "1", required = false) Integer websiteType  //网站名
                                            ) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            if (userids == null){
                userids = "";
            }
            List<String> idList = Arrays.asList(userids.replace("，", ",").split(","));
            List<String> useridList = new ArrayList<String>();
            for (String userid : idList) {
                if (StringUtils.isNotBlank(userid) && StringUtils.isNumeric(userid.trim()) && !useridList.contains(userid.trim())){
                    useridList.add(userid.trim());
                }
            }
            if (useridList == null || useridList.size() == 0){
                result.put("state", "false");
                result.put("message", "未录入用户id");
                return result;
            }
            result = tabCouponService.addCouponUser(couponCode, useridList, websiteType);
        } catch (Exception e) {
            result.put("state", "false");
            result.put("message", "录入异常");
            LOG.error("addCouponUser 折扣卷码关联用户id", e);
        }
        return result;
    }
    /**
     * 折扣卷码关联用户id -> 删除之前关联的用户id
     * 		http://127.0.0.1:8086/cbtconsole/coupon/delCouponUser.do?couponCode=&userid=
     *
     * @param couponCode 折扣卷id
     * @param userid 关联的用户id
     * @return
     *
     **/
    @RequestMapping(value = "/delCouponUser.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> delCouponUser(String couponCode, String userid) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            result = tabCouponService.delCouponUser(couponCode, userid);
        } catch (Exception e) {
            result.put("state", "false");
            result.put("message", "录入异常");
            LOG.error("delCouponUser 折扣卷码关联用户id", e);
        }
        return result;
    }

}