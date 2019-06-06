package com.importExpress.service.impl;

import com.importExpress.mapper.HotEventsMapper;
import com.importExpress.pojo.HotEventsGoods;
import com.importExpress.pojo.HotEventsInfo;
import com.importExpress.service.HotEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotEventsServiceImpl implements HotEventsService {

    @Autowired
    private HotEventsMapper eventsMapper;

    @Override
    public List<HotEventsInfo> queryForInfoList(HotEventsInfo eventsInfo) {
        return eventsMapper.queryForInfoList(eventsInfo);
    }

    @Override
    public List<HotEventsGoods> queryForGoodsList(HotEventsGoods eventsGoods) {
        return eventsMapper.queryForGoodsList(eventsGoods);
    }

    @Override
    public int insertIntoHotEventsInfo(HotEventsInfo eventsInfo) {
        return eventsMapper.insertIntoHotEventsInfo(eventsInfo);
    }

    @Override
    public int insertIntoHotEventsGoods(HotEventsGoods eventsGoods) {
        return eventsMapper.insertIntoHotEventsGoods(eventsGoods);
    }

    @Override
    public int updateIntoHotEventsInfo(HotEventsInfo eventsInfo) {
        return eventsMapper.updateIntoHotEventsInfo(eventsInfo);
    }

    @Override
    public int updateIntoHotEventsGoods(HotEventsGoods eventsGoods) {
        return eventsMapper.updateIntoHotEventsGoods(eventsGoods);
    }

    @Override
    public int deleteIntoHotEventsInfo(HotEventsInfo eventsInfo) {
        return eventsMapper.deleteIntoHotEventsInfo(eventsInfo);
    }

    @Override
    public int deleteIntoHotEventsGoods(HotEventsGoods eventsGoods) {
        return eventsMapper.deleteIntoHotEventsGoods(eventsGoods);
    }
}
