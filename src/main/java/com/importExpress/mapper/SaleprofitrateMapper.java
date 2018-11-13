package com.importExpress.mapper;

import com.importExpress.pojo.Saleprofitrate;
import com.importExpress.pojo.SaleprofitrateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaleprofitrateMapper {
    int countByExample(SaleprofitrateExample example);

    int deleteByExample(SaleprofitrateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Saleprofitrate record);

    int insertSelective(Saleprofitrate record);

    List<Saleprofitrate> selectByExample(SaleprofitrateExample example, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    Saleprofitrate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Saleprofitrate record, @Param("example") SaleprofitrateExample example);

    int updateByExample(@Param("record") Saleprofitrate record, @Param("example") SaleprofitrateExample example);

    int updateByPrimaryKeySelective(Saleprofitrate record);

    int updateByPrimaryKey(Saleprofitrate record);

    List<Saleprofitrate>  getList(@Param("userId") int userId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
    
}