package com.cbt.track.service;

import com.cbt.bean.TabTrackInfo;

import java.util.Map;

public interface TabTrackInfoService {

//    List<TabTrackInfo> queryTrackNo(String currDate);

    TabTrackInfo queryByTrackNo(String trackNo);

    Map<String,Object> getWarningRecordList(Integer page, Integer rows, String startDate, String endDate, int warning, Integer userid);

    Map<String,Object> getRecordListByTrackState(Integer page, Integer rows, String startDate, String endDate, int trackState, Integer userid);

//    List<TabTrackInfo> queryOrderNo();

    Map<String,Object> getRecordListByOrderOrTrackNo(String orderOrTrackNo, Integer userid);

	boolean updatestate(TabTrackInfo tabTrackInfo);

	TabTrackInfo queryStateByTrackNo(String trackNo);

	Map<String, Integer> queryWaringNum(String startDate, String endDate);

//    List<String> getAllTrackNo();
}
