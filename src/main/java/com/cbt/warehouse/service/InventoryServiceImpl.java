package com.cbt.warehouse.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.common.StringUtils;
import com.cbt.orderinfo.dao.OrderinfoMapper;
import com.cbt.pojo.Inventory;
import com.cbt.util.StrUtils;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.InventoryMapper;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.InventoryBarcodeRecord;
import com.cbt.website.bean.InventoryCheck;
import com.cbt.website.bean.InventoryCheckRecord;
import com.cbt.website.bean.InventoryCheckWrap;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.InventoryDetails;
import com.cbt.website.bean.InventoryDetailsWrap;
import com.cbt.website.bean.InventoryLock;
import com.cbt.website.bean.InventoryLog;
import com.cbt.website.bean.InventorySku;
import com.cbt.website.bean.InventoryTemporary;
import com.cbt.website.bean.InventoryWrap;
import com.cbt.website.bean.LossInventoryRecord;
import com.cbt.website.bean.LossInventoryWrap;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
@Service
public class InventoryServiceImpl implements  InventoryService{
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(InventoryServiceImpl.class);
	@Autowired
	private InventoryMapper inventoryMapper;
	@Autowired
	private OrderinfoMapper orderinfoMapper;
	@Autowired
	private IPurchaseMapper pruchaseMapper;
	
	IExpressTrackDao dao = new ExpressTrackDaoImpl();
	/**
	 * ??????ID????????????
	 */
	@Override
	public Inventory queryInById(String id) {

		return inventoryMapper.queryInById(id);
	}
	@Override
	public int recordLossInventory(Map<String, Object> map) {

		return inventoryMapper.recordLossInventory(map);
	}
	@Override
	public Inventory getInventoryByPid(Map<String, String> map) {
		return inventoryMapper.getInventoryByPid(map);
	}

	@Override
	public int insertInventoryYmx(Map<String, String> map) {
		return inventoryMapper.insertInventoryYmx(map);
	}
	@Override
	public List<InventoryData> getIinOutInventory(Map<Object, Object> map) {
		
		List<InventoryData> toryList = inventoryMapper.getIinOutInventory(map);
		if(toryList == null || toryList.isEmpty()) {
			return null;
		}
		List<Integer> list = new ArrayList<>();
		StringBuilder pids = new StringBuilder();
		StringBuilder opration = null;
		for (int i=0,size=toryList.size();i<size;i++) {
			InventoryData t = toryList.get(i);
			list.add(t.getId());
			String goodscatid = t.getGoodsCatid();
			if(StringUtil.isBlank(goodscatid) || "0".equals(goodscatid)){
				t.setCategoryName("??????");
			}
			t.setSkuContext("<em class='emsku'>"+t.getSku()+"</em><br>skuid:<em class='emskuid'>"+t.getSkuid()+"</em><br>specid:<em class='emspecid'>"+t.getSpecid()+"</em>");
			
			String url="";
			if(!StringUtils.isStrNull(t.getCarUrlMD5()) && t.getCarUrlMD5().startsWith("A")){
				url="https://www.import-express.com/goodsinfo/a-2"+t.getGoodsPid()+".html";
			}else if(!StringUtils.isStrNull(t.getCarUrlMD5()) && t.getCarUrlMD5().startsWith("D")){
				url="https://www.import-express.com/goodsinfo/a-1"+t.getGoodsPid()+".html";
			}else if(!StringUtils.isStrNull(t.getCarUrlMD5()) && t.getCarUrlMD5().startsWith("N")){
				url="https://www.import-express.com/goodsinfo/a-3"+t.getGoodsPid()+".html";
			}else if(!StringUtils.isStrNull(t.getGoodsUrl()) && t.getGoodsUrl().contains("ali")){
				url="https://www.import-express.com/goodsinfo/a-2"+t.getGoodsPid()+".html";
			}else if(!StringUtils.isStrNull(t.getCarUrlMD5()) && !StringUtils.isStrNull(t.getGoodsPid())){
				url="https://www.import-express.com/spider/detail?&source="+t.getCarUrlMD5()+"&item="+t.getGoodsPid()+"";
			}else {
				url="https://www.import-express.com/goodsinfo/a-1"+t.getGoodsPid()+".html";
			}
			String car_img=t.getCarImg();
			if(StringUtil.isNotBlank(car_img)) {
				car_img = car_img.replace(".60x60.jpg", ".400x400.jpg");
//				String imgs[]=car_img.split("kf");
//				if(imgs.length > 1) {
//					String one=imgs[0];
//					String two=imgs[1].replace(".jpg_50x50","");
//					url="https://s.1688.com/youyuan/index.htm?tab=imageSearch&from=plugin&imageType="+one+"&imageAddress=kf"+two+"";
//				}
//				
//				t.setCarImg("<a href='"+url+"' title='?????????????????????' target='_blank'>"
//						+ "<img   class=\"img-responsive\" src='"+ (car_img.indexOf("1.png")>-1?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":car_img) + "' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"+ car_img + "')\" height='100' width='100'></a>");
				
				
				t.setCarImg(car_img);
			}
			t.setGoodsUrl(StringUtil.isBlank(t.getGoodsUrl())?"":t.getGoodsUrl());
			t.setCarUrlMD5(StringUtil.isBlank(t.getCarUrlMD5())?"":t.getCarUrlMD5());
			
			pids.append("'").append(t.getGoodsPid()).append("',");
			
			String unsellableReason="--";
			String onLine="????????????";
			int valid= t.getOnlineFlag();
			if("1".equals(valid)){
				onLine="??????";
			}else if("0".equals(valid)){
				onLine="??????";
				unsellableReason= Utility.getUnsellableReason(t.getUnsellableReason(),unsellableReason);
				t.setUnsellableReason(unsellableReason);
			}else if("2".equals(valid) && (t.getGoodsUrl().indexOf("aliexpress")>-1 || t.getCarUrlMD5().startsWith("A"))){
				onLine="ali??????????????????";
			}else{
				onLine="???????????????";
			}
			t.setOnline(onLine);
			
			if("0".equals(map.get("export"))){
				opration = new StringBuilder();
				//??????/??????
				opration.append("<button class=\"btn btn-info mt5 btn-check-list\" onclick=\"updateCheck('0','"+i+"','"+t.getId()+"')\"> ")
				.append("????????????").append("</button><br>");
				
				//??????/??????
				opration.append("<button class=\"btn btn-info mt5\" onclick=\"updateInventory('0','"+i+"','"+t.getId()+"')\"> ")
				.append("????????????").append("</button>");
				
				//????????????
				opration.append("<br><a target='_blank' href='/cbtconsole/editc/detalisEdit?pid=").append(t.getGoodsPid())
				.append("'><button class=\"btn btn-warning mt5\">").append("????????????</button></a>");
				
				//????????????
				opration.append("<br><a target='_blank' href='/cbtconsole/website/inventorydetails.jsp?inid=").append(t.getId())
				.append("'><button class=\"btn btn-success mt5\">").append("????????????</button></a>");
				
				t.setOperation(opration.toString());
			}
			if(StringUtil.isBlank(t.getCheckTime())) {
				t.setCheckTime("");
			}
			t.setRemarkContext(getRemark(t.getId()));
			toryList.set(i, t);
		}
		return toryList;
	}

	private String getRemark(int skuId) {
		List<Map<String,Object>> inventoryRemark = inventoryMapper.getInventoryRemark(skuId);
		if(inventoryRemark == null || inventoryRemark.isEmpty()) {
			return "";
		}
		String remark = "";
		int remarkIndex = 0;
		for(Map<String,Object> m : inventoryRemark) {
			String iRemark = StrUtils.object2Str(m.get("remark"));
			if(StringUtil.isNotBlank(iRemark) && remarkIndex < 20) {
				int changeType = Integer.parseInt(StrUtils.object2NumStr(m.get("change_type")));
				remark = remark+"<li "+(remarkIndex > 2 ? "class=\"li_more_s\"" : "")+">";
				remark = changeType == 3 ? remark : remark+StrUtils.object2Str(m.get("createtime"))+":";
				remark = remark+iRemark+"</li>";
				remarkIndex = remarkIndex + 1;
			}
		}
		remark = remarkIndex > 3 ? remark + "<a onclick=\"vMoreLi(this)\">View More</a>" : remark ;
		return remark;
	}
	@Override
	public int problem_inventory(Map<Object, Object> map) {
		return inventoryMapper.problem_inventory(map);
	}

	@Override
	public Inventory queryInId(String old_sku, String old_url, String old_barcode,String car_urlMD5,String flag) {

		return inventoryMapper.queryInId(old_sku,old_url,old_barcode,car_urlMD5,flag);
	}
	@Override
	public int insertChangeBarcode(int id, String old_barcode,
	                               String new_barcode) {

		return inventoryMapper.insertChangeBarcode(id,old_barcode,new_barcode);
	}

	@Override
	public int getIinOutInventoryCount(Map<Object, Object> map) {
		return inventoryMapper.getIinOutInventoryCount(map);
	}

	@Override
	public List<AliCategory> searchAliCategory(String type, String cid) {

		return inventoryMapper.searchAliCategory(type,cid);
	}

	@Override
	public OrderDetailsBean findOrderDetails(Map<Object, Object> map) {

		return inventoryMapper.findOrderDetails(map);
	}

	@Override
	public int isExitBarcode(String barcode) {

		return inventoryMapper.isExitBarcode(barcode);
	}
	@Override
	public int updateIsStockFlag(String goods_pid) {

		return inventoryMapper.updateIsStockFlag(goods_pid);
	}
	@Override
	public int updateIsStockFlag2(String goods_pid) {

		return inventoryMapper.updateIsStockFlag2(goods_pid);
	}
	@Override
	public int updateSourcesLog(int in_id,String name,String old_sku, String old_url,
	                            String new_barcode, String old_barcode, int new_remaining,
	                            int old_remaining, String remark) {

		return inventoryMapper.updateSourcesLog(in_id,name,old_sku,old_url,new_barcode,old_barcode,new_remaining,old_remaining,remark);
	}

	@Override
	public int updateIsStockFlag1(String goods_pid) {

		return inventoryMapper.updateIsStockFlag1(goods_pid);
	}
	/**
	 * ??????????????????
	 * @param map
	 * @return
	 */
	@Override
	public int inventoryEntry(Map<Object, Object> map) {

		return inventoryMapper.inventoryEntry(map);
	}

	@Override
	public int updateSources(String flag,String old_sku, String goods_pid,String car_urlMD5,
	                         String new_barcode, String old_barcode, int new_remaining,
	                         int old_remaining, String remark,double new_inventory_amount) {

		return inventoryMapper.updateSources(flag,old_sku,goods_pid,car_urlMD5,new_barcode,old_barcode,new_remaining,old_remaining,remark,new_inventory_amount);
	}
	/**
	 * ??????ID??????????????????
	 */
	@Override
	public int deleteInventory(int id,String dRemark) {

		return inventoryMapper.deleteInventory(id,dRemark);
	}
	@Override
	public String getNewInventory() {
		PurchaseSamplingStatisticsPojo p=inventoryMapper.getNewInventory();
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
	public String getLossInventory() {
		PurchaseSamplingStatisticsPojo p=inventoryMapper.getLossInventory();
		return getDate(p,2);
	}

	@Override
	public String getSaleInventory() {
		PurchaseSamplingStatisticsPojo p=inventoryMapper.getSaleInventory();
		return getDate(p,1);
	}
	@Override
	public String getDeleteInventory() {
		PurchaseSamplingStatisticsPojo p=inventoryMapper.getDeleteInventory();
		return getDate(p,3);
	}
	public String getDate(PurchaseSamplingStatisticsPojo p, int type){
		StringBuilder str=new StringBuilder();
		if(p != null){
			if(StringUtil.isNotBlank(p.getAdmName())){
				if(type == 1){
					str.append("<a target='_blank' href='/cbtconsole/website/saleInventory.jsp?times=2000&amount="+(StringUtil.isNotBlank(p.getPid())?p.getPid().replace("-",""):0)+"' title='?????????????????????????????????'>"+p.getAdmName()+"</a>").append("/");
				}else if(type == 2){
					str.append("<a target='_blank' href='/cbtconsole/website/loss_inventory_log.jsp?times=2000' title='???????????????????????????'>"+p.getAdmName()+"</a>").append("/");
				}else if(type == 3){
					str.append("<a target='_blank' href='/cbtconsole/website/inventory_delete_log.jsp?times=2000' title='???????????????????????????'>"+p.getAdmName()+"</a>").append("/");
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
	public int addInventory(Map<String,String> inventory) {
		//1.???????????????????????????????????????
		if(inventory == null) {
			return 0;
		}
		int result = 0;
		//1688??????????????????????????????
		String checkOrderhid = inventory.get("checkOrderhid");
		if(org.apache.commons.lang.StringUtils.isNotBlank(checkOrderhid)) {
			String barcode = inventory.get("barcode");
			String[] barcodes = {};
			if(StringUtil.isNotBlank(barcode)) {
				barcode.split(",");
			}
			String newbarcode =  "";
			int barcodeIndex = 0;
			List<Tb1688OrderHistory> tbGoodsData = orderinfoMapper.getTbGoodsData(checkOrderhid);
			for(Tb1688OrderHistory t : tbGoodsData) {
				newbarcode = barcodes.length > 0 && barcodes.length > barcodeIndex? barcodes[barcodeIndex] : "";
				inventory.put("barcode", newbarcode);
				inventory.put("goods_pid", t.getItemid());
				inventory.put("goods_p_pid", t.getItemid());
				inventory.put("car_type", t.getSku());
				inventory.put("specid", StringUtil.isBlank(t.getSpecId()) ? t.getItemid() : t.getSpecId());
				inventory.put("skuid",StringUtil.isBlank(t.getSkuID()) ? t.getItemid() : t.getSkuID());
				inventory.put("inventory_count", t.getItemqty());
				inventory.put("yourorder", t.getItemqty());
				inventory.put("goods_url", t.getImgurl());
				inventory.put("db_flag", "0");
				inventory.put("car_img", t.getImgurl());
				inventory.put("goods_p_price", String.valueOf(t.getItemprice()));
				inventory.put("goodsprice", String.valueOf(t.getItemprice()));
				inventory.put("goods_p_url", t.getImgurl());
				inventory.put("good_name", t.getItemname());
				inventory.put("tb_1688_itemid", t.getItemid());
				inventory.put("tborderid", t.getOrderid());
				inventory.put("tbspecid", StringUtil.isBlank(t.getSpecId()) ? t.getItemid() : t.getSpecId());
				inventory.put("tbskuid", StringUtil.isBlank(t.getSkuID()) ? t.getItemid() : t.getSkuID());
				result = inventoryOperation(inventory,true);
			}
		}else {
			inventory.put("db_flag", "1");
			result = inventoryOperation(inventory,false);
		}
		return result;
	}
	
	private int inventoryOperation(Map<String,String> inventory,boolean isTbOrder) {
		int inventory_count = Integer.valueOf(StrUtils.object2NumStr(inventory.get("inventory_count")));
		if(inventory_count == 0) {
			return 0;
		}
		InventoryDetails iDetail = new InventoryDetails();
		iDetail.setGoodsPSkuid(inventory.get("tbskuid"));
		iDetail.setGoodsPSpecid(inventory.get("tbspecid"));
		iDetail.setTbShipno(inventory.get("shipno"));
		
		InventorySku iSku = new InventorySku();
		iSku.setBarcode(inventory.get("barcode"));
		iSku.setDbFlag(Integer.valueOf(StrUtils.object2NumStr(inventory.get("db_flag"))));
		//inventory_type 0???????????????  1??????????????????
		iSku.setInventoryType(0);
		//??????????????????????????????????????????id????????????????????????????????????????????????
		Map<String, String> orderDetails = isTbOrder ? null : inventoryMapper.getOrderDetails(inventory);
		if(!isTbOrder && orderDetails == null) {
			return 0;
		}
		if(orderDetails != null) {
			inventory.putAll(orderDetails);
			//??????sku????????????????????????????????????????????????id????????????id
			String car_type = inventory.get("car_type");
			iSku.setGoodsPid(orderDetails.get("goods_pid"));
			iSku.setSku(car_type);
			iSku.setCarImg(orderDetails.get("car_img"));
			iSku.setCarUrlMD5(orderDetails.get("car_urlMD5"));
			iSku.setGoodsCatid(orderDetails.get("goodscatid"));
			iSku.setGoodsName(orderDetails.get("goodsname"));
			iSku.setGoodsPPid(orderDetails.get("tb_1688_itemid"));
			iSku.setGoodsPPrice(orderDetails.get("goods_p_price"));
			iSku.setGoodsPrice(orderDetails.get("goodsprice"));
			iSku.setGoodsPUrl(orderDetails.get("goods_p_url"));
			iSku.setGoodsUrl(orderDetails.get("car_url"));
			iDetail.setGoodsPImg(orderDetails.get("goods_p_img"));
			iDetail.setTbOrderid(orderDetails.get("tborderid"));
			if(StringUtils.isStrNull(car_type) || "0".equals(car_type)) {
				String skuid = inventory.get("skuid");
				String specid = inventory.get("specid");
				skuid = StringUtils.isStrNull(skuid) ? orderDetails.get("goods_pid") : skuid;
				specid = StringUtils.isStrNull(specid) ? orderDetails.get("goods_pid") : specid;
				inventory.put("skuid", skuid);
				inventory.put("specid", specid);
			}
			iSku.setSkuid(inventory.get("skuid"));
			iSku.setSpecid(inventory.get("specid"));
		}else {
			iSku.setSku(inventory.get("car_type"));
			iSku.setCarImg(inventory.get("car_img"));
			iSku.setGoodsPid(inventory.get("goods_pid"));
			iSku.setGoodsName(inventory.get("good_name"));
			iSku.setGoodsPPid(inventory.get("tb_1688_itemid"));
			iSku.setGoodsPPrice(inventory.get("goods_p_price"));
			iSku.setGoodsPrice(inventory.get("goodsprice"));
			iSku.setGoodsPUrl(inventory.get("goods_p_url"));
			iSku.setGoodsUrl(inventory.get("goods_url"));
			iSku.setSkuid(inventory.get("skuid"));
			iSku.setSpecid(inventory.get("specid"));
			iSku.setDbFlag(0);
			iSku.setBarcode(inventory.get("barcode"));
			iSku.setRemaining(Integer.valueOf(StrUtils.object2NumStr(inventory.get("yourorder"))));
			iDetail.setGoodsPImg(inventory.get("goods_p_img"));
			iDetail.setTbOrderid(inventory.get("tborderid"));
			iDetail.setGoodsNumber(Integer.valueOf(StrUtils.object2NumStr(inventory.get("yourorder"))));
		}
		
		//2.??????????????????
		InventorySku isExsisInventory = inventoryMapper.getInventory(iSku);
		
		iSku.setOdid(Integer.valueOf(StrUtils.object2NumStr(inventory.get("odid"))));
		
		int beforeRemaining = 0;
		int afterRemaining = inventory_count;
		Integer inventory_sku_id  = 0;
		if(isExsisInventory == null) {
			iSku.setRemaining(inventory_count);
			iSku.setCanRemaining(inventory_count);
			inventoryMapper.insertInventory(iSku);
			//3.?????????id
			inventory_sku_id = iSku.getId();
			inventory.put("inventory_sku_id", String.valueOf(inventory_sku_id));
		}else {
			if(isTbOrder) {
				iSku.setBarcode(isExsisInventory.getBarcode());
				inventory.put("barcode", isExsisInventory.getBarcode());
			}
			int remaining = isExsisInventory.getRemaining();
			int can_remaining = isExsisInventory.getCanRemaining();
			inventory_sku_id =  isExsisInventory.getId();
			//???????????????
			beforeRemaining = remaining;
			//???????????????
			afterRemaining = beforeRemaining + inventory_count;
					
			//????????????
			can_remaining = inventory_count + can_remaining;
			
			//?????????????????????
			iSku.setRemaining(afterRemaining);
			iSku.setCanRemaining(can_remaining);
			iSku.setId(inventory_sku_id);
			iSku.setGoodsPid(isExsisInventory.getGoodsPid());
			inventoryMapper.updateInventory(iSku);
		}
		inventory.put("before_remaining", String.valueOf(beforeRemaining));
		inventory.put("after_remaining", String.valueOf(afterRemaining));
		
		//4.???????????????????????? change_type 0????????? 1?????????  2????????????3?????????  4?????? 5-????????????
		InventoryLog iLog = new InventoryLog();
		iLog.setAfterRemaining(afterRemaining);
		iLog.setBeforeRemaining(beforeRemaining);
		iLog.setRemaining(inventory_count);
		iLog.setBarcode(inventory.get("barcode"));
		iLog.setChangeType(1);
		iLog.setGoodsName(iSku.getGoodsName());
		iLog.setGoodsPid(iSku.getGoodsPid());
		iLog.setGoodsPPid(iSku.getGoodsPPid());
		iLog.setGoodsUrl(iSku.getGoodsUrl());
		iLog.setInventorySkuId(inventory_sku_id);
		if(StringUtil.isBlank(inventory.get("log_remark"))) {
			iLog.setRemark("??????,????????????");
			iDetail.setRemark("odid:"+StrUtils.object2NumStr(inventory.get("odid"))+"??????,????????????");
		}else {
			iLog.setRemark(inventory.get("log_remark"));
			iDetail.setRemark(inventory.get("log_remark"));
		}
		iLog.setSku(iSku.getSku());
		iLog.setSkuid(iSku.getSkuid());
		iLog.setSpecid(iSku.getSpecid());
		inventoryMapper.insertInventoryLog(iLog);
		
		//5.???????????????????????? ??????storage_outbound_details??????storage_outbound_details
		if(!isTbOrder) {
			inventory.put("inventory_sku_id", String.valueOf(inventory_sku_id));
			inventoryMapper.insertStorageOutboundDetails(inventory);
		}
		//6.??????????????????????????????
		//0 ??????  1 ?????? 2 ?????? 4??????  5-???????????? 6-???????????? 7-????????????
		iDetail.setGoodsPPrice(iSku.getGoodsPPrice());
		iDetail.setType(5);
		iDetail.setAdmid(Integer.valueOf(StrUtils.object2NumStr(inventory.get("admId"))));
		iDetail.setGoodsName(iSku.getGoodsName());
		iDetail.setGoodsImg(iSku.getCarImg());
		iDetail.setGoodsNumber(inventory_count);
		iDetail.setGoodsPPid(iSku.getGoodsPPid());
		iDetail.setGoodsPid(iSku.getGoodsPid());
		iDetail.setGoodsPrice(iSku.getGoodsPrice());
		iDetail.setGoodsUrl(iSku.getGoodsUrl());
		iDetail.setInventoryId(inventory_sku_id);
		iDetail.setSku(iSku.getSku());
		iDetail.setOrderno(inventory.get("orderid"));
		iDetail.setOdId(Integer.valueOf(StrUtils.object2NumStr(inventory.get("odid"))));
		iDetail.setGoodsSkuid(iSku.getSkuid());
		iDetail.setGoodsSpecid(iSku.getSpecid());
		inventoryMapper.insertInventoryDetailsSku(iDetail);
		return inventory_sku_id;
		
	}
	@Override
	public int useInventory(Map<String, String> map) {
		if(map == null || map.isEmpty()) {
			return 0;
		}
		int inventory_count = Integer.valueOf(StrUtils.object2NumStr(map.get("inventory_count")));
		int googs_number = Integer.valueOf(StrUtils.object2NumStr(map.get("googs_number")));
		int goodsUnit = 1;
		String strgoodsUnit = map.get("goodsUnit");
		strgoodsUnit = StrUtils.matchStr(strgoodsUnit, "([1-9]\\d*)");
		goodsUnit = StrUtils.isNum(strgoodsUnit) ? Integer.valueOf(strgoodsUnit) : goodsUnit;
		if(inventory_count < 1 || googs_number * goodsUnit < 1) {
			return 0;
		}
		map.put("useAllInventory", "false");
		//????????????????????????????????????????????????????????????????????????????????????????????????
		if(googs_number * goodsUnit < inventory_count) {
			inventory_count = googs_number * goodsUnit;
			map.put("useAllInventory", "true");
		}
		map.put("inventory_count", String.valueOf(inventory_count));
		map.put("inventory_count_use", String.valueOf(inventory_count));
		
		//1.??????????????????????????????????????????????????????
		String id = map.get("inventory_sku_id");
		InventorySku iSku = new InventorySku();
		iSku.setId(Integer.valueOf(id));
		
		//????????????????????????  ???????????????
		//????????????????????????????????????????????????????????????
		InventorySku inventoryMap = inventoryMapper.getInventory(iSku);
		if(inventoryMap == null){
			return 0;
		}
		int canRemaining = inventoryMap.getCanRemaining();
		//???????????????????????????can_remaing??????
		if(canRemaining < inventory_count) {
			return 0;
		}
		map.put("goods_title",inventoryMap.getGoodsName());
		map.put("goods_url",inventoryMap.getGoodsPUrl());
		map.put("googs_img",inventoryMap.getCarImg());
		map.put("goods_price",inventoryMap.getGoodsPrice());
		map.put("itemid",inventoryMap.getGoodsPPid());
		map.put("price",inventoryMap.getGoodsPPrice());
		map.put("admid",map.get("admId"));
		map.put("orderNo", map.get("orderid"));
		map.put("goodid", map.get("goodsid"));
		map.put("od_id", map.get("odid"));
		map.put("purchase_state", "4");
		map.put("buycount", map.get("inventory_count"));
		map.put("orderNumRemarks", "????????????"+map.get("inventory_count"));
		map.put("goods_pid", inventoryMap.getGoodsPid());
		map.put("inventory_sku_id", String.valueOf(inventoryMap.getId()));
		map.put("inventory_barcode", inventoryMap.getBarcode());
		addLockInventory(map);
		
		//???????????????????????????can_remaing??????
		iSku.setRemaining(inventoryMap.getRemaining());
		iSku.setCanRemaining(canRemaining - inventory_count);
		int updateInventoryById = inventoryMapper.updateInventory(iSku);
		
		String orderid = map.get("orderid");
		String odid =  map.get("odid");
		//2.??????????????????
		InventoryLog iLog = new InventoryLog();
		iLog.setSku(inventoryMap.getSku());
		iLog.setAfterRemaining(inventoryMap.getRemaining());
		iLog.setBeforeRemaining(inventoryMap.getRemaining());
		iLog.setRemaining(inventory_count);
		iLog.setChangeType(4);
		iLog.setRemark("??????orderid:"+orderid+"/od_id:"+odid+"???????????????????????????"+inventory_count+",??????????????????,??????????????????");
		map.put("sku_details_remark", "??????orderid:"+orderid+"/od_id:"+odid+"???????????????????????????"+inventory_count+",??????????????????,????????????????????????");
		iLog.setGoodsName(inventoryMap.getGoodsName());
		iLog.setGoodsPid(inventoryMap.getGoodsPid());
		iLog.setGoodsPPid(inventoryMap.getGoodsPPid());
		iLog.setGoodsUrl(inventoryMap.getGoodsUrl());
		iLog.setInventorySkuId(inventoryMap.getId());
		iLog.setSkuid(inventoryMap.getSkuid());
		iLog.setSpecid(inventoryMap.getSpecid());
		inventoryMapper.insertInventoryLog(iLog);
		
		//3.??????????????????????????????
		//?????? 0 ??????  1 ??????
		map.put("type", "1");
		map.put("inventory_sku_id", String.valueOf(inventoryMap.getId()));
		map.put("inventory_count", String.valueOf(inventory_count));
		inventoryMapper.addInventoryDetailsSku(map);
		
//		int updateInventoryById = reduseInventory(map, iSku, inventory_count, true);
		
		return updateInventoryById > 0 ? inventory_count : 0;
	}
	@Override
	public int cancelInventory(Map<String, String> map) {
		if(map == null || map.isEmpty()) {
			return 0;
		}
		int cancelCount = Integer.valueOf(StrUtils.object2NumStr(map.get("cance_inventory_count")));
		if(cancelCount < 1) {
			return 0;
		}
		InventorySku iSku = new InventorySku();
		iSku.setSpecid(map.get("specid"));
		iSku.setSkuid(map.get("skuid"));
		Map<String, String> orderDetails = inventoryMapper.getOrderDetails(map);
		iSku.setGoodsPid(orderDetails.get("goods_pid"));
		
		//??????????????????
		reduseInventory(map, iSku, cancelCount, false);
		
		//????????????????????????storage_outbound_details
		return orderinfoMapper.updateUutboundDetails(map);
	}
	
	
	/**????????????
	 * @param map
	 * @param isku
	 * @param isReduce true ????????????  false ??????????????????
	 * @return
	 */
	private int reduseInventory(Map<String, String> map,InventorySku iSku,int inventory_count,boolean isReduce) {
		
		//????????????????????????????????????????????????????????????
		InventorySku inventoryMap = inventoryMapper.getInventory(iSku);
		if(inventoryMap == null){
			return 0;
		}
		String orderid = map.get("orderid");
		String odid =  map.get("odid");
		
		int before_remaining = inventoryMap.getRemaining();
		int can_remaining = inventoryMap.getCanRemaining();
		
		int after_remaining = before_remaining - inventory_count;
		can_remaining = isReduce ? can_remaining : can_remaining - inventory_count;
		if(before_remaining ==0) {
			return 0;
		}
		after_remaining = Math.max(after_remaining, 0);
		//1.????????????
		iSku.setId(inventoryMap.getId());
		iSku.setRemaining(after_remaining);
		iSku.setCanRemaining(can_remaining);
		int updateInventory = inventoryMapper.updateInventory(iSku);
		
		//2.??????????????????
		InventoryLog iLog = new InventoryLog();
		iLog.setSku(inventoryMap.getSku());
		iLog.setAfterRemaining(after_remaining);
		iLog.setBeforeRemaining(before_remaining);
		iLog.setRemaining(inventory_count);
		if(isReduce) {
			iLog.setRemark("??????orderid:"+orderid+"/od_id:"+odid+"????????????????????????"+inventory_count+",????????????");
			iLog.setChangeType(4);
			map.put("sku_details_remark", "??????orderid:"+orderid+"/od_id:"+odid+"????????????????????????"+inventory_count+",????????????");
			map.put("type", "1");
		}else {
			iLog.setRemark("??????orderid:"+orderid+"/od_id:"+odid+" ????????????,??????????????????:"+inventory_count);
			iLog.setChangeType(2);
			map.put("sku_details_remark", "??????orderid:"+orderid+"/od_id:"+odid+" ????????????,??????????????????:"+inventory_count);
			map.put("type", "6");
		}
		iLog.setGoodsName(inventoryMap.getGoodsName());
		iLog.setGoodsPid(inventoryMap.getGoodsPid());
		iLog.setGoodsPPid(inventoryMap.getGoodsPPid());
		iLog.setGoodsUrl(inventoryMap.getGoodsUrl());
		iLog.setInventorySkuId(inventoryMap.getId());
		iLog.setSkuid(inventoryMap.getSkuid());
		iLog.setSpecid(inventoryMap.getSpecid());
		inventoryMapper.insertInventoryLog(iLog);
		
		//3.??????????????????????????????
		//?????? 0 ??????  1 ??????
		map.put("inventory_sku_id", String.valueOf(inventoryMap.getId()));
		map.put("inventory_count", String.valueOf(inventory_count));
		inventoryMapper.addInventoryDetailsSku(map);
		return updateInventory;
	}
	
	
	@Override
	public Map<String,Object> reportLossInventory(Map<String, Object> map) {
		Map<String, String> inv = new HashMap<String, String>();
		Map<String,Object> result = new HashMap<String, Object>();
		
		String inventory_sku_id = (String)map.get("inventory_sku_id" );
		InventorySku iSku = new InventorySku();
		iSku.setId(Integer.valueOf(inventory_sku_id));
		InventorySku inventoryByid = inventoryMapper.getInventory(iSku);
		if(inventoryByid == null ) {
			result.put("status", 100);
			result.put("reason", "???????????????");
			return result;
		}
		int changeNumber = (int)map.get("changeNumber");
		if(changeNumber < 0) {
			result.put("status", 101);
			result.put("reason", "????????????????????????");
			return result;
		}
		int remaining = inventoryByid.getRemaining();
		int can_remaining = inventoryByid.getCanRemaining();
		
		//??????????????????
		int change_number = Math.abs(remaining - changeNumber);
		
		int before_remaining = remaining;
		int after_remaining = changeNumber;
		int lossChangeType = Integer.valueOf(StrUtils.object2NumStr(map.get("change_type")));
		inv.put("goods_p_url", inventoryByid.getGoodsPUrl());
		inv.put("goods_p_price", inventoryByid.getGoodsPPrice());
		if(remaining > changeNumber) {
			//????????????
			inv.put("can_remaining", String.valueOf(can_remaining - change_number));
			iSku.setCanRemaining(can_remaining - change_number);
			inv.put("change_type", "2");
		}else {
			//????????????
			inv.put("can_remaining", String.valueOf(can_remaining + change_number));
			iSku.setCanRemaining(can_remaining + change_number);
			inv.put("change_type", "1");
		}
		inv.put("remaining", String.valueOf(after_remaining));
		iSku.setRemaining(after_remaining);
		
		//1.???????????????loss_inventory_record
		 map.put("change_number", change_number);
		 
		 LossInventoryRecord record = new LossInventoryRecord();
		 record.setChangeAdm(Integer.valueOf(StrUtils.object2NumStr(map.get("change_adm"))));
		 record.setChangeNumber(change_number);
		 record.setChangeType(lossChangeType);
		 record.setGoodsPid((String)map.get("goods_pid"));
		 record.setInventorySkuId(Integer.valueOf(inventory_sku_id));
		 record.setSkuid((String)map.get("skuid"));
		 record.setSpecid((String)map.get("specid"));
		 record.setId(0);
		 record.setRemark(StrUtils.object2Str(map.get("remark")));
		 inventoryMapper.addLossInventoryRecord(record);
		 if(record.getId() == 0) {
			 result.put("status", 102);
			 result.put("reason", "???????????????loss_inventory_record??????");
			 return result;
		 }
		 int lossInventoryRecordid = record.getId();
		//2.???????????????  inwentory_sku
		inv.put("inventory_sku_id", inventory_sku_id);
		int updateInventoryById = inventoryMapper.updateInventory(iSku);
		if(updateInventoryById == 0) {
			 result.put("status", 103);
			 result.put("reason", "???????????????  inwentory_sku??????");
			 return result;
		}
		
		//3.??????????????? inventory_sku_log
		inv.put("inventory_count", String.valueOf(change_number));
		inv.put("before_remaining", String.valueOf(before_remaining));
		inv.put("after_remaining", String.valueOf(after_remaining));
		String remark = "????????????,"+lossInventoryRecordid+"/"+lossChangeType+"??????:"+StrUtils.object2Str(map.get("remark"));
		inv.put("log_remark", remark);
		inv.put("sku_details_remark", remark);
		int inventoryChangeRecordid = inventoryMapper.addInventoryLogByInventoryid(inv);
		if(inventoryChangeRecordid == 0) {
			result.put("status", 104);
			result.put("reason", "??????????????? inventory_sku_log??????");
			return result;
		}
		
		//4.??????????????? inventory_details_sku
		 inv.put("admid", String.valueOf(map.get("change_adm")));
		 inv.put("type", "2");
		 int addInventoryDetailsSku = inventoryMapper.addInventoryDetailsSku(inv);
		 if(addInventoryDetailsSku == 0) {
			result.put("status", 105);
			result.put("reason", "??????????????? inventory_details_sku??????");
			return result; 
		 }
		 result.put("status", 200);
		return result;
	}
	
	@Override
	public List<InventoryDetailsWrap> inventoryDetails(Map<String,Object> map){
		List<InventoryDetailsWrap> result = new ArrayList<InventoryDetailsWrap>();
		List<InventoryDetails> inventoryDetails = inventoryMapper.inventoryDetails(map);
		InventoryDetailsWrap wrap = null;
		String skuContext = "",orderContext="",delContext="";
		for(InventoryDetails i : inventoryDetails) {
			wrap = new InventoryDetailsWrap();
			wrap.setAdm(i.getAdm());
			wrap.setCreatetime(i.getCreatetime());
			String goodsImg = i.getGoodsImg();
			goodsImg = StringUtil.isBlank(goodsImg) ? goodsImg : goodsImg.replace(".60x60.jpg", ".400x400.jpg");
			wrap.setGoodsImg("<img class='img_class' src='"+goodsImg+"'>");
			wrap.setGoodsNumber(i.getGoodsNumber());
			wrap.setGoodsPid(i.getGoodsPid());
			wrap.setInventoryId(i.getInventoryId());
			wrap.setId(i.getId());
			wrap.setRemark(i.getRemark());
			
			skuContext = StringUtil.isBlank(i.getSku()) ? "" : "sku:" + i.getSku();
			skuContext = StringUtil.isBlank(i.getGoodsSkuid()) ? skuContext : skuContext + "<br>Skuid:" + i.getGoodsSkuid();
			skuContext = StringUtil.isBlank(i.getGoodsSpecid()) ? skuContext : skuContext + "<br>Specid:" + i.getGoodsSpecid();
			
			orderContext = StringUtil.isBlank(i.getOrderno()) ? "" : "orderno:" + i.getOrderno();
			orderContext = orderContext + "<br>od_id:" + i.getOdId();
			
			if(i.getDel() == 1) {
				delContext = "??????:"+i.getDelDatetime()+"<br>????????????:"+i.getDelAdm();
				delContext = StringUtil.isBlank(i.getDelRemark()) ? delContext : delContext + "<br>????????????:" + i.getDelRemark();
			}
			wrap.setTypeContext(inventoryDetailsType(i.getType()));
			wrap.setSkuContext(skuContext);
			wrap.setDelContext(delContext );
			wrap.setOrderContext(orderContext);
			
			result.add(wrap);
		}
		
		return result;
		
	}
	private String inventoryDetailsType(int type) {
		String result = "";
		// 0 ??????  1 ?????? 2 ?????? 4??????  5-???????????? 6-???????????? 7-????????????
		switch (type) {
		case 0:
			result = "??????,??????????????????";
			break;
		case 1:
			result = "???????????????????????????";
			break;
		case 2:
			result = "??????/??????";
			break;
		case 4:
			result = "??????";
			break;
		case 5:
			result = "????????????";
			break;
		case 6:
			result = "????????????";
			break;
		case 7:
			result = "????????????";
			break;
		default:
			result = "??????";
			break;
		}
		return result;
	}
	@Override
	public  int inventoryDetailsCount(Map<String,Object> map){
		return inventoryMapper.inventoryDetailsCount(map);
	}
	@Override
	public int inputInventory(Map<String, String> param) {
		String goodsUrl = "https://www.import-express.com/goodsinfo/aa-0-0-1"+param.get("goods_pid")+".html";
		InventorySku iSku = new InventorySku();
		iSku.setBarcode(param.get("barcode"));
		iSku.setCarImg(param.get("img"));
		iSku.setCarUrlMD5("");
		iSku.setGoodsCatid(param.get("goodsCatid"));
		iSku.setGoodsName(param.get("goods_name"));
		iSku.setGoodsPid(param.get("goods_pid"));
		iSku.setGoodsPrice(param.get("goods_price"));
		iSku.setGoodsUrl(goodsUrl);
		iSku.setInventoryType(0);
		iSku.setRemark(param.get("remark"));
		iSku.setSku(param.get("sku"));
		iSku.setSkuid(param.get("skuid"));
		iSku.setSpecid(param.get("specid"));
		
		InventoryLog iLog = new InventoryLog();
		InventoryDetails iDetail = new InventoryDetails();
		
		int change_remaining = Integer.valueOf(StrUtils.object2NumStr(param.get("count")));
		int inventorySkuId = 0;
		int before_remaining = 0;
		int after_remaining = change_remaining;
		//??????inventory_sku
		InventorySku inventory = inventoryMapper.getInventory(iSku);
		if(inventory == null) {
			iSku.setRemaining(change_remaining);
			iSku.setCanRemaining(change_remaining);
			inventoryMapper.insertInventory(iSku);
			inventorySkuId = iSku.getId();
		}else {
			before_remaining = inventory.getRemaining();
			int can_remaining = inventory.getCanRemaining();
			
			after_remaining = before_remaining + change_remaining;
			can_remaining = can_remaining + change_remaining;
			
			iSku.setRemaining(after_remaining);
			iSku.setCanRemaining(can_remaining);
			iSku.setId(inventory.getId());
			inventorySkuId = inventory.getId();
			inventoryMapper.updateInventory(iSku);
		}
		
		iLog.setAfterRemaining(after_remaining);
		iLog.setBarcode(param.get("barcode"));
		iLog.setBeforeRemaining(before_remaining);
		iLog.setChangeType(1);
		iLog.setGoodsName(param.get("goods_name"));
		iLog.setGoodsPid(param.get("goods_pid"));
		iLog.setGoodsUrl(goodsUrl);
		iLog.setInventorySkuId(inventorySkuId);
		iLog.setRemaining(change_remaining);
		//1??????  2?????? 3????????? 4??????
		if("1".equals(param.get("reasonType"))) {
			iLog.setRemark("????????????,??????,??????:"+param.get("remark"));
			iDetail.setRemark("????????????,??????,??????:"+param.get("remark"));
		}else if("2".equals(param.get("reasonType"))) {
			iLog.setRemark("????????????,??????,??????:"+param.get("remark"));
			iDetail.setRemark("????????????,??????,??????:"+param.get("remark"));
		}else if("3".equals(param.get("reasonType"))) {
			iLog.setRemark("????????????,?????????,??????:"+param.get("remark"));
			iDetail.setRemark("????????????,?????????,??????:"+param.get("remark"));
		}else{
			iLog.setRemark("????????????,??????,??????:"+param.get("remark"));
			iDetail.setRemark("????????????,??????,??????:"+param.get("remark"));
		}
		iLog.setSku(param.get("sku"));
		iLog.setSkuid(param.get("skuid"));
		iLog.setSpecid(param.get("specid"));
		
		//????????????inventory_sku_log
		inventoryMapper.insertInventoryLog(iLog);
		
		iDetail.setAdmid(Integer.valueOf(StrUtils.object2NumStr(param.get("admid"))));
		iDetail.setGoodsImg(param.get("img"));
		iDetail.setGoodsName(param.get("goods_name"));
		iDetail.setGoodsNumber(change_remaining);
		iDetail.setGoodsPid(param.get("goods_pid"));
		iDetail.setGoodsPrice(param.get("goods_price"));
		iDetail.setGoodsSkuid(param.get("skuid"));
		iDetail.setGoodsSpecid(param.get("specid"));
		iDetail.setType(5);
		iDetail.setSku(param.get("sku"));
		iDetail.setInventoryId(inventorySkuId);
		iDetail.setGoodsUrl(goodsUrl);
		if("1".equals(param.get("isTBOrder"))) {
			iDetail.setTbOrderid(param.get("tbOrderid"));
			iDetail.setTbShipno(param.get("tbShipno"));
			iDetail.setGoodsPPid(param.get("goods_pid"));
			iDetail.setGoodsPPrice(param.get("goods_price"));
			iDetail.setGoodsPSkuid(param.get("skuid"));
			iDetail.setGoodsPSpecid(param.get("specid"));
			iDetail.setGoodsPUrl(param.get("goods_purl"));
			iDetail.setGoodsPImg(param.get("img"));
		}
		//????????????inventory_details_sku
		return inventoryMapper.insertInventoryDetailsSku(iDetail);
	}
	@Override
	public List<Map<String, Object>> getTbGoods(String orderShipno) {
		
		return inventoryMapper.getTbGoods(orderShipno);
	}
	@Override
	public List<InventoryCheckWrap> invetoryCheckList(Map<Object, Object> map) {
//		List<InventoryCheck> unDoneInventoryCheck = inventoryMapper.getUnDoneInventoryCheck();
		//??????????????????
		List<InventoryData> iinOutInventory = getIinOutInventory(map);
		if(iinOutInventory == null || iinOutInventory.isEmpty()) {
			return null;
		}
		List<InventoryCheckWrap> result = new ArrayList<>();
		InventoryCheckWrap wrap = null;
		for(InventoryData i : iinOutInventory) {
			wrap = new InventoryCheckWrap();
			wrap.setOdid(i.getOdid());
			wrap.setTime(i.getUpdatetime());
			wrap.setBarcode(i.getBarcode());
			wrap.setGoodsPid(i.getGoodsPid());
			wrap.setGoodsImg(i.getCarImg());
			wrap.setGoodsPrice(i.getGoodsPrice());
			wrap.setGoodsSku(i.getSku());
			wrap.setGoodsSkuid(i.getSkuid());
			wrap.setGoodsSpecid(i.getSpecid());
			wrap.setInventorySkuId(i.getId());
			wrap.setRemaining(i.getRemaining());
			wrap.setCategoryName(i.getCategoryName());
			wrap.setCatid(i.getGoodsCatid());
			wrap.setGoodsName(i.getGoodsName());
			wrap.setOperation(i.getOperation());
			wrap.setCanRemaining(i.getCanRemaining());
			
			wrap.setInventoryCheckId(i.getInventoryCheckId());
			wrap.setLastCheckTime(i.getCheckTime());
			wrap.setLastCheckRemaining(i.getCheckRemaining());
			if(i.getCheckTempId() != 0) {
				wrap.setRemaining(i.getTempRemaining());
				wrap.setCanRemaining(i.getTempRemaining());
				wrap.setBarcode(i.getTempBarcode());
			}
			wrap.setCheckId(i.getCheckTempId());
			result.add(wrap);
		}
		
		return result;
	}
	@Override
	public InventoryCheck getLastInventoryCheck() {
		return inventoryMapper.getLastInventoryCheck();
	}
	@Override
	public List<InventoryCheck> getUnDoneInventoryCheck() {
		return inventoryMapper.getUnDoneInventoryCheck();
	}
	@Override
	public int insertInventoryCheck(InventoryCheck check) {
		inventoryMapper.insertInventoryCheck(check);
		String table_name = LocalDate.now().toString().replace("-", "_");
		table_name = "inventory_sku_"+table_name;
		if(StringUtil.isBlank(inventoryMapper.showCheckTable(table_name))) {
			inventoryMapper.copyInventoryCheck(table_name);
		}
		return check.getId();
	}
	@Override
	public int updateInventoryCheckCancel(InventoryCheck check) {
		
		//1.?????????????????????inventory_sku_check????????????
		inventoryMapper.updateInventoryCheckCancel(check);
		
		//2.??????inventory_sku_check_record_temp??????????????????
		int dicRecord = inventoryMapper.deleteInventoryCheckRecord(check.getId());
		
		return dicRecord;
	}
	@Override
	public List<Map<String, Object>> getInventoryCatList() {
		return inventoryMapper.getInventoryCatList();
	}
	@Override
	public int insertInventoryCheckRecord(InventoryCheckRecord record) {
		inventoryMapper.insertInventoryCheckRecord(record);
		return record.getId();
	}
	@Override
	public int updateInventoryCheckRecord(InventoryCheckRecord record) {
		return inventoryMapper.updateInventoryCheckRecord(record);
	}
	@Override
	public List<InventoryCheckRecord> doneInventoryCheckRecord(int checkId,int admid) {
		
		//1.???inventory_sku_check_record_temp ???????????????????????????inventory_sku_check_record
		inventoryMapper.doneInventoryCheckRecord(checkId);
		
		//2.??????inventory_sku_check_record_temp??????????????????
		List<InventoryCheckRecord> iCRList = inventoryMapper.getInventoryCheckRecord(checkId);
		
		//3.?????????????????????inventory_sku
		for(InventoryCheckRecord i : iCRList) {
			InventorySku item = new InventorySku();
			item.setId(i.getInventorySkuId());
			item.setRemaining(i.getCheckRemaining());
			item.setCanRemaining(i.getCheckRemaining());
			item.setInventoryCheckId(checkId);
			item.setCheckRemaining(i.getCheckRemaining());
			item.setBarcode(i.getAfterBarcode());
			inventoryMapper.updateInventoryCheckFlag(item );
			
			int before_remaining = i.getInventoryRemaining();
			
			int after_remaining = i.getCheckRemaining();
			int inventory_count = Math.abs(after_remaining - before_remaining );
			
			Map<String, String> inventory = new HashMap<>();
			//4.???????????????????????????inventory_sku_log
			inventory.put("inventory_sku_id", String.valueOf(i.getInventorySkuId()));
			inventory.put("admid", String.valueOf(admid));
			inventory.put("inventory_count", String.valueOf(inventory_count));
			inventory.put("log_remark", i.getCreateTime()+"????????????");
			inventory.put("before_remaining", String.valueOf(before_remaining));
			inventory.put("after_remaining", String.valueOf(after_remaining));
			inventory.put("change_type", "3");
			inventoryMapper.addInventoryLogByInventoryid(inventory);
			
			//5.??????????????????inventory_details_sku
			inventory.put("type", "4");
			inventory.put("sku_details_remark", "????????????");
			inventoryMapper.addInventoryDetailsSku(inventory );
		}
		
		//6.??????inventory_sku_check_record_temp??????????????????
		inventoryMapper.deleteInventoryCheckRecord(checkId);
		
		//7.???????????????????????? inventory_sku_check
		inventoryMapper.updateInventoryCheckDone(checkId);
		
		return iCRList;
	}
	@Override
	public List<InventoryCheckRecord> getICRHistory(int inid,int page,String goodsPid) {
		return inventoryMapper.getICRHistory(inid, page,goodsPid);
	}
	@Override
	public int getICRHistoryCount(int inid,String goodsPid) {
		return inventoryMapper.getICRHistoryCount(inid,goodsPid);
	}
	
	/**???????????????????????????
	 * @param map
	 */
	private void addLockInventory(Map<String,String> map) {
//			String odid =  map.get("odid");
//			????????????????????????
		//?????????????????????????????????????????????????????????
		/*if("true".equals(map.get("useAllInventory"))) {
			updateOrderState(map);
		}*/
		//??????????????????????????????
		//????????????
		int odId = Integer.parseInt(StrUtils.object2NumStr(map.get("od_id")));
		int inId = Integer.parseInt(StrUtils.object2NumStr(map.get("inventory_sku_id")));
		InventoryLock ilock = new InventoryLock();
		ilock.setFlag(0);
		ilock.setInId(inId);
		ilock.setIsDelete(0);
		ilock.setIsUse(1);
		ilock.setOdId(odId);
		int inventoryCount = Integer.parseInt(StrUtils.object2NumStr(map.get("inventory_count")));
		ilock.setLockRemaining(inventoryCount);
		inventoryMapper.insertLockInventory(ilock);
		
		//???????????????????????????????????????
		InventoryBarcodeRecord record = new  InventoryBarcodeRecord();
		record.setAdmid(Integer.parseInt(StrUtils.object2NumStr(map.get("admId"))));
		record.setInventoryBarcode(map.get("inventory_barcode"));
		record.setInventoryId(inId);
		record.setLockId(ilock.getId());
		record.setOdId(odId);
		record.setOrderBarcode(map.get("barcode"));
		record.setState(0);
		record.setChangeNum(inventoryCount);
		inventoryMapper.insertInventoryBarcodeRecord(record );
		
	}
	
	/**???????????????????????????
	 * @param map
	 */
	private void updateOrderState(Map<String,String> map){
		try {

			//???????????????????????? ?????????????????????
//			order_details.state=1 order_details.checked=1 
			orderinfoMapper.updateChecked(map);

			// ??????????????????
			// orderinfoMapper.insertInventoryIdRelationtable(map);
			
			//???????????????DP??????
			int isDropshipOrder=orderinfoMapper.queyIsDropshipOrder(map);
			//?????????????????????????????????????????????ID???????????????????????????????????????????????????????????????????????????
			Map<String,Object> orderinfoMap = pruchaseMapper.queryUserIdAndStateByOrderNo(map.get("orderid"));
			
			orderinfoMapper.updateOrderDetails(map);
			SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
			

			
			if (isDropshipOrder == 1) {
				int counts=orderinfoMapper.getDtailsState(map);
//				orderinfoMapper.updateOrderDetails(map);
//				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
//				int counts=orderinfoMapper.getDtailsState(map);
				if(counts == 0){
					orderinfoMapper.updateDropshiporder(map);
					SendMQ.sendMsg(new RunSqlModel("update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"+map.get("orderid")+"' " +
							"and id='"+map.get("odid")+"')"));
				}
				//??????????????????????????????????????????
				counts=orderinfoMapper.getAllChildOrderState(map);
				/*if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//??????????????????????????????
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//?????????????????????
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}*/
			}else{
				// ???dropshi??????
//				orderinfoMapper.updateOrderDetails(map);
//				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
				//??????????????????????????????
//				int counts=orderinfoMapper.getDetailsState(map);
				/*if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//??????????????????????????????
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//?????????????????????
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}*/
			}
			int counts=orderinfoMapper.getDetailsState(map);
			if(counts == 0){
				orderinfoMapper.updateOrderInfoState(map);
				SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
				//??????????????????????????????
				if(!orderinfoMap.get("old_state").toString().equals("2")){
					//?????????????????????
					NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
							map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
				}
			}
			orderinfoMap.clear();
			//??????????????????
			LOG.info("--------------------????????????????????????--------------------");
			map.put("old_itemqty",map.get("inventory_count_use"));
			map.put("remark", "?????????????????????????????????????????? inventory_sku_id:"+map.get("inventory_sku_id")+"/orderid:"+map.get("orderid")+"/od_id:"+map.get("od_id"));
			orderinfoMapper.insertGoodsInventory(map);
			LOG.info("--------------------????????????????????????--------------------");

			
			//insertOrderProductSource
			pruchaseMapper.insertOrderProductSource(map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("????????????????????????",e);
		}
		
	}
	
	
	@Override
	public int cancelOrderToInventory(String orderNo,int admId,String admName) {
		// ??????????????????????????????
		int cancelUseInventory = cancelUseInventory(orderNo,admId,null);
		
		//?????????????????????????????????????????????????????????????????????
		int addOrderInventory = addOrderInventory(orderNo, admId,admName,null);
		
		return cancelUseInventory + addOrderInventory;
	}
	/**
	 * @return
	 */
	private int addOrderInventory(String orderNo,int admId,String admName,String odid) {
		List<Map<String,Object>> checkedOrderDetails = null;
		if(StringUtil.isBlank(orderNo)) {
			checkedOrderDetails = inventoryMapper.getCheckedOrderDetailsByOdid(odid);
		}else {
			checkedOrderDetails = inventoryMapper.getCheckedOrderDetailsByOrderno(orderNo);
		}
		if(checkedOrderDetails == null || checkedOrderDetails.isEmpty()) {
			return 0;
		}
		InventoryTemporary inventory = null;
		for(Map<String,Object> c : checkedOrderDetails) {
			inventory = new InventoryTemporary();
			
			int lock_remaining = Integer.parseInt(StrUtils.object2NumStr(c.get("lock_remaining")));
			int yourder =  Integer.parseInt(StrUtils.object2NumStr(c.get("yourorder")));
			int seilUnit =  1;//Integer.parseInt(StrUtils.object2NumStr(c.get("seilUnit")));
			int inventoryCount = yourder * seilUnit - lock_remaining;
			
			
			inventory.setInventory_count(String.valueOf(inventoryCount));
			inventory.setTbskuid(StrUtils.object2Str(c.get("tbskuid")));
			inventory.setTbspecid(StrUtils.object2Str(c.get("tbspecid")));
			inventory.setShipno(StrUtils.object2Str(c.get("shipno")));
			inventory.setBarcode(StrUtils.object2Str(c.get("barcode")));
			inventory.setCar_type(StrUtils.object2Str(c.get("car_type")));
			inventory.setSkuid(StrUtils.object2Str(c.get("skuid")));
			inventory.setSpecid(StrUtils.object2Str(c.get("specid")));
			inventory.setOdid(StrUtils.object2Str(c.get("od_id")));
			inventory.setAdminId(String.valueOf(admId));
			inventory.setAdmName( admName);
			inventory.setLog_remark(StrUtils.object2Str(c.get("orderid"))+"/"+StrUtils.object2Str(c.get("od_id"))+"????????????,?????????????????????????????????");
			inventory.setUnit( String.valueOf(seilUnit));
			inventory.setStorage_count(String.valueOf(yourder) );
			inventory.setWhen_count(String.valueOf(yourder * seilUnit) );
			inventory.setGoodid( StrUtils.object2Str(c.get("goodsid"))); 
			inventory.setStorage_type("3"); 
			inventory.setOrderid( StrUtils.object2Str(c.get("orderid"))); 
//			int in_id = addInventory(inventory);
			
			//??????????????????
			inventoryMapper.addInventoryTemporary(inventory);
			int tem_id = inventory.getId();
			
			
			if(inventoryCount != 0) {
				int odId = Integer.parseInt(StrUtils.object2NumStr(c.get("od_id")));
				Map<String, Object> addInventory = inventoryMapper.getAddInventory(odId);
				
				//????????????????????????
				InventoryBarcodeRecord record = new InventoryBarcodeRecord();
				record.setAdmid(admId);
				record.setInventoryBarcode(addInventory!=null?StrUtils.object2Str(addInventory.get("barcode")):"SHCR001001003");
				record.setTemId(tem_id);
				int inid = Integer.parseInt(addInventory!=null?StrUtils.object2NumStr(addInventory.get("in_id")):"0");
				record.setInventoryId(inid == 0 ? 0 : inid);
				record.setLockId(0);
				record.setState(1);
				record.setRemark(StrUtils.object2Str(c.get("orderid"))+"/"+StrUtils.object2Str(c.get("od_id"))+"??????????????????");
				record.setOrderBarcode(StrUtils.object2Str(c.get("barcode")));
				record.setOdId(odId);
				record.setChangeNum(inventoryCount);
				inventoryMapper.insertInventoryBarcodeRecord(record );
			}
		}
		return checkedOrderDetails.size();
	}
	
	/**????????????????????????????????????
	 * @return
	 */
	private int cancelUseInventory(String orderNo,int admId,String odid) {
		//?????????????????????????????????
		List<Map<String,Object>> inventoryUsed =  null;
		if(StringUtil.isNotBlank(orderNo)) {
			inventoryUsed =  inventoryMapper.getInventoryUsedByOrderno(orderNo);
		}else {
			inventoryUsed =  inventoryMapper.getInventoryUsedByOdid(odid);
		}
		if(inventoryUsed == null || inventoryUsed.isEmpty()) {
			return 0;
		}
		for(Map<String,Object> i :  inventoryUsed) {
			int lockInventoryFlag = Integer.parseInt(StrUtils.object2NumStr(i.get("li_flag")));
			if(lockInventoryFlag == 1) {
			     //?????????????????????????????????????????????, ?????????????????????
				inventoryMapper.updateBarcodeRecord(Integer.parseInt(StrUtils.object2NumStr(i.get("barcodeId"))), 1);
			}
			
			//??????lock_inventory??????is_delete=1
			inventoryMapper.cancelLockInventory(Integer.parseInt(StrUtils.object2NumStr(i.get("li_id"))));
			
			int canRemaining = Integer.parseInt(StrUtils.object2NumStr(i.get("can_remaining")));
			int remaining = Integer.parseInt(StrUtils.object2NumStr(i.get("remaining")));
			int lockRemaining = Integer.parseInt(StrUtils.object2NumStr(i.get("lock_remaining")));
			int inventorySkuId = Integer.parseInt(StrUtils.object2NumStr(i.get("in_id")));
			
			//??????id_relationtable ????????????????????? ???is_delete=1 ????????????????????? goodstatus=5
//			ir.id as ir_id,ir.goodstatus as ir_goodstatus,ir.itemqty,
			int itemqty = Integer.parseInt(StrUtils.object2NumStr(i.get("itemqty")));
			int goodstatus = Integer.parseInt(StrUtils.object2NumStr(i.get("ir_goodstatus")));
			int ir_id = Integer.parseInt(StrUtils.object2NumStr(i.get("ir_id")));
			inventoryMapper.updateidRelationState(ir_id,itemqty == lockRemaining ? 1 : 0);
			
			//????????????inventory_sku
			InventorySku isku = new InventorySku();
			isku.setId(inventorySkuId);
			isku.setRemaining(remaining + lockRemaining);
			isku.setCanRemaining(canRemaining + lockRemaining);
			inventoryMapper.updateInventory(isku);
			
			
			//?????????????????? inventory_sku_log
			Map<String,String> ilog = new HashMap<>();
			ilog.put("inventory_count",StrUtils.object2NumStr(i.get("lock_remaining")));
			ilog.put("before_remaining",StrUtils.object2NumStr(i.get("remaining")));
			ilog.put("after_remaining",String.valueOf(remaining + lockRemaining));
			ilog.put("log_remark","??????"+StrUtils.object2Str(i.get("orderid"))+"/"+StrUtils.object2NumStr(i.get("od_id"))+"??????,????????????????????????");
			ilog.put("change_type","1");
			ilog.put("inventory_sku_id",StrUtils.object2NumStr(i.get("in_id")));
			ilog.put("od_id", StrUtils.object2NumStr(i.get("od_id")));
			inventoryMapper.addInventoryLogByInventoryid(ilog);
			
			//??????????????? inventory_details_sku
			ilog.put("admid",String.valueOf(admId));
			ilog.put("type","0");
			ilog.put("sku_details_remark","??????"+StrUtils.object2Str(i.get("orderid"))+"/"+StrUtils.object2NumStr(i.get("od_id"))+"??????,????????????????????????");
			inventoryMapper.addInventoryDetailsSku(ilog);
			
		}
		return 1;
	}
	@Override
	public int cancelToInventory(String[] odidLst, int admid, String admName) {
		if(odidLst == null) {
			return 0;
		}
		int result  =0;
		for(String odid : odidLst) {
			result += cancelUseInventory(null, admid, odid);
			addOrderInventory(null, admid, admName, odid);
		}
		return result;
	}
	@Override
	public List<InventoryWrap> inventoryBarcodeList(Map<String, Object> map) {
		List<InventoryWrap> inventoryBarcodeList = inventoryMapper.inventoryBarcodeList(map);
		if(inventoryBarcodeList == null || inventoryBarcodeList.isEmpty()) {
			return null;
		}
		List<String> lstSkuid = Lists.newArrayList();
		for(InventoryWrap i : inventoryBarcodeList) {
			if(StringUtil.isBlank(i.getIskSkuid())) {
				continue;
			}
			lstSkuid.add(i.getIskSkuid());
		}
		List<Map<String,Object>> returnGoods = inventoryMapper.returnGoods(lstSkuid);
		Map<String,Integer> setM = Maps.newHashMap();
		for(Map<String,Object> r : returnGoods) {
			setM.put(r.get("skuID").toString(), Integer.parseInt(r.get("count").toString()));
		}
		for(InventoryWrap i : inventoryBarcodeList) {
			if(i.getIbState() == 0) {
				i.setStateContext("??????????????????,????????????????????????");
			}else if(i.getIbState() == 1) {
				i.setStateContext("???????????????????????????????????????");
			}else if(i.getIbState() == 2) {
				i.setStateContext("?????????????????????");
			}else if(i.getIbState() == 3) {
				i.setStateContext("?????????????????????");
			}else if(i.getIbState() == 4) {
				i.setStateContext("???????????????????????????");
			}else if(i.getIbState() == 5) {
				i.setStateContext("???????????????????????????");
			}
			String iskSCarImg = i.getIskSCarImg();
			iskSCarImg = iskSCarImg == null ? iskSCarImg : iskSCarImg.replace(".60x60.jpg", ".400x400.jpg");
			String odCarImg = i.getOdCarImg();
			odCarImg = odCarImg == null ? odCarImg : odCarImg.replace(".60x60.jpg", ".400x400.jpg");
			
			i.setIskSCarImg(iskSCarImg);
			i.setOdCarImg(odCarImg);
			
			Integer returnCount = setM.get(i.getIskSkuid());
			returnCount = returnCount == null ? 0 : returnCount;
			i.setReturnOrderNum(returnCount);
			
		}
		return inventoryBarcodeList;
	}
	@Override
	public int inventoryBarcodeListCount(Map<String, Object> map) {
		return inventoryMapper.inventoryBarcodeListCount(map);
	}
	@Override
	public List<InventoryLog> inventoryLogList(Map<String, Object> map) {
		List<InventoryLog> inventoryLogList = inventoryMapper.inventoryLogList(map);
		if(inventoryLogList == null || inventoryLogList.isEmpty()) {
			return Lists.newArrayList();
		}
		for(InventoryLog i:inventoryLogList) {
			if(i.getChangeType() == 3) {
				i.setRemaining(i.getAfterRemaining() - i.getBeforeRemaining());
			}
		}
		return inventoryLogList;
	}
	@Override
	public int inventoryLogListCount(Map<String, Object> map) {
		return inventoryMapper.inventoryLogListCount(map);
	}
	@Override
	public int moveBarcode(Map<String, Object> mapParam) {
		String position = dao.getPosition((String)mapParam.get("orderbarcode"));
		mapParam.put("position", position);
		int ibid = (int)mapParam.get("ibid");
		InventoryWrap wrap = inventoryMapper.getInventoryBarcode(ibid);
		if(wrap == null) {
			return 0;
		}
		InventorySku item = new InventorySku();
		item.setId(wrap.getInid());
		InventorySku inventoryMap = inventoryMapper.getInventory(item );
		if(inventoryMap == null) {
			return 0;
		}
		//????????????
		if(Integer.parseInt(StrUtils.object2NumStr(mapParam.get("inorout"))) == 0) {
			int lockRemaining = wrap.getLockRemaining();
			//?????????????????????????????????????????????????????????
			if(/*wrap.getOdSeilUnit() * */wrap.getOdYourOrder() == lockRemaining) {
				Map<String,String> map = new HashMap<>();
				map.put("orderid",wrap.getOrderid());
				map.put("odid",String.valueOf(wrap.getOdid()));
				map.put("inventory_sku_id",String.valueOf(wrap.getInid()));
				map.put("od_id",String.valueOf(wrap.getOdid()));
				map.put("inventory_count_use",String.valueOf(lockRemaining));
				map.put("goods_title",inventoryMap.getGoodsName());
				map.put("goods_url",inventoryMap.getGoodsPUrl());
				map.put("googs_img",inventoryMap.getCarImg());
				map.put("goods_price",inventoryMap.getGoodsPrice());
				map.put("itemid",inventoryMap.getGoodsPPid());
				map.put("price",inventoryMap.getGoodsPPrice());
				map.put("goods_pid", inventoryMap.getGoodsPid());
				map.put("orderNo", wrap.getOrderid());
				map.put("goodid", wrap.getGoodsid());
				map.put("purchase_state", "4");
				map.put("buycount", String.valueOf(lockRemaining));
				map.put("orderNumRemarks", "????????????"+lockRemaining);
				updateOrderState(map);
			}
			//????????????inventory_sku ???remaining
			//?????????????????????
			item.setRemaining(inventoryMap.getRemaining() - lockRemaining);
			item.setCanRemaining(inventoryMap.getCanRemaining());
			inventoryMapper.updateInventory(item);
			
			//???????????? #{inventory_count},#{od_id} #{admid},#{type} #{inventory_sku_id}
			Map<String, String> inventory = new HashMap<>();
			inventory.put("inventory_count", String.valueOf(lockRemaining));
			inventory.put("od_id", String.valueOf(wrap.getOdid()));
			inventory.put("admid", StrUtils.object2NumStr(mapParam.get("admid")));
			//0 ??????  1 ?????? 2 ?????? 4??????  5-???????????? 6-???????????? 7-????????????
			inventory.put("type", "6");
			inventory.put("inventory_sku_id",String.valueOf(wrap.getInid()) );
			inventory.put("sku_details_remark","????????????????????????????????????????????????????????????,??????????????????");
			inventoryMapper.addInventoryDetailsSku(inventory );
			
			//????????????#{inventory_count},#{before_remaining},#{after_remaining}, #{log_remark},#{change_type},#{inventory_sku_id}
			inventory.put("before_remaining", String.valueOf(inventoryMap.getRemaining()));
			inventory.put("after_remaining", String.valueOf(inventoryMap.getRemaining() - lockRemaining));
			inventory.put("change_type", "2");
			inventory.put("log_remark","????????????????????????????????????????????????????????????,??????????????????");
			inventoryMapper.addInventoryLogByInventoryid(inventory);
		}else {
			//?????????????????????????????????
			//0?????????????????????,???????????????????????? 1?????????????????????????????????????????? 2???????????????????????? 3???????????????????????? 4?????????????????????????????? 5??????????????????????????????
			int temid = (int)mapParam.get("temid");
			if(temid > 0) {
				Map<String, String> inventoryTemporary = inventoryMapper.getInventoryTemporary(temid);
				int addInventory = addInventory(inventoryTemporary);
				if(addInventory > 0) {
					inventoryMapper.updateInventoryTemporary(temid, 1);
				}
			}
		}
		return inventoryMapper.moveBarcode(mapParam);
	}
	@Override
	public int updateRemark(Map<String, Object> mapParam) {
		int state = (int)mapParam.get("state");
		int ibState = (int)mapParam.get("ibState");
		int ibid = (int)mapParam.get("ibid");
		if(state < 4 || state > 5) {
			return 0;
		}
		InventoryWrap wrap = inventoryMapper.getInventoryBarcode(ibid);
		if(wrap == null) {
			return 0;
		}
		
		int lockRemaining = wrap.getLockRemaining();
		int remaining = wrap.getRemaining();
		int canRemaining = wrap.getCanRemaining();
		int changeNum = wrap.getChangeNum();
		InventorySku isku = new InventorySku();
		isku.setId(wrap.getInid());
		//??????????????????????????????????????????????????????lock_inventory??? ??????????????????????????????
		if(state==4) {
			//?????????????????????
			isku.setRemaining(remaining);
			isku.setCanRemaining(canRemaining + lockRemaining);
			inventoryMapper.updateInventory(isku );
			
			//???????????? #{inventory_count},#{od_id} #{admid},#{type} #{inventory_sku_id}
			Map<String, String> inventory = new HashMap<>();
			inventory.put("inventory_count", String.valueOf(lockRemaining));
			inventory.put("od_id", String.valueOf(wrap.getOdid()));
			inventory.put("admid", StrUtils.object2NumStr(mapParam.get("admid")));
			//0 ??????  1 ?????? 2 ?????? 4??????  5-???????????? 6-???????????? 7-????????????
			inventory.put("type", "7");
			inventory.put("inventory_sku_id",String.valueOf(wrap.getInid()) );
			inventory.put("sku_details_remark","????????????????????????????????????????????????????????????,????????????????????????");
			inventoryMapper.addInventoryDetailsSku(inventory );
			
			//????????????#{inventory_count},#{before_remaining},#{after_remaining}, #{log_remark},#{change_type},#{inventory_sku_id}
			inventory.put("before_remaining", String.valueOf(remaining));
			inventory.put("after_remaining", String.valueOf(remaining));
			//0????????? 1?????????  2????????????3?????????  4?????? 5-????????????
			inventory.put("change_type", "5");
			inventory.put("log_remark","???????????????????????????????????????,??????????????????,????????????????????????");
			inventoryMapper.addInventoryLogByInventoryid(inventory);
			
			if(ibState == 0 ) {
				//??????????????????
				inventoryMapper.cancelLockInventory(wrap.getLiid());
				//??????????????????
				//?????????????????? ??????????????????????????????
				//????????????????????????????????????????????????
				if(wrap.getOdYourOrder() != lockRemaining) {
//					order_details.state=0 order_details.checked=0  id_relationtable.goodstatus=5   order_product_source.purchase_state  order_product_source.is_replenishment
					orderinfoMapper.cancelOrderState(wrap.getOdid());
				}
			}
		}else {
		  int temid = (int)mapParam.get("temid");
		  inventoryMapper.updateInventoryTemporary(temid, -1);
		 /* if(temid == 0) {
			  //?????????????????????
			  isku.setRemaining(Math.max(remaining-changeNum, 0));
			  isku.setCanRemaining(Math.max(canRemaining-changeNum, 0));
			  inventoryMapper.updateInventory(isku );
			  
			  //???????????? #{inventory_count},#{od_id} #{admid},#{type} #{inventory_sku_id}
			  Map<String, String> inventory = new HashMap<>();
			  inventory.put("inventory_count", String.valueOf(changeNum));
			  inventory.put("od_id", String.valueOf(wrap.getOdid()));
			  inventory.put("admid", StrUtils.object2NumStr(mapParam.get("admid")));
			  //0 ??????  1 ?????? 2 ?????? 4??????  5-???????????? 6-???????????? 7-????????????
			  inventory.put("type", "7");
			  inventory.put("inventory_sku_id",String.valueOf(wrap.getInid()) );
			  inventory.put("sku_details_remark","??????????????????????????????????????????????????????????????????,????????????????????????");
			  inventoryMapper.addInventoryDetailsSku(inventory );
			  
			  //????????????#{inventory_count},#{before_remaining},#{after_remaining}, #{log_remark},#{change_type},#{inventory_sku_id}
			  inventory.put("before_remaining", String.valueOf(remaining));
			  inventory.put("after_remaining", String.valueOf(Math.max(remaining-changeNum, 0)));
			  
			  //0????????? 1?????????  2????????????3?????????  4?????? 5-????????????
			  inventory.put("change_type", "5");
			  inventory.put("log_remark","??????????????????????????????????????????????????????????????????,????????????????????????");
			  inventoryMapper.addInventoryLogByInventoryid(inventory);
		  }*/
		}
		return inventoryMapper.updateRemark(mapParam);
	}
	@Override
	public List<LossInventoryWrap> inventoryLossList(Map<String, Object> map) {
		List<LossInventoryWrap> inventoryLossList = inventoryMapper.inventoryLossList(map);
		for(LossInventoryWrap l : inventoryLossList) {
			String img = l.getImg() == null ? "" : l.getImg();
			l.setImg(img.replace(".60x60.jpg", ".400x400.jpg"));
			//0  ?????? 1 ??????  3 ?????? 4 ??????  5 ?????? 7 ????????????
			String changeContext = "";
			switch (l.getChangeType()) {
			case 0:
				changeContext = "??????";
				break;
			case 1:
				changeContext = "??????";
				break;
			case 3:
				changeContext = "??????";
				break;
			case 4:
				changeContext = "??????";
				break;
			case 5:
				changeContext = "??????";
				break;
			case 8:
				changeContext = "??????";
				break;

			default:
				changeContext = "????????????";
				break;
			}
			l.setChangeContext(changeContext);
		}
		
		return inventoryLossList;
	}
	@Override
	public int inventoryLossListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return inventoryMapper.inventoryLossListCount(map);
	}
	@Override
	public int getUnDoneInventoryBarcode() {
		// TODO Auto-generated method stub
		return inventoryMapper.getUnDoneInventoryBarcode();
	}
	@Override
	public List<InventoryCheckWrap> invetoryCheck() {
		//??????????????????
		List<InventoryData> iinOutInventory = inventoryMapper.invetoryCheck();
		if(iinOutInventory == null || iinOutInventory.isEmpty()) {
			return null;
		}
		List<InventoryCheckWrap> result = new ArrayList<>();
		InventoryCheckWrap wrap = null;
		for(InventoryData i : iinOutInventory) {
			wrap = new InventoryCheckWrap();
			
			wrap.setBarcode(i.getBarcode());
			wrap.setGoodsPid(i.getGoodsPid());
			wrap.setGoodsImg("<img src=\""+i.getCarImg()+"\">");
			wrap.setGoodsPrice(i.getGoodsPrice());
			wrap.setGoodsSku(i.getSku());
			wrap.setGoodsSkuid(i.getSkuid());
			wrap.setGoodsSpecid(i.getSpecid());
			wrap.setInventorySkuId(i.getId());
			wrap.setRemaining(i.getRemaining());
			wrap.setCategoryName(i.getCategoryName());
			wrap.setCatid(i.getGoodsCatid());
			wrap.setGoodsName(i.getGoodsName());
			wrap.setOperation(i.getOperation());
			wrap.setCanRemaining(i.getCanRemaining());
			wrap.setInventoryCheckId(i.getInventoryCheckId());
			wrap.setLastCheckRemaining(i.getCheckRemaining());
			
			result.add(wrap);
		}
		
		return result;
	}
	@Override
	public List<String> barcodeList() {
		// TODO Auto-generated method stub
		return inventoryMapper.barcodeList();
	}
	@Override
	public int updateBarcode(Map<String, Object> map) {
		//inventory_sku
		int updateBarcode = inventoryMapper.updateBarcode(map);
		
		//inventory_sku_log
		Map<String, String> inventory = Maps.newHashMap();
		inventory.put("change_type", "0");
		inventory.put("inventory_count", "0");
		inventory.put("before_remaining", StrUtils.object2NumStr(map.get("remaining")));
		inventory.put("after_remaining", StrUtils.object2NumStr(map.get("remaining")));
		inventory.put("inventory_sku_id", map.get("inid").toString());
		inventory.put("log_remark",  map.get("beforeBarcode").toString()+"???????????????"+ map.get("afterBarcode").toString());
		int addInventoryLog = inventoryMapper.addInventoryLogByInventoryid(inventory );
		updateBarcode += addInventoryLog;
		
		return updateBarcode;
	}
}