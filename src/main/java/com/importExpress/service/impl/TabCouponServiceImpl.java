package com.importExpress.service.impl;

import com.cbt.bean.UserBean;
import com.cbt.jcys.util.HttpUtil;
import com.cbt.util.DateFormatUtil;
import com.cbt.warehouse.service.WarehouseServiceImpl;
import com.ctc.wstx.util.DataUtil;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.mapper.TabCouponMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.TabCouponService;
import com.importExpress.utli.SearchFileUtils;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TabCouponServiceImpl implements TabCouponService {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TabCouponServiceImpl.class);

    @Autowired
	private TabCouponMapper tabCouponMapper;

    @Autowired
    private SendMailFactory sendMailFactory;
	
	@Override
	public Map<String, Object> queryTabCouponList(Integer page, Integer rows, String typeCode, Integer valid, Integer timeTo) {
		List<TabCouponNew> list = tabCouponMapper.queryTabCouponList((page - 1) * rows, rows, typeCode, valid, timeTo);
		Long totalCount = tabCouponMapper.queryTabCouponListCount(typeCode, valid, timeTo);
		
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
        //发送mq {"count":"100","describe":"-3$","from":"1545580800","id":"001-20181228-100","leftCount":"100","to":"1545926400","type":"1","valid":"1","value":"10-3"}
        SendMQ sendMQ = null;
        try {
            sendMQ = new SendMQ();
            sendMQ.sendCouponMsg(json);
        } catch (Exception e) {
            throw new RuntimeException("mq发送失败");
        } finally {
            if (sendMQ != null){
                sendMQ.closeConn();
            }
        }
    }

	@Override
	public Boolean checkCouponCode(String couponCode) {
		return tabCouponMapper.checkCouponCode(couponCode) > 0;
	}

    @Override
    public TabCouponNew queryTabCouponOne(String couponCode) {
        TabCouponNew result = tabCouponMapper.queryTabCouponOne(couponCode);
        //关联的用户
        String userids = tabCouponMapper.queryTabCouponUser(couponCode);
        if (StringUtils.isNotBlank(userids)){
            result.setUserids(userids);
        }
        //分享链接
        if (result != null && result.getShareFlag() == 1) {
            String value = result.getValue();
            String[] valueArr = value.split("-");
            if (valueArr != null && valueArr.length > 0) {
                String shareUrl = SearchFileUtils.importexpressPath
                        + "/coupon/shareCoupon?couponcode=" + couponCode
                        + "&shareid=" + valueArr[valueArr.length-1];
                result.setShareUrl(shareUrl);
            }
        }
        return result;
    }

    @Override
    public Map<String, String> addCouponUser(String couponCode, List<String> useridList) {
        Map<String, String> result = new HashMap<String, String>();
        //查询优惠卷信息
        TabCouponNew tabCouponNew = queryTabCouponOne(couponCode);
        if (tabCouponNew == null){
            result.put("state", "false");
            result.put("message", "未找到该优惠卷信息");
            return result;
        }
        //查询被关联用户信息
        List<UserBean> userList = tabCouponMapper.queryLocalUser(useridList);
        if (userList == null || userList.size() == 0){
            result.put("state", "false");
            result.put("message", "未找到被关联的用户信息");
            return result;
        }
        //查询本地已关联记录 查询用户id
        List<String> localUserList = tabCouponMapper.queryCouponUsersCount(couponCode);
        //去除本地已关联记录
        if (localUserList != null && localUserList.size() > 0){
            Iterator<UserBean> iterator = userList.iterator();
            while (iterator.hasNext()){
                UserBean bean = iterator.next();
                if (localUserList.contains(String.valueOf(bean.getId()))){
                    iterator.remove();
                }
            }
        }
        if (userList == null || userList.size() == 0){
            result.put("state", "false");
            result.put("message", "未找到被关联的用户信息,或者关联的用户已进行了关联");
            return result;
        }

        if (tabCouponNew.getCount() < userList.size()
                || tabCouponNew.getCount() < localUserList.size() + userList.size()){
            result.put("state", "false");
            result.put("message", "卷数量不足(已关联用户数+待关联用户数>卷总数量)");
            return result;
        }
        //保存记录到本地
        tabCouponMapper.insertCouponUsers(tabCouponNew, userList);
        //发送mq 到线上 {"id":"001-20181228-100","to":"1546185600","type":"2","userid":"1000"}
        SendMQ sendMQ = null;
        try {
            sendMQ = new SendMQ();
            for (UserBean userBean : userList) {
                CouponUserRedisBean bean = new CouponUserRedisBean(couponCode, new Long(tabCouponNew.getTo().getTime()).toString(), String.valueOf(userBean.getId()));
                String json = JSONObject.fromObject(bean).toString();
                sendMQ.sendCouponMsg(json);
            }
            //异步发送邮件
            Runnable task=new TabCouponServiceImpl.sendCouponMailTask(userList, tabCouponNew);
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException("mq发送失败");
        } finally {
            if (sendMQ != null){
                sendMQ.closeConn();
            }
        }
        result.put("state", "true");
        result.put("message", "关联用户id成功, 正在发送邮件通知客户。");
        return result;
    }

    class sendCouponMailTask implements Runnable{
        private List<UserBean> list;
        private TabCouponNew tabCouponNew;
        public sendCouponMailTask(List<UserBean> list, TabCouponNew tabCouponNew) {
            this.list=list;
            this.tabCouponNew = tabCouponNew;
        }

        @Override
        public void run() {
            if (list == null || list.size() == 0){
                return;
            }
            for (UserBean userBean : list) {
                Map<String, Object> model = new HashMap<>();
                try {
                    //邮件信息
                    model.put("firstName", userBean.getName());
                    model.put("description", tabCouponNew.getDescribe());
                    model.put("codeId", tabCouponNew.getId());
                    model.put("validityPeriod", DateFormatUtil.getWithDaySkim(tabCouponNew.getFrom()) + "-" + DateFormatUtil.getWithDaySkim(tabCouponNew.getTo()));
                    model.put("codeValue", "$" + tabCouponNew.getValue().split("-")[1]);

                    model.put("email", userBean.getEmail());
                    model.put("title", "A coupon was sent to your individual center!");

                    sendMailFactory.sendMail(model.get("email").toString(), null, model.get("title").toString(), model, TemplateType.COUPON);
                } catch (Exception e) {
                    LOG.error("sendCouponMailTask 异步发送邮件 error, 邮件信息:" + JSONObject.fromObject(model).toString(), e);
                }
            }
        }
    }

    @Override
    public Map<String, String> delCouponUser(String couponCode, String userid) {
        Map<String, String> result = new HashMap<String, String>();
        String res = HttpUtil.postCoupon("/coupon/deleteCoupon", "pass", "huisj123lo", "type", "0", "couponcode", couponCode, "userid", userid);
        if (StringUtils.isNotBlank(res) && Long.valueOf(res) >= 0){
            //更新本地关联的记录
            tabCouponMapper.updateCouponUsers(couponCode, userid);
            result.put("state", "true");
            result.put("message", "删除用户" + userid + "关联的折扣卷" + couponCode + "成功");
        } else {
            result.put("state", "false");
            result.put("message", "调用线上删除用户" + userid + "关联的折扣卷" + couponCode + "失败");
        }
        return result;
    }

    @Override
    public Map<String, String> delCoupon(String couponCode) {
        Map<String, String> result = new HashMap<String, String>();
        //发送mq删除线上  {"key":"coupon:001-20180929-1000","type":"3"}
//        Map<String, String> jsonMap = new HashMap<String, String>();
//        jsonMap.put("key", "coupon:" + couponCode);
//        jsonMap.put("type", "3");
//        String json = JSONObject.fromObject(jsonMap).toString();
        String res = HttpUtil.postCoupon("/coupon/deleteCoupon", "pass", "huisj123lo", "type", "1", "couponcode", couponCode);
        if (StringUtils.isNotBlank(res) && Long.valueOf(res) >= 0){
            //本地数据删除（打标记）
            tabCouponMapper.updateCouponValid(couponCode, 0);
            result.put("state", "true");
            result.put("message", "删除折扣卷成功");
        } else {
            result.put("state", "false");
            result.put("message", "调用线上接口删除折扣卷失败");
        }
        return result;
    }

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String querycouponcode() {
        String couponCode = null;
	    //查询当天最后一张卷码
        String maxCouponCode = tabCouponMapper.queryCouponCodeByCreatetime(DATEFORMAT.format(new Date()).toString() + " 00:00:00");
        if (StringUtils.isNotBlank(maxCouponCode)){
            try {
                Integer num = Integer.valueOf(maxCouponCode.split("-")[0]);
                couponCode = new DecimalFormat("0000").format(num + 1);
            } catch (Exception e){

            }
        }
        if (couponCode == null){
            couponCode = "0001";
        }

        String curTime = String.valueOf(System.currentTimeMillis());
        couponCode += "-" + curTime.substring(curTime.length() - 6) + "-0";
        
        return couponCode;
    }
    
    public static void main(String[] args) {
        System.out.println(new Date());
    }
    
    
}
