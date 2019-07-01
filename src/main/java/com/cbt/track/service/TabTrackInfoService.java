package com.cbt.track.service;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.bean.TabTrackInfo;

import java.util.Map;

public interface TabTrackInfoService {

//    List<TabTrackInfo> queryTrackNo(String currDate);

    TabTrackInfo queryByTrackNo(String trackNo);

    Map<String,Object> getWarningRecordList(Integer page, Integer rows, String startDate, String endDate, int warning, Integer userid);

    Map<String,Object> getRecordListByTrackState(Integer page, Integer rows, String startDate, String endDate, int trackState, Integer userid);

//    List<TabTrackInfo> queryOrderNo();

    Map<String,Object> getRecordListByOrderOrTrackNo(String orderOrTrackNo, Integer userid, String startDate, String endDate);

    Map<String,Object> getRecordListByUserid(String orderUserid, Integer userid, String startDate, String endDate);

    boolean updatestate(TabTrackInfo tabTrackInfo);

	TabTrackInfo queryStateByTrackNo(String trackNo);

	Map<String, Integer> queryWaringNum(Map<String, Object> param);

    void getTrackInfoList(EasyUiJsonResult json, Map<String, Object> param);

//    List<String> getAllTrackNo();
}
