package com.cbt.controller;

import com.cbt.bean.*;
import com.cbt.customer.service.IShopUrlService;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.service.SingleGoodsService;
import com.cbt.util.IpCheckUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jxw
 * @ClassName SingleGoodsController
 * @Description 单个商品controller
 * @date 2018年4月22日
 */
@Controller
@RequestMapping(value = "/singleGoods")
public class SingleGoodsController {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SingleGoodsController.class);
    private static final String LOCAL_SHOW_URL = "http://192.168.1.31:9090/";
    private static final String REMOTE_SHOW_URL = "http://117.144.21.74:9090/";
    private static final String LOCAL_FILE_PATH = "G:/img_unzip/";

    private List<ConfirmUserInfo> allAdms = new ArrayList<ConfirmUserInfo>();
    private List<Admuser> adminList = null;

    @Autowired
    private CustomGoodsService ctmGdService;

    @Autowired
    private SingleGoodsService sgGsService;

    @Autowired
    private IShopUrlService shopUrlService;

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title queryForList
     * @Description 根据条件查询列表数据
     */
    @RequestMapping("/queryForList")
    @ResponseBody
    public EasyUiJsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        int startNum = 0;
        int limitNum = 20;
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            pid = "";
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
        String stateStr = request.getParameter("state");
        int state = 0;
        if (!(stateStr == null || "".equals(stateStr) || "0".equals(stateStr))) {
            state = Integer.valueOf(stateStr);
        }
        int drainageFlag = 0;
        String drainageFlagStr = request.getParameter("drainageFlag");
        if(StringUtils.isNotBlank(drainageFlagStr)){
            drainageFlag = Integer.valueOf(drainageFlagStr);
        }

        int goodsType = -1;
        String goodsTypeStr = request.getParameter("goodsType");
        if(StringUtils.isNotBlank(goodsTypeStr)){
            goodsType = Integer.valueOf(goodsTypeStr);
        }

        try {
            SingleQueryGoodsParam queryPm = new SingleQueryGoodsParam();
            queryPm.setPid(pid);
            queryPm.setEdtime(edtime);
            queryPm.setSttime(sttime);
            queryPm.setAdmid(admid);
            queryPm.setLimitNum(limitNum);
            queryPm.setStartNum(startNum);
            queryPm.setState(state);
            queryPm.setDrainageFlag(drainageFlag);
            queryPm.setGoodsType(goodsType);
            List<SameTypeGoodsBean> res = sgGsService.queryForList(queryPm);
            int count = sgGsService.queryForListCount(queryPm);
            if (res.size() > 0) {
                dealRalationAdmin(res);
            }
            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title saveGoods
     * @Description 对PID进行已经在线校检
     */
    @RequestMapping("/checkExitsGoods")
    @ResponseBody
    public JsonResult checkExitsGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }

        String goodsUrl = request.getParameter("goodsUrl");
        if (goodsUrl == null || "".equals(goodsUrl)) {
            json.setOk(false);
            json.setMessage("获取1688URL失败");
            return json;
        } else {
            if (goodsUrl.indexOf("?") > -1) {
                goodsUrl = goodsUrl.substring(0, goodsUrl.indexOf("?"));
            }
        }

        try {
            String pid = goodsUrl.substring(goodsUrl.lastIndexOf("/") + 1, goodsUrl.indexOf(".html"));
            CustomGoodsPublish goods = ctmGdService.queryGoodsDetails(pid, 0);
            if (goods == null) {
                json.setOk(true);
                json.setTotal(0L);
            } else {
                json.setOk(true);
                json.setTotal(1L);
                String msg = "";
                if (goods.getValid() > 0) {
                    if ("2".equals(goods.getIsEdited())) {
                        msg = "商品已经在线[上架，已编辑]";
                    } else {
                        msg = "商品已经在线[上架，未编辑]";
                    }
                    json.setMessage(msg);
                } else {
                    json.setMessage("商品已经在线[下架]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("检查商品是否存在失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("检查商品是否存在失败，原因:" + e.getMessage());
        }
        return json;
    }

    /**
     * @param request
     * @param response
     * @return JsonResult
     * @Title saveGoods
     * @Description 新增商品
     */
    @RequestMapping("/saveGoods")
    @ResponseBody
    public JsonResult saveGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }

        String goodsUrl = request.getParameter("goodsUrl");
        if (goodsUrl == null || "".equals(goodsUrl)) {
            json.setOk(false);
            json.setMessage("获取1688URL失败");
            return json;
        } else {
            if (goodsUrl.indexOf("?") > -1) {
                goodsUrl = goodsUrl.substring(0, goodsUrl.indexOf("?"));
            }
        }
        String goodsWeight = request.getParameter("goodsWeight");
        if (goodsWeight == null || "".equals(goodsWeight)) {
            json.setOk(false);
            json.setMessage("获取商品重量失败");
            return json;
        }

        int drainageFlag = 0;
        String drainageFlagStr = request.getParameter("drainageFlag");
        if (StringUtils.isNotBlank(drainageFlagStr)) {
            drainageFlag = Integer.valueOf(drainageFlagStr);
        }


        int goodsType = 0;
        String goodsTypeStr = request.getParameter("goodsType");
        if (StringUtils.isNotBlank(goodsTypeStr)) {
            goodsType = Integer.valueOf(goodsTypeStr);
        }

        String aliPid = request.getParameter("aliPid");
        String aliPrice = request.getParameter("aliPrice");
        if(goodsType > 0){
            if (StringUtils.isBlank(aliPid)) {
                json.setOk(false);
                json.setMessage("获取对标商品ID失败");
                return json;
            }
            if (StringUtils.isBlank(aliPrice)) {
                json.setOk(false);
                json.setMessage("获取对标商品价格失败");
                return json;
            }
        }

        try {

            json = sgGsService.saveGoods(goodsUrl, admuser.getId(), Double.valueOf(goodsWeight),drainageFlag,goodsType,aliPid,aliPrice);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("插入商品失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("插入商品失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/deleteGoodsByPid")
    @ResponseBody
    public JsonResult deleteGoodsByPid(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        }

        String pid = request.getParameter("pid");
        if (pid == null || "".equals(pid)) {
            json.setOk(false);
            json.setMessage("获取当前PID失败");
            return json;
        }

        try {
            boolean isSuccess = sgGsService.deleteGoodsByPid(pid);
            if (isSuccess) {
                json.setOk(true);
            } else {
                json.setOk(false);
                json.setMessage("执行失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("pid:" + pid + ",删除失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("pid:" + pid + ",删除失败，原因:" + e.getMessage());
        }
        return json;
    }

    @RequestMapping("/getAdminList")
    @ResponseBody
    public JsonResult getAdminList(HttpServletRequest request, HttpServletResponse response) {
        JsonResult json = new JsonResult();
        try {
            if(allAdms == null || allAdms.size() == 0){
                queryAllAdms();
            }
            json.setOk(true);
            json.setData(allAdms);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("获取用户列表失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("获取用户列表失败，原因 :" + e.getMessage());
        }
        return json;
    }

    private void dealRalationAdmin(List<SameTypeGoodsBean> goodList) {
        if (adminList == null || adminList.size() == 0) {
            adminList = sgGsService.queryAllAdmin();
        }
        for (SameTypeGoodsBean good : goodList) {
            if (good.getAdminId() > 0) {
                for (Admuser admin : adminList) {
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

    private void queryAllAdms() {
        if (allAdms != null) {
            allAdms.clear();
        }
        UserDao dao = new UserDaoImpl();
        List<ConfirmUserInfo> admList = dao.getAllUserHasOffUser();
        for (ConfirmUserInfo userInfo : admList) {
            String userName = userInfo.getConfirmusername();
            if (userInfo.getRole() == 0) {
                if (userName.equalsIgnoreCase("Ling")) {
                    allAdms.add(userInfo);
                } else if (userName.equalsIgnoreCase("testAdm")) {
                    allAdms.add(userInfo);
                }
            }else{
                allAdms.add(userInfo);
            } /*else if (userInfo.getRole() == 2) {
                if(!(userName.contains("cangku1") || userName.contains("nihaisheng"))){
                    allAdms.add(userInfo);
                }
            }else if(userInfo.getRole() == 3){
                if(!(userName.contains("Sale1") || userName.contains("Sale2") || userName.contains("Sale3")
                        || userName.contains("Sale4") || userName.contains("Sale5") || userName.contains("testPur"))){
                    allAdms.add(userInfo);
                }
            }else if(userInfo.getRole() == 1){
                allAdms.add(userInfo);
            }*/
        }
    }


    @RequestMapping("/queryOffShelfList")
    @ResponseBody
    public EasyUiJsonResult queryOffShelfList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        NeedOffShelfBean offShelfParam = new NeedOffShelfBean();
        int startNum = 0;
        int limitNum = 25;
        String pageStr = request.getParameter("page");
        if (StringUtil.isNotBlank(pageStr) && !"0".equals(pageStr)) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        offShelfParam.setStartNum(startNum);
        offShelfParam.setLimitNum(limitNum);

        String pid = request.getParameter("pid");
        if (StringUtil.isNotBlank(pid)) {
            offShelfParam.setPid(pid);
        }
        String catid = request.getParameter("catid");
        if (StringUtil.isNotBlank(catid)) {
            offShelfParam.setCatid(catid);
        }
        String beginTime = request.getParameter("beginTime");
        if (StringUtil.isNotBlank(beginTime)) {
            offShelfParam.setBeginTime(beginTime);
        }
        String endTime = request.getParameter("endTime");
        if (StringUtil.isNotBlank(endTime)) {
            offShelfParam.setEndTime(endTime);
        }
        String isOffShelfStr = request.getParameter("isOffShelf");
        int isOffShelf = -1;
        if (StringUtil.isNotBlank(isOffShelfStr)) {
            isOffShelf = Integer.valueOf(isOffShelfStr);
        }
        offShelfParam.setIsOffShelf(isOffShelf);

        String reasonStr = request.getParameter("reason");
        int reason = 0;
        if (StringUtil.isNotBlank(reasonStr)) {
            reason = Integer.valueOf(reasonStr);
        }
        offShelfParam.setReason(reason);

        String updateFlagStr = request.getParameter("updateFlag");
        int updateFlag;
        if (StringUtil.isNotBlank(updateFlagStr)) {
            updateFlag = Integer.valueOf(updateFlagStr);
        } else {
            updateFlag = -1;
        }
        offShelfParam.setUpdateFlag(updateFlag);

        String neverFlagStr = request.getParameter("neverFlag");
        int neverFlag;
        if (StringUtil.isNotBlank(neverFlagStr)) {
            neverFlag = Integer.valueOf(neverFlagStr);
        } else {
            neverFlag = 0;
        }
        offShelfParam.setNeverOffFlag(neverFlag);
        try {
            List<NeedOffShelfBean> res = shopUrlService.queryNeedOffShelfByParam(offShelfParam);
            List<String> pids = new ArrayList<>(res.size());
            for (NeedOffShelfBean offBean : res) {
                pids.add(offBean.getPid());
            }

            Map<String, Integer> rsMap = shopUrlService.queryCompetitiveFlag(pids);
            for (NeedOffShelfBean offBean : res) {
                if (rsMap.containsKey(offBean.getPid())) {
                    offBean.setCompetitiveFlag(1);
                }
            }
            pids.clear();
            rsMap.clear();
            int count = shopUrlService.queryNeedOffShelfByParamCount(offShelfParam);
            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/queryCrossBorderGoodsForList")
    @ResponseBody
    public EasyUiJsonResult queryCrossBorderGoodsForList(HttpServletRequest request, HttpServletResponse response) {

        EasyUiJsonResult json = new EasyUiJsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        if (StringUtil.isBlank(admuserJson)) {
            json.setSuccess(false);
            json.setMessage("用户未登陆");
            return json;
        }

        int startNum = 0;
        int limitNum = 50;
        String rowsStr = request.getParameter("rows");
        if(StringUtil.isNotBlank(rowsStr)){
            limitNum = Integer.valueOf(rowsStr);
        }

        String pageStr = request.getParameter("page");
        if (!(StringUtil.isBlank(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String pid = request.getParameter("pid");
        if (StringUtil.isBlank(pid)) {
            pid = "";
        }
        String catid = request.getParameter("catid");
        if (StringUtil.isBlank(catid) || "0".equals(catid)) {
            catid = "";
        }

        int isPass = -1;
        String isPassStr = request.getParameter("isPass");
        if (StringUtils.isNotBlank(isPassStr)) {
            isPass = Integer.valueOf(isPassStr);
        }

        int isUpdate = -1;
        String isUpdateStr = request.getParameter("isUpdate");
        if (StringUtils.isNotBlank(isUpdateStr)) {
            isUpdate = Integer.valueOf(isUpdateStr);
        }

        String ipStr = request.getParameter("ip");



        try {
            SingleGoodsCheck queryPm = new SingleGoodsCheck();
            queryPm.setPid(pid);
            queryPm.setCatid(catid);
            queryPm.setIsPass(isPass);
            queryPm.setIsUpdate(isUpdate);
            queryPm.setLimitNum(limitNum);
            queryPm.setStartNum(startNum);
            List<SingleGoodsCheck> res = sgGsService.queryCrossBorderGoodsForList(queryPm);
            //判断是否是外网，如果是，进行路径替换http://117.144.21.74

            System.err.println("ip:" + ipStr);
            if (StringUtils.isNotBlank(ipStr)) {
                if (ipStr.contains("27.115.")) {
                    for (SingleGoodsCheck goodsCheck : res) {
                        if (StringUtils.isNotBlank(goodsCheck.getEninfoShow1())) {
                            goodsCheck.setEninfoShow1(goodsCheck.getEninfoShow1().replace(LOCAL_SHOW_URL, REMOTE_SHOW_URL));
                        }
                        if (StringUtils.isNotBlank(goodsCheck.getEninfoShow2())) {
                            goodsCheck.setEninfoShow2(goodsCheck.getEninfoShow2().replace(LOCAL_SHOW_URL, REMOTE_SHOW_URL));
                        }
                        if (StringUtils.isNotBlank(goodsCheck.getEninfoShow3())) {
                            goodsCheck.setEninfoShow3(goodsCheck.getEninfoShow3().replace(LOCAL_SHOW_URL, REMOTE_SHOW_URL));
                        }
                    }
                }
            }

            int count = sgGsService.queryCrossBorderGoodsForListCount(queryPm);
            json.setSuccess(true);
            json.setRows(res);
            json.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/updateSingleGoodsCheck")
    @ResponseBody
    public JsonResult updateSingleGoodsCheck(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }

        String pid = request.getParameter("pid");
        if (StringUtil.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取PID失败");
            return json;
        }

        try {
            SingleGoodsCheck queryPm = new SingleGoodsCheck();
            queryPm.setPid(pid);
            queryPm.setIsPass(1);
            sgGsService.updateSingleGoodsCheck(queryPm);
            //sgGsService.insertIntoSingleGoodsByIsCheck(pid);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("更新失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败，原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/batchInsertIntoSingleGoods")
    @ResponseBody
    public JsonResult batchInsertIntoSingleGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }

        int startNum = 0;
        int limitNum = 50;
        String pageStr = request.getParameter("page");
        if (!(StringUtil.isBlank(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String pid = request.getParameter("pid");
        if (StringUtil.isBlank(pid)) {
            pid = "";
        }
        String catid = request.getParameter("catid");
        if (StringUtil.isBlank(catid) || "0".equals(catid)) {
            catid = "";
        }

        int isPass = -1;
        String isPassStr = request.getParameter("isPass");
        if (StringUtils.isNotBlank(isPassStr)) {
            isPass = Integer.valueOf(isPassStr);
        }

        int isUpdate = -1;
        String isUpdateStr = request.getParameter("isUpdate");
        if (StringUtils.isNotBlank(isUpdateStr)) {
            isUpdate = Integer.valueOf(isUpdateStr);
        }

        try {
            SingleGoodsCheck queryPm = new SingleGoodsCheck();
            queryPm.setPid(pid);
            queryPm.setCatid(catid);
            queryPm.setIsPass(isPass);
            queryPm.setIsUpdate(isUpdate);
            queryPm.setLimitNum(limitNum);
            queryPm.setStartNum(startNum);
            List<SingleGoodsCheck> res = sgGsService.queryCrossBorderGoodsForList(queryPm);
            for(SingleGoodsCheck goodsCheck : res){
                if(goodsCheck.getIsPass() == 0){
                    sgGsService.insertIntoSingleGoodsByIsCheck(goodsCheck.getPid());
                }
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("更新失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败，原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/dealSameShopSingleGoods")
    @ResponseBody
    public JsonResult dealSameShopSingleGoods(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }
        String shopId = request.getParameter("shopId");
        if (StringUtil.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }

        String type = request.getParameter("type");
        if (StringUtil.isBlank(type)) {
            json.setOk(false);
            json.setMessage("获取执行类别失败");
            return json;
        }
        try {
            SingleGoodsCheck queryPm = new SingleGoodsCheck();
            queryPm.setShopId(shopId);
            List<SingleGoodsCheck> res = sgGsService.queryCrossBorderGoodsForList(queryPm);
            if("0".equals(type)){
                //全过
                for(SingleGoodsCheck goodsCheck : res){
                    if(goodsCheck.getIsPass() == 0){
                        sgGsService.insertIntoSingleGoodsByIsCheck(goodsCheck.getPid());
                    }
                }
            }else if("1".equals(type)){
                //全否
                for(SingleGoodsCheck goodsCheck : res){
                    goodsCheck.setIsPass(1);
                }
                sgGsService.batchUpdateSingleGoodsCheck(res);
            }else{
                json.setOk(false);
                json.setMessage("获取参数失败，执行终止");
            }
            if(res != null){
                res.clear();
            }
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("更新失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("更新失败，原因:" + e.getMessage());
        }
        return json;
    }



    @RequestMapping(value = "/genCatergoryTree")
	@ResponseBody
	public List<Map<String, Object>> genCatergoryTree(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> treeRoot = new HashMap<String, Object>();// 根节点
        List<Map<String, Object>> treeMap = new ArrayList<Map<String, Object>>();// 根节点的所有子节点
        String pid = request.getParameter("pid");
        if (StringUtil.isBlank(pid)) {
            pid = "";
        }
        int isPass = -1;
        String isPassStr = request.getParameter("isPass");
        if (StringUtils.isNotBlank(isPassStr)) {
            isPass = Integer.valueOf(isPassStr);
        }

        int isUpdate = -1;
        String isUpdateStr = request.getParameter("isUpdate");
        if (StringUtils.isNotBlank(isUpdateStr)) {
            isUpdate = Integer.valueOf(isUpdateStr);
        }
        try {
            SingleGoodsCheck queryPm = new SingleGoodsCheck();
            queryPm.setIsPass(isPass);
            queryPm.setIsUpdate(isUpdate);
            queryPm.setPid(pid);

            List<CategoryBean> categorys = sgGsService.queryCateroryList(queryPm);
            List<Map<String, Object>> parentMaps = new ArrayList<Map<String, Object>>();
            // 循环获取一级目录数据并赋值子菜单
            for (CategoryBean ct : categorys) {
                if (ct.getLv() == 1 && !"2".equals(ct.getCid())) {
                    Map<String, Object> childMap = new HashMap<String, Object>();
                    childMap.put("id", ct.getCid());
                    childMap.put("text", ct.getCategoryName());
                    childMap.put("state", "closed");
                    childMap.put("total", ct.getTotal());
                    childMap.put("children", getChildMap(ct.getCid(), categorys, ct.getLv()));
                    parentMaps.add(childMap);
                }
            }
            List<Map<String, Object>> nwParentMaps = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> ptMap : parentMaps) {
                int ptCount = doTreeCount(ptMap);
                if (ptCount == 0) {
                    ptMap.put("children", null);
                } else {
                    ptCount += Integer.valueOf(ptMap.get("total").toString());
                    ptMap.put("text", ptMap.get("text") + "(<b>" + ptCount + "</b>)");
                    nwParentMaps.add(ptMap);
                }
            }
            parentMaps.clear();


            int count = sgGsService.queryCrossBorderGoodsForListCount(queryPm);

            treeRoot.put("children", nwParentMaps);
            treeRoot.put("id", "0");
            treeRoot.put("text", "全部类别" + "(<b>" + count + "</b>)");
            treeMap.add(treeRoot);
            categorys.clear();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        return treeMap;
    }


	// 根据cid和lv寻找次级目录
	private List<Map<String, Object>> getChildMap(String cid, List<CategoryBean> categorys, int lv) {
		List<Map<String, Object>> childMaps = new ArrayList<Map<String, Object>>();
		for (CategoryBean ct : categorys) {
			// 判断path数据不为空lv是传递参数的+1值，并且在path中含有父类的cid
			if ((ct.getLv() == lv + 1) && !(ct.getPath() == null || "".equals(ct.getPath()))) {
				// 寻找当前数据的
				String[] catids = ct.getPath().split(",");
				if (catids.length > 0) {
					for (String catid : catids) {
						if (cid.equals(catid)) {
							Map<String, Object> child = new HashMap<String, Object>();
							child.put("id", ct.getCid());
							child.put("text", ct.getCategoryName());
							child.put("total", ct.getTotal());

							List<Map<String, Object>> childList = getChildMap(ct.getCid(), categorys, ct.getLv());
							// 递归创建
							child.put("children", childList);
							if (ct.getLv() == 2 && childList.size() > 0) {
								child.put("state", "closed");
							}
							childMaps.add(child);
							break;
						}
					}
				}
				catids = null;
			}
		}
		return childMaps;
	}

	// 递归统计总数
	@SuppressWarnings("unchecked")
	private int doTreeCount(Map<String, Object> parentMap) {
		int count = 0;
		List<Map<String, Object>> list = (List<Map<String, Object>>) parentMap.get("children");
		if (list == null || list.size() == 0) {
			return count;
		}
		List<Map<String, Object>> nwList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> childMap : list) {
			// 统计子节点的孩子总数+本身的总数
			int cur_cnt = doTreeCount(childMap);
			if (cur_cnt == 0) {
				childMap.put("children", null);
			}
			cur_cnt += Integer.valueOf(childMap.get("total").toString());
			childMap.put("text", childMap.get("text") + "(<b>" + cur_cnt + "</b>)");
			// 覆盖赋值
			childMap.put("total", cur_cnt);
			if (cur_cnt > 0) {
				nwList.add(childMap);
			}
			count += cur_cnt;
		}
		parentMap.put("children", nwList);
		list.clear();
		// 返回前记录当前节点的统计个数
		return count;
	}




	@RequestMapping("/setMainImgByPid")
    @ResponseBody
    public JsonResult setMainImgByPid(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }
        String pid = request.getParameter("pid");
        if (StringUtil.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取商品PID失败");
            return json;
        }

        String imgUrl = request.getParameter("imgUrl");
        if (StringUtil.isBlank(imgUrl)) {
            json.setOk(false);
            json.setMessage("获取图片路径失败");
            return json;
        }
        try {
            if (imgUrl.contains("alicdn.")) {
                imgUrl = "";
            }
            if (imgUrl.contains(REMOTE_SHOW_URL)) {
                imgUrl = imgUrl.replace(REMOTE_SHOW_URL, LOCAL_FILE_PATH);
            } else if (imgUrl.contains(LOCAL_SHOW_URL)) {
                imgUrl = imgUrl.replace(LOCAL_SHOW_URL, LOCAL_FILE_PATH);
            }
            sgGsService.setMainImgByPid(pid, imgUrl);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PID:" + pid + ",设置搜索图失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("PID:" + pid + ",设置搜索图失败，原因:" + e.getMessage());
        }
        return json;
    }


    @RequestMapping("/setMainImgByShopId")
    @ResponseBody
    public JsonResult setMainImgByShopId(HttpServletRequest request, HttpServletResponse response) {

        JsonResult json = new JsonResult();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = new Admuser();
        if (admuserJson == null) {
            json.setOk(false);
            json.setMessage("用户未登陆");
            return json;
        } else {
            admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        }
        String shopId = request.getParameter("shopId");
        if (StringUtil.isBlank(shopId)) {
            json.setOk(false);
            json.setMessage("获取店铺ID失败");
            return json;
        }

        String imgUrl = request.getParameter("imgUrl");
        if (StringUtil.isBlank(imgUrl)) {
            json.setOk(false);
            json.setMessage("获取图片路径失败");
            return json;
        }
        try {
            if (imgUrl.contains("alicdn.")) {
                imgUrl = "";
            }
            if (imgUrl.contains(REMOTE_SHOW_URL)) {
                imgUrl = imgUrl.replace(REMOTE_SHOW_URL, LOCAL_FILE_PATH);
            } else if (imgUrl.contains(LOCAL_SHOW_URL)) {
                imgUrl = imgUrl.replace(LOCAL_SHOW_URL, LOCAL_FILE_PATH);
            }
            sgGsService.setMainImgByShopId(shopId, imgUrl);
            json.setOk(true);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("shopId:" + shopId + ",设置搜索图失败，原因 :" + e.getMessage());
            json.setOk(false);
            json.setMessage("shopId:" + shopId + ",设置搜索图失败，原因:" + e.getMessage());
        }
        return json;
    }

}
