package com.cbt.systemcode.ctrl;

import com.cbt.common.CommonConstants;
import com.cbt.pojo.SystemCode;
import com.cbt.pojo.SystemCodeExample;
import com.cbt.pojo.SystemCodeExample.Criteria;
import com.cbt.systemcode.service.SystemCodeService;
import com.cbt.website.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping(value = "/systemCode")
public class SystemCodeController {

	@Autowired
	private SystemCodeService systemCodeService;

	
   /**
    * 
   * @Title: selectSystemCode 
   * @Description: 查询分类
   * @param @param request
   * @param @return  JsonResult
   * @param @throws ParseException    设定文件 
   * @return JsonResult    返回类型 
   * @throws
    */
	@RequestMapping(value = "/selectSystemCode")  
	@ResponseBody  
	public JsonResult selectSystemCode(HttpServletRequest request
			 ) throws ParseException{
		JsonResult json = new JsonResult();
		SystemCodeExample example = new SystemCodeExample();
		Criteria criteria = example.createCriteria();
		criteria.andCodeTypeEqualTo(CommonConstants.CODE_TYPE);
		List<SystemCode> systemCodeList = systemCodeService.selectByExample(example);
		if(systemCodeList!=null && systemCodeList.size()>0){
			json.setData(systemCodeList);
			json.setOk(true);
		}else{
			json.setMessage("查询失败！");
			json.setOk(false);
		}
		return json;
	}

	
	

}