package com.importExpress.mapper;

import com.importExpress.pojo.ShopBrandAuthorization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopUrlMapper {

    /**
     * 根据店铺ID查询店铺品牌和授权信息
     * @param shopId
     * @return
     */
    List<ShopBrandAuthorization> queryBrandAuthorizationByShopId(String shopId);

    /**
     * 根据品牌ID查询品牌和授权信息
     * @param id
     * @return
     */
    ShopBrandAuthorization queryBrandAuthorizationById(Integer id);

     /**
     * 检查是否存在店铺品牌名称
     * @param shopId
     * @param brandName
     * @param brandId
     * @return
     */
    int checkBrandAuthorizationByName(@Param("shopId") String shopId, @Param("brandName") String brandName,
                                      @Param("brandId") Integer brandId);

    /**
     * 插入品牌和授权信息
     * @param shopBrandAuthorization
     * @return
     */
    int insertIntoShopBrandAuthorization(ShopBrandAuthorization shopBrandAuthorization);

    /**
     * 更新品牌和授权信息
     * @param shopBrandAuthorization
     * @return
     */
    int updateShopBrandAuthorization(ShopBrandAuthorization shopBrandAuthorization);

    /**
     * 根据牌ID删除品牌和授权信息
     * @param id
     * @return
     */
    int deleteShopBrandAuthorizationById(Integer id);

    /**
     * 根据店铺ID删除品牌和授权信息
     * @param shopId
     * @return
     */
    int deleteShopBrandAuthorizationByShopId(String shopId);
}
