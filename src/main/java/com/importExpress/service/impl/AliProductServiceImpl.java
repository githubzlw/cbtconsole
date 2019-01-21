package com.importExpress.service.impl;

import com.cbt.dao.SingleGoodsDao;
import com.cbt.dao.impl.SingleGoodsDaoImpl;
import com.importExpress.mapper.AliProductMapper;
import com.importExpress.pojo.AliProductBean;
import com.importExpress.pojo.Goods1688OfferBean;
import com.importExpress.pojo.ImportProductBean;
import com.importExpress.service.AliProductService;
import com.importExpress.utli.GoodsPricePraseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AliProductServiceImpl implements AliProductService {

    @Autowired
    private AliProductMapper aliProductMapper;

    private SingleGoodsDao singleGoodsDao = new SingleGoodsDaoImpl();

    public List<AliProductBean> queryForList(AliProductBean aliProduct) {
        return aliProductMapper.queryForList(aliProduct);
    }

    public int queryForListCount(AliProductBean aliProduct) {
        return aliProductMapper.queryForListCount(aliProduct);
    }

    public List<ImportProductBean> query1688ByLire(String aliPid) {
        List<ImportProductBean> list = aliProductMapper.query1688ByLire(aliPid);
        for (ImportProductBean gd : list) {
            GoodsPricePraseUtil.praseExpressPrice(gd);
        }
        return list;
    }

    public List<ImportProductBean> query1688ByPython(String aliPid) {
        List<ImportProductBean> list = aliProductMapper.query1688ByPython(aliPid);
        GoodsPricePraseUtil.prase1688Goods(list);
        return list;
    }

    @Override
    public int setAliFlag(String aliPid, int dealState,int adminId) {
        return aliProductMapper.setAliFlag(aliPid, dealState,adminId);
    }

    @Override
    public int set1688PidFlag(String aliPid, String pid, int dealState,int adminId) {
        return aliProductMapper.set1688PidFlag(aliPid, pid, dealState,adminId);
    }

    @Override
    public int develop1688Pid(String aliPid,String aliPrice, String pid, int adminId) {
        //更新标识
        aliProductMapper.setDevelop1688PidFlag(aliPid, pid, adminId);
        List<String> pids =  new ArrayList<>(1);
        pids.add(pid);
        singleGoodsDao.deleteSingleOffersByPids(pids);
        //同步数据到singleGoods表
        Goods1688OfferBean goods1688OfferBean = aliProductMapper.queryGoodsOffersByPid(pid);
        goods1688OfferBean.setAliPid(aliPid);
        goods1688OfferBean.setAliPrice(aliPrice);
        return singleGoodsDao.insertIntoSingleGoods(goods1688OfferBean);
    }

}
