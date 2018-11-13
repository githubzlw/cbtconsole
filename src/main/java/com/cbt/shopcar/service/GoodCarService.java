package com.cbt.shopcar.service;

import com.cbt.shopcar.pojo.GoodCar;
import com.cbt.shopcar.pojo.GoodsCar;
import com.cbt.shopcar.pojo.GoodsCarExample;
import com.importExpress.pojo.SpiderNewBean;

import java.util.List;

public interface GoodCarService {

    List<GoodsCar> selectByExample(GoodsCarExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCar record);

    int insertSelective(GoodsCar record);

    int updateByPrimaryKey(GoodsCar record);

    List<GoodCar> getGoodCar(String sessionid, Integer userid, Integer preshopping);


    int updateGoodsCarPrice(int goodsId, int userId, double goodsPrice);


    List<SpiderNewBean> querySpiderBeanByUserId(int userId);

    int deleteMarketingByUserId(int userId);
}
