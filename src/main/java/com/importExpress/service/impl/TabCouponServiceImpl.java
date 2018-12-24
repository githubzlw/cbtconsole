package com.importExpress.service.impl;

import com.importExpress.mapper.TabCouponMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.TabCouponService;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class TabCouponServiceImpl implements TabCouponService {

	@Autowired
	private TabCouponMapper tabCouponMapper;
	
	@Override
	public Map<String, Object> queryTabCouponList(Integer page, Integer rows, String typeCode) {
		List<TabCouponNew> list = tabCouponMapper.queryTabCouponList((page - 1) * rows, rows, typeCode);
		Long totalCount = tabCouponMapper.queryTabCouponListCount(typeCode);
		
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("recordList", list);
        map.put("totalCount", totalCount);
        return map;
	}

	@Override
	public List<TabCouponType> queryTabCouponTypeCodeList() {
		return tabCouponMapper.queryTabCouponTypeCodeList();
	}

	@Override
	public List<TabCouponRules> queryTabCouponRulesList() {
		return tabCouponMapper.queryTabCouponRulesList();
	}

	@Override
	public void addCoupon(CouponRedisBean couponRedis, TabCouponNew tabCouponNew) throws Exception {
		String json = JSONObject.fromObject(couponRedis).toString();
		tabCouponNew.setMqlog(json);
		//保存记录到本地
		tabCouponMapper.addCoupon(tabCouponNew);
		//发送mq
		SendMQ sendMQ = new SendMQ();
		sendMQ.sendCouponMsg(json);
		sendMQ.closeConn();
	}

	@Override
	public Boolean checkCouponCode(String couponCode) {
		return tabCouponMapper.checkCouponCode(couponCode) > 0;
	}

    @Override
    public TabCouponNew queryTabCouponOne(String couponCode) {
        TabCouponNew result = tabCouponMapper.queryTabCouponOne(couponCode);
        String userids = tabCouponMapper.queryTabCouponUser(couponCode);
        if (StringUtils.isNotBlank(userids)){
            result.setUserids(userids);
        }
        return result;
    }

    @Override
    public Map<String, String> addCouponUser(String couponCode, List<String> useridList) {
        Map<String, String> result = new HashMap<String, String>();
        TabCouponNew tabCouponNew = queryTabCouponOne(couponCode);
        if (tabCouponNew == null){
            result.put("state", "false");
            result.put("message", "未找到该优惠卷信息");
            return result;
        }
        //保存记录到本地
        tabCouponMapper.insertCouponUsers(tabCouponNew, useridList);
        //发送mq 到线上
        SendMQ sendMQ = null;
        try {
            sendMQ = new SendMQ();
            for (String userid : useridList) {
                CouponUserRedisBean bean = new CouponUserRedisBean(couponCode, new Long(tabCouponNew.getTo().getTime()).toString(), userid);
                String json = JSONObject.fromObject(bean).toString();
                sendMQ.sendCouponMsg(json);
            }
        } catch (Exception e) {
            throw new RuntimeException("mq发送失败");
        } finally {
            if (sendMQ != null){
                sendMQ.closeConn();
            }
        }
        result.put("state", "true");
        result.put("message", "关联用户id成功");
        return result;
    }
}
