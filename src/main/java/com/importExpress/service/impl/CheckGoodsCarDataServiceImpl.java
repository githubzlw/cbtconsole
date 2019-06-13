package com.importExpress.service.impl;

import com.cbt.paypal.result.ResponseData;
import com.cbt.website.bean.GoodsCarActiveBean;
import com.importExpress.pojo.GoodsCarconfigExample;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import com.importExpress.service.GoodsCarconfigService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * *****************************************************************************************
 *
 * @ClassName CheckGoodsCarDataServiceImpl
 * @Author: cjc
 * @Descripeion 校验购物车数据
 * @Date： 2019/5/22 11:40:02
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       11:40:022019/5/22     cjc                       初版
 * ******************************************************************************************
 */
@Service
@Slf4j
public class CheckGoodsCarDataServiceImpl implements com.importExpress.service.CheckGoodsCarDataService {
    @Autowired
    GoodsCarconfigService goodsCarconfigService;

    public static AtomicInteger count = new AtomicInteger(0);

    public static void increase() {
        count.incrementAndGet();
    }
    @Override
    public ResponseData checkGoodsCarData(){
        GoodsCarconfigExample example = new GoodsCarconfigExample();
        GoodsCarconfigExample.Criteria configCriteria = example.createCriteria();
        //获取当前时间
        LocalDateTime  tody = LocalDateTime.now();
        //日期为最近三个月的时间
        LocalDateTime threeMonthsAgoTime = tody.minus(3, ChronoUnit.MONTHS);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = threeMonthsAgoTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        configCriteria.andUpdatetimeGreaterThan(date);
        //获取所有有标识的购物车数据
        List<GoodsCarconfigWithBLOBs> list = goodsCarconfigService.selectByExampleWithBLOBs(example);
        list.stream().forEach(goodsCarconfig ->{
            checkData(goodsCarconfig);
        });
        String msg = " 没有异常:";
        if(count.get()>0){
            msg = "有异常:";
        }
        return new ResponseData(msg,String.valueOf(count.get()));
    }
    @Async
    public void checkData(GoodsCarconfigWithBLOBs goodsCarBean){
        int userid = 0;
        String userCookieId="";
        if(null != goodsCarBean.getUserid()){
            userid = goodsCarBean.getUserid();
        }else {
            userCookieId = goodsCarBean.getUsercookieid();
        }
        log.info("当前购物车id 为:[{}],userCookieId:[{}]",userid,userCookieId);
        String buyformecarconfig = goodsCarBean.getBuyformecarconfig();
        if(StringUtils.isNotBlank(buyformecarconfig)){
            try{
                List<GoodsCarActiveBean> list_active = (List<GoodsCarActiveBean>) JSONArray.toCollection(JSONArray.fromObject(buyformecarconfig),GoodsCarActiveBean.class);
            }catch(Exception e){
                log.error("当前用户解析失败userid:[{}],userCookieId:[{}],e:{}",userid,userCookieId,e);
                log.error(buyformecarconfig);
                increase();
            }
        }
    }
}
