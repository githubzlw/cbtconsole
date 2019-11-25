package com.cbt.winit.api.component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.parse.service.StrUtils;
import com.cbt.winit.api.model.RequestMsg;
import com.cbt.winit.api.model.WarehouseWrap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.pojo.GoodsSkuAttr;
import com.importExpress.pojo.OverseasWarehouseStock;
import com.importExpress.service.GoodsSkuAttrService;
import com.importExpress.service.OverseasWarehouseStockService;

@Component
@Qualifier("queryInventory")
public class QueryInventory extends QueryBase{
	@Autowired
	private OverseasWarehouseStockService owsService;
	@Autowired
	private GoodsSkuAttrService goodsSkuAttrService;
	
	private WarehouseWrap  warehouse;
	private int pageNum = 1;
	
	private int pageTotal = 1;
	private int stock;
	
	public void toDo() {
		doAction();
	}
	
	@Override
	protected void setdBusinessData(RequestMsg requestMsg) {
		Map<String, Object> data = Maps.newTreeMap();
		//产品类型ID
		data.put("categoryID", "");
		//DOI层级： 1：30以下 2：30-60 3：60-90 4：90以上
		data.put("DOITier", "");
		//库存类型：Country：国家，Warehouse：仓库
		data.put("inventoryType", "Warehouse");
		//商品是否有效,Y/N
		data.put("isActive", "Y");
		//页码
		data.put("pageNum", String.valueOf(this.pageNum));
		//每页显示数量
		data.put("pageSize", "100");
		//产品SKU编码
		data.put("productCode", "");
		//产品名称
		data.put("name", "");
		//规格
		data.put("specification", "");
		//	仓库ID（warehouseID、warehouseCode必填其中一个）
		data.put("warehouseId", this.warehouse.getId());
		//仓库Code（warehouseID、warehouseCode必填其中一个）
		data.put("warehouseCode", this.warehouse.getCode());
		requestMsg.setData(data);
		
	}

	@Override
	protected void setRequestAction(RequestMsg requestMsg) {
		requestMsg.setAction("queryProductInventoryList4Page");
		
	}

	@Override
	protected void parseRequestResult(String result) {
//		System.out.println(result);
		JSONObject resultObject = JSONObject.parseObject(result);
		JSONObject dataObject = (JSONObject)resultObject.get("data");
		JSONObject pageObject = (JSONObject)dataObject.get("page");
		
		//总数量
		int totalRows = pageObject.getInteger("TotalRows");
		//每页返回数量
		int numRows = pageObject.getInteger("NumRows");
		//起始
//		int startRow = pageObject.getInt("StartRow");
		this.pageTotal = totalRows % numRows == 0 ? totalRows / numRows :  totalRows / numRows +1;
		
		JSONArray latArray= (JSONArray)dataObject.get("list");
		if(latArray == null) {
			return ;
		}
		int totalStock = 0;
		for(int i=0,size=latArray.size();i<size;i++) {
			JSONObject lstObject = (JSONObject)latArray.get(i);
			//商品编码
			String productCode = lstObject.getString("productCode");
			if(!StrUtils.isMatch(productCode, "(\\d+(\\-\\d+)*)")) {
				continue;
			}
//			System.out.println(productCode);
			//仓库名称
//			String warehouseName = lstObject.getString("warehouseName");
			//仓库code
//			String warehouseCode = lstObject.getString("warehouseCode");
			//仓库ID
//			String warehouseId = lstObject.getString("warehouseId");
			//DOI
//			String doi = lstObject.getString("DOI");
			//全部的DOI
//			String doiAll = lstObject.getString("DOIAll");
			//近7天平均库存
//			String averageStockQty7 = lstObject.getString("averageStockQty7");
			//近15天平均库存
//			String averageStockQty15 = lstObject.getString("averageStockQty15");
			//近30天平均库存
//			String averageStockQty = lstObject.getString("averageStockQty");
			//近7天平均销量
//			String averageSalesQty7 = lstObject.getString("averageSalesQty7");
			//近15天平均销量
//			String averageSalesQty15 = lstObject.getString("averageSalesQty15");
			//近30天平均销量
//			String averageSalesQty = lstObject.getString("averageSalesQty");
			//历史入库
//			String qtyHisIn = lstObject.getString("qtyHisIn");
			//历史出库
//			String qtyHisOut = lstObject.getString("qtyHisOut");
			//可用库存
			String qtyAvailable = lstObject.getString("qtyAvailable");
			totalStock += Integer.parseInt(qtyAvailable);
			//待出库
//			String qtyReserved = lstObject.getString("qtyReserved");
			//在途库存
//			String qtyOrdered = lstObject.getString("qtyOrdered");
			//存储仓库存
//			String qtySw = lstObject.getString("qtySw");
			//共享库存,共享给分销平台使用的库存，比如卖家A商品有100个，设置了专属库存是80%，那么就是20%的库存是共享库存，也就是20个
//			int qtyShareStorage = lstObject.getInt("qtyShareStorage");
			//平均销量
//			int avgSales = lstObject.getInt("avgSales");
			//历史销量
//			int qtySellHisOut = lstObject.getInt("qtySellHisOut");
			//禁止库存数量
//			int prohibitUsableQty = lstObject.getInt("prohibitUsableQty");
			//是否退货库存
//			String isReturnInventory = lstObject.getString("isReturnInventory");
			//是否禁止出库
//			String isprohibitoutbound = lstObject.getString("isprohibitoutbound");
			
			//商品ID
			String productId = lstObject.getString("productId");
			//商品英文名字
			String eName = lstObject.getString("eName");
			//商品中文名字
//			String name = lstObject.getString("name");
			//规格
			String specification = lstObject.getString("specification");
			//商品描述
//			String description = lstObject.getString("description");
			String[] productCodes = productCode.split("-");
			productId = productCodes[0];
			String skuid = "";
			String specid = "";
			String coden = "";
			
			if(productCodes.length > 1) {
				GoodsSkuAttr parseGoodsSku = goodsSkuAttrService.parseGoodsSku(productCode);
				if(parseGoodsSku.getErrorCode() > 102) {
					skuid = parseGoodsSku.getSkuid();
					specid = parseGoodsSku.getSpecid();
				}
				List<Long> attrList = Lists.newArrayList();
				for(int j=1;j<productCodes.length;j++) {
					attrList.add(Long.parseLong(productCodes[j]));
				}
				coden = productCodes[0];
				
				attrList.stream().sorted().collect(Collectors.toList());
				for(Long a : attrList) {
					coden +="-"+a;
				}
				
			}else {
				skuid = productCode;
				specid = productCode;
				coden = productCode;
			}
			
			OverseasWarehouseStock stock = OverseasWarehouseStock.builder()
												.code(productCode)
												.coden(coden)
												.goodsName(eName.replace("'", "\\'"))
												.goodsPid(productId)
												.owStock(Integer.parseInt(qtyAvailable))
												.sku(specification)
												.skuid(skuid)
												.specid(specid)
												.build();
//			System.out.println(stock.toString());
			int syncStock = owsService.syncStock(stock );
//			System.out.println(productCode+"^^^^^^"+syncStock);
		}
		
		setStock(totalStock);
	}

	public void setWarehouse(WarehouseWrap warehouse) {
		this.warehouse = warehouse;
	}

	public int getPageTotal() {
		return pageTotal;
	}
	
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum > 1 ? pageNum : 1;
	}

	public int getStock() {
		return stock;
	}

	private void setStock(int stock) {
		this.stock = stock;
	}
	
}
