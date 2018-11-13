package com.importExpress.controller;

import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.service.WebhoolPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/webhook/payment")
public class WebHookPaymentController {
	@Autowired
	private WebhoolPaymentService webhoolPaymentService;

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUiJsonResult list(HttpServletRequest request, HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		request.getParameter("row");
		
		int startNum = 0;
        int limitNum = 20;
        String limitStr = request.getParameter("rows");
        if (!(limitStr == null || "".equals(limitStr) || "0".equals(limitStr))) {
        	limitNum = Integer.valueOf(limitStr);
        }
        String pageStr = request.getParameter("page");
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        String type = request.getParameter("type");
		try {
			Map<String, Object> result = webhoolPaymentService.list(startNum, limitNum, type);
//            long total = (long)result.get("total");
            json.setSuccess(true);
            json.setRows(result.get("data"));
            json.setTotal((int)result.get("total"));
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
		return json;
	}
	
	
	
}
