package com.cbt.report.dao;

import com.cbt.pojo.GeneralReport;
import com.cbt.pojo.ReportInfo;
import com.cbt.pojo.ReportInfoExample;
import com.cbt.pojo.StatisticalReportPojo;
import com.cbt.report.vo.StatisticalReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportInfoMapper {
    int countByExample(ReportInfoExample example);

    int deleteByExample(ReportInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReportInfo record);
    /**
     * 查询分类销售统计报表
     * @param map
     * @return
     */
    List<StatisticalReportPojo> getCategoryReport(Map<String, String> map);
    /**
     * 查询总分类销售统计报表
     * @param map
     * @return
     */
    List<StatisticalReportPojo> getAllCategoryReport(Map<String, String> map);
    /**
     * 查询分类销售总数
     * @param map
     * @return
     */
    List<StatisticalReportPojo> getCategoryReportCount(Map<String, String> map);
    int insertSelective(ReportInfo record);

    List<ReportInfo> selectByExample(ReportInfoExample example);

    ReportInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReportInfo record, @Param("example") ReportInfoExample example);

    int updateByExample(@Param("record") ReportInfo record, @Param("example") ReportInfoExample example);

    int updateByPrimaryKeySelective(ReportInfo record);

    int updateByPrimaryKey(ReportInfo record);
    
    List<ReportInfo> selectByTime(@Param("t") StatisticalReportVo statisticalReportVo);
    
    List<ReportInfo> selectOrderByTime(StatisticalReportVo statisticalReportVo);
    
    List<ReportInfo> selectByGeneral(GeneralReport generalReport);
}