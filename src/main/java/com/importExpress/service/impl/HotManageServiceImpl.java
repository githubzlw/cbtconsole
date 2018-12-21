package com.importExpress.service.impl;

import com.cbt.warehouse.pojo.HotCategory;
import com.cbt.warehouse.pojo.HotDiscount;
import com.cbt.warehouse.pojo.HotEvaluation;
import com.importExpress.mapper.HotManageMapper;
import com.importExpress.pojo.HotSellGoods;
import com.importExpress.service.HotManageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotManageServiceImpl implements HotManageService {
    @Autowired
    private HotManageMapper hotManageMapper;


    @Override
    public List<HotCategory> queryForList(HotCategory param) {
        return hotManageMapper.queryForList(param);
    }

    @Override
    public int queryForListCount(HotCategory param) {
        return hotManageMapper.queryForListCount(param);
    }

    @Override
    public int checkHotCategoryIsExists(HotCategory param) {
        return hotManageMapper.checkHotCategoryIsExists(param);
    }

    @Override
    public int insertIntoHotCategory(HotCategory param) {
        return hotManageMapper.insertIntoHotCategory(param);
    }

    @Override
    public HotCategory getCategoryById(int id) {
        return hotManageMapper.getCategoryById(id);
    }

    @Override
    public int updateHotCategory(HotCategory param) {
        hotManageMapper.insertHotSellingUpdateLog(param.getId(), "", param.getUpdateAdminId(), param.getIsOn());
        return hotManageMapper.updateHotCategory(param);
    }

    @Override
    public HotDiscount queryDiscountByHotIdAndPid(int hotId, String pid) {
        return hotManageMapper.queryDiscountByHotIdAndPid(hotId, pid);
    }

    @Override
    public int checkDiscountIsExists(int hotId, String pid) {
        return hotManageMapper.checkDiscountIsExists(hotId, pid);
    }

    @Override
    public int insertIntoDiscount(HotDiscount hotDiscount) {
        return hotManageMapper.insertIntoDiscount(hotDiscount);
    }

    @Override
    public int updateDiscountInfo(HotDiscount hotDiscount) {
        return hotManageMapper.updateDiscountInfo(hotDiscount);
    }

    @Override
    public int checkEvaluationIsExists(String goodsPid, String skuId) {
        return hotManageMapper.checkEvaluationIsExists(goodsPid, skuId);
    }

    @Override
    public HotEvaluation queryHotEvaluationById(int id) {
        return hotManageMapper.queryHotEvaluationById(id);
    }

    @Override
    public int insertIntoHotEvaluation(HotEvaluation hotEvaluation) {
        return hotManageMapper.insertIntoHotEvaluation(hotEvaluation);
    }

    @Override
    public int updateHotEvaluation(HotEvaluation hotEvaluation) {
        return hotManageMapper.updateHotEvaluation(hotEvaluation);
    }

    @Override
    public int deleteCategory(int id) {
        hotManageMapper.deleteGoodsByCategoryId(id);
        return hotManageMapper.deleteCategory(id);
    }


    @Override
	public List<HotSellGoods> queryGoodsByHotType(int hotType) {
        List<HotSellGoods> hotGoods = hotManageMapper.queryGoodsByHotType(hotType);
        if (hotGoods != null) {
            for (HotSellGoods goods : hotGoods) {
                String import_url = "https://www.import-express.com/goodsinfo/"
                        + goods.getShow_name().trim().replace(" ", "-")
                        + "-1" + goods.getGoods_pid() + ".html";


                goods.setGoods_import_url(import_url);

                //获取产品区间价
                String wprice = goods.getGoods_price();
                String rangePrice = goods.getRangePrice();
                String price_1688 = goods.getPrice1688();
                String feeprice = goods.getFeeprice();
                if (rangePrice != null && !"".equals(rangePrice) && !"[]".equals(rangePrice)) {
                    goods.setPrice_show(rangePrice);
                    String[] rangePriceList = rangePrice.split("-");
                    if (rangePriceList.length == 2) {
                        goods.setMaxPrice(rangePriceList[1].trim());
                        goods.setMinPrice(rangePriceList[0].trim());
                    } else {
                        goods.setMaxPrice(rangePriceList[0].trim());
                        goods.setMinPrice(rangePriceList[0].trim());
                    }
                    rangePriceList = null;
                } else {
                    if (goods.getIsSoldFlag() > 0) {
                        if (StringUtils.isNotBlank(feeprice) && !"[]".equals(feeprice)) {
                            String price_max = "0";
                            String price_min = "0";
                            if (feeprice.indexOf(",") > -1) {
                                String[] prices = feeprice.split(",");
                                String[] price1 = prices[prices.length - 1].replace(" ", "").split("\\$");
                                String[] price2 = prices[0].replace(" ", "").split("\\$");
                                price_min = price1[1].replace("]", "");
                                price_max = price2[1].replace("[", "");
                                goods.setPrice_show(price_min.trim() + "-" + price_max.trim());
                                goods.setGoods_price(feeprice);
                                goods.setMaxPrice(price_max.trim());
                                goods.setMinPrice(price_min.trim());
                            } else {
                                String[] feePriceList = feeprice.split("\\$");
                                goods.setPrice_show(feePriceList[1].replace("]", "").trim());
                                goods.setMaxPrice(feePriceList[1].replace("]", "").trim());
                                goods.setMinPrice(feePriceList[1].replace("]", "").trim());
                            }
                        } else {
                            goods.setPrice_show(price_1688.trim());
                        }
                    } else {
                        if (wprice != null && !"".equals(wprice) && !"[]".equals(wprice)) {
                            String price_max = "0";
                            String price_min = "0";
                            if (wprice.indexOf(",") > -1) {
                                String[] prices = wprice.split(",");
                                String[] price1 = prices[prices.length - 1].replace(" ", "").split("\\$");
                                String[] price2 = prices[0].replace(" ", "").split("\\$");
                                price_min = price1[1].replace("]", "");
                                price_max = price2[1].replace("[", "");
                                goods.setPrice_show(price_min.trim() + "-" + price_max.trim());
                                goods.setMaxPrice(price_max.trim());
                                goods.setMinPrice(price_min.trim());
                            } else {
                                String[] wpriceList = wprice.split("\\$");
                                goods.setPrice_show(wpriceList[1].replace("]", "").trim());
                                goods.setMaxPrice(wpriceList[1].replace("]", "").trim());
                                goods.setMinPrice(wpriceList[1].replace("]", "").trim());
                            }
                        } else {
                            goods.setPrice_show(price_1688.trim());
                            goods.setMaxPrice(price_1688.trim());
                            goods.setMinPrice(price_1688.trim());
                        }
                    }
                }
                goods.setPrice1688(goods.getMaxPrice());
                goods.setRangePrice(goods.getMinPrice());
            }
        }
        return hotGoods;
    }

	@Override
	public List<HotCategory> queryCategoryList(HotCategory hotCategory) {
		return hotManageMapper.queryCategoryList(hotCategory);
	}


}
