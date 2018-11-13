package com.cbt.parse.daoimp;

import com.cbt.parse.bean.GoodsDaoBean;

import java.util.ArrayList;

public interface IGoodsDao {
	
	/**添加一条商品信息数据
	 */
	public  int addSourceData(GoodsDaoBean bean);
	/**
	 * 根据goodsid查询替换商品图片
	 * @Title getImages 
	 * @Description TODO
	 * @param car_id
	 * @return
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getImages(String car_id);
	
	/**更新商品数据状态
	 * 
	 */
	public  int updatevalid(String valid, String url);


	/**查询商品数据
	 *
	 */
	public GoodsDaoBean queryData(String table, String url);

	/**查询商品数据
	 *
	 */
	public GoodsDaoBean queryPid(String pid, String cid);
	

}
