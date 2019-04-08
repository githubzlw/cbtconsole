package com.cbt.warehouse.ctrl;

import com.cbt.bean.Subscribe;
import com.cbt.util.Redis;
import com.cbt.warehouse.service.SubScribeService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("SubScription")
public class SubScriptionController {

	@Autowired
	private SubScribeService subScribeService;

	@RequestMapping("querySubScription")
	@ResponseBody
	public EasyUiJsonResult querySubScription(HttpServletRequest request, HttpServletResponse response){

		EasyUiJsonResult json = new EasyUiJsonResult();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		if (StringUtil.isBlank(admuserJson)) {
			json.setSuccess(false);
			json.setMessage("用户未登陆");
			return json;
		}

		String Email = request.getParameter("email");
		String email =null;
		if (StringUtils.isNotBlank(Email)) {
			email = String.valueOf(Email);
		}

		int startNum = 0;
		int limitNum = 50;
		String pageStr = request.getParameter("page");
		if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
			startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
		}
		Subscribe param = new Subscribe();
		try {

			param.setEmail(email);
			param.setStartNum(startNum);
			param.setLimitNum(limitNum);

			List<Subscribe> res = subScribeService.queryAllSubscribe(param);
			int count = subScribeService.queryAllSubscribeConut(param);

			json.setSuccess(true);
			json.setRows(res);
			json.setTotal(count);
		} catch (Exception e) {
			e.printStackTrace();
			//LOG.error("查询失败，原因 :" + e.getMessage());
			json.setSuccess(false);
			json.setMessage("查询失败，原因:" + e.getMessage());
		}
		return json;
	}
}