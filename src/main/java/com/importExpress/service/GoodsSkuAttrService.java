package com.importExpress.service;

import java.util.List;

import com.importExpress.pojo.GoodsSkuAttr;

public interface GoodsSkuAttrService {
	
	/**保存数据
	 * @param skuAttr
	 * @return
	 */
	GoodsSkuAttr insertGoodsSkuAttr(GoodsSkuAttr skuAttr);
	/**指定产品的指定规格组合是否存在
	 * @param sku
	 * @return
	 */
	GoodsSkuAttr countGoodsSku(String pid,String skuattr);
	
	
	
	/**海外仓商品解析sku
	 * @param code
	 * @return
	 */
	GoodsSkuAttr parseGoodsSku(String code);
	/**海外仓商品解析sku
	 * @param code
	 * @return
	 */
	List<GoodsSkuAttr> ergodicSkuid(String pid);
}
