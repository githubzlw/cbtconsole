package com.cbt.warehouse.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbt.common.cache.RedisUtil;
import com.importExpress.pojo.UserFreeNotFree;
import com.importExpress.service.UserFreeNotFreeService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.FreightFee.service.FreightFeeSerive;
import com.cbt.bean.BlackList;
import com.cbt.bean.CustomGoodsBean;
import com.cbt.bean.Forwarder;
import com.cbt.bean.LocationManagementInfo;
import com.cbt.bean.LocationTracking;
import com.cbt.bean.Logisticsinfo;
import com.cbt.bean.OrderAddress;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderInfoPrint;
import com.cbt.bean.Orderinfo;
import com.cbt.bean.RechargeRecord;
import com.cbt.bean.StorageInspectionLogPojo;
import com.cbt.bean.StorageLocationBean;
import com.cbt.bean.ZoneBean;
import com.cbt.bean.badGoods;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.orderinfo.dao.OrderinfoMapper;
import com.cbt.pojo.BuyerCommentPojo;
import com.cbt.pojo.CustomsRegulationsPojo;
import com.cbt.pojo.EntypeBen;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.RedManProductBean;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.processes.servlet.Currency;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.StrUtils;
import com.cbt.util.Util;
import com.cbt.warehouse.dao.WarehouseMapper;
import com.cbt.warehouse.pojo.AdmuserPojo;
import com.cbt.warehouse.pojo.AllProblemPojo;
import com.cbt.warehouse.pojo.ClassDiscount;
import com.cbt.warehouse.pojo.DisplayBuyInfo;
import com.cbt.warehouse.pojo.GoodsInventory;
import com.cbt.warehouse.pojo.JcexPrintInfo;
import com.cbt.warehouse.pojo.OrderFeePojo;
import com.cbt.warehouse.pojo.OrderInfoCountPojo;
import com.cbt.warehouse.pojo.OrderInfoPojo;
import com.cbt.warehouse.pojo.OrderProductSurcePojo;
import com.cbt.warehouse.pojo.OrderReplenishmentPojo;
import com.cbt.warehouse.pojo.RefundSamplePojo;
import com.cbt.warehouse.pojo.SampleOrderBean;
import com.cbt.warehouse.pojo.SbxxPojo;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.warehouse.pojo.Tb1688Account;
import com.cbt.warehouse.pojo.Tb1688Pojo;
import com.cbt.warehouse.pojo.returndisplay;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.Utility;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.PurchaseBean;
import com.cbt.website.bean.PurchaseDetailsBean;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;
import com.cbt.website.bean.SampleGoodsBean;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.bean.UserInfo;
import com.cbt.website.bean.UserOrderDetails;
import com.cbt.website.bean.outIdBean;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.mapper.ProductSkuWeightMapper;
import com.importExpress.pojo.ProductSkuWeight;
import com.importExpress.utli.JsonUtils;
import com.importExpress.utli.MultiSiteUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SearchFileUtils;
import com.importExpress.utli.SendMQ;

@Service
public class WarehouseServiceImpl implements IWarehouseService {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(WarehouseServiceImpl.class);
    @Autowired
    private ProductSkuWeightMapper skuWeightMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private FreightFeeSerive freightFeeSerive;
    @Autowired
    private CustomGoodsService customGoodsService;
    @Autowired
    private IPurchaseMapper pruchaseMapper;
    @Autowired
	private OrderinfoMapper orderinfoMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserFreeNotFreeService userFreeNotFreeService;

    @Override
    public outIdBean findOutId(Integer uid) {

        return warehouseMapper.findOutId(uid);
    }



    @Override
    public SearchResultInfo getWeight() {
        return warehouseMapper.getWeight();
    }

    @Override
    public int saveWeight(Map<String, String> map) {
    	String weight = map.get("weight");
    	double dWeight = Double.parseDouble(weight) * 1000;
    	weight = String.valueOf(dWeight).split("\\.")[0];
    	
    	String vweight = StrUtils.object2DotNumStr(map.get("volumeWeight"));
    	double dvweight = Double.parseDouble(vweight) * 1000;
    	vweight = String.valueOf(dvweight).split("\\.")[0];
    	
    	int admid = Integer.parseInt(StrUtils.object2NumStr(map.get("adminid")));
    	ProductSkuWeight wprap = ProductSkuWeight.builder()
    			.adminid(admid)
    			.pid(map.get("pid"))
    			.skuid(map.get("skuid"))
    			.weight(Integer.parseInt(weight) )
    			.v_weight(Integer.parseInt(vweight)).build();
		int updateWeight = skuWeightMapper.updateWeight(wprap );
		if(updateWeight == 0) {
			updateWeight = skuWeightMapper.insertWeight(wprap );
		}
//		int odid = Integer.parseInt(map.get("odid"));
		//??????????????????
//		saveWeightFlag(map.get("pid"),admid,odid,map.get("skuid"));
        return warehouseMapper.saveWeight(map);
    }

    //result 0-????????????;2-pid????????????;1-????????????????????????;3-?????????????????????;4-???????????????????????????;
    @Override
    public int saveWeightFlag(String pid, int adminId, int odId,String skuid) {
        // ??????????????????????????????
        SearchResultInfo weightAndSyn = warehouseMapper.getGoodsWeight(pid, odId);
        if (null == weightAndSyn){
            return 3;
        }
        if (weightAndSyn.getSyn() == 1){
            return 4;
        }
        weightAndSyn.setSkuID(skuid);
        //?????????????????????
        Runnable task=new SetGoodsWeightByWeigherTask(pid, weightAndSyn, adminId);
        new Thread(task).start();
        return 1;
    }

    class SetGoodsWeightByWeigherTask implements Runnable{
        public String pid;
        public String newWeight;
        public int adminId;
        private SearchResultInfo weightAndSyn;
        public SetGoodsWeightByWeigherTask(String pid, SearchResultInfo weightAndSyn, int adminId) {
            this.pid=pid;
            //this.newWeight=newWeight;
            this.weightAndSyn = weightAndSyn;
            this.adminId=adminId;
        }

        @Override
        public void run() {
            try {
                //jxw 05-28??????????????????????????????
                customGoodsService.setGoodsWeightByWeigherInfo(pid, weightAndSyn, adminId);
                warehouseMapper.updateGoodsWeightFlag(pid, Integer.valueOf(weightAndSyn.getOdid()));
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("SetGoodsWeightByWeigherTask ???????????????????????? error", e);
            }
        }
    }

    @Override
    public int updateQuestPicPath(String id, String path) {
        return warehouseMapper.updateQuestPicPath(id,path);
    }

    @Override
    public List<SampleGoodsBean> getSampleGoods(String [] pids) {
        return warehouseMapper.getSampleGoods(pids);
    }

    @Override
    public List<SampleGoodsBean> getLiSameGoods(String[] pids, String [] shopIds, String [] pIds) {
        return warehouseMapper.getLiSameGoods(pids,shopIds,pIds);
    }

    @Override
    public List<SampleGoodsBean> getRecommdedSameGoods(Map<String, String> map) {
        return warehouseMapper.getRecommdedSameGoods(map);
    }

    @Override
    public String getAllOrderPid(Map<String, String> map) {
        return warehouseMapper.getAllOrderPid(map);
    }

    @Override
    public int refundGoods(Map<String, String> map) {
        int row=0;
        boolean flag=true;
        //????????????????????????????????????????????????
        Inventory i=warehouseMapper.checkGoodsInvengory(map);
        if(i != null){
            //????????????????????????
            OrderDetailsBean od=warehouseMapper.getOrderDetails(map);
            if(od != null && od.getYourorder()>0){
                int yourorder=od.getYourorder();
                if(i.getFlag() == 1 && Integer.valueOf(i.getNew_remaining())<yourorder){
                    flag=false;
                }else if(i.getFlag() == 0 && Integer.valueOf(i.getRemaining())<yourorder){
                    flag=false;
                }
                if(flag){
                    //??????????????????
                    map.put("yourorder",String.valueOf(yourorder));
                    map.put("id",String.valueOf(i.getId()));
                    String goods_p_price=i.getGoods_p_price();
                    String price=goods_p_price;
                    if(goods_p_price.indexOf(",")>-1){
                        goods_p_price=goods_p_price.split(",")[0];
                    }
                    double cancelAmount=yourorder*Double.parseDouble(goods_p_price);
                    map.put("cancelAmount",String.valueOf(cancelAmount));
                    map.put("flag",String.valueOf(i.getFlag()));
                    row=warehouseMapper.updateInventory(map);
                   warehouseMapper.updateIsRefund(map);
                }
            }
        }
        return row;
    }

    @Override
    public int saveQualityData(Map<String, String> map) {
        int row=warehouseMapper.saveQualityData(map);
        if(row>0){
            row=warehouseMapper.queryQeId(map);
            map.put("qid",String.valueOf(row));
            warehouseMapper.saveQualityDataLog(map);
        }
        return row;
    }

    @Override
    public Map<String, String> getExchange(Map<String, String> map) {
        return warehouseMapper.getExchange(map);
    }

    @Override
    public OrderInfoCountPojo getPresentationsData(Map<String, String> map) {
        return warehouseMapper.getPresentationsData(map);
    }

    @Override
    public OrderInfoCountPojo getOpenCountData(Map<String, String> map) {
        return warehouseMapper.getOpenCountData(map);
    }

    @Override
    public OrderInfoCountPojo getAddCarData(Map<String, String> map) {
        return warehouseMapper.getAddCarData(map);
    }

    @Override
    public OrderInfoCountPojo getGoodsBuyData(Map<String, String> map) {
        return warehouseMapper.getGoodsBuyData(map);
    }

    @Override
    public OrderInfoCountPojo getGoodsSalesAmountData(Map<String, String> map) {
        return warehouseMapper.getGoodsSalesAmountData(map);
    }

    @Override
    public OrderInfoCountPojo getCancelData(Map<String, String> map) {
        return warehouseMapper.getCancelData(map);
    }

    @Override
    public int updateQualityData(Map<String, String> map) {
        int row=warehouseMapper.updateQualityData(map);
        if(row>0){
            row=warehouseMapper.queryQeId(map);
            map.put("qid",String.valueOf(row));
            warehouseMapper.saveQualityDataLog(map);
        }
        return row;
    }

    @Override
    public String getQualityEvaluation(Map<String, String> map) {
        return warehouseMapper.getQualityEvaluation(map);
    }

    @Override
    public int delInPic(Map<String, String> map) {
        return warehouseMapper.delInPic(map);
    }

    @Override
    public int insertInspectionPicture(String pid, String picPath) {
        return warehouseMapper.insertInspectionPicture(pid,picPath);
    }

    @Override
    public int disabled(Map<String, String> map) {
        return warehouseMapper.disabled(map);
    }

    @Override
    public int insertEvaluation(Map<String, String> map) {
        int row=0;
        try{

            String id=warehouseMapper.queryQevId(map);
            if(StringUtil.isBlank(id)){
                row=warehouseMapper.insertEvaluation(map);
                SendMQ.sendMsg(new RunSqlModel("insert into goods_evaluation (goods_pid,evaluation,createtime) values('"+map.get("goods_pid")+"','"+map.get("evaluation")+"',now())"));
                id=warehouseMapper.queryQevId(map);
            }else{
                row=warehouseMapper.updateEvaluation(map);
                SendMQ.sendMsg(new RunSqlModel("update goods_evaluation set evaluation='"+map.get("evaluation")+"' where goods_pid='"+map.get("goods_pid")+"'"));
            }
            map.put("ge_id",id);
            warehouseMapper.insertEvaluationLog(map);

        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public int insInsp(Map<String, String> map) {
	    int checked=0;
    	try{
		    checked=warehouseMapper.checnInspIsExit(map);
		    if(checked<=0){
			    checked=warehouseMapper.insertInspPath(map);
		    }else{
			    checked=0;
		    }
		    if(checked>0){

			    SendMQ.sendMsg(new RunSqlModel(" insert into inspection_picture(pid,orderid,pic_path,createtime,odid) values('"+map.get("goodsPid")+"','"+map.get("orderid")+"','"+map.get("picPath")+"',now(),'"+map.get("odid")+"')"));

		    }
	    }catch (Exception e){
    		e.printStackTrace();
	    }
        return checked;
    }

    @Override
    public OrderAddress getAddressByOrderID(String orderNo) {

        return warehouseMapper.getAddressByOrderID(orderNo);
    }

    @Override
    public List<PurchaseDetailsBean> getPurchaseDetails(String orderNo) {

        return warehouseMapper.getPurchaseDetails(orderNo);
    }

    @Override
    public List<PurchaseBean> getOutByID(String userid, String ordernolist) {

        String sql = ordernolist;
        String[] ck = null;
        if(ordernolist==null||ordernolist.equals("")){
            ordernolist="<br/>";
            ck = ordernolist.split("<br/>");
        } else {
            ordernolist = ordernolist.replace(",","<br/>");
            ck = ordernolist.split("<br/>");
        }

        List<PurchaseBean> pbList = new ArrayList<PurchaseBean>();
        return warehouseMapper.getOutByID(userid,sql);
    }

    @Override
    public void callResult(Map<String, String> param) {

        warehouseMapper.callResult(param);
    }

    @Override
    public void callUpdateIdrelationtable(Map<String, String> param) {

        warehouseMapper.callUpdateIdrelationtable(param);
    }

    @Override
    public void insertStorage_location(String a, String b, String c, String d,
                                       String e) {

        warehouseMapper.insertStorage_location(a, b, c, d, e);

    }

    @Override
    public List<StorageLocationBean> getAllStorageLocationByPage(int startNum,int endNum) {

        return warehouseMapper.getAllStorageLocationByPage(startNum,endNum);
    }

    @Override
    public List<StorageLocationBean> getOrderinfoPage(Map<String, Object> map) {
        List<StorageLocationBean> list=warehouseMapper.getOrderinfoPage(map);
        for (int i = 0; i < list.size(); i++) {
            StorageLocationBean s = list.get(i);
            if (s.getChecked() == 0 && "1".equals(s.getGoodstatus())) {
                s.setGoodstatus("????????????,?????????");
            } else if ("2".equals(s.getGoodstatus())) {
                s.setGoodstatus("????????????????????????????????????");
            } else if ("3".equals(s.getGoodstatus())) {
                s.setGoodstatus("?????????????????????????????????");
            } else if ("4".equals(s.getGoodstatus())) {
                s.setGoodstatus("?????????????????????????????????");
            } else if ("5".equals(s.getGoodstatus())) {
                s.setGoodstatus("????????????????????????????????????");
            } else if ("6".equals(s.getGoodstatus())) {
            	s.setGoodstatus("???????????????????????????????????????");
            } else {
                s.setGoodstatus("???????????????????????????");
            }
            List<String> picList=new ArrayList<String>();
            String pics=s.getPicturepath();
            if(StringUtil.isNotBlank(pics) && pics.contains(",")){
                //??????????????????
                String pic []= pics.split(",");
                for(int j=0;j<pic.length;j++){
                    String path=pic[j];
                    if(path.contains("import")){
//                        path=path.replace("https://img.import-express.com/importcsvimg/inspectionImg/","http://192.168.1.34:8085/");
//						picList.add(path);
                    }else{
                        path=getNewPicPath(path);
                    }
                    picList.add(path);
                }
            }else if(StringUtil.isNotBlank(pics)){
                if(pics.contains("import")){
//                    pics=pics.replace("https://img.import-express.com/importcsvimg/inspectionImg/","http://192.168.1.34:8085/");
                    picList.add(pics);
                }else{
                    picList.add(getNewPicPath(pics));
                }
            }
            s.setPicList(picList);
//			s.setPicturepath("https://img.import-express.com/importcsvimg/inspectionImg/"+s.getPicturepath()+"");
        }
        return list;
    }

    public String getNewPicPath(String pics){
        if(pics.contains("2018-05") || pics.contains("2018-04") || pics.contains("2018-03") || pics.contains("2018-02") || pics.contains("2018-01") || pics.contains("2018-05") ||
                pics.contains("2017-03") || pics.contains("2018-04") || pics.contains("2018-05") || pics.contains("2018-06")
                || pics.contains("2018-07") || pics.contains("2018-08") ||  pics.contains("2018-09") ||  pics.contains("2018-10") ||  pics.contains("2018-11") ||  pics.contains("2018-12")){
            pics= Util.PIC_URL+pics+"";
//            pics="http://27.115.38.42:8084/"+pics+"";
        }else{
//            pics="http://192.168.1.34:8085/"+pics+"";
            pics=Util.PIC_URL+pics+"";

        }
        return pics;
    }

    @Override
    public int getCountOrderinfo(Map<String, Object> map) {

        return warehouseMapper.getCountOrderinfo(map);
    }

    @Override
    /**
     * ???????????? ?????????????????????????????????
     * @param map
     * @return   ?????????????????????????????????????????????
     * whj 2017-05-11
     */
    public List<String> getsourceValidation(Map<String, Object> map) {

        return warehouseMapper.getsourceValidation(map);
    }

    @Override
    /**
     * ??????????????????
     */
    public List<String> getBuyerName(String admuserid) {

        return warehouseMapper.getBuyerName(admuserid);
    }

    @Override
    /**
     * ??????????????????
     */
    public String getBuyerNames(String admuserid) {

        return warehouseMapper.getBuyerNames(admuserid);
    }

    @Override
    /**
     * ???????????? ?????????????????????????????????
     * @param map
     * @return   ???????????????????????????????????????????????????
     * whj 2017-05-11
     */
    public List<String> getsourceValidationForBuy(Map<String, Object> map) {

        return warehouseMapper.getsourceValidationForBuy(map);
    }

    @Override
    public List<StorageLocationBean> getOrderInfoInspection(Map<String, Object> map,String ip) {
        List<StorageLocationBean> newlist1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> newlist2 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list2 = new ArrayList<StorageLocationBean>();
        list1 = warehouseMapper.getOrderInfoInspection(map);
        if(list1.size()>0){
            List<String>  orderList = warehouseMapper.checkOrders(list1,0);
            for(StorageLocationBean bean:list1){
                if(!orderList.contains(bean.getOrder_no())){
                    newlist1.add(bean);
                }
            }
        }
        //??????dropship??????
        list2 = warehouseMapper.getDropShipOrderInfoInspection(map);
        for(StorageLocationBean bean:list2){
            StringBuffer stringBuffer = new StringBuffer(bean.getUser_id());
            bean.setUser_id(stringBuffer.insert(1, "D").toString());
        }
        if(list2.size()>0){
            List<String>  dropOrderList = warehouseMapper.checkOrders(list2,1);
            for(StorageLocationBean bean:list2){
                if(!dropOrderList.contains(bean.getOrder_no())){
                    newlist2.add(bean);
                }
            }
        }
        newlist2.addAll(newlist1);
        for (StorageLocationBean bean : newlist2) {
            String urls = "";
            if (bean.getLocalImgPath() != null
                    && bean.getLocalImgPath().indexOf("&") > -1) {
                for (int j = 0; j < bean.getLocalImgPath().split("&").length; j++) {
                    urls += "<img onerror='imgError(this,\"" + bean.getLocalImgPath().split("&")[j] + "\")' width='50px' onclick='BigImg(this,\""
                            + bean.getLocalImgPath().split("&")[j] + "\",\""
                            + bean.getOrder_no() + "\");' id='"
                            + bean.getLocalImgPath().split("&")[j]
                            + "' height='50px' alt='' src='" + SearchFileUtils.IMAGEHOSTURL
                            + bean.getLocalImgPath().split("&")[j] + "'/>";
                }
            } else if (bean.getLocalImgPath() != null
                    && bean.getLocalImgPath().length() > 0) {
                urls = "<img onerror='imgError(this,\"" + bean.getLocalImgPath() + "\")' width='50px' height='50px' onclick='BigImg(this,\""
                        + bean.getLocalImgPath()
                        + "\",\""
                        + bean.getOrder_no()
                        + "\");' id='"
                        + bean.getLocalImgPath()
                        + "' src='"
                        + SearchFileUtils.IMAGEHOSTURL + bean.getLocalImgPath() + "'/>";
            }
            bean.setLocalImgPath(urls);
            //
            int sampleOrderCount = orderinfoMapper.querySampleOrderInfoByOrderId(bean.getOrder_no());
            if(sampleOrderCount > 0){
                bean.setSampleOrder(1);
            }
        }
        return newlist2;
    }

    @Override
    public List<StorageLocationBean> getOrderInfoInspectionall(Map<String, Object> map,String ip) {
        List<StorageLocationBean> newlist1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> newlist2 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list2 = new ArrayList<StorageLocationBean>();
        list1 = warehouseMapper.getOrderInfoInspectionall(map);
        if(list1.size()>0){
            List<String>  orderList = warehouseMapper.checkOrders(list1,0);
            for(StorageLocationBean bean:list1){
                if(!orderList.contains(bean.getOrder_no())){
                    newlist1.add(bean);
                }
            }
        }
        //??????dropship??????
        list2 = warehouseMapper.getDropShipOrderInfoInspectionall(map);
        for(StorageLocationBean bean:list2){
            StringBuffer stringBuffer = new StringBuffer(bean.getUser_id());
            bean.setUser_id(stringBuffer.insert(1, "D").toString());
        }
        if(list2.size()>0){
            List<String>  dropOrderList = warehouseMapper.checkOrders(list2,1);
            for(StorageLocationBean bean:list2){
                if(!dropOrderList.contains(bean.getOrder_no())){
                    newlist2.add(bean);
                }
            }
        }
        newlist2.addAll(newlist1);
        for (StorageLocationBean bean : newlist2) {
            String urls = "";
            if (bean.getLocalImgPath() != null
                    && bean.getLocalImgPath().indexOf("&") > -1) {
                for (int j = 0; j < bean.getLocalImgPath().split("&").length; j++) {
                    urls += "<img onerror='imgError(this,\"" + bean.getLocalImgPath().split("&")[j] + "\")' width='50px' onclick='BigImg(this,\""
                            + bean.getLocalImgPath().split("&")[j] + "\",\""
                            + bean.getOrder_no() + "\");' id='"
                            + bean.getLocalImgPath().split("&")[j]
                            + "' height='50px' alt='' src='" + SearchFileUtils.IMAGEHOSTURL
                            + bean.getLocalImgPath().split("&")[j] + "'/>";
                }
            } else if (bean.getLocalImgPath() != null
                    && bean.getLocalImgPath().length() > 0) {
                urls = "<img onerror='imgError(this,\"" + bean.getLocalImgPath() + "\")' width='50px' height='50px' onclick='BigImg(this,\""
                        + bean.getLocalImgPath()
                        + "\",\""
                        + bean.getOrder_no()
                        + "\");' id='"
                        + bean.getLocalImgPath()
                        + "' src='"
                        + SearchFileUtils.IMAGEHOSTURL + bean.getLocalImgPath() + "'/>";
            }
            bean.setLocalImgPath(urls);
        }
        return newlist2;
    }

    @Override
    public int getCountOrderInfoInspection(Map<String, Object> map) {

        int count = warehouseMapper.getCountOrderInfoInspection(map);
        count +=warehouseMapper.getCountDropShipOrderInfoInspection(map);
        return count;
    }

    @Override
    public int insertOrderfeeFromOrderInfo(Map<String, Object> map,int dropshipFlag) {
        map.put("dropshipFlag",dropshipFlag);
        return warehouseMapper.insertOrderfeeFromOrderInfo(map);
    }

    @Override
    public String getOrderidAddress(Map<String, Object> map) {

        return warehouseMapper.getOrderidAddress(map);
    }

    @Override
    public List<OrderInfoPrint> getOrderidPrinInfo(Map<String, Object> map) {

        List<OrderInfoPrint> list1 = warehouseMapper.getOrderidPrinInfo(map);
        for(OrderInfoPrint bean:list1){
            bean.setIsDropshipFlag(0);
        }
        List<OrderInfoPrint> list2 = warehouseMapper.getDropshipOrderidPrinInfo(map);
        for(OrderInfoPrint ob:list2){
            ob.setIsDropshipFlag(1);
        }
        List<OrderInfoPrint> list  = new ArrayList<OrderInfoPrint>();
        list.addAll(list1);
        list.addAll(list2);
        return list;

    }

    @Override
    public int delteFromOrderFeeByOrderid(Map<String, Object> map) {

        return warehouseMapper.delteFromOrderFeeByOrderid(map);
    }

    @Override
    public List<Forwarder> getForwarder(Map<String, Object> map) {

        return warehouseMapper.getForwarder(map);
    }

    @Override
    public int getCountForwarder(Map<String, Object> map) {

        return warehouseMapper.getCountForwarder(map);
    }

    @Override
    public List<Logisticsinfo> getlogisticsidAndState(Map<String, Object> map) {

        return warehouseMapper.getlogisticsidAndState(map);
    }

    @Override
    public String getOrderCreateTime(Map<String, Object> map,int dropshipFlag) {
        map.put("dropshipFlag",dropshipFlag);
        return warehouseMapper.getOrderCreateTime(map);
    }

	@Override
	public int getUndeliveredOrder(String orderId) {
    	int remark=0;
		remark=warehouseMapper.getUndeliveredOrder(orderId);
		return remark;
	}

	@Override
    public Logisticsinfo getlogisticsinfo(Map<String, Object> map) {

        return warehouseMapper.getlogisticsinfo(map);
    }
    /**
     * ???????????????????????????
     */
    @Override
    public int refundShipnoEntry(Map<String, Object> map) {
        int row=warehouseMapper.refundShipnoEntry(map);
        if(row>0){
            //??????????????????????????????????????????
            warehouseMapper.updateRefundFlag(map);
        }
        return row;
    }
    @Override
    public List<String> getRefundGoodsPid(String goodsid, String orderid) {

        return warehouseMapper.getRefundGoodsPid(goodsid,orderid);
    }
    @Override
    public String getState(Map<String, Object> map) {

        return warehouseMapper.getState(map);
    }
    @Override
    public int getStoragCount(Map<String, Object> map) {

        return warehouseMapper.getStoragCount(map);
    }

    @Override
    public int updateInventoryCount(Map<String, Object> map) {
        int row=warehouseMapper.updateInventoryCount(map);
        return row;
    }

    @Override
    public List<OrderProductSurcePojo> getlogisticsAndOrderProductSurc(
            Map<String, Object> map) {

        return warehouseMapper.getlogisticsAndOrderProductSurc(map);
    }

    @Override
    public List<OrderInfoPrint> getIdrelationtable(Map<String, Object> map) {

        return warehouseMapper.getIdrelationtable(map);
    }

    @Override
    public int getCountIdrelationtable(Map<String, Object> map) {

        return warehouseMapper.getCountIdrelationtable(map);
    }

    @Override
    public int updateFromOrderFeeByOrderid(Map<String, Object> map) {

        return warehouseMapper.updateFromOrderFeeByOrderid(map);
    }

    @Override
    public int updateExperssNo(Map<String, Object> map) {

        return warehouseMapper.updateExperssNo(map);
    }

    @Override
    public List<OrderFeePojo> getOrderFee(Map<String, Object> map) {

        return warehouseMapper.getOrderFee(map);
    }

    @Override
    public OrderInfoPojo getOutOrderInfo(Map<String, Object> map) {

        return warehouseMapper.getOutOrderInfo(map);
    }


    @Override
    public OrderDetailsBean queryVideo(Map<String, String> map) {
        return warehouseMapper.queryVideo(map);
    }

    @Override
    public int updateCustomVideoUrl(Map<String, String> map) {
        return warehouseMapper.updateCustomVideoUrl(map);
    }

    @Override
    public List<SearchResultInfo> queryPictureInfos(Map<String, String> map) {
        List<SearchResultInfo> list=warehouseMapper.queryPictureInfos(map);
        for (SearchResultInfo s : list) {
            s.setCurrency("<a title='????????????????????????' target='_blank' href='https://www.import-express.com/goodsinfo/cbtconsole-1"+s.getOrderid()+".html'><img src='"+(s.getInvoice()+s.getCurrency())+"' style='width:220px;'></img></a>");
            StringBuilder sb=new StringBuilder(3);
            List<SearchResultInfo> ps=warehouseMapper.getPicturePath(s.getOrderid());
            int index=1;
            for(int i=0;i<ps.size();i++){
                SearchResultInfo p=ps.get(i);
                String picture=p.getGoods_url();
                if(!picture.contains("http")){
                    picture=Util.PIC_URL+picture;
                }
                sb.append("<div class='div_box'><img src='"+picture+"' class='pic_01' onmouseout='closeBigImg();'" +
                        " onmouseover=BigImg(\""+picture+"\") style='width:220px;'></img>");
                sb.append("<br><div class='div_btn'><button onclick=\"uploadInPic("
                        + p.getGoods_p_url() + ",\'" + p.getGoods_img_url()
                        + "\',\'" + s.getOrderid()
                        + "\',\'" + picture
                        + "\',"+p.getCurrency()+")\">????????????</button>");
                sb.append("<button onclick=\"delInPic("+p.getGoods_p_url()+",\'"+p.getGoods_img_url()+"\',\'"+s.getOrderid()+"\',\'"+picture+"\',"+p.getCurrency()+")\">????????????</button>");
                if(p.getUserid()==0){
                    sb.append("<button style='color:red' onclick=\"disabledPic("+p.getCurrency()+",1,\'"+picture+"\')\">??????</button>");
                    if(StringUtil.isNotBlank(map.get("odid"))){
                        sb.append("<button style='color:red' onclick=\"insInsp("+p.getOrderid()+","+map.get("odid")+",\'"+picture+"\',\'"+map.get("oldOrderid")+"\')\">??????</button>");
                    }
                    if(index<=3){
                        sb.append("<span style='color:red'>?????????...</span>");
                        index++;
                    }
                    sb.append("</div></div>");
                }else{
                    sb.append("<button style='color:green' onclick=\"disabledPic("+p.getCurrency()+",0,\'"+picture+"\')\">??????</button>");
                    if(StringUtil.isNotBlank(map.get("odid"))){
                        sb.append("<button style='color:red' onclick=\"insInsp("+p.getOrderid()+","+map.get("odid")+",\'"+picture+"\',\'"+map.get("oldOrderid")+"\')\">??????</button>");
                    }
                    sb.append("</div></div>");
                }
            }
            s.setGcUnit(sb.toString());
            //|<button onclick=\"showEvaluation("+s.getOrderid()+")\">????????????</button>fdgdf
            s.setPosition("<button onclick=\"uploadPics("+s.getOrderid()+")\">???????????????</button>|<button onclick=\"openEvaluation("+s.getOrderid()+")\">????????????</button>|<a target='_blank' href='/cbtconsole/website/upload_video.jsp?goods_pid="+s.getOrderid()+"'>????????????</a>");
            String valid=s.getValid();
            if("0".equals(valid)){
                valid="??????";
            }else if("1".equals(valid)){
                valid="??????";
            }else if("2".equals(valid)){
                valid="?????????";
            }else{
                valid="??????";
            }
            s.setValid(valid);
        }
        return list;
    }

    @Override
    public int queryPictureInfosCount(Map<String, String> map) {

        return warehouseMapper.queryPictureInfosCount(map);
    }

    @Override
    public List<OrderFeePojo> getFpxCountryCode() {

        return warehouseMapper.getFpxCountryCode();
    }

    @Override
    public List<OrderInfoPojo> getFpxProductCode() {

        return warehouseMapper.getFpxProductCode();
    }

    @Override
    public int updateOrderFeeByOrderid(Map<String, Object> map) {

        return warehouseMapper.updateOrderFeeByOrderid(map);
    }

    @Override
    public List<OrderFeePojo> getCodemaster() {

        return warehouseMapper.getCodemaster();
    }

    @Override
    public List<OrderInfoPojo> getOutCount() {

        return warehouseMapper.getOutCount();
    }

    @Override
    public OrderInfoPojo getPaymentFy(Map<String, Object> map) {

        return warehouseMapper.getPaymentFy(map);
    }

    @Override
    public String getOrderProblem(Map<String, Object> map) {

        return warehouseMapper.getOrderProblem(map);
    }

    @Override
    public int updateOrderinfoAll(Map<String, Object> map) {

        return warehouseMapper.updateOrderinfoAll(map);
    }

    @Override
    public int updateOrderinfo(Map<String, Object> map) {

        return warehouseMapper.updateOrderinfo(map);
    }

    @Override
    public List<OrderInfoPojo> getNotMoneyOrderinfo(Map<String, Object> map) {

        return warehouseMapper.getNotMoneyOrderinfo(map);
    }

    @Override
    public List<OrderFeePojo> getOrderfeeFreight(Map<String, Object> map, HttpServletRequest request) {
        List<OrderFeePojo> list=warehouseMapper.getOrderfeeFreight(map);
        double exchange_rate = 1;
        Map<String, Double> maphl = Currency.getMaphl(request);
        for (int i = 0; i < list.size(); i++) {
            if (!"".equals(list.get(i).getCurrency())&& list.get(i).getCurrency() != null) {
                exchange_rate = maphl.get("RMB");
                exchange_rate = exchange_rate/ maphl.get(list.get(i).getCurrency());
                double eur = exchange_rate;
                double temp = Float.parseFloat(list.get(i).getActure_fee());
                list.get(i).setActure_fee_RMB((new BigDecimal(temp * eur).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()) + "");
                exchange_rate = maphl.get("USD");
                exchange_rate = exchange_rate/ maphl.get(list.get(i).getCurrency());
                eur = exchange_rate;
                temp = Float.parseFloat(list.get(i).getActure_fee());
                list.get(i).setActure_fee_USD((new BigDecimal(temp * eur).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()) + "");
            }
        }
        return list;
    }

    @Override
    public int getOrderfeeFreightCount(Map<String, Object> map) {

        return warehouseMapper.getOrderfeeFreightCount(map);
    }

    @Override
    public AdmuserPojo getAdmuserSendMailInfo(Map<String, Object> map) {

        return warehouseMapper.getAdmuserSendMailInfo(map);
    }

    @Override
    public OrderInfoCountPojo getOrderInfoCountByState(Map<String, Object> map) {

        return warehouseMapper.getOrderInfoCountByState(map);
    }

    @Override
    public List<TaoBaoOrderInfo> getBuyReturnManage(String goodsid,int page,String state,String startTime,String endTime) {
    	
    	
    	
        List<TaoBaoOrderInfo> list=warehouseMapper.getBuyReturnManage(goodsid,page,state,startTime,endTime);
        for(TaoBaoOrderInfo t:list){
            t.setItemname("<a target='_blank' href='"+t.getItemurl()+"'>"+(t.getItemname().substring(0,t.getItemname().length()/3))+"</a>");
            t.setImgurl("<img style='width:100px;height:100px;' src='"+t.getImgurl()+"'></img>");
            if("0".equals(t.getbId())){
                t.setState("?????????????????????");
            }else if(StringUtil.isNotBlank(t.getbId()) && !"0".equals(t.getbId())){
                t.setState("????????????????????????");
            }else{
                t.setState("?????????");
                t.setOperating("<button  onclick='returns(0,\""+t.getOrderid()+"\")'>????????????</button><button  onclick='returns("+t.getId()+",\""+t.getOrderid()+"\")'>????????????</button>");
            }
        }
        return list;
    }

    @Override
    public int getBuyReturnManageCount(String goodsid,String state,String startTime,String endTime) {
        return warehouseMapper.getBuyReturnManageCount(goodsid,state,startTime,endTime);
    }

    @Override
    public List<UserInfo> getUserInfoForPrice(Map<String, Object> map) {
        List<UserInfo> userInfos=warehouseMapper.getUserInfoForPrice(map);
        List<ConfirmUserInfo> list = warehouseMapper.getAllAdmuser();
        List<Integer> userCheck = new ArrayList<Integer>();
        if (CollectionUtils.isNotEmpty(userInfos)) {
            userCheck = warehouseMapper.queryUserCheckByUserid(userInfos);
        }
        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo = userInfos.get(i);
            StringBuffer admuser = new StringBuffer();
            // @author: cjc @date???2020/1/19 10:47 ??????   Description :  ?????????????????????????????????
            boolean userNotFreeState = getUserNotFreeState(userInfo.getUserid());
            String string = "??????????????????";
            int state = 0;
            if(userNotFreeState){
                // @author: cjc @date???2020/1/20 2:35 ??????   Description : ???????????????????????????
                string = "???????????????";
                state = 1;
            }
            String changeNotFreeButton = "<button id='free' onclick=\"changeNotFree(\'" + userInfo.getUserid() + "\', \'" +state + "\')\">"+string+"</button>" + userInfo.getBusinessName();
            userInfo.setBusinessName(changeNotFreeButton);
            admuser.append("<select "+("0".equals(map.get("roleType"))?"":"disabled='disabled'")+" id='admuser_" + userInfo.getUserid()
                    + "'><option value='0'>?????????</option>");
            String loginType = "";
            if (!StringUtils.isStrNull(userInfo.getPass())) {
                loginType = "????????????";
            } else if (StringUtils.isStrNull(userInfo.getPass())
                    && !StringUtils.isStrNull(userInfo.getBind_google())) {
                loginType = "google??????";
            } else {
                loginType = "facebook??????";
            }
            userInfo.setLoginStyle(loginType);
            userInfo.setUserLogin("<button onclick=\"userlogin("
                    + userInfo.getUserid() + ",\'" + userInfo.getUserName()
                    + "\',\'" + userInfo.getCurrency()
                    + "\')\">????????????????????????</button>");
            int site = userInfo.getSite();
            String siteTypeStrBySit = MultiSiteUtil.getSiteTypeStrBySit(site);
            userInfo.setWebSite(siteTypeStrBySit);
            userInfo.setUserManager("<button onclick=\"useraddress("
                    + userInfo.getUserid() + ",\'" + userInfo.getUserName()
                    + "\',\'" + userInfo.getCurrency()
                    + "\')\">??????????????????</button>");
            userInfo.setOperation("<button onclick=\"upEmail("
                    + userInfo.getUserid()
                    + ",'"
                    + userInfo.getEmail()
                    + "')\">????????????</button><button id='upphone' onclick=\"upPhone("
                    + userInfo.getUserid()
                    + ",'"
                    + (StringUtils.isStrNull(userInfo.getOtherphone()) ? ""
                    : userInfo.getOtherphone())
                    + "')\">???????????????</button><button onclick=\"changeavailable("
                    + userInfo.getUserid()
                    + ",'"
                    + userInfo.getAvailable()
                    + "')\">????????????</button><br /><button id='but2' onclick='fnsetDropshipUser()'>?????????Drop ship??????</button>"
                    + "<button onclick=\"showRemark(\'" + userInfo.getUserid() + "\')\">??????</button>"
                    + "<button onclick=\"showUserType(\'" + userInfo.getUserid() + "\', \'" + (userCheck.contains(userInfo.getUserid())?1:0) + "\')\">??????</button>"
            + "<button onclick=\"openRecommendEmail("+userInfo.getUserid()+","+site+")\">??????????????????</button>"
            );
            for (int j = 0; j < list.size(); j++) {
                ConfirmUserInfo c = list.get(j);
                if (c.getConfirmusername().equals(userInfo.getAdminname())) {
                    admuser.append("<option value='" + c.getId()
                            + "' selected='selected'>" + c.getConfirmusername()
                            + "</option>");
                } else {
                    admuser.append("<option value='" + c.getId() + "'>"
                            + c.getConfirmusername() + "</option>");
                }
            }
            admuser.append("</select><input id=\"confirm\" type=\"button\" "+("0".equals(map.get("roleType"))?"":"disabled='disabled'")+" value=\"??????\"   onclick=\"addUser("
                    + userInfo.getUserid()
                    + ",'"
                    + userInfo.getEmail()
                    + "','"
                    + userInfo.getUserName()
                    + "','"
                    + userInfo.getAdminname()
                    + "')\">");
            userInfo.setAdmuser(admuser.toString());
            // ???????????????jxw????????????????????????????????????????????????
            /*double goods_price=0.00;
            if(!StringUtils.isStrNull(userInfo.getCar_info()) && userInfo.getCar_info().length()>10){
                List<GoodsCarActiveBean> list_active  =  (List<GoodsCarActiveBean>) net.sf.json.JSONArray.toCollection(JSONArray.fromObject(userInfo.getCar_info()),GoodsCarActiveBean.class);
                System.out.println("list_active=="+list_active.toString());
                for (GoodsCarActiveBean g : list_active) {
                    goods_price+=g.getNumber()*Double.valueOf(g.getPrice());
                }
            }*/
            userInfo.setGoodsPriceUrl("<a href=javascript:userlogin("
                    + userInfo.getUserid()
                    + ",\'"
                    + userInfo.getUserName()
                    + "\',\'"
                    + userInfo.getCurrency()
                    + "\')>"
                    + String.format("%.2f", userInfo.getGoodsPrice()) + "</a>");
            String gname = warehouseMapper.getGname(userInfo.getGid());
            userInfo.setGrade(gname);
            if (StringUtils.isStrNull(userInfo.getBusinessName())) {
                userInfo.setBusinessName("???");
            }
        }
        return userInfos;
    }
    @Override
    public List<ShopManagerPojo> getShopManagerList(Map<String, Object> map) {
        List<ShopManagerPojo> list=warehouseMapper.getShopManagerList(map);
        for (ShopManagerPojo s : list) {
            s.setShop_id("<a href='/cbtconsole/website/shop_manager_details.jsp?id=" + s.getId() + "&status="+s.getRemark()+"' target='_blank'>"+ s.getShop_id() + "</a>");
            if ("0".equals(s.getRemark())) {
                s.setRemark("????????????");
            } else if ("1".equals(s.getRemark())) {
                s.setRemark("????????????");
            } else if ("2".equals(s.getRemark())) {
                s.setRemark("????????????");
            } else if ("3".equals(s.getRemark())) {
                s.setRemark("????????????");
            } else if ("4".equals(s.getRemark())) {
                s.setRemark("??????????????????");
            } else {
                s.setRemark("?????????");
            }
            if ("0".equals(s.getFlag())) {
                s.setFlag("?????????");
            } else if ("1".equals(s.getFlag())) {
                s.setFlag("?????????");
            }
            if ("0".equals(s.getSystem_evaluation())) {
                s.setSystem_evaluation("?????????");
            } else if ("1".equals(s.getSystem_evaluation())) {
                s.setSystem_evaluation("?????????");
            }
            if (s.getShop_url() != null && !"".equals(s.getShop_url())) {
                s.setShop_url("<a target='_blank' href='" + s.getShop_url() + "'>" + s.getShop_url() + "<a>");
            }
            String details = s.getImgs();
            StringBuffer b = new StringBuffer("<div style='text-align:center;height:auto;overflow:auto;'>");
            if (details != null && details.indexOf("@") > -1) {
                for (int i = 0; i < details.split("@").length && i < 4; i++) {
                    String detail = details.split("@")[i];
                    if (!(detail.indexOf("gif") > -1)) {
                        b.append("<img src='" + detail
                                + "' style='width:100px;height:100px;' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"
                                + detail + "')\">");
                    }
                }
                b.append("<div>");
                s.setImgs(b.toString());
            }
            s.setOperation("<select   onchange='updateState(" + s.getId()
                    + ",this.value);'><option value='-1'>??????</option><option value='2'>????????????</option><option value='3'>????????????</option></select>");
        }
        return list;
    }
    @Override
    public int getShopManagerListCount(Map<String, Object> map) {

        return warehouseMapper.getShopManagerListCount(map);
    }
    @Override
    public List<RefundSamplePojo> searchRefundSample(Map<String, Object> map) {
        StringBuilder sb=new StringBuilder();
        DecimalFormat    df   = new DecimalFormat("######0.00");
        List<RefundSamplePojo> list=warehouseMapper.searchRefundSample(map);
        StringBuilder pids=new StringBuilder();
        for (RefundSamplePojo r : list) {
            String state="????????????";
            String style="";
            if("1".equals(r.getState())){
                state="?????????";
                style="none";
            }
            r.setImg("<img src='"+r.getImg()+"' style='width:50px;height:50px'>");
            r.setGoods_name("<a href='"+r.getGoods_p_url()+"' target='_blank'>"+(StringUtils.isStrNull(r.getGoods_p_name())?r.getGoods_name().substring(0,r.getGoods_name().length()/2):r.getGoods_name().substring(0,r.getGoods_name().length()/2))+"</a>");
            r.setOperating("<button style='display:"+style+"' onclick='nntryShipno("+r.getItemqty()+","+r.getFlag()+","+(StringUtils.isStrNull(r.getIn_id())?"0000":r.getIn_id())+","+r.getT_id()+",\""+r.getT_orderid()+"\",\""+r.getOd_orderid()+"\",\""+r.getGoodsid()+"\")'>??????</button>");
            sb.append("'").append(r.getGoods_pid()).append("',");
            r.setState(state);
            pids.append("'").append(r.getGoods_pid()).append("',");
        }
//        if(list.size()>0){
//            DataSourceSelector.set("dataSource28hop");
//            Map<String,String> map3=new HashMap<String, String>();
//            Map<String,String> map4=new HashMap<String, String>();
//            Map<String,String> map5=new HashMap<String, String>();
//            List<Map<String, String>> map1=warehouseMapper.getShopIdForPid(sb.toString().substring(0,sb.toString().length()-1));
//            DataSourceSelector.restore();
//            for (Map<String, String> map2 : map1) {
//                map3.put(map2.get("1688_pid"), map2.get("shop_id"));
//                map4.put(map2.get("1688_pid"), map2.get("levels"));
//                map5.put(map2.get("1688_pid"), map2.get("goods_price").contains("-")?map2.get("goods_price").split("-")[0].trim():map2.get("goods_price"));
//            }
//            for (RefundSamplePojo r1 : list) {
//                r1.setShop_id(map3.get(r1.getGoods_pid()));
//                r1.setShop_id_socre(map4.get(r1.getGoods_pid()));
//                double rate=(Double.valueOf(map5.get(r1.getGoods_pid()))*Util.EXCHANGE_RATE-Double.valueOf(r1.getPrice())-Double.valueOf(r1.getPreferential()))/(Double.valueOf(r1.getPrice())+Double.valueOf(r1.getPreferential()));
//                BigDecimal bg = new BigDecimal(rate);
//                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100;
//                r1.setProfit_rate(df.format(f1)+"%");
//            }
//        }
        return list;
    }
    public List<RefundSamplePojo> searchRefundSampleCount(Map<String, Object> map) {
        List<RefundSamplePojo> list=warehouseMapper.searchRefundSampleCount(map);
        return list;
    }
    @Override
    public List<TaoBaoOrderInfo> searchRefundOrder(Map<String, Object> map) {
        List<TaoBaoOrderInfo> list=warehouseMapper.searchRefundOrder(map);
        for (TaoBaoOrderInfo t : list) {
            //???????????????????????????????????????????????????????????????
            if(Integer.valueOf(t.getCounts())>=Integer.valueOf(t.getItemqty())){
                t.setOperating("<a title='?????????????????????????????????????????????' target='_blank' href='/cbtconsole/website/refundSample.jsp?orderid="+t.getOrderid()+"'>????????????</a>");
            }else{
                t.setOperating("<a title='?????????????????????????????????????????????' target='_blank' href='/cbtconsole/website/refundSample.jsp?orderid="+t.getOrderid()+"'>????????????</a>||<a href='javascript:openRefund(\""+t.getOrderid()+"\");'>????????????</a>");
            }
            t.setOrderid("<a title='?????????1688??????????????????' href='https://trade.1688.com/order/new_step_order_detail.htm?orderId="+t.getOrderid()+"' target='_blank'>"+t.getOrderid()+"</a>");
        }
        return list;
    }
    @Override
    public List<TaoBaoOrderInfo> searchRefundOrderCount(Map<String, Object> map) {

        return warehouseMapper.searchRefundOrderCount(map);
    }

    @Override
    public List<TaoBaoOrderInfo> searchMonthlyRefund(Map<String, Object> map) {

        return warehouseMapper.searchMonthlyRefund(map);
    }
    @Override
    public List<TaoBaoOrderInfo> searchMonthlyRefundCount(
            Map<String, Object> map) {

        return warehouseMapper.searchMonthlyRefundCount(map);
    }

    @Override
    public List<ShopManagerPojo> getPriorityCategory(Map<String, Object> map) {
        List<ShopManagerPojo> list=warehouseMapper.getPriorityCategory(map);
        for(ShopManagerPojo s:list){
            if("1".equals(s.getStatus())){
                s.setStatus("<span style='color:red'>??????</span>");
                s.setRemark("<button onclick=\"updateState("+s.getId()+",0)\" style='background-color:green'>???????????????????????????</button>|<button onclick=\"openEditKeyword("+s.getId()+",'"+s.getCategory()+"','"+s.getKeyword()+"','"+(StringUtil.isBlank(s.getAntiKey())?"":s.getAntiKey())+"')\">???????????????????????????</button>");
            }else{
                s.setStatus("<span style='color:green'>??????</span>");
                s.setRemark("<button onclick=\"updateState("+s.getId()+",1)\" style='background-color:red'>???????????????????????????</button>|<button onclick=\"openEditKeyword("+s.getId()+",'"+s.getCategory()+"','"+s.getKeyword()+"','"+(StringUtil.isBlank(s.getAntiKey())?"":s.getAntiKey())+"')\">???????????????????????????</button>");
            }
            s.setMinPrice("<input type='text' id='"+s.getId()+"_min'  title='?????????????????????' onkeyup='if(event.keyCode==13){updatePrice(0,"+s.getId()+")}' value='"+(StringUtil.isBlank(s.getMinPrice())?"-":s.getMinPrice())+"'>");
            s.setAntiKey("<input type='text' id='"+s.getId()+"_anti'  title='???????????????????????????' onkeyup=\"if(event.keyCode==13){updateAntikey("+s.getId()+","+s.getAntiId()+",'"+(StringUtil.isBlank(s.getKeyword())?"":s.getKeyword())+"')}\" value='"+(StringUtil.isBlank(s.getAntiKey())?"":s.getAntiKey())+"'>");
//            s.setMaxPrice("<input type='text' id='"+s.getId()+"_max' title='?????????????????????' onkeyup='if(event.keyCode==13){updatePrice(1,"+s.getId()+")}' value='"+(StringUtil.isBlank(s.getMaxPrice())?"-":s.getMaxPrice())+"'>");
            String enName=s.getEnName();
            String category=s.getCategory();
            if(StringUtil.isBlank(enName) && StringUtil.isNotBlank(category)){
                String names=warehouseMapper.getEnName(category);
                s.setEnName(names);
            }
        }
        return list;
    }

    @Override
    public int getPriorityCategoryCount(Map<String, Object> map) {
        return warehouseMapper.getPriorityCategoryCount(map);
    }

    @Override
    public List<ShopManagerPojo> getShopBuyLogInfo(Map<String, String> map) {
        List<ShopManagerPojo> list=warehouseMapper.getShopBuyLogInfo(map);
        List<ShopManagerPojo> shopSupplier=warehouseMapper.getShopSupplier(map);
        for(ShopManagerPojo s:list){
            s.setShopId("<a target='_blank' href='/cbtconsole/supplierscoring/supplierproducts?shop_id="+s.getShopId()+"'>"+s.getShopId()+"</a>");
        }
        ShopManagerPojo s=new ShopManagerPojo();
        s.setShopId("<span style='color:red'>??????ID</span>");
        s.setOrderdate("<span style='color:red'>????????????</span>");
        s.setTotalprice("<span style='color:red'>????????????</span>");
        s.setOrderid("<span style='color:red'>????????????</span>");
        s.setUsername("<span style='color:red'>?????????</span>");
        list.add(s);
        list.addAll(shopSupplier);
        return list;
    }

    @Override
    public int getShopBuyLogInfoCount(Map<String, String> map) {
        return warehouseMapper.getShopBuyLogInfoCount(map);
    }

    @Override
    public List<BlackList> getUserBackList(Map<String, String> map) {
        List<BlackList> list=warehouseMapper.getUserBackList(map);
        for(BlackList b:list){
            StringBuilder op=new StringBuilder();
            String flag=b.getFlag();
            if("0".equals(flag)){
                flag="<span style='color:green'>?????????</span>";
                op.append("<button style='color:red' onclick=\"updateFlag("+b.getId()+",1)\">??????</button>");
            }else if("1".equals(flag)){
                flag="<span style='color:red'>??????</span>";
                op.append("<button style='color:green' onclick=\"updateFlag("+b.getId()+",0)\">??????</button>");
            }
            if("0".equals(b.getType())){
                b.setType("???????????????");
            }else if("1".equals(b.getType())){
                b.setType("ip?????????");
            }else if("2".equals(b.getType())){
                b.setType("???????????????");
            }
            op.append("|<button onclick=\"updateEmail("+b.getId()+",'"+b.getBlackVlue()+"')\">??????</button>");
            b.setOption(op.toString());
            b.setFlag(flag);
        }
        return list;
    }

    @Override
    public List<BlackList> getUserBackListCount(Map<String, String> map) {
        return warehouseMapper.getUserBackListCount(map);
    }

    @Override
    public List<ShopManagerPojo> getShopManagerDetailsList(Map<String, Object> map) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        List<ShopManagerPojo> list=warehouseMapper.getShopManagerDetailsList(map);
        for (ShopManagerPojo s : list) {
            StringBuffer b = new StringBuffer("<div style='text-align:center;height:auto;overflow:auto;'>");
            String detail = s.getImgs();
            if (detail.indexOf(",") > -1) {
                String[] im = detail.split(",");
                for (int m = 0; m < im.length; m++) {
                    if (im[m].indexOf(".jpg")>-1 || im[m].indexOf(".png")>-1 || im[m].indexOf(".gif")>-1) {
                        Matcher t = p.matcher(im[m]);
                        im[m] = t.replaceAll("").replace("\\", "/");
                        b.append("<img src='http://192.168.1.28:90/shopimg/" + im[m].substring(14,im[m].length())+ "' style='width:100px;height:100px;' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('http://192.168.1.28:90/shopimg/" + im[m].substring(14,im[m].length())+ "')\">");
                    }
                }
            }
            b.append("</div>");
            s.setImgs(b.toString());
            s.setGoods_name("<a target='_blank' href='" + s.getGoods_p_url() + "'>"
                    + s.getGoods_name().substring(0, s.getGoods_name().length() / 3) + "...</a>");
            s.setOperation("<select   onchange=\"updateState(this.value,1,'"+s.getGoods_pid()+"')\"><option value='-1'>??????</option><option value='0'>?????????</option><option value='1'>??????</option><option value='2'>?????????</option><option value='3'>?????????</option></select>");
            if("0".equals(s.getStatus())){
                s.setStatus("?????????");
            }else if("1".equals(s.getStatus())){
                s.setStatus("??????");
            }else if("2".equals(s.getStatus())){
                s.setStatus("?????????");
            }else if("3".equals(s.getStatus())){
                s.setStatus("?????????");
            }else{
                s.setStatus("??????");
            }
        }
        return list;
    }
    @Override
    public List<com.cbt.pojo.AdmuserPojo> getAllBuyer(int id) {

        return warehouseMapper.getAllBuyer(id);
    }

    @Override
    public List<ZoneBean> getAllZone() {
        return warehouseMapper.getAllZone();
    }

    @Override
    public int addSampleRemark(Map<String, Object> map) {

        return warehouseMapper.addSampleRemark(map);
    }
    @Override
    public int deleteSource(Map<String,Object> map) {

        return warehouseMapper.deleteSource(map);
    }

    @Override
    public int updateCatePrice(Map<String, String> map) {
        return warehouseMapper.updateCatePrice(map);
    }

    @Override
    public int updatePrice(Map<String, Object> map) {

        return warehouseMapper.updatePrice(map);
    }

    @Override
    public BuyerCommentPojo getBuyerCommentPojo(Map<String, String> map) {
        return warehouseMapper.getBuyerCommentPojo(map);
    }

    @Override
    public int saveCommentContent(Map<String, String> map) {
        return warehouseMapper.saveCommentContent(map);
    }

    @Override
    public int insertStorageProblemOrder(Map<String, Object> map) {

        return warehouseMapper.insertStorageProblemOrder(map);
    }

    @Override
    public int saveClothingData(Map<String, String> map) {

        return warehouseMapper.saveClothingData(map);
    }

    @Override
    public List<RedManProductBean> getRedProduct(Map<String, String> map) {
        List<RedManProductBean> list=warehouseMapper.getRedProduct(map);
        for(RedManProductBean p:list){
            String pids=p.getPids();
            StringBuilder pid=new StringBuilder();
            String [] ps=pids.split(",");
            for(int i=0;i<ps.length;i++){
                if(i==ps.length-1){
                    pid.append("'").append(ps[i]).append("'");
                }else{
                    pid.append("'").append(ps[i]).append("',");
                }
            }
            if(StringUtil.isNotBlank(pid.toString())){
                List<CustomGoodsBean> cList=warehouseMapper.getCustomPids(pid.toString());
                pid.setLength(0);
                for(CustomGoodsBean c:cList){
                    pid.append("<a target='_blank'title='"+c.getEnname().replace("'","")+"' href='https://www.import-express.com/goodsinfo/cbtconsole-1"+c.getPid()+".html'><img src='"+(c.getRemotpath()+c.getCustomMainImage())+"'></img></a>");
                }
            }
            p.setRedProduct(pid.toString());
            pid.setLength(0);
            pid.append("recipients:").append(p.getRecipients()).append("<br>").append("address:").append(p.getAddress()).append("<br>").append("phonenumber:").append(p.getPhonenumber())
            .append("<br>").append("zipcode:").append(p.getZipcode()).append("<br>").append("statename:").append(p.getStatename())
            .append("<br>").append("city:").append(p.getCity()).append("<br>").append("countryname:").append(p.getCountryname());
            p.setAddress(pid.toString());
            if(StringUtil.isBlank(p.getShipno())){
                p.setShipno("<button onclick='saveShipno("+p.getId()+",0)'>??????????????????</button>");
            }else{
                p.setShipno(""+p.getShipno()+"<br><button onclick='saveShipno("+p.getId()+",1)'>??????????????????</button>");
            }
        }
        return list;
    }

    @Override
    public List<RedManProductBean> getRedProductCount(Map<String, String> map) {
        return warehouseMapper.getRedProductCount(map);
    }

    @Override
    public int insertShipno(Map<String, String> map) {
        return warehouseMapper.insertShipno(map);
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public List<PurchaseSamplingStatisticsPojo> getPurchaseSamplingStatistics(
            Map<String, Object> map) {
        List<PurchaseSamplingStatisticsPojo> list=new ArrayList<PurchaseSamplingStatisticsPojo>();
        Connection conn28=null,conn5=null,conn=null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            list=warehouseMapper.getPurchaseSamplingStatistics(map);
            if(list.size()>0){
                conn28 = DBHelper.getInstance().getConnection6();
                conn = DBHelper.getInstance().getConnection();
                conn5 = DBHelper.getInstance().getConnection5();
            }
            for(PurchaseSamplingStatisticsPojo p:list) {
                int counts=0;
                StringBuilder sql=new StringBuilder();
                sql.append("select count(a.goods_pid) as counts from ( SELECT sgo.goods_pid,sgo.create_time,sgo.good_url,sgo.admin_id,sgo.goods_name,sgo.pic AS img_1688");
                sql.append(" ,sgo.crawl_flag,sgo.set_weight,sgo.clear_flag, IFNULL(sgr.remotpath,'') AS remotpath,IFNULL(sgr.enname,'') AS express_name,");
                sql.append(" IFNULL(sgr.custom_main_image,'') AS express_img,IFNULL(sgr.valid,0) AS valid,IFNULL(sgr.sync_flag,0) AS sync_flag,");
                sql.append(" IFNULL(sgr.sync_remark,'') AS sync_remark,sgo.drainage_flag FROM single_goods_offers sgo LEFT JOIN useful_data.single_goods_ready sgr");
                sql.append(" ON sgo.goods_pid = sgr.pid WHERE 1 = 1 ");
                sql.append(" AND sgo.admin_id =").append(p.getAdmId());
                if(!"0".equals(String.valueOf(map.get("days")))){
                    sql.append(" AND TO_DAYS(NOW())-TO_DAYS(sgo.create_time)<=").append(map.get("days"));
                }
                sql.append(" AND IFNULL(sgr.sync_flag,0)=1) a");
                stmt= conn28.prepareStatement(sql.toString());
                rs=stmt.executeQuery();
                if(rs.next()){
                    counts=rs.getInt("counts");
                }
                p.setOnlineProducts(String.valueOf(counts));
                sql.setLength(0);
                sql.append("SELECT COUNT(0) AS counts FROM ali_1688_goods_online WHERE admin_id ="+p.getAdmId()+"");
                if(!"0".equals(String.valueOf(map.get("days")))){
                    sql.append(" AND TO_DAYS(NOW())-TO_DAYS(create_time)<=").append(map.get("days"));
                }
                stmt= conn.prepareStatement(sql.toString());
                rs=stmt.executeQuery();
                if(rs.next()){
                    counts=rs.getInt("counts");
                }
                p.setBenchmarksCount("<a target='_blank' title='????????????????????????' href='/cbtconsole/website/aliBeanchmarkingStatistic.jsp?admName="+p.getAdmName()+"&days="+(StringUtil.isNotBlank(String.valueOf(map.get("days")))?map.get("days"):"1500")+"'>"+counts+"</a>");
                sql.setLength(0);
                sql.append("select count(id) as counts from shop_url_bak where admuser='"+p.getAdmName()+"'");
                if(!"0".equals(String.valueOf(map.get("days")))){
                    sql.append(" AND TO_DAYS(NOW())-TO_DAYS(updatetime)<=").append(map.get("days"));
                }
                stmt= conn5.prepareStatement(sql.toString());
                rs=stmt.executeQuery();
                if(rs.next()){
                    counts=rs.getInt("counts");
                }
                p.setOnlineStores("<a target='_blank' href='/cbtconsole/website/input_shop_list.jsp?admName="+p.getAdmName()+"&days="+(StringUtil.isNotBlank(String.valueOf(map.get("days")))?map.get("days"):"1500")+"' title='??????????????????'>"+counts+"</a>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closeConnection(conn28);
            DBHelper.getInstance().closeConnection(conn5);
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closePreparedStatement(stmt);
        }
        return list;
    }

    @Override
    public List<Inventory> getAllUser() {
        return warehouseMapper.getAllUser();
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> salesPerformanceCount(Map<String, Object> map) {
        return warehouseMapper.salesPerformanceCount(map);
    }

	@Override
	public List<PurchaseSamplingStatisticsPojo> salesPerformanDetails(Map<String, String> map) {
		List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.salesPerformanDetails(map);
		for(PurchaseSamplingStatisticsPojo p:list){
			p.setGoodsname("<a target='_blank' href='https://www.import-express.com/goodsinfo/cbtconsole-1"+p.getGoods_pid()+".html'>"+(p.getGoodsname().substring(0,p.getGoodsname().length()/3))+"...</a>");
			if("0".equals(p.getState())){
				p.setState("?????????");
			}else if("1".equals(p.getState())){
				p.setState("?????????");
			}
			if("0".equals(p.getChecked())){
				p.setChecked("???????????????");
			}else{
				p.setChecked("????????????");
			}
			p.setCar_img("<img src='"+p.getCar_img()+"'></img>");
		}
		return list;
	}

	@Override
	public List<PurchaseSamplingStatisticsPojo> salesPerformanDetailsCount(Map<String, String> map) {
		return warehouseMapper.salesPerformanDetailsCount(map);
	}

	@Override
    public List<PurchaseSamplingStatisticsPojo> weightProblemDetails(Map<String, String> map) {
        List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.weightProblemDetails(map);
//        0???????????????1688????????????2kg???1?????????????????????0?????????2???1688?????????0?????????3??????????????????/1688??????>200%???4???1688??????/???????????????>200%???
//        5????????????????????? && ????????????1688??????????????????50%(??????????????????)???6????????????????????????/????????????>1.3 && ?????????>20????????????7???????????????1688??????????????????30%(??????????????????) && ?????????>20????????????
//        8???????????????1688??????????????????100%(??????????????????) && ?????????<20????????????9???1688??????<20??? ??? 1688?????????>1???10:1688?????????ali??????(1688????????????ali?????????)??????30%???
        for(PurchaseSamplingStatisticsPojo p:list){
            if("0".equals(p.getProblem_type())){
                p.setProblem_type("????????????1688????????????2kg");
            }else if("1".equals(p.getProblem_type())){
                p.setProblem_type("??????????????????0??????");
            }else if("2".equals(p.getProblem_type())){
                p.setProblem_type("1688?????????0??????");
            }else if("3".equals(p.getProblem_type())){
                p.setProblem_type("???????????????/1688??????>200%");
            }else if("4".equals(p.getProblem_type())){
                p.setProblem_type("1688??????/???????????????>200%");
            }else if("5".equals(p.getProblem_type())){
                p.setProblem_type("?????????????????? && ????????????1688??????????????????50%(??????????????????)");
            }else if("6".equals(p.getProblem_type())){
                p.setProblem_type("?????????????????????/????????????>1.3 && ?????????>20?????????");
            }else if("7".equals(p.getProblem_type())){
                p.setProblem_type("????????????1688??????????????????30%(??????????????????) && ?????????>20?????????");
            }else if("8".equals(p.getProblem_type())){
                p.setProblem_type("????????????1688??????????????????100%(??????????????????) && ?????????<20?????????");
            }else if("9".equals(p.getProblem_type())){
                p.setProblem_type("1688??????<20??? ??? 1688?????????>1");
            }else if("10".equals(p.getProblem_type())){
                p.setProblem_type("1688?????????ali??????(1688????????????ali?????????)??????30%");
            }
        }
        return list;
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> weightProblemDetailsCount(Map<String, String> map) {
        List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.weightProblemDetailsCount(map);
        return list;
    }

	@Override
	public List<PurchaseSamplingStatisticsPojo> monthSalesEffortsList(Map<String, String> map) {
		List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.monthSalesEffortsList(map);
		return list;
	}

	@Override
	public List<PurchaseSamplingStatisticsPojo> monthSalesEffortsListCount(Map<String, String> map) {
		List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.monthSalesEffortsListCount(map);
		return list;
	}

	@Override
    public List<PurchaseSamplingStatisticsPojo> getCleaningQuality(Map<String, String> map) {
        List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.getCleaningQuality(map);
        int allWeightCount=0;
        int allShelfCount=0;
        for(PurchaseSamplingStatisticsPojo p:list){
            allWeightCount+= Integer.valueOf(p.getWeightProblem());
            allShelfCount+= Integer.valueOf(p.getAutoShelf());
            p.setWeightProblem("<a title='????????????' target='_blank' href='/cbtconsole/website/weightProblemDetails.jsp?" +
                    "adminId="+p.getAdmin_id()+"&editTime="+(StringUtil.isBlank(map.get("editTime"))?"0":map.get("editTime"))+"&createtime="+(StringUtil.isBlank(map.get("createtime"))?"0":map.get("createtime"))+"'>"+p.getWeightProblem()+"</a>");
        }
        PurchaseSamplingStatisticsPojo ps=new PurchaseSamplingStatisticsPojo();
        ps.setAdmName("<span style='color:red;font-size:25px;'>??????:</span>");
        ps.setWeightProblem("<span style='color:red;font-size:25px;'>"+String.valueOf(allWeightCount)+"</span>");
        ps.setAutoShelf("<span style='color:red;font-size:25px;'>"+String.valueOf(allShelfCount)+"</span>");
        list.add(ps);
        return list;
    }
    @Override
    public List<PurchaseSamplingStatisticsPojo> getCleaningQualityCount(Map<String, Object> map) {
        return warehouseMapper.getCleaningQualityCount(map);
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> salesPerformance(Map<String, Object> map) {
        //????????????????????????
        List<PurchaseSamplingStatisticsPojo> list=warehouseMapper.salesPerformance(map);
        int openCount=0,addCount=0,buyCount=0,cancelCount=0,editCount=0;
        double saleCount=0.00;
        for(PurchaseSamplingStatisticsPojo p:list){
            map.put("admName",p.getAdmName());
            p.setEditCount(warehouseMapper.getEditCount(map));
            openCount+= Integer.valueOf(StringUtil.isBlank(p.getOpenCount())?"0":p.getOpenCount());
            addCount+= Integer.valueOf(StringUtil.isBlank(p.getAddCount())?"0":p.getAddCount());
            buyCount+= Integer.valueOf(StringUtil.isBlank(p.getBuyCount())?"0":p.getBuyCount());
            cancelCount+= Integer.valueOf(StringUtil.isBlank(p.getCancalCount())?"0":p.getCancelCount());
            editCount+= Integer.valueOf(StringUtil.isBlank(p.getEditCount())?"0":p.getEditCount());
            saleCount+=Double.parseDouble(StringUtil.isBlank(p.getSaleCount())?"0.00":p.getSaleCount());
            p.setBuyCount("<a target='_blank' href='/cbtconsole/website/salesPerformanDetails.jsp?admName="+p.getAdmName()+"&editTime="+map.get("updatetime")+"' title='????????????????????????'>"+p.getBuyCount()+"</a>");
//            //???????????????????????????????????????
//            p.setPresentations(warehouseMapper.getPresentations(p.getPid(),p.getUpdatetime()));
//            //???????????????????????????
//            p.setOpenCount(warehouseMapper.getOpenCount(p.getPid(),p.getUpdatetime()));
//            //???????????????????????????
//            p.setAddCarCount(warehouseMapper.getAddCarCount(p.getPid(),p.getUpdatetime()));
//            //?????????????????????
//            Map<String,String> buyMap=warehouseMapper.getBuyCount(p.getPid(),p.getUpdatetime());
//            if(buyMap != null && buyMap.get("buyCount") != null){
//                p.setBuyCount(String.valueOf(buyMap.get("buyCount")));
//            }
//            if(buyMap != null && buyMap.get("salesAmount") != null){
//                p.setSalesAmount(String.valueOf(buyMap.get("salesAmount")));
//            }
//            //?????????????????????
//            p.setCancalCount(warehouseMapper.getCancelCount(p.getPid(),p.getUpdatetime()));
        }
        PurchaseSamplingStatisticsPojo pp=new PurchaseSamplingStatisticsPojo();
        pp.setAdmName("<span style='color:red'>??????");
        pp.setOpenCount("<span style='color:red'>"+String.valueOf(openCount)+"</span>");
        pp.setAddCount("<span style='color:red'>"+String.valueOf(addCount)+"</span>");
        pp.setBuyCount("<span style='color:red'>"+String.valueOf(buyCount)+"</span>");
        pp.setCancelCount("<span style='color:red'>"+String.valueOf(cancelCount)+"</span>");
        pp.setEditCount("<span style='color:red'>"+String.valueOf(editCount)+"</span>");
        pp.setSaleCount("<span style='color:red'>"+String.valueOf(saleCount)+"</span>");
        list.add(pp);
        return list;
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> getPurchaseSamplingStatisticsCount(
            Map<String, Object> map) {

        return warehouseMapper.getPurchaseSamplingStatisticsCount(map);
    }
    @Override
    public String getAliPid(String goods_pid) {

        return warehouseMapper.getAliPid(goods_pid);
    }
    @Override
    public List<Inventory> getHavebarcode() {

        return warehouseMapper.getHavebarcode();
    }
    @Override
    public int getShopManagerListDetailsCount(Map<String, Object> map) {

        return warehouseMapper.getShopManagerListDetailsCount(map);
    }
    @Override
    public int updateShopState(Map<String, Object> map) {

        return warehouseMapper.updateShopState(map);
    }
    @Override
    public List<UserInfo> getUserInfoForPriceCount(Map<String, Object> map) {
        List<UserInfo> userInfos=warehouseMapper.getUserInfoForPriceCount(map);
        return userInfos;
    }


    @Override
    public Tb1688Pojo getTbState(Map<String, Object> map) {

        return warehouseMapper.getTbState(map);
    }

    @Override
    public List<AllProblemPojo> getAllProblem(Map<String, Object> map) {

        return warehouseMapper.getAllProblem(map);
    }

    @Override
    public int getTotalNumber(Map<String, Object> map) {

        return warehouseMapper.getTotalNumber(map);
    }

    @Override
    public List<String> getAllProposal(Map<String, Object> map) {

        return warehouseMapper.getAllProposal(map);
    }
    @Override
    public int insertShopId(Map<String, Object> map) {

        return warehouseMapper.insertShopId(map);
    }
    @Override
    public List<OrderReplenishmentPojo> getIsReplenishment(Map<String, Object> map) {
        return warehouseMapper.getIsReplenishment(map);
    }
    @Override
    public List<DisplayBuyInfo> displayBuyLog(Map<String, Object> map) {

        return warehouseMapper.displayBuyLog(map);
    }
    @Override
    public Map<String, String> getCompanyInfo(String goods_pid) {

        return warehouseMapper.getCompanyInfo(goods_pid);
    }
    @Override
    public int insertDeclareinfo(Map<String, Object> map) {

        return warehouseMapper.insertDeclareinfo(map);
    }

    @Override
    public List<JcexPrintInfo> getJcexPrintInfo(Map<String, Object> map) {

        return warehouseMapper.getJcexPrintInfo(map);
    }

    @Override
    public int updateDeclareinfoByOrderid(Map<String, Object> map) {

        return warehouseMapper.updateDeclareinfoByOrderid(map);
    }

    @Override
    public List<Integer> queryUser() {
        return warehouseMapper.queryUser();
    }

    @Override
    public int delteOrderReplenishment(Map<String, Object> map) {

        return warehouseMapper.delteOrderReplenishment(map);
    }

    @Override
    public String getOrderAddress(Map<String, Object> map) {

        return warehouseMapper.getOrderAddress(map);
    }

    @Override
    public int addKeyword(Map<String, String> map) {
        int row=0;
        try{

            row=warehouseMapper.addKeyword(map);
            if(row>0){
            	if(org.apache.commons.lang3.StringUtils.isNotBlank(map.get("keyword"))) {
            		String keyword = org.apache.commons.lang3.StringUtils.replace(map.get("keyword"), "'", "\\'");
            		SendMQ.sendMsg(new RunSqlModel("insert into priority_category(keyword,category) values('"+keyword+"','"+map.get("cateId")+"')"));
            		if(org.apache.commons.lang3.StringUtils.isNotBlank(map.get("antiKey"))) {
            			RunSqlModel runSqlModel = new RunSqlModel("update anti_key_words set flag =0,auti_word = '"+map.get("antiKey")+"' where keyword='"+keyword+"')");
            			int res = Integer.parseInt(SendMQ.sendMsgByRPC(runSqlModel));
            			if(res < 1) {
            				runSqlModel = new RunSqlModel("insert into anti_key_words (keyword,auti_word,flag) values('"+keyword+"','"+map.get("antiKey")+"',0)");
            				res = Integer.parseInt(SendMQ.sendMsgByRPC(runSqlModel));
            			}
            		}else {
            			RunSqlModel runSqlModel = new RunSqlModel("update anti_key_words set flag =1,auti_word = '' where keyword='"+keyword+"')");
            			SendMQ.sendMsg(runSqlModel);
            		}
            	}
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public int editKeyword(Map<String, String> map) {
        int row=0;
        try{

            row=warehouseMapper.editKeyword(map);
            if(row>0){
                SendMQ.sendMsg(new RunSqlModel("update priority_category set category="+map.get("cid")+" where id="+map.get("id")+""));
                if(org.apache.commons.lang3.StringUtils.isNotBlank(map.get("akey"))) {
                	
                	if(org.apache.commons.lang3.StringUtils.isNotEmpty(map.get("antiKey"))) {
                		RunSqlModel runSqlModel = new RunSqlModel("update anti_key_words set flag =0,auti_word = '"+map.get("antiKey")+"' where keyword='"+map.get("akey")+"')");
                		int res = Integer.parseInt(SendMQ.sendMsgByRPC(runSqlModel));
                		if(res < 1) {
                			runSqlModel = new RunSqlModel("insert into anti_key_words (keyword,auti_word,flag) values('"+map.get("akey")+"','"+map.get("antiKey")+"',0)");
                			res = Integer.parseInt(SendMQ.sendMsgByRPC(runSqlModel));
                		}
                	}else {
                		RunSqlModel runSqlModel = new RunSqlModel("update anti_key_words set flag =1,auti_word = '' where keyword='"+map.get("akey")+"')");
                		SendMQ.sendMsg(runSqlModel);
                	}
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public int updateStateCategory(Map<String, String> map) {
        int row=0;
        try{

            row=warehouseMapper.updateStateCategory(map);
            if(row>0){
                SendMQ.sendMsg(new RunSqlModel("update priority_category set status="+map.get("state")+" where id="+map.get("id")+""));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }
    @Override
    public int checkedGoods(Map<String, String> map) {
        return warehouseMapper.checkedGoods(map);
    }

    @Override
    public int productAuthorization(Map<String, String> map) {
        int ret=warehouseMapper.checkIsExit(map);
        if(ret>0){
            ret=warehouseMapper.updateAuthorizedFlag(map);
        }else{
            ret=warehouseMapper.insertAuthorizedFlag(map);
        }
        return ret;
    }

    @Override
    public int insertOrderRemark(Map<String, Object> map) {
        int count=warehouseMapper.findOrderRemark(map);
        if(count>0){
            return warehouseMapper.updateOrderRemark(map);
        }else{
            return warehouseMapper.insertOrderRemark(map);
        }
    }

    @Override
    public CustomsRegulationsPojo getCustomsRegulationsPojo(String orderid) {
        return warehouseMapper.getCustomsRegulationsPojo(orderid);
    }

    @Override
    public int insertWarningInfo(Map<String, String> map) {
        return warehouseMapper.insertWarningInfo(map);
    }

    @Override
    public List<Map<String,String>> getGoodsCar(Map<String, Object> map) {

        return warehouseMapper.getGoodsCar(map);
    }

    @Override
    public List<CustomGoodsBean> getAllGoodsInfos(String goods_pids) {

        return warehouseMapper.getAllGoodsInfos(goods_pids);
    }

    @Override
    public int insertOrderInfo(Orderinfo oi) {

        return warehouseMapper.insertOrderInfo(oi);
    }

    @Override
    public OrderDetailsBean getOldDetails(String pid) {
        return warehouseMapper.getOldDetails(pid);
    }

	@Override
	public OrderDetailsBean getCustomBeack(String pid) {
		return warehouseMapper.getCustomBeack(pid);
	}

	@Override
    public int insertOrderDetails(List<OrderDetailsBean> list) {

        return warehouseMapper.insertOrderDetails(list);
    }

    @Override
    public int getOrderDetailsExit(String goods_pid, String car_type) {

        return warehouseMapper.getOrderDetailsExit(goods_pid,car_type);
    }

    @Override
    public void insertGd(List<OrderDetailsBean> list) {

        warehouseMapper.insertGd(list);
    }

    @Override
    public int updateGdOdid() {
        return   warehouseMapper.updateGdOdid();
    }

    @Override
    public List<OrderDetailsBean> getOrderDetailsByOrderid(String orderid) {

        return warehouseMapper.getOrderDetailsByOrderid(orderid);
    }

    @Override
    public int updateCrossBorder(String goods_pid) {

        return warehouseMapper.updateCrossBorder(goods_pid);
    }

    @Override
    public int updateCrossShopr(String goods_pid) {

        return warehouseMapper.updateCrossShopr(goods_pid);
    }
    @Override
    public int updateShop(String goods_pid) {

        return warehouseMapper.updateShop(goods_pid);
    }

    @Override
    public int updateSamplFlag(String goodsPid,int count) {

        return warehouseMapper.updateSamplFlag(goodsPid,count);
    }

    @Override
    public String getHsCode(Map<String, Object> map) {

        return warehouseMapper.getHsCode(map);
    }

    @Override
    public String getShipmentno() {

        return warehouseMapper.getShipmentno();
    }

    @Override
    public int batchInsertSP(List<Map<String, String>> list) {
        return warehouseMapper.batchInsertSP(list);
    }

    @Override
    public int batchInsertSPLog(List<Map<String, String>> list) {
        return warehouseMapper.batchInsertSPLog(list);
    }

    @Override
    public int deleteShippingPackage(Map<String, String> map) {
        return warehouseMapper.deleteShippingPackage(map);
    }

    @Override
    public int selectShippingPackage(Map<String, String> map) {
        return warehouseMapper.selectShippingPackage(map);
    }

    @Override
    public List<Map<String, String>> getMaxImg(Map<String, String> map) {
        return warehouseMapper.getMaxImg(map);
    }

    @Override
    public List<Map<String, String>> getCntSum(Map<String, String> map) {
        return warehouseMapper.getCntSum(map);
    }

    @Override
    public List<ShippingPackage> getPackageInfo(Map<String, String> map) {
        List<ShippingPackage> list=warehouseMapper.getPackageInfo(map);
        list.stream().forEach(s->{
            s.setShipmentno("<input type='text' id='packageNo' value='"+map.get("shipmentno")+"' style='width: 50px' disabled='disabled'/><input type='hidden' id='"+map.get("shipmentno")+"' value='"+map.get("shipmentno")+"' />");
            s.setSweight("<input type='text' id='weight' value='"+s.getSweight()+"' />");
            String a="";
            String b="";
            String c="";
            if(StringUtil.isNotBlank(s.getSvolume())){
                String [] svolume =s.getSvolume().split("\\*");
                a=svolume[0];
                b=svolume[1];
                c=svolume[2];
            }
            s.setVolumeweight("<input type='text' id='volumeLength' value='"+a+"'  style='width: 50px'/>*<input type='text' id='width'  value='"+b+"' style='width: 50px'/>*<input type='text' id='height'  value='"+c+"' style='width: 50px'/>");
        });
        return list;
    }

    @Override
    public int batchUpdateShippingPackage(List<Map<String, String>> list) {
        return warehouseMapper.batchUpdateShippingPackage(list);
    }

    @Override
    public List<ShippingPackage> getPackageInfoList(Map<String, String> map) {
        List<ShippingPackage> list =new ArrayList<ShippingPackage>();
        try{
            list = warehouseMapper.getPackageInfoList(map);
            for(ShippingPackage bean:list){
                bean.setIsDropshipFlag(0);
            }
            List<ShippingPackage> dropshipList = warehouseMapper.getDropshipPackageInfoList(map);
            for(ShippingPackage bean:dropshipList){
                bean.setIsDropshipFlag(1);
            }
            list.addAll(dropshipList);
//        for(int i=0;i<list.size();i++){
//            ShippingPackage s=list.get(i);
//            s.setEstimatefreight(String.valueOf(Double.valueOf(s.getEstimatefreight())+s.getPid_amount()));
//        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ShippingPackage> getShippingPackageById(Map<String, String> map) {
        return warehouseMapper.getShippingPackageById(map);
    }

    @Override
    public int bgUpdate(List<Map<String, String>> list) {
        return warehouseMapper.bgUpdate(list);
    }

    @Override
    public int bgUpdateLog(List<Map<String, String>> list) {
        return warehouseMapper.bgUpdateLog(list);
    }

    @Override
    public List<SbxxPojo> getSbxxList(Map<String, String> map) {
        return warehouseMapper.getSbxxList(map);
    }

    @Override
    public int insertSbxx(Map<String, String> map) {
        return warehouseMapper.insertSbxx(map);
    }

    @Override
    public int updateOpsState(Map<String, String> map) {
        return warehouseMapper.updateOpsState(map);
    }

    @Override
    public int updateOrderinfoNumber(Map<String, String> map) {
        return warehouseMapper.updateOrderinfoNumber(map);
    }

    @Override
    public int updateOrderinfoState(Map<String, String> map) {
        //?????????????????????
        return warehouseMapper.updateOrderinfoState(map);
    }
    @Override
    public int updateChildOrderinfoState(Map<String, String> mapc) {
        //??????dropship??????????????????
        return warehouseMapper.updateChildOrderinfoState(mapc);
    }

    @Override
    public int GetSetOrdrerState(Map<String, String> map) {
        return warehouseMapper.GetSetOrdrerState(map);
    }

    @Override
    public int updateOrder(Map<String, String> map) {
        return warehouseMapper.updateOrder(map);
    }


    @Override
    public int updateorderDetailsState(Map<String, String> map) {
        return warehouseMapper.updateorderDetailsState(map);
    }

    @Override
    public int getOdIsState(Map<String, String> map) {
        return warehouseMapper.getOdIsState(map);
    }

    @Override
    public List<JcexPrintInfo> getJcexPrintInfoPlck(Map<String, Object> map) {
        List<JcexPrintInfo> jcexList=warehouseMapper.getJcexPrintInfoPlck(map);
        for (int i = 0; i < jcexList.size(); i++) {
            String orderid = jcexList.get(i).getOrderno();
            PurchaseServer purchaseServer = new PurchaseServerImpl();
            UserOrderDetails uod = purchaseServer.getUserDetails(orderid + ",");
            jcexList.get(i).setAdminname(uod.getAdminname());
            jcexList.get(i).setAdmincompany(uod.getAdmincompany());
            jcexList.get(i).setAdmincode(uod.getAdmincode());
            jcexList.get(i).setZone(uod.getZone());
            jcexList.get(i).setRecipients(uod.getRecipients());
            jcexList.get(i).setAddress(uod.getAddress()+uod.getUserstreet());
            jcexList.get(i).setAddress2(uod.getAddress2());
            jcexList.get(i).setStatename(uod.getStatename());
            jcexList.get(i).setZipcode(uod.getZipcode());
            jcexList.get(i).setPhone(uod.getPhone());
            jcexList.get(i).setPayType("PP");
            jcexList.get(i).setPay_curreny("USD");
            Map<String, Object> map1 = new HashMap<String, Object>();
            map.put("productName", jcexList.get(i).getProductname());
            String ret = warehouseMapper.getHsCode(map1);
            jcexList.get(i).setHscode(ret);
            map1.clear();
            String useridAndOrderid = "";
            if (jcexList.get(i).getOrderno() != null && !"".equals(jcexList.get(i).getOrderno())) {
                if (jcexList.get(i).getOrderno().indexOf("_") != -1) {
                    useridAndOrderid = jcexList.get(i).getOrderno().substring(jcexList.get(i).getOrderno().length() - 6,jcexList.get(i).getOrderno().length());
                } else {
                    useridAndOrderid = jcexList.get(i).getOrderno().substring(jcexList.get(i).getOrderno().length() - 4,jcexList.get(i).getOrderno().length());
                }
                useridAndOrderid = jcexList.get(i).getUserid() + "/"+ useridAndOrderid;
            }
            jcexList.get(i).setUseridAndOrderid(useridAndOrderid);
            System.out.println("this.orderno=" + jcexList.get(i).getOrderno()+ "     useridAndOrderid=" + useridAndOrderid);

        }
        return jcexList;
    }

    @Override
    public List<JcexPrintInfo> getJcexPrintInfoPlckCount(Map<String, Object> map) {
        List<JcexPrintInfo> jcexList=warehouseMapper.getJcexPrintInfoPlckCount(map);
        for (int i = 0; i < jcexList.size(); i++) {
            String orderid = jcexList.get(i).getOrderno();
            PurchaseServer purchaseServer = new PurchaseServerImpl();
            UserOrderDetails uod = purchaseServer.getUserDetails(orderid + ",");
            jcexList.get(i).setAdminname(uod.getAdminname());
            jcexList.get(i).setAdmincompany(uod.getAdmincompany());
            jcexList.get(i).setAdmincode(uod.getAdmincode());
            jcexList.get(i).setZone(uod.getZone());
            jcexList.get(i).setRecipients(uod.getRecipients());
            jcexList.get(i).setAddress(uod.getAddress()+uod.getUserstreet());
            jcexList.get(i).setAddress2(uod.getAddress2());
            jcexList.get(i).setStatename(uod.getStatename());
            jcexList.get(i).setZipcode(uod.getZipcode());
            jcexList.get(i).setPhone(uod.getPhone());
            jcexList.get(i).setPayType("PP");
            jcexList.get(i).setPay_curreny("USD");
            Map<String, Object> map1 = new HashMap<String, Object>();
            map.put("productName", jcexList.get(i).getProductname());
            String ret = warehouseMapper.getHsCode(map1);
            jcexList.get(i).setHscode(ret);
            map1.clear();
            String useridAndOrderid = "";
            if (jcexList.get(i).getOrderno() != null && !"".equals(jcexList.get(i).getOrderno())) {
                if (jcexList.get(i).getOrderno().indexOf("_") != -1) {
                    useridAndOrderid = jcexList.get(i).getOrderno().substring(jcexList.get(i).getOrderno().length() - 6,jcexList.get(i).getOrderno().length());
                } else {
                    useridAndOrderid = jcexList.get(i).getOrderno().substring(jcexList.get(i).getOrderno().length() - 4,jcexList.get(i).getOrderno().length());
                }
                useridAndOrderid = jcexList.get(i).getUserid() + "/"+ useridAndOrderid;
            }
            jcexList.get(i).setUseridAndOrderid(useridAndOrderid);
        }
        return jcexList;
    }

    @Override
    public List<Forwarder> getForwarderplck(Map<String, Object> map) {
        List<Forwarder>  list = warehouseMapper.getForwarderplck(map);
//        for(Forwarder bean :list){
//            bean.setIsDropshipFlag(0);
//        }
//        List<Forwarder>  fwList = warehouseMapper.getDropshipForwarderplck(map);
//        for(Forwarder bean :fwList){
//            bean.setIsDropshipFlag(1);
//        }
//        list.addAll(fwList);
        return list;
    }

    @Override
    public int getCountForwarderplck(Map<String, Object> map) {
        int count = warehouseMapper.getCountForwarderplck(map);
//        int count2 = warehouseMapper.getDropshipCountForwarderplck(map);
//        count+=count2;
        return count;
    }

    @Override
    public void getShippingPackCost(Forwarder forwarder) {
        String weight =forwarder.getSweight();
        String volumn =forwarder.getVolumeweight()<=0 ? "0" : String.valueOf(forwarder.getVolumeweight());
        String svolumn = forwarder.getSvolume();
        String countId =forwarder.getAd_country();
        String shippingmethod =forwarder.getTransportcompany();
        if (StringUtil.isNotBlank(weight) || StringUtil.isNotBlank(volumn)) {
            String subShippingmethod =StringUtil.isBlank(forwarder.getShippingtype())?"":String.valueOf(forwarder.getShippingtype());
            double weights_ = Double.parseDouble(weight);
            double volume_ = Double.parseDouble(volumn);
            //??????
            Map freightEpacketMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "epacket", subShippingmethod, svolumn);
            double freightEpacket=Double.valueOf(freightEpacketMap.get("freightFee").toString());
            //jcex
            Map freightJcexMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "jcex", subShippingmethod, svolumn);
            double freightJcex=Double.valueOf(freightJcexMap.get("freightFee").toString());
            //?????????
            Map freightYfhMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "?????????", subShippingmethod, svolumn);
            double freightYfh=Double.valueOf(freightYfhMap.get("freightFee").toString());
            double optimal_cost=0.00;
            String optimal_company="";
            if(freightEpacket<freightJcex && freightEpacket<freightYfh){
                optimal_cost=freightEpacket;
                optimal_company="??????";
            }else if(freightJcex<freightEpacket && freightJcex<freightYfh){
                optimal_cost=freightJcex;
                optimal_company="JCEX";
            }else if(freightYfh<freightEpacket && freightYfh<freightJcex){
                optimal_cost=freightYfh;
                optimal_company="?????????";
            }
            forwarder.setOptimal_cost(optimal_cost);
            forwarder.setOptimal_company(optimal_company);
        }
    }

    @Override
    public int updateExperssNoPlck(Map<String, Object> map) {
        return warehouseMapper.updateExperssNoPlck(map);
    }

    @Override
    public int updateGoodsDistribution(Map<String, String> map) {
        return warehouseMapper.updateGoodsDistribution(map);
    }

    @Override
    public int updateRemainingPrice(Map<String, String> map) {
        return warehouseMapper.updateRemainingPrice(map);
    }

    @Override
    public int updateODPState(Map<String, String> map) {
        return warehouseMapper.updateODPState(map);
    }

    @Override
    public int updateSendMail(Map<String, String> map) {
        return warehouseMapper.updateSendMail(map);
    }
    @Override
    public int insertId_relationtable(Map<String, String> map) {
        return warehouseMapper.insertId_relationtable(map);
    }
    @Override
    public int xlsbatch(List<Map<String, String>> list) {
        return warehouseMapper.xlsbatch(list);
    }

    @Override
    public int addGoodsInventory(Map<String,String> map) {
        return warehouseMapper.addGoodsInventory(map);
    }

    @Override
    public String getUserName(String userid) {
        return warehouseMapper.getUserName(userid);
    }

    @Override
    public List<GoodsInventory>  addGoodsInvent(String orderid) {
        warehouseMapper.addGoodsInvent(orderid);
        List<GoodsInventory> list = warehouseMapper.selectInventByOrderId(orderid);
        return  list;
    }

    @Override
    public int  insertSp(String shipmentno) {
        return warehouseMapper.insertSp(shipmentno);
    }

    @Override
    public int selectOrderFeeByOrderid(Map<String, Object> map) {
        List<String>  list = warehouseMapper.selectOrderFeeByOrderid(map);
        int res = list.size();
        return res;
    }

    @Override
    public List<String> getOrderNoList(Map<String, Object> map) {
        return warehouseMapper.getOrderNoList(map);
    }
    @Override
    public String getSpId(String orderids) {

        return warehouseMapper.getSpId(orderids);
    }

    @Override
    public int upOrderInfo(Map<String, Object> map) {
        return 	warehouseMapper.upOrderInfo(map);
    }

    @Override
    public int upId_relationtable(Map<String, Object> map) {
        return  warehouseMapper.upId_relationtable(map);
    }

    @Override
    public int upOrderFee(Map<String, Object> map) {
        return  warehouseMapper.upOrderFee(map);
    }

    @Override
    public int upWaraingState(Map<String, Object> map) {
        return  warehouseMapper.upWaraingState(map);
    }

    @Override
    public String getMegerOrder(String orderId) {
        return  warehouseMapper.getMegerOrder(orderId);
    }

    @Override
    public List<String> getOrderFeeOrderNO(String orderno) {
        return  warehouseMapper.getOrderFeeOrderNO(orderno);
    }

    @Override
    public int updateDropshipState(Map<String, Object> map) {
        return warehouseMapper.updateDropshipState(map);
    }

    @Override
    public String checkState(String orderno) {
        //?????????????????????????????????????????????????????????
        int sum = warehouseMapper.selectCountByparent_order_no(orderno);
        //????????????????????????????????????3?????????
        int count = warehouseMapper.selectSumByState(orderno);
        String order= null;
        if(count==sum){
            //????????????????????????3,?????????????????????
            order = warehouseMapper.selectDropshipOrderNo(orderno);
        }
        return order;
    }

    @Override
    public int updateMainDropshipState(String mainOrder) {
        return warehouseMapper.updateMainDropshipState(mainOrder);
    }

    @Override
    public List<LocationManagementInfo> getLocationManagementInfo(Map<Object, Object> map) {

        return warehouseMapper.getLocationManagementInfo(map);
    }

    @Override
    public List<LocationManagementInfo> getLocationManagementInfoByOrderid(
            String orderid) {

        return warehouseMapper.getLocationManagementInfoByOrderid(orderid);
    }

    @Override
    public int resetLocation(Map<String, String> map) {

        return warehouseMapper.resetLocation(map);
    }

    @Override
    public int updateFlag(String id, String type) {
        return warehouseMapper.updateFlag(id,type);
    }

    @Override
    public int updatebackEmail(String id, String newBlackVlue,String type) {
        return warehouseMapper.updatebackEmail(id,newBlackVlue,type);
    }

    @Override
    public int addBackUser(String blackVlue, String type,String userName) {
        return warehouseMapper.addBackUser(blackVlue,type,userName);
    }

    @Override
    public int searchCount(Map<Object, Object> map) {

        return warehouseMapper.searchCount(map);
    }

    @Override
    public void inertLocationTracking(Map<String, String> map) {

        warehouseMapper.inertLocationTracking(map);
    }

    @Override
    public List<LocationTracking> getLocationTracking(Map<Object, Object> map) {

        return warehouseMapper.getLocationTracking(map);
    }

    @Override
    public int searchCount1() {

        return warehouseMapper.searchCount1();
    }

    @Override
    public String getCreateTime(String barcode) {
        return warehouseMapper.getCreateTime(barcode);
    }

    @Override
    public int noInspection() {

        List<String> rows=warehouseMapper.noInspection();
        return rows!=null?rows.size():0;
    }

    @Override
    public List<LocationManagementInfo> getCheckOrders(String orderids, int num) {

        return warehouseMapper.getCheckOrders(orderids,num);
    }

    @Override
    public List<StorageLocationBean> getAllOutboundorder(Map<Object, Object> map) {

        return warehouseMapper.getAllOutboundorder(map);
    }

    @Override
    public List<OrderDetailsBean> getOrderDetailsInfo(Map<Object, Object> map) {

        return warehouseMapper.getOrderDetailsInfo(map);
    }

    @Override
    public int updateState(Map<String, String> map) {

        return warehouseMapper.updateState(map);
    }

    @Override
    public int getMid() {

        return warehouseMapper.getMid();
    }

    @Override
    public int getShortTerm() {

        return warehouseMapper.getShortTerm();
    }

    @Override
    public String getOrderIdByBarcode(String barcode) {

        return warehouseMapper.getOrderIdByBarcode(barcode);
    }

    @Override
    public List<String> getSaleOrderid(String shipno) {
        return warehouseMapper.getSaleOrderid(shipno);
    }

    @Override
    public List<StorageInspectionLogPojo> getStorageInspectionLogInfo(Map<String, String> map) {
        return warehouseMapper.getStorageInspectionLogInfo(map);
    }


    @Override
    public LocationManagementInfo getTaoBaoInfos(String orderid) {

        return warehouseMapper.getTaoBaoInfos(orderid);
    }

    @Override
    public int getAcounts(String orderid) {

        return warehouseMapper.getAcounts(orderid);
    }

    @Override
    public int getAmounts(String orderid) {

        return warehouseMapper.getAmounts(orderid);
    }

    @Override
    public List<TaoBaoOrderInfo> getPurchaseOrderDetails(Map<Object, Object> map) {

        return warehouseMapper.getPurchaseOrderDetails(map);
    }

    @Override
    public int updateIsRead(String orderid) {

        return warehouseMapper.updateIsRead(orderid);
    }

    @Override
    public List<String> getInfos(int admid) {

        return warehouseMapper.getInfos(admid);
    }

    @Override
    public List<Tb1688Account> getAllBuy() {

        return warehouseMapper.getAllBuy();
    }

    @Override
    public void updateMessageY(String ids) {

        warehouseMapper.updateMessageY(ids);
    }

    @Override
    public OrderBean getUserOrderInfoByOrderNo(String orderNo) {
        return warehouseMapper.getUserOrderInfoByOrderNo(orderNo);
    }

    @Override
    public int updateBarcodeByOrderNo(String orderid) {

        List<String>  list = warehouseMapper.selectBarcideByOrderNo(orderid);
        int ret = 0 ;
        if(list.size()>0){
            ret = warehouseMapper.updateBarcodeByOrderNo(list);
        }
        return ret;
    }

    @Override
    public List<TaoBaoOrderInfo> getAllCount(Map<Object, Object> map) {

        return warehouseMapper.getAllCount(map);
    }

    @Override
    public int queryOrderState(Map<String, String> map) {

        return warehouseMapper.queryOrderState(map);
    }

    @Override
    public int updateOrderState(Map<String, String> map) {

        return warehouseMapper.updateOrderState(map);
    }

    @Override
    public int updateAllDetailsState(Map<String, String> map) {

        return warehouseMapper.updateAllDetailsState(map);
    }

    @Override
    public int updateTbState(Map<String, String> map) {
        return warehouseMapper.updateTbState(map);
    }

    @Override
    public int insertRemark(Map<String, String> map) {

        return warehouseMapper.insertRemark(map);
    }

    @Override
    public List<String> allLibraryCount() {

        return warehouseMapper.allLibraryCount();
    }

    @Override
    public int getStockOrderInfo() {

        return warehouseMapper.getStockOrderInfo();
    }

    @Override
    public List<StorageLocationBean> allLibrary(Map<Object, Object> map) {

        return warehouseMapper.allLibrary(map);
    }

    @Override
    public void treasuryNote(String orderNo, int goodsid, String remarkContent) {
        warehouseMapper.treasuryNote(orderNo, goodsid, remarkContent);
    }
    @Override
    public int updateCheckForNote(String orderNo, int goodsid) {
        return warehouseMapper.updateCheckForNote(orderNo, goodsid);
    }
    @Override
    public Integer[] deleteOrderGoods(String orderNo, int goodId, int userid,
                                      int purchase_state, List<ClassDiscount> cdList, HttpServletResponse response) {
        int res = warehouseMapper.getDelOrder(orderNo);//????????????????????????????????????????????????????????????

        warehouseMapper.updateOrderChange2State(orderNo, goodId, null);//??????order_change

        Map<String, Object> orderinfoMap = warehouseMapper.getOrderinfoByOrderNo(orderNo);//???????????????????????????

        String remaining = String.valueOf(orderinfoMap.get("remaining_price"));

        orderinfoMap.put("remaining_price", Utility.getStringIsNull(remaining) ? remaining : "0");

        List<OrderDetailsBean> orderDetails = warehouseMapper.getDelOrderByOrderNoAndStatus(orderNo, goodId);//????????????????????????????????????

        int deleteCtpoOrderGoods = warehouseMapper.updateOrderDetails2StateByGoodId(orderNo, goodId);//????????????????????????=2
        if (deleteCtpoOrderGoods < 0) {
            deleteCtpoOrderGoods = 0;
        }
        //??????????????????????????????????????????????????????
        //Map<String, Object> odblist=orderMapper.getDelOrderByOrderNoAndStatus(orderNo,goodId, 2);
        //List<OrderDetailsBean> orderDetailsBeans = (List<OrderDetailsBean>) orderinfoMap.get("odb");
        double discount_amount = Double.parseDouble(orderinfoMap.get("discount_amount").toString());//????????????????????????
        double share_discount = Double.parseDouble(orderinfoMap.get("share_discount").toString());//??????????????????
        double coupon_discount = Double.parseDouble(orderinfoMap.get("coupon_discount").toString());//????????????
        String currency = orderinfoMap.get("currency").toString();
        double order_ac = Double.parseDouble(orderinfoMap.get("order_ac").toString());
        double product_cost = Double.parseDouble(orderinfoMap.get("product_cost").toString());
        double cashback = Double.parseDouble(orderinfoMap.get("cashback").toString());
        double payprice = Double.parseDouble(orderinfoMap.get("pay_price").toString());//.get("pay_price").toString()
        //????????????????????????
//		String currency_u = userMapper.getCurrencyById(userid);
        double userAccount =Double.parseDouble(String.valueOf(warehouseMapper.getBalance_currency(userid).get("available_m")));
        double exchange_rate = 1;
        String pay_price = "";
        if(orderinfoMap != null && deleteCtpoOrderGoods == 1){
            for (int i = 0; i < orderDetails.size(); i++) {
                OrderDetailsBean odb = orderDetails.get(i);
                if(odb.getGoodsid() == goodId){
                    int yourorder=odb.getYourorder();
                    String goodsprice=odb.getGoodsprice();
                    BigDecimal b=new BigDecimal(goodsprice);
                    double mc_gross=b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    //?????????????????????=(??????*??????)-??????????????????????????????
                    double available=mc_gross*yourorder;
                    double discount_pro = 0;
                    double  newShareDiscount =0;
                    double  newCouponDiscount =0;
                    //???????????????????????????
                    if(discount_amount != 0 && odb.getDiscount_ratio() != 0){
                        discount_pro = new BigDecimal((1-odb.getDiscount_ratio()) * available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    //????????????
                    if(coupon_discount>0){
                        newCouponDiscount = new BigDecimal(available/product_cost*coupon_discount).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    //??????????????????????????????
                    if(share_discount>0){
                        newShareDiscount = new BigDecimal(available * 0.03 ).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        if(newShareDiscount>share_discount){
                            newShareDiscount = share_discount;
                        }
                    }
                    available = new BigDecimal(available - discount_pro-newShareDiscount-newCouponDiscount).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    discount_amount = discount_amount - discount_pro;
                    share_discount = share_discount - newShareDiscount;
                    share_discount = share_discount>0?share_discount:0;
                    product_cost = new BigDecimal(product_cost - available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					/*if(odb.getGoods_class() != 0){
						up_discount = 1;
					}*/


                    //????????????????????????????????????->??????????????????????????????????????????-????????????????????????????????????????????????????????????->????????????????????????????????????(??????????????????-????????????),?????????????????????0->???????????????????????????(????????????)
                    String remaining_price =  (String) orderinfoMap.get("remaining_price");
                    //??????????????????
                    double r_price=0;
                    if(Utility.getIsDouble(remaining_price)){
                        BigDecimal r=new BigDecimal(remaining_price);
                        r_price=r.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    if(product_cost < 200 && cashback != 0){
                        cashback = 0;
                        r_price = r_price + 10;
                    }
                    double available_r = 0;
                    if(r_price > available ){
                        //????????????????????????????????????->????????????????????????????????????(??????????????????-????????????)
                        available_r = new BigDecimal(r_price - available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        pay_price = new BigDecimal(payprice).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
                        warehouseMapper.updateOrderPayPrice1(userid, orderNo, pay_price, null,discount_amount,share_discount,product_cost,available_r,cashback);
                    }else{
                        //????????????????????????????????????->??????????????????????????????????????????-?????????????????????
                        available_r = new BigDecimal(available - r_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        if(res == 1){
                            available_r = payprice;
                            order_ac = new BigDecimal(order_ac*exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }else{
                            order_ac = 0;
                        }
                        double available_ru = new BigDecimal(available_r*exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        pay_price = new BigDecimal(payprice - available_r).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
                        //?????????????????????????????????
                        if(Double.parseDouble(pay_price) >= 0){
                            //??????????????????
                            RechargeRecord rr = new RechargeRecord();
                            rr.setUserid(userid);
                            rr.setPrice(available_ru);
                            rr.setRemark_id(orderNo);
                            rr.setType(1);
                            rr.setBalanceAfter(userAccount+available_ru);
                            rr.setRemark("cancel:"+orderNo+",goodsid:"+goodId);
                            rr.setCurrency(currency);
                            warehouseMapper.addRechargeRecord(rr);
                            warehouseMapper.upUserPrice(userid, available_ru, order_ac);
                            warehouseMapper.updateOrderPayPrice1(userid, orderNo, pay_price, null, discount_amount,share_discount,product_cost, 0.0,cashback);
                            //????????????--?????????????????????????????????
                            try {
                                warehouseMapper.updateAcceptPriceByOrderNo(orderNo,pay_price);
                            } catch (Exception e) {
                                LOG.error("??????????????????", e);
                            }

                        }
                    }
                }
            }

        }
        if(res == 1 && deleteCtpoOrderGoods == 1){
            //???????????????????????????????????? ???????????????-1????????????????????????6
            warehouseMapper.upOrderPurchase(purchase_state, orderNo, -1);
            //????????????????????????message ????????????????????????????????????
            warehouseMapper.delMessageByOrderid(orderNo, "Order #");
            //?????????????????? accept_share ??????
            try {
                int ret = warehouseMapper.updateAcceptShareByOrderNo(orderNo);
                if(ret>0){
                    //??????cookie ?????? shareID
                    Cookie cookie = new Cookie("shareID",null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            } catch (Exception e) {
            }
            return new Integer[]{deleteCtpoOrderGoods,6};
        }else{
            warehouseMapper.upOrderPurchase(purchase_state, orderNo, 5);
            Integer orderstate =  warehouseMapper.checkUpOrderState(orderNo);
            return new Integer[]{deleteCtpoOrderGoods,orderstate};
//			return new Integer[]{deleteCtpoOrderGoods,5};
        }
    }

    @Override
    public List<OrderBean> getOrderInfo(int userid, String order_no) {
        return warehouseMapper.getOrderInfoByOrder_noAndUserid( userid, order_no.split(","));
    }

    @Override
    public int insertBuluInfo(List<Map<String, Object>> bgList) {
        return  warehouseMapper.insertBuluInfo(bgList);
    }

    @Override
    public String getOrderInfoStateByOrderNo(String orderNo) {
        return warehouseMapper.getOrderInfoStateByOrderNo(orderNo);
    }

    @Override
    public int getOrderDetailsStateByOrderNoAndGoodsid(String orderNo, int odid) {
        return warehouseMapper.getOrderDetailsStateByOrderNoAndGoodsid(orderNo, odid);
    }

    @Override
    public int updateOrderSourceState(String orderid) {

        return warehouseMapper.updateOrderSourceState(orderid);
    }

    @Override
    public int checkAuthorizedFlag(String orderid) {
        int ret=0;
        Map<String,String> map=new HashMap<String,String>();
        List<String> oList=warehouseMapper.getAllGoodsPidsByOrderNo(orderid);
        for(String pid:oList){
            map.put("goodsPid",pid);
            ret=warehouseMapper.checkIsExit(map);
            if(ret<=0){
                map.put("flag","2");
                ret=warehouseMapper.insertAuthorizedFlag(map);
            }
        }
        return ret;
    }

    @Override
    public int insertGoodsInventory(String orderid) {
        return warehouseMapper.insertGoodsInventory(orderid);
    }

    @Override
    public int updateBuluOrderState(List<Map<String, Object>> bgList) {

        // dropship ??????
        List<Map<String, Object>>  list  = new  ArrayList<Map<String,Object>>();
        for(Map<String,Object> map: bgList){
            int  isDropShipFlag =  (Integer) map.get("isDropShipFlag");
            if(isDropShipFlag==1){
                list.add(map);
            }
        }
        //??????orderinfo ????????????
        warehouseMapper.updateBuluOrderState(bgList);
        //??????dropshipOrder ????????????
        warehouseMapper.updateBuluDropShipOrderState(list);
        return warehouseMapper.updateBuluOrderState(bgList);
    }

    @Override
    public Map<String, Object> getDetailsByRemarks(String remarks) {
        Map<String, Object> resMap = new HashMap<String, Object>();
//		remarks = remarks.trim();
//		remarks = remarks.substring(0, remarks.length()-1);
        List<String> remarksList = new ArrayList<String>();
        String[] remarksArr = remarks.split(",");
        for (String temRemarks : remarksArr) {
            if (temRemarks != null && !"".equals(temRemarks)) {
                remarksList.add(temRemarks.trim());
            }
        }

        //?????????????????
        ArrayList<String> picturepaths = new ArrayList<String>();
        resMap.put("picturepaths", picturepaths);
        // order_details.picturepath		orderid,picturepath
        List<Map<String, Object>> orderDetailss = warehouseMapper.getOrderDetailsByOrderidIn(remarksList);
        for (Map<String, Object> orderDetails : orderDetailss) {
            Object picturepath = orderDetails.get("picturepath");
            if (picturepath != null && !"".equals(picturepath.toString())){
                picturepath = picturepath.toString().replaceAll("http:", "https:");
                if ("https:".equals(picturepath.toString().substring(0,6))) {
                    picturepaths.add(picturepath.toString());
                } else {
                    picturepaths.add(Util.PIC_URL + picturepath.toString());
                }
            }
        }
        //????????????
        ArrayList<String> ftpPicPaths = new ArrayList<String>();
        resMap.put("ftpPicPaths", ftpPicPaths);
        // products packed ???????????? orderinfo.ftpPicPath 		order_no,ftpPicPath
        List<Map<String, Object>> orderinfos = warehouseMapper.getOrderinfoByOrderNoIn(remarksList);
        for (Map<String, Object> orderinfo : orderinfos) {
            Object ftpPicPath = orderinfo.get("ftpPicPath");
            if (ftpPicPath != null && !"".equals(ftpPicPath.toString())){
                String[] split = ftpPicPath.toString().split("&");
                if (split != null && split.length > 0){
                    for (String str : split) {
                        if (str != null && "http:".equals(str.substring(0,5))){
                            ftpPicPaths.add(str);
                        }
                    }
                }
            }
        }
        return resMap;
    }
    
    @Override
    public void insertChangeLog(Map<String, Object> map) {
    	//???????????????????????? ???????????????????????????????????????????????????????????? ly 2018/08/22 15:36
    	warehouseMapper.insertChangeLog(map);
    }

    @Override
    public int FindOrderCount(String admuserid) {
        int count=0;
        try {
            count= this.warehouseMapper.FindOrderCount();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public  List<CustomGoodsBean> getBadgoods(int start,int pagesize,String pidc) {
        try {
             if ("".equals(pidc)){
                 pidc=null;
             }
            List<String> list=this.warehouseMapper.FindAllPid(start,pagesize,pidc);
            List<CustomGoodsBean> goodsBeans=new ArrayList<>();
            int i=0;
            for (String pid:list) {
                CustomGoodsBean goodsBean = this.warehouseMapper.selectByPid(pid);
                goodsBeans.add(goodsBean);
            }
            return goodsBeans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int getBadgoodsCount() {
        try {
            int conut=this.warehouseMapper.FindCount();
            return conut;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int AddBadOrder(String pid, Double price) {

        try {
            int conut=this.warehouseMapper.AddBadOrder(pid,price);
            return conut;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int UpdateState(String pid) {
        try {
            int conut=this.warehouseMapper.UpdateState(pid);
            return conut;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<badGoods> findAllCustomBypid(String pid, Double price,int pagesize,int start,String cupid) {
        try {
            if ("".equals(cupid)){
                cupid=null;
            }
            List<badGoods> list=this.warehouseMapper.findAllCustomBypid(pid,price,pagesize,start,cupid);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int findAllCustomBypidCount(String pid, Double price,String cupid) {
        try {
            if ("".equals(cupid)){
                cupid=null;
            }
            int count=this.warehouseMapper.findAllCustomBypidCount(pid,price,cupid);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int AddReviewGoods(String pid, String catid1, String name, String maxPrice) {
        try {
            int count=this.warehouseMapper.AddReviewGoods(pid,catid1,name,maxPrice);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public returndisplay FindReturnTime(String orderid) {
        try {
            returndisplay re=new returndisplay();
            re=this.warehouseMapper.FindReturnTime(orderid);
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getRepathByPid(String pid) {
        String path=this.warehouseMapper.getRepathByPid(pid);
        return path;
    }

    @Override
    public List<Inventory> FindAllGoods(int page,int pagesize,String pid) {
        List<Inventory> list=new ArrayList<>();
        try {
            if(pid !=null){
                pid=pid.replaceAll("???",",");
                String [] arr=pid.split(",");
                List<String> listpid = Arrays.asList(arr);
                for (String pi:listpid){
                    List<Inventory> inventories=this.warehouseMapper.FindGoodsByPid(pi);
                   list.addAll(inventories);
                }
                for (int i=0;i<list.size();i++){
                    list.get(i).setSkuList(this.getSkulistSd(list.get(i).getEntype(),list.get(i).getFinal_weight()));
                }
            }else {
               list = this.warehouseMapper.FindAllGoods(page * pagesize, pagesize, pid);
                for (int i=0;i<list.size();i++){
                    list.get(i).setSkuList(this.getSkulist(list.get(i).getSku(),list.get(i).getFinal_weight()));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setInventoryCountBySkuAndPid(List<SampleOrderBean> list) {
        try {
            for (int i=0;i<list.size();i++) {
                this.warehouseMapper.setInventoryCountBySkuAndPid(list.get(i));
            }
            return true;
        }  catch (Exception e) {
        e.printStackTrace();
            return false;
    }

    }

    @Override
    public boolean addprocurement(List<SampleOrderBean> orderNos) {
        try {
        SampleOrderBean ben=this.orderinfoMapper.addprocurement(orderNos.get(0).getOrderNo().replace("_SP",""));
        boolean bo=false;
        for (SampleOrderBean sob:orderNos) {
            if (ben != null) {
                ben.setPid(sob.getPid());
                ben.setOrderNo(sob.getOrderNo());
                  this.orderinfoMapper.addOrder(ben);
            }else {
                return false;
            }
        }
        return true;
        }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int orderdtailDetail() {
        try {
         this.warehouseMapper.orderdtailDetail();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int insertMqLog(String sqlStr, String shopNo, String orderNo, String paramStr) {
        return warehouseMapper.insertMqLog(sqlStr, shopNo, orderNo, paramStr);
    }

    public Map<String,List<String>> getSkulist(String sku,String weight) {
         String [] arr=sku.split(",");
         List<String> list = Arrays.asList(arr);
         List lists=new ArrayList(list);
        Map<String,List<String>> map=new HashMap<>();
        map.put("??????",list);
       return map;
    }
    public Map<String,List<String>> getSkulistSd(String sku,String weight) {
        List<EntypeBen> EntypeBens= JsonUtils.jsonToList(sku,EntypeBen.class);
        Map<String,List<String>> map=new HashMap<>();
        Set<String> set=new HashSet<>();
        for (EntypeBen en:EntypeBens){
         set.add(en.getType());
        }
        for(int i=0;i<set.size();i++){
            List list=new ArrayList();
        for (EntypeBen en:EntypeBens){
                if (en.getType().equals(set.toArray()[i])){
                    list.add(en.getValue()+"@"+en.getId());
                    map.put(en.getType(),list);
                }
            }
        }
        return map;
    }



	@Override
	public List<Map<String, Object>> getOverseasWarehouseStockOrder(String ordernoOrUserid) {
		return orderinfoMapper.getOverseasWarehouseStockOrder(ordernoOrUserid);
	}

    public boolean getUserNotFreeState(int userId){
        // @author: cjc @date???2020/1/19 5:09 ??????   Description : ??????redis????????????
        String key = "AUTH:USER_NOTFREE";
        boolean b = redisUtil.hasKey(key);
        if(!b){
            List<UserFreeNotFree> userFreeNotFrees = userFreeNotFreeService.selectAll();
            if(userFreeNotFrees.size() > 0){
                Map<String,Object> map = new HashMap<>();
                userFreeNotFrees.stream().forEach(userFreeNotFree -> {
                    redisUtil.sSet(key, String.valueOf(userId));
                });
            }
        }
        boolean member = redisUtil.isMember(key, String.valueOf(userId));
        return  member;
    }
}
