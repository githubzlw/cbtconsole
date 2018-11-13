package com.cbt.parse.daoimp;

import com.cbt.bean.Eightcatergory;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICatergoryDao {
	
	/**添加一条数据信息
	 *
	 */
	public  int addCatergory(int id, String catergory, String productname,
                             int minorder, String unit, float price, String url, String imgurl, int valid);

	/**更新一条数据信息(更新url是否有效)
	 *
	 */
	public  int updateValid(String url, int valid);
	
	
	/**查询数据，符合valid状态为1的url
	 * 
	 */
	public ArrayList<Eightcatergory> queryData();
	
	/**查询数据库所有数据
	 * 
	 */
	public ArrayList<HashMap<String, String>> query(); 
}
