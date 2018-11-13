package com.cbt.website.quartz;

import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.util.Util;
import com.cbt.util.Utility;
import com.cbt.warehouse.util.StringUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.DecimalFormat;
import java.util.List;

public class ParseOrderFreightJob extends QuartzJobBean {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ParseOrderFreightJob.class);


    @Autowired
    private IOrderinfoService iOrderinfoService;


    public void testFun() throws Exception {
    }

    public void flushOrderFreight() {
        try{
            logger.info("开始刷新订单预估运费数据===");
//            List<OrderBean> list=iOrderinfoService.getFlushOrderFreightOrder();
//            flushFreight(list);
//            list.clear();
            logger.info("结束刷新订单预估运费数据===");
        }catch (Exception e){
            logger.info("刷新订单运费运费错误。、。。。。。。。。。。");
        }
        //采购输入的价格数量和订单销售数量有问题的订单预警
        List<OrderBean> listFlag=iOrderinfoService.getAllOrderInfo();
        for(int i=0;i<listFlag.size();i++){
            OrderBean o=listFlag.get(i);
            String orderNo=o.getOrderNo();
            String yourorder=o.getYourorder();
            String buyAmount=o.getBuyAmount();
            String buycount=o.getBuycount();
            String esprice=o.getEsBuyPrice();
            System.out.println("orderNo==================="+orderNo);
            if(StringUtil.isBlank(buyAmount) || StringUtil.isBlank(buycount)){
                continue;
            }
            double fit=Math.abs((Double.parseDouble(esprice)-Double.parseDouble(buyAmount))/Double.parseDouble(buyAmount)*100);
            if(Integer.valueOf(buycount)>Integer.valueOf(yourorder) || fit>10){
                iOrderinfoService.deleteFlagByOrder(orderNo);
                iOrderinfoService.insertFlagByOrderid(orderNo);
            }
        }
    }

    public void flushFreight(List<OrderBean> list){
        DecimalFormat df = new DecimalFormat("######0.00");
        for(OrderBean o:list){
            logger.info("开始刷新订单【"+o.getOrderNo()+"】运费和预估采购额");
            double freightFee = iOrderinfoService.getFreightFee(o.getVolumeweight(), o);
            String freight=df.format(freightFee);
            //刷新订单的预计采购金额
            List<OrderDetailsBean> odList=iOrderinfoService.getOrdersDetails(o.getOrderNo());
            double BuyPrice=0.00;
            for(OrderDetailsBean odb:odList){
                String price1688 = Utility.getStringIsNull(odb.getCbrPrice()) ? odb.getCbrPrice() : "0";
                if("0".equals(price1688) || StringUtil.isBlank(odb.getCbrPrice())){
                    price1688=odb.getCbrdPrice();
                }
                price1688=StringUtil.isBlank(price1688)?"0":price1688;
                String es_price=price1688;
                if(odb.getState() ==1 || odb.getState() == 0){
                    es_price=StringUtil.getEsPrice(es_price);
                }else{
                    es_price="0.00";
                }
                String ali_sellunit=odb.getAli_sellunit();
                int unit= Util.getNumberForStr(ali_sellunit);
                BuyPrice+=Double.valueOf(es_price)*odb.getYourorder()*unit;
            }
            double pid_amount=0.00;
            if(odList.size()>0){
                pid_amount=odList.get(0).getPid_amount();
            }
            //更新预估国际运费,预估采购金额
            String esBuyPrice=df.format(BuyPrice+pid_amount);
            logger.info("订单【"+o.getOrderNo()+"】运费【"+freight+"】和预估采购额：【"+esBuyPrice+"】");
            iOrderinfoService.updateFreightForOrder(o.getOrderNo(),freight,esBuyPrice);
        }
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    }

}
