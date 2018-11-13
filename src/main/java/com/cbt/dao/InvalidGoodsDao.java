package com.cbt.dao;

import com.cbt.bean.IntensveBean;
import com.cbt.bean.InvalidUrlBean;

import java.util.List;

public interface InvalidGoodsDao {
	
	/**屏蔽商品链接
	 * @date 2016年11月28日
	 * @author abc
	 * @param goodsUuid uuid  
	 * @param goodsPid 产品id
	 * @return  
	 */
	public int addFilterGoodsUrl(String goodsUuid, String goodsPid);
	/**屏蔽商品链接
	 * @date 2016年11月28日
	 * @author abc
	 * @param goodsUuid uuid
	 * @param goodsPid 产品id
	 * @return
	 */
	public int addFilterStoreUrl(String storeUrl, String storeId);


		/**产品屏蔽
		 * @date 2016年11月28日
		 * @author abc
		 * @param gurl
		 * @return
		 */
		public int updateGoodsUrl(String gurl, String goodsPid);

		/**屏蔽列表
		 * @date 2016年12月5日
		 * @author abc
		 * @param page
		 * @return
		 */
		public List<InvalidUrlBean> getInvalidUrls(int page, int type);
		
		/**屏蔽列表
		 * @date 2016年12月5日
		 * @author abc
		 * @param page
		 * @return  
		 */
		public List<IntensveBean> getIntensve();
}
