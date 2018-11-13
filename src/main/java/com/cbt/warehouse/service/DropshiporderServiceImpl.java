package com.cbt.warehouse.service;

import com.cbt.bean.CtpoOrderBean;
import com.cbt.bean.OrderBean;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.warehouse.dao.DropshiporderMapper;
import com.cbt.warehouse.dao.OrderMapper;
import com.cbt.warehouse.pojo.Dropshiporder;
import com.cbt.warehouse.pojo.DropshiporderExample;
import com.cbt.warehouse.pojo.DropshiporderExample.Criteria;
import com.cbt.warehouse.util.Utility;
import com.cbt.website.bean.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**   
 * @Title : DropshiporderServiceImpl.java 
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : cjc
 * @date : 2017年4月12日
 * @version : V1.0   
 */
@Service
public class DropshiporderServiceImpl implements DropshiporderService {
	private static final Log LOG = LogFactory.getLog(DropshiporderServiceImpl.class);

	@Autowired
	private DropshiporderMapper dropshiporderMapper;
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public int addDropshiporder(Dropshiporder dropshiporder) {
		return dropshiporderMapper.insertSelective(dropshiporder);
	}

	@Override
	public int updateDropshiporder(String orderno) {
		return dropshiporderMapper.updateDropshiporder(orderno);
	}

//	@Override
//	public List<Dropshiporder> getDropShipOrderList(String ParentOrderNo, int userId) {
//		DropshiporderExample example = new DropshiporderExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUserIdEqualTo(userId);
//		criteria.andParentOrderNoEqualTo(ParentOrderNo);
//		return dropshiporderMapper.selectByExample(example);
//	}
	
	@Override
	public DataGridResult getDropShipOrderList(String ParentOrderNo, Integer userId,Integer page) {
		DropshiporderExample example = new DropshiporderExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andParentOrderNoEqualTo(ParentOrderNo);
		criteria.andStateNotEqualTo("6");
		if (page == null || page == 0) {
			page = 1;
		}
		PageHelper.startPage(page, 10, "orderid desc");
//		List<Dropshiporder> list = dropshiporderMapper.selectByExample(example);
		List<Dropshiporder> list = dropshiporderMapper.selectByUserIdAndParentOrderNo(userId,ParentOrderNo);
		DataGridResult result = new DataGridResult();
		result.setResult(true);
		result.setMessage(ParentOrderNo);
		result.setRows(list);
		PageInfo<Dropshiporder> pageInfo = new PageInfo<Dropshiporder>(list);
		result.setTotalCount(pageInfo.getTotal());
		result.setCurrePage(page);
		result.setPageSize(10);
		result.setTotalPage(((int)pageInfo.getTotal() + 10 - 1) / 10);
		return result;
	}
	
	
	
	public List<OrderBean> change2OrderBean(List<Map<String, Object>> ordersMap) {
		List<OrderBean> oblist = new ArrayList<OrderBean>();
		for (Map<String, Object> map : ordersMap) {
			OrderBean temp = new OrderBean();
			temp.setOrderNo((String)map.get("order_no"));
			temp.setForeign_freight((String)map.get("foreign_freight"));
			String product_cost_ = (String)map.get("product_cost");
			if (StringUtils.isNotBlank(product_cost_)) {
				double fp = Double.parseDouble(product_cost_);
				temp.setProduct_cost(new BigDecimal(fp).setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
			} else {
				temp.setProduct_cost(null);
			}
			temp.setState(Integer.parseInt((String)map.get("state")));
			String createTime = String.valueOf(map.get("create_time")); 
			temp.setCreatetime(createTime == null ? null : createTime.substring(0, 16));
			temp.setRemaining_price(map.get("remaining_price") == null ? 0.0 : (Double)map.get("remaining_price"));
			String pay_price=(String)map.get("pay_price");
			if(pay_price != null && !"".equals(pay_price)){
				temp.setPay_price(new BigDecimal(pay_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
			}	
			temp.setMode_transport((String)map.get("mode_transport"));
			temp.setServer_update((Integer)map.get("server_update"));
			temp.setCurrency((String)map.get("currency"));
			temp.setActual_ffreight((String)map.get("actual_ffreight"));
			temp.setPaystatus((String)map.get("paystatus"));
			temp.setExpressNo((String)map.get("express_no"));
			temp.setDeliveryTime(map.get("delivery_time") == null ? 0 : Integer.valueOf((String)map.get("delivery_time")));
			String service_fee = (String) (map.get("service_fee") == null ? 0.0 :map.get("service_fee").toString());
			temp.setService_fee(service_fee);
			temp.setChaOrderNo(String.valueOf(map.get("chaOrderNo")));
			//Subsitution Confirmed标识
			temp.setDomestic_freight((String)map.get("domestic_freight"));
			//免邮费免邮判断
			String modeTransport = (String)map.get("mode_transport");
			if(modeTransport != null && modeTransport.indexOf("@0@all") != -1) {
				temp.setFree_shipping(1);
			}else{
				temp.setFree_shipping(0);
			}
			//未支付订单先放到已支付订单里，1分钟内如果没支付在 放回未支付订单
			temp.setPayFlag(Integer.parseInt(map.get("payflag").toString()));
			//未支付订单4小時内不可以取消
			temp.setCancelFlag(Integer.parseInt(map.get("cancelFlag").toString()));
			//已支付订单8小時内消息切换标识
			temp.setComformFlag(Integer.parseInt(map.get("comformFlag").toString()));
			
			//PayPal 审核中
			if(temp.getState()==0 && "0".equals(temp.getPaystatus())){
				temp.setPayFlag(1);
			}
			
			oblist.add(temp);
		}
		return oblist;
	}
	
	
	/**
	 * 参见OrderServiceImpl类的getCtpoOrderInfo方法
	 */
	@Override
	public Map<String, Object> getDropShipOrderInfo(String orderNo, int userId) {
		Map<String, Object> ctpoOrderInfo=new HashMap<String,Object>();
		List<CtpoOrderBean> ctpoList=new ArrayList<CtpoOrderBean>();
		List<Map<String, Object>> listMap = dropshiporderMapper.getDropShipOrderInfo(orderNo,userId);
		float productCost=0;//需要支付的钱
		String jiaohuoriqi="";
		String foreign_freight = "0";
		double discount_amount = 0;
		String currency = "USD";
		double order_ac = 0;
		String service_fee = "0";
		double cashback = 0,extra_freight=0;
		for (Map<String, Object> map : listMap) {
			discount_amount = (Double)map.get("discount_amount");
			order_ac = (Double)map.get("order_ac");
			int goodId = (Integer)map.get("goodsid");
			CtpoOrderBean ctpoOrderBean=new CtpoOrderBean();
			ctpoOrderBean.setState(String.valueOf(map.get("state")));
			ctpoOrderBean.setId((Integer)map.get("orderid"));
			ctpoOrderBean.setOrderNo(String.valueOf(map.get("child_order_no")));
			ctpoOrderBean.setGoodId(goodId);
			ctpoOrderBean.setGoodsName((String)map.get("goodsname"));
			ctpoOrderBean.setGoodsUrl((String)map.get("car_url"));
			ctpoOrderBean.setGoodsImg((String)map.get("car_img"));
			ctpoOrderBean.setGoodsRemark((String)map.get("remark"));
			ctpoOrderBean.setPurchase_state((Integer)map.get("purchase_state"));
			currency = (String)map.get("currency");
			ctpoOrderBean.setCurrency(currency);
			ctpoOrderBean.setImg_type((String)map.get("goods_typeimg"));
			ctpoOrderBean.setDiscount_amount((Double)map.get("discount_amount"));
			ctpoOrderBean.setTypes((String)map.get("car_type"));
			String freight = (String)map.get("goodsfreight");
			float goodsfreight=0;
			if(Utility.getStringIsNull(freight)){
				goodsfreight=Float.parseFloat(freight);
			}
			String ff = (String)map.get("foreign_freight");
			if(Utility.getStringIsNull(ff)){
				foreign_freight = ff;
			}
			cashback = (Double)map.get("cashback");
			extra_freight = (Double)map.get("extra_freight");
			String service_f = (String)map.get("service_fee");
			if(Utility.getStringIsNull(service_f)){
				service_fee = service_f;
			}
			ctpoOrderBean.setGoodsfreight(freight);
			float goodsPrice=0;
			String goodsPriceStr=(String)map.get("goodsprice");
			if(Utility.getStringIsNull(goodsPriceStr)){
				goodsPrice=Float.parseFloat(goodsPriceStr);
			}
			ctpoOrderBean.setGoodsPrice(goodsPriceStr);
			/*String productCostStr = (String)map.get("oi.product_cost");
			if(Utility.getStringIsNull(productCostStr)){
				totalProductCost=Float.parseFloat(productCostStr);
			}*/
			ctpoOrderBean.setDeliveryTime((String)map.get("delivery_time"));
			int yourorder = (Integer)map.get("yourorder");
			ctpoOrderBean.setYourOrder(yourorder);
			List<String> type1List=new ArrayList<String>();
			List<String> type2List=new ArrayList<String>();
			List<String> type3List=new ArrayList<String>();
			List<String> type5List=new ArrayList<String>();
			List<String> type7List=new ArrayList<String>();
			boolean isCancel=false;
//			float danj=goodsPrice;
			List<Map<String, Object>> changes = orderMapper.getOrderChanges(orderNo, goodId);
			for (Map<String, Object> change : changes) {
				int ropType = (Integer)change.get("ropType");
				//begin jxw 2017-3-24 添加问题类型为4的标识
				if(ropType == 4){
					ctpoOrderBean.setRopType(ropType);
				}
				//end
				String oldValue=(String)change.get("oldValue");
				String newValue=(String)change.get("newValue");
				switch (ropType) {
				case 1://价格变动
					type1List.add(oldValue);
					type1List.add(newValue);
					/*if(Utility.getStringIsNull(newValue)){
						Float danj=Float.parseFloat(newValue);
						goodsPrice=danj;
//						ctpoOrderBean.setGoodsPrice(danj+"");
					}*/
					ctpoOrderBean.setChange1(type1List);
					break;
				case 2://交期变动
//					if(!Utility.getStringIsNull(oldValue)){
//						oldValue=Utility.getDateFormatYMD().format(new Date());
//					}else if(oldValue.trim().equals("0")){
//						oldValue=Utility.getDateFormatYMD().format(new Date());
//					}
					type2List.add(oldValue);
					type2List.add(newValue);
					if (Utility.getStringIsNull(newValue)) {
						jiaohuoriqi = newValue;
					}
					ctpoOrderBean.setChange2(type2List);
					break;
				case 3://最小定量
					type3List.add(oldValue);
					type3List.add(newValue);
					yourorder=Integer.parseInt(newValue);
					ctpoOrderBean.setChange3(type3List);
					break;
				case 4://取消商品
					ctpoOrderBean.setChange4(newValue);
					isCancel=true;
					break;
				case 5://交流信息
					SimpleDateFormat format = new SimpleDateFormat("hhaa,MM-dd ",Locale.ENGLISH);  
					String dateline = format.format((Date)change.get("dateline"));
					if(oldValue.equals("1")){
						type5List.add(dateline+"  Supplier:"+newValue);
					}else{
						type5List.add(dateline+"  Customer:"+newValue);
					}
					break;
				case 7://国内运费
					type7List.add(oldValue);
					type7List.add(newValue);
					ctpoOrderBean.setChange7(type7List);
					break;
				default:
					break;
				}
			}
			ctpoOrderBean.setChange5(type5List);
			if(!isCancel){
//				ctpoOrderBean.setYourOrder(yourorder);
//				ctpoOrderBean.setGoodsPrice(goodsPrice+"");
				productCost=productCost+(goodsPrice*yourorder+goodsfreight);
			}
			ctpoOrderBean.setCancel(isCancel);
			ctpoList.add(ctpoOrderBean);
		}
		ctpoOrderInfo.put("currency", currency);
		ctpoOrderInfo.put("ctpoList", ctpoList);
		ctpoOrderInfo.put("productCost", productCost);
		ctpoOrderInfo.put("discount_amount", discount_amount);
		ctpoOrderInfo.put("order_ac", order_ac);
		ctpoOrderInfo.put("foreign_freight", foreign_freight);
		ctpoOrderInfo.put("service_fee", service_fee);
		ctpoOrderInfo.put("cashback", cashback);
		ctpoOrderInfo.put("extra_freight", extra_freight);
		if(jiaohuoriqi!=null){
			ctpoOrderInfo.put("jiaoqiTime", jiaohuoriqi);
		}else{
			ctpoOrderInfo.put("jiaoqiTime", "");
		}
		
		//查询该订单支付总金额
		ctpoOrderInfo.put("totalProductCost", getOrdersPay(orderNo)[0]);
		//查询该订单Cash Back金额
		ctpoOrderInfo.put("cashback", getOrdersPay(orderNo)[2]);
		return ctpoOrderInfo;
	}


	@Override
	public Object[] getOrdersPay(String orderNo) {
		Map<String, Object> ordersPay = dropshiporderMapper.getOrdersPay(orderNo);
		Object[] orderinfo = new Object[3];
		if(ordersPay!=null){
			Double pay_price = Double.parseDouble(ordersPay.get("pay_price").toString());
			orderinfo[0] = Utility.formatPrice(pay_price.toString()).replaceAll(",", "");
			orderinfo[1] = ordersPay.get("currency").toString();
			orderinfo[2] = ordersPay.get("cashback").toString();
		}else{
			orderinfo[0] = "0.00";
			orderinfo[1] = "USD";
			orderinfo[2] = 0;
		}
		return orderinfo;
	}

	@Override
	public Dropshiporder getDropShipOrder(String childOrderNo, int userId) {
		DropshiporderExample example = new DropshiporderExample();
		Criteria criteria = example.createCriteria();
		if (userId != 0) {
			criteria.andUserIdEqualTo(userId);
		}
		criteria.andChildOrderNoEqualTo(childOrderNo);
//		List<Dropshiporder> list = dropshiporderMapper.selectByExample(example);
		List<Dropshiporder> list = new ArrayList<Dropshiporder>();
		try {
			DataSourceSelector.set("dataSource127hop");
			 list = dropshiporderMapper.selectByExample(example);
	        DataSourceSelector.restore(); 
		} catch (Exception e) {
			LOG.error("获取线上的dropship信息：", e);
		}
		if (list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateDropShipOrder(Dropshiporder dropshiporder) {
		return dropshiporderMapper.updateByPrimaryKeySelective(dropshiporder);
	}

	@Override
	public List<Dropshiporder> getDropshiporderList(String parentOrderNo, int userId, String state) {
		DropshiporderExample example = new DropshiporderExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentOrderNoEqualTo(parentOrderNo);
		criteria.andUserIdEqualTo(userId);
		criteria.andStateEqualTo(state);
		return dropshiporderMapper.selectByExample(example);
	}


}
