package com.cbt.website.quartz;

import com.cbt.orderinfo.service.IOrderinfoService;

import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class parseOrderManageJob extends QuartzJobBean {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(parseOrderManageJob.class);


    @Autowired
    private IOrderinfoService iOrderinfoService;


    public void testFun() throws Exception {
        System.out.println("testFun begin...");
        logger.info("testFun begin...");
    }

    public void flushOrderManage() throws Exception {
        logger.info("开始刷新订单管理页面数据===");
        Map<String,String> paramMap=new HashMap<String,String>();
        for(int i=1;i<=20;i++){
            int page=i>0?(i - 1) * 40:0;
            List<Map<String, String>> list=iOrderinfoService.getOrderManagementQuery(paramMap);
            for(Map<String, String> map:list){
                if(map.get("cancel_obj") == null){
                    map.put("cancel_obj","");
                }
                if(map.get("buyer") == null){
                    map.put("buyer","");
                }
                if(map.get("changenum") == null){
                    map.put("changenum","");
                }
                if(map.get("paytime") == null){
                    map.put("paytime","");
                }
                if(map.get("orderremark") == null){
                    map.put("orderremark","");
                }
                if(map.get("checked") == null){
                    map.put("checked","");
                }
                if(map.get("pay_price_three") == null){
                    map.put("pay_price_three","");
                }
                if(map.get("countrys") == null){
                    map.put("countrys","");
                }
            }
            iOrderinfoService.insertOrderinfoCache(list);
        }
    }





    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    }

}
