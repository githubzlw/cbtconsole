//package com.cbt.jcys.util;
//
//import org.jbarcode.JBarcode;
//import org.jbarcode.encode.Code128Encoder;
//import org.jbarcode.encode.InvalidAtributeException;
//import org.jbarcode.paint.EAN13TextPainter;
//import org.jbarcode.paint.WidthCodedPainter;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class TestBarCode {
//	public static void main(String[] args) throws InvalidAtributeException, IOException {
//		//支持EAN13, EAN8, UPCA, UPCE, Code 3 of 9, Codabar, Code 11
//	      JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
//	      //生成. 欧洲商品条码(=European Article Number)
//	      //这里我们用作图书条码
//	      BufferedImage localBufferedImage=null;
//	      int t = 1001;
//
//
//
//
//	      for(int i=1; i<=150; i++){
//
//	    	  String str = "SHS168"+t;
//	    	  t++;
//		      localBufferedImage = localJBarcode.createBarcode(str);
//		      File f = new File("F:\\imgs\\"+i+".jpg");
//		      ImageIO.write(localBufferedImage, "jpg", f);
//
//
//	      }
//
////	      File file =
//	}
//}
