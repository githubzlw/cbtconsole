package com.importExpress.service;

import com.importExpress.pojo.GoodsCarconfig;
import com.importExpress.pojo.GoodsCarconfigExample;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCarconfigService {
    int countByExample(GoodsCarconfigExample example);

    int deleteByExample(GoodsCarconfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCarconfigWithBLOBs record);

    int insertSelective(GoodsCarconfigWithBLOBs record);

    List<GoodsCarconfigWithBLOBs> selectByExampleWithBLOBs(GoodsCarconfigExample example);

    List<GoodsCarconfig> selectByExample(GoodsCarconfigExample example);

    GoodsCarconfigWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GoodsCarconfigWithBLOBs record, @Param("example") GoodsCarconfigExample example);

    int updateByExampleWithBLOBs(@Param("record") GoodsCarconfigWithBLOBs record, @Param("example") GoodsCarconfigExample example);

    int updateByExample(@Param("record") GoodsCarconfig record, @Param("example") GoodsCarconfigExample example);

    int updateByPrimaryKeySelective(GoodsCarconfigWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GoodsCarconfigWithBLOBs record);

    int updateByPrimaryKey(GoodsCarconfig record);

    List<GoodsCarconfigWithBLOBs> queryByIsNew(int userId);

    //更新标识
    int updateByIdAndUserId(int id, int userId);

    //更新表信息
    boolean updateGoodsCarConfig(GoodsCarconfigWithBLOBs record);
}
