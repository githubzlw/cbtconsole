package com.cbt.warehouse.ctrl;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.bean.TypeBean;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.AliCategoryPojo;
import com.cbt.warehouse.pojo.EventGoodsDetailsPojo;
import com.cbt.warehouse.pojo.EventGoodsPojo;
import com.cbt.warehouse.service.EventGoodsService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.EventGoodsBean;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/eventGoods")
public class EventGoodsCtrl extends UtilAll {
	@Autowired
	private EventGoodsService eventGoodsService;

	// ****************************************拿货
	// 添加部分***********start*****************************************

	@RequestMapping(value = "/mainEventGoods.do")
	public String mainEventGoods(HttpServletRequest request, Model model) {
		return "EventGoods";
	}

	// 读取样品
	@RequestMapping(value = "/getGoodsDataById.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGoodsDataById(HttpServletRequest request, Model model) {
		String url = request.getParameter("url");
		Map<String, Object> map = new HashMap<String, Object>();
		EventGoodsBean bean=new EventGoodsBean();
		try{
			// 如果传入的url是全路径的话，截取？前面的，保存到map里。
			map.put("url", url);
			DataSourceSelector.set("dataSource127hop");
			bean = eventGoodsService.getGoodsDataById(map);
			// 如果在数据库中没有查找到数据的话，就去AilExpress中爬数据。
			if (null == bean) {
				GoodsBean goodsBean = ParseGoodsUrl.parseGoods2(url, null, null);
				bean = new EventGoodsBean();
				bean.setGoodsid(0);
				bean.setGoodsurl(TypeUtils.encodeGoods(goodsBean.getpUrl()));
				bean.setImg(goodsBean.getpImage().get(0));
				bean.setFactory_price(0.00);
				bean.setFlag(null);
				bean.setName(goodsBean.getpName());
				ArrayList<TypeBean> type = goodsBean.getType();
				// sampleType1是显示在样品区的样品的规格
				String sampleType1 = "";
				for (TypeBean typeBean : type) {
					String sampleType = typeBean.getType() + typeBean.getValue()
							+ "@" + typeBean.getId() + ",";
					sampleType1 = sampleType1 + sampleType;
				}
				bean.setType(sampleType1);
				bean.setWeight(goodsBean.getWeight());
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONObject.fromObject(bean).toString();
	}

	
	// 所有样品
	@RequestMapping(value = "/getTbGoodsSample.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSample(HttpServletRequest request, Model model) {
		String title = request.getParameter("title");
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		List<EventGoodsPojo> tbGoodslist=new ArrayList<EventGoodsPojo>();
		map.put("title", title);
		try{
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = eventGoodsService.getTbGoodsSample(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONArray.fromObject(tbGoodslist).toString();
	}

	// 单件样品
	@RequestMapping(value = "/getTbGoodsSampleById.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSampleById(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		EventGoodsPojo eventGoods=new EventGoodsPojo();
		try{
			if (isStringNull(id)) {
				return "1001";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			DataSourceSelector.set("dataSource127hop");
			eventGoods = eventGoodsService.getTbGoodsSampleById(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONObject.fromObject(eventGoods).toString();
	}

	// 读取样品类型
	@RequestMapping(value = "/getAliCategory.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAliCategory(HttpServletRequest request, Model model) {
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("sql","select * from ali_category where LENGTH(0+path)=LENGTH(path) order by path*1 ");
		List<AliCategoryPojo> list=new ArrayList<AliCategoryPojo>();
		try{
			DataSourceSelector.set("dataSource127hop");
			list = eventGoodsService.getAliCategory(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONArray.fromObject(list).toString();
	}

	// 读取样品下一级类型
	@RequestMapping(value = "/getSubType.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSubType(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		Map<String, Object> map = new HashMap<String, Object>();
		List<AliCategoryPojo> list=new ArrayList<AliCategoryPojo>();
		try{
			map.put("sql","select * from ali_category where path like  '"+ id+ ",%' and LENGTH(replace(path,',','--'))-LENGTH(path)=1 order by path*1 ");
			DataSourceSelector.set("dataSource127hop");
			list = eventGoodsService.getAliCategory(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONArray.fromObject(list).toString();
	}

	// 读取样品下一级类型
	@RequestMapping(value = "/getSubType2.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSubType2(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		Map<String, Object> map = new HashMap<String, Object>();
		List<AliCategoryPojo> list=new ArrayList<AliCategoryPojo>();
		try{
			map.put("sql","select * from ali_category where path like  '"+ id+ ",%' and LENGTH(replace(path,',','--'))-LENGTH(path)=2 order by path*1 ");
			DataSourceSelector.set("dataSource127hop");
			list = eventGoodsService.getAliCategory(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONArray.fromObject(list).toString();
	}

	// 插入样品
	@RequestMapping(value = "/insertTbGoodsSample.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertTbGoodsSample(HttpServletRequest request, Model model) {
		String cid = request.getParameter("cid");
		String category = request.getParameter("category");
		String title = request.getParameter("title");
		Map<String, Object> map = new HashMap<String, Object>();
		int ret=1;
		try{
			map.put("cid", cid);
			map.put("category", category);
			map.put("title", title);
			map.put("category", map.get("category").toString().replaceAll("'", "&apos;"));
			map.put("title", map.get("title").toString().replaceAll("'", "&apos;"));

			SendMQ.sendMsg(new RunSqlModel("insert into eventgoods (cid,category,title,createtime) values('"+map.get("cid")+"','"+map.get("category")+"','"+map.get("title")+"',now())"));

		}catch (Exception e){
			e.printStackTrace();
		}
		if (ret > 0) {
			return "1000";
		} else {
			return "1002";
		}
	}

	// 样品所有商品
	@RequestMapping(value = "/getTbGoodsSampleDetails.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSampleDetails(HttpServletRequest request,
			Model model) {
		String goodssampleid = request.getParameter("goodssampleid");

		if (isStringNull(goodssampleid)) {
			return "1001";
		}

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		List<EventGoodsDetailsPojo> tbGoodslist=new ArrayList<EventGoodsDetailsPojo>();
		map.put("goodssampleid", goodssampleid);
		try{
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = eventGoodsService.getTbGoodsSampleDetails(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONArray.fromObject(tbGoodslist).toString();
	}

	// 删除单件商品
	@RequestMapping(value = "/delteCommodityByid.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delteCommodityByid(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		id= StringUtil.isBlank(id)?"00000":id;
		int ret=1;
		try{

			SendMQ.sendMsg(new RunSqlModel("delete from eventgoodsdetails where id='"+id+"'"));

		}catch (Exception e){
			e.printStackTrace();
		}
		if (isStringNull(id)) {
			return "1001";
		}
		if (ret > 0) {
			return "1000";
		} else {
			return "1004";
		}

	}

	// 删除单件商品
	@RequestMapping(value = "/deleteTbGoodsSample.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteTbGoodsSample(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");

		if (isStringNull(id)) {
			return "1001";
		}
		int ret=0;
		try{
			DataSourceSelector.set("dataSource127hop");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			ret = eventGoodsService.deleteTbGoodsSample(map);
			map.put("goodssampleid", id);
			ret += eventGoodsService.delteGoodsSampleDetails(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		if (ret > 0) {
			return "1000";
		} else {
			return "1004";
		}

	}

	// 插入单件商品
	@RequestMapping(value = "/insertTbGoodsSampleDetails.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertTbGoodsSampleDetails(HttpServletRequest request,
			Model model) {
		String goodssampleid = request.getParameter("goodssampleid");
		String goodsid = request.getParameter("goodsid");
		String goodsname = request.getParameter("goodsname");
		String goodsimg = request.getParameter("goodsimg");
		String goodsurl=request.getParameter("goodsurl");
		String originalprice = request.getParameter("originalprice");
		String goodsprice = request.getParameter("goodsprice");
		String discount = request.getParameter("discount");
		String type = request.getParameter("type");
		String weight = request.getParameter("weight");
		String flag = request.getParameter("flag");
		String avilibleStock = request.getParameter("avilibleStock");
		String sold = request.getParameter("sold");
		int ret=1;
		try{
			if (isStringNull(goodssampleid)) {
				return "1001";
			}
			goodsname=goodsname.toString().replaceAll("'", "&apos;");

			SendMQ.sendMsg(new RunSqlModel("insert into eventgoodsdetails (flag,type,weight,goodssampleid,goodsid,goodsname,goodsurl,goodsimg,avilibleStock,sold," +
					"goodsprice,originalprice,discount) values('"+flag+"','"+type+"','"+weight+"','"+goodssampleid+"','"+goodsid+"','"+goodsname+"','"+goodsurl+"'," +
					"'"+goodsimg+"','"+avilibleStock+"','"+sold+"','"+goodsprice+"','"+originalprice+"','"+discount+""));

		}catch (Exception e){
			e.printStackTrace();
		}
		if (ret > 0) {
			return "1000";
		} else {
			return "1002";
		}
	}

	// 修改单件商品
	@RequestMapping(value = "/updateTbGoodsSampleDetailsByid.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateTbGoodsSampleDetailsByid(HttpServletRequest request,
			Model model) {
		String id = request.getParameter("id");
		String goodsid = request.getParameter("goodsid");
		String goodsname = request.getParameter("goodsname");
		String goodsurl = request.getParameter("goodsurl");
		String goodsimg = request.getParameter("goodsimg");
		String goodsprice = request.getParameter("goodsprice");
		String type = request.getParameter("type");
		String weight = request.getParameter("weight");
		String originalprice = request.getParameter("originalprice");
		String discount = request.getParameter("discount");
		String flag = request.getParameter("flag");
		String avilibleStock = request.getParameter("avilibleStock");
		String sold = request.getParameter("sold");
		int ret=1;
		try{
			if (isStringNull(id)) {
				return "1001";
			}

			SendMQ.sendMsg(new RunSqlModel("update eventgoodsdetails set flag='"+flag+"',originalprice='"+originalprice+"',type='"+type+"',weight='"+weight+"'," +
					"goodsid='"+goodsid+"',goodsname='"+goodsname+"',goodsurl='"+goodsurl+"',goodsimg='"+goodsimg+"'," +
					"goodsprice='"+goodsprice+"',discount='"+discount+"',avilibleStock='"+avilibleStock+"',sold='"+sold+"'where id='"+id+"'"));

		}catch (Exception e){
			e.printStackTrace();
		}
		if (ret > 0) {
			return "1000";
		} else {
			return "1005";
		}

	}

	// 修改单件样品
	@RequestMapping(value = "/updateTbGoodsSampleByid.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateTbGoodsSampleByid(HttpServletRequest request,
			Model model) {

		String cid = request.getParameter("cid");
		String id = request.getParameter("id");
		String category = request.getParameter("category");
		String title = request.getParameter("title");
		// String viewimg = request.getParameter("viewimg");
		String discount = request.getParameter("discount");
		String discountprice = request.getParameter("discountprice");
		String minnum = request.getParameter("minnum");
		String defaultnum = request.getParameter("defaultnum");
		String remark = request.getParameter("remark");
		String ymx_discount = request.getParameter("ymx_discount");

		if (isStringNull(id)) {
			return "1001";
		}
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数

		map.put("cid", cid);
		map.put("id", id);
		map.put("category", category);
		map.put("title", title);
		map.put("discount", discount);
		map.put("discountprice", discountprice);
		map.put("minnum", minnum);
		map.put("defaultnum", defaultnum);
		map.put("remark", remark);
		map.put("ymx_discount", ymx_discount);

		// 创建人
		String admuserJson = Redis
				.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
				Admuser.class);
		if (adm == null) {
			return "1003";
		}
		map.put("createuser", adm.getId());
		int ret=0;
		try{
			DataSourceSelector.set("dataSource127hop");
			ret = eventGoodsService.updateTbGoodsSampleByid(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		if (ret > 0) {
			return "1000";
		} else {
			return "1005";
		}

	}

	// 单件样品
	@RequestMapping(value = "/getTbGoodsSampleDetailsById.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSampleDetailsById(HttpServletRequest request,
			Model model) {
		String id = request.getParameter("id");

		if (isStringNull(id)) {
			return "1001";
		}

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("id", id);
		EventGoodsDetailsPojo tbGoodslist=new EventGoodsDetailsPojo();
		try{
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = eventGoodsService.getTbGoodsSampleDetailsById(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}

		return net.sf.json.JSONObject.fromObject(tbGoodslist).toString();
	}
	// ****************************************拿货
	// 添加部分***********end*****************************************

}
