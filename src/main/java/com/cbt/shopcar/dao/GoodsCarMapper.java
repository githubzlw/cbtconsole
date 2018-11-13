package com.cbt.shopcar.dao;

import com.cbt.shopcar.pojo.GoodCar;
import com.cbt.shopcar.pojo.GoodsCar;
import com.cbt.shopcar.pojo.GoodsCarExample;
import com.importExpress.pojo.SpiderNewBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCarMapper {
    int countByExample(GoodsCarExample example);

    int deleteByExample(GoodsCarExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCar record);

    int insertSelective(GoodsCar record);

    List<GoodsCar> selectByExample(GoodsCarExample example);

    GoodsCar selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GoodsCar record, @Param("example") GoodsCarExample example);

    int updateByExample(@Param("record") GoodsCar record, @Param("example") GoodsCarExample example);

    int updateByPrimaryKeySelective(GoodsCar record);

    int updateByPrimaryKey(GoodsCar record);
    
    List<GoodCar> getGoodCar(@Param("sessionid") String sessionid, @Param("userid") Integer userid, @Param("preshopping") Integer preshopping);

    int updateGoodsCarPrice(@Param("goodsId") int goodsId, @Param("userId") int userId, @Param("goodsPrice") double goodsPrice);

    List<SpiderNewBean> querySpiderBeanByUserId(@Param("userId") int userId);

    int deleteMarketingByUserId(@Param("userId") int userId);
    
}