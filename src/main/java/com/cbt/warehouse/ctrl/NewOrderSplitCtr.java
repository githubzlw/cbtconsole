package com.cbt.warehouse.ctrl;

import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pojo.Admuser;
import com.cbt.util.*;
import com.cbt.website.dao.*;
import com.cbt.website.service.IOrderSplitServer;
import com.cbt.website.service.OrderSplitServer;
import com.cbt.website.util.JsonResult;

import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import net.sf.json.JSONObject;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/orderSplit")
public class NewOrderSplitCtr {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(NewOrderSplitCtr.class);
	@Autowired
	private SendMailFactory sendMailFactory;
	/**
	 * 订单拆分(正常订单和Drop Ship订单)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/doSplit.do")
	@ResponseBody
	public JsonResult doSplit(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("ordersplit start");
		JsonResult json = new JsonResult();
		try {
			String orderno = request.getParameter("orderno");
			String odids = request.getParameter("odids");
			String state = request.getParameter("state");// 0取消1二次出货
			if (state == null || "".equals(state)) {
				json.setOk(false);
				json.setMessage("获取拆单状态失败");
			}

			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			if (admuserJson == null) {
				json.setOk(false);
				json.setMessage("用户未登陆");
				return json;
			} else {
				Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
				// 判断是否是Drop Ship订单，根据订单号获取订单信息
				OrderInfoDao orderInfoDao = new OrderInfoImpl();
				OrderBean orderBean = orderInfoDao.getOrderInfo(orderno, null);
				IOrderSplitServer splitServer = new OrderSplitServer();
				if (orderBean.getIsDropshipOrder() == 1) {
					// Drop Ship 拆单流程
					Map<String, String> map = splitServer.splitOrderShip(orderno, odids, orderBean.getUserid(),state);
					if("1".equals(map.get("res"))){
						json.setOk(true);
						json.setData(map.get("orderNoNew"));
					}else{
						json.setOk(false);
						json.setMessage(map.get("msg"));
					}
				} else {
					// 正常拆单流程
					// json = splitCommonOrder(orderno, odids, state);
					json = newSplitCommonOrder(admuser, orderno, odids, state);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("orderSplit error:" + e.getMessage());
			json.setOk(false);
			json.setMessage("orderSplit error:" + e.getMessage());
		}
		// LOG.info("ordersplit end");
		return json;
	}

	@SuppressWarnings("finally")
	private JsonResult splitCommonOrder(String orderNo, String odids, String state) {
		JsonResult json = new JsonResult();
		IOrderSplitDao dao = new OrderSplitDaoImpl();
		String nwOrderNo = "";

		/*
		 * 1.所有 有 采购链接的 商品 就 直接 转入一个 新订单，并且转状态到 采购中 2.已支付的产品金额按产品直接拆分 3.客户是以前付过
		 * 运费的，我们就按照 体积重量，直接拆分 这 两个订单 体积重量 = 长* 宽*高 (厘米)/5000 和 实际重量对比 取 大值
		 * 4.客户如果取消商品，而该订单 是 -split 的，就不再计算批量折扣 5.原订单的批量折扣金额， 按照价格比例分开 6.50美元的
		 * 运费credit的拆分
		 */
		try {
			// 查询订单和订单详情
			OrderBean orderBean = dao.getOrders(orderNo);
			String[] odidLst = odids.split("@");// 需要取消的商品order_details的id
			// 记录老订单拆分日志
			String info_log = ", product_cost=" + orderBean.getProduct_cost() + ",pay_price=" + orderBean.getPay_price()
					+ ",pay_price_tow=" + orderBean.getPay_price_tow() + ",pay_price_three="
					+ orderBean.getPay_price_three() + ",actual_ffreight=" + orderBean.getActual_ffreight()
					+ ",service_fee=" + orderBean.getService_fee() + ",order_ac=" + orderBean.getOrder_ac()
					+ ",discount_amount=" + orderBean.getDiscount_amount() + ",cashback=" + orderBean.getCashback()
					+ ",extra_freight=" + orderBean.getExtra_freight() + ",share_discount="
					+ orderBean.getShare_discount() + ",Coupon_discount=" + orderBean.getCoupon_discount()
					+ ",extra_discount=" + orderBean.getExtra_discount() + "state=" + orderBean.getState();// --cjc

			LOG.info("ordersplit info_log:" + info_log);

			dao.addOrderInfoLog(orderBean.getOrderNo(), info_log);
			List<OrderDetailsBean> orderDetails = dao.getOrdersDetails_split(orderNo);

			List<OrderDetailsBean> nwOrderDetails = new ArrayList<OrderDetailsBean>();
			List<Integer> goodsIds = new ArrayList<Integer>();
			// 判断是否获取到要拆分的商品ids
			if (odidLst.length > 0) {
				for (OrderDetailsBean odds : orderDetails) {
					for (String odid : odidLst) {
						if (odds.getId() == Integer.valueOf(odid)) {
							nwOrderDetails.add(odds);
							goodsIds.add(odds.getGoodsid());
							break;
						}
					}
				}

				if (nwOrderDetails.size() > 0) {
					// 生成另一个采购中订单
					// 修改已有货源订单详情的订单号
					String orderNo1 = null;
					if (orderNo.length() > 17) {
						OrderBean orderBean1 = null;
						if (orderNo.indexOf("_") > -1) {
							String[] n = orderNo.split("_");
							String orderNo_ = n[0];
							orderBean1 = dao.getOrders(orderNo_);
						}
						String maxSplitOrderNo = orderBean1.getMaxSplitOrder();
						if (maxSplitOrderNo.indexOf("_") > -1) {
							int splitIndex = Integer.parseInt(maxSplitOrderNo.split("_")[1]);
							String[] n = orderNo.split("_");
							orderNo1 = n[0];
							nwOrderNo = orderNo1 + "_" + (splitIndex + 1);
						}
					} else {
						nwOrderNo = orderNo + "_1";
						String maxSplitOrderNo = orderBean.getMaxSplitOrder();
						if (maxSplitOrderNo.indexOf("_") > -1) {
							int splitIndex = Integer.parseInt(maxSplitOrderNo.split("_")[1]);
							nwOrderNo = orderNo + "_" + (splitIndex + 1);
						}

					}

					// 开始执行拆单
					boolean isOk = true;
					StringBuffer ocBf = new StringBuffer();
					for (OrderDetailsBean nwOdDt : nwOrderDetails) {
						if (dao.orderSplitByOrderNo(orderBean.getUserid(), orderNo, nwOrderNo, nwOdDt.getGoodsid(),
								nwOdDt.getYourorder())) {
							ocBf.append("," + nwOdDt.getGoodsid());
							continue;
						} else {
							isOk = false;
							break;
						}
					}

					if (!isOk) {
						json.setOk(false);
						json.setMessage("拆分失败，已成功拆分商品：" + (ocBf.length() > 0 ? ocBf.toString().substring(1) : ""));
					} else {
						// 判断是否是取消状态,是取消,则更新新订单的状态
						if ("0".equals(state)) {
							// 执行退款操作:更新客户余额=新订单payprice;更新新订单状态为取消;
							// 新增客户余额变更记录表recharge_record;新增支付记录payment
							dao.cancelNewOrder(orderBean.getUserid(), nwOrderNo);
						} else {
							// 补充新的支付信息
							dao.insertIntoPayment(orderBean.getUserid(), nwOrderNo, orderNo);
						}
						// 更新订单的状态
						dao.checkAndUpdateOrderState(orderNo, nwOrderNo);
						// 更新新订单商品的入库信息,本地
						if (goodsIds.size() > 0) {
							dao.updateWarehouseInfo(orderNo, nwOrderNo, goodsIds);
						}

						json.setOk(true);
						json.setMessage("拆分成功");
						json.setData(nwOrderNo);
						if ("0".equals(state)) {
							// 拆单取消后取消的商品如果有使用库存则还原库存
							dao.cancelInventory(odidLst);
						}
					}
				} else {
					json.setOk(false);
					json.setMessage("拆分失败，匹配商品不成功");
				}

			} else {
				json.setOk(false);
				json.setMessage("获取拆分商品失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("OrderSplitServer-Exception:", e);
			json.setOk(false);
			json.setMessage("拆分失败，原因：" + e.getMessage());
		} finally {
			return json;
		}
	}

	@SuppressWarnings("finally")
	private JsonResult newSplitCommonOrder(Admuser admuser, String orderNo, String odids, String state) {

		// 1.订单拆单前，做数据保存信息
		// 2.复制订单信息充当临时订单
		// 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库
		// 4.执行拆单操作
		// 5.执行完成后，给出执行的结果并保存数据库
		// 6.如果是取消商品进余额，则调用统一接口进行客户余额变更

		JsonResult json = new JsonResult();
		IOrderSplitDao splitDao = new OrderSplitDaoImpl();
		String nwOrderNo = "";
		try {
			// 查询订单和订单详情
			OrderBean orderBean = splitDao.getOrders(orderNo);
			String[] odidLst = odids.split("@");// 需要取消的商品order_details的id
			// 1.拆单之前保存订单原始信息 log
			boolean isSuccess = saveOrderInfoBeforeSplitOrder(orderBean, orderNo, odids, admuser);
			if (isSuccess) {
				// 2.复制订单信息充当临时订单
				OrderBean orderBeanTemp = (OrderBean) orderBean.clone();
				// 获取所有订单详情的信息
				List<OrderDetailsBean> orderDetails = splitDao.getOrdersDetails_split(orderNo);
				List<OrderDetailsBean> nwOrderDetails = new ArrayList<OrderDetailsBean>();
				List<Integer> goodsIds = new ArrayList<Integer>();
				// 判断是否获取到要拆分的商品ids
				if (odidLst.length > 0) {
					double totalPayPriceOld = orderBean.getPay_price();// 原订单支付金额
					double totalGoodsCostOld = 0;// 原订单商品总价
					for (OrderDetailsBean odds : orderDetails) {
						for (String odid : odidLst) {
							if (odds.getId() == Integer.valueOf(odid)) {
								nwOrderDetails.add(odds);
								goodsIds.add(odds.getGoodsid());
								break;
							}
						}
						totalGoodsCostOld += Double.valueOf(odds.getGoodsprice()) * odds.getYourorder();
					}
					// 判断传递的odids有效
					if (nwOrderDetails.size() == odidLst.length && !(totalPayPriceOld <= 0 || totalGoodsCostOld <= 0)) {
						// 3.计算预期结果并保存和拆单操作
						calculateExpectedResult(json, nwOrderDetails, orderNo, nwOrderNo, orderBean, totalGoodsCostOld,
								totalPayPriceOld, orderBeanTemp, admuser, state, odidLst, goodsIds);
						//取消订单后商品进入库存中
						//判断该订单是否为测试订单如果是则不入库存
//						boolean flag=splitDao.checkTestOrder(odidLst[0]);
//						if(flag){
							if ("0".equals(state)) {
								for (String odid : odidLst) {
									splitDao.addInventory(odid,"拆单取消库存");
								}
							}
//						}
					} else {
						json.setOk(false);
						if (nwOrderDetails.size() == 0 || nwOrderDetails.size() != odidLst.length) {
							json.setMessage("拆分失败，匹配商品不成功");
						} else if (totalPayPriceOld <= 0) {
							json.setMessage("拆分失败，此订单支付金额小于等于0");
						} else if (totalGoodsCostOld <= 0) {
							json.setMessage("拆分失败，此订单总价小于等于0");
						}
					}
				} else {
					json.setOk(false);
					json.setMessage("获取需要拆分的商品失败");
				}
			} else {
				json.setOk(false);
				json.setMessage("保存日志失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("OrderSplitServer-Exception:", e);
			json.setOk(false);
			json.setMessage("拆分失败，原因：" + e.getMessage());
		} finally {
			return json;
		}
	}

	/**
	 * 拆单之前保存订单原始信息
	 */
	private boolean saveOrderInfoBeforeSplitOrder(OrderBean orderBean, String orderNo, String odids, Admuser admuser) {
		IOrderSplitDao splitDao = new OrderSplitDaoImpl();
		// 记录老订单拆分日志
		String info_log = ", product_cost=" + orderBean.getProduct_cost() + ",pay_price=" + orderBean.getPay_price()
				+ ",pay_price_tow=" + orderBean.getPay_price_tow() + ",pay_price_three="
				+ orderBean.getPay_price_three() + ",actual_ffreight=" + orderBean.getActual_ffreight()
				+ ",service_fee=" + orderBean.getService_fee() + ",order_ac=" + orderBean.getOrder_ac()
				+ ",discount_amount=" + orderBean.getDiscount_amount() + ",cashback=" + orderBean.getCashback()
				+ ",extra_freight=" + orderBean.getExtra_freight() + ",share_discount=" + orderBean.getShare_discount()
				+ ",Coupon_discount=" + orderBean.getCoupon_discount() + ",extra_discount="
				+ orderBean.getExtra_discount() + "state=" + orderBean.getState() 
				+ "vatBalance = "+ orderBean.getVatBalance(); // --cjc
		// 1.订单拆单前，做数据保存信息
		LOG.info("ordersplit orderNo:" + orderNo + "; info_log:[" + info_log + "];");
		LOG.info("ordersplit odids:[" + odids + "];");
		info_log = "";
		return splitDao.addOrderInfoAndPaymentLog(orderBean.getOrderNo(), admuser, 0);
	}

	/**
	 * 计算预期结果并保存
	 */
	private void calculateExpectedResult(JsonResult json, List<OrderDetailsBean> nwOrderDetails, String orderNo,
			String nwOrderNo, OrderBean orderBean, double totalGoodsCostOld, double totalPayPriceOld,
			OrderBean orderBeanTemp, Admuser admuser, String state, String[] odidLst, List<Integer> goodsIds) {
		IOrderSplitDao splitDao = new OrderSplitDaoImpl();
		// 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库
		double totalGoodsCostNew = 0;// 新的订单商品总价
		for (OrderDetailsBean nwOdDt : nwOrderDetails) {
			totalGoodsCostNew += Double.valueOf(nwOdDt.getGoodsprice()) * nwOdDt.getYourorder();
		}
		if (totalGoodsCostNew > 0) {
			// 拆单新生成的订单号
			String orderNo1 = null;
			if (orderNo.length() > 17) {
				OrderBean orderBean1 = null;
				if (orderNo.indexOf("_") > -1) {
					String[] n = orderNo.split("_");
					String orderNo_ = n[0];
					orderBean1 = splitDao.getOrders(orderNo_);
				}
				String maxSplitOrderNo = orderBean1.getMaxSplitOrder();
				if (maxSplitOrderNo.indexOf("_") > -1) {
					int splitIndex = Integer.parseInt(maxSplitOrderNo.split("_")[1]);
					String[] n = orderNo.split("_");
					orderNo1 = n[0];
					nwOrderNo = orderNo1 + "_" + (splitIndex + 1);
				}
			} else {
				nwOrderNo = orderNo + "_1";
				String maxSplitOrderNo = orderBean.getMaxSplitOrder();
				if (maxSplitOrderNo.indexOf("_") > -1) {
					int splitIndex = Integer.parseInt(maxSplitOrderNo.split("_")[1]);
					nwOrderNo = orderNo + "_" + (splitIndex + 1);
				}
			}
			// 支付金额拆分逻辑：拆分比例=拆单商品的总价/原订单所有商品的总价
			// 所有的折扣金额都按照计算的拆分比例计算，新生成的订单支付金额也是按照比例分割
			double splitRatio = totalGoodsCostNew / totalGoodsCostOld;// 拆分比例
			double totalPayPriceNew = totalPayPriceOld * splitRatio;// 新的订单支付金额
			double coupon_discount_old = orderBean.getCoupon_discount();// coupon优惠
			double coupon_discount_new = coupon_discount_old * splitRatio;// 新订单coupon优惠
			double extra_discount_old = orderBean.getExtra_discount();// 其他优惠
			double extra_discount_new = extra_discount_old * splitRatio;// 新订单其他优惠
			double grade_discount_old = orderBean.getGradeDiscount();// 等级优惠
			double grade_discount_new = grade_discount_old * splitRatio;// 新订单等级优惠
			double share_discount_old = orderBean.getShare_discount();// 分享优惠
			double share_discount_new = share_discount_old * splitRatio;// 新订单分享优惠
			double discount_amount_old = orderBean.getDiscount_amount();// 优惠金额（之前BIz等）
			double discount_amount_new = discount_amount_old * splitRatio;// 新订单优惠金额（之前BIz等）
			double cash_back_old = orderBean.getCashback();// cash_back折扣
			double cash_back_new = cash_back_old * splitRatio;// 新订单cash_back折扣
			double extra_freight_old = orderBean.getExtra_freight();// 额外运费
			double extra_freight_new = extra_freight_old * splitRatio;//新订单额外运费
			
			double vatBalanceOld = orderBean.getVatBalance();//双清包税价格
			double vatBalanceNew = vatBalanceOld * splitRatio;//新双清包税价格
			// 新生成订单信息
			OrderBean odbeanNew = new OrderBean();
			odbeanNew.setVatBalance(vatBalanceNew);
			odbeanNew.setUserid(orderBean.getUserid());
			odbeanNew.setOrderNo(nwOrderNo);
			odbeanNew.setExchange_rate(orderBean.getExchange_rate());
			odbeanNew.setCoupon_discount(genDoubleWidthTwoDecimalPlaces(coupon_discount_new));
			odbeanNew.setExtra_discount(genDoubleWidthTwoDecimalPlaces(extra_discount_new));
			odbeanNew.setGradeDiscount(
					Float.valueOf(String.valueOf(genDoubleWidthTwoDecimalPlaces(grade_discount_new))));
			odbeanNew.setShare_discount(genDoubleWidthTwoDecimalPlaces(share_discount_new));
			odbeanNew.setDiscount_amount(genDoubleWidthTwoDecimalPlaces(discount_amount_new));
			odbeanNew.setProduct_cost(String.valueOf(genDoubleWidthTwoDecimalPlaces(totalGoodsCostNew)));
			odbeanNew.setCashback(cash_back_new);
			odbeanNew.setExtra_freight(extra_freight_new);
			odbeanNew.setOrderDetail(nwOrderDetails);
			// 计算新订单支付金额
			totalPayPriceNew = totalGoodsCostNew - coupon_discount_new - share_discount_new - discount_amount_new
					- grade_discount_new-cash_back_new + extra_freight_new;
			odbeanNew.setPay_price(genDoubleWidthTwoDecimalPlaces(totalPayPriceNew));
			// 原始订单减去拆分比例后的值
			orderBeanTemp.setVatBalance(vatBalanceOld - vatBalanceNew);
			orderBeanTemp.setCoupon_discount(genDoubleWidthTwoDecimalPlaces(coupon_discount_old - coupon_discount_new));
			orderBeanTemp.setExtra_discount(genDoubleWidthTwoDecimalPlaces(extra_discount_old - extra_discount_new));
			orderBeanTemp.setGradeDiscount(Float
					.valueOf(String.valueOf(genDoubleWidthTwoDecimalPlaces(grade_discount_old - grade_discount_new))));
			orderBeanTemp.setShare_discount(genDoubleWidthTwoDecimalPlaces(share_discount_old - share_discount_new));
			orderBeanTemp.setDiscount_amount(genDoubleWidthTwoDecimalPlaces(discount_amount_old - discount_amount_new));
			orderBeanTemp.setProduct_cost(
					String.valueOf(genDoubleWidthTwoDecimalPlaces(totalGoodsCostOld - totalGoodsCostNew)));
			orderBeanTemp.setPay_price(genDoubleWidthTwoDecimalPlaces(totalPayPriceOld - totalPayPriceNew));
			orderBeanTemp.setCashback(genDoubleWidthTwoDecimalPlaces(cash_back_old - cash_back_new));
			orderBeanTemp.setExtra_freight(genDoubleWidthTwoDecimalPlaces(extra_freight_old - extra_freight_new));
			// 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库(保存预期结果)
			List<OrderBean> orderBeans = new ArrayList<OrderBean>();
			orderBeans.add(odbeanNew);
			orderBeans.add(orderBeanTemp);
			boolean success = splitDao.saveOrderInfoLogByList(orderBeans, admuser);
			if (success) {
				// 开始拆单操作
				doSplitOrderAction(json, nwOrderDetails, orderNo, nwOrderNo, orderBeanTemp, odbeanNew, admuser, state,
						odidLst, goodsIds, (float) totalPayPriceNew);
			} else {
				json.setOk(false);
				json.setMessage("保存拆单信息失败，程序终止执行");
			}
		} else {
			json.setOk(false);
			json.setMessage("拆分失败，拆分商品商品总价为0");
		}
	}

	/**
	 * 真正的拆单操作
	 */
	private void doSplitOrderAction(JsonResult json, List<OrderDetailsBean> nwOrderDetails, String orderNo,
			String nwOrderNo, OrderBean orderBeanTemp, OrderBean odbeanNew, Admuser admuser, String state,
			String[] odidLst, List<Integer> goodsIds, float totalPayPriceNew) {

		IOrderSplitDao splitDao = new OrderSplitDaoImpl();
		// 4.执行拆单操作
		// 开始执行拆单
		boolean isOk = splitDao.newOrderSplitFun(orderBeanTemp, odbeanNew, nwOrderDetails,state);
		if (!isOk) {
			json.setOk(false);
			json.setMessage("拆分失败请重试");
		} else {

			// 更新订单的状态
			splitDao.checkAndUpdateOrderState(orderNo, nwOrderNo);
			// 更新新订单商品的入库信息,本地
			if (goodsIds.size() > 0) {
				splitDao.updateWarehouseInfo(orderNo, nwOrderNo, goodsIds);
			}

			// 判断是否是取消状态,是取消,则更新新订单的状态
			if ("0".equals(state)) {
				// 执行退款操作:更新客户余额=新订单payprice;更新新订单状态为取消;
				// 新增客户余额变更记录表recharge_record;新增支付记录payment
				splitDao.cancelNewOrder(orderBeanTemp.getUserid(), nwOrderNo);
				// 拆单取消后取消的商品如果有使用库存则还原库存
				splitDao.cancelInventory(odidLst);
				// 5.如果是取消商品进余额，则调用统一接口进行客户余额变更
				ChangUserBalanceDao balanceDao = new ChangUserBalanceDaoImpl();
				balanceDao.changeBalance(orderBeanTemp.getUserid(), genFloatWidthTwoDecimalPlaces(totalPayPriceNew), 1,
						1, nwOrderNo, "", admuser.getId());
			}
			// 6.执行完成后，给出执行的结果并保存数据库
			splitDao.addOrderInfoAndPaymentLog(nwOrderNo, admuser, 1);

			json.setOk(true);
			json.setMessage("拆分成功");
			json.setData(nwOrderNo);
		}

	}

	/**
	 * 生成两位小数的double类型数据
	 * 
	 * @param numVal
	 * @return
	 */
	private double genDoubleWidthTwoDecimalPlaces(double numVal) {
		BigDecimal bd = new BigDecimal(numVal);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 生成两位小数的float类型数据
	 * 
	 * @param numVal
	 * @return
	 */
	private float genFloatWidthTwoDecimalPlaces(float numVal) {
		BigDecimal bd = new BigDecimal(numVal);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 获取订单拆分后的邮件信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/genOrderSplitEmail.do")
	@ResponseBody
	public String genOrderSplitEmail(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("genOrderSplitEmail start sendEmailInfo");
		Map<String, Object> model =new HashMap<>();
		String message = "success!";
		String email = request.getParameter("email");
		if(StringUtils.isNotBlank(email)){
			String regex = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(email);
			while(m.find()) {
				email = m.group();
			}
		}
		try {
			String remark = org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("remark ")) ? request.getParameter("remark") : "";
			model.put("remark",remark);
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String odids = request.getParameter("odids");
				String orderNo = request.getParameter("orderno");
				String ordernoNew = request.getParameter("ordernoNew");

				String time = request.getParameter("time");
				String time_ = request.getParameter("time_");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				int state = Integer.parseInt(request.getParameter("state"));

				IOrderSplitDao orderSplitDao = new OrderSplitDaoImpl();
				// 老订单的订单信息
				OrderBean oldOrderBean = orderSplitDao.getOrderInfo(orderNo);

				OrderBean nwOrderBean = new OrderBean();
				nwOrderBean.setOrderNo(ordernoNew);

				Calendar calendar = Calendar.getInstance();
				String expect_arrive_time_ = "";
				String expect_arrive_time = "";
				if (state == 1) {
					// 获取运输方式和对应的运输时间
					String mode_transport = oldOrderBean.getMode_transport();
					int mode_transport_day = 0;
					if (mode_transport != null && mode_transport.indexOf("@") > -1) {
						String[] mode_transports = mode_transport.split("@");
						if (mode_transports.length > 3) {
							mode_transport = mode_transport.split("@")[1];
							if (Utility.getStringIsNull(mode_transport)) {
								if (mode_transport.indexOf("-") > -1) {
									mode_transport = mode_transport.split("-")[1];
								}
								mode_transport_day = Integer.parseInt(mode_transport.replace("Days", "").trim());
							}
						}
					}
					if (oldOrderBean.getOrderNo().indexOf("_") > -1) {
						if (mode_transport_day != 0 && Utility.getStringIsNull(time_)) {
							calendar.setTime(sdf.parse(time_));
							calendar.set(Calendar.DAY_OF_MONTH,
									calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
							expect_arrive_time_ = sdf.format(calendar.getTime());
						}
					} else {
						if (mode_transport_day != 0 && Utility.getStringIsNull(time)) {
							calendar.setTime(sdf.parse(time));
							calendar.set(Calendar.DAY_OF_MONTH,
									calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
							expect_arrive_time = sdf.format(calendar.getTime());
						}
					}
				}

				// 获取用户uuid和个人中心自动登录路径
				String uuid = UUIDUtil.getEffectiveUUID(oldOrderBean.getUserid(), "");
				String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
				request.setAttribute("autoUrl", url);
				String autoUrl = "http://www.import-express.com"+url;
				model.put("autoUrl", autoUrl);
				// 获取用户email
				IOrderSplitServer splitServer = new OrderSplitServer();
				String userEmailByUserName = splitServer.getUserEmailByUserName(oldOrderBean.getUserid());
				request.setAttribute("email", userEmailByUserName);
				model.put("email", userEmailByUserName);
				List<Object[]> orderDetails = orderSplitDao.queryLocalOrderDetails(orderNo);
				List<Object[]> nwOrderDetails = new ArrayList<Object[]>();// 拆分的订单
				List<Object[]> oldOrderDetails = new ArrayList<Object[]>();// 剩余的订单
				// 发送邮件开始
				String[] odidLst = odids.split("@");
				// 判断是否获取到要拆分的商品ids
				if (odidLst.length > 0) {
					for (int i = 0; i < orderDetails.size(); i++) {
						boolean isNew = false;
						for (String odid : odidLst) {
							if (orderDetails.get(i)[1].toString().equals(odid)) {
								nwOrderDetails.add(orderDetails.get(i));
								isNew = true;
								break;
							}
						}
						if (!isNew) {
							oldOrderDetails.add(orderDetails.get(i));
						}
					}
				}

				float product_cost = 0;
				if (nwOrderDetails.size() > 0) {
					for (int k = 0; k < nwOrderDetails.size(); k++) {
						product_cost += Float.valueOf(nwOrderDetails.get(k)[3].toString());

					}
				}

				nwOrderBean.setProduct_cost(String.valueOf(product_cost));
				// 发送邮件开始
				request.setAttribute("details", oldOrderDetails);
				request.setAttribute("details_", nwOrderDetails);
				model.put("details", oldOrderDetails);
				model.put("details_", nwOrderDetails);
				if (state == 1) {
					request.setAttribute("expect_arrive_time_", expect_arrive_time_);
					request.setAttribute("expect_arrive_time", expect_arrive_time);
					request.setAttribute("orderbean", oldOrderBean);
					request.setAttribute("orderbean_", nwOrderBean);
					model.put("expect_arrive_time_", expect_arrive_time_);
					model.put("expect_arrive_time", expect_arrive_time);
					model.put("orderbean", oldOrderBean);
					model.put("orderbean_", nwOrderBean);
				} else {
					request.setAttribute("orderbean_", nwOrderBean);
					model.put("orderbean_", nwOrderBean);
				}
				request.setAttribute("currency", oldOrderBean.getCurrency());
				model.put("currency", oldOrderBean.getCurrency());
			} else {

				String orderNo = request.getParameter("orderno");
				String ordernoNew = request.getParameter("ordernoNew");
				String time = request.getParameter("time");
				String time_ = request.getParameter("time_");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				int state = Integer.parseInt(request.getParameter("state"));
				model.put("orderno",orderNo);
				model.put("ordernoNew",ordernoNew);
				model.put("time",time);
				model.put("time_",time_);
				model.put("state",state);
				IOrderSplitServer splitServer = new OrderSplitServer();
				String[] orderNos;
				if (state == 1) {
					orderNos = new String[] { orderNo, ordernoNew };
				} else {
					orderNos = new String[] { ordernoNew };
				}
				List<OrderBean> orderBeans = splitServer.getSplitOrder(orderNos);
				Calendar calendar = Calendar.getInstance();
				String expect_arrive_time_ = "";
				String expect_arrive_time = "";
				OrderBean obBean = new OrderBean();
				OrderBean obBean_ = new OrderBean();
				if (state == 1) {
					for (int i = 0; i < orderBeans.size(); i++) {
						// 获取运输方式和对应的运输时间
						String mode_transport = orderBeans.get(i).getMode_transport();
						int mode_transport_day = 0;
						if (mode_transport != null && mode_transport.indexOf("@") > -1) {
							String[] mode_transports = mode_transport.split("@");
							if (mode_transports.length > 3) {
								mode_transport = mode_transport.split("@")[1];
								if (Utility.getStringIsNull(mode_transport)) {
									if (mode_transport.indexOf("-") > -1) {
										mode_transport = mode_transport.split("-")[1];
									}
									mode_transport_day = Integer.parseInt(mode_transport.replace("Days", "").trim());
								}
							}
						}
						if (orderBeans.get(i).getOrderNo().indexOf("_") > -1) {
							if (mode_transport_day != 0 && Utility.getStringIsNull(time_)) {
								calendar.setTime(sdf.parse(time_));
								calendar.set(Calendar.DAY_OF_MONTH,
										calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
								expect_arrive_time_ = sdf.format(calendar.getTime());
							}
							if(orderNo.equals(orderBeans.get(i).getOrderNo())){
								obBean = orderBeans.get(i);
							}else{
								obBean_ = orderBeans.get(i);
							}
						} else {
							if (mode_transport_day != 0 && Utility.getStringIsNull(time)) {
								calendar.setTime(sdf.parse(time));
								calendar.set(Calendar.DAY_OF_MONTH,
										calendar.get(Calendar.DAY_OF_MONTH) + mode_transport_day);
								expect_arrive_time = sdf.format(calendar.getTime());
							}
							obBean = orderBeans.get(i);
						}
					}
				} else {
					obBean_ = orderBeans.get(0);
				}

				// 获取用户uuid和个人中心自动登录路径
				String uuid = UUIDUtil.getEffectiveUUID(orderBeans.get(0).getUserid(), "");
				String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
				request.setAttribute("autoUrl", url);
				// 获取用户email
				request.setAttribute("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
				model.put("autoUrl", url);
				model.put("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
				List<Object[]> orderDetails = new ArrayList<Object[]>();
				if (state == 1) {
					orderDetails = splitServer.getSplitOrderDetails(orderNos);
				} else {
					orderDetails = splitServer.getOrderDetails(orderNos);
				}
				List<Object[]> details_ = new ArrayList<Object[]>();// 拆分的订单
				List<Object[]> details = new ArrayList<Object[]>();// 剩余的订单
				// 发送邮件开始
				for (int i = 0; i < orderDetails.size(); i++) {
					if ((orderDetails.get(i)[0].toString().indexOf("_") > -1)) {
						if (orderNo.length() > 17) {
							if (!orderDetails.get(i)[0].equals(ordernoNew)) {
								details.add(orderDetails.get(i));
							} else {
								details_.add(orderDetails.get(i));
							}
						} else {
							details_.add(orderDetails.get(i));
						}
					} else {
						details.add(orderDetails.get(i));
					}
				}

				request.setAttribute("details", details);
				request.setAttribute("details_", details_);
				model.put("details", details);
				model.put("details_", details_);
				if (state == 1) {
					request.setAttribute("expect_arrive_time_", expect_arrive_time_);
					request.setAttribute("expect_arrive_time", expect_arrive_time);
					request.setAttribute("orderbean", obBean);
					request.setAttribute("orderbean_", obBean_);
					model.put("expect_arrive_time_", expect_arrive_time_);
					model.put("expect_arrive_time", expect_arrive_time);
					model.put("orderbean", obBean);
					model.put("orderbean_", obBean_);
					model.put("title","ImportExpress Mail Dismantling");
					model.put("message","");
				} else {
					//针对取消的订单计算折扣金额

					double coupon_discount = obBean_.getCoupon_discount();// coupon优惠
					double extra_discount = obBean_.getExtra_discount();// 其他优惠
					double grade_discount = obBean_.getGradeDiscount();// 等级优惠
					double share_discount = obBean_.getShare_discount();// 分享优惠
					double discount_amount = obBean_.getDiscount_amount();// 优惠金额（之前BIz等）
					double cash_back = obBean_.getCashback();// cash_back折扣

					double extra_freight = obBean_.getExtra_freight();// 额外运费
					double vatBalance = obBean_.getVatBalance();//双清包税价格

					double totalDisCount = coupon_discount + extra_discount + grade_discount + share_discount + discount_amount + cash_back;
					if(totalDisCount > 0.01){
						request.setAttribute("totalDisCount", orderBeans.get(0).getCurrency() + " -" + (BigDecimalUtil.truncateDouble(totalDisCount,2)));
						model.put("totalDisCount", orderBeans.get(0).getCurrency() + " -" + (BigDecimalUtil.truncateDouble(totalDisCount,2)));
					}else{
						request.setAttribute("totalDisCount", "--");
						model.put("totalDisCount", "--");
					}

					double totalExtraFree = extra_freight + vatBalance;
					if(totalExtraFree > 0.01){
						request.setAttribute("totalExtraFree", orderBeans.get(0).getCurrency() + " " + BigDecimalUtil.truncateDouble(totalExtraFree,2));
						model.put("totalExtraFree", orderBeans.get(0).getCurrency() + " " + BigDecimalUtil.truncateDouble(totalExtraFree,2));
					}else{
						request.setAttribute("totalExtraFree", "--");
						model.put("totalExtraFree", "--");
					}
					request.setAttribute("orderbean_", obBean_);
					model.put("orderbean_", obBean_);
					model.put("title","Your Import Express order was partially cancelled");
					model.put("message",obBean_.getOrderNo());
				}
				request.setAttribute("currency", orderBeans.get(0).getCurrency());
				model.put("currency", orderBeans.get(0).getCurrency());
				String liveChatLink = "http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=496777&configID=70901&lan=en&jid=4818862369&enterurl=http%3A%2F%2Fwww.import-express.com%2Fcbtconsole%2Fapa%2Fcontact.html&amp;timestamp=1441622560799&amp;pagereferrer=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2F&amp;firstEnterUrl=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2Fcbtconsole%2Fapa%2Fcontact%2Ehtml&amp;pagetitle=Customer+Service";
				model.put("here",liveChatLink);
			}
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(model);
			String modeStr = jsonObject.toString();
			try {
				sendMailFactory.sendMail(String.valueOf(model.get("email")), email, "Due to supply reasons, we can only send your order partially at first.", model, TemplateType.DISMANTLING);
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("genOrderSplitEmail: email:"+model.get("email")+" model_json:"+ modeStr +" e.message:"+ e.getMessage());
				message = "Failed to send mail, please contact the developer by screen, thank you！"+ e.getMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("genOrderSplitEmail:" + e.getMessage());
		}
		LOG.info("getOrderSplit sendEmailInfo end");
		return message;
	}
	@RequestMapping("/sendEmailTest")
	@ResponseBody
	public  String sendEmailTest(){
		Map<String, Object> model =new HashMap<>();
		model.put("email","saycjc@outlook.com");
		model.put("name", "saycjc");
		model.put("pass", "111");
		model.put("activeLink", "111");
		model.put("here", "111");
		//sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "Due to supply reasons, we can only send your order partially at first.", model, TemplateType.WELCOME);
		//发送拆单
		String modelStr ="{\"here\":\"http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=496777&configID=70901&lan=en&jid=4818862369&enterurl=http%3A%2F%2Fwww.import-express.com%2Fcbtconsole%2Fapa%2Fcontact.html&amp;timestamp=1441622560799&amp;pagereferrer=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2F&amp;firstEnterUrl=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2Fcbtconsole%2Fapa%2Fcontact%2Ehtml&amp;pagetitle=Customer+Service\",\"orderno\":\"QC13519415298019\",\"time_\":\"2018-12-13\",\"remark\":\"\",\"title\":\"ImportExpress Mail Dismantling\",\"message\":\"\",\"orderbean\":{\"actualPay\":0,\"actual_allincost\":0,\"actual_ffreight\":\"0\",\"actual_freight_c\":0,\"actual_lwh\":\"\",\"actual_volume\":\"\",\"actual_weight\":\"\",\"actual_weight_estimate\":0,\"address\":null,\"address2\":\"\",\"addressid\":0,\"addresss\":\"\",\"adminemail\":\"\",\"adminid\":0,\"adminname\":\"\",\"applicable_credit\":0,\"arrive_time\":\"\",\"backFlag\":\"\",\"businessName\":\"\",\"buyAmount\":\"\",\"buycount\":\"\",\"buyid\":0,\"buyuser\":\"\",\"cancelFlag\":0,\"cancel_obj\":0,\"cashback\":0,\"cg\":0,\"chaOrderNo\":\"\",\"changenum\":0,\"checked\":\"\",\"checkeds\":0,\"client_update\":0,\"comformFlag\":0,\"complain\":0,\"count\":0,\"countOd\":\"\",\"country\":\"\",\"countryName\":\"\",\"countryNameCN\":\"\",\"coupon_discount\":0,\"createtime\":\"2018-12-13 17:42:40.0\",\"currency\":\"USD\",\"custom_discuss_other\":\"\",\"del_state\":0,\"deliver\":0,\"deliveryTime\":3,\"details_number\":8,\"details_pay\":\"\",\"discount_amount\":0,\"domestic_freight\":\"\",\"dropShipList\":\"\",\"dropShipState\":\"\",\"dzConfirmname\":\"\",\"dzConfirmtime\":\"\",\"dzId\":0,\"dzOrderno\":\"\",\"email\":\"\",\"esBuyPrice\":\"\",\"exchange_rate\":\"\",\"expect_arrive_time\":\"\",\"expressNo\":\"\",\"extra_discount\":0,\"extra_freight\":0.12,\"firstdiscount\":\"\",\"foreign_freight\":\"0\",\"free_shipping\":0,\"freightFee\":0,\"grade\":\"\",\"gradeDiscount\":0,\"gradeName\":\"\",\"id\":0,\"ip\":\"192.168.1.249\",\"isDropshipOrder\":0,\"maxSplitOrder\":\"\",\"memberFee\":0,\"mode_transport\":\"EPACKET (USPS)@9-15@USA@all\",\"new_zid\":\"\",\"no_checked\":\"\",\"odcount\":0,\"oldValue\":\"\",\"orderDetail\":[],\"orderFlag\":0,\"orderNo\":\"QC13519415298019\",\"orderNumber\":false,\"orderRemark\":\"\",\"order_ac\":0,\"order_count\":0,\"ordernum\":0,\"packag_number\":0,\"package_style\":0,\"payFlag\":0,\"payUserName\":\"\",\"pay_price\":32.74,\"pay_price_three\":\"0.0\",\"pay_price_tow\":\"0\",\"pay_type\":\"\",\"paystatus\":\"\",\"paytypes\":\"\",\"phonenumber\":\"\",\"problem\":\"\",\"processingfee\":0,\"product_cost\":\"32.49\",\"purchase\":0,\"purchase_number\":0,\"recipients\":\"\",\"remaining_price\":0,\"reminded\":[],\"rk\":0,\"ropType\":0,\"server_update\":0,\"service_fee\":\"0.0\",\"share_discount\":0,\"spider\":[],\"state\":5,\"stateDesc\":\"确认价格中\",\"statename\":\"\",\"storagetime\":\"\",\"street\":\"\",\"svolume\":\"\",\"total\":0,\"transport_time\":\"\",\"userEmail\":\"\",\"userName\":\"\",\"userid\":1019075,\"vatBalance\":0,\"volumeweight\":\"\",\"yhCount\":0,\"yourorder\":\"\",\"zipcode\":\"\"},\"expect_arrive_time\":\"\",\"ordernoNew\":\"QC13519415298019_6\",\"details_\":[[\"QC13519415298019_6\",259121,1,\"22.43\",\"Spring Long Sleeve Miss KTV Dress Night Club Sexy Buttock Cover Skirt With Slim Button And Fishtail Skirt\",\"https://img1.import-express.com/importcsvimg/img/546718222139/3954913382_1799662277.400x400.jpg\",\"Color:Black@32162,Size:M@4502\",null]],\"orderbean_\":{\"actualPay\":0,\"actual_allincost\":0,\"actual_ffreight\":\"0\",\"actual_freight_c\":0,\"actual_lwh\":\"\",\"actual_volume\":\"\",\"actual_weight\":\"\",\"actual_weight_estimate\":0,\"address\":null,\"address2\":\"\",\"addressid\":0,\"addresss\":\"\",\"adminemail\":\"\",\"adminid\":0,\"adminname\":\"\",\"applicable_credit\":0,\"arrive_time\":\"\",\"backFlag\":\"\",\"businessName\":\"\",\"buyAmount\":\"\",\"buycount\":\"\",\"buyid\":0,\"buyuser\":\"\",\"cancelFlag\":0,\"cancel_obj\":0,\"cashback\":0,\"cg\":0,\"chaOrderNo\":\"\",\"changenum\":0,\"checked\":\"\",\"checkeds\":0,\"client_update\":0,\"comformFlag\":0,\"complain\":0,\"count\":0,\"countOd\":\"\",\"country\":\"\",\"countryName\":\"\",\"countryNameCN\":\"\",\"coupon_discount\":0,\"createtime\":\"2018-12-13 17:42:40.0\",\"currency\":\"USD\",\"custom_discuss_other\":\"\",\"del_state\":0,\"deliver\":0,\"deliveryTime\":3,\"details_number\":1,\"details_pay\":\"\",\"discount_amount\":0,\"domestic_freight\":\"\",\"dropShipList\":\"\",\"dropShipState\":\"\",\"dzConfirmname\":\"\",\"dzConfirmtime\":\"\",\"dzId\":0,\"dzOrderno\":\"\",\"email\":\"\",\"esBuyPrice\":\"\",\"exchange_rate\":\"\",\"expect_arrive_time\":\"\",\"expressNo\":\"\",\"extra_discount\":0,\"extra_freight\":0.09,\"firstdiscount\":\"\",\"foreign_freight\":\"0\",\"free_shipping\":0,\"freightFee\":0,\"grade\":\"\",\"gradeDiscount\":0,\"gradeName\":\"\",\"id\":0,\"ip\":\"192.168.1.249\",\"isDropshipOrder\":0,\"maxSplitOrder\":\"\",\"memberFee\":0,\"mode_transport\":\"EPACKET (USPS)@9-15@USA@all\",\"new_zid\":\"\",\"no_checked\":\"\",\"odcount\":0,\"oldValue\":\"\",\"orderDetail\":[],\"orderFlag\":0,\"orderNo\":\"QC13519415298019_6\",\"orderNumber\":false,\"orderRemark\":\"\",\"order_ac\":0,\"order_count\":0,\"ordernum\":0,\"packag_number\":0,\"package_style\":0,\"payFlag\":0,\"payUserName\":\"\",\"pay_price\":22.52,\"pay_price_three\":\"0\",\"pay_price_tow\":\"0\",\"pay_type\":\"\",\"paystatus\":\"\",\"paytypes\":\"\",\"phonenumber\":\"\",\"problem\":\"\",\"processingfee\":0,\"product_cost\":\"22.43\",\"purchase\":0,\"purchase_number\":0,\"recipients\":\"\",\"remaining_price\":0,\"reminded\":[],\"rk\":0,\"ropType\":0,\"server_update\":0,\"service_fee\":\"0\",\"share_discount\":0,\"spider\":[],\"state\":5,\"stateDesc\":\"确认价格中\",\"statename\":\"\",\"storagetime\":\"\",\"street\":\"\",\"svolume\":\"\",\"total\":0,\"transport_time\":\"\",\"userEmail\":\"\",\"userName\":\"\",\"userid\":1019075,\"vatBalance\":0,\"volumeweight\":\"\",\"yhCount\":0,\"yourorder\":\"\",\"zipcode\":\"\"},\"autoUrl\":\"/user/toEmailPage?uid=c9ff91b2-8309-4620-b19b-be756ae4edcc&url=%2Findividual%2FgetCenter\",\"details\":[[\"QC13519415298019\",259117,1,\"10.06\",\"Standard Code Sexy Super-popular Digital Printing Dress\",\"https://img.import-express.com/importcsvimg/singleimg/549046313792/4103293319_891407924.400x400.jpg\",\"Color:D1121@32161,Size:S@4501\",\"https://img.import-express.com/importcsvimg/singleimg/549046313792/4103293319_891407924.60x60.jpg@\"],[\"QC13519415298019\",259122,1,\"22.43\",\"Spring Long Sleeve Miss KTV Dress Night Club Sexy Buttock Cover Skirt With Slim Button And Fishtail Skirt\",\"https://img1.import-express.com/importcsvimg/img/546718222139/3954913382_1799662277.400x400.jpg\",\"Color:Black@32162,Size:L@4503\",null]],\"currency\":\"USD\",\"time\":null,\"state\":1,\"email\":\"saycjc@outlook.com\",\"expect_arrive_time_\":\"2018-12-28\"}";
		//发送拆单取消
		modelStr ="{\"here\":\"http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=496777&configID=70901&lan=en&jid=4818862369&enterurl=http%3A%2F%2Fwww.import-express.com%2Fcbtconsole%2Fapa%2Fcontact.html&amp;timestamp=1441622560799&amp;pagereferrer=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2F&amp;firstEnterUrl=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2Fcbtconsole%2Fapa%2Fcontact%2Ehtml&amp;pagetitle=Customer+Service\",\"orderno\":\"QC13519415298019\",\"time_\":\"2018-12-13\",\"remark\":\"\",\"title\":\"Your Import Express order was partially cancelled\",\"message\":\"QC13519415298019_7\",\"ordernoNew\":\"QC13519415298019_7\",\"details_\":[[\"QC13519415298019_7\",259122,1,\"22.43\",\"Spring Long Sleeve Miss KTV Dress Night Club Sexy Buttock Cover Skirt With Slim Button And Fishtail Skirt\",\"https://img1.import-express.com/importcsvimg/img/546718222139/3954913382_1799662277.400x400.jpg\",\"Color:Black@32162,Size:L@4503\",null]],\"orderbean_\":{\"actualPay\":0,\"actual_allincost\":0,\"actual_ffreight\":\"0\",\"actual_freight_c\":0,\"actual_lwh\":\"\",\"actual_volume\":\"\",\"actual_weight\":\"\",\"actual_weight_estimate\":0,\"address\":null,\"address2\":\"\",\"addressid\":0,\"addresss\":\"\",\"adminemail\":\"\",\"adminid\":0,\"adminname\":\"\",\"applicable_credit\":0,\"arrive_time\":\"\",\"backFlag\":\"\",\"businessName\":\"\",\"buyAmount\":\"\",\"buycount\":\"\",\"buyid\":0,\"buyuser\":\"\",\"cancelFlag\":0,\"cancel_obj\":0,\"cashback\":0,\"cg\":0,\"chaOrderNo\":\"\",\"changenum\":0,\"checked\":\"\",\"checkeds\":0,\"client_update\":0,\"comformFlag\":0,\"complain\":0,\"count\":0,\"countOd\":\"\",\"country\":\"\",\"countryName\":\"\",\"countryNameCN\":\"\",\"coupon_discount\":0,\"createtime\":\"2018-12-13 17:42:40.0\",\"currency\":\"USD\",\"custom_discuss_other\":\"\",\"del_state\":0,\"deliver\":0,\"deliveryTime\":3,\"details_number\":1,\"details_pay\":\"\",\"discount_amount\":0,\"domestic_freight\":\"\",\"dropShipList\":\"\",\"dropShipState\":\"\",\"dzConfirmname\":\"\",\"dzConfirmtime\":\"\",\"dzId\":0,\"dzOrderno\":\"\",\"email\":\"\",\"esBuyPrice\":\"\",\"exchange_rate\":\"\",\"expect_arrive_time\":\"\",\"expressNo\":\"\",\"extra_discount\":0,\"extra_freight\":0.08,\"firstdiscount\":\"\",\"foreign_freight\":\"0\",\"free_shipping\":0,\"freightFee\":0,\"grade\":\"\",\"gradeDiscount\":0,\"gradeName\":\"\",\"id\":0,\"ip\":\"192.168.1.249\",\"isDropshipOrder\":0,\"maxSplitOrder\":\"\",\"memberFee\":0,\"mode_transport\":\"EPACKET (USPS)@9-15@USA@all\",\"new_zid\":\"\",\"no_checked\":\"\",\"odcount\":0,\"oldValue\":\"\",\"orderDetail\":[],\"orderFlag\":0,\"orderNo\":\"QC13519415298019_7\",\"orderNumber\":false,\"orderRemark\":\"\",\"order_ac\":0,\"order_count\":0,\"ordernum\":0,\"packag_number\":0,\"package_style\":0,\"payFlag\":0,\"payUserName\":\"\",\"pay_price\":22.51,\"pay_price_three\":\"0\",\"pay_price_tow\":\"0\",\"pay_type\":\"\",\"paystatus\":\"\",\"paytypes\":\"\",\"phonenumber\":\"\",\"problem\":\"\",\"processingfee\":0,\"product_cost\":\"22.43\",\"purchase\":0,\"purchase_number\":0,\"recipients\":\"\",\"remaining_price\":0,\"reminded\":[],\"rk\":0,\"ropType\":0,\"server_update\":0,\"service_fee\":\"0\",\"share_discount\":0,\"spider\":[],\"state\":-1,\"stateDesc\":\"订单取消\",\"statename\":\"\",\"storagetime\":\"\",\"street\":\"\",\"svolume\":\"\",\"total\":0,\"transport_time\":\"\",\"userEmail\":\"\",\"userName\":\"\",\"userid\":1019075,\"vatBalance\":0,\"volumeweight\":\"\",\"yhCount\":0,\"yourorder\":\"\",\"zipcode\":\"\"},\"autoUrl\":\"/user/toEmailPage?uid=c9ff91b2-8309-4620-b19b-be756ae4edcc&url=%2Findividual%2FgetCenter\",\"details\":[],\"currency\":\"USD\",\"time\":null,\"state\":0,\"totalDisCount\":\"--\",\"totalExtraFree\":\"USD 0.08\",\"email\":\"saycjc@outlook.com\"}";
		String shopcar="{\"updateList\":[{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"11.0\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1174,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":6.09,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":11,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"11.0\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1175,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":6.09,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":11,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"}],\"userEmail\":\"137365374@qq.com\",\"sourceList\":[{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1177,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1178,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1182,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1183,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1196,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1197,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1218,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1219,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1264,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1265,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1339,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1340,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1415,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":6.41,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1416,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@1$6.41,2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":5.91,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1428,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":5.91,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1429,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":5.91,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1456,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":5.91,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1457,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":5.91,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:White;Size:34;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3785797301_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"711196137f465530974817c7b7ce59de\",\"id\":1463,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053595\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"},{\"addprice\":0,\"aliposttime\":\"9-15\",\"bizpricediscount\":\"1038378,125372002,1034340\",\"bulkVolume\":\"0\",\"catid\":\"1034340\",\"comparealiprice\":\"0.00\",\"currency\":\"USD\",\"datatime\":null,\"deliveryTime\":\"6\",\"depositRate\":0,\"extraFreight\":0,\"feeprice\":\"\",\"firstnumber\":1,\"firstprice\":5.91,\"flag\":0,\"freeScDays\":\"9-15\",\"freeShoppingCompany\":\"\",\"freeprice\":6.41,\"freight\":0,\"freightFree\":0,\"goodsClass\":0,\"goodsEmail\":\"\",\"goodsTitle\":\"Classic Fashionable Sexy One Word Button Band Open Toe High Heel Sandal Open Toe High Heels Shoes\",\"goodsType\":\"Color:Gold;Size:35;\",\"goodsUrl\":\"/goodsinfo/classic-able-sexy-one-word-button-band-open-toe-high-1544182264149.html\",\"goodsdataId\":0,\"goodsunit\":\"pair\",\"goodsurlmd5\":\"DC40D807D2F2EE21F3080F9321832C2E1\",\"googsColor\":\"\",\"googsImg\":\"https://img1.import-express.com/importcsvimg/img/544182264149/3788124439_1057423199.60x60.jpg\",\"googsNumber\":1,\"googsPrice\":\"6.09\",\"googsSeller\":\"dd\",\"googsSize\":\"\",\"groupBuyId\":0,\"guid\":\"4be89b42fb7b1f21e618e3ce70a401db\",\"id\":1464,\"isbattery\":0,\"isfeight\":1,\"isfreeshipproduct\":0,\"isshippingPromote\":0,\"isvolume\":0,\"itemid\":\"544182264149\",\"methodFeight\":0,\"normLeast\":\"1\",\"normMost\":\"\",\"notfreeprice\":4.56,\"perWeight\":\"0.237\",\"preferential\":\"0\",\"price1\":0,\"price2\":0,\"price3\":4.56,\"price4\":6.41,\"pricelistsize\":\"3@@@@@2$6.09,10$5.91\",\"pwprice\":\"\",\"remark\":\"\",\"samplefee\":0,\"samplemoq\":0,\"seilunit\":\"pair\",\"sessionid\":\"\",\"shopid\":\"shop1463417989898\",\"skuid1688\":\"3278242053608\",\"sourceUrl\":0,\"spiderprice\":0,\"state\":0,\"theproductfrieght\":0,\"totalPrice\":6.09,\"totalWeight\":\"0.237\",\"trueShipping\":\"\",\"updatetime\":null,\"userid\":46261,\"width\":\"\"}],\"offRate\":-80.62,\"success\":1,\"offCost\":-9.82,\"productCost\":12.18,\"actualCost\":22}";
		model = SerializeUtil.JsonToMapStr(modelStr);
		sendMailFactory.sendMail(  String.valueOf(model.get("email")),null,"Due to supply reasons, we can only send your order partially at first.", model, TemplateType.DISMANTLING);
		String closeOrder="{\"orderNo\":\"QC13519415298019_6\",\"name\":\"saycjc@outlook.com\",\"accountLink\":\"https://www.import-express.com/individual/getCenter\",\"email\":\"Sale1\"}";
		model = SerializeUtil.JsonToMapStr(modelStr);
		//sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "Your ImportExpress Order " + String.valueOf(model.get("orderNo")) + " transaction is closed!", model, TemplateType.CANCEL_ORDER);
		/*// 购物车营销
		model = SerializeUtil.JsonToMapStr(shopcar);
		sendMailFactory.sendMail("saycjc@outlook.com", null,
				"Your ImportExpress Order " + String.valueOf(model.get("orderNo")) + " transaction is closed!",
				model, TemplateType.SHOPPING_CART_MARKETING);*/
		return "success!";
	}
}
