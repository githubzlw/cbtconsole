package com.cbt.customer.service;


import com.cbt.bean.*;
import com.cbt.customer.dao.IShopUrlDao;
import com.cbt.customer.dao.ShopUrlDaoImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ShopUrlServiceImpl implements IShopUrlService {

    IShopUrlDao dao = new ShopUrlDaoImpl();

    @Override
    public ShopUrl findById(int id) {
        return dao.findById(id);
    }

    @Override
    public List<ShopUrl> findAll(String shopId, String shopBrand, String shopUserName, String date, int start,
                                 int end, String timeFrom, String timeTo, int isOn, int state, int isAuto, int readyDel,
                                 int shopType,int authorizedFlag,int authorizedFileFlag,int ennameBrandFlag,String shopids, int translateDescription) {
        return dao.findAll(shopId, shopBrand, shopUserName, date, start, end, timeFrom, timeTo, isOn, state, isAuto, readyDel,shopType,authorizedFlag,
                authorizedFileFlag,ennameBrandFlag,shopids, translateDescription);
    }

    @Override
    public String getShopList(String admName, String days) {
        return dao.getShopList(admName,days);
    }

    @Override
    public int total(String shopId, String shopBrand, String shopUserName, String date, String timeFrom, String timeTo, int isOn, int state,
                     int isAuto, int readyDel,int shopType,int authorizedFlag,int authorizedFileFlag,int ennameBrandFlag,String shopids, int translateDescription) {
        return dao.total(shopId, shopBrand, shopUserName, date, timeFrom, timeTo, isOn, state, isAuto, readyDel,shopType,authorizedFlag,
                authorizedFileFlag,ennameBrandFlag,shopids, translateDescription);
    }

    @Override
    public int delById(int id) {
        return dao.delById(id);
    }

    @Override
    public int insertOrUpdate(ShopUrl su, String[] urls,int isChange) {

        if(isChange > 0){
            dao.changShopToManually(su.getShopId());
        }
        return dao.insertOrUpdate(su, urls);
    }

    @Override
    public List<ShopGoods> findAllGoods(String shopId, int start, int end) {
        return dao.findAllGoods(shopId, start, end);
    }

    @Override
    public int goodsTotal(String shopId) {
        return dao.goodsTotal(shopId);
    }

    @Override
    public List<ShopInfoBean> queryInfoByShopId(String shopId, String catid) {
        return dao.queryInfoByShopId(shopId, catid);
    }

    @Override
    public List<GoodsProfitReference> queryGoodsProfitReferences(String shopId) {
        return dao.queryGoodsProfitReferences(shopId);
    }

    @Override
    public boolean saveAndUpdateInfos(List<ShopInfoBean> shopInfos) {
        return dao.saveCategoryInfos(shopInfos);
    }

    @Override
    public List<ShopGoodsInfo> queryDealGoodsByShopId(String shopId) {
        return dao.queryDealGoodsByShopId(shopId);
    }

    @Override
    public boolean insertShopInfos(List<ShopInfoBean> infos) {
        //获取其他相同的类别关键词重量插入当前类别数据
        dao.insertShopCatidAvgWeight(infos);
        return dao.insertShopInfos(infos);
    }

    @Override
    public boolean updateShopInfos(List<ShopInfoBean> infos) {
        return dao.updateShopInfos(infos);
    }

    @Override
    public List<ShopGoodsInfo> query1688GoodsByShopId(String shopId) {
        return dao.query1688GoodsByShopId(shopId);
    }

    @Override
    public boolean deleteShopReadyGoods(String shopId, String pids) {
        return dao.deleteShopReadyGoods(shopId, pids);
    }


    @Override
    public boolean deleteShopOfferGoods(String shopId, String pids) {
        return dao.deleteShopOfferGoods(shopId, pids);
    }


    @Override
    public List<ShopGoodsEnInfo> queryDealGoodsWithInfoByShopId(String shopId) {
        return dao.queryDealGoodsWithInfoByShopId(shopId);
    }

    @Override
    public boolean updateGoodsEninfo(List<ShopGoodsEnInfo> goodsList) {
        return dao.updateGoodsEninfo(goodsList);
    }

    @Override
    public CustomGoodsPublish queryGoodsInfo(String shopId, String pid) {
        return dao.queryGoodsInfo(shopId, pid);
    }

    @Override
    public boolean saveEditGoods(CustomGoodsPublish cgp, String shopId, int adminId) {
        return dao.saveEditGoods(cgp, shopId, adminId);
    }

    @Override
    public Map<String, String> queryShopDealState(String shopId) {
        return dao.queryShopDealState(shopId);
    }

    @Override
    public List<Category1688Bean> queryAll1688Category() {
        return dao.queryAll1688Category();
    }

    @Override
    public Map<String, Integer> queryDealState(String shopId) {
        return dao.queryDealState(shopId);
    }

    @Override
    public boolean updateShopState(String shopId, int state) {
        return dao.updateShopState(shopId, state);
    }

    @Override
    public List<ShopCatidWeight> queryShopCatidWeightListByShopId(String shopId) {
        return dao.queryShopCatidWeightListByShopId(shopId);
    }

    @Override
    public boolean batchInsertCatidWeight(List<ShopCatidWeight> list) {
        return dao.batchInsertCatidWeight(list);
    }

    @Override
    public boolean batchUpdateCatidWeight(List<ShopCatidWeight> list) {
        return dao.batchUpdateCatidWeight(list);
    }

    @Override
    public float calculateAvgWeightByCatid(String shopId, String catid) {
        return dao.calculateAvgWeightByCatid(shopId, catid);
    }

    @Override
    public List<GoodsOfferBean> queryOriginalGoodsInfo(String shopId) {
        return dao.queryOriginalGoodsInfo(shopId);
    }

    @Override
    public boolean batchUpdateErrorWeightGoods(List<GoodsOfferBean> goodsErrInfos) {
        return dao.batchUpdateErrorWeightGoods(goodsErrInfos);
    }

    @Override
    public boolean deleteOriginalGoodsOffer(String shopId, String pids) {
        return dao.deleteOriginalGoodsOffer(shopId, pids);
    }

    @Override
    public boolean insertShopGoodsDeleteImgs(List<ShopGoodsInfo> shopGoodsInfos, int adminId) {
        return dao.insertShopGoodsDeleteImgs(shopGoodsInfos, adminId);
    }

    @Override
    public boolean updateWeightFlag(String shopId) {
        return dao.updateWeightFlag(shopId);
    }

    @Override
    public List<CustomGoodsPublish> queryOnlineGoodsByShopId(String shopId) {
        return dao.queryOnlineGoodsByShopId(shopId);
    }

    @Override
    public boolean saveReadyDeleteShop(String shopId, int type, String remark) {
        return dao.saveReadyDeleteShop(shopId, type, remark);
    }

    @Override
    public boolean deleteCatidGoods(String shopId, String catids) {
        return dao.deleteCatidGoods(shopId,catids);
    }

    @Override
    public boolean checkIsBlackShopByShopId(String shopId) {
        return dao.checkIsBlackShopByShopId(shopId);
    }

    @Override
    public boolean setShopType(String shopId, int type) {
        return dao.setShopType(shopId,type);
    }

    @Override
    public boolean setAuthorizedFlag(String shopId, Integer authorizedFlag) {
        return dao.setAuthorizedFlag(shopId, authorizedFlag);
    }

    @Override
    public List<NeedOffShelfBean> queryNeedOffShelfByParam(NeedOffShelfBean offShelf) {
        return dao.queryNeedOffShelfByParam(offShelf);
    }

    @Override
    public int queryNeedOffShelfByParamCount(NeedOffShelfBean offShelf) {
        return dao.queryNeedOffShelfByParamCount(offShelf);
    }

    @Override
    public Map<String, Integer> queryCompetitiveFlag(List<String> pids) {
        return dao.queryCompetitiveFlag(pids);
    }

    @Override
    public boolean deleteGoodsByPid(String pid) {
        return dao.deleteGoodsByPid(pid);
    }

    @Override
    public int setShopTranslate(String shopId) {
        return dao.setShopTranslate(shopId);
    }


}
