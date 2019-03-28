package com.importExpress.service.impl;

import com.importExpress.mapper.OutofstockdemandtableMapper;
import com.importExpress.pojo.Outofstockdemandtable;
import com.importExpress.pojo.OutofstockdemandtableExample;
import com.importExpress.service.CustomBenchmarkService;
import com.importExpress.service.OutofstockdemandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * *****************************************************************************************
 *
 * @ClassName OutofstockdemandServiceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2019/1/10 8:55:12
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       8:55:122019/1/10     cjc                       初版
 * ******************************************************************************************
 */
@Service
public class OutofstockdemandServiceImpl implements OutofstockdemandService {
    @Autowired
    private OutofstockdemandtableMapper out;
    @Autowired
    private CustomBenchmarkService custombenchmarkskuservice;
    @Override
    public int countByExample(OutofstockdemandtableExample example) {
        return out.countByExample(example);
    }

    @Override
    public int deleteByExample(OutofstockdemandtableExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(Outofstockdemandtable record) {
        return 0;
    }

    @Override
    public int insertSelective(Outofstockdemandtable record) {
        return 0;
    }

    @Override
    public List<Outofstockdemandtable> selectByExample(OutofstockdemandtableExample example) {
        return out.selectByExample(example);
    }

    @Override
    public Outofstockdemandtable selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(Outofstockdemandtable record, OutofstockdemandtableExample example) {
        return 0;
    }

    @Override
    public int updateByExample(Outofstockdemandtable record, OutofstockdemandtableExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(Outofstockdemandtable record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Outofstockdemandtable record) {
        return 0;
    }

    @Override
    public List<Outofstockdemandtable> getOutofstockdemandList(int startNum, int limitNum, String startTime, String endTime, String email, int flag, String itemId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sTime = null;
        Date eTime = null;
        try {
            sTime = sdf.parse(startTime);
            eTime = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OutofstockdemandtableExample example = new OutofstockdemandtableExample();
        OutofstockdemandtableExample.Criteria criteria = example.createCriteria();
        criteria.andCreatimeBetween(sTime,eTime);
        if(flag!=-1){
            criteria.andFlagEqualTo(flag);
        }
        if(StringUtils.isNotBlank(itemId)){
            criteria.andItemidEqualTo(itemId);
        }
        if(StringUtils.isNotBlank(email)){
            criteria.andEmailEqualTo(email);
        }
        List<Outofstockdemandtable> list = out.selectByExample(example);
        list.stream().forEach(e ->{
            e.setCtime(sdf.format(e.getCreatime()));
            e.setEtime(sdf.format(e.getUpdatetime()));
            e.setItemid("<a href ='https://www.import-express.com/goodsinfo/cbtconsole-1"+e.getItemid()+".html' target='_blank'>"+e.getItemid()+"</a>");
            String emails = e.getEmail();
            e.setEmail("<a href=\"mailto:"+emails+"\">"+emails+"</a>");
            String goodstype = e.getGoodstype();
            e.setGoodstype("");
            String[] split = goodstype.split("@");
            if(split.length>0){
                Arrays.asList(split).stream().forEach(j ->{
                    String typename = "";
                    String s = j.replaceAll("[^(0-9)]", "");
                    if(StringUtils.isNotBlank(s)){
                        String typeNameByDataBase = custombenchmarkskuservice.selectTypeNameBySkuId(s);
                        e.setGoodstype(e.getGoodstype()+typeNameByDataBase+"||");
                    }else{
                        e.setGoodstype(e.getGoodstype()+typename+j+"||");
                    }

               });
            }
        });
        return list;
    }
}
