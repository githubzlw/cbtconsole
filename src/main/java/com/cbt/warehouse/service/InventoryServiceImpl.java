package com.cbt.warehouse.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection.StringTuple;
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
import com.cbt.website.bean.InventoryWrap;
import com.cbt.website.bean.LossInventoryRecord;
import com.cbt.website.bean.LossInventoryWrap;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.mysql.fabric.xmlrpc.base.Data;
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
			if(StringUtil.isNotBlank(car_img)) {
				car_img = car_img.replace(".60x60.jpg", ".400x400.jpg");
//				String imgs[]=car_img.split("kf");
//				if(imgs.length > 1) {
//					String one=imgs[0];
//					String two=imgs[1].replace(".jpg_50x50","");
//					url="https://s.1688.com/youyuan/index.htm?tab=imageSearch&from=plugin&imageType="+one+"&imageAddress=kf"+two+"";
//				}
//				
//				t.setCarImg("<a href='"+url+"' title='跳转到网站链接' target='_blank'>"
//						+ "<img   class=\"img-responsive\" src='"+ (car_img.indexOf("1.png")>-1?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":car_img) + "' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"+ car_img + "')\" height='100' width='100'></a>");
				
				
				t.setCarImg(car_img);
			}
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
				opration.append("<button class=\"btn btn-info mt5 btn-check-list\" onclick=\"updateCheck('0','"+i+"','"+t.getId()+"')\"> ")
				.append("库存盘点").append("</button><br>");
				
				//报损/调整
				opration.append("<button class=\"btn btn-info mt5\" onclick=\"updateInventory('0','"+i+"','"+t.getId()+"')\"> ")
				.append("报损调整").append("</button>");
				
				//产品编辑
				opration.append("<br><a target='_blank' href='/cbtconsole/editc/detalisEdit?pid=").append(t.getGoodsPid())
				.append("'><button class=\"btn btn-warning mt5\">").append("产品编辑</button></a>");
				
				//库存明细
				opration.append("<br><a target='_blank' href='/cbtconsole/website/inventorydetails.jsp?inid=").append(t.getId())
				.append("'><button class=\"btn btn-success mt5\">").append("库存明细</button></a>");
				
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
		
		//4.插入库存变更记录 change_type 0：默认 1：增加  2：减少，3：盘点  4占用 5-取消占用
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
			iLog.setRemark("验货,增加库存");
			iDetail.setRemark("odid:"+StrUtils.object2NumStr(inventory.get("odid"))+"验货,增加库存");
		}else {
			iLog.setRemark(inventory.get("log_remark"));
			iDetail.setRemark(inventory.get("log_remark"));
		}
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
		//0 入库  1 出库 2 报损 4盘点  5-入库完成 6-出库完成 7-移库取消
		iDetail.setGoodsPPrice(iSku.getGoodsPPrice());
		iDetail.setType(5);
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
		//如果库存大于客户订单下单数量，则全部使用库存，若不够在采购其他的
		if(googs_number * goodsUnit < inventory_count) {
			inventory_count = googs_number * goodsUnit;
			map.put("useAllInventory", "true");
		}
		map.put("inventory_count", String.valueOf(inventory_count));
		map.put("inventory_count_use", String.valueOf(inventory_count));
		
		//1.如果该商品是有录入库存则做想应的减少
		String id = map.get("inventory_sku_id");
		InventorySku iSku = new InventorySku();
		iSku.setId(Integer.valueOf(id));
		
		//获取库存可用数量  先占用库存
		//如果该商品验货是有录入库存则做想应的减少
		InventorySku inventoryMap = inventoryMapper.getInventory(iSku);
		if(inventoryMap == null){
			return 0;
		}
		int canRemaining = inventoryMap.getCanRemaining();
		//更新库存的可用库存can_remaing数量
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
		map.put("orderNumRemarks", "使用库存"+map.get("inventory_count"));
		map.put("goods_pid", inventoryMap.getGoodsPid());
		map.put("inventory_sku_id", String.valueOf(inventoryMap.getId()));
		map.put("inventory_barcode", inventoryMap.getBarcode());
		addLockInventory(map);
		
		//更新库存的可用库存can_remaing数量
		iSku.setRemaining(inventoryMap.getRemaining());
		iSku.setCanRemaining(canRemaining - inventory_count);
		int updateInventoryById = inventoryMapper.updateInventory(iSku);
		
		String orderid = map.get("orderid");
		String odid =  map.get("odid");
		//2.库存变更记录
		InventoryLog iLog = new InventoryLog();
		iLog.setSku(inventoryMap.getSku());
		iLog.setAfterRemaining(inventoryMap.getRemaining());
		iLog.setBeforeRemaining(inventoryMap.getRemaining());
		iLog.setRemaining(inventory_count);
		iLog.setChangeType(4);
		iLog.setRemark("订单orderid:"+orderid+"/od_id:"+odid+"采购先占用库存数量"+inventory_count+",等待仓库确认,可用库存减少");
		map.put("sku_details_remark", "订单orderid:"+orderid+"/od_id:"+odid+"采购先占用库存数量"+inventory_count+",等待仓库确认,可用库存库存减少");
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
		String orderid = map.get("orderid");
		String odid =  map.get("odid");
		
		int before_remaining = inventoryMap.getRemaining();
		int can_remaining = inventoryMap.getCanRemaining();
		
		int after_remaining = before_remaining - inventory_count;
		can_remaining = isReduce ? can_remaining : can_remaining - inventory_count;
		if(after_remaining < 0) {
			return 0;
		}
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
		if(isReduce) {
			iLog.setRemark("订单orderid:"+orderid+"/od_id:"+odid+"采购使用库存数量"+inventory_count+",库存减少");
			iLog.setChangeType(4);
			map.put("sku_details_remark", "订单orderid:"+orderid+"/od_id:"+odid+"采购使用库存数量"+inventory_count+",库存减少");
			map.put("type", "1");
		}else {
			iLog.setRemark("订单orderid:"+orderid+"/od_id:"+odid+" 验货取消,取消库存数量:"+inventory_count);
			iLog.setChangeType(2);
			map.put("sku_details_remark", "订单orderid:"+orderid+"/od_id:"+odid+" 验货取消,取消库存数量:"+inventory_count);
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
		
		//3.记录库存入库明细操作
		//入库 0 入库  1 出库
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
			result.put("reason", "库存不存在");
			return result;
		}
		int changeNumber = (int)map.get("changeNumber");
		if(changeNumber < 0) {
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
		int lossChangeType = Integer.valueOf(StrUtils.object2NumStr(map.get("change_type")));
		inv.put("goods_p_url", inventoryByid.getGoodsPUrl());
		inv.put("goods_p_price", inventoryByid.getGoodsPPrice());
		if(remaining > changeNumber) {
			//库存减少
			inv.put("can_remaining", String.valueOf(can_remaining - change_number));
			iSku.setCanRemaining(can_remaining - change_number);
			inv.put("change_type", "2");
		}else {
			//库存增加
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
		String remark = "库存报损,"+lossInventoryRecordid+"/"+lossChangeType+"备注:"+StrUtils.object2Str(map.get("remark"));
		inv.put("log_remark", remark);
		inv.put("sku_details_remark", remark);
		int inventoryChangeRecordid = inventoryMapper.addInventoryLogByInventoryid(inv);
		if(inventoryChangeRecordid == 0) {
			result.put("status", 104);
			result.put("reason", "库存变更表 inventory_sku_log错误");
			return result;
		}
		
		//4.库存明细表 inventory_details_sku
		 inv.put("admid", String.valueOf(map.get("change_adm")));
		 inv.put("type", "2");
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
		String skuContext = "",orderContext="",delContext="";
		for(InventoryDetails i : inventoryDetails) {
			wrap = new InventoryDetailsWrap();
			wrap.setCreatetime(i.getCreatetime());
			wrap.setGoodsImg("<img class='img_class' src='"+i.getGoodsImg()+"'>");
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
				delContext = "时间:"+i.getDelDatetime()+"<br>删除人员:"+i.getDelAdm();
				delContext = StringUtil.isBlank(i.getDelRemark()) ? delContext : delContext + "<br>删除原因:" + i.getDelRemark();
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
		// 0 入库  1 出库 2 报损 4盘点  5-入库完成 6-出库完成 7-移库取消
		switch (type) {
		case 0:
			result = "入库,等待移库操作";
			break;
		case 1:
			result = "出库，等待移库操作";
			break;
		case 2:
			result = "报损";
			break;
		case 4:
			result = "盘点";
			break;
		case 5:
			result = "入库完成";
			break;
		case 6:
			result = "出库完成";
			break;
		case 7:
			result = "取消移库";
			break;
		default:
			result = "入库";
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
			iDetail.setRemark("录入库存,添加,备注:"+param.get("remark"));
		}else if("2".equals(param.get("reasonType"))) {
			iLog.setRemark("录入库存,补货,备注:"+param.get("remark"));
			iDetail.setRemark("录入库存,补货,备注:"+param.get("remark"));
		}else if("3".equals(param.get("reasonType"))) {
			iLog.setRemark("录入库存,线下单,备注:"+param.get("remark"));
			iDetail.setRemark("录入库存,线下单,备注:"+param.get("remark"));
		}else{
			iLog.setRemark("录入库存,其他,备注:"+param.get("remark"));
			iDetail.setRemark("录入库存,其他,备注:"+param.get("remark"));
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
		
		return inventoryMapper.getTbGoods(orderShipno);
	}
	@Override
	public List<InventoryCheckWrap> invetoryCheckList(Map<Object, Object> map) {
//		List<InventoryCheck> unDoneInventoryCheck = inventoryMapper.getUnDoneInventoryCheck();
		//获取库存数据
		List<InventoryData> iinOutInventory = getIinOutInventory(map);
		if(iinOutInventory == null || iinOutInventory.isEmpty()) {
			return null;
		}
		List<InventoryCheckWrap> result = new ArrayList<>();
		InventoryCheckWrap wrap = null;
		for(InventoryData i : iinOutInventory) {
			wrap = new InventoryCheckWrap();
			
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
		
		//1.更新盘点记录为inventory_sku_check取消盘点
		inventoryMapper.updateInventoryCheckCancel(check);
		
		//2.清空inventory_sku_check_record_temp本次盘点数据
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
		
		//1.将inventory_sku_check_record_temp 本次盘点数据更新到inventory_sku_check_record
		inventoryMapper.doneInventoryCheckRecord(checkId);
		
		//2.获取inventory_sku_check_record_temp本次盘点数据
		List<InventoryCheckRecord> iCRList = inventoryMapper.getInventoryCheckRecord(checkId);
		
		//3.更新库存表库存inventory_sku
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
			//4.更新库存变更记录表inventory_sku_log
			inventory.put("inventory_sku_id", String.valueOf(i.getInventorySkuId()));
			inventory.put("admid", String.valueOf(admid));
			inventory.put("inventory_count", String.valueOf(inventory_count));
			inventory.put("log_remark", i.getCreateTime()+"库存盘点");
			inventory.put("before_remaining", String.valueOf(before_remaining));
			inventory.put("after_remaining", String.valueOf(after_remaining));
			inventory.put("change_type", "3");
			inventoryMapper.addInventoryLogByInventoryid(inventory);
			
			//5.更新库存明细inventory_details_sku
			inventory.put("type", "4");
			inventory.put("sku_details_remark", "库存盘点");
			inventoryMapper.addInventoryDetailsSku(inventory );
		}
		
		//6.清空inventory_sku_check_record_temp本次盘点数据
		inventoryMapper.deleteInventoryCheckRecord(checkId);
		
		//7.更新盘点完成标志 inventory_sku_check
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
	
	/**更新订单相关表状态
	 * @param map
	 */
	private void addLockInventory(Map<String,String> map) {
//			String odid =  map.get("odid");
//			订单产品要入库，
		//如果全部使用库存，订单状态改为验货无误
		/*if("true".equals(map.get("useAllInventory"))) {
			updateOrderState(map);
		}*/
		//采购使用库存锁定库存
		//使用库存
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
		
		//标记库位移动，告知仓库移位
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
	
	/**更新订单相关表状态
	 * @param map
	 */
	private void updateOrderState(Map<String,String> map){
		try {

			//订单产品要入库， 状态要验货无误
//			order_details.state=1 order_details.checked=1 
			orderinfoMapper.updateChecked(map);
			
			//查询是否是DP订单
			int isDropshipOrder=orderinfoMapper.queyIsDropshipOrder(map);
			//获取未更新之前，订单状态和客户ID，比较前后状态是否一致，不一致说明订单状态已经修改
			Map<String,Object> orderinfoMap = pruchaseMapper.queryUserIdAndStateByOrderNo(map.get("orderid"));
			
			orderinfoMapper.updateOrderDetails(map);
			SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
			
			int counts=orderinfoMapper.getDtailsState(map);
			
			if (isDropshipOrder == 1) {
//				orderinfoMapper.updateOrderDetails(map);
//				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
//				int counts=orderinfoMapper.getDtailsState(map);
				if(counts == 0){
					orderinfoMapper.updateDropshiporder(map);
					SendMQ.sendMsg(new RunSqlModel("update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"+map.get("orderid")+"' " +
							"and id='"+map.get("odid")+"')"));
				}
				//判断主单下所有的子单是否到库
				counts=orderinfoMapper.getAllChildOrderState(map);
				/*if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//判断订单状态是否一致
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//发送消息给客户
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}*/
			}else{
				// 非dropshi订单
//				orderinfoMapper.updateOrderDetails(map);
//				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
				//判断订单是否全部到库
//				int counts=orderinfoMapper.getDetailsState(map);
				/*if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//判断订单状态是否一致
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//发送消息给客户
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}*/
			}
			if(counts == 0){
				orderinfoMapper.updateOrderInfoState(map);
				SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
				//判断订单状态是否一致
				if(!orderinfoMap.get("old_state").toString().equals("2")){
					//发送消息给客户
					NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
							map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
				}
			}
			orderinfoMap.clear();
			//记录商品入库
			LOG.info("--------------------开始记录商品入库--------------------");
			map.put("old_itemqty",map.get("inventory_count_use"));
			map.put("remark", "有库存商品，采购自动匹配入库 inventory_sku_id:"+map.get("inventory_sku_id")+"/orderid:"+map.get("orderid")+"/od_id:"+map.get("od_id"));
			orderinfoMapper.insertGoodsInventory(map);
			LOG.info("--------------------结束记录商品入库--------------------");

			
			//insertOrderProductSource
			pruchaseMapper.insertOrderProductSource(map);
		} catch (Exception e) {
			LOG.error("新订单相关表状态",e);
		}
		
	}
	
	
	@Override
	public int cancelOrderToInventory(String orderNo,int admId,String admName) {
		// 释放该订单占用的库存
		int cancelUseInventory = cancelUseInventory(orderNo,admId,null);
		
		//对于没有使用库存订单的商品做库存增加到库存表中
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
		Map<String, String> inventory = null;
		for(Map<String,Object> c : checkedOrderDetails) {
			inventory = new HashMap<>();
			
			int lock_remaining = Integer.parseInt(StrUtils.object2NumStr(c.get("lock_remaining")));
			int yourder =  Integer.parseInt(StrUtils.object2NumStr(c.get("yourorder")));
			int seilUnit =  1;//Integer.parseInt(StrUtils.object2NumStr(c.get("seilUnit")));
			int inventoryCount = yourder * seilUnit - lock_remaining;
			inventory.put("inventory_count", String.valueOf(inventoryCount));
			inventory.put("tbskuid", StrUtils.object2Str(c.get("tbskuid")));
			inventory.put("tbspecid", StrUtils.object2Str(c.get("tbspecid")));
			inventory.put("shipno", StrUtils.object2Str(c.get("shipno")));
			inventory.put("barcode", StrUtils.object2Str(c.get("barcode")));
			inventory.put("car_type", StrUtils.object2Str(c.get("car_type")));
			inventory.put("skuid", StrUtils.object2Str(c.get("skuid")));
			inventory.put("specid", StrUtils.object2Str(c.get("specid")));
			inventory.put("odid", StrUtils.object2Str(c.get("od_id")));
			inventory.put("adminId", String.valueOf(admId));
			inventory.put("admName", admName);
			inventory.put("log_remark", StrUtils.object2Str(c.get("orderid"))+"/"+StrUtils.object2Str(c.get("od_id"))+"订单取消,已经验货的产品进入库存");
			inventory.put("unit", String.valueOf(seilUnit));
			inventory.put("storage_count",String.valueOf(yourder) );
			inventory.put("when_count",String.valueOf(yourder * seilUnit) );
			inventory.put("goodid", StrUtils.object2Str(c.get("goodsid"))); 
			inventory.put("storage_type", "3"); 
			inventory.put("orderid", StrUtils.object2Str(c.get("orderid"))); 
			int in_id = addInventory(inventory);
			if(lock_remaining == 0) {
				int odId = Integer.parseInt(StrUtils.object2NumStr(c.get("od_id")));
				Map<String, Object> addInventory = inventoryMapper.getAddInventory(odId);
				if(addInventory != null) {
					//插入库位移库数据
					InventoryBarcodeRecord record = new InventoryBarcodeRecord();
					record.setAdmid(admId);
					record.setInventoryBarcode(StrUtils.object2Str(addInventory.get("barcode")));
					
					int inid = Integer.parseInt(StrUtils.object2NumStr(addInventory.get("id")));
					record.setInventoryId(inid == 0 ? in_id : inid);
					record.setLockId(0);
					record.setState(1);
					record.setRemark(StrUtils.object2Str(c.get("orderid"))+"/"+StrUtils.object2Str(c.get("od_id"))+"订单取消商品");
					record.setOrderBarcode(StrUtils.object2Str(c.get("barcode")));
					record.setOdId(odId);
					record.setChangeNum(inventoryCount);
					inventoryMapper.insertInventoryBarcodeRecord(record );
				}
			}
		}
		return checkedOrderDetails.size();
	}
	
	/**取消订单采购使用过的库存
	 * @return
	 */
	private int cancelUseInventory(String orderNo,int admId,String odid) {
		//判断订单是否使用了库存
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
			     //仓库已经移库，需要操作还原库位, 库位变更记录表
				inventoryMapper.updateBarcodeRecord(Integer.parseInt(StrUtils.object2NumStr(i.get("barcodeId"))), 1);
			}
			
			//更新lock_inventory状态is_delete=1
			inventoryMapper.cancelLockInventory(Integer.parseInt(StrUtils.object2NumStr(i.get("li_id"))));
			
			int canRemaining = Integer.parseInt(StrUtils.object2NumStr(i.get("can_remaining")));
			int remaining = Integer.parseInt(StrUtils.object2NumStr(i.get("remaining")));
			int lockRemaining = Integer.parseInt(StrUtils.object2NumStr(i.get("lock_remaining")));
			int inventorySkuId = Integer.parseInt(StrUtils.object2NumStr(i.get("in_id")));
			
			//更新id_relationtable 若完全使用库存 则is_delete=1 不完全使用库存 goodstatus=5
//			ir.id as ir_id,ir.goodstatus as ir_goodstatus,ir.itemqty,
			int itemqty = Integer.parseInt(StrUtils.object2NumStr(i.get("itemqty")));
			int goodstatus = Integer.parseInt(StrUtils.object2NumStr(i.get("ir_goodstatus")));
			int ir_id = Integer.parseInt(StrUtils.object2NumStr(i.get("ir_id")));
			inventoryMapper.updateidRelationState(ir_id,itemqty == lockRemaining ? 1 : 0);
			
			//库存还原inventory_sku
			InventorySku isku = new InventorySku();
			isku.setId(inventorySkuId);
			isku.setRemaining(remaining + lockRemaining);
			isku.setCanRemaining(canRemaining + lockRemaining);
			inventoryMapper.updateInventory(isku);
			
			
			//库存变更日志 inventory_sku_log
			Map<String,String> ilog = new HashMap<>();
			ilog.put("inventory_count",StrUtils.object2NumStr(i.get("lock_remaining")));
			ilog.put("before_remaining",StrUtils.object2NumStr(i.get("remaining")));
			ilog.put("after_remaining",String.valueOf(remaining + lockRemaining));
			ilog.put("log_remark","订单"+StrUtils.object2Str(i.get("orderid"))+"/"+StrUtils.object2NumStr(i.get("od_id"))+"取消,使用过的库存还原");
			ilog.put("change_type","1");
			ilog.put("inventory_sku_id",StrUtils.object2NumStr(i.get("in_id")));
			ilog.put("od_id", StrUtils.object2NumStr(i.get("od_id")));
			inventoryMapper.addInventoryLogByInventoryid(ilog);
			
			//库存记录表 inventory_details_sku
			ilog.put("admid",String.valueOf(admId));
			ilog.put("type","0");
			ilog.put("sku_details_remark","订单"+StrUtils.object2Str(i.get("orderid"))+"/"+StrUtils.object2NumStr(i.get("od_id"))+"取消,使用过的库存还原");
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
		for(InventoryWrap i : inventoryBarcodeList) {
			if(i.getIbState() == 0) {
				i.setStateContext("采购使用库存,等待仓库移出库存");
			}else if(i.getIbState() == 1) {
				i.setStateContext("商品取消，等待仓库移入库存");
			}else if(i.getIbState() == 2) {
				i.setStateContext("已完成移出库存");
			}else if(i.getIbState() == 3) {
				i.setStateContext("已完成移入库存");
			}else if(i.getIbState() == 4) {
				i.setStateContext("仓库取消了出库请求");
			}else if(i.getIbState() == 5) {
				i.setStateContext("仓库取消了入库请求");
			}
		}
		return inventoryBarcodeList;
	}
	@Override
	public int inventoryBarcodeListCount(Map<String, Object> map) {
		return inventoryMapper.inventoryBarcodeListCount(map);
	}
	@Override
	public int updateBarcode(Map<String, Object> mapParam) {
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
		//移出库存
		if(Integer.parseInt(StrUtils.object2NumStr(mapParam.get("inorout"))) == 0) {
			int lockRemaining = wrap.getLockRemaining();
			//如果全部使用库存，订单状态改为验货无误
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
				map.put("orderNumRemarks", "使用库存"+lockRemaining);
				updateOrderState(map);
			}
			//更新库存inventory_sku 的remaining
			//库存数量要改变
			item.setRemaining(inventoryMap.getRemaining() - lockRemaining);
			item.setCanRemaining(inventoryMap.getCanRemaining());
			inventoryMapper.updateInventory(item);
			
			//库存明细 #{inventory_count},#{od_id} #{admid},#{type} #{inventory_sku_id}
			Map<String, String> inventory = new HashMap<>();
			inventory.put("inventory_count", String.valueOf(lockRemaining));
			inventory.put("od_id", String.valueOf(wrap.getOdid()));
			inventory.put("admid", StrUtils.object2NumStr(mapParam.get("admid")));
			//0 入库  1 出库 2 报损 4盘点  5-入库完成 6-出库完成 7-移库取消
			inventory.put("type", "6");
			inventory.put("inventory_sku_id",String.valueOf(wrap.getInid()) );
			inventory.put("sku_details_remark","仓库确认采购使用库存的请求，完成移库操作,库存数量减少");
			inventoryMapper.addInventoryDetailsSku(inventory );
			
			//库存日志#{inventory_count},#{before_remaining},#{after_remaining}, #{log_remark},#{change_type},#{inventory_sku_id}
			inventory.put("before_remaining", String.valueOf(inventoryMap.getRemaining()));
			inventory.put("after_remaining", String.valueOf(inventoryMap.getRemaining() - lockRemaining));
			inventory.put("change_type", "2");
			inventory.put("log_remark","仓库确认采购使用库存的请求，完成移库操作,库存数量减少");
			inventoryMapper.addInventoryLogByInventoryid(inventory);
		}
		return inventoryMapper.updateBarcode(mapParam);
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
		
		InventorySku isku = new InventorySku();
		isku.setId(wrap.getInid());
		//采购使用库存请求被仓库拒绝，库存锁定lock_inventory表 状态要解除，还原库存
		if(state==4) {
			//库存数量要改变
			isku.setRemaining(remaining);
			isku.setCanRemaining(canRemaining + lockRemaining);
			inventoryMapper.updateInventory(isku );
			
			//库存明细 #{inventory_count},#{od_id} #{admid},#{type} #{inventory_sku_id}
			Map<String, String> inventory = new HashMap<>();
			inventory.put("inventory_count", String.valueOf(lockRemaining));
			inventory.put("od_id", String.valueOf(wrap.getOdid()));
			inventory.put("admid", StrUtils.object2NumStr(mapParam.get("admid")));
			//0 入库  1 出库 2 报损 4盘点  5-入库完成 6-出库完成 7-移库取消
			inventory.put("type", "7");
			inventory.put("inventory_sku_id",String.valueOf(wrap.getInid()) );
			inventory.put("sku_details_remark","仓库拒绝采购使用库存的请求，取消库存占用,可用库存数量增加");
			inventoryMapper.addInventoryDetailsSku(inventory );
			
			//库存日志#{inventory_count},#{before_remaining},#{after_remaining}, #{log_remark},#{change_type},#{inventory_sku_id}
			inventory.put("before_remaining", String.valueOf(remaining));
			inventory.put("after_remaining", String.valueOf(remaining));
			//0：默认 1：增加  2：减少，3：盘点  4占用 5-取消占用
			inventory.put("change_type", "5");
			inventory.put("log_remark","仓库拒绝采购使用库存的请求,取消库存占用,可用库存数量增加");
			inventoryMapper.addInventoryLogByInventoryid(inventory);
			
			if(ibState == 0 ) {
				//取消库存使用
				inventoryMapper.cancelLockInventory(wrap.getLiid());
				//订单入库记录
				//完全使用库存 更改状态为确认价格中
				//更改状态为采购中，验货，数量不够
				if(wrap.getOdYourOrder() != lockRemaining) {
//					order_details.state=0 order_details.checked=0  id_relationtable.goodstatus=5   order_product_source.purchase_state  order_product_source.is_replenishment
					orderinfoMapper.cancelOrderState(wrap.getOdid());
				}
			}
		}
		return inventoryMapper.updateRemark(mapParam);
	}
	@Override
	public List<LossInventoryWrap> inventoryLossList(Map<String, Object> map) {
		List<LossInventoryWrap> inventoryLossList = inventoryMapper.inventoryLossList(map);
		for(LossInventoryWrap l : inventoryLossList) {
			String img = l.getImg() == null ? "" : l.getImg();
			l.setImg(img.replace(".60x60.jpg", ".400x400.jpg"));
			//0  损坏 1 遗失  3 添加 4 补货  5 漏发 7 其他原因
			String changeContext = "";
			switch (l.getChangeType()) {
			case 0:
				changeContext = "损坏";
				break;
			case 1:
				changeContext = "遗失";
				break;
			case 3:
				changeContext = "添加";
				break;
			case 4:
				changeContext = "补货";
				break;
			case 5:
				changeContext = "漏发";
				break;

			default:
				changeContext = "其他原因";
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
		//获取库存数据
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
}