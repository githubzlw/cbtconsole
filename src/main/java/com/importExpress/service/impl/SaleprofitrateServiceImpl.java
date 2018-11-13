/**   
 *
 * @Title SaleprofitrateServiceImpl.java 
 * @Prject cbtconsole
 * @Package com.importExpress.service.impl 
 * @Description TODO
 * @author cerong E-mail: saycjc@outlook.com  
 * @date 2018年4月24日 下午5:22:10 
 * @version V1.0   
 * Copyright © 2018 上海策融网络科技有限公司. All rights reserved.
 */ 
package com.importExpress.service.impl;

import com.importExpress.mapper.SaleprofitrateMapper;
import com.importExpress.pojo.Saleprofitrate;
import com.importExpress.pojo.SaleprofitrateExample;
import com.importExpress.service.SaleprofitrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @author 作者 E-mail: saycjc@outlook.com
* @version 创建时间：2018年4月24日 下午5:22:10 
* 类说明 
*/

/**
 * @ClassName SaleprofitrateServiceImpl 
 * @Description TODO
 * @author cerong
 * @date 2018年4月24日 下午5:22:10  
 */
@Service
public class SaleprofitrateServiceImpl implements SaleprofitrateService {
    @Autowired
    private SaleprofitrateMapper saleprofitrateMapper;
    /* (非 Javadoc) 
    * <p>Title: countByExample</p> 
    * <p>Description: </p> 
    * @param example
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#countByExample(com.importExpress.pojo.SaleprofitrateExample) 
    */
    @Override
    public int countByExample(SaleprofitrateExample example) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.countByExample(example);
    }

    /* (非 Javadoc) 
    * <p>Title: deleteByExample</p> 
    * <p>Description: </p> 
    * @param example
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#deleteByExample(com.importExpress.pojo.SaleprofitrateExample) 
    */
    @Override
    public int deleteByExample(SaleprofitrateExample example) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.deleteByExample(example);
    }

    /* (非 Javadoc) 
    * <p>Title: deleteByPrimaryKey</p> 
    * <p>Description: </p> 
    * @param id
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#deleteByPrimaryKey(java.lang.Integer) 
    */
    @Override
    public int deleteByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.deleteByPrimaryKey(id);
    }

    /* (非 Javadoc) 
    * <p>Title: insert</p> 
    * <p>Description: </p> 
    * @param record
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#insert(com.importExpress.pojo.Saleprofitrate) 
    */
    @Override
    public int insert(Saleprofitrate record) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.insert(record);
    }

    /* (非 Javadoc) 
    * <p>Title: insertSelective</p> 
    * <p>Description: </p> 
    * @param record
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#insertSelective(com.importExpress.pojo.Saleprofitrate) 
    */
    @Override
    public int insertSelective(Saleprofitrate record) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.insertSelective(record);
    }

    /* (非 Javadoc) 
    * <p>Title: selectByExample</p> 
    * <p>Description: </p> 
    * @param example
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#selectByExample(com.importExpress.pojo.SaleprofitrateExample) 
    */
    /*@Override
    public List<Saleprofitrate> selectByExample(SaleprofitrateExample example) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.selectByExample(example, null, null);
    }*/

    /* (非 Javadoc) 
    * <p>Title: selectByPrimaryKey</p> 
    * <p>Description: </p> 
    * @param id
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#selectByPrimaryKey(java.lang.Integer) 
    */
    @Override
    public Saleprofitrate selectByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.selectByPrimaryKey(id);
    }

    /* (非 Javadoc) 
    * <p>Title: updateByExampleSelective</p> 
    * <p>Description: </p> 
    * @param record
    * @param example
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#updateByExampleSelective(com.importExpress.pojo.Saleprofitrate, com.importExpress.pojo.SaleprofitrateExample) 
    */
    @Override
    public int updateByExampleSelective(Saleprofitrate record,
            SaleprofitrateExample example) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.updateByExampleSelective(record, example);
    }

    /* (非 Javadoc) 
    * <p>Title: updateByExample</p> 
    * <p>Description: </p> 
    * @param record
    * @param example
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#updateByExample(com.importExpress.pojo.Saleprofitrate, com.importExpress.pojo.SaleprofitrateExample) 
    */
    @Override
    public int updateByExample(Saleprofitrate record,
            SaleprofitrateExample example) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.updateByExample(record, example);
    }

    /* (非 Javadoc) 
    * <p>Title: updateByPrimaryKeySelective</p> 
    * <p>Description: </p> 
    * @param record
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#updateByPrimaryKeySelective(com.importExpress.pojo.Saleprofitrate) 
    */
    @Override
    public int updateByPrimaryKeySelective(Saleprofitrate record) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.updateByPrimaryKeySelective(record);
    }

    /* (非 Javadoc) 
    * <p>Title: updateByPrimaryKey</p> 
    * <p>Description: </p> 
    * @param record
    * @return 
    * @see com.importExpress.service.SaleprofitrateService#updateByPrimaryKey(com.importExpress.pojo.Saleprofitrate) 
    */
    @Override
    public int updateByPrimaryKey(Saleprofitrate record) {
        // TODO Auto-generated method stub
        return saleprofitrateMapper.updateByPrimaryKey(record);
    }

    @Override
    public Map<String, Object> getList(SaleprofitrateExample example, Integer currentPage,
                                       Integer pageSize, int userId) {
        Integer offset = (currentPage-1)*pageSize;
        List<Saleprofitrate> list = saleprofitrateMapper.getList(userId, offset, pageSize);
        Integer count = saleprofitrateMapper.countByExample(example);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("list", list);
        map.put("count", count);
        return map;
    }

}
 