package com.importExpress.service.impl;

import com.cbt.bean.UserBean;
import com.cbt.jcys.util.HttpUtil;
import com.cbt.util.DateFormatUtil;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.mapper.TabCouponMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.TabCouponService;
import com.importExpress.utli.SearchFileUtils;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.SwitchDomainNameUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
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
	public Map<String, Object> queryTabCouponList(Integer page, Integer rows, String typeCode, Integer valid, Integer timeTo, Integer couponSite) {
		List<TabCouponNew> list = tabCouponMapper.queryTabCouponList((page - 1) * rows, rows, typeCode, valid, timeTo, couponSite);
		Long totalCount = tabCouponMapper.queryTabCouponListCount(typeCode, valid, timeTo, couponSite);
		
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
	public Map<String, String> addCoupon(CouponRedisBean couponRedis, TabCouponNew tabCouponNew, List<String> useridList) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
		String json = JSONObject.fromObject(couponRedis).toString();
		tabCouponNew.setMqlog(json);
		//?????????????????????
		tabCouponMapper.addCoupon(tabCouponNew);
        //??????mq {"count":"100","describe":"-3$","from":"1545580800","id":"001-20181228-100","leftCount":"100","to":"1545926400","type":"1","valid":"1","value":"10-3"}

        try {

            SendMQ.sendCouponMsg(json, tabCouponNew.getSite());
        } catch (Exception e) {
            throw new RuntimeException("mq????????????");
        }
        result.put("state", "true");
        result.put("message", "?????????????????????!");
        //????????????id
        if (useridList.size() > 0){
            List<UserBean> userList = tabCouponMapper.queryLocalUser(useridList);
            if (userList == null || userList.size() == 0){
                result.put("message", "?????????????????????, ????????????????????????????????????, ???????????????!");
                return result;
            }
            if(couponRedis.getIsShopCar() == 0){
                insertCouponUsersAndSendMail(tabCouponNew, userList);
                result.put("message", "?????????????????????, ???????????????id??????, ?????????????????????????????????");
            }else{
                tabCouponNew.setIsShopFlag(1);
                insertCouponUsersAndSendMail(tabCouponNew, userList);
                result.put("message", "?????????????????????, ???????????????id??????!");
            }
        }
        return result;
    }

	@Override
	public Boolean checkCouponCode(String couponCode) {
		return tabCouponMapper.checkCouponCode(couponCode) > 0;
	}


    /**
     * ??????????????????
     * */
    public static String getShareUrl(String couponCode, String shareid, Integer websiteType) {
        String shareUrl = "/coupon/shareCoupon?couponcode=" + couponCode + "&shareid=" + shareid;
        if (websiteType == 1) {
            return SearchFileUtils.importexpressPath + shareUrl;
        } else if (websiteType == 2) {
            return SearchFileUtils.kidsPath + shareUrl;
        } else if (websiteType == 3) {
            return SearchFileUtils.petsPath + shareUrl;
        } else {
            return SearchFileUtils.importexpressPath + shareUrl
                    + "<br /><br />" + SearchFileUtils.kidsPath + shareUrl
                    + "<br /><br />" + SearchFileUtils.petsPath + shareUrl;
        }
    }


    @Override
    public TabCouponNew queryTabCouponOne(String couponCode) {
        TabCouponNew result = tabCouponMapper.queryTabCouponOne(couponCode);
        //???????????????
        String userids = tabCouponMapper.queryTabCouponUser(couponCode);
        if (StringUtils.isNotBlank(userids)){
            result.setUserids(userids);
        }
        //????????????
        if (result != null && result.getShareFlag() == 1) {
            String value = result.getValue();
            String[] valueArr = value.split("-");
            if (valueArr != null && valueArr.length > 0) {
                result.setShareUrl(getShareUrl(couponCode, valueArr[valueArr.length-1], result.getSite()));
            }
        }
        return result;
    }

    @Override
    public Map<String, String> addCouponUser(String couponCode, List<String> useridList, Integer websiteType) {
        Map<String, String> result = new HashMap<String, String>();
        //?????????????????????
        TabCouponNew tabCouponNew = queryTabCouponOne(couponCode);
        if (tabCouponNew == null){
            result.put("state", "false");
            result.put("message", "???????????????????????????");
            return result;
        }
        //???????????????????????????
        List<UserBean> userList = tabCouponMapper.queryLocalUser(useridList);
        if (userList == null || userList.size() == 0){
            result.put("state", "false");
            result.put("message", "?????????????????????????????????");
            return result;
        }
        //??????????????????????????? ????????????id
        List<String> localUserList = tabCouponMapper.queryCouponUsersCount(couponCode);
        //???????????????????????????
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
            result.put("message", "?????????????????????????????????,???????????????????????????????????????");
            return result;
        }

        if (tabCouponNew.getCount() < userList.size()
                || tabCouponNew.getCount() < localUserList.size() + userList.size()){
            result.put("state", "false");
            result.put("message", "???????????????(??????????????????+??????????????????>????????????)");
            return result;
        }
        tabCouponNew.setWebsiteType(websiteType);
        insertCouponUsersAndSendMail(tabCouponNew, userList);
        result.put("state", "true");
        result.put("message", "????????????id??????, ?????????????????????????????????");
        return result;
    }

    public void insertCouponUsersAndSendMail(TabCouponNew tabCouponNew, List<UserBean> userList) {
        //?????????????????????
        tabCouponMapper.insertCouponUsers(tabCouponNew, userList);
        //??????mq ????????? {"id":"001-20181228-100","to":"1546185600","type":"2","userid":"1000"}

        try {

            for (UserBean userBean : userList) {
                CouponUserRedisBean bean = new CouponUserRedisBean(tabCouponNew.getId(), new Long(tabCouponNew.getTo().getTime()).toString(), String.valueOf(userBean.getId()));
                String json = JSONObject.fromObject(bean).toString();
                SendMQ.sendCouponMsg(json, tabCouponNew.getWebsiteType());
            }
            if(tabCouponNew.getIsShopFlag() == 0){
                //??????????????????
                Runnable task=new sendCouponMailTask(userList, tabCouponNew);
                new Thread(task).start();
            }
        } catch (Exception e) {
            throw new RuntimeException("mq????????????");
        }
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
                    //????????????
                    model.put("firstName", userBean.getName());
                    model.put("description", tabCouponNew.getDescribe());
                    model.put("codeId", tabCouponNew.getId());
                    model.put("validityPeriod", DateFormatUtil.getWithDaySkim(tabCouponNew.getFrom()) + "-" + DateFormatUtil.getWithDaySkim(tabCouponNew.getTo()));
                    model.put("codeValue", "$" + tabCouponNew.getValue().split("-")[1]);

                    model.put("email", userBean.getEmail());
                    model.put("title", "A coupon was sent to your individual center!");
                    model.put("websiteType", tabCouponNew.getWebsiteType());

                    sendMailFactory.sendMail(model.get("email").toString(), null, model.get("title").toString(), model, TemplateType.COUPON);
                } catch (Exception e) {
                    LOG.error("sendCouponMailTask ?????????????????? error, ????????????:" + JSONObject.fromObject(model).toString(), e);
                }
            }
        }
    }

    @Override
    public Map<String, String> delCouponUser(String couponCode, String userid) {
        Map<String, String> result = new HashMap<String, String>();
        String res = HttpUtil.postCoupon(SearchFileUtils.importexpressPath, "/coupon/deleteCoupon", "pass", "huisj123lo", "type", "0", "couponcode", couponCode, "userid", userid);
        res += HttpUtil.postCoupon(SearchFileUtils.kidsPath, "/coupon/deleteCoupon", "pass", "huisj123lo", "type", "0", "couponcode", couponCode, "userid", userid);
        if (StringUtils.isNotBlank(res) && Long.valueOf(res) >= 0){
            //???????????????????????????
            tabCouponMapper.updateCouponUsers(couponCode, userid);
            result.put("state", "true");
            result.put("message", "????????????" + userid + "??????????????????" + couponCode + "??????");
        } else {
            result.put("state", "false");
            result.put("message", "????????????????????????" + userid + "??????????????????" + couponCode + "??????");
        }
        return result;
    }

    @Override
    public Map<String, String> delCoupon(String couponCode) {
        TabCouponNew tabCouponNew = tabCouponMapper.queryTabCouponOne(couponCode);
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put("key", "coupon:" + couponCode);
        jsonMap.put("type", "3");
        String json = JSONObject.fromObject(jsonMap).toString();

        try {

            //??????mq????????????  {"key":"coupon:001-20180929-1000","type":"3"}
            SendMQ.sendCouponMsg(json, tabCouponNew.getSite());
            //?????????????????????????????????
            tabCouponMapper.updateCouponValid(couponCode, 0);
            result.put("state", "true");
            result.put("message", "?????????????????????");
        } catch (Exception e) {
            result.put("state", "true");
            result.put("message", "?????????????????????");
        }
//        String res = HttpUtil.postCoupon("/coupon/deleteCoupon", "pass", "huisj123lo", "type", "1", "couponcode", couponCode);
//        if (StringUtils.isNotBlank(res) && Long.valueOf(res) >= 0){
//            //?????????????????????????????????
//            tabCouponMapper.updateCouponValid(couponCode, 0);
//            result.put("state", "true");
//            result.put("message", "?????????????????????");
//        } else {
//            result.put("state", "false");
//            result.put("message", "???????????????????????????????????????");
//        }
        return result;
    }

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String querycouponcode() {
        String couponCode = null;
	    //??????????????????????????????
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

        //??????????????????
        String curTime = "";
        long count = 1;
        while (count > 0){
            curTime = String.valueOf(System.currentTimeMillis()).substring(6, 12);
            count = tabCouponMapper.queryCouponCodeCount("%" + curTime + "%");
        }
        couponCode += "-" + curTime + "-1";
        return couponCode;
    }
    
    public static void main(String[] args) {
        System.out.println(String.valueOf(System.currentTimeMillis()).substring(6, 6));
        System.out.println(1);
    }

    @Override
    public int SendGuestbook(int id, String replyContent, String date, String name, String qustion, String pname, String email, int parseInt, String purl, String sale_email, String picPath, Integer websiteType) {
        new Thread(){
            public void run() {
//                StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
//                sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Sir/Madam,</div><br><div style='font-size: 13px;'>");
//                sb.append("<div >" + replyContent + " </div>");
//                sb.append("<br><div style='margin-bottom: 10px;'><span style='font-weight: bold'>Your Question:</span>["+id+"]"+qustion+" </div>");
//                sb.append("<div style='margin-bottom: 10px;'><span style='font-weight: bold'>Item:</span><a href='"+purl+"'> "+pname+"</a></div>");
//                if(StringUtil.isNotBlank(picPath)){
//                    sb.append("<br><img src='"+picPath+"'></img><br>");
//                }
                //sb.append("<br><div>We hope you enjoy the shopping experience on Import-Express.com!</div><br>");
//		        sb.append("<div style='style='font-weight: bold'>Best regards, </div><div style='font-weight: bold'><a href='http://www.import-express.com'>www.Import-Express.com</a></div></div>");

                String pnameHtml = "<a href='"+purl+"'> "+pname+"</a>";
                if(StringUtils.isNotBlank(picPath)){
                    pnameHtml += "<br><img src='"+picPath+"'></img><br>";
                }

                Map<String,Object> modelM = new HashedMap();
                modelM.put("replyContent", replyContent);
                modelM.put("qustion", "[" + id + "]" + qustion);
                modelM.put("pname", pnameHtml);
                modelM.put("websiteType", websiteType);
                String title = "[" + id + "]" + SwitchDomainNameUtil.checkNullAndReplace("Inquiry Reply From ImportExpress", websiteType);
                sendMailFactory.sendMail(email, null, title, modelM, TemplateType.GUESTBOOK_REPLY);
            }
        }.start();
        return 0;
    }


    @Override
    public List<String> getCouponCode(String url, String param) {
        return sendPost(url, param);
    }

    /**
     * ????????? URL ??????POST???????????????
     *
     * @param url   ??????????????? URL
     * @param param ???????????????????????????????????? name1=value1&name2=value2 ????????????
     * @return ????????????????????????????????????
     */
    public List<String> sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        ArrayList<String> result = new ArrayList<String>();
        try {
            URL realUrl = new URL(url);
            // ?????????URL???????????????
            URLConnection conn = realUrl.openConnection();
            // ???????????????????????????
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ??????POST??????????????????????????????
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ??????URLConnection????????????????????????
            out = new PrintWriter(conn.getOutputStream());
            // ??????????????????
            out.print(param);
            // flush??????????????????
            out.flush();
            // ??????BufferedReader??????????????????URL?????????
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            System.out.println("?????? POST ?????????????????????" + e);
            e.printStackTrace();
        }
        //??????finally?????????????????????????????????
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int addTabCouponNew(TabCouponNew tabCouponNew) {
        return tabCouponMapper.addTabCouponNew(tabCouponNew);
    }

    @Override
    public int addTabCouponNewLog(TabCouponLog tabCouponLog) {
        return tabCouponMapper.addTabCouponNewLog(tabCouponLog);
    }


}
