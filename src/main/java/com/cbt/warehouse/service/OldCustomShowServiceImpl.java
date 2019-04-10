package com.cbt.warehouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zefer.html.doc.o;

import com.cbt.bean.Orderinfo;
import com.cbt.pojo.Admuser;
import com.cbt.warehouse.dao.OldCustomShowMapper;
import com.cbt.warehouse.pojo.OldCustom;
@Service
public class OldCustomShowServiceImpl implements OldCustomShowService {
@Autowired
private OldCustomShowMapper oldCustomShowMapper;
	@Override
	public List<OldCustom> FindOldCustoms(String admName, String staTime,
			String enTime, String email, String id, String cuName, int page,
			int pagesize) {
		try {
			if("".equals(admName) || "-1".equals(admName)){
			admName=null;	
			}
			if("".equals(staTime) ){
				staTime=null;	
				}
			if("".equals(enTime) ){
				enTime=null;	
				}
			if("".equals(enTime) ){
				enTime=null;	
				}
			if("".equals(email) ){
				email=null;	
				}
			if("".equals(id) ){
				id=null;	
				}
			if("".equals(cuName) ){
				cuName=null;	
				}
			
			List<OldCustom> list=this.oldCustomShowMapper.FindOldCustoms(admName,staTime,enTime,email,id,cuName,page,pagesize);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	@Override
	public int getOldCustomCount(String admName, String staTime, String enTime,
			String email, String id, String cuName) {
		try {
			if("".equals(admName) ||"-1".equals(admName)){
				admName=null;	
				}
				if("".equals(staTime) ){
					staTime=null;	
					}
				if("".equals(enTime) ){
					enTime=null;	
					}
				if("".equals(enTime) ){
					enTime=null;	
					}
				if("".equals(email) ){
					email=null;	
					}
				if("".equals(id) ){
					id=null;	
					}
				if("".equals(cuName) ){
					cuName=null;	
					}
			int count=this.oldCustomShowMapper.getOldCustomCount(admName,staTime,enTime,email,id,cuName);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		
	}
	@Override
	public List<Admuser> FindAllAdm() {
		List<Admuser> list=this.oldCustomShowMapper.FindAllAdm();
		return list;
	}
	@Override
	public List<Orderinfo> FindOrderByUsid(String usid, int start, int pagesize,String order) {
		if ("".equals(order)) {
			order=null;
		}
		
		try {
			List<Orderinfo> list=this.oldCustomShowMapper.FindOrderByUsid(usid,start,pagesize,order);	
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public int getOrderCount(String usid,String order) {
		if ("".equals(order)) {
			order=null;
		}
		try {
			int count=this.oldCustomShowMapper.getOrderCount(usid,order);	
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		
	}
	

}
