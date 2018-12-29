package com.importExpress.service.impl;

import com.importExpress.mapper.AliProductMapper;
import com.importExpress.pojo.AliProductBean;
import com.importExpress.pojo.ImportProductBean;
import com.importExpress.service.AliProductService;
import com.importExpress.utli.GoodsPricePraseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliProductServiceImpl implements AliProductService {

    @Autowired
    private AliProductMapper aliProductMapper;

    public List<AliProductBean> queryForList(AliProductBean aliProduct) {
        return aliProductMapper.queryForList(aliProduct);
    }

    public int queryForListCount(AliProductBean aliProduct) {
        return aliProductMapper.queryForListCount(aliProduct);
    }

    public List<ImportProductBean> query1688ByLire(String aliPid) {
        List<ImportProductBean> list = aliProductMapper.query1688ByLire(aliPid);
        for (ImportProductBean gd : list) {
            GoodsPricePraseUtil.prasePrice(gd);
        }
        return list;
    }

    public List<ImportProductBean> query1688ByPython(String aliPid) {
        List<ImportProductBean> list = aliProductMapper.query1688ByPython(aliPid);
        for (ImportProductBean gd : list) {
            GoodsPricePraseUtil.prasePrice(gd);
        }
        return list;
    }
}
