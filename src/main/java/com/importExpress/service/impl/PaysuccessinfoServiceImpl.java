package com.importExpress.service.impl;

import com.importExpress.mapper.PaysuccessinfoMapper;
import com.importExpress.pojo.Paysuccessinfo;
import com.importExpress.pojo.PaysuccessinfoExample;
import com.importExpress.service.PaysuccessinfoService;
import com.importExpress.utli.Utility;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * *****************************************************************************************
 *
 * @ClassName PaysuccessinfoServiceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2019/6/20 20:24:23
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       20:24:232019/6/20     cjc                       初版
 * ******************************************************************************************
 */
@Service
public class PaysuccessinfoServiceImpl implements PaysuccessinfoService {
    @Autowired
    PaysuccessinfoMapper paysuccessinfoMapper;
    @Override
    public int countByExample(PaysuccessinfoExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(PaysuccessinfoExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(Paysuccessinfo record) {
        return 0;
    }

    @Override
    public int insertSelective(Paysuccessinfo record) {
        return 0;
    }

    @Override
    public List<Paysuccessinfo> selectByExample(PaysuccessinfoExample example) {
        return paysuccessinfoMapper.selectByExample(example);
    }

    @Override
    public Paysuccessinfo selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(Paysuccessinfo record, PaysuccessinfoExample example) {
        return 0;
    }

    @Override
    public int updateByExample(Paysuccessinfo record, PaysuccessinfoExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(Paysuccessinfo record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Paysuccessinfo record) {
        return 0;
    }

    @Override
    public List<Integer> queryUserListByAdminId(Integer adminId) {
        return paysuccessinfoMapper.queryUserListByAdminId(adminId);
    }
    @Override
    public List<Paysuccessinfo> queryPaySuccessInfoList(String pageStr,String limitNumStr,String sttime,String edtime,String userIdStr,String orderNo,Integer userId){
        //格式化 日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int startNum = 0;
        int limitNum = 50;

        if (!(limitNumStr == null || "".equals(limitNumStr) || "0".equals(limitNumStr))) {
            limitNum = Integer.valueOf(limitNumStr) ;
        }
        if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
            startNum = (Integer.valueOf(pageStr) - 1) * limitNum;
        }
        if (sttime == null || "".equals(sttime)) {
            sttime = "2018-12-12 00:00:00";
        } else {
            sttime += " 00:00:00";
        }
        if (edtime == null || "".equals(edtime)) {
            edtime = "2050-12-12 23:59:59";
        } else {
            edtime += " 23:59:59";
        }
        PaysuccessinfoExample example = new PaysuccessinfoExample();
        PaysuccessinfoExample.Criteria criteria = example.createCriteria();
        String orderByClause = example.getOrderByClause();
        criteria.andDelEqualTo(0);
        List<Integer> userIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(userIdStr)) {
            userIdStr = userIdStr.replace("\\s*","");
            criteria.andUseridEqualTo(Integer.parseInt(userIdStr));
        }else {
            //如果是超级管理员则可以看所有的
            if(Utility.ADMIN1 != userId && Utility.ADMIN2 != userId){
                // @author: cjc @date：2019/6/21 9:25:37   Description : 获取该管理员下的用户列表
                userIdList = queryUserListByAdminId(userId);
            }
        }
        if(userIdList.size() > 0){
            criteria.andUseridIn(userIdList);
        }
        if(StringUtils.isNotBlank(orderNo)){
            orderNo = orderNo.replaceAll("\\s*","");
            criteria.andOrdernoEqualTo(orderNo);
        }
        Date sTime = null;
        Date eTime = null;
        try {
            sTime = sdf.parse(sttime);
            eTime = sdf.parse(edtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        criteria.andCreatimeGreaterThan(sTime);
        criteria.andCreatimeLessThan(eTime);
        if(StringUtils.isNotBlank(userIdStr)){
            userIdStr = userIdStr.replaceAll("\\s*","");
            criteria.andUseridEqualTo(Integer.parseInt(userIdStr));
        }
        List<Paysuccessinfo> paysuccessinfoList = paysuccessinfoMapper.selectByExample(example);
        paysuccessinfoList.stream().forEach(e ->{
            e.setCreatimeStr(sdf.format(e.getCreatime()));
            switch (e.getSampleschoice()){
                case 0:
                    e.setSampleschoicestr("用户无选择");
                    break;
                case 1:
                    e.setSampleschoicestr("YES");
                    break;
                case 2:
                    e.setSampleschoicestr("NO");
                    break;
                case 3:
                    e.setSampleschoicestr("用户看不到");
                    break;
                    default:
                        e.setSampleschoicestr("error");
                        break;
            }
            switch (e.getSharechoice()){
                case 0:
                    e.setSharechoicestr("用户无选择");
                    break;
                case 1:
                    e.setSharechoicestr("YES");
                    break;
                case 2:
                    e.setSharechoicestr("NO");
                    break;
                case 3:
                    e.setSharechoicestr("用户看不到");
                    break;
                    default:
                        e.setSharechoicestr("error");
                        break;
            }
            e.setOrderno("<a href ='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+e.getOrderno()+"' target='_blank'>"+e.getOrderno()+"</a>");
        });
        //分页
        paysuccessinfoList = paysuccessinfoList.stream().skip(startNum).limit(limitNum).collect(Collectors.toList());
        return paysuccessinfoList;
    }
}
