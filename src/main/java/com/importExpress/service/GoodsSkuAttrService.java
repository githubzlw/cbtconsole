package com.importExpress.service;

import java.util.List;
import java.util.Map;

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
	
	/**更新order_detail_sku_match 状态
	 * @param id
	 * @param goodsSkuAttrId
	 * @return
	 */
	int updateWaitMatch(int id,GoodsSkuAttr goodsSkuAttrId);
	
	/**获取带解析的产品
	 * @return
	 */
	List<Map<String,Object>> getWaitMatch();

}
