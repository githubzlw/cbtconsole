package com.cbt.track.service.impl;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.bean.TabTrackDetails;
import com.cbt.bean.TabTrackForward;
import com.cbt.bean.TabTrackInfo;
import com.cbt.track.dao.TabTrackDetailsMapping;
import com.cbt.track.dao.TabTrackInfoMapping;
import com.cbt.track.service.TabTrackInfoService;
import com.cbt.util.DateFormatUtil;
import org.apache.commons.collections.CollectionUtils;
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
            if (CollectionUtils.isNotEmpty(tabTrackDetailsList)){
                tabTrackInfo.setTabTrackDetailsList(tabTrackDetailsList);
            }
            List<TabTrackForward> forwardList = tabTrackInfoMapping.queryTrackForward(tabTrackInfo.getTrackNo());
            if (CollectionUtils.isNotEmpty(forwardList)){
                tabTrackInfo.setForwardList(forwardList);
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
    public void getTrackInfoList(EasyUiJsonResult json, Map<String, Object> param) {
        List<TabTrackInfo> list = tabTrackInfoMapping.getTrackInfoList(param);
        json.setRows(list);
        json.setSuccess(true);
        if ((int)param.get("export") == 0) {
            // 查询 按钮

            // dp订单标记 用于页面中跳转到订单页
            if (list.size() > 0) {
                List<String> dpOrderList = tabTrackInfoMapping.getIsDropshipOrder(list);
                for (TabTrackInfo bean : list) {
                    if (dpOrderList.contains(bean.getOrderNo())) {
                        bean.setTarOrderNo(bean.getOrderNo().split("_")[0]);
                    } else {
                        bean.setTarOrderNo(bean.getOrderNo());
                    }
                }
            }

            Integer totalCount = tabTrackInfoMapping.getTrackInfoListCount(param);
            json.setTotal(totalCount);
            return;
        }

        // 导出 按钮
        if (CollectionUtils.isEmpty(list)) {
            json.setMessage("该条件下查询不到数据");
            json.setSuccess(false);
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("用户ID,用户邮箱,主单号,负责人,运单号,物流公司,转单号,转单物流公司,运单状态,订单支付时间,出货时间,运单完成时间,\r\n");
            for (TabTrackInfo bean : list) {
                sb.append(bean.getId()).append(",")
                        .append(bean.getEmail()).append(",")
                        .append(bean.getOrderNo()).append(",")
                        .append(bean.getAdmName()).append(",")
                        .append(bean.getTrackNo()).append(",")
                        .append(bean.getTrackCompany()).append(",")
                        .append(bean.getForwardNo()==null?"":"'" + bean.getForwardNo()).append(",")
                        .append(bean.getForwardCompany()==null?"":bean.getForwardCompany().replaceAll(",", " ")).append(",")
                        .append(formatterTrackState(bean.getTrackState())).append(",") //运单状态
                        .append(DateFormatUtil.getWithSeconds(bean.getOrderPaytime())).append(",")
                        .append(DateFormatUtil.getWithSeconds(bean.getSenttime())).append(",")
                        .append(DateFormatUtil.getWithSeconds(bean.getDeliveredTime())).append(",\r\n");
            }
            String message = sb.toString().replaceAll(",null,", ",,");
            message = message.replaceAll(",null,", ",,");
            json.setMessage(message);
        }
    }

    public static String formatterTrackState(Integer trackState){
        if (trackState == null){
            return "";
        }
        switch(trackState) {
            case 3:
                return "已签收";
            case 4:
                return "退回";
            case 5:
                return "异常";
            case 6:
                return "内部异常";
            case 7:
                return "手动标记正常";
            default:
                return "已发货";
        }
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
	public Map<String, Integer> queryWaringNum(Map<String, Object> param) {
		Map<String, Integer> result = new HashMap<String, Integer>();
        param.put("warning", 0);
        result.put("waring0", tabTrackInfoMapping.getTrackInfoListCount(param));
        param.put("warning", 1);
        result.put("waring1", tabTrackInfoMapping.getTrackInfoListCount(param));
        param.put("warning", 2);
        result.put("waring2", tabTrackInfoMapping.getTrackInfoListCount(param));
        param.put("warning", 4);
        result.put("waring4", tabTrackInfoMapping.getTrackInfoListCount(param));
        param.put("warning", 5);
        result.put("waring5", tabTrackInfoMapping.getTrackInfoListCount(param));
        param.put("warning", 6);
        result.put("waring6", tabTrackInfoMapping.getTrackInfoListCount(param));
		return result;
	}

	

}
