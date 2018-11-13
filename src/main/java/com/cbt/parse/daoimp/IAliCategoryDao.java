package com.cbt.parse.daoimp;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author ylm
 * 获取商品类别
 */
public interface IAliCategoryDao {
	
	/**
	 * 获取商品分类
	 * 
	 * @param user
	 * 	用户信息
	 */
	public String getAliCategory(int aid, String type);
	
	/**通过catname 以及父类cid 获取cid
	 * @param catname
	 * @param cid_parent
	 * @return
	 */
	public String getCid(String catname, String cid_parent);
	
	/**通过cid获取类别名称
	 * @param cid
	 * @return
	 */
	public String getCname(String cid);
	
	
	/**通过cid 列出所有的信息
	 * @param cid
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getAllCategory(String cid);
	
}
