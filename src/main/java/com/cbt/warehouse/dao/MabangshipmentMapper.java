package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.Mabangshipment;
import com.cbt.warehouse.pojo.MabangshipmentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MabangshipmentMapper {
    int countByExample(MabangshipmentExample example);

    int deleteByExample(MabangshipmentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Mabangshipment record);

    int insertSelective(Mabangshipment record);

    List<Mabangshipment> selectByExample(MabangshipmentExample example);

    Mabangshipment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Mabangshipment record, @Param("example") MabangshipmentExample example);

    int updateByExample(@Param("record") Mabangshipment record, @Param("example") MabangshipmentExample example);

    int updateByPrimaryKeySelective(Mabangshipment record);

    int updateByPrimaryKey(Mabangshipment record);
    
    //添加马帮运单信息列表
    int insertMabangShipment(@Param("list") List<Mabangshipment> list);
    
    //查询到货情况
    List<Mabangshipment> selectInstorage(Mabangshipment mb);
    //查询到货情况数量
    int selectInstorageCount(Mabangshipment mb);
    
    //查询采购成本
    List<Mabangshipment> selectPurchaseCost(Mabangshipment mb);
}