package com.cbt.warehouse.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import b.b.b.c.ab;

import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.dao.LookReturnOrderMapper;
import com.cbt.warehouse.pojo.ReturnRes;
import com.cbt.warehouse.pojo.Returndetails;
import com.cbt.warehouse.pojo.Returnresult;
@Service
public class LookReturnOrderServiceImpl implements LookReturnOrderService {
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
    @Autowired
    private LookReturnOrderMapper lookReturnOrderMapper;
	private List<Returndetails> list;
	@Override
	public List<ReturnRes> SelectReturnOrder(String admuserid, String returnState,
			String orderNum,int page,String mid) {
		if ("".equals(admuserid)) {
			admuserid=null;
		}
		if ("".equals(returnState)) {
			returnState=null;
		}
		if ("".equals(orderNum)) {
			orderNum=null;
		}
		List<ReturnRes> arr=new ArrayList<ReturnRes>();
		// TODO Auto-generated method stub
		List<Returndetails> returndetails=this.lookReturnOrderMapper.SelectReturnOrder(admuserid,returnState,orderNum,page,mid);
		if (returndetails.size()==0) {
			return null;
		}
		for (int i = 0; i < returndetails.size(); i++) {
			ReturnRes res=new ReturnRes();
			List<Returndetails>	list=null;
			if("退货申请".equals(returnState)){
			list=this.lookReturnOrderMapper.FindReturnOrderS(returndetails.get(i).getCusorder());	
			}else{
			list=this.lookReturnOrderMapper.FindReturnOrder(returndetails.get(i).getCusorder());
			}
			res.setCusorder(returndetails.get(i).getCusorder());
			res.setList(list);
			arr.add(res);
		}
		System.err.println("退货条数"+returndetails.size());
		/* for (Returndetails t : returndetails) {
			 System.err.println("采购来源"+t.getPurSou());
	            //当退货数量大于采购数量时，屏蔽整单退货按钮
	           if(Integer.valueOf(t.getReturnNum())>=Integer.valueOf(t.getPurNum())){
	                t.setOperating("<a title='点击查看该采购订单下的所有商品' target='_blank' href='/cbtconsole/website/refundSample.jsp?orderid="+t.getTborder()+"'>查看详情</a>");
	            }else{
	                t.setOperating("<a title='点击查看该采购订单下的所有商品' target='_blank' href='/cbtconsole/website/refundSample.jsp?orderid="+t.getTborder()+"'>查看详情</a>||<a href='javascript:openRefund(\""+t.getTborder()+"\");'>整单退货</a>");
	            }
	            t.setTborder("<a title='跳转到1688订单详情页面' href='https://trade.1688.com/order/new_step_order_detail.htm?orderId="+t.getTborder()+"' target='_blank'>"+t.getTborder()+"</a>");
	        }*/
		return arr;
	}
	@Override
	public Boolean UpdataReturnOrder(String order,String returnState,int sta,String returnOrder,String company) {
		// TODO Auto-generated method stub
		Boolean f;
		 Date date = new Date();
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String string= sdf.format(date);
		if (sta==1) {
			f= this.lookReturnOrderMapper.UpdataReturnTbOrder(order,returnState,returnOrder,company,string);	
		}else {
			f= this.lookReturnOrderMapper.UpdataReturnOrder(order,returnState);	
		}
		
		return f;
		
	}
	@Override
	public Boolean AddOrder(Returndetails returndetails) {
		Boolean bo;
		bo=this.lookReturnOrderMapper.AddOrder(returndetails);
		return bo;
	}
	@Override
	public ReturnRes FindReturnOrder(String order) {
		List<Returndetails> res=new ArrayList<Returndetails>();
		Returndetails returndetail=new Returndetails();
		Returndetails re=new Returndetails();
		List<Returnresult> rets=this.lookReturnOrderMapper.FindCusOrder(order);
		System.err.println("长度为"+rets.size());
		if (rets.size()==0) {
			return null;
		}
		ReturnRes returnRes=new ReturnRes();
		for(int i=0;i<rets.size();i++){
			if (rets.get(i).getTborder()==null) {
				continue;
			}
		Returndetails returndetails =this.lookReturnOrderMapper.FindReturndetails(rets.get(i).getTborder());
		System.err.println("订单"+rets.get(i).getTborder());
		returndetails.setWarehouse(rets.get(i).getWarehouse());
		System.err.println("仓库"+rets.get(i).getWarehouse());
		returndetails.setOrdeerPeo(rets.get(i).getOrdeerPeo());
		System.err.println("采购"+rets.get(i).getOrdeerPeo());
		returndetails.setOrderSale(rets.get(i).getOrderSale());
		System.err.println("销售"+rets.get(i).getOrderSale());
		returndetails.setCusorder(order);
		res.add(returndetails);
		}
		returnRes.setCusorder(order);
		returnRes.setList(res);
		return returnRes;
	}
	@Override
	public Boolean RemReturnOrder(String order,int sta) {
		Boolean f;
		if (sta==1) {
			 f= this.lookReturnOrderMapper.RemReturnTbOrder(order);
		}else {
			 f= this.lookReturnOrderMapper.RemReturnOrder(order);	
		}
		
		return f;
		
	}
	
	@Override
	public Boolean AddReturntbOrder(String cusorder, String returnApply,
			String returnReason, Double returnMoney, String tborder,
			String ordeerPeo, String warehouse) {
		int s=this.lookReturnOrderMapper.findTborder(tborder);
		if (s>0) {
		return false;	
		}
		Returndetails returndetails =this.lookReturnOrderMapper.FindReturndetails(tborder);
		returndetails.setCusorder(cusorder);
		returndetails.setOrdeerPeo(ordeerPeo);
		returndetails.setReturnApply(returnApply);
		returndetails.setReturnMoney(returnMoney);
		if ("2".equals(returnReason)) {
			returnReason="次品退货";
		}
		if ("3".equals(returnReason)) {
			returnReason="客户退单";
		}
		returndetails.setReturnReason(returnReason);
		returndetails.setTborder(tborder);
		returndetails.setWarehouse(warehouse);
		returndetails.setReturnState("退货申请");
		Boolean fBoolean=this.AddOrder(returndetails);
		return fBoolean;
	}
	@Override
	public int countPage(String admuserid, String state, String orderNum,
			int page, String mid) {
			if ("".equals(admuserid)) {
				admuserid=null;
			}
			if ("".equals(state)) {
				state=null;
			}
			if ("".equals(orderNum)) {
				orderNum=null;
			}
			int totalpage=this.lookReturnOrderMapper.countPage(admuserid,state,orderNum,page,mid);
		return totalpage;
	}}
