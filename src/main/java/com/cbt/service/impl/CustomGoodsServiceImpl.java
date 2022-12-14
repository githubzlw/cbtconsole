package com.cbt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.cbt.bean.*;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.DateFormatUtil;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.mapper.CustomGoodsMapper;
import com.importExpress.pojo.*;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.GoodsMongoDbLocalUtil;
import com.importExpress.utli.OKHttpUtils;
import com.importExpress.utli.SwitchDomainNameUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomGoodsServiceImpl implements CustomGoodsService {

    private static final Log logger = LogFactory.getLog(CustomGoodsServiceImpl.class);

    private static final String CHECK_PID_EXISTS_URL = "http://52.34.56.133:15793/mongo/get?pid=";
    //private static final String CHECK_PID_EXISTS_URL = "http://192.168.1.153:27017/mongo/get?pid=";

    private static final String DELETE_STATIC_URL = "http://192.168.1.31:9090/productStatic/deleteFileWithPid/";

    private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();

    private OKHttpUtils okHttpUtils = new OKHttpUtils();

    @Autowired
    private GoodsMongoDbLocalUtil mongoDbLocalUtil;

    @Autowired
    private CustomGoodsMapper customGoodsMapper;

    @Override
    public List<CategoryBean> getCaterory() {

        return customGoodsDao.getCaterory();
    }

    @Override
    public int addReviewRemark(Map<String, String> map) {
        //?????????????????? ??? ????????????
        return customGoodsMapper.addReviewRemark(map);
    }

    @Override
    public int updateReviewRemark(Map<String, String> map) {
        return customGoodsMapper.updateReviewRemark(map);
    }

    @Override
    public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean) {
        //return customGoodsDao.queryCateroryByParam(queryBean);
        return customGoodsMapper.queryCategoryByParam(queryBean);
    }

    @Override
    public List<CategoryBean> queryStaticizeCateroryByParam() {
        return customGoodsDao.queryStaticizeCateroryByParam();
    }

    @Override
    public List<CustomGoodsBean> getGoodsList(String catid, int page, String sttime, String edtime, int state) {

        return customGoodsDao.getGoodsList(catid, page, sttime, edtime, state);
    }

    @Override
    public CustomGoodsPublish getGoods(String pid, int type) {

        return customGoodsDao.getGoods(pid, type);
    }

    @Override
    public int updateInfo(CustomGoodsBean bean) {

        mongoDbLocalUtil.updatePid(bean.getPid());
        return customGoodsDao.updateInfo(bean);
    }

    @Override
    public int publish(CustomGoodsPublish bean) {

        System.err.println("----------pid:" + bean.getPid() + ",publish begin---------");
        //????????????range_price,?????????sku??????
        if (StringUtils.isNotBlank(bean.getRangePrice())) {
            //sku??????
            List<CustomBenchmarkSkuNew> insertList = new ArrayList<>();
            List<ImportExSku> skuList = JSONArray.parseArray(bean.getSku(), ImportExSku.class);
            for (ImportExSku exSku : skuList) {
                CustomBenchmarkSkuNew skuNew = new CustomBenchmarkSkuNew();
                skuNew.setFinalWeight(bean.getFinalWeight());
                skuNew.setWprice(bean.getWprice());
                skuNew.setSkuPropIds(exSku.getSkuPropIds());
                skuNew.setSkuAttr(exSku.getSkuAttr());
                skuNew.setPid(bean.getPid());
                SkuValPO skuValPO = new SkuValPO();
                skuValPO.setActSkuCalPrice((double) exSku.getSkuVal().getActSkuCalPrice());
                skuValPO.setActSkuMultiCurrencyCalPrice((double) exSku.getSkuVal().getActSkuMultiCurrencyCalPrice());
                skuValPO.setActSkuMultiCurrencyDisplayPrice((double) exSku.getSkuVal().getActSkuMultiCurrencyDisplayPrice());
                skuValPO.setSkuMultiCurrencyCalPrice((double) exSku.getSkuVal().getSkuMultiCurrencyCalPrice());
                skuValPO.setSkuMultiCurrencyDisplayPrice((double) exSku.getSkuVal().getSkuMultiCurrencyDisplayPrice());
                skuValPO.setSkuCalPrice((double) exSku.getSkuVal().getSkuCalPrice());
                skuNew.setSkuVal(skuValPO);
                insertList.add(skuNew);

            }
            if (insertList.size() > 0) {
                // ??????27???28?????????
                customGoodsDao.updateCustomBenchmarkSkuNew(bean.getPid(), insertList);
                // ??????MQ??????AWS???????????????
                // GoodsInfoUpdateOnlineUtil.updateCustomBenchmarkSkuNewByMq(bean.getPid(), insertList);
                insertList.clear();
            }
        }

        //int res = customGoodsDao.publish(bean,0);
        // ????????????jdbc??????AWS??????
        //int res = customGoodsDao.publish(bean);

        // ??????MQ??????AWS???????????????
        // GoodsInfoUpdateOnlineUtil.publishToOnlineByMq(bean);

        int res = 0;
        // ??????MongoDB??????AWS???????????????
        if (GoodsInfoUpdateOnlineUtil.publishToOnlineByMongoDB(bean)) {
            // ??????27
            // customGoodsDao.publish(bean, 0);
            // ??????28???????????????
            res = customGoodsDao.publishTo28(bean);
            if (res == 0) {
                customGoodsDao.publishTo28(bean);
            }

            if(bean.getValid() == 0 || bean.getValid() == 2){
                //??????SkuGoodsOffers???SingleOffersChild??????
                int count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
                if (count > 0) {
                    customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.parseDouble(bean.getFinalWeight()));
                } else {
                    customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.parseDouble(bean.getFinalWeight()));
                }
            }


            /*//??????SkuGoodsOffers???SingleOffersChild??????
            int count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
            //????????????SkuGoodsOffers??????????????????SkuGoodsOffers
            if (count > 0) {
                customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
            } else {
                //????????????????????????SingleOffersChild??????
                customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
            }*/
        } else {
            customGoodsMapper.insertIntoGoodsImgUpLog(bean.getPid(), "", bean.getAdminId(), "publish error");
        }
        mongoDbLocalUtil.updatePid(bean.getPid());
        return res;
    }


    @Override
    public String getGoodsInfo(String pid) {

        return customGoodsDao.getGoodsInfo(pid);
    }

    @Override
    public int updateState(int state, String pid, int adminid) {

        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.updateState(state, pid, adminid);
    }

    @Override
    public int updateValid(int valid, String pid) {

        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.updateValid(valid, pid);
    }

    @Override
    public int insertRecord(String pid, String admin, int state, String record) {

        return customGoodsDao.insertRecord(pid, admin, state, record);
    }

    @Override
    public List<CustomRecord> getRecordList(String pid, int page) {

        return customGoodsDao.getRecordList(pid, page);
    }

    @Override
    public List<CustomGoodsBean> getGoodsList(String pidList) {

        return customGoodsDao.getGoodsList(pidList);
    }


    @Override
    public boolean updateStateList(int state, String pids, int adminid, String reason) {
        // AWS??????
        // GoodsInfoUpdateOnlineUtil.batchUpdateGoodsStateByMQ(state, pids, adminid);
        GoodsInfoUpdateOnlineUtil.batchUpdateGoodsStateMongoDB(state, pids, adminid);
        // ????????????
        mongoDbLocalUtil.updatePidList(pids);
        return customGoodsDao.updateStateList(state, pids, adminid, reason);
    }

    @Override
    public int updateValidList(int valid, String pids) {

        mongoDbLocalUtil.updatePidList(pids);
        return customGoodsDao.updateValidList(valid, pids);
    }

    @Override
    public int insertRecordList(List<String> pids, String admin, int state, String record) {

        return customGoodsDao.insertRecordList(pids, admin, state, record);
    }

    @Override
    public List<CustomGoodsBean> getGoodsListByCatid(String catid) {

        return customGoodsDao.getGoodsListByCatid(catid);
    }

    @Override
    public int updateInfoList(List<CustomGoodsBean> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        for (CustomGoodsBean goodsBean : list) {
            mongoDbLocalUtil.updatePid(goodsBean.getPid());
        }

        return customGoodsDao.updateInfoList(list);
    }

    @Override
    public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean) {
        //return customGoodsDao.queryGoodsInfos(queryBean);
        return customGoodsMapper.queryForListByParam(queryBean);
    }

    @Override
    public int queryGoodsInfosCount(CustomGoodsQuery queryBean) {
        //return customGoodsDao.queryGoodsInfosCount(queryBean);
        return customGoodsMapper.queryForListByParamCount(queryBean);
    }

    @Override
    public int queryStaticizeGoodsInfosCount() {
        return customGoodsDao.queryStaticizeGoodsInfosCount();
    }

    @Override
    public void batchSaveEnName(Admuser user, List<CustomGoodsBean> cgLst) {
        customGoodsDao.batchSaveEnName(user, cgLst);
        for (CustomGoodsBean goodsBean : cgLst) {
            mongoDbLocalUtil.updatePid(goodsBean.getPid());
        }
    }

    @Override
    public CustomGoodsPublish queryGoodsDetails(String pid, int type) {
        DataSourceSelector.restore();
        CustomGoodsPublish bean = customGoodsMapper.queryGoodsDetailsByPid(pid);
        if (bean != null) {
            bean.setOnlineUrl(GoodsInfoUtils.genOnlineUrl(bean));
        }
        if (type > 0) {
            SwitchDomainNameUtil.changeCustomGoodsPublishBean(bean, type + 1);
        }
        return bean;
    }

    @Override
    public int saveEditDetalis(CustomGoodsPublish cgp, String adminName, int adminId, int type) {
        //return customGoodsDao.saveEditDetalis(cgp, adminName, adminId, type);
        mongoDbLocalUtil.updatePid(cgp.getPid());
        cgp.setAdminId(adminId);
        cgp.setGoodsState(type == 1 ? 4 : 5);
        return customGoodsMapper.updateGoodsDetailsByInfo(cgp);
    }

    @Override
    public GoodsPictureQuantity queryPictureQuantityByPid(String pid) {
        return customGoodsDao.queryPictureQuantityByPid(pid);
    }

    @Override
    public List<CustomGoodsPublish> getAllReviewByPid(String pid) {
        return customGoodsMapper.getAllReviewByPid(pid);
    }

    @Override
    public int setGoodsValid(String pid, String adminName, int adminId, int type, String remark) {
        // AWS??????
        // MQ
        // GoodsInfoUpdateOnlineUtil.setGoodsValidByMq(pid,type);
        // MongoDB
        GoodsInfoUpdateOnlineUtil.setGoodsValidByMongoDb(pid, type);
        mongoDbLocalUtil.updatePid(pid);

        if (type == -1) {
            try {
                String rs = okHttpUtils.get(DELETE_STATIC_URL + pid);
                System.err.println("delete static Url:" + DELETE_STATIC_URL + pid + ",rs:" + rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return customGoodsDao.setGoodsValid(pid, adminName, adminId, type, 6, remark);
    }

    @Override
    public int setGoodsValid2(String pid, String adminName, int adminId, int type, String remark) {
        // AWS??????
        // MQ
        // GoodsInfoUpdateOnlineUtil.setGoodsValidByMq(pid,type);
        // MongoDB
        GoodsInfoUpdateOnlineUtil.setGoodsValidByMongoDb2(pid, type);
        return customGoodsDao.setGoodsValid2(pid, adminName, adminId, type, 24, remark);
    }

    @Override
    public boolean batchDeletePids(String[] pidLst) {
        return customGoodsDao.batchDeletePids(pidLst);
    }

    @Override
    public int updateGoodsState(String pid, int goodsState) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.updateGoodsState(pid, goodsState);
    }

    @Override
    public boolean updateBmFlagByPids(String[] pidLst, int adminid) {
        mongoDbLocalUtil.updatePidArr(pidLst);
        return customGoodsDao.updateBmFlagByPids(pidLst, adminid) == pidLst.length;
    }

    @Override
    public ShopManagerPojo queryByShopId(String shopId) {
        return customGoodsDao.queryByShopId(shopId);
    }

    @Override
    public boolean batchInsertSimilarGoods(String mainPid, String similarPids, int adminId, List<String> existPids) {
        return customGoodsDao.batchInsertSimilarGoods(mainPid, similarPids, adminId, existPids);
    }

    @Override
    public List<SimilarGoods> querySimilarGoodsByMainPid(String mainPid) {
        return customGoodsDao.querySimilarGoodsByMainPid(mainPid);
    }

    @Override
    public boolean setGoodsFlagByPid(GoodsEditBean editBean) {
        mongoDbLocalUtil.updatePid(editBean.getPid());
        return customGoodsDao.setGoodsFlagByPid(editBean);
    }

    @Override
    public boolean checkIsHotGoods(String pid) {
        return customGoodsDao.checkIsHotGoods(pid);
    }

    @Override
    public String getWordSizeInfoByPid(String pid) {
        return customGoodsDao.getWordSizeInfoByPid(pid);
    }

    @Override
    public boolean deleteWordSizeInfoByPid(String pid) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.deleteWordSizeInfoByPid(pid);
    }

    @Override
    public boolean setNoBenchmarking(String pid, double finalWeight) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.setNoBenchmarking(pid, finalWeight);
    }

    @Override
    public boolean upCustomerReady(String pid, String aliPid, String aliPrice, int bmFlag, int isBenchmark, String edName, String rwKeyword, int flag) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.upCustomerReady(pid, aliPid, aliPrice, bmFlag, isBenchmark, edName, rwKeyword, flag);
    }


    @Override
    public boolean setNeverOff(String pid) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.setNeverOff(pid);
    }

    @Override
    public int insertPidIsEdited(String shopId, String pid, int adminId) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.insertPidIsEdited(shopId, pid, adminId);
    }

    @Override
    public int checkIsEditedByPid(String pid) {
        return customGoodsMapper.checkIsEditedByPid(pid);
    }

    @Override
    public int updatePidIsEdited(GoodsEditBean editBean) {
        //???????????????????????????????????????????????????????????????
        if (editBean.getBenchmarking_flag() == 1) {
            CustomGoodsPublish good = customGoodsDao.getGoods(editBean.getPid(), 0);
            double finalWeight = 0;
            if (StringUtils.isNotBlank(good.getFinalWeight())) {
                finalWeight = Double.parseDouble(good.getFinalWeight());
            }
            customGoodsDao.setNoBenchmarking(editBean.getPid(), finalWeight);
        }
        mongoDbLocalUtil.updatePid(editBean.getPid());
        return customGoodsMapper.updatePidIsEdited(editBean);
    }

    @Override
    public int queryTypeinGoodsTotal(int adminId, int valid) {
        return customGoodsMapper.queryTypeinGoodsTotal(adminId, valid);
    }

    @Override
    public int queryOnlineGoodsTotal(int valid) {
        return customGoodsMapper.queryOnlineGoodsTotal(valid);
    }

    @Override
    public int queryIsEditOnlineGoodsTotal(int adminId, int valid) {
        return customGoodsMapper.queryIsEditOnlineGoodsTotal(adminId, valid);
    }

    @Override
    public int saveBenchmarking(String pid, String aliPid, String aliPrice) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.saveBenchmarking(pid, aliPid, aliPrice);
    }

    @Override
    public List<String> queryStaticizeList(String catid) {
        return customGoodsMapper.queryStaticizeList(catid);
    }

    @Override
    public int insertIntoGoodsEditBean(GoodsEditBean editBean) {
        mongoDbLocalUtil.updatePid(editBean.getPid());
        return customGoodsMapper.insertIntoGoodsEditBean(editBean);
    }

    @Override
    public List<GoodsEditBean> queryGoodsEditBean(GoodsEditBean editBean) {
        return customGoodsMapper.queryGoodsEditBean(editBean);
    }

    @Override
    public int queryGoodsEditBeanCount(GoodsEditBean editBean) {
        return customGoodsMapper.queryGoodsEditBeanCount(editBean);
    }

    @Override
    public int queryMaxIdFromCustomGoods() {
        return customGoodsMapper.queryMaxIdFromCustomGoods();
    }

    @Override
    public List<GoodsParseBean> queryCustomGoodsByLimit(int minId, int maxId) {
        return customGoodsMapper.queryCustomGoodsByLimit(minId, maxId);
    }

    @Override
    public int updateCustomGoodsStatistic(GoodsParseBean goodsParseBean) {
        mongoDbLocalUtil.updatePid(goodsParseBean.getPid());
        return customGoodsMapper.updateCustomGoodsStatistic(goodsParseBean);
    }

    @Override
    public int checkIsExistsGoods(String pid) {
        return customGoodsMapper.checkIsExistsGoods(pid);
    }

    @Override
    public int insertCustomGoodsStatistic(GoodsParseBean goodsParseBean) {
        mongoDbLocalUtil.updatePid(goodsParseBean.getPid());
        return customGoodsMapper.insertCustomGoodsStatistic(goodsParseBean);
    }

    @Override
    public int updateGoodsSearchNum(String pid) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.updateGoodsSearchNum(pid);
    }

    @Override
    public int updateGoodsClickNum() {
        return customGoodsMapper.updateGoodsClickNum();
    }

    @Override
    public int updateGoodsWeightByPid(String pid, double newWeight, double oldWeight, int weightIsEdit) {
        // ??????????????????revise_weight ?????????????????? =  newWeight
        // ??????????????????final_weight?????????????????? =  newWeight
        // ?????????????????? source_pro_flag = 7 ??????????????????????????????
        // ??????????????????weight ??????weight??????????????????0????????????weight =  newWeight
        // ??????????????????old_weight,???????????????????????????old_weight = oldWeight
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.updateGoodsWeightByPid(pid, newWeight, oldWeight, weightIsEdit);
    }

    @Override
    public int editAndLockProfit(String pid, int type, double editProfit) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.editAndLockProfit(pid, type, editProfit);
    }

    @Override
    public JsonResult setGoodsWeightByWeigher(String pid, String newWeight, int weightIsEdit) {
        JsonResult json = new JsonResult();
        // ??????????????????
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        boolean is = updateGoodsWeightByPid(pid, Double.valueOf(newWeight), Double.valueOf(orGoods.getFinalWeight()), weightIsEdit) > 0;
        mongoDbLocalUtil.updatePid(pid);
        if (is) {
            /*// ????????????????????????
            String url = EditorController.SHOPGOODSWEIGHTCLEARURL + "pid=" + pid + "&finalWeight=" + newWeight
                    + "&sourceTable=custom_benchmark_ready&database=27";
            String resultJson = DownloadMain.getContentClient(url, null);
            System.err.println("pid=" + pid + ",result:[" + resultJson + "]");
            JSONObject jsonJt = JSONObject.fromObject(resultJson);
            System.out.println(json.toString());
            if (!jsonJt.getBoolean("ok")) {
                //???????????????????????????????????????

                json.setOk(false);
                json.setMessage("???????????????????????????????????????" + jsonJt.getString("message"));
            } else {
                json.setOk(true);
                json.setMessage("????????????");
            }*/

            json.setOk(true);
            json.setMessage("????????????");
        } else {
            json.setOk(false);
            json.setMessage("????????????????????????");
        }
        return json;
    }

    @Override
    public JsonResult setGoodsWeightByWeigherNew(String pid, String newWeight, int weightIsEdit, int adminId) {
        JsonResult json = new JsonResult();
        // ??????????????????
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        // ??????27?????????????????????
        boolean is = updateGoodsWeightByPid(pid, Double.valueOf(newWeight), Double.valueOf(orGoods.getFinalWeight()), weightIsEdit) > 0;
        if (is) {
            // ??????????????????
            GoodsEditBean editBean = new GoodsEditBean();
            editBean.setAdmin_id(adminId);
            if (StringUtils.isBlank(orGoods.getWeight()) || "0".equals(orGoods.getWeight()) || "0.00".equals(orGoods.getWeight())) {
                editBean.setWeight_old(orGoods.getWeight());
                editBean.setWeight_new(newWeight);
            }
            editBean.setRevise_weight_old(orGoods.getReviseWeight());
            editBean.setFinal_weight_old(orGoods.getFinalWeight());
            editBean.setRevise_weight_new(newWeight);
            editBean.setFinal_weight_new(newWeight);
            editBean.setPid(pid);
            customGoodsMapper.insertIntoGoodsPriceOrWeight(editBean);

            boolean isSuccess = refreshPriceRelatedData(orGoods, newWeight);
            if (isSuccess) {
                json.setOk(true);
                json.setMessage("??????????????????");
            } else {
                json.setOk(false);
                json.setMessage("??????????????????");
            }
        } else {
            json.setOk(false);
            json.setMessage("??????????????????");
        }
        mongoDbLocalUtil.updatePid(pid);
        return json;
    }

    @Override
    public List<OnlineGoodsCheck> queryOnlineGoodsForList(OnlineGoodsCheck queryPm) {
        return customGoodsMapper.queryOnlineGoodsForList(queryPm);
    }

    @Override
    public int queryOnlineGoodsForListCount(OnlineGoodsCheck queryPm) {
        return customGoodsMapper.queryOnlineGoodsForListCount(queryPm);
    }

    @Override
    public List<CategoryBean> queryCategoryList(OnlineGoodsCheck queryPm) {
        return customGoodsMapper.queryCategoryList(queryPm);
    }

    @Override
    public boolean refreshPriceRelatedData(CustomGoodsPublish bean, String newWeight) {
        // ??????28??????custom_benchmark_ready_newest [source_pro_flag]=7
        // ??????????????????final_weight?????????????????? =  newWeight
        // ?????????????????? source_pro_flag = 7 ??????????????????????????????
        int count = customGoodsDao.updateSourceProFlag(bean.getPid(), newWeight);
        if (count > -1) {
            //??????SkuGoodsOffers???SingleOffersChild??????
            count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
            if (count > 0) {
                count = customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.valueOf(newWeight));
            } else {
                count = customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.valueOf(newWeight));
            }
            /*if(count > -1){
                //??????sku??????
                customGoodsDao.deleteSkuByPid(bean.getPid());
                List<CustomBenchmarkSkuNew> list = customGoodsDao.querySkuByPid(bean.getPid());
                if(!(list == null || list.isEmpty())){
                    count = customGoodsDao.insertIntoSkuToOnline(list);
                }else{
                    System.err.println("pid:" + bean.getPid() + ",sku list is empty");
                }
            }*/
        }

        mongoDbLocalUtil.updatePid(bean.getPid());
        return count > 0;
    }

    @Override
    public int updatePromotionFlag(String pid) {
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        customGoodsDao.insertIntoSingleOffersChild(orGoods.getPid(), Double.parseDouble(orGoods.getFinalWeight()));
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsDao.updatePromotionFlag(pid);
    }

    @Override
    public int queryMd5ImgByUrlCount(String pid, String url, String shopId) {
        return customGoodsMapper.queryMd5ImgByUrlCount(pid, url, shopId);
    }

    @Override
    public List<GoodsMd5Bean> queryMd5ImgByUrlList(String pid, String url, String shopId) {
        return customGoodsMapper.queryMd5ImgByUrlList(pid, url, shopId);
    }

    @Override
    public List<CustomGoodsPublish> queryGoodsByPidList(List<String> pidList) {
        if (pidList != null && pidList.size() > 0) {
            return customGoodsMapper.queryGoodsByPidList(pidList);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updatePidEnInfo(CustomGoodsPublish gd) {
        customGoodsDao.updatePidEnInfo(gd);
        customGoodsMapper.updatePidEnInfo(gd);
        mongoDbLocalUtil.updatePid(gd.getPid());
        return GoodsInfoUpdateOnlineUtil.updatePidEnInfo(gd);
    }

    @Override
    public int updateMd5ImgDeleteFlag(String pid, String url, String shopId) {
        // ?????????????????????
        customGoodsMapper.deleteMd5ImgSameTempFlag(pid, url, shopId);
        return customGoodsMapper.updateMd5ImgDeleteFlag(pid, url, shopId);
    }

    @Override
    public List<ShopMd5Bean> queryForMd5List(ShopMd5Bean shopMd5Bean) {
        return customGoodsMapper.queryForMd5List(shopMd5Bean);
    }

    @Override
    public int queryForMd5ListCount(ShopMd5Bean shopMd5Bean) {
        return customGoodsMapper.queryForMd5ListCount(shopMd5Bean);
    }

    @Override
    public List<GoodsMd5Bean> queryShopGoodsByMd5(String md5Val) {
        return customGoodsMapper.queryShopGoodsByMd5(md5Val);
    }

    @Override
    public int checkShopGoodsImgIsMarkByParam(ShopMd5Bean shopMd5Bean) {
        return customGoodsDao.checkShopGoodsImgIsMarkByParam(shopMd5Bean);
    }

    @Override
    public int updateImgDeleteByMd5(String goodsMd5) {
        // ?????????????????????
        customGoodsMapper.deleteMd5ImgNoSameTempFlag(goodsMd5);
        return customGoodsMapper.updateImgDeleteByMd5(goodsMd5);
    }

    @Override
    public List<ShopMd5Bean> queryMd5NoSameList(ShopMd5Bean shopMd5Bean) {
        return customGoodsMapper.queryMd5NoSameList(shopMd5Bean);
    }

    @Override
    public int queryMd5NoSameListCount(ShopMd5Bean shopMd5Bean) {
        return customGoodsMapper.queryMd5NoSameListCount(shopMd5Bean);
    }

    @Override
    public int setNewAliPidInfo(String pid, String aliPid, String aliPrice) {
        customGoodsDao.setNewAliPidInfo(pid, aliPid, aliPrice);
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        customGoodsDao.insertIntoSingleOffersChild(pid, Double.parseDouble(orGoods.getFinalWeight()));
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.setNewAliPidInfo(pid, aliPid, aliPrice);
    }

    @Override
    public List<String> queryPidListByState(int state) {
        return customGoodsMapper.queryPidListByState(state);
    }

    @Override
    public int insertIntoGoodsPriceOrWeight(GoodsEditBean editBean) {
        mongoDbLocalUtil.updatePid(editBean.getPid());
        return customGoodsMapper.insertIntoGoodsPriceOrWeight(editBean);
    }

    @Override
    public Map<String, String> queryNewAliPriceByAliPid(String aliPid) {
        return customGoodsDao.queryNewAliPriceByAliPid(aliPid);
    }

    @Override
    public int updateWeightFlag(String pid, int flag) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.updateWeightFlag(pid, flag);
    }

    @Override
    public int updateGoodsSku(String pid, String oldSku, String newSku, int adminId, double finalWeight,
                              String rangePrice, String rangePriceFree, float minPrice) {
        // 1.???????????????sku???????????????
        customGoodsMapper.updateSkuInfo(pid, newSku, rangePrice, rangePriceFree, minPrice);
        // 2.??????sku??????
        customGoodsMapper.insertIntoSkuLog(pid, oldSku, newSku, adminId);
        // 3.???child?????????????????????
        mongoDbLocalUtil.updatePid(pid);
        // return customGoodsDao.insertIntoSingleOffersChild(pid, finalWeight);
        return 1;
    }

    @Override
    public int remarkSoftGoodsValid(String pid, int reason) {
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.remarkSoftGoodsValid(pid, reason);
    }

    @Override
    public List<String> queryPidByShopId(String shopId) {
        return customGoodsMapper.queryPidByShopId(shopId);
    }

    @Override
    public int updateVolumeWeight(String pid, String newWeight) {
        // ??????27
        customGoodsMapper.updateVolumeWeight(pid, newWeight);
        // ??????28
        customGoodsDao.updateVolumeWeight(pid, newWeight);
        // ??????mongodb
        GoodsInfoUpdateOnlineUtil.updateVolumeWeight(pid, newWeight);
        return 1;
    }

    @Override
    public JsonResult setGoodsWeightByWeigherInfo(String pid, SearchResultInfo weightAndSyn, int adminId) {
        JsonResult json = new JsonResult();
        // JXW 2019-11-15 ???????????????????????????????????????

        //  ??????json??????
        GoodsWeightChange weightChange = new GoodsWeightChange();
        weightChange.setSkuid(weightAndSyn.getSkuID());
        weightChange.setAdminId(adminId);
        weightChange.setGoodsType(weightAndSyn.getGoods_type());
        weightChange.setPid(pid);
        weightChange.setVolumeWeight(weightAndSyn.getVolume_weight());
        weightChange.setWeight(weightAndSyn.getWeight());
        customGoodsMapper.saveGoodsWeightChange(weightChange);
        json.setOk(true);

        return json;
    }

    @Override
    public JsonResult syncLocalWeightToOnline(GoodsWeightChange weightChange) {
        // JXW 2019-11-15 ??????sku??????
        JsonResult json = new JsonResult();
        // ??????????????????
        CustomGoodsPublish orGoods = queryGoodsDetails(weightChange.getPid(), 0);
        if (StringUtils.isNotBlank(orGoods.getSku())) {
            List<ImportExSku> skuList = JSONArray.parseArray(orGoods.getSku(), ImportExSku.class);
            // ???????????????type??????
            String typeStr = weightChange.getGoodsType();
            String skuid = weightChange.getSkuid() == null ? "" : weightChange.getSkuid();
            // Colour:black@32161,Size:S@4501,
            String[] typeStrList = typeStr.split(",");
            String ppId = "";
            boolean isSkuid = weightChange.getPid().equals(skuid) || StringUtils.isBlank(skuid);
            if (isSkuid) {
                for (String childType : typeStrList) {
                    if (StringUtils.isNotBlank(childType)) {
                        String[] childList = childType.split("@");
                        if (childList.length == 2 && StringUtils.isNotBlank(childList[1])) {
                            ppId += "," + childList[1];
                        }
                    }
                }
            }
            double finalWeight = 0;
            for (ImportExSku exSku : skuList) {
                if ((!isSkuid && skuid.equals(exSku.getSkuId()))
                        || (isSkuid && StringUtils.isNotBlank(ppId) && checkIsEqualPpid(ppId.substring(1), exSku.getSkuPropIds()))) {

                    finalWeight = BigDecimalUtil.truncateDouble(Float.parseFloat(weightChange.getWeight()), 3);
                    exSku.setFianlWeight(finalWeight);
                    if (StringUtils.isNotBlank(weightChange.getVolumeWeight())) {
                        double volumeWeight = BigDecimalUtil.truncateDouble(Float.parseFloat(weightChange.getVolumeWeight()), 3);
                        exSku.setVolumeWeight(volumeWeight);
                    }
                    break;
                }
            }
            // ??????sku??????
            updateGoodsSku(weightChange.getPid(), orGoods.getSku(), skuList.toString(), weightChange.getAdminId(), finalWeight, null, null, 0);
            skuList.clear();
        }

        // ??????????????????
        GoodsEditBean editBean = new GoodsEditBean();
        editBean.setNew_title(weightChange.getGoodsType() + ",sku ??????");
        editBean.setAdmin_id(weightChange.getAdminId());
        if (StringUtils.isBlank(orGoods.getWeight()) || "0".equals(orGoods.getWeight()) || "0.00".equals(orGoods.getWeight())) {
            editBean.setWeight_old(orGoods.getWeight());
            editBean.setWeight_new(weightChange.getWeight());
        }
        editBean.setRevise_weight_old(orGoods.getReviseWeight());
        editBean.setFinal_weight_old(orGoods.getFinalWeight());
        editBean.setRevise_weight_new(weightChange.getWeight());
        editBean.setFinal_weight_new(weightChange.getWeight());
        editBean.setPid(weightChange.getPid());
        customGoodsMapper.insertIntoGoodsPriceOrWeight(editBean);
        weightChange.setSyncFlag(1);
        customGoodsMapper.setGoodsWeightChangeFlag(weightChange);
        json.setOk(true);

        return json;
    }

    private boolean checkIsEqualPpid(String ppId, String skuId) {
        String parentSku = "," + skuId + ",";
        String[] ppIdList = ppId.split(",");
        int count = 0;
        for (String child : ppIdList) {
            if (parentSku.contains(child)) {
                count++;
            }
        }
        return ppIdList.length == count;
    }


    @Override
    public List<CustomGoodsPublish> queryGoodsShowInfos(CustomGoodsQuery queryBean) {
        return customGoodsMapper.queryGoodsShowInfos(queryBean);
    }

    @Override
    public int queryGoodsShowInfosCount(CustomGoodsQuery queryBean) {
        return customGoodsMapper.queryGoodsShowInfosCount(queryBean);
    }

    @Override
    public List<ShopGoodsSalesAmount> queryShopGoodsSalesAmountAll() {
        return customGoodsMapper.queryShopGoodsSalesAmountAll();
    }

    @Override
    public ShopGoodsSalesAmount queryShopGoodsSalesAmountByShopId(String shopId) {
        return customGoodsMapper.queryShopGoodsSalesAmountByShopId(shopId);
    }

    @Override
    public int insertIntoGoodsImgUpLog(String pid, String imgUrl, int adminId, String remark) {
        return customGoodsMapper.insertIntoGoodsImgUpLog(pid, imgUrl, adminId, remark);
    }

    @Override
    public List<CustomGoodsPublish> queryGoodsByShopId(String shopId) {
        return customGoodsMapper.queryGoodsByShopId(shopId);
    }

    @Override
    public List<String> queryKidsCanUploadCatid() {
        return customGoodsMapper.queryKidsCanUploadCatid();
    }

    @Override
    public List<Map<String, String>> queryAllOffLineReason() {
        return customGoodsMapper.queryAllOffLineReason();
    }

    @Override
    public String queryChTypeBySkuId(String skuId) {
        return customGoodsMapper.queryChTypeBySkuId(skuId);
    }

    @Override
    public List<String> queryAllShopBlackList() {
        return customGoodsMapper.queryAllShopBlackList();
    }

    @Override
    public int insertIntoDescribeLog(String pid, int adminId) {
        return customGoodsMapper.insertIntoDescribeLog(pid, adminId);
    }

    @Override
    public Map<String, String> queryDescribeLogInfo(String pid) {
        return customGoodsMapper.queryDescribeLogInfo(pid);
    }

    @Override
    public List<String> queryDescribeLogList() {
        return customGoodsMapper.queryDescribeLogList();
    }

    @Override
    public Map<String, String> getGoodsByPid(String pid) {
        // TODO Auto-generated method stub
        return customGoodsMapper.getGoodsByPid(pid);
    }

    @Override
    public int insertIntoGoodsOverSeaInfo(GoodsOverSea goodsOverSea) {
        return customGoodsMapper.insertIntoGoodsOverSeaInfo(goodsOverSea);
    }

    @Override
    public List<GoodsOverSea> queryGoodsOverSeaInfoByPid(String pid) {
        return customGoodsMapper.queryGoodsOverSeaInfoByPid(pid);
    }

    @Override
    public int setSearchable(String pid, int flag, int adminId) {
        customGoodsMapper.insertSearchableLog(pid, flag, adminId);
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.setSearchable(pid, flag);
    }

    @Override
    public int setTopSort(String pid, int newSort, int adminId) {
        customGoodsMapper.setTopSortLog(pid, newSort, adminId);
        mongoDbLocalUtil.updatePid(pid);
        return customGoodsMapper.setTopSort(pid, newSort);
    }

    @Override
    public int saveGoodsWeightChange(GoodsWeightChange weightChange) {
        return customGoodsMapper.saveGoodsWeightChange(weightChange);
    }

    @Override
    public List<GoodsWeightChange> queryGoodsWeightChangeList(GoodsWeightChange weightChange) {
        return customGoodsMapper.queryGoodsWeightChangeList(weightChange);
    }

    @Override
    public int queryGoodsWeightChangeListCount(GoodsWeightChange weightChange) {
        return customGoodsMapper.queryGoodsWeightChangeListCount(weightChange);
    }

    @Override
    public int updateGoodsOverSeaInfo(GoodsOverSea goodsOverSea) {
        return customGoodsMapper.updateGoodsOverSeaInfo(goodsOverSea);
    }

    @Override
    public int setGoodsWeightChangeFlag(GoodsWeightChange weightChange) {
        return customGoodsMapper.setGoodsWeightChangeFlag(weightChange);
    }

    @Override
    public List<String> queryOnlineSync() {
        return customGoodsMapper.queryOnlineSync();
    }

    @Override
    public int deleteOnlineSync(String pid) {
        return customGoodsMapper.deleteOnlineSync(pid);
    }

    @Override
    public int insertIntoOnlineSync(String pid) {
        return customGoodsMapper.insertIntoOnlineSync(pid);
    }

    @Override
    public List<CustomGoodsPublish> queryGoodsDeleteInfo(CustomGoodsQuery queryBean) {
        return customGoodsMapper.queryGoodsDeleteInfo(queryBean);
    }

    @Override
    public int queryGoodsDeleteInfoCount(CustomGoodsQuery queryBean) {
        return customGoodsMapper.queryGoodsDeleteInfoCount(queryBean);
    }

    @Override
    public CustomGoodsPublish queryGoodsDeleteDetails(String pid) {
        return customGoodsMapper.queryGoodsDeleteDetails(pid);
    }

    @Override
    public List<String> queryOrinfringementPids() {
        return customGoodsMapper.queryOrinfringementPids();
    }

    @Override
    public int syncDataToDeleteInfo(String pid) {
        return customGoodsMapper.syncDataToDeleteInfo(pid);
    }

    @Override
    public int deleteDataByPid(String pid) {
        customGoodsDao.deleteDataByPid(pid);
        return customGoodsMapper.deleteDataByPid(pid);
    }

    @Override
    public int updateDeleteInfoFlag(String pid) {
        return customGoodsMapper.updateDeleteInfoFlag(pid);
    }

    @Override
    public int querySalableByPid(String pid) {
        return customGoodsMapper.querySalableByPid(pid);
    }

    @Override
    public int setSalable(String pid, int flag, int adminId) {
        return customGoodsMapper.setSalable(pid, flag, adminId);
    }

    @Override
    public List<Map<String, Object>> getProductInfoByLimit(int minId, int maxId) {
        return customGoodsMapper.getProductInfoByLimit(minId, maxId);
    }

    @Override
    public int updateCustomShopType(String shopId, int type) {
        return customGoodsMapper.updateCustomShopType(shopId, type);
    }


    @Override
    public List<String> getPipeList() {
        return customGoodsMapper.getPipeList();
    }

    @Override
    public ProductSingleBean queryPidSingleBean(String pid) {
        return customGoodsMapper.queryPidSingleBean(pid);
    }

    @Override
    public int insertB2cPriceLog(CustomGoodsPublish goods) {
        return customGoodsMapper.insertB2cPriceLog(goods);
    }

    @Override
    public int setNoUpdatePrice(CustomGoodsPublish goods) {
        return customGoodsMapper.setNoUpdatePrice(goods);
    }

    @Override
    public CustomGoodsPublish selectB2cPriceLog(String pid) {
        return customGoodsMapper.selectB2cPriceLog(pid);
    }

    @Override
    public int saveNewGoodsDetails(CustomGoodsPublish cgp, int adminId, int type) {
        cgp.setAdminId(adminId);
        cgp.setGoodsState(type == 1 ? 4 : 5);
        int result = 0;
        CustomGoodsPublish customGoodsPublish = customGoodsMapper.queryNewGoodsDetailsByPid(cgp.getPid());
        if (customGoodsPublish == null) {
            result = customGoodsMapper.saveNewGoodsDetails(cgp);
        } else {
            result = customGoodsMapper.updateNewGoodsDetailsByInfo(cgp);
        }

        if (type == 1) {
            result = checkOnlineMongodbByPid(cgp.getPid());
            if (result == 0) {
                InputData inputData = new InputData('c'); //u???????????????c???????????????d????????????
                inputData.setPid(cgp.getPid());
                inputData.setPath_catid(cgp.getPathCatid());
                //inputData.setImg_check("1");
                //inputData.setValid("1");
                inputData.setImg(cgp.getImg());
                inputData.setCatid1(cgp.getCatid1());
                inputData.setGoodsstate(String.valueOf(cgp.getGoodsState()));
                inputData.setWprice(cgp.getWprice());
                inputData.setFree_price_new(cgp.getFree_price_new());
                inputData.setFinal_weight(cgp.getFinalWeight());
                inputData.setVolume_weight(cgp.getVolumeWeight());
                inputData.setMorder(String.valueOf(cgp.getMorder()));
                inputData.setCustom_main_image(cgp.getCustomMainImage());
                inputData.setEnname(cgp.getEnname());
                inputData.setEndetail(cgp.getEndetail());
                inputData.setEninfo(cgp.getEninfo());
                inputData.setSellunit(cgp.getSellUnit());
                inputData.setSize_info_en(cgp.getSizeInfoEn());
                inputData.setCur_time(DateFormatUtil.getWithSeconds(new Date()));

                boolean isOk = GoodsInfoUpdateOnlineUtil.updateLocalAndSolr(inputData, 1, 0);
                if (isOk) {
                    CustomGoodsPublish bean = queryGoodsDetails(cgp.getPid(), 0);
                    if (bean == null) {
                        result = customGoodsMapper.saveNewGoodsDetailsPush(cgp);
                    }
                } else {

                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public int queryNewPid() {
        int pid = customGoodsMapper.queryNewPid();
        customGoodsMapper.updateNewPid();
        return pid;
    }

    @Override
    public CustomGoodsPublish queryNewGoodsDetails(String pid) {
        CustomGoodsPublish bean = customGoodsMapper.queryNewGoodsDetailsByPid(pid);
        return bean;
    }

    @Override
    public int updateNewGoodsDetailsByInfo(CustomGoodsPublish cgp) {
        return customGoodsMapper.updateNewGoodsDetailsByInfo(cgp);
    }

    public static int checkOnlineMongodbByPid(String pid) {
        OKHttpUtils okHttpUtils = new OKHttpUtils();
        int rs = 0;
        try {
            String result = okHttpUtils.get(CHECK_PID_EXISTS_URL + pid);
            if ("1".equals(result)) {
                rs = 1;
            } else if ("0".equals(result)) {
                rs = 0;
            } else if ("-1".equals(result)) {
                rs = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            rs = -2;
        }
        System.err.println("pid:" + pid + ",mongodb query result:" + rs);
        return rs;
    }

    @Override
    public int updateEntypeSkuByPid(CustomGoodsPublish cgp) {
        return customGoodsMapper.updateEntypeSkuByPid(cgp);
    }

    @Override
    public int batchUpdatePriceAndWeight(List<CustomGoodsPublish> list) {
        return customGoodsMapper.batchUpdatePriceAndWeight(list);
    }

}
