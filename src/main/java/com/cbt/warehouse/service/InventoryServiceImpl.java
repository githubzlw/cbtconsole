package com.cbt.warehouse.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.common.StringUtils;
import com.cbt.orderinfo.dao.OrderinfoMapper;
import com.cbt.pojo.Inventory;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.InventoryMapper;
import com.cbt.warehouse.util.StringUtil;
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
	public int recordLossInventory(Map<Object, Object> map) {

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
	public List<Inventory> getIinOutInventory(Map<Object, Object> map) {
		List<Inventory> toryList=inventoryMapper.getIinOutInventory(map);
		StringBuilder pids=new StringBuilder();
		for (Inventory inventory : toryList) {
			inventory.setGoods_url(StringUtil.isBlank(inventory.getGoods_url())?"":inventory.getGoods_url());
			inventory.setCar_urlMD5(StringUtil.isBlank(inventory.getCar_urlMD5())?"":inventory.getCar_urlMD5());
			pids.append("'").append(inventory.getGoods_pid()).append("',");
			String valid=inventory.getOnLine();
			String onLine="状态错误";
			String unsellableReason="--";
			if("1".equals(valid)){
				onLine="上架";
			}else if("0".equals(valid)){
				onLine="下架";
				unsellableReason= Utility.getUnsellableReason(inventory.getUnsellableReason(),unsellableReason);
				inventory.setUnsellableReason(unsellableReason);
			}else if("2".equals(valid) && (inventory.getGoods_url().indexOf("aliexpress")>-1 || inventory.getCar_urlMD5().startsWith("A"))){
				onLine="ali产品实时抓取";
			}else{
				onLine="商品已删除";
			}
			inventory.setOnLine(onLine);
			if("0".equals(map.get("export"))){
				if (inventory.getFlag() == 1) {
					if(map.get("flag")==null || "".equals(map.get("flag"))){
						inventory.setOperation(("<a style='color:red' id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getNew_remaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark()) + "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>").replaceAll("\n", ""));
					}else{
						inventory.setOperation(("<a id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getNew_remaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark()) + "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>").replaceAll("\n", ""));
					}
				} else{
					inventory.setNew_remaining("");
					if(inventory.getFlag()==0){
						inventory.setOperation(("<a id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getRemaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark())+ "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>|<a onclick=\"problem_inventory("+inventory.getId()+")\">问题库存</a>").replaceAll("\n", ""));
					}else{
						inventory.setOperation(("<a id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getRemaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark())+ "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>").replaceAll("\n", ""));
					}
				}
				String url="";
				if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && inventory.getCar_urlMD5().startsWith("A")){
					url="https://www.import-express.com/goodsinfo/a-2"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && inventory.getCar_urlMD5().startsWith("D")){
					url="https://www.import-express.com/goodsinfo/a-1"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && inventory.getCar_urlMD5().startsWith("N")){
					url="https://www.import-express.com/goodsinfo/a-3"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getGoods_url()) && inventory.getGoods_url().contains("ali")){
					url="https://www.import-express.com/goodsinfo/a-2"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && !StringUtils.isStrNull(inventory.getGoods_pid())){
					url="https://www.import-express.com/spider/detail?&source="+inventory.getCar_urlMD5()+"&item="+inventory.getGoods_pid()+"";
				}else {
					url="https://www.import-express.com/goodsinfo/a-1"+inventory.getGoods_pid()+".html";
				}
				if("1".equals(inventory.getDb_flag())){
					inventory.setEditLink("<a target='_blank' href='/cbtconsole/editc/detalisEdit?pid="+inventory.getPid()+"'>产品编辑链接</a>");
				}else{
					inventory.setEditLink("--");
				}
				if("4".equals(inventory.getOnline_flag())){
					String car_img=inventory.getCar_img();
					String imgs[]=car_img.split("kf");
					String one=imgs[0];
					String two=imgs[1].replace(".jpg_50x50","");
					url="https://s.1688.com/youyuan/index.htm?tab=imageSearch&from=plugin&imageType="+one+"&imageAddress=kf"+two+"";
				}else if("1".equals(inventory.getOnline_flag())){
					url="https://www.aliexpress.com/item/a/"+inventory.getGoods_pid()+".html";
				}
				inventory.setCar_img("<a href='"+url+"' title='跳转到网站链接' target='_blank'>"
						+ "<img  src='"+ (inventory.getCar_img().indexOf("1.png")>-1?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":inventory.getCar_img()) + "' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"+ inventory.getCar_img() + "')\" height='100' width='100'></a>");
				if(!StringUtils.isStrNull(inventory.getGoods_p_url())){
					url=inventory.getGoods_p_url();
					inventory.setGood_name("<a href='"+url+"' target='_blank' title='"+(StringUtils.isStrNull(url)?"未匹配到1688链接":"跳转到1688链接")+"'>"
							+ inventory.getGood_name().substring(0, inventory.getGood_name().length() / 3) + "</a>");
				}else{
					inventory.setGood_name("<span'>"+ inventory.getGood_name().substring(0, inventory.getGood_name().length() / 3) + "</span>");

				}
				inventory.setSku("<a title='查看出入库明细' href='/cbtconsole/website/in_out_details.jsp?in_id="+inventory.getId()+"' target='_blank'>"+(StringUtils.isStrNull(inventory.getSku())?"无规格":inventory.getSku())+"</a>");
				if(!StringUtils.isStrNull(inventory.getGoodsid())){
					inventory.setRemaining("<a title='跳转到入库记录页面' style='color:red;text-decoration-line:underline' target='_blank' href='/cbtconsole/warehouse/getOrderinfoPage.do?goodid="+inventory.getGoodsid()+"'>"+inventory.getRemaining()+"</a>");
				}else{
					inventory.setRemaining("<span>"+inventory.getRemaining()+"</span>");
				}
			}
			String goodscatid=inventory.getGoodscatid();
			if(StringUtil.isBlank(goodscatid) || "0".equals(goodscatid)){
				inventory.setGoodscatid("其他");
			}
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
	public List<Inventory> getIinOutInventoryCount(Map<Object, Object> map) {
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
				inventory.put("car_img", t.getImgurl());
				inventory.put("goods_p_price", String.valueOf(t.getItemprice()));
				inventory.put("goodsprice", String.valueOf(t.getItemprice()));
				inventory.put("goods_p_url", t.getImgurl());
				inventory.put("good_name", t.getItemname());
				inventory.put("tb_1688_itemid", t.getItemid());
				inventory.put("tborderid", t.getOrderid());
				inventory.put("tb_1688_itemid", t.getItemid());
				inventory.put("tb_1688_itemid", t.getItemid());
				result = result + inventoryOperation(inventory,true);
			}
		}else {
			result = inventoryOperation(inventory,false);
		}
		return result;
	}
	
	private int inventoryOperation(Map<String,String> inventory,boolean isTbOrder) {
		int inventory_count = Integer.valueOf(inventory.get("inventory_count"));
		if(inventory_count == 0) {
			return 0;
		}
		//inventory_type 0：电商库存  1：亚马逊库存
		inventory.put("inventory_type", "0");
		//获取订单详情中产品数据：产品id、产品名称、产品价格、产品规格等
		Map<String, String> orderDetails = isTbOrder ? null : inventoryMapper.getOrderDetails(inventory);
		if(orderDetails != null) {
			inventory.putAll(orderDetails);
			
			//产品sku：若所选产品规格没有值，使用产品id代替规格id
			String car_type = inventory.get("car_type");
			if(StringUtils.isStrNull(car_type) || "0".equals(car_type)) {
				String skuid = inventory.get("skuid");
				String specid = inventory.get("specid");
				skuid = StringUtils.isStrNull(skuid) ? orderDetails.get("goods_pid") : skuid;
				specid = StringUtils.isStrNull(specid) ? orderDetails.get("goods_pid") : specid;
				inventory.put("skuid", skuid);
				inventory.put("specid", specid);
			}
		}
		
		//2.是否存在库存
		Map<String,Object> isExsisInventory = inventoryMapper.getInventory(inventory);
		int beforeRemaining = 0;
		int afterRemaining = inventory_count;
		if(isExsisInventory == null) {
			if(isTbOrder) {
				String oldbarcode = (String)isExsisInventory.get("barcode");
				if(StringUtil.isNotBlank(oldbarcode)) {
					inventory.put("barcode", oldbarcode);
				}
			}
			inventoryMapper.addInventory(inventory);
		}else {
			if(isTbOrder) {
				inventory.put("barcode", (String)isExsisInventory.get("barcode"));
			}
			String goods_p_url = (String)isExsisInventory.get("goods_p_url");
			String goods_p_price = (String)isExsisInventory.get("goods_p_price");
			int remaining = (int)isExsisInventory.get("remaining");
			int can_remaining = (int)isExsisInventory.get("can_remaining");
			
			//更改前库存
			beforeRemaining = remaining;
			//更改后库存
			afterRemaining = beforeRemaining + inventory_count;
					
			inventory.put("goods_p_url", goods_p_url);
			
			inventory.put("goods_p_price", goods_p_price);
			
			//可用库存
			can_remaining = inventory_count + can_remaining;
			inventory.put("can_remaining", String.valueOf(can_remaining));
			
			//更新后库存增加
			inventory.put("remaining", String.valueOf(afterRemaining));
			inventoryMapper.updateInventory(inventory);
		}
		
		//3.库存表id
		Integer inventory_sku_id = inventoryMapper.isExsisInventory(inventory);
		inventory.put("inventory_sku_id", String.valueOf(inventory_sku_id));
		inventory.put("before_remaining", String.valueOf(beforeRemaining));
		inventory.put("after_remaining", String.valueOf(afterRemaining));
		
		//4.插入库存变更记录 change_type 1:入库 2：出库
		inventory.put("change_type", "1");
		inventoryMapper.addInventoryChangeRecord(inventory);
		
		//6.记录库存入库明细操作
		//入库 0 入库  1 出库
		inventory.put("type", "0");
		return inventoryMapper.insertInventoryDetailsSku(inventory);
		
	}
}