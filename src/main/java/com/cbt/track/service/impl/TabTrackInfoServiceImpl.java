package com.cbt.track.service.impl;

import com.cbt.bean.TabTrackDetails;
import com.cbt.bean.TabTrackInfo;
import com.cbt.track.dao.TabTrackDetailsMapping;
import com.cbt.track.dao.TabTrackInfoMapping;
import com.cbt.track.service.TabTrackInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TabTrackInfoServiceImpl implements TabTrackInfoService {

    @Autowired
    private TabTrackInfoMapping tabTrackInfoMapping;

    @Autowired
    private TabTrackDetailsMapping tabTrackDetailsMapping;


    @Override
    public TabTrackInfo queryByTrackNo(String trackNo) {
        TabTrackInfo tabTrackInfo = tabTrackInfoMapping.queryByTrackNo(trackNo);
        if (tabTrackInfo != null){
            List<TabTrackDetails> tabTrackDetailsList = tabTrackDetailsMapping.queryByTrackNo(tabTrackInfo.getTrackNo());
            if (tabTrackDetailsList != null && tabTrackDetailsList.size() > 0){
                tabTrackInfo.setTabTrackDetailsList(tabTrackDetailsList);
            }
        }
        return tabTrackInfo;
    }

    @Override
    public Map<String, Object> getWarningRecordList(Integer page, Integer rows, String startDate, String endDate, int warning, Integer userid) {
    	List<TabTrackInfo> list = tabTrackInfoMapping.getWarningRecordList(page==null?null:(page-1)*rows, rows, startDate, endDate, warning, userid);
        Integer totalCount = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, warning, userid);
        queryTrackInfo(list);//查询最新一条物流
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("recordList", list);
        map.put("totalCount", totalCount);
        return map;
    }

    @Override
    public Map<String, Object> getRecordListByTrackState(Integer page, Integer rows, String startDate, String endDate, int trackState, Integer userid) {
    	List<TabTrackInfo> list = tabTrackInfoMapping.getRecordListByTrackState(page==null?null:(page-1)*rows, rows, startDate, endDate, trackState, userid);
        Integer totalCount = tabTrackInfoMapping.getRecordCountByTrackState(startDate, endDate, trackState, userid);
        queryTrackInfo(list);//查询最新一条物流
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("recordList", list);
        map.put("totalCount", totalCount);
        return map;

    }

    //查询最新一条物流
    public void queryTrackInfo(List<TabTrackInfo> list) {
        if (list != null && list.size() > 0){
            for (TabTrackInfo bean : list) {
                String trackNo = bean.getTrackNo();
                String info = tabTrackDetailsMapping.queryMaxTimeTrackInfo(trackNo);
                if (StringUtils.isNotBlank(info)){
                    bean.setInfo(info);
                }
            }
        }
    }


    @Override
    public Map<String, Object> getRecordListByOrderOrTrackNo(String orderOrTrackNo, Integer userid, String startDate, String endDate) {
    	List<TabTrackInfo> list = tabTrackInfoMapping.getRecordListByOrderOrTrackNo("%" + orderOrTrackNo + "%", "%" + orderOrTrackNo + "%", userid, startDate, endDate);
        //通过转单运单查询
    	List<TabTrackInfo> list2 = tabTrackInfoMapping.getForwardListByTrackNo("%" + orderOrTrackNo + "%", userid, startDate, endDate);
    	if(null != list2 && list2.size() > 0){
    		list.addAll(list2);
    	}
    	
    	Map<String,Object> map = new HashMap<String, Object>();
        if (list != null && list.size() > 0) {
        	//去除重复
        	Set<String> onlySet = new HashSet<String>();
        	List<TabTrackInfo> delList = new ArrayList<TabTrackInfo>();
        	for (TabTrackInfo bean : list) {
        		String tem = bean.getOrderNo() + bean.getTrackNo() + bean.getTrackCompany();
				if (!onlySet.contains(tem)) {
					onlySet.add(tem);
				} else {
					delList.add(bean);
				}
			}
        	if (null != delList && delList.size() > 0) {
				list.removeAll(delList);
			}
			queryTrackInfo(list);//查询最新一条物流
            map.put("recordList", list);
            map.put("totalCount", list.size());
        }
        return map;
    }

    @Override
    public Map<String, Object> getRecordListByUserid(String orderUserid, Integer userid, String startDate, String endDate) {
        List<TabTrackInfo> list = tabTrackInfoMapping.getRecordListByUserid(orderUserid, userid, startDate, endDate);

        Map<String,Object> map = new HashMap<String, Object>();
        if (list != null && list.size() > 0) {
            queryTrackInfo(list);//查询最新一条物流
            map.put("recordList", list);
            map.put("totalCount", list.size());
        }
        return map;
    }

    @Override
	public boolean updatestate(TabTrackInfo tabTrackInfo) {
		try {
			tabTrackInfoMapping.updatestate(tabTrackInfo);
			return true;
		} catch (Exception e) {
			System.out.println("错误 TabTrackInfoServiceImpl updatestate");
		}
		return false;
	}

	@Override
	public TabTrackInfo queryStateByTrackNo(String trackNo) {
		return tabTrackInfoMapping.queryStateByTrackNo(trackNo);
	}

	@Override
	public Map<String, Integer> queryWaringNum(String startDate, String endDate) {
		Map<String, Integer> result = new HashMap<String, Integer>();
        Integer waring0 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 0, null);
        Integer waring1 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 1, null);
        Integer waring2 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 2, null);
//        Integer waring3 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 3);
        Integer waring4 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 4, null);
        Integer waring5 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 5, null);
        Integer waring6 = tabTrackInfoMapping.getWarningRecordCount(startDate, endDate, 6, null);
        result.put("waring0", waring0);
        result.put("waring1", waring1);
        result.put("waring2", waring2);
//        result.put("waring3", waring3);
        result.put("waring4", waring4);
        result.put("waring5", waring5);
        result.put("waring6", waring6);
		return result;
	}

	

}
