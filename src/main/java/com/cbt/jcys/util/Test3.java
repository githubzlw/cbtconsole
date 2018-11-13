package com.cbt.jcys.util;

import org.jbarcode.encode.InvalidAtributeException;

import java.io.IOException;

public class Test3 {
	public static void main(String[] args) throws InvalidAtributeException,
			IOException {
//
//		// 支持EAN13, EAN8, UPCA, UPCE, Code 3 of 9, Codabar, Code 11
//		JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),
//				WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
//		// 生成. 欧洲商品条码(=European Article Number)
//		// 这里我们用作图书条码
//		BufferedImage localBufferedImage = null;
//
//		int t = 1001;
//		String str3;
//		String str2;
//		int a = 1, b = 1, c = 1;
//		for (int i = 1; i <= 150; i++) {
//
//			// String str = "SHS168";
//			t++;
//
//			if (c > 5) { // 列
//				b++;
//				c = 1;
//			}
//			if (b > 10) { // 行
//				a++;
//				b = 1;
//				c = 1;
//			}
//
//			str2 = a + "区——" + b + "行——" + c + "列";
//			str3 = a + "-" + b + "-" + c;
//			String str = "SHCR";
//			if (b == 10) {
//				str = str + "00" + a + "0" + b + "00" + c;
//			} else {
//				str = str + "00" + a + "00" + b + "00" + c;
//			}
//
//			System.out.println(str + "     " + str2);
//
//			localBufferedImage = localJBarcode.createBarcode(str);
//			File f = new File("F:\\imgs\\" + i + ".jpg");
//			ImageIO.write(localBufferedImage, "jpg", f);
//			c++;
	
			
			
//		}
		String order = "1234_1";
		System.out.println(order.substring(0,order.indexOf("_1")));
	}
	
}
