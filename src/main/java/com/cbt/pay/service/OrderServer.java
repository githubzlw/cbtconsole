package com.cbt.pay.service;

import com.cbt.bean.Address;
import com.cbt.bean.Eightcatergory;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.OrderDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServer implements IOrderServer {
 
	IOrderDao dao = new OrderDao();
	/*@Override
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage) {
		List<OrderDetailsBean> list = dao.getOrders(userID, state, (startpage-1)*8,8);
		return list;
	}*/
	 
	@Override
	public void add(List<OrderDetailsBean> orderdetails) {
		// TODO Auto-generated method stub
		dao.addOrderDetail(orderdetails);
	}
	public List<OrderDetailsBean>getOrder(int userid, String itemid){
		return dao.getOrderDetail(userid, itemid);
	}
	@Override
	public int addAddress(Address add) {
		// TODO Auto-generated method stub
		return dao.addAddress(add);
	}
	@Override
	public List<Address> getUserAddr(int userid) {
		// TODO Auto-generated method stub
		return dao.getUserAddr(userid);
	}
	@Override
	public int existUserAddr(int userid) {
		// TODO Auto-generated method stub
		return dao.existUserAddr(userid);
	}
	 
	@Override
	public void addOrderInfo(List<OrderBean> oblist,int addressid,int odcount) {
		// TODO Auto-generated method stub
		dao.addOrderInfo(oblist,addressid,odcount);
		//保存混批折扣记录
		OrderBean ob = oblist.get(0);
		if(ob.getDiscount_amount() > 0){
			dao.saveOrder_discount(ob.getOrderNo(), 1, ob.getDiscount_amount(), "");
		}
	}
	@Override
	public List<OrderBean> getOrderInfo(int userid, String order_no) {
		// TODO Auto-generated method stub
		return dao.getOrderInfo(userid, order_no);
	}
	@Override
	public void updateOrderState(int userid, String orderid,String pay_price_three) {
		// TODO Auto-generated method stub
		dao.updateOrderState(userid, orderid,pay_price_three);
	}
	@Override
	public int updateGoodscarState(int userid, String itemid) {
		// TODO Auto-generated method stub
	   return dao.updateGoodscarState(userid, itemid);
	}
	@Override
	public int updateGoodscarState(String itemid){
		// TODO Auto-generated method stub
		   return dao.updateGoodscarState(itemid);
		}
	@Override
	public void updateUserAddress( int id, String address,
			String country, String phonenumber, String zipcode,String address2,String statename, String recipients, String street) {
		// TODO Auto-generated method stub
		dao.updateUserAddress(id, address, country, phonenumber, zipcode, address2,statename, recipients, street);
	}
	@Override
	public List<Eightcatergory> getHomefurnitureProduct(String catergory) {
		// TODO Auto-generated method stub
		return dao.getHomefurnitureProduct(catergory);
	}
	 
	 
	@Override
	public int updateIndvidualAddress(int userid, int id, String address,
			String country, String phonenumber, String zipcode,String address2) {
		int res = 0;
		if(id == 0){
			Address add = new Address();
			add.setUserid(userid);
			add.setAddress(address);
			add.setCountry(country);
			add.setZip_code(zipcode);
			add.setPhone_number(phonenumber);
			dao.addAddress(add);
		}else{
			res = dao.updateUserAddress(id, address, country, phonenumber, zipcode,address2,"California", null, null);
		}
		return res;
	}

	@Override
	public void updateGoodscarStateAgain(int userid, String itemid) {
		// TODO Auto-generated method stub
		dao.updateGoodscarStateAgain(userid, itemid);
	}
	@Override
	public float getTotalPrice(String goodsids) {
		return dao.getTotalPrice(goodsids);
	}

	@Override
	public void delUserAddressByid(int id) {
		// TODO Auto-generated method stub
		dao.delUserAddressByid(id);
	}

	@Override
	public void setDefault(int id, int userid) {
		// TODO Auto-generated method stub
		dao.setDefault(id, userid);
	}

	@Override
	public Address getUserAddrById(int id) {
		// TODO Auto-generated method stub
		return dao.getUserAddrById(id);
	}
	
	public int getAddressCountByUserId(int userid){
		return dao.getAddressCountByUserId(userid);
	}

	@Override
	public void updateOrderPayPrice(int userid, String order_no,
			String pay_price, String ipnAddressJson) {
		// TODO Auto-generated method stub
		dao.updateOrderPayPrice(userid, order_no, pay_price, ipnAddressJson, -1, -1, 0);
	}

	@Override
	public int addOrderAddress(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.addOrderAddress(map);
	}
	@Override
	public int addOrderAddress(List<Map<String,Object>> maps) {
		// TODO Auto-generated method stub
		return dao.addOrderAddress(maps);
	}
	@Override
	public void updateOrderStatePayPrice(int userid, String orderid,
			String pay_price_three, String pryprice, String ipnAddressJson,double order_ac) {
		dao.updateOrderStatePayPrice(userid, orderid, pay_price_three, pryprice, ipnAddressJson,order_ac);
	}
	@Override
	public void updateOrderStatePayPrice(int userid, List<String[]> orderInfo, String ipnAddressJson) {
		dao.updateOrderStatePayPrice(userid, orderInfo, ipnAddressJson);
	}
	@Override
	public int saveOrder_discount(String orderno, int discounttype,
			double price, String discountinfo) {
		return dao.saveOrder_discount(orderno, discounttype, price, discountinfo);
	}

	@Override
	public int upOrderExpress(String orderno, String mode_transport,
			String actual_ffreight) {
		return dao.upOrderExpress(orderno, mode_transport, actual_ffreight);
	}

	@Override
	public int upOrderExpress(String orderno, String mode_transport,
			String actual_ffreight, String remaining_price, String pay_price_tow, String service_fee, double pay_price) {
		// TODO Auto-generated method stub
		return dao.upOrderExpress(orderno, mode_transport, actual_ffreight,remaining_price,pay_price_tow,service_fee, pay_price);
	}

	@Override
	public int upOrderService_fee(String orderno, String service_fee) {
		// TODO Auto-generated method stub
		return dao.upOrderService_fee(orderno, service_fee);
	}

	@Override
	public String initCheckData(String orderNos) {
		return dao.initCheckData(orderNos);
	}
	
	
}
