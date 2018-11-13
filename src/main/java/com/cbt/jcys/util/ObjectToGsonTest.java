package com.cbt.jcys.util;

import com.cbt.jcys.bean.DataInfo;
import com.cbt.jcys.bean.Detail;
import com.cbt.jcys.bean.FPDetail;
import com.cbt.jcys.bean.PriceData;

import java.util.HashMap;
import java.util.Map;

public class ObjectToGsonTest {
	public static void main(String[] args) throws Exception {
		JcgjSoapHttpPost jc=new JcgjSoapHttpPost();  //gson
		//{"DataInfo":{"HYBM":"SH0809","PWD":"SH61504007","WorldCountry":"FR","FasterWay":"","FasterPort":"0","PackageType":"PAK","cweight":"3","clong":"3","cwidth":"3","cheight":"3"}}]}
		PriceData pd = new PriceData();
		pd.setHYBM("SH0809");      //会员登录名  ZYW0037   SH0809
		pd.setPWD("SH61504007");    			//登录密码      1  SH61504007
		pd.setWorldCountry("FR");   //国家
		pd.setFasterWay("");        //快件网络
		pd.setFasterPort("0");      //出运口岸  KNSHA
		pd.setPackageType("PAK");   //货件类型
		pd.setCweight("3.000");     //货物实重
		pd.setClong("3");          //长
		pd.setCwidth("3");         //宽
		pd.setCheight("3");         //高
		
		//DataInfo 
		
		DataInfo di = new DataInfo();
		di.setHYBM("SH0809");     //客户编码
		di.setPWD("SH61504007");  //客户密码
		di.setBillNo("4444");	  //运单号	
		di.setKJLX("11");			//快件类型
		di.setFJRGS("11");			//发件人公司
		di.setFJRXM("11");			//发件人电话
		di.setHGZCBM("11");			//海关注册编码
		
		di.setSJRXM("11");			//收件人姓名
		di.setSJRDH("11");			//收件人电话
		di.setSJRGJ("11");			//收件人国家
		di.setSJRYB("11");			//收件人邮编
		di.setSJRCS_YWMC("");		//收件人城市  ???
		di.setSTATENAME("");		//收件人州名
		di.setSJRCS("11");			//收件人城市
		di.setSJRGS("11");			//收件人公司
		di.setPPCC("11");			//预付到付    1.PP 2.CC 3.TT
		di.setJS("11");				//件数
		di.setWPLX("11");			//物品类型
		di.setPM("11");				//中文品名
		di.setYWPM("11");			//英文品名	
		di.setHSBM("11");			//HS编码
		di.setSBJE("11");			//申报金额
		di.setSBBZ("11");			//申报币种
		di.setZSBGBZ("11");			//是否报关标志  1-是，0-否
		di.setTYBM("11");			//统一编码
		di.setSJZL("11");			//实重
		di.setTJZL("11");			//体重
		di.setCKMC("11");			//仓库名称
		di.setSUBFLAG("广州仓");		//仓库标识符  必填
//		di.setBillno(jc.callGetNumber("JP", "03"));
		
		//Detail
		Detail[]ds = new Detail[2];
		Detail d1= new Detail();
		Detail d2= new Detail();
		
		d1.setZDBH("111");			//子单编码
		d1.setHWMS("111");			//货物描述
		d1.setJS("111");			//件数
		d1.setSZ("111");			//实重
		d1.setC("111");				//长
		d1.setK("111");				//宽
		d1.setG("11");				//高
		d1.setTZ("111");			//体重
		
		
		d2.setZDBH("111");			//
		d2.setHWMS("111");			//
		d2.setJS("111");			//
		d2.setSZ("111");			//
		d2.setC("111");				//
		d2.setK("111");				//
		d2.setG("11");				//
		d2.setTZ("111");			//
		
		ds[0] = d1;
		ds[1] = d2;
			
		//FPDetail
		FPDetail []fts = new FPDetail[1];
		FPDetail ft1 = new FPDetail();
		
		ft1.setNJS("111");			//内件数
		ft1.setSBBZ("111");			//申报币种
		ft1.setSBJE("111");			//申报金额
		ft1.setYWPM("111");			//英文品名
		ft1.setPM("中文品名");		//中文品名
		ft1.setCZ("材质");			//材质
		ft1.setHSBM("abds165477");	//海关编码
		ft1.setDJJE("0.18");		//单价金额
		ft1.setJZDW("111");			//计重单位
		ft1.setGGXH("111");			//规格型号
		ft1.setYT("111");			//用途
		fts[0] = ft1;
		
		//pageck
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Map pacs[] = new HashMap[1];
        Map<String, Object> pacdata = new HashMap<String, Object>();
        
        //组装
        di.setDetail(ds);
//        di.setFPDetail(fts);
        pacdata.put("DataInfo", di);  //di  运单     pd 报价
        
        pacs[0] = pacdata;
        mapdata.put("Package", pacs);
        
       
		String strpac = jc.objToGson(mapdata);
	//	System.out.println(strpac);
		
		
//		REQUEST  运单数据
	//	System.out.println("REQUEST__结果："+jc.callRequest(strpac)); 
//		
		//GetNumber 运单号 982008543 982008565 982008576
		System.out.println("GetNumber__结果："+jc.callGetNumber("JP", "05")); 
		
		//Billno   查询转单号  
//		System.out.println("Billno__结果："+jc.callBillno("963581503")); 
		
		//baojia   运费
	//	System.out.println("callPrice__结果："+jc.callPrice(strpac)); 
	//	List<PriceReturnJsonNew> list = jc.getJcFreight(pd);
		
		System.out.println("111");
		
		
		//运单跟踪
//		System.out.println("callTrackCn__结果："+jc.callTrackCn("123")); 
	}
}
