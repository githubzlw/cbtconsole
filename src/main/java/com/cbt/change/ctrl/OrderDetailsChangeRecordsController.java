package com.cbt.change.ctrl;

import com.cbt.change.bean.OrderDetailsChangeRecords;
import com.cbt.change.bean.OrderDetailsChangeRecordsQuery;
import com.cbt.change.service.OrderDetailsChangeRecordsService;
import com.cbt.website.util.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orderDetailsChangeRecords")
public class OrderDetailsChangeRecordsController {

	private static final Log LOG = LogFactory.getLog(OrderDetailsChangeRecordsController.class);

	@Resource
	private OrderDetailsChangeRecordsService detailsChangeService;

	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		OrderDetailsChangeRecordsQuery records = getOrderDetailsChangeRecordsQuery(request);
		List<OrderDetailsChangeRecords> recordsLst = new ArrayList<OrderDetailsChangeRecords>();
		try {
			recordsLst = detailsChangeService.queryForList(records);
			int total = detailsChangeService.queryForCount(records);
			json.setOk(true);
			json.setTotal(Long.valueOf(total));
			json.setData(recordsLst);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The query fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}
	}

	@RequestMapping("/insertOrderDetailsChange")
	@ResponseBody
	public JsonResult insertOrderDetailsChange(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		OrderDetailsChangeRecords records = genOrderDetailsChangeRecords(request);
		try {
			detailsChangeService.insertChangeRecords(records);
			json.setOk(true);
			json.setMessage("The insertOrderDetailsChange success");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The insertOrderDetailsChange fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The insertOrderDetailsChange fails, reason is :" + e.getMessage());
			return json;
		}
	}

	private OrderDetailsChangeRecordsQuery getOrderDetailsChangeRecordsQuery(HttpServletRequest request) {
		OrderDetailsChangeRecordsQuery records = new OrderDetailsChangeRecordsQuery();
		String idStr = request.getParameter("id");
		if (!(idStr == null || "".equals(idStr))) {
			records.setId(Integer.valueOf(idStr));
		}
		String orderNo = request.getParameter("orderNo");
		if (!(orderNo == null || "".equals(orderNo))) {
			records.setOrderNo(orderNo);
		}
		String odsId = request.getParameter("odsId");
		if (!(odsId == null || "".equals(odsId))) {
			records.setOdsId(Integer.valueOf(odsId));
		}
		String goodsId = request.getParameter("goodsId");
		if (!(goodsId == null || "".equals(goodsId))) {
			records.setGoodsId(Integer.valueOf(goodsId));
		}
		String odsStatus = request.getParameter("odsStatus");
		if (!(odsStatus == null || "".equals(odsStatus))) {
			records.setOdsStatus(odsStatus);
		}
		String purchaseStatus = request.getParameter("purchaseStatus");
		if (!(purchaseStatus == null || "".equals(purchaseStatus))) {
			records.setPurchaseStatus(purchaseStatus);
		}
		String userId = request.getParameter("userId");
		if (!(userId == null || "".equals(userId))) {
			records.setUserId(Integer.valueOf(userId));
		}
		String adminId = request.getParameter("adminId");
		if (!(adminId == null || "".equals(adminId))) {
			records.setAdminId(Integer.valueOf(adminId));
		}
		String beginDate = request.getParameter("beginDate");
		if (!(beginDate == null || "".equals(beginDate))) {
			records.setBeginDate(beginDate);
		}
		String endDate = request.getParameter("endDate");
		if (!(endDate == null || "".equals(endDate))) {
			records.setEndDate(endDate);
		}
		String limitCount = request.getParameter("limitCount");
		if (!(limitCount == null || "".equals(limitCount))) {
			records.setLimitCount(Integer.valueOf(limitCount));
		}
		int page = 1;
		String pageStr = request.getParameter("page");
		if (pageStr != null && !"".equals(pageStr)) {
			page = Integer.valueOf(pageStr);
		}
		if (page <= 0) {
			page = 1;
		}
		records.setStartNo(page);
		return records;
	}

	private OrderDetailsChangeRecords genOrderDetailsChangeRecords(HttpServletRequest request) {
		OrderDetailsChangeRecords records = new OrderDetailsChangeRecords();
		String orderNo = request.getParameter("orderNo");
		if (!(orderNo == null || "".equals(orderNo))) {
			records.setOrderNo(orderNo);
		}
		String odsId = request.getParameter("odsId");
		if (!(odsId == null || "".equals(odsId))) {
			records.setOdsId(Integer.valueOf(odsId));
		}
		String goodsId = request.getParameter("goodsId");
		if (!(goodsId == null || "".equals(goodsId))) {
			records.setGoodsId(Integer.valueOf(goodsId));
		}

		String goodsPrice = request.getParameter("goodsPrice");
		if (!(goodsPrice == null || "".equals(goodsPrice))) {
			records.setGoodsPrice(goodsPrice);
		}
		String goodsNumber = request.getParameter("goodsNumber");
		if (!(goodsNumber == null || "".equals(goodsNumber))) {
			records.setGoodsNumber(Integer.valueOf(goodsNumber));
		}

		String odsStatus = request.getParameter("odsStatus");
		if (!(odsStatus == null || "".equals(odsStatus))) {
			records.setOdsStatus(odsStatus);
		}
		String purchaseStatus = request.getParameter("purchaseStatus");
		if (!(purchaseStatus == null || "".equals(purchaseStatus))) {
			records.setPurchaseStatus(purchaseStatus);
		}
		String userId = request.getParameter("userId");
		if (!(userId == null || "".equals(userId))) {
			records.setUserId(Integer.valueOf(userId));
		}
		String adminId = request.getParameter("adminId");
		if (!(adminId == null || "".equals(adminId))) {
			records.setAdminId(Integer.valueOf(adminId));
		}
		return records;
	}

}
