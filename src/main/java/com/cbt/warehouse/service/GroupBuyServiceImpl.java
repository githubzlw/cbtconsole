package com.cbt.warehouse.service;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.GroupBuyGoodsBean;
import com.cbt.bean.ShopGoodsInfo;
import com.cbt.warehouse.dao.GroupBuyDao;
import com.cbt.warehouse.dao.GroupBuyDaoImpl;
import com.cbt.warehouse.pojo.GroupBuyManageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupBuyServiceImpl implements GroupBuyService {
    private GroupBuyDao dao = new GroupBuyDaoImpl();


    @Override
    public List<GroupBuyManageBean> getGroupBuyInfos(int id, String beginTime, String endTime,int type, int startNum, int offset) {
        return dao.getGroupBuyInfos(id, beginTime, endTime, type,startNum, offset);
    }

    @Override
    public GroupBuyManageBean queryInfoById(int id) {
        return dao.queryInfoById(id);
    }

    @Override
    public int getGroupBuyInfosCount(int id, String beginTime, String endTime,int type) {
        return dao.getGroupBuyInfosCount(id, beginTime, endTime, type);
    }

    @Override
    public int insertGroupBuyInfos(GroupBuyManageBean gbmInfo) {
        return dao.insertGroupBuyInfos(gbmInfo);
    }

    @Override
    public boolean updateGroupBuyInfos(GroupBuyManageBean gbmInfo) {
        return dao.updateGroupBuyInfos(gbmInfo);
    }

    @Override
    public boolean deleteGroupBuyInfos(int id) {
        return dao.deleteGroupBuyInfos(id);
    }

    @Override
    public GroupBuyManageBean queryLatestDateInfoByPid(String pid) {
        return dao.queryLatestDateInfoByPid(pid);
    }

    @Override
    public List<ShopGoodsInfo> queryShopGoodsFromGroupBuy(int gbId) {
        return dao.queryShopGoodsFromGroupBuy(gbId);
    }

    @Override
    public boolean insertGroupBuyShopGoods(List<GroupBuyGoodsBean> infos) {
        return dao.insertGroupBuyShopGoods(infos);
    }

    @Override
    public boolean deleteGroupBuyShopGoods(List<GroupBuyGoodsBean> infos) {
        return dao.deleteGroupBuyShopGoods(infos);
    }

    @Override
    public List<ShopGoodsInfo> queryShopGoodsByShopIdAndPid(String shopId, String pid) {
        return dao.queryShopGoodsByShopIdAndPid(shopId, pid);
    }

    @Override
    public CustomGoodsPublish queryFor1688Goods(String pid) {
        return dao.queryFor1688Goods(pid);
    }
}
