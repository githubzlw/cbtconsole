package com.cbt.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @ClassName ImagePHashUtil
 * @Description 汉明距离比较工具类
 * @author Jxw
 * @date 2018年3月30日
 */
public class ImagePHashUtil {
	private static int SIZE = 32;
	private static int SMALLERSIZE = 8;
	private static double[] COEFF = null;

	static {
		initCoefficients();
	}

	public static int genDistance(String originalfileName, String checkfileName) {
		File originalfile = new File(originalfileName);
		if (!originalfile.exists()) {
			System.err.println("[" + originalfileName + "]@[" + checkfileName + "] result:[-1]");
			return -1;
		}
		File checkfile = new File(checkfileName);
		if (!checkfile.exists()) {
			System.err.println("[" + originalfileName + "]@[" + checkfileName + "] result:[-1]");
			return -1;
		}
		InputStream originalIn = null;
		InputStream checkIn = null;
		try {
			originalIn = new FileInputStream(originalfile);
			checkIn = new FileInputStream(checkfile);
			String image1 = getHash(originalIn);
			String image2 = getHash(checkIn);
			int rs = distance(image1, image2);
			if (rs < 4) {
				System.err.println("[" + originalfileName + "]@[" + checkfileName + "] result:[" + rs + "]");
			} else {
				// System.out.println("[" + originalfileName + "]@[" +
				// checkfileName + "] result:[" + rs + "]");
			}
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("genDistance error:" + e.getMessage());
			System.err.println("[" + originalfileName + "]@[" + checkfileName + "] result:[-1]");
		} finally {
			try {
				if (originalIn != null) {
					originalIn.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (checkIn != null) {
					checkIn.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.err.println("[" + originalfileName + "]@[" + checkfileName + "] result:[-1]");
		return -1;
	}

	public static int genDistanceWithInputStream(InputStream originalIn, InputStream checkIn) {
		if (originalIn == null) {
			System.err.println("[获取原始文件流失败] result:[-1]");
			return -1;
		}
		if (checkIn == null) {
			System.err.println("[获取比较文件流失败] result:[-1]");
			return -1;
		}
		try {
			String image1 = getHash(originalIn);
			String image2 = getHash(checkIn);
			int rs = distance(image1, image2);
			if (rs < 4) {
				//System.err.println("result:[" + rs + "]");
			} else {
				// System.out.println("[" + originalfileName + "]@[" +
				// checkfileName + "] result:[" + rs + "]");
			}
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("genDistance error:[" + e.getMessage() + "]-- result:[-1]");
		}
		return -1;
	}
	
	public static int genDistanceWithBufferedImage(BufferedImage originalimg, BufferedImage checkimg) {
		if (originalimg == null) {
			System.err.println("[获取原始文件流失败] result:[-1]");
			return -1;
		}
		if (checkimg == null) {
			System.err.println("[获取比较文件流失败] result:[-1]");
			return -1;
		}
		try {
			String image1 = getHashWithBufferedImage(originalimg);
			String image2 = getHashWithBufferedImage(checkimg);
			int rs = distance(image1, image2);
			if (rs < 4) {
				//System.err.println("result:[" + rs + "]");
			} else {
				// System.out.println("[" + originalfileName + "]@[" +
				// checkfileName + "] result:[" + rs + "]");
			}
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("genDistance error:[" + e.getMessage() + "]-- result:[-1]");
		}
		return -1;
	}

	private static int distance(String s1, String s2) {
		int counter = 0;
		for (int k = 0; k < s1.length(); k++) {
			if (s1.charAt(k) != s2.charAt(k)) {
				counter++;
			}
		}
		return counter;
	}

	private static void initCoefficients() {
		COEFF = null;
		COEFF = new double[SIZE];
		for (int i = 1; i < SMALLERSIZE; i++) {
			COEFF[i] = 1;
		}
		COEFF[0] = 1 / Math.sqrt(2.0);
	}

	// Returns a 'binary string' (like. 001010111011100010) which is easy to do
	// a hamming distance on.
	private static String getHash(InputStream is) throws Exception {
		BufferedImage img = ImageIO.read(is);

		/*
		 * 1. Reduce size. Like Average Hash, pHash starts with a small image.
		 * However, the image is larger than 8x8; 32x32 is a good size. This is
		 * really done to simplify the DCT computation and not because it is
		 * needed to reduce the high frequencies.
		 */
		img = resize(img, SIZE, SIZE);

		/*
		 * 2. Reduce color. The image is reduced to a grayscale just to further
		 * simplify the number of computations.
		 */
		img = grayscale(img);

		double[][] vals = new double[SIZE][SIZE];

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				vals[x][y] = getBlue(img, x, y);
			}
		}

		/*
		 * 3. Compute the DCT. The DCT separates the image into a collection of
		 * frequencies and scalars. While JPEG uses an 8x8 DCT, this algorithm
		 * uses a 32x32 DCT.
		 */
		double[][] dctVals = applyDCT(vals);
		// System.out.println("DCT: " + (System.currentTimeMillis() - start));

		/*
		 * 4. Reduce the DCT. This is the magic step. While the DCT is 32x32,
		 * just keep the top-left 8x8. Those represent the lowest frequencies in
		 * the picture.
		 */
		/*
		 * 5. Compute the average value. Like the Average Hash, compute the mean
		 * DCT value (using only the 8x8 DCT low-frequency values and excluding
		 * the first term since the DC coefficient can be significantly
		 * different from the other values and will throw off the average).
		 */
		double total = 0;

		for (int x = 0; x < SMALLERSIZE; x++) {
			for (int y = 0; y < SMALLERSIZE; y++) {
				total += dctVals[x][y];
			}
		}
		total -= dctVals[0][0];

		double avg = total / (double) ((SMALLERSIZE * SMALLERSIZE) - 1);

		/*
		 * 6. Further reduce the DCT. This is the magic step. Set the 64 hash
		 * bits to 0 or 1 depending on whether each of the 64 DCT values is
		 * above or below the average value. The result doesn't tell us the
		 * actual low frequencies; it just tells us the very-rough relative
		 * scale of the frequencies to the mean. The result will not vary as
		 * long as the overall structure of the image remains the same; this can
		 * survive gamma and color histogram adjustments without a problem.
		 */
		String hash = "";

		for (int x = 0; x < SMALLERSIZE; x++) {
			for (int y = 0; y < SMALLERSIZE; y++) {
				if (x != 0 && y != 0) {
					hash += (dctVals[x][y] > avg ? "1" : "0");
				}
			}
		}

		return hash;
	}
	
	
	
	private static String getHashWithBufferedImage(BufferedImage img) throws Exception {

		img = resize(img, SIZE, SIZE);


		img = grayscale(img);

		double[][] vals = new double[SIZE][SIZE];

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				vals[x][y] = getBlue(img, x, y);
			}
		}

	
		double[][] dctVals = applyDCT(vals);

		double total = 0;

		for (int x = 0; x < SMALLERSIZE; x++) {
			for (int y = 0; y < SMALLERSIZE; y++) {
				total += dctVals[x][y];
			}
		}
		total -= dctVals[0][0];

		double avg = total / (double) ((SMALLERSIZE * SMALLERSIZE) - 1);

		String hash = "";

		for (int x = 0; x < SMALLERSIZE; x++) {
			for (int y = 0; y < SMALLERSIZE; y++) {
				if (x != 0 && y != 0) {
					hash += (dctVals[x][y] > avg ? "1" : "0");
				}
			}
		}

		return hash;
	}
	

	private static BufferedImage grayscale(BufferedImage img) {
		colorConvert.filter(img, img);
		return img;
	}

	private static ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

	private static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	private static int getBlue(BufferedImage img, int x, int y) {
		return (img.getRGB(x, y)) & 0xff;
	}

	private static double[][] applyDCT(double[][] f) {
		int N = SIZE;

		double[][] F = new double[N][N];
		for (int u = 0; u < N; u++) {
			for (int v = 0; v < N; v++) {
				double sum = 0.0;
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI)
								* Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * (f[i][j]);
					}
				}
				sum *= ((COEFF[u] * COEFF[v]) / 4.0);
				F[u][v] = sum;
			}
		}
		return F;
	}

	public static void main(String[] args) {
//		System.out.println("1:1 Score is " + genDistance("E:/testImg/1.jpg", "E:/testImg/1.jpg"));
//		System.out.println("1:2 Score is " + genDistance("E:/testImg/1.jpg", "E:/testImg/2.jpg"));
//		System.out.println("1:3 Score is " + genDistance("E:/testImg/1.jpg", "E:/testImg/3.jpg"));
//		System.out.println("2:5 Score is " + genDistance("E:/testImg/2.jpg", "E:/testImg/5.jpg"));
//		System.out.println("3:4 Score is " + genDistance("E:/testImg/3.jpg", "E:/testImg/4.jpg"));
//		System.out.println("4:6 Score is " + genDistance("E:/testImg/4.jpg", "E:/testImg/6.jpg"));
//		System.out.println("5:6 Score is " + genDistance("E:/testImg/5.jpg", "E:/testImg/6.jpg"));
//		System.out.println("7:8 Score is " + genDistance("E:/testImg/7.jpg", "E:/testImg/8.jpg"));
//		System.out.println("7:9 Score is " + genDistance("E:/testImg/7.jpg", "E:/testImg/9.jpg"));
		String orImg ="http://117.144.21.74:8000/shopimages15/560545677938/desc/5332923967_680457220.jpg".replace("http://117.144.21.74:8000/","K:/shopimages/");
		String ckImg ="http://117.144.21.74:8000/shopimages15/560336830255/desc/5293957205_680457220.jpg".replace("http://117.144.21.74:8000/","K:/shopimages/");
		
		
		System.out.println(genDistance(orImg,ckImg));

	}
}
