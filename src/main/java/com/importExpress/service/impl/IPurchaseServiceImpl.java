package com.importExpress.service.impl;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.trade.param.AlibabaTradeFastAddress;
import com.alibaba.trade.param.AlibabaTradeFastCargo;
import com.alibaba.trade.param.AlibabaTradeFastCreateOrderParam;
import com.alibaba.trade.param.AlibabaTradeFastCreateOrderResult;
import com.cbt.bean.Address;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.common.StringUtils;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.orderinfo.dao.OrderinfoMapper;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.cbt.pojo.StraightHairPojo;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.util.AppConfig;
import com.cbt.util.Util;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.InventoryMapper;
import com.cbt.warehouse.dao.WarehouseMapper;
import com.cbt.warehouse.pojo.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PrePurchasePojo;
import com.cbt.website.bean.PurchaseGoodsBean;
import com.cbt.website.bean.PurchasesBean;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.cbt.website.dao2.Page;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.ibm.icu.math.BigDecimal;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.mapper.CustomGoodsMapper;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.pojo.PurchaseInfoBean;
import com.importExpress.pojo.ShopGoodsSalesAmount;
import com.importExpress.service.IPurchaseService;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.regex.Pattern;

@Service
public class IPurchaseServiceImpl implements IPurchaseService {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(IPurchaseServiceImpl.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static String CHAR_STR="(\\d{6})";
	@Autowired
	private IPurchaseMapper pruchaseMapper;
	@Autowired
	private InventoryMapper inventoryMapper;
	@Autowired
	private OrderinfoMapper orderinfoMapper;
	@Autowired
	private SendMailFactory sendMailFactory;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private CustomGoodsMapper customGoodsMapper;
	@Autowired
	private WarehouseMapper dao;
	int total;
	int goodsnum = 0;

	@Override
	public OrderBean getOrders(String orderNo) {
		return pruchaseMapper.getOrders(orderNo);
	}
	@Override
	public int getFpCount(String orderid, String admuserid) {

		return pruchaseMapper.getFpCount(orderid,admuserid);
	}


	@Override
	public Double getAllFreightByOrderid(String orderNo) {
		double allWeight=0.00;
		Map<String,String> map=pruchaseMapper.getAllFreightByOrderid(orderNo);
		if(map != null && map.get("allWeight") != null){
			allWeight=Double.parseDouble(String.valueOf(map.get("allWeight")));
		}
		return allWeight;
	}

	@Override
	public String getOtherSources(String orderNo, String odid, String goods_url) {
		String uuu = "";
		String gUrl = "<table border='1'><tr><td width='20%'>货源单价</td><td width='50%'>货源链接</td><td width='20%'>使用货源</td><td width='10%'>更新时间</td></tr>";
		String sql = "";
		try{
			List<Map<String,String>> mapList=pruchaseMapper.getOtherSources(orderNo,odid);
			int i = 0;
			for(Map<String,String> map:mapList){
				String url =String.valueOf(map.get("nv"));
				String shop_id=String.valueOf(map.get("shop_id"));
				uuu = uuu
						+ "<tr><td width='20%'>"
						+ String.valueOf(map.get("gp"))
						+ "RMB</td><td width='60%'><a href='"
						+ url
						+ "' target='block'>"
						+ url
						+ "</a></td>"
						+ "<td width='20%'><input type='hidden' id='"+i+"_this_shop_id' value='"+(StringUtils.isStrNull(shop_id) || "".equals(shop_id)?"":shop_id)+"'/>"
						+ "<input type='hidden' id='"+i+"_this_address' value='"+String.valueOf(map.get("address"))+"'/><input type='button' value='使用货源' onclick='FnUseThis("
						+ String.valueOf(map.get("gp")) + "," + i + ");'>"
						+ "<input type='hidden' id='id_" + i + "' value='"
						+ url + "'></td><td width='10%'>" + String.valueOf(map.get("ut"))
						+ "</td></tr>";
				i++;
			}
			if (uuu == "" || uuu == null) {
				uuu = "<tr><td colspan='3' align='center'>暂无其他货源。</td></tr>";
			}
			gUrl = gUrl + uuu + "</table>";
		}catch (Exception e){
			e.printStackTrace();
		}
		return gUrl;
	}

	@Override
	public int checkOrder(String orderNo, String odid) {
		int state=0;
		//首先整单是否被取消
		state=pruchaseMapper.checkOrder(orderNo);
		if (state != -1 && state != 6) {
			//订单为正常状态则判断商品是否被取消
			state = pruchaseMapper.checkGoods(orderNo, odid);
			if (state != 2) {
				String states = pruchaseMapper.checkOrderChange(orderNo, odid);
				if (StringUtil.isNotBlank(states) && "4".equals(states)){
					state = 2;
				}
			}
		} else {
			state = 111;
		}
		return state;
	}

	@Override
	public String getUserbyID(String buyerId) {
		return pruchaseMapper.getUserbyID(buyerId);
	}

	@Override
	public int PurchaseComfirmTwoHyqr(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid, String goodsurl, String googsimg,
	                                  String goodsprice, String goodstitle, int googsnumber, String oldValue, String newValue, int purchaseCount, String child_order_no, String isDropshipOrder) {
		int i = 0,res = 0,buyCount = 0;
		i++;
		String goods_p_url = "";
		try{
			SendMQ sendMQ=new SendMQ();
			res=pruchaseMapper.getOpsCount(orderNo,od_id);
			if (res == 0) {
				Map<String,String> odMap=pruchaseMapper.getOrderDtails(od_id);
				if(odMap != null){
					Pattern pattern = Pattern.compile(CHAR_STR);
					boolean flag=pattern.matcher(goods_p_url).matches();
					if(flag){
						goods_p_url = "https://detail.1688.com/offer/"+ odMap.get("goods_pid") + ".html";
					}else{
						goods_p_url = "https://www.amazon.com/"+ odMap.get("goodsname")+"/dp/"+ odMap.get("goods_pid")+"";
					}
					buyCount =Integer.valueOf(String.valueOf(odMap.get("yourorder")));
					googsimg=String.valueOf(odMap.get("car_img"));
					if (googsnumber <= 0) {
						googsnumber = buyCount;
					}
					if ("".equals(newValue)) {
						newValue = goods_p_url;
					}
					String itemid = Util.getItemid(newValue);
					//删除货源
					pruchaseMapper.deleteSource(orderNo,String.valueOf(od_id));
					pruchaseMapper.insertSource(admid,userid,orderNo,goodid,goodsdataid,goods_p_url,goodsurl,newValue,googsimg,goodsprice,oldValue,goodstitle,googsnumber,purchaseCount,od_id,itemid);
					pruchaseMapper.updateOrderDetails(orderNo,od_id);
					i++;
				}
			}else{
				//更新货源信息
				pruchaseMapper.updateSourceInfo(admid,goodid,orderNo,od_id);
			}
			sendMQ.sendMsg(new RunSqlModel("update order_details set purchase_state=1 where orderid='"+orderNo+"' and id='"+od_id+"'"));
			//确认采购LOG日志记录
			List<String> idList=pruchaseMapper.getIdList(orderNo,String.valueOf(od_id));
			if(idList.size()>0){
				pruchaseMapper.udpateLog(admid,orderNo,String.valueOf(od_id),"确认采购");
			}else{
				pruchaseMapper.insertLog(admid,orderNo,String.valueOf(od_id),"确认采购");
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public void AddRecource(Map<String, String> map) {
		String resource=map.get("resource");
		String itemid="0000";
		String sqlll = "";
		String sqlc = "";
		String sqls = "";
		int orderChangeState=0;
		String bargainRemark = "";
		String deliveryRemark = "";
		String colorReplaceRemark = "";
		String sizeReplaceRemark = "";
		String orderNumRemarks = "";
		String questionsRemarks = "";
		String unquestionsRemarks = "";
		if(StringUtil.isNotBlank(resource)){
			resource = TypeUtils.modefindUrl(resource, 1);
		}
		if(resource!=null && !"".equals(resource)){
			itemid = Util.getItemid(resource);
		}
		map.put("itemid",itemid);
		try{
			SendMQ sendMQ=new SendMQ();
			if (resource.contains("1688.com")) {
				resource = resource.substring(0, resource.indexOf(".html") + 5);
			} else if (resource.contains("taobao")) {
				System.out.println("url==" + resource);
				String x = resource.split("\\?")[0];
				String y[] = resource.split("\\?")[1].split("&");
				for (int i = 0; i < y.length; i++) {
					if (y[i].contains("id")) {
						resource = x + "?" + y[i];
					}
				}
			}
			int ii=pruchaseMapper.getOrderChangeState(map);
			int iia=pruchaseMapper.getSourceCount(map);
//			int iib=pruchaseMapper.getGoodsSourceCount(map);
			if (ii != 0) {
				pruchaseMapper.updateGoodsChangeState(map);
			}
			pruchaseMapper.updateGoodsOfSource(map);
			// 问题货源:
			StringBuffer questionBz = new StringBuffer();
			String reason=map.get("reason");
			if (StrUtils.isNotNullEmpty(reason)) {
				String[] reasons = reason.split("//,");
				for (String str : reasons) {
					String bz = str.split(":")[1];
					String bzType = str.split(":")[0];
					if ("砍价情况".equals(bzType)) {
						bargainRemark = bz;
						questionBz.append(bz);
					}else if ("交期偏长".equals(bzType)) {
						deliveryRemark = bz;
						questionBz.append(bz);
					}else if ("颜色替换".equals(bzType)) {
						colorReplaceRemark = bz;
						questionBz.append(bz);
					}else if ("尺寸替换".equals(bzType)) {
						sizeReplaceRemark = bz;
						questionBz.append(bz);
					}else if ("订量问题".equals(bzType)) {
						orderNumRemarks = bz;
						questionBz.append(bz);
					}else if ("有疑问备注".equals(bzType)) {
						questionsRemarks = bz;
						questionBz.append(bz);
					}else if ("无疑问备注".equals(bzType)) {
						unquestionsRemarks = bz;
					}
				}
			}
			map.put("bargainRemark",bargainRemark);
			map.put("deliveryRemark",deliveryRemark);
			map.put("colorReplaceRemark",colorReplaceRemark);
			map.put("sizeReplaceRemark",sizeReplaceRemark);
			map.put("orderNumRemarks",orderNumRemarks);
			map.put("questionsRemarks",questionsRemarks);
			map.put("unquestionsRemarks",unquestionsRemarks);
			map.put("goodsdataid",StringUtil.isBlank(map.get("goodsdataid"))?"":map.get("goodsdataid"));
//			pruchaseMapper.deleteSource(map);
			map.put("purchase_state","0");
			if(StrUtils.isNotNullEmpty(questionBz.toString())){
				map.put("purchase_state","5");
			}
			int row=0;
			if(iia>0){
				//更新货源信息
				row=pruchaseMapper.updateOrderProductSource(map);
			}else{
				//插入货源信息
				pruchaseMapper.deleteSource(map.get("orderNo"),map.get("od_id"));
				row=pruchaseMapper.insertOrderProductSource(map);
			}
			if(row>0){
				int opsId=pruchaseMapper.getOrderProductSource(map);
				map.put("opsId",String.valueOf(opsId));
				pruchaseMapper.addRecodChangeSourceLog(map);
			}
			if("客户已同意替换".equals(map.get("issuree"))){
				pruchaseMapper.updateDetails(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set car_url = '"+map.get("resource")+"' where orderid='"+map.get("orderNo")+"' and id = '"+map.get("od_id")+"'"));
			}
			//查询goods_source是否有该货源记录
			Map<String,String> gMap=pruchaseMapper.queryGoodsSource(map);
			if(gMap != null && gMap.size()>0){
				//更新goodsSource
				map.put("ggoods_pid", gMap.get("goods_pid"));
				map.put("gcar_urlMD5", gMap.get("car_urlMD5"));
				map.put("gshop_id", gMap.get("shop_id"));
				map.put("gaddress",StringUtil.isBlank(map.get("straight_address"))?"":gMap.get("address"));
				pruchaseMapper.updateGoodsSource(map);
			}else{
				OrderDetailsBean odb=pruchaseMapper.queryOrderDetails(map);
				if(odb != null){
					map.put("ogoods_pid",odb.getGoods_pid());
					map.put("ocar_urlMD5",odb.getCar_urlMD5());
					pruchaseMapper.insertGoodsSource(map);
				}
			}
			//将产品表中的店铺ID添加到货源表中
			String cShopId=pruchaseMapper.queryShopId(map);
			if(StringUtil.isNotBlank(cShopId)){
				map.put("cShopId",cShopId);
				pruchaseMapper.updateOrderProduct(map);
			}
			//
			if (map.get("type").equals("2")) {
				List<OrderDetailsBean> odList=pruchaseMapper.getOrderDeatails(map);
				for(OrderDetailsBean o:odList){
//					map.put("odOrderid",o.getOrderid());
//					map.put("odGoodsid",String.valueOf(o.getGoodsid()));
					int opsCount=pruchaseMapper.queryOrderProductSource(o.getOrderid(),String.valueOf(o.getId()));
					if(opsCount>0){
						map.put("o_odid",String.valueOf(o.getId()));
						pruchaseMapper.updateBuyUrl(map);
					}else{
						map.put("newuserid",String.valueOf(o.getUserid()));
						map.put("newgoodsid",String.valueOf(o.getGoodsid()));
						map.put("newgoodsdata_id",String.valueOf(o.getGoodsdata_id()));
						map.put("newcar_img",String.valueOf(o.getCar_img()));
						map.put("newgoodsprice",String.valueOf(o.getGoodsprice()));
						map.put("newgoodsname",String.valueOf(o.getGoodsname()));
						map.put("newyourorder",String.valueOf(o.getYourorder()));
						map.put("newid",String.valueOf(o.getId()));
						pruchaseMapper.insertOrderSource(map);
					}
				}
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}finally {

		}


	}

	@Override
	public Map<String, Object> queryUserIdAndStateByOrderNo(String orderNo) {
		return pruchaseMapper.queryUserIdAndStateByOrderNo(orderNo);
	}
	@Override
	public int insertOrderReplenishment(Map<String, Object> map) {
		map.put("goods_title", map.get("goods_title").toString().replaceAll("'", "&apos;"));
		return pruchaseMapper.insertOrderReplenishment(map);
	}
	@Override
	public int addReplenishmentRecord(Map<String, Object> map) {
		return pruchaseMapper.addReplenishmentRecord(map);
	}
	@Override
	public List<Replenishment_RecordPojo> getIsReplenishments(Map<String, Object> map) {
		return pruchaseMapper.getIsReplenishments(map);
	}
	@Override
	public List<OfflinePurchaseRecordsPojo> getIsOfflinepurchase(Map<String,Object> map){
		return pruchaseMapper.getIsOfflinepurchase(map);
	}
	@Override
	public int updateReplenishmentState(Map<String, Object> map) {
		return pruchaseMapper.updateReplenishmentState(map);
	}

	@Override
	public OrderProductSource ShowRmark(String orderNo, int goodsdataid, int goodid, String odid) {
		return pruchaseMapper.ShowRmark(orderNo,goodsdataid,goodid,odid);
	}

	@Override
	public List<Map<String,String>> allQrNew(String orderid, int adminid) {
		List<Map<String,String>> mapList=pruchaseMapper.getAllNewDatas(orderid,adminid);
		return mapList;
	}

	@Override
	public String allcgqrQrNew(String orderid, int adminid, Integer websiteType) {
		String datas = "";
		StringBuffer bf = new StringBuffer();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> list1=new ArrayList<String>();
		Map<String,String> map=new HashMap<String,String>();
		map.put("orderid",orderid);
		try{
			SendMQ sendMQ=new SendMQ();
			int isDropshipOrder=orderinfoMapper.queyIsDropshipOrder(map);
			int isSendEmail=pruchaseMapper.getIsSendEmail(orderid);
			//查询可以确认采购的商品信息
			List<Map<String,String>> mapList=pruchaseMapper.getAlCgGoodsInfos(orderid,adminid);
			for(Map<String,String> gmap:mapList){
				String odid=String.valueOf(gmap.get("od_id"));
				list1.add(odid);
				List<String> idList=pruchaseMapper.getIdList(orderid,odid);
				if(idList.size()>0){
					pruchaseMapper.udpateLog(adminid,orderid,odid,"确认采购");
				}else{
					pruchaseMapper.insertLog(adminid,orderid,odid,"确认采购");
				}
				pruchaseMapper.updateCgSourceState(orderid,odid,adminid);
				sendMQ.sendMsg(new RunSqlModel(" UPDATE order_details  SET purchase_state=3,purchase_time=now() WHERE id='"+odid+"' and orderid='"+orderid+"'"));
				bf.append(orderid).append(";").append(odid).append(";").append(sdf.format(date)).append("&");
			}

			if (isDropshipOrder == 1) {
				String dropshipid=pruchaseMapper.getDpOrderInfo(orderid);
				if(StringUtil.isNotBlank(dropshipid)){
					pruchaseMapper.updateDpOrderState(dropshipid);

					sendMQ.sendMsg(new RunSqlModel(" update dropshiporder set state= (SELECT IF ((select count(*) from ( select * from order_details " +
							"where dropshipid='"+dropshipid+"' and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"+dropshipid+"'"));
				}
			}
			//获取未更新之前，订单状态和客户ID，比较前后状态是否一致，不一致说明订单状态已经修改
			Map<String,Object> orderInfo = pruchaseMapper.queryUserIdAndStateByOrderNo(orderid);

			pruchaseMapper.updateOrderInfoState(orderid);
			sendMQ.sendMsg(new RunSqlModel("update orderinfo set state= (SELECT IF ((select count(*) from ( select * from order_details where orderid='"+orderid+"' " +
					" and state!=2 AND purchase_state=3) a)>0,'1','5')) where order_no='"+orderid+"'"));

			if(orderInfo != null && orderInfo.size() == 3){
				if(!orderInfo.get("old_state").equals(orderInfo.get("new_state"))){
					//发送消息给客户
					NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderInfo.get("user_id").toString()),orderid,
							Integer.valueOf(orderInfo.get("old_state").toString()),Integer.valueOf(orderInfo.get("new_state").toString()));
				}
			}
			orderInfo.clear();

			datas = bf.toString().length() > 0 ? bf.toString().substring(0,bf.toString().length() - 1) : "";
			map.clear();
			sendMQ.closeConn();
			if(isSendEmail<=0){
				//发送邮件给客户告知已经发货
				IGuestBookService ibs = new GuestBookServiceImpl();
				OrderBean ob=dao.getUserOrderInfoByOrderNo(orderid);
				Map<String,Object> modelM = new HashedMap();
				modelM.put("name",ob.getEmail());
				modelM.put("orderid",orderid);
				modelM.put("recipients",ob.getRecipients());
				modelM.put("street",ob.getStreet());
				modelM.put("street1",ob.getAddresss());
				modelM.put("city",ob.getAddress2());
				modelM.put("state",ob.getStatename());
				modelM.put("country",ob.getCountry());
				modelM.put("zipCode",ob.getZipcode());
				modelM.put("phone",ob.getPhonenumber());
                modelM.put("websiteType", websiteType);
                if (websiteType == 1) {
                    modelM.put("toHref", "https://www.import-express.com/apa/tracking.html?loginflag=false&orderNo=" + orderid + "");
                } else if (websiteType == 2) {
                    modelM.put("toHref", "https://www.kidsproductwholesale.com/apa/tracking.html?loginflag=false&orderNo=" + orderid + "");
                } else if (websiteType == 3) {
                    modelM.put("toHref", "https://www.lovelypetsupply.com/apa/tracking.html?loginflag=false&orderNo=" + orderid + "");
                }
				sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order purchase notice", modelM, TemplateType.PURCHASE);
				//插入发送邮件记录
				pruchaseMapper.insertPurchaseEmail(orderid);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public List<String> getPrePurchaseCount(Map<String, Object> map) {

		return pruchaseMapper.getPrePurchaseCount(map);
	}

	@Override
	public List<PrePurchasePojo> getPrePurchase(Map<String, Object> map) {
		IExpressTrackDao dao1 = new ExpressTrackDaoImpl();
		IOrderwsServer server1 = new OrderwsServer();
		List<PrePurchasePojo> list=pruchaseMapper.getPrePurchase(map);
		for (PrePurchasePojo p : list) {
			int checked=pruchaseMapper.getChecked(p.getOrderid(),map.get("admuserid").toString());//验货无误商品数量
			List<String> problem=pruchaseMapper.getProblem(p.getOrderid(),map.get("admuserid").toString());//验货有误商品数量
			int fp=pruchaseMapper.getFpCount(p.getOrderid(),map.get("admuserid").toString());//该订单分配给采购多少个商品
			int rk=pruchaseMapper.getStorageCount(p.getOrderid(),map.get("admuserid").toString());//该订单分配采购的商品中入库了多少商品
			int cg=pruchaseMapper.getPurchaseCount(p.getOrderid(),map.get("admuserid").toString());//该订单分配给该采购采购了多少商品\
			int bsFlag =pruchaseMapper.checkPurchaseCount(p.getOrderid());//采购数量是否正确标识，不对：1，正确：0。
//            int noShipInfo=dao.getNoShipInfo(p.getOrderid(),map.get("admuserid").toString());
			//查询该订单有多少没有物流信息的商品数
			int index_num=0;
			List<PurchasesBean> list_p=pruchaseMapper.getFpOrderDetails(p.getOrderid(),map.get("admuserid").toString());
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
			String mesStr = "";
			if(bsFlag==1){
				mesStr= "<span style='color:red'>采购数量不对</span>&nbsp";
			}
			p.setOption(mesStr+"<a target='_blank' href='/cbtconsole/warehouse/getSampleGoods?orderid="+p.getOrderid()+"'>建议采样</a>");
			int goods_info=pruchaseMapper.getGoodsInfo(p.getOrderid(),map.get("admuserid").toString());
			//拼接显示页面的订单号信息
			getOrderIdInfo(map, p, fp, goods_info);
		}
		return list;
	}

	private void getOrderIdInfo(Map<String, Object> map, PrePurchasePojo p, int fp, int goods_info) {
		if(goods_info>0){
			if(fp==0){
				p.setOrderid("<a  style='color:green;' target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
						+ "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid")) || "84".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a><img src='/cbtconsole/img/tip1.png' style='margin-left:10px;'></img>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
			}else{
				p.setOrderid("<a  target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
						+ "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid")) || "84".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a><img src='/cbtconsole/img/tip1.png' style='margin-left:10px;'></img>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
			}
		}else{
			if(fp==0){
				p.setOrderid("<a  target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' style='color:green;' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
						+ "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid")) || "84".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
			}else{
				p.setOrderid("<a  target='_blank' title='"+(StringUtils.isStrNull(p.getRemark())?"无":p.getRemark())+"' href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0"
						+ "&admid="+("1".equals(map.get("admuserid")) || "83".equals(map.get("admuserid")) || "84".equals(map.get("admuserid"))?"999":map.get("admuserid"))+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0&userid="+p.getUser_id()+"'>"+p.getOrderid()+"</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='/cbtconsole/img/"+(StringUtils.isStrNull(p.getRemark())?"add_remark.png":"tip_red.png")+"' onclick='addOrderInfo(\""+ p.getOrderid() + "\",\""+ map.get("admuserid").toString() + "\")'  title='添加订单采购备注' style='margin-left:10px;'></img>");
			}
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

	private void getAmounts(Map<String, Object> map, PrePurchasePojo p, int checked, List<String> problem, int fp, int rk, int cg, int index_num) {
		String numUrl="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid="+("1".equals(map.get("admuserid").toString())  || "83".equals(map.get("admuserid")) || "84".equals(map.get("admuserid"))?"999":map.get("admuserid").toString())+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&goodname=&goodid=5201315&search_state=0";
		String num=index_num>0?"<a href='"+numUrl+"' target='_blank' title='点击查看确认采购1天后还没有物流信息商品'>"+index_num+"</a>":"0";
		if(problem.size()>0){
			String toUrl="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid="+("1".equals(map.get("admuserid").toString())  || "83".equals(map.get("admuserid")) || "84".equals(map.get("admuserid"))?"999":map.get("admuserid").toString())+"&orderno="+p.getOrderid()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&goodname=&goodid=5201314&search_state=0";
			p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;background-color:aquamarine'>"+rk+"/"+checked+"/<a href='"+toUrl+"' target='_blank' title='点击查看验货疑问商品'>"+problem.size()+"</a>/"+num+"</span>");
		}else if(cg>=fp && checked<fp){
			p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;background-color:yellow'>"+rk+"/"+checked+"/"+problem.size()+"/"+num+"</span>");
		}else if(checked==fp){
			p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;background-color:#FF00FF'>"+rk+"/"+checked+"/"+problem.size()+"/"+num+"</span>");
		}else{
			p.setAmounts("<span style='font-size:19px;text-align:center;vertical-align:middle;'>"+rk+"/"+checked+"/"+problem.size()+"/"+num+"</span>");
		}
	}

	private int getIndexNum(IExpressTrackDao dao1, IOrderwsServer server1, PrePurchasePojo p, int index_num, List<PurchasesBean> list_p) {
		for (PurchasesBean purchasesBean : list_p) {
			String admName = dao1.queryBuyCount(purchasesBean.getConfirm_userid());
			TaoBaoOrderInfo t = server1.getShipStatusInfo(purchasesBean.getTb_1688_itemid(), purchasesBean.getLast_tb_1688_itemid(),
					purchasesBean.getConfirm_time().substring(0, 10), admName,"",purchasesBean.getOffline_purchase(),p.getOrderid(),purchasesBean.getGoodsid());
			if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {
			}else{
				index_num++;
			}
		}
		return index_num;
	}

	@Override
	public List<PrePurchasePojo> getPrePurchaseForTB(Map<String, Object> map) {
		List<PrePurchasePojo> list=pruchaseMapper.getPrePurchaseForTB(map);
		for (PrePurchasePojo p : list) {
			if(p.getRemark().contains("</br>")){
				p.setStatus("<a target='_blank' style='color:red' href ='javascript:return false;' onclick='return false;'>已处理</a>");
			}else{
				p.setStatus("<a target='_blank' href='/cbtconsole/website/purchase_order_details.jsp?orderid="+p.getOrderid()+"&id="+map.get("admuserid")+"'>处理</a>");
			}
		}
		return list;
	}
	@Override
	public List<PrePurchasePojo> getPrePurchaseForTBCount(Map<String, Object> map) {

		return pruchaseMapper.getPrePurchaseForTBCount(map);
	}

	@Override
	public int insertSources(Map<String, String> map) {
		int row = 0;
		StringBuffer orderid = new StringBuffer();
		try{
			String itemid = Util.getItemid(map.get("taobao_url"));
			if (itemid == null || "".equals(itemid)) {
				itemid = "0000";
			}
			if (map.get("shipno") == null || map.get("shipno").equals("")) {
				map.remove("shipno");
				StringBuffer orderid1 = new StringBuffer();
				for (int i = 0; i < 7; i++) {
					orderid1.append((int) (Math.random() * 100));
				}
				map.put("shipno", orderid1 + itemid);
				for (int i = 0; i < 7; i++) {
					orderid.append((int) (Math.random() * 100));
				}
			} else {
				String tbOrderid=pruchaseMapper.getTbOrderid(map.get("shipno"));
				if (StringUtil.isNotBlank(tbOrderid)){
					orderid.append(tbOrderid);
				} else {
					for (int i = 0; i < 7; i++) {
						orderid.append((int) (Math.random() * 100));
					}
				}
			}
			String tbId=pruchaseMapper.getTbOrderId(map.get("TbOrderid"),map.get("odid"));
			map.put("itemid",itemid);
			if(StringUtil.isNotBlank(tbId)){
				pruchaseMapper.updateTaoBaoOrder(map);
				row = 1;
			}else{
				map.put("orderid",orderid.toString());
				pruchaseMapper.insertTaoBaoOrder(map);
				row = 2;
			}
			map.put("orderid",orderid.toString());
			int a=pruchaseMapper.updateSourceState(map);
			int b=pruchaseMapper.insertRecords(map);
		}catch (Exception e){
			row = 3;
		}
		return row;
	}

	@Override
	public String allQxcgQrNew(String orderid, int adminid) {
		String datas = "";
		StringBuffer bf = new StringBuffer();
		List<String> list_odid=new ArrayList<String>();
		//判断是否为dp订单
		Map<String,String> map=new HashMap<String,String>();
		map.put("orderid",orderid);
		try{
			SendMQ sendMQ=new SendMQ();
			int isDropshipOrder=orderinfoMapper.queyIsDropshipOrder(map);
			//查询可以取消采购的商品
			List<String> odidList=pruchaseMapper.getAllGoodsisList(orderid,adminid);
			for(String odid:odidList){
				list_odid.add(odid);
				//取消采购LOG日志记录
				List<String> idList=pruchaseMapper.getIdList(orderid,String.valueOf(odid));
				if(idList.size()>0){
					pruchaseMapper.udpateLog(adminid,orderid,String.valueOf(odid),"取消采购");
				}else{
					pruchaseMapper.insertLog(adminid,orderid,String.valueOf(odid),"取消采购");
				}
				pruchaseMapper.updateQxSourceState(orderid,odid);
				sendMQ.sendMsg(new RunSqlModel(" UPDATE order_details  SET purchase_state=1,purchase_time=null WHERE id='"+odid+"' and orderid='"+orderid+"'"));
				bf.append(orderid).append(";").append(odid).append("&");
			}

			if (isDropshipOrder == 1) {
				String dropshipid=pruchaseMapper.getDpOrderInfo(orderid);
				if(StringUtil.isNotBlank(dropshipid)){
					pruchaseMapper.updateDpOrderState(dropshipid);
					sendMQ.sendMsg(new RunSqlModel(" update dropshiporder set state= (SELECT IF ((select count(*) from ( select * from order_details " +
							"where dropshipid='"+dropshipid+"' and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"+dropshipid+"'"));
				}
			}
			//获取未更新之前，获取订单状态和客户ID，比较前后状态是否一致，不一致说明订单状态已经修改
			Map<String,Object> orderInfo = pruchaseMapper.queryUserIdAndStateByOrderNo(orderid);

			pruchaseMapper.updateOrderInfoState(orderid);
			sendMQ.sendMsg(new RunSqlModel(" update orderinfo set state= (SELECT IF ((select count(*) from ( select * from order_details where orderid='"+orderid+"' " +
					" and state!=2 AND purchase_state=3) a)>0,'1','5')) where order_no='"+orderid+"'"));

			if(orderInfo != null && orderInfo.size() == 3){
				if(!orderInfo.get("old_state").toString().equals(orderInfo.get("new_state").toString())){
					//发送消息给客户
					NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderInfo.get("user_id").toString()),orderid,
							Integer.valueOf(orderInfo.get("old_state").toString()),Integer.valueOf(orderInfo.get("new_state").toString()));
				}
			}
			orderInfo.clear();

			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		map.clear();
		return bf.toString();
	}

	@Override
	public int useInventory(Map<String, String> map) {
		int row = 0;
		try{
//			SendMQ sendMQ=new SendMQ();
			if("1".equals(map.get("isUse"))){
				//使用库存
				pruchaseMapper.updateLockInventory(map);
			}else{
				pruchaseMapper.updateInventory(map);
				//释放锁定库存后更新线上产品表。预上线表。后台产品表库存标识为无库存
				String goods_pid=pruchaseMapper.getGoodsid(map);
				if(StringUtil.isNotBlank(goods_pid)){
					//更新本地产品表为无库存标识
					map.put("goods_pid",goods_pid);
					pruchaseMapper.updateCustomSstockFlag(map);
//					sendMQ.sendMsg(new RunSqlModel("update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'"));
					GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"1");
				}
				pruchaseMapper.updateDetailsRemark(map);
			}
//			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String allQxQrNew(String orderid, int adminid) {
		String datas = "";
		StringBuffer bf = new StringBuffer();
		try{
			SendMQ sendMQ=new SendMQ();
			List<String> goodsids=pruchaseMapper.getGoodsisList(orderid,adminid);
			for(String odid:goodsids){
				//货源确认LOG日志记录
				List<String> idList=pruchaseMapper.getIdList(orderid,odid);
				if(idList.size()>0){
					pruchaseMapper.udpateLog(adminid,orderid,odid,"取消货源");
				}else{
					pruchaseMapper.insertLog(adminid,orderid,odid,"取消货源");
				}
				//更新货源信息
				pruchaseMapper.updateOrderProSource(orderid,odid);
				//更新order_details状态
				pruchaseMapper.updateDetailsState(orderid,odid);
				sendMQ.sendMsg(new RunSqlModel("UPDATE order_details  SET purchase_state=0 WHERE id='"+odid+"' and orderid='"+orderid+"'"));
				bf.append(orderid).append(";").append(odid).append("&");
			}
			datas = bf.toString().length() > 0 ? bf.toString().substring(0,bf.toString().length() - 1) : "";
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public String getUserName(int adminid) {
		return pruchaseMapper.getUserName(adminid);
	}

	@Override
	public List<ChangeGoodsLogPojo> getDetailsChangeInfo(Map<String, String> map) {
		return pruchaseMapper.getDetailsChangeInfo(map);
	}

	@Override
	public String queryBuyCount(int admuserid) {
		return pruchaseMapper.queryBuyCount(admuserid);
	}

	@Override
	public List<String> getNoShipInfoOrder(Map<String, String> map) {
		return pruchaseMapper.getNoShipInfoOrder(map);
	}

	@Override
	public String getNotShipped(Map<String, Object> map) {
		return pruchaseMapper.getNotShipped(map);
	}
	@Override
	public String getShippedNoStorage(Map<String, Object> map) {
		return pruchaseMapper.getShippedNoStorage(map);
	}
	//当日分配采购种类
	@Override
	public String getDistributionCount(Map<String, Object> map) {
		return pruchaseMapper.getDistributionCount(map);
	}
	@Override
	public String getCgCount(Map<String, Object> map) {

		return pruchaseMapper.getCgCount(map);
	}
	@Override
	public String getSjCgCount(Map<String, Object> map) {
		return pruchaseMapper.getSjCgCount(map);
	}
	@Override
	public List<PurchasesBean> getFpOrderDetails(String orderid, String admuserid) {
		return pruchaseMapper.getFpOrderDetails(orderid,admuserid);
	}
	//获得每月采购数量
	@Override
	public String getMCgCount(Map<String, Object> map) {

		return pruchaseMapper.getMCgCount(map);
	}
	//当月分配种类
	@Override
	public String getfpCount(Map<String, Object> map) {

		return pruchaseMapper.getfpCount(map);
	}
	@Override
	public OrderInfoCountPojo getOrderInfoCountNoitemid(Map<String, Object> map) {
		return pruchaseMapper.getOrderInfoCountNoitemid(map);
	}

	@Override
	public List<PurchasesBean> getOrderInfoCountItemid(Map<String, Object> map) {

		return pruchaseMapper.getOrderInfoCountItemid(map);
	}
	@Override
	public OrderInfoCountPojo getNoMatchOrderByTbShipno(Map<String, Object> map) {

		return pruchaseMapper.getNoMatchOrderByTbShipno(map);
	}

	@Override
	public Page findPageByCondition(String pagenum, String orderid, String admid, String userid, String orderno,
	                                String goodid, String date, String days, String state, int unpaid, int pagesize, String orderid_no_array,
	                                String goodsid, String goodname,String orderarrs,String search_state) {
		Page page=new Page();
		IExpressTrackDao dao1 = new ExpressTrackDaoImpl();
		IOrderwsServer server1 = new OrderwsServer();
		DecimalFormat df = new DecimalFormat("########0.00");
		List<PurchasesBean> pbList = new ArrayList<PurchasesBean>();
		boolean unuseInventory = true;
		try{
			//获取订单信息
			//采购状态：0：未开始采购；1:采购货源确认 2、开始采购 3、已采购，没到货 4 、已入库 5：问题货源 6：已入库，但货有问题  8、出运中， 9、已退货 10、已换货  11：已取消 12替代',
			OrderBean ob = pruchaseMapper.getOrders(orderno);
			if(ob != null){
				String countryNameCn=ob.getCountryNameCN();
				countryNameCn = Utility.getStringIsNull(countryNameCn)?countryNameCn : "USA";
				ob.setCountryNameCN(countryNameCn);
				String dzconfirmtime = ob.getDzConfirmtime();
				ob.setDzConfirmtime(Utility.getStringIsNull(dzconfirmtime)? dzconfirmtime.substring(0, dzconfirmtime.indexOf(" ")) : "");
				ob.setOrderNo(orderno);
				String pay_price_tow =ob.getPay_price_tow();
				ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ? pay_price_tow : "0");
				Address address = new Address();
				String country = "0";
				String new_zid=ob.getNew_zid();
				if (StringUtils.isStrNull(new_zid)) {
					country = StringUtils.isStrNull(ob.getCountry()) ? "0" : ob.getCountry();
				} else {
					country = new_zid;
				}
				// 国家ID
				address.setId(Integer.parseInt(country));
				if(ob.getCountry() != null && ob.getCountry().contains("AFRICA")){
					address.setCountry(StringUtils.isStrNull(ob.getNew_zid()) ? ob.getCountryName():ob.getCountry() .replace(" ", ""));
				}else{
					address.setCountry(StringUtils.isStrNull(ob.getNew_zid()) ? ob.getCountryName():ob.getCountry());
				}
				address.setAddress(ob.getAddresss());
				address.setPhone_number(ob.getPhonenumber());
				address.setZip_code(StringUtils.isStrNull(ob.getZipcode()) || "null".equals(ob.getZipcode()) ? "无": ob.getZipcode());
				address.setStatename(ob.getStatename());
				address.setAddress2(ob.getAddress2());
				address.setRecipients(ob.getRecipients());
				ob.setAddress(address);
				ob.setOrderNumber(ob.getOrdernum() == 1);
				ob.setPay_price(Double.parseDouble(Utility.formatPrice(String.valueOf(ob.getPay_price())).replaceAll(",", "")));
				String foreign_freight_ = ob.getForeign_freight();
				ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
				ob.setEmail(ob.getAdminemail());
				// 提醒，0-客户投诉，1-入库问题，2，-出库问题
				ob.setReminded(new int[]{ob.getComplain(),ob.getPurchase(), ob.getDeliver()});
				String paytype=ob.getPaytypes();
				String tp="支付错误";
				if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
					StringBuilder types=new StringBuilder();
					String [] t=paytype.split(",");
					for(int i=0;i<t.length;i++){
						types.append(StringUtil.getPayType(t[i]));
						if(i != t.length-1){
							types.append(",");
						}
					}
					tp=types.toString();
				}else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
					tp=StringUtil.getPayType(paytype);
				}
				ob.setPaytypes(tp);
			}
			int admin=StringUtils.isStrNull(admid)?111111111:Integer.parseInt(admid);
			int user=StringUtils.isStrNull(userid)?111111111:Integer.parseInt(userid);
			int good=StringUtils.isStrNull(goodid)?111111111:Integer.parseInt(goodid);
			int day=StringUtils.isStrNull(days)?111111111:Integer.parseInt(days);
			int stat=StringUtils.isStrNull(state)?11111111:Integer.parseInt(state);
			StringBuilder tborder=new StringBuilder();
			Calendar cale = Calendar.getInstance();
			cale.add(Calendar.DATE, -1);
			String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cale.getTime());
			String time=yesterday + "23:59:59";

			String dayy = "";
			if (day != 111111111) {
				Date dat = new Date();
				dayy = Util.beforNumDay(dat, -day);
			}else{
				dayy = day + "";
			}
			goodname=goodname == null ? "" : goodname;
			goodsid=goodsid == null ? "" : goodsid;
			orderid_no_array=orderid_no_array == null ? "" : orderid_no_array;
			orderno=orderno == null ? "" : orderno;
			dayy=dayy == null ? "" : dayy;
			date=date == null ? "" : date;
//			good=good==5201314?111111111:good;
			pagesize=(good==5201314 || good==5201315)?500:pagesize;
			int startindex = Integer.parseInt(pagenum);
			if (startindex > 0) {
				startindex = (startindex - 1) * Integer.valueOf(pagesize);
			}else{
				startindex=0;
			}
			int states=Integer.valueOf(search_state)==0?11111111:Integer.valueOf(search_state);
			String fileByOrderid = orderinfoMapper.getFileByOrderid(orderno);
			TabTransitFreightinfoUniteOur fo = new TabTransitFreightinfoUniteOur();
			String shop_id_1688 = "";
			System.out.println("采购详情存储过程查 询 ：call purchase_search_bygoods("+startindex+","+pagesize+","+admin+","+user+",'"+orderno+"',"+(good==5201314 || good==5201315?111111111:good)+",'"+date+"','"+dayy+"',"+states+","+unpaid+",'"+orderid_no_array+"','"+goodsid+"','"+goodname+"')");
			List<Map<String, String>> list=pruchaseMapper.findPageByCondition(startindex,pagesize,admin,user,orderno,good==5201314 || good==5201315?111111111:good,date,dayy,states,unpaid,orderid_no_array,goodsid,goodname);
			if(list.size()>0){
				String modeTranSport = ob.getMode_transport();
				String orderTimeStr = "Epacket";
				String jcex = "JCEX";
				String countryNameCn = "美国";
				countryNameCn = Utility.getStringIsNull(ob.getCountryNameCN()) ? ob.getCountryNameCN() : countryNameCn;
				int isEub = 1;
				if(!StringUtils.isStrNull(modeTranSport) && modeTranSport.indexOf("@") > -1) {
					if(modeTranSport.split("@").length > 1) {
						orderTimeStr = modeTranSport.split("@")[0];
					}
				}
				if(jcex.equals(orderTimeStr)) {
					isEub =0;
				}
				fo=orderinfoMapper.getFreightInfo(countryNameCn, isEub);
				if(fo == null) {
					countryNameCn = "美国";
					fo = orderinfoMapper.getFreightInfo(countryNameCn, isEub);
				}
			}
			
			for(Map<String, String> map:list){
				String rkgoodstatus=map.get("rkgoodstatus").toString().trim();
				if(good==5201314 && (StringUtils.isStrNull(rkgoodstatus) || "0".equals(rkgoodstatus) || "1".equals(rkgoodstatus))){
					continue;
				}
				if(good==5201315){
					if(Integer.valueOf(String.valueOf(map.get("purchase_state")))!=3){
						continue;
					}
					String admName = dao1.queryBuyCount(Integer.valueOf(String.valueOf(map.get("confirm_userid"))));
					TaoBaoOrderInfo t = server1.getShipStatusInfo(String.valueOf(map.get("tb_1688_itemid")), String.valueOf(map.get("last_tb_1688_itemid")),
							String.valueOf(map.get("confirm_time")).substring(0, 10), admName,"",Integer.valueOf(String.valueOf(map.get("offline_purchase")))
							,String.valueOf(map.get("order_no")),Integer.valueOf(String.valueOf(map.get("od_id"))));
					if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {
						continue;
					}
				}
				String sql = "",tb_remark = "";
				PurchasesBean purchaseBean = new PurchasesBean();
				unuseInventory = true;
				int straight_flag=0;
				String address="";
				//获取商品供应商直发地址
				getStraightAddress(shop_id_1688,map,address,straight_flag);
				//获取供应商名字和供应商评级
				getShopInfo(map);
				//将值填入bean中
				getBeanValue(map, purchaseBean);
//				purchaseBean.setStraight_flag(straight_flag);
//				purchaseBean.setStraight_address(StringUtils.isStrNull(address)?"":address);
				purchaseBean.setStraight_flag(Integer.valueOf(String.valueOf(map.get("straight_flag"))));
				purchaseBean.setStraight_address(StringUtils.isStrNull(map.get("shopAddress"))?"":String.valueOf(map.get("shopAddress")));
//				pruchaseMapper.updateOrderDetailsFlag(straight_flag,String.valueOf(map.get("order_no")),String.valueOf(map.get("od_id")));
				setInvoiceVaue(purchaseBean, fileByOrderid);
				
				//获取商品库存数据
				purchaseBean.setInventorySkuId("0");
				String inventoryRemark="";
				//查询该商品是否有使用库存
				purchaseBean.setLock_remaining("0");
				 /*SELECT li.in_id,li.lock_remaining,li.id as li_id,ib.state as ib_state,ib.remark as ib_remark
			        ib.order_barcode as ib_order_barcode
			         FROM lock_inventory li 
			        left join inventory_barcode_record ib on li.id=ib.lock_id  WHERE li.od_id=${odid}*/
				 
				Map<String, Object> useInventoryMap=pruchaseMapper.getUseInventory(purchaseBean.getOd_id());
				if(useInventoryMap != null) {
					String useInventory = com.cbt.util.StrUtils.object2Str(useInventoryMap.get("lock_remaining"));
					if(StringUtil.isNotBlank(useInventory)){
						inventoryRemark="该商品使用了【"+useInventory+"】件库存";
						int ibState = Integer.parseInt(com.cbt.util.StrUtils.object2NumStr(useInventoryMap.get("ib_state")));
						if(ibState == 0) {
							inventoryRemark += ",仓库未确认库存,未完成移出库存操作";
						}else if(ibState == 1){
							inventoryRemark += ",仓库未确认商品取消,未完成移入库存操作";
						}else if(ibState == 2){
							inventoryRemark += ",仓库已确认库存,并完成移出库存";
						}else if(ibState == 3){
							inventoryRemark += ",仓库已确认商品取消,并完成移入库存";
						}else if(ibState == 4){
							inventoryRemark += ",仓库拒绝采购使用库存商品请求";
						}else if(ibState == 5){
							inventoryRemark += ",仓库拒绝商品进入库存请求";
						}
						if(ibState > 3 && StringUtil.isNotBlank(com.cbt.util.StrUtils.object2Str(useInventoryMap.get("ib_remark")))) {
							inventoryRemark +=",原因:"+com.cbt.util.StrUtils.object2Str(useInventoryMap.get("ib_remark"));
						}
						purchaseBean.setLock_remaining(ibState == 4 ? "0":useInventory);
						unuseInventory = false;
						String in_id = com.cbt.util.StrUtils.object2Str(useInventoryMap.get("in_id"));
						purchaseBean.setIn_id(in_id);
					}
					
				}
				purchaseBean.setInventoryRemark(inventoryRemark);
				//查找是否有库存可使用
				getInventoryCount(map, purchaseBean,unuseInventory);
				//查看商品关联的1688订单号
				String shipnos=map.get("shipnos");
				//获取淘宝订单号
				gettbOrderid(tborder, shipnos);
				purchaseBean.setTborderInfo(tborder.toString());
				//获取商品发货后的物流信息
				getShipStatus(time, map, purchaseBean);
				String mode_transport = "", od_country = "",goodtype = "<br/>",cGoodstype = null;
				//获取订单的国家和免邮信息
				getModeTranspor(map,mode_transport,od_country);
				purchaseBean.setMode_transport(mode_transport);
				purchaseBean.setOrderaddress(od_country);
				String str = map.get("odremark");
				String str2 = getOdRemark(str, "");
				purchaseBean.setRemarkpurchase(str2);
				//获取商品的goodsType
				goodtype=getGoodsType(map,goodtype,cGoodstype);
				purchaseBean.setcGoodstype(StringUtils.isStrNull(cGoodstype)?"":cGoodstype.replace("'", ""));
				purchaseBean.setGoods_type(goodtype);
				double fright_=Double.valueOf(StringUtils.isStrNull(map.get("goodsprice"))?"0.00":map.get("goodsprice"));//-rss.getDouble("freight");
				purchaseBean.setGoods_price(String.valueOf(df.format(fright_)));
				String fileimgname = map.get("fileimgname");
				if (StringUtil.isNotBlank(fileimgname)) {
					String t = fileimgname.split(",")[0];
					purchaseBean.setFileimgname(AppConfig.product.substring(0,AppConfig.product.indexOf(":", 12))+ ":90" + t);
				}
				if (StringUtil.isBlank(purchaseBean.getFileimgname())) {
					purchaseBean.setFileimgname(purchaseBean.getGoogs_img());
				}
				StringBuffer sbf = getStringBufferMark(map);
				// 货源问题备注
				purchaseBean.setRemark(sbf.toString() + tb_remark);
				String issure = "", purtime = "", pursure = "",ali_pid="",shop_id="",shop_ids="";
				String pt = map.get("confirm_time");
				int product_state = Integer.valueOf(String.valueOf(map.get("product_state")));
				if(Integer.valueOf(String.valueOf(map.get("purchase_state")))==100){
					purchaseBean.setPurchase_state(0);
				}else{
					purchaseBean.setPurchase_state(Integer.valueOf(String.valueOf(map.get("purchase_state"))));
				}
				purtime=StringUtil.isBlank(pt)?"":pt.substring(0, 19);
				String times=StringUtils.isStrNull(map.get("purchasetime"))?"":map.get("purchasetime");
				if(Integer.valueOf(String.valueOf(map.get("purchase_state")))==3){
					times=StringUtils.isStrNull(map.get("purchasetime"))?map.get("confirm_time"):map.get("purchasetime");
				}
				purchaseBean.setPurchasetime(times);
				String goods_pid=String.valueOf(map.get("goods_pid"));
				String car_urlMD5 = map.get("car_urlMD5");
				List<String> shop_id_b=new ArrayList<String>();
				if(Integer.valueOf(String.valueOf(map.get("isDropshipOrder")))==3){
					ali_pid="";
					shop_id="";
				}else{
					shop_id=map.get("shop_id");
					if(StringUtils.isStrNull(shop_id) || "-".equals(shop_id)){
						shop_id="0000";
					}else if(!StringUtils.isStrNull(shop_id) && !"null".equals(shop_id) && shop_id.contains("\\//")){
						shop_id=shop_id.split("\\//")[1].split("\\.")[0];
					}
					//补货
//					if(Integer.valueOf(String.valueOf(map.get("is_replenishment")==null?"0":map.get("is_replenishment")))==1){
//						shop_id_b=pruchaseMapper.getBhShopId(String.valueOf(map.get("order_no")),String.valueOf(map.get("goodsid")));
//					}
				}
				purchaseBean.setBh_shop_id(shop_id_b);
				purchaseBean.setAli_pid(ali_pid);
				if(org.apache.commons.lang.StringUtils.isBlank(shop_id_1688)){
					shop_id_1688=shop_id;
				}
				purchaseBean.setShop_id(shop_id_1688);
//				//店铺对应的级别
////				String level = pruchaseMapper.supplierScoringLevel(shop_id);
////				purchaseBean.setLevel(level);
				purchaseBean.setGoods_pid(goods_pid);
				purchaseBean.setCar_urlMD5(car_urlMD5);
				//获取商品产品单页链接
				getGoodsUrl(map, purchaseBean, goods_pid, car_urlMD5);
				String goods_p_url=map.get("newValue")==null?"":map.get("newValue");
				if("".equals(goods_p_url)){
//					goods_p_url=purchaseBean.getGoods_url();
					//新订单采购货源 为最后一次采购来源
					goods_p_url=pruchaseMapper.getLastPurchaseSource(goods_pid);
					if(StringUtils.isEmpty(goods_p_url)){
						goods_p_url=purchaseBean.getGoods_url();
					}
				}
				purchaseBean.setNewValue(goods_p_url.replace("'", " "));
				getSourceOfGoods(map, purchaseBean);
				String cn = map.get("companyname");
				purchaseBean.setCompanyName(StringUtil.isNotBlank(cn)?cn:"无");
				//获取商品单位
				getGoodsUnits(map, purchaseBean);
				String admin1 = String.valueOf(map.get("admin"));
				if (!"  ".equals(admin1) && admin1 != null && admin1.length() < 3) {
					PurchaseServer purchaseServer = new PurchaseServerImpl();
					String aduser = purchaseServer.getUserbyID(admin1);
					purchaseBean.setAdmin(aduser);
				}
				String isDropshipOrder1 = String.valueOf(map.get("isDropshipOrder"));
				if (!"  ".equals(isDropshipOrder1) && isDropshipOrder1 != null && admin1.length() > 0) {
					purchaseBean.setIsDropshipOrder(isDropshipOrder1);
				}
				//商品销售单价
				String goods_price = purchaseBean.getGoods_price();
				//商品重量(加购物车)
				String car_weight=map.get("car_weight");
				car_weight=StringUtil.isBlank(car_weight)?"0":car_weight;
				//预估国际单品运费
				double freightFee =ob.getFreightFee();
				//iOrderinfoService.getFreightFee(car_weight, ob);
				//实际预估采购单品金额
				String es_price="0.00";
				es_price=pruchaseMapper.getEsPrice(String.valueOf(map.get("order_no")),String.valueOf(map.get("od_id")));
				es_price = getEsPrice(es_price);
				double esPrice=Double.parseDouble(es_price);
				String oldValue = purchaseBean.getOldValue();
				// 【预估单品利润金额RMB（预估单品利润率%）】=客户实际支付单品金额-实际预估采购单品金额-预估国际单品运费
				purchaseBean.setExchange_rate(String.valueOf(map.get("exchange_rate")));
				if(Double.valueOf(goods_price)<=0){
					purchaseBean.setProfit("0.00(<span style=color:red>0</span>)");
				}else{
					double p=Double.valueOf(goods_price)*Double.parseDouble(String.valueOf(map.get("exchange_rate")))-esPrice-freightFee;
					String fit=df.format(p/(Double.valueOf(goods_price)*Double.parseDouble(String.valueOf(map.get("exchange_rate")))));
					purchaseBean.setProfit(df.format(p)+"(<span style=color:"+(Double.parseDouble(fit)<=0?"red":"green")+">"+fit+"</span>)");
				}
				int od_state = Integer.valueOf(String.valueOf(map.get("od_state")==null?"0":map.get("od_state")));
				int purchase_state = Integer.valueOf(String.valueOf(map.get("purchase_state")==null?"0":map.get("purchase_state")));
				issure = getIssure(map, issure, product_state, od_state, purchase_state);
				purchaseBean.setIssure(issure);
				purchaseBean.setOriginalGoodsUrl("");
				purchaseBean.setPuechaseTime(purtime);
				String querneGoods = "";
				String remarkAgainBtn = "";
				//拼接按钮
				Map<String,String> bMap=getRemarksValue(querneGoods,pursure,remarkAgainBtn,purchaseBean.getOistate(),purchaseBean.getOdstate(),map,purchase_state);
				querneGoods=bMap.get("querneGoods");
				remarkAgainBtn=bMap.get("remarkAgainBtn");
				pursure=bMap.get("pursure");
				// 13 客户同意替换
				if (od_state == 13) {
					purchaseBean.setNewValue(map.get("car_url"));
					purchaseBean.setIssure("客户已同意替换");
				}
				purchaseBean.setQuerneGoods(querneGoods);
				purchaseBean.setRemarkAgainBtn(remarkAgainBtn);
				String strProductValue = "";
				strProductValue = map.get("productchange");
				if (strProductValue.equals("1")) {
					purchaseBean.setProductState("商品已被取消，请勿采购");
					pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='采购确认' class='f' disabled='disabled'/>";
				}
				purchaseBean.setPurchaseSure(pursure);
				String adtime = String.valueOf(map.get("addtime")==null?"":map.get("addtime"));
				String addtime = StringUtil.isBlank(adtime)?"":adtime.substring(0, 19);
				purchaseBean.setAddtime(addtime);
				if (purchaseBean.getImg_type() != null) {
					String temp = purchaseBean.getImg_type();
					if (temp.indexOf(".jpg") != -1) {
						temp = temp.substring(0, temp.indexOf(".jpg") + 4);
					} else if (temp.indexOf(".png") != -1) {
						temp = temp.substring(0, temp.indexOf(".png") + 4);
					}
					purchaseBean.setImg_type(temp);
				}
				if(StringUtil.isBlank(purchaseBean.getTb_1688_itemid()) || purchaseBean.getGoods_pid().equals(purchaseBean.getTb_1688_itemid())){
					purchaseBean.setShopFlag("1");
				}else{
					purchaseBean.setShopFlag("0");
				}
				returndisplay re=this.pruchaseMapper.getApplyTime(purchaseBean.getOrderNo(),purchaseBean.getGoods_pid());
				if (re !=null) {
					String StDate = re.getApplyUser() + "于" + re.getApplyTime() + "退货";
					purchaseBean.setReturnTime(StDate);
				}
				pbList.add(purchaseBean);
				total = Integer.parseInt(map.get("totalCount")==null?"0": String.valueOf(map.get("totalCount")));
			}
			// 获取数据
			List<ShopGoodsSalesAmount> shopGoodsSalesAmountList =  customGoodsMapper.queryShopGoodsSalesAmountAll();
			Set<String> pid_list=new HashSet<String>();
			for(int i=0;i<pbList.size();i++){
				genShopPrice(pbList.get(i),shopGoodsSalesAmountList);
				PurchasesBean shipBean=this.customGoodsMapper.FindShipnoByOdid(pbList.get(i));
				if (shipBean !=null) {
					pbList.get(i).setShipnoid(shipBean.getShipnoid());
					pbList.get(i).setTborderid(shipBean.getTborderid());
					if ("undefined".equals(shipBean.getShipnoid())&&shipBean.getTborderid()!=null){
						String shipnum=this.customGoodsMapper.FindShipnoByTbor(shipBean.getTborderid());
						pbList.get(i).setShipnoid(shipnum);
					}
				}
				pid_list.add(pbList.get(i).getGoods_pid());
			}
			shopGoodsSalesAmountList.clear();
			double pid_amount=0;
			if(list.size()>0){
				pid_amount=pid_list.size()*5;
			}
			page.setPid_amount(pid_amount);
			page.setRecords(pbList);
			page.setTotalrecords("5201314".equals(goodid) || "5201315".equals(goodid)?list.size():total);
			//标记该订单销售发送消息已读
			pruchaseMapper.updateGoodsCommunicationInfo(orderno,admin);
			page.setPagenum(Integer.parseInt(pagenum));
			int totalpage = total % pagesize == 0 ? total / pagesize: (total / pagesize + 1);
			page.setTotalpage("5201314".equals(goodid) || "5201315".equals(goodid)?1:totalpage);
//			Map<Object,Object> map =new HashMap<Object,Object>();
			List<StraightHairPojo> list_stra= pruchaseMapper.straightHairList();
			try{
				SendMQ sendMQ = new SendMQ();
				updateDetailsState(list_stra, sendMQ);
				sendMQ.closeConn();
			}catch (Exception e){
				System.out.println("MQ错误。。。。。。。。。。。");
			}finally {
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return page;
	}

	private void genShopPrice(PurchasesBean purchasesBean,List<ShopGoodsSalesAmount> shopGoodsSalesAmountList){
		for(ShopGoodsSalesAmount salesAmount : shopGoodsSalesAmountList){
			if(salesAmount.getShopId().equals(purchasesBean.getGoodsShop())){
				purchasesBean.setGoodsShopPrice(salesAmount.getTotalPrice());
				break;
			}
		}
	}

	/**
	 * 获取订单的国家和免邮信息
	 * @param map
	 * @param mode_transport
	 * @param od_country
	 */
	public void getModeTranspor(Map<String,String> map,String mode_transport,String od_country){
		if (map.get("mode_transport") != null && !map.get("mode_transport").equals("")) {
			mode_transport = "非免邮";
			String[] mode_transport_ = map.get("mode_transport").split("@");
			for (int j = 0; j < mode_transport_.length; j++) {
				if (j == mode_transport_.length - 2 && Utility.getIsDouble(mode_transport_[mode_transport_.length - 2])) {
					if (Double.parseDouble(mode_transport_[mode_transport_.length - 2]) == 0) {
						mode_transport_[mode_transport_.length - 1] = "免邮";
						mode_transport = "免邮";
					}
				}
				if (j == mode_transport_.length - 3) {
					od_country = mode_transport_[mode_transport_.length - 3];
				}
			}
		}
	}

	public String getGoodsType(Map<String,String> map,String goodtype,String cGoodstype){
		if (map.get("goods_type") != null) {
			String types = map.get("goods_type");
			String types1 = "";
			if (types.indexOf("<") > -1 && types.indexOf(">") > -1) {
				for (int j = 1; j < 4 && types.indexOf("<") > -1 && types.indexOf(">") > -1; j++) {
					types1 = types.substring(types.indexOf("<"),types.indexOf(">") + 1);
					types = types.replace(types1, "");
				}
			}
			cGoodstype = types;
			if ((types).contains("@")) {
				String[] gdtp = (types).split(",");
				for (String ty : gdtp) {
					if (!(ty == null || "".equals(ty.trim())) && ty.indexOf("@") > 0) {
						goodtype = goodtype+ ty.substring(0, (ty.indexOf("@")))+ ";<br/>";
					}
				}
			} else if ((types).contains(",")) {
				goodtype = types.replace(",", ";<br/>");
			} else {
				goodtype = types;
			}
		}
		if(StringUtil.isBlank(goodtype) || "null".equals(goodtype)){
			goodtype="-";
		}
		return goodtype;
	}

	private String getOdRemark(String str, String str2) {
		int i = 0;
		if (str != null && str.length() > 30) {
			for (; i < str.length() / 30; i++) {
				str2 += str.substring(i * 30, (i + 1) * 30)+ "</br>";
			}
			if (i > 0) {
				str2 += str.substring(i * 30, str.length());
			}
		} else {
			str2 = str;
		}
		return str2;
	}

	private void getShipStatus(String time, Map<String, String> map, PurchasesBean purchaseBean) {
		if(Integer.valueOf(String.valueOf(map.get("purchase_state")))==3 && map.get("confirm_time").compareTo(time)<0){
			String admName = orderinfoMapper.queryBuyCount(Integer.valueOf(String.valueOf(map.get("confirm_userid"))));
			TaoBaoOrderInfo t = iOrderinfoService.getShipStatusInfo(String.valueOf(map.get("tb_1688_itemid")), String.valueOf(map.get("last_tb_1688_itemid")),
					String.valueOf(map.get("confirm_time")), admName,"",Integer.valueOf(String.valueOf(map.get("offline_purchase"))),String.valueOf(String.valueOf(map.get("order_no"))),Integer.valueOf(String.valueOf(map.get("goodsid"))));
			if(t==null){
				//1.点了采购确认但没有1688订单
				purchaseBean.setShipstatus("没有匹配到采购订单或者还未发货");
			}else if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {
                purchaseBean.setTborderid(t.getOrderid());
                purchaseBean.setShipnoid(t.getShipno());
				String shipstatus = t.getShipstatus().split("\n")["2".equals(t.getTbOr1688())?0:t.getShipstatus().split("\n").length - 1];
				if("2".equals(t.getTbOr1688()) && !"等待买家确认收货".equals(shipstatus)){
					String msg=t.getShipstatus().split("\n")[1];
					purchaseBean.setShipstatus(shipstatus+"\n "+msg);
					if (StringUtil.isNotBlank(t.getOrderid())){
						purchaseBean.setShipno(t.getOrderid());
					}
				}else{
					purchaseBean.setShipstatus(shipstatus);
					if (StringUtil.isNotBlank(t.getOrderid())){
						purchaseBean.setShipno(t.getOrderid());
					}
				}
			}else if(t!=null && !StringUtils.isStrNull(t.getShipno()) && StringUtils.isStrNull(t.getShipstatus())){
				//点了采购确认 有了1688订单但是没有发货
				purchaseBean.setShipstatus("已发货系统未抓到物流信息");
				if (StringUtil.isNotBlank(t.getOrderid())){
					purchaseBean.setShipno(t.getOrderid());
				}
			}else{
				purchaseBean.setShipstatus("没有匹配到采购订单或者还未发货");
			}
			if(t!=null &&  !StringUtils.isStrNull(t.getSupport_info()) && t.getSupport_info().length()>5 && t.getSupport_info().indexOf(";")>-1){
				String imgs[]=t.getSupport_info().split(";");
				StringBuilder b=new StringBuilder();
				for (String s : imgs) {
					if(!StringUtils.isStrNull(s) && s.indexOf("https")>=0){
						String s1=s.indexOf("&")>=0?s.split("&")[0]:s;
						String msg=s.indexOf("&")>=0?s.split("&")[1]:"";
						b.append("<img src='"+s1+"' title='"+msg+"'/>");
					}
				}
				purchaseBean.setSupport_info(b.toString());
			}
		}else{
			purchaseBean.setShipstatus("");
		}
	}

	private void gettbOrderid(StringBuilder tborder, String shipnos) {
		if(!"0".equals(shipnos) && !"undefined".equals(shipnos) && StringUtil.isNotBlank(shipnos)){
			if(shipnos.indexOf(",")>-1){
				String [] str=shipnos.split(",");
				StringBuilder sb=new StringBuilder();
				for(String s:str){
					sb.append("'"+s+"',");
				}
				shipnos=sb.toString().substring(0,sb.toString().length()-1);
			}else{
				shipnos="'"+shipnos+"'";
			}
			List<Map<String,String>> tMap=pruchaseMapper.getTaoBaoInfoByShipno(shipnos);
			for(Map<String,String> t:tMap){
				if(!tborder.toString().contains(t.get("orderid"))){
					tborder.append(t.get("orderid")).append(":￥").append(t.get("totalprice")).append(";");
				}
			}
		}
	}

	/**
	 * 获取供应商直发地址
	 * @param shop_id_1688
	 * @param map
	 * @param address
	 * @param straight_flag
	 */
	/*public void getStraightAddress(String shop_id_1688,Map<String,String> map,String address,int straight_flag){
		String level="";
		String s_id="";
		String quality="";
		if(Integer.valueOf(String.valueOf(map.get("straight_flag"))) == 0){
			if(map.get("shop_id") != null){
				s_id=map.get("goodsShop");
			}else{
				s_id="0000";
			}
			Map<String, String> shopMap=pruchaseMapper.getShopIdByGoodsPid(String.valueOf(map.get("goodsShop")));
			if(shopMap != null){
				address=shopMap.get("address");
				level=shopMap.get("level");
				quality=shopMap.get("quality_avg");
				if(!StringUtils.isStrNull(address) && !StringUtils.isStrNull(level) && address.contains("广东") && "优选供应商".equals(level)){
					straight_flag=1;
				}
			}
		}
		map.put("level",StringUtil.isBlank(level)?"":level);
		map.put("shopAddress",address);
		map.put("quality",quality);
		map.put("straight_flag",String.valueOf(straight_flag));
	}*/

	
	/**
	 * 获取供应商直发地址
	 * @param shop_id_1688
	 * @param map
	 * @param address
	 * @param straight_flag
	 */
	public void getStraightAddress(String shop_id_1688,Map<String,String> map,String address,int straight_flag){
		String level="";
		String s_id="";
		String quality="";
//		if(Integer.valueOf(String.valueOf(map.get("straight_flag"))) == 0){
			if(map.get("shop_id") != null){
				s_id=map.get("goodsShop");
			}else{
				s_id="0000";
			}
			Map<String, String> shopMap=pruchaseMapper.getShopIdByGoodsPid(String.valueOf(map.get("goodsShop")));
			if(shopMap != null){
				address=shopMap.get("address");
				level=shopMap.get("level");
				quality=shopMap.get("quality_avg");
				if(Integer.valueOf(String.valueOf(map.get("straight_flag"))) == 0){
					if(!StringUtils.isStrNull(address) && !StringUtils.isStrNull(level) && (address.contains("广东") || address.contains("义乌")) && "直发供应商".equals(level)){
						straight_flag=1;
					}
				}else{
					straight_flag = Integer.valueOf(String.valueOf(map.get("straight_flag")));
				}
				
			}
//		}
		map.put("level",StringUtil.isBlank(level)?"":level);
		map.put("shopAddress",address);
		map.put("quality",quality);
		map.put("straight_flag",String.valueOf(straight_flag));
	}

	/**
	 * 获取供应商名字和供应商评级
	 * @param map
	 */
	public void getShopInfo(Map<String,String> map){
		String shopName="";
		String supplyGrade="";

		Map<String, String> shopMap=pruchaseMapper.getShopInfo(String.valueOf(map.get("goodsShop")));
		if(shopMap != null){
            shopName=shopMap.get("shop_name");
            supplyGrade=shopMap.get("supply_grade");
		}
		map.put("shopName",shopName);
		map.put("supplyGrade",supplyGrade);

	}


	private void setInvoiceVaue(PurchasesBean purchaseBean, String fileByOrderid) {
		if (fileByOrderid == null || fileByOrderid.length() < 10) {
			purchaseBean.setInvoice(0);
		}else if (fileByOrderid.indexOf(".pdf") > -1){
			purchaseBean.setInvoice(1);
		}else {
			purchaseBean.setInvoice(2);
		}
	}

	private StringBuffer getStringBufferMark(Map<String, String> map) {
		String mk;
		StringBuffer sbf = new StringBuffer();
		if (map.get("bargainRemark") != null && !(map.get("bargainRemark")).equals("")) {
			mk = "砍价情况:" + String.valueOf(map.get("bargainRemark")) + " ;";
			sbf.append(mk);
		}
		if (map.get("deliveryRemark") != null && !(map.get("deliveryRemark")).equals("")) {
			mk = "交期偏长:" + String.valueOf(map.get("deliveryRemark")) + " ;";
			sbf.append(mk);
		}
		if (map.get("colorReplaceRemark") != null && !(map.get("colorReplaceRemark")).equals("")) {
			mk = "颜色替换:" + String.valueOf(map.get("colorReplaceRemark"))+ " ;";
			sbf.append(mk);
		}
		if (map.get("sizeReplaceRemark") != null && !(map.get("sizeReplaceRemark")).equals("")) {
			mk = "尺寸替换:" + String.valueOf(map.get("sizeReplaceRemark"))+ " ;";
			sbf.append(mk);
		}
		if (map.get("orderNumRemarks") != null && !(map.get("orderNumRemarks")).equals("")) {
			mk = "订量问题:" + String.valueOf(map.get("orderNumRemarks")) + " ;";
			sbf.append(mk);
		}
		if (map.get("questionsRemarks") != null && !(map.get("questionsRemarks")).equals("")) {
			mk = "疑问备注:" + String.valueOf(map.get("questionsRemarks")) + " ;";
			sbf.append(mk);
		}
		if (map.get("unquestionsRemarks") != null && !(map.get("unquestionsRemarks")).equals("")) {
			mk = "无疑问备注:" + String.valueOf(map.get("unquestionsRemarks"))+ " ;";
			sbf.append(mk);
		}
		if (map.get("againRemarks") != null && !(map.get("againRemarks")).equals("")) {
			mk = "再次备注:" + String.valueOf(map.get("againRemarks")) + " ;";
			sbf.append(mk);
		}
		if(Integer.valueOf(String.valueOf(map.get("lock_remaining1")==null?"0":map.get("lock_remaining1")))>0){
			mk = "使用库存【"+String.valueOf(map.get("lock_remaining1"))+"】件";
			sbf.append(mk);
		}
		return sbf;
	}

	private void getGoodsUrl(Map<String, String> map, PurchasesBean purchaseBean, String goods_pid, String car_urlMD5) {
		String new_car_url="";
		String old_url = map.get("old_url").trim();
		if(!StringUtils.isStrNull(map.get("car_url"))){
			new_car_url=map.get("car_url");
		}else if(!StringUtils.isStrNull(old_url)){
			new_car_url=old_url;
		}else if(StringUtils.isStrNull(map.get("car_url")) && !StringUtils.isStrNull(car_urlMD5)){
			new_car_url="https://www.import-express.com/product/detail?&source="+car_urlMD5+"&item="+goods_pid;
		}
		new_car_url= StringUtil.getNewUrl(new_car_url,goods_pid,car_urlMD5);
		purchaseBean.setImportExUrl(new_car_url);
		if(car_urlMD5 == null || "".equals(car_urlMD5)){
			purchaseBean.setGoods_url("https://detail.1688.com/offer/"+goods_pid+".html");
		}else{
			if(car_urlMD5.substring(0, 1).equals("D")){
				//电商网站链接
				purchaseBean.setGoods_url("https://detail.1688.com/offer/"+goods_pid+".html");
			}else if(car_urlMD5.substring(0, 1).equals("M")){
				purchaseBean.setGoods_url("https://www.amazon.com/"+(map.get("goods_title")==null?"a":map.get("goods_title").replace("'", ""))+"/dp/"+goods_pid);
			}else if(car_urlMD5.substring(0, 1).equals("A")){
				purchaseBean.setGoods_url("http://www.aliexpress.com/item/a/"+goods_pid+".html");
			}else{
				purchaseBean.setGoods_url(map.get("car_url"));
			}
		}
	}

	private void getInventoryCount(Map<String, String> map, PurchasesBean purchaseBean,boolean unUseInventory) {
		purchaseBean.setInventory("0");
		purchaseBean.setInventorySkuId("0");
		purchaseBean.setRemaining("0");
		purchaseBean.setNew_remaining("0");
		Map<String, Object> inventory = inventoryMapper.getInventoryByOrderDetialsId(String.valueOf(map.get("od_id")));
		if(inventory != null) {
			purchaseBean.setSkuid((String)inventory.get("skuid"));
			purchaseBean.setSpecid((String)inventory.get("specid"));
			if(unUseInventory) {
				String can_remaining = com.cbt.util.StrUtils.object2NumStr(inventory.get("can_remaining"));
				purchaseBean.setInventory(can_remaining);
				purchaseBean.setInventorySkuId(com.cbt.util.StrUtils.object2NumStr(inventory.get("inventory_id")));
				purchaseBean.setRemaining(can_remaining);
				purchaseBean.setNew_remaining(can_remaining);
			}
		}
		/*String nm = null;
		try {
			nm = map.get("inventory");
			int s = nm.length();
			if (s > 10) {
				nm = "0";
				purchaseBean.setInventory(nm);
			} else {
				purchaseBean.setInventory(nm);
			}

		} catch (Exception e) {
			nm = "0";
			purchaseBean.setInventory(nm);
		}*/
	}

	private void getGoodsUnits(Map<String, String> map, PurchasesBean purchaseBean) {
		try {
			String seilUnit_ = String.valueOf(map.get("seilUnit"));
			seilUnit_ = seilUnit_.trim();
			String goodsUnit_ = String.valueOf(map.get("goodsUnit"));
			goodsUnit_ = goodsUnit_.trim();
			if (seilUnit_.indexOf("(") > -1) {
				seilUnit_ = seilUnit_.substring(seilUnit_.indexOf("("));
			}
			if (seilUnit_ == goodsUnit_) {
				seilUnit_ = null;
			}
			purchaseBean.setSeilUnit(seilUnit_);
			purchaseBean.setGoodsUnit(goodsUnit_);
		} catch (Exception e) {
			LOG.info("该订单的产品单位不存在：" + String.valueOf(map.get("order_no"))+ "," + String.valueOf(map.get("goodsid")));
		}
	}

	private void getSourceOfGoods(Map<String, String> map, PurchasesBean purchaseBean) {
		String sog;
		try {
			sog = map.get("source_of_goods");
			if (sog.length() > 10) {
				sog = "0";
				purchaseBean.setSource_of_goods(Integer.valueOf(sog));
			} else {
				purchaseBean.setSource_of_goods(Integer.valueOf(sog));
			}
		} catch (Exception e2) {
			sog = "0";
			purchaseBean.setSource_of_goods(Integer.valueOf(sog));
		}
	}

	private String getEsPrice(String es_price) {
		if(StringUtil.isNotBlank(es_price)){
			if(es_price.indexOf(",")>-1) {
				String prices=es_price.split(",")[0].replace("[","").replace(" ","").trim();
				if(prices.indexOf("$") > -1){
					es_price =prices .split("\\$")[1];
				}else if(prices.indexOf("￥") > -1){
					es_price =prices .split("￥")[1];
				}
			}else if(es_price.indexOf("[")>-1){
				es_price=es_price.replace("[","").replace("]","").replace(" ","");
				if(es_price.indexOf("$") > -1){
					es_price =es_price .split("\\$")[1];
				}else if(es_price.indexOf("￥") > -1){
					es_price =es_price .split("￥")[1];
				}
			}
			if(es_price.indexOf("-")>-1){
				es_price=es_price.split("-")[0];
			}
		}else{
			es_price="0.00";
		}
		return es_price;
	}

	private void updateDetailsState(List<StraightHairPojo> list_stra, SendMQ sendMQ) throws Exception {
		for (StraightHairPojo s : list_stra) {
			if(StringUtil.isNotBlank(s.getStates()) && Integer.valueOf(s.getStates())>0){
				s.setStates("<span style='color:green'>已签收</span>");
//				map.put("orderid",s.getOrderid());
//				map.put("goodsid",s.getGoodsid());
				pruchaseMapper.updateState(s.getOrderid(),s.getGoodsid());
				sendMQ.sendMsg(new RunSqlModel("update order_details set state=1,checked=1 where orderid='"+s.getOrderid()+"' and goodsid='"+s.getGoodsid()+"'"));
				Map<String,Integer> qMap=pruchaseMapper.queryState(s.getOrderid());
				if(qMap != null && qMap.get("states")>0 && qMap.get("states").equals(qMap.get("counts"))){
					pruchaseMapper.updateOrderInfo(s.getOrderid());
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set  state=2 where order_no='"+s.getOrderid()+"'"));
					//发送消息给客户,订单状态
					NotifyToCustomerUtil.updateOrderState(Integer.valueOf(s.getUserid()),s.getOrderid(),1,2);
				}
				sendMQ.closeConn();
			}
		}
	}

	private String getIssure(Map<String, String> map, String issure, int product_state, int od_state, int purchase_state) {
		if (map.get("newValue") == null || String.valueOf(map.get("newValue")).equals("")) {
			if (purchase_state == 12) {
				issure = "正在讨论替代";
			}
		} else {
			if (product_state == 1) {
				issure = "历史货源";
				if (purchase_state == 12) {
					issure = "正在讨论替代";
				} else if (purchase_state == 13 || od_state == 13) {
					issure = "客户已同意替换";
				} else if (purchase_state == 14) {
					issure = "不同意替换";
				}
			} else if (product_state == 0) {
				if (purchase_state == 1) {
					issure = "货源已确认";
				} else if (purchase_state == 3) {
					issure = "货源已采购";
				} else if (purchase_state == 3) {
					issure = "货源已入库";
				} else if (purchase_state == 12) {
					issure = "正在讨论替代";
				} else if (purchase_state == 13 || od_state == 13) {
					issure = "客户已同意替换";
				} else if (purchase_state == 14) {
					issure = "不同意替换";
				}
			}
		}
		return issure;
	}

	public Map<String,String> getRemarksValue(String querneGoods,String pursure,String remarkAgainBtn,String oistate,String odstate,Map<String,String> map,int purchase_state){
		Map<String,String> vMap=new HashMap<String,String>();
		if("6".equals(oistate) || "-1".equals(oistate) || "2".equals(odstate)){
			//订单被取消
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='货源确认' class='f' disabled='disabled'/>";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='采购确认' class='f' disabled='disabled'/>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("goodsid"))+ ")' disabled='disabled' />";
		}else if (purchase_state == 0) {
			//还没有开始采购
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='采购确认' class='f' disabled='disabled'/>";
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='货源确认' class='f'/>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("goodsid")) + ")' />";
		}else if (purchase_state == 1) {
			// 采购货源确认
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='取消货源' class='f'/>";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='采购确认' class='f'/>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("goodsid")) + ")'/>";
		}else if (purchase_state == 3) {
			// 已采购，没到货
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='取消货源' class='f' disabled='disabled'/>";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='取消采购' class='f' />";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("od_id")) + ")'/>";
		}else if(purchase_state == 4 || purchase_state == 6 || purchase_state == 8){
			//已入库
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='取消货源' class='f' disabled='disabled'/>";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='取消采购' class='f' disabled='disabled'/>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("od_id"))+ ")' disabled='disabled' />";
		}else if (purchase_state == 5) {
			// 货源有问题
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='货源确认' class='f'";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='采购确认' class='f' disabled='disabled'//>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("od_id")) + ")' />";
		} else if (purchase_state == 12) {
			// 正在讨论替换
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='货源确认' class='f' disabled='disabled'";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("od_id"))+ "' value='采购确认' class='f' disabled='disabled'/>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("od_id"))+ ")' disabled='disabled' />";
		} else if (purchase_state == 13) {
			// 客户同意替换 || od_state==13
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("goodsid"))+ "' value='货源确认' class='f'";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("goodsid"))+ "' value='采购确认' class='f' />";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("od_id")) + ")'/>";
		} else if (purchase_state == 100) {
			//还没有录入货源
			querneGoods = "<input type='button' id='hyqr"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("goodsid"))+ "' value='货源确认'  class='f'";
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("goodsid"))+ "' value='采购确认' disabled='disabled' class='f' disabled='disabled'/>";
			remarkAgainBtn = "<input class='remarkBtn' type='button' value='再次备注' onclick='remarkAgain(\""+ String.valueOf(map.get("order_no"))+ "\","+ String.valueOf(map.get("od_id"))+ ","+ String.valueOf(map.get("od_id")) + ")'";
			if(map.get("inventory_remark")!=null && String.valueOf(map.get("inventory_remark")).indexOf("库存充足")>-1){
				querneGoods+=" disabled='disabled'";
				remarkAgainBtn+=" disabled='disabled'";
			}
			querneGoods+="/>";
			remarkAgainBtn+="/>";
		}
		vMap.put("querneGoods",querneGoods);
		vMap.put("remarkAgainBtn",remarkAgainBtn);
		vMap.put("pursure",pursure);
		return vMap;
	}

	/**
	 * 将值填入bean中
	 * @param map
	 * @param purchaseBean
	 */
	private void getBeanValue(Map<String, String> map, PurchasesBean purchaseBean) {
		if (map.get("aruadminid") == null || (String.valueOf(map.get("aruadminid")).trim()).equals("")) {
			purchaseBean.setSaler("暂未分配");
		} else {
			purchaseBean.setSaler(map.get("aruadminid"));
		}
		if(map.get("aFlag") != null && StringUtil.isNotBlank(String.valueOf(map.get("aFlag")))){
			purchaseBean.setAuthorized_flag(Integer.valueOf(String.valueOf(map.get("aFlag"))));
		}else{
			purchaseBean.setAuthorized_flag(1);
		}
		String authorizedFlag="未授权";
		if((map.get("tb_1688_itemid") != null && String.valueOf(map.get("goods_pid")).equals(String.valueOf(map.get("tb_1688_itemid")))) || map.get("tb_1688_itemid") == null){
			//有录入货源而且货源信息和推荐货源一致
			if(map.get("authorizedFlag")!=null){
				// 0-未授权  1-部分授权 2-自有品牌 3-无需授权 -1 -侵权
				authorizedFlag=String.valueOf(map.get("authorizedFlag"));
				if("0".equals(authorizedFlag)){
					authorizedFlag="未授权";
				}else if("1".equals(authorizedFlag)){
					authorizedFlag="部分授权";
				}else if("2".equals(authorizedFlag)){
					authorizedFlag="自有品牌";
				}else if("3".equals(authorizedFlag)){
					authorizedFlag="自有品牌";
				}else if("-1".equals(authorizedFlag)){
					authorizedFlag="侵权";
				}
			}
			purchaseBean.setShopAddress(map.get("shopAddress"));
		}else{
			//录入货源和推荐货源不一致，根据录入的货源店铺查询授权信息
			purchaseBean.setShopAddress("");
		}
		purchaseBean.setAuthorizedFlag(authorizedFlag);
		String type_name="";
		if(map.get("type_name") !=null){
			type_name=String.valueOf(map.get("type_name"));
		}
		purchaseBean.setType_name(type_name);
		String tbSku="";
		if(map.get("tbSku")!=null){
			tbSku=String.valueOf(map.get("tbSku"));
		}
		purchaseBean.setTbSku(tbSku);
		String noChangeRemark=String.valueOf(map.get("noChnageRemark"));
		if(StringUtil.isBlank(noChangeRemark)){
			noChangeRemark="";
		}else{
			noChangeRemark="<span style='color:red'>客户不同意替换备注:"+noChangeRemark+"</span>";
		}
		purchaseBean.setShopInventory(String.valueOf(map.get("shopInventory")));
		purchaseBean.setNoChnageRemark(noChangeRemark);
		purchaseBean.setOd_state(Integer.valueOf(String.valueOf(map.get("od_state")==null?"0":map.get("od_state"))));
		purchaseBean.setLastValue(map.get("lastValue"));
		String pidInventory="0";
		if(map.get("pidInventory") !=null && !"".equals(String.valueOf(map.get("pidInventory")).trim())){
			pidInventory=String.valueOf(map.get("pidInventory")).trim();
		}
		purchaseBean.setPidInventory(pidInventory);
		purchaseBean.setOldValue(map.get("oldValue"));
		if(org.apache.commons.lang3.StringUtils.isNotBlank(map.get("goods_title"))){
			purchaseBean.setGoods_title(map.get("goods_title").replace("'", ""));
		} else{
			purchaseBean.setGoods_title("");
		}
		if (map.get("paytime") == null || String.valueOf(map.get("paytime")).trim().length() < 1) {
			purchaseBean.setPaytime(map.get("paytime"));
		}else {
			purchaseBean.setPaytime(String.valueOf(map.get("paytime")).substring(0, 10));
		}
		String goods_img = map.get("googs_img").replace("60x60", "400x400").replace("32x32", "400x400").replace("50x50", "200x200");
		if (goods_img.contains(".jpg")) {
			purchaseBean.setGoogs_img((goods_img.substring(0,goods_img.lastIndexOf(".jpg") + 4)));
		} else if (goods_img.contains(".png")) {
			purchaseBean.setGoogs_img(goods_img.substring(0, (goods_img.lastIndexOf(".png") + 4)));
		} else {
			purchaseBean.setGoogs_img(goods_img);
		}
		purchaseBean.setDeliveryTime(map.get("delivery_time"));
		purchaseBean.setDetails_number(Integer.valueOf(String.valueOf(map.get("details_number")))- Integer.valueOf(String.valueOf(map.get("purchase_number"))));
		purchaseBean.setPurchase_number(Integer.valueOf(String.valueOf(map.get("purchase_number"))));
		purchaseBean.setGoodsid(Integer.valueOf(String.valueOf(map.get("goodsid"))));
		purchaseBean.setOd_id(Integer.valueOf(String.valueOf(map.get("od_id"))));
		purchaseBean.setGoodsdata_id(Integer.valueOf(String.valueOf(map.get("goodsdata_id"))));
		purchaseBean.setRemarkpurchase(map.get("odremark"));
		purchaseBean.setLevel(map.get("level"));
		purchaseBean.setQuality(map.get("quality"));
		purchaseBean.setInventory_remark(map.get("inventory_remark"));
		purchaseBean.setUserid(Integer.valueOf(String.valueOf(map.get("userid"))));
		purchaseBean.setShop_ids(StringUtils.isStrNull(map.get("shop_id")) || "null".equals(map.get("shop_id"))?"":map.get("shop_id"));
		purchaseBean.setBuyid(map.get("buyid"));
		purchaseBean.setLock_remaining(map.get("lock_remaining"));
		purchaseBean.setRemaining(map.get("remaining"));
		purchaseBean.setIn_id(map.get("in_id"));
		purchaseBean.setTb_1688_itemid(map.get("tb_1688_itemid"));
		purchaseBean.setNew_remaining(map.get("new_remaining"));
		purchaseBean.setOrderNo(map.get("order_no"));
		purchaseBean.setOrderid(Integer.valueOf(String.valueOf(map.get("orderid"))));
		purchaseBean.setGoodssourcetype(StringUtils.isStrNull(map.get("goodssourcetype"))?"":map.get("goodssourcetype").replace("'", ""));
		purchaseBean.setOrdertime(String.valueOf(map.get("create_time")).substring(0, 10));
		purchaseBean.setOrderremarkNew(map.get("orderremark"));
		purchaseBean.setConfirm_userid(Integer.valueOf(String.valueOf(map.get("confirm_userid")==null?"0":map.get("confirm_userid"))));
		purchaseBean.setIs_replenishment(Integer.valueOf(String.valueOf(map.get("is_replenishment")==null?"0":map.get("is_replenishment"))));
		purchaseBean.setOrderProblem(map.get("problem"));
		purchaseBean.setGoods_info(map.get("goods_info"));
		purchaseBean.setRefund_flag(Integer.valueOf(String.valueOf(map.get("refund_flag")==null?"0":map.get("refund_flag"))));
		purchaseBean.setCurrency(map.get("currency"));
		purchaseBean.setGoogs_number(Integer.valueOf(String.valueOf(map.get("yourorder"))));
		purchaseBean.setCginfo(map.get("cginfo"));
		purchaseBean.setOffline_purchase(Integer.valueOf(String.valueOf(map.get("offline_purchase")==null?"0":map.get("offline_purchase"))));
		purchaseBean.setTotal(String.valueOf(map.get("totalCount")));
		purchaseBean.setCbrWeight(map.get("cbrWeight"));
		purchaseBean.setCarWeight(map.get("od_total_weight"));
		purchaseBean.setGoodsShop(String.valueOf(map.get("goodsShop")));
//		purchaseBean.setStraight_time(StringUtils.isStrNull(map.get("straight_time"))?"无":map.get("straight_time"));
		if(StringUtils.isEmpty(map.get("straight_time"))){
			purchaseBean.setStraight_time("无");
		}else{
			purchaseBean.setStraight_time(String.valueOf(map.get("straight_time")).substring(0, 10));
		}
		
		purchaseBean.setSource_shop_id("");
		String oistate=String.valueOf(map.get("oistate"));
		purchaseBean.setOistate(oistate);
		String odstate=String.valueOf(map.get("odstate"));
		purchaseBean.setOdstate(odstate);
		String strProductValue = "",pursure="";
		strProductValue = map.get("productchange");
		if (strProductValue.equals("1")) {
			purchaseBean.setProductState("商品已被取消，请勿采购");
			pursure = "<input type='button' id='"+ String.valueOf(map.get("order_no"))+ String.valueOf(map.get("goodsid"))+ "' value='采购确认' class='f' disabled='disabled'/>";
		}
		purchaseBean.setPurchaseSure(pursure);
		purchaseBean.setRukuTime(map.get("ruku_time"));
		purchaseBean.setTb_orderid(map.get("tb_orderid"));
		purchaseBean.setPosition(map.get("position"));
		purchaseBean.setRkgoodstatus(map.get("rkgoodstatus"));
		String img_type = map.get("img_type").replace("60x60", "400x400").replace("32x32", "400x400").replace("50x50", "200x200");
		purchaseBean.setImg_type(img_type);
		purchaseBean.setYiruku("");
		int bc = Integer.valueOf(String.valueOf(map.get("buycount")==null?"0":map.get("buycount")));
		if (bc == 0) {
			purchaseBean.setPurchaseCount(Integer.valueOf(String.valueOf(map.get("yourorder")==null?"0":map.get("yourorder"))));
		} else {
			purchaseBean.setPurchaseCount(Integer.valueOf(String.valueOf(map.get("buycount")==null?"0":map.get("buycount"))));
		}
		String order_remark = map.get("adminRemark");
		String od_remark = "";
		if (order_remark == null || order_remark.equals("")) {
			od_remark = "";
		} else {
			od_remark = (order_remark.replace(":", "")).replace(";", "");
		}
		if (od_remark == null || od_remark.equals("")) {
			purchaseBean.setOrderremark_btn("");
			purchaseBean.setOrderremark("");
		} else {
			purchaseBean.setOrderremark(order_remark);
			if (order_remark.length() > 117) {
				purchaseBean.setOrderremark_btn("<a id=\"remark_"+ String.valueOf(map.get("order_no"))+ "\" class=\"order_remark\" onclick=\"fnShow_od_remark(this.id);\">查看更多>></a>");
			}
		}
		// 获取dropship的订单号
		String child_order_no1 = map.get("dropshipid");
		if (!"  ".equals(child_order_no1) && child_order_no1 != null && child_order_no1.length() > 0) {
			purchaseBean.setChild_order_no(child_order_no1);
		}
        purchaseBean.setShopName(map.get("shopName"));
        purchaseBean.setShopGrade(map.get("supplyGrade"));
		purchaseBean.setMorder(String.valueOf(map.get("morder")));
		purchaseBean.setReplacementProduct(map.get("replacement_product"));
	}

	@Override
	public List<Map<String,Object>> getComfirmedSourceGoods() {
		return pruchaseMapper.getComfirmedSourceGoods();
	}
	
	@Override
	public List<Map<String,Object>> getSizeChart(String  catid) {
		return pruchaseMapper.getSizeChart(catid);
	}
	@Override
	public List<Map<String,Object>> getSizeChart_add(String  catid) {
		return pruchaseMapper.getSizeChart_add(catid);
	}
	@Override
	public int updateSizeChart(String imgname,String localpath,int rowid) {
		return pruchaseMapper.updateSizeChart(imgname,localpath, rowid);
	}
	@Override
	public int updateSizeChart_add(String imgname,String localpath,int rowid) {
		return pruchaseMapper.updateSizeChart_add(imgname,localpath, rowid);
	}
	@Override
	public int updateSizeChartUpload(List<Integer> rowidArray) {
		return pruchaseMapper.updateSizeChartUpload(rowidArray);
	}
	@Override
	public List<Map<String,Object>> getSizeChartPidInfo() {
		return pruchaseMapper.getSizeChartPidInfo();
	}
	@Override
	public List<Map<String,Object>> loadCategoryName(String catid) {
		return pruchaseMapper.loadCategoryName(catid+"%");
	}
	@Override
	public List<Map<String,Object>> loadCategoryName_add(String catid) {
		return pruchaseMapper.loadCategoryName_add(catid+"%");
	}
	@Override
	public int updateSizeChartById(List<Integer> rowidArray,int userid) {
		return pruchaseMapper.updateSizeChartById(rowidArray,userid);
	}
	@Override
	public int updateSizeChartById_add(List<Integer> rowidArray,int userid) {
		return pruchaseMapper.updateSizeChartById_add(rowidArray,userid);
	}

	@Override
	public List<PurchaseInfoBean> queryOrderProductSourceByOrderNo(String orderNo) {
		return pruchaseMapper.queryOrderProductSourceByOrderNo(orderNo);
	}

    @Override
    public void changeAllBuyer(String orderNo, Integer integer) {
        this.pruchaseMapper.changeAllBuyer(orderNo,integer);
    }

	@Override
	public void changeBuyerByPid(String odid, String admid, String orderNo) {
		String pid=this.pruchaseMapper.FindPidByOdid(odid);
		this.pruchaseMapper.changeBuyerByPid(odid,admid,orderNo,pid);
	}

    @Override
	public AlibabaTradeFastCreateOrderResult generateOrdersByShopId(String app_key,String sec_key,String access_taken,List<PurchaseGoodsBean> beanList) {
		ApiExecutor apiExecutor = new ApiExecutor(app_key,sec_key);
		AlibabaTradeFastCreateOrderParam param = new AlibabaTradeFastCreateOrderParam();
		param.setFlow("general");//大市场普通订单：general，代销订单：saleproxy
		param.setMessage("");//买家留言内容

		//收货地址
		AlibabaTradeFastAddress address = new AlibabaTradeFastAddress();
		address.setAddressId(528361511L);
		address.setFullName("电商 Alisa");
		address.setMobile("13645062059");
		address.setPostCode("200000");
		address.setProvinceText("上海");
		address.setCityText("上海市");
		address.setAreaText("普陀区");
		address.setAddress("上海 上海市 普陀区 长风新村街道 云岭东路609号(汇银铭尊)1号楼605室 上海凯融信息公司（拒收天天快递）");
		param.setAddressParam(address);

		//订单商品列表
		List<AlibabaTradeFastCargo> cargoList = new ArrayList<AlibabaTradeFastCargo>();
		for(PurchaseGoodsBean pgb:beanList) {
			AlibabaTradeFastCargo cargo = new AlibabaTradeFastCargo();
			cargo.setOfferId(Long.parseLong(pgb.getPid()));
			cargo.setSpecId(pgb.getSpecId());
			cargo.setQuantity(new BigDecimal(pgb.getNumber()).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
			cargoList.add(cargo);
		}
		param.setCargoParamList(cargoList.toArray(new AlibabaTradeFastCargo[]{}));

		//自动下单,正式时，调用API
		AlibabaTradeFastCreateOrderResult result = apiExecutor.execute(param,access_taken);

		//自动下单 test
		//AlibabaTradeFastCreateOrderResult result = new AlibabaTradeFastCreateOrderResult();
//		result.setMessage("");
//		result.setSuccess(true);
//		AlibabaTradeFastResult result1 = new AlibabaTradeFastResult();
//		result1.setOrderId("235350477276010705");
//		result1.setTotalSuccessAmount(13000l);
//		result.setResult(result1);	
		return result;
	}
	@Override
	public int updateAutoOrderFlag(List<Integer> idsList) {
		return pruchaseMapper.updateAutoOrderFlag(idsList);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	@Override
	public List<String> sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.add(line);
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	@Override
	public int addIdRelationTable(Map<String, String> map) {
		int inventory_count_use = Integer.valueOf(map.get("inventory_count_use"));
		int googs_number = Integer.valueOf(map.get("googs_number"));
		if(inventory_count_use < 1) {
			return 0;
		}
		
		String inventory_sku_id = map.get("inventory_sku_id");
		//inventory_sku
		
		Map<String, Object> taobaoOrderHistory = inventoryMapper.getInventoryDetailSku(inventory_sku_id);
		if(taobaoOrderHistory == null || taobaoOrderHistory.isEmpty()) {
			return 0;
		}
		
		int goodsUnit = 1;
		String strgoodsUnit = map.get("goodsUnit");
		strgoodsUnit = StrUtils.matchStr(strgoodsUnit, "([1-9]\\d*)");
		goodsUnit = StrUtils.isNum(strgoodsUnit) ? Integer.valueOf(strgoodsUnit) : goodsUnit;
		
		//tbOr1688,orderid,itemname,itemid,sku,shipno,shipper,username,imgurl,itemurl,specId,skuID
		map.put("tborderid", (String)taobaoOrderHistory.get("1688_orderid"));
		map.put("shipno", (String)taobaoOrderHistory.get("1688_shipno"));
		map.put("itemid", (String)taobaoOrderHistory.get("goods_p_pid"));
		map.put("taobaospec", (String)taobaoOrderHistory.get("sku"));
		map.put("specid", (String)taobaoOrderHistory.get("goods_p_specid"));
		map.put("skuid", (String)taobaoOrderHistory.get("goods_p_skuid"));
		map.put("goodurl", (String)taobaoOrderHistory.get("goods_p_url"));
		map.put("taobaoprice", com.cbt.util.StrUtils.object2PriceStr(taobaoOrderHistory.get("goods_p_price")));
		//'0为 未出货，1已出货'
		map.put("state", "0");
		//'入库删除标记:0.已入库;1.入库已取消'
		map.put("is_delete", "0");
		//'商品状态：1.到货了;2.该货没到;3.破损;4.有疑问;5.数量不够 6:品牌未授权'
		map.put("goodstatus", googs_number * goodsUnit == inventory_count_use ? "1" : "5");
		//'商品到货数量'
		map.put("goodarrivecount", String.valueOf(inventory_count_use));
		//'记录商品数量'
		map.put("itemqty", String.valueOf(inventory_count_use));
		//'1 正常入库  0补货订单入库'
		map.put("is_replenishment", "1");
		//'是否1688退货  0没有  1退货'
		map.put("is_refund", "0");
		
		map.put("weight", "0");
		
		map.put("warehouse_remark", "有库存商品，采购自动匹配入库 inventory_sku_id:"+inventory_sku_id+"/orderid:"+map.get("orderid")+"/od_id:"+map.get("od_id"));
		
		int addIdRelationTable = inventoryMapper.addIdRelationTable(map);
		
		return addIdRelationTable;
	}
}
