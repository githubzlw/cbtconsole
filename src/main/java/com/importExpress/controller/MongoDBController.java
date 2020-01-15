package com.importExpress.controller;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.listener.ContextListener;
import com.importExpress.pojo.InputData;
import com.importExpress.pojo.MongoGoodsBean;
import com.importExpress.service.MongoGoodsService;
import com.importExpress.utli.EasyUiTreeUtils;
import com.importExpress.utli.GoodsBeanUtil;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.controller
 * @date:2019/11/22
 */
@Controller
@RequestMapping("/mongo")
public class MongoDBController {
    private static final Log logger = LogFactory.getLog(MongoDBController.class);

    @Autowired
    private CustomGoodsService customGoodsService;

    @Autowired
    private MongoGoodsService mongoGoodsService;

    @RequestMapping("/queryListByParam")
    public ModelAndView queryListByParam(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView("customGoodsList");

        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null || user.getId() == 0) {
            return mv;
        } else {
            String roletype = user.getRoletype();
            int uid = user.getId();
            String admName = user.getAdmName();
            mv.addObject("admName", admName);
            mv.addObject("roletype", roletype);
            mv.addObject("uid", uid);
        }


        CustomGoodsQuery queryBean = GoodsBeanUtil.genQueryBean(request);
        List<CustomGoodsPublish> goodsList = mongoGoodsService.queryListByParam(queryBean);
        goodsList.stream().forEach(c -> {
            String complainId = c.getComplainId();
            complainId = StringUtils.endsWith(complainId, ",") ? complainId.substring(0, complainId.length() - 1) : complainId;
            if (StringUtils.isNotBlank(complainId)) {
                c.setComplain(Arrays.asList(complainId.split(",")));
            }
        });

        long count = mongoGoodsService.queryListByParamCount(queryBean);
        long amount = (count % 50 == 0 ? count / 50 : count / 50 + 1);
        mv.addObject("catid", queryBean.getCatid());
        mv.addObject("goodsList", goodsList);
        mv.addObject("totalpage", amount);
        mv.addObject("totalNum", count);
        mv.addObject("currentpage", queryBean.getCurrPage());
        mv.addObject("pagingNum", 50);
        return mv;
    }


    @RequestMapping(value = "/queryCategoryTree")
    @ResponseBody
    public List<Map<String, Object>> queryCategoryTree(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long begin = System.currentTimeMillis();
        CustomGoodsQuery queryBean = GoodsBeanUtil.genQueryBean(request);


        List<String> catidList = mongoGoodsService.findCatidFromMongo3(queryBean);
        long count = catidList.size();
        Map<String, Integer> catidMap = new HashMap<>(11000);
        for (String catid : catidList) {
            if (catidMap.containsKey(catid)) {
                catidMap.put(catid, catidMap.get(catid) + 1);
            } else {
                catidMap.put(catid, 1);
            }
        }

        List<CategoryBean> categorys = ContextListener.getCopyList();
        categorys.stream().forEach(e -> {
            e.setTotal(0);
            if (catidMap.containsKey(e.getCid())) {
                e.setTotal(catidMap.get(e.getCid()));
            }
        });
        catidList.clear();

        List<Map<String, Object>> treeMap = EasyUiTreeUtils.genEasyUiTree(categorys, (int) count);
        categorys.clear();
        Long end = System.currentTimeMillis();
        System.err.println(end - begin);
        return treeMap;
    }

    @RequestMapping("/syncAllGoodsToMongo")
    @ResponseBody
    public JsonResult syncAllGoodsToMongo() {
        JsonResult json = new JsonResult();
        List<String> errorList = new ArrayList<>();
        try {

            int limitNum = 100;
            int maxId = customGoodsService.queryMaxIdFromCustomGoods();
            int fc = maxId / limitNum;
            if (maxId % limitNum > 0) {
                fc++;
            }

            List<MongoGoodsBean> list = null;
            int queryTotal = 0;
            int insertTotal = 0;
            Map<String, Integer> cidMap = new HashMap<>();

            for (int i = 1; i <= fc; i++) {
                list = mongoGoodsService.queryBeanByLimit((i - 1) * limitNum, i * limitNum);
                System.err.println("-- " + i + "/" + fc);
                queryTotal += list.size();
                if (CollectionUtils.isNotEmpty(list)) {

                    /*for(MongoGoodsBean bean : list) {
                        try {
                            if(StringUtils.isNotBlank(bean.getSku())){
                                JSONArray sku_json = JSONArray.fromObject(bean.getSku());
                                List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
                                // System.err.println(skuList.size());
                                skuList.clear();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("pid:" + bean.getPid());
                            errorList.add(bean.getPid());
                        }
                    }*/


                    List<String> tempList = new ArrayList<>();
                    for (MongoGoodsBean bean : list) {
                        if (cidMap.containsKey(bean.getCatid1())) {
                            cidMap.put(bean.getCatid1(), cidMap.get(bean.getCatid1()) + 1);
                        } else {
                            cidMap.put(bean.getCatid1(), 1);
                        }
                        tempList.add(bean.getPid());
                    }

                    List<String> checkList = mongoGoodsService.checkIsMongoByList(tempList);
                    tempList.clear();
                    if (CollectionUtils.isNotEmpty(checkList)) {
                        List<MongoGoodsBean> updateList = list.stream().filter(e -> checkList.contains(e.getPid()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(updateList)) {
                            insertTotal += mongoGoodsService.batchUpdateGoodsInfoToMongoDb(updateList);
                            updateList.clear();
                        }

                        List<MongoGoodsBean> insertList = list.stream().filter(e -> !checkList.contains(e.getPid()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(insertList)) {
                            insertTotal += mongoGoodsService.insertGoodsToMongoBatch(insertList);
                            insertList.clear();
                        }
                    } else {
                        insertTotal += mongoGoodsService.insertGoodsToMongoBatch(list);
                    }
                    checkList.clear();
                }
                list.clear();
            }

            // mongoGoodsService.insertIntoPidCatidNum(cidMap);

            if (list != null) {
                list.clear();
            }
            cidMap.clear();
            System.err.println("query total:" + queryTotal);
            System.err.println("insert total:" + insertTotal);

            json.setOk(true);
        } catch (Exception e) {
            json.setOk(false);
            json.setMessage(e.getMessage());
            e.printStackTrace();
            logger.error("syncAllGoodsToMongo error:", e);
        }
        for (String pid : errorList) {
            System.err.println(pid);
        }
        return json;
    }


    @RequestMapping("/insertOrUpdateMongodb")
    @ResponseBody
    public JsonResult insertOrUpdateMongodb(String pid) {
        JsonResult json = new JsonResult();
        if (StringUtils.isBlank(pid)) {
            json.setOk(false);
            json.setMessage("获取PID失败");
            return json;
        }

        try {

            int isRs = mongoGoodsService.insertOrUpdateMongodb(pid);
            if (isRs == 0) {
                json.setOk(false);
                json.setMessage("执行失败");
            } else {
                json.setOk(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("pid:" + pid + ",error:", e);
        }
        return json;
    }


    @GetMapping("/checkPid/{pid}")
    @ResponseBody
    public JsonResult checkPidImg(@PathVariable(name = "pid") String pid) {
        JsonResult json = new JsonResult();
        try {
            CustomGoodsPublish goods = customGoodsService.queryGoodsDetails(pid, 0);
            boolean isCheckImg = GoodsInfoUtils.checkOffLineImg(goods, 0);
            if (isCheckImg) {
                json.setSuccess("检查成功");
            } else {
                json.setErrorInfo("检查异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("pid:" + pid + ",error:", e);
        }
        return json;
    }


    @GetMapping("/getOffLineOrInfringementGoods")
    public ModelAndView getOffLineOrInfringementGoods(@RequestParam(value = "type", required = true, defaultValue = "0") Integer type,
                                                      @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
                                                      @RequestParam(value = "catid", required = false) String catid,
                                                      @RequestParam(value = "shopId", required = false) String shopId) {
        ModelAndView mv = new ModelAndView("offLineOrInfringementGoods");
        // type 1侵权 2硬下架
        if (type <= 0) {
            mv.addObject("isSu", 0);
            mv.addObject("message", "获取参数失败");
            return mv;
        }
        int limitNum = 30;
        try {
            CustomGoodsQuery queryBean = new CustomGoodsQuery();
            queryBean.setCurrPage(page);
            queryBean.setPage((queryBean.getCurrPage() - 1) * limitNum);
            queryBean.setLimitNum(limitNum);
            if (StringUtils.isNotBlank(catid)) {
                queryBean.setCatid(catid);
            }
            if (StringUtils.isNotBlank(shopId)) {
                queryBean.setShopId(shopId);
            }
            mv.addObject("catid", catid);
            mv.addObject("shopId", shopId);
            mv.addObject("type", type);

            List<CustomGoodsPublish> goodsList = new ArrayList<>();
            int count = 0;
            if (type == 1) {
                goodsList = customGoodsService.queryGoodsDeleteInfo(queryBean);
                count = customGoodsService.queryGoodsDeleteInfoCount(queryBean);
            } else if (type == 2) {
                queryBean.setSoldFlag(-1);
                queryBean.setFromFlag(-1);
                goodsList = customGoodsService.queryGoodsInfos(queryBean);
                count = customGoodsService.queryGoodsInfosCount(queryBean);
            }
            dealGoodsList(goodsList);
            mv.addObject("goodsList", goodsList);
            mv.addObject("count", count);
            mv.addObject("currPage", page);
            if (count % limitNum > 0) {
                mv.addObject("totalPage", count / limitNum + 1);
            } else {
                mv.addObject("totalPage", count / limitNum + 1);
            }
            mv.addObject("isSu", 1);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("isSu", 0);
            mv.addObject("message", e.getMessage());
            logger.error("getOffLineOrInfringementGoods,error:", e);
        }

        return mv;
    }


    @PostMapping("/deleteImg")
    @ResponseBody
    public JsonResult deleteImg(@RequestParam(value = "type", required = true, defaultValue = "0") Integer type,
                                @RequestParam(value = "pids", required = true) String pids) {
        JsonResult json = new JsonResult();
        if (StringUtils.isBlank(pids)) {
            json.setErrorInfo("获取PID失败");
            return json;
        }

        try {
            String[] pidList = pids.split(",");
            int total = 0;
            for (String pid : pidList) {
                CustomGoodsPublish goods = null;
                if (type == 1) {
                    goods = customGoodsService.queryGoodsDeleteDetails(pid);
                } else {
                    goods = customGoodsService.queryGoodsDetails(pid, 0);
                }
                if (goods != null) {
                    if (GoodsInfoUtils.deleteByOkHttp(goods)) {
                        total++;
                        if (type == 2) {
                            goods.setEninfo("");
                            customGoodsService.updatePidEnInfo(goods);
                        }
                    }
                }
            }
            json.setSuccess("总数:" + pidList.length + ",成功数:" + total);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("pids:" + pids + ",error:", e);
        }
        return json;
    }


    @PostMapping("/deleteData")
    @ResponseBody
    public JsonResult deleteData(@RequestParam(value = "pids", required = true) String pids) {
        JsonResult json = new JsonResult();
        if (StringUtils.isBlank(pids)) {
            json.setErrorInfo("获取PID失败");
            return json;
        }

        try {
            String[] pidList = pids.split(",");
            int total = 0;
            for (String pid : pidList) {
                if (deleteDataAll(pid)) {
                    total++;
                }
            }
            json.setSuccess("总数:" + pidList.length + ",成功数:" + total);
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("pids:" + pids + ",error:", e);
        }
        return json;
    }


    @GetMapping("/syncDeleteInfo")
    @ResponseBody
    public JsonResult syncDeleteInfo() {
        JsonResult json = new JsonResult();
        try {
            List<String> pidList = customGoodsService.queryOrinfringementPids();
            if (CollectionUtils.isNotEmpty(pidList)) {
                int total = 0;
                for (String pid : pidList) {
                    if (syncInfo(pid)) {
                        total++;
                    }
                }
                json.setSuccess("总数:" + pidList.size() + ",成功数:" + total);
            } else {
                json.setSuccess("总数:" + 0 + ",成功数:" + 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage(e.getMessage());
            logger.error("syncDeleteInfo error:", e);
        }
        return json;
    }


    private boolean syncInfo(String pid) {
        boolean isSu = false;
        try {
            customGoodsService.syncDataToDeleteInfo(pid);
            isSu = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("syncInfo pid:" + pid + ",error:" + e);
        }
        return isSu;
    }

    private boolean deleteDataAll(String pid) {
        boolean isSu = false;
        try {
            // 删除MongoDB
            InputData inputData = new InputData('d');
            inputData.setPid(pid);
            isSu = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1);
            if (isSu) {
                // 删除图片
                CustomGoodsPublish goods = customGoodsService.queryGoodsDeleteDetails(pid);
                GoodsInfoUtils.deleteByOkHttp(goods);
                // 删除数据库
                customGoodsService.deleteDataByPid(pid);
                // 删除标识
                customGoodsService.updateDeleteInfoFlag(pid);
                isSu = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("deleteDataAll pid:" + pid + ",error:" + e);
        }
        return isSu;
    }

    private void dealGoodsList(List<CustomGoodsPublish> goodsList) {
        if (CollectionUtils.isNotEmpty(goodsList)) {
            goodsList.forEach(e -> {
                if (e.getShowMainImage().contains("http")) {
                    e.setCustomMainImage(e.getShowMainImage());
                } else {
                    e.setCustomMainImage(e.getRemotpath() + e.getShowMainImage());
                }
                List<String> imgList = GoodsInfoUtils.genDetailsImgList(e.getEninfo(), e.getRemotpath());
                if (CollectionUtils.isNotEmpty(imgList)) {
                    if (imgList.size() > 5) {
                        e.setInfoList(imgList.stream().limit(5).collect(Collectors.toList()));
                    } else {
                        e.setInfoList(imgList);
                    }
                }
            });
        }
    }
}
