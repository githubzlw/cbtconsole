package com.cbt.parse.service;

import com.cbt.bean.GoodsFarBean;
import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class SimilarImageSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> hashCodes = new ArrayList<String>();
	    
	    String filename = ImageHelper.path + "\\images\\";
	    String hashCode = null;
		
	    for (int i = 0; i < 6; i++)
        {
		    hashCode = produceFingerPrint(filename + "example" + (i + 1) + ".jpg");
		    hashCodes.add(hashCode);
        }	    
	    System.out.println("Resources: ");
	    System.out.println(hashCodes);
	    System.out.println();
	    
		String sourceHashCode = produceFingerPrint(filename + "source.jpg");
		System.out.println("Source: ");
		System.out.println(sourceHashCode);
		System.out.println();
		
		for (int i = 0; i < hashCodes.size(); i++)
        {
		    int difference = hammingDistance(sourceHashCode, hashCodes.get(i));
		    System.out.print("汉明距离:"+difference+"     ");
		    if(difference==0){
		    	System.out.println("source.jpg图片跟example"+(i+1)+".jpg一样");
		    }else if(difference<=5){
		    	System.out.println("source.jpg图片跟example"+(i+1)+".jpg非常相似");
		    }else if(difference<=10){
		    	System.out.println("source.jpg图片跟example"+(i+1)+".jpg有点相似");
		    }else if(difference>10){
		    	System.out.println("source.jpg图片跟example"+(i+1)+".jpg完全不一样");
		    }
        }
		
	}

	public static void pictureCompare(){
		
		
		
	    String aliFileName = "F:/img/";
	    String taobaoFileName = "F:/imgTb/";
	    
		
	    
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		//取得比较最大商品id
		int maxCount = ips.getCpMaxCount();

		List<GoodsFarBean> goodsFarBeans = ips.findByCpPicture(maxCount);
		
		String aliHashCode = null;
	    String tbHashCode = null;
		List<String> aliList = new ArrayList<String>();
		List<String> tbList = new ArrayList<String>();
		if(goodsFarBeans !=null){
			for(int i=0; i<goodsFarBeans.size(); i++){
				
				aliHashCode = produceFingerPrint(aliFileName +goodsFarBeans.get(i).getTbPId()+"/ali/ali1.jpg");
			    aliList.add(aliHashCode);
			    tbHashCode = produceFingerPrint(taobaoFileName +goodsFarBeans.get(i).getTbPId()+"/taobao/taobao1.jpg");
			    tbList.add(tbHashCode);
			}
		}
		
		for (int i = 0; i < goodsFarBeans.size(); i++)
        {
		    int difference0 = hammingDistance(tbList.get(i), aliList.get(i));
		    //更新相似度
			int count = ips.updateSimilarity(goodsFarBeans.get(i).getTbPId(), difference0,0);
			if(count==1){
				System.out.println("sucess");
			}else{
				System.out.println("faild");
			}
        }
		
//		//taobao2
//		String aliHashCode1 = null;
//	    String tbHashCode1 = null;
//		List<String> aliList1 = new ArrayList<String>();
//		List<String> tbList1 = new ArrayList<String>();
//		if(goodsFarBeans !=null){
//			for(int i=0; i<goodsFarBeans.size(); i++){
//				
//				aliHashCode1 = produceFingerPrint(aliFileName +goodsFarBeans.get(i).getTbPId()+"/ali/ali1.jpg");
//			    aliList1.add(aliHashCode1);
//			    tbHashCode1 = produceFingerPrint(taobaoFileName +goodsFarBeans.get(i).getTbPId()+"/taobao/taobao2.jpg");
//			    tbList1.add(tbHashCode1);
//			}
//		}
//		
//		for (int i = 0; i < goodsFarBeans.size(); i++)
//        {
//		    int difference1 = hammingDistance(tbList1.get(i), aliList1.get(i));
//		    //更新相似度
//			int count = ips.updateSimilarity(goodsFarBeans.get(i).getTbPId(), difference1,1);
//			if(count==1){
//				System.out.println("sucess");
//			}else{
//				System.out.println("faild");
//			}
//        }
//		
//		//taobao3
//		String aliHashCode2 = null;
//	    String tbHashCode2 = null;
//		List<String> aliList2 = new ArrayList<String>();
//		List<String> tbList2 = new ArrayList<String>();
//		if(goodsFarBeans !=null){
//			for(int i=0; i<goodsFarBeans.size(); i++){
//				
//				aliHashCode2 = produceFingerPrint(aliFileName +goodsFarBeans.get(i).getTbPId()+"/ali/ali1.jpg");
//			    aliList2.add(aliHashCode2);
//			    tbHashCode2 = produceFingerPrint(taobaoFileName +goodsFarBeans.get(i).getTbPId()+"/taobao/taobao3.jpg");
//			    tbList2.add(tbHashCode2);
//			}
//		}
//		
//		for (int i = 0; i < goodsFarBeans.size(); i++)
//        {
//		    int difference2 = hammingDistance(tbList2.get(i), aliList2.get(i));
//		    //更新相似度
//			int count = ips.updateSimilarity(goodsFarBeans.get(i).getTbPId(), difference2,2);
//			if(count==1){
//				System.out.println("sucess");
//			}else{
//				System.out.println("faild");
//			}
//        }
//		
//		//taobao4
//		String aliHashCode3 = null;
//	    String tbHashCode3 = null;
//		List<String> aliList3 = new ArrayList<String>();
//		List<String> tbList3 = new ArrayList<String>();
//		if(goodsFarBeans !=null){
//			for(int i=0; i<goodsFarBeans.size(); i++){
//				
//				aliHashCode3 = produceFingerPrint(aliFileName +goodsFarBeans.get(i).getTbPId()+"/ali/ali1.jpg");
//			    aliList3.add(aliHashCode3);
//			    tbHashCode3 = produceFingerPrint(taobaoFileName +goodsFarBeans.get(i).getTbPId()+"/taobao/taobao4.jpg");
//			    tbList3.add(tbHashCode3);
//			}
//		}
//		
//		for (int i = 0; i < goodsFarBeans.size(); i++)
//        {
//		    int difference3 = hammingDistance(tbList3.get(i), aliList3.get(i));
//		    //更新相似度
//			int count = ips.updateSimilarity(goodsFarBeans.get(i).getTbPId(), difference3,3);
//			if(count==1){
//				System.out.println("sucess");
//			}else{
//				System.out.println("faild");
//			}
//        }
	}
	
	/**
	 * 计算"汉明距离"（Hamming distance）。
	 * 如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。
	 * @param sourceHashCode 源hashCode
	 * @param hashCode 与之比较的hashCode
	 */
	public static int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();
		
		for (int i = 0; i < len; i++) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				difference ++;
			} 
		}
		
		return difference;
	}

	/**
	 * 生成图片指纹
	 * @param filename 文件名
	 * @return 图片指纹
	 */
	public static String produceFingerPrint(String filename) {
		BufferedImage source = ImageHelper.readPNGImage(filename);// 读取文件

		int width = 8;
		int height = 8;
		
		// 第一步，缩小尺寸。
		// 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
		BufferedImage thumb = ImageHelper.thumb(source, width, height, false);
		
		// 第二步，简化色彩。
		// 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
		int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i * height + j] = ImageHelper.rgbToGray(thumb.getRGB(i, j));
			}
		}
		
		// 第三步，计算平均值。
		// 计算所有64个像素的灰度平均值。
		int avgPixel = ImageHelper.average(pixels);
		
		// 第四步，比较像素的灰度。
		// 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
		int[] comps = new int[width * height];
		for (int i = 0; i < comps.length; i++) {
			if (pixels[i] >= avgPixel) {
				comps[i] = 1;
			} else {
				comps[i] = 0;
			}
		}
		
		// 第五步，计算哈希值。
		// 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < comps.length; i+= 4) {
			int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
			hashCode.append(binaryToHex(result));
		}
		
		// 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。
		return hashCode.toString();
	}

	/**
	 * 二进制转为十六进制
	 * @param int binary
	 * @return char hex
	 */
	private static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary)
		{
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}

}
