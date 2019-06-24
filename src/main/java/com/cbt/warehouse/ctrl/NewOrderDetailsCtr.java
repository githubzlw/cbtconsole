package com.cbt.warehouse.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.cbt.auto.service.IOrderAutoService;
import com.cbt.auto.service.PreOrderAutoService;
import com.cbt.bean.*;
import com.cbt.change.util.ChangeRecordsDao;
import com.cbt.change.util.CheckCanUpdateUtil;
import com.cbt.change.util.ErrorLogDao;
import com.cbt.change.util.OnlineOrderInfoDao;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.dao.IRechargeRecordSSMDao;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.parse.service.StrUtils;
import com.cbt.pay.service.IPayServer;
import com.cbt.pay.service.PayServer;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.processes.service.UserServer;
import com.cbt.report.service.TabTransitFreightinfoUniteNewExample;
import com.cbt.util.*;
import com.cbt.warehouse.service.GoodsCommentsService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.bean.PaymentDetails;
import com.cbt.website.bean.QualityResult;
import com.cbt.website.dao.*;
import com.cbt.website.service.*;
import com.cbt.website.util.JsonResult;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.pojo.PurchaseInfoBean;
import com.importExpress.service.IPurchaseService;
import com.importExpress.service.OrderCancelApprovalService;
import com.importExpress.service.PaymentServiceNew;
import com.importExpress.utli.FreightUtlity;
import com.importExpress.utli.NotifyToCustomerUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Controller
@RequestMapping("/orderDetails")
public class NewOrderDetailsCtr {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(NewOrderDetailsCtr.class);
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private IRechargeRecordSSMDao rechargeRecordDao;
	@Autowired
	private GoodsCommentsService goodsCommentsService;

	@Autowired
	private IPurchaseService iPurchaseService;
	@Autowired
	private SendMailFactory sendMailFactory;

	@Autowired
    private OrderCancelApprovalService approvalService;

	@Autowired
	private PaymentServiceNew paymentServiceNew;
	/**
	/**
	 * 根据订单号获取订单详情
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/queryByOrderNo.do")
	public String queryByOrderNo(HttpServletRequest request, HttpServletResponse response) {
		DataSourceSelector.restore();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df = new DecimalFormat("######0.00");
		//try {
			String orderNo = request.getParameter("orderNo");
			if(StringUtils.isNotBlank(orderNo)){
				orderNo = orderNo.replaceAll("'","");
			}
			String payTime = request.getParameter("paytime");
			request.setAttribute("payToTime", payTime);
			// 获取所有采购人员信息
			request.setAttribute("aublist", net.sf.json.JSONArray.fromObject(iOrderinfoService.getBuyerAndAll()));

//			String allFreight = String.valueOf(iOrderinfoService.getAllFreightByOrderid(orderNo));
			// 订单信息
			OrderBean orderInfo =iOrderinfoService.getOrders(orderNo);
			//获取实际运费
			Long start = System.currentTimeMillis();
			double freightCostByOrderno = FreightUtlity.getFreightByOrderno(orderNo);
			System.out.println("花费时间： = " + (System.currentTimeMillis() - start));
			// 获取汇率
			double rate =Double.valueOf(orderInfo.getExchange_rate());
			//获取订单出运信息
			List<ShippingBean> spb=iOrderinfoService.getShipPackmentInfo(orderNo);
			String actual_freight="654321";
			String transportcompany="-";
			String shippingtype="-";
			String awes_freight="-";
			Double actualFreight=0.00;
			Double awesFreight=0.00;
			//eric称重重量包裹
			Double ac_weight=0.00;
			if(spb .size()>0){
				for(ShippingBean s:spb){
					if(s != null && StringUtil.isNotBlank(s.getTransportcompany())){
						actualFreight += StringUtil.isBlank(s.getActual_freight())?0.00:Double.parseDouble(s.getActual_freight());
						if(!transportcompany.contains(s.getTransportcompany())){
							transportcompany =transportcompany+";"+(StringUtil.isBlank(s.getTransportcompany())?"":s.getTransportcompany());
						}
						if(!shippingtype.contains(s.getShippingtype())){
							shippingtype=shippingtype+";"+(StringUtil.isBlank(s.getShippingtype())?"":s.getShippingtype());
						}
						awesFreight=awesFreight+Double.parseDouble(s.getEstimatefreight())*rate;
						ac_weight+=Double.parseDouble(StringUtil.isBlank(s.getAc_weight())?"0.00":s.getAc_weight());
					}
				}
				actual_freight=df.format(actualFreight);
				awes_freight=df.format(awesFreight);
			}
			//获取预估运费 start
			double freightFee =orderInfo.getFreightFee();
			//getFreightFee(allFreight, orderInfo);
			// 获取预估运费 end
			//出运国际运费（公式计算）
			double estimatefreight=iOrderinfoService.getEstimatefreight(orderNo);
			//获取产品表中公式计算的重量
			double allWeight=iOrderinfoService.getAllWeight(orderNo);
			request.setAttribute("actual_freight",actual_freight);
			request.setAttribute("transportcompany",transportcompany);
			request.setAttribute("shippingtype",shippingtype);
			request.setAttribute("ac_weight",ac_weight);
			request.setAttribute("awes_freight",awes_freight);
			//request.setAttribute("allFreight", freightFee);
			if(freightCostByOrderno>0){
				request.setAttribute("allFreight", freightCostByOrderno);
			}else {
				request.setAttribute("allFreight", freightFee);
			}
			request.setAttribute("estimatefreight",estimatefreight*rate);
			request.setAttribute("allWeight",allWeight);
			//产品预计采购金额-
			double es_buyAmount=0.00;
			request.setAttribute("es_buyAmount",es_buyAmount);
			String fileByOrderid =orderInfo.getFileByOrderid();// iOrderinfoService.getFileByOrderid(orderNo);
			String invoice="2";
			if (fileByOrderid == null || fileByOrderid.length() < 10) {
				invoice="0";
			} else if (fileByOrderid.indexOf(".pdf") > -1){
				invoice="1";
			}
			request.setAttribute("invoice", invoice);
			// 更改购物车表的商品备注为已读
			iOrderinfoService.updateGoodsCarMessage(orderNo);
			// 订单商品详情
			List<OrderDetailsBean> odb=iOrderinfoService.getOrdersDetails(orderNo);
			Double es_prices=0.00;
			double feeWeight=0.00;
			Map<String,Double> shopShippingCostMap= new HashedMap();
			Double ShippingCost = 0d;
			shopShippingCostMap.put("shopShippingCost",0d);
			for (int i = 0; i < odb.size(); i++) {
				OrderDetailsBean o = odb.get(i);
				if(!shopShippingCostMap.containsKey(o.getCbrShopid())){
					Double shopShippingCost = shopShippingCostMap.get("shopShippingCost");
					shopShippingCostMap.put("shopShippingCost",shopShippingCost+=5d);
					shopShippingCostMap.put(o.getCbrShopid(),shopShippingCost+=5d);
				}
				if (StringUtil.isNotBlank(o.getConfirm_userid()) && Integer.valueOf(o.getPurchase_state())>2) {
					//对于已经确认采购的商品查询是否有物流信息
					getShippStatus(o);
				}
				es_prices+=o.getEs_price();
				if(o.getIs_sold_flag() != 0){
					feeWeight+=o.getOd_total_weight();
				}
			}
			request.setAttribute("feeWeight",df.format(feeWeight));
			Forwarder forw = null;
			int state = orderInfo.getState();
			// zp 修改 到账信息和订单信息一起查
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", orderInfo.getDzId());
			map.put("orderno", orderInfo.getDzOrderno());
			map.put("confirmname", orderInfo.getDzConfirmname());
			map.put("confirmtime", orderInfo.getDzConfirmtime());
			// 收集order_detail的 id
			String str_oid = "";
			// 计算订单总金额
			float sumPrice = 0;
			String purchasetime_order = "";
			double preferential_price = 0;
			for (int i = 0; i < odb.size(); i++) {
				if (state == 1) {
					preferential_price += odb.get(i).getPreferential_price();
					String purchase_time = odb.get(i).getPurchase_time();
					if (Utility.getStringIsNull(purchase_time) && Utility.getStringIsNull(purchasetime_order)) {
						long purchase_time_long = Long.valueOf(purchase_time.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
						long purchasetime_order_long = Long.valueOf(purchasetime_order.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
						purchasetime_order = purchase_time_long > purchasetime_order_long ? purchase_time: purchasetime_order;
					}else {
						purchasetime_order = purchase_time;
					}
				}
				if (odb.get(i).getOrsstate() > 0) {
					sumPrice += odb.get(i).getSumGoods_p_price();
				}
				str_oid = str_oid + odb.get(i).getId() + ",";
				String goods_img = odb.get(i).getGoods_img();
				if (goods_img != null && goods_img.equals("/img/1.png")) {
					odb.get(i).setGoods_img("/cbtconsole/img/1.png");
				}
				if(StringUtil.isNotBlank(odb.get(i).getPrice1688())){
					odb.get(i).setPrice1688(odb.get(i).getPrice1688().replaceAll("\\s*", "").replaceAll("\\$", "￥"));
				}
			}
			String arrive_time1 = "";
			String arrive_time = orderInfo.getMode_transport();
			int arrive = 0;
			String shipMethod = "0";
			if (!Utility.getStringIsNull(orderInfo.getExpect_arrive_time())) {
				arrive_time1 = orderInfo.getExpect_arrive_time();
				arrive = -1;
			}
			if (Utility.getStringIsNull(arrive_time) && arrive_time.indexOf("@") > -1) {
				String[] strings = arrive_time.split("@");
				arrive_time = strings[1];
				shipMethod = strings[0];
				if (arrive_time.indexOf("-") > -1) {
					String[] arrive_time_ = arrive_time.split("-");
					int max = Utility.getIsInt(arrive_time_[1]) ? Integer.parseInt(arrive_time_[1]) : 0;
					arrive = max;
				} else {
					arrive = Utility.getIsInt(arrive_time) ? Integer.parseInt(arrive_time) : 0;
				}
				if (arrive != 0 && arrive != -1) {
					Calendar c = Calendar.getInstance();
					if (Utility.getStringIsNull(orderInfo.getTransport_time())) {
						Date parse = null;
						try {
							parse = sf.parse(orderInfo.getTransport_time());
						} catch (ParseException e) {
							e.printStackTrace();
							LOG.error("订单详情：",orderNo+e.getMessage());
						}
						c.setTime(parse);
					}
					c.add(Calendar.DAY_OF_MONTH, arrive);
					arrive_time = sf.format(c.getTime());
				}
			}else {
				arrive_time = "";
			}
			List<Map<String, String>> pays =iOrderinfoService.getOrdersPays(orderNo);
			request.setAttribute("pays", pays);
			if (state == 3) {
				forw=iOrderinfoService.getForwarder(orderNo);
				// 读取物流信息
				List<CodeMaster> logisticsList=iOrderinfoService.getLogisticsInfo();
				request.setAttribute("logisticsList", logisticsList);
			} else if (state == 4) {
				Evaluate evaluate =iOrderinfoService.getEvaluate(orderNo);
				request.setAttribute("evaluate", evaluate);
			} else if (state != 0 || state != -1 || state != 7) {
				String pay_time = "";
				for (int i = 0; i < pays.size(); i++) {
					String order_pay = pays.get(i).get("orderid");
					if (orderNo.equals(order_pay)) {
						pay_time = String.valueOf(pays.get(i).get("createtime"));
					}
				}
				if (Utility.getStringIsNull(pay_time)) {
					// 支付时间+最长国内交期+国际运输时间<当前时间
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(sf.parse(pay_time));
					} catch (ParseException e) {
						e.printStackTrace();
						LOG.error("订单详情：",orderNo+e.getMessage());
					}
					if (state == 5) {
						c.add(Calendar.DAY_OF_MONTH, 2);
						if (System.currentTimeMillis() > c.getTime().getTime()) {
							request.setAttribute("delivery_warning", "(采购预警" + sf.format(c.getTime()) + ")");
						}
					} else if (state == 1) {
						c.add(Calendar.DAY_OF_MONTH, orderInfo.getDeliveryTime() - 2);
						if (System.currentTimeMillis() > c.getTime().getTime()) {
							request.setAttribute("delivery_warning", "(入库预警" + sf.format(c.getTime()) + ")");
						}
					} else if (state == 2) {
						c.add(Calendar.DAY_OF_MONTH, orderInfo.getDeliveryTime());
						if (System.currentTimeMillis() > c.getTime().getTime()) {
							request.setAttribute("delivery_warning", "(出运预警" + sf.format(c.getTime()) + ")");
						}
					}
				}
			}
			if (arrive != 0) {
				request.setAttribute("expect_arrive_time", arrive == -1 ? arrive_time1 : arrive_time);
			}
			if (str_oid.length() > 0) {
				str_oid = str_oid.substring(0, str_oid.length() - 1);
			}
			request.setAttribute("str_oid", str_oid);
			request.setAttribute("shipMethod", shipMethod);
			request.setAttribute("orderNo", orderNo);
			request.setAttribute("orderDetail", odb);
			request.setAttribute("order_state", orderInfo.getState());
			IZoneServer os = new ZoneServer();
			request.setAttribute("countryList",os.getAllZone());
		// 实际运 费
			Double actual_ffreight_ = Utility.getIsDouble(orderInfo.getActual_ffreight())? Double.parseDouble(orderInfo.getActual_ffreight()) : 0;
			request.setAttribute("actual_ffreight_", actual_ffreight_);
			request.setAttribute("foreign_freight", StringUtil.isNotBlank(orderInfo.getForeign_freight())?Double.parseDouble(orderInfo.getForeign_freight()):0.00);
			request.setAttribute("service_fee",StringUtil.isNotBlank(orderInfo.getService_fee())?Double.parseDouble(orderInfo.getService_fee()):0.00);
			request.setAttribute("actual_lwh",StringUtil.isNotBlank(orderInfo.getActual_lwh())?Double.parseDouble(orderInfo.getActual_lwh()):0.00);
			request.setAttribute("firstdiscount",StringUtil.isNotBlank(orderInfo.getFirstdiscount())?Double.parseDouble(orderInfo.getFirstdiscount()):0.00);
			// service_fee是从数据表读取的 test
			request.setAttribute("service_fee", Utility.getIsDouble(orderInfo.getService_fee())? Double.parseDouble(orderInfo.getService_fee()) : 0);
			request.setAttribute("cashback", orderInfo.getCashback());
			String mode_transport = "";
			int isFree = 0;
			if (Utility.getStringIsNull(orderInfo.getMode_transport())) {
				String[] mode_transport_ = orderInfo.getMode_transport().split("@");
				int transportLength=mode_transport_.length;
				for (int j = 0; j < transportLength; j++) {
					int afterLength=transportLength - 2;
					if (j == afterLength && Utility.getIsDouble(mode_transport_[afterLength]) && Double.parseDouble(mode_transport_[afterLength]) == 0) {
						if (mode_transport_[transportLength - 1].equals("all")) {
							mode_transport_[transportLength - 1] = "免邮";
							isFree = 1;
						} else {
							mode_transport_[transportLength - 1] = "不知重量";
							isFree = 1;
						}
					}
					if (j == transportLength - 1 && mode_transport_[j].equals("product")) {
						mode_transport_[j] = "未付运费";
					}else if (j == transportLength - 1 && mode_transport_[j].equals("all") && isFree != 1) {
						mode_transport_[j] = "全付";
					}
					mode_transport += mode_transport_[j];
					if (mode_transport.indexOf(".") > -1) {
						mode_transport = mode_transport.split("\\.")[0];
					}
					if (j != transportLength - 1)
						mode_transport += "@";
				}
			}
			request.setAttribute("isSplitOrder", orderInfo.getIsDropshipOrder());
			orderInfo.setApplicable_credit(new BigDecimal(orderInfo.getApplicable_credit()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			// 订单批量优惠总
			request.setAttribute("preferential_price", preferential_price);
			request.setAttribute("sumPrice", sumPrice);
			// 订单采购时间
			request.setAttribute("purchasetime_order", purchasetime_order);
			// 查询用户相关订单->同一客户，还没出货的 订单
			request.setAttribute("orderNos", iOrderinfoService.getOrderNos(orderInfo.getUserid(),orderNo));

			// 2019-6-5 支付成功页面用户勾选的是否先发送样品: 是(先发送样品)
            request.setAttribute("sampleschoice", iOrderinfoService.getSampleschoice(orderNo));

            // 判断订单时候有替代产品
			int count = orderInfo.getPackage_style();
			request.setAttribute("count", count);
			/* 抵扣赠送运费操作---2016.7.25.abc---start */
			// 订单在未付运费的情况下和全付的时候后期需要增加运费的情况下
			if (orderInfo.getState() == 2) {
				double unpaid_freight = 0.0;
				if (mode_transport.indexOf("未付运费") > -1 || mode_transport.indexOf("全付") > -1) {
					unpaid_freight = orderInfo.getRemaining_price();
				}
				// 客户还需付款
				if (orderInfo.getRemaining_price() > 0) {
					request.setAttribute("unpaid_freight", unpaid_freight);
					request.setAttribute("freight_deduction", "1");
				}
			}
			/* 抵扣赠送运费操作---2016.7.25.abc----end */
			request.setAttribute("mode_transport", mode_transport);
			request.setAttribute("isFree", isFree);
			request.setAttribute("forwarder", forw);
			request.setAttribute("paymentconfirm", map);
			// 7.21 订单详情页面增加利润率
			String[] strs = mode_transport.split("@");
			int jq = 0;
			if (strs.length > 1 && strs[1].indexOf("-") > 0) {
				String[] jqs = null;
				if (strs[1].indexOf("Days") > 0) {
					jqs = strs[1].substring(0, strs[1].indexOf(" Days")).split("-");
				} else {
					jqs = strs[1].split("-");
				}
				for (int j = 0; j < jqs.length; j++) {
					jq += Integer.parseInt(jqs[j]);
				}
			}
			double sale =orderInfo.getPay_price() * rate;
			if(orderInfo.getMemberFee()>10){
				sale-=orderInfo.getMemberFee()* rate;
			}
			double buy = 0.0;
			double volume = 0.0;
			double weight = 0.0;
			double goodsWeight=0.00;
			for (int i = 0; i < odb.size(); i++) {
				if (odb.get(i).getState() != 2) {
					buy += odb.get(i).getSumGoods_p_price();
					//volume += odb.get(i).getOd_bulk_volume();
					weight += odb.get(i).getOd_total_weight();
					if(StringUtil.isNotBlank(odb.get(i).getFinal_weight())){
						goodsWeight+=Double.parseDouble(odb.get(i).getFinal_weight());
					}
				}
			}
			double pid_amount=0.00;
			if(odb.size()>0){
				pid_amount=odb.get(0).getPid_amount();
			}
			pid_amount = shopShippingCostMap.get("shopShippingCost");
//			if(buy<=0){
//				pid_amount=0.00;
//			}
			//订单商品总重量
			orderInfo.setVolumeweight(String.valueOf(Math.round(weight * 1000) / 1000.0));
			request.setAttribute("order", orderInfo);
			request.setAttribute("avg_jq", jq / 2);
			request.setAttribute("sale", Math.round(sale * 100) / 100.0);
			request.setAttribute("buy", Math.round((buy) * 100) / 100.0);
			request.setAttribute("volume", Math.round(volume * 1000) / 1000.0);
			request.setAttribute("weight", Math.round(weight * 1000) / 1000.0);
			request.setAttribute("piaAmount",pid_amount);
			request.setAttribute("es_prices",es_prices);
			request.setAttribute("goodsWeight",goodsWeight);
			String countryid = "";
			if (odb.size() != 0 && odb != null) {
				countryid = odb.get(0).getCountry();
				if ("42".equals(countryid)|| "43".equals(countryid)) {
					countryid = "36";
				}
			}
			request.setAttribute("country", countryid);
			request.setAttribute("countryName",(orderInfo.getMode_transport() == null || "".equals(orderInfo.getMode_transport())) ? "USA": strs[2]);
			request.setAttribute("orderNo", orderInfo.getOrderNo());
			request.setAttribute("rate", rate);
			request.setAttribute("userid", orderInfo.getUserid());
			
			//对同地址不同账号客户进行警告
			request.setAttribute("userIds", iOrderinfoService.getSameAdrDifAccount(orderInfo.getUserid(),
					StrUtils.matchStr(orderInfo.getAddress().getAddress(), "(\\d+(\\.\\d+){0,1})"),
					StrUtils.matchStr(orderInfo.getAddress().getStreet(),"(\\d+(\\.\\d+){0,1})"),
					orderInfo.getAddress().getZip_code(),countryid,orderInfo.getAddress().getAddress2(),
					orderInfo.getAddress().getRecipients()));
			
			
			String lirun1 = null;
			if (sale == 0.0 || buy == 0.0) {
				lirun1 = "--";
			} else {
				lirun1 = String.format("%.2f", ((sale - buy) / sale * 100)) + "%";
			}
			request.setAttribute("lirun1", lirun1);
			request.setAttribute("isDropshipOrder", orderInfo.getIsDropshipOrder());
			// 查看订单对应邮件
//			IEmailReceiveService emailService = new EmailReceiveServiceImpl();
			// 查看客户来往邮件
//			List<EmailReceive1> emaillist = iOrderinfoService.getall(orderNo);
//			request.setAttribute("emaillist", emaillist);
			//用户信息    yyl - start
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser admuserinfo = JSONObject.parseObject(admuserJson, Admuser.class);
			request.setAttribute("admuserinfo", admuserinfo);
			//根据产品id 和 订单号 去查询销售评论状态
			List<String> lists = new ArrayList<String>();
			for (OrderDetailsBean orderDetailsBean : odb) {
				lists.add(orderDetailsBean.getGoods_pid());
			}
			request.setAttribute("lists", lists);
			request.setAttribute("isDropFlag",0);
		//} catch (Exception e) {
			/*e.printStackTrace();
			LOG.error("查询详情失败，原因：" + e.getMessage());*/
		//}
		return "order_details_new";
	}

	@RequestMapping("/getIpnaddress")
	@ResponseBody
	public Map<String, String> getIpnaddress(HttpServletRequest request, HttpServletResponse response){
		UserDao dao = new UserDaoImpl();
		Map<String, String> map=new HashMap<String, String>();
		String orderid = request.getParameter("orderid");
		map = dao.getIpnaddress(orderid);
		map.put("orderid",orderid);
		return map;
	}

	/**
	 * 对于已经确认采购的商品查询是否有物流信息
	 * @param o
	 */
	private void getShippStatus(OrderDetailsBean o) {
		String admName=iOrderinfoService.queryBuyCount(Integer.valueOf(o.getConfirm_userid()));
		TaoBaoOrderInfo t=iOrderinfoService.getShipStatusInfo(o.getTb_1688_itemid(), o.getLast_tb_1688_itemid(),
				o.getConfirm_time(), admName,o.getShipno(),o.getOffline_purchase(),o.getOrderid(),o.getGoodsid());
		if (t != null && t.getShipstatus() != null && t.getShipstatus().length() > 0) {
			String shipstatus = t.getShipstatus().split("\n")["2".equals(t.getTbOr1688())?0:t.getShipstatus().split("\n").length - 1];
			if("2".equals(t.getTbOr1688()) && !"等待买家确认收货".equals(shipstatus)){
				String msg=t.getShipstatus().split("\n")[1];
				o.setShipstatus(shipstatus+"\n "+msg);
			}else{
				o.setShipstatus(shipstatus);
			}
			o.setShipno(t.getShipno());
		}
	}

	/**
	 * @Title: getFreightFee
	 * @Author: cjc
	 * @Despricetion:TODO 获取预估运费
	 * @Date: 2018/6/12 18:03
	 * @Param: [allFreight, orderInfo]
	 * @Return: double
	 */
	public double getFreightFee(String allFreight,  OrderBean orderInfo) {
		double esFreight=0.00;
		String modeTransport = iOrderinfoService.getModeTransport(orderInfo.getOrderNo());
		//getOrders(orderInfo.getOrderNo());
//		String modeTransport=info.getMode_transport();
		if(StringUtil.isBlank(modeTransport)){
			return 0.00;
		}
		String [] strs=modeTransport.split("@");
		String type=strs[0];
		if(StringUtil.isBlank(type) || "null".equals(type)){
			type="Epacket";
		}
		type=type.toLowerCase();
		String country="";
		//特殊商品
		double weight1=0.00;
		//普通商品
		double weight2=0.00;
		List<OrderDetailsBean> odList=iOrderinfoService.getCatidDetails(orderInfo.getOrderNo());
		for(OrderDetailsBean odd:odList){
			String catid=odd.getGoodscatid();
			double odWeight=odd.getOd_total_weight();
			if(Util.getThisCatIdIsSpecialStr(catid)){
				//特殊商品
				weight1+=odWeight;
			}else{
				//普通商品
				weight2+=odWeight;
			}
			country=odd.getCountryId();
		}
		double freight1=0.00,freight2=0.00;
		if(weight1>0){
			freight1=getOrderShippingCost(weight1,Integer.valueOf(country),type,true);
		}
		if(weight2>0){
			freight2=getOrderShippingCost(weight2,Integer.valueOf(country),type,false);;
		}
		esFreight +=(freight1+freight2);
		return esFreight;
	}

	public double getOrderShippingCost(double weight ,int countryid,String shippingMethod,boolean isSpecialCatid){
		double sumEubSpecialWeight = 0d,sumFreeSpecialWeight = 0d,sumNormalWeight = 0d,umSpecialWeight = 0d,sumEubNormalWeight = 0d,
				sumEubWeight = 0d,sumFreeNormaWeight = 0d,umFreeSpecialWeight = 0d,sumFreeWeight = 0d,shippingCost = 0d,sumGoodsCarWeight = 0d,sumSpecialWeight = 0d;
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
			criteria1.andCountryidEqualTo(999);
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
				if(sumSpecialWeight > 21000){
					singleFreightSpc = sumSpecialWeight >0 ? singleFreight.add(specialBigWeightPrice.multiply(new BigDecimal(sumSpecialWeight/1000))) : new BigDecimal(0);

				}else if(specialBaseWeight == ratioWeight){
					singleFreightSpc = sumSpecialWeight >0 ? specialBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumSpecialWeight,specialBaseWeight)),1d))).multiply(specialRatioPrice)) :zero;
				}else {
					singleFreightSpc = sumSpecialWeight >0 ? specialBasePrice.add( new BigDecimal((sumSpecialWeight - specialBaseWeight)/specialBaseWeight).multiply(specialRatioPrice)) :new BigDecimal(0);
				}
				if(sumNormalWeight > 21000){
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBigWeightPrice.multiply(new BigDecimal(sumGoodsCarWeight/1000))) : new BigDecimal(0);
				}else if(normalBaseWeight == ratioWeight){
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumGoodsCarWeight,normalBaseWeight)),1d))).multiply(normalRatioPrice))) : zero;
				}else {
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBasePrice.add( new BigDecimal((sumGoodsCarWeight - normalBaseWeight)/normalBaseWeight).multiply(normalRatioPrice))) : new BigDecimal(0);
				}
				singleFreight = singleFreightSpc.add(singleFreightNor);
			}else 	if(sumGoodsCarWeight > 21000){
				singleFreight = sumGoodsCarWeight > 0 ? singleFreight.add(normalBigWeightPrice.multiply(new BigDecimal(sumGoodsCarWeight/1000))) : zero;
			}else if(normalBaseWeight == ratioWeight){
				singleFreight = sumGoodsCarWeight > 0 ? normalBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumGoodsCarWeight,normalBaseWeight)),1d))).multiply(normalRatioPrice) ) : zero;
			}else {
				singleFreight = sumGoodsCarWeight > 0 ? normalBasePrice.add( new BigDecimal((sumGoodsCarWeight - normalBaseWeight)/normalBaseWeight).multiply(normalRatioPrice) ) : zero;
			}
		}
		shippingCost = singleFreight.doubleValue();
		return shippingCost;
	}


	@RequestMapping(value = "/queryOrderFreight.do")
	@ResponseBody
	public JsonResult queryOrderFreight(HttpServletRequest request, HttpServletResponse response) {
		IZoneServer zs = new ZoneServer();
		JsonResult json = new JsonResult();
		try {
			String countryid_req = request.getParameter("countryid");
			String volume = request.getParameter("volume");
			String weigth = request.getParameter("weigth");
			double weigth_ = 0;
			int countryid = 0;
			double volume_ = 0;
			if (Utility.getIsInt(countryid_req)) {
				countryid = Integer.parseInt(countryid_req);
			} else {
				json.setOk(false);
				json.setMessage("获取countryid失败");
				return json;
			}
			if (Utility.getIsDouble(volume)) {
				volume_ = Double.parseDouble(volume);
			} else {
				json.setOk(false);
				json.setMessage("获取volume失败");
				return json;
			}
			if (Utility.getIsDouble(weigth)) {
				weigth_ = Double.parseDouble(weigth);
			} else {
				json.setOk(false);
				json.setMessage("获取weigth失败");
				return json;
			}
			List<ShippingBean> list = zs.getShippingBeans(countryid, new double[] { weigth_ }, new double[] { 1 },new double[] { volume_ }, new String[] { "0" }, new int[] { 1 });
			json.setOk(true);
			json.setData(list);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("查询运费失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询运费失败,原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 判断 是否有相似账号
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/queryRepeatUserid.do")
	@ResponseBody
	public JsonResult queryRepeatUserid(HttpServletRequest request, HttpServletResponse response) {
		String userid = request.getParameter("userid");
		JsonResult json = new JsonResult();
		try {
			if (StringUtil.isBlank(userid)) {
				json.setOk(false);
				json.setMessage("获取userid失败");
				return json;
			}
			int uid = Integer.parseInt(request.getParameter("userid"));
			IOrderwsServer server = new OrderwsServer();
			List<Integer> list_uid = server.getRepeatUserid(uid);
			json.setOk(true);
			json.setData(list_uid);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("查询相似账号失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("查询相似账号失败,原因：" + e.getMessage());
		}

		return json;
	}

	/**
	 * 手动调整采购人员
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/changeBuyer.do")
	@ResponseBody
	public JsonResult changeBuyer(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String odid = request.getParameter("odid");
		String admuserid = request.getParameter("admuserid");
		try {
			if (StringUtil.isBlank(odid)) {
				json.setOk(false);
				json.setMessage("获取订单详情id失败");
				return json;
			}
			if (StringUtil.isBlank(admuserid)) {
				json.setOk(false);
				json.setMessage("获取采购人id失败");
				return json;
			}
			IOrderwsServer server = new OrderwsServer();
			server.changeBuyer(Integer.valueOf(odid), Integer.valueOf(admuserid));
			json.setOk(true);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("调整采购人员失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("调整采购人员失败,原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 调整整个订单的采购人员
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/changeOrderBuyer.do")
	@ResponseBody
	public JsonResult changeOrderBuyer(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String orderid = request.getParameter("orderid");
		String admuserid = request.getParameter("admuserid");
		String odids=request.getParameter("odids");
		try {
			if (StringUtil.isBlank(orderid)) {
				json.setOk(false);
				json.setMessage("获取订单号失败");
				return json;
			}
			if (StringUtil.isBlank(admuserid)) {
				json.setOk(false);
				json.setMessage("获取采购人id失败");
				return json;
			}
			IOrderwsServer server = new OrderwsServer();
			server.changeOrderBuyer(orderid, Integer.valueOf(admuserid),odids);
			json.setOk(true);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("调整整个订单的采购人员失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("调整整个订单的采购人员失败,原因：" + e.getMessage());
		}

		return json;
	}

	/**
	 * 通过oid查找商品采购员
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/qyeruBuyerByOrderNo.do")
	@ResponseBody
	public JsonResult qyeruBuyerByOrderNo(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String odids = request.getParameter("str_oid"); // order_detail的id拼接的字符串
		if (odids == null || "".equals(odids)) {
			json.setOk(false);
			json.setMessage("获取odids失败");
			return json;
		}
		try {
			IOrderwsServer server = new OrderwsServer();
			List<Map<String, String>> list = server.getBuyerByOrderNo(odids);
			json.setOk(true);
			json.setData(list);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("通过oid查找商品采购员失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("通过oid查找商品采购员失败,原因：" + e.getMessage());
		}
		return json;

	}

	/**
	 * 获取历史价格
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/showHistoryPrice.do")
	@ResponseBody
	public JsonResult showHistoryPrice(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String url = request.getParameter("url");
		if (url == null || "".equals(url)) {
			json.setOk(false);
			json.setMessage("获取url失败");
			return json;
		}
		try {
			GoodsPriceHistoryserviceImpl historyservice = new GoodsPriceHistoryserviceImpl();
			List<Object[]> list = historyservice.seehistoryPrice(url);
			System.out.println(list);
			if (!list.isEmpty() && list.size() > 0) {
				json.setOk(true);
				json.setData(list);
			} else {
				json.setOk(false);
				json.setMessage("暂无历史价格!");
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("获取历史价格失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("获取历史价格失败,原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 添加补货订单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/afterReplenishment.do")
	@ResponseBody
	public JsonResult afterReplenishment(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		IOrderAutoService preOrderAutoService=new PreOrderAutoService();
		String parm = request.getParameter("parm");
		if (parm == null || "".equals(parm)) {
			json.setOk(false);
			json.setMessage("获取参数失败");
			return json;
		}
		try {
			String[] data = parm.split(",");
			IOrderwsServer server = new OrderwsServer();
			String newOrderid = "";
			String oldOrderid = "";
			int row = 0;
			if (data.length > 0) {
				oldOrderid = data[0].split(":")[2].toString();
				newOrderid = oldOrderid + "_" + oldOrderid.substring(oldOrderid.length() - 4, oldOrderid.length());
				LOG.info("补货新的订单号为:" + newOrderid);
				row = +server.addOrderInfo(data[0].split(":")[2].toString(), newOrderid, data.length);
				if (row > 0) {
					// 判断新订单是否为取消
					int state = server.judgeOrderState(newOrderid);
					if (state == -1 || state == 6) {
						Random random = new Random();
						int randomInt = random.nextInt(90) + 10;
						newOrderid = oldOrderid + "_"
								+ oldOrderid.substring(oldOrderid.length() - 4, oldOrderid.length()) + randomInt;
						LOG.info("补货新的订单号为:" + newOrderid);
						row = +server.addOrderInfo(data[0].split(":")[2].toString(), newOrderid, data.length);
					}
					// 分配给主单的采购人员
					int admuserid = server.addAutoAdmuser(oldOrderid);
					for (int i = 0; i < data.length; i++) {
						String[] obj = data[i].split(":");
						String goodsid = obj[0].toString();
						String count = obj[1].toString();
						String orderid = obj[2].toString();
						LOG.info("商品编号:" + goodsid + "\t 补货数量:" + count + "\t 订单号:" + newOrderid);
						row += server.addOrderDetails(goodsid, count, newOrderid, orderid, admuserid);
					}
				}
			}
			if (row - 1 != data.length) {
				LOG.info("新增补货订单失败，开始删除");
				boolean flag = server.deleteOrderInfo(newOrderid);
				if (flag) {
					LOG.info("回滚成功");
				} else {
					LOG.info("回滚失败");
				}
				json.setOk(false);
				json.setMessage("新增补货订单失败");
			} else {
				json.setOk(true);
				json.setMessage("新增补货订单成功");
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("新增补货订单失败，原因：" + e.getMessage());
			json.setOk(false);
			json.setMessage("新增补货订单失败,原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 根据订单号获取dropship子订单详情
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/queryChildrenOrderDetail.do")
	public String queryChildrenOrderDetail(HttpServletRequest request, HttpServletResponse response) {
		DecimalFormat df = new DecimalFormat("######0.00");
		String orderNo = request.getParameter("orderNo");
		String payTime = request.getParameter("paytime");
		request.setAttribute("payToTime", payTime);
		String isDropshipOrder = request.getParameter("isDropshipOrder");
		try {

			IOrderwsServer server = new OrderwsServer();
//			List<Admuser> aublist = server.getAllBuyer();
			List<Admuser> aublist =iOrderinfoService.getAllBuyer();
			request.setAttribute("aublist", net.sf.json.JSONArray.fromObject(aublist));
			// 订单信息
//			OrderBean order = server.getChildrenOrders(orderNo);
			OrderBean order =iOrderinfoService.getChildrenOrders(orderNo);
			PaymentDaoImp paymentDao = new PaymentDao();
//			String fileByOrderid = paymentDao.getFileByOrderid(orderNo);
			String allFreight =iOrderinfoService.getAllFreightByOrderid(orderNo);
			double freightFee = getFreightFee(allFreight, order);
			request.setAttribute("allFreight",freightFee);
			String fileByOrderid=iOrderinfoService.getFileByOrderid(orderNo);
			if (fileByOrderid == null || fileByOrderid.length() < 10) {
				request.setAttribute("invoice", "0");
			} else {
				if (fileByOrderid.indexOf(".pdf") > -1) {
					request.setAttribute("invoice", "1");
				} else {
					request.setAttribute("invoice", "2");
				}
			}
			// 订单商品详情 --getChildrenOrdersDetails
//			List<OrderDetailsBean> odb = server.getChildrenOrdersDetails(orderNo);
			List<OrderDetailsBean> odb =iOrderinfoService.getChildrenOrdersDetails(orderNo);
			Forwarder forw = null;
			int state = order.getState();

			IZoneServer os = new ZoneServer();
			request.setAttribute("countryList",os.getAllZone());
			// zp 修改 到账信息和订单信息一起查
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("id", order.getDzId());
			map.put("orderno", order.getDzOrderno());
			map.put("confirmname", order.getDzConfirmname());
			map.put("confirmtime", order.getDzConfirmtime());
			//获取订单出运信息
			// 获取汇率
			double rate =Double.valueOf(order.getExchange_rate());
			List<ShippingBean> spb=iOrderinfoService.getShipPackmentInfo(orderNo);
			String actual_freight="654321";
			String transportcompany="-";
			String shippingtype="-";
			String awes_freight="-";
			Double actualFreight=0.00;
			Double awesFreight=0.00;
			if(spb.size()>0){
				for(ShippingBean s:spb){
					if(s != null && StringUtil.isNotBlank(s.getTransportcompany())){
						actualFreight += StringUtil.isBlank(s.getActual_freight())?0.00:Double.parseDouble(s.getActual_freight());
						if(!transportcompany.contains(s.getTransportcompany())){
							transportcompany =transportcompany+";"+(StringUtil.isBlank(s.getTransportcompany())?"":s.getTransportcompany());
						}
						if(!shippingtype.contains(s.getShippingtype())){
							shippingtype=shippingtype+";"+(StringUtil.isBlank(s.getShippingtype())?"":s.getShippingtype());
						}
						awesFreight=awesFreight+Double.parseDouble(s.getEstimatefreight())*rate;
					}
				}
				actual_freight=df.format(actualFreight);
				awes_freight=df.format(awesFreight);
			}
			request.setAttribute("actual_freight",actual_freight);
			request.setAttribute("transportcompany",transportcompany);
			request.setAttribute("shippingtype",shippingtype);
			request.setAttribute("awes_freight",awes_freight);
			String str_oid = ""; // 收集order_detail的 id
			// 计算订单总金额
			float sumPrice = 0;

			String purchasetime_order = "";
			double preferential_price = 0;
			Double es_prices=0.00;
			for (int i = 0; i < odb.size(); i++) {
				if (state == 1) {
					preferential_price += odb.get(i).getPreferential_price();
					String purchase_time = odb.get(i).getPurchase_time();
					if (Utility.getStringIsNull(purchase_time)) {
						if (Utility.getStringIsNull(purchasetime_order)) {
							long purchase_time_long = Long
									.valueOf(purchase_time.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
							long purchasetime_order_long = Long
									.valueOf(purchasetime_order.replaceAll("[-\\s:]", "").replaceAll(".0", ""));
							purchasetime_order = purchase_time_long > purchasetime_order_long ? purchase_time
									: purchasetime_order;
						} else {
							purchasetime_order = purchase_time;
						}
					}
				}
				if (odb.get(i).getOrsstate() > 0) {
					sumPrice += odb.get(i).getSumGoods_p_price();
				}

				str_oid = str_oid + odb.get(i).getId() + ",";
				String goods_img = odb.get(i).getGoods_img();
				if (goods_img != null && goods_img.equals("/img/1.png")) {
					odb.get(i).setGoods_img("/cbtconsole/img/1.png");
				}
				es_prices+=odb.get(i).getEs_price();
			}

			String arrive_time1 = "";
			String arrive_time = order.getMode_transport();
			int arrive = 0;
			String shipMethod = "0";
			if (!Utility.getStringIsNull(order.getExpect_arrive_time())) {
				arrive_time1 = order.getExpect_arrive_time();
				arrive = -1;
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			if (Utility.getStringIsNull(arrive_time)) {
				if (arrive_time.indexOf("@") > -1) {
					String[] strings = arrive_time.split("@");
					arrive_time = strings[1];
					shipMethod = strings[0];
					if (arrive_time.indexOf("-") > -1) {
						String[] arrive_time_ = arrive_time.split("-");
						int max = Utility.getIsInt(arrive_time_[1]) ? Integer.parseInt(arrive_time_[1]) : 0;
						arrive = max;
					} else {
						arrive = Utility.getIsInt(arrive_time) ? Integer.parseInt(arrive_time) : 0;
					}

					if (arrive != 0 && arrive != -1) {
						Calendar c = Calendar.getInstance();
						if (Utility.getStringIsNull(order.getTransport_time())) {
							c.setTime(sf.parse(order.getTransport_time()));
						}
						c.add(Calendar.DAY_OF_MONTH, arrive);
						arrive_time = sf.format(c.getTime());
					}
				} else {
					arrive_time = "";
				}
			}
			if (state == 3) {
				forw = iOrderinfoService.getForwarder(orderNo);
				// zlw add start
				// 读取物流信息
				List<CodeMaster> logisticsList =iOrderinfoService.getLogisticsInfo();
				request.setAttribute("logisticsList", logisticsList);
				// zlw add end
			} else if (state == 4) {
				Evaluate evaluate =iOrderinfoService.getEvaluate(orderNo);
				request.setAttribute("evaluate", evaluate);
			} else if (state != 0 || state != -1 || state != 7) {
				// 读取到账信息
				List<Map<String, String>> pays =iOrderinfoService.getOrdersPays(orderNo);
				request.setAttribute("pays", pays);
				// 采购和出运预警
				String pay_time = "";
				for (int i = 0; i < pays.size(); i++) {
					String order_pay = pays.get(i).get("orderid");
					if (orderNo.equals(order_pay)) {
						pay_time = pays.get(i).get("createtime");
					}
				}
				if (Utility.getStringIsNull(pay_time)) {
					// 支付时间+最长国内交期+国际运输时间<当前时间
					Calendar c = Calendar.getInstance();
					c.setTime(sf.parse(pay_time));
					if (state == 5) {
						c.add(Calendar.DAY_OF_MONTH, 2);
						if (System.currentTimeMillis() > c.getTime().getTime()) {
							request.setAttribute("delivery_warning", "(采购预警" + sf.format(c.getTime()) + ")");
						}
					} else if (state == 1) {
						c.add(Calendar.DAY_OF_MONTH, order.getDeliveryTime() - 2);
						if (System.currentTimeMillis() > c.getTime().getTime()) {
							request.setAttribute("delivery_warning", "(入库预警" + sf.format(c.getTime()) + ")");
						}
					} else if (state == 2) {
						c.add(Calendar.DAY_OF_MONTH, order.getDeliveryTime());
						if (System.currentTimeMillis() > c.getTime().getTime()) {
							request.setAttribute("delivery_warning", "(出运预警" + sf.format(c.getTime()) + ")");
						}
					}
				}
			}
			// DataSourceSelector.restore();

			if (arrive != 0) {
				request.setAttribute("expect_arrive_time", arrive == -1 ? arrive_time1 : arrive_time);// 预计到货时间
			}
			if (str_oid.length() > 0) {
				str_oid = str_oid.substring(0, str_oid.length() - 1);
			}
			request.setAttribute("str_oid", str_oid);
			request.setAttribute("shipMethod", shipMethod);
			request.setAttribute("orderNo", orderNo);
			request.setAttribute("orderDetail", odb); // 包含货源地址
			request.setAttribute("order", order);
			// 实际运费
			Double actual_ffreight_ = Utility.getIsDouble(order.getActual_ffreight())
					? Double.parseDouble(order.getActual_ffreight()) : 0;
			request.setAttribute("actual_ffreight_", actual_ffreight_);
			// service_fee是从数据表读取的
			request.setAttribute("service_fee",
					Utility.getIsDouble(order.getService_fee()) ? Double.parseDouble(order.getService_fee()) : 0);
			request.setAttribute("cashback", order.getCashback());
			String mode_transport = "";
			int isFree = 0;
			int isSplitOrder = 0;
			if (order.getActual_freight_c() != 0) {
				isSplitOrder = 1;
			}

			if (Utility.getStringIsNull(order.getMode_transport())) {
				String[] mode_transport_ = order.getMode_transport().split("@");
				for (int j = 0; j < mode_transport_.length; j++) {
					if (j == mode_transport_.length - 2
							&& Utility.getIsDouble(mode_transport_[mode_transport_.length - 2])) {
						if (Double.parseDouble(mode_transport_[mode_transport_.length - 2]) == 0) {
							if (mode_transport_[mode_transport_.length - 1].equals("all")) {
								mode_transport_[mode_transport_.length - 1] = "免邮";
								isFree = 1;
							} else {
								mode_transport_[mode_transport_.length - 1] = "不知重量";
								isFree = 1;
							}
						}
					}
					if (j == mode_transport_.length - 1) {
						if (mode_transport_[j].equals("product")) {
							mode_transport_[j] = "未付运费";
						} else if (mode_transport_[j].equals("all")) {
							if (isFree != 1) {
								mode_transport_[j] = "全付";
							}
						}
					}
					mode_transport += mode_transport_[j];
					if (mode_transport.indexOf(".") > -1) {
						mode_transport = mode_transport.split("\\.")[0];
					}
					if (j != mode_transport_.length - 1)
						mode_transport += "@";
				}
			}

			if (orderNo.indexOf("_") > -1) {
				isSplitOrder = 1;
			}
			request.setAttribute("isSplitOrder", isSplitOrder);

			// 获取用户货币单位 order中已经包含-----ylm用户货币和订单货币单位不一致
			order.setApplicable_credit(new BigDecimal(order.getApplicable_credit()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			// 订单批量优惠总
			request.setAttribute("preferential_price", preferential_price);
			request.setAttribute("sumPrice", sumPrice);
			// 订单采购时间
			request.setAttribute("purchasetime_order", purchasetime_order);
			// 查询用户相关订单->同一客户，还没出货的 订单
			request.setAttribute("orderNos",iOrderinfoService.getOrderNos(order.getUserid(),orderNo));
			// 判断订单时候有替代产品
			int count = order.getPackage_style();
			request.setAttribute("count", count);
			/* 抵扣赠送运费操作---2016.7.25.abc---start */
			// 订单在未付运费的情况下和全付的时候后期需要增加运费的情况下
			if (order.getState() == 2) {
				double unpaid_freight = 0.0;
				if (mode_transport.indexOf("未付运费") > -1 || mode_transport.indexOf("全付") > -1) {
					unpaid_freight = order.getRemaining_price();
				}
				// 客户还需付款
				if (order.getRemaining_price() > 0) {
					request.setAttribute("unpaid_freight", unpaid_freight);
					request.setAttribute("freight_deduction", "1");
				}
			}

			/* 抵扣赠送运费操作---2016.7.25.abc----end */
			request.setAttribute("mode_transport", mode_transport);
			request.setAttribute("isFree", isFree);
			request.setAttribute("forwarder", forw);
			request.setAttribute("paymentconfirm", map);

			// 7.21 订单详情页面增加利润率
			String[] strs = mode_transport.split("@");
			int jq = 0;
			if (strs.length > 1 && strs[1].indexOf("-") > 0) {
				String[] jqs = null;
				if (strs[1].indexOf("Days") > 0) {
					jqs = strs[1].substring(0, strs[1].indexOf(" Days")).split("-");
				} else {
					jqs = strs[1].split("-");
				}
				for (int j = 0; j < jqs.length; j++) {
					jq += Integer.parseInt(jqs[j]);
				}
			}
			double pid_amount=0.00;
			if(odb.size()>0){
				pid_amount=odb.get(0).getPid_amount();
			}
			// 获取汇率
//			ICurrencyService currencyDao = new CurrencyService();
//			double rate = currencyDao.currencyConverter("RMB");
			double sp = Double.parseDouble(order.getProduct_cost() == null ? "0.0" : order.getProduct_cost());
			double hp = order.getDiscount_amount();
			double sale =0.00;
			double buy = 0.0;
			double volume = 0.0;
			double weight = 0.0;
			for (int i = 0; i < odb.size(); i++) {
				if (odb.get(i).getState() != 2) {
					buy += odb.get(i).getSumGoods_p_price();
					//volume += odb.get(i).getOd_bulk_volume();
					weight += odb.get(i).getOd_total_weight();
				}
			}

			sale=iOrderinfoService.getAcPayPrice(orderNo);//dp订单实际支付金额
			request.setAttribute("avg_jq", jq / 2);
			//实际支付金额
			request.setAttribute("sale", sale);
			//预计采购金额
			request.setAttribute("es_prices",es_prices);
			request.setAttribute("piaAmount",pid_amount);
			request.setAttribute("buy", Math.round((buy+pid_amount) * 100) / 100.0);
			request.setAttribute("volume", Math.round(volume * 1000) / 1000.0);
			request.setAttribute("weight", Math.round(weight * 1000) / 1000.0);
			String countryid = "0";
			if (odb.size() != 0 && odb != null) {
				countryid = odb.get(0).getCountry();
				if ("42".equals(countryid)|| "43".equals(countryid)) {
					countryid = "36";
				}
			}
			request.setAttribute("country", countryid);
			request.setAttribute("countryName",
					(order.getMode_transport() == null || "".equals(order.getMode_transport())) ? "USA" : strs[2]);
			request.setAttribute("orderNo", order.getOrderNo());
//			request.setAttribute("rate", rate);
			request.setAttribute("userid", order.getUserid());
			request.setAttribute("isDropshipOrder1", isDropshipOrder);
			String lirun1 = null;
			if (sale == 0.0 || buy == 0.0) {
				lirun1 = "--";
			} else {
				lirun1 = String.format("%.2f", ((sale - buy) / sale * 100)) + "%";
			}
			request.setAttribute("lirun1", lirun1);
			request.setAttribute("isDropshipOrder", order.getIsDropshipOrder());
			//用户信息yyl
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser admuserinfo = JSONObject.parseObject(admuserJson, Admuser.class);
			request.setAttribute("admuserinfo", admuserinfo);
			//根据产品id去查询评论
			List<String> lists = new ArrayList<String>();
			for (OrderDetailsBean orderDetailsBean : odb) {
				//一个订单下的该商品的pid只有一个
				lists.add(orderDetailsBean.getGoods_pid());
			}
			request.setAttribute("lists", lists);
			request.setAttribute("isDropFlag",1);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询dropship子订单详情失败，原因：" + e.getMessage());
		}

		return "order_details_new";

	}

	/**
	 * 查询产品验货是质检信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/openCheckResult")
	@ResponseBody
	public JsonResult openCheckResult(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		Map<String,String> map=new HashMap<String,String>(2);
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		// 判断是否登录失效，失效则不能执行
		if (adm == null) {
			json.setOk(false);
			json.setMessage("请登录后操作");
			return json;
		} else {
			IOrderwsServer orderwsServer = new OrderwsServer();
			String orderid = request.getParameter("orderid");
			String goodsid=request.getParameter("goodsid");
			map.put("orderid",orderid);
			map.put("goodsid",goodsid);
			List<QualityResult> list=orderwsServer.openCheckResult(map);
			json.setData(list);
			return json;

		}
	}

	/**
	 * 取消订单
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/closeOrder.do")
	@ResponseBody
	public JsonResult closeOrder(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		// 判断是否登录失效，失效则不能执行
		if (adm == null) {
			json.setOk(false);
			json.setMessage("请登录后操作");
			return json;
		} else {
			String orderNo = request.getParameter("orderNo");
			if (orderNo == null || "".equalsIgnoreCase(orderNo)) {
				json.setOk(false);
				json.setMessage("获取订单号失败");
				return json;
			}
			String currency = request.getParameter("currency");
			if (currency == null || "".equalsIgnoreCase(currency)) {
				json.setOk(false);
				json.setMessage("获取货币单位失败");
				return json;
			}
			String toEmail = request.getParameter("email");
			if (toEmail == null || "".equalsIgnoreCase(toEmail)) {
				json.setOk(false);
				json.setMessage("获取客户邮箱失败");
				return json;
			}
			String confirmEmail = request.getParameter("confirmEmail");
			/*
			 * if (confirmEmail == null || "".equalsIgnoreCase(confirmEmail)) {
			 * json.setOk(false); json.setMessage("获取销售邮箱失败"); return json; }
			 */
			//Added <V1.0.1> Start： cjc 2018/10/23 16:25 TODO 判断是否是droship子订单，如果是子订单则要去 查询子订单的状态 0:默认不是  1：是
			String isDropshipOrder1 = request.getParameter("isDropshipOrder1");
			//End：

			try {
				IOrderwsServer orderwsServer = new OrderwsServer();
				//如果线上订单已经被取消则不做操作 王宏杰 2018-09-29
				int oiState=orderwsServer.checkOrderState(orderNo,isDropshipOrder1);
				if(oiState == -1 || oiState == 6){
					json.setOk(false);
					json.setMessage("线上订单已被取消，无法再取消<br>这个订单三分钟之后变成取消状态");
					return json;
				}
				// 判断当前订单是否是drop订单
				boolean isDrop = orderwsServer.isDropshipOrder(orderNo) > 0;
				if (isDrop) {
					String mainOrderNo = orderwsServer.queryMainOrderByDropship(orderNo);
					//Added <V1.0.1> Start： cjc 2018/10/23 16:52 TODO 如果是取消dropship 子订单 则需要再查询一下主订单是否是取消状态
					oiState=orderwsServer.checkOrderState(orderNo,"0");
					if(oiState == -1 || oiState == 6){
						json.setOk(false);
						json.setMessage("线上主订单已被取消，无法再取消<br>这个订单三分钟之后变成取消状态");
						return json;
					}
					//End：
					if (mainOrderNo == null || "".equals(mainOrderNo)) {
						String remark = "订单号:" + orderNo + ",获取线上dropship主订单号失败,终止取消";
						LOG.error(remark);
						// 消息提醒添加错误记录
						InsertMessageNotification messageDao = new InsertMessageNotification();
						messageDao.orderChangeError(orderNo, adm.getId(), remark);
						json.setOk(false);
						json.setMessage("获取线上dropship主订单号失败");
						return json;
					} else {
						json = closeDropShipOrder(request, response, orderwsServer, adm.getId(), mainOrderNo, orderNo,
								toEmail, confirmEmail);
					}
				} else {
					json = closeGeneralOrder(request, response, orderwsServer, adm.getId(), orderNo, toEmail,
							confirmEmail);
				}

			} catch (Exception e) {
				LOG.error("closeOrder error, orderNo : " + orderNo + ",reason : " + e.getMessage());
				json.setOk(false);
				json.setMessage("取消订单失败,原因：" + e.getMessage());
			}
			return json;

		}
	}

	/**
	 * dropship订单取消
	 *
	 * @param request
	 * @param response
	 * @param orderwsServer
	 * @param adminId
	 * @param orderNo
	 * @param toEmail
	 * @param confirmEmail
	 * @throws Exception
	 */
	private JsonResult closeDropShipOrder(HttpServletRequest request, HttpServletResponse response,
                                          IOrderwsServer orderwsServer, int adminId, String mainOrderNo, String orderNo, String toEmail,
                                          String confirmEmail) throws Exception {
		Map<String, Object> model =new HashMap<>();
		LOG.info("closeDropShipOrder start");
		JsonResult json = new JsonResult();

		// 如果需要取消的订单号就是主订单号则调用普通取消方法,普通取消方法进行显示订单状态校检
		if (mainOrderNo.equals(orderNo)) {
			int res = orderwsServer.iscloseOrder(orderNo);
			if (res > 0) {
				json.setOk(false);
				json.setMessage("该订单已取消，终止操作");
			} else {
				LOG.info("订单号:" + orderNo + ",更新线上orderinfo订单状态:" + -1);
				// jxw 2017-4-25添加订单状态判断,修改订单状态为-1(后台取消),操作人目前是adminId
				boolean isCheck = CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(mainOrderNo, -1, adminId);
				if (isCheck) {

					// 修改orderinfo表状态
					res = orderwsServer.closeOrder(orderNo);
					if (res > 0) {
						// 释放该订单占用的库存
						orderwsServer.cancelInventory(mainOrderNo);
						//对于没有使用库存订单的商品做库存增加到库存表中
						orderwsServer.cancelInventory1(mainOrderNo);
					}
					// 修改dropshiporder表状态
					orderwsServer.closeDropshipOrderByMainOrderNo(mainOrderNo);
					int userId = Integer.parseInt(request.getParameter("userId"));

					// 获取用户货币单位
					// zlw add start
					float actualPay = Float.parseFloat(request.getParameter("actualPay"));
					float order_ac = Float.parseFloat(request.getParameter("order_ac"));
					UserDao dao = new UserDaoImpl();
					order_ac = new BigDecimal(order_ac).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
					dao.updateUserAvailable(userId,
							new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
							" system closeOrder:" + orderNo,orderNo, String.valueOf(adminId), 0, order_ac, 1);
					// zlw add end

					// ssd add start
					// 发送取消订单的提醒邮件
					StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
					sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
							+ AppConfig.ip_email + "/img/logo.png' ></img></a>");
					sbBuffer.append(
							"<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
					sbBuffer.append("<br><br>Order#: " + orderNo);
					sbBuffer.append(
							"<br><br>We apologize, but despite our efforts, we weren’t able to fulfill some or all of the items in your order.");
					sbBuffer.append(
							"<br>We apologize for any inconvenience this has caused and look forward to your next visit to ");
					sbBuffer.append("<a href='" + AppConfig.server_path + "'>www.importx.com</a>.");
					sbBuffer.append("<br>Thank you for shopping with us.");
					sbBuffer.append("<br>To review your order status, click ");
					sbBuffer.append("<a href='" + AppConfig.center_path + "'>" + AppConfig.center_path + "</a>.");
					sbBuffer.append("<br><br>Sincerely,");
					sbBuffer.append("<br>Import-Express Team");

					SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
							"Your ImportExpress Order " + orderNo + " transaction is closed!", "", orderNo, 2);

					// jxw 2017-4-25 插入成功，插入信息放入更改记录表中
					insertChangeRecords(orderNo, -1, adminId);
					json.setOk(true);
				}
			}
			LOG.info("closeGeneralOrder end");
		} else {
			// 子订单取消,
			// 判断线上订单状态是否取消
			int isMainCancel = orderwsServer.isCloseByDropshipMainOrder(mainOrderNo);
			if (isMainCancel > 0) {
				InsertMessageNotification messageDao = new InsertMessageNotification();
				String remark = "dropship主订单号:" + mainOrderNo + ",线上订单状态为已取消状态,不符合规则,拒绝更新";
				LOG.error(remark);
				// 消息提醒添加错误记录
				messageDao.orderChangeError(orderNo, adminId, remark);
				// 更新错误日志记录
				String sqlStr = "dropship MainOrderNo:" + mainOrderNo + ",operationType:"
						+ OrderInfoConstantUtil.OFFLINECANCEL;
				ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);
				json.setOk(false);
				json.setMessage("该dropship主订单已取消，终止操作");
			} else {

				// 判断子订单是否处于取消状态
				int res = orderwsServer.isCloseDropshipOrder(orderNo);
				if (res > 0) {
					LOG.info("子订单号:" + orderNo + ",线上dropship子订单已取消");
					json.setOk(false);
					json.setMessage("该dropship子订单已取消，终止操作");
				} else {
					// 子订单不是取消状态,设置为取消状态,
					LOG.info("订单号:" + orderNo + ",更新线上dropship子订单状态: -1");

					float totalPrice = Float.parseFloat(request.getParameter("totalPrice"));
					float freight = Float.parseFloat(request.getParameter("freight"));
					float weight = Float.parseFloat(request.getParameter("weight"));
					int userId = Integer.parseInt(request.getParameter("userId"));
					res = orderwsServer.closeDropshipOrder(userId, mainOrderNo, orderNo, totalPrice,0, freight, weight);
					// 取消成功后,返回现金给客户,
					if (res > 0) {
						// 获取用户货币单位
						// zlw add start
						float actualPay = Float.parseFloat(request.getParameter("actualPay"));
						float order_ac = Float.parseFloat(request.getParameter("order_ac"));
						UserDao dao = new UserDaoImpl();
						order_ac = new BigDecimal(order_ac).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
						dao.updateUserAvailable(userId,
								new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
								" system closeOrder:" + orderNo,orderNo, null, 0, order_ac, 1);
						// zlw add end

						// 直接SQL语句判断整个dropship订单是否全部取消,如果取消,则更新orderInfo的状态为取消
						int mainOrderUpd = orderwsServer.updateMainOrderByDropship(userId, mainOrderNo, orderNo);
						if (mainOrderUpd <= 0 && mainOrderUpd != -2) {// -2表示全部取消
							String remark = "dropship订单号:" + orderNo + ",更新主订单号状态["
									+ OrderInfoConstantUtil.OFFLINECANCEL + "]失败";
							LOG.error(remark);
							// 消息提醒添加错误记录
							InsertMessageNotification messageDao = new InsertMessageNotification();
							messageDao.orderChangeError(orderNo, adminId, remark);
							// 更新错误日志记录
							String sqlStr = "dropshipOrderNo:" + orderNo + ",operationType:"
									+ OrderInfoConstantUtil.OFFLINECANCEL;
							ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);
						}

						// ssd add start
						// 发送取消订单的提醒邮件
						StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
						sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
								+ AppConfig.ip_email + "/img/logo.png' ></img></a>");
						sbBuffer.append(
								"<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
						sbBuffer.append("<br><br>Order#: " + orderNo);
						sbBuffer.append(
								"<br><br>We apologize, but despite our efforts, we weren’t able to fulfill some or all of the items in your order.");
						sbBuffer.append(
								"<br>We apologize for any inconvenience this has caused and look forward to your next visit to ");
						sbBuffer.append("<a href='" + AppConfig.server_path + "'>www.importx.com</a>.");
						sbBuffer.append("<br>Thank you for shopping with us.");
						sbBuffer.append("<br>To review your order status, click ");
						sbBuffer.append("<a href='" + AppConfig.center_path + "'>" + AppConfig.center_path + "</a>.");
						sbBuffer.append("<br><br>Sincerely,");
						sbBuffer.append("<br>Import-Express Team");

						//					SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
//							"Your ImportExpress Order " + orderNo + " transaction is closed!", "", orderNo, 2);
						model.put("email",confirmEmail);
						model.put("name",toEmail);
						model.put("accountLink",AppConfig.center_path);
						model.put("orderNo",orderNo);
						net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(model);
						String modeStr = jsonObject.toString();

						sendMailFactory.sendMail(toEmail, null, "Your ImportExpress Order " + orderNo + " transaction is closed!", model, TemplateType.CANCEL_ORDER);
						// jxw 2017-4-25 插入成功，插入信息放入更改记录表中
						insertChangeRecords(orderNo, -1, adminId);
						json.setOk(true);
					} else {
						String remark = "订单号:" + orderNo + ",更改dropship子订单号失败,更改状态:"
								+ OrderInfoConstantUtil.OFFLINECANCEL;
						LOG.error(remark);
						InsertMessageNotification messageDao = new InsertMessageNotification();
						// 消息提醒添加错误记录
						messageDao.orderChangeError(orderNo, adminId, remark);
						// 更新错误日志记录
						String sqlStr = "dropshipOrderNo:" + orderNo + ",operationType:"
								+ OrderInfoConstantUtil.OFFLINECANCEL;
						ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);

						json.setOk(false);
						json.setMessage("取消失败，请通知管理员");

					}
				}

			}
			LOG.info("closeDropShipOrder end");

		}
		return json;

	}

	/**
	 * 普通订单取消
	 *
	 * @param request
	 * @param response
	 * @param orderwsServer
	 * @param adminId
	 * @param orderNo
	 * @param toEmail
	 * @param confirmEmail
	 * @throws Exception
	 */
	private JsonResult closeGeneralOrder(HttpServletRequest request, HttpServletResponse response,
                                         IOrderwsServer orderwsServer, int adminId, String orderNo, String toEmail, String confirmEmail)
			throws Exception {
		Map<String, Object> model =new HashMap<>();
		LOG.info("closeGeneralOrder start,orderNo : " + orderNo);

		JsonResult json = new JsonResult();


		// jxw 2017-4-25添加订单状态判断,修改订单状态为-1(后台取消),操作人目前是adminId
		boolean isCheck = CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(orderNo, -1, adminId);

		if (isCheck) {

            int res = orderwsServer.iscloseOrder(orderNo);
            if (res > 0) {
                json.setOk(false);
                json.setMessage("该订单已取消，终止操作");
            } else {
                int userId = Integer.parseInt(request.getParameter("userId"));
                //获取未更新之前，订单状态和客户ID，比较前后状态是否一致，不一致说明订单状态已经修改
                Map<String, Object> orderinfoMap = iPurchaseService.queryUserIdAndStateByOrderNo(orderNo);
                //根据订单号判断是否有PayPal或者stripe支付
                int isPay = paymentServiceNew.checkIsPayPalOrStripePay(userId, orderNo);
                if (isPay > 0 && !orderNo.contains("_")) {
                    int isCheckApproval = approvalService.checkIsExistsApproval(userId, orderNo);
                    if (isCheckApproval > 0) {
                        json.setMessage("此订单已经在取消订单审批流程中");
                        json.setOk(false);
                    } else {


                        // 获取用户货币单位
                        // zlw add start

                        LOG.info("订单号:" + orderNo + ",更新线上orderinfo订单状态:" + -1);
                        res = orderwsServer.closeOrder(orderNo);
                        //判断取消订单是否是测试订单
//				        boolean flag=orderwsServer.checkTestOrder(orderNo);
                        if (res > 0) {
                            // 释放该订单占用的库存
                            orderwsServer.cancelInventory(orderNo);
                            //对于没有使用库存订单的商品做库存增加到库存表中
                            orderwsServer.cancelInventory1(orderNo);

                            //取消订单后，通知客户订单状态修改
                            NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()), orderNo,
                                    Integer.valueOf(orderinfoMap.get("old_state").toString()), -1);

                            // 走审批流程
                            double actualPay = Double.parseDouble(request.getParameter("actualPay"));
                            OrderCancelApproval cancelApproval = new OrderCancelApproval();
                            cancelApproval.setUserId(userId);
                            cancelApproval.setOrderNo(orderNo);
                            cancelApproval.setPayPrice(actualPay);
                            cancelApproval.setType(2);
                            cancelApproval.setDealState(0);
                            cancelApproval.setOrderState(Integer.valueOf(orderinfoMap.get("old_state").toString()));
                            NotifyToCustomerUtil.insertIntoOrderCancelApproval(cancelApproval);
                            json.setOk(true);
                        }else{
                            json.setMessage("更新订单状态失败");
                            json.setOk(false);
                        }
                    }
                } else {
					res = orderwsServer.closeOrder(orderNo);
					if (res > 0) {
						// 获取用户货币单位
						// zlw add start
						float actualPay = Float.parseFloat(request.getParameter("actualPay"));
						float order_ac = Float.parseFloat(request.getParameter("order_ac"));
						int isDropshipOrder = Integer.valueOf(request.getParameter("isDropshipOrder"));
						if (isDropshipOrder == 2) {
							actualPay = 0;
							order_ac = 0;
						}
						/*UserDao dao = new UserDaoImpl();
						order_ac = new BigDecimal(order_ac).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
						dao.updateUserAvailable(userId,
								new BigDecimal(actualPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(),
								" system closeOrder:" + orderNo, orderNo, String.valueOf(adminId), 0, order_ac, 1);*/
						// zlw add end

						// 执行取消完成后，插入退款数据
						OrderCancelApproval cancelApproval = new OrderCancelApproval();
						cancelApproval.setUserId(userId);
						cancelApproval.setOrderNo(orderNo);
						cancelApproval.setPayPrice(actualPay);
						cancelApproval.setType(2);
						cancelApproval.setDealState(0);
						cancelApproval.setAgreeAmount(actualPay);
						cancelApproval.setOrderState(Integer.valueOf(orderinfoMap.get("old_state").toString()));
						NotifyToCustomerUtil.insertIntoOrderCancelApproval(cancelApproval);
						json.setOk(true);
					} else {
						json.setOk(false);
					}

				}
                if (json.isOk()) {
                    // ssd add start
                    // 发送取消订单的提醒邮件
                    StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
                    sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
                            + AppConfig.ip_email + "/img/logo.png' ></img></a>");
                    sbBuffer.append(
                            "<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
                    sbBuffer.append("<br><br>Order#: " + orderNo);
                    sbBuffer.append(
                            "<br><br>We apologize, but despite our efforts, we weren’t able to fulfill some or all of the items in your order.");
                    sbBuffer.append(
                            "<br>We apologize for any inconvenience this has caused and look forward to your next visit to ");
                    sbBuffer.append("<a href='" + AppConfig.server_path + "'>www.importx.com</a>.");
                    sbBuffer.append("<br>Thank you for shopping with us.");
                    sbBuffer.append("<br>To review your order status, click ");
                    sbBuffer.append("<a href='" + AppConfig.center_path + "'>" + AppConfig.center_path + "</a>.");
                    sbBuffer.append("<br><br>Sincerely,");
                    sbBuffer.append("<br>Import-Express Team");
                    //SendEmail.send(confirmEmail, null, toEmail, sbBuffer.toString(),
//							"Your ImportExpress Order " + orderNo + " transaction is closed!", "", orderNo, 2);
                    model.put("email", confirmEmail);
                    model.put("name", toEmail);
                    model.put("accountLink", AppConfig.center_path);
                    model.put("orderNo", orderNo);
                    net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(model);
                    String modeStr = jsonObject.toString();
                    try {
                        sendMailFactory.sendMail(toEmail, null, "Your ImportExpress Order " + orderNo + " transaction is closed!", model, TemplateType.CANCEL_ORDER);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.error("genOrderSplitEmail: email:" + model.get("email") + " model_json:" + modeStr + " e.message:" + e.getMessage());
                        json.setMessage("Failed to send mail, please contact the developer by screen, thank you！" + e.getMessage());
                    }
                    // jxw 2017-4-25 插入成功，插入信息放入更改记录表中
                    try {
                        insertChangeRecords(orderNo, -1, adminId);
                    } catch (Exception e) {
                        LOG.error("insertChangeRecords order[orderNo] error:" + e.getMessage());
                    }
                    LOG.info("closeGeneralOrder end");
                    json.setOk(true);
                }
            }
        }
		return json;

	}

	private void insertChangeRecords(String orderNo, int operationType, int adminId) {

		Orderinfo orderinfo = new Orderinfo();
		try {

			// 插入成功，插入信息放入更改记录表中
			OnlineOrderInfoDao infoDao = new OnlineOrderInfoDao();
			orderinfo = infoDao.queryOrderInfoByOrderNo(orderNo);
			if (orderinfo != null) {
				ChangeRecordsDao cRecordsDao = new ChangeRecordsDao();
				cRecordsDao.insertOrderChange(orderinfo, adminId, operationType);
			} else {
				LOG.error("订单[" + orderNo + "]获取数据失败，插入更改记录取消");
			}

		} catch (Exception e) {
			e.getStackTrace();
			if (orderinfo != null) {
				LOG.error("订单[" + orderinfo.getOrderNo() + "]更改失败，修改状态为：" + operationType);
			} else {
				LOG.error("订单[" + orderNo + "]获取数据失败，插入更改记录取消");
			}
		}
	}

	// 获取订单支付详情

	@RequestMapping(value = "/orderPayDetails.do")
	public String orderPayDetails(HttpServletRequest request, HttpServletResponse response) {
		DecimalFormat df = new DecimalFormat("######0.00");
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String admName = adm.getAdmName();

		String orderNo = request.getParameter("orderNo");
		String userId = request.getParameter("userId");

		IOrderwsServer server = new OrderwsServer();
		IPayServer payServer = new PayServer();

		try {
			// 关联到账的信息(状态为交易成功的)
			List<Payment> payList = payServer.getOrdersPayList(orderNo, 1);
			//原订单金额
			double oldAmount=0.00;
			//拆单金额
			double splitAmount=0.00;
			for(Payment p:payList){
				if(p.getOrderdesc().contains("split")){
					splitAmount+=p.getPayment_amount();
				}else{
					oldAmount+=p.getPayment_amount();
				}
			}
			//现有原订单金额
			double nowAmount=oldAmount-splitAmount;
			request.setAttribute("oldAmount",df.format(oldAmount));
			request.setAttribute("splitAmount",df.format(splitAmount));
			request.setAttribute("nowAmount",df.format(nowAmount));
			OrderBean order = new OrderBean();
			List<OrderBean> orders = new ArrayList<OrderBean>();
			// 订单信息
			List<OrderBean> list = server.getListOrders(orderNo);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getOrderNo().equals(orderNo)) {
					order = list.get(i);
				} else {
					orders.add(list.get(i));
				}
			}
			if (StrUtils.isMatch(userId, "(\\d+)")) {
				UserServer userServer = new UserServer();
				double[] balance = userServer.getBalance(Integer.valueOf(userId));
				request.setAttribute("balance", balance[0]);
			}

			// 订单商品详情
			//List<OrderDetailsBean> odb = server.getOrdersDetails(orderNo);

			//订单金额校验
			checkOrderAmount(request, order, userId);

			// //orderDetail
			// request.setAttribute("orderDetail", odb); // 包含货源地址
			// 订单支付确认信息回显
			PaymentConfirm paymentConfirm = server.queryForPaymentConfirm(orderNo);
			// 是否为黑名单
			int row = server.isTblack(payList.size() > 0 && StringUtil.isNotBlank(payList.get(0).getUsername()) ? payList.get(0).getUsername() : "----");
			if (row > 0) {
				request.setAttribute("isTblack", "该用户为黑名单用户");
			}
			//获取订单IPN付款信息
			int paymentStatus=server.getIpnPaymentStatus(orderNo);
			String msg="";
			if(paymentStatus==0){
				msg="【paypal实际到账异常，请联系研发部！】";
			}
			request.setAttribute("paymentInfo", msg);
			// 当前订单信息
			request.setAttribute("order", order);
			// 与当前订单关联订单信息
			request.setAttribute("orders", orders);
			request.setAttribute("payList", payList);
			request.setAttribute("paymentConfirm", paymentConfirm);
			// 实际运费
			request.setAttribute("actual_ffreight_", Utility.getIsDouble(order.getActual_ffreight())
					? Double.parseDouble(order.getActual_ffreight()) : 0);
			request.setAttribute("userId", userId);
			request.setAttribute("flag", orderNo.length()==18);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error(e.getMessage());
		}

		if (adm.getRoletype() == 0 || adm.getRoletype() == 4) {
			return "paymentConfirm";
		} else {
			return "paymentConfirm1";
		}
	}

	/**
	 *
	 * @Title checkOrderAmount
	 * @Description 订单金额校验
	 * @param request
	 * @param userId
	 * @return void
	 */
	private boolean checkOrderAmount(HttpServletRequest request, OrderBean orderInfo, String userId){
		request.setAttribute("checkOrder", 0);
		int uid = Integer.parseInt(userId);
		//1.进行客户的余额校检
		IUserDao userDao = new com.cbt.processes.dao.UserDao();
		UserBean userBean = userDao.getUserFromIdForCheck(uid);
		float userBanlance = Float.valueOf(userBean.getAvailable_m());
		if(userBanlance < 0){
			request.setAttribute("checkOrder", 1);
			request.setAttribute("checkMessage", "客户余额负数("+ userBanlance +")");
			return false;
		}
		//余额支付部分校检payment和recharge_record表
		//余额支付payment记录=余额变更记录值  为正常订单
		PaymentDaoImp dao = new PaymentDao();
		PaymentDetails pyd = dao.queryBalancePayment(orderInfo.getOrderNo());
		float balancePayment = (pyd ==null ? 0: pyd.getPaymentAmount());

		float rrPayment =0;
		List<RechargeRecord> rrList = rechargeRecordDao.queryBalancePayRecords(uid);
		if(rrList.size() > 0){
			for(RechargeRecord rr : rrList){
				if(!(rr.getRemark_id() == null || "".equals(rr.getRemark_id()))){
					if(rr.getRemark_id().indexOf(orderInfo.getOrderNo()) > -1){
						rrPayment += rr.getPrice();
					}
				}
			}
		}
		BigDecimal   bd1   =   new   BigDecimal(Math.abs(balancePayment - rrPayment));
		float   ft1   =   bd1.setScale(3,   BigDecimal.ROUND_HALF_UP).floatValue();
		if(ft1 > 0.01){
			request.setAttribute("checkOrder", 1);
			request.setAttribute("checkMessage", "此订单余额支付记录值("+ balancePayment +")和客户余额变更值("+ rrPayment +")不符");
			return false;
		}


		//2.总金额校检
		//订单本身校检，Remaining_price等于0  为正常订单
		if(orderInfo.getRemaining_price() > 0){
			request.setAttribute("checkOrder", 1);
			request.setAttribute("checkMessage", "此订单客户还需付款("+ orderInfo.getRemaining_price() +")");
			return false;
		}

		//订单详情商品总值 = 订单商品总金额  为正常订单
		// 订单详情商品总值
		IOrderwsServer server = new OrderwsServer();
		float odbPrice = server.queryGoodsPriceFromDetails(orderInfo.getOrderNo());
		//订单商品总金额
		float producCost = Float.valueOf(orderInfo.getProduct_cost());
		BigDecimal   bd2   =   new   BigDecimal(Math.abs(odbPrice - producCost));
		float   ft2   =   bd2.setScale(3,   BigDecimal.ROUND_HALF_UP).floatValue();
		if(ft2 > 0.01 ){
			request.setAttribute("checkOrder", 1);
			request.setAttribute("checkMessage", "此订单详情商品总值("+ odbPrice +")和订单表记录值("+ producCost +")不符");
			return false;
		}

		//3.优惠校检    商品总金额-coupon优惠-其他优惠-等级优惠-分享优惠-优惠金额（之前BIz等）-cash_back折扣 + serviceFee=实际支付金额    为正常订单

		double couponDiscount = orderInfo.getCoupon_discount();// coupon优惠
		double extraDiscount = orderInfo.getExtra_discount();// 其他优惠
		double gradeDiscount = orderInfo.getGradeDiscount();// 等级优惠
		double shareDiscount = orderInfo.getShare_discount();// 分享优惠
		double discountAmount = orderInfo.getDiscount_amount();// 优惠金额（之前BIz等）
		double cashBack = orderInfo.getCashback();// cash_back折扣
		double payPrice = orderInfo.getPay_price();//实际支付金额
		double serviceFee = Double.valueOf(orderInfo.getService_fee() ==null ? "0" : orderInfo.getService_fee());//服务费用
		double extraFreight= orderInfo.getExtra_freight();//额外运费
		double vatBalance = orderInfo.getVatBalance();//双清包税
		double actual_freight_c = orderInfo.getActual_freight_c();//未满足金额收取50或80美元
		String firstDiscountStr =orderInfo.getFirstdiscount();
		double firstDiscount = 0;
		if(org.apache.commons.lang3.StringUtils.isNotBlank(firstDiscountStr)){
			firstDiscount = Double.valueOf(firstDiscountStr);
		}

		//质检费
		double actual_lwh = 0;
		if(org.apache.commons.lang3.StringUtils.isNotBlank(orderInfo.getActual_lwh())){
			actual_lwh = Double.valueOf(orderInfo.getActual_lwh());
		}
		//手续费
		double processingfee = orderInfo.getProcessingfee();
		//会员费
		double memberFee = orderInfo.getMemberFee();

		double couponAmount = Double.parseDouble(orderInfo.getCouponAmount());

		// 会员费不算优惠金额,去掉
		double calculatePrice = odbPrice -couponDiscount -extraDiscount-gradeDiscount-shareDiscount-discountAmount
				-cashBack + serviceFee + extraFreight - firstDiscount + vatBalance + actual_freight_c
				+ actual_lwh + processingfee-couponAmount;

		BigDecimal bd3   =   new   BigDecimal(Math.abs(calculatePrice - payPrice));
		float ft3   =   bd3.setScale(3,   BigDecimal.ROUND_HALF_UP).floatValue();
		if(ft3 > 0.01 ){

			request.setAttribute("checkOrder", 1);
			request.setAttribute("checkMessage", "实际支付金额("+ payPrice +")不等于商品总金额减去优惠("+ BigDecimalUtil.truncateDouble(calculatePrice,2) +")");
			return false;
		}
		return true;
	}

	// 保存咨询问题 发送通知和 确认购买
	@RequestMapping(value = "/sendCutomers.do")
	public JsonResult sendCutomers(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String orderNo = null;
		String orderNo1 = request.getParameter("orderNo");
		int whichOne = Integer.parseInt(request.getParameter("whichOne"));
		int isDropship = Integer.parseInt(request.getParameter("isDropship"));// 获取到是否是isDropship订单
		// 0不是，1是
		if (isDropship == 1) {
			orderNo = orderNo1.substring(0, orderNo1.indexOf("_"));
		} else {
			orderNo = orderNo1;
		}
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		try {
			IOrderwsServer server = new OrderwsServer();
			String res = server.sendCutomers(serverName, serverPort, orderNo, whichOne, isDropship, orderNo1); // result:1
			json.setOk(true);
			json.setData(res);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error(e.getMessage());
			json.setOk(false);
			json.setMessage("sendCutomers error:" + e.getMessage());
		}
		return json;
	}

	// 后台发送邮件按钮-提醒支付运费
	@RequestMapping(value = "/sendEmailFright.do")
	public JsonResult sendEmailFright(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonResult json = new JsonResult();
		String orderNo = request.getParameter("orderNo");
		String remark = request.getParameter("remark");
		String price = request.getParameter("price");
		String copyEmail = request.getParameter("copyEmail").trim();
		String actual_ffreight = request.getParameter("actual_ffreight");// 实际运费
		String ys_ffreight = "";// 原先运费
		String currency = request.getParameter("currency");
		String pay_ffreight = request.getParameter("pay_ffreight");// 支付运费金额
		String weight = request.getParameter("weight");// 原本重量
		String actual_weight = request.getParameter("actual_weight");// 实际重量
		String arrive_time = request.getParameter("arrive_time");// 到货日期
		String transport_time = request.getParameter("transport_time");// 国际运输时间
		String userid = request.getParameter("userid");

		try {
			IOrderwsDao dao = new OrderwsDao();
			if (Utility.getStringIsNull(remark)) {
				ys_ffreight = dao.getOrder_reductionfreight(orderNo) + "";
				dao.upOrder_reductionfreight(orderNo, Utility.getIsDouble(price) ? Double.parseDouble(price) : 0,
						remark);
			}
			int res = 0;
			// 获取用户email
			IOrderSplitServer splitServer = new OrderSplitServer();
			String toEmail = splitServer.getUserEmailByUserName(Integer.parseInt(userid));
			//String uuid1 = UUIDUtil.getEffectiveUUID(0, toEmail);
			// String path1 = UUIDUtil.getAutoLoginPath("/individual/getCenter",
			// uuid1);
			StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
			sbBuffer.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='"
					+ AppConfig.ip_email + "/img/logo.png' ></img></a>");
			sbBuffer.append(
					"<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear " + toEmail + "</div>");
			double actual_ffreight_ = Utility.getIsDouble(actual_ffreight) ? Double.parseDouble(actual_ffreight) : 0;
			double actual_weight_ = Utility.getIsDouble(actual_weight) ? Double.parseDouble(actual_weight) : 0;
			double pay_ffreight_ = Utility.getIsDouble(pay_ffreight) ? Double.parseDouble(pay_ffreight) : 0;
			double weight_ = Utility.getIsDouble(weight) ? Double.parseDouble(weight) : 0;
			if (pay_ffreight_ > 0) {
				sbBuffer.append(
						"<br>Hello, your products are ready to ship, but you need to pay some additional shipping fee.<br>");
			} else {
				sbBuffer.append("<br>Hello, your products are ready to ship, please pay the shipping fee now.<br>");
			}
			sbBuffer.append(remark);
			double price_ = Utility.getIsDouble(price) ? Double.parseDouble(price) : 0;
			if (actual_weight_ > weight_) {
				sbBuffer.append("The original estimated shipping weight is " + weight + ", but the actual weight is "
						+ actual_weight + ".  So, the shipping cost has changed from " + currency + ys_ffreight + " to "
						+ currency + actual_ffreight + ".");
			}

			actual_ffreight_ = new BigDecimal(actual_ffreight_ - price_).setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			if (price_ > 0) {
				sbBuffer.append("We have also granted you an additional discount of " + currency + price_
						+ ".  This reduces shipping cost from " + currency + ys_ffreight + " to " + currency
						+ actual_ffreight_ + ".");
			}
			if (pay_ffreight_ > 0) {
				sbBuffer.append("<div>Shipping Fee Due:" + actual_ffreight_ + "</div>");
			} else {
				sbBuffer.append(
						"<div>You have paid " + currency + pay_ffreight_ + " of shipping cost previously</div>");
				double remainning = new BigDecimal(actual_ffreight_ - pay_ffreight_)
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				sbBuffer.append("<div>Remaining Shipping Fee Due:" + currency + remainning + "</div>");
			}
			sbBuffer.append("<div>The estimated transit time is " + transport_time
					+ " days, and you can expect to receive the package on " + arrive_time + "</div>");
			// 获取uuid拼接邮件
			String uuid = UUIDUtil.getEffectiveUUID(0, toEmail);
			String path = UUIDUtil.getAutoLoginPath(
					"/processesServlet?action=emailLink&className=OrderInfo&orderNo=" + orderNo, uuid);
			sbBuffer.append("<br>Click <a href='" + AppConfig.ip_email + path
					+ "'>here</a> to go to import-express.com to pay.</div>");
			// sbBuffer.append("<br>Click <a
			// href='"+AppConfig.ip_email+"/processesServlet?action=emailLink&className=OrderInfo&orderNo="+orderNo+"'>here</a>
			// to go to import-express.com to pay.</div>");
			sbBuffer.append(
					"<br><div style='font-weight: bolder;'>Best regards</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>");
			sbBuffer.append(
					"<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
			sbBuffer.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
			path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
			sbBuffer.append(
					"This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account "
							+ "<a style='color: #0070C0' href='" + AppConfig.ip_email + path + "'>here</a>.");
			sbBuffer.append("</div></div></div>");

			String sendemail = null;
			String pwd = null;
			if (Utility.getStringIsNull(copyEmail)) {
				IUserDao userDao = new com.cbt.processes.dao.UserDao();
				String[] adminEmail = userDao.getAdminUser(0, copyEmail, 0);
				if (adminEmail != null) {
					sendemail = adminEmail[0];
					pwd = adminEmail[1];
				}
			}
			res = SendEmail.send(sendemail, pwd, toEmail, sbBuffer.toString(),
					"Shipping fee needed for your Import-Express order!", "", orderNo, 1);
			json.setOk(true);
			json.setData(res);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error(e.getMessage());
			json.setOk(false);
			json.setMessage("sendEmailFright error:" + e.getMessage());
		}
		return json;
	}


	@RequestMapping(value = "/splitByNumPage")
	public String splitByNumPage(HttpServletRequest request, HttpServletResponse response) {

		try {
			String orderNo = request.getParameter("orderNo");
			if (StringUtils.isBlank(orderNo)) {
				request.setAttribute("isShow", 0);
				request.setAttribute("message", "获取订单号失败");
			} else {
				request.setAttribute("orderNo", orderNo);
			}
			// 订单信息
			DataSourceSelector.restore();
			OrderBean orderInfo = iOrderinfoService.getOrders(orderNo);
			// 订单商品详情
			List<OrderDetailsBean> odbList = iOrderinfoService.getOrdersDetails(orderNo);
			List<PurchaseInfoBean> purchaseInfoList = iPurchaseService.queryOrderProductSourceByOrderNo(orderNo);

			List<OrderDetailsBean> nwOdbList;
			if (purchaseInfoList != null && purchaseInfoList.size() > 0) {
				Set<Integer> hasPurchaseSet = purchaseInfoList.stream()
						.filter(e -> e.getConfirmUserId() != null && e.getConfirmUserId() > 0)
						.map(PurchaseInfoBean::getOdId).collect(toSet());
				nwOdbList = odbList.stream().filter(e -> hasPurchaseSet.contains(e.getId())).collect(Collectors.toList());
				request.setAttribute("odList", nwOdbList);
				purchaseInfoList.clear();
				hasPurchaseSet.clear();
				odbList.clear();
			} else {
				request.setAttribute("odList", odbList);
			}
			request.setAttribute("orderInfo", orderInfo);
			request.setAttribute("isShow", 1);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("isShow", 0);
			request.setAttribute("message", e.getMessage());
			LOG.error("splitByNumPage error:", e);
		}

		return "orderSplitNum";
	}

}
