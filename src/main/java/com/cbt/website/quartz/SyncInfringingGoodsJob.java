package com.cbt.website.quartz;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class SyncInfringingGoodsJob implements Job {
    private static final Log LOG = LogFactory.getLog(SyncInfringingGoodsJob.class);
    private static CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("SyncInfringingGoodsJob begin...");
        LOG.info("SyncInfringingGoodsJob begin...");
        int total = 0;
        try {
            if (customGoodsDao == null) {
                customGoodsDao = new CustomGoodsDaoImpl();
            }
            List<String> pids = customGoodsDao.queryInfringingGoodsByLimit(1000);
            if(pids == null || pids.size() == 0){
                System.err.println("SyncInfringingGoodsJob this list size 0 :<:<:<" );
            }
            for (String pid : pids) {
                if (StringUtils.isNotBlank(pid)) {
                    customGoodsDao.updateInfringingGoodsByPid(pid);
                    //执行下架逻辑
                    customGoodsDao.setGoodsValid(pid, "", 0, -1, 0,"侵权下架");
                    total ++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("SyncInfringingGoodsJob error:" + e.getMessage());
            LOG.error("SyncInfringingGoodsJob error:" + e.getMessage());
        }

        System.out.println("SyncInfringingGoodsJob end,本次更新数量:" + total);
        LOG.info("SyncInfringingGoodsJob end,本次更新数量:" + total);

    }
}
