package com.cbt.track.dao;

import com.cbt.bean.TabTrackInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TabTrackInfoMapping {

    TabTrackInfo queryByTrackNo(@Param("trackNo") String trackNo);

    List<TabTrackInfo> getWarningRecordList(@Param("startBars") Integer startBars, @Param("rows") Integer rows,
                                            @Param("startDate") String startDate, @Param("endDate") String endDate,
                                            @Param("warning") int warning, @Param("userid") Integer userid);

    Integer getWarningRecordCount(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                  @Param("warning") int warning, @Param("userid") Integer userid);

    List<TabTrackInfo> getRecordListByTrackState(@Param("startBars") Integer startBars, @Param("rows") Integer rows,
                                                 @Param("startDate") String startDate, @Param("endDate") String endDate,
                                                 @Param("trackState") int trackState, @Param("userid") Integer userid);

    Integer getRecordCountByTrackState(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                       @Param("trackState") int trackState, @Param("userid") Integer userid);

    List<TabTrackInfo> getRecordListByOrderOrTrackNo(@Param("orderNo") String orderNo, @Param("trackNo") String trackNo, @Param("userid") Integer userid,
                                                     @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<TabTrackInfo> getRecordListByUserid(@Param("orderUserid") String orderUserid, @Param("userid") Integer userid,
                                             @Param("startDate") String startDate, @Param("endDate") String endDate);

	void updatestate(TabTrackInfo tabTrackInfo);

	TabTrackInfo queryStateByTrackNo(@Param("trackNo") String trackNo);

	List<TabTrackInfo> getForwardListByTrackNo(@Param("trackNo") String trackNo, @Param("userid") Integer userid,
                                               @Param("startDate") String startDate, @Param("endDate") String endDate);

}
