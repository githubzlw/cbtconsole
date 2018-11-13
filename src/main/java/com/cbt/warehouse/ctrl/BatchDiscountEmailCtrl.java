package com.cbt.warehouse.ctrl;

import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.BatchDiscountEmail;
import com.cbt.warehouse.pojo.BatchDiscountEmailDetails;
import com.cbt.warehouse.pojo.BatchDiscountPurchasPrice;
import com.cbt.warehouse.pojo.PreferentialPrice;
import com.cbt.warehouse.service.BatchDiscountEmailService;
import com.cbt.warehouse.util.BatchDiscountUtil;
import com.cbt.website.dao2.PurchaseDao;
import com.cbt.website.dao2.PurchaseDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/batchDiscountEmail")
public class BatchDiscountEmailCtrl {
	private static final Log LOG = LogFactory.getLog(BatchDiscountEmailCtrl.class);

	@Autowired
	private BatchDiscountEmailService bdEmailService;

	/**
	 * 根据条件查询所有批量优惠邮件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryEmailList.do")
	@ResponseBody
	public EasyUiJsonResult queryEmailList(HttpServletRequest request, HttpServletResponse response) {

		EasyUiJsonResult json = new EasyUiJsonResult();

		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			orderNo = "";
		}

		String adminIdStr = request.getParameter("adminId");
		int adminId = 0;
		if (!(adminIdStr == null || "".equals(adminIdStr))) {
			adminId = Integer.valueOf(adminIdStr);
		}

		String userIdStr = request.getParameter("userId");
		int userId = 0;
		if (!(userIdStr == null || "".equals(userIdStr))) {
			userId = Integer.valueOf(userIdStr);
		}

		String flag = request.getParameter("flag");
		if (flag == null || "".equals(flag)) {
			flag = "";
		}

		String beginDate = request.getParameter("beginDate");
		if (beginDate == null || "".equals(beginDate)) {
			beginDate = "";
		}
		String endDate = request.getParameter("endDate");
		if (endDate == null || "".equals(endDate)) {
			endDate = "";
		}

		String pageNumStr = request.getParameter("rows");
		int pageNum = 20;
		if (pageNumStr == null || "".equals(pageNumStr) || "0".equals(pageNumStr)) {
			json.setSuccess(false);
			json.setMessage("获取分页数失败");
			return json;
		} else {
			pageNum = Integer.valueOf(pageNumStr);
		}

		String stateNumStr = request.getParameter("page");
		int stateNum = 1;
		if (!(stateNumStr == null || "".equals(stateNumStr) || "0".equals(stateNumStr))) {
			stateNum = Integer.valueOf(stateNumStr);
		}

		try {
			List<BatchDiscountEmail> emailLst = bdEmailService.queryEmailList(orderNo, adminId, userId, flag, beginDate,
					endDate, (stateNum - 1) * pageNum, pageNum);
			int total = bdEmailService.queryEmailListCount(orderNo, adminId, userId, flag, beginDate, endDate);

			json.setSuccess(true);
			json.setRows(emailLst);
			json.setTotal(total);
		} catch (Exception e) {
			e.getStackTrace();
			json.setSuccess(false);
			json.setMessage("获取批量优惠邮件失败，原因：" + e.getMessage());
			LOG.error("获取批量优惠邮件失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 根据条件查询所有批量优惠邮件详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryDetailsList.do")
	@ResponseBody
	public JsonResult queryDetailsList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();

		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			json.setOk(false);
			json.setMessage("获取订单号失败");
			return json;
		}

		try {
			// 查询所有批量优惠邮件id的详细信息
			List<BatchDiscountEmailDetails> emailDetailsLst = bdEmailService.queryDetailsList(orderNo);
			if (emailDetailsLst.size() > 0) {
				// 判断不为0的情况下查询商品的所有批量价格
				for (BatchDiscountEmailDetails emailDetails : emailDetailsLst) {
					List<BatchDiscountPurchasPrice> purchasPriceList = bdEmailService
							.queryPurchasPriceList(emailDetails.getGoodsId());
					if (purchasPriceList.size() > 0) {
						emailDetails.setPurchasPriceList(purchasPriceList);
					}
					emailDetails.setGoodsImportUrl("https://www.import-express.com/spider/getSpider?url="
							+ TypeUtils.encodeGoods(emailDetails.getGoodsUrl()));
				}
			}
			json.setOk(true);
			json.setData(emailDetailsLst);
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("获取批量优惠邮件详情失败，原因：" + e.getMessage());
			LOG.error("获取批量优惠邮件详情失败，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 批量更新采购价格数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/batchUpdatePurchasPrice.do")
	@ResponseBody
	public JsonResult batchUpdatePurchasPrice(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null) {
			json.setOk(false);
			json.setMessage("获取登录用户信息失败");
			return json;
		}

		String idsStr = request.getParameter("ids");
		if (idsStr == null || "".equals(idsStr)) {
			json.setOk(false);
			json.setMessage("获取商品ids失败，请重试");
			return json;
		}

		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			json.setOk(false);
			json.setMessage("获取订单号失败");
			return json;
		}

		String type = request.getParameter("type");
		if (type == null || "".equals(type)) {
			json.setOk(false);
			json.setMessage("获取发送类别失败");
			return json;
		}

		try {

			List<Integer> uedLst = new ArrayList<Integer>();
			// 循环获取goodsId的数据
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				uedLst.add(Integer.valueOf(id));
			}
			// 批量更新批量优惠邮件详情商品有效
			bdEmailService.batchUpdateEmailDetailsValid(uedLst, orderNo);

			// 批量更新goodsId的批量价格数据
			List<BatchDiscountPurchasPrice> uppLst = getPurchasPriceList(request);
			if (!(uppLst == null || uppLst.size() == 0)) {
				for (BatchDiscountPurchasPrice pprice : uppLst) {
					pprice.setAdminId(user.getId());
					pprice.setAdminName(user.getAdmName());
				}
				bdEmailService.batchUpdatePurchasPrice(uppLst);
			}
			// 如果是废弃不发，更新邮件状态
			if ("0".equals(type)) {
				bdEmailService.updateEmailFlagByOrderNo(orderNo, "-3");
			}
			json.setOk(true);
			json.setMessage("批量更新成功");
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("批量更新失败，原因：" + e.getMessage());
			LOG.error("获批量更新失败，原因：" + e.getMessage());
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	private List<BatchDiscountPurchasPrice> getPurchasPriceList(HttpServletRequest request) {

		String ppListJsonStr = request.getParameter("ppJson");

		JSONArray jsonArray = JSONArray.fromObject(ppListJsonStr);// 把String转换为json

		List<BatchDiscountPurchasPrice> ppLst = (List<BatchDiscountPurchasPrice>) JSONArray.toCollection(jsonArray,
				BatchDiscountPurchasPrice.class);// 这里的t是Class<T>
		return ppLst;
	}

	/**
	 * 批量更新采购价格数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sendBatchDiscountEmail.do")
	@ResponseBody
	public JsonResult sendBatchDiscountEmail(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			json.setOk(false);
			json.setMessage("获取订单号失败，请重试");
			return json;
		}
		String userEmail = request.getParameter("userEmail");
		if (userEmail == null || "".equals(userEmail)) {
			json.setOk(false);
			json.setMessage("获取客户邮箱失败，请重试");
			return json;
		}
		String emailInfo = request.getParameter("emailInfo");
		if (emailInfo == null || "".equals(emailInfo)) {
			json.setOk(false);
			json.setMessage("获取邮件体失败，请重试");
			return json;
		}
		String sendEmail = "";// 发件人邮箱
		String pwd = "";// 发件人密码
		try {
			BatchDiscountEmail bdEamilInfo = bdEmailService.queryEmailByOrderNo(orderNo);
			IUserDao userDao = new UserDao();
			String[] adminEmail = userDao.getAdminUser(0, "", bdEamilInfo.getUserId());
			if (adminEmail != null) {
				sendEmail = adminEmail[0];
				pwd = adminEmail[1];
			}
			// userEmail = bdEamilInfo.getUserEmail();
			int res = SendEmail.send(sendEmail, pwd, userEmail, emailInfo, "ImportExpress Volume Discount", userEmail,
					orderNo, 1);
			if (res == 1) {
				bdEmailService.updateEmailFlagByOrderNo(orderNo, "2");
				json.setOk(true);
				json.setMessage("发送邮件成功");
			} else {
				bdEmailService.updateEmailFlagByOrderNo(orderNo, "-1");
				json.setOk(false);
				json.setMessage("发送邮件失败");
			}
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("发送邮件执行失败，原因：" + e.getMessage());
			LOG.error("发送邮件执行失败，原因：" + e.getMessage());
			bdEmailService.updateEmailFlagByOrderNo(orderNo, "0");
		}
		return json;
	}

	/**
	 * 跳转到邮件预览界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/jumpToEmailPreview.do")
	public String jumpToEmailPreview(HttpServletRequest request, Model model, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String orderNo = request.getParameter("orderNo");
		if (orderNo == null || "".equals(orderNo)) {
			return "batchDiscountEmailPreview";
		}
		try {
			BatchDiscountEmail bdEamilInfo = bdEmailService.queryEmailByOrderNo(orderNo);
			request.setAttribute("bdEamilInfo", bdEamilInfo);
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("跳转邮件预览失败，原因：" + e.getMessage());
			LOG.error("跳转邮件预览失败，原因：" + e.getMessage());
		}
		return "batchDiscountEmailPreview";
	}

	/**
	 * 生成批量价格邮件的线程
	 * 
	 * @author JXW
	 *
	 */
	class BatchDiscountThread extends Thread {
		private String orderNo;

		public BatchDiscountThread(String orderNo) {
			super();
			this.orderNo = orderNo;
		}

		@Override
		public synchronized void run() {
			try {

				boolean checked = false;
				// 自动发送条件：
				// 1.选择有批量采购价格的当天出运的订单
				checked = bdEmailService.checkIsPurchasPrice(orderNo);
				// 判断是否满足发送批量邮件的条件
				if (checked) {
					// 生成批量价格邮件的主体
					bdEmailService.insertEmail(this.orderNo);
					bdEmailService.insertEmailDetails(this.orderNo);
					// 更新商品的默认折扣价格=采购价格*1.2
					bdEmailService.updateDefaultPurchasPrice(this.orderNo);
					// 建立关联关系
					bdEmailService.buildRelationshipsByOrderNo(this.orderNo);
					bdEmailService.insertPurchasPrice(this.orderNo);
					// 开始判断符合条件的商品
					// 2.产品优惠单价 = 批量采购价格 * 1.2 + 批量的运费/ 批量数量(以最小订单量传给参数)
					// 产品优惠单价 <= 客户上单成交单价 * 1（暂定，后续根据情况再调）
					checkIsBatchDiscountEamil(this.orderNo);
				}
			} catch (Exception e) {
				e.getStackTrace();
				LOG.error("生成批量价格邮件出错,原因:" + e.getMessage());
			}

		}
	}

	private void checkIsBatchDiscountEamil(String orderNo) {
		try {
			PurchaseDao purchaseDao = new PurchaseDaoImpl();
			// 2.产品优惠单价 = 批量采购价格 * 1.2 + 批量的运费/ 批量数量(以最小订单量传给参数)
			// 产品优惠单价 <= 客户上单成交单价 * 1（暂定，后续根据情况再调）
			List<BatchDiscountEmailDetails> nwEdLst = new ArrayList<BatchDiscountEmailDetails>();
			// 获取邮件详情的数据
			List<BatchDiscountEmailDetails> emailDetails = bdEmailService.queryDetailsList(orderNo);
			for (BatchDiscountEmailDetails emailDetail : emailDetails) {

				// 计算商品的免邮价格
				float freeShippingPrice = 0;// 接口调用免邮价格
				if (freeShippingPrice > 0) {
					emailDetail.setFreeShippingPrice(freeShippingPrice);
				}

				//现在调整了，记得要修改
				List<PreferentialPrice> pfPriceList = purchaseDao.queryPreferentialPrice(orderNo,
						emailDetail.getGoodsId(),"");
				List<PreferentialPrice> nwPfPriceList = new ArrayList<PreferentialPrice>();
				if (pfPriceList != null && pfPriceList.size() > 0) {
					nwEdLst.add(emailDetail);
					for (PreferentialPrice pfPrice : pfPriceList) {
						float discountPrice = 0;// 产品优惠单价
						float purchasePrice = 0;// 批量采购价格

						float discountFreight = 0;// 批量的运费,使用接口获取数据
						int discountNumber = pfPrice.getBegin();// 批量的数量
						// 接口调用获取singleiscountFreight数据
						float singleiscountFreight = 0;// 批量的单个商品运费价格

						float totalDiscountPrice = purchasePrice * BatchDiscountUtil.PURCHASPRICERATIO
								+ singleiscountFreight;// 单品总费用含运费
						if (discountPrice <= totalDiscountPrice * BatchDiscountUtil.DEALPRICERATIO) {
							nwPfPriceList.add(pfPrice);
						}
					}
					System.out.println("当前商品[" + emailDetail.getGoodsId() + "]含有的批量价格的个数:" + nwPfPriceList.size());
					/*
					 * if (nwPfPriceList.size() > 0) { for (PreferentialPrice
					 * nwPfPrice : nwPfPriceList) { // 更新商品的有效和免邮价格
					 * batchDiscountEmailService.updateValidByGoodsId(orderNo,
					 * nwPfPrice.getGoodsid(), nwPfPrice.getBegin(),
					 * nwPfPrice.getEnd()); } }
					 */
				}
			}
			// 统一更新details的表免邮价格
			for (BatchDiscountEmailDetails eDetail : emailDetails) {
				bdEmailService.updateEmailDetailsFreeShippingPrice(orderNo, eDetail.getGoodsId(),
						eDetail.getFreeShippingPrice());
			}
			System.out.println("当前订单[" + orderNo + "]的含有批量价格的商品个数:" + nwEdLst.size());

		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("检查商品的批量价格有效出错,原因:" + e.getMessage());
		}
	}

}
