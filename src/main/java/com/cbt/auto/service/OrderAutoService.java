package com.cbt.auto.service;//package com.cbt.auto.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.cbt.auto.ctrl.OrderAutoBean;
//import com.cbt.auto.ctrl.PureAutoPlanBean;
//import com.cbt.auto.dao.OrderAutoMapper;
//import com.cbt.bean.AdmDsitribution;
//import com.cbt.bean.OrderAutoDetail;
//import com.cbt.pojo.GoodsDistribution;
//@Service
//public class OrderAutoService implements IOrderAutoService{
//	@Autowired
//	private OrderAutoMapper dao;
//
//	@Override
//	public List getAllOrderAutoInfo(String order) {
//		
//		return dao.getALLOrderDetailCount(order);
//	}
//
//	@Override
//	public List getGoodsDistributionCount(String goodsId) {
//		
//		return dao.getGoodsDistributionCount(goodsId);
//	}
//
//	@Override
//	public int insertDG(GoodsDistribution gd) {
//		
//		return dao.insertDG(gd);
//	}
//	
//	public int insertPap(PureAutoPlanBean pap){
//		
//		return dao.insertPap(pap);
//	}
//
//	@Override
//	public List<AdmDsitribution> getALLAdmDsitribution() {
//		
//		return dao.getALLAdmDsitribution();
//	}
//
//	@Override
//	public List getAllAdmUser() {
//		
//		return dao.getAllAdmUser();
//	}
//
//	@Override
//	public List<OrderAutoDetail> getOrderDetail(String goodid,String order) {
//
//		return dao.getOrderDetail(goodid,order);
//	}
//	
//	public int getMaxId(){
//		
//		return dao.getMaxId();
//	}
//
//	public List<OrderAutoBean> getAllOrderAuto(int maxId){
//		
//		return dao.getAllOrderAuto(maxId);
//	}
//	
//	public List getAllocatedOrder(){
//		
//		return dao.getAllocatedOrder();
//	}
//
//}
