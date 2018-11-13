package com.cbt.util;

import java.math.BigDecimal;

public class FeightUtils {

	 /**运费
	 * @date 2017年10月10日
	 * @author user4
	 * @param weight
	 * @return  
	 */
	public static  double getCarFeight(double weight){
	        double weight_g = weight * 1000;
//	        double newFeight = weight==0 ? 0.0 : minNum(3 + 0.08 * weight_g, getJCEXCarFeight(weight) * 0.92);
//	        double newFeight = weight==0 ? 0.0 : minNum(9 + 0.08 * weight_g, getJCEXCarFeight(weight) * 0.92);
	        
	        double newFeight = weight==0 ? 0.0 : Util.PERGRAM * weight_g;
//	      double newFeight = weight==0 ? 0.0 : 9 + 0.08 * weight_g;
//	      CARPRICELOG.warn("EpacketCarFeight:"+(9 + 0.08 * weight_g)+"JCEXCarFeight*0.92:"+(getJCEXCarFeight(weight) * 0.92));
	        return newFeight;
	    }
	
	
	/**
	 * 
	 * @Title getCarFeightByNew 
	 * @Description 新的运费计算
	 * @param weight
	 * @return
	 * @return double
	 */
	public static  double getCarFeightByNew(double weight){
        double weight_g = weight * 1000;
        return (weight==0 ? 0.0 : Util.PERGRAM * weight_g);
    }
	
	
	//59.8
	
	 public static Double minNum(Double  num1, Double num2) {
	        if (num1 < num2) {
	            return new BigDecimal(num1).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
	        }
	        return new BigDecimal(num2).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue() ;
	    }
	
	/**JCEX运费
	 * @date 2017年10月10日
	 * @author user4
	 * @param weight
	 * @return  
	 */
	public static  double getJCEXCarFeight(double weight){
	        double weight_g = weight * 1000;
	        double newFeight = weight==0 ? 
	        		0.0 : (weight_g > 20000 ? (304+(weight_g-500)/500*19):(70+(weight_g-500)/500*25));
	        
	        return new BigDecimal(newFeight).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue() ;
	    }
	
	//最新运费
	public static  double getCarFeightNew(double weight,int catId){
		double newFeight = 0.0;
		double weight_g = weight * 1000;
		boolean isSpecialCatId = getThisCatIdIsSpecial(catId);
		if(isSpecialCatId){
			newFeight = weight==0 ? 0.0 : Util.PERGRAMSPECIAL * weight_g;
		}else {
			newFeight = weight==0 ? 0.0 : Util.PERGRAM * weight_g;
		}
		return new BigDecimal(newFeight).setScale(7,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * @param catId
	 * @return 判断当前商品是否是带电 或者彩妆
	 */
	public  static boolean getThisCatIdIsSpecial(int catId){
		boolean isSpecial = false;
		int[] catIds =  new int[]{
				10204 ,1047893 ,123610009 ,727 ,728 ,123754001 ,82101 ,1042634 ,1043162 ,
				1043498 ,121702001 ,126182004 ,50906 ,126144003 ,126178001 ,1550 ,124186012 ,
				124188008 ,124936001 ,10331 ,122700010 ,122708008 ,122698011 ,1047903 ,1047904 ,
				1047905 ,1047996 ,122698010 ,124188009 ,124952002 ,124952003 ,124952005 ,124952007 ,
				125012003 ,704 ,720 ,724 ,725 ,3414 ,1032233 ,1032781 ,1032782 ,122314006 ,124000001 ,
				50903 ,1033984 ,1037149 ,702 ,10256 ,1042047 ,124736031 ,124742035 ,10246 ,1048186 ,
				122704001 ,123608002 ,124734039 ,726 ,122396005 ,122398004 ,1046691 ,121624001 ,123648002 ,
				123650001 ,123736004 ,123736005 ,123736006 ,123736007 ,123736008 ,123752002 ,123752003 ,
				123752004 ,123754004 ,123754005 ,123754006 ,123756001 ,123756003 ,123758003 ,123758004 ,
				123758005 ,123758006 ,123760002 ,123760003 ,123760004 ,123860004 ,124580001 ,124582001 ,124734030 ,
				124734031 ,124736033 ,124736056 ,124740027 ,124740028 ,124824005 ,124902001 ,124904002 ,124906001 ,
				124908001 ,122178001 ,122180001 ,122310007 ,122374007 ,123736003 ,123754002 ,123754003 ,123756002 ,
				123758001 ,123758002 ,124272012 ,124812002 ,1034778 ,1034779 ,1034780 ,1034782 ,1034783 ,1034784 ,
				1034785 ,1036812 ,1040898 ,1040900 ,1043093 ,1043094 ,1043095 ,1043504 ,1046942 ,124186007 ,1043175 ,
				1034758 ,1041576 ,1033966 ,1033967 ,1033971 ,1033973 ,1033975 ,123854005 ,123864004 ,123864005 ,
				126144004 ,126178002 ,126180001 ,123860006 ,126180002};
		for (int i = 0; i < catIds.length; i++) {
			if(catId ==catIds[i]){
				isSpecial = true;
			}
		}
		return isSpecial;
	}

}
