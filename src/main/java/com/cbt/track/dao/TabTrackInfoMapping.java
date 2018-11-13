package com.cbt.track.dao;

import com.cbt.bean.TabTrackInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TabTrackInfoMapping {

    TabTrackInfo queryByTrackNo(@Param("trackNo") String trackNo);

    List<TabTrackInfo> getWarningRecordList(@Param("startBars") Integer startBars, @Param("rows") Integer rows, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("warning") int warning);

    Integer getWarningRecordCount(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("warning") int warning);

    List<TabTrackInfo> getRecordListByTrackState(@Param("startBars") Integer startBars, @Param("rows") Integer rows, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("trackState") int trackState);

    Integer getRecordCountByTrackState(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("trackState") int trackState);

    List<TabTrackInfo> getRecordListByOrderOrTrackNo(@Param("orderNo") String orderNo, @Param("trackNo") String trackNo);

	void updatestate(TabTrackInfo tabTrackInfo);

	TabTrackInfo queryStateByTrackNo(@Param("trackNo") String trackNo);

	List<TabTrackInfo> getForwardListByTrackNo(@Param("trackNo") String trackNo);

}
