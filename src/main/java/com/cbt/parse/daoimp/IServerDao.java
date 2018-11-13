package com.cbt.parse.daoimp;

import java.util.ArrayList;
import java.util.HashMap;


public interface IServerDao {
	
	/**更新代理服务器的error内容
	 * 
	 */
	public  int addError(String ip, String port, String status, String error);

	/**更新代理服务器的status状态
	 *
	 */
	public  int updateStatus(String ip, String status, String error);

	/**更新代理服务器的valid状态
	 *
	 */
	public  int updateValid(String ip, int valid);

	/**添加数据
	 *
	 */
	public  int addServer(String ip, String port, String status, int valid, String error);
	
	/**
	 * 删除数据
	 */
	public int deleteServer(String ip);
	
	/**查询数据，符合status状态为“active”的代理服务器
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryData(); 
	
	/**查询数据库所有数据
	 * @return
	 */
	public ArrayList<HashMap<String, String>> query(); 
	
	/**查询数据库，符合valid状态为“1”（即代理服务器有效）
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryValid();
	

}
