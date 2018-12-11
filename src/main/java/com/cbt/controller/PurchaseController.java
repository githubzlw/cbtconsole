package com.cbt.controller;

import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.bean.ShippingBean;
import com.cbt.common.StringUtils;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.ChangeGoodsLogPojo;
import com.cbt.warehouse.pojo.DisplayBuyInfo;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.PurchasesBean;
import com.cbt.website.dao2.Page;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.service.IPurchaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/purchase")
public class PurchaseController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PurchaseController.class);
	private PurchaseServer purchaseServer = new PurchaseServerImpl();
	@Autowired
	private IPurchaseService iPurchaseService;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private IWarehouseService iWarehouseService;
	@RequestMapping(value = "/determineStraighthair")
	@ResponseBody
	protected void determineStraighthair(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<Object,Object> map = new HashMap<Object,Object>();
		response.setCharacterEncoding("utf-8");
		String orderid = request.getParameter("orderid");
		String goodsid=request.getParameter("goodsid");
		String odid=request.getParameter("odid");
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("odid", odid);
		int row=purchaseServer.determineStraighthair(map);
		PrintWriter out = response.getWriter();
		out.print(row);
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/getDetailsChangeInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDetailsChangeInfo(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");

		Map<String, String> map = new HashMap<String, String>();
		map.put("goodsid", goodsid);
		map.put("orderid", orderid);
		List<ChangeGoodsLogPojo> list = iPurchaseService.getDetailsChangeInfo(map);
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 全部取消货源
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allQxQrNew")
	@ResponseBody
	protected void allQxQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=iPurchaseService.allQxQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	/**
	 * 采购是否使用库存
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/useInventory")
	@ResponseBody
	protected void useInventory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<String, String> map = new HashMap<String, String>();
		int row=0;
		String od_id=request.getParameter("od_id");
		String isUse=request.getParameter("isUse");
		map.put("od_id", od_id);
		map.put("isUse", isUse);
		row = iPurchaseService.useInventory(map);
		PrintWriter out = response.getWriter();
		out.print(row + "");
		out.close();
	}

	/**
	 * 获取其他货源
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getOtherSources")
	@ResponseBody
	protected void getOtherSources(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		response.setCharacterEncoding("UTF-8");
		String orderNo = request.getParameter("orderNo");
		String odid = request.getParameter("odid");
		String goods_url = request.getParameter("goods_url");
		String otherUrl = "";
		int st = iPurchaseService.checkOrder(orderNo, odid);
		if (st == 111) { // 订单被取消
			otherUrl = "cancel";
		} else if (st == 2) { // 商品被取消
			otherUrl = "cancel";
		} else {
			otherUrl = iPurchaseService.getOtherSources(orderNo, odid, goods_url);
		}
		PrintWriter out = response.getWriter();
		out.print(otherUrl);
		out.flush();
		out.close();
	}

	/**
	 * 原链接订单信息录入
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/insertSources")
	@ResponseBody
	protected void insertSources(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Map<String, String> map = new HashMap<String, String>();
		String sessionId = request.getSession().getId();
		String admuserw = Redis.hget(sessionId, "admuser");
		SerializeUtil su = new SerializeUtil();
		Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
		int adminid = admuser.getId();
		String shipno = request.getParameter("shipno");
		String odid = request.getParameter("odid");
		String taobaoPrice = request.getParameter("taobaoPrice");
		taobaoPrice=StringUtil.isBlank(taobaoPrice)?"0":taobaoPrice;
		String taobaoFeight = request.getParameter("taobaoFeight");
		taobaoFeight=StringUtil.isBlank(taobaoFeight)?"0":taobaoFeight;
		String delivary_date = request.getParameter("delivary_date");
		String goodsQty = request.getParameter("goodsQty");
		goodsQty=StringUtil.isBlank(goodsQty)?"0":goodsQty;
		String username = getInsertSourceString(request, map, adminid, shipno, taobaoPrice, taobaoFeight, delivary_date, goodsQty,odid);
		double prices = Integer.parseInt(goodsQty) * Double.parseDouble(taobaoPrice) + Double.parseDouble(taobaoFeight);
		map.put("totalprice", String.valueOf(prices));
		int row=0;
		if(StringUtil.isNotBlank(username)){
			row = iPurchaseService.insertSources(map);
		}
		PrintWriter out = response.getWriter();
		out.print(row + "");
		out.flush();
		out.close();
	}

	private String getInsertSourceString(HttpServletRequest request, Map<String, String> map, int adminid, String shipno, String taobaoPrice, String taobaoFeight, String delivary_date, String goodsQty, String odid) {
		String taobao_url = request.getParameter("taobao_url");
		String goods_sku = request.getParameter("goods_sku");
		String taobao_name = request.getParameter("taobao_name");
		String preferential = request.getParameter("preferential");
		String paydate = request.getParameter("paydate");
		String goods_imgs = request.getParameter("goods_imgs");
		String admName =request.getParameter("admName");
		String TbOrderid = request.getParameter("TbOrderid");
		String TbGoodsid = request.getParameter("TbGoodsid");
		String username = iWarehouseService.getBuyerNames(String.valueOf(adminid));
		map.put("username", username);
		map.put("shipno", shipno);
		map.put("taobaoPrice", taobaoPrice);
		map.put("taobaoFeight", taobaoFeight);
		map.put("goodsQty", goodsQty);
		map.put("taobao_url", taobao_url);
		map.put("goods_sku", goods_sku);
		map.put("taobao_name", taobao_name);
		map.put("preferential", preferential);
		map.put("paydate", paydate);
		map.put("goods_imgs", goods_imgs);
		map.put("delivary_date", delivary_date);
		map.put("TbOrderid", TbOrderid);
		map.put("TbGoodsid", TbGoodsid);
		map.put("adminid", String.valueOf(adminid));
		map.put("odid",odid);
		return username;
	}

	/**
	 * 一键取消采购
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allQxcgQrNew")
	@ResponseBody
	protected void allQxcgQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=iPurchaseService.allQxcgQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	/**
	 * 一键确认采购
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allcgqrQrNew")
	@ResponseBody
	protected void allcgqrQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas=iPurchaseService.allcgqrQrNew(orderNo,Integer.valueOf(admid));
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	/**
	 *  货源确认
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/PurchaseComfirmTwoHyqr")
	@ResponseBody
	protected void PurchaseComfirmTwoHyqr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String goodsurl = request.getParameter("goodsurl");
		String googsimg = request.getParameter("googsimg");
		// 用户价格
		String goodsprice = request.getParameter("goodsprice");
		String goodstitle = request.getParameter("goodstitle");
		// 用户定量
		String googsnumberr = request.getParameter("googsnumber");
		int googsnumber = Integer.parseInt(googsnumberr);
		String oldValue = request.getParameter("oldValue");
		String newValue = request.getParameter("newValue");
		String purchaseCountt = request.getParameter("purchaseCount");
		int purchaseCount = Integer.parseInt(purchaseCountt);
		String orderNo = request.getParameter("orderno");
		String odid = request.getParameter("od_id");
		String adminid = request.getParameter("adminid");
		int admid=0;
		if(adminid==null || "".equals(adminid)){
			String sessionId = request.getSession().getId();
			String admuserw = Redis.hget(sessionId, "admuser");
			SerializeUtil su = new SerializeUtil();
			Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
			admid=admuser.getId();
		}else{
			admid = Integer.parseInt(adminid);
		}
		if(admid==999){
			admid=9;
		}
		String useridd = request.getParameter("userid");
		int userid = Integer.parseInt(useridd);
		String goodsdata_idd = request.getParameter("goodsdataid");
		int goodsdataid = Integer.parseInt(goodsdata_idd);
		//判断订单是否被取消
		int st = iPurchaseService.checkOrder(orderNo,String.valueOf(odid));
		int od_id = Integer.parseInt(request.getParameter("od_id"));// order_details
		String child_order_no = request.getParameter("child_order_no");
		String isDropshipOrder = request.getParameter("isDropshipOrder");
		if (st == 111) {
			// 订单被取消
		} else if (st == 2) {
			// 商品被取消 PurchaseComfirmTwoHyqr
		} else {
			st = iPurchaseService.PurchaseComfirmTwoHyqr(userid, orderNo, od_id, 0, goodsdataid, admid, goodsurl,
					googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount, child_order_no,
					isDropshipOrder);
			if (st != 0) {
				// 记录操作
				Date nowTime = new Date();
				SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String writeStr = "******货源确认操作" + odid + ">>>>>>     时间:" + time.format(nowTime) + "      ";
				writeStr += "userid:" + userid + "      ";
				writeStr += "orderNo:" + orderNo + "      ";
				writeStr += "goodsid:" + odid + "      ";
				writeStr += "录入货源:" + newValue + "      ";
				writeStr += "\r\n\r\n";
				UtilAll.printBufInfo(writeStr);
				st = 100;
				LOG.info("确认货源时间:" + time.format(nowTime));
			}
		}

		PrintWriter out = response.getWriter();
		out.print(st);
		out.flush();
		out.close();
	}

	/**
	 * 全部确认货源
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/allQrNew")
	@ResponseBody
	protected void allQrNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo=request.getParameter("orderNo");
		String admid=request.getParameter("admid");
		String datas="";
		if(admid==null || "".equals(admid)){
			String sessionId = request.getSession().getId();
			String admuserw = Redis.hget(sessionId, "admuser");
			SerializeUtil su = new SerializeUtil();
			Admuser admuser = (Admuser) su.JsonToObj(admuserw, Admuser.class);
			admid=String.valueOf(admuser.getId());
		}
		if("999".equals(admid) || "1".equals(admid) || "83".equals(admid) || "84".equals(admid)){
			datas="";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer bf = new StringBuffer();
			Date date = new Date();
			List<Map<String,String>> mapList=iPurchaseService.allQrNew(orderNo,Integer.valueOf(admid));
			for(Map<String,String> map:mapList){
				String oldValue =String.valueOf(map.get("goods_p_price"));
				String newValue =String.valueOf(map.get("goods_p_url"));
				int purchaseCount = Integer.valueOf(String.valueOf(map.get("buycount")));
				if (purchaseCount == 0) {
					purchaseCount =Integer.valueOf(String.valueOf(map.get("yourorder")));
				}
				String url="";
				String car_urlMD5=String.valueOf(map.get("car_urlMD5"));
				String goods_pid=String.valueOf(map.get("goods_pid"));
				String goodsName=String.valueOf(map.get("goodsname"));
				if(StringUtil.isNotBlank(car_urlMD5) && car_urlMD5.substring(0, 1).equals("D")){
					url="https://detail.1688.com/offer/"+goods_pid+".html";
				}else if(StringUtil.isNotBlank(car_urlMD5) && car_urlMD5.substring(0, 1).equals("M")){
					url="https://www.amazon.com/"+(StringUtil.isBlank(goodsName)?"a":goodsName)+"/dp/"+goods_pid;
				}else{
					url=String.valueOf(map.get("car_url"));
				}
				iPurchaseService.PurchaseComfirmTwoHyqr(Integer.valueOf(String.valueOf(map.get("userid"))),String.valueOf(map.get("orderid")),
						Integer.valueOf(String.valueOf(map.get("id"))),Integer.valueOf(String.valueOf(map.get("goodsid"))),
						Integer.valueOf(String.valueOf(map.get("goodsdata_id"))),Integer.valueOf(admid),url,String.valueOf(map.get("car_img")),String.valueOf(map.get("goodsprice"))
						,goodsName,Integer.valueOf(String.valueOf(map.get("yourorder"))),oldValue,newValue,purchaseCount,"","");
				bf.append(String.valueOf(map.get("orderid"))).append(";")
						.append(String.valueOf(map.get("id"))).append(";")
						.append(sdf.format(date)).append("&");
				datas=bf.toString();
			}
		}
		PrintWriter out = response.getWriter();
		out.print(datas);
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/ShowRmark")
	@ResponseBody
	protected void ShowRmark(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String orderNo = request.getParameter("orderNo");
		String goodsdataid = request.getParameter("goodsdataid");
		String goodid = request.getParameter("goodid");
		String odid = request.getParameter("odid");
		response.setCharacterEncoding("UTF-8");
		OrderProductSource orderProductSource = iPurchaseService.ShowRmark(orderNo, Integer.parseInt(goodsdataid), Integer.parseInt(goodid),odid);
		JSONObject json  =JSONObject.fromObject(orderProductSource);
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.flush();
		out.close();
	}


	@RequestMapping(value = "/addGoodNoForRedis")
	@ResponseBody
	protected void addGoodNoForRedis(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		String parameters=request.getParameter("parameters");
		System.out.println("parameters为"+parameters);
		String admIds=request.getParameter("admId");
		int admid=Integer.valueOf(StringUtil.isBlank(admIds)?"0":admIds);
		System.out.println("当前用户id为:"+admIds);
		String str[]=new String[parameters.indexOf(",")>-1?parameters.split(",").length:1];
		if(parameters.indexOf(",")>-1){
			str=parameters.split(",");
		}else{
			str[0]=parameters;

		}
		String goodNo="";
		String orderNo="";
		if("0".equals(admIds)){
			//获取登录用户
			String admJson = Redis.hget(request.getSession().getId(), "admuser");
			com.cbt.pojo.Admuser user = (com.cbt.pojo.Admuser)SerializeUtil.JsonToObj(admJson, com.cbt.pojo.Admuser.class);
			admid=user.getId();
		}
		String admname=iPurchaseService.getUserName(admid);
		boolean flag=true;
		Properties properties = new Properties();
		try
		{
			String filePath= GetConfigureInfo.getAdgoodsPath();//获取项目路径
			InputStream inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
			inputStream.close(); //关闭流
			String result = properties.getProperty("result");
			if(result!=null && !result.equals("")){
				JSONArray json=JSONArray.fromObject(result);
				for(int i=0;i<json.size();i++){
					Map map=(Map) json.get(i);
					if(map.get(String.valueOf(admname))!=null){//如果该用户已经存在
						List list=(List) map.get(String.valueOf(admname));
						for(int j=0;j<str.length;j++){
							goodNo=str[j].split(":")[0];
							orderNo=str[j].split(":")[1];
							if(!list.contains(goodNo+":"+orderNo)){
								list.add(goodNo+":"+orderNo);
								json.remove(i);
								json.add(map);
								this.writeData("result", json.toString(), filePath);
							}
						}
						flag=false;
						break;
					}
				}
				if(flag){
					Map mapn = new HashMap();
					List list=new ArrayList();
					for(int j=0;j<str.length;j++){
						list.add(str[j].split(":")[0]+":"+str[j].split(":")[1]);
					}
					mapn.put(String.valueOf(admname), list);
					json.add(mapn);
					this.writeData("result", json.toString(), filePath);
				}
			}else{
				Map map = new HashMap();
				List list=new ArrayList();
				for(int j=0;j<str.length;j++){
					list.add(str[j].split(":")[0]+":"+str[j].split(":")[1]);
				}
				map.put(String.valueOf(admname), list);
				JSONArray json = JSONArray.fromObject(map);
				this.writeData("result", json.toString(), filePath);
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void writeData(String key, String value,String fileURL) {
		Properties prop = new Properties();
		InputStream fis = null;
		OutputStream fos = null;
		try {
			File file = new File(fileURL);
			if (!file.exists())
				file.deleteOnExit();
			file.createNewFile();
			fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();//一定要在修改值之前关闭fis
			fos = new FileOutputStream(file);
			prop.setProperty(key, value);
			prop.store(fos, "Update '" + key + "' value");
			fos.close();

		} catch (IOException e) {
			System.out.println("Visit " + fileURL + " for updating "
					+ value + " value error");
		}finally{
			try {
				fos.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 货源录入采购页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/AddResource")
	public void AddResource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Map<String,String> map=new HashMap<String, String>();
		map.put("type",request.getParameter("type"));
		map.put("admid",request.getParameter("admid"));
		map.put("userid",request.getParameter("userid"));
		map.put("goodsdata_id",request.getParameter("goodsdata_id"));
		map.put("goods_url",request.getParameter("goods_url"));
		map.put("googs_img",request.getParameter("googs_img"));
		map.put("goods_price",request.getParameter("goods_price"));
		map.put("goods_title",request.getParameter("goods_title"));
		map.put("googs_number",request.getParameter("googs_number"));
		map.put("orderNo",request.getParameter("orderNo"));
		String shop_id=request.getParameter("shop_id");
//		map.put("shop_id",StringUtil.isBlank(shop_id)?"":shop_id);
		map.put("goodid",request.getParameter("goodid"));
		map.put("cGoodstype",request.getParameter("cGoodstype"));
		map.put("od_id",request.getParameter("od_id"));
		map.put("reason",request.getParameter("reason"));
		map.put("state_flag",request.getParameter("state_flag"));
//		map.put("straight_address",request.getParameter("straight_address"));

		map.put("issuree",request.getParameter("issuree"));
		map.put("buycount",request.getParameter("buycount"));
		map.put("currency",request.getParameter("currency"));
//		String straight_address=request.getParameter("straight_address");
//		map.put("straight_address",StringUtil.isBlank(straight_address)?"":straight_address);
		double price=0;
		int state=0;
		String resource="";
		PrintWriter out = response.getWriter();
		try{
			if (!map.get("reason").contains("无货源")) {
				String pricee = request.getParameter("price");
				price = Double.parseDouble(pricee);
				resource = request.getParameter("resource");
				if (resource.contains("1688.com")) {
					resource = resource.substring(0, resource.indexOf(".html") + 5);
				}else if(resource.contains("taobao")){
					String x=resource.split("\\?")[0];
					String y[]=resource.split("\\?")[1].split("&");
					for(int i=0;i<y.length;i++){
						if(y[i].contains("id")){
							resource=x+"?"+y[i];
						}
					}
				}
			}
			map.put("resource",resource);
			map.put("price",String.valueOf(price));
			// 判断是否替换成功标识
			String aduser = iPurchaseService.getUserbyID(map.get("admid"));
			request.setAttribute("adusername", aduser);
			String sessionId = request.getSession().getId();
			request.setAttribute("admid", map.get("admid"));
			state=iPurchaseService.checkOrder(map.get("orderNo"), map.get("od_id"));
			if (state != 111 && state != 2) {
				iPurchaseService.AddRecource(map);
				Date date=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time=sdf.format(date);
				System.out.println(time+"采购【"+map.get("admid")+"】修改了订单【"+map.get("orderNo")+"】商品【"+map.get("od_id")+"】的货源链接为:"+resource);
			}
		}catch (Exception e){
			e.printStackTrace();
			out.print(0);
		}
		out.print(state);
		out.flush();
		out.close();
	}


	@RequestMapping(value = "/queryPurchaseInfo")
	public String queryPurchaseInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		long start = System.currentTimeMillis();
		System.out.println("queryPurchaseInfo:" + sdf1.format(new Date()) + " 按条件查询");
		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				return "main_login";
			}
			String orderid_no_array = request.getParameter("orderid_no_array");
			String idtypes_ = request.getParameter("idtypes_");
			String goodsid = request.getParameter("goodsid");
			String pagenum = request.getParameter("pagenum");
			String orderid = request.getParameter("orderid");
			String userid = request.getParameter("userid");
			String admid = request.getParameter("admid");
			String orderno = request.getParameter("orderno");
			String goodid = request.getParameter("goodid");
			String goodname = request.getParameter("goodname");
			String date = request.getParameter("date");
			String days = request.getParameter("days");
			String state = request.getParameter("state");
			String pagesize = request.getParameter("pagesize");
			String unpaid = request.getParameter("unpaid");
			String orderarrs = request.getParameter("orderarrs");
			String search_state = request.getParameter("search_state");
			Integer adminid = user.getId();
			Integer cgid = user.getId();
			orderid = StringUtils.isStrNull(orderid) ? null : orderid;
			admid = StringUtils.isStrNull(admid) ? "" : admid;
			userid = StringUtils.isStrNull(userid) ? "" : userid;
			orderno = StringUtils.isStrNull(orderno) ? "" : orderno;
			goodid = StringUtils.isStrNull(goodid) ? "" : goodid.trim();
			goodname = StringUtils.isStrNull(goodname) ? "" : goodname;
			date = StringUtils.isStrNull(date) ? "" : date;
			days = StringUtils.isStrNull(days) ? "" : days;
			state = StringUtils.isStrNull(state) ? "" : state;
			int unpay = 0;
			unpay = StringUtils.isStrNull(unpaid) ? 0 : Integer.parseInt(unpaid);
			System.out.println("========开始执行,purchaseServer.findPageByCondition:" + sdf1.format(new Date()) + "========");
			Page page=iPurchaseService.findPageByCondition(pagenum, orderid, admid, userid, orderno, goodid, date, days,
					state, unpay, Integer.valueOf(pagesize), orderid_no_array, goodsid, goodname, orderarrs,search_state);
			System.out.println("========结束执行,purchaseServer.findPageByCondition:" + sdf1.format(new Date()) + "========");
			if(page.getRecords().size()>0){
				PurchasesBean p=(PurchasesBean)page.getRecords().get(page.getRecords().size()-1);
				String tbInfo=p.getTborderInfo();
				request.setAttribute("tbInfo", StringUtil.isBlank(tbInfo)?"":tbInfo);
			}
			//获取订单出运信息
			String actual_freight = getActualFreight(orderno);
			request.setAttribute("actual_freight",actual_freight);
			//获取预估运费 start
			// 订单信息
			OrderBean orderInfo =iPurchaseService.getOrders(orderno);
			String allFreight = String.valueOf(iPurchaseService.getAllFreightByOrderid(orderno));
			double freightFee = orderInfo.getFreightFee();
			//iOrderinfoService.getFreightFee(allFreight, orderInfo);
			// 获取预估运费 end
			saveValueForRequest(request, user, idtypes_, userid, admid, orderno, goodid, goodname, date, days, state, pagesize, search_state, cgid, unpay, page, freightFee);
			// 获取所有采购人员信息
			List<com.cbt.pojo.Admuser> aublist = iOrderinfoService.getAllBuyer();
			request.setAttribute("aublist", net.sf.json.JSONArray.fromObject(aublist));
			System.out.println("========全部结束:" + sdf1.format(new Date()) + "========");
			List<OrderProductSource> odids = purchaseServer.getAllGoodsids(adminid);
			request.setAttribute("odids", JSONArray.fromObject(odids).toString());
			long end = System.currentTimeMillis();
			System.out.println("耗时:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("执行错误，原因：" + e.getMessage());
			LOG.error("执行错误，原因：" + e.getMessage());
		}
		return "PurchaseShow";
	}

	private void saveValueForRequest(HttpServletRequest request, Admuser user, String idtypes_, String userid, String admid, String orderno, String goodid, String goodname, String date, String days, String state, String pagesize, String search_state, Integer cgid, int unpay, Page page, double freightFee) {
		request.setAttribute("allFreight", freightFee);
		request.setAttribute("pagenum", page.getPagenum());
		request.setAttribute("totalnum", page.getTotalrecords());
		request.setAttribute("totalpage", page.getTotalpage());
		request.setAttribute("pblist", page.getRecords());
		request.setAttribute("pid_amount",page.getPid_amount());
		request.setAttribute("admid", admid == null || "".equals(admid) || "1".equals(admid) || "83".equals(admid) || "84".equals(admid) ? "999" : admid);
		request.setAttribute("cgid", cgid);
		request.setAttribute("userid", userid);
		request.setAttribute("idtypes_", idtypes_);
		request.setAttribute("goodname", goodname);
		request.setAttribute("search_state", search_state);
		request.setAttribute("orderno", orderno);
		request.setAttribute("goodid", "5201314".equals(goodid) || "5201315".equals(goodid) ? "" : goodid);
		request.setAttribute("date", date);
		request.setAttribute("days", days);
		request.setAttribute("state", state);
		request.setAttribute("admuser", user);
		request.setAttribute("unpay", unpay);
		request.setAttribute("page_size", Integer.parseInt(pagesize));
		request.setAttribute("hideTr", "<script>hideTr()</script>");
		request.setAttribute("keepValue", "<script>keepValue()</script>");
	}

	private String getActualFreight(String orderno) {
		List<ShippingBean> spb=iOrderinfoService.getShipPackmentInfo(orderno);
		String actual_freight="0";
		for(ShippingBean s:spb){
			if(s != null && StringUtil.isNotBlank(s.getActual_freight())){
				actual_freight+=Double.parseDouble(s.getActual_freight());
			}
		}
		return actual_freight;
	}

}
