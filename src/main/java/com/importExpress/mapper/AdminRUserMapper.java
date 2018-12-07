package com.importExpress.mapper;

import com.importExpress.pojo.AdminRUser;
import com.importExpress.pojo.AdminRUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRUserMapper {
    int countByExample(AdminRUserExample example);

    int deleteByExample(AdminRUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AdminRUser record);

    int insertSelective(AdminRUser record);

    List<AdminRUser> selectByExample(AdminRUserExample example);

    AdminRUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AdminRUser record, @Param("example") AdminRUserExample example);

    int updateByExample(@Param("record") AdminRUser record, @Param("example") AdminRUserExample example);

    int updateByPrimaryKeySelective(AdminRUser record);

    int updateByPrimaryKey(AdminRUser record);
    
    
    /**
     * @param adminID
     * @return
     */
    List<AdminRUser> selectByUserID(@Param("list")List<String> list,@Param("admid")int admid);
    
    
    
}