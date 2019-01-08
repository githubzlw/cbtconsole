package com.cbt.util;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片分辨率压缩
 *
 */
public class ImageCompression {

	/**
	 * 检查图片分辨率
	 * 
	 * @param imgUrl
	 *            : 图片全路径
	 * @param width
	 *            : 宽度大小
	 * @param height
	 *            : 高度大小
	 * @return
	 */
	public static boolean checkImgResolution(String imgUrl, int width, int height) {
		boolean is = false;
		try {
			File srcfile = new File(imgUrl);
			// 检查文件是否存在
			if (srcfile.exists()) {
				if (width > 0 && height > 0) {
					int[] results = getImgWidth(srcfile);
					is = (results[0] >= width && results[1] >= height);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return is;
	}

	/**
	 * 采用指定宽度、高度或压缩比例 的方式对图片进行压缩
	 * 
	 * @param imgUrl
	 *            源图片地址
	 * @param targetImgUrl
	 *            目标图片地址
	 * @param width
	 *            压缩后图片宽度（当rate < 0.1时，必传）
	 * @param height
	 *            压缩后图片高度（当rate < 0.1时，必传）
	 * @param rate
	 *            压缩比例 必须 >= 0.1
	 */
	public static boolean reduceImg(String imgUrl, String targetImgUrl, int width, int height, double rate) {
		boolean is = false;
		FileOutputStream out = null;
		try {
			File srcfile = new File(imgUrl);
			// 检查文件是否存在
			if (srcfile.exists()) {
				int[] results = getImgWidth(srcfile);
				if (rate >= 0.1) {
					width = (int) (results[0] * rate);
					height = (int) (results[1] * rate);
				} else {
					// 只提供压缩，超过原图片分辨率的终止执行
					if (!(width >= results[0] || height >= results[1])) {
						return is;
					}
				}
				readAndReduceImg(srcfile,width,height,targetImgUrl);
				is = true;
			}
		} catch (IOException ex) {
			ex.getStackTrace();
			System.out.println("imgUrl:" + imgUrl + ",targetImgUrl" + targetImgUrl);
			System.out.print(ex.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return is;
	}

	/**
	 * 根据宽度压缩图片，高度等比例压缩
	 * 
	 * @param width
	 *            压缩宽度，必填
	 * @param imgUrl
	 *            源图片地址
	 * @param targetImgUrl
	 *            目标图片地址
	 */
	public static boolean reduceImgByWidth(double width, String imgUrl, String targetImgUrl) {
		boolean is = false;
		FileOutputStream out = null;
		try {
			File srcfile = new File(imgUrl);
			// 检查文件是否存在
			if (srcfile.exists()) {
				int[] results = getImgWidth(srcfile);
				// 只提供压缩，判断宽度小于width或者高度小于10分辨率的图片终止执行
				if (width <= 0 || results[0] < width || results[1] < 10) {
					return is;
				}
				double rate = width / results[0];
				int height = (int) (results[1] * rate);
				readAndReduceImg(srcfile,(int)width,height,targetImgUrl);
				is = true;
			}
		} catch (IOException ex) {
			ex.getStackTrace();
			System.out.println("imgUrl:" + imgUrl + ",targetImgUrl" + targetImgUrl);
			System.out.print(ex.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return is;
	}


	private static void readAndReduceImg(File srcfile,int width,int height,String targetImgUrl) throws IOException{
		// 开始读取文件并进行压缩
				//宽度压缩
				Image src = javax.imageio.ImageIO.read(srcfile);
				BufferedImage tag;
				if(targetImgUrl.contains(".png") || targetImgUrl.contains(".PNG")){
					tag = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
				}else{
					tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				}

				// tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


				tag.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
				//out = new FileOutputStream(targetImgUrl);
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				//质量压缩
//				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(tag);
//		        param.setQuality(quality, true);
//		        encoder.setJPEGEncodeParam(param);
//				encoder.encode(tag);
				String fileSuffix = targetImgUrl.substring(targetImgUrl.lastIndexOf(".") + 1);
				ImageIO.write(tag,fileSuffix,new File(targetImgUrl));
	}

	/**
	 * 获取图片宽度
	 * 
	 * @param file
	 *            图片文件
	 * @return 宽度
	 */
	public static int[] getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图宽
			result[1] = src.getHeight(null); // 得到源图高
		} catch (Exception e) {
			e.getStackTrace();
			System.out.print(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
//		String imgUrl = "E:/imgCompression/112.jpg";
//		System.out.println(imgUrl + ",判断宽高超过300:" + checkImgResolution(imgUrl, 300, 300));
//		System.out.println(imgUrl + ",判断宽高超过300:" + checkImgResolution(imgUrl, 300, 300));
//		System.out.println(imgUrl + ",判断宽高超过400:" + checkImgResolution(imgUrl, 400, 400));
//		String targetImgUrl = "E:/imgCompression/112-2.jpg";
//		System.out.println(imgUrl + ",压缩比例0.9:" + reduceImg(imgUrl, targetImgUrl, 0, 0, 0.9));
//		String targetImgUrl1 = "E:/imgCompression/112-400.jpg";
//		System.out.println(imgUrl + ",标准宽度400压缩:" + reduceImgByWidth(400.00, imgUrl, targetImgUrl1));
//
//		System.out.println("===========================================");
//
//		String imgUrl2 = "E:/imgCompression/223.jpg";
//		System.out.println(imgUrl2 + ",判断宽高超过300:" + checkImgResolution(imgUrl2, 300, 300));
//		System.out.println(imgUrl2 + ",判断宽高超过400:" + checkImgResolution(imgUrl2, 400, 400));
//		String targetImgUrl2 = "E:/imgCompression/223-2.jpg";
//		System.out.println(imgUrl2 + ",压缩比例0.9:" + reduceImg(imgUrl2, targetImgUrl2, 0, 0, 0.9));
//		String targetImgUrl3 = "E:/imgCompression/223_400x400.jpg";
//		System.out.println(imgUrl2 + ",标准宽度400压缩:" + reduceImgByWidth(400.00, imgUrl2, targetImgUrl3));
//		String targetImgUrl4 = "E:/imgCompression/223_100x100.jpg";
//		System.out.println(imgUrl2 + ",标准宽度100压缩:" + reduceImgByWidth(100.00, imgUrl2, targetImgUrl4));

		String imgUrl2 = "E:/imgCompression/00003333r.png";
		String targetImgUrl4 = "E:/imgCompression/0000667778.png";
		System.out.println(imgUrl2 + ",标准宽度700压缩:" + reduceImgByWidth(700.00, imgUrl2, targetImgUrl4));
	}

}
