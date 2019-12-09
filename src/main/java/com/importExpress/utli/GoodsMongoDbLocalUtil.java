package com.importExpress.utli;

import com.importExpress.service.MongoGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: 本地MongoDB更新
 * @date:2019/11/30
 */
@Service
public class GoodsMongoDbLocalUtil {
    private static final Log logger = LogFactory.getLog(GoodsMongoDbLocalUtil.class);

    @Autowired
    private MongoGoodsService mongoGoodsService;


    public boolean updatePidArr(String[] pidList) {
        if (pidList != null) {
            for (String pid : pidList) {
                updatePid(pid);
            }
        }
        return true;
    }


    public boolean updatePidList(String pids) {
        if (StringUtils.isNotBlank(pids)) {
            String[] pidList = pids.split(",");
            for (String pid : pidList) {
                updatePid(pid);
            }
        }
        return true;
    }

    public boolean updatePid(String pid) {
        boolean isSu = false;
        try {
            mongoGoodsService.insertOrUpdateMongodb(pid);
            isSu = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertOrUpdateMongodb pid:" + pid, e);
        }
        return isSu;
    }
}
