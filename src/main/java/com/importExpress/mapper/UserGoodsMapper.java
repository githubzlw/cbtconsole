package com.importExpress.mapper;

import com.importExpress.pojo.UserGoods;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.mapper
 * @date:2019/12/24
 */
@Resource
public interface UserGoodsMapper {

    /**
     * 根据参数查询商品信息
     * @param goods
     * @return
     */
    List<UserGoods> queryForList(UserGoods goods);

    /**
     * 根据参数查询商品信息总数
     * @param goods
     * @return
     */
    int queryForListCount(UserGoods goods);
}
