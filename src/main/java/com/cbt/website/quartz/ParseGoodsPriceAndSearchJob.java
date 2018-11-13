package com.cbt.website.quartz;

import com.cbt.service.CustomGoodsService;
import com.cbt.util.StrUtils;
import com.importExpress.pojo.GoodsParseBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;

public class ParseGoodsPriceAndSearchJob extends QuartzJobBean {
    private static final Log logger = LogFactory.getLog(ParseGoodsPriceAndSearchJob.class);

    @Autowired
    private CustomGoodsService customGoodsService;


    public void testFun() throws Exception {
        System.out.println("parseGoodsPriceAndSearchJob testFun begin...");
        logger.info("parseGoodsPriceAndSearchJob testFun begin...");
    }


    public void parseAndUpdateGoodsInfo() throws Exception {
        System.out.println("parseGoodsPriceAndSearchJob begin...");
        logger.info("parseGoodsPriceAndSearchJob begin...");

        try {
            int totalCount = 0;
            int maxId = customGoodsService.queryMaxIdFromCustomGoods();
            int fc = maxId / 500;
            if (maxId % 500 > 0) {
                fc++;
            }
            List<GoodsParseBean> goodsList = new ArrayList<>();

            for (int i = 1; i <= fc; i++) {
                goodsList = customGoodsService.queryCustomGoodsByLimit((i - 1) * 500, i * 500);
                if (goodsList != null && goodsList.size() > 0) {
                    System.out.println("-- do " +  i + "/" + fc + "--");
                    for (GoodsParseBean goodsParse : goodsList) {
                        genGoodsDataAndUpdate(goodsParse);
                        totalCount ++;
                        if(totalCount > 300){
                            //break;
                        }
                    }
                    goodsList.clear();
                }
                if(totalCount > 300){
                    //break;
                }
            }
            //更新点击次数
            customGoodsService.updateGoodsClickNum();


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("parseGoodsPriceAndSearchJob error:" + e.getMessage());
            logger.error("parseGoodsPriceAndSearchJob error:" + e.getMessage());
        }

        System.out.println("parseGoodsPriceAndSearchJob end!!");
        logger.info("parseGoodsPriceAndSearchJob end!!");

    }

    private void genGoodsDataAndUpdate(GoodsParseBean goodsParse) {
        String[] priceList = null;
        String[] moqList = null;
        try {
            //解析weight
            if (StringUtils.isNotBlank(goodsParse.getWeight_1688())) {
                goodsParse.setWeight_1688(StrUtils.matchStr(goodsParse.getWeight_1688(), "(\\d+(\\.\\d+){0,1})"));
            }
            //解析wholesale_price
            if (StringUtils.isNotBlank(goodsParse.getWholesale_price())) {
                priceList = goodsParse.getWholesale_price().split(",");
                moqList = priceList[0].split("\\$");
                String tempBetweenPrice = moqList[1].trim().replace("]","");
                if(tempBetweenPrice.contains("-")){
                    goodsParse.setPrice_1688(tempBetweenPrice.split("-")[1].trim());
                }else{
                    goodsParse.setPrice_1688(tempBetweenPrice);
                }
            }

            //解析ali_weight
            if (StringUtils.isNotBlank(goodsParse.getAli_weight())) {
                goodsParse.setAli_weight(StrUtils.matchStr(goodsParse.getAli_weight(), "(\\d+(\\.\\d+){0,1})"));
                goodsParse.setAliUpdate(1);
            }
            //解析ali_price
            if (StringUtils.isNotBlank(goodsParse.getAli_price())) {
                if (goodsParse.getAli_price().contains("-")) {
                    goodsParse.setAli_price(goodsParse.getAli_price().split("-")[1].trim());
                    goodsParse.setAliUpdate(2);
                }
            }
            /*int count = customGoodsService.checkIsExistsGoods(goodsParse.getPid());
            if (count == 0) {
                customGoodsService.insertCustomGoodsStatistic(goodsParse);
            }*/
            //更新解析信息的统计
            customGoodsService.updateCustomGoodsStatistic(goodsParse);
            //更新搜索次数的统计
            customGoodsService.updateGoodsSearchNum(goodsParse.getPid());
            System.out.println("pid:" + goodsParse.getPid() + " update success !!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("pid:" + goodsParse.getPid() + ",genGoodsDataAndUpdate error :" + e.getMessage());
            logger.error("pid:" + goodsParse.getPid() + ",genGoodsDataAndUpdate error :" + e.getMessage());
        }
    }


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
