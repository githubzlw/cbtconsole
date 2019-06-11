package com.importExpress.service;

import com.importExpress.pojo.HotEventsGoods;
import com.importExpress.pojo.HotEventsInfo;

import java.util.List;

public interface HotEventsService {

    /**
     * 查询全部的Info信息
     *
     * @return
     */
    List<HotEventsInfo> queryForInfoList(HotEventsInfo eventsInfo);

    /**
     * 查询全部的goods信息
     *
     * @return
     */
    List<HotEventsGoods> queryForGoodsList(HotEventsGoods eventsGoods);

    /**
     * 插入info信息
     *
     * @param eventsInfo
     * @return
     */
    int insertIntoHotEventsInfo(HotEventsInfo eventsInfo);

    /**
     * 插入商品信息
     *
     * @param eventsGoods
     * @return
     */
    int insertIntoHotEventsGoods(HotEventsGoods eventsGoods);

    /**
     * 更新info信息
     *
     * @param eventsInfo
     * @return
     */
    int updateIntoHotEventsInfo(HotEventsInfo eventsInfo);

    /**
     * 更新goods信息
     *
     * @param eventsGoods
     * @return
     */
    int updateIntoHotEventsGoods(HotEventsGoods eventsGoods);

    /**
     * 删除info信息
     *
     * @param eventsInfo
     * @return
     */
    int deleteIntoHotEventsInfo(HotEventsInfo eventsInfo);

    /**
     * 删除goods信息
     *
     * @param eventsGoods
     * @return
     */
    int deleteIntoHotEventsGoods(HotEventsGoods eventsGoods);
}
