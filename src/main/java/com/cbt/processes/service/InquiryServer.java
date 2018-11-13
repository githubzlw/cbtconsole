package com.cbt.processes.service;

import com.cbt.bean.InquiryDetail;
import com.cbt.processes.dao.IInquiry;
import com.cbt.processes.dao.ISpiderDao;
import com.cbt.processes.dao.InquiryDao;
import com.cbt.processes.dao.SpiderDao;
import com.cbt.website.dao.IMessageDao;
import com.cbt.website.dao.MessageDao;

import java.text.SimpleDateFormat;
import java.util.List;

public class InquiryServer implements IInquiryServer {

	IInquiry dao = new InquiryDao();
	
	@Override
	public int saveInquiry(int userid, String payno,double server_price,int pay_state, String[] goodsid) {
		ISpiderDao isdao = new SpiderDao();
		//查询是否已添加
		List<String> goodse = dao.getInquiryExist(goodsid);
		//添加询价表
		int inquiryid = 0 ;
		if(goodse.size()>0){
			inquiryid = dao.saveInquiry(userid, payno, server_price, pay_state);
			//添加询价详情表
			if(inquiryid>0){
			    dao.saveInquiryDetail(goodse, payno);
			    //修改购物车状态，改成2(询价中),已询问到价格后改成0
			    isdao.upGoogs_car_state(goodse, 2);
			}
		}
		
		
		return inquiryid ;
	}

	@Override
	public int upInquiryDetail(int userid,int id, int state, double price) {
		int res = dao.upInquiryDetail(id, state, price);
		IMessageDao messageDao = new MessageDao();
		messageDao.addMessage(userid, "询价信息有更新内容", id+"");
		return res;
	}

	@Override
	public List<InquiryDetail> getInquiry(int state, int userid) {
		List<InquiryDetail> inquiryDetails = dao.getInquiry(state, userid);
		return inquiryDetails;
	}
 
	@Override
	public List<InquiryDetail> getInquiry(String inqId) {
		List<InquiryDetail> inquiryDetails = dao.getInquiry(inqId);
		return inquiryDetails;
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmssms");//150304104422
		//System.out.println(sdf.format(new Date()));
	}

	@Override
	public int upInquiryPay(String payno, int state) {
		return dao.upInquiryPay(payno, state);
	}

	@Override
	public int delInquiry(String payno) {
		return dao.delInquiry(payno);
	}
}
