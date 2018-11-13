package com.cbt.redNet.dao;

import com.cbt.redNet.pojo.redNet;
import com.cbt.redNet.pojo.redNetStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RedNetMapper {

	List<redNetStatistics> showRedNetStatistics(@Param("redNetId") String redNetId, @Param("pushTime") String pushTime, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("page") int page);

	List<redNet> showRedNetInfo(@Param("redNetId") String redNetId, @Param("redNetName") String redNetName, @Param("site") String site, @Param("page") int page);

	List<redNet> showAllRedNet();

	int addRedNet(redNet bean);

	redNet showRedNetById(int id);

	int addRedNetStatistics(redNetStatistics bean);

	int CountRedNetStatistics(@Param("redNetId") String redNetId, @Param("pushTime") String pushTime, @Param("startTime") String startTime, @Param("endTime") String endTime);

	int CountRedNetInfo(@Param("redNetId") String redNetId, @Param("redNetName") String redNetName, @Param("site") String site);

	int selectRedNetByName(String redNetName);

	int selectRedNetStatistics(String shareId);

	int updateRedNet(redNet bean);

	redNetStatistics showRedNetStatisticsById(int id);

	int updateRedNetStatistics(redNetStatistics bean);

}
