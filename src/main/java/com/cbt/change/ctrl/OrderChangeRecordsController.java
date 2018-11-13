package com.cbt.change.ctrl;

import com.cbt.change.bean.OrderChangeRecords;
import com.cbt.change.bean.OrderChangeRecordsQuery;
import com.cbt.change.service.OrderChangeRecordsService;
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
@RequestMapping("/orderChangeRecords")
public class OrderChangeRecordsController {
	private static final Log LOG = LogFactory.getLog(OrderChangeRecordsController.class);

	@Resource
	private OrderChangeRecordsService orderChangeService;

	@RequestMapping("/queryForList")
	@ResponseBody
	public JsonResult queryForList(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		OrderChangeRecordsQuery records = getOrderChangeRecordsQuery(request);
		List<OrderChangeRecords> recordsLst = new ArrayList<OrderChangeRecords>();
		try {
			recordsLst = orderChangeService.queryForList(records);
			int total = orderChangeService.queryForCount(records);
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

	@RequestMapping("/insertOrderChange")
	@ResponseBody
	public JsonResult insertOrderChange(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		OrderChangeRecords records = genOrderChangeRecords(request);
		try {
			orderChangeService.insertRecords(records);
			json.setOk(true);
			json.setMessage("The insertOrderChange success");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The insertOrderChange fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The insertOrderChange fails, reason is :" + e.getMessage());
			return json;
		}
	}

	private OrderChangeRecordsQuery getOrderChangeRecordsQuery(HttpServletRequest request) {
		OrderChangeRecordsQuery records = new OrderChangeRecordsQuery();
		String idStr = request.getParameter("id");
		if (!(idStr == null || "".equals(idStr))) {
			records.setId(Integer.valueOf(idStr));
		}
		String orderNo = request.getParameter("orderNo");
		if (!(orderNo == null || "".equals(orderNo))) {
			records.setOrderNo(orderNo);
		}
		String userId = request.getParameter("userId");
		if (!(userId == null || "".equals(userId))) {
			records.setUserId(Integer.valueOf(userId));
		}
		String adminId = request.getParameter("adminId");
		if (!(adminId == null || "".equals(adminId))) {
			records.setAdminId(Integer.valueOf(adminId));
		}
		String operationType = request.getParameter("operationType");
		if (!(operationType == null || "".equals(operationType))) {
			records.setOperationType(Integer.valueOf(operationType));
		}
		String status = request.getParameter("status");
		if (!(status == null || "".equals(status))) {
			records.setStatus(status);
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

	private OrderChangeRecords genOrderChangeRecords(HttpServletRequest request) {
		OrderChangeRecords records = new OrderChangeRecords();
		String idStr = request.getParameter("id");
		if (!(idStr == null || "".equals(idStr))) {
			records.setId(Integer.valueOf(idStr));
		}
		String orderNo = request.getParameter("orderNo");
		if (!(orderNo == null || "".equals(orderNo))) {
			records.setOrderNo(orderNo);
		}
		String userId = request.getParameter("userId");
		if (!(userId == null || "".equals(userId))) {
			records.setUserId(Integer.valueOf(userId));
		}
		String adminId = request.getParameter("adminId");
		if (!(adminId == null || "".equals(adminId))) {
			records.setAdminId(Integer.valueOf(adminId));
		}
		String operationType = request.getParameter("operationType");
		if (!(operationType == null || "".equals(operationType))) {
			records.setOperationType(Integer.valueOf(operationType));
		}
		String productCost = request.getParameter("productCost");
		if (!(productCost == null || "".equals(productCost))) {
			records.setProductCost(productCost);
		}
		String payPrice = request.getParameter("payPrice");
		if (!(payPrice == null || "".equals(payPrice))) {
			records.setPayPrice(payPrice);
		}
		String remainingPrice = request.getParameter("remainingPrice");
		if (!(remainingPrice == null || "".equals(remainingPrice))) {
			records.setRemainingPrice(remainingPrice);
		}
		String status = request.getParameter("status");
		if (!(status == null || "".equals(status))) {
			records.setStatus(status);
		}
		return records;
	}

}
