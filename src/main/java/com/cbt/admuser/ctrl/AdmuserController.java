package com.cbt.admuser.ctrl;

import com.cbt.admuser.service.AdmuserService;
import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.util.JsonResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/admuser")
public class AdmuserController {

	@Autowired
	private AdmuserService admuserService;

	
	@RequestMapping(value = "/assignment")
	@ResponseBody
	public JsonResult assignment(HttpServletRequest request) throws ParseException{
		JsonResult json =new JsonResult();
		//获取登录用户
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		Random random = new Random();
		int ranNum=0;int row;int num=0;
		com.cbt.website.dao.UserDao dao=new UserDaoImpl();
		if(num > 0){
			json.setOk(true);
			json.setData(num);
		}else{
			json.setOk(false);
			json.setMessage("客服分配失败！");
		}
		return json;
	}
	
	@RequestMapping(value = "/findpasswordinfo")
	public ModelAndView findpasswordinfo(HttpServletRequest request,
                                         ModelAndView mav) throws ParseException{
		//获取登录用户
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm.getId()==1){
			List<Admuser> admuserList = admuserService.selectAdmuser();
			mav.addObject("admuserList", admuserList);
		}
		mav.addObject("admuser", adm);
		mav.setViewName("/findpassword");
		return mav;
	}
	
	@RequestMapping(value = "/findpassword")
	@ResponseBody
	public JsonResult findpassword(HttpServletRequest request,
			@Param("admuser")Admuser admuser) throws ParseException{
		JsonResult json =new JsonResult();
		int num = admuserService.updateByPrimaryKeySelective(admuser);
		if(num > 0){
			json.setOk(true);
			json.setData(num);
		}else{
			json.setOk(false);
			json.setMessage("密码修改失败！");
		}
		return json;
	}
	
	@RequestMapping(value = "/queryAllAdmuser")
	@ResponseBody
	public JsonResult queryAllAdmuser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JsonResult json =new JsonResult();
		List<Admuser> admuserLis = admuserService.selectAdmuser();
		json.setOk(true);
		json.setData(admuserLis);
		return json;
	}
	
}