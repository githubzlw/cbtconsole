package com.importExpress.service;

import com.importExpress.pojo.Saleprofitrate;
import com.importExpress.pojo.SaleprofitrateExample;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SaleprofitrateService {
    int countByExample(SaleprofitrateExample example);

    int deleteByExample(SaleprofitrateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Saleprofitrate record);

    int insertSelective(Saleprofitrate record);

   /* List<Saleprofitrate> selectByExample(SaleprofitrateExample example);*/

    Saleprofitrate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Saleprofitrate record, @Param("example") SaleprofitrateExample example);

    int updateByExample(@Param("record") Saleprofitrate record, @Param("example") SaleprofitrateExample example);

    int updateByPrimaryKeySelective(Saleprofitrate record);

    int updateByPrimaryKey(Saleprofitrate record);
    
    Map<String,Object> getList(SaleprofitrateExample example, Integer currentPage, Integer pageSize, int userId);

}