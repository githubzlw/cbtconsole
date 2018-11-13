package com.cbt.auto.service;

import com.cbt.auto.ctrl.AutoToSalePojo;
import com.cbt.auto.ctrl.OrderAutoBean;
import com.cbt.auto.ctrl.PureAutoPlanBean;
import com.cbt.auto.dao.OrderAutoDao;
import com.cbt.auto.dao.OrderAutoDaoImpl;
import com.cbt.bean.AdmDsitribution;
import com.cbt.bean.OrderAutoDetail;
import com.cbt.bean.OrderProductSource;
import com.cbt.pojo.GoodsDistribution;

import java.util.List;
import java.util.Map;

public class PreOrderAutoService implements IOrderAutoService {
	OrderAutoDao dao=new OrderAutoDaoImpl();

	@Override
	public List<OrderAutoBean> getAllOrderAutoInfo(String order) {
		
		return dao.getAllOrderAutoInfo(order);
	}
	
	@Override
	public Map<Integer, Integer> getAllBuyerCount() {
		
		return dao.getAllBuyerCount();
	}

	@Override
	public Map<Integer, Integer> getLessSaler() {
		return dao.getLessSaler();
	}

	@Override
	public int getGoodsDistributionCount(String carUrl) {
		
		return dao.getGoodsDistributionCount(carUrl);
	}
	
	@Override
	public List<String> getUnassignedOrders() {
		
		return dao.getUnassignedOrders();
	}
	
	public String getTwoCid(String goodsCatId){
		
		return dao.getTwoCid(goodsCatId);
	}
	
	@Override
	public Map<String, String> getAllOrderRefresh() {
		
		return dao.getAllOrderRefresh();
	}
	
	
	@Override
	/**
	 * 根据订单详情的goodscatid获取商品类别
	 * @param goodsCatId
	 * @return 商品类别
	 * @author 王宏杰
	 * @ 2016-09-23
	 */
	public String getAliCategoryType(String goodsCatId){
		
		return dao.getAliCategoryType(goodsCatId).trim();
	}

	@Override
	public int insertDG(GoodsDistribution gd) {
		
		return dao.insertDG(gd);
	}
	
	@Override
	public void inventoryLock(String orderNo) {
		
		dao.inventoryLock(orderNo);
	}

	@Override
	public void queryChangeLogToAotu() {
		dao.queryChangeLogToAotu();
	}

	@Override
	public String getPayPalAddress(String orderNo) {
		return dao.getPayPalAddress(orderNo);
	}

	@Override
	public int updateIsStockFlag() {
		return dao.updateIsStockFlag();
	}

	@Override
	public List<AdmDsitribution> getAdmuserType(String time) {
		
		return dao.getAdmuserType(time);
	}
	
	@Override
	public int insertBGP(OrderAutoBean o,String time) {
		
		return dao.insertBGP(o,time);
	}

	@Override
	public int insertPap(PureAutoPlanBean pap) {
		
		return dao.insertPap(pap);
	}

	@Override
	public List<AdmDsitribution> getALLAdmDsitribution() {
		
		return dao.getALLAdmDsitribution();
	}

	@Override
	public List<String> getAllAdmUser() {
		
		return dao.getAllAdmUser();
	}

	@Override
	public List<String> getPureSalesAdmUser() {
		return dao.getPureSalesAdmUser();
	}

	@Override
	public List<String> getPurePurchaseAdmUser() {
		return dao.getPurePurchaseAdmUser();
	}

	@Override
	public int getAdminId(String orderNo) {
		return dao.getAdminId(orderNo);
	}

	@Override
	public List<OrderAutoDetail> getOrderDetail(String goodid, String order) {
		
		return dao.getOrderDetail(goodid, order);
	}

	@Override
	public Map<Integer, Integer> getMoreGoodsPid(String orderNo) {
		return dao.getMoreGoodsPid(orderNo);
	}

	@Override
	public Map<Integer, Integer> getSevenLeastAdmuser() {
		return dao.getSevenLeastAdmuser();
	}

	@Override
	public int insertAdminUser(String orderNo, int adminid) {
		return dao.insertAdminUser(orderNo,adminid);
	}

	@Override
	public int getMaxId() {
		
		return dao.getMaxId();
	}

	@Override
	public List getAllOrderAuto(int maxId) {
		
		return dao.getAllOrderAuto(maxId);
	}
	
	@Override
	public List<OrderProductSource> getPreAutoSourceAddTime() {
		
		return dao.getPreAutoSourceAddTime();
	}

	@Override
	public List getAllocatedOrder() {
		
		return dao.getAllocatedOrder();
	}
	
	@Override
	/**
     * 根据还未分配的订单号查询商品个数
     * @param orderids
     * @return 商品数量
     * @author 王宏杰
     * @ 2016-09-26
     */
	public int getAllCount(String orderids){
		
		return dao.getAllCount(orderids);
	}
	
	public Map getAdmAutoCount(String time2){
		return dao.getAdmAutoCount(time2);
	}
	/**
	 * 自动分配销售
	 * @author  王宏杰
	 * 2016-10-10
	 */
	public List<AutoToSalePojo> getOrderAutoToSale(){
		return dao.getOrderAutoToSale();
	}
	
	public Map getBeforeNoComplete(){
		return dao.getBeforeNoComplete();
	}
	
	public Map getNoCompleteCount(){
		return dao.getNoCompleteCount();
	}
	
	@Override
	public int getExitGoodsPid(String goods_pid) {
		
		return dao.getExitGoodsPid(goods_pid);
	}
	
	@Override
	public int getNewColudBuyer(String pid) {
		
		return dao.getNewColudBuyer(pid);
	}
	
	@Override
	public Map<Integer, List<String>> getAllProcurementExpertise() {
		
		return dao.getAllProcurementExpertise();
	}
	
	@Override
	public Map<String,List<OrderAutoBean>> getItems(String orderid) {
		
		return dao.getItems(orderid);
	}
	
	@Override
	public String getFirdstCid(String id) {
		
		return dao.getFirdstCid(id);
	}
	
	@Override
	public AdmDsitribution getAdmuserNoComplete(String admuserid) {
		
		return dao.getAdmuserNoComplete(admuserid);
	}

}
