package com.importExpress.service;

import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.importExpress.mapper.AllotAdminMapper;
import com.importExpress.mapper.AllotAdminMapperImpl;

/**
 * @author 王宏杰
 *自动分配销售人员
 *2016-10-08
 */
public class AllotAdminServiceImpl implements AllotAdminService {



	//private AllotAdminMapper allotAdminMapper;
	
	@Override
	public void allotAdminSaler(int type, int userId, String sessionId, String email,String orderAdmin,String username) {
		AllotAdminMapper allotAdminMapper=new AllotAdminMapperImpl();
	//1.如果是订单分配，小于100没有商品或订单留言的默认不分配
		//查询该用户是否已分配销售负责人
		try{
			int res = allotAdminMapper.exAdmin(userId, email);
			System.out.println("res=="+res);
			if(res > 0){
				return;
			}
		//2.专业引流进来的追溯7天以内的数据，针对订单：如果用户有多个引流，则分配到7天内的第一个引流销售中;其他：该商品如果属于专业引流进来则分配到对应销售，否则按规则三进行
			Integer adminid = allotAdminMapper.getAllotAdminPage(userId, sessionId, type);
			
			if(orderAdmin!=null && !orderAdmin.equals("")){
				adminid=allotAdminMapper.getAdmuserid(orderAdmin);
			}
			
		//3.按1天内销售人员分配的数量(数量中包括引流分配的)，优先分配给数量少的
			if(adminid == null ||  adminid == 0){
				adminid = allotAdminMapper.getAllotAdmin();
			}
			System.out.println("分配的销售人："+adminid);
			//res = allotAdminMapper.allotAdminSaler(userId,email,adminid);
			if(adminid>0){
				UserDao dao = new UserDaoImpl();
				 String admName=allotAdminMapper.seAdmin(adminid.intValue());
				 res=dao.updateAdminuser(userId, adminid, "", email, username, admName);
				if(res > 0){
					System.out.println("--------------分配成功--------------------userid:"+userId+"\t：admName："+admName);
					allotAdminMapper.addAdminSaler(adminid);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}



}
