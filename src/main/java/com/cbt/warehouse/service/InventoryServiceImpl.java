package com.cbt.warehouse.service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.common.StringUtils;
import com.cbt.pojo.Inventory;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.InventoryMapper;
import com.cbt.warehouse.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class InventoryServiceImpl implements  InventoryService{
	@Autowired
	private InventoryMapper inventoryMapper;


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
				// }
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
}