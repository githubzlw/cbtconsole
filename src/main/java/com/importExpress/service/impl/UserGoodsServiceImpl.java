package com.importExpress.service.impl;

import com.cbt.util.GoodsInfoUtils;
import com.importExpress.mapper.UserGoodsMapper;
import com.importExpress.pojo.UserGoods;
import com.importExpress.service.UserGoodsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.service.impl
 * @date:2019/12/24
 */
@Service
public class UserGoodsServiceImpl implements UserGoodsService {


    @Autowired
    private UserGoodsMapper userGoodsMapper;

    @Override
    public List<UserGoods> queryForList(UserGoods goods) {
        List<UserGoods> list = userGoodsMapper.queryForList(goods);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(e -> {
                if (StringUtils.isNotBlank(e.getMainImg()) && !e.getMainImg().contains("http")) {
                    e.setMainImg(e.getRemotePath() + e.getMainImg());
                }
                if(StringUtils.isNotBlank(e.getGoodsName())){
                    e.setGoodsUrl(GoodsInfoUtils.genOnlineUrlByParam(e.getPid(), "", e.getGoodsName()));
                }
            });
        }
        return list;
    }

    @Override
    public int queryForListCount(UserGoods goods) {
        return userGoodsMapper.queryForListCount(goods);
    }
}
