package com.cbt.warehouse.dao;

import com.cbt.pojo.ImgPojo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GetPriceMapper {
    @Select("SELECT id,remotpath as remotPath,eninfo as enInfo from custom_benchmark_ready WHERE catid1=#{categoryId} AND valid=1  LIMIT #{page},20")
    List<ImgPojo> FindAllImgUrl(@Param("categoryId") int categoryId, @Param("page") int page);
    @Select("SELECT category_id,`name` FROM 1688_category WHERE (parent_id in (SELECT category_id FROM 1688_category WHERE parent_id=311) OR parent_id=311)" +
            " and category_id not in(SELECT parent_id FROM 1688_category WHERE parent_id in (SELECT category_id FROM 1688_category WHERE parent_id=311))")
    List<ImgPojo> FindCategory();
    @Select("SELECT COUNT(1) from custom_benchmark_ready WHERE catid1=#{categoryId} AND valid=1")
    int FindAllImgUrlCount(@Param("categoryId") int categoryId);
    @Select("INSERT INTO `Size_chart_X` (`imgurl`) VALUES (#{sizeChart});")
    void AddSizeChartList(@Param("sizeChart") String sizeChart);
    @Select("SELECT count(1) FROM size_chart_x WHERE imgurl=#{url}")
    int FindImgUrlByurl(@Param("url") String url);
    @Select("SELECT imgurl FROM size_chart_x LIMIT #{page},4")
    List<String> FindAllTranslationImg(@Param("page") int page);
    @Select("SELECT count(1) FROM size_chart_x")
    int FindAllTranslationImgCount();
    @Select("DELETE FROM size_chart_x WHERE imgurl=#{url}")
    void delImgurlByList(@Param("url") String url);
}
