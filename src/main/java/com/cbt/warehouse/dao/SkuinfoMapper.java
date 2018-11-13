package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.Skuinfo;
import com.cbt.warehouse.pojo.SkuinfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuinfoMapper {
    int countByExample(SkuinfoExample example);

    int deleteByExample(SkuinfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Skuinfo record);

    int insertSelective(Skuinfo record);

    List<Skuinfo> selectByExample(SkuinfoExample example);

    Skuinfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Skuinfo record, @Param("example") SkuinfoExample example);

    int updateByExample(@Param("record") Skuinfo record, @Param("example") SkuinfoExample example);

    int updateByPrimaryKeySelective(Skuinfo record);

    int updateByPrimaryKey(Skuinfo record);
    
  //添加马帮运单信息列表
    int insertSkuinfo(@Param("list") List<Skuinfo> list);
    
    public List<Skuinfo> selectShipImgLinkBySKU(Skuinfo sku);
}