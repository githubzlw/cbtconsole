package com.cbt.website.server;

import com.cbt.bean.CodeMaster;
import com.cbt.bean.OrderDatailsNew;
import com.cbt.bean.OrderProductSource;
import com.cbt.common.StringUtils;
import com.cbt.pojo.StraightHairPojo;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.report.dao.TaoBaoOrderMapper;
import com.cbt.warehouse.pojo.PreferentialPrice;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.*;
import com.cbt.website.dao2.Page;
import com.cbt.website.dao2.PurchaseDao;
import com.cbt.website.dao2.PurchaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseServerImpl implements PurchaseServer {

	private PurchaseDao phDao = new PurchaseDaoImpl();
	@Autowired
	private TaoBaoOrderMapper taoBaoOrderMapper;
	@Override
	public List<AdminUserBean> getAllAdmUser() {
		
		return phDao.getAllAdmUser();
	}

	public List<OrderProductSource> getAllGoodsids(int adminid) {
		return phDao.getAllGoodsids(adminid);
	};
	
	@Override
	public List<PurchasesBean> getStockOrderInfo(String pagenum, String orderid, String admid, String userid, String orderno,
                                                 String goodid, String date, String days, String state, int unpaid, int pagesize, String orderid_no_array,
                                                 String goodsid, String goodname) {
		int startindex = Integer.parseInt(orderid);
		int user;
		if (userid == "" || userid == null) {
			user = 111111111;
		} else {
			user = Integer.parseInt(userid);
		}
		int good;
		if (goodid == "" || goodid == null) {
			good = 111111111;
		} else {
			good = Integer.parseInt(goodid);
		}
		int day;
		if (days == "" || days == null) {
			day = 111111111;
		} else {
			day = Integer.parseInt(days);
		}
		int stat;
		if (state == "" || state == null) {
			stat = 11111111;
		} else {
			stat = Integer.parseInt(state);
		}
		return phDao.getPurchaseByXXX(startindex, pagesize, 0, user, orderno, good, date, day, stat,
				unpaid, orderid_no_array, goodsid, goodname,"","0");
	}

	@Override
	public Page findPageByCondition(String pagenum, String orderid, String admid, String userid, String orderno,
			String goodid, String date, String days, String state, int unpaid, int pagesize, String orderid_no_array,
			String goodsid, String goodname,String orderarrs,String search_state) {
		
		int startindex = Integer.parseInt(pagenum);
		if (startindex > 0) {
			startindex = (startindex - 1) * pagesize;
		}else{
			startindex=0;
		}
		int admin=StringUtils.isStrNull(admid)?111111111:Integer.parseInt(admid);
		int user=StringUtils.isStrNull(userid)?111111111:Integer.parseInt(userid);
		int good=StringUtils.isStrNull(goodid)?111111111:Integer.parseInt(goodid);
		int day=StringUtils.isStrNull(days)?111111111:Integer.parseInt(days);
		int stat=StringUtils.isStrNull(state)?11111111:Integer.parseInt(state);
		Page page = new Page();
		List<PurchasesBean> list=phDao.getPurchaseByXXX(startindex, pagesize, admin, user, orderno, good, date, day, stat,
				unpaid, orderid_no_array, goodsid, goodname,orderarrs,search_state);
		Set<String> pid_list=new HashSet<String>();
		for(int i=0;i<list.size();i++){
			pid_list.add(list.get(i).getGoods_pid());
		}
		double pid_amount=0;
		if(list.size()>0){
			pid_amount=pid_list.size()*5;
		}
		page.setPid_amount(pid_amount);
		page.setRecords(list);
		page.setTotalrecords("5201314".equals(goodid) || "5201315".equals(goodid)?list.size():phDao.splitPage());
		//标记该订单销售发送消息已读
		phDao.updateGoodsCommunicationInfo(orderno,admin);
		int pagenumm = Integer.parseInt(pagenum);
		page.setPagenum(pagenumm);
		int totalpage = phDao.splitPage() % pagesize == 0 ? phDao.splitPage() / pagesize: (phDao.splitPage() / pagesize + 1);
		page.setTotalpage("5201314".equals(goodid) || "5201315".equals(goodid)?1:totalpage);
		Map<Object,Object> map =new HashMap<Object,Object>();
		List<StraightHairPojo> list_stra= phDao.StraightHairList();
		for (StraightHairPojo s : list_stra) {
			if(StringUtil.isNotBlank(s.getStates()) && Integer.valueOf(s.getStates())>0){
				s.setStates("<span style='color:green'>已签收</span>");
				map.put("orderid",s.getOrderid());
				map.put("goodsid",s.getGoodsid());
				phDao.updateState(s.getOrderid(),s.getGoodsid());
			}
		}
		return page;
	}

	@Override
	public void AddRmark(String orderNo, int goodsdata, String remark, int goodid) {
		
		phDao.AddRmark(orderNo, goodsdata, remark, goodid);
	}

	@Override
	public String getOtherSources(String orderNo, int goodid, String goods_url) {
		
		return phDao.getOtherSources(orderNo, goodid, goods_url);
	}

	@Override
	public String getAddressByOrderID(String orderNo) {
		
		return phDao.getAddressByOrderID(orderNo);
	}

	@Override
	public void OutPortNow(String orderno, String wuliuNumber, String transport, String userid) {
		
		phDao.OutPortNow(orderno, wuliuNumber, transport, userid);
	}

	@Override
	public void AddRecource(String type, int admid, int userid, int goodsdataid, String goods_url, String googs_img,
			double goodsprice, String goods_title, int googsnumber, String orderNo, int od_id, int goodid, double price,
			String resource, int buycount, String resaon, String currency, String pname, String cGoodstypee,
			String issuree,String shop_id,String state_flag,String straight_address) {
		
		phDao.AddRecource(type, admid, userid, goodsdataid, goods_url, googs_img, goodsprice, goods_title, googsnumber,
				orderNo, od_id, goodid, price, resource, buycount, resaon, currency, pname, cGoodstypee, issuree,shop_id,state_flag,straight_address);
	}

	@Override
	public void AddNoGS(String goods_url, String goods_type, String userid) {
		phDao.AddNoGS(goods_url, goods_type, userid);
	}

	@Override
	public List<outIdBean> findOutId(int uid, String ordernolist) {
		
		return phDao.findOutId(uid, ordernolist);
	}

	@Override
	public String getEmail(int userid) {
		
		return phDao.getEmail(userid);
	}

	@Override
	public int PurchaseComfirmOne(String orderNo, int goodsid, int adminid) {
		
		return phDao.PurchaseComfirmOne(orderNo, goodsid, adminid);
	}

	@Override
	public int PurchaseComfirmTwo(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
			String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
			String newValue, int purchaseCount) {
		
		return phDao.PurchaseComfirmTwo(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl, googsimg,
				goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);
		// return phDao.purchaseConfirmation("1",admid,orderNo,goodid);
	}

	@Override
	public List<PurchaseBean> getOutByID(int userid, String str) {
		
		return phDao.getOutByID(userid, str);
	}

	@Override
	public List<outIdBean> getOutNowId() {
		
		return phDao.getOutNowId();
	}

	@Override
	public List<outIdBean> getOutNowIdTwo() {
		
		return phDao.getOutNowIdTwo();
	}

	@Override
	public String getUserbyID(String admid) {
		
		return phDao.getUserbyID(admid);
	}
	
	@Override
	public int getBuyId(String orderid, String goodsid) {
		
		return phDao.getBuyId(orderid,goodsid);
	}

	@Override
	public UserOrderDetails getUserDetails(String orderno) {
		
		return phDao.getUserDetails(orderno);
	}

	@Override
	public int findOrderService(String ordernolist) {
		
		return phDao.findOrderService(ordernolist);
	}

	@Override
	public int YiRuKu(String url, String orderno, int goodid) {
		
		return phDao.YiRuKu(url, orderno, goodid);
	}

	@Override
	public void ProblemGoods(String url, String orderno, int goodid) {
		
		phDao.ProblemGoods(url, orderno, goodid);
	}

	@Override
	public OrderProductSource ShowRmark(String orderNo, int goodadaid, int goodid) {
		
		return phDao.ShowRmark(orderNo, goodadaid, goodid);
	}

	@Override
	public String getYFHTurnOrder(String yfhOrder) {
		
		return phDao.getYFHTurnOrder(yfhOrder);
	}

	@Override
	public List<CodeMaster> getCodeMaster() {
		
		return phDao.getCodeMaster();
	}

	@Override
	public OrderPayDetails getDetailsByOrder(String adminid, String uid, String order) {
		
		return phDao.getDetailsByOrder(adminid, uid, order);
	}

	@Override
	public List<CountryCodeBean> getCountryCode() {
		
		return phDao.getCountryCode();
	}

	@Override
	public List<ProductCodeBean> getProductCode() {
		
		return phDao.getProductCode();
	}

	@Override
	public int saveOrderFee(OrderFeeDetails ofd) {
		
		return phDao.saveOrderFee(ofd);
	}

	@Override
	public List<outIdBean> findOutIdTwo(int id) {
		
		return phDao.findOutIdTwo(id);
	}

	@Override
	public String getOriginalGoodsUrl(String orderno, int goodsid) {
		
		return phDao.getOriginalGoodsUrl(orderno, goodsid);
	}
	
	@Override
	public String allQxcgQrNew(String orderNo,int id) {
		
		return phDao.allQxcgQrNew(orderNo,id);
	}

	// 货源确认
	@Override
	public int PurchaseComfirmTwoHyqr(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
			String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
			String newValue, int purchaseCount, String child_order_no, String isDropshipOrder) {

		return phDao.PurchaseComfirmTwoHyqr(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl, googsimg,
				goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount, child_order_no,
				isDropshipOrder);
	}
	
	@Override
	public String allQxQrNew(String orderNo,int id) {
		
		return phDao.allQxQrNew(orderNo,id);
	}
	
	@Override
	public String allQrNew(String orderNo,int id) {
		
		return phDao.allQrNew(orderNo,id);
	}
	
	@Override
	public String allcgqrQrNew(String orderNo,int id) {
		
		return phDao.allcgqrQrNew(orderNo,id);
	}

	@Override
	public int PurchaseComfirmOneQxhy(String orderNo, int odid, int adminid) {
		
		// PurchaseComfirmOneQxhy
		return phDao.PurchaseComfirmOneQxhy(orderNo, odid, adminid);
	}
	
	@Override
	/**
	 * 获取采购商品的批量优惠价格
	 * @param orderid
	 * @param goodsid
	 * @return
	 * @author whj
	 */
	public List<PreferentialPrice> queryPreferentialPrice(String orderid, int goodsid, String goods_p_url){
		
		return phDao.queryPreferentialPrice(orderid,goodsid,goods_p_url);
	}
	
	/**
	 * 添加采购商品的批量优惠价格
	 * @param map
	 * @return
	 * @author whj
	 */
	@Override
	public int addPreferentialPrice(Map<Object, Object> map) {
		
		return phDao.addPreferentialPrice(map);
	}
	
	/**
	 * 修改采购商品的批量优惠价格
	 * @param map
	 * @return 影响的行数
	 */
	public int updatePreferentialPrice(Map<Object,Object> map){
		
		return phDao.updatePreferentialPrice(map);
	}

	// 查询订单商品
	@Override
	public List<OrderDatailsNew> getOrderdataelsNew(String order) {
		
		return phDao.getOrderdataelsNew(order);
	}

	@Override
	public int PurchaseComfirmTwoHyqr127(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
			String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
			String newValue, int purchaseCount) {
		
		return phDao.PurchaseComfirmTwoHyqr127(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl, googsimg,
				goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);
	}

	@Override
	public int PurchaseComfirmTwo127(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
			String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
			String newValue, int purchaseCount) {
		
		return phDao.PurchaseComfirmTwo127(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl, googsimg,
				goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);
	}

	@Override
	public int YiRuKu127(String url, String orderno, int goodid) {
		
		return phDao.YiRuKu127(url, orderno, goodid);
	}

	@Override
	public void AddRecource127(String type, int admid, int userid, int goodsdataid, String goods_url, String googs_img,
			double goodsprice, String goods_title, int googsnumber, String orderNo, int od_id, int goodid, double price,
			String resource, int buycount, String reason, String currency, String pname) {

		phDao.AddRecource127(type, admid, userid, goodsdataid, goods_url, googs_img, goodsprice, goods_title,
				googsnumber, orderNo, od_id, goodid, price, resource, buycount, reason, currency, pname);

	}

	@Override
	public void OutPortNow127(String orderno, String wuliuNumber, String transport, String userid) {
		
		phDao.OutPortNow127(orderno, wuliuNumber, transport, userid);
	}

	@Override
	public int PurchaseComfirmOne127(String orderNo, int goodsid, int adminid) {
		
		return phDao.PurchaseComfirmOne127(orderNo, goodsid, adminid);
	}

	@Override
	public int purchaseConfirmation(String string, int admid, String orderNo, int goodid) {
		
		return phDao.purchaseConfirmation(string, admid, orderNo, goodid);
	}

	@Override
	public int PurchaseComfirmTwo_crossshop(int userid, String orderNo, int od_id, int goodid, int goodsdataid,
			int admid, String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber,
			String oldValue, String newValue, int purchaseCount) {
		
		return phDao.PurchaseComfirmTwo_crossshop(userid, orderNo, od_id, goodid, goodsdataid, admid, goodsurl,
				googsimg, goodsprice, goodstitle, googsnumber, oldValue, newValue, purchaseCount);
	}

	@Override
	public int insertSources(Map<String, String> map) {
		
		return phDao.insertSources(map);
	}
	/**
	 * 申请线下采购付款
	 * @param map
	 * @return
	 */
	@Override
	public int OfflinePaymentApplication(Map<String, String> map){
		
		return phDao.OfflinePaymentApplication(map);
	}
	@Override
	public int determineStraighthair(Map<Object, Object> map) {
		
		return phDao.determineStraighthair(map);
	}
	/**
	 * 采购是否使用库存
	 */
	@Override
	public int useInventory(Map<String, String> map) {
		
		return phDao.useInventory(map);
	}
	/**
	 * 本链接采样信息录入
	 */
	@Override
	public int insertDateToAliInfoData(String od_id,String name) {
		
		return phDao.insertDateToAliInfoData(od_id,name);
	}
	/**
	 * 保存商品沟通备注信息
	 * @param map  订单号、商品号、备注信息
	 * @author 王宏杰  2017-07-26
	 */
	@Override
	public String saveRepalyContent(Map<String, String> map) {
		
		return phDao.saveRepalyContent(map);
	}
	
	@Override
	public String getUserName(String name) {
		
		return phDao.getUserName(name);
	}

	@Override
	public void notePurchaseAgain(String orderNo, int od_id, int goodsid, String remarkContent) {
		phDao.notePurchaseAgain(orderNo, od_id, goodsid, remarkContent);
	}

	@Override
	public UserOrderDetails getUserAddr(String orderid) throws Exception {
		
		return phDao.getUserAddr(orderid);
	}

}
