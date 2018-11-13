package com.cbt.report.dao;

import com.cbt.pojo.GeneralReport;
import com.cbt.pojo.GeneralReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GeneralReportMapper {
    int countByExample(GeneralReportExample example);

    int deleteByExample(GeneralReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GeneralReport record);

    int insertSelective(GeneralReport record);

    List<GeneralReport> selectByExample(GeneralReportExample example);

    GeneralReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GeneralReport record, @Param("example") GeneralReportExample example);

    int updateByExample(@Param("record") GeneralReport record, @Param("example") GeneralReportExample example);

    int updateByPrimaryKeySelective(GeneralReport record);

    int updateByPrimaryKey(GeneralReport record);
    
    GeneralReport selectBySelective(GeneralReport record);
    
    int selectExistExport(GeneralReport generalReport);
    
    int deleteReportDetailOrder(GeneralReport generalReport);
    /**
     * 生成订单分类报表 王宏杰2018-06-21
     * @param map
     * @return
     */
    int insertOrderReport(Map<String, String> map);
}