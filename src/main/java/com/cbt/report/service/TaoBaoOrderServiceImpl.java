package com.cbt.report.service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.pojo.*;
import com.cbt.report.dao.TaoBaoOrderMapper;
import com.cbt.util.DoubleUtil;
import com.cbt.warehouse.ctrl.NewOrderDetailsCtr;
import com.cbt.warehouse.pojo.Shipments;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.importExpress.mapper.IPurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author whj
 *
 */
@Service
public class TaoBaoOrderServiceImpl implements TaobaoOrderService {
	@Autowired
	private TaoBaoOrderMapper taoBaoOrderMapper;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private IPurchaseMapper purchaseMapper;

	@Override
	public List<TaoBaoOrderInfo> getTaoBaoOrderList(String orderstatus,String orderSource,String orderdate,String procurementAccount,int start,int end,int DeliveryDate,String orderid,String isCompany,String myorderid,String startdate,String enddate,String paystartdate,String payenddate,int page) {

		return taoBaoOrderMapper.selectTaoBaoOrder(orderstatus,orderSource,orderdate,procurementAccount,start,end,DeliveryDate,orderid,isCompany,myorderid,startdate,enddate,paystartdate,payenddate,page);
	}

	@Override
	public List<TaoBaoOrderInfo> getAllCount(String orderstatus,String orderSource,String orderdate,String procurementAccount,int start,int end,int DeliveryDate,String orderid,String isCompany,String myorderid,String startdate,String enddate,String paystartdate,String payenddate) {

		return taoBaoOrderMapper.selectAllCount(orderstatus,orderSource,orderdate,procurementAccount,start,end,DeliveryDate,orderid,isCompany,myorderid,startdate,enddate,paystartdate,payenddate);
	}

	@Override
	public List<TaoBaoOrderInfo> getTaoBaoOrderDetails(String orderid,String bforderid) {

		return taoBaoOrderMapper.getTaoBaoOrderDetails(orderid,bforderid);
	}

	@Override
	public List isStorage(String orderid) {

		return taoBaoOrderMapper.isStorage(orderid);
	}

	@Override
	public List<TaoBaoOrderInfo> associatedOrder(String orderid) {

		return taoBaoOrderMapper.associatedOrder(orderid);
	}

	@Override
	public List<String> selectStatus() {

		return taoBaoOrderMapper.selectStatus();
	}

	@Override
	public List<com.cbt.pojo.Admuser> getAllBuyer() {

		return taoBaoOrderMapper.getAllBuyer();
	}

	@Override
	public int saveRemark(Map<String, String> map) {
		return taoBaoOrderMapper.saveRemark(map);
	}

	/**
	 * 优惠券启用
	 */
	@Override
	public int enable(int id) {

		return taoBaoOrderMapper.enable(id);
	}

	/**
	 * 查询优惠券主表状态
	 * @param subsidiary_id
	 * @return coupons_id
	 * 2017-06-27 王宏杰
	 */
	@Override
	public int queryCouponsState(int subsidiary_id) {

		return taoBaoOrderMapper.queryCouponsState(subsidiary_id);
	}

	/**
	 * 批量启用停用优惠券详情
	 * @param map
	 * @return
	 * 2017-06-28 王宏杰
	 */
	@Override
	public int AllenableDetails(Map<Object, Object> map) {

		return taoBaoOrderMapper.AllenableDetails(map);
	}

	/**
	 * 批量启用、停用优惠券
	 * @param map
	 * @return
	 * 2017-06-27 王宏杰
	 */
	@Override
	public int Allenable(Map<Object, Object> map) {

		return taoBaoOrderMapper.Allenable(map);
	}
	/**
	 * 线下采购付款审核
	 */
	@Override
	public int ThroughReview(Map<Object, Object> map) {

		return taoBaoOrderMapper.ThroughReview(map);
	}

	/**
	 * 保存修改的优惠券详情
	 * @param map
	 * @return
	 * 2017-06-27 王宏杰
	 */
	@Override
	public int addCoupusDetailsView(Map<Object, Object> map) {

		return taoBaoOrderMapper.addCoupusDetailsView(map);
	}

	@Override
	public String getUserName(int userid) {

		return taoBaoOrderMapper.getUserName(userid);
	}

	/**
	 * 批量更改优惠券详情状态
	 * @param ids
	 * @return
	 * 2017-07-27 whj
	 */
	@Override
	public int enableForSubsidiary(int ids) {

		return taoBaoOrderMapper.enableForSubsidiary(ids);
	}

	@Override
	public List<InOutDetailsInfo> getIinOutDetails(Map<String,String> map) {
		List<InOutDetailsInfo> list=taoBaoOrderMapper.getIinOutDetails(map);
		return list;
	}

	@Override
	public List<InOutDetailsInfo> getIinOutDetailsCount(Map<String,String> map) {

		return taoBaoOrderMapper.getIinOutDetailsCount(map);
	}

	@Override
	public List<StorageOutboundDetailsPojo> searchInventoryInOutDetails(
			Map<Object, Object> map) {
		List<StorageOutboundDetailsPojo> list=taoBaoOrderMapper.searchInventoryInOutDetails(map);
		return list;
	}

	@Override
	public List<StorageOutboundDetailsPojo> searchInventoryInOutDetailsCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.searchInventoryInOutDetailsCount(map);
	}

	/**
	 * 查询优惠券信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Coupons> searchCoupusManagement(Map<Object, Object> map) {
		List<Coupons> list1=taoBaoOrderMapper.searchCoupusManagement(map);
		for (Coupons coupons : list1) {
			if ("1".equals(coupons.getCoupons_type())) {
				coupons.setCoupons_type("限额券");
			} else if ("2".equals(coupons.getCoupons_type())) {
				coupons.setCoupons_type("折扣券");
			} else if ("3".equals(coupons.getCoupons_type())) {
				coupons.setCoupons_type("运费抵用券");
			}
			if ("1".equals(coupons.getDisbursement())) {
				coupons.setDisbursement("新用户发放");
			} else if ("2".equals(coupons.getDisbursement())) {
				coupons.setDisbursement("用户自领");
			} else if ("3".equals(coupons.getDisbursement())) {
				coupons.setDisbursement("下单奖励");
			} else if ("4".equals(coupons.getDisbursement())) {
				coupons.setDisbursement("运营赠送");
			}
			if (coupons.getValidity_type() == 1) {
				coupons.setValidity_day("无期限");
			} else if (coupons.getValidity_type() == 2) {
				coupons.setValidity_day(coupons.getStartdata() + "~" + coupons.getEnddata());
			} else if (coupons.getValidity_type() == 3) {
				coupons.setValidity_day("领取后【" + coupons.getStartdata() + "】天");
			}
			if ("0".equals(coupons.getIs_enable())) {
				coupons.setIs_enable("启用");
			} else if ("1".equals(coupons.getIs_enable())) {
				coupons.setIs_enable("停用");
			}
			if (!StringUtils.isStrNull(coupons.getUsing_range())) {
				coupons.setUsing_range(taoBaoOrderMapper.queryUsingRangeForIds(
						coupons.getUsing_range().substring(0, coupons.getUsing_range().length() - 1)));
			} else {
				coupons.setUsing_range("无");
			}
			coupons.setOperation("<button onclick=\"enable(" + coupons.getId()
					+ ")\">启用</button><button onclick=\"view(" + coupons.getId() + ",'" + coupons.getCoupons_type()
					+ "','" + coupons.getDenomination() + "','" + coupons.getMinimum_cons() + "','"
					+ coupons.getDisbursement() + "','" + coupons.getCoupons_name() + "')\">查看</button>");
		}
		return list1;
	}

	/**
	 * 查询优惠券详情
	 */
	@Override
	public List<CouponSubsidiary> searchCoupusDetails(Map<Object, Object> map) {
		List<CouponSubsidiary> list1=taoBaoOrderMapper.searchCoupusDetails(map);
		for (CouponSubsidiary couponSubsidiary : list1) {
			if (couponSubsidiary.getState() == 1) {
				couponSubsidiary.setStates("待使用");
			} else if (couponSubsidiary.getState() == 2) {
				couponSubsidiary.setStates("已使用");
			} else if (couponSubsidiary.getState() == 3) {
				couponSubsidiary.setStates("占用中");
			} else if (couponSubsidiary.getState() == 4) {
				couponSubsidiary.setStates("已失效");
			}
			if ("1".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("新用户发放");
			} else if ("2".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("用户自领");
			} else if ("3".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("下单奖励");
			} else if ("4".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("运营赠送");
			}
			if (couponSubsidiary.getValidity_type() == 1) {
				couponSubsidiary.setValidity_day("无期限");
			} else if (couponSubsidiary.getValidity_type() == 2) {
				couponSubsidiary.setValidity_day(couponSubsidiary.getStartdata() + "~" + couponSubsidiary.getEnddata());
			} else if (couponSubsidiary.getValidity_type() == 3) {
				couponSubsidiary.setValidity_day("领取后【" + couponSubsidiary.getStartdata() + "】天");
			}
			if (couponSubsidiary.getUse_time() == null || "".equals(couponSubsidiary.getUse_time())) {
				couponSubsidiary.setUse_time("-");
			}
			if (couponSubsidiary.getGet_time() == null || "".equals(couponSubsidiary.getGet_time())) {
				couponSubsidiary.setGet_time("-");
			}
			if (couponSubsidiary.getRemark() == null || "".equals(couponSubsidiary.getRemark())) {
				couponSubsidiary.setRemark("-");
			}
			if (couponSubsidiary.getUser_name() == null || "".equals(couponSubsidiary.getUser_name())) {
				couponSubsidiary.setUser_name("-");
			}
			if ("0".equals(couponSubsidiary.getIs_enable())) {
				couponSubsidiary.setIs_enable("启用");
			} else if ("1".equals(couponSubsidiary.getIs_enable())) {
				couponSubsidiary.setIs_enable("停用");
			}
			couponSubsidiary.setOperation("<button onclick=\"enable(" + couponSubsidiary.getId()
					+ ")\">启用</button><button onclick=\"view(" + couponSubsidiary.getId() + ")\">查看</button>");
		}
		return list1;
	}

	/**
	 * 获取优惠券详情明细
	 * @param map
	 * @return
	 * 2017-06-27 王宏杰
	 */
	@Override
	public List<CouponSubsidiary> searchCoupusDetailsView(Map<Object, Object> map) {
		List<CouponSubsidiary> list1=taoBaoOrderMapper.searchCoupusDetailsView(map);
		for (CouponSubsidiary couponSubsidiary : list1) {
			if ("1".equals(couponSubsidiary.getCoupons_type())) {
				couponSubsidiary.setCoupons_type("限额券");
			} else if ("2".equals(couponSubsidiary.getCoupons_type())) {
				couponSubsidiary.setCoupons_type("折扣券");
			} else if ("3".equals(couponSubsidiary.getCoupons_type())) {
				couponSubsidiary.setCoupons_type("运费抵用券");
			}
			if (couponSubsidiary.getState() == 1) {
				couponSubsidiary.setStates("待使用");
			} else if (couponSubsidiary.getState() == 2) {
				couponSubsidiary.setStates("已使用");
			} else if (couponSubsidiary.getState() == 3) {
				couponSubsidiary.setStates("占用中");
			} else if (couponSubsidiary.getState() == 4) {
				couponSubsidiary.setStates("已失效");
			}
			if ("1".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("新用户发放");
			} else if ("2".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("用户自领");
			} else if ("3".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("下单奖励");
			} else if ("4".equals(couponSubsidiary.getDisbursement())) {
				couponSubsidiary.setDisbursement("运营赠送");
			}
			if (!StringUtils.isStrNull(couponSubsidiary.getUsing_range())) {
				couponSubsidiary.setUsing_range(taoBaoOrderMapper.queryUsingRangeForIds(couponSubsidiary
						.getUsing_range().substring(0, couponSubsidiary.getUsing_range().length() - 1)));
			}
		}
		return list1;
	}
	/**
	 * 根据选择的年月获取相应的支付宝数据
	 * data 日期  2017-04
	 */
	@Override
	public List<ZfuDate> getZfuDate(String data) {

		return taoBaoOrderMapper.getZfuDate(data);
	}
	/**
	 * 修改/添加当月支付宝余额
	 * @param map
	 * @return
	 */
	@Override
	public int addZfbData(Map<Object, Object> map) {

		return taoBaoOrderMapper.addZfbData(map);
	}
	/**
	 * 月统计采购对账报表
	 * @param map
	 * @return
	 */
	@Override
	public List<BuyReconciliationPojo> buyReconciliationReport(Map<Object, Object> map,String type) {
		DecimalFormat df = new DecimalFormat("#0.###");
		List<BuyReconciliationPojo> list=taoBaoOrderMapper.buyReconciliationReport(map);
		for (BuyReconciliationPojo buyReconciliationPojo : list) {
			double ac = Double.valueOf(buyReconciliationPojo.getBeginBlance())//51832.66
					+ Double.valueOf(buyReconciliationPojo.getTransfer())//205001.00
					- Double.valueOf(buyReconciliationPojo.getEndBlance())//106764.13
					- Double.valueOf(buyReconciliationPojo.getEbayAmount())//37000.00
					- Double.valueOf(buyReconciliationPojo.getMaterialsAmount())//4876.44
					- Double.valueOf(buyReconciliationPojo.getZfbFright());//30000.00
//  51832.66+205001.00-106764.13-37000.00-4876.44-30000.00
			double zfbPayAmount = Double.valueOf(buyReconciliationPojo.getNormalAmount())
					+ Double.valueOf(buyReconciliationPojo.getCancelAmount())
					+ Double.valueOf(buyReconciliationPojo.getNoMatchingOrder())
					+ Double.valueOf(buyReconciliationPojo.getNoStorage());
			//月度利润
			double monthProfit=Double.valueOf(StringUtil.isBlank(buyReconciliationPojo.getOrder_sales())?"0.00":buyReconciliationPojo.getOrder_sales())
					- Double.valueOf(buyReconciliationPojo.getGrabAmount())
					- Double.valueOf(buyReconciliationPojo.getPayFreight())
					- Double.valueOf(buyReconciliationPojo.getSale_inventory())
					+ Double.valueOf(buyReconciliationPojo.getInventory_amount())
					- Double.valueOf(buyReconciliationPojo.getBalance_compensation());
			buyReconciliationPojo.setProfit(df.format(monthProfit));
			if("0".equals(type)){
				buyReconciliationPojo.setGrabAmounts("<span style='color:red'>" + df.format(ac) + "</span>");
				buyReconciliationPojo.setZfbPayAmount("<span style='color:red'>" + df.format(zfbPayAmount) + "</span>");
				buyReconciliationPojo.setGrabAmount("<a target='_blank' href='/cbtconsole/website/grabNormalOrder.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getGrabAmount() + "'>"
						+ buyReconciliationPojo.getGrabAmount() + "</a>");
				buyReconciliationPojo.setNoMatchingOrder("<a target='_blank' href='/cbtconsole/website/noMatchingOrder.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getNoMatchingOrder() + "'>"
						+ buyReconciliationPojo.getNoMatchingOrder() + "</a>");
				buyReconciliationPojo.setNoStorage("<a target='_blank' href='/cbtconsole/website/noStorage.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getNoStorage() + "'>" + buyReconciliationPojo.getNoStorage() + "</a>");
				buyReconciliationPojo.setCancelAmount("<a target='_blank' href='/cbtconsole/website/cancelAmount.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getCancelAmount() + "'>" + buyReconciliationPojo.getCancelAmount()
						+ "</a>");
				buyReconciliationPojo.setLastMonth("<a target='_blank' href='/cbtconsole/website/lastMonth.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getLastMonth() + "'>" + buyReconciliationPojo.getLastMonth() + "</a>");
				buyReconciliationPojo.setForecastAmount("<a target='_blank' href='/cbtconsole/website/forecastAmount.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getForecastAmount() + "'>"
						+ buyReconciliationPojo.getForecastAmount() + "</a>");
				buyReconciliationPojo.setActualAmount("<a target='_blank' href='/cbtconsole/website/actualAmount.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getActualAmount() + "'>" + buyReconciliationPojo.getActualAmount()
						+ "</a>");
				buyReconciliationPojo.setInventory_amount("<a target='_blank' href='/cbtconsole/website/inventoryAmount.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getInventory_amount() + "'>" + buyReconciliationPojo.getInventory_amount()
						+ "</a>");
				buyReconciliationPojo.setSale_inventory("<a target='_blank' href='/cbtconsole/website/saleInventory.jsp?times="
						+ buyReconciliationPojo.getTimes().replace("年", "-").replace("月", "") + "&amount="
						+ buyReconciliationPojo.getSale_inventory() + "'>" + buyReconciliationPojo.getSale_inventory()
						+ "</a>");
			}else{
				buyReconciliationPojo.setGrabAmounts(String.valueOf(ac));
				buyReconciliationPojo.setZfbPayAmount(df.format(zfbPayAmount));
			}
//			OrderSalesAmountPojo osp=taoBaoOrderMapper.getMonthlyFinancialStatistics(buyReconciliationPojo.getTimes());
//			if(osp!=null && StringUtil.isNotBlank(osp.getOrder_sales())){
//				buyReconciliationPojo.setOrder_sales(osp.getOrder_sales());
//				buyReconciliationPojo.setBalance_compensation(osp.getBalance_compensation());
//			}

		}
		return list;
	}
	/**
	 * 订单页面的销售额统计报表
	 */
	@Override
	public List<OrderSalesAmountPojo> orderSalesAmount(Map<Object, Object> map) {
		List<OrderSalesAmountPojo> list=taoBaoOrderMapper.orderSalesAmount(map);
		for (OrderSalesAmountPojo orderSalesAmountPojo : list) {
			orderSalesAmountPojo.setForecastProfits(
					"<span style='color:red'>" + orderSalesAmountPojo.getForecastProfits() + "</span>");
		}
		return list;
	}

	@Override
	public List<OrderSalesAmountPojo> getProfitSummaryData(Map<String, String> map) {
		DecimalFormat df = new DecimalFormat("#0.00");
		List<OrderSalesAmountPojo> list=taoBaoOrderMapper.getProfitSummaryData(map);
		double estimateProfitAmount=0.00;
		double forecastProfitsAmount=0.00;
		double endProfit=0.00;
		for(int i=0;i<list.size();i++){
			OrderSalesAmountPojo o=list.get(i);
			o.setSalesAmount(df.format(Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())));
			if(StringUtil.isNotBlank(o.getBuyAmount()) && Double.parseDouble(o.getBuyAmount())>0){
				o.setBuyAmount(df.format(Double.valueOf(o.getBuyAmount())+o.getPid_amount()));
			}else{
				o.setBuyAmount("0.00");
			}
			//根据实际重量预估的运费实际利润金额=实际销售额-实际重量预估运费-录入采购额
			double profit=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
					-Double.valueOf(StringUtil.isBlank(o.getBuyAmount())?"0.00":o.getBuyAmount())
					-Double.valueOf(StringUtil.isBlank(o.getEstimateFreight())?"0.00":o.getEstimateFreight());
			if(StringUtil.isBlank(o.getBuyAmount()) || Double.parseDouble(o.getBuyAmount())<=0){
				profit=0.00;
			}
			if(profit>0){
				forecastProfitsAmount+=profit;
			}
			//根据物流公司运费计算最终利润金额=实际销售额-录入采购金额-实际支出运费
			double endProfits=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
					-Double.valueOf(StringUtil.isBlank(o.getBuyAmount())?"0.00":o.getBuyAmount())
					-Double.valueOf(StringUtil.isBlank(o.getFreight())?"0.00":o.getFreight());
			if(StringUtil.isBlank(o.getBuyAmount()) || Double.parseDouble(o.getBuyAmount())<=0 || StringUtil.isBlank(o.getFreight()) || Double.parseDouble(o.getFreight())<=0){
				endProfits=0.00;
			}
			if(endProfits>0){
				endProfit+=endProfits;
			}
			//根据预估重量预估运费计算预估利润金额=实际销售额-预估采购金额-客户付的预估国际运费
			double estimateProfit=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
					-Double.valueOf(o.getEsBuyPrice())
					-Double.valueOf(StringUtil.isBlank(o.getCustom_freight())?"0.00":o.getCustom_freight());
			estimateProfitAmount+=estimateProfit;
		}
		if(list.size()>0){
			list.get(0).setEstimateProfit(df.format(estimateProfitAmount));
			list.get(0).setForecastProfits(df.format(forecastProfitsAmount));
			list.get(0).setEndProfits(df.format(endProfit));
		}
		return list;
	}

	@Override
	public int deleteUserProfitByMonth(String time) {
		return taoBaoOrderMapper.deleteUserProfitByMonth(time);
	}

	@Override
	public List<OrderSalesAmountPojo> getUserProfitByMonth(Map<String, String> map) {
		DecimalFormat df = new DecimalFormat("#0.00");
		List<OrderSalesAmountPojo> list=taoBaoOrderMapper.getUserProfitByMonth(map);
		for(int i=0;i<list.size();i++){
			OrderSalesAmountPojo o=list.get(i);
			String shipnos=o.getShipnos();
			StringBuilder sbs=new StringBuilder();
			if(StringUtil.isNotBlank(shipnos) && shipnos.indexOf(",")>-1){
				String [] nos=shipnos.split(",");
				for(String n:nos){
					sbs.append(n).append(" ");
				}
			}else{
				sbs.append(StringUtil.isBlank(shipnos)?"":shipnos);
			}
			o.setShipnos(sbs.toString());
			sbs.setLength(0);
			String orderids=o.getOrderids();
			if(StringUtil.isNotBlank(orderids) && orderids.indexOf(",")>-1){
				String [] nos=orderids.split(",");
				for(String n:nos){
					String []orderInfo=n.split("@");
					sbs.append("<a target='_blank' style='color:"+("1".equals(orderInfo[1])?"red":"green")+"' href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+orderInfo[0]+"'>"+orderInfo[0]+"</a>").append(" ");
				}
			}else{
				String [] orderInfo=orderids.split("@");
				sbs.append(StringUtil.isBlank(orderInfo[0])?"":"<a target='_blank' style='color:"+("1".equals(orderInfo[1])?"red":"green")+"' href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+orderInfo[0]+"'>"+orderInfo[0]+"</a>");
			}
			o.setOrderids(sbs.toString());
			o.setId("<a tilte='点击查看户交易记录' target='_blank' href='/cbtconsole/website/paycheck_new.jsp?u_id="+o.getId()+"&s_time="+map.get("startTime").substring(0,10)+"&e_time="+map.get("endTime").substring(0,10)+"'>"+o.getId()+"</a>");
			o.setSalesAmount(df.format(Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())));
			if(StringUtil.isNotBlank(o.getBuyAmount())){
				o.setBuyAmount(df.format(Double.valueOf(o.getBuyAmount())+o.getPid_amount()));
			}else{
				o.setBuyAmount("0.00");
			}
			//根据实际重量预估的运费实际利润金额=实际销售额-实际重量预估运费-录入采购额
			double profit=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
					-Double.valueOf(StringUtil.isBlank(o.getBuyAmount())?"0.00":o.getBuyAmount())
					-Double.valueOf(StringUtil.isBlank(o.getEstimateFreight())?"0.00":o.getEstimateFreight());
			if(StringUtil.isBlank(o.getBuyAmount()) || Double.parseDouble(o.getBuyAmount())<=0 || StringUtil.isBlank(o.getEstimateFreight()) || Double.parseDouble(o.getEstimateFreight())<=0){
				profit=0.00;
			}
			if(profit>0){
				o.setForecastProfits("<span title='实际重量预估的运费实际利润金额=实际销售额-实际重量预估运费-录入采购额' style='color:red'>"+df.format(profit)+"</span>");
				o.setfProfits(df.format(profit/(Double.valueOf(o.getSalesAmount()))*100)+"%");
			}else{
				o.setForecastProfits("<span title='实际重量预估的运费实际利润金额=实际销售额-实际重量预估运费-录入采购额' style='color:red'>--</span>");
				o.setfProfits("--");
			}
			//根据物流公司运费计算最终利润金额=实际销售额-录入采购金额-实际支出运费
			double endProfits=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
					-Double.valueOf(StringUtil.isBlank(o.getBuyAmount())?"0.00":o.getBuyAmount())
					-Double.valueOf(StringUtil.isBlank(o.getFreight())?"0.00":o.getFreight());
			if(StringUtil.isBlank(o.getBuyAmount()) || Double.parseDouble(o.getBuyAmount())<=0 || StringUtil.isBlank(o.getFreight()) || Double.parseDouble(o.getFreight())<=0){
				endProfits=0.00;
			}
			if(endProfits>0){
				o.setEndProfits("<span title='物流公司运费计算最终利润金额=实际销售额-录入采购金额-实际支出运费' style='color:red'>"+df.format(endProfits)+"</span>");
			}else{
				o.setEndProfits("<span title='物流公司运费计算最终利润金额=实际销售额-录入采购金额-实际支出运费' style='color:red'>--</span>");
			}
			//根据预估重量预估运费计算预估利润金额=实际销售额-预估采购金额-客户付的预估国际运费
			double estimateProfit=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
					-Double.valueOf(o.getEsBuyPrice())
					-Double.valueOf(StringUtil.isBlank(o.getCustom_freight())?"0.00":o.getCustom_freight());
			o.setEstimateProfit("<span title='预估重量预估运费计算预估利润金额=实际销售额-预估采购金额-客户付的预估国际运费' style='color:red'>"+df.format(estimateProfit)+"</span>");
			o.setEsprofits(df.format(estimateProfit/(Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount()))*100)+"%");
			if(StringUtil.isNotBlank(o.getEstimateFreight())){
				o.setEstimateFreight(df.format(Double.valueOf(o.getEstimateFreight())));
			}
		}
		return list;
	}

	public double getOrderShippingCost(double weight ,int countryid,String shippingMethod,boolean isSpecialCatid){
		double sumNormalWeight = 0d;
		double sumSpecialWeight = 0d;
		double sumEubNormalWeight = 0d;
		double sumEubSpecialWeight = 0d;
		double sumEubWeight = 0d;
		//免邮商品的总重量
		double sumFreeNormaWeight = 0d;
		double sumFreeSpecialWeight = 0d;
		double sumFreeWeight = 0d;
		double shippingCost = 0d;
		double sumGoodsCarWeight = 0d;
		if(isSpecialCatid){
			sumSpecialWeight = DoubleUtil.mul(weight,1000d);
		}else {
			sumGoodsCarWeight =  DoubleUtil.mul(weight,1000d);
		}
		double normalBaseWeight = 0d;
		BigDecimal normalBasePrice = new BigDecimal(0);
		BigDecimal normalRatioPrice = new BigDecimal(0);
		BigDecimal normalBigWeightPrice = new BigDecimal(0);
		Double specialBaseWeight = 0d;
		BigDecimal specialBasePrice = new BigDecimal(0);
		BigDecimal specialRatioPrice = new BigDecimal(0);
		BigDecimal specialBigWeightPrice = new BigDecimal(0);
		//第一步 根据国家id 运输方式 获取相关运费信息
		TabTransitFreightinfoUniteNewExample example = new TabTransitFreightinfoUniteNewExample();
		TabTransitFreightinfoUniteNewExample.Criteria criteria = example.createCriteria();
		criteria.andCountryidEqualTo(countryid);
		criteria.andTransportModeEqualTo(shippingMethod);
		List<TabTransitFreightinfoUniteNew> list = iOrderinfoService.selectByExample(example);
		if(list.size()<=0){
			TabTransitFreightinfoUniteNewExample example1 = new TabTransitFreightinfoUniteNewExample();
			TabTransitFreightinfoUniteNewExample.Criteria criteria1 = example1.createCriteria();
			criteria1.andCountryidEqualTo(29);
			criteria1.andTransportModeEqualTo(shippingMethod);
			list = iOrderinfoService.selectByExample(example1);
		}
		BigDecimal sumFreight = new BigDecimal(0);
		TransitPricecost transitPricecost = new TransitPricecost();
		BigDecimal singleFreight = new BigDecimal(0);
		BigDecimal singleFreightNor = new BigDecimal(0);
		BigDecimal singleFreightSpc = new BigDecimal(0);
		//免邮商品当前的运费
		BigDecimal singleFreeFreightNor = new BigDecimal(0);
		BigDecimal singleFreeFreightSpc = new BigDecimal(0);
		//免邮商品当前运输费方式不带首重运费
		BigDecimal sumFreeShippingCost = new BigDecimal(0);
		// 判断初始重量是否是500g
		double ratioWeight = 500d;
		BigDecimal zero = new BigDecimal(0);
		if(list.size()>0){
			TabTransitFreightinfoUniteNew transitInfo =  list.get(0);
			shippingMethod = transitInfo.getTransportMode();
			normalBaseWeight = transitInfo.getNormalBaseWeight();
			normalBasePrice = transitInfo.getNormalBasePrice();
			normalRatioPrice = transitInfo.getNormalRatioPrice();
			normalBigWeightPrice = transitInfo.getNormalBigWeightPrice();
			specialBaseWeight = transitInfo.getSpecialBaseWeight();
			specialBasePrice = transitInfo.getSpecialBasePrice();
			specialRatioPrice = transitInfo.getSpecialRatioPrice();
			specialBigWeightPrice = transitInfo.getSpecialBigWeightPrice();
			boolean isSplit = transitInfo.getSplit() == 1 ? true : false;
			if(isSplit && sumSpecialWeight>0){
				sumGoodsCarWeight = sumSpecialWeight;
			}
			//基础运费加上 20%利润率
			BigDecimal multiple = new BigDecimal(1.00).setScale(2,BigDecimal.ROUND_HALF_UP);
			//Added <V1.0.1> Start： cjc 2018/8/18 18:26 TODO DHL,FEDEX 运费涨价10%
			BigDecimal rate = new BigDecimal(0.90).setScale(2,BigDecimal.ROUND_HALF_UP);
			if("DHL".equals(shippingMethod) || "FEDEX".equals(shippingMethod)){
				normalBasePrice = normalBasePrice.multiply(rate).setScale(6);
				normalRatioPrice = normalRatioPrice.multiply(rate).setScale(6);
				normalBigWeightPrice = normalBigWeightPrice.multiply(rate).setScale(6);
				specialBasePrice = specialBasePrice.multiply(rate).setScale(6);
				specialBigWeightPrice = specialBigWeightPrice.multiply(rate).setScale(6);
				specialRatioPrice = specialRatioPrice.multiply(rate).setScale(6);
			}
			boolean isEC = false;
			if(isSplit){
				//Added <V1.0.1> Start： cjc 2018/8/31 11:14 TODO 计算当前重量是 如果 ratio weight=500g 则 运费 = sumweight/500 倍数 * ratio price
				if(sumSpecialWeight > 21000){
					singleFreightSpc = sumSpecialWeight >0 ? singleFreight.add(specialBigWeightPrice.multiply(new BigDecimal(sumSpecialWeight/1000))) : new BigDecimal(0);

				}else {
					if(specialBaseWeight == ratioWeight){
						singleFreightSpc = sumSpecialWeight >0 ? specialBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumSpecialWeight,specialBaseWeight)),1d))).multiply(specialRatioPrice)) :zero;

					}else {
						singleFreightSpc = sumSpecialWeight >0 ? specialBasePrice.add( new BigDecimal((sumSpecialWeight - specialBaseWeight)/specialBaseWeight).multiply(specialRatioPrice)) :new BigDecimal(0);

					}
				}
				if(sumNormalWeight > 21000){
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBigWeightPrice.multiply(new BigDecimal(sumGoodsCarWeight/1000))) : new BigDecimal(0);

				}else {
					if(normalBaseWeight == ratioWeight){
						singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumGoodsCarWeight,normalBaseWeight)),1d))).multiply(normalRatioPrice))) : zero;

					}else {
						singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBasePrice.add( new BigDecimal((sumGoodsCarWeight - normalBaseWeight)/normalBaseWeight).multiply(normalRatioPrice))) : new BigDecimal(0);

					}

				}

				singleFreight = singleFreightSpc.add(singleFreightNor);
			}else {
				if(sumGoodsCarWeight > 21000){
					singleFreight = sumGoodsCarWeight > 0 ? singleFreight.add(normalBigWeightPrice.multiply(new BigDecimal(sumGoodsCarWeight/1000))) : zero;
				}else {
					if(normalBaseWeight == ratioWeight){
						singleFreight = sumGoodsCarWeight > 0 ? normalBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumGoodsCarWeight,normalBaseWeight)),1d))).multiply(normalRatioPrice) ) : zero;
					}else {
						singleFreight = sumGoodsCarWeight > 0 ? normalBasePrice.add( new BigDecimal((sumGoodsCarWeight - normalBaseWeight)/normalBaseWeight).multiply(normalRatioPrice) ) : zero;
					}
				}
			}
		}
		shippingCost = singleFreight.doubleValue();
		return shippingCost;
	}

	@Override
	public List<OrderSalesAmountPojo> getUserProfitByMonthCount(Map<String, String> map) {
		DecimalFormat df = new DecimalFormat("#0.##");
		IOrderwsServer server = new OrderwsServer();
		NewOrderDetailsCtr newOrder=new NewOrderDetailsCtr();
		List<OrderSalesAmountPojo> list=taoBaoOrderMapper.getUserProfitByMonthCount(map);
		if("1".equals(map.get("type"))){
			for(int i=0;i<list.size();i++){
				OrderSalesAmountPojo o=list.get(i);
				//获取用户下单付的预估国际运费
				double freightFee=0.00;
				List<Map<String,String>> orderList=taoBaoOrderMapper.getAllOrderNo(o.getId(),map.get("startTime"),map.get("endTime"));
//				for(String orderNo:orderList){
//					String allFreight = String.valueOf(server.getAllFreightByOrderid(orderNo));
//					OrderBean orderInfo = server.getOrders(orderNo);
//					freightFee += newOrder.getFreightFee(allFreight, orderInfo);
//				}
				o.setCustom_freight(df.format(freightFee));
				//实际运费
				o.setFreight(taoBaoOrderMapper.getActualFreight(o.getId(),map.get("startTime"),map.get("endTime")));
				o.setSalesAmount(df.format(Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())));
				o.setBuyAmount(df.format(Double.valueOf(o.getBuyAmount())+o.getPid_amount()));
				double profit=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
						-Double.valueOf(StringUtil.isBlank(o.getBuyAmount())?"0.00":o.getBuyAmount())
						-Double.valueOf(StringUtil.isBlank(o.getFreight())?"0.00":o.getFreight());
				o.setForecastProfits(df.format(profit));
				o.setProfits(df.format(profit/(Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount()))*100));
				double estimateProfit=Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount())
						-Double.valueOf(StringUtil.isBlank(o.getBuyAmount())?"0.00":o.getBuyAmount())
						-Double.valueOf(StringUtil.isBlank(o.getEstimateFreight())?"0.00":o.getEstimateFreight());
				o.setEstimateProfit(df.format(estimateProfit));
				o.setEsprofits(df.format(estimateProfit/(Double.valueOf(StringUtil.isBlank(o.getSalesAmount())?"0.00":o.getSalesAmount()))*100));
			}
		}
		return list;
	}

	/**
	 * 付款审批的相关财务统计报表
	 */
	@Override
	public List<OrderDetetailsSalePojo> orderDetetailsSale(Map<Object, Object> map) {
		List<OrderDetetailsSalePojo> list=taoBaoOrderMapper.orderDetetailsSale(map);
		for (OrderDetetailsSalePojo o : list) {
			o.setTimes("<a href='/cbtconsole/website/orderDetailsSalesDetails.jsp?times=" + o.getTimes()
					+ "' target='_blank'>" + o.getTimes() + "</a>");
		}
		return list;
	}

	@Override
	public int insertUserProfitBatch(List<OrderSalesAmountPojo> list) {
		return taoBaoOrderMapper.insertUserProfitBatch(list);
	}

	@Override
	public List<OrderSalesAmountPojo> getUserProfitByMonthData(Map<String, String> map) {
		return taoBaoOrderMapper.getUserProfitByMonthData(map);
	}

	@Override
	public List<OrderSalesAmountPojo> getUserProfitByMonthCountData(Map<String, String> map) {
		return taoBaoOrderMapper.getUserProfitByMonthCountData(map);
	}

	/**
	 * 付款审批的相关财务统计报表数量
	 */
	@Override
	public List<OrderDetetailsSalePojo> orderDetetailsSaleCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.orderDetetailsSaleCount(map);
	}
	/**
	 * 付款审批的相关财务统计明细报表
	 */
	@Override
	public List<OrderDetetailsSalePojo> orderDetetailsSaleDetails(Map<Object, Object> map) {
		List<OrderDetetailsSalePojo> list=taoBaoOrderMapper.orderDetetailsSaleDetails(map);
		for (OrderDetetailsSalePojo o : list) {
			o.setOrderActualFright("<span>" + o.getOrderActualFright() + "</span><span style='color:red'>(出运号:"+ o.getShipmentno() + ")</span>");
		}
		List<OrderDetetailsSalePojo> list1 = taoBaoOrderMapper.orderDetetailsSale(map);
		OrderDetetailsSalePojo ads = new OrderDetetailsSalePojo();
		ads.setOrder_no("<span style='color:red'>日期:" + list1.get(0).getTimes() + "</span>");
		ads.setOriginalAmount("<span style='color:red'>总进账:" + list1.get(0).getTotalSum() + "</span>");
		ads.setEstimateBuyAmount("<span style='color:red'>总退款:" + list1.get(0).getTitalRefund() + "</span>");
		ads.setEstimateFright("<span style='color:red'>余额支付:" + list1.get(0).getBalancePayment() + "</span>");
		ads.setEstimateProfits("<span style='color:red'>余额充值:" + list1.get(0).getBalancePrepaid() + "</span>");
		ads.setOrderactualAmount("<span style='color:red'>销售进账:" + list1.get(0).getSalesAmount() + "</span>");
		ads.setOrderActualBuyAmount("<span style='color:red'>取消订单销售额:" + list1.get(0).getCancelBeforeOrderAmount() + "</span>");
		list.add(ads);
		return list;
	}
	/**
	 * 付款审批的相关财务统计报表明细数量
	 */
	@Override
	public List<OrderDetetailsSalePojo> orderDetetailsSaleDetailsCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.orderDetetailsSaleDetailsCount(map);
	}
	/**
	 * 将使用的库存放回到库存库位
	 * @author whj
	 * 2017-08-22
	 */
	@Override
	public int reductionInventory(Map<Object, Object> map) {

		return taoBaoOrderMapper.reductionInventory(map);
	}
	/**
	 * 商品取消后使用的库存还原
	 */
	@Override
	public List<CancelGoodsInventory> searchCancelGoodsInventory(Map<Object, Object> map) {

		return taoBaoOrderMapper.searchCancelGoodsInventory(map);
	}
	/**
	 * 线下采购申请明细
	 * @param map
	 * @return
	 */
	@Override
	public List<OfflinePaymentApplicationPojo> getOfflinePaymentApplication(
			Map<Object, Object> map) {
		List<OfflinePaymentApplicationPojo> list=taoBaoOrderMapper.getOfflinePaymentApplication(map);
		for (OfflinePaymentApplicationPojo o : list) {
			o.setCar_img("<img src='" + o.getCar_img() + "' height='100' width='100'>");
			if("1".equals(o.getFlag())){
				o.setOperation("<button disabled='disabled' style='background-color:gray' onclick=\"throughReview(" + o.getId()+ ")\">通过</button>");
			}else{
				o.setOperation("<button onclick=\"throughReview(" + o.getId()+ ")\">通过</button>");
			}
			o.setFlag("1".equals(o.getFlag())?"已批准":"未批准");
			o.setOff_remark("<textarea style='width:186px;height:100px;'>"+(o.getOff_remark()==null?"":o.getOff_remark())+"</textarea>");
		}
		return list;
	}
	@Override
	public int straightShipnoEntry(Map<Object, Object> map) {

		return taoBaoOrderMapper.straightShipnoEntry(map);
	}
	/**
	 * 线下采购申请明细数量
	 * @param map
	 * @return
	 */
	@Override
	public List<OfflinePaymentApplicationPojo> getOfflinePaymentApplicationCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.getOfflinePaymentApplicationCount(map);
	}
	/**
	 * 无订单匹配采购订单明细
	 */
	@Override
	public List<TaoBaoOrderInfo> getNoMatchingOrder(Map<Object, Object> map) {

		return taoBaoOrderMapper.getNoMatchingOrder(map);
	}
	/**
	 * 无订单匹配采购订单明细数量
	 */
	@Override
	public List<TaoBaoOrderInfo> getNoMatchingOrderCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getNoMatchingOrderCount(map);
	}
	/**
	 * 无订单匹配采购订单明细
	 */
	@Override
	public List<TaoBaoOrderInfo> getNoStorage(Map<Object, Object> map) {

		return taoBaoOrderMapper.getNoStorage(map);
	}

	@Override
	public List<TaoBaoOrderInfo> getNoStorageDetails(Map<String, String> map) {
		List<TaoBaoOrderInfo> list=taoBaoOrderMapper.getNoStorageDetails(map);
		for (TaoBaoOrderInfo c : list) {
			if ("0".equals(c.getTbOr1688())) {
				c.setTbOr1688("淘宝");
			} else if ("1".equals(c.getTbOr1688())) {
				c.setTbOr1688("1688");
			} else if ("3".equals(c.getTbOr1688())) {
				c.setTbOr1688("天猫");
			} else {
				c.setTbOr1688("未知");
			}
			c.setItemname("<a target='_blank' href='"+c.getItemurl()+"'>"+c.getItemname().substring(0,c.getItemname().length()/3)+"</a>");
			c.setImgurl("<img src='"+c.getImgurl()+"' height='100' width='100'>");
		}
		return list;
	}

	@Override
	public int getNoStorageDetailsCount(Map<String, String> map) {
		return taoBaoOrderMapper.getNoStorageDetailsCount(map);
	}

	/**
	 * 无订单匹配采购订单明细数量
	 */
	@Override
	public List<TaoBaoOrderInfo> getNoStorageCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getNoStorageCount(map);
	}
	/**
	 * 当月产生库存明细
	 */
	@Override
	public List<InventoryDetailsPojo> getInventoryAmount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getInventoryAmount(map);
	}
	/**
	 * Importexpress-GA数据统计
	 *
	 * @param
	 * @param
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 * @author whj 2017-08-28
	 */
	@Override
	public List<DataQueryBean> getOrderQuery(Map<Object, Object> map) {

		return taoBaoOrderMapper.getOrderQuery(map);
	}
	/**
	 * 查询建议或确定广东直发的列表
	 */
	@Override
	public List<StraightHairPojo> StraightHairList(Map<Object, Object> map) {
		List<StraightHairPojo> list= taoBaoOrderMapper.StraightHairList(map);
		for (StraightHairPojo s : list) {
			s.setImg("<a target='_blank' href='"+s.getCar_url()+"'><img  height='100' width='100' src='"+s.getImg()+"'/></a>");
			if("1".equals(s.getState())){
				s.setState("建议直发");
			}else if("2".equals(s.getState())){
				s.setState("确定直发");
			}
			if(StringUtil.isNotBlank(s.getStates()) && Integer.valueOf(s.getStates())>0){
				s.setStates("<span style='color:green'>已签收</span>");
			}else{
				s.setStates("<span style='color:red'>未签收</span>");
			}
			s.setOperating("<button onclick='nntryShipno(\""+s.getOrderid()+"\",\""+s.getGoodsid()+"\")'>录入快递号</button>");
		}
		return list;
	}
	@Override
	public List<LossInventoryPojo> searchLossInventory(Map<Object,Object> map) {
		List<LossInventoryPojo> list=taoBaoOrderMapper.searchLossInventory(map);
		for (LossInventoryPojo l : list) {
			l.setCar_img("<a href='"+l.getGoods_p_url()+"' target='_blank'><img  src='"+ (l.getCar_img().indexOf("1.png")>-1?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":l.getCar_img()) + "' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"+ l.getCar_img() + "')\" height='100' width='100'></a>");
		}
		return list;
	}
	@Override
	public List<PayFailureOrderPojo> getPayFailureOrder(Map<Object, Object> map) {
		List<PayFailureOrderPojo> list=taoBaoOrderMapper.getPayFailureOrder(map);
		for (PayFailureOrderPojo p : list) {
			p.setDescription("<span title='"+p.getDescription()+"'>"+p.getDescription()+"</span>");
		}
		return list;
	}

	@Override
	public List<ReviewManage> reviewManagerment(Map<String, String> map) {
		List<ReviewManage> list=taoBaoOrderMapper.reviewManagerment(map);
		for(ReviewManage r:list){
			r.setSendEmail("<button onclick=\"msgbox(" + r.getUser_id() + ")\">邮件回复</button>");
			if("1".equals(r.getShow_flag())){
				r.setShow_flag("<span style='color:green'>前端显示</span><br><button style='color:red' onclick=\"setDisplay(" + r.getId() + ",0)\">设置不显示</button>");
			}else if("0".equals(r.getShow_flag())){
				r.setShow_flag("<span style='color:red'>前端不显示<span><br><button style='color:green' onclick=\"setDisplay(" + r.getId() + ",1)\">设置显示</button>");
			}
			r.setUser_id("用户id:"+r.getUser_id()+"<br>用户账号:"+r.getUser_name()+"");
			r.setGoodsInfo("<a target='_blank' href='https://www.import-express.com/goodsinfo/cbtconsole-1"+r.getGoods_pid()+".html'><img style='width:60px;height:60px;' src=\""+r.getCar_img()+"\"></img></a><br>"+r.getGoodsname()+"");
			r.setGoodsid("订单号:"+r.getOrder_no()+"<br>商品号:"+r.getGoodsid()+"<br>商品pid:"+r.getGoods_pid()+"");
		}
		return list;
	}

	@Override
	public List<ReviewManage> reviewManagermentCount(Map<String, String> map) {
		return taoBaoOrderMapper.reviewManagermentCount(map);
	}

	@Override
	public List<PayFailureOrderPojo> getPayFailureOrderCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.getPayFailureOrderCount(map);
	}
	@Override
	public List<LossInventoryPojo> searchLossInventoryCount(Map<Object,Object> map) {

		return taoBaoOrderMapper.searchLossInventoryCount(map);
	}
	/**
	 * 查询建议或确定广东直发的列表数量
	 */
	@Override
	public List<StraightHairPojo> StraightHairListCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.StraightHairListCount(map);
	}
	/**
	 * 当月产生库存明细数量
	 */
	@Override
	public List<InventoryDetailsPojo> getInventoryAmountCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.getInventoryAmountCount(map);
	}
	/**
	 * 当月销售库存明细
	 */
	@Override
	public List<InventoryDetailsPojo> getSaleInventory(Map<Object, Object> map) {

		return taoBaoOrderMapper.getSaleInventory(map);
	}
	/**
	 * 当月销售库存明细数量
	 */
	@Override
	public List<InventoryDetailsPojo> getSaleInventoryCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.getSaleInventoryCount(map);
	}

	@Override
	public int setDisplay(Map<String, String> map) {
		return taoBaoOrderMapper.setDisplay(map);
	}

	/**
	 * 商品取消后使用的库存还原记录数量
	 */
	@Override
	public int searchCancelGoodsInventoryCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.searchCancelGoodsInventoryCount(map);
	}
	/**
	 * 采购订单对应上月销售订单明细
	 */
	@Override
	public List<TaoBaoOrderInfo> getCancelAmount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getCancelAmount(map);
	}
	/**
	 * 采购订单对应上月销售订单明细数量
	 */
	@Override
	public List<TaoBaoOrderInfo> getCancelAmountCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getCancelAmountCount(map);
	}
	/**
	 * 采购订单对应取消销售订单明细
	 */
	@Override
	public List<TaoBaoOrderInfo> getLastAmount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getLastAmount(map);
	}
	/**
	 * 采购订单对应取消销售订单明细数量
	 */
	@Override
	public List<TaoBaoOrderInfo> getLastAmountCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getLastAmountCount(map);
	}
	/**
	 * 抓取正常采购订单明细
	 */
	@Override
	public List<TaoBaoOrderInfo> getGrabNormalAmount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getGrabNormalAmount(map);
	}
	/**
	 * 抓取正常采购订单明细数量
	 */
	@Override
	public List<TaoBaoOrderInfo> getGrabNormalAmountCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.getGrabNormalAmountCount(map);
	}
	/**
	 * 预估订单运费明细
	 */
	@Override
	public List<ShippingPackage> getforecastAmount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getforecastAmount(map);
	}
	/**
	 * 预估订单运费明细数量
	 */
	@Override
	public List<ShippingPackage> getforecastAmountCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getforecastAmountCount(map);
	}
	/**
	 *  实际支付运费运单明细
	 */
	@Override
	public List<Shipments> getPayFreight(Map<Object, Object> map) {

		return taoBaoOrderMapper.getPayFreight(map);
	}
	/**
	 *  实际支付运费运单明细数量
	 */
	@Override
	public List<Shipments> getPayFreightCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.getPayFreightCount(map);
	}
	/**
	 * 根据类别ID获取类别名称
	 * @param ids 类别id
	 * @return 类别名称
	 * @author 王宏杰 2017-07-27
	 */
	@Override
	public String queryUsingRangeForIds(String ids) {

		return taoBaoOrderMapper.queryUsingRangeForIds(ids);
	}

	@Override
	public int searchCoupusDetailsCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.searchCoupusDetailsCount(map);
	}

	@Override
	public int searchCoupusManagementCount(Map<Object, Object> map) {

		return taoBaoOrderMapper.searchCoupusManagementCount(map);
	}

	/**
	 * 查询当天最后生成的批次
	 * time 当天日期格式
	 * @return
	 */
	@Override
	public String queryOldBatch(String time) {

		return taoBaoOrderMapper.queryOldBatch(time);
	}

	/**
	 * 创建优惠券
	 * @param map
	 * @return
	 * 2017-06-26 whj
	 */
	@Override
	public int createCoupons(Map<Object, Object> map) {

		return taoBaoOrderMapper.createCoupons(map);
	}

	@Override
	public int delCoupons(int id) {

		return taoBaoOrderMapper.delCoupons(id);
	}

	@Override
	public List<AliCategory> queryUsingRange() {

		return taoBaoOrderMapper.queryUsingRange();
	}

	@Override
	public int getCouponsId(String batch) {

		return taoBaoOrderMapper.getCouponsId(batch);
	}

	/**
	 * 批量生成优惠券详情
	 */
	@Override
	public int createCouponSubsidiary(List<Map<String, Object>> list) {

		return taoBaoOrderMapper.createCouponSubsidiary(list);
	}

	@Override
	public List<Inventory> searchGoodsInventoryDeleteInfo(
			Map<Object, Object> map) {
		List<Inventory> toryList=taoBaoOrderMapper.searchGoodsInventoryDeleteInfo(map);
		if("0".equals(map.get("export"))){
			for (Inventory inventory : toryList) {
				if (inventory.getFlag() == 1) {
					if(map.get("flag")==null || "".equals(map.get("flag"))){
						inventory.setOperation(("<a style='color:red' id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getNew_remaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark()) + "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>").replaceAll("\n", ""));
					}else{
						inventory.setOperation(("<a id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getNew_remaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark()) + "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>").replaceAll("\n", ""));
					}
				} else {
					inventory.setNew_remaining("");
					inventory.setOperation(("<a id='pd_"+inventory.getId()+"' onclick=\"update_inventory("+inventory.getFlag()+","+inventory.getId()+",'" + inventory.getBarcode()+ "','" + inventory.getRemaining() + "','" + (StringUtils.isStrNull(inventory.getRemark())?"":inventory.getRemark())+ "')\">盘点</a>|<a onclick=\"delete_inventory("+inventory.getId()+",'"+inventory.getGoods_pid()+"','"+(StringUtils.isStrNull(inventory.getNew_barcode())?inventory.getBarcode():inventory.getNew_barcode())+"','"+(inventory.getNew_inventory_amount()>0?inventory.getNew_inventory_amount():inventory.getInventory_amount())+"')\">删除</a>").replaceAll("\n", ""));
				}
				// }
				String url="";
				if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && inventory.getCar_urlMD5().startsWith("A")){
					url="https://www.import-express.com/goodsinfo/"+inventory.getGood_name()+"-2"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && inventory.getCar_urlMD5().startsWith("D")){
					url="https://www.import-express.com/goodsinfo/"+inventory.getGood_name()+"-1"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getCar_urlMD5()) && inventory.getCar_urlMD5().startsWith("N")){
					url="https://www.import-express.com/goodsinfo/"+inventory.getGood_name()+"-3"+inventory.getGoods_pid()+".html";
				}else if(!StringUtils.isStrNull(inventory.getGoods_url()) && inventory.getGoods_url().contains("ali")){
					url="https://www.import-express.com/goodsinfo/"+inventory.getGood_name()+"-2"+inventory.getGoods_pid()+".html";
				}
				inventory.setCar_img("<a href='"+url+"' target='_blank'><img  src='"+ (inventory.getCar_img().indexOf("1.png")>-1?"/cbtconsole/img/yuanfeihang/loaderTwo.gif":inventory.getCar_img()) + "' onmouseout=\"closeBigImg();\" onmouseover=\"BigImg('"+ inventory.getCar_img() + "')\" height='100' width='100'></a>");
				if(!StringUtils.isStrNull(inventory.getGoods_p_url())){
					url=inventory.getGoods_p_url();
				}
				inventory.setGood_name("<a href='"+url+"' target='_blank'>"
						+ inventory.getGood_name().substring(0, inventory.getGood_name().length() / 3) + "</a>");
			}
		}
		return toryList;
	}

	@Override
	public List<Inventory> searchGoodsInventoryDeleteInfoCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.searchGoodsInventoryDeleteInfoCount(map);
	}
	/**
	 * 查询库存盘点记录
	 */
	@Override
	public List<Inventory> searchGoodsInventoryUpdateInfo(
			Map<Object, Object> map) {
		List<Inventory> list=taoBaoOrderMapper.searchGoodsInventoryUpdateInfo(map);
		for (Inventory i : list) {
			i.setGood_name("<a href='"+i.getGoods_url()+"' target='_blank' title='"+i.getGood_name()+"'>"+(i.getGood_name().length()>15?i.getGood_name().substring(0,15):i.getGood_name())+"</a>");
			i.setCar_img("<a href='"+i.getGoods_p_url()+"' target='_blank' title='采购链接'><img src='"+i.getCar_img()+"' height='100' width='100'></a>");
		}
		return list;
	}
	@Override
	public List<Orderinfo> searchSampleProduct(Map<Object, Object> map) {
		List<Orderinfo> list= taoBaoOrderMapper.searchSampleProduct(map);
		for (Orderinfo o : list) {
			o.setOrderNo("<a href='/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=999&orderno="+o.getOrderNo()+"&days=999&unpaid=0&pagesize=50&orderarrs=0&search_state=0' target='_blank' title='跳转到订单采购页面'>"+o.getOrderNo()+"</a>");
		}
		return list;
	}
	@Override
	public List<Orderinfo> searchSampleProductCount(Map<Object, Object> map) {
		List<Orderinfo> list= taoBaoOrderMapper.searchSampleProductCount(map);
		return list;
	}
	/**
	 * 查询库存盘点记录总数
	 */
	@Override
	public List<Inventory> searchGoodsInventoryUpdateInfoCount(
			Map<Object, Object> map) {

		return taoBaoOrderMapper.searchGoodsInventoryUpdateInfoCount(map);
	}


	/**
	 * 添加未入库采购订单备注
	 */
	@Override
	public int updateReply(Map<Object, Object> map) {

		return taoBaoOrderMapper.updateReply(map);
	}

	/**
	 * 强制入库
	 */
	@Override
	public int inStorage(Map<Object, Object> map) {

		return taoBaoOrderMapper.inStorage(map);
	}
	@Override
	public String getOperationRemark(Map<Object, Object> map) {

		return taoBaoOrderMapper.getOperationRemark(map);
	}
	@Override
	public List<TaoBaoOrderInfo> queryBuyCount(String torderid,
	                                           String opsorderid) {

		return taoBaoOrderMapper.queryBuyCount(torderid,opsorderid);
	}


	@Override
	public List<OrderProductSource> getSourceValidation(String buyer, String account, int page, String startdate, String enddate) {

		return taoBaoOrderMapper.getSourceValidation(buyer,account,page,startdate,enddate);
	}

	@Override
	public String getTbOrderRemark(String id) {

		return taoBaoOrderMapper.getTbOrderRemark(id);
	}

	@Override
	public int addTbOrderRemark(String id, String new_remark) {

		return taoBaoOrderMapper.addTbOrderRemark(id,new_remark);
	}

	@Override
	public List<TaoBaoOrderInfo> getSourceValidationForTb(String buyer,
	                                                      String account, int page,String startdate,String enddate,String isTrack) {
		List<TaoBaoOrderInfo> list=taoBaoOrderMapper.getSourceValidationForTb(buyer,account,page,startdate,enddate,isTrack);
		for(int i=0;i<list.size();i++){
			TaoBaoOrderInfo t=list.get(i);
			List<OrderProductSource> o_list=taoBaoOrderMapper.getOrderProductSource(t.getItemid(),t.getOrderdate());
			if(o_list.size()==1){
				t.setOpsorderid(o_list.get(0).getOrderid());
				t.setGoodsImgUrl(o_list.get(0).getGoodsImgUrl());
			}else if(o_list.size()>1){
				t.setGoodsImgUrl(o_list.get(0).getGoodsImgUrl());
				StringBuilder sb=new StringBuilder();
				for(int j=0;j<o_list.size();j++){
					sb.append(o_list.get(j).getOrderid()).append(",");
				}
				t.setOpsorderid(sb.toString());
			}else{
				t.setOpsorderid("");
				t.setGoodsImgUrl("");
			}
		}
		return list;
	}

	@Override
	public int getSourceValidationForTbCount(String buyer,
	                                         String account, int page,String startdate,String enddate,String isTrack) {

		return taoBaoOrderMapper.getSourceValidationForTbCount(buyer,account,page,startdate,enddate,isTrack);
	}

	@Override
	public String getBuyerAmountByMouth(String account,String startdate,String enddate) {

		return taoBaoOrderMapper.getBuyerAmountByMouth(account,startdate,enddate);
	}

	@Override
	public List<TaoBaoOrderInfo> getTaoBaoNoInspection(Map<String, String> map) {
		List<TaoBaoOrderInfo> list=taoBaoOrderMapper.getTaoBaoNoInspection(map);
		for(int i=0;i<list.size();i++){
			TaoBaoOrderInfo t=list.get(i);
			//查看该1688订单是否入库
			int id_state=taoBaoOrderMapper.getIdState(t.getOrderid());
			String inspection_result="未验货";
			String state_msg="未入库";
			if(id_state>0){
				state_msg="已入库";
				String in_result=taoBaoOrderMapper.getInspectionResult(t.getOrderid(),t.getItemid());
				if(StringUtil.isNotBlank(in_result) && in_result.split(",")[1].equals("1")){
					inspection_result="<span style='color:green'>验货无误</span>";
				}else if(StringUtil.isNotBlank(in_result) && in_result.split(",")[1].equals("0") && !"1".equals(in_result.split(",")[1])){
					inspection_result="<span style='color:red'>验货有问题</span>";
				}
			}
			t.setInspection_result(inspection_result);
			t.setId_state(state_msg);
			t.setSample_goods("未知");
			String return_goods="<span style='color:green'>未退货</span>";
			if(t.getShipstatus().contains("退款")){
				return_goods="<span stype='color:red'>退货商品</span>";
			}
			t.setReturn_goods(return_goods);
		}
		return list;
	}

	@Override
	public List<TaoBaoOrderInfo> getTaoBaoNoInspectionCount(Map<String, String> map) {
		return taoBaoOrderMapper.getTaoBaoNoInspectionCount(map);
	}

	@Override
	public List<OrderProductSource> getNopurchaseDistribution(Map<String, String> map) {
		return taoBaoOrderMapper.getNopurchaseDistribution(map);
	}

	@Override
	public List<OrderProductSource> getNopurchaseDistributionCount(Map<String, String> map) {
		return taoBaoOrderMapper.getNopurchaseDistributionCount(map);
	}

	@Override
	public List<OrderProductSource> getOneMathchMoreOrderInfo(Map<String,String> map) {
		List<OrderProductSource> list=taoBaoOrderMapper.getOneMathchMoreOrderInfo(map);
		for(int i=0;i<list.size();i++){
			OrderProductSource o=list.get(i);
			String shipnos=o.getShipno()+o.getRe_shipnos();
			if(StringUtil.isNotBlank(shipnos)){
				String [] str=shipnos.split(",");
				StringBuilder sb=new StringBuilder();
				sb.append("(");
				for(int j=0;j<str.length;j++){
					if(j==str.length-1){
						sb.append("'").append(str[j]).append("'");
					}else{
						sb.append("'").append(str[j]).append("',");
					}
				}
				sb.append(")");
				//根据入库时记录的运单号查询1688订单好
				String tbOrderid=taoBaoOrderMapper.getBuyOrderId(sb.toString());
				o.setBuyerOrderid(tbOrderid);
			}
		}
		return list;
	}

	@Override
	public List<OrderProductSource> getOneMathchMoreOrderInfoCount(Map<String,String> map) {
		return taoBaoOrderMapper.getOneMathchMoreOrderInfoCount(map);
	}

	@Override
	public int getCount(String buyer,String startdate,String enddate) {

		return taoBaoOrderMapper.getCount(buyer,startdate,enddate);
	}

	@Override
	public List<TaoBaoOrderInfo> getAllBuyerOrderInfo(Map<String, String> map) {
		return taoBaoOrderMapper.getAllBuyerOrderInfo(map);
	}

	@Override
	public List<TaoBaoOrderInfo> getAllBuyerOrderInfoCount(Map<String, String> map) {
		return taoBaoOrderMapper.getAllBuyerOrderInfoCount(map);
	}

	@Override
	public int getCounts(String account,String startdate,String enddate) {

		return taoBaoOrderMapper.getCounts(account,startdate,enddate);
	}

	@Override
	public int getSourceValidationCount(String buyer,String account,int page,String startdate,String enddate) {

		return taoBaoOrderMapper.getSourceValidationCount(buyer,account,page,startdate,enddate);
	}
	@Override
	public List<Inventory> getAllInventory() {
		return taoBaoOrderMapper.getAllInventory();
	}

	@Override
	public List<String> getNewBarcode() {

		return taoBaoOrderMapper.getNewBarcode();
	}
	/**
	 * 更新库存
	 * @param map
	 * @return
	 */
	@Override
	public int updateInventory(Map<Object, Object> map) {

		return taoBaoOrderMapper.updateInventory(map);
	}

}