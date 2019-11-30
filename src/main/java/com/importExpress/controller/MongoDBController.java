package com.importExpress.controller;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.listener.ContextListener;
import com.importExpress.pojo.MongoGoodsBean;
import com.importExpress.service.MongoGoodsService;
import com.importExpress.utli.EasyUiTreeUtils;
import com.importExpress.utli.GoodsBeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
                        insertTotal += mongoGoodsService.batchUpdateGoodsInfoToMongoDb(updateList);
                        updateList.clear();
                        List<MongoGoodsBean> insertList = list.stream().filter(e -> !checkList.contains(e.getPid()))
                                .collect(Collectors.toList());
                        insertTotal += mongoGoodsService.insertGoodsToMongoBatch(insertList);
                        insertList.clear();
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
}
