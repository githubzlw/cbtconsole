package com.importExpress.mapper;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.GoodsSkuAttr;

public interface GoodsSkuAttrMapper {
	
	
	/**插入数据
	 * @param sku
	 * @return
	 */
	int insertGoodsSku(GoodsSkuAttr sku);
	
	/**指定产品的指定规格组合是否存在
	 * @param sku
	 * @return
	 */
	GoodsSkuAttr countGoodsSku(@Param("pid")String pid,@Param("skuattr")String skuattr);
	
	/**产品sku解析匹配错误记录
	 * @param sku
	 * @return
	 */
	int addGoodsSkuErrorlog(GoodsSkuAttr sku);
	
}
