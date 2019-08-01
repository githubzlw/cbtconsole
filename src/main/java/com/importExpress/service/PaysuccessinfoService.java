package com.importExpress.service;

import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.Paysuccessinfo;
import com.importExpress.pojo.PaysuccessinfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaysuccessinfoService {
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

    List<Integer> queryUserListByAdminId(Integer adminId);

    EasyUiJsonResult queryPaySuccessInfoList(String pageStr, String limitNumStr, String sttime, String edtime, String userIdStr, String orderNo, Integer userId);
}
