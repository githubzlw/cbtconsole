package com.cbt.jcys.util;


public class Tsetbar2 {
	public static void main(String[] args) {
	      int t = 1001;
	      
	      String str2;
	      int a=1,b=1,c=1;
	      for(int i=1; i<=150; i++){
	    	  
	    	  String str = "SHS1168"+t;
	    	  t++;
	    	  
	    	  if(c>5){  //列
	    		  b++;
	    		  c=1;
	    	  }
	    	  if(b>=10){ //行
	    		  a++;
	    		  b=1;
	    		  c=1;
	    	  }
	    	  
		      str2 = (char)(a+64)+"区——"+b+"行——"+c+"列";
		      c++;
	    	  System.out.println(str+"     "+str2);
	    	  
		      
	      }
	}
}
