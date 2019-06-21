package com.importExpress.mapper;

import com.importExpress.pojo.Paysuccessinfo;
import com.importExpress.pojo.PaysuccessinfoExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PaysuccessinfoMapper {
    int countByExample(PaysuccessinfoExample example);

    int deleteByExample(PaysuccessinfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Paysuccessinfo record);

    int insertSelective(Paysuccessinfo record);

    List<Paysuccessinfo> selectByExample(PaysuccessinfoExample example);

    Paysuccessinfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Paysuccessinfo record, @Param("example") PaysuccessinfoExample example);

    int updateByExample(@Param("record") Paysuccessinfo record, @Param("example") PaysuccessinfoExample example);

    int updateByPrimaryKeySelective(Paysuccessinfo record);

    int updateByPrimaryKey(Paysuccessinfo record);

    @Select("select d.userid from admuser c,admin_r_user d where c.id = d.adminid and c.id = #{adminId}")
    List<Integer> queryUserListByAdminId(@Param("adminId") Integer adminId);
}