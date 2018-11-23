package com.cbt.warehouse.service;

import com.cbt.FreightFee.service.FreightFeeSerive;
import com.cbt.bean.*;
import com.cbt.bean.OrderBean;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.BuyerCommentPojo;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.processes.servlet.Currency;
import com.cbt.service.CustomGoodsService;
import com.cbt.warehouse.dao.IWarehouseDao;
import com.cbt.warehouse.pojo.*;
import com.cbt.warehouse.pojo.ClassDiscount;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.Utility;
import com.cbt.website.bean.*;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SearchFileUtils;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONArray;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WarehouseServiceImpl implements IWarehouseService {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(WarehouseServiceImpl.class);
    @Autowired
    private IWarehouseDao dao;
    @Autowired
    private FreightFeeSerive freightFeeSerive;
    @Autowired
    private CustomGoodsService customGoodsService;

    @Override
    public outIdBean findOutId(Integer uid) {

        return dao.findOutId(uid);
    }



    @Override
    public SearchResultInfo getWeight() {
        return dao.getWeight();
    }

    @Override
    public int saveWeight(Map<String, String> map) {
        return dao.saveWeight(map);
    }

    //result 0-处理异常;2-pid数据问题;1-同步到产品库成功;3-未找到重量数据;4-已经同步到产品库过;
    @Override
    public int saveWeightFlag(String pid) {
        // 查询已保存的实秤重量
        SearchResultInfo weightAndSyn = dao.getGoodsWeight(pid);
        if (null == weightAndSyn){
            return 3;
        }
        if (weightAndSyn.getSyn() == 1){
            return 4;
        }
        customGoodsService.setGoodsWeightByWeigher(pid, weightAndSyn.getWeight()); //蒋先伟同步重量到产品库接口
        dao.updateGoodsWeightFlag(pid);
        return 1;
    }

    @Override
    public int updateQuestPicPath(String id, String path) {
        return dao.updateQuestPicPath(id,path);
    }

    @Override
    public List<SampleGoodsBean> getSampleGoods(String [] pids) {
        return dao.getSampleGoods(pids);
    }

    @Override
    public List<SampleGoodsBean> getLiSameGoods(String[] pids, String [] shopIds, String [] pIds) {
        return dao.getLiSameGoods(pids,shopIds,pIds);
    }

    @Override
    public List<SampleGoodsBean> getRecommdedSameGoods(Map<String, String> map) {
        return dao.getRecommdedSameGoods(map);
    }

    @Override
    public String getAllOrderPid(Map<String, String> map) {
        return dao.getAllOrderPid(map);
    }

    @Override
    public int refundGoods(Map<String, String> map) {
        int row=0;
        boolean flag=true;
        //判断该商品是否有取消订单产生库存
        Inventory i=dao.checkGoodsInvengory(map);
        if(i != null){
            //该商品有库存数据
            OrderDetailsBean od=dao.getOrderDetails(map);
            if(od != null && od.getYourorder()>0){
                int yourorder=od.getYourorder();
                if(i.getFlag() == 1 && Integer.valueOf(i.getNew_remaining())<yourorder){
                    flag=false;
                }else if(i.getFlag() == 0 && Integer.valueOf(i.getRemaining())<yourorder){
                    flag=false;
                }
                if(flag){
                    //库存减少操作
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
                    row=dao.updateInventory(map);
                   dao.updateIsRefund(map);
                }
            }
        }
        return row;
    }

    @Override
    public int saveQualityData(Map<String, String> map) {
        int row=dao.saveQualityData(map);
        if(row>0){
            row=dao.queryQeId(map);
            map.put("qid",String.valueOf(row));
            dao.saveQualityDataLog(map);
        }
        return row;
    }

    @Override
    public Map<String, String> getExchange(Map<String, String> map) {
        return dao.getExchange(map);
    }

    @Override
    public OrderInfoCountPojo getPresentationsData(Map<String, String> map) {
        return dao.getPresentationsData(map);
    }

    @Override
    public OrderInfoCountPojo getOpenCountData(Map<String, String> map) {
        return dao.getOpenCountData(map);
    }

    @Override
    public OrderInfoCountPojo getAddCarData(Map<String, String> map) {
        return dao.getAddCarData(map);
    }

    @Override
    public OrderInfoCountPojo getGoodsBuyData(Map<String, String> map) {
        return dao.getGoodsBuyData(map);
    }

    @Override
    public OrderInfoCountPojo getGoodsSalesAmountData(Map<String, String> map) {
        return dao.getGoodsSalesAmountData(map);
    }

    @Override
    public OrderInfoCountPojo getCancelData(Map<String, String> map) {
        return dao.getCancelData(map);
    }

    @Override
    public int updateQualityData(Map<String, String> map) {
        int row=dao.updateQualityData(map);
        if(row>0){
            row=dao.queryQeId(map);
            map.put("qid",String.valueOf(row));
            dao.saveQualityDataLog(map);
        }
        return row;
    }

    @Override
    public String getQualityEvaluation(Map<String, String> map) {
        return dao.getQualityEvaluation(map);
    }

    @Override
    public int delInPic(Map<String, String> map) {
        return dao.delInPic(map);
    }

    @Override
    public int insertInspectionPicture(String pid, String picPath) {
        return dao.insertInspectionPicture(pid,picPath);
    }

    @Override
    public int disabled(Map<String, String> map) {
        return dao.disabled(map);
    }

    @Override
    public int insertEvaluation(Map<String, String> map) {
        int row=0;
        try{
            SendMQ sendMQ=new SendMQ();
            String id=dao.queryQevId(map);
            if(StringUtil.isBlank(id)){
                row=dao.insertEvaluation(map);
                sendMQ.sendMsg(new RunSqlModel("insert into goods_evaluation (goods_pid,evaluation,createtime) values('"+map.get("goods_pid")+"','"+map.get("evaluation")+"',now())"));
                id=dao.queryQevId(map);
            }else{
                row=dao.updateEvaluation(map);
                sendMQ.sendMsg(new RunSqlModel("update goods_evaluation set evaluation='"+map.get("evaluation")+"' where goods_pid='"+map.get("goods_pid")+"'"));
            }
            map.put("ge_id",id);
            dao.insertEvaluationLog(map);
            sendMQ.closeConn();
        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public int insInsp(Map<String, String> map) {
	    int checked=0;
    	try{
		    checked=dao.checnInspIsExit(map);
		    if(checked<=0){
			    checked=dao.insertInspPath(map);
		    }else{
			    checked=0;
		    }
		    if(checked>0){
			    SendMQ sendMQ = new SendMQ();
			    sendMQ.sendMsg(new RunSqlModel(" insert into inspection_picture(pid,orderid,pic_path,createtime,odid) values('"+map.get("goodsPid")+"','"+map.get("orderid")+"','"+map.get("picPath")+"',now(),'"+map.get("odid")+"')"));
			    sendMQ.closeConn();
		    }
	    }catch (Exception e){
    		e.printStackTrace();
	    }
        return checked;
    }

    @Override
    public OrderAddress getAddressByOrderID(String orderNo) {

        return dao.getAddressByOrderID(orderNo);
    }

    @Override
    public List<PurchaseDetailsBean> getPurchaseDetails(String orderNo) {

        return dao.getPurchaseDetails(orderNo);
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
        return dao.getOutByID(userid,sql);
    }

    @Override
    public void callResult(Map<String, String> param) {

        dao.callResult(param);
    }

    @Override
    public void callUpdateIdrelationtable(Map<String, String> param) {

        dao.callUpdateIdrelationtable(param);
    }

    @Override
    public void insertStorage_location(String a, String b, String c, String d,
                                       String e) {

        dao.insertStorage_location(a, b, c, d, e);

    }

    @Override
    public List<StorageLocationBean> getAllStorageLocationByPage(int startNum,int endNum) {

        return dao.getAllStorageLocationByPage(startNum,endNum);
    }

    @Override
    public List<StorageLocationBean> getOrderinfoPage(Map<String, Object> map) {
        List<StorageLocationBean> list=dao.getOrderinfoPage(map);
        for (int i = 0; i < list.size(); i++) {
            StorageLocationBean s = list.get(i);
            if (s.getChecked() == 0 && "1".equals(s.getGoodstatus())) {
                s.setGoodstatus("已到仓库,未校验");
            } else if ("2".equals(s.getGoodstatus())) {
                s.setGoodstatus("已到仓库，已校验该到没到");
            } else if ("3".equals(s.getGoodstatus())) {
                s.setGoodstatus("已到仓库，已校验有破损");
            } else if ("4".equals(s.getGoodstatus())) {
                s.setGoodstatus("已到仓库，已校验有疑问");
            } else if ("5".equals(s.getGoodstatus())) {
                s.setGoodstatus("已到仓库，已校验数量有误");
            } else {
                s.setGoodstatus("已到仓库，验货无误");
            }
            List<String> picList=new ArrayList<String>();
            String pics=s.getPicturepath();
            if(StringUtil.isNotBlank(pics) && pics.contains(",")){
                //多张验货图片
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
            pics="https://img.import-express.com/importcsvimg/inspectionImg/"+pics+"";
//            pics="http://27.115.38.42:8084/"+pics+"";
        }else{
//            pics="http://192.168.1.34:8085/"+pics+"";
            pics="https://img.import-express.com/importcsvimg/inspectionImg/"+pics+"";

        }
        return pics;
    }

    @Override
    public int getCountOrderinfo(Map<String, Object> map) {

        return dao.getCountOrderinfo(map);
    }

    @Override
    /**
     * 查询采购 所有昨天确认采购的订单
     * @param map
     * @return   采购昨天确认采购的订单数量集合
     * whj 2017-05-11
     */
    public List<String> getsourceValidation(Map<String, Object> map) {

        return dao.getsourceValidation(map);
    }

    @Override
    /**
     * 获取采购账号
     */
    public List<String> getBuyerName(String admuserid) {

        return dao.getBuyerName(admuserid);
    }

    @Override
    /**
     * 获取采购账号
     */
    public String getBuyerNames(String admuserid) {

        return dao.getBuyerNames(admuserid);
    }

    @Override
    /**
     * 查询采购 所有昨天实际采购的订单
     * @param map
     * @return   采购昨天确认实际采购的订单数量集合
     * whj 2017-05-11
     */
    public List<String> getsourceValidationForBuy(Map<String, Object> map) {

        return dao.getsourceValidationForBuy(map);
    }

    @Override
    public List<StorageLocationBean> getOrderInfoInspection(Map<String, Object> map,String ip) {
        List<StorageLocationBean> newlist1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> newlist2 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list2 = new ArrayList<StorageLocationBean>();
        list1 = dao.getOrderInfoInspection(map);
        if(list1.size()>0){
            List<String>  orderList = dao.checkOrders(list1,0);
            for(StorageLocationBean bean:list1){
                if(!orderList.contains(bean.getOrder_no())){
                    newlist1.add(bean);
                }
            }
        }
        //获取dropship信息
        list2 = dao.getDropShipOrderInfoInspection(map);
        for(StorageLocationBean bean:list2){
            StringBuffer stringBuffer = new StringBuffer(bean.getUser_id());
            bean.setUser_id(stringBuffer.insert(1, "D").toString());
        }
        if(list2.size()>0){
            List<String>  dropOrderList = dao.checkOrders(list2,1);
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
    public List<StorageLocationBean> getOrderInfoInspectionall(Map<String, Object> map,String ip) {
        List<StorageLocationBean> newlist1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> newlist2 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list1 = new ArrayList<StorageLocationBean>();
        List<StorageLocationBean> list2 = new ArrayList<StorageLocationBean>();
        list1 = dao.getOrderInfoInspectionall(map);
        if(list1.size()>0){
            List<String>  orderList = dao.checkOrders(list1,0);
            for(StorageLocationBean bean:list1){
                if(!orderList.contains(bean.getOrder_no())){
                    newlist1.add(bean);
                }
            }
        }
        //获取dropship信息
        list2 = dao.getDropShipOrderInfoInspectionall(map);
        for(StorageLocationBean bean:list2){
            StringBuffer stringBuffer = new StringBuffer(bean.getUser_id());
            bean.setUser_id(stringBuffer.insert(1, "D").toString());
        }
        if(list2.size()>0){
            List<String>  dropOrderList = dao.checkOrders(list2,1);
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

        int count = dao.getCountOrderInfoInspection(map);
        count +=dao.getCountDropShipOrderInfoInspection(map);
        return count;
    }

    @Override
    public int insertOrderfeeFromOrderInfo(Map<String, Object> map,int dropshipFlag) {
        map.put("dropshipFlag",dropshipFlag);
        return dao.insertOrderfeeFromOrderInfo(map);
    }

    @Override
    public String getOrderidAddress(Map<String, Object> map) {

        return dao.getOrderidAddress(map);
    }

    @Override
    public List<OrderInfoPrint> getOrderidPrinInfo(Map<String, Object> map) {

        List<OrderInfoPrint> list1 = dao.getOrderidPrinInfo(map);
        for(OrderInfoPrint bean:list1){
            bean.setIsDropshipFlag(0);
        }
        List<OrderInfoPrint> list2 = dao.getDropshipOrderidPrinInfo(map);
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

        return dao.delteFromOrderFeeByOrderid(map);
    }

    @Override
    public List<Forwarder> getForwarder(Map<String, Object> map) {

        return dao.getForwarder(map);
    }

    @Override
    public int getCountForwarder(Map<String, Object> map) {

        return dao.getCountForwarder(map);
    }

    @Override
    public List<Logisticsinfo> getlogisticsidAndState(Map<String, Object> map) {

        return dao.getlogisticsidAndState(map);
    }

    @Override
    public String getOrderCreateTime(Map<String, Object> map,int dropshipFlag) {
        map.put("dropshipFlag",dropshipFlag);
        return dao.getOrderCreateTime(map);
    }

	@Override
	public int getUndeliveredOrder(String orderId) {
    	int remark=0;
		remark=dao.getUndeliveredOrder(orderId);
		return remark;
	}

	@Override
    public Logisticsinfo getlogisticsinfo(Map<String, Object> map) {

        return dao.getlogisticsinfo(map);
    }
    /**
     * 手动录入退样运单号
     */
    @Override
    public int refundShipnoEntry(Map<String, Object> map) {
        int row=dao.refundShipnoEntry(map);
        if(row>0){
            //标记商品在采购状态为退货标识
            dao.updateRefundFlag(map);
        }
        return row;
    }
    @Override
    public List<String> getRefundGoodsPid(String goodsid, String orderid) {

        return dao.getRefundGoodsPid(goodsid,orderid);
    }
    @Override
    public String getState(Map<String, Object> map) {

        return dao.getState(map);
    }
    @Override
    public int getStoragCount(Map<String, Object> map) {

        return dao.getStoragCount(map);
    }

    @Override
    public int updateInventoryCount(Map<String, Object> map) {
        int row=dao.updateInventoryCount(map);
        return row;
    }

    @Override
    public List<OrderProductSurcePojo> getlogisticsAndOrderProductSurc(
            Map<String, Object> map) {

        return dao.getlogisticsAndOrderProductSurc(map);
    }

    @Override
    public List<OrderInfoPrint> getIdrelationtable(Map<String, Object> map) {

        return dao.getIdrelationtable(map);
    }

    @Override
    public int getCountIdrelationtable(Map<String, Object> map) {

        return dao.getCountIdrelationtable(map);
    }

    @Override
    public int updateFromOrderFeeByOrderid(Map<String, Object> map) {

        return dao.updateFromOrderFeeByOrderid(map);
    }

    @Override
    public int updateExperssNo(Map<String, Object> map) {

        return dao.updateExperssNo(map);
    }

    @Override
    public List<OrderFeePojo> getOrderFee(Map<String, Object> map) {

        return dao.getOrderFee(map);
    }

    @Override
    public OrderInfoPojo getOutOrderInfo(Map<String, Object> map) {

        return dao.getOutOrderInfo(map);
    }


    @Override
    public OrderDetailsBean queryVideo(Map<String, String> map) {
        return dao.queryVideo(map);
    }

    @Override
    public int updateCustomVideoUrl(Map<String, String> map) {
        return dao.updateCustomVideoUrl(map);
    }

    @Override
    public List<SearchResultInfo> queryPictureInfos(Map<String, String> map) {
        List<SearchResultInfo> list=dao.queryPictureInfos(map);
        for (SearchResultInfo s : list) {
            s.setCurrency("<a title='查看产品单页信息' target='_blank' href='https://www.import-express.com/goodsinfo/cbtconsole-1"+s.getOrderid()+".html'><img src='"+(s.getInvoice()+s.getCurrency())+"' style='width:220px;'></img></a>");
            StringBuilder sb=new StringBuilder(3);
            List<SearchResultInfo> ps=dao.getPicturePath(s.getOrderid());
            int index=1;
            for(int i=0;i<ps.size();i++){
                SearchResultInfo p=ps.get(i);
                String picture=p.getGoods_url();
                if(!picture.contains("http")){
                    picture="http://img.import-express.com/importcsvimg/inspectionImg/"+picture;
                }
                sb.append("<div class='div_box'><img src='"+picture+"' class='pic_01' onmouseout='closeBigImg();'" +
                        " onmouseover=BigImg(\""+picture+"\") style='width:220px;'></img>");
                sb.append("<br><div class='div_btn'><button onclick=\"uploadInPic("
                        + p.getGoods_p_url() + ",\'" + p.getGoods_img_url()
                        + "\',\'" + s.getOrderid()
                        + "\',\'" + picture
                        + "\',"+p.getCurrency()+")\">图片更换</button>");
                sb.append("<button onclick=\"delInPic("+p.getGoods_p_url()+",\'"+p.getGoods_img_url()+"\',\'"+s.getOrderid()+"\',\'"+picture+"\',"+p.getCurrency()+")\">图片删除</button>");
                if(p.getUserid()==0){
                    sb.append("<button style='color:red' onclick=\"disabledPic("+p.getCurrency()+",1,\'"+picture+"\')\">停用</button>");
                    if(StringUtil.isNotBlank(map.get("odid"))){
                        sb.append("<button style='color:red' onclick=\"insInsp("+p.getOrderid()+","+map.get("odid")+",\'"+picture+"\',\'"+map.get("oldOrderid")+"\')\">关联</button>");
                    }
                    if(index<=3){
                        sb.append("<span style='color:red'>展示中...</span>");
                        index++;
                    }
                    sb.append("</div></div>");
                }else{
                    sb.append("<button style='color:green' onclick=\"disabledPic("+p.getCurrency()+",0,\'"+picture+"\')\">启用</button>");
                    if(StringUtil.isNotBlank(map.get("odid"))){
                        sb.append("<button style='color:red' onclick=\"insInsp("+p.getOrderid()+","+map.get("odid")+",\'"+picture+"\',\'"+map.get("oldOrderid")+"\')\">关联</button>");
                    }
                    sb.append("</div></div>");
                }
            }
            s.setGcUnit(sb.toString());
            //|<button onclick=\"showEvaluation("+s.getOrderid()+")\">前台展示</button>fdgdf
            s.setPosition("<button onclick=\"uploadPics("+s.getOrderid()+")\">上传新图片</button>|<button onclick=\"openEvaluation("+s.getOrderid()+")\">产品评论</button>");
            String valid=s.getValid();
            if("0".equals(valid)){
                valid="下架";
            }else if("1".equals(valid)){
                valid="上架";
            }else if("2".equals(valid)){
                valid="软下架";
            }else{
                valid="其他";
            }
            s.setValid(valid);
        }
        return list;
    }

    @Override
    public int queryPictureInfosCount(Map<String, String> map) {

        return dao.queryPictureInfosCount(map);
    }

    @Override
    public List<OrderFeePojo> getFpxCountryCode() {

        return dao.getFpxCountryCode();
    }

    @Override
    public List<OrderInfoPojo> getFpxProductCode() {

        return dao.getFpxProductCode();
    }

    @Override
    public int updateOrderFeeByOrderid(Map<String, Object> map) {

        return dao.updateOrderFeeByOrderid(map);
    }

    @Override
    public List<OrderFeePojo> getCodemaster() {

        return dao.getCodemaster();
    }

    @Override
    public List<OrderInfoPojo> getOutCount() {

        return dao.getOutCount();
    }

    @Override
    public OrderInfoPojo getPaymentFy(Map<String, Object> map) {

        return dao.getPaymentFy(map);
    }

    @Override
    public String getOrderProblem(Map<String, Object> map) {

        return dao.getOrderProblem(map);
    }

    @Override
    public int updateOrderinfoAll(Map<String, Object> map) {

        return dao.updateOrderinfoAll(map);
    }

    @Override
    public int updateOrderinfo(Map<String, Object> map) {

        return dao.updateOrderinfo(map);
    }

    @Override
    public List<OrderInfoPojo> getNotMoneyOrderinfo(Map<String, Object> map) {

        return dao.getNotMoneyOrderinfo(map);
    }

    @Override
    public List<OrderFeePojo> getOrderfeeFreight(Map<String, Object> map, HttpServletRequest request) {
        List<OrderFeePojo> list=dao.getOrderfeeFreight(map);
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

        return dao.getOrderfeeFreightCount(map);
    }

    @Override
    public AdmuserPojo getAdmuserSendMailInfo(Map<String, Object> map) {

        return dao.getAdmuserSendMailInfo(map);
    }

    @Override
    public OrderInfoCountPojo getOrderInfoCountByState(Map<String, Object> map) {

        return dao.getOrderInfoCountByState(map);
    }

    @Override
    public OrderInfoCountPojo getOrderInfoCountNoitemid(Map<String, Object> map) {
        return dao.getOrderInfoCountNoitemid(map);
    }
    @Override
    public OrderInfoCountPojo getNoMatchOrderByTbShipno(Map<String, Object> map) {

        return dao.getNoMatchOrderByTbShipno(map);
    }

    @Override
    public List<String> getNoShipInfoOrder(Map<String, String> map) {
        return dao.getNoShipInfoOrder(map);
    }

    @Override
    public List<PurchasesBean> getOrderInfoCountItemid(Map<String, Object> map) {

        return dao.getOrderInfoCountItemid(map);
    }

    @Override
    public List<TaoBaoOrderInfo> getBuyReturnManage(String goodsid,int page,String state,String startTime,String endTime) {
        List<TaoBaoOrderInfo> list=dao.getBuyReturnManage(goodsid,page,state,startTime,endTime);
        for(TaoBaoOrderInfo t:list){
            t.setItemname("<a target='_blank' href='"+t.getItemurl()+"'>"+(t.getItemname().substring(0,t.getItemname().length()/3))+"</a>");
            t.setImgurl("<img style='width:100px;height:100px;' src='"+t.getImgurl()+"'></img>");
            if("0".equals(t.getbId())){
                t.setState("已标记整单退货");
            }else if(StringUtil.isNotBlank(t.getbId()) && !"0".equals(t.getbId())){
                t.setState("已标记该商品退货");
            }else{
                t.setState("未退货");
                t.setOperating("<button  onclick='returns(0,\""+t.getOrderid()+"\")'>整单退货</button><button  onclick='returns("+t.getId()+",\""+t.getOrderid()+"\")'>部分退货</button>");
            }
        }
        return list;
    }

    @Override
    public int getBuyReturnManageCount(String goodsid,String state,String startTime,String endTime) {
        return dao.getBuyReturnManageCount(goodsid,state,startTime,endTime);
    }

    @Override
    public List<UserInfo> getUserInfoForPrice(Map<String, Object> map) {
        List<UserInfo> userInfos=dao.getUserInfoForPrice(map);
        DecimalFormat df = new DecimalFormat("0.00");
        List<ConfirmUserInfo> list = dao.getAllAdmuser();
        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo = userInfos.get(i);
            StringBuffer admuser = new StringBuffer();
            admuser.append("<select id='admuser_" + userInfo.getUserid()
                    + "'><option value='0'>未分配</option>");
            if (!StringUtils.isStrNull(userInfo.getPass())) {
                userInfo.setLoginStyle("网站登录");
            } else if (StringUtils.isStrNull(userInfo.getPass())
                    && !StringUtils.isStrNull(userInfo.getBind_google())) {
                userInfo.setLoginStyle("google登录");
            } else {
                userInfo.setLoginStyle("facebook登录");
            }
            userInfo.setUserLogin("<button onclick=\"userlogin("
                    + userInfo.getUserid() + ",\'" + userInfo.getUserName()
                    + "\',\'" + userInfo.getCurrency()
                    + "\')\">模拟用户登录接口</button>");
            userInfo.setUserManager("<button onclick=\"useraddress("
                    + userInfo.getUserid() + ",\'" + userInfo.getUserName()
                    + "\',\'" + userInfo.getCurrency()
                    + "\')\">用户管理地址</button>");
            userInfo.setOperation("<button onclick=\"upEmail("
                    + userInfo.getUserid()
                    + ",'"
                    + userInfo.getEmail()
                    + "')\">修改邮箱</button><button id='upphone' onclick=\"upPhone("
                    + userInfo.getUserid()
                    + ",'"
                    + (StringUtils.isStrNull(userInfo.getOtherphone()) ? ""
                    : userInfo.getOtherphone())
                    + "')\">修改手机号</button><button onclick=\"changeavailable("
                    + userInfo.getUserid()
                    + ",'"
                    + userInfo.getAvailable()
                    + "')\">修改余额</button><button id='but2' onclick='fnsetDropshipUser()'>设置为Drop ship客户</button>");
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
            admuser.append("</select><input id=\"confirm\" type=\"button\" value=\"确认\" onclick=\"addUser("
                    + userInfo.getUserid()
                    + ",'"
                    + userInfo.getEmail()
                    + "','"
                    + userInfo.getUserName()
                    + "','"
                    + userInfo.getAdminname()
                    + "')\">");
            userInfo.setAdmuser(admuser.toString());
            double goods_price=0.00;
            if(!StringUtils.isStrNull(userInfo.getCar_info()) && userInfo.getCar_info().length()>10){
                List<GoodsCarActiveBean> list_active  =  (List<GoodsCarActiveBean>) net.sf.json.JSONArray.toCollection(JSONArray.fromObject(userInfo.getCar_info()),GoodsCarActiveBean.class);
                System.out.println("list_active=="+list_active.toString());
                for (GoodsCarActiveBean g : list_active) {
                    goods_price+=g.getNumber()*Double.valueOf(g.getPrice());
                }
            }
            userInfo.setGoodsPriceUrl("<a href=javascript:toShopCar("
                    + userInfo.getUserid()
                    + ",\'"
                    + userInfo.getUserName()
                    + "\',\'"
                    + userInfo.getCurrency()
                    + "\')>"
                    + String.format("%.2f", goods_price) + "</a>");
            String gname = dao.getGname(userInfo.getGid());
            userInfo.setGrade(gname);
            if (StringUtils.isStrNull(userInfo.getBusinessName())) {
                userInfo.setBusinessName("无");
            }
        }
        return userInfos;
    }
    @Override
    public List<ShopManagerPojo> getShopManagerList(Map<String, Object> map) {
        List<ShopManagerPojo> list=dao.getShopManagerList(map);
        for (ShopManagerPojo s : list) {
            s.setShop_id("<a href='/cbtconsole/website/shop_manager_details.jsp?id=" + s.getId() + "&status="+s.getRemark()+"' target='_blank'>"+ s.getShop_id() + "</a>");
            if ("0".equals(s.getRemark())) {
                s.setRemark("自动禁用");
            } else if ("1".equals(s.getRemark())) {
                s.setRemark("自动全免");
            } else if ("2".equals(s.getRemark())) {
                s.setRemark("人工解禁");
            } else if ("3".equals(s.getRemark())) {
                s.setRemark("人工全免");
            } else if ("4".equals(s.getRemark())) {
                s.setRemark("系统无法判断");
            } else {
                s.setRemark("未检测");
            }
            if ("0".equals(s.getFlag())) {
                s.setFlag("未完成");
            } else if ("1".equals(s.getFlag())) {
                s.setFlag("已完成");
            }
            if ("0".equals(s.getSystem_evaluation())) {
                s.setSystem_evaluation("未判断");
            } else if ("1".equals(s.getSystem_evaluation())) {
                s.setSystem_evaluation("已判断");
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
                    + ",this.value);'><option value='-1'>全部</option><option value='2'>人工解禁</option><option value='3'>人工全免</option></select>");
        }
        return list;
    }
    @Override
    public int getShopManagerListCount(Map<String, Object> map) {

        return dao.getShopManagerListCount(map);
    }
    @Override
    public List<RefundSamplePojo> searchRefundSample(Map<String, Object> map) {
        StringBuilder sb=new StringBuilder();
        DecimalFormat    df   = new DecimalFormat("######0.00");
        List<RefundSamplePojo> list=dao.searchRefundSample(map);
        StringBuilder pids=new StringBuilder();
        for (RefundSamplePojo r : list) {
            String state="建议退货";
            String style="";
            if("1".equals(r.getState())){
                state="退货中";
                style="none";
            }
            r.setImg("<img src='"+r.getImg()+"' style='width:50px;height:50px'>");
            r.setGoods_name("<a href='"+r.getGoods_p_url()+"' target='_blank'>"+(StringUtils.isStrNull(r.getGoods_p_name())?r.getGoods_name().substring(0,r.getGoods_name().length()/2):r.getGoods_name().substring(0,r.getGoods_name().length()/2))+"</a>");
            r.setOperating("<button style='display:"+style+"' onclick='nntryShipno("+r.getItemqty()+","+r.getFlag()+","+(StringUtils.isStrNull(r.getIn_id())?"0000":r.getIn_id())+","+r.getT_id()+",\""+r.getT_orderid()+"\",\""+r.getOd_orderid()+"\",\""+r.getGoodsid()+"\")'>退货</button>");
            sb.append("'").append(r.getGoods_pid()).append("',");
            r.setState(state);
            pids.append("'").append(r.getGoods_pid()).append("',");
        }
//        if(list.size()>0){
//            DataSourceSelector.set("dataSource28hop");
//            Map<String,String> map3=new HashMap<String, String>();
//            Map<String,String> map4=new HashMap<String, String>();
//            Map<String,String> map5=new HashMap<String, String>();
//            List<Map<String, String>> map1=dao.getShopIdForPid(sb.toString().substring(0,sb.toString().length()-1));
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
        List<RefundSamplePojo> list=dao.searchRefundSampleCount(map);
        return list;
    }
    @Override
    public List<TaoBaoOrderInfo> searchRefundOrder(Map<String, Object> map) {
        List<TaoBaoOrderInfo> list=dao.searchRefundOrder(map);
        for (TaoBaoOrderInfo t : list) {
            //当退货数量大于采购数量时，屏蔽整单退货按钮
            if(Integer.valueOf(t.getCounts())>=Integer.valueOf(t.getItemqty())){
                t.setOperating("<a title='点击查看该采购订单下的所有商品' target='_blank' href='/cbtconsole/website/refundSample.jsp?orderid="+t.getOrderid()+"'>查看详情</a>");
            }else{
                t.setOperating("<a title='点击查看该采购订单下的所有商品' target='_blank' href='/cbtconsole/website/refundSample.jsp?orderid="+t.getOrderid()+"'>查看详情</a>||<a href='javascript:openRefund(\""+t.getOrderid()+"\");'>整单退货</a>");
            }
            t.setOrderid("<a title='跳转到1688订单详情页面' href='https://trade.1688.com/order/new_step_order_detail.htm?orderId="+t.getOrderid()+"' target='_blank'>"+t.getOrderid()+"</a>");
        }
        return list;
    }
    @Override
    public List<TaoBaoOrderInfo> searchRefundOrderCount(Map<String, Object> map) {

        return dao.searchRefundOrderCount(map);
    }

    @Override
    public List<TaoBaoOrderInfo> searchMonthlyRefund(Map<String, Object> map) {

        return dao.searchMonthlyRefund(map);
    }
    @Override
    public List<TaoBaoOrderInfo> searchMonthlyRefundCount(
            Map<String, Object> map) {

        return dao.searchMonthlyRefundCount(map);
    }

    @Override
    public List<ShopManagerPojo> getPriorityCategory(Map<String, Object> map) {
        List<ShopManagerPojo> list=dao.getPriorityCategory(map);
        for(ShopManagerPojo s:list){
            if("1".equals(s.getStatus())){
                s.setStatus("<span style='color:red'>停用</span>");
                s.setRemark("<button onclick=\"updateState("+s.getId()+",0)\" style='background-color:green'>启用搜索词优先类别</button>|<button onclick=\"openEditKeyword("+s.getId()+",'"+s.getCategory()+"')\">修改搜索词优先类别</button>");
            }else{
                s.setStatus("<span style='color:green'>启用</span>");
                s.setRemark("<button onclick=\"updateState("+s.getId()+",1)\" style='background-color:red'>停用搜索词优先类别</button>|<button onclick=\"openEditKeyword("+s.getId()+",'"+s.getCategory()+"')\">修改搜索词优先类别</button>");
            }
            s.setMinPrice("<input type='text' id='"+s.getId()+"_min'  title='按回车修改价格' onkeyup='if(event.keyCode==13){updatePrice(0,"+s.getId()+")}' value='"+(StringUtil.isBlank(s.getMinPrice())?"-":s.getMinPrice())+"'>");
//            s.setMaxPrice("<input type='text' id='"+s.getId()+"_max' title='按回车修改价格' onkeyup='if(event.keyCode==13){updatePrice(1,"+s.getId()+")}' value='"+(StringUtil.isBlank(s.getMaxPrice())?"-":s.getMaxPrice())+"'>");
            String enName=s.getEnName();
            String category=s.getCategory();
            if(StringUtil.isBlank(enName) && StringUtil.isNotBlank(category)){
                String names=dao.getEnName(category);
                s.setEnName(names);
            }
        }
        return list;
    }

    @Override
    public int getPriorityCategoryCount(Map<String, Object> map) {
        return dao.getPriorityCategoryCount(map);
    }

    @Override
    public List<ShopManagerPojo> getShopBuyLogInfo(Map<String, String> map) {
        List<ShopManagerPojo> list=dao.getShopBuyLogInfo(map);
        List<ShopManagerPojo> shopSupplier=dao.getShopSupplier(map);
        for(ShopManagerPojo s:list){
            s.setShopId("<a target='_blank' href='/cbtconsole/supplierscoring/supplierproducts?shop_id="+s.getShopId()+"'>"+s.getShopId()+"</a>");
        }
        ShopManagerPojo s=new ShopManagerPojo();
        s.setShopId("<span style='color:red'>店铺ID</span>");
        s.setOrderdate("<span style='color:red'>评论时间</span>");
        s.setTotalprice("<span style='color:red'>评论备注</span>");
        s.setOrderid("<span style='color:red'>质量评分</span>");
        s.setUsername("<span style='color:red'>评价人</span>");
        list.add(s);
        list.addAll(shopSupplier);
        return list;
    }

    @Override
    public int getShopBuyLogInfoCount(Map<String, String> map) {
        return dao.getShopBuyLogInfoCount(map);
    }

    @Override
    public List<BlackList> getUserBackList(Map<String, String> map) {
        List<BlackList> list=dao.getUserBackList(map);
        for(BlackList b:list){
            StringBuilder op=new StringBuilder();
            String flag=b.getFlag();
            if("0".equals(flag)){
                flag="<span style='color:green'>使用中</span>";
                op.append("<button style='color:red' onclick=\"updateFlag("+b.getId()+",1)\">停用</button>");
            }else if("1".equals(flag)){
                flag="<span style='color:red'>停用</span>";
                op.append("<button style='color:green' onclick=\"updateFlag("+b.getId()+",0)\">使用</button>");
            }
            op.append("|<button onclick=\"updateEmail("+b.getId()+",'"+b.getEmail()+"')\">修改</button>");
            b.setOption(op.toString());
            b.setFlag(flag);
        }
        return list;
    }

    @Override
    public List<BlackList> getUserBackListCount(Map<String, String> map) {
        return dao.getUserBackListCount(map);
    }

    @Override
    public List<ShopManagerPojo> getShopManagerDetailsList(Map<String, Object> map) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        List<ShopManagerPojo> list=dao.getShopManagerDetailsList(map);
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
            s.setOperation("<select   onchange=\"updateState(this.value,1,'"+s.getGoods_pid()+"')\"><option value='-1'>全部</option><option value='0'>不可用</option><option value='1'>可用</option><option value='2'>无详情</option><option value='3'>全长图</option></select>");
            if("0".equals(s.getStatus())){
                s.setStatus("不可用");
            }else if("1".equals(s.getStatus())){
                s.setStatus("可用");
            }else if("2".equals(s.getStatus())){
                s.setStatus("无详情");
            }else if("3".equals(s.getStatus())){
                s.setStatus("全长图");
            }else{
                s.setStatus("未知");
            }
        }
        return list;
    }
    @Override
    public List<com.cbt.pojo.AdmuserPojo> getAllBuyer(int id) {

        return dao.getAllBuyer(id);
    }

    @Override
    public int addSampleRemark(Map<String, Object> map) {

        return dao.addSampleRemark(map);
    }
    @Override
    public int deleteSource(Map<String,Object> map) {

        return dao.deleteSource(map);
    }

    @Override
    public int updateCatePrice(Map<String, String> map) {
        return dao.updateCatePrice(map);
    }

    @Override
    public int updatePrice(Map<String, Object> map) {

        return dao.updatePrice(map);
    }

    @Override
    public BuyerCommentPojo getBuyerCommentPojo(Map<String, String> map) {
        return dao.getBuyerCommentPojo(map);
    }

    @Override
    public int saveCommentContent(Map<String, String> map) {
        return dao.saveCommentContent(map);
    }

    @Override
    public int insertStorageProblemOrder(Map<String, Object> map) {

        return dao.insertStorageProblemOrder(map);
    }

    @Override
    public int saveClothingData(Map<String, String> map) {

        return dao.saveClothingData(map);
    }

    /**
     * 给人赋能数据统计查询
     */
    @Override
    public List<PurchaseSamplingStatisticsPojo> getPurchaseSamplingStatistics(
            Map<String, Object> map) {
        List<PurchaseSamplingStatisticsPojo> list=new ArrayList<PurchaseSamplingStatisticsPojo>();
        Connection conn28=null,conn5=null,conn=null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            list=dao.getPurchaseSamplingStatistics(map);
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
                p.setBenchmarksCount("<a target='_blank' title='查看对标商品明细' href='/cbtconsole/website/aliBeanchmarkingStatistic.jsp?admName="+p.getAdmName()+"&days="+(StringUtil.isNotBlank(String.valueOf(map.get("days")))?map.get("days"):"1500")+"'>"+counts+"</a>");
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
                p.setOnlineStores("<a target='_blank' href='/cbtconsole/website/input_shop_list.jsp?admName="+p.getAdmName()+"&days="+(StringUtil.isNotBlank(String.valueOf(map.get("days")))?map.get("days"):"1500")+"' title='查看店铺详情'>"+counts+"</a>");
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
        return dao.getAllUser();
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> salesPerformanceCount(Map<String, Object> map) {
        return dao.salesPerformanceCount(map);
    }

	@Override
	public List<PurchaseSamplingStatisticsPojo> salesPerformanDetails(Map<String, String> map) {
		List<PurchaseSamplingStatisticsPojo> list=dao.salesPerformanDetails(map);
		for(PurchaseSamplingStatisticsPojo p:list){
			p.setGoodsname("<a target='_blank' href='https://www.import-express.com/goodsinfo/cbtconsole-1"+p.getGoods_pid()+".html'>"+(p.getGoodsname().substring(0,p.getGoodsname().length()/3))+"...</a>");
			if("0".equals(p.getState())){
				p.setState("未到库");
			}else if("1".equals(p.getState())){
				p.setState("已到库");
			}
			if("0".equals(p.getChecked())){
				p.setChecked("未验货无误");
			}else{
				p.setChecked("验货无误");
			}
			p.setCar_img("<img src='"+p.getCar_img()+"'></img>");
		}
		return list;
	}

	@Override
	public List<PurchaseSamplingStatisticsPojo> salesPerformanDetailsCount(Map<String, String> map) {
		return dao.salesPerformanDetailsCount(map);
	}

	@Override
    public List<PurchaseSamplingStatisticsPojo> weightProblemDetails(Map<String, String> map) {
        List<PurchaseSamplingStatisticsPojo> list=dao.weightProblemDetails(map);
//        0：速卖通或1688重量大于2kg；1：速卖通重量为0或空；2：1688重量为0或空；3：速卖通重量/1688重量>200%；4：1688重量/速卖通重量>200%；
//        5：速卖通有批量 && 速卖通和1688重量相差小于50%(速卖通为分母)；6：原来清洗的重量/反推重量>1.3 && 工厂价>20人民币；7：速卖通和1688重量相差超过30%(速卖通为分母) && 工厂价>20人民币；
//        8：速卖通和1688重量相差超过100%(速卖通为分母) && 工厂价<20人民币；9：1688价格<20元 而 1688起批量>1；10:1688重量与ali重量(1688为分母或ali为分母)偏差30%；
        for(PurchaseSamplingStatisticsPojo p:list){
            if("0".equals(p.getProblem_type())){
                p.setProblem_type("速卖通或1688重量大于2kg");
            }else if("1".equals(p.getProblem_type())){
                p.setProblem_type("速卖通重量为0或空");
            }else if("2".equals(p.getProblem_type())){
                p.setProblem_type("1688重量为0或空");
            }else if("3".equals(p.getProblem_type())){
                p.setProblem_type("速卖通重量/1688重量>200%");
            }else if("4".equals(p.getProblem_type())){
                p.setProblem_type("1688重量/速卖通重量>200%");
            }else if("5".equals(p.getProblem_type())){
                p.setProblem_type("速卖通有批量 && 速卖通和1688重量相差小于50%(速卖通为分母)");
            }else if("6".equals(p.getProblem_type())){
                p.setProblem_type("原来清洗的重量/反推重量>1.3 && 工厂价>20人民币");
            }else if("7".equals(p.getProblem_type())){
                p.setProblem_type("速卖通和1688重量相差超过30%(速卖通为分母) && 工厂价>20人民币");
            }else if("8".equals(p.getProblem_type())){
                p.setProblem_type("速卖通和1688重量相差超过100%(速卖通为分母) && 工厂价<20人民币");
            }else if("9".equals(p.getProblem_type())){
                p.setProblem_type("1688价格<20元 而 1688起批量>1");
            }else if("10".equals(p.getProblem_type())){
                p.setProblem_type("1688重量与ali重量(1688为分母或ali为分母)偏差30%");
            }
        }
        return list;
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> weightProblemDetailsCount(Map<String, String> map) {
        List<PurchaseSamplingStatisticsPojo> list=dao.weightProblemDetailsCount(map);
        return list;
    }

	@Override
	public List<PurchaseSamplingStatisticsPojo> monthSalesEffortsList(Map<String, String> map) {
		List<PurchaseSamplingStatisticsPojo> list=dao.monthSalesEffortsList(map);
		return list;
	}

	@Override
	public List<PurchaseSamplingStatisticsPojo> monthSalesEffortsListCount(Map<String, String> map) {
		List<PurchaseSamplingStatisticsPojo> list=dao.monthSalesEffortsListCount(map);
		return list;
	}

	@Override
    public List<PurchaseSamplingStatisticsPojo> getCleaningQuality(Map<String, String> map) {
        List<PurchaseSamplingStatisticsPojo> list=dao.getCleaningQuality(map);
        int allWeightCount=0;
        int allShelfCount=0;
        for(PurchaseSamplingStatisticsPojo p:list){
            allWeightCount+= Integer.valueOf(p.getWeightProblem());
            allShelfCount+= Integer.valueOf(p.getAutoShelf());
            p.setWeightProblem("<a title='查看详情' target='_blank' href='/cbtconsole/website/weightProblemDetails.jsp?" +
                    "adminId="+p.getAdmin_id()+"&editTime="+(StringUtil.isBlank(map.get("editTime"))?"0":map.get("editTime"))+"&createtime="+(StringUtil.isBlank(map.get("createtime"))?"0":map.get("createtime"))+"'>"+p.getWeightProblem()+"</a>");
        }
        PurchaseSamplingStatisticsPojo ps=new PurchaseSamplingStatisticsPojo();
        ps.setAdmName("<span style='color:red;font-size:25px;'>总计:</span>");
        ps.setWeightProblem("<span style='color:red;font-size:25px;'>"+String.valueOf(allWeightCount)+"</span>");
        ps.setAutoShelf("<span style='color:red;font-size:25px;'>"+String.valueOf(allShelfCount)+"</span>");
        list.add(ps);
        return list;
    }
    @Override
    public List<PurchaseSamplingStatisticsPojo> getCleaningQualityCount(Map<String, Object> map) {
        return dao.getCleaningQualityCount(map);
    }

    @Override
    public List<PurchaseSamplingStatisticsPojo> salesPerformance(Map<String, Object> map) {
        //人为编辑过的产品
        List<PurchaseSamplingStatisticsPojo> list=dao.salesPerformance(map);
        int openCount=0,addCount=0,buyCount=0,cancelCount=0,editCount=0;
        double saleCount=0.00;
        for(PurchaseSamplingStatisticsPojo p:list){
            map.put("admName",p.getAdmName());
            p.setEditCount(dao.getEditCount(map));
            openCount+= Integer.valueOf(StringUtil.isBlank(p.getOpenCount())?"0":p.getOpenCount());
            addCount+= Integer.valueOf(StringUtil.isBlank(p.getAddCount())?"0":p.getAddCount());
            buyCount+= Integer.valueOf(StringUtil.isBlank(p.getBuyCount())?"0":p.getBuyCount());
            cancelCount+= Integer.valueOf(StringUtil.isBlank(p.getCancalCount())?"0":p.getCancelCount());
            editCount+= Integer.valueOf(StringUtil.isBlank(p.getEditCount())?"0":p.getEditCount());
            saleCount+=Double.parseDouble(StringUtil.isBlank(p.getSaleCount())?"0.00":p.getSaleCount());
            p.setBuyCount("<a target='_blank' href='/cbtconsole/website/salesPerformanDetails.jsp?admName="+p.getAdmName()+"&editTime="+map.get("updatetime")+"' title='查看购买商品详情'>"+p.getBuyCount()+"</a>");
//            //产品在搜索结果中被呈现次数
//            p.setPresentations(dao.getPresentations(p.getPid(),p.getUpdatetime()));
//            //产品页面被打开次数
//            p.setOpenCount(dao.getOpenCount(p.getPid(),p.getUpdatetime()));
//            //产品被加购物车次数
//            p.setAddCarCount(dao.getAddCarCount(p.getPid(),p.getUpdatetime()));
//            //产品被购买次数
//            Map<String,String> buyMap=dao.getBuyCount(p.getPid(),p.getUpdatetime());
//            if(buyMap != null && buyMap.get("buyCount") != null){
//                p.setBuyCount(String.valueOf(buyMap.get("buyCount")));
//            }
//            if(buyMap != null && buyMap.get("salesAmount") != null){
//                p.setSalesAmount(String.valueOf(buyMap.get("salesAmount")));
//            }
//            //产品被取消次数
//            p.setCancalCount(dao.getCancelCount(p.getPid(),p.getUpdatetime()));
        }
        PurchaseSamplingStatisticsPojo pp=new PurchaseSamplingStatisticsPojo();
        pp.setAdmName("<span style='color:red'>总计");
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

        return dao.getPurchaseSamplingStatisticsCount(map);
    }
    @Override
    public String getAliPid(String goods_pid) {

        return dao.getAliPid(goods_pid);
    }
    @Override
    public List<Inventory> getHavebarcode() {

        return dao.getHavebarcode();
    }
    @Override
    public int getShopManagerListDetailsCount(Map<String, Object> map) {

        return dao.getShopManagerListDetailsCount(map);
    }
    @Override
    public List<PrePurchasePojo> getPrePurchaseForTB(Map<String, Object> map) {
        List<PrePurchasePojo> list=dao.getPrePurchaseForTB(map);
        for (PrePurchasePojo p : list) {
            p.setStatus("<a target='_blank' href='/cbtconsole/website/purchase_order_details.jsp?orderid="+p.getOrderid()+"&id="+map.get("admuserid")+"'>处理</a>");
        }
        return list;
    }
    @Override
    public List<PrePurchasePojo> getPrePurchaseForTBCount(Map<String, Object> map) {

        return dao.getPrePurchaseForTBCount(map);
    }
    @Override
    public List<PrePurchasePojo> getPrePurchase(Map<String, Object> map) {
        IExpressTrackDao dao1 = new ExpressTrackDaoImpl();
        IOrderwsServer server1 = new OrderwsServer();
        List<PrePurchasePojo> list=dao.getPrePurchase(map);
        for (PrePurchasePojo p : list) {
            int checked=dao.getChecked(p.getOrderid(),map.get("admuserid").toString());//验货无误商品数量
            List<String> problem=dao.getProblem(p.getOrderid(),map.get("admuserid").toString());//验货有误商品数量
            int fp=dao.getFpCount(p.getOrderid(),map.get("admuserid").toString());//该订单分配给采购多少个商品
            int rk=dao.getStorageCount(p.getOrderid(),map.get("admuserid").toString());//该订单分配采购的商品中入库了多少商品
            int cg=dao.getPurchaseCount(p.getOrderid(),map.get("admuserid").toString());//该订单分配给该采购采购了多少商品\
//            int noShipInfo=dao.getNoShipInfo(p.getOrderid(),map.get("admuserid").toString());
            //查询该订单有多少没有物流信息的商品数
            int index_num=0;
            List<PurchasesBean> list_p=dao.getFpOrderDetails(p.getOrderid(),map.get("admuserid").toString());
            index_num = getIndexNum(dao1, server1, p, index_num, list_p);
            if(cg<fp){
                p.setAmount_s("<span style='background-color:aquamarine'>"+fp+"/"+cg+"</span>");
            }else{
                p.setAmount_s("<span style='background-color:yellow'>"+fp+"/"+cg+"</span>");
            }
            getAmounts(map, p, checked, problem, fp, rk, cg, index_num);
            //判断采购订单状态
            getGoodsStatus(p, checked, problem, fp, rk, cg);
            p.setProduct_cost(p.getProduct_cost()+" USD");
	        p.setOption("<a target='_blank' href='/cbtconsole/warehouse/getSampleGoods?orderid="+p.getOrderid()+"'>建议采样</a>");
            int goods_info=dao.getGoodsInfo(p.getOrderid(),map.get("admuserid").toString());
            //拼接显示页面的订单号信息
            getOrderIdInfo(map, p, fp, goods_info);
        }
        return list;
    }

    private void getOrderIdInfo(Map<String, Object> map, PrePurchasePojo p, int fp, int goods_info) {
        if(goods_info>0){
            if(fp==0){
                p.setOrderid("<a  style='color:green;' target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
                        + "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a><img src='/cbtconsole/img/tip1.png' style='margin-left:10px;'></img>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
            }else{
                p.setOrderid("<a  target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
                        + "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a><img src='/cbtconsole/img/tip1.png' style='margin-left:10px;'></img>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
            }
        }else{
            if(fp==0){
                p.setOrderid("<a  target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' style='color:green;' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
                        + "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
            }else{
                p.setOrderid("<a  target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
                        + "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
            }
        }
    }

    private int getIndexNum(IExpressTrackDao dao1, IOrderwsServer server1, PrePurchasePojo p, int index_num, List<PurchasesBean> list_p) {
        for (PurchasesBean purchasesBean : list_p) {
            System.out.println("goodsid="+purchasesBean.getGoodsid());
            if("1043736".equals(purchasesBean.getGoodsid())){
                System.out.println("===");
            }
            String admName = dao1.queryBuyCount(purchasesBean.getConfirm_userid());
            TaoBaoOrderInfo t = server1.getShipStatusInfo(purchasesBean.getTb_1688_itemid(), purchasesBean.getLast_tb_1688_itemid(),
                    purchasesBean.getConfirm_time().substring(0, 10), admName,"",purchasesBean.getOffline_purchase(),p.getOrderid(),purchasesBean.getGoodsid());
            if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {

            }else{
                index_num++;
                System.out.println("goodsid="+purchasesBean.getGoodsid());
            }
        }
        return index_num;
    }

    private void getAmounts(Map<String, Object> map, PrePurchasePojo p, int checked, List<String> problem, int fp, int rk, int cg, int index_num) {
        String numUrl="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid="+("1".equals(map.get("admuserid").toString())  || "83".equals(map.get("admuserid"))?"999":map.get("admuserid").toString())+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&goodname=&goodid=5201315&search_state=0";
        String num=index_num>0?"<a href='"+numUrl+"' target='_blank' title='点击查看确认采购1天后还没有物流信息商品'>"+index_num+"</a>":"0";
        if(problem.size()>0){
            String toUrl="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid="+("1".equals(map.get("admuserid").toString())  || "83".equals(map.get("admuserid"))?"999":map.get("admuserid").toString())+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&goodname=&goodid=5201314&search_state=0";
            p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;background-color:aquamarine'>"+rk+"/"+checked+"/<a href='"+toUrl+"' target='_blank' title='点击查看验货疑问商品'>"+problem.size()+"</a>/"+num+"</span>");
        }else if(cg>=fp && checked<fp){
            p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;background-color:yellow'>"+rk+"/"+checked+"/"+problem.size()+"/"+num+"</span>");
        }else if(checked==fp){
            p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;background-color:#FF00FF'>"+rk+"/"+checked+"/"+problem.size()+"/"+num+"</span>");
        }else{
            p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;'>"+rk+"/"+checked+"/"+problem.size()+"/"+num+"</span>");
        }
    }

    /**
     * 判断采购订单状态
     * @param p
     * @param checked
     * @param problem
     * @param fp
     * @param rk
     * @param cg
     */
    private void getGoodsStatus(PrePurchasePojo p, int checked, List<String> problem, int fp, int rk, int cg) {
        if ("6".equals(p.getStatus()) || "-1".equals(p.getStatus())) {
            p.setStatus("<span style='color:green'>订单取消</span>");
        }else if ("3".equals(p.getStatus())) {
            p.setStatus("<span style='color:crimson'>出运中</span>");
        }else if ("4".equals(p.getStatus())) {
            p.setStatus("<span style='color:green'>完结</span>");
        }else if(fp==0){
            p.setStatus("<span style='color:green'>订单中商品已被取消</span>");
        }else if (cg<=0) {
            p.setStatus("<span style='color:blueviolet'>未采购</span>");
        }else if(cg>0 && cg<fp){
            p.setStatus("<span style='color:green'>采购中</span>");
        }else if(cg==fp && rk<cg){
            p.setStatus("<span style='color:blue'>采购完成,但未全部到库</span>");
        }else if(checked==fp){
            p.setStatus("<span style='color:green'>已到仓库,验货无误</span>");
        }else if(problem.size()!=0){
            p.setStatus("<span style='color:deepskyblue'>已全部到仓库,校验有问题</span>");
        }else if(rk==fp && rk>checked){
            p.setStatus("<span style='color:dodgerblue'>已全部到仓库,未校验</span>");
        }else{
            p.setStatus("<span style='color:red'>未知状态</span>");
        }
    }

    @Override
    public int getFpCount(String orderid, String admuserid) {

        return dao.getFpCount(orderid,admuserid);
    }
    @Override
    public List<String> getPrePurchaseCount(Map<String, Object> map) {

        return dao.getPrePurchaseCount(map);
    }
    @Override
    public int updateShopState(Map<String, Object> map) {

        return dao.updateShopState(map);
    }
    @Override
    public List<UserInfo> getUserInfoForPriceCount(Map<String, Object> map) {
        List<UserInfo> userInfos=dao.getUserInfoForPriceCount(map);
        return userInfos;
    }


    @Override
    public Tb1688Pojo getTbState(Map<String, Object> map) {

        return dao.getTbState(map);
    }

    @Override
    public List<AllProblemPojo> getAllProblem(Map<String, Object> map) {

        return dao.getAllProblem(map);
    }

    @Override
    public int getTotalNumber(Map<String, Object> map) {

        return dao.getTotalNumber(map);
    }

    @Override
    public List<String> getAllProposal(Map<String, Object> map) {

        return dao.getAllProposal(map);
    }

    @Override
    public int insertOrderReplenishment(Map<String, Object> map) {

        map.put("goods_title", map.get("goods_title").toString().replaceAll("'", "&apos;"));

        return dao.insertOrderReplenishment(map);
    }
    @Override
    public int updateReplenishmentState(Map<String, Object> map) {

        return dao.updateReplenishmentState(map);
    }
    @Override
    public int insertShopId(Map<String, Object> map) {

        return dao.insertShopId(map);
    }
    @Override
    public int addReplenishmentRecord(Map<String, Object> map) {

        return dao.addReplenishmentRecord(map);
    }
    @Override
    public List<OrderReplenishmentPojo> getIsReplenishment(Map<String, Object> map) {

        return dao.getIsReplenishment(map);
    }
    @Override
    public List<Replenishment_RecordPojo> getIsReplenishments(
            Map<String, Object> map) {

        return dao.getIsReplenishments(map);
    }
    @Override
    public List<DisplayBuyInfo> displayBuyLog(Map<String, Object> map) {

        return dao.displayBuyLog(map);
    }
    @Override
    public List<OfflinePurchaseRecordsPojo> getIsOfflinepurchase(Map<String,Object> map){
        return dao.getIsOfflinepurchase(map);
    }
    @Override
    public Map<String, String> getCompanyInfo(String goods_pid) {

        return dao.getCompanyInfo(goods_pid);
    }
    @Override
    public int insertDeclareinfo(Map<String, Object> map) {

        return dao.insertDeclareinfo(map);
    }

    @Override
    public List<JcexPrintInfo> getJcexPrintInfo(Map<String, Object> map) {

        return dao.getJcexPrintInfo(map);
    }

    @Override
    public int updateDeclareinfoByOrderid(Map<String, Object> map) {

        return dao.updateDeclareinfoByOrderid(map);
    }

    @Override
    public List<Integer> queryUser() {
        return dao.queryUser();
    }

    @Override
    public int delteOrderReplenishment(Map<String, Object> map) {

        return dao.delteOrderReplenishment(map);
    }

    @Override
    public String getOrderAddress(Map<String, Object> map) {

        return dao.getOrderAddress(map);
    }

    @Override
    public String getCgCount(Map<String, Object> map) {

        return dao.getCgCount(map);
    }
    //当月分配种类
    @Override
    public String getfpCount(Map<String, Object> map) {

        return dao.getfpCount(map);
    }

    @Override
    public String getNotShipped(Map<String, Object> map) {
        return dao.getNotShipped(map);
    }

    @Override
    public String getShippedNoStorage(Map<String, Object> map) {
        return dao.getShippedNoStorage(map);
    }

    @Override
    public int addKeyword(Map<String, String> map) {
        int row=0;
        try{
            SendMQ sendMQ=new SendMQ();
            row=dao.addKeyword(map);
            if(row>0){
                sendMQ.sendMsg(new RunSqlModel("insert into priority_category(keyword,category) values('"+map.get("keyword")+"','"+map.get("cateId")+"')"));
            }
            sendMQ.closeConn();
        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public int editKeyword(Map<String, String> map) {
        int row=0;
        try{
            SendMQ sendMQ=new SendMQ();
            row=dao.editKeyword(map);
            if(row>0){
                sendMQ.sendMsg(new RunSqlModel("update priority_category set category="+map.get("cid")+" where id="+map.get("id")+""));
            }
            sendMQ.closeConn();
        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public int updateStateCategory(Map<String, String> map) {
        int row=0;
        try{
            SendMQ sendMQ=new SendMQ();
            row=dao.updateStateCategory(map);
            if(row>0){
                sendMQ.sendMsg(new RunSqlModel("update priority_category set status="+map.get("state")+" where id="+map.get("id")+""));
            }
            sendMQ.closeConn();
        }catch (Exception e){
            e.printStackTrace();
        }
        return row;
    }

    //获得每月采购数量
    @Override
    public String getMCgCount(Map<String, Object> map) {

        return dao.getMCgCount(map);
    }
    //当日分配采购种类
    @Override
    public String getDistributionCount(Map<String, Object> map) {

        return dao.getDistributionCount(map);
    }

    @Override
    public String getDeleteInventory() {
        PurchaseSamplingStatisticsPojo p=dao.getDeleteInventory();
        return getDate(p,3);
    }

    public String getDate(PurchaseSamplingStatisticsPojo p, int type){
        StringBuilder str=new StringBuilder();
        if(p != null){
            if(StringUtil.isNotBlank(p.getAdmName())){
                if(type == 1){
                    str.append("<a target='_blank' href='/cbtconsole/website/saleInventory.jsp?times=2000&amount="+(StringUtil.isNotBlank(p.getPid())?p.getPid().replace("-",""):0)+"' title='查看使用库存商品的商品'>"+p.getAdmName()+"</a>").append("/");
                }else if(type == 2){
	                str.append("<a target='_blank' href='/cbtconsole/website/loss_inventory_log.jsp?times=2000' title='查看库存损耗的商品'>"+p.getAdmName()+"</a>").append("/");
                }else if(type == 3){
	                str.append("<a target='_blank' href='/cbtconsole/website/inventory_delete_log.jsp?times=2000' title='查看删除库存的商品'>"+p.getAdmName()+"</a>").append("/");
                }else{
                    str.append(p.getAdmName()).append("/");
                }
            }else{
                str.append("0/");
            }
            if(StringUtil.isNotBlank(p.getPid())){
                str.append(p.getPid().replace("-",""));
            }else{
                str.append("0");
            }
        }
        return str.toString();
    }



    @Override
    public String getLossInventory() {
        PurchaseSamplingStatisticsPojo p=dao.getLossInventory();
        return getDate(p,2);
    }

    @Override
    public String getSaleInventory() {
        PurchaseSamplingStatisticsPojo p=dao.getSaleInventory();
        return getDate(p,1);
    }

    @Override
    public String getNewInventory() {
        PurchaseSamplingStatisticsPojo p=dao.getNewInventory();
        StringBuilder str=new StringBuilder();
        if(p != null){
            if(StringUtil.isNotBlank(p.getAdmName())){
                str.append(p.getAdmName()).append("/");
            }else{
                str.append("0/");
            }
            if(StringUtil.isNotBlank(p.getPid())){
                str.append(p.getPid());
            }else{
                str.append("0");
            }
        }
        return str.toString();
    }

    @Override
    public String getSjCgCount(Map<String, Object> map) {

        return dao.getSjCgCount(map);
    }

    @Override
    public int checkedGoods(Map<String, String> map) {
        return dao.checkedGoods(map);
    }

    @Override
    public int productAuthorization(Map<String, String> map) {
        int ret=dao.checkIsExit(map);
        if(ret>0){
            ret=dao.updateAuthorizedFlag(map);
        }else{
            ret=dao.insertAuthorizedFlag(map);
        }
        return ret;
    }

    @Override
    public int insertOrderRemark(Map<String, Object> map) {
        int count=dao.findOrderRemark(map);
        if(count>0){
            return dao.updateOrderRemark(map);
        }else{
            return dao.insertOrderRemark(map);
        }
    }

    @Override
    public int insertWarningInfo(Map<String, String> map) {
        return dao.insertWarningInfo(map);
    }

    @Override
    public List<Map<String,String>> getGoodsCar(Map<String, Object> map) {

        return dao.getGoodsCar(map);
    }

    @Override
    public List<CustomGoodsBean> getAllGoodsInfos(String goods_pids) {

        return dao.getAllGoodsInfos(goods_pids);
    }

    @Override
    public int insertOrderInfo(Orderinfo oi) {

        return dao.insertOrderInfo(oi);
    }

    @Override
    public OrderDetailsBean getOldDetails(String pid) {
        return dao.getOldDetails(pid);
    }

	@Override
	public OrderDetailsBean getCustomBeack(String pid) {
		return dao.getCustomBeack(pid);
	}

	@Override
    public int insertOrderDetails(List<OrderDetailsBean> list) {

        return dao.insertOrderDetails(list);
    }

    @Override
    public int getOrderDetailsExit(String goods_pid, String car_type) {

        return dao.getOrderDetailsExit(goods_pid,car_type);
    }

    @Override
    public void insertGd(List<OrderDetailsBean> list) {

        dao.insertGd(list);
    }

    @Override
    public int updateGdOdid() {
        return   dao.updateGdOdid();
    }

    @Override
    public List<OrderDetailsBean> getOrderDetailsByOrderid(String orderid) {

        return dao.getOrderDetailsByOrderid(orderid);
    }

    @Override
    public int updateCrossBorder(String goods_pid) {

        return dao.updateCrossBorder(goods_pid);
    }

    @Override
    public int updateCrossShopr(String goods_pid) {

        return dao.updateCrossShopr(goods_pid);
    }
    @Override
    public int updateShop(String goods_pid) {

        return dao.updateShop(goods_pid);
    }

    @Override
    public int updateSamplFlag(String goodsPid,int count) {

        return dao.updateSamplFlag(goodsPid,count);
    }

    @Override
    public String getHsCode(Map<String, Object> map) {

        return dao.getHsCode(map);
    }

    @Override
    public String getShipmentno() {

        return dao.getShipmentno();
    }

    @Override
    public int batchInsertSP(List<Map<String, String>> list) {
        return dao.batchInsertSP(list);
    }

    @Override
    public int deleteShippingPackage(Map<String, String> map) {
        return dao.deleteShippingPackage(map);
    }

    @Override
    public int selectShippingPackage(Map<String, String> map) {
        return dao.selectShippingPackage(map);
    }

    @Override
    public List<Map<String, String>> getMaxImg(Map<String, String> map) {
        return dao.getMaxImg(map);
    }

    @Override
    public List<Map<String, String>> getCntSum(Map<String, String> map) {
        return dao.getCntSum(map);
    }

    @Override
    public ShippingPackage getPackageInfo(Map<String, String> map) {
        return dao.getPackageInfo(map);
    }

    @Override
    public int batchUpdateShippingPackage(List<Map<String, String>> list) {
        return dao.batchUpdateShippingPackage(list);
    }

    @Override
    public List<ShippingPackage> getPackageInfoList(Map<String, String> map) {
        List<ShippingPackage> list = dao.getPackageInfoList(map);
        for(ShippingPackage bean:list){
            bean.setIsDropshipFlag(0);
        }
        List<ShippingPackage> dropshipList = dao.getDropshipPackageInfoList(map);
        for(ShippingPackage bean:dropshipList){
            bean.setIsDropshipFlag(1);
        }
        list.addAll(dropshipList);
        for(int i=0;i<list.size();i++){
            ShippingPackage s=list.get(i);
            s.setEstimatefreight(String.valueOf(Double.valueOf(s.getEstimatefreight())+s.getPid_amount()));
        }
        return list;
    }

    @Override
    public List<ShippingPackage> getShippingPackageById(Map<String, String> map) {
        return dao.getShippingPackageById(map);
    }

    @Override
    public int bgUpdate(List<Map<String, String>> list) {
        return dao.bgUpdate(list);
    }

    @Override
    public List<SbxxPojo> getSbxxList(Map<String, String> map) {
        return dao.getSbxxList(map);
    }

    @Override
    public int insertSbxx(Map<String, String> map) {
        return dao.insertSbxx(map);
    }

    @Override
    public int updateOpsState(Map<String, String> map) {
        return dao.updateOpsState(map);
    }

    @Override
    public int updateOrderinfoNumber(Map<String, String> map) {
        return dao.updateOrderinfoNumber(map);
    }

    @Override
    public int updateOrderinfoState(Map<String, String> map) {
        //更新订单的状态
        return dao.updateOrderinfoState(map);
    }
    @Override
    public int updateChildOrderinfoState(Map<String, String> mapc) {
        //更新dropship子订单的状态
        return dao.updateChildOrderinfoState(mapc);
    }

    @Override
    public int GetSetOrdrerState(Map<String, String> map) {
        return dao.GetSetOrdrerState(map);
    }

    @Override
    public int updateOrder(Map<String, String> map) {
        return dao.updateOrder(map);
    }


    @Override
    public int updateorderDetailsState(Map<String, String> map) {
        return dao.updateorderDetailsState(map);
    }

    @Override
    public int getOdIsState(Map<String, String> map) {
        return dao.getOdIsState(map);
    }

    @Override
    public List<JcexPrintInfo> getJcexPrintInfoPlck(Map<String, Object> map) {
        List<JcexPrintInfo> jcexList=dao.getJcexPrintInfoPlck(map);
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
            String ret = dao.getHsCode(map1);
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
        List<JcexPrintInfo> jcexList=dao.getJcexPrintInfoPlckCount(map);
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
            String ret = dao.getHsCode(map1);
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
        List<Forwarder>  list = dao.getForwarderplck(map);
//        for(Forwarder bean :list){
//            bean.setIsDropshipFlag(0);
//        }
//        List<Forwarder>  fwList = dao.getDropshipForwarderplck(map);
//        for(Forwarder bean :fwList){
//            bean.setIsDropshipFlag(1);
//        }
//        list.addAll(fwList);
        return list;
    }

    @Override
    public int getCountForwarderplck(Map<String, Object> map) {
        int count = dao.getCountForwarderplck(map);
//        int count2 = dao.getDropshipCountForwarderplck(map);
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
            //邮政
            Map freightEpacketMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "epacket", subShippingmethod, svolumn);
            double freightEpacket=Double.valueOf(freightEpacketMap.get("freightFee").toString());
            //jcex
            Map freightJcexMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "jcex", subShippingmethod, svolumn);
            double freightJcex=Double.valueOf(freightJcexMap.get("freightFee").toString());
            //原飞航
            Map freightYfhMap=freightFeeSerive.getFreightFee(weights_, volume_, countId, "原飞航", subShippingmethod, svolumn);
            double freightYfh=Double.valueOf(freightYfhMap.get("freightFee").toString());
            double optimal_cost=0.00;
            String optimal_company="";
            if(freightEpacket<freightJcex && freightEpacket<freightYfh){
                optimal_cost=freightEpacket;
                optimal_company="邮政";
            }else if(freightJcex<freightEpacket && freightJcex<freightYfh){
                optimal_cost=freightJcex;
                optimal_company="JCEX";
            }else if(freightYfh<freightEpacket && freightYfh<freightJcex){
                optimal_cost=freightYfh;
                optimal_company="原飞航";
            }
            forwarder.setOptimal_cost(optimal_cost);
            forwarder.setOptimal_company(optimal_company);
        }
    }

    @Override
    public int updateExperssNoPlck(Map<String, Object> map) {
        return dao.updateExperssNoPlck(map);
    }

    @Override
    public int updateGoodsDistribution(Map<String, String> map) {
        return dao.updateGoodsDistribution(map);
    }

    @Override
    public int updateRemainingPrice(Map<String, String> map) {
        return dao.updateRemainingPrice(map);
    }

    @Override
    public int updateODPState(Map<String, String> map) {
        return dao.updateODPState(map);
    }

    @Override
    public int updateSendMail(Map<String, String> map) {
        return dao.updateSendMail(map);
    }
    @Override
    public int insertId_relationtable(Map<String, String> map) {
        return dao.insertId_relationtable(map);
    }
    @Override
    public int xlsbatch(List<Map<String, String>> list) {
        return dao.xlsbatch(list);
    }

    @Override
    public int addGoodsInventory(Map<String,String> map) {
        return dao.addGoodsInventory(map);
    }

    @Override
    public String getUserName(String userid) {
        return dao.getUserName(userid);
    }

    @Override
    public List<GoodsInventory>  addGoodsInvent(String orderid) {
        dao.addGoodsInvent(orderid);
        List<GoodsInventory> list = dao.selectInventByOrderId(orderid);
        return  list;
    }

    @Override
    public int  insertSp(String shipmentno) {
        return dao.insertSp(shipmentno);
    }

    @Override
    public int selectOrderFeeByOrderid(Map<String, Object> map) {
        List<String>  list = dao.selectOrderFeeByOrderid(map);
        int res = list.size();
        return res;
    }

    @Override
    public List<String> getOrderNoList(Map<String, Object> map) {
        return dao.getOrderNoList(map);
    }
    @Override
    public String getSpId(String orderids) {

        return dao.getSpId(orderids);
    }

    @Override
    public int upOrderInfo(Map<String, Object> map) {
        return 	dao.upOrderInfo(map);
    }

    @Override
    public int upId_relationtable(Map<String, Object> map) {
        return  dao.upId_relationtable(map);
    }

    @Override
    public int upOrderFee(Map<String, Object> map) {
        return  dao.upOrderFee(map);
    }

    @Override
    public int upWaraingState(Map<String, Object> map) {
        return  dao.upWaraingState(map);
    }

    @Override
    public String getMegerOrder(String orderId) {
        return  dao.getMegerOrder(orderId);
    }

    @Override
    public List<String> getOrderFeeOrderNO(String orderno) {
        return  dao.getOrderFeeOrderNO(orderno);
    }

    @Override
    public int updateDropshipState(Map<String, Object> map) {
        return dao.updateDropshipState(map);
    }

    @Override
    public String checkState(String orderno) {
        //查询子单对应主单，主单所包含的子单总数
        int sum = dao.selectCountByparent_order_no(orderno);
        //查询子单对应主单，状态为3的个数
        int count = dao.selectSumByState(orderno);
        String order= null;
        if(count==sum){
            //如果子单状态都为3,则返回主订单号
            order = dao.selectDropshipOrderNo(orderno);
        }
        return order;
    }

    @Override
    public int updateMainDropshipState(String mainOrder) {
        return dao.updateMainDropshipState(mainOrder);
    }

    @Override
    public List<LocationManagementInfo> getLocationManagementInfo(Map<Object, Object> map) {

        return dao.getLocationManagementInfo(map);
    }

    @Override
    public List<LocationManagementInfo> getLocationManagementInfoByOrderid(
            String orderid) {

        return dao.getLocationManagementInfoByOrderid(orderid);
    }

    @Override
    public int resetLocation(Map<String, String> map) {

        return dao.resetLocation(map);
    }

    @Override
    public int updateFlag(String id, String type) {
        return dao.updateFlag(id,type);
    }

    @Override
    public int updatebackEmail(String id, String email) {
        return dao.updatebackEmail(id,email);
    }

    @Override
    public int addBackUser(String email, String ip,String userName) {
        return dao.addBackUser(email,ip,userName);
    }

    @Override
    public int searchCount(Map<Object, Object> map) {

        return dao.searchCount(map);
    }

    @Override
    public void inertLocationTracking(Map<String, String> map) {

        dao.inertLocationTracking(map);
    }

    @Override
    public List<LocationTracking> getLocationTracking(Map<Object, Object> map) {

        return dao.getLocationTracking(map);
    }

    @Override
    public int searchCount1() {

        return dao.searchCount1();
    }

    @Override
    public String getCreateTime(String barcode) {
        return dao.getCreateTime(barcode);
    }

    @Override
    public int noInspection() {

        List<String> rows=dao.noInspection();
        return rows!=null?rows.size():0;
    }

    @Override
    public List<LocationManagementInfo> getCheckOrders(String orderids, int num) {

        return dao.getCheckOrders(orderids,num);
    }

    @Override
    public List<StorageLocationBean> getAllOutboundorder(Map<Object, Object> map) {

        return dao.getAllOutboundorder(map);
    }

    @Override
    public List<OrderDetailsBean> getOrderDetailsInfo(Map<Object, Object> map) {

        return dao.getOrderDetailsInfo(map);
    }

    @Override
    public int updateState(Map<String, String> map) {

        return dao.updateState(map);
    }

    @Override
    public int getMid() {

        return dao.getMid();
    }

    @Override
    public int getShortTerm() {

        return dao.getShortTerm();
    }

    @Override
    public String getOrderIdByBarcode(String barcode) {

        return dao.getOrderIdByBarcode(barcode);
    }

    @Override
    public List<String> getSaleOrderid(String shipno) {
        return dao.getSaleOrderid(shipno);
    }

    @Override
    public List<StorageInspectionLogPojo> getStorageInspectionLogInfo(Map<String, String> map) {
        return dao.getStorageInspectionLogInfo(map);
    }


    @Override
    public LocationManagementInfo getTaoBaoInfos(String orderid) {

        return dao.getTaoBaoInfos(orderid);
    }

    @Override
    public int getAcounts(String orderid) {

        return dao.getAcounts(orderid);
    }

    @Override
    public int getAmounts(String orderid) {

        return dao.getAmounts(orderid);
    }

    @Override
    public List<TaoBaoOrderInfo> getPurchaseOrderDetails(Map<Object, Object> map) {

        return dao.getPurchaseOrderDetails(map);
    }

    @Override
    public int updateIsRead(String orderid) {

        return dao.updateIsRead(orderid);
    }

    @Override
    public List<String> getInfos(int admid) {

        return dao.getInfos(admid);
    }

    @Override
    public List<Tb1688Account> getAllBuy() {

        return dao.getAllBuy();
    }

    @Override
    public void updateMessageY(String ids) {

        dao.updateMessageY(ids);
    }

    @Override
    public OrderBean getUserOrderInfoByOrderNo(String orderNo) {
        return dao.getUserOrderInfoByOrderNo(orderNo);
    }

    @Override
    public int updateBarcodeByOrderNo(String orderid) {

        List<String>  list = dao.selectBarcideByOrderNo(orderid);
        int ret = 0 ;
        if(list.size()>0){
            ret = dao.updateBarcodeByOrderNo(list);
        }
        return ret;
    }

    @Override
    public List<TaoBaoOrderInfo> getAllCount(Map<Object, Object> map) {

        return dao.getAllCount(map);
    }

    @Override
    public int queryOrderState(Map<String, String> map) {

        return dao.queryOrderState(map);
    }

    @Override
    public int updateOrderState(Map<String, String> map) {

        return dao.updateOrderState(map);
    }

    @Override
    public int updateAllDetailsState(Map<String, String> map) {

        return dao.updateAllDetailsState(map);
    }

    @Override
    public int insertRemark(Map<String, String> map) {

        return dao.insertRemark(map);
    }

    @Override
    public List<String> allLibraryCount() {

        return dao.allLibraryCount();
    }

    @Override
    public int getStockOrderInfo() {

        return dao.getStockOrderInfo();
    }

    @Override
    public List<StorageLocationBean> allLibrary(Map<Object, Object> map) {

        return dao.allLibrary(map);
    }

    @Override
    public void treasuryNote(String orderNo, int goodsid, String remarkContent) {
        dao.treasuryNote(orderNo, goodsid, remarkContent);
    }
    @Override
    public int updateCheckForNote(String orderNo, int goodsid) {
        return dao.updateCheckForNote(orderNo, goodsid);
    }
    @Override
    public Integer[] deleteOrderGoods(String orderNo, int goodId, int userid,
                                      int purchase_state, List<ClassDiscount> cdList, HttpServletResponse response) {
        int res = dao.getDelOrder(orderNo);//根据订单号获取当前订单有多少个商品未取消

        dao.updateOrderChange2State(orderNo, goodId, null);//更新order_change

        Map<String, Object> orderinfoMap = dao.getOrderinfoByOrderNo(orderNo);//获取当前订单的信息

        String remaining = String.valueOf(orderinfoMap.get("remaining_price"));

        orderinfoMap.put("remaining_price", Utility.getStringIsNull(remaining) ? remaining : "0");

        List<OrderDetailsBean> orderDetails = dao.getDelOrderByOrderNoAndStatus(orderNo, goodId);//获取订单改商品的订单详情

        int deleteCtpoOrderGoods = dao.updateOrderDetails2StateByGoodId(orderNo, goodId);//修改订单详情状态=2
        if (deleteCtpoOrderGoods < 0) {
            deleteCtpoOrderGoods = 0;
        }
        //查询该订单中的订单详情是否还存在数据
        //Map<String, Object> odblist=orderMapper.getDelOrderByOrderNoAndStatus(orderNo,goodId, 2);
        //List<OrderDetailsBean> orderDetailsBeans = (List<OrderDetailsBean>) orderinfoMap.get("odb");
        double discount_amount = Double.parseDouble(orderinfoMap.get("discount_amount").toString());//订单类别折扣金额
        double share_discount = Double.parseDouble(orderinfoMap.get("share_discount").toString());//社交分享折扣
        double coupon_discount = Double.parseDouble(orderinfoMap.get("coupon_discount").toString());//返单优惠
        String currency = orderinfoMap.get("currency").toString();
        double order_ac = Double.parseDouble(orderinfoMap.get("order_ac").toString());
        double product_cost = Double.parseDouble(orderinfoMap.get("product_cost").toString());
        double cashback = Double.parseDouble(orderinfoMap.get("cashback").toString());
        double payprice = Double.parseDouble(orderinfoMap.get("pay_price").toString());//.get("pay_price").toString()
        //获取用户货币单位
//		String currency_u = userMapper.getCurrencyById(userid);
        double userAccount =Double.parseDouble(String.valueOf(dao.getBalance_currency(userid).get("available_m")));
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
                    //该商品的总金额=(数量*单价)-该商品的混批折扣金额
                    double available=mc_gross*yourorder;
                    double discount_pro = 0;
                    double  newShareDiscount =0;
                    double  newCouponDiscount =0;
                    //混批折扣按比例减少
                    if(discount_amount != 0 && odb.getDiscount_ratio() != 0){
                        discount_pro = new BigDecimal((1-odb.getDiscount_ratio()) * available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    //返单优惠
                    if(coupon_discount>0){
                        newCouponDiscount = new BigDecimal(available/product_cost*coupon_discount).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    //社交分享折扣相应减少
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


                    //订单所欠费用小于商品费用->删除的商品放入余额（商品费用-订单所欠费用），订单所欠费用大于商品费用->修改订单表的订单所欠费用(订单所欠费用-商品费用),订单所欠费用为0->删除的商品放入余额(商品费用)
                    String remaining_price =  (String) orderinfoMap.get("remaining_price");
                    //订单所欠费用
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
                        //订单所欠费用大于商品费用->修改订单表的订单所欠费用(订单所欠费用-商品费用)
                        available_r = new BigDecimal(r_price - available).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        pay_price = new BigDecimal(payprice).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
                        dao.updateOrderPayPrice1(userid, orderNo, pay_price, null,discount_amount,share_discount,product_cost,available_r,cashback);
                    }else{
                        //订单所欠费用小于商品费用->删除的商品放入余额（商品费用-订单所欠费用）
                        available_r = new BigDecimal(available - r_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        if(res == 1){
                            available_r = payprice;
                            order_ac = new BigDecimal(order_ac*exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }else{
                            order_ac = 0;
                        }
                        double available_ru = new BigDecimal(available_r*exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        pay_price = new BigDecimal(payprice - available_r).setScale(2,   BigDecimal.ROUND_HALF_UP).toString();
                        //将删除的商品钱放入余额
                        if(Double.parseDouble(pay_price) >= 0){
                            //添加充值记录
                            RechargeRecord rr = new RechargeRecord();
                            rr.setUserid(userid);
                            rr.setPrice(available_ru);
                            rr.setRemark_id(orderNo);
                            rr.setType(1);
                            rr.setBalanceAfter(userAccount+available_ru);
                            rr.setRemark("cancel:"+orderNo+",goodsid:"+goodId);
                            rr.setCurrency(currency);
                            dao.addRechargeRecord(rr);
                            dao.upUserPrice(userid, available_ru, order_ac);
                            dao.updateOrderPayPrice1(userid, orderNo, pay_price, null, discount_amount,share_discount,product_cost, 0.0,cashback);
                            //社交分享--同步更新分享者优惠折扣
                            try {
                                dao.updateAcceptPriceByOrderNo(orderNo,pay_price);
                            } catch (Exception e) {
                                LOG.error("直接取消商品", e);
                            }

                        }
                    }
                }
            }

        }
        if(res == 1 && deleteCtpoOrderGoods == 1){
            //将订单状态改为已删除状态 后台取消为-1，前台客户取消为6
            dao.upOrderPurchase(purchase_state, orderNo, -1);
            //删除对应消息删除message 表中有确认价格信息的数据
            dao.delMessageByOrderid(orderNo, "Order #");
            //更新社交分享 accept_share 状态
            try {
                int ret = dao.updateAcceptShareByOrderNo(orderNo);
                if(ret>0){
                    //清除cookie 中的 shareID
                    Cookie cookie = new Cookie("shareID",null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            } catch (Exception e) {
            }
            return new Integer[]{deleteCtpoOrderGoods,6};
        }else{
            dao.upOrderPurchase(purchase_state, orderNo, 5);
            Integer orderstate =  dao.checkUpOrderState(orderNo);
            return new Integer[]{deleteCtpoOrderGoods,orderstate};
//			return new Integer[]{deleteCtpoOrderGoods,5};
        }
    }

    @Override
    public List<OrderBean> getOrderInfo(int userid, String order_no) {
        return dao.getOrderInfoByOrder_noAndUserid( userid, order_no.split(","));
    }

    @Override
    public int insertBuluInfo(List<Map<String, Object>> bgList) {
        return  dao.insertBuluInfo(bgList);
    }

    @Override
    public String getOrderInfoStateByOrderNo(String orderNo) {
        return dao.getOrderInfoStateByOrderNo(orderNo);
    }

    @Override
    public int getOrderDetailsStateByOrderNoAndGoodsid(String orderNo, int odid) {
        return dao.getOrderDetailsStateByOrderNoAndGoodsid(orderNo, odid);
    }

    @Override
    public int updateOrderSourceState(String orderid) {

        return dao.updateOrderSourceState(orderid);
    }

    @Override
    public int checkAuthorizedFlag(String orderid) {
        int ret=0;
        Map<String,String> map=new HashMap<String,String>();
        List<String> oList=dao.getAllGoodsPidsByOrderNo(orderid);
        for(String pid:oList){
            map.put("goodsPid",pid);
            ret=dao.checkIsExit(map);
            if(ret<=0){
                map.put("flag","2");
                ret=dao.insertAuthorizedFlag(map);
            }
        }
        return ret;
    }

    @Override
    public int insertGoodsInventory(String orderid) {
        return dao.insertGoodsInventory(orderid);
    }

    @Override
    public int updateBuluOrderState(List<Map<String, Object>> bgList) {

        // dropship 订单
        List<Map<String, Object>>  list  = new  ArrayList<Map<String,Object>>();
        for(Map<String,Object> map: bgList){
            int  isDropShipFlag =  (Integer) map.get("isDropShipFlag");
            if(isDropShipFlag==1){
                list.add(map);
            }
        }
        //更新orderinfo 订单信息
        dao.updateBuluOrderState(bgList);
        //更新dropshipOrder 订单信息
        dao.updateBuluDropShipOrderState(list);
        return dao.updateBuluOrderState(bgList);
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

        //鍏ュ簱鐓х墖
        ArrayList<String> picturepaths = new ArrayList<String>();
        resMap.put("picturepaths", picturepaths);
        // order_details.picturepath		orderid,picturepath
        List<Map<String, Object>> orderDetailss = dao.getOrderDetailsByOrderidIn(remarksList);
        for (Map<String, Object> orderDetails : orderDetailss) {
            Object picturepath = orderDetails.get("picturepath");
            if (picturepath != null && !"".equals(picturepath.toString())){
                picturepath = picturepath.toString().replaceAll("http:", "https:");
                if ("https:".equals(picturepath.toString().substring(0,6))) {
                    picturepaths.add(picturepath.toString());
                } else {
                    picturepaths.add("http://img.import-express.com/importcsvimg/inspectionImg/" + picturepath.toString());
                }
            }
        }
        //出货照片
        ArrayList<String> ftpPicPaths = new ArrayList<String>();
        resMap.put("ftpPicPaths", ftpPicPaths);
        // products packed 出货照片 orderinfo.ftpPicPath 		order_no,ftpPicPath
        List<Map<String, Object>> orderinfos = dao.getOrderinfoByOrderNoIn(remarksList);
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
    	//保存运单变更记录 （用于退回等订单将原物流保存到新物流商） ly 2018/08/22 15:36
    	dao.insertChangeLog(map);
    }

}
