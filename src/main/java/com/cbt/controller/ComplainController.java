package com.cbt.controller;

import com.cbt.bean.Complain;
import com.cbt.bean.ComplainFile;
import com.cbt.bean.ComplainVO;
import com.cbt.method.service.OrderDetailsService;
import com.cbt.method.service.OrderDetailsServiceImpl;
import com.cbt.orderinfo.service.OrderinfoService;
import com.cbt.pojo.page.Page;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.service.IComplainService;
import com.cbt.service.RefundSSService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.CustomerDisputeBean;
import com.importExpress.service.CustomerDisputeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/complain")
public class ComplainController {
	
	private OrderDetailsService orderDetailsService = new OrderDetailsServiceImpl();
	@Autowired
	private CustomerDisputeService customerDisputeService;
	@Autowired
	private IComplainService complainService;
	
	@Autowired
	private RefundSSService refundService;
	@Autowired
	private AdditionalBalanceService additionalBalanceService;
	
	
	@RequestMapping(value = "/searchComplainByParam", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView searchComplainByParam(HttpServletRequest request, Integer userid, String creatTime, Integer complainState, String username, int toPage, String currentPage, String type,Integer check){
		ModelAndView mv = new ModelAndView("complainCenter");
		//获取登录用户
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String admName = adm.getAdmName();
		Complain t = new Complain();
		if( userid!=null&&userid!=0){
			t.setUserid(userid);
			mv.addObject("userid",userid);
		}
		if(creatTime!=null && creatTime!=""){
			t.setCreatTime(creatTime);
			mv.addObject("appdate",creatTime);
		}
//		if(complainState!=-1){ //不要加判断条件，否则当complainState==-1时不赋值，complainState属性默认为0，-1状态也是查询0
			t.setComplainState(complainState);
//		}
		t.setComplainType(type);
		if(StringUtil.isNotBlank(username)){
			mv.addObject("username",username);
		}
		if(StringUtil.isNotBlank(type)){
			mv.addObject("type",type);
		}
		Page<ComplainVO> page = new Page<ComplainVO>();
		int aa =1;
		if(currentPage!=""||!currentPage.equals("")){
			aa =Integer.parseInt(currentPage);
		}
		page.setCurrentPage(aa);
		page.setStartIndex(toPage);
		check = check == null ? 0 : check;
		page = complainService.searchComplainByParam(t,username,page,admName,check);
		List<ComplainVO> list = page.getList();
		//投诉是否有退款申请
		String useridList = "";
		List<String> orderIdList = new ArrayList<String>();
		for(ComplainVO c:list){
			int userid2 = c.getUserid();
			useridList = useridList+userid2+",";
			List<String> corderIdList = c.getOrderIdList();
			if(StringUtils.isBlank(c.getDisputeId()) && corderIdList != null && !corderIdList.isEmpty()) {
				for(String o : corderIdList) {
					if(StringUtil.isNotBlank(o) ) {
						orderIdList.add(o.split("_")[0]);
					}
				}
			}
		}
		Map<String, Object> dispute = customerDisputeService.list(orderIdList);
		useridList = useridList.endsWith(",")?useridList.substring(0, useridList.length()-1):useridList;
		Map<String, String> complainRefundByUserids = refundService.getComplainRefundByUserids(useridList);
		for(ComplainVO c:list){
			String isRefund = complainRefundByUserids.get(c.getUserid()+"");
			isRefund = isRefund==null||isRefund.isEmpty()?"0":isRefund;
			c.setIsRefund(Integer.valueOf(isRefund));
			List<String> corderIdList = c.getOrderIdList();
			if(StringUtils.isBlank(c.getDisputeId()) && corderIdList != null && !corderIdList.isEmpty()) {
				for(String o : corderIdList) {
					o = o.split("_")[0];
					CustomerDisputeBean cDisputeBean = (CustomerDisputeBean)dispute.get(o);
					if(cDisputeBean != null) {
						c.setDisputeId(cDisputeBean.getDisputeID());
						c.setMerchantId(cDisputeBean.getMerchantID());
						break;
					}
				}
				
			}
			
		}
		
		page.setList(list);
		
		List<AdminUserBean> AdmuserList = refundService.getAllAdmUser();
		if(page.getList()!=null&&page.getList().size()>0){
			mv.addObject("complainState",complainState);
			mv.addObject("status","1");
			mv.addObject("page",page);
			mv.addObject("admuserList",AdmuserList);
		}else{
			mv.addObject("complainState",complainState);
			mv.addObject("status","-1");
			mv.addObject("message","未查询到数据");
			mv.addObject("admuserList",AdmuserList);
		}
		mv.addObject("adminid",adm.getId());
		return mv;
	}
	@RequestMapping(value = "/getComplainByCid", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getComplainByCid(Integer cid){
		ModelAndView mv = new ModelAndView("complainDetail");
		ComplainVO complain = new ComplainVO();
		complain = complainService.getComplainByCid(cid);
		List<ComplainFile> imgs = complainService.getImgsByCid(cid);
		double moneyAmountByCid = additionalBalanceService.getMoneyAmountByCid(cid+"");
		
		if(complain!=null){
			mv.addObject("status","1");
			mv.addObject("complain",complain);
			mv.addObject("imgsList",imgs);
			mv.addObject("money",moneyAmountByCid);
		}else{
			mv.addObject("status","-1");
			mv.addObject("message","未查询到数据");
		}
		return mv;
	}
	
	
	@RequestMapping(value = "/getCommunicatingByCidBG", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  getCommunicatingByCidBG(Integer complainId){
		Map<String,Object> map = new HashMap<String, Object>();
		if(complainId!=0){
			List<ComplainVO> list = new ArrayList<ComplainVO>();
			list = complainService.getCommunicatingByCidBG(complainId);
			if(list!=null && list.size()!=0){
				map.put("status", true);
				map.put("complainList", list);
			}else{
				map.put("status", false);
				map.put("complainList", list);
			}
		}else{
			map.put("message","Something bad happend.");
		}
		return map;
	}
	
	
	@RequestMapping(value = "/closeComplain", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  closeComplain(Integer complainId){
		Map<String,Object> map = new HashMap<String, Object>();
		int row =0;
		if(complainId!=0){
			row= complainService.closeComplain(complainId);
			if(row>0){
				map.put("status", true);
				map.put("message", "关闭成功");
			}else{
				map.put("status", false);
				map.put("message", "关闭失败，请联系管理员。");
			}
		}else{
			map.put("message","Something bad happend.");
		}
		return map;
	}
	@RequestMapping(value = "/order/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  orderlist(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		
		String userid = request.getParameter("userid");
		
		List<Map<String,Object>> orderDetailByUser = orderDetailsService.getOrderDetailByUser(Integer.valueOf(userid));
		
		
		map.put("data", orderDetailByUser);
		return map;
	}
	@RequestMapping(value = "/order/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  complainUpdate(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		
		String id = request.getParameter("id");
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		complainService.updateGoodsid(Integer.valueOf(id), orderid, goodsid);
		map.put("status", true);
		return map;
	}
	@RequestMapping(value = "/dispute/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  complainUpdateDisputeID(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		
		String id = request.getParameter("id");
		String disputeid = request.getParameter("disputeid");
		String merchantid = request.getParameter("merchantid");
		complainService.updateDisputeid(Integer.valueOf(id), disputeid,merchantid);
		map.put("status", true);
		return map;
	}
	
	
	
}
