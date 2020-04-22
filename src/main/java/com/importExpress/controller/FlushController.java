package com.importExpress.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.importExpress.pojo.CommonResult;
import com.importExpress.utli.CloudHelp;

@Controller
@RequestMapping("/init/flush")
public class FlushController {
	@Autowired
	private CloudHelp cloudHelp;
	
	/**更新类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/synonyms")
	@ResponseBody
	public CommonResult synonyms(HttpServletRequest request, HttpServletResponse response){
		return cloudHelp.cloudCallback(cloudHelp.FLUSH_URL_SYNONYMS);
	}
	/**更新类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/synonyms/category")
	@ResponseBody
	public CommonResult synonymsCategory(HttpServletRequest request, HttpServletResponse response){
		return cloudHelp.cloudCallback(cloudHelp.FLUSH_URL_SYNONYMS_CATEGORY);
	}
	/**更新类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/prority")
	@ResponseBody
	public CommonResult prority(HttpServletRequest request, HttpServletResponse response){
		return cloudHelp.cloudCallback(cloudHelp.FLUSH_URL_PRIORITY);
	}
	/**更新类别同义词
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/price")
	@ResponseBody
	public CommonResult price(HttpServletRequest request, HttpServletResponse response){
		return cloudHelp.cloudCallback(cloudHelp.FLUSH_URL_PRICE);
	}
	
	

}
