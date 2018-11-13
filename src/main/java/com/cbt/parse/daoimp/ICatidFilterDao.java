package com.cbt.parse.daoimp;

import com.cbt.parse.bean.CatidFilterBean;

import java.util.ArrayList;


public interface ICatidFilterDao {
	
	public int add(CatidFilterBean bean);
	
	public ArrayList<CatidFilterBean> query(String catid, int type);


	/**查询类别cid，得到类别过滤（type=1）
	 * @param catid
	 * @return
	 */
	public String queryCidFilter(String catid);

	/**查询类别cid，得到默认重量（type=3）
	 * @param catid
	 * @return
	 */
	public String queryWeightFilter(String catid);

	/**查询类别cid，得到最小价格设置（type=4）
	 * @param catid
	 * @return
	 */
	public String queryPriceFilter(String catid);


	/**查询类别cid，关键词keyword 过滤指定关键词进入1688数据搜索（type=5）
	 * @param catid
	 * @return
	 */
	public int queryKeyWordsFilter(String catid, String keyword);

	/**查询类别cid，得到最小销量设置（type=6）
	 * @param catid
	 * @return
	 */
	public int querySoldFilter(String catid);

	/**指定类别
	 * @param catid
	 * @return
	 */
	public int getCatidFilter(String catid, int type);
	
	
}
