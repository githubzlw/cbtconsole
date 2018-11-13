package com.cbt.warehouse.service;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.GroupBuyGoodsBean;
import com.cbt.bean.ShopGoodsInfo;
import com.cbt.warehouse.pojo.GroupBuyManageBean;

import java.util.List;


public interface GroupBuyService {

    /**
     * @param id
     * @param beginTime
     * @param endTime
     * @param startNum
     * @param offset
     * @return List<GroupBuyManageBean>
     * @Title getGroupBuyInfos
     * @Description 获取分页的团购商品信息
     */
    List<GroupBuyManageBean> getGroupBuyInfos(int id, String beginTime, String endTime, int type, int startNum, int offset);

    /**
     * @param id
     * @return GroupBuyManageBean
     * @Title queryInfoById
     * @Description 根据ID获取团购商品信息
     */
    GroupBuyManageBean queryInfoById(int id);

    /**
     * @param id
     * @param beginTime
     * @param endTime
     * @return int
     * @Title getGroupBuyInfosCount
     * @Description 获取全部团购商品数据的总数
     */
    int getGroupBuyInfosCount(int id, String beginTime, String endTime, int type);

    /**
     * @param gbmInfo
     * @return int
     * @Title insertGroupBuyInfos
     * @Description 插入团购商品信息
     */
    int insertGroupBuyInfos(GroupBuyManageBean gbmInfo);

    /**
     * @param gbmInfo
     * @return boolean
     * @Title updateGroupBuyInfos
     * @Description 更新团购商品信息
     */
    boolean updateGroupBuyInfos(GroupBuyManageBean gbmInfo);

    /**
     * @param id
     * @return boolean
     * @Title deleteGroupBuyInfos
     * @Description 删除团购商品信息
     */
    boolean deleteGroupBuyInfos(int id);

    /**
     * @param pid
     * @return
     * @Title queryLatestDateInfoByPid
     * @Description 查询最近一次添加的此商品
     */
    GroupBuyManageBean queryLatestDateInfoByPid(String pid);


    /**
     * 查询团购活动下选中的团购商品
     *
     * @param gbId
     * @return
     */
    List<ShopGoodsInfo> queryShopGoodsFromGroupBuy(int gbId);

    /**
     * 插入新的团购商品的痛店铺商品数据
     *
     * @param infos
     * @return
     */
    boolean insertGroupBuyShopGoods(List<GroupBuyGoodsBean> infos);

    /**
     * 删除团购商品的痛店铺商品数据
     *
     * @param infos
     * @return
     */
    boolean deleteGroupBuyShopGoods(List<GroupBuyGoodsBean> infos);

    /**
     * 根据店铺ID下非PID的查询产品表所有商品
     *
     * @param shopId
     * @param pid
     * @return
     */
    List<ShopGoodsInfo> queryShopGoodsByShopIdAndPid(String shopId, String pid);

    /**
     * 根据pid获取1688商品信息
     *
     * @param pid
     * @return
     */
    CustomGoodsPublish queryFor1688Goods(String pid);

}
