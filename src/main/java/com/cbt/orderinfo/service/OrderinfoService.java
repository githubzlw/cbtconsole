package com.cbt.orderinfo.service;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.cbt.warehouse.pojo.OrderDetailsBeans;
import com.cbt.warehouse.pojo.SampleOrderBean;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.bean.Address;
import com.cbt.bean.AutoOrderBean;
import com.cbt.bean.CodeMaster;
import com.cbt.bean.Evaluate;
import com.cbt.bean.Forwarder;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderChange;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Orderinfo;
import com.cbt.bean.Payment;
import com.cbt.bean.ShippingBean;
import com.cbt.bean.TabTransitFreightinfoUniteNew;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.bean.TransitPricecost;
import com.cbt.bean.UserBean;
import com.cbt.common.StringUtils;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.orderinfo.dao.OrderinfoMapper;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.GoodsDistribution;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.report.service.TabTransitFreightinfoUniteNewExample;
import com.cbt.track.dao.TabTrackInfoMapping;
import com.cbt.util.DoubleUtil;
import com.cbt.util.Util;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.WarehouseMapper;
import com.cbt.warehouse.service.InventoryService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.SearchTaobaoInfo;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.pojo.SplitGoodsNumBean;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

@Service
public class OrderinfoService implements IOrderinfoService {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderinfoService.class);
	@Autowired
	private OrderinfoMapper orderinfoMapper;
	@Autowired
	private WarehouseMapper warehouseMapper;

	@Autowired
	private IPurchaseMapper pruchaseMapper;

	@Autowired
	private TabTrackInfoMapping tabTrackInfoMapping;
	@Autowired
	private InventoryService inventoryService;

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderinfoService.class);

	@Override
	public List<Map<String, String>> getOrders(int userID, int state,
	                                           Date startdate, Date enddate, String email, String orderno,
	                                           int startpage, int page, int admuserid, int buyid, int showUnpaid,String type,int status) {
		return orderinfoMapper.getOrders(userID, state, startdate, enddate, email, orderno, (startpage-1)*40, 40, admuserid, buyid, showUnpaid,type,status);
	}




	@Override
	public String getProblem(String orderid) {
		return orderinfoMapper.getProblem(orderid);
	}

	@Override
	public int insertOrderinfoCache(List<Map<String, String>> list) {
		return 0;
	}

	@Override
	public List<OrderBean> getFlushOrderFreightOrder() {
		return orderinfoMapper.getFlushOrderFreightOrder();
	}

	@Override
	public List<OrderBean> getAllOrderInfo() {
		return orderinfoMapper.getAllOrderInfo();
	}


	@Override
	public int deleteFlagByOrder(String orderNo) {
		return orderinfoMapper.deleteFlagByOrder(orderNo);
	}

	@Override
	public int insertFlagByOrderid(String orderNo) {
		return orderinfoMapper.insertFlagByOrderid(orderNo);
	}

	@Override
	public List<OrderBean> getFlushOrderFreightOrderCancel() {
		return orderinfoMapper.getFlushOrderFreightOrderCancel();
	}

	@Override
	public int getAllOrderinfoFreight() {
		return orderinfoMapper.getAllOrderinfoFreight();
	}

	@Override
	public List<OrderBean> getAllNoFreight() {
		return orderinfoMapper.getAllNoFreight();
	}

	@Override
	public String getOrderNo() {
		return orderinfoMapper.getOrderNo();
	}

	@Override
	public String getExchangeRate() {
		String rate=orderinfoMapper.getExchangeRate();
		if(StringUtil.isBlank(rate)){
			rate="6.3";
		}
		return rate;
	}

	@Override
	public String getCountryById(String country, String userid) {
		String coun=orderinfoMapper.getCountryById(country);
		if(StringUtil.isBlank(coun)){
			coun=orderinfoMapper.getOtherCountry(userid);
		}
		return coun;
	}

	@Override
	public int addOrderInfo(List<OrderBean> OrderBean, int address) {
		return orderinfoMapper.addOrderInfo(OrderBean,address);
	}

	@Override
	public int addOrderDetail(List<OrderDetailsBean> orderdetails) {
		return orderinfoMapper.addOrderDetail(orderdetails);
	}

	@Override
	public int addOrderAddress(Map<String, String> addressMap) {
		return orderinfoMapper.addOrderAddress(addressMap);
	}

	@Override
	public UserBean getUserFromIdForCheck(int userId) {
		return orderinfoMapper.getUserFromIdForCheck(userId);
	}

	@Override
	public int addPayment(PaymentBean payment) {
		int row=orderinfoMapper.getPayMentCount(payment);
		if(row>0){
			row=orderinfoMapper.updatePayMent(payment);
		}else{
			row=orderinfoMapper.insertPayMent(payment);
		}
		return row;
	}

	@Override
	public List<Address> getUserAddr(int userId) {
		List<Address> address = new ArrayList<Address>();
		List<Address> list=orderinfoMapper.getUserAddr(userId);
		Address defAddress = new Address();
		int flag=0;
		for(Address a:list){
			Address temp = new Address();
			if(a.getId() == a.getDefaultaddress()){
				defAddress.setRecipients(a.getRecipients());
				defAddress.setAddress(a.getAddress());
				defAddress.setCountry(a.getCountry());
				defAddress.setPhone_number(a.getPhone_number());
				defAddress.setZip_code(a.getZip_code());
				defAddress.setUserid(a.getUserid());
				defAddress.setId(a.getId());
				defAddress.setCountryname(a.getCountryname());
				defAddress.setAddress2(a.getAddress2());
				defAddress.setStatename(a.getStatename());
				defAddress.setDefaultaddress(a.getDefaultaddress());
				defAddress.setStreet(a.getStreet());
				flag=1;
				continue;
			}
			temp.setRecipients(a.getRecipients());
			temp.setAddress(a.getAddress());
			temp.setCountry(a.getCountry());
			temp.setPhone_number(a.getPhone_number());
			temp.setZip_code(a.getZip_code());
			temp.setUserid(a.getUserid());
			temp.setId(a.getId());
			temp.setCountryname(a.getCountryname());
			temp.setAddress2(a.getAddress2());
			temp.setStatename(a.getStatename());
			temp.setDefaultaddress(a.getDefaultaddress());
			temp.setStreet(a.getStreet());
			address.add(temp);
		}
		if(flag == 1){
			address.add(0, defAddress);
		}
		return address;
	}

	@Override
	public int addPaymentNote(String userid, String orderno, String dealMan,String upfile) {
		return orderinfoMapper.addPaymentNote(userid,orderno,dealMan,upfile);
	}

	@Override
	public int countPaymentInvoiceByorderuser(String userid, String orderno) {
		return orderinfoMapper.countPaymentInvoiceByorderuser(userid,orderno);
	}

	@Override
	public int updatePaymentNote(String userid, String orderno, String dealMan,String upfile) {
		return orderinfoMapper.updatePaymentNote(userid,orderno,dealMan,upfile);
	}

	@Override
	public List<OrderDetailsBean> getCatidDetails(String orderid) {
		return orderinfoMapper.getCatidDetails(orderid);
	}

	@Override
	public List<TabTransitFreightinfoUniteNew> selectByExample(TabTransitFreightinfoUniteNewExample example) {
		return orderinfoMapper.selectByExample(example);
	}

	@Override
	public List<Map<String,String>> allTrack(Map<String, String> map) {
		int adminid=orderinfoMapper.getAdmNameByShipno(map);
		List<Map<String,String>> list=orderinfoMapper.getOrderData(map.get("shipno"),adminid);
		return list;
	}

	@Override
	public List<OrderDetailsBean> getAllCancelDetails(Map<String, String> map) {
		return orderinfoMapper.getAllCancelDetails(map);
	}

	@Override
	public int updateGoodStatus(Map<String, String> map) {
		int row=0;
		String remark = "";
		int old_itemqty=0;
		String sql="";
		try{

			//查询入库备注
			Map<String,String> reMap=orderinfoMapper.getRemarkInspetion(map);
			if(reMap !=null){
				remark = reMap.get("warehouse_remark") + map.get("warehouseRemark");
				old_itemqty=Integer.valueOf(reMap.get("itemqty"));
			}else{
				remark = map.get("warehouseRemark");
			}
			old_itemqty= Integer.parseInt(map.get("count"))+old_itemqty;
			map.put("old_itemqty",String.valueOf(old_itemqty));
			map.put("remark",remark);
//			row=orderinfoMapper.updateIdrelationtable(map);
			//入库操作
			String username=map.get("userName");
			String positon="";
			if("Sherry".equals(username)){
				positon=orderinfoMapper.getPositionByBarcode(map.get("barcode"),"1");
			}else{
				positon=orderinfoMapper.getPositionByBarcode(map.get("barcode"),"2");
			}
			int idd=orderinfoMapper.getIdRelationtable(map);
			map.put("positon",positon);
			if("0".equals(map.get("repState"))){
				map.put("old_itemqty","0");
				orderinfoMapper.insertIdRelationtable(map);
			}else if(idd<=0){
				orderinfoMapper.insertIdRelationtable(map);
			}else{
				orderinfoMapper.updateIdRationtable(map);
			}
			if("1".equals(map.get("status"))){
				//点击到库时根据spec_id关联抓取订单的id
				String typeName=orderinfoMapper.getTypeNameByOdid(map);
				//查询入库对应的淘宝订单
				List<TaoBaoOrderInfo> tList=orderinfoMapper.getTaobaoInfoByOrderid(map);
				int tbId=0;
				if(StringUtil.isNotBlank(typeName) && typeName.contains("&gt;")){
					String [] types=typeName.split("&gt;");
					if(types.length >= 2){
						String sku1=types[0];
						String sku2=types[1];
						tbId=UtilAll.getTbId(tList,sku1,sku2);
					}
				}else if(StringUtil.isNotBlank(typeName)){
					tbId=UtilAll.getTbId(tList,typeName,"");
				}
				map.put("tbId",String.valueOf(tbId));

				// map.put("tbId",map.get("taobaoId"));
				//更新订单详情表状态为已经到仓库
				orderinfoMapper.updateState(map);
				if("0".equals(map.get("repState"))){
					//如果是补货则更新order_replenishment的状态为1已完成
					orderinfoMapper.updateOrderReState(map);
				}else{
					//更新采购表状态为已到库
					orderinfoMapper.updateBuyState(map);
				}
			}else{
				//更新采购表状态为问题货源
				orderinfoMapper.updateOrderSourceState(map);
			}
			//等于1为到库操作，其余为验货有疑问
			if(Integer.valueOf(map.get("status")) != 1){
				//Added <V1.0.1> Start： cjc 2019/3/27 17:28:32 Description : 验货有疑问 后续操作
				// TODO 1. 更新商品状态为采购中 2.更新入库记录为无效记录  是否需要启用待数据验证
				//orderinfoMapper.updateOrderDetailsState(map.get("odid"),map.get("orderid"));
				//End：
				return row;
			}
			sql="update storage_problem_order set flag=1 where shipno='"+map.get("shipno")+"'";
			orderinfoMapper.updateStorageProblemOrder(map);
			//查询是否是DP订单
			int isDropshipOrder=orderinfoMapper.queyIsDropshipOrder(map);
			//查询客户ID
			//int userId = orderinfoMapper.queryUserIdByOrderNo(map.get("orderid"));
			//获取未更新之前，订单状态和客户ID，比较前后状态是否一致，不一致说明订单状态已经修改
			Map<String,Object> orderinfoMap = pruchaseMapper.queryUserIdAndStateByOrderNo(map.get("orderid"));
			if (isDropshipOrder == 1) {
				orderinfoMapper.updateOrderDetails(map);
				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
				int counts=orderinfoMapper.getDtailsState(map);
				if(counts == 0){
					orderinfoMapper.updateDropshiporder(map);
					SendMQ.sendMsg(new RunSqlModel("update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"+map.get("orderid")+"' " +
							"and id='"+map.get("odid")+"')"));
				}
				//判断主单下所有的子单是否到库
				counts=orderinfoMapper.getAllChildOrderState(map);
				if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//判断订单状态是否一致
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//发送消息给客户
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}
			}else{
				// 非dropshi订单
				orderinfoMapper.updateOrderDetails(map);
				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
				//判断订单是否全部到库
				int counts=orderinfoMapper.getDetailsState(map);
				if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//判断订单状态是否一致
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//发送消息给客户
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}
			}
			orderinfoMap.clear();
			//记录商品入库
			LOG.info("--------------------开始记录商品入库--------------------");
			Map<String,String> inMap=orderinfoMapper.queryData(map);
			if(inMap != null){
				map.put("goods_pid",inMap.get("goods_pid"));
				orderinfoMapper.insertGoodsInventory(map);
			}
			LOG.info("--------------------结束记录商品入库--------------------");

		}catch (Exception e){
			e.printStackTrace();
		}
		return 1;
	}


	@Override
	public int insertScanLog(String shipno, String admName) {
		return orderinfoMapper.insertScanLog(shipno,admName);
	}

	@Override
	public List<Tb1688OrderHistory> getGoodsData(String shipno) {
		List<Tb1688OrderHistory> list=orderinfoMapper.getGoodsData(shipno);
		for(Tb1688OrderHistory t:list){
			t.setImgurl(t.getImgurl().replace(".80x80","").replace(".60x60",""));
			t.setSkuID(StringUtil.isBlank(t.getSkuID()) ? t.getItemid() : t.getSkuID());
			t.setSpecId(StringUtil.isBlank(t.getSpecId()) ? t.getItemid() : t.getSpecId());
		}
		return list;
	}

	private String changetypeName(String _strType) {
		if (_strType == null || _strType.isEmpty()) {
			return "";
		}
		String strType = "";
		for (String strTp : _strType.split(",")) {
			strType = strType + " " + strTp.split("@")[0];
		}
		return strType.replace("undefined", "");
	}

	@Override
	public List<SearchResultInfo> getOrder(String shipno, String checked) {
		List<SearchResultInfo> info = new ArrayList<SearchResultInfo>();
		List<SearchTaobaoInfo> taobaoinfoList = new ArrayList<SearchTaobaoInfo>();
		int adminid = 520;
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		try{
			String admin=orderinfoMapper.getAdminid(shipno);
			if(StringUtil.isNotBlank(admin)){
				adminid=Integer.valueOf(admin);
			}
			if ("1".equals(checked)) {
				resultList=orderinfoMapper.getOrderDataOne(shipno);
			}else{
				resultList=orderinfoMapper.getOrderData(shipno,adminid);
			}
			Set set=new HashSet();
			for(Map<String,String> map:resultList){
				SearchResultInfo searchresultinfo = new SearchResultInfo();
				String resultTaobaoItemId = null;
				resultTaobaoItemId = map.get("tb_1688_itemid");
				String car_type =map.get("car_type");
				String types1 = "";
				if (car_type.indexOf("<") > -1 && car_type.indexOf(">") > -1) {
					for (int j = 1; j < 4 && car_type.indexOf("<") > -1 && car_type.indexOf(">") > -1; j++) {
						types1 = car_type.substring(car_type.indexOf("<"),car_type.indexOf(">") + 1);
						car_type = car_type.replace(types1, "");
					}
				}
				//订单收获地址
				
//				String address =String.valueOf(map.get("address"));
				searchresultinfo.setStrcar_type(changetypeName(car_type));
				searchresultinfo.setIsExitPhone(String.valueOf(map.get("isExitPhone")));
				searchresultinfo.setTaobao_itemid(resultTaobaoItemId);
				//获取商品的店铺名称
				String shop_id="0000";
				String goods_pid=String.valueOf(map.get("goods_pid"));
				
				//查询产品品牌授权情况
				Map<String, Object> goodsBrandAuthorization = orderinfoMapper.getGoodsBrandAuthorization(goods_pid);
				//0-未授权  1-部分授权 2-全部授权   -1 -侵权
				String authorized_flag = "0";
				String brand_name = "";
				String brandid = "0000";
				if(goodsBrandAuthorization != null) {
					Object authorizeState = goodsBrandAuthorization.get("authorize_state");
					authorized_flag = authorizeState == null ? "0" : String.valueOf(authorizeState);
					brand_name = (String)goodsBrandAuthorization.get("brand_name");
					brand_name = brand_name == null ? "" : brand_name;
					shop_id = goodsBrandAuthorization.get("shop_id") != null ? (String)goodsBrandAuthorization.get("shop_id") : shop_id;
					brandid = goodsBrandAuthorization.get("brand_id") != null ? goodsBrandAuthorization.get("brand_id").toString() : brandid;
				}
				
				/*if(goods_pid.equals(String.valueOf(map.get("tb_1688_itemid")))){
					//采购货源和推荐货源一致
					shop_id=orderinfoMapper.getShopId(goods_pid);
					//是否授权
					String flag="1";
					if(map.get("authorizedFlag") == null || StringUtil.isBlank(map.get("authorizedFlag")) || "0".equals(String.valueOf(map.get("authorizedFlag"))) || "2".equals(String.valueOf(map.get("authorizedFlag")))){
						flag="0";
					}
					if("0".equals(flag) && ((address.contains("美国") || "36".equals(address) || "usa".equals(address.toLowerCase()))
							|| (address.contains("英国") || "35".equals(address) || "uk".equals(address.toLowerCase()))
							|| (address.contains("加拿大") || "6".equals(address) || "CANADA".equals(address.toLowerCase())))){
						check="请核查该商品是否侵权";
					}
				}*/
				//采购是否该商品授权  1已授权
				/*String authorized_flag=String.valueOf(map.get("aFlag"));
				if("0".equals(authorized_flag)){
					check="采购已对该商品授权";
				}else if("2".equals(authorized_flag)){
					//查询上一次该商品发货的订单信息
					check= warehouseMapper.getBatckInfo(goods_pid);
				}
				check=StringUtil.isBlank(check)?"-":check;*/
				String check = "未授权品牌，通知采购核查";
				String authorizeRemark = "未授权";
				if("2".equals(authorized_flag)){
					check = "采购已对该品牌授权";
					authorizeRemark = "已授权";
				}else if("1".equals(authorized_flag)){
					check = "采购已对该品牌部分授权";
					authorizeRemark = "部分授权";
				}else if("-1".equals(authorized_flag)){
					check = "该品牌侵权";
					authorizeRemark = "侵权";
				}
				searchresultinfo.setBrandid(brandid);
				searchresultinfo.setAuthorizeRemark(authorizeRemark);
				searchresultinfo.setAuthorizeState(authorized_flag);
				searchresultinfo.setAuthorizedFlag(check);
				searchresultinfo.setBrandName(brand_name);
				searchresultinfo.setShop_id(shop_id);
				searchresultinfo.setOdid(String.valueOf(map.get("odid")));
                // 2018/11/06 11:39 ly 实秤重量 是否已同步到产品库
                SearchResultInfo weightAndSyn = warehouseMapper.getGoodsWeight(map.get("goods_pid"), Integer.valueOf(String.valueOf(map.get("odid"))));
                if (null != weightAndSyn){
                    searchresultinfo.setWeight(weightAndSyn.getWeight());
                    searchresultinfo.setSyn(weightAndSyn.getSyn());
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(weightAndSyn.getVolume_weight())){
                    	searchresultinfo.setVolume_weight(weightAndSyn.getVolume_weight());
					}else{
                    	searchresultinfo.setVolume_weight("");
					}
                }
				//获取验货商品的最大类别ID 王宏杰
				String catid="";
				if("1".equals(checked)){
					String goodscatid=map.get("goodscatid");
					catid=orderinfoMapper.getCatid(goodscatid);
				}
				searchresultinfo.setCatid(catid);
				String orderid = String.valueOf(map.get("orderid"));
				searchresultinfo.setOrderid(orderid);
				int goodsid=Integer.valueOf(String.valueOf(map.get("goodsid")));
				set.add(orderid);
				searchresultinfo.setGoodsid(goodsid);
				//保存值到bean中
				saveValueForBean(map, searchresultinfo);
//				PaymentDaoImp paymentDao = new PaymentDao();
				String fileByOrderid = orderinfoMapper.getFileByOrderid(orderid);
				//拼接备注信息
				StringBuffer sbf = getStringForRemark(map, searchresultinfo, orderid, fileByOrderid);
				searchresultinfo.setRemark(sbf.toString());
				sbf.setLength(0);
				searchresultinfo.setImgList(taobaoinfoList);
				List<Object[]> orderremark = new ArrayList<Object[]>();
				List<Map<String,String>> remarkList=orderinfoMapper.getOrderRemark(orderid);
				for(Map<String,String> reMap:remarkList){
					Object[] objects = {reMap.get("orderid"), reMap.get("orderremark"), reMap.get("remarkuserid"),reMap.get("createtime")};
					orderremark.add(objects);
				}
				searchresultinfo.setOdid(String.valueOf(map.get("odid")));
				searchresultinfo.setOrderremark(orderremark);
				//TODO zlc bug处理期，后期需要更改
				searchresultinfo.setSource1688_img(map.get("source1688_img"));
				searchresultinfo.setGoods_img_url(map.get("goods_img_url"));
				searchresultinfo.setDp_num(Integer.valueOf(map.get("dp_num")));
				searchresultinfo.setDp_total(Integer.valueOf(map.get("dp_total")));
				searchresultinfo.setDp_city(map.get("dp_city"));
				searchresultinfo.setDp_country(map.get("dp_country"));
				searchresultinfo.setDp_province(map.get("dp_province"));
				//采购备注-2019.07.04-sj
				searchresultinfo.setContext(map.get("context")!=null?map.get("context") : "");
				if(StringUtil.isBlank(car_type) || "0".equals(car_type)) {
					searchresultinfo.setSpecId(map.get("tb_1688_itemid"));
					searchresultinfo.setSkuID(map.get("tb_1688_itemid"));
				}else {
					searchresultinfo.setSpecId(map.get("specid")!=null?map.get("specid") : "");
					searchresultinfo.setSkuID(map.get("skuid")!=null?map.get("skuid") : "");
				}
				if(org.apache.commons.lang3.StringUtils.isNotBlank(map.get("taobao_id"))){
					searchresultinfo.setTaobaoId(Integer.parseInt(map.get("taobao_id")));
				}

				info.add(searchresultinfo);
			}
			//一个1688包裹对应的采购订单数量
			if(info.size()>0){
				info.get(0).setOrder_num(set.size());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 拼接备注信息
	 * @param map
	 * @param searchresultinfo
	 * @param orderid
	 * @param fileByOrderid
	 * @return
	 */
	private StringBuffer getStringForRemark(Map<String, String> map, SearchResultInfo searchresultinfo, String orderid, String fileByOrderid) {
		if (fileByOrderid == null || fileByOrderid.length() < 10) {
			searchresultinfo.setInvoice("");
		} else {
			searchresultinfo.setInvoice("<button><a style='color:red' target='_blank' href='/cbtconsole/autoorder/show?orderid="+orderid+"'>查看invoice</a></button>");
		}
		searchresultinfo.setOdRemark(String.valueOf(map.get("odremark")) == null || "".equals(String.valueOf(map.get("odremark"))) ? "无": String.valueOf(map.get("odremark")));
		String mk = "";
		StringBuffer sbf = new StringBuffer();
		if (map.get("bargainRemark") != null && !(map.get("bargainRemark")).equals("")) {
			mk = "砍价情况:" + map.get("bargainRemark") + " ;";
			sbf.append(mk);
		}
		if (map.get("deliveryRemark") != null && !(map.get("deliveryRemark")).equals("")) {
			mk = "交期偏长:" + map.get("deliveryRemark") + " ;";
			sbf.append(mk);
		}
		if (map.get("colorReplaceRemark") != null && !(map.get("colorReplaceRemark")).equals("")) {
			mk = "颜色替换:" + map.get("colorReplaceRemark")
					+ " ;";
			sbf.append(mk);
		}
		if (map.get("sizeReplaceRemark") != null && !(map.get("sizeReplaceRemark")).equals("")) {
			mk = "尺寸替换:" + map.get("sizeReplaceRemark")+ " ;";
			sbf.append(mk);
		}
		if (map.get("orderNumRemarks") != null && !(map.get("orderNumRemarks")).equals("")) {
			mk = "订量问题:" + map.get("orderNumRemarks") + " ;";
			sbf.append(mk);
		}
		if (map.get("questionsRemarks") != null && !(map.get("questionsRemarks")).equals("")) {
			mk = "疑问备注:" + map.get("questionsRemarks") + " ;";
			sbf.append(mk);
		}
		if (map.get("unquestionsRemarks") != null && !(map.get("unquestionsRemarks")).equals("")) {
			mk = "无疑问备注:" + map.get("unquestionsRemarks")+ " ;";
			sbf.append(mk);
		}
		if (map.get("againRemarks") != null && !(map.get("againRemarks")).equals("")) {
			mk = "再次备注:" + map.get("againRemarks") + " ;";
			sbf.append(mk);
		}
		if(Integer.valueOf(String.valueOf(map.get("isDropshipOrder")))==3){
			sbf.append("该订单为采样订单");
		}
		return sbf;
	}

	/**
	 * 验货查询将获取的值保存在bean中
	 * @param map
	 * @param searchresultinfo
	 */
	private void saveValueForBean(Map<String, String> map, SearchResultInfo searchresultinfo) {
		searchresultinfo.setTbOrderIdPositions(map.get("orderPOSITION"));
		searchresultinfo.setGoodstatus(map.get("goodstatus"));
		searchresultinfo.setUserid(Integer.valueOf(String.valueOf(map.get("userid"))));
		searchresultinfo.setOrdercount(Integer.valueOf(String.valueOf(map.get("ordercount"))));
		searchresultinfo.setOrderbuycount(Integer.valueOf(String.valueOf(map.get("buycnt"))));
		searchresultinfo.setGoods_pid(map.get("goods_pid"));
		searchresultinfo.setChecked(Integer.valueOf(String.valueOf(map.get("checked"))));
		searchresultinfo.setSeilUnit(map.get("seilUnit")==null || "".equals(map.get("seilUnit"))?"无":map.get("seilUnit"));
		searchresultinfo.setGcUnit(map.get("gcUnit")==null || "".equals(map.get("gcUnit"))?"无":map.get("gcUnit"));
		searchresultinfo.setIsDropshipOrder(String.valueOf(map.get("isDropshipOrder")));
		searchresultinfo.setTaobao_orderid(String.valueOf(map.get("taobaoOrderid")));
		searchresultinfo.setPosition(String.valueOf(map.get("goodsPOSITION")));
		searchresultinfo.setOrderRemark(String.valueOf(map.get("orderRemark")));
		searchresultinfo.setGoodsdataid(Integer.valueOf(String.valueOf(map.get("goodsdataid"))));
		searchresultinfo.setGoods_name(String.valueOf(map.get("goods_name")));
		searchresultinfo.setGoods_url(String.valueOf(map.get("goods_url")));
		searchresultinfo.setGoods_p_url(String.valueOf(map.get("goods_p_url")));
		searchresultinfo.setGoods_img_url(String.valueOf(map.get("od_goods_img_url")).replace("80x80","400x400").replace("60x60","400x400"));
		searchresultinfo.setImg(com.cbt.website.util.Utility.ImgMatch(map.get("img").replace("60x60","400x400").replace("80x80","400x400")));
		searchresultinfo.setCurrency(String.valueOf(map.get("currency")));
		searchresultinfo.setGoods_price(Double.parseDouble(String.valueOf(map.get("odGoodsPrice"))));
		searchresultinfo.setGoods_p_price(Double.parseDouble(String.valueOf(map.get("goods_p_price"))));
		searchresultinfo.setPurchase_state(Integer.valueOf(String.valueOf(map.get("purchase_state"))));
		searchresultinfo.setUsecount(Integer.valueOf(String.valueOf(map.get("usecount"))));
		searchresultinfo.setBuycount(Integer.valueOf(String.valueOf(map.get("buycount"))));
	}

	@Override
	public int changeBuyer(Map<String, String> map) {
		//判断该商品是否分配过采购
		int row =orderinfoMapper.queryGoodsDis(map);
		if(row>0){
			row=orderinfoMapper.updateGoodsDis(map);
		}
		return row;
	}

	@Override
	public int updatecanceltatus(Map<String, String> map) {
		int row=0;
		try{

			// 直接删除 标识记为已经删除
			row=orderinfoMapper.updateIdrelationtableFlag(map);
			int counts=orderinfoMapper.getInCount(map);
			if(counts == 0){
				row=orderinfoMapper.udpateStorage(map);
			}
			row=orderinfoMapper.updateDetailsShipno(map);
			row=orderinfoMapper.updateOrderState(map);
			if ("0".equals(map.get("repState"))) {
				row=orderinfoMapper.updateOrderRe(map);
			}else{
				row=orderinfoMapper.updateOrderSource(map);
			}
			// 更新线上状态
			SendMQ.sendMsg(new RunSqlModel("UPDATE order_details t SET t.state = 0 WHERE t.orderid = '"+map.get("orderid")+"' AND t.id = '"+map.get("odid")+"'"));
			SendMQ.sendMsg(new RunSqlModel("UPDATE orderinfo t SET t.state = 1 WHERE t.order_no = '"+map.get("orderid")+"'"));

		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int orderReturn(Map<String,String> map) {
		return orderinfoMapper.orderReturn(map);
	}

	@Override
	public int addUser(Map<String, String> map) {
		map.put("admName",orderinfoMapper.getAdmName(map));
		int row=0;
		try{

			row=orderinfoMapper.queryAdmin(map);
			if(row>0){
                //更新商业询盘中分配的数据
			    orderinfoMapper.updateBusiess(map);
                //更新销售人
                row=orderinfoMapper.updateAdminUser(map);
				if(row>0){
					SendMQ.sendMsg(new RunSqlModel("update admin_r_user set adminid="+map.get("adminid")+",admName='"+map.get("admName")+"' where userid="+map.get("userid")+""));
				}
			}else{
				//新增销售记录
				row=orderinfoMapper.insertAdminUser(map);
				if(row>0){
					SendMQ.sendMsg(new RunSqlModel("insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) " +
							"values('"+map.get("userid")+"','"+map.get("email")+"','"+map.get("email")+"','"+map.get("adminid")+"',now(),'"+map.get("admName")+"')"));
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int checkOrderState(String orderid) {
		return orderinfoMapper.checkOrderState(orderid);
	}

	@Override
	public int updatecancelChecktatus(Map<String, String> map) {
		int row=0;
		int old_itemqty=0;
		double weights=0.00;
		try{
			Map<String,String> inMap=orderinfoMapper.getInspetionMap(map);
//			cancelCount=Integer.valueOf(map.get("cance_inventory_count"));
			if(inMap != null){
				old_itemqty=Integer.valueOf(String.valueOf(inMap.get("itemqty")));
				weights=Double.parseDouble(String.valueOf(inMap.get("weight")))-Double.parseDouble(StringUtil.isBlank(String.valueOf(map.get("weight")))?"0.00":String.valueOf(map.get("weight")));
				int count_=old_itemqty-Integer.valueOf(String.valueOf(map.get("count")));
				if(count_<0){
					count_=0;
				}
				map.put("weight",String.valueOf(weights));
				map.put("count",String.valueOf(count_));
				orderinfoMapper.updateRelationTable(map);
			}
			row=orderinfoMapper.updateDetails(map);
			
			//如果该商品验货是有录入库存则做想应的减少
			inventoryService.cancelInventory(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int updateTbstatus(Map<String, String> map) {
		int row=0;
		String remark = "";
		try{

			Double weights=Double.parseDouble(map.get("weight"));
			row=orderinfoMapper.updateChecked(map);
			row=orderinfoMapper.updateSourceState(map);
			Map<String,String> inMap=orderinfoMapper.getInspetionMap(map);
			if(inMap != null){
				remark = inMap.get("warehouse_remark") + map.get("warehouseRemark");
				weights+=Double.parseDouble(String.valueOf(inMap.get("weight")));
			}else{
				remark =String.valueOf(map.get("warehouseRemark"));
			}
			map.put("warehouseRemark",remark);
			map.put("weight",String.valueOf(weights));
			row=orderinfoMapper.updateRelationTable(map);
			String orderid=String.valueOf(map.get("orderid"));
			String goodsid=String.valueOf(map.get("goodid"));
			//判断是否为dp订单
			int isDropshipOrder=orderinfoMapper.queyIsDropshipOrder(map);
			if (isDropshipOrder == 1) {
				orderinfoMapper.updateOrderDetails(map);
				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+orderid+"' and id='"+map.get("odid")+"'"));
				int counts=orderinfoMapper.getDtailsState(map);
				if(counts == 0){
					orderinfoMapper.updateDropshiporder(map);
					SendMQ.sendMsg(new RunSqlModel("update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"+orderid+"' " +
							"and id='"+map.get("odid")+"')"));
				}
				//判断主单下所有的子单是否到库
				counts=orderinfoMapper.getAllChildOrderState(map);
				if(counts == 0){
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+orderid+"'"));
				}
			}else{
				// 非dropshi订单
				orderinfoMapper.updateOrderDetails(map);
				SendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+orderid+"' and id='"+map.get("odid")+"'"));
				//判断订单是否全部到库
				int counts=orderinfoMapper.getDetailsState(map);
				if(counts == 0){
					System.err.println("orderNo:" + orderid + ",验货无误");
					orderinfoMapper.updateOrderInfoState(map);
					SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+orderid+"'"));
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public List<com.cbt.pojo.Admuser> getAllBuyer() {
		return orderinfoMapper.getAllBuyer();
	}

	@Override
	public List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo) {
		Map<String, ArrayList<Object[]>> changInfo = new HashMap<String, ArrayList<Object[]>>();
		List<OrderChange> ocList=orderinfoMapper.getOrderChange(orderNo);
		List<OrderDetailsBean> list=orderinfoMapper.getChildrenOrdersDetails(orderNo);
		for(OrderDetailsBean odb:list){
			String weitht1688 = Utility.getStringIsNull(odb.getCbrWeight()) ?odb.getCbrWeight() : "0";
			String price1688 = Utility.getStringIsNull(odb.getCbrPrice()) ? odb.getCbrPrice() : "0";
			String es_price=price1688;
			if(odb.getState() ==1 || odb.getState() == 0){
				es_price=StringUtil.getEsPriceNew(es_price,odb.getYourorder());
			}else{
				es_price="0.00";
			}
			odb.setEs_price(Double.valueOf(es_price)*odb.getYourorder());
			odb.setWeight1688(weitht1688);
			odb.setPrice1688(price1688);
			odb.setOrderid(orderNo);
			odb.setId(odb.getOid());
			odb.setCheckproduct_fee(odb.getCheckprice_fee());
			odb.setFreight(odb.getGoodsfreight());
			odb.setGoods_img(odb.getCar_img());
			odb.setGoods_freight(odb.getGoodsfreight());
			odb.setGoods_type(odb.getCar_type());
			odb.setNewsourceurl(odb.getNewValue());
			// 存储采购备注
			// 拆分采购备注
			String oremark = "";
			oremark = getStringOdRemark(odb, oremark);
			odb.setOldUrl(oremark);
			// 临时替代产品金额
			odb.setSourc_price(odb.getOldValue());
			odb.setChange_price("");
			odb.setChange_delivery("");
			odb.setIscancel(0);
			odb.setImg_type(odb.getGoods_typeimg());
			String car_urlMD5=odb.getCar_urlMD5();
			String goods_pid=odb.getGoods_pid();
			if (StringUtil.isNotBlank(car_urlMD5) && car_urlMD5.startsWith("D")) {
				odb.setMatch_url("https://detail.1688.com/offer/" + (goods_pid) + ".html");
			}else if(StringUtil.isNotBlank(car_urlMD5) && car_urlMD5.startsWith("A")){
				odb.setMatch_url("http://www.aliexpress.com/item/a/" + goods_pid + ".html");
			}
			// 优惠的金额
			double sprice = Double.parseDouble(odb.getSprice());
			if (sprice != 0 && odb.getState() != 2) {
				double goodssprice_d = Double.parseDouble(odb.getGoodsprice());
				odb.setPreferential_price(new BigDecimal((sprice - goodssprice_d) * odb.getYourorder()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			getRoTypeAndState(changInfo, odb);
		}
		return list;
	}

	private void getRoTypeAndState(Map<String, ArrayList<Object[]>> changInfo, OrderDetailsBean odb) {
		if (changInfo.get("order" + odb.getId()) != null) {
			List<Object[]> changeInfoList = changInfo.get("order" + odb.getId());
			for (int i = 0; i < changeInfoList.size(); i++) {
				int ropType = (Integer) changeInfoList.get(i)[0];
				int del_state = (Integer) changeInfoList.get(i)[1];
				String values = (String) changeInfoList.get(i)[2];
				if (ropType == 1) {
					odb.setChange_price(values);
				} else if (ropType == 2) {
					odb.setChange_delivery(values);
				} else if (ropType == 3) {
					odb.setChange_number(values);
				} else if (ropType == 4) {
					odb.setIscancel(1);
				} else if (ropType == 5) {
					if (odb.getChange_communication() == null) {
						List<String> communications = new ArrayList<String>();
						communications.add(values);
						odb.setChange_communication(communications);
					} else {
						odb.getChange_communication().add(values);
					}
					odb.setRopType(ropType);
					odb.setDel_state(del_state);
				}
			}
		}
	}

	private String getStringOdRemark(OrderDetailsBean odb, String oremark) {
		if (StrUtils.isNotNullEmpty(odb.getBargainRemark())) {
			oremark += "砍价情况:" + odb.getBargainRemark() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getDeliveryRemark())) {
			oremark += "交期偏长:" + odb.getDeliveryRemark() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getColorReplaceRemark())) {
			oremark += "颜色替换:" + odb.getColorReplaceRemark() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getSizeReplaceRemark())) {
			oremark += "尺寸替换:" + odb.getSizeReplaceRemark() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getOrderNumRemarks())) {
			oremark += "订量问题:" +odb.getOrderNumRemarks() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getQuestionsRemarks())) {
			oremark += "有疑问备注:" + odb.getQuestionsRemarks() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getUnquestionsRemarks())) {
			oremark += "无疑问备注:" +odb.getUnquestionsRemarks() + ",";
		}
		if (StrUtils.isNotNullEmpty(odb.getAgainRemarks())) {
			oremark += "再次备注:" + odb.getAgainRemarks() + ",";
		}
		return oremark;
	}

	@Override
	public String getAllFreightByOrderid(String orderNo) {
		return orderinfoMapper.getAllFreightByOrderid(orderNo);
	}

	@Override
	public OrderBean getChildrenOrders(String orderNo) {
		OrderBean ob=orderinfoMapper.getChildrenOrders(orderNo);
		if(ob != null){
			String dzconfirmtime = ob.getDzConfirmtime();
			ob.setDzConfirmtime(Utility.getStringIsNull(dzconfirmtime)? dzconfirmtime.substring(0, dzconfirmtime.indexOf(" ")) : "");
			ob.setOrderNo(orderNo);
			String pay_price_tow = ob.getPay_price_tow();
			ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ? pay_price_tow : "0");
			Address address = new Address();
			address.setId(Integer.valueOf(ob.getCountry()));
			address.setAddress(ob.getAddresss());
			address.setCountry(ob.getCountryName());
			address.setPhone_number(ob.getPhonenumber());
			address.setZip_code(ob.getZipcode());
			address.setStatename(ob.getStatename());
			address.setAddress2(ob.getAddress2());
			address.setRecipients(ob.getRecipients());
			ob.setAddress(address);
			ob.setOrderNumber(ob.getOrdernum() == 1);
			ob.setPay_price(Double.parseDouble(Utility.formatPrice(String.valueOf(ob.getPay_price())).replaceAll(",", "")));
			ob.setForeign_freight(Utility.getStringIsNull(ob.getForeign_freight()) ? ob.getForeign_freight() : "0");
			// 提醒，0-客户投诉，1-入库问题，2，-出库问题
			ob.setReminded(new int[]{ob.getComplain(), ob.getPurchase(), ob.getDeliver()});
		}
		return ob;
	}

	@Override
	public List<Map<String, String>> getOrderManagementQuery(Map<String,String> paramMap) {
		long start=System.currentTimeMillis();
		UserDao udao = new UserDaoImpl();
		List<Map<String, String>> list=orderinfoMapper.getOrderManagementQuery(paramMap);
		long start2=System.currentTimeMillis();
		for(Map<String, String> map:list){
			String orderRemark = map.get("orderRemark");
			String paytype=map.get("paytypes");
			String tp="支付类型错误";
			if(org.apache.commons.lang3.StringUtils.isNoneBlank(orderRemark)){
				if(orderRemark.indexOf("@@@@@justSave") > -1){
					tp = "USA订单等待处理!";
				}
			}
			int count=this.pruchaseMapper.FindCountByEmial(map.get("email"));
			map.put("emailcount",String.valueOf(count));
			if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
				StringBuilder types=new StringBuilder();
				String [] t=paytype.split(",");
				for(int i=0;i<t.length;i++){
					types.append(StringUtil.getPayType(t[i]));
					if(i != t.length-1){
						types.append(",");
					}
				}
				tp=types.toString();
			}else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
				tp=StringUtil.getPayType(paytype);
			}
			map.put("paytypes",tp);
			String allFreight =String.valueOf(map.get("allFreight"));
			OrderBean orderInfo=new OrderBean();
			orderInfo.setMode_transport(String.valueOf(map.get("mode_transport")));
			orderInfo.setOrderNo(String.valueOf(map.get("order_no")));
			String addressFlag="0";
			String odCode=map.get("odCode");
			String ipnaddress=map.get("ipnaddress");
            Map<String, String> ipnaddress1 = udao.getIpnaddress(orderInfo.getOrderNo());
            ipnaddress = ipnaddress1.get("address_country_code");
            if(StringUtils.isEmpty(ipnaddress)){
                ipnaddress = ipnaddress1.get("ipnaddress");
			}
			String zCountry=map.get("zCountry");
			String ordertype=String.valueOf(map.get("ordertype"));
			if((StringUtil.isBlank(odCode) || StringUtil.isBlank(ipnaddress) || !ipnaddress.equals(odCode)) && tp.indexOf("paypal")>-1){
				logger.warn("简称国家不一致： odCode={},ipnaddress={}", odCode,ipnaddress);
				Map<String,String> aMap = ipnaddress1;
				String addressCountry=aMap.get("address_country");
				String address_country_code=aMap.get("address_country_code");
				//Rewriter
				//Rewrite <V1.0.1> Start：cjc 2019/3/4 15:02:58 TODO 不是到为什么USA 的ipn 回调简称US非要改成 USA 去掉
				/*if("US".equals(address_country_code)){
					address_country_code="USA";
				}*/
				//End：

				if("3".equals(ordertype)){
					//B2B订单
					addressFlag="3";
				}else if(StringUtil.isBlank(addressCountry)){
					//没有支付信息，
					addressFlag="2";
				//Rewriter
				//Rewrite <V1.0.1> Start：cjc 2019/3/4 14:56:41
				//}else if(StringUtil.isNotBlank(zCountry) && StringUtil.isNotBlank(addressCountry) && !zCountry.equals(addressCountry) && !odCode.equals(address_country_code)){
				//End：
				//Added <V1.0.1> Start： cjc 2019/3/4 14:57:03 TODO jack 建议说只用对比简称就行
				}else if(StringUtil.isNotBlank(odCode) && StringUtil.isNotBlank(address_country_code) && !odCode.equals(address_country_code)){
				//End：
					//两边都有编码但不一致
					addressFlag="1";
				}else if(StringUtil.isBlank(zCountry) || StringUtil.isBlank(addressCountry)){
					logger.warn("二次校验，全称国家不一致： zCountry={},addressCountry={}", zCountry,addressCountry);
					addressFlag="1";
				}
			}
			map.put("addressFlag",addressFlag);
			double freightFee = orderInfo.getFreightFee();
			map.put("allFreight",String.valueOf(freightFee));
			String exchange_rate=String.valueOf(map.get("exchange_rate"));
			if(StringUtil.isBlank(exchange_rate) || Double.parseDouble(exchange_rate)<=0){
				exchange_rate="6.3";
			}
			map.put("estimatefreight",String.valueOf(Double.parseDouble(String.valueOf(StringUtil.isBlank(map.get("estimatefreight"))?"0":map.get("estimatefreight")))*Double.parseDouble(exchange_rate)));
		}
		  long end = System.currentTimeMillis();
		 logger.info("订单管理查询总时间:"+(end-start));
		 System.out.println("订单管理查询总时间:"+(end-start)+"list.size()"+list.size());
        System.out.println("查存储过程时间 = " + (start2 - start)+" 解析时间："+ (end - start2));
		 if((System.currentTimeMillis()-start)>2000){
		     logger.error("订单管理查询总时间超过2s,list size:[{}]",list.size());
         }
		return list;
	}

	@Override
	public List<ShippingBean> getShipPackmentInfo(String orderNo) {
		List<ShippingBean> sb=orderinfoMapper.getShipPackmentInfo(orderNo);
		return sb;
	}

	@Override
	public Double getEstimatefreight(String orderNo) {
		double estimatefreight=0.00;
		Object o=orderinfoMapper.getEstimatefreight(orderNo);
		if(o != null){
			estimatefreight=(Double)o;
		}
		return estimatefreight;
	}

	@Override
	public double getAllWeight(String orderNo) {
		return orderinfoMapper.getAllWeight(orderNo);
	}

	@Override
	public String getFileByOrderid(String orderNo) {
		return orderinfoMapper.getFileByOrderid(orderNo);
	}

	@Override
	public int cancelOrder(String orderid) {
		int row=0;
		try{

			row=orderinfoMapper.cancelOrder(orderid);
			SendMQ.sendMsg(new RunSqlModel("update orderinfo set state=-1 where order_no='"+orderid+"'"));

		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int cancelPayment(String pid) {
		int row=0;
		try{

			row=orderinfoMapper.cancelPayment(pid);
			SendMQ.sendMsg(new RunSqlModel("update payment set paystatus=0  where id='"+pid+"'"));

		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public List<AutoOrderBean> getOrderList(String orderid, String userid, String page) {
		List<AutoOrderBean> list = new ArrayList<AutoOrderBean>();
		List<AutoOrderBean> dataList=orderinfoMapper.getOrderList(orderid,userid,page);
		int count=orderinfoMapper.getCounts();
		for(AutoOrderBean a:dataList){
			AutoOrderBean bean = new AutoOrderBean();
			bean.setCurrency(a.getCurrency());
			bean.setOrderAdmin(a.getOrderAdmin());
			bean.setOrderid(a.getOrderid());
			bean.setPaymentAdmin(a.getPaymentAdmin());
			bean.setPayPrice(a.getPayPrice());
			int state = a.getState();
			String strState = state == 0 ? "等待付款" : "";
			strState = state == 5 ? "确认价格中" : strState;
			strState = state == -1 ? "后台取消" : strState;
			strState = state == 6 ? "用户取消" : strState;
			strState = state == 1 ? "购买中" : strState;
			strState = state == 2 ? "已到仓库" : strState;
			strState = state == 3 ? "出运中" : strState;
			strState = state == 4 ? "完结" : strState;
			strState = state == 7 ? "预订单" : strState;
			bean.setOrderState(strState);
			bean.setUserid(a.getUserid());
			bean.setCount(count);
			bean.setIndex(a.getOid());
			String paymentstr =a.getPaymentstr();
			if (paymentstr == null || paymentstr.isEmpty()) {
				bean.setPayStatus("未付款");
				bean.setPayType("");
				bean.setPayId(0);
			} else {
				String[] paymentstrs = paymentstr.split(",");
				bean.setPayStatus(paymentstrs[0].equals("0") ? "未付款" : "已付款");
				bean.setPayType(paymentstrs[1].equals("0") ? "paypal" : "Wire Transfer");
				bean.setPayId(Integer.valueOf(paymentstrs[2]));
			}
			bean.setCreateTime(a.getCreateTime());
			bean.setId(a.getId());
			list.add(bean);
		}
		return list;
	}

	@Override
	public TabTransitFreightinfoUniteOur getFreightInfo(String countryNameCn, int isEub) {
		TabTransitFreightinfoUniteOur tf=orderinfoMapper.getFreightInfo(countryNameCn,isEub);
		return tf;
	}

	@Override
	public int updateFreight(String orderNo, String freight) {
		return orderinfoMapper.updateFreight(orderNo,freight);
	}

	@Override
	public double getFreightFee(String allFreight,OrderBean orderInfo) {
		double esFreight=0.00;
		String modeTransport =orderInfo.getMode_transport();
		//orderinfoMapper.getModeTransport(orderInfo.getOrderNo());
		if(StringUtil.isBlank(modeTransport)){
			return 0.00;
		}
		String [] strs=modeTransport.split("@");
		String type=strs[0];
		if(StringUtil.isBlank(type) || "null".equals(type)){
			type="Epacket";
		}
		type=type.toLowerCase();
		String country="";
		//特殊商品
		double weight1=0.00;
		//普通商品
		double weight2=0.00;
		List<OrderDetailsBean> odList=orderinfoMapper.getCatidDetails(orderInfo.getOrderNo());
		for(OrderDetailsBean odd:odList){
			String catid=odd.getGoodscatid();
			double odWeight=odd.getOd_total_weight();
			if(Util.getThisCatIdIsSpecialStr(catid)){
				//特殊商品
				weight1+=odWeight;
			}else{
				//普通商品
				weight2+=odWeight;
			}
			country=odd.getCountryId();
		}
		double freight1=0.00,freight2=0.00;
		if(weight1>0){
			freight1=getOrderShippingCost(weight1,Integer.valueOf(country),type,true);
		}
		if(weight2>0){
			freight2=getOrderShippingCost(weight2,Integer.valueOf(country),type,false);;
		}
		esFreight +=(freight1+freight2);
		return esFreight;
	}

	@Override
	public int updateFreightForOrder(String orderNo, String freight,String esprice) {
		return orderinfoMapper.updateFreightForOrder(orderNo,freight,esprice);
	}

	public double getOrderShippingCost(double weight ,int countryid,String shippingMethod,boolean isSpecialCatid){
		double sumEubSpecialWeight = 0d,sumFreeSpecialWeight = 0d,sumNormalWeight = 0d,umSpecialWeight = 0d,sumEubNormalWeight = 0d,
				sumEubWeight = 0d,sumFreeNormaWeight = 0d,umFreeSpecialWeight = 0d,sumFreeWeight = 0d,shippingCost = 0d,sumGoodsCarWeight = 0d,sumSpecialWeight = 0d;
		if(isSpecialCatid){
			sumSpecialWeight = DoubleUtil.mul(weight,1000d);
		}else {
			sumGoodsCarWeight =  DoubleUtil.mul(weight,1000d);
		}
		double normalBaseWeight = 0d;
		BigDecimal normalBasePrice = new BigDecimal(0);
		BigDecimal normalRatioPrice = new BigDecimal(0);
		BigDecimal normalBigWeightPrice = new BigDecimal(0);
		Double specialBaseWeight = 0d;
		BigDecimal specialBasePrice = new BigDecimal(0);
		BigDecimal specialRatioPrice = new BigDecimal(0);
		BigDecimal specialBigWeightPrice = new BigDecimal(0);
		//第一步 根据国家id 运输方式 获取相关运费信息
		TabTransitFreightinfoUniteNewExample example = new TabTransitFreightinfoUniteNewExample();
		TabTransitFreightinfoUniteNewExample.Criteria criteria = example.createCriteria();
		criteria.andCountryidEqualTo(countryid);
		criteria.andTransportModeEqualTo(shippingMethod);
		List<TabTransitFreightinfoUniteNew> list = orderinfoMapper.selectByExample(example);
		if(list.size()<=0){
			TabTransitFreightinfoUniteNewExample example1 = new TabTransitFreightinfoUniteNewExample();
			TabTransitFreightinfoUniteNewExample.Criteria criteria1 = example1.createCriteria();
			criteria1.andCountryidEqualTo(29);
			criteria1.andTransportModeEqualTo(shippingMethod);
			list = orderinfoMapper.selectByExample(example1);
		}
		BigDecimal sumFreight = new BigDecimal(0);
		TransitPricecost transitPricecost = new TransitPricecost();
		BigDecimal singleFreight = new BigDecimal(0);
		BigDecimal singleFreightNor = new BigDecimal(0);
		BigDecimal singleFreightSpc = new BigDecimal(0);
		//免邮商品当前的运费
		BigDecimal singleFreeFreightNor = new BigDecimal(0);
		BigDecimal singleFreeFreightSpc = new BigDecimal(0);
		//免邮商品当前运输费方式不带首重运费
		BigDecimal sumFreeShippingCost = new BigDecimal(0);
		// 判断初始重量是否是500g
		double ratioWeight = 500d;
		BigDecimal zero = new BigDecimal(0);
		if(list.size()>0){
			TabTransitFreightinfoUniteNew transitInfo =  list.get(0);
			shippingMethod = transitInfo.getTransportMode();
			normalBaseWeight = transitInfo.getNormalBaseWeight();
			normalBasePrice = transitInfo.getNormalBasePrice();
			normalRatioPrice = transitInfo.getNormalRatioPrice();
			normalBigWeightPrice = transitInfo.getNormalBigWeightPrice();
			specialBaseWeight = transitInfo.getSpecialBaseWeight();
			specialBasePrice = transitInfo.getSpecialBasePrice();
			specialRatioPrice = transitInfo.getSpecialRatioPrice();
			specialBigWeightPrice = transitInfo.getSpecialBigWeightPrice();
			boolean isSplit = transitInfo.getSplit() == 1 ? true : false;
			if(isSplit && sumSpecialWeight>0){
				sumGoodsCarWeight = sumSpecialWeight;
			}
			//基础运费加上 20%利润率
			BigDecimal multiple = new BigDecimal(1.00).setScale(2,BigDecimal.ROUND_HALF_UP);
			//Added <V1.0.1> Start： cjc 2018/8/18 18:26 TODO DHL,FEDEX 运费涨价10%
			BigDecimal rate = new BigDecimal(0.90).setScale(2,BigDecimal.ROUND_HALF_UP);
			if("DHL".equals(shippingMethod) || "FEDEX".equals(shippingMethod)){
				normalBasePrice = normalBasePrice.multiply(rate).setScale(6);
				normalRatioPrice = normalRatioPrice.multiply(rate).setScale(6);
				normalBigWeightPrice = normalBigWeightPrice.multiply(rate).setScale(6);
				specialBasePrice = specialBasePrice.multiply(rate).setScale(6);
				specialBigWeightPrice = specialBigWeightPrice.multiply(rate).setScale(6);
				specialRatioPrice = specialRatioPrice.multiply(rate).setScale(6);
			}
			boolean isEC = false;
			if(isSplit){
				if(sumSpecialWeight > 21000){
					singleFreightSpc = sumSpecialWeight >0 ? singleFreight.add(specialBigWeightPrice.multiply(new BigDecimal(sumSpecialWeight/1000))) : new BigDecimal(0);

				}else if(specialBaseWeight == ratioWeight){
					singleFreightSpc = sumSpecialWeight >0 ? specialBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumSpecialWeight,specialBaseWeight)),1d))).multiply(specialRatioPrice)) :zero;
				}else {
					singleFreightSpc = sumSpecialWeight >0 ? specialBasePrice.add( new BigDecimal((sumSpecialWeight - specialBaseWeight)/specialBaseWeight).multiply(specialRatioPrice)) :new BigDecimal(0);
				}
				if(sumNormalWeight > 21000){
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBigWeightPrice.multiply(new BigDecimal(sumGoodsCarWeight/1000))) : new BigDecimal(0);
				}else if(normalBaseWeight == ratioWeight){
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumGoodsCarWeight,normalBaseWeight)),1d))).multiply(normalRatioPrice))) : zero;
				}else {
					singleFreightNor = sumGoodsCarWeight >0 ? singleFreight.add(normalBasePrice.add( new BigDecimal((sumGoodsCarWeight - normalBaseWeight)/normalBaseWeight).multiply(normalRatioPrice))) : new BigDecimal(0);
				}
				singleFreight = singleFreightSpc.add(singleFreightNor);
			}else 	if(sumGoodsCarWeight > 21000){
				singleFreight = sumGoodsCarWeight > 0 ? singleFreight.add(normalBigWeightPrice.multiply(new BigDecimal(sumGoodsCarWeight/1000))) : zero;
			}else if(normalBaseWeight == ratioWeight){
				singleFreight = sumGoodsCarWeight > 0 ? normalBasePrice.add( new BigDecimal((DoubleUtil.sub(Math.ceil(DoubleUtil.divide(sumGoodsCarWeight,normalBaseWeight)),1d))).multiply(normalRatioPrice) ) : zero;
			}else {
				singleFreight = sumGoodsCarWeight > 0 ? normalBasePrice.add( new BigDecimal((sumGoodsCarWeight - normalBaseWeight)/normalBaseWeight).multiply(normalRatioPrice) ) : zero;
			}
		}
		shippingCost = singleFreight.doubleValue();
		return shippingCost;
	}

	@Override
	public List<Admuser> getBuyerAndAll() {
		return orderinfoMapper.getBuyerAndAll();
	}


	private final static List<String> MSG_COUNTRY_LIST = new ArrayList<String>(){{
        add("yigo");
        add("Guam");
        add("hawaii");
        add("Honolulu");
        add("Puerto Rico");
        add("Virgin Islands");
        add("Samoa");
        add("Mariana Islands");
    }};

    @Override
    public String checkCountryMsg(String orderid) {
        OrderBean orders = getOrders(orderid);
        if (orders != null && orders.getAddress() != null) {
            String address = orders.getAddress().toString().toLowerCase();
            for (String country : MSG_COUNTRY_LIST) {
                if (address.indexOf(country.toLowerCase()) > 0) {
                    return "请注意国家!<br />USA(" + country + ")";
                }
            }
        }
        return null;
    }

	@Override
	public int updateOrderInfoFreight(String orderNo, String amount) {
		return orderinfoMapper.updateOrderInfoFreight(orderNo, amount);
	}

    @Override
    public boolean setSampleGoodsIsOrder(String orderNo, Integer userId, List<SampleOrderBean> sampleOrderBeanList) {

    	try {
			if (sampleOrderBeanList != null && sampleOrderBeanList.size() > 0) {
				for(SampleOrderBean sm : sampleOrderBeanList){
					sm.setOrderNo(orderNo);
					sm.setUserId(userId);
				}
				// 保存客户选择的样品信息到数据库
//				this.orderinfoMapper.batchInsertIntoSampleOrderGoods(sampleOrderBeanList);

				for (SampleOrderBean sob:sampleOrderBeanList){
					String sql=" insert into sample_order_info(user_id,order_no,pid,img_url,sku_id,en_type,is_choose)" +
							"        values " +
							"            ('"+sob.getUserId()+"','"+sob.getOrderNo()+"','"+sob.getPid()+"','"+sob.getImgUrl()+"','"+sob.getSkuId()+"','"+sob.getEnType()+"','"+sob.getIsChoose()+"')";
					SendMQ.sendMsg(new RunSqlModel(sql));
				}


				// 更新客户选择的样品信息
				// sampleOrderService.updateSampleOrderGoods(sampleOrderBeanList);
				// 生成样品订单数据
				boolean bo=this.genSampleOrderByInfoList(orderNo, userId, sampleOrderBeanList);
//				sampleOrderBeanList.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("userId:" + userId + "orderNo:" + orderNo + ",setSampleGoodsIsOrder error: ", e);
			return false;
		}

    	return true;
    }

	@Override
	public int updateOrderSplitNumOrderDetailsData(String oldOrderNo, String newOrderNo) {
		return orderinfoMapper.updateOrderSplitNumOrderDetailsData(oldOrderNo, "'" + newOrderNo + "'");
	}

	@Override
	public int updateOrderSplitNumPurchaseData(String orderNo) {
		return orderinfoMapper.updateOrderSplitNumPurchaseData(orderNo);
	}

	@Override
	public int updateOrderSplitNumIdRelationtableData(String orderNo) {
		return orderinfoMapper.updateOrderSplitNumIdRelationtableData(orderNo);
	}

	@Override
	public int updateOrderSplitNumGoodsCommunicationInfoData(String orderNo) {
		return orderinfoMapper.updateOrderSplitNumGoodsCommunicationInfoData(orderNo);
	}

	public boolean genSampleOrderByInfoList(String orderNo, int userId, List<SampleOrderBean> sampleOrderBeanList) {
		// 插入订单详情
		String spOrderNo = orderNo + "_SP";
		String urlmd5=this.orderinfoMapper.getUrlMd5ByOrder(orderNo);
		//生成order-details
		List<OrderDetailsBeans> odbList = new ArrayList<>();
		for (SampleOrderBean sampleOrderBean : sampleOrderBeanList) {
			sampleOrderBean.setOrderNo(spOrderNo);
			sampleOrderBean.setUserId(userId);

			OrderDetailsBeans temp = new OrderDetailsBeans();
			temp.setGoodsid((int) ((Math.random() * 9 + 1) * 500000));
			temp.setIsStockFlag(0);
			temp.setShopCount(0);
			temp.setDelivery_time("");
			temp.setIsFreeShipProduct(3);
			temp.setGoods_pid(sampleOrderBean.getPid());
			temp.setIsFeight(3);
			temp.setStartPrice("0");
			temp.setBizPriceDiscount("0");
			temp.setCheckprice_fee(0);
			temp.setCheckproduct_fee(0);
			temp.setState(0);
			temp.setFileupload("/temp/pic");
			temp.setActual_weight("0");
			temp.setFreight_free(0);
			temp.setYourorder(sampleOrderBean.getGoodsNum());
			temp.setUserid(userId);
			temp.setGoodsname(sampleOrderBean.getEnName());
			temp.setGoodsprice("0");
			temp.setFreight("0");
			temp.setRemark("sample goods");
			temp.setGoods_url(sampleOrderBean.getOnlineUrl());
			temp.setGoodsUrlMD5(urlmd5);
			temp.setGoods_img(sampleOrderBean.getImgUrl());
			temp.setGoods_type(sampleOrderBean.getEnType());
			temp.setTotal_weight(sampleOrderBean.getWeight());
			temp.setGoodscatid(sampleOrderBean.getCatid());
			temp.setSerUnit(sampleOrderBean.getSellUnit());
			temp.setActual_volume(sampleOrderBean.getSkuId());
			temp.setOrderid(spOrderNo);
			temp.setExtra_freight(0);
			odbList.add(temp);
		}


		try {
//			//添加订单详细信息
//			this.orderinfoMapper.batchAddOrderDetail(odbList);
//			// 插入地址信息
//			this.orderinfoMapper.insertSampleOrderAddress(orderNo, spOrderNo);
//			// 插入订单信息
//			this.orderinfoMapper.insertSampleOrderInfo(orderNo, spOrderNo);
//			// 设置样品商品订单号
//			this.orderinfoMapper.updateSampleOrderGoods(sampleOrderBeanList);


			for (OrderDetailsBeans od:odbList){
				String sql="insert order_details(goodsid,orderid,dropshipid,delivery_time,checkprice_fee,checkproduct_fee,state,fileupload,yourorder," +
						"userid,goodsname,goodsprice,goodsfreight,goodsdata_id,remark,goods_class,extra_freight,car_url,car_urlMD5,goods_pid," +
						"car_img,car_type,freight_free,od_bulk_volume,od_total_weight,discount_ratio,goodscatid,isFeight,seilUnit," +
						"startPrice,bizPriceDiscount,group_buy_id,actual_volume,actual_weight,isFreeShipProduct,shopCount) values";
               String value="('"+od.getGoodsid()+"','"+od.getOrderid()+"','"+od.getDropshipid()+"','"+od.getDelivery_time()+"','"+od.getCheckprice_fee()+"','"+od.getCheckproduct_fee()+"','0','"+od.getFileupload()+"','"+od.getYourorder()+"','" +
					   od.getUserid()+"','"+SendMQ.repCha(od.getGoodsname())+"','"+od.getGoodsprice()+"','"+od.getGoodsfreight()+"','"+od.getGoodsdata_id()+"','"+od.getRemark()+"','"+od.getGoods_class()+"','"+od.getExtra_freight()+"','"+od.getGoods_url()+"','"+od.getGoodsUrlMD5()+"','"+od.getGoods_pid()+"','" +
					   od.getGoods_img()+"','"+SendMQ.repCha(od.getGoods_type())+"','"+od.getFreight_free()+"','"+od.getBulk_volume()+"','"+od.getTotal_weight()+"','"+od.getDiscount_ratio()+"','"+od.getGoodscatid()+"','"+od.getIsFeight()+"','"+od.getSerUnit()+"','" +
					   od.getStartPrice()+"','"+od.getBizPriceDiscount()+"','"+od.getGroupBuyId()+"','"+od.getActual_volume()+"','"+od.getActual_weight()+"','"+od.getIsFreeShipProduct()+"','"+od.getShopCount()+"')";
			sql+=value;
				//添加订单详细信息

				SendMQ.sendMsg(new RunSqlModel(sql));

			}
			// 插入地址信息
			SendMQ.sendMsg(new RunSqlModel("insert into order_address(AddressID,orderNo,Country,statename,address,address2,phoneNumber,zipcode,Adstatus,street,recipients)" +
					"select AddressID,'"+spOrderNo+"' as orderNo,Country,statename,address,address2,phoneNumber,zipcode,Adstatus,street,recipients " +
					"FROM order_address WHERE  orderNo='"+orderNo+"' and NOT EXISTS (SELECT orderNo FROM order_address WHERE orderNo='"+spOrderNo+"')"));

			// 插入订单信息
			SendMQ.sendMsg(new RunSqlModel(" insert orderinfo(order_no,user_id,product_cost,state,delivery_time,service_fee,ip,mode_transport,create_time,details_number," +
					"        pay_price_three,actual_allincost,foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,share_discount," +
					"        order_ac,actual_lwh,actual_weight,actual_weight_estimate,extra_freight,orderRemark,cashback,firstdiscount," +
					"        isDropshipOrder,address_id,packag_number,coupon_discount,exchange_rate,grade_discount,vatbalance,ordertype,actual_freight_c," +
					"        memberFee,processingfee) " +
					"        select '"+spOrderNo+"' as order_no,user_id,0 as product_cost,5 as state,delivery_time,0 as service_fee,ip,mode_transport,create_time," +
					"        2 as details_number, 0 as pay_price_three,actual_allincost,0 as foreign_freight,0 as pay_price,0 as pay_price_tow,currency," +
					"        0 as actual_ffreight,0 as discount_amount,0 as share_discount," +
					"        0 as order_ac,0 as actual_lwh,actual_weight,actual_weight_estimate,0 as extra_freight,'sampleOrder' as orderRemark," +
					"        0 as cashback,0 as firstdiscount,isDropshipOrder,address_id,packag_number,0 as coupon_discount," +
					"        exchange_rate,grade_discount,vatbalance,ordertype,actual_freight_c,0 as memberFee,processingfee" +
					"        FROM orderinfo WHERE order_no ='"+orderNo+"' and NOT EXISTS (SELECT order_no FROM orderinfo WHERE order_no='"+spOrderNo+"')"));

			for (SampleOrderBean sb:sampleOrderBeanList){
				String sql="";
				if (sb.getIsChoose()>0){
					sql = " update sample_order_info set order_no = '"+sb.getOrderNo()+"',is_choose = '"+sb.getIsChoose()+"' where user_id = '"+sb.getUserId()+"' and pid = '"+sb.getPid()+"'";
				}else {
					sql = " update sample_order_info set order_no = '"+sb.getOrderNo()+"' where user_id = '"+sb.getUserId()+"' and pid = '"+sb.getPid()+"'";
				}
				// 设置样品商品订单号
				SendMQ.sendMsg(new RunSqlModel(sql));
			}



		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
    @Override
	public OrderBean getOrders(String orderNo) {
		OrderBean ob=orderinfoMapper.getOrder(orderNo);
		if(ob != null){
			if (ob.getIsDropshipOrder() == 1) {
				//dropship订单
				String dropShipList = orderinfoMapper.getDropshipOrderNoList(orderNo);
				ob.setDropShipList(dropShipList);
			}
			String couponAmount=ob.getCouponAmount();
			if(StringUtil.isBlank(couponAmount)){
				couponAmount="0";
			}
			ob.setCouponAmount(couponAmount);
			List<String> yhCount=pruchaseMapper.getProblem(orderNo,"1");//验货疑问总数
			int checkeds=pruchaseMapper.getChecked(orderNo,"1");//验货无误总数
			int cg=pruchaseMapper.getPurchaseCount(orderNo,"1");//采购总数
			int rk=pruchaseMapper.getStorageCount(orderNo,"1");//入库总数
			//判断该用户是否为黑名单
			int backList= warehouseMapper.getBackList(ob.getUserEmail());
			int payBackList=0;
			if(StringUtil.isNotBlank(ob.getPayUserName())){
				payBackList= warehouseMapper.getPayBackList(ob.getPayUserName());
			}
			String backFlag="";
			if(backList>0 || payBackList>0){
				backFlag="该用户为黑名单";
			}
			ob.setBackFlag(backFlag);
			ob.setYhCount(yhCount.size());
			ob.setCheckeds(checkeds);
			ob.setCg(cg);
			ob.setRk(rk);
			String countryNameCn = Utility.getStringIsNull(ob.getCountryNameCN())? ob.getCountryNameCN() : "USA";
			ob.setCountryNameCN(countryNameCn);
			ob.setDzConfirmtime(Utility.getStringIsNull(ob.getDzConfirmtime())? ob.getDzConfirmtime().substring(0, ob.getDzConfirmtime().indexOf(" ")) : "");
			ob.setOrderNo(orderNo);
			ob.setExchange_rate(StringUtil.isBlank(ob.getExchange_rate())?"6.3":ob.getExchange_rate());
			String pay_price_tow = ob.getPay_price_tow();
			ob.setPay_price_tow(Utility.getIsDouble(pay_price_tow) ? pay_price_tow : "0");
			ob.setExpect_arrive_time(StringUtil.isNotBlank(ob.getExpect_arrive_time())? ob.getExpect_arrive_time() : "");
			ob.setTransport_time(StringUtil.isNotBlank(ob.getTransport_time())? ob.getTransport_time() : "");
			Address address = new Address();
			String country = "0";
			if (StringUtils.isStrNull(ob.getNew_zid())) {
				country = StringUtils.isStrNull(ob.getCountry()) ? "0" : ob.getCountry();
			} else {
				country = ob.getNew_zid();
			}
			address.setId(Integer.parseInt(country));
			if(ob.getCountry() != null && ob.getCountry().contains("AFRICA")){
				address.setCountry(StringUtils.isStrNull(ob.getNew_zid()) ? ob.getCountryName():ob.getCountry() .replace(" ", ""));
			}else{
				address.setCountry(StringUtils.isStrNull(ob.getNew_zid()) ? ob.getCountryName():ob.getCountry());
			}
			address.setAddress(ob.getAddresss());
			address.setPhone_number(ob.getPhonenumber());
			address.setZip_code(StringUtil.isBlank(ob.getZipcode())?"无":ob.getZipcode());
			address.setStatename(ob.getStatename());
			address.setAddress2(ob.getAddress2());
			address.setRecipients(ob.getRecipients());
			address.setStreet(ob.getStreet());
			ob.setAddress(address);
			ob.setOrderNumber(ob.getOrdernum() == 1);
			ob.setPay_price(Double.parseDouble(Utility.formatPrice(String.valueOf(ob.getPay_price())).replaceAll(",", "")));
			ob.setForeign_freight(Utility.getStringIsNull(ob.getForeign_freight()) ? ob.getForeign_freight() : "0");
			ob.setEmail(ob.getAdminname());
			// 提醒，0-客户投诉，1-入库问题，2，-出库问题
			ob.setReminded(new int[]{ob.getComplain(),ob.getPurchase(), ob.getDeliver()});
			String paytype=ob.getPaytypes();
			String tp="支付错误";
			if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
				StringBuilder types=new StringBuilder();
				String [] t=paytype.split(",");
				for(int i=0;i<t.length;i++){
					types.append(StringUtil.getPayType(t[i]));
					if(i != t.length-1){
						types.append(",");
					}
				}
				tp=types.toString();
			}else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
				tp=StringUtil.getPayType(paytype);
			}
			ob.setPaytypes(tp);
			String gradeName="Non Member";
			double grade=Double.parseDouble(String.format("%.2f", ob.getGradeDiscount()/Double.parseDouble(ob.getProduct_cost())))*100;
			if(grade==3){
				gradeName="BizClub Member";
			}else if(grade==5){
				gradeName="Silver VIP";
			}else if(grade==7){
				gradeName="Gold VIP";
			}else if(grade==9){
				gradeName="Platinum VIP";
			}else if(grade==12){
				gradeName="Diamond VIP";
			}
			ob.setGradeName(gradeName);
		}
		return ob;
	}

	@Override
	public Forwarder getForwarder(String orderNo) {
		String newOrderNo = orderNo + ",";
		Forwarder forword=orderinfoMapper.getForwarder(newOrderNo);
		String expressno = "";
		String logistics_name = "";
		if(forword !=null){
			expressno=forword.getExpress_no();
			logistics_name=forword.getLogistics_name();
		}
		if (StrUtils.isNotNullEmpty(expressno) || StrUtils.isNotNullEmpty(logistics_name)) {
			forword.setOrder_no(orderNo);
			forword.setExpress_no(expressno);
			forword.setLogistics_name(logistics_name);
			forword.setNew_state("");
			forword.setTransport_details("");
			forword.setIsneed(0);
		}
		if (forword == null) {
			forword=orderinfoMapper.getForwarder(orderNo);
			if(forword !=null){
				expressno=forword.getExpress_no();
				logistics_name=forword.getLogistics_name();
			}
			if (StrUtils.isNotNullEmpty(expressno) || StrUtils.isNotNullEmpty(logistics_name)) {
				forword.setOrder_no(orderNo);
				forword.setExpress_no(expressno);
				forword.setLogistics_name(logistics_name);
				forword.setNew_state("");
				forword.setTransport_details("");
				forword.setIsneed(0);
			}
		}
		return forword;
	}

	@Override
	public List<Map<String, String>> getOrdersPays(String orderNo) {
		int flag=0;
		if(orderNo.indexOf("_") == -1){
			flag=1;
			orderNo = orderNo.substring(0, orderNo.length() - 1);
		}
		return orderinfoMapper.getOrdersPays(orderNo,flag);
	}

	@Override
	public List<CodeMaster> getLogisticsInfo() {
		return orderinfoMapper.getLogisticsInfo();
	}

	@Override
	public List<String> getOrderNos(int userId,String orderNo) {
		return orderinfoMapper.getOrderNos(userId,orderNo);
	}

	@Override
	public List<String> getSameAdrDifAccount(int userId, String address, String street, String zipCode,String country, String city, String recipients) {
		return orderinfoMapper.getSameAdrDifAccount(userId,address,street,zipCode,country,city,recipients);
	}
	
	
	@Override
	public double getAcPayPrice(String orderNo) {
		return orderinfoMapper.getAcPayPrice(orderNo);
	}

	@Override
	public String getModeTransport(String orderNo) {
		return orderinfoMapper.getModeTransport(orderNo);
	}

	@Override
	public List<EmailReceive1> getall(String orderNo) {
		List<EmailReceive1> list=orderinfoMapper.getall(orderNo);
		for(EmailReceive1 email:list){
			String content=email.getContent();
			try{
				String content1="";
				String encoding="utf-8";
				File file=new File(content);
				if(file.isFile() && file.exists()){
					InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					while((lineTxt = bufferedReader.readLine()) != null){
						content1+=lineTxt;
					}
					read.close();
				}
				email.setContent(content1);
			}catch(Exception e){

			}
		}
		return list;
	}

	@Override
	public Evaluate getEvaluate(String orderNo) {
		return orderinfoMapper.getEvaluate(orderNo);
	}

	@Override
	public TaoBaoOrderInfo getShipStatusInfo(String tb1688Itemid, String lastTb1688Itemid, String confirmTime, String admName, String shipno, int offlinePurchase, String orderId, int goodsId) {
		TaoBaoOrderInfo t=new TaoBaoOrderInfo();
		if(offlinePurchase == 1){
			t=orderinfoMapper.getShipStatusInfoFor(tb1688Itemid,lastTb1688Itemid,confirmTime,admName,shipno,offlinePurchase,orderId,goodsId);
		}else{
			t=orderinfoMapper.getOrderReplenishment(tb1688Itemid,lastTb1688Itemid,confirmTime,admName,shipno,offlinePurchase,orderId,goodsId);
			if(t!=null){
				t=orderinfoMapper.getShipStatusInfo(t.getTb_1688_itemid(),t.getCreatetime(),admName);
			}else{
				t=orderinfoMapper.getShipStatusInfos(tb1688Itemid,lastTb1688Itemid,confirmTime,admName);
			}
		}
		if(t != null){
			t.setSupport_info(StringUtils.isStrNull(t.getSupport_info())
					|| t.getSupport_info().length() < 5 ? "" : t.getSupport_info());
		}
		return t;
	}

	@Override
	public String queryBuyCount(int admuserId) {
		return orderinfoMapper.queryBuyCount(admuserId);
	}

	@Override
	public List<String> getOrderIdList() {
		return orderinfoMapper.getOrderIdList();
	}

	@Override
	public int updateGoodsCarMessage(String orderNo) {
		return orderinfoMapper.updateGoodsCarMessage(orderNo);
	}

	@Override
	public List<OrderDetailsBean> getOrdersDetails(String orderNo) {
		DecimalFormat df = new DecimalFormat("######0.00");
		Map<String, ArrayList<Object[]>> changInfo = new HashMap<String, ArrayList<Object[]>>();
		List<OrderChange> ocList=orderinfoMapper.getOrderChange(orderNo);
		for(OrderChange oc:ocList){
			String values =oc.getNewValue();
			String oldValue =oc.getOldValue();
			if (oc.getRopType() == 1 && oc.getDel_state() == 1) {
				values = "原始价格:" + oldValue;
			}else if(oc.getRopType() == 5 && Utility.getStringIsNull(oldValue)){
				values = oc.getDateline() + "   "+(oldValue.equals("1")?"公司":"客户")+"：" + values;
			}
			if (changInfo.get("order" + oc.getGoodId()) == null) {
				ArrayList<Object[]> order_change = new ArrayList<Object[]>();
				order_change.add(new Object[]{oc.getRopType(), oc.getDel_state(), values, oldValue,oc.getGoodId()});
				changInfo.put("order" + oc.getGoodId(), order_change);
			} else {
				changInfo.get("order" + oc.getGoodId()).add(new Object[]{oc.getRopType(), oc.getDel_state(), values});
			}
		}
		List<OrderDetailsBean> list=orderinfoMapper.getOrdersDetails(orderNo);
		for(OrderDetailsBean odb:list){
			String aliLink = "";
			String alipid = Utility.getStringIsNull(odb.getAlipid()) ?odb.getAlipid() : "0";
			if (!"0".equals(alipid)) {
				aliLink = "https://www.aliexpress.com/item/aaa/" + alipid + ".html";
			}
			odb.setAlipid(aliLink);
			odb.setOrderid(orderNo);
			String pidInventory=odb.getPidInventory();
			if(StringUtil.isBlank(pidInventory) || Integer.valueOf(pidInventory)<=0){
				pidInventory="0";
			}
			odb.setPidInventory(pidInventory);
			//产品总重量
			String final_weight= odb.getFinal_weight();
			if(StringUtil.isBlank(final_weight)){
				final_weight=odb.getDfinal_weight();
			}
			if(StringUtil.isBlank(final_weight)){
				final_weight="0.00";
			}
			if(odb.getGoods_pid().equals(odb.getTb_1688_itemid())){
				odb.setShopFlag("1");
			}else{
				odb.setShopFlag("0");
			}
//			odb.setFinal_weight(df.format(Double.parseDouble(final_weight)*odb.getYourorder()));
			// 16 88 的重 量
			String weitht1688 = Utility.getStringIsNull(odb.getCbrWeight()) ? odb.getCbrWeight(): "0";
			// 1688的价格
			String price1688 = Utility.getStringIsNull(odb.getCbrPrice()) ? odb.getCbrPrice() : "0";
			if("0".equals(price1688) || StringUtil.isBlank(odb.getCbrPrice())){
				price1688=odb.getCbrdPrice();
			}
			price1688=StringUtil.isBlank(price1688)?"0":price1688;
			odb.setAli_price(StringUtils.isStrNull(odb.getAli_price()) ? "非精确对标" : odb.getAli_price());
			odb.setCountry(StringUtils.isStrNull(odb.getNew_zid()) ?odb.getCountry() :odb.getNew_zid());
			odb.setId(odb.getOid());
			//1688原始货源价格
			odb.setPrice1688(price1688);
			//产品实际预估利润
			if(odb.getOldValue()!=null){
				double profit_price=Double.parseDouble(odb.getGoodsprice())*6.6;
				odb.setPd_profit_price((profit_price/Double.parseDouble(odb.getOldValue()))*100);
			}else{
				odb.setPd_profit_price(0.00);
			}

			odb.setFreight(odb.getGoodsfreight());
			String goods_pid = StringUtil.isBlank(odb.getGoods_pid())? "0" : odb.getGoods_pid();
			odb.setCar_type(StringUtil.isBlank(odb.getCar_type())? "" :odb.getCar_type());
			String es_price=price1688;
			if(odb.getState() ==1 || odb.getState() == 0){
				es_price=StringUtil.getEsPriceNew(es_price,odb.getYourorder());
			}else{
				es_price="0.00";
			}
			String ali_sellunit=odb.getAli_sellunit();
			int unit=Util.getNumberForStr(ali_sellunit);
			odb.setEs_price(Double.valueOf(es_price)*odb.getYourorder()*unit);
			String shop_id = odb.getCbrShopid();
			if(StringUtil.isBlank(shop_id) || !odb.getGoods_pid().equals(odb.getTb_1688_itemid())){
				shop_id="采购货源和推荐货源不一致，无法提供供应商";
			}
			odb.setShop_id(shop_id);
			String inventoryRemark="";
			//查询该商品是否有使用库存
			Map<String, Object> useInventoryMap=pruchaseMapper.getUseInventory(odb.getOid());
			if(useInventoryMap != null) {
				String useInventory = com.cbt.util.StrUtils.object2Str(useInventoryMap.get("lock_remaining"));
				if(StringUtil.isNotBlank(useInventory)){
					inventoryRemark="该商品使用了【"+useInventory+"】件库存";
				}
				int ibState = Integer.parseInt(com.cbt.util.StrUtils.object2NumStr(useInventoryMap.get("ib_state")));
				if(ibState == 0) {
					inventoryRemark += ",仓库未确认库存,未完成移出库存操作";
				}else if(ibState == 1){
					inventoryRemark += ",仓库未确认商品取消,未完成移入库存操作";
				}else if(ibState == 2){
					inventoryRemark += ",仓库已确认库存,并完成移出库存";
				}else if(ibState == 3){
					inventoryRemark += ",仓库已确认商品取消,并完成移入库存";
				}else if(ibState == 4){
					inventoryRemark += ",仓库拒绝采购使用库存商品请求";
				}else if(ibState == 5){
					inventoryRemark += ",仓库拒绝商品进入库存请求";
				}
				if(ibState > 3 && StringUtil.isNotBlank(com.cbt.util.StrUtils.object2Str(useInventoryMap.get("ib_remark")))) {
					inventoryRemark +=",原因:"+com.cbt.util.StrUtils.object2Str(useInventoryMap.get("ib_remark"));
				}
			}
			
			odb.setInventoryRemark(inventoryRemark);

			String buy_url = getBuyUrl(odb);
			buy_url=StringUtil.getNewUrl(buy_url,goods_pid,odb.getCar_urlMD5());
			odb.setGoods_url(buy_url);
			// 转换图片小图到大图
			String car_img = odb.getCar_img();
			if (car_img == null || "".equals(car_img)) {
				odb.setGoods_img(car_img);
			} else if (car_img.indexOf(".jpg") != car_img.lastIndexOf(".jpg")){
				car_img = car_img.substring(0, car_img.indexOf(".jpg") + ".jpg".length());
			}else if (car_img.indexOf("32x32") > -1) {
				car_img = car_img.replace("32x32", "400x400");
			}else if (car_img.indexOf("60x60") > -1) {
				car_img = car_img.replace("60x60", "400x400");
			}
			odb.setGoods_img(car_img);
			odb.setGoods_freight(odb.getGoodsfreight());
			odb.setGoods_type(odb.getCar_type());
			odb.setNewsourceurl(odb.getNewValue());
			odb.setOldsourceurl(odb.getLastValue());
			// 拆分采购备注
			String oremark = "";
			getStringOdRemark(odb,oremark);
			odb.setOremark(oremark);
			odb.setSourc_price(odb.getOldValue());
			odb.setChange_price("");
			odb.setChange_delivery("");
			odb.setIscancel(0);
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			odb.setPurchase_time(StringUtil.isBlank(odb.getPurchase_time()) ? "" : odb.getPurchase_time());
			// 批量优惠前金额，原价
			double sprice =Double.parseDouble(odb.getSprice());
			if (StringUtil.isNotBlank(odb.getCar_urlMD5())) {
				if (odb.getCar_urlMD5().startsWith("D")) {
					odb.setMatch_url("https://detail.1688.com/offer/" + odb.getGoods_pid() + ".html");
				} else if (odb.getCar_urlMD5().startsWith("A")) {
					odb.setMatch_url("http://www.aliexpress.com/item/a/" + odb.getGoods_pid() + ".html");
				}
			}else {
				odb.setMatch_url("https://detail.1688.com/offer/" + odb.getGoods_pid() + ".html");
			}
			//获取产品单位
			getSeilUnit(odb);
			if (sprice != 0 && odb.getState() != 2) {
				double goodssprice_d = Double.parseDouble(odb.getGoodsprice());
				odb.setPreferential_price(new BigDecimal((sprice - goodssprice_d) * odb.getYourorder()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			getRoTypeAndState(changInfo, odb);
		}
		return list;
	}

	private void getSeilUnit(OrderDetailsBean odb) {
		try {
			String seilUnit_ =odb.getSeilUnit();
			seilUnit_ = seilUnit_.trim();
			if (seilUnit_.indexOf("(") > -1) {
				seilUnit_ = seilUnit_.substring(seilUnit_.indexOf("("));
			}
			odb.setSeilUnit(seilUnit_);
			odb.setGoodsUnit(seilUnit_);
		} catch (Exception e) {
		}
	}

	private String getBuyUrl(OrderDetailsBean odb) {
		String buy_url;
		if (!StringUtils.isStrNull(odb.getCar_url())) {
			buy_url = odb.getCar_url();
		} else if (!StringUtils.isStrNull(odb.getLastValue())) {
			buy_url = odb.getLastValue();
		} else if (!StringUtils.isStrNull(odb.getNewValue())) {
			buy_url = odb.getNewValue();
		} else {
			buy_url = "";
		}
		return buy_url;
	}

	@Override
	public String getUserEmailByOrderNo(String orderNo) {
		return orderinfoMapper.getUserEmailByOrderNo(orderNo);
	}

	@Override
	public int updateOrderinfoUpdateState(String orderNo) {
		return orderinfoMapper.updateOrderinfoUpdateState(orderNo);
	}

	@Override
	public int checkRecord(String orderNo) {
		return orderinfoMapper.checkRecord(orderNo);
	}

	@Override
	public int insertEmailRecord(String orderNo) {
		return orderinfoMapper.insertEmailRecord(orderNo);
	}

	@Override
	public List<Map<String, String>> getorderPending(int admuserid) {
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		List<String> orderIds=orderinfoMapper.getOrderIds(admuserid);
		if(orderIds != null && !orderIds.isEmpty()){
			list=orderinfoMapper.getorderPending(orderIds);
			orderIds.clear();
			for(Map<String, String> map:list){
				int count=this.pruchaseMapper.FindCountByEmial(map.get("email"));
				map.put("emailcount",String.valueOf(count));
				String tp="支付错误";
				String paystatus="";
				String orderNo=map.get("order_no");
				Payment p=orderinfoMapper.getPayTypeForOrderNo(orderNo);
				if(p!=null && StringUtil.isNotBlank(p.getPaytype())){
					String paytype=p.getPaytype();
					if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
						StringBuilder types=new StringBuilder();
						String [] t=paytype.split(",");
						for(int i=0;i<t.length;i++){
							types.append(StringUtil.getPayType(t[i]));
							if(i != t.length-1){
								types.append(",");
							}
						}
						tp=types.toString();
					}else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
						tp=StringUtil.getPayType(paytype);
					}
					paystatus=String.valueOf(p.getPaystatus());
				}
				map.put("paytypes",tp);
				map.put("paytype",paystatus);
				map.put("problems","-");
				map.put("estimatefreight","0");
				map.put("allFreights","0");
			}
		}
		return list;
	}

	@Override
	public List<Map<String, String>> getOrders1(int userID, int state,
	                                            Date startdate, Date enddate, String email, String orderno,
	                                            int startpage, int page, int admuserid, int buyid, int showUnpaid,String type,int status) {
		return orderinfoMapper.getOrders1(userID, state, startdate, enddate, email, orderno, (startpage-1)*40, 40, admuserid, buyid, showUnpaid,type,status);
	}
	@Override
	public int getOrdersCount(Map<String,String> paramMap) {
		paramMap.put("startdate_req","0".equals(paramMap.get("startdate_req"))?null:paramMap.get("startdate_req"));
		paramMap.put("enddate_req","0".equals(paramMap.get("enddate_req"))?null:paramMap.get("enddate_req"));
		return orderinfoMapper.getOrdersCount(paramMap);
	}

	@Override
	public List<Map<String, Integer>> getOrdersState(int admuserid) {
		List<Map<String, Integer>> result = orderinfoMapper.getOrdersState(admuserid);
		//国际物流预警中预警条数
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, - 90);
		String payStartDate = DATEFORMAT.format(ca.getTime()) + " 00:00";
        Map<String, Object> param = new HashMap<String, Object>(){{
            put("payStartDate", payStartDate);
            put("warning", 0);
        }};
		Integer waring0 = tabTrackInfoMapping.getTrackInfoListCount(param);
		Map<String, Integer> cywMap = new HashMap<String, Integer>();
		cywMap.put("state", 1);
		cywMap.put("counts", waring0);
		result.add(cywMap);
		// 支付失败的订单
		List<String> orderNoList = orderinfoMapper.getOrderIds(admuserid);
       	if(orderNoList.size() > 0){
			List<Map<String, String>> list = orderinfoMapper.getorderPending(orderNoList);
			orderNoList.clear();
			for (Map<String, Integer> bean : result) {
				if ("order_pending".equals(bean.get("state"))) {
					bean.put("counts", list==null?0:list.size());
				}
			}
		}
		return result;
	}
	
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<ConfirmUserInfo> getAllSalesAndBuyer() {
		return orderinfoMapper.getAllSalesAndBuyer();
	}

	@Override
	public int queryUserIdByOrderNo(String orderNo) {
		return orderinfoMapper.queryUserIdByOrderNo(orderNo);
	}
	@Override
	public List<Orderinfo> getAllOrderShippingMehtodIsNull() {
		return orderinfoMapper.getAllOrderShippingMehtodIsNull();
	}

	@Override
	public int getCountryIdByName(String countryNameEn) {
		int countryId = 999;
		ResourceBundle resource = ResourceBundle.getBundle("resource",Locale.getDefault());
		String zoneInfo = resource.getString("zoneInfo");
		Map<String,Integer > countryNameMap = new HashedMap();
		if(zoneInfo != null){
			String[] split = zoneInfo.split(",");
			for (int i = 0; i < split.length; i++) {
				String o =  split[i];
				String[] split1 = o.split("@");
				countryNameMap.put(split1[1],Integer.parseInt(split1[0]));
			}
		}
		if(countryNameMap.get(countryNameEn) != null){
			countryId = countryNameMap.get(countryNameEn);
		}
		return countryId;
	}
	@Override
	public  int updateOrderinfomodeTransport(String modeTransport,String orderNo){
		return orderinfoMapper.updateOrderinfomodeTransport(modeTransport,orderNo);
	}

	@Override
	public Boolean UpdateGoodsState(String goods_pid) {

		int b= 0;
		try {
			b = this.orderinfoMapper.UpdateGoodsState(goods_pid);
			if (b==0){
            b=this.orderinfoMapper.InserGoodsState(goods_pid);
            }
			return b>0?true:false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean UpdateAllGoodsState(String tbOrderId) {
		try {
			List<String>list =this.orderinfoMapper.FindAllGoodsPid(tbOrderId);
			boolean b = false;
			for (String goods_pid:list){
                b=this.UpdateGoodsState(goods_pid);
            }
            return b;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    @Override
    public boolean getSampleschoice(String orderNo) {
	    String sampleschoice = this.orderinfoMapper.getSampleschoice(orderNo);
	    if ("1".equals(sampleschoice)) {
	        return true;
        }
        return false;
    }

	@Override
	public int insertIntoOrderSplitNumLog(List<SplitGoodsNumBean> splitGoodsNumBeanList) {
		return orderinfoMapper.insertIntoOrderSplitNumLog(splitGoodsNumBeanList);
	}

    @Override
    public List<GoodsDistribution> queryGoodsDistributionByOrderNo(String orderNo) {
        return orderinfoMapper.queryGoodsDistributionByOrderNo(orderNo);
    }

    @Override
    public int batchUpdateDistribution(List<GoodsDistribution> goodsDistributionList) {
        return orderinfoMapper.batchUpdateDistribution(goodsDistributionList);
    }

	@Override
	public int querySampleOrderInfoByOrderId(String orderNo) {
		return orderinfoMapper.querySampleOrderInfoByOrderId(orderNo);
	}
	@Override
	public List<Tb1688OrderHistory> checkOrder(String shipno,int tbsourceCount) {
		int orderCount = orderinfoMapper.getCheckOrderCount(shipno);
		if(orderCount == tbsourceCount) {
			return null;
		}
		List<Tb1688OrderHistory> result = new ArrayList<>();
		List<Tb1688OrderHistory> tb1688Orders = orderinfoMapper.getGoodsData(shipno);
		List<Map<String, Object>> orderDataCheck = orderinfoMapper.getOrderDataCheck(shipno);
		boolean isInventory = false;
		for(Tb1688OrderHistory t : tb1688Orders) {
			isInventory = false;
			String tbskuid = StringUtil.isBlank(t.getSkuID()) ? t.getItemid() : t.getSkuID();
			String tbspecid = StringUtil.isBlank(t.getSpecId()) ? t.getItemid() : t.getSpecId();
			
			for(Map<String, Object> m : orderDataCheck) {
				String car_type = (String)m.get("car_type");
				String specid = "";
				String skuid = "";
				if(StringUtil.isBlank(car_type) || "0".equals(car_type)) {
					specid = (String)m.get("tb_1688_itemid");
					skuid = (String)m.get("tb_1688_itemid");
				}else {
					specid = (String)m.get("specid")!=null?(String)m.get("specid") : "";
					skuid = (String)m.get("skuid")!=null?(String)m.get("skuid") : "";
				}
				isInventory = org.apache.commons.lang.StringUtils.equals(tbspecid, specid) ||  org.apache.commons.lang.StringUtils.equals(tbskuid, skuid);
			}
			if(!isInventory) {
				result.add(t);
			}
		}
		return result;
	}
}



















