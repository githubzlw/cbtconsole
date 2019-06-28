package com.cbt.service.impl;

import com.cbt.bean.*;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.BigDecimalUtil;
import com.cbt.util.GoodsInfoUtils;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.mapper.CustomGoodsMapper;
import com.importExpress.pojo.*;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomGoodsServiceImpl implements CustomGoodsService {
    private CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();

    @Autowired
    private CustomGoodsMapper customGoodsMapper;

    @Override
    public List<CategoryBean> getCaterory() {

        return customGoodsDao.getCaterory();
    }

    @Override
    public int addReviewRemark(Map<String, String> map) {
        //保存一条记录 和 保存原表
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

        return customGoodsDao.updateInfo(bean);
    }

    @Override
    public int publish(CustomGoodsPublish bean) {

        //如果存在range_price,则更新sku数据
        if (StringUtils.isNotBlank(bean.getRangePrice())) {
            //sku更新
            List<CustomBenchmarkSkuNew> insertList = new ArrayList<>();
            JSONArray sku_json = JSONArray.fromObject(bean.getSku());
            List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);

            for (ImportExSku exSku : skuList) {
                CustomBenchmarkSkuNew skuNew = new CustomBenchmarkSkuNew();
                skuNew.setFinalWeight(bean.getFinalWeight());
                skuNew.setWprice(bean.getWprice());
                skuNew.setSkuPropIds(exSku.getSkuPropIds());
                skuNew.setSkuAttr(exSku.getSkuAttr());
                skuNew.setPid(bean.getPid());
                SkuValPO skuValPO = new SkuValPO();
                skuValPO.setActSkuCalPrice(Double.valueOf(exSku.getSkuVal().getActSkuCalPrice()));
                skuValPO.setActSkuMultiCurrencyCalPrice(Double.valueOf(exSku.getSkuVal().getActSkuMultiCurrencyCalPrice()));
                skuValPO.setActSkuMultiCurrencyDisplayPrice(Double.valueOf(exSku.getSkuVal().getActSkuMultiCurrencyDisplayPrice()));
                skuValPO.setSkuMultiCurrencyCalPrice(Double.valueOf(exSku.getSkuVal().getSkuMultiCurrencyCalPrice()));
                skuValPO.setSkuMultiCurrencyDisplayPrice(Double.valueOf(exSku.getSkuVal().getSkuMultiCurrencyDisplayPrice()));
                skuValPO.setSkuCalPrice(Double.valueOf(exSku.getSkuVal().getSkuCalPrice()));
                skuNew.setSkuVal(skuValPO);
                insertList.add(skuNew);

            }
            if (insertList.size() > 0) {
                // 更新27和28表数据
                customGoodsDao.updateCustomBenchmarkSkuNew(bean.getPid(), insertList);
                // 使用MQ更新AWS服务器数据
                // GoodsInfoUpdateOnlineUtil.updateCustomBenchmarkSkuNewByMq(bean.getPid(), insertList);
                insertList.clear();
            }
        }

        //int res = customGoodsDao.publish(bean,0);
        // 屏蔽使用jdbc更新AWS数据
        //int res = customGoodsDao.publish(bean);

        // 使用MQ更新AWS服务器数据
        // GoodsInfoUpdateOnlineUtil.publishToOnlineByMq(bean);

        int res = 0;
        // 使用MongoDB更新AWS服务器数据
        if (GoodsInfoUpdateOnlineUtil.publishToOnlineByMongoDB(bean)) {
            // 更新27
            customGoodsDao.publish(bean, 0);
            // 更新28并重试一次
            res = customGoodsDao.publishTo28(bean);
            if (res == 0) {
                customGoodsDao.publishTo28(bean);
            }

            /*//更新SkuGoodsOffers和SingleOffersChild信息
            int count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
            //如果存在SkuGoodsOffers信息直接更新SkuGoodsOffers
            if (count > 0) {
                customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
            } else {
                //否则插入或者更新SingleOffersChild信息
                customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
            }*/
        }else{
            customGoodsMapper.insertIntoGoodsImgUpLog(bean.getPid(),"",bean.getAdminId(),"publish error");
        }
        return res;
    }


    @Override
    public String getGoodsInfo(String pid) {

        return customGoodsDao.getGoodsInfo(pid);
    }

    @Override
    public int updateState(int state, String pid, int adminid) {

        return customGoodsDao.updateState(state, pid, adminid);
    }

    @Override
    public int updateValid(int valid, String pid) {

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
        // AWS更新
        // GoodsInfoUpdateOnlineUtil.batchUpdateGoodsStateByMQ(state, pids, adminid);
        GoodsInfoUpdateOnlineUtil.batchUpdateGoodsStateMongoDB(state, pids, adminid);
        // 本地更新
        return customGoodsDao.updateStateList(state, pids, adminid, reason);
    }

    @Override
    public int updateValidList(int valid, String pids) {

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
    }

    @Override
    public CustomGoodsPublish queryGoodsDetails(String pid, int type) {
        //return customGoodsDao.queryGoodsDetails(pid, type);
        DataSourceSelector.restore();
        CustomGoodsPublish bean = customGoodsMapper.queryGoodsDetailsByPid(pid);
        if(bean != null){
            bean.setOnlineUrl(GoodsInfoUtils.genOnlineUrl(bean));
        }
        return bean;
    }

    @Override
    public int saveEditDetalis(CustomGoodsPublish cgp, String adminName, int adminId, int type) {
        //return customGoodsDao.saveEditDetalis(cgp, adminName, adminId, type);
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
        // AWS更新
        // MQ
        // GoodsInfoUpdateOnlineUtil.setGoodsValidByMq(pid,type);
        // MongoDB
        GoodsInfoUpdateOnlineUtil.setGoodsValidByMongoDb(pid, type);
        return customGoodsDao.setGoodsValid(pid, adminName, adminId, type, 6, remark);
    }

    @Override
    public int setGoodsValid2(String pid, String adminName, int adminId, int type, String remark) {
        // AWS更新
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
        return customGoodsDao.updateGoodsState(pid, goodsState);
    }

    @Override
    public boolean updateBmFlagByPids(String[] pidLst, int adminid) {
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
        return customGoodsDao.deleteWordSizeInfoByPid(pid);
    }

    @Override
    public boolean setNoBenchmarking(String pid, double finalWeight) {
        return customGoodsDao.setNoBenchmarking(pid, finalWeight);
    }

    @Override
    public boolean upCustomerReady(String pid, String aliPid, String aliPrice, int bmFlag, int isBenchmark, String edName, String rwKeyword, int flag) {
        return customGoodsDao.upCustomerReady(pid, aliPid, aliPrice, bmFlag, isBenchmark, edName, rwKeyword, flag);
    }


    @Override
    public boolean setNeverOff(String pid) {
        return customGoodsDao.setNeverOff(pid);
    }

    @Override
    public int insertPidIsEdited(String shopId, String pid, int adminId) {
        return customGoodsMapper.insertPidIsEdited(shopId, pid, adminId);
    }

    @Override
    public int checkIsEditedByPid(String pid) {
        return customGoodsMapper.checkIsEditedByPid(pid);
    }

    @Override
    public int updatePidIsEdited(GoodsEditBean editBean) {
        //如果有对标标识，则进行非对标的相关数据清除
        if (editBean.getBenchmarking_flag() == 1) {
            CustomGoodsPublish good = customGoodsDao.getGoods(editBean.getPid(), 0);
            double finalWeight = 0;
            if (StringUtils.isNotBlank(good.getFinalWeight())) {
                finalWeight = Double.valueOf(good.getFinalWeight());
            }
            customGoodsDao.setNoBenchmarking(editBean.getPid(), finalWeight);
        }
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
        return customGoodsMapper.saveBenchmarking(pid, aliPid, aliPrice);
    }

    @Override
    public List<String> queryStaticizeList(String catid) {
        return customGoodsMapper.queryStaticizeList(catid);
    }

    @Override
    public int insertIntoGoodsEditBean(GoodsEditBean editBean) {
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
        return customGoodsMapper.updateCustomGoodsStatistic(goodsParseBean);
    }

    @Override
    public int checkIsExistsGoods(String pid) {
        return customGoodsMapper.checkIsExistsGoods(pid);
    }

    @Override
    public int insertCustomGoodsStatistic(GoodsParseBean goodsParseBean) {
        return customGoodsMapper.insertCustomGoodsStatistic(goodsParseBean);
    }

    @Override
    public int updateGoodsSearchNum(String pid) {
        return customGoodsMapper.updateGoodsSearchNum(pid);
    }

    @Override
    public int updateGoodsClickNum() {
        return customGoodsMapper.updateGoodsClickNum();
    }

    @Override
    public int updateGoodsWeightByPid(String pid, double newWeight, double oldWeight, int weightIsEdit) {
        // 数据库字段值revise_weight 人为修改重量 =  newWeight
        // 数据库字段值final_weight最终展示重量 =  newWeight
        // 数据库字段值 source_pro_flag = 7 标识是人为修改的重量
        // 数据库字段值weight 如果weight值是空的或者0，设置数weight =  newWeight
        // 数据库字段值old_weight,记录老的重量数据，old_weight = oldWeight
        return customGoodsMapper.updateGoodsWeightByPid(pid, newWeight, oldWeight, weightIsEdit);
    }

    @Override
    public int editAndLockProfit(String pid, int type, double editProfit) {
        return customGoodsMapper.editAndLockProfit(pid, type, editProfit);
    }

    @Override
    public JsonResult setGoodsWeightByWeigher(String pid, String newWeight, int weightIsEdit) {
        JsonResult json = new JsonResult();
        // 获取商品信息
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        boolean is = updateGoodsWeightByPid(pid, Double.valueOf(newWeight), Double.valueOf(orGoods.getFinalWeight()), weightIsEdit) > 0;
        if (is) {
            /*// 重新刷新价格数据
            String url = EditorController.SHOPGOODSWEIGHTCLEARURL + "pid=" + pid + "&finalWeight=" + newWeight
                    + "&sourceTable=custom_benchmark_ready&database=27";
            String resultJson = DownloadMain.getContentClient(url, null);
            System.err.println("pid=" + pid + ",result:[" + resultJson + "]");
            JSONObject jsonJt = JSONObject.fromObject(resultJson);
            System.out.println(json.toString());
            if (!jsonJt.getBoolean("ok")) {
                //更新成功后，直接发布到线上

                json.setOk(false);
                json.setMessage("修改重量后，价格清洗失败：" + jsonJt.getString("message"));
            } else {
                json.setOk(true);
                json.setMessage("执行成功");
            }*/

            json.setOk(true);
            json.setMessage("执行成功");
        } else {
            json.setOk(false);
            json.setMessage("执行错误，请重试");
        }
        return json;
    }

    @Override
    public JsonResult setGoodsWeightByWeigherNew(String pid, String newWeight, int weightIsEdit, int adminId) {
        JsonResult json = new JsonResult();
        // 获取商品信息
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        // 更新27数据库重量信息
        boolean is = updateGoodsWeightByPid(pid, Double.valueOf(newWeight), Double.valueOf(orGoods.getFinalWeight()), weightIsEdit) > 0;
        if (is) {
            // 插入日志记录
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
                json.setMessage("更新数据成功");
            } else {
                json.setOk(false);
                json.setMessage("更新数据失败");
            }
        } else {
            json.setOk(false);
            json.setMessage("更新数据失败");
        }
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
        // 更新28库的custom_benchmark_ready_newest [source_pro_flag]=7
        // 数据库字段值final_weight最终展示重量 =  newWeight
        // 数据库字段值 source_pro_flag = 7 标识是人为修改的重量
        int count = customGoodsDao.updateSourceProFlag(bean.getPid(), newWeight);
        if (count > -1) {
            //更新SkuGoodsOffers和SingleOffersChild信息
            count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
            if (count > 0) {
                count = customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.valueOf(newWeight));
            } else {
                count = customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.valueOf(newWeight));
            }
            /*if(count > -1){
                //插入sku信息
                customGoodsDao.deleteSkuByPid(bean.getPid());
                List<CustomBenchmarkSkuNew> list = customGoodsDao.querySkuByPid(bean.getPid());
                if(!(list == null || list.isEmpty())){
                    count = customGoodsDao.insertIntoSkuToOnline(list);
                }else{
                    System.err.println("pid:" + bean.getPid() + ",sku list is empty");
                }
            }*/
        }

        return count > 0;
    }

    @Override
    public int updatePromotionFlag(String pid) {
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        customGoodsDao.insertIntoSingleOffersChild(orGoods.getPid(), Double.valueOf(orGoods.getFinalWeight()));
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
        if(pidList != null && pidList.size() > 0){
            return customGoodsMapper.queryGoodsByPidList(pidList);
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updatePidEnInfo(CustomGoodsPublish gd) {
        customGoodsDao.updatePidEnInfo(gd);
        customGoodsMapper.updatePidEnInfo(gd);
        return GoodsInfoUpdateOnlineUtil.updatePidEnInfo(gd);
    }

    @Override
    public int updateMd5ImgDeleteFlag(String pid, String url, String shopId) {
        // 更新临时表数据
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
        // 更新临时表数据
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
        customGoodsDao.insertIntoSingleOffersChild(pid, Double.valueOf(orGoods.getFinalWeight()));
        return customGoodsMapper.setNewAliPidInfo(pid, aliPid, aliPrice);
    }

    @Override
    public List<String> queryPidListByState(int state) {
        return customGoodsMapper.queryPidListByState(state);
    }

    @Override
    public int insertIntoGoodsPriceOrWeight(GoodsEditBean editBean) {
        return customGoodsMapper.insertIntoGoodsPriceOrWeight(editBean);
    }

    @Override
    public Map<String, String> queryNewAliPriceByAliPid(String aliPid) {
        return customGoodsDao.queryNewAliPriceByAliPid(aliPid);
    }

    @Override
    public int updateWeightFlag(String pid, int flag) {
        return customGoodsMapper.updateWeightFlag(pid, flag);
    }

    @Override
    public int updateGoodsSku(String pid, String oldSku, String newSku, int adminId, double finalWeight) {
        // 1.更新产品表sku数据和标识
        customGoodsMapper.updateSkuInfo(pid, newSku);
        // 2.插入sku日志
        return customGoodsMapper.insertIntoSkuLog(pid, oldSku, newSku, adminId);
        // 3.走child表进行线上更新
        //return customGoodsDao.insertIntoSingleOffersChild(pid, finalWeight);
    }

    @Override
    public int remarkSoftGoodsValid(String pid, int reason) {
        return customGoodsMapper.remarkSoftGoodsValid(pid, reason);
    }

    @Override
    public List<String> queryPidByShopId(String shopId) {
        return customGoodsMapper.queryPidByShopId(shopId);
    }

    @Override
    public int updateVolumeWeight(String pid, String newWeight) {
        // 更新27
        customGoodsMapper.updateVolumeWeight(pid, newWeight);
        // 更新28
        customGoodsDao.updateVolumeWeight(pid, newWeight);
        // 更新mongodb
        GoodsInfoUpdateOnlineUtil.updateVolumeWeight(pid, newWeight);
        return 1;
    }

    @Override
    public JsonResult setGoodsWeightByWeigherInfo(String pid, SearchResultInfo weightAndSyn, int adminId) {
        JsonResult json = new JsonResult();
        // 获取商品信息
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        if (StringUtils.isNotBlank(orGoods.getSku())) {
            JSONArray sku_json = JSONArray.fromObject(orGoods.getSku());
            List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
            // 查找匹配的type数据
            String typeStr = weightAndSyn.getGoods_type();
            // Colour:black@32161,Size:S@4501,
            String[] typeStrList = typeStr.split(",");
            String ppId = "";
            for (String childType : typeStrList) {
                if (StringUtils.isNotBlank(childType)) {
                    String[] childList = childType.split("@");
                    if (childList.length == 2 && StringUtils.isNotBlank(childList[1])) {
                        ppId += "," + childList[1];
                    }
                }
            }
            double finalWeight = 0;
            for (ImportExSku exSku : skuList) {
                if (StringUtils.isNotBlank(ppId) && checkIsEqualPpid(ppId.substring(1), exSku.getSkuPropIds())) {
                    finalWeight = BigDecimalUtil.truncateDouble(Float.valueOf(weightAndSyn.getWeight()), 3);
                    exSku.setFianlWeight(finalWeight);
                    if (StringUtils.isNotBlank(weightAndSyn.getVolume_weight())) {
                        double volumeWeight = BigDecimalUtil.truncateDouble(Float.valueOf(weightAndSyn.getVolume_weight()), 3);
                        exSku.setVolumeWeight(volumeWeight);
                    }
                    break;
                }
            }
            // 进行sku更新
            updateGoodsSku(pid, orGoods.getSku(), skuList.toString(), adminId, finalWeight);
            skuList.clear();
        }

        // 插入日志记录
        GoodsEditBean editBean = new GoodsEditBean();
        editBean.setNew_title(weightAndSyn.getGoodsType() + ",sku 更新");
        editBean.setAdmin_id(adminId);
        if (StringUtils.isBlank(orGoods.getWeight()) || "0".equals(orGoods.getWeight()) || "0.00".equals(orGoods.getWeight())) {
            editBean.setWeight_old(orGoods.getWeight());
            editBean.setWeight_new(weightAndSyn.getWeight());
        }
        editBean.setRevise_weight_old(orGoods.getReviseWeight());
        editBean.setFinal_weight_old(orGoods.getFinalWeight());
        editBean.setRevise_weight_new(weightAndSyn.getWeight());
        editBean.setFinal_weight_new(weightAndSyn.getWeight());
        editBean.setPid(pid);
        customGoodsMapper.insertIntoGoodsPriceOrWeight(editBean);
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

}
