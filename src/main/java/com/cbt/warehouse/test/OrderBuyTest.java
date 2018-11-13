package com.cbt.warehouse.test;

import com.cbt.warehouse.service.IOrderBuyService;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.service.OrderBuyServiceImpl;
import com.cbt.warehouse.service.WarehouseServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrderBuyTest {
	
	public static void main(String[] args) {   
		IOrderBuyService buyService;
		IWarehouseService iWarehouseService;
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext-base.xml"});
		buyService=(OrderBuyServiceImpl)context.getBean(OrderBuyServiceImpl.class);
		iWarehouseService =(WarehouseServiceImpl)context.getBean(WarehouseServiceImpl.class);
//		List<OrderBuyBean> list =  buyService.getOrderBuyById(2);
		
		
		
//		
//		for(int i=0; i<list.size(); i++){
//			OrderBuyBean ob = list.get(0);
//			System.out.println(ob.getBuyid()+"--"+ob.getBuyuser()+"--"+ob.getId());
//		}
		
//		OrderBuyBean ob2 = buyService.getById(2);
//		System.out.println(ob2.getBuyid()+"--"+ob2.getBuyuser()+"--"+ob2.getId()+"--"+ob2.getTime());
		
//		outIdBean ob = iWarehouseService.findOutId(0);
//		System.out.println(ob.getEmail());
//		OrderAddress oa = iWarehouseService.getAddressByOrderID("O2253294600230983");   //取地址
//		List<PurchaseDetailsBean> oa = iWarehouseService.getPurchaseDetails("O2253294600230983");   //取地址
		
//		List<PurchaseBean> oa = iWarehouseService.getOutByID("1648","");   //取地址
//		System.out.println(oa.get(0).getName()+"---"+oa.get(0).getOrderNo());
		
//		Map<String,String> map= new HashMap<String,String>();
//		map.put("orderId","31");
//		map.put("goodId","31");
//		map.put("goodStatus","3");
//		map.put("goodUrl","31");
//		map.put("barCode","7888991");
//		map.put("useridV","31");
//		map.put("usernameV","31");
//		map.put("tborderidV","31");
		
		
		
		
//		iWarehouseService.callResult(map);
//		iWarehouseService.callUpdateIdrelationtable(map);
//		Map<String,String> map= new HashMap<String,String>();
//		map.put("startNum"," 0 ");
//		map.put("endNum"," 10 ");
//		
//		List<StorageLocationBean> list = iWarehouseService.getAllStorageLocationByPage(0,10);
//		//list.get(0).getOrderid()
//		
//		for(int i=0; i<list.size(); i++){
//			System.out.println(list.get(i).getOrderid());
//		}
	//	System.out.println(list);
		
//		System.out.println(map.toString());
//       return citys;
		// 5列 10行
		  //库位表生成数据
		 int t = 1001;
	      String str3;
	      String str2;
	      int a=1,b=1,c=1;
	      for(int i=1; i<=500; i++){
	    	  
	     // String str = "SHS168";
	    	  t++;
	    	  
	    	  if(c>11){  //列
	    		  b++;
	    		  c=1;
	    	  }
	    	  if(b>10){ //行
	    		  a++;
	    		  b=1;
	    		  c=1;
	    	  }
	    	   str2 = a+"区——"+b+"行——"+c+"列";
	  	     str3 = a+"-"+b+"-"+c;
	  	     String str = "SHCR";
	  	     if(b==10 ||a>=10 ||c>=10)
	  	     {
	  	    	String tb="-0"+b;
	  	    	String ta="-0"+a;
	  	    	String tc="-0"+c;
	  	       if(b==10){
	  	    	 tb="-"+b;
	  	       }
	  	       if(a>=10){
	  	    	 ta="-"+a;
	  	       }
	  	       if(c>=10){
	  	    	 tc="-"+c;
	  	       }
	  	       str = str+ta+tb+tc;
	  	     }
	  	     else
	  	     {
	  	    	 str = str+"-0"+a+"-0"+b+"-0"+c;
	  	     }
	 		
	 	
	 	     
	    	  System.out.println(str+"     "+str2);
	    	 
//	    		try {
//	    			JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(),
//	    					WidthCodedPainter.getInstance(),
//	    					EAN13TextPainter.getInstance());
//	    			 localJBarcode.setEncoder(Code39Encoder.getInstance());  
//	    		      localJBarcode.setPainter(WideRatioCodedPainter.getInstance());  
//	    		      localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());  
//	    		      localJBarcode.setShowCheckDigit(false);  
////	    			  JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());  
//
//	    			// 生成. 欧洲商品条码(=European Article Number)
//	    			// 这里我们用作图书条码
//	    			OneBarcodeUtil u = new OneBarcodeUtil();
//	    			BufferedImage localBufferedImage = localJBarcode.createBarcode(str);
//	    			u.saveToPNG(localBufferedImage, i+".png");
//
//	    		} catch (Exception localException) {
//	    			localException.printStackTrace();
//	    		}	    	  
	    	  iWarehouseService.insertStorage_location(str, a+"", b+"", c+"", str3);
	    	 
	    	   c++;
	      }
//	      
	}
}
