package com.cbt.feedback.dao;

import com.cbt.feedback.bean.ErrorFeedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ErrorFeedbackMapper {

	List<ErrorFeedback> showErrorFb(@Param("type") int type, @Param("belongto") String belongto, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("deFlag") int deFlag, @Param("start") int start);

	int updateErrorFlag(@Param("id") int id, @Param("remark") String remark);

	int count(@Param("type") int type, @Param("belongto") String belongto, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("deFlag") int deFlag);

	int InsertErrorInfo(@Param("content") String content, @Param("createtime") String createtime, @Param("belongTo") String belongTo,
                        @Param("type") int type, @Param("logLocation") String logLocation, @Param("positionFlag") int positionFlag);

	int updateSomeErrorFlag(List<Map<String, String>> bgList);

}
