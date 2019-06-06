package com.cbt.export.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.export.pojo.ExportInfo;
import com.cbt.export.service.ExportService;
import com.cbt.util.StrUtils;
import com.cbt.warehouse.dao.WarehouseMapper;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.ibm.icu.math.BigDecimal;

@Service
public class ExportServiceImpl implements ExportService {
	@Autowired
	private WarehouseMapper warehouseMapper;
	private DecimalFormat format = new DecimalFormat("#0.00");
	private DecimalFormat wformat = new DecimalFormat("#0.000");

	@Override
	public List<ExportInfo> getExport(int start, int limit,String order_no) {
		List<ExportInfo> exports = new ArrayList<>();
		
		Set<String> oids = new HashSet<String>();
		
		//1.从出库表获得出库的订单 select * from shipping_package
		List<ShippingPackage> shippingPack = warehouseMapper.getShippingPack(start, limit,order_no);
		Map<String,ShippingPackage> shippingPackMap = new HashMap<>();
		for(ShippingPackage s : shippingPack) {
			String remarks = s.getRemarks();
			if(StringUtils.isNotBlank(remarks)) {
				String[] remarkss = remarks.split(",");
				for(String r : remarkss) {
					if(StringUtils.isNotBlank(r)) {
						oids.add(r);
						if(r.indexOf("_")>-1) {
							oids.add(r.split("_")[0]);
						}
						shippingPackMap.put(r, s);
					}
				}
			}else {
				oids.add(s.getOrderid());
				if(s.getOrderid().indexOf("_")>-1) {
					oids.add(s.getOrderid().split("_")[0]);
				}
				shippingPackMap.put(s.getOrderid(), s);
			}
		}
		List<String> orderids =  new ArrayList<>(oids);
		if(orderids == null || orderids.isEmpty()) {
			return exports;
		}
		
		//订单号-订单信息
		Map<String,Map<String,Object>> orderMap = new HashMap<>();
		//订单号 - 支付信息
		Map<String,Map<String,Object>> payMap = new HashMap<>();
		List<Map<String,Object>> orders = warehouseMapper.getOrder(orderids);
		for(Map<String,Object> o : orders) {
			orderMap.put(StrUtils.object2Str(o.get("order_no")), o);
		}
		List<Map<String,Object>> orderPay = warehouseMapper.getOrderPay(orderids);
		//3.从支付表获得支付信息
		for(Map<String,Object> o : orderPay) {
			String orderid = StrUtils.object2Str(o.get("orderid"));
			Map<String, Object> map = payMap.get(orderid);
			if(map != null) {
				if(o.get("paymentid") == null) {
					o.put("paymentid",map.get("paymentid"));
				}
				Object payment_amount = map.get("payment_amount");
				if(payment_amount != null) {
					BigDecimal amount = new BigDecimal(StrUtils.object2PriceStr(o.get("payment_amount"))).add(new BigDecimal(StrUtils.object2PriceStr(payment_amount)));
					o.put("payment_amount",format.format(amount));
				}
			}
			payMap.put(orderid, o);
		}
		
		ExportInfo export;
		//2.从订单表获得订单详情、出运地址
		List<Map<String,Object>> orderDetails = warehouseMapper.getOrderDetailsForExport(orderids);
		
		//订单-商品列表----同一个产品id不同sku合并
		Map<String,ExportInfo> orderDetailMap = new HashMap<>();
		
		//订单商品总价值
		Map<String,BigDecimal> orderCost = new HashMap<>();
		
		for(Map<String,Object> o : orderDetails) {
			String orderid = StrUtils.object2Str(o.get("orderid"));
			String pid = StrUtils.object2Str(o.get("goods_pid"));
			
			export = orderDetailMap.get(orderid+"-"+pid);
			
			String goodsprice = StrUtils.object2PriceStr(o.get("goodsprice"));
			String yourorder = StrUtils.object2Str(o.get("yourorder"));
			yourorder = StrUtils.isNum(yourorder) ? yourorder : "1";
			String strWeight = StrUtils.object2PriceStr(o.get("weight"));
			strWeight = StringUtils.isBlank(strWeight) ? StrUtils.object2PriceStr(o.get("actual_weight")) : strWeight;
			
			BigDecimal cost = new BigDecimal(goodsprice).multiply(new BigDecimal(yourorder));
			BigDecimal orderPrice = orderCost.get(orderid);
			orderPrice = orderPrice == null ? cost : orderPrice.add(cost);
			orderCost.put(orderid, orderPrice);
			
			BigDecimal weight = new BigDecimal(strWeight).multiply(new BigDecimal(yourorder));
			
			if(export == null) {
				export = new ExportInfo();
				export.setGoodsPrice(goodsprice);
				export.setQuality(yourorder);
				export.setPid(pid);
				export.setGoodsWeight(strWeight);
				export.setGoodsName(StrUtils.object2Str(o.get("name")));
				export.setNumber(orderid);
				export.setGoodsCost(format.format(cost.doubleValue()));
			}else {
				String exportQuality = export.getQuality();
				
				String quality = String.valueOf(Integer.valueOf(exportQuality) + Integer.valueOf(yourorder));
				export.setQuality(String.valueOf(quality));
				
				cost = cost.add(new BigDecimal(export.getGoodsCost()));
				export.setGoodsCost(format.format(cost.doubleValue()));
				export.setGoodsPrice(format.format(cost.divide(new BigDecimal(quality))));
				
				BigDecimal goodsTotalWeght = new BigDecimal(export.getGoodsWeight()).multiply(new BigDecimal(exportQuality));
				goodsTotalWeght = goodsTotalWeght.add(weight);
				goodsTotalWeght = goodsTotalWeght.divide(new BigDecimal(quality));
				export.setGoodsWeight(wformat.format(goodsTotalWeght));
			}
			
			orderDetailMap.put(orderid+"-"+pid, export);
			
		}
		
		Iterator<Entry<String, ExportInfo>> iterator = orderDetailMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, ExportInfo> next = iterator.next();
			String orderidPid = next.getKey();
			export = next.getValue();
			String orderid = orderidPid.split("-")[0];
			
			export.setNumber(orderid);
			export.setCompanyCode("SHCR");
			export.setPostAddress("上海市普陀区云岭东路609号1号楼605室");
			export.setPostContry("142");
			export.setPostName("杨毅明");
			export.setPostNumber("13817133805");
			export.setPremium("0");
			export.setEnterpriseCode("3114967705");
			export.setEnterpriseName("上海策融网络科技有限公司");
			export.setCurrency("502");
			export.setHsNumber("");
			export.setCustomsRecordNumber("");
			
			ShippingPackage shippingPackage = shippingPackMap.get(orderid);
			if(shippingPackage != null) {
				export.setLogisticscode(shippingPackage.getTransportcompany());
				export.setShipmentNumber(shippingPackage.getExpressno());
				export.setSubShipmentNumber("");
				export.setPackageNumber("1");
				export.setPackageCode("");
				export.setId(Integer.valueOf(shippingPackage.getId()));
				export.setNetWeight(shippingPackage.getSweight());
				export.setWeight(shippingPackage.getSweight());
			}
			
			Map<String, Object> order = orderMap.get(orderid);
			if(order != null) {
				export.setReceivingAddress(StrUtils.object2Str(order.get("address")));
				export.setReceivingContry("502"/*order.get("Country")*/);
				export.setReceivingName(StrUtils.object2Str(order.get("recipients")));
				export.setReceivingNumber(StrUtils.object2Str(order.get("phoneNumber")));
			}
			
			Map<String, Object> pay = payMap.get(orderid);
			pay = pay == null && orderid.indexOf("_") > -1? payMap.get(orderid.split("_")[0]) : pay;
			
			if(pay != null) {
				export.setReceivingTime(StrUtils.object2Str(pay.get("createtime")));
				export.setTransactionNumber(StrUtils.object2Str(pay.get("paymentid")));
				export.setTotalAmount(StrUtils.object2PriceStr(pay.get("payment_amount")));
			}
			BigDecimal orderCostTotal = orderCost.get(orderid);
			
			export.setCost(orderCostTotal == null ? "0" : format.format(orderCostTotal));
			
			String totalAmount = export.getTotalAmount();
			
			BigDecimal otherCost = new BigDecimal(StringUtils.isBlank(totalAmount) ? "0" : totalAmount).subtract(orderCostTotal);
			export.setOtherCost(format.format(otherCost));
			exports.add(export);
		}
		
		exports = exports.stream().sorted((e1,e2) -> {
			return Integer.compare(e2.getId(), e1.getId());	
		}).collect(Collectors.toList());;
		return exports;
	}

	@Override
	public int getExportCount() {
		// TODO Auto-generated method stub
		return warehouseMapper.getShippingPackCount();
	}

	
	
	
}
