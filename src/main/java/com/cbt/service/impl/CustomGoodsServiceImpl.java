package com.cbt.service.impl;

import com.cbt.bean.*;
import com.cbt.controller.EditorController;
import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.parse.service.DownloadMain;
import com.cbt.service.CustomGoodsService;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.mapper.CustomGoodsMapper;
import com.importExpress.pojo.*;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                GoodsInfoUpdateOnlineUtil.updateCustomBenchmarkSkuNewByMq(bean.getPid(),insertList);
                insertList.clear();
            }
        }

        int res = customGoodsDao.publish(bean,0);
        // 屏蔽使用jdbc更新AWS数据
        //int res = customGoodsDao.publish(bean);

        // 使用MQ更新AWS服务器数据
        // GoodsInfoUpdateOnlineUtil.publishToOnlineByMq(bean);

        // 使用MongoDB更新AWS服务器数据
        GoodsInfoUpdateOnlineUtil.publishToOnlineByMongoDB(bean);

        res = 1;
        if (res > 0) {
            int count = customGoodsDao.publishTo28(bean);
            if (count == 0) {
                customGoodsDao.publishTo28(bean);
            }
        }

        //更新SkuGoodsOffers和SingleOffersChild信息
        int count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
        //如果存在SkuGoodsOffers信息直接更新SkuGoodsOffers
        if (count > 0) {
            customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
        } else {
            //否则插入或者更新SingleOffersChild信息
            customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
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
    public boolean updateStateList(int state, String pids, int adminid) {
        // AWS更新
        // GoodsInfoUpdateOnlineUtil.batchUpdateGoodsStateByMQ(state, pids, adminid);
        GoodsInfoUpdateOnlineUtil.batchUpdateGoodsStateMongoDB(state, pids, adminid);
        // 本地更新
        return customGoodsDao.updateStateList(state, pids, adminid);
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
        return customGoodsMapper.queryGoodsDetailsByPid(pid);
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
    public int setGoodsValid(String pid, String adminName, int adminId, int type,String remark) {
        // AWS更新
        // MQ
        // GoodsInfoUpdateOnlineUtil.setGoodsValidByMq(pid,type);
        // MongoDB
        GoodsInfoUpdateOnlineUtil.setGoodsValidByMongoDb(pid,type);
        return customGoodsDao.setGoodsValid(pid, adminName, adminId, type, 6,remark);
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
        if(editBean.getBenchmarking_flag() == 1){
            CustomGoodsPublish good = customGoodsDao.getGoods(editBean.getPid(), 0);
            double finalWeight = 0;
            if(StringUtils.isNotBlank(good.getFinalWeight())){
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
    public int updateGoodsWeightByPid(String pid, double newWeight,double oldWeight,int weightIsEdit) {
        return customGoodsMapper.updateGoodsWeightByPid(pid, newWeight,oldWeight,weightIsEdit);
    }

    @Override
    public int editAndLockProfit(String pid, int type, double editProfit) {
        return customGoodsMapper.editAndLockProfit(pid, type, editProfit);
    }

    @Override
    public JsonResult setGoodsWeightByWeigher(String pid, String newWeight) {
        JsonResult json = new JsonResult();
        // 获取商品信息
        CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
        boolean is = updateGoodsWeightByPid(pid, Double.valueOf(newWeight), Double.valueOf(orGoods.getFinalWeight()), 2) > 0;
        if (is) {
            // 重新刷新价格数据
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
            }
        } else {
            json.setOk(false);
            json.setMessage("执行错误，请重试");
        }
        return json;
    }

    @Override
    public JsonResult setGoodsWeightByWeigherNew(String pid, String newWeight) {
        JsonResult json = setGoodsWeightByWeigher(pid, newWeight);
        if (json.isOk()) {
            CustomGoodsPublish orGoods = queryGoodsDetails(pid, 0);
            boolean isSuccess = refreshPriceRelatedData(orGoods);
            if (!isSuccess) {
                json.setOk(false);
                json.setMessage("更新数据失败");
            }
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
    public boolean refreshPriceRelatedData(CustomGoodsPublish bean) {
        //更新28库的custom_benchmark_ready_newest [source_pro_flag]=7
        int count = customGoodsDao.updateSourceProFlag(bean.getPid());
        if (count > -1) {
            //更新SkuGoodsOffers和SingleOffersChild信息
            count = customGoodsDao.checkSkuGoodsOffers(bean.getPid());
            if (count > 0) {
                count = customGoodsDao.updateSkuGoodsOffers(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
            } else {
                count = customGoodsDao.insertIntoSingleOffersChild(bean.getPid(), Double.valueOf(bean.getFinalWeight()));
            }
            if(count > -1){
                //插入sku信息
                customGoodsDao.deleteSkuByPid(bean.getPid());
                List<CustomBenchmarkSkuNew> list = customGoodsDao.querySkuByPid(bean.getPid());
                if(!(list == null || list.isEmpty())){
                    count = customGoodsDao.insertIntoSkuToOnline(list);
                }else{
                    System.err.println("pid:" + bean.getPid() + ",sku list is empty");
                }
            }
        }

        return count > 0;
    }

}
