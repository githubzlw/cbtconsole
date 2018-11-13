package com.importExpress.utli;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import org.apache.commons.lang3.StringUtils;

public class GoodsPriceUpdateUtil {

    public static final double EXCHANGE_RATE = 6.6;

    // 精确对标的产品 加价率
		public static double getAddPriceJz(double priceXs){
			double addPriceLv = 0;
			//如果 价差系数X <1.1, 加价率= 0.1
			if(priceXs < 1.1){
				addPriceLv = 0.1;
				//如果 价差系数X >2, 加价率= 1
			}else if(priceXs > 2){
				addPriceLv = 1;
				//否则, 加价率= 价差系数X-1
			}else{
				addPriceLv = priceXs - 1;
			}
			return addPriceLv;
		}


		//取得类别调整系数
		public static double getCatxs(String catId,String factoryPrice){
			double catXs=0;
			CustomGoodsDao dao = new CustomGoodsDaoImpl();
			String rs = dao.getCatxs(catId,factoryPrice);
			if(StringUtils.isBlank(rs)){
				catXs=0;
			}else{
				catXs = Double.valueOf(rs);
			}
			return catXs;
		}
}
