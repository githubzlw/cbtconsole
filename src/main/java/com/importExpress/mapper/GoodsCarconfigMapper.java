package com.importExpress.mapper;

import com.importExpress.pojo.GoodsCarconfig;
import com.importExpress.pojo.GoodsCarconfigExample;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import com.importExpress.pojo.ShopCarNewBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCarconfigMapper {
    int countByExample(GoodsCarconfigExample example);

    int deleteByExample(GoodsCarconfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCarconfigWithBLOBs record);

    int insertSelective(GoodsCarconfigWithBLOBs record);

    List<GoodsCarconfigWithBLOBs> selectByExampleWithBLOBs(GoodsCarconfigExample example);

    List<GoodsCarconfig> selectByExample(GoodsCarconfigExample example);

    GoodsCarconfigWithBLOBs selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") GoodsCarconfigWithBLOBs record, @Param("example") GoodsCarconfigExample example);

    int updateByExampleWithBLOBs(@Param("record") GoodsCarconfigWithBLOBs record, @Param("example") GoodsCarconfigExample example);

    int updateByExample(@Param("record") GoodsCarconfig record, @Param("example") GoodsCarconfigExample example);

    int updateByPrimaryKeySelective(GoodsCarconfigWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GoodsCarconfigWithBLOBs record);

    int updateByPrimaryKey(GoodsCarconfig record);

    List<GoodsCarconfigWithBLOBs> queryByIsNew(@Param("userId") int userId);

    int updateByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    /**
     * 新购物车Bean获取
     * @return
     */
     ShopCarNewBean queryShopCarNewBeanByUserId(int userId);
}