package com.importExpress.controller;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.importExpress.service.QueryUserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/queryuser")
public class QueryUserController {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(QueryUserController.class);

    @Autowired
	private QueryUserService queryUserService;
	
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



}