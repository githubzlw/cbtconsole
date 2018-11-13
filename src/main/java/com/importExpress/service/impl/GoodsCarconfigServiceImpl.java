package com.importExpress.service.impl;

import com.cbt.dao.GoodsCarConfigDao;
import com.cbt.dao.impl.GoodsCarConfigDaoImpl;
import com.importExpress.mapper.GoodsCarconfigMapper;
import com.importExpress.pojo.GoodsCarconfig;
import com.importExpress.pojo.GoodsCarconfigExample;
import com.importExpress.pojo.GoodsCarconfigWithBLOBs;
import com.importExpress.service.GoodsCarconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *****************************************************************************************
 *
 * @ClassName goodsCarconfigServiceImpl
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/9/15 16:58
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       16:582018/9/15     cjc                       初版
 * ******************************************************************************************
 */
@Service("goodsCarconfigService")
public class GoodsCarconfigServiceImpl implements GoodsCarconfigService {
    @Autowired
    private GoodsCarconfigMapper goodsCarconfigMapper;


    private GoodsCarConfigDao carConfigDao = new GoodsCarConfigDaoImpl();


    @Override
    public int countByExample(GoodsCarconfigExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(GoodsCarconfigExample example) {
        return goodsCarconfigMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return goodsCarconfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GoodsCarconfigWithBLOBs record) {
        return goodsCarconfigMapper.insert(record);
    }

    @Override
    public int insertSelective(GoodsCarconfigWithBLOBs record) {
        return goodsCarconfigMapper.insertSelective(record);
    }

    @Override
    public List<GoodsCarconfigWithBLOBs> selectByExampleWithBLOBs(GoodsCarconfigExample example) {
        return goodsCarconfigMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<GoodsCarconfig> selectByExample(GoodsCarconfigExample example) {
        return goodsCarconfigMapper.selectByExample(example);
    }

    @Override
    public GoodsCarconfigWithBLOBs selectByPrimaryKey(Integer id) {
        return goodsCarconfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(GoodsCarconfigWithBLOBs record, GoodsCarconfigExample example) {
        return goodsCarconfigMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(GoodsCarconfigWithBLOBs record, GoodsCarconfigExample example) {
        return goodsCarconfigMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(GoodsCarconfig record, GoodsCarconfigExample example) {
        return goodsCarconfigMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(GoodsCarconfigWithBLOBs record) {
        return goodsCarconfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(GoodsCarconfigWithBLOBs record) {
        return goodsCarconfigMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(GoodsCarconfig record) {
        return goodsCarconfigMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<GoodsCarconfigWithBLOBs> queryByIsNew(int userId) {
        return goodsCarconfigMapper.queryByIsNew(userId);
    }

    @Override
    public int updateByIdAndUserId(int id, int userId) {
        //更新标识
        return goodsCarconfigMapper.updateByIdAndUserId(id, userId);
    }

    @Override
    public boolean updateGoodsCarConfig(GoodsCarconfigWithBLOBs record) {
        //更新线上
        return carConfigDao.updateGoodsCarConfig(record);
    }


}
