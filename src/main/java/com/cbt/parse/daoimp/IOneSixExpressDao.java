package com.cbt.parse.daoimp;

import com.cbt.parse.bean.OneSixExpressBean;
import com.cbt.parse.bean.SqlBean;

import java.util.ArrayList;

public interface IOneSixExpressDao {
	
	/**添加数据
	 * @param bean
	 * @return
	 */
	public int add(OneSixExpressBean bean);
	/**更新数据
	 * @param bean
	 * @return
	 */
	public int update(OneSixExpressBean bean);
	
	/**更新数据valid值
	 * @param bean
	 * @return
	 */
	public int updateValid(String url, int valid);

	/**查询某一条url数据是否存在
	 * @param url
	 * @return
	 */
	public int queryExsis(String url);

	/**搜索数据集合
	 * @param bean
	 * @return
	 */
	public ArrayList<OneSixExpressBean> querySearch(ArrayList<SqlBean> bean);

	/**后台货源数据集合
	 * @param bean
	 * @return
	 */
	public ArrayList<OneSixExpressBean> querySearchWbsite(ArrayList<SqlBean> bean);

	/**商店数据集合
	 * @param sid
	 * @param page
	 * @return
	 */
	public ArrayList<OneSixExpressBean> queryStore(String sid, int page);

	/**单条商品数据
	 * @param url
	 * @param pid
	 * @return
	 */
	public OneSixExpressBean queryGoods(String url, String pid);

	/**分类
	 * @param bean
	 * @return
	 */
	public ArrayList<OneSixExpressBean> queryCate(ArrayList<SqlBean> bean, int caid_index);
	/**获取父类id
	 * @param catid
	 * @return
	 */
	public OneSixExpressBean queryCateParent(String catid);

	/**获取数据表搜有集合
	 * @return
	 */
	public ArrayList<OneSixExpressBean> getAllData(int valid);
	/**
	 * @param id1
	 * @param id2
	 * @return
	 */
	public ArrayList<OneSixExpressBean> queryImg(int id1, int id2);

}
