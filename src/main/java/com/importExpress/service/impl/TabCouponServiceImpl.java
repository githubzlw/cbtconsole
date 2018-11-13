package com.importExpress.service.impl;

import com.importExpress.mapper.TabCouponMapper;
import com.importExpress.pojo.CouponRedisBean;
import com.importExpress.pojo.TabCouponNew;
import com.importExpress.pojo.TabCouponRules;
import com.importExpress.pojo.TabCouponType;
import com.importExpress.service.TabCouponService;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    


}
