package com.importExpress.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.importExpress.mapper.GoodsSkuAttrMapper;
import com.importExpress.pojo.GoodsSkuAttr;
import com.importExpress.service.GoodsSkuAttrService;
@Service
public class GoodsSkuAttrServiceImpl implements GoodsSkuAttrService {
	@Autowired
	private GoodsSkuAttrMapper goodsSkuAttrMapper;

	@Override
	public GoodsSkuAttr insertGoodsSkuAttr(GoodsSkuAttr skuAttr) {
		GoodsSkuAttr id = goodsSkuAttrMapper.countGoodsSku(skuAttr.getPid(),skuAttr.getSkuattr());
		if(id == null) {
			goodsSkuAttrMapper.insertGoodsSku(skuAttr);
			return skuAttr;
		}
		return id;
	}

	@Override
	public int updateWaitMatch(int id, GoodsSkuAttr goodsSkuAttrId) {
		return goodsSkuAttrMapper.updateWaitMatch(id, goodsSkuAttrId);
	}

	@Override
	public List<Map<String, Object>> getWaitMatch() {
		goodsSkuAttrMapper.syncOrderDetails();
		return goodsSkuAttrMapper.getWaitMatch();
	}

	@Override
	public GoodsSkuAttr countGoodsSku(String pid, String skuattr	) {
		GoodsSkuAttr countGoodsSku = goodsSkuAttrMapper.countGoodsSku(pid,skuattr);
		return countGoodsSku;
	}

}
