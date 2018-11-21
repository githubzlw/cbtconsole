package com.cbt.warehouse.ctrl;

import com.cbt.pojo.page.Page;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.Utility;
import com.cbt.warehouse.pojo.SupplierProductsBean;
import com.cbt.warehouse.pojo.SupplierScoringBean;
import com.cbt.warehouse.service.SupplierScoringService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.QueAns;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * 供应商打分机制
 * @ClassName SupplierScoringController 
 * @Description TODO
 * @author Administrator
 * @date 2018年2月13日 上午10:35:05
 */
@Controller
@RequestMapping("/supplierscoring")
public class SupplierScoringController {
	@Autowired
	private SupplierScoringService supplierScoringService;
	private Logger LOG = Logger.getLogger(SupplierScoringController.class);

	/**
	 * 查询所有供应商列表
	 */
	@RequestMapping("querySupplierScoringList")
	public String querySupplierScoringList(HttpServletRequest request, HttpServletResponse response) {
		try {
			//获取登陆用户id
	        String sessionId = request.getSession().getId();
	        String userJson = Redis.hget(sessionId, "admuser");
	        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
	        if (user == null) {
	        	return "login";
			}
	        String userid = String.valueOf(user.getId());//用户id
			//筛选条件 最近7天已经验货的, 本人采购的，未评价的(true); 所有(false)
			boolean flag = "1".equals(request.getParameter("flag"))?true:false;
			//供应商ID
			String shop_id=request.getParameter("shop_id");
			//质量评分
			String quality=request.getParameter("quality");
			//服务评分
			String services=request.getParameter("services");
			String authorizedFlag=request.getParameter("authorizedFlag");
			authorizedFlag=StringUtil.isBlank(authorizedFlag)?null:authorizedFlag;
			shop_id = StringUtils.isNotBlank(shop_id) ? new String(shop_id.getBytes("iso8859-1"), "utf-8"): null;
			String level = request.getParameter("level");
			request.setAttribute("flag",flag);
			request.setAttribute("level",StringUtil.isBlank(level)?"":level);
			request.setAttribute("quality",StringUtil.isBlank(quality)?"":quality);
			request.setAttribute("services",StringUtil.isBlank(services)?"":services);
			request.setAttribute("authorizedFlag",StringUtil.isBlank(authorizedFlag)?"":authorizedFlag);
			//此处暂时只做这个操作,后续需要将中文改成数字表示
			request.setAttribute("select_shop_id", StringUtils.isBlank(shop_id)?"":shop_id);
			if(StringUtils.isBlank(level)){
				request.setAttribute("select_level", "");
			}else{
				if("1".equals(level)){
					request.setAttribute("select_level", 1);
					level="合作过的供应商";
				}else if("3".equals(level)){
					request.setAttribute("select_level", 2);
					level="黑名单";
				}else if("2".equals(level)){
					request.setAttribute("select_level", 3);
					level="优选供应商";
				}
			}
			response.setHeader("content-type", "text/html;charset=UTF-8");
			request.setAttribute("select_test", "供应商测试");
			int start = Utility.getStringIsNull(request.getParameter("currpage")) ? Integer.parseInt(request.getParameter("currpage")) : 1;
			int pagesize = 20;
			Page<SupplierScoringBean> pageInfo = supplierScoringService.queryList(start, pagesize, shop_id, level,quality,services,
					authorizedFlag,flag,userid);
			List<SupplierScoringBean> scoringlist = pageInfo.getList();
			if (scoringlist == null) {
				LOG.warn("工厂列表查询为空");
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("shop_id", StringUtil.isBlank(shop_id)?"":shop_id);
			//合作过的供应商
			int cooperatedCount=supplierScoringService.getCooperatedCount();
			//优质供应商
			int highCount=supplierScoringService.getHighCount();
			//黑名单供应商
			int blacklistCount=supplierScoringService.getBlacklistCount();
			//普通供应商
			int ordinaryCount=supplierScoringService.getOrdinaryCount();
			request.setAttribute("cooperatedCount",cooperatedCount);
			request.setAttribute("highCount",highCount);
			request.setAttribute("blacklistCount",blacklistCount);
			request.setAttribute("ordinaryCount",ordinaryCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "supplierscoring";
	}

	/**
	 * 查询供应商下的样品
	 */
	@RequestMapping("supplierproducts")
	public String queryGoodsByShopId(HttpServletRequest request, HttpServletResponse response) {
		//获取登陆用户id
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        if (user == null) {
        	return "login";
		}
        String userid = String.valueOf(user.getId());//用户id
		List<SupplierProductsBean> supplierproducts = new ArrayList<SupplierProductsBean>();
		List<SupplierProductsBean> supplierProductsBeans = new ArrayList<SupplierProductsBean>();
		List<SupplierProductsBean> supplierProductsBeansList = new ArrayList<SupplierProductsBean>();
		Map<String,String> map=new HashMap<String,String>();
		try{
			String shop_id=request.getParameter("shop_id");
			boolean flag="1".equals(request.getParameter("flag"))?true:false;
			String goodsPid=request.getParameter("goodsPid");
			map.put("shop_id",shop_id);
			map.put("userid",userid);
			map.put("flag",String.valueOf(flag));
			map.put("goodsPid",StringUtil.isBlank(goodsPid)?null:goodsPid);
			supplierproducts=supplierScoringService.getAllShopInfo(map);
			if (!flag) {
				supplierProductsBeans=supplierScoringService.getAllShopGoodsInfo(map);
			}
			supplierProductsBeansList=supplierScoringService.getAllShopGoodsInfoList(map);
			request.setAttribute("supplierproducts", supplierproducts);
			request.setAttribute("supplierProductsBeans",supplierProductsBeans);
			request.setAttribute("supplierProductsBeansList",supplierProductsBeansList);
			request.setAttribute("map",map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "supplierproduct";
	}


	@RequestMapping("queryShopInfo")
	public String queryShopInfo(HttpServletRequest request, HttpServletResponse response) {
		try{
			String shop_id=request.getParameter("shop_id");
			SupplierScoringBean oneScoringByShopId = supplierScoringService.searchOneScoringByShopId(shop_id);
			PrintWriter out = response.getWriter();
			JSONObject jsonob = JSONObject.fromObject(oneScoringByShopId);
			out.print(jsonob);
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return "supplierscoring";
	}

	/**
	 * 查询供应商下的样品
	 */
	@RequestMapping("supplierproducts1")
	public String queryGoodsByShopId1(HttpServletRequest request, HttpServletResponse response) {
		List<SupplierProductsBean> supplierproducts = new ArrayList<SupplierProductsBean>();
		try {
//			DataSourceSelector.set("dataSource28hop");
			String shop_id = request.getParameter("shop_id");
			String goodsPid = request.getParameter("goods_pid");
			String admJson = Redis.hget(request.getSession().getId(), "admuser");
			if (admJson == null) {
				return "main_login";
			}
			Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson,Admuser.class);
			request.setAttribute("roleType", user.getRoletype());
			//表示该操作只查询库存协议和退货天数
			String inventory = request.getParameter("inventory");
			if(StringUtils.isNotBlank(inventory) && "true".equals(inventory)){
				SupplierScoringBean oneScoringByShopId = supplierScoringService.searchOneScoringByShopId(shop_id);
				PrintWriter out = response.getWriter();
				JSONObject jsonob = JSONObject.fromObject(oneScoringByShopId);
				out.print(jsonob);
				out.close();
				return "supplierscoring";
			}
			if (StringUtils.isBlank(goodsPid)||"-1".equals(goodsPid)) {
				// 表示不是通过goods_pid 和shop_id来的
				request.setAttribute("isGP", "false");
			} else {
				request.setAttribute("isGP", "true");
			}
			// 查询各部门对该供应商的打分信息
			List<SupplierProductsBean> supplierBean = supplierScoringService.querySupplierAllScoresSupplier(shop_id);
			request.setAttribute("supplierBean", supplierBean);
			// 只有shopid时表示对店铺进行打分
			if (StringUtils.isNotBlank(shop_id)&& StringUtils.isBlank(goodsPid)) {
				// 根据信息去查询该用户对店铺的打的分
				SupplierProductsBean supplierProductsBean = supplierScoringService.searchUserScoringByShopId(shop_id, user.getId());
				if (supplierProductsBean == null) {
					// 还没有打过分
					supplierProductsBean = new SupplierProductsBean();
				}
				if(StringUtils.isNotBlank(shop_id)&&!"0000".equals(shop_id)){
					supplierProductsBean.setShopId(shop_id);
					request.setAttribute("supplierscoringBean",
							supplierProductsBean);
					// 查询该店铺的协议库存信息
					SupplierScoringBean scoringByShopId = supplierScoringService.searchOneScoringByShopId(shop_id);
					request.setAttribute("scoringByShopId", scoringByShopId);
				}
			} else {
					if("-1".equals(goodsPid)){
						goodsPid=null;
					}
				// 加一个逻辑:只传一个shop_id且不是0000的时候,先去查询该shop_id下是否存在商品,没有的话默认对店铺进行打分
				if ( StringUtils.isNotBlank(shop_id) && !"0000".equals(shop_id)
						&& StringUtils.isNotBlank(goodsPid)) {
					List<SupplierProductsBean> queryProductsByShopId = supplierScoringService.queryProductsByShopId(shop_id, goodsPid, -1);
					// 表示该shop_id下没商品.默认对其打分
					if (queryProductsByShopId.size() == 0) {
						SupplierProductsBean supplierProductsBean = supplierScoringService.searchUserScoringByShopId(shop_id,user.getId());
						if (supplierProductsBean == null) {
							supplierProductsBean = new SupplierProductsBean();
						}
						supplierProductsBean.setShopId(shop_id);
						request.setAttribute("isGP", "true");
						request.setAttribute("supplierscoringBean",supplierProductsBean);
						SupplierScoringBean scoringByShopId = supplierScoringService.searchOneScoringByShopId(shop_id);
						request.setAttribute("scoringByShopId", scoringByShopId);
						// 查询该店铺的库存信息
						return "supplierproduct";
					}
				}
				// 查询不是当前用户打分的数据(包括只对单个商品进行打分)
				supplierproducts = supplierScoringService.queryProductsByShopId(shop_id, goodsPid, user.getId());
				// 查询需要用户打分的(包括单个商品)
				List<SupplierProductsBean> productsBeans = supplierScoringService.queryOneProductsUserId(shop_id, goodsPid);
				List<SupplierProductsBean> supplierProductsBeans = new ArrayList<SupplierProductsBean>();
				// 根据原始的产品信息去产品打分中查询是否存在
				for (SupplierProductsBean supplierProductsBean : productsBeans) {
					SupplierProductsBean newSupplierProductsBean = supplierScoringService
							.queryByProductScoreId(
									supplierProductsBean.getGoodsPid(),
									user.getId());
					// 当前用户打过
					if (newSupplierProductsBean != null) {
						newSupplierProductsBean
								.setGoodsImg(supplierProductsBean.getGoodsImg());
						supplierProductsBeans.add(newSupplierProductsBean);
					} else {
						supplierProductsBeans.add(supplierProductsBean);
					}
				}

				request.setAttribute("supplierproducts", supplierproducts);
				request.setAttribute("supplierProductsBeans",
						supplierProductsBeans);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "supplierproduct";
	}

	@RequestMapping("updateRemark")
	@ResponseBody
	public Map<String, String> updateRemark(HttpServletRequest request) {
		Map<String, String> map=new HashMap<String, String>();
		String id=request.getParameter("id");
		String remark=request.getParameter("newRemark");
		String newQuality=request.getParameter("newQuality");
		int row=supplierScoringService.updateRemark(id,remark,newQuality);
		map.put("row",row+"");
		return map;
	}

	@RequestMapping("saveproductscord")
	@ResponseBody
	public Map<String, String> saveproductscord(HttpServletRequest request) {
		Map<String, String> maps = new HashMap<String, String>();
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson,Admuser.class);
		if(user == null ){
			return maps;
		}
		String quality=request.getParameter("quality");
		String remarks=request.getParameter("remarks");
		String shopId=request.getParameter("shopId");
		String goodsPid=request.getParameter("goodsPid");
		String inven = request.getParameter("inven");
		String days = request.getParameter("rerundays");
		goodsPid=StringUtil.isBlank(goodsPid)?shopId:goodsPid;
		maps.put("quality",quality);
		maps.put("remarks",StringUtil.isBlank(remarks)?"":remarks);
		maps.put("shopId",shopId);
		maps.put("goodsPid",goodsPid);
		maps.put("admuserId",String.valueOf(user.getId()));
		maps.put("username",user.getAdmName());
		maps.put("days",StringUtil.isBlank(days)?"0":days);
		maps.put("inven",StringUtil.isBlank(inven)?"1":inven);
		try{
			//记录当次打分信息
			int row=supplierScoringService.saveSupplierProduct(maps);
			if (row>0 && goodsPid.equals(shopId)) {
				//店铺打分
				double qua=Double.valueOf(quality);
				String level="";
				//判断该店铺是否已算过平均分
				List<SupplierScoringBean> sList=supplierScoringService.getSupplierScoring(maps);
				if(sList.size()>0){
					//查询该店铺及产品打分的总质量分
					List<SupplierProductsBean> spList=supplierScoringService.getAllShopScoring(maps);
					int i=0;
					qua=0.00;
					for(SupplierProductsBean s:spList){
						qua+=s.getQuality();
						i++;
					}
					qua=qua/i;
					maps.put("level",getLevel(qua));
					maps.put("quality",String.valueOf(qua));
					supplierScoringService.updateSupplierScoring(maps);
				}else{
					maps.put("level",getLevel(qua));
					//首次不需要算店铺平均分直接插入信息
					supplierScoringService.saveSupplierScoring(maps);
				}
			}
			maps.put("flag","success");
		}catch (Exception e){
			e.printStackTrace();
			maps.put("flag","error");
		}
		return maps;
	}


	public String getLevel(double qua){
		String level="合作过的供应商";
		if (qua > 4 ) {
			level="优选供应商";
		} else if (qua >= 1 && qua<=2) {
			level="黑名单";
		}else if(qua>2 && qua<=4){
			level="普通供应商";
		}
		return level;
	}


	/**
	 * 保存打分后的产品huozhegongyingshang
	 */
	@RequestMapping("saveproductscord1")
	@ResponseBody
	public Map<String, Object> saveproductscord1(HttpServletRequest request) {
		SupplierProductsBean supplierProductsBean=new SupplierProductsBean();
		SupplierScoringBean supplierScoringBean =new SupplierScoringBean();
		Map<String, Object> maps = new HashMap<String, Object>();
		try {
			double quality=Double.parseDouble(request.getParameter("quality"));
			String remarks=request.getParameter("remarks");
			String shopId=request.getParameter("shopId");
			String goodsPid=request.getParameter("goodsPid");
			supplierProductsBean.setQuality(quality);
			supplierProductsBean.setService(0.00);
			supplierProductsBean.setRemarks(remarks);
			supplierProductsBean.setShopId(shopId);
			supplierProductsBean.setGoodsPid(goodsPid);
			supplierScoringBean.setShopId(shopId);
			String admJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson,Admuser.class);
			if (StringUtils.isNotBlank(supplierProductsBean.getGoodsPid())) {
				// 先判断是通过店铺还是通过产品打分
				supplierProductsBean.setUserId(user.getId());
				supplierProductsBean.setUserName(user.getAdmName());
				supplierProductsBean.setUpdateTime(new Date());
				supplierScoringService.saveOrupdateProductScord(supplierProductsBean);
				supplierScoringBean.setId(0);
			} else {
				//表示是通过供应商来的
				if (supplierScoringBean.getId() == 0) {
					supplierScoringBean.setCreateTime(new Date());
				}
				supplierScoringBean.setUpdateTime(new Date());
				if (supplierScoringBean.getQualityAvg() != 0.0 || supplierScoringBean.getServiceAvg() != 0.0) {
					supplierScoringService.saveOrupdateSupplierScoringScord(supplierScoringBean, user.getId(),user.getAdmName());
				}
				String inven = request.getParameter("inven");
				String days = request.getParameter("rerundays");
				  if(StringUtils.isNotBlank(inven) || StringUtils.isNotBlank(days)){
					// 此处执行的是通过供应商打分页面对库存修改的
					if (!StringUtils.isBlank(days) || !StringUtils.isBlank(inven)) {
						if (StringUtils.isNotBlank(inven)) {
							supplierScoringBean.setInventoryAgreement(Integer.parseInt(inven));
						}
						if (StringUtils.isNotBlank(days)) {
							supplierScoringBean.setReturnDays(Integer.parseInt(days));
						}
						SupplierScoringBean oneScoringByShopId = supplierScoringService.searchOneScoringByShopId(request.getParameter("shopId"));
						supplierScoringBean.setId(oneScoringByShopId==null?0:oneScoringByShopId.getId());
						supplierScoringService.saveOrUpdateInven(supplierScoringBean);
					}
				}
			}
			// 每次添加或者修改,都要对打过分的产品进行重新统计平均分
			List<SupplierProductsBean> listProducts = supplierScoringService.searchProductListByShopId(supplierProductsBean.getShopId());
			double qualitySum = 0;
			double serviceSum = 0;
			// 计算
			for (SupplierProductsBean supplierProduct : listProducts) {
				serviceSum += supplierProduct.getService();
				qualitySum += supplierProduct.getQuality();
			}
			// 查询供应商表中是否存在该打过分的数据
			SupplierScoringBean scoringBean = supplierScoringService.searchOneScoringByShopId(supplierProductsBean.getShopId());
			// 平均分 数据的封装
			if (scoringBean == null) {
				scoringBean = new SupplierScoringBean();
			}
			int i = 0;
			int j = 0;
			for (SupplierProductsBean sup : listProducts) {
				if(sup.getService() != 0){
					i++;
				}
				if(sup.getQuality() != 0){
					j++;
				}
			}
			double qualityAvg = 0.0;
			double serviceAvg = 0.0;
			if(j != 0){
				 qualityAvg = qualitySum / j;
			}
			if(i != 0){
				 serviceAvg = serviceSum / i;
			}
			scoringBean.setShopId(supplierProductsBean.getShopId());
			scoringBean.setQualityAvg(qualityAvg);
			scoringBean.setServiceAvg(serviceAvg);
			if (qualityAvg > 4 && serviceAvg > 4) {
				scoringBean.setLevel("优选供应商");
			} else if (qualityAvg >= 1 && serviceAvg >= 1 && qualityAvg<=2 && serviceAvg<=2 ) {
				scoringBean.setLevel("黑名单");
			}else if(qualityAvg>2 && qualityAvg<=4 && serviceAvg>2 && serviceAvg<=4){
				scoringBean.setLevel("普通供应商");
			}else {
				scoringBean.setLevel("合作过的供应商");
			}
			// 保存数据
			if (scoringBean.getId() == 0) {
				scoringBean.setCreateTime(new Date());
			}
			scoringBean.setUpdateTime(new Date());
			supplierScoringService.insertOrupdateScoringScoring(scoringBean);
			maps.put("id", supplierProductsBean.getId());
			maps.put("flag", "success");
			return maps;
		} catch (Exception e) {
			LOG.error("计算供应商打分时出现问题:" );
			e.printStackTrace();
		}
		maps.put("flag", "error");
		return maps;
	}

	@RequestMapping("lookQuestion")
	@ResponseBody
	public Map<String, Object> lookQuestion(HttpServletRequest request) {
		Map<String, Object> map=new HashMap<String, Object>();
		String pid=request.getParameter("pid");
		List<QueAns> list=supplierScoringService.lookQuestion(pid);
		map.put("list",list);
		return map;
	}

	/**
	 * 是否有库存协议处理
	 * 
	 * @Title saveorupdateInventoryAgreement
	 * @Description TODO
	 * @param request
	 * @return
	 * @return String
	 */
	@RequestMapping("saveorupdateInventoryAgreement")
	@ResponseBody
	public Map<String, Object> saveorupdateInventoryAgreement(
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			String shopid = request.getParameter("shopid");
			String inventoryAgreement = request
					.getParameter("inventoryAgreement");
			String returnDays = request.getParameter("returnDays");
			if (StringUtils.isBlank(inventoryAgreement)
					&& StringUtils.isBlank(returnDays)) {
				map.put("flag", "error");
				return map;
			}
			/*
			 * Integer inven =
			 * StringUtils.isNotBlank(inventoryAgreement)?Integer
			 * .parseInt(inventoryAgreement):null; Integer returnDay =
			 * StringUtils
			 * .isNotBlank(returnDays)?Integer.parseInt(returnDays):null;
			 */
			SupplierScoringBean bean = new SupplierScoringBean();
			bean.setShopId(shopid);
			if (StringUtils.isNotBlank(id) && !"0".equals(id)) {
				bean.setId(Integer.parseInt(id));
			} else {
				bean.setCreateTime(new Date());
			}
			if (StringUtils.isNotBlank(inventoryAgreement)) {
				bean.setInventoryAgreement(Integer.parseInt(inventoryAgreement));
			}
			if (StringUtils.isNotBlank(returnDays)) {
				bean.setReturnDays(Integer.parseInt(returnDays));
			}
			bean.setUpdateTime(new Date());
			supplierScoringService.saveOrUpdateInven(bean);
			map.put("flag", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "error");
		}
		return map;
	}
}
