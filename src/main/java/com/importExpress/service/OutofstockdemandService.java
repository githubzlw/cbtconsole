package com.importExpress.service;

import com.importExpress.pojo.Outofstockdemandtable;
import com.importExpress.pojo.OutofstockdemandtableExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutofstockdemandService {
    int countByExample(OutofstockdemandtableExample example);

    int deleteByExample(OutofstockdemandtableExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Outofstockdemandtable record);

    int insertSelective(Outofstockdemandtable record);

    List<Outofstockdemandtable> selectByExample(OutofstockdemandtableExample example);

    Outofstockdemandtable selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Outofstockdemandtable record, @Param("example") OutofstockdemandtableExample example);

    int updateByExample(@Param("record") Outofstockdemandtable record, @Param("example") OutofstockdemandtableExample example);

    int updateByPrimaryKeySelective(Outofstockdemandtable record);

    int updateByPrimaryKey(Outofstockdemandtable record);

    List<Outofstockdemandtable> getOutofstockdemandList(int startNum, int limitNum, String startTime, String endTime, String email, int flag, String itimId);
}
