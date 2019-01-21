package com.importExpress.controller;

import com.cbt.service.CustomGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.AliProductBean;
import com.importExpress.service.AliProductService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

@Controller
@RequestMapping("/aliProductCtr")
public class AliProductMacthController {
    private static final Log logger = LogFactory.getLog(AliProductMacthController.class);

    @Autowired
    private AliProductService aliProductService;
    
    @Autowired
    private CustomGoodsService customGoodsService;
    
    /**
     * 查询阿里信息列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/queryForList")
    public ModelAndView queryForList(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("aliMatch1688");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            return mv;
        }

        AliProductBean productBean = new AliProductBean();
        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isNotBlank(aliPid)) {
            productBean.setAliPid(aliPid.trim());
        }

        String keyword = request.getParameter("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            productBean.setKeyword(keyword.trim());
        }

        String adminIdStr = request.getParameter("adminId");
        if (StringUtils.isNotBlank(adminIdStr)) {
            productBean.setAdminId(Integer.valueOf(adminIdStr));
        }

        String dealStateStr = request.getParameter("dealState");
        if (StringUtils.isNotBlank(dealStateStr)) {
            if (!"-1".equals(dealStateStr)) {
                productBean.setDealState(Integer.valueOf(dealStateStr));
            }
        }

        int page = 1;
        String pageStr = request.getParameter("page");
        if (StringUtils.isNotBlank(pageStr)) {
            page = Integer.valueOf(pageStr);
        }
        int limitNum = 20;
        productBean.setLimitNum(limitNum);
        productBean.setStartNum((page - 1) * limitNum);

        try {
            mv.addObject("aliPid", productBean.getAliPid() == null ? "" : productBean.getAliPid());
            mv.addObject("keyword", productBean.getKeyword() == null ? "" : productBean.getKeyword());
            mv.addObject("page", page);
            mv.addObject("adminId", productBean.getAdminId());
            mv.addObject("dealState", dealStateStr);

            List<AliProductBean> aliBeans = aliProductService.queryForList(productBean);
            for (AliProductBean aliProduct : aliBeans) {
                aliProduct.setProductListLire(aliProductService.query1688ByLire(aliProduct.getAliPid()));
                aliProduct.setProductListPython(aliProductService.query1688ByPython(aliProduct.getAliPid()));
            }
            int total = aliProductService.queryForListCount(productBean);
            mv.addObject("infos", aliBeans);
            mv.addObject("total", total);
            if (total % limitNum == 0) {
                mv.addObject("totalPage", total / limitNum);
            } else {
                mv.addObject("totalPage", total / limitNum + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return mv;
    }


    /**
     * 更新ali商品的处理标识
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/setAliFlag")
    @ResponseBody
    public JsonResult setAliFlag(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isBlank(aliPid)) {
            json.setOk(false);
            json.setMessage("获取AliPid失败");
            return json;
        }

        String dealStateStr = request.getParameter("dealState");
        if (StringUtils.isBlank(dealStateStr)) {
            json.setOk(false);
            json.setMessage("获取处理状态失败");
            return json;
        }
        try {
            aliProductService.setAliFlag(aliPid, Integer.valueOf(dealStateStr), user.getId());
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("setAliFlag error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败:" + e.getMessage());
            logger.error("setAliFlag error:" + e.getMessage());
        }
        return json;
    }


    /**
     * 更新lire商品的处理标识
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/set1688PidFlag")
    @ResponseBody
    public JsonResult set1688PidFlag(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isBlank(aliPid)) {
            json.setOk(false);
            json.setMessage("获取AliPid失败");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取1688Pid失败");
            return json;
        }

        String dealStateStr = request.getParameter("dealState");
        
        int validInt = Integer.valueOf(request.getParameter("valid"));
        String aliPriceStr = request.getParameter("aliPrice");
        String aliPirce ="";
        if(aliPriceStr.indexOf('-')>-1){
        	aliPirce = aliPriceStr.split("-")[1];
        }else{
        	aliPirce = aliPriceStr;
        }
        String edName = request.getParameter("edName");
        String rwKeyword = request.getParameter("keyword");
        
        
        if (StringUtils.isBlank(dealStateStr)) {
            json.setOk(false);
            json.setMessage("获取处理状态失败");
            return json;
        }
        try {
            aliProductService.set1688PidFlag(aliPid, pid, Integer.valueOf(dealStateStr), user.getId());
            // 如果是对标，更新对标信息
            if ("2".equals(dealStateStr)) {
                aliProductService.setAliFlag(aliPid, 2, user.getId());
            }
           // 如果是删除同款，更新状态信息
            if("3".equals(dealStateStr)){
            	// aliProductService.setAliFlag(aliPid, 3, user.getId());
            	//删除同款
                customGoodsService.setGoodsValid(pid, user.getAdmName(), user.getId(), -1, "同款下架");
            }

            //在线商品 对标
            if("2".equals(dealStateStr) && validInt != 0){
            	// 精准对标更新产品表ali_pid,ali_price,bm_flag=1,isBenchmark=1 27,28,31,线上
            	boolean is = customGoodsService.upCustomerReady(pid,aliPid,aliPirce,1, 1,edName,rwKeyword,1);
                if (is) {
                    json.setOk(true);
                    json.setMessage("执行成功");
                } else {
                    json.setOk(true);
                    json.setMessage("执行错误，请重试");
                }
            	
            //在线商品 相似
            }else if("1".equals(dealStateStr) && validInt !=0){
            	// 相似对标，更新产品表ali_pid,ali_price,bm_flag=1,isBenchmark=5 27,28,线上
            	boolean is = customGoodsService.upCustomerReady(pid,aliPid,aliPirce,1, 5,edName,rwKeyword,2);
            	if (is) {
                    json.setOk(true);
                    json.setMessage("执行成功");
                } else {
                    json.setOk(true);
                    json.setMessage("执行错误，请重试");
                }
            }
            
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("aliPid:" + aliPid + ",set1688PidFlag error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败:" + e.getMessage());
            logger.error("aliPid:" + aliPid + ",set1688PidFlag error:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/develop1688Pid")
    @ResponseBody
    public JsonResult develop1688Pid(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            json.setOk(false);
            json.setMessage("请登录后操作");
            return json;
        }

        String aliPid = request.getParameter("aliPid");
        if (StringUtils.isBlank(aliPid)) {
            json.setOk(false);
            json.setMessage("获取AliPid失败");
            return json;
        }

        String pid = request.getParameter("pid");
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取1688Pid失败");
            return json;
        }

        String aliPrice;
        String aliPriceStr = request.getParameter("aliPrice");
        if (StringUtils.isBlank(aliPriceStr)) {
            json.setOk(false);
            json.setMessage("获取速卖通价格失败");
            return json;
        }else{
            if(aliPriceStr.contains("-")){
                aliPrice = aliPriceStr.split("-")[1];
            }else{
                aliPrice = aliPriceStr;
            }
        }

        try {
            aliProductService.develop1688Pid(aliPid,aliPrice, pid,user.getId());
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("develop1688Pid error:" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败:" + e.getMessage());
            logger.error("develop1688Pid error:" + e.getMessage());
        }
        return json;
    }





    /**
     * 删除同款
     *
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("/up1688PidFlag")
//    @ResponseBody
//    public JsonResult up1688PidFlag(HttpServletRequest request, HttpServletResponse response) {
//        JsonResult json = new JsonResult();
//
//        String sessionId = request.getSession().getId();
//        String userJson = Redis.hget(sessionId, "admuser");
//        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
//        if (user == null || user.getId() == 0) {
//            json.setOk(false);
//            json.setMessage("请登录后操作");
//            return json;
//        }
//
//        String pid = request.getParameter("pid");
//        if (StringUtils.isBlank(pid)) {
//            json.setOk(false);
//            json.setMessage("获取1688Pid失败");
//            return json;
//        }
//
//
//        try {
//        	int count = customGoodsService.setGoodsValid(pid, user.getAdmName(), user.getId(), -1, "同款下架");
//            if (count > 0) {
//            	json.setOk(true);
//            }else{
//            	json.setOk(false);
//            }
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("up1688PidFlag error:" + e.getMessage());
//            json.setOk(false);
//            json.setMessage("更新失败:" + e.getMessage());
//            logger.error("up1688PidFlag error:" + e.getMessage());
//        }
//        return json;
//    }
    
    
    
}
