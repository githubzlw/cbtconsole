package com.cbt.warehouse.ctrl;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.bean.TypeBean;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.AliCategoryPojo;
import com.cbt.warehouse.pojo.ExpressRecord;
import com.cbt.warehouse.pojo.TbGoodsSampleDetailsPojo;
import com.cbt.warehouse.pojo.TbGoodsSamplePojo;
import com.cbt.warehouse.service.TakeGoodsService;
import com.cbt.warehouse.util.OrderInfoPage;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.SampleGoodsBean;
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
import java.util.regex.Pattern;

@Controller
@RequestMapping("/takeGoods")
public class TakeGoodsCtrl extends UtilAll {
	@Autowired
	private TakeGoodsService goodsService;

	// ****************************************拿货
	// 添加部分***********start*****************************************

	@RequestMapping(value = "/mainTbGoodsSample.do")
	public String mainTbGoodsSample(HttpServletRequest request, Model model) {
		return "tbGoodsSample";
	}

	// 读取样品
	@RequestMapping(value = "/getGoodsDataById.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGoodsDataById(HttpServletRequest request, Model model) {
		String url = request.getParameter("url");
		SampleGoodsBean bean=new SampleGoodsBean();
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("url", url);
		try{
			DataSourceSelector.set("dataSource127hop");
			bean = goodsService.getGoodsDataById(map);
			if (null == bean) {
				GoodsBean goodsBean = ParseGoodsUrl.parseGoods2(url, null, null);
				bean = new SampleGoodsBean();
				bean.setGoodsid(0);
				bean.setUrl(url);
				bean.setImg(goodsBean.getpImage().get(0));
				bean.setFactory_price(0.00);
				bean.setFlag(null);
				bean.setAmazongoosid("");
				bean.setAmazongoosprice(0.00);
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

	// 保存扫描记录
	@RequestMapping(value = "/insertExpressRecord.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertExpressRecord(HttpServletRequest request, Model model) {
		String express_code = request.getParameter("express_code");
		String r="1";
		try{
			SendMQ sendMQ = new SendMQ();
			sendMQ.sendMsg(new RunSqlModel("insert into express_record (create_time,express_code)values(now(),'"+express_code+"');"));
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return r;
	}

	// 读取所有扫描记录
	@RequestMapping(value = "/checkOrder.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String getcheckOrder(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");// 订单号
		List orderList = goodsService.getidRelationtable(orderid);
		for (int i = 0; i < orderList.size(); i++) {
			System.out.println("orderid=" + orderList.get(i));
		}
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderid", orderid);
		return "checkOrder";

	}

	// 读取所有扫描记录
	@RequestMapping(value = "/getExpressRecord.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String getExpressRecord(HttpServletRequest request, Model model) {
		int pageNum = 1;
		int pageSize = 50;
		String express_code = request.getParameter("express_code");
		String ckStartTime = (String) request.getParameter("ckStartTime");
		String ckEndTime = (String) request.getParameter("ckEndTime");
		String t = request.getParameter("pageNum");
		if (t != null && !"".equals(t)) {
			pageNum = Integer.parseInt(t);
		}
		t = request.getParameter("pageSize");
		if (t != null && !"".equals(t)) {
			pageSize = Integer.parseInt(t);
		}
		try{
			int startNum = pageNum * pageSize - pageSize;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("express_code", express_code);
			map.put("ckStartTime", ckStartTime);
			map.put("ckEndTime", ckEndTime);
			DataSourceSelector.set("dataSource127hop");
			int count = goodsService.getCountExpressRecord(map);
			map.put("startNum", startNum);
			map.put("endNum", pageSize);
			List<ExpressRecord> recordlist = goodsService.getExpressRecord(map);
			OrderInfoPage oip = new OrderInfoPage();
			oip.setRecordlist(recordlist);
			oip.setPageNum(pageNum);
			oip.setPageSize(pageSize);
			oip.setPageSum(count);
			oip.setCkEndTime(ckEndTime);
			oip.setCkStartTime(ckStartTime);
			oip.setExpress_code(express_code);
			request.setAttribute("oip", oip);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return "expressrecord";
	}

	// 所有样品
	@RequestMapping(value = "/getTbGoodsSample.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSample(HttpServletRequest request, Model model) {
		String title = request.getParameter("title");

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("title", title);
		List<TbGoodsSamplePojo> tbGoodslist=new ArrayList<TbGoodsSamplePojo>();
		try{
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = goodsService
					.getTbGoodsSample(map);
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
		TbGoodsSamplePojo tbGoodslist=new TbGoodsSamplePojo();
		if (isStringNull(id)) {
			return "1001";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try{
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = goodsService.getTbGoodsSampleById(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONObject.fromObject(tbGoodslist).toString();
	}

	// 读取样品类型
	@RequestMapping(value = "/getAliCategory.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAliCategory(HttpServletRequest request, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AliCategoryPojo> list=new ArrayList<AliCategoryPojo>();
		try{
			map.put("sql","select * from ali_category where LENGTH(0+path)=LENGTH(path) order by path*1 ");
			DataSourceSelector.set("dataSource127hop");
			list = goodsService.getAliCategory(map);
		}catch (Exception e){
			e.printStackTrace();
		}  finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONArray.fromObject(list).toString();
	}

	// 读取样品下一级类型
	@RequestMapping(value = "/getSubType.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSubType(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("sql",
				"select * from ali_category where path like  '"
						+ id
						+ ",%' and LENGTH(replace(path,',','--'))-LENGTH(path)=1 order by path*1 ");

		DataSourceSelector.set("dataSource127hop");
		List<AliCategoryPojo> list = goodsService.getAliCategory(map);
		DataSourceSelector.restore();

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
			list = goodsService.getAliCategory(map);
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
		String viewimg = request.getParameter("viewimg");
		String discount = request.getParameter("discount");
		String discountprice = request.getParameter("discountprice");
		String minnum = request.getParameter("minnum");
		String defaultnum = request.getParameter("defaultnum");
		String remark = request.getParameter("remark");
		String ymx_discount = request.getParameter("ymx_discount");
		// 判断数据是否为空
		if (isStringNull(category) && isStringNull(title)
				&& isStringNull(viewimg) && isStringNull(discount)
				&& isStringNull(discountprice) && isStringNull(minnum)
				&& isStringNull(defaultnum) && isStringNull(remark)) {
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("category", category);
		map.put("title", title);
		map.put("viewimg", viewimg);
		map.put("discount", discount);
		map.put("discountprice", discountprice);
		map.put("minnum", minnum);
		map.put("defaultnum", defaultnum);
		map.put("remark", remark);
		map.put("ymx_discount", ymx_discount);
		try{
			SendMQ sendMQ = new SendMQ();
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
			if (adm == null) {
				return "1003";
			}
			map.put("createuser", adm.getId());
			map.put("category", map.get("category").toString().replaceAll("'", "&apos;"));
			map.put("title", map.get("title").toString().replaceAll("'", "&apos;"));
			int ret =1;// goodsService.insertTbGoodsSample(map);
			sendMQ.sendMsg(new RunSqlModel("insert into tb_goods_sample_details (flag,type,weight,goodssampleid,goodsid,goodsname,goodsurl,goodsimg,goodsprice,amazongoosid," +
					"amazongoosprice,originalprice) values('"+map.get("flag")+"','"+map.get("type")+"','"+map.get("weight")+"','"+map.get("goodssampleid")+"','"+map.get("goodsid")+"'" +
					",'"+map.get("goodsname")+"','"+map.get("goodsurl")+"'," +
					"'"+map.get("goodsimg")+"','"+map.get("goodsprice")+"','"+map.get("amazongoosid")+"','"+map.get("amazongoosprice")+"','"+map.get("originalprice")+"')"));
			if (ret > 0) {
				return "1000";
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return "1002";
	}

	// 样品所有商品
	@RequestMapping(value = "/getTbGoodsSampleDetails.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSampleDetails(HttpServletRequest request,
			Model model) {
		String goodssampleid = request.getParameter("goodssampleid");
		List<TbGoodsSampleDetailsPojo> tbGoodslist=new ArrayList<TbGoodsSampleDetailsPojo>();
		if (isStringNull(goodssampleid)) {
			return "1001";
		}
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goodssampleid", goodssampleid);
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = goodsService.getTbGoodsSampleDetails(map);
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
		if (isStringNull(id)) {
			return "1001";
		}
		try{
			SendMQ sendMQ = new SendMQ();
			sendMQ.sendMsg(new RunSqlModel("delete from tb_goods_sample_details where id='"+id+"'"));
			int ret =1;// goodsService.delteCommodityByid(map);
			if (ret > 0) {
				return "1000";
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return "1004";
	}

	// 删除单件商品
	@RequestMapping(value = "/deleteTbGoodsSample.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteTbGoodsSample(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		int ret =1;
		try{
			SendMQ sendMQ = new SendMQ();
			if (isStringNull(id)) {
				return "1001";
			}
			sendMQ.sendMsg(new RunSqlModel("delete from tb_goods_sample where id='"+id+"'"));
			sendMQ.sendMsg(new RunSqlModel("delete from tb_goods_sample_details where goodssampleid ='"+id+"'"));
			sendMQ.closeConn();
			if (ret > 0) {
				return "1000";
			} else {
				return "1004";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "1001";
	}

	// 插入单件商品
	@RequestMapping(value = "/insertTbGoodsSampleDetails.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertTbGoodsSampleDetails(HttpServletRequest request,
			Model model) {
		String goodssampleid = request.getParameter("goodssampleid");
		String goodsid = request.getParameter("goodsid");
		String goodsname = request.getParameter("goodsname");
		String goodsurl = request.getParameter("goodsurl");
		String goodsimg = request.getParameter("goodsimg");
		String goodsprice = request.getParameter("goodsprice");
		String amazongoosid = request.getParameter("amazongoosid");
		String amazongoosprice = request.getParameter("amazongoosprice");
		String type = request.getParameter("type");
		String weight = request.getParameter("weight");
		String originalprice = request.getParameter("originalprice");
		String flag = request.getParameter("flag");
		try{
			SendMQ sendMQ = new SendMQ();
			if (isStringNull(goodssampleid)) {
				return "1001";
			}
			sendMQ.sendMsg(new RunSqlModel("insert into tb_goods_sample_details (flag,type,weight,goodssampleid,goodsid,goodsname,goodsurl,goodsimg," +
					"goodsprice,amazongoosid,amazongoosprice,originalprice)values('"+flag+"','"+type+"','"+weight+"','"+goodssampleid+"','"+goodsid+"','"+goodsname+"','"+goodsurl+"'," +
					"'"+goodsimg+"','"+goodsprice+"','"+amazongoosid+"','"+amazongoosprice+"','"+originalprice+"')"));
			int ret =1;
			sendMQ.closeConn();
			if (ret > 0) {
				return "1000";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "1002";
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
		String amazongoosid = request.getParameter("amazongoosid");
		String amazongoosprice = request.getParameter("amazongoosprice");
		String type = request.getParameter("type");
		String weight = request.getParameter("weight");
		String originalprice = request.getParameter("originalprice");
		String flag = request.getParameter("flag");
		try{
			SendMQ sendMQ = new SendMQ();
			if (isStringNull(id)) {
				return "1001";
			}
			int ret =1;
			sendMQ.sendMsg(new RunSqlModel("update tb_goods_sample_details set flag='"+flag+"',originalprice='"+originalprice+"',type='"+type+"',weight='"+weight+"'," +
					"goodsid='"+goodsid+"',goodsname='"+goodsname+"',goodsurl='"+goodsurl+"',goodsimg='"+goodsimg+"'," +
					"goodsprice='"+goodsprice+"',amazongoosid='"+amazongoosid+"',amazongoosprice='"+amazongoosprice+"'where id='"+id+"'"));
			sendMQ.closeConn();
			if (ret > 0) {
				return "1000";
			} else {
				return "1005";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "1001";
	}

	// 修改单件样品
	@RequestMapping(value = "/updateTbGoodsSampleByid.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateTbGoodsSampleByid(HttpServletRequest request, Model model) {
		String cid = request.getParameter("cid");
		String id = request.getParameter("id");
		String category = request.getParameter("category");
		String title = request.getParameter("title");
		String discount = request.getParameter("discount");
		String discountprice = request.getParameter("discountprice");
		String minnum = request.getParameter("minnum");
		String defaultnum = request.getParameter("defaultnum");
		String remark = request.getParameter("remark");
		String ymx_discount = request.getParameter("ymx_discount");
		try{
			if (isStringNull(id)) {
				return "1001";
			}
			Map<String, Object> map = new HashMap<String, Object>();
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
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
			if (adm == null) {
				return "1003";
			}
			map.put("createuser", adm.getId());
			DataSourceSelector.set("dataSource127hop");
			int ret = goodsService.updateTbGoodsSampleByid(map);
			if (ret > 0) {
				return "1000";
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return "1005";
	}

	// 批量导入
	@RequestMapping(value = "/batchImportTbGSD.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String batchImportTbGSD(HttpServletRequest request, Model model) {
		String useridORorderid = request.getParameter("useridORorderid");
		if (null == useridORorderid || "".equals(useridORorderid)) {
			return null;
		}
		String profit = request.getParameter("profit");
		String goodssampleid = request.getParameter("goodssampleid");

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		// map.put("useridORorderid", useridORorderid);
		String regex = "[a-zA-Z]";
		if (Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
				.matcher(useridORorderid).find()) {
			map.put("orderid", useridORorderid);
		} else {
			map.put("userid", Integer.parseInt(useridORorderid));
		}

		map.put("profit", profit);
		map.put("goodssampleid", goodssampleid);

		DataSourceSelector.set("dataSource127hop");
		int ret = goodsService.batchImportTbGSD(map);
		DataSourceSelector.restore();

		return ret + "";
	}

	// 单件样品
	@RequestMapping(value = "/getTbGoodsSampleDetailsById.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTbGoodsSampleDetailsById(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		TbGoodsSampleDetailsPojo tbGoodslist=new TbGoodsSampleDetailsPojo();
		try{
			if (isStringNull(id)) {
				return "1001";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			DataSourceSelector.set("dataSource127hop");
			tbGoodslist = goodsService.getTbGoodsSampleDetailsById(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return net.sf.json.JSONObject.fromObject(tbGoodslist).toString();
	}

}
