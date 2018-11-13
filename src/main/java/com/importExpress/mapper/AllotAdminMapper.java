package com.importExpress.mapper;

/**
 * @author 王宏杰
 *自动分配销售人员
 *2016-10-08
 */
public interface AllotAdminMapper {


	
	/**
	 * 该用户是否已绑定销售人员
	 * 
	 */
	public int exAdmin(int userid, String useremail);

	/**
	 * 该用户是否已绑定销售人员
	 *
	 */
	public String seAdmin(int id);
	/**
	 * 插入一天所分配的销售人员
	 *
	 */
	public int addAdminSaler(int adminid);

//	/**
//	 * 自动分配销售人员
//	 *
//	 */
//	public int allotAdminSaler(int userid,String useremail,int adminid);
//
	/**
	 * 获取今天最少分配的销售人员
	 *
	 */
	public Integer getAllotAdmin();

	public Integer getAdmuserid(String admName);

	/**
	 * 专业引流进来的追溯7天以内的数据，针对订单：如果用户有多个引流，则分配到7天内的第一个引流销售中;其他：分配到最近一个引流
	 * @param type 0-其他，1-订单生成
	 */
	public Integer getAllotAdminPage(int userid, String sessionid, int type);


}
