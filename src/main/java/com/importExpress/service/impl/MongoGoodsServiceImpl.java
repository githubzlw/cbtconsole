package com.importExpress.service.impl;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.service.MongoGoodsService;
import com.importExpress.mapper.CustomGoodsMapper;
import com.importExpress.pojo.MongoGoodsBean;
import com.importExpress.utli.MapAndBeanUtil;
import com.importExpress.utli.MongoDBHelp;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.WriteModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.service.impl
 * @date:2019/11/22
 */
@Service
public class MongoGoodsServiceImpl implements MongoGoodsService {

    private static final String GOODS_COLLECTION_NAME = "product";

    @Autowired
    private CustomGoodsMapper customGoodsMapper;


    @Override
    public List<CustomGoodsPublish> queryListByParam(CustomGoodsQuery queryBean) throws Exception {

        BasicDBObject find = getParamByQueryBean(queryBean);

        BasicDBObject sort = new BasicDBObject();
        if ("1".equals(queryBean.getIsSort())) {
            sort.put("search_num", -1);
        } else if ("2".equals(queryBean.getIsSort()) || "3".equals(queryBean.getIsSort())) {
            sort.put("click_num", -1);
        } else if ("4".equals(queryBean.getIsSort())) {
            sort.put("catid1", -1);
        }
        List<MongoGoodsBean> list = MongoDBHelp.INSTANCE.findAnyFromMongo3(GOODS_COLLECTION_NAME, find, sort, queryBean.getPage(), 50);

        return changeToResultBean(list);
    }


    private List<CustomGoodsPublish> changeToResultBean(List<MongoGoodsBean> list) throws Exception {
        List<CustomGoodsPublish> rsList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            rsList = new ArrayList<>(list.size() * 2);
            Map<String, Object> tempMap = null;
            for (MongoGoodsBean mongoGoodsBean : list) {

                CustomGoodsPublish publish = new CustomGoodsPublish();
                publish.setAdminId(mongoGoodsBean.getAdmin_id());
                publish.setAliGoodsImgUrl(mongoGoodsBean.getAli_img());
                publish.setAliGoodsName(mongoGoodsBean.getAli_name());
                publish.setAliGoodsPid(mongoGoodsBean.getAli_pid());
                publish.setAliGoodsPrice(mongoGoodsBean.getAli_price());
                publish.setBmFlag(Integer.parseInt(mongoGoodsBean.getBm_flag()));
                mongoGoodsBean.getCategory_total();
                publish.setClickNum(mongoGoodsBean.getClick_num());
                mongoGoodsBean.getComplain_count();
                publish.setCustomMainImage(mongoGoodsBean.getCustom_main_image());
                publish.setShowMainImage(mongoGoodsBean.getCustom_main_image());
                publish.setEnname(mongoGoodsBean.getEnname());
                publish.setFinalWeight(mongoGoodsBean.getFinal_weight());
                publish.setFromFlag(mongoGoodsBean.getFrom_flag());
                publish.setInfringingFlag(Integer.parseInt(mongoGoodsBean.getInfringing_flag()));
                publish.setIsAbnormal(mongoGoodsBean.getIs_abnormal());
                publish.setAddCarFlag(Integer.parseInt(mongoGoodsBean.getIs_add_car_flag()));
                publish.setIsEdited(Integer.parseInt(mongoGoodsBean.getIs_edited()));
                publish.setIsBenchmark(Integer.parseInt(mongoGoodsBean.getIsBenchmark()));
                publish.setKeyword(mongoGoodsBean.getKeyword());
                publish.setLocalpath(mongoGoodsBean.getLocalpath());
                publish.setMaxPrice(mongoGoodsBean.getMax_price());
                publish.setName(mongoGoodsBean.getName());
                publish.setOcrMatchFlag(Integer.parseInt(mongoGoodsBean.getOcr_match_flag()));
                publish.setOffReason(mongoGoodsBean.getOff_reason());
                publish.setPid(mongoGoodsBean.getPid());
                mongoGoodsBean.getPrice_1688();
                publish.setPriorityFlag(Integer.parseInt(mongoGoodsBean.getPriority_flag()));
                publish.setPublishtime(mongoGoodsBean.getPublish_time());
                publish.setRebidFlag(Integer.parseInt(mongoGoodsBean.getRebid_flag()));
                publish.setRemotpath(mongoGoodsBean.getRemotpath());
                publish.setReviewCount(mongoGoodsBean.getReview_count());
                publish.setShopId(mongoGoodsBean.getShop_id());
                publish.setSourceProFlag(Integer.parseInt(mongoGoodsBean.getSource_pro_flag()));
                publish.setSourceUsedFlag(Integer.parseInt(mongoGoodsBean.getSource_used_flag()));
                publish.setUnsellAbleReason(Integer.parseInt(mongoGoodsBean.getUnsellableReason()));
                publish.setUpdatetime(mongoGoodsBean.getUpdate_time());
                publish.setValid(mongoGoodsBean.getValid());
                publish.setWeight(mongoGoodsBean.getWeight());
                mongoGoodsBean.getWeight_check();
                publish.setWeightFlag(Integer.parseInt(mongoGoodsBean.getWeight_flag()));
                publish.setCatid1(mongoGoodsBean.getCatid1());

                rsList.add(publish);
            }
        }
        return rsList;
    }

    @Override
    public long queryListByParamCount(CustomGoodsQuery queryBean) throws Exception {

        BasicDBObject find = getParamByQueryBean(queryBean);
        return MongoDBHelp.INSTANCE.count3(GOODS_COLLECTION_NAME, find);
    }

    @Override
    public List<String> findCatidFromMongo3(CustomGoodsQuery queryBean) throws Exception {
        BasicDBObject find = getParamByQueryBean(queryBean);
        return MongoDBHelp.INSTANCE.findCatidFromMongo3(GOODS_COLLECTION_NAME, find);
    }

    private BasicDBObject getParamByQueryBean(CustomGoodsQuery queryBean) {
        BasicDBObject paramBean = new BasicDBObject();
        try {
            Map<String, Object> map = MapAndBeanUtil.bean2map(queryBean);
            for (String key : map.keySet()) {
                if (key != null && map.get(key) != null && StringUtils.isNotBlank(map.get(key).toString())) {
                    switch (key) {
                        case "catid":
                            if (map.get(key) != null && StringUtils.isNotBlank(map.get(key).toString())
                                    && !"0".equals(map.get(key).toString())) {
                                List<String> list = customGoodsMapper.queryCatidByPath(map.get(key).toString());
                                paramBean.put("catid1", new BasicDBObject(QueryOperators.IN, list));
                                break;
                            }
                        case "sttime":
                            paramBean.put("publish_time", new BasicDBObject(QueryOperators.GTE, map.get(key)));
                            break;
                        case "edtime":
                            paramBean.put("publish_time", new BasicDBObject(QueryOperators.LTE, map.get(key)));
                            break;
                        case "state":
                            if ("2".equals(map.get(key).toString())) {
                                paramBean.put("valid", new BasicDBObject(QueryOperators.IN, new int[]{0, 2}));
                            } else if ("3".equals(map.get(key).toString()) || "5".equals(map.get(key).toString())) {
                                paramBean.put("goodsstate", Integer.parseInt(map.get(key).toString()));
                            } else if ("4".equals(map.get(key).toString())) {
                                paramBean.put("valid", 1);
                            } else if ("6".equals(map.get(key).toString())) {
                                paramBean.put("goodsstate", 6);
                            }
                            break;
                        case "adminId":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("admin_id", map.get(key).toString());
                            }
                            break;
                        case "isEdited":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("is_edited", map.get(key).toString());
                            }
                            break;
                        case "isAbnormal":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("is_abnormal", map.get(key).toString());
                            }
                            break;
                        case "isBenchmark":
                            if ("1".equals(map.get(key).toString())) {
                                paramBean.put("isBenchmark", "1");
                                paramBean.put("bm_flag", "1");
                            } else if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("isBenchmark", map.get(key).toString());
                            }
                            break;
                        case "weightCheck":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("weight_check", map.get(key).toString());
                            }
                            break;
                        case "bmFlag":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("bm_flag", map.get(key).toString());
                            }
                            break;
                        case "sourceProFlag":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("source_pro_flag", map.get(key).toString());
                            }
                            break;
                        case "soldFlag":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("sold_flag", map.get(key).toString());
                            }
                            break;
                        case "priorityFlag":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("priority_flag", map.get(key).toString());
                            }
                            break;
                        case "addCarFlag":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("is_add_car_flag", map.get(key).toString());
                            }
                            break;
                        case "sourceUsedFlag":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("source_used_flag", map.get(key).toString());
                            }
                            break;
                        case "ocrMatchFlag":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("ocr_match_flag", map.get(key).toString());
                            }
                            break;
                        case "rebidFlag":
                            if (Integer.parseInt(map.get(key).toString()) > 0) {
                                paramBean.put("rebid_flag", map.get(key).toString());
                            }
                            break;
                        case "infringingFlag":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("infringing_flag", map.get(key).toString());
                            }
                            break;
                        case "aliWeightBegin":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("ali_price", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            }
                            break;
                        case "aliWeightEnd":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("ali_price", new BasicDBObject(QueryOperators.LTE, map.get(key).toString()));
                            }
                            break;
                        case "onlineTime":
                            paramBean.put("publish_time", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            break;
                        case "offlineTime":
                            paramBean.put("off_time", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            break;
                        case "editBeginTime":
                            paramBean.put("update_time", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            break;
                        case "editEndTime":
                            paramBean.put("update_time", new BasicDBObject(QueryOperators.LTE, map.get(key).toString()));
                            break;
                        case "weight1688Begin":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("weight", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            }
                            break;
                        case "weight1688End":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("weight", new BasicDBObject(QueryOperators.LTE, map.get(key).toString()));
                            }
                            break;
                        case "price1688Begin":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("price_1688", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            }
                            break;
                        case "price1688End":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("price_1688", new BasicDBObject(QueryOperators.LTE, map.get(key).toString()));
                            }
                            break;
                        case "isSort":
                            if (Integer.parseInt(map.get(key).toString()) == 3) {
                                paramBean.put("click_num", new BasicDBObject(QueryOperators.GT, "0"));
                            }
                            break;
                        case "isComplain":
                            if (Integer.parseInt(map.get(key).toString()) == 1) {
                                paramBean.put("complain_count", new BasicDBObject(QueryOperators.GT, "0"));
                            }
                            break;
                        case "fromFlag":
                            if (Integer.parseInt(map.get(key).toString()) > -1) {
                                paramBean.put("from_flag", "0");
                            }
                            break;
                        case "finalWeightBegin":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("final_weight", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            }
                            break;
                        case "finalWeightEnd":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("final_weight", new BasicDBObject(QueryOperators.LTE, map.get(key).toString()));
                            }
                            break;
                        case "minPrice":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("max_price", new BasicDBObject(QueryOperators.GTE, map.get(key).toString()));
                            }
                            break;
                        case "maxPrice":
                            if (Double.parseDouble(map.get(key).toString()) > 0) {
                                paramBean.put("max_price", new BasicDBObject(QueryOperators.LTE, map.get(key).toString()));
                            }
                            break;
                        case "isSoldFlag":
                            if (Integer.parseInt(map.get(key).toString()) == 0) {
                                paramBean.put("is_sold_flag", "0");
                            }
                            break;
                        case "isWeigthZero":
                            if (Integer.parseInt(map.get(key).toString()) == 1) {

                                List<BasicDBObject> list = new ArrayList<>();
                                list.add(new BasicDBObject("is_sold_flag", new BasicDBObject("$eq", "")));
                                list.add(new BasicDBObject("is_sold_flag", new BasicDBObject("$eq", "0")));
                                list.add(new BasicDBObject("is_sold_flag", new BasicDBObject("$eq", "0.0")));
                                list.add(new BasicDBObject("is_sold_flag", new BasicDBObject("$eq", "0.00")));
                                paramBean.put(QueryOperators.OR, list);
                            }
                            break;
                        case "isWeigthCatid":
                            if (Integer.parseInt(map.get(key).toString()) == 1) {
                                paramBean.put("weight_flag", "1");
                            }
                            break;
                        case "qrCatid":
                            paramBean.put("catid1", map.get(key).toString());
                            break;
                        case "shopId":
                            paramBean.put("shop_id", map.get(key).toString());
                            break;
                        case "chKeyWord":
                            paramBean.put("name", "/" + map.get(key).toString() + "/");
                            break;
                        default:
                            // paramBean.put(key, map.get(key));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramBean;
    }


    @Override
    public List<MongoGoodsBean> queryBeanByLimit(int minId, int maxId) {
        return customGoodsMapper.queryBeanByLimit(minId, maxId);
    }

    @Override
    public MongoGoodsBean queryBeanByPid(String pid) {
        return customGoodsMapper.queryBeanByPid(pid);
    }

    @Override
    public int insertGoodsToMongoBatch(List<MongoGoodsBean> list) throws Exception {


        List<WriteModel<Document>> writeModelList = changeToMongoInsertDocument(list);


        return MongoDBHelp.INSTANCE.insertBatch3(GOODS_COLLECTION_NAME, writeModelList);
    }

    @Override
    public int insertGoodsToMongoSingle(MongoGoodsBean goodsBean) throws Exception {
        Map<String, Object> map = MapAndBeanUtil.bean2map(goodsBean);
        Document json = new Document(map);
        MongoDBHelp.INSTANCE.insert3(GOODS_COLLECTION_NAME, json);
        return 1;
    }

    @Override
    public int insertIntoPidCatidNum(Map<String, Integer> cidMap) {
        return customGoodsMapper.insertIntoPidCatidNum(cidMap);
    }

    @Override
    public List<CategoryBean> queryAllCategoryBean() {
        return customGoodsMapper.queryAllCategoryBean();
    }

    @Override
    public List<String> checkIsMongoByList(List<String> pidList) {
        return MongoDBHelp.INSTANCE.checkIsMongoByList(GOODS_COLLECTION_NAME, pidList);
    }

    @Override
    public boolean checkIsMongoByPid(String pid) {
        return MongoDBHelp.INSTANCE.checkIsMongoByPid(GOODS_COLLECTION_NAME, pid);
    }

    @Override
    public long updateGoodsInfoToMongoDb(MongoGoodsBean mongoGoodsBean) throws Exception {
        BasicDBObject filter = new BasicDBObject();

        filter.put("pid", mongoGoodsBean.getPid());
        BasicDBObject update = getMongoUpdateBean(mongoGoodsBean);
        return MongoDBHelp.INSTANCE.update3(GOODS_COLLECTION_NAME, filter, update);
    }

    @Override
    public long batchUpdateGoodsInfoToMongoDb(List<MongoGoodsBean> list) throws Exception {
        List<UpdateManyModel<Document>> upList = changeToMongoUpdateDocument(list);
        return MongoDBHelp.INSTANCE.updateBatch3(GOODS_COLLECTION_NAME, upList);
    }

    private List<WriteModel<Document>> changeToMongoInsertDocument(List<MongoGoodsBean> list) throws Exception {
        List<WriteModel<Document>> writeModelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (MongoGoodsBean mongoGoodsBean : list) {
                Document insertDocument = new Document(MapAndBeanUtil.bean2map(mongoGoodsBean));
                InsertOneModel<Document> insertOneModel = new InsertOneModel<>(insertDocument);
                writeModelList.add(insertOneModel);
            }
        }
        return writeModelList;
    }

    private List<UpdateManyModel<Document>> changeToMongoUpdateDocument(List<MongoGoodsBean> list) throws Exception {
        List<UpdateManyModel<Document>> updateManyModelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (MongoGoodsBean mongoGoodsBean : list) {
                BasicDBObject filter = new BasicDBObject();
                filter.put("pid", mongoGoodsBean.getPid());
                BasicDBObject update = getMongoUpdateBean(mongoGoodsBean);
                UpdateManyModel<Document> insertOneModel = new UpdateManyModel<>(filter, update);
                updateManyModelList.add(insertOneModel);
            }
        }
        return updateManyModelList;
    }


    private BasicDBObject getMongoUpdateBean(MongoGoodsBean mongoGoodsBean) throws Exception {
        BasicDBObject update = new BasicDBObject();
        BasicDBObject param = new BasicDBObject();

        Map<String, Object> map = MapAndBeanUtil.bean2map(mongoGoodsBean);
        for (String key : map.keySet()) {
            if (StringUtils.isNotBlank(key)) {
                param.put(key, map.get(key));
            }
        }
        update.put("$set", param);
        return update;
    }
}
