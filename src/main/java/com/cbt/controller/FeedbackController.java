package com.cbt.controller;

import com.cbt.bean.FeedbackBean;
import com.cbt.service.FeedbackService;
import com.cbt.website.util.JsonResult;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/cbt/feedback")
public class FeedbackController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(FeedbackController.class);

	@Autowired
	private FeedbackService feedbackService;

	@RequestMapping("/queryFeedbackById")
	@ResponseBody
	public JsonResult queryFeedbackById(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String id = request.getParameter("id");
		FeedbackBean feedback = new FeedbackBean();
		try {
			if (!(id == null || "".equals(id))) {
				feedback = feedbackService.queryById(Integer.valueOf(id));
				if (feedback != null) {
					if (feedback.getContent() == null || "".equals(feedback.getContent())) {
						feedback.setContent("");
					} else {
						feedback.setContent(URLDecoder.decode(feedback.getContent()));
					}
					if (feedback.getOtherComment() == null || "".equals(feedback.getOtherComment())) {
						feedback.setOtherComment("");
					} else {
						feedback.setOtherComment(URLDecoder.decode(feedback.getOtherComment()));
					}
				}
				json.setOk(true);
				json.setData(feedback);
				return json;
			} else {
				json.setOk(false);
				json.setMessage("Failed to get feedbackid");
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The query fails, the reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}
	}

	@RequestMapping("/queryFeedbackByType")
	@ResponseBody
	public JsonResult queryFeedbackByType(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		List<FeedbackBean> feedbackBeans = new ArrayList<FeedbackBean>();
		String type = request.getParameter("type");
		try {
			if (!(type == null || "".equals(type))) {
				feedbackBeans = feedbackService.queryByType(Integer.valueOf(type));
				json.setOk(true);
				json.setData(feedbackBeans);
				return json;
			} else {
				json.setOk(false);
				json.setMessage("Failed to get the feedbackType");
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("The query fails, reason is :" + e.getMessage());
			json.setOk(false);
			json.setMessage("The query fails, reason is :" + e.getMessage());
			return json;
		}
	}

}
