package com.cbt.orderinfo.service;

import com.cbt.bean.*;
import com.cbt.common.StringUtils;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.orderinfo.dao.OrderinfoMapper;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.report.service.TabTransitFreightinfoUniteNewExample;
import com.cbt.track.dao.TabTrackInfoMapping;
import com.cbt.util.DoubleUtil;
import com.cbt.util.Util;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.IWarehouseDao;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.website.bean.*;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderinfoService implements IOrderinfoService {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderinfoService.class);
	@Autowired
	private OrderinfoMapper dao;
	@Autowired
	private IWarehouseDao iWarehouseDao;

	@Autowired
	private IPurchaseMapper pruchaseMapper;
	
	@Autowired
	private TabTrackInfoMapping tabTrackInfoMapping;

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderinfoService.class);

	@Override
	public List<Map<String, String>> getOrders(int userID, int state,
	                                           Date startdate, Date enddate, String email, String orderno,
	                                           int startpage, int page, int admuserid, int buyid, int showUnpaid,String type,int status) {
		return dao.getOrders(userID, state, startdate, enddate, email, orderno, (startpage-1)*40, 40, admuserid, buyid, showUnpaid,type,status);
	}




	@Override
	public String getProblem(String orderid) {
		return dao.getProblem(orderid);
	}

	@Override
	public int insertOrderinfoCache(List<Map<String, String>> list) {
		return 0;
	}

	@Override
	public List<OrderBean> getFlushOrderFreightOrder() {
		return dao.getFlushOrderFreightOrder();
	}

	@Override
	public List<OrderBean> getAllOrderInfo() {
		return dao.getAllOrderInfo();
	}


	@Override
	public int deleteFlagByOrder(String orderNo) {
		return dao.deleteFlagByOrder(orderNo);
	}

	@Override
	public int insertFlagByOrderid(String orderNo) {
		return dao.insertFlagByOrderid(orderNo);
	}

	@Override
	public List<OrderBean> getFlushOrderFreightOrderCancel() {
		return dao.getFlushOrderFreightOrderCancel();
	}

	@Override
	public int getAllOrderinfoFreight() {
		return dao.getAllOrderinfoFreight();
	}

	@Override
	public List<OrderBean> getAllNoFreight() {
		return dao.getAllNoFreight();
	}

	@Override
	public String getOrderNo() {
		return dao.getOrderNo();
	}

	@Override
	public String getExchangeRate() {
		String rate=dao.getExchangeRate();
		if(StringUtil.isBlank(rate)){
			rate="6.3";
		}
		return rate;
	}

	@Override
	public String getCountryById(String country, String userid) {
		String coun=dao.getCountryById(country);
		if(StringUtil.isBlank(coun)){
			coun=dao.getOtherCountry(userid);
		}
		return coun;
	}

	@Override
	public int addOrderInfo(List<OrderBean> OrderBean, int address) {
		return dao.addOrderInfo(OrderBean,address);
	}

	@Override
	public int addOrderDetail(List<OrderDetailsBean> orderdetails) {
		return dao.addOrderDetail(orderdetails);
	}

	@Override
	public int addOrderAddress(Map<String, String> addressMap) {
		return dao.addOrderAddress(addressMap);
	}

	@Override
	public UserBean getUserFromIdForCheck(int userId) {
		return dao.getUserFromIdForCheck(userId);
	}

	@Override
	public int addPayment(PaymentBean payment) {
		int row=dao.getPayMentCount(payment);
		if(row>0){
			row=dao.updatePayMent(payment);
		}else{
			row=dao.insertPayMent(payment);
		}
		return row;
	}

	@Override
	public List<Address> getUserAddr(int userId) {
		List<Address> address = new ArrayList<Address>();
		List<Address> list=dao.getUserAddr(userId);
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
		return dao.addPaymentNote(userid,orderno,dealMan,upfile);
	}

	@Override
	public int countPaymentInvoiceByorderuser(String userid, String orderno) {
		return dao.countPaymentInvoiceByorderuser(userid,orderno);
	}

	@Override
	public int updatePaymentNote(String userid, String orderno, String dealMan,String upfile) {
		return dao.updatePaymentNote(userid,orderno,dealMan,upfile);
	}

	@Override
	public List<OrderDetailsBean> getCatidDetails(String orderid) {
		return dao.getCatidDetails(orderid);
	}

	@Override
	public List<TabTransitFreightinfoUniteNew> selectByExample(TabTransitFreightinfoUniteNewExample example) {
		return dao.selectByExample(example);
	}

	@Override
	public List<Map<String,String>> allTrack(Map<String, String> map) {
		int adminid=dao.getAdmNameByShipno(map);
		List<Map<String,String>> list=dao.getOrderData(map.get("shipno"),adminid);
		return list;
	}

	@Override
	public List<OrderDetailsBean> getAllCancelDetails(Map<String, String> map) {
		return dao.getAllCancelDetails(map);
	}

	@Override
	public int updateGoodStatus(Map<String, String> map) {
		int row=0;
		String remark = "";
		int old_itemqty=0;
		String sql="";
		try{
			SendMQ sendMQ = new SendMQ();
			//查询入库备注
			Map<String,String> reMap=dao.getRemarkInspetion(map);
			if(reMap !=null){
				remark = reMap.get("warehouse_remark") + map.get("warehouseRemark");
				old_itemqty=Integer.valueOf(reMap.get("itemqty"));
			}else{
				remark = map.get("warehouseRemark");
			}
			old_itemqty= Integer.parseInt(map.get("count"))+old_itemqty;
			map.put("old_itemqty",String.valueOf(old_itemqty));
			map.put("remark",remark);
//			row=dao.updateIdrelationtable(map);
			//入库操作
			String username=map.get("userName");
			String positon="";
			if("Sherry".equals(username)){
				positon=dao.getPositionByBarcode(map.get("barcode"),"1");
			}else{
				positon=dao.getPositionByBarcode(map.get("barcode"),"2");
			}
			int idd=dao.getIdRelationtable(map);
			map.put("positon",positon);
			if("0".equals(map.get("repState"))){
				map.put("old_itemqty","0");
				dao.insertIdRelationtable(map);
			}else if(idd<=0){
				dao.insertIdRelationtable(map);
			}else{
				dao.updateIdRationtable(map);
			}
			if("1".equals(map.get("status"))){
				//点击到库时根据spec_id关联抓取订单的id
				String typeName=dao.getTypeNameByOdid(map);
				//查询入库对应的淘宝订单
				List<TaoBaoOrderInfo> tList=dao.getTaobaoInfoByOrderid(map);
				int tbId=0;
				if(StringUtil.isNotBlank(typeName) && typeName.contains("&gt;")){
					String [] types=typeName.split("&gt;");
					String sku1=types[0];
					String sku2=types[1];
					tbId=UtilAll.getTbId(tList,sku1,sku2);
				}else if(StringUtil.isNotBlank(typeName)){
					tbId=UtilAll.getTbId(tList,typeName,"");
				}
				map.put("tbId",String.valueOf(tbId));
				//更新订单详情表状态为已经到仓库
				dao.updateState(map);
				if("0".equals(map.get("repState"))){
					//如果是补货则更新order_replenishment的状态为1已完成
					dao.updateOrderReState(map);
				}else{
					//更新采购表状态为已到库
					dao.updateBuyState(map);
				}
			}else{
				//更新采购表状态为问题货源
				dao.updateOrderSourceState(map);
			}
			//等于1为到库操作，其余为验货有疑问
			if(Integer.valueOf(map.get("status")) != 1){
				return row;
			}
			sql="update storage_problem_order set flag=1 where shipno='"+map.get("shipno")+"'";
			dao.updateStorageProblemOrder(map);
			//查询是否是DP订单
			int isDropshipOrder=dao.queyIsDropshipOrder(map);
			//查询客户ID
			//int userId = dao.queryUserIdByOrderNo(map.get("orderid"));
			//获取未更新之前，订单状态和客户ID，比较前后状态是否一致，不一致说明订单状态已经修改
			Map<String,Object> orderinfoMap = pruchaseMapper.queryUserIdAndStateByOrderNo(map.get("orderid"));
			if (isDropshipOrder == 1) {
				dao.updateOrderDetails(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
				int counts=dao.getDtailsState(map);
				if(counts == 0){
					dao.updateDropshiporder(map);
					sendMQ.sendMsg(new RunSqlModel("update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"+map.get("orderid")+"' " +
							"and id='"+map.get("odid")+"')"));
				}
				//判断主单下所有的子单是否到库
				counts=dao.getAllChildOrderState(map);
				if(counts == 0){
					dao.updateOrderInfoState(map);
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
					//判断订单状态是否一致
					if(!orderinfoMap.get("old_state").toString().equals("2")){
						//发送消息给客户
						NotifyToCustomerUtil.updateOrderState(Integer.valueOf(orderinfoMap.get("user_id").toString()),
								map.get("orderid"),Integer.valueOf(orderinfoMap.get("old_state").toString()),2);
					}
				}
			}else{
				// 非dropshi订单
				dao.updateOrderDetails(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'"));
				//判断订单是否全部到库
				int counts=dao.getDetailsState(map);
				if(counts == 0){
					dao.updateOrderInfoState(map);
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+map.get("orderid")+"'"));
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
			Map<String,String> inMap=dao.queryData(map);
			if(inMap != null){
				map.put("goods_pid",inMap.get("goods_pid"));
				dao.insertGoodsInventory(map);
			}
			LOG.info("--------------------结束记录商品入库--------------------");
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return 1;
	}


	@Override
	public int insertScanLog(String shipno, String admName) {
		return dao.insertScanLog(shipno,admName);
	}

	@Override
	public List<Tb1688OrderHistory> getGoodsData(String shipno) {
		List<Tb1688OrderHistory> list=dao.getGoodsData(shipno);
		for(Tb1688OrderHistory t:list){
			t.setImgurl(t.getImgurl().replace(".80x80","").replace(".60x60",""));
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
			String admin=dao.getAdminid(shipno);
			if(StringUtil.isNotBlank(admin)){
				adminid=Integer.valueOf(admin);
			}
			if ("1".equals(checked)) {
				resultList=dao.getOrderDataOne(shipno);
			}else{
				resultList=dao.getOrderData(shipno,adminid);
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
				String check="";
				String address =String.valueOf(map.get("address"));
				searchresultinfo.setStrcar_type(changetypeName(car_type));
				searchresultinfo.setIsExitPhone(String.valueOf(map.get("isExitPhone")));
				searchresultinfo.setTaobao_itemid(resultTaobaoItemId);
				//获取商品的店铺名称
				String shop_id="0000";
				String goods_pid=String.valueOf(map.get("goods_pid"));
				if(goods_pid.equals(String.valueOf(map.get("tb_1688_itemid")))){
					//采购货源和推荐货源一致
					shop_id=dao.getShopId(goods_pid);
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
				}
				//采购是否该商品授权  1已授权
				String authorized_flag=String.valueOf(map.get("aFlag"));
				if("0".equals(authorized_flag)){
					check="采购已对该商品授权";
				}else if("2".equals(authorized_flag)){
					//查询上一次该商品发货的订单信息
					check=iWarehouseDao.getBatckInfo(goods_pid);
				}
				check=StringUtil.isBlank(check)?"-":check;
				searchresultinfo.setAuthorizedFlag(check);
				searchresultinfo.setShop_id(shop_id);
				searchresultinfo.setOdid(String.valueOf(map.get("odid")));
                // 2018/11/06 11:39 ly 实秤重量 是否已同步到产品库
                SearchResultInfo weightAndSyn = iWarehouseDao.getGoodsWeight(map.get("goods_pid"));
                if (null != weightAndSyn){
                    searchresultinfo.setWeight(weightAndSyn.getWeight());
                    searchresultinfo.setSyn(weightAndSyn.getSyn());
                }
				//获取验货商品的最大类别ID 王宏杰
				String catid="";
				if("1".equals(checked)){
					String goodscatid=map.get("goodscatid");
					catid=dao.getCatid(goodscatid);
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
				String fileByOrderid = dao.getFileByOrderid(orderid);
				//拼接备注信息
				StringBuffer sbf = getStringForRemark(map, searchresultinfo, orderid, fileByOrderid);
				searchresultinfo.setRemark(sbf.toString());
				sbf.setLength(0);
				searchresultinfo.setImgList(taobaoinfoList);
				List<Object[]> orderremark = new ArrayList<Object[]>();
				List<Map<String,String>> remarkList=dao.getOrderRemark(orderid);
				for(Map<String,String> reMap:remarkList){
					Object[] objects = {reMap.get("orderid"), reMap.get("orderremark"), reMap.get("remarkuserid"),reMap.get("createtime")};
					orderremark.add(objects);
				}
				searchresultinfo.setOdid(String.valueOf(map.get("odid")));
				searchresultinfo.setOrderremark(orderremark);
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
		searchresultinfo.setGoods_price(Double.parseDouble(String.valueOf(map.get("goods_price"))));
		searchresultinfo.setGoods_p_price(Double.parseDouble(String.valueOf(map.get("goods_p_price"))));
		searchresultinfo.setPurchase_state(Integer.valueOf(String.valueOf(map.get("purchase_state"))));
		searchresultinfo.setUsecount(Integer.valueOf(String.valueOf(map.get("usecount"))));
		searchresultinfo.setBuycount(Integer.valueOf(String.valueOf(map.get("buycount"))));
	}

	@Override
	public int changeBuyer(Map<String, String> map) {
		//判断该商品是否分配过采购
		int row =dao.queryGoodsDis(map);
		if(row>0){
			row=dao.updateGoodsDis(map);
		}
		return row;
	}

	@Override
	public int updatecanceltatus(Map<String, String> map) {
		int row=0;
		try{
			SendMQ sendMQ = new SendMQ();
			// 直接删除 标识记为已经删除
			row=dao.updateIdrelationtableFlag(map);
			int counts=dao.getInCount(map);
			if(counts == 0){
				row=dao.udpateStorage(map);
			}
			row=dao.updateDetailsShipno(map);
			row=dao.updateOrderState(map);
			if ("0".equals(map.get("repState"))) {
				row=dao.updateOrderRe(map);
			}else{
				row=dao.updateOrderSource(map);
			}
			// 更新线上状态
			sendMQ.sendMsg(new RunSqlModel("UPDATE order_details t SET t.state = 0 WHERE t.orderid = '"+map.get("orderid")+"' AND t.id = '"+map.get("odid")+"'"));
			sendMQ.sendMsg(new RunSqlModel("UPDATE orderinfo t SET t.state = 1 WHERE t.order_no = '"+map.get("orderid")+"'"));
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int orderReturn(Map<String,String> map) {
		return dao.orderReturn(map);
	}

	@Override
	public int addUser(Map<String, String> map) {
		map.put("admName",dao.getAdmName(map));
		int row=0;
		try{
			SendMQ sendMQ = new SendMQ();
			row=dao.queryAdmin(map);
			if(row>0){
                //更新商业询盘中分配的数据
			    dao.updateBusiess(map);
                //更新销售人
                row=dao.updateAdminUser(map);
				if(row>0){
					sendMQ.sendMsg(new RunSqlModel("update admin_r_user set adminid="+map.get("adminid")+",admName='"+map.get("admName")+"' where userid="+map.get("userid")+""));
				}
			}else{
				//新增销售记录
				row=dao.insertAdminUser(map);
				if(row>0){
					sendMQ.sendMsg(new RunSqlModel("insert into admin_r_user(userid,username,useremail,adminid,createdate,admName) " +
							"values('"+map.get("userid")+"','"+map.get("email")+"','"+map.get("email")+"','"+map.get("adminid")+"',now(),'"+map.get("admName")+"')"));
				}
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int checkOrderState(String orderid) {
		return dao.checkOrderState(orderid);
	}

	@Override
	public int updatecancelChecktatus(Map<String, String> map) {
		int row=0;
		int old_itemqty=0;
		double weights=0.00;
		int cancelCount=0;
		try{
			Map<String,String> inMap=dao.getInspetionMap(map);
			cancelCount=Integer.valueOf(map.get("cance_inventory_count"));
			if(inMap != null){
				old_itemqty=Integer.valueOf(String.valueOf(inMap.get("itemqty")));
				weights=Double.parseDouble(String.valueOf(inMap.get("weight")))-Double.parseDouble(StringUtil.isBlank(String.valueOf(map.get("weight")))?"0.00":String.valueOf(map.get("weight")));
				int count_=old_itemqty-Integer.valueOf(String.valueOf(map.get("count")));
				if(count_<0){
					count_=0;
				}
				map.put("weight",String.valueOf(weights));
				map.put("count",String.valueOf(count_));
				dao.updateRelationTable(map);
			}
			row=dao.updateDetails(map);
			//如果该商品验货是有录入库存则做想应的减少
			Map<String,String> inventoryMap=dao.getInventoryMap(map);
			if(inventoryMap != null){
				String sku=String.valueOf(inventoryMap.get("car_type"));
				String car_urlMD5=String.valueOf(inventoryMap.get("car_urlMD5"));
				String goods_pid=String.valueOf(inventoryMap.get("goods_pid"));
				String goods_p_price=String.valueOf(inventoryMap.get("goods_p_price"));
				//销售数量
				int yourorder=Integer.valueOf(String.valueOf(inventoryMap.get("yourorder")));
				//库存减少数量
				int inventory_count=cancelCount;
				map.put("sku",StringUtils.isStrNull(sku)?"":sku.trim());
				map.put("car_urlMD5",car_urlMD5);
				map.put("goods_pid",goods_pid);
				if(inventory_count>0){
					//库存减少
					Inventory in=dao.getInventoryInfo(map);
					if(in != null && in.getFlag()==1){
						double amount=in.getNew_inventory_amount()-(inventory_count/Integer.valueOf(map.get("seiUnit")))*Double.valueOf(goods_p_price);
						map.put("amount",String.valueOf(amount));
						map.put("new_remaining",String.valueOf(Integer.valueOf(in.getNew_remaining())-inventory_count<0?0:(Integer.valueOf(in.getNew_remaining())-inventory_count)));
						map.put("can_remaining",String.valueOf(Integer.valueOf(in.getCan_remaining())-inventory_count<0?0:(Integer.valueOf(in.getCan_remaining())-inventory_count)));
						dao.updateInventory(map);
					}else if(in != null && in.getFlag()==0){
						//未盘点
						map.put("new_remaining",String.valueOf(Integer.valueOf(in.getRemaining())-inventory_count<0?0:(Integer.valueOf(in.getRemaining())-inventory_count)));
						map.put("can_remaining",String.valueOf(Integer.valueOf(in.getCan_remaining())-inventory_count<0?0:(Integer.valueOf(in.getCan_remaining())-inventory_count)));
						double amount=in.getInventory_amount()-(inventory_count/Integer.valueOf(String.valueOf(map.get("seiUnit"))))*Double.valueOf(goods_p_price);
						map.put("amount",String.valueOf(amount));
						dao.updateInventoryFlag(map);
					}
				}
				//删除验货时的记录storage_outbound_details
				dao.updateUutboundDetails(map);
			}
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
			SendMQ sendMQ=new SendMQ();
			Double weights=Double.parseDouble(map.get("weight"));
			row=dao.updateChecked(map);
			row=dao.updateSourceState(map);
			Map<String,String> inMap=dao.getInspetionMap(map);
			if(inMap != null){
				remark = inMap.get("warehouse_remark") + map.get("warehouseRemark");
				weights+=Double.parseDouble(String.valueOf(inMap.get("weight")));
			}else{
				remark =String.valueOf(map.get("warehouseRemark"));
			}
			map.put("warehouseRemark",remark);
			map.put("weight",String.valueOf(weights));
			row=dao.updateRelationTable(map);
			String orderid=String.valueOf(map.get("orderid"));
			String goodsid=String.valueOf(map.get("goodid"));
			//判断是否为dp订单
			int isDropshipOrder=dao.queyIsDropshipOrder(map);
			if (isDropshipOrder == 1) {
				dao.updateOrderDetails(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+orderid+"' and id='"+map.get("odid")+"'"));
				int counts=dao.getDtailsState(map);
				if(counts == 0){
					dao.updateDropshiporder(map);
					sendMQ.sendMsg(new RunSqlModel("update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"+orderid+"' " +
							"and id='"+map.get("odid")+"')"));
				}
				//判断主单下所有的子单是否到库
				counts=dao.getAllChildOrderState(map);
				if(counts == 0){
					dao.updateOrderInfoState(map);
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+orderid+"'"));
				}
			}else{
				// 非dropshi订单
				dao.updateOrderDetails(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set state=1 where orderid='"+orderid+"' and id='"+map.get("odid")+"'"));
				//判断订单是否全部到库
				int counts=dao.getDetailsState(map);
				if(counts == 0){
					dao.updateOrderInfoState(map);
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+orderid+"'"));
				}
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public List<com.cbt.pojo.Admuser> getAllBuyer() {
		return dao.getAllBuyer();
	}

	@Override
	public List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo) {
		Map<String, ArrayList<Object[]>> changInfo = new HashMap<String, ArrayList<Object[]>>();
		List<OrderChange> ocList=dao.getOrderChange(orderNo);
		List<OrderDetailsBean> list=dao.getChildrenOrdersDetails(orderNo);
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
		if (changInfo.get("order" + odb.getGoodsid()) != null) {
			List<Object[]> changeInfoList = changInfo.get("order" + odb.getGoodsid());
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
		return dao.getAllFreightByOrderid(orderNo);
	}

	@Override
	public OrderBean getChildrenOrders(String orderNo) {
		OrderBean ob=dao.getChildrenOrders(orderNo);
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
	public List<Map<String, String>> getOrderManagementQuery(int userID, int state, String startdate, String enddate, String email, String orderno, int startpage, int page, int admuserid, int buyid,
	                                                         int showUnpaid, String type, int status, String paymentid) {
		long start=System.currentTimeMillis();
		UserDao udao = new UserDaoImpl();
		List<Map<String, String>> list=dao.getOrderManagementQuery(userID,state,StringUtils.isStrNull(startdate)?"":startdate,StringUtils.isStrNull(enddate)?"":enddate,StringUtils.isStrNull(email)?"":email, StringUtils.isStrNull(orderno)?"":orderno,startpage,page,admuserid,buyid,showUnpaid,StringUtils.isStrNull(type)?"":type,status,paymentid);
		for(Map<String, String> map:list){
			String paytype=map.get("paytypes");
			String tp="支付类型错误";
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
			if(StringUtils.isEmpty(ipnaddress)){
				ipnaddress = udao.getIpnaddress(orderInfo.getOrderNo()).get("ipnaddress");
			}
			String zCountry=map.get("zCountry");
			String ordertype=String.valueOf(map.get("ordertype"));
			if((StringUtil.isBlank(odCode) || StringUtil.isBlank(ipnaddress) || !ipnaddress.equals(odCode)) && tp.indexOf("paypal")>-1){
				logger.warn("简称国家不一致： odCode={},ipnaddress={}", odCode,ipnaddress);
				Map<String,String> aMap = udao.getIpnaddress(orderInfo.getOrderNo());
				String addressCountry=aMap.get("address_country");
				if("3".equals(ordertype)){
					//B2B订单
					addressFlag="3";
				}else if(StringUtil.isBlank(addressCountry)){
					//没有支付信息，
					addressFlag="2";
				}else if(StringUtil.isNotBlank(zCountry) && StringUtil.isNotBlank(addressCountry) && zCountry.equals(addressCountry)){
					//两边都有编码但不一致
					addressFlag="1";
				}else{
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
			map.put("estimatefreight",String.valueOf(Double.parseDouble(String.valueOf(map.get("estimatefreight")))*Double.parseDouble(exchange_rate)));
		}
		logger.info("订单管理查询总时间:"+(System.currentTimeMillis()-start));
		return list;
	}

	@Override
	public List<ShippingBean> getShipPackmentInfo(String orderNo) {
		List<ShippingBean> sb=dao.getShipPackmentInfo(orderNo);
		return sb;
	}

	@Override
	public Double getEstimatefreight(String orderNo) {
		double estimatefreight=0.00;
		Object o=dao.getEstimatefreight(orderNo);
		if(o != null){
			estimatefreight=(Double)o;
		}
		return estimatefreight;
	}

	@Override
	public double getAllWeight(String orderNo) {
		return dao.getAllWeight(orderNo);
	}

	@Override
	public String getFileByOrderid(String orderNo) {
		return dao.getFileByOrderid(orderNo);
	}

	@Override
	public int cancelOrder(String orderid) {
		int row=0;
		try{
			SendMQ sendMQ=new SendMQ();
			row=dao.cancelOrder(orderid);
			sendMQ.sendMsg(new RunSqlModel("update orderinfo set state=-1 where order_no='"+orderid+"'"));
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public int cancelPayment(String pid) {
		int row=0;
		try{
			SendMQ sendMQ=new SendMQ();
			row=dao.cancelPayment(pid);
			sendMQ.sendMsg(new RunSqlModel("update payment set paystatus=0  where id='"+pid+"'"));
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public List<AutoOrderBean> getOrderList(String orderid, String userid, String page) {
		List<AutoOrderBean> list = new ArrayList<AutoOrderBean>();
		List<AutoOrderBean> dataList=dao.getOrderList(orderid,userid,page);
		int count=dao.getCounts();
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
		TabTransitFreightinfoUniteOur tf=dao.getFreightInfo(countryNameCn,isEub);
		return tf;
	}

	@Override
	public int updateFreight(String orderNo, String freight) {
		return dao.updateFreight(orderNo,freight);
	}

	@Override
	public double getFreightFee(String allFreight,OrderBean orderInfo) {
		double esFreight=0.00;
		String modeTransport =orderInfo.getMode_transport();
		//dao.getModeTransport(orderInfo.getOrderNo());
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
		List<OrderDetailsBean> odList=dao.getCatidDetails(orderInfo.getOrderNo());
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
		return dao.updateFreightForOrder(orderNo,freight,esprice);
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
		List<TabTransitFreightinfoUniteNew> list = dao.selectByExample(example);
		if(list.size()<=0){
			TabTransitFreightinfoUniteNewExample example1 = new TabTransitFreightinfoUniteNewExample();
			TabTransitFreightinfoUniteNewExample.Criteria criteria1 = example1.createCriteria();
			criteria1.andCountryidEqualTo(29);
			criteria1.andTransportModeEqualTo(shippingMethod);
			list = dao.selectByExample(example1);
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
		return dao.getBuyerAndAll();
	}

	@Override
	public OrderBean getOrders(String orderNo) {
		OrderBean ob=dao.getOrder(orderNo);
		if(ob != null){
			if (ob.getIsDropshipOrder() == 1) {
				//dropship订单
				String dropShipList = dao.getDropshipOrderNoList(orderNo);
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
			int backList=iWarehouseDao.getBackList(ob.getUserEmail());
			int payBackList=0;
			if(StringUtil.isNotBlank(ob.getPayUserName())){
				payBackList=iWarehouseDao.getPayBackList(ob.getPayUserName());
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
			String dzconfirmtime =ob.getDzConfirmtime();
			ob.setDzConfirmtime(Utility.getStringIsNull(dzconfirmtime)? dzconfirmtime.substring(0, dzconfirmtime.indexOf(" ")) : "");
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
			}else if(grade==7){
				gradeName="Silver VIP";
			}else if(grade==10){
				gradeName="Gold VIP";
			}else if(grade==14){
				gradeName="Platinum VIP";
			}else if(grade==18){
				gradeName="Diamond VIP";
			}
			ob.setGradeName(gradeName);
		}
		return ob;
	}

	@Override
	public Forwarder getForwarder(String orderNo) {
		String newOrderNo = orderNo + ",";
		Forwarder forword=dao.getForwarder(newOrderNo);
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
			forword=dao.getForwarder(orderNo);
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
		return dao.getOrdersPays(orderNo,flag);
	}

	@Override
	public List<CodeMaster> getLogisticsInfo() {
		return dao.getLogisticsInfo();
	}

	@Override
	public List<String> getOrderNos(int userId,String orderNo) {
		return dao.getOrderNos(userId,orderNo);
	}

	@Override
	public double getAcPayPrice(String orderNo) {
		return dao.getAcPayPrice(orderNo);
	}

	@Override
	public String getModeTransport(String orderNo) {
		return dao.getModeTransport(orderNo);
	}

	@Override
	public List<EmailReceive1> getall(String orderNo) {
		List<EmailReceive1> list=dao.getall(orderNo);
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
		return dao.getEvaluate(orderNo);
	}

	@Override
	public TaoBaoOrderInfo getShipStatusInfo(String tb1688Itemid, String lastTb1688Itemid, String confirmTime, String admName, String shipno, int offlinePurchase, String orderId, int goodsId) {
		TaoBaoOrderInfo t=new TaoBaoOrderInfo();
		if(offlinePurchase == 1){
			t=dao.getShipStatusInfoFor(tb1688Itemid,lastTb1688Itemid,confirmTime,admName,shipno,offlinePurchase,orderId,goodsId);
		}else{
			t=dao.getOrderReplenishment(tb1688Itemid,lastTb1688Itemid,confirmTime,admName,shipno,offlinePurchase,orderId,goodsId);
			if(t!=null){
				t=dao.getShipStatusInfo(t.getTb_1688_itemid(),t.getCreatetime(),admName);
			}else{
				t=dao.getShipStatusInfos(tb1688Itemid,lastTb1688Itemid,confirmTime,admName);
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
		return dao.queryBuyCount(admuserId);
	}

	@Override
	public List<String> getOrderIdList() {
		return dao.getOrderIdList();
	}

	@Override
	public int updateGoodsCarMessage(String orderNo) {
		return dao.updateGoodsCarMessage(orderNo);
	}

	@Override
	public List<OrderDetailsBean> getOrdersDetails(String orderNo) {
		DecimalFormat df = new DecimalFormat("######0.00");
		Map<String, ArrayList<Object[]>> changInfo = new HashMap<String, ArrayList<Object[]>>();
		List<OrderChange> ocList=dao.getOrderChange(orderNo);
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
		List<OrderDetailsBean> list=dao.getOrdersDetails(orderNo);
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
			String useInventory=pruchaseMapper.getUseInventory(odb.getOid());
			if(StringUtil.isNotBlank(useInventory)){
				inventoryRemark="该商品使用了【"+useInventory+"】件库存";
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
		return dao.getUserEmailByOrderNo(orderNo);
	}

	@Override
	public int updateOrderinfoUpdateState(String orderNo) {
		return dao.updateOrderinfoUpdateState(orderNo);
	}

	@Override
	public int checkRecord(String orderNo) {
		return dao.checkRecord(orderNo);
	}

	@Override
	public int insertEmailRecord(String orderNo) {
		return dao.insertEmailRecord(orderNo);
	}

	@Override
	public List<Map<String, String>> getorderPending() {
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		String orderIds=dao.getOrderIds();
		if(StringUtil.isNotBlank(orderIds)){
			list=dao.getorderPending(orderIds);
			for(Map<String, String> map:list){
				String tp="支付错误";
				String paystatus="";
				String orderNo=map.get("order_no");
				Payment p=dao.getPayTypeForOrderNo(orderNo);
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
		return dao.getOrders1(userID, state, startdate, enddate, email, orderno, (startpage-1)*40, 40, admuserid, buyid, showUnpaid,type,status);
	}
	@Override
	public int getOrdersCount(int userID, int state, String startdate,
	                          String enddate, String email, String orderno, int admuserid,
	                          int buyid, int showUnpaid,String type,int status) {
		return dao.getOrdersCount(userID, state, startdate, enddate, email, orderno, admuserid, buyid, showUnpaid,type,status);
	}

	@Override
	public List<Map<String, Integer>> getOrdersState(int admuserid) {
		List<Map<String, Integer>> result = dao.getOrdersState(admuserid);
		//国际物流预警中预警条数
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, - 90);
		String startDate = DATEFORMAT.format(ca.getTime()) + " 00:00";
		Integer waring0 = tabTrackInfoMapping.getWarningRecordCount(startDate, "", 0, null);
		Map<String, Integer> cywMap = new HashMap<String, Integer>();
		cywMap.put("state", 1);
		cywMap.put("counts", waring0);
		result.add(cywMap);
		return result;
	}
	
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<ConfirmUserInfo> getAllSalesAndBuyer() {
		return dao.getAllSalesAndBuyer();
	}

	@Override
	public int queryUserIdByOrderNo(String orderNo) {
		return dao.queryUserIdByOrderNo(orderNo);
	}
	@Override
	public List<Orderinfo> getAllOrderShippingMehtodIsNull() {
		return dao.getAllOrderShippingMehtodIsNull();
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
		return dao.updateOrderinfomodeTransport(modeTransport,orderNo);
	}
}



















