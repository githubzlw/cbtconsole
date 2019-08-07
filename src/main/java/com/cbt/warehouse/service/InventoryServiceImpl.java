package com.cbt.warehouse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.InventoryDetails;
import com.cbt.website.bean.InventoryDetailsWrap;
import com.cbt.website.bean.InventoryLog;
import com.cbt.website.bean.InventorySku;
import com.cbt.website.bean.LossInventoryRecord;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;
@Service
public class InventoryServiceImpl implements  InventoryService{
	@Autowired
	private InventoryMapper inventoryMapper;
	@Autowired
	private OrderinfoMapper orderinfoMapper;

	/**
	 * 根据ID获取库存
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
		StringBuilder pids = new StringBuilder();
		StringBuilder opration = null;
		for (int i=0,size=toryList.size();i<size;i++) {
			InventoryData t = toryList.get(i);
			String goodscatid = t.getGoodsCatid();
			if(StringUtil.isBlank(goodscatid) || "0".equals(goodscatid)){
				t.setCategoryName("其他");
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
			String imgs[]=car_img.split("kf");
			if(imgs.length > 1) {
				String one=imgs[0];
				String two=imgs[1].replace(".jpg_50x50","");
				url="https://s.1688.com/youyuan/index.htm?tab=imageSearch&from=plugin&imageType="+one+"&imageAddress=kf"+two+"";
			}
			
			t.setCarImg("<a href='"+url+"' title='跳转到网站链接' target='_blank'>"
					+ "<img   class=\"img-responsive\" src='"+ (car_img.indexOf("1.png")>-1?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":car_img) + "' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"+ car_img + "')\" height='100' width='100'></a>");
		
			
			
			
			t.setGoodsUrl(StringUtil.isBlank(t.getGoodsUrl())?"":t.getGoodsUrl());
			t.setCarUrlMD5(StringUtil.isBlank(t.getCarUrlMD5())?"":t.getCarUrlMD5());
			
			pids.append("'").append(t.getGoodsPid()).append("',");
			
			String unsellableReason="--";
			String onLine="状态错误";
			int valid= t.getOnlineFlag();
			if("1".equals(valid)){
				onLine="上架";
			}else if("0".equals(valid)){
				onLine="下架";
				unsellableReason= Utility.getUnsellableReason(t.getUnsellableReason(),unsellableReason);
				t.setUnsellableReason(unsellableReason);
			}else if("2".equals(valid) && (t.getGoodsUrl().indexOf("aliexpress")>-1 || t.getCarUrlMD5().startsWith("A"))){
				onLine="ali产品实时抓取";
			}else{
				onLine="商品已删除";
			}
			t.setOnline(onLine);
			
			if("0".equals(map.get("export"))){
				opration = new StringBuilder();
				//报损/调整
				opration.append("<button class=\"btn btn-default mt5\" onclick=\"updateInventory('0','"+i+"','"+t.getId()+"')\"> ")
				.append("报损调整").append("</button>");
				
				//产品编辑
				opration.append("<br><a target='_blank' href='/cbtconsole/editc/detalisEdit?pid=").append(t.getGoodsPid())
				.append("'><button class=\"btn btn-default mt5\">").append("产品编辑</button></a>");
				
				//库存明细
				opration.append("<br><a target='_blank' href='/cbtconsole/website/inventorydetails.jsp?inid=").append(t.getId())
				.append("'><button class=\"btn btn-default mt5\">").append("库存明细</button></a>");
				
				t.setOperation(opration.toString());
			}
			toryList.set(i, t);
		}
		return toryList;
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
	 * 手动录入库存
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
	 * 根据ID删除库存数据
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
	public int addInventory(Map<String,String> inventory) {
		//1.库存数量不足，不许进入库存
		if(inventory == null) {
			return 0;
		}
		int result = 0;
		//1688订单多余产品进入库存
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
				inventory.put("skuid",StringUtil.isBlank(t.getSkuID()) ? t.getItemid() : t.getSkuID() );
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
				result = result + inventoryOperation(inventory,true);
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
		//inventory_type 0：电商库存  1：亚马逊库存
		iSku.setInventoryType(0);
		//获取订单详情中产品数据：产品id、产品名称、产品价格、产品规格等
		Map<String, String> orderDetails = isTbOrder ? null : inventoryMapper.getOrderDetails(inventory);
		if(!isTbOrder && orderDetails == null) {
			return 0;
		}
		if(orderDetails != null) {
			inventory.putAll(orderDetails);
			//产品sku：若所选产品规格没有值，使用产品id代替规格id
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
		
		//2.是否存在库存
		InventorySku isExsisInventory = inventoryMapper.getInventory(iSku);
		
		iSku.setOdid(Integer.valueOf(StrUtils.object2NumStr(inventory.get("odid"))));
		
		int beforeRemaining = 0;
		int afterRemaining = inventory_count;
		Integer inventory_sku_id  = 0;
		if(isExsisInventory == null) {
			iSku.setRemaining(inventory_count);
			iSku.setCanRemaining(inventory_count);
			inventoryMapper.insertInventory(iSku);
			//3.库存表id
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
			//更改前库存
			beforeRemaining = remaining;
			//更改后库存
			afterRemaining = beforeRemaining + inventory_count;
					
			//可用库存
			can_remaining = inventory_count + can_remaining;
			
			//更新后库存增加
			iSku.setRemaining(afterRemaining);
			iSku.setCanRemaining(can_remaining);
			iSku.setId(inventory_sku_id);
			iSku.setGoodsPid(isExsisInventory.getGoodsPid());
			inventoryMapper.updateInventory(iSku);
		}
		inventory.put("before_remaining", String.valueOf(beforeRemaining));
		inventory.put("after_remaining", String.valueOf(afterRemaining));
		
		//4.插入库存变更记录 change_type 1:入库 2：出库
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
		iLog.setRemark("验货,增加库存");
		iLog.setSku(iSku.getSku());
		iLog.setSkuid(iSku.getSkuid());
		iLog.setSpecid(iSku.getSpecid());
		inventoryMapper.insertInventoryLog(iLog);
		
		//5.库存关联入库记录 插入storage_outbound_details记录storage_outbound_details
		if(!isTbOrder) {
			inventory.put("inventory_sku_id", String.valueOf(inventory_sku_id));
			inventoryMapper.insertStorageOutboundDetails(inventory);
		}
		//6.记录库存入库明细操作
		//入库 0 入库  1 出库
		iDetail.setGoodsPPrice(iSku.getGoodsPPrice());
		iDetail.setType(0);
		iDetail.setAdmid(Integer.valueOf(StrUtils.object2NumStr(inventory.get("adminId"))));
		iDetail.setGoodsName(iSku.getGoodsName());
		iDetail.setGoodsImg(iSku.getCarImg());
		iDetail.setGoodsNumber(inventory_count);
		iDetail.setGoodsPid(iSku.getGoodsPid());
		iDetail.setGoodsPrice(iSku.getGoodsPrice());
		iDetail.setGoodsUrl(iSku.getGoodsUrl());
		iDetail.setInventoryId(inventory_sku_id);
		iDetail.setSku(iSku.getSku());
		iDetail.setOrderno(inventory.get("orderid"));
		iDetail.setOdId(Integer.valueOf(StrUtils.object2NumStr(inventory.get("odid"))));
		iDetail.setGoodsSkuid(iSku.getSkuid());
		iDetail.setGoodsSpecid(iSku.getSpecid());
		
		return inventoryMapper.insertInventoryDetailsSku(iDetail);
		
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
		//如果库存大于客户订单下单数量，则全部使用库存，若不够在采购其他的
		if(googs_number * goodsUnit < inventory_count) {
			inventory_count = googs_number * goodsUnit;
		}
		//1.如果该商品是有录入库存则做想应的减少
		String id = map.get("inventory_sku_id");
		InventorySku iSku = new InventorySku();
		iSku.setId(Integer.valueOf(id));
		
		int updateInventoryById = reduseInventory(map, iSku, inventory_count, true);
		
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
		iSku.setGoodsPid(map.get("specid"));
		iSku.setSkuid(map.get("skuid"));
		Map<String, String> orderDetails = inventoryMapper.getOrderDetails(map);
		iSku.setGoodsPid(orderDetails.get("goods_pid"));
		
		//库存减少操作
		reduseInventory(map, iSku, cancelCount, false);
		
		//删除验货时的记录storage_outbound_details
		return orderinfoMapper.updateUutboundDetails(map);
	}
	
	
	/**库存减少
	 * @param map
	 * @param isku
	 * @param isReduce true 使用库存  false 取消验货库存
	 * @return
	 */
	private int reduseInventory(Map<String, String> map,InventorySku iSku,int inventory_count,boolean isReduce) {
		
		//如果该商品验货是有录入库存则做想应的减少
		InventorySku inventoryMap = inventoryMapper.getInventory(iSku);
		if(inventoryMap == null){
			return 0;
		}
		if(isReduce) {
			//采购使用库存锁定库存
			map.put("is_use", "1");
//			inventoryMapper.insertLockInventory(map);
		}
		int before_remaining = inventoryMap.getRemaining();
		int can_remaining = inventoryMap.getCanRemaining();
		
		int after_remaining = before_remaining - inventory_count;
		can_remaining = can_remaining - inventory_count;
		
		//1.库存减少
		iSku.setId(inventoryMap.getId());
		iSku.setRemaining(after_remaining);
		iSku.setCanRemaining(can_remaining);
		int updateInventory = inventoryMapper.updateInventory(iSku);
		
		//2.库存变更记录
		InventoryLog iLog = new InventoryLog();
		iLog.setSku(inventoryMap.getSku());
		iLog.setAfterRemaining(after_remaining);
		iLog.setBeforeRemaining(before_remaining);
		iLog.setRemaining(inventory_count);
		iLog.setChangeType(2);
		String orderid = map.get("orderid");
		String odid =  map.get("odid");
		if(isReduce) {
			iLog.setRemark("订单orderid:"+orderid+"/od_id:"+odid+"采购使用库存数量"+inventory_count+"，库存减少");
		}else {
			iLog.setRemark("订单orderid:"+orderid+"/od_id:"+odid+" 验货取消,取消库存数量:"+inventory_count);
		}
		iLog.setGoodsName(inventoryMap.getGoodsName());
		iLog.setGoodsPid(inventoryMap.getGoodsPid());
		iLog.setGoodsPPid(inventoryMap.getGoodsPPid());
		iLog.setGoodsUrl(inventoryMap.getGoodsUrl());
		iLog.setInventorySkuId(inventoryMap.getId());
		iLog.setSkuid(inventoryMap.getSkuid());
		iLog.setSpecid(inventoryMap.getSpecid());
		inventoryMapper.insertInventoryLog(iLog);
		
		//3.记录库存入库明细操作
		//入库 0 入库  1 出库
		map.put("type", "1");
		map.put("inventory_sku_id", String.valueOf(inventoryMap.getId()));
		map.put("inventory_count", String.valueOf(inventory_count));
		inventoryMapper.addInventoryDetailsSku(map);
		return updateInventory;
	}
	
	
	@Override
	@Transactional
	public Map<String,Object> reportLossInventory(Map<String, Object> map) {
		Map<String, String> inv = new HashMap<String, String>();
		Map<String,Object> result = new HashMap<String, Object>();
		
		String inventory_sku_id = (String)map.get("inventory_sku_id" );
		InventorySku iSku = new InventorySku();
		iSku.setId(Integer.valueOf(inventory_sku_id));
		InventorySku inventoryByid = inventoryMapper.getInventory(iSku);
		if(inventoryByid == null ) {
			result.put("status", 100);
			result.put("reason", "库存不存在");
			return result;
		}
		int changeNumber = (int)map.get("changeNumber");
		if(changeNumber < 1) {
			result.put("status", 101);
			result.put("reason", "库存修改数量错误");
			return result;
		}
		int remaining = inventoryByid.getRemaining();
		int can_remaining = inventoryByid.getCanRemaining();
		
		//库存变更数量
		int change_number = Math.abs(remaining - changeNumber);
		
		int before_remaining = remaining;
		int after_remaining = changeNumber;
		
		inv.put("goods_p_url", inventoryByid.getGoodsPUrl());
		inv.put("goods_p_price", inventoryByid.getGoodsPPrice());
		//库存减少
		if(remaining > changeNumber) {
			inv.put("can_remaining", String.valueOf(can_remaining - change_number));
			iSku.setCanRemaining(can_remaining - change_number);
			inv.put("change_type", "2");
		}else {
			inv.put("can_remaining", String.valueOf(can_remaining + change_number));
			iSku.setCanRemaining(can_remaining + change_number);
			inv.put("change_type", "1");
		}
		inv.put("remaining", String.valueOf(after_remaining));
		iSku.setRemaining(after_remaining);
		
		//1.损益记录表loss_inventory_record
		 map.put("change_number", change_number);
		 
		 LossInventoryRecord record = new LossInventoryRecord();
		 record.setChangeAdm(Integer.valueOf(StrUtils.object2NumStr(map.get("change_adm"))));
		 record.setChangeNumber(change_number);
		 record.setChangeType(Integer.valueOf(StrUtils.object2NumStr(map.get("change_type"))));
		 record.setGoodsPid((String)map.get("goods_pid"));
		 record.setInventorySkuId(Integer.valueOf(inventory_sku_id));
		 record.setSkuid((String)map.get("skuid"));
		 record.setSpecid((String)map.get("specid"));
		 record.setId(0);
		 inventoryMapper.addLossInventoryRecord(record);
		 if(record.getId() == 0) {
			 result.put("status", 102);
			 result.put("reason", "损益记录表loss_inventory_record错误");
			 return result;
		 }
		 int lossInventoryRecordid = record.getId();
		//2.库存表更新  inwentory_sku
		inv.put("inventory_sku_id", inventory_sku_id);
		int updateInventoryById = inventoryMapper.updateInventory(iSku);
		if(updateInventoryById == 0) {
			 result.put("status", 103);
			 result.put("reason", "库存表更新  inwentory_sku错误");
			 return result;
		}
		
		//3.库存变更表 inventory_sku_log
		inv.put("inventory_count", String.valueOf(change_number));
		inv.put("before_remaining", String.valueOf(before_remaining));
		inv.put("after_remaining", String.valueOf(after_remaining));
		inv.put("log_remark", "库存报损,loss_inventory_record_id:"+lossInventoryRecordid);
		int inventoryChangeRecordid = inventoryMapper.addInventoryChangeRecordByInventoryid(inv);
		if(inventoryChangeRecordid == 0) {
			result.put("status", 104);
			result.put("reason", "库存变更表 inventory_sku_log错误");
			return result;
		}
		
		
		//4.库存明细表 inventory_details_sku
		 inv.put("type", "2");
		 inv.put("admid", String.valueOf(map.get("change_adm")));
		 int addInventoryDetailsSku = inventoryMapper.addInventoryDetailsSku(inv);
		 if(addInventoryDetailsSku == 0) {
			result.put("status", 105);
			result.put("reason", "库存明细表 inventory_details_sku错误");
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
		String skuContext = "",orderContext="",delContext="",typeContext="";
		for(InventoryDetails i : inventoryDetails) {
			wrap = new InventoryDetailsWrap();
			wrap.setCreatetime(i.getCreatetime());
			wrap.setGoodsImg("<img class='img_class' src='"+i.getGoodsImg()+"'>");
			wrap.setGoodsNumber(i.getGoodsNumber());
			wrap.setGoodsPid(i.getGoodsPid());
			wrap.setInventoryId(i.getInventoryId());
			wrap.setId(i.getId());
			
			skuContext = StringUtil.isBlank(i.getSku()) ? "" : "sku:" + i.getSku();
			skuContext = StringUtil.isBlank(i.getGoodsSkuid()) ? skuContext : skuContext + "<br>Skuid:" + i.getGoodsSkuid();
			skuContext = StringUtil.isBlank(i.getGoodsSpecid()) ? skuContext : skuContext + "<br>Specid:" + i.getGoodsSpecid();
			
			orderContext = StringUtil.isBlank(i.getOrderno()) ? "" : "orderno:" + i.getOrderno();
			orderContext = orderContext + "<br>od_id:" + i.getOdId();
			
			if(i.getDel() == 1) {
				delContext = "时间:"+i.getDelDatetime()+"<br>删除人员:"+i.getDelAdm();
				delContext = StringUtil.isBlank(i.getDelRemark()) ? delContext : delContext + "<br>删除原因:" + i.getDelRemark();
			}
			// '0 入库  1 出库 2 报损',
			typeContext = i.getType() == 0 ? "入库" : i.getType() == 1? "出库" : " 报损";
			wrap.setSkuContext(skuContext);
			wrap.setDelContext(delContext );
			wrap.setOrderContext(orderContext);
			wrap.setTypeContext(typeContext);
			
			result.add(wrap);
		}
		
		return result;
		
	}
	@Override
	public  int inventoryDetailsCount(Map<String,Object> map){
		return inventoryMapper.inventoryDetailsCount(map);
	}
	@Override
	public int inputInventory(Map<String, String> param) {
		String goodsUrl = "https://www.import-express.com/goodsinfo/aa-0-0-"+param.get("goods_pid")+".html";
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
		//库存inventory_sku
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
		//1添加  2补货 3线下单 4其他
		if("1".equals(param.get("reasonType"))) {
			iLog.setRemark("录入库存,添加,备注:"+param.get("remark"));
		}else if("2".equals(param.get("reasonType"))) {
			iLog.setRemark("录入库存,补货,备注:"+param.get("remark"));
		}else if("3".equals(param.get("reasonType"))) {
			iLog.setRemark("录入库存,线下单,备注:"+param.get("remark"));
		}else{
			iLog.setRemark("录入库存,其他,备注:"+param.get("remark"));
		}
		iLog.setSku(param.get("sku"));
		iLog.setSkuid(param.get("skuid"));
		iLog.setSpecid(param.get("specid"));
		
		//库存日志inventory_sku_log
		inventoryMapper.insertInventoryLog(iLog);
		
		iDetail.setAdmid(Integer.valueOf(StrUtils.object2NumStr(param.get("admid"))));
		iDetail.setGoodsImg(param.get("img"));
		iDetail.setGoodsName(param.get("goods_name"));
		iDetail.setGoodsNumber(change_remaining);
		iDetail.setGoodsPid(param.get("goods_pid"));
		iDetail.setGoodsPrice(param.get("goods_price"));
		iDetail.setGoodsSkuid(param.get("skuid"));
		iDetail.setGoodsSpecid(param.get("specid"));
		iDetail.setType(0);
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
		
		//库存明细inventory_details_sku
		return inventoryMapper.insertInventoryDetailsSku(iDetail);
	}
	@Override
	public List<Map<String, Object>> getTbGoods(String orderShipno) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}