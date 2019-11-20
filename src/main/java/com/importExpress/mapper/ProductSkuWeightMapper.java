package com.importExpress.mapper;

import com.importExpress.pojo.ProductSkuWeight;

public interface ProductSkuWeightMapper {
	
	/**新增产品重量
	 * @param wprap
	 * @return
	 */
	int insertWeight(ProductSkuWeight wprap);
	
	/**更新产品重量
	 * @param wprap
	 * @return
	 */
	int updateWeight(ProductSkuWeight wprap);
	

}
