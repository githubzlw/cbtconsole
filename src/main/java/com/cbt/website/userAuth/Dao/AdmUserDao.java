package com.cbt.website.userAuth.Dao;

import com.cbt.website.userAuth.bean.Admuser;

import java.util.List;

public interface AdmUserDao {

	/**
	 * 根据用户名和密码获取管理员信息
	 * 
	 * @param name
	 * @param pass
	 * @return
	 */
	public Admuser getAdmUser(String name, String pass) throws Exception;
	public Admuser getAdmUserMd5(String name, String pass) throws Exception;
	
	public Admuser getAdmUserById(int id) throws Exception;

	/**
	 * 获取除自己外所有的管理员
	 * 
	 * @param admName
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllAdmuser(String admName) throws Exception;

	public Admuser getAdmUserFromUser(int userid) throws Exception;

	/**
	 * 获取所有用户信息
	 * 
	 * @return
	 */
	public List<Admuser> queryForList() throws Exception;
	/**
	 * 获取所有用户信息
	 * 
	 * @return
	 */
	public Admuser queryForListByName(String name);

	/**
	 * 根据角色查询用户信息
	 * 
	 * @param roleType
	 * @return
	 */
	public List<Admuser> queryByRoleType(int roleType) throws Exception;

	/**
	 * 根据orderNo查询销售信息
	 * 
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public List<Admuser> queryByOrderNo(String orderNo) throws Exception;
	
	/**
	 * 根据orderid,goodsid获取采购人员
	 * @param orderid
	 * @param goodsid
	 * @return 采购人员ID
	 * @author 王宏杰 2017-07-27
	 */
	public int queryByBuyerOrderNo(String orderid, String goodsid);

	/**
	 * 查询订单和商品id对应的采购人员
	 * @param orderNo
	 * @param goodid
	 * @return
	 * @throws Exception
	 */
	public Admuser queryByOrderNoAndGoodid(String orderNo, int goodid) throws Exception;
	/**
	 * 查询订单和商品id对应的销售人员
	 * @param orderNo
	 * @param goodid
	 * @return
	 * @throws Exception
	 */
	public Admuser querySalesByOrderNoAndGoodid(String orderNo, int goodid) throws Exception;

}
