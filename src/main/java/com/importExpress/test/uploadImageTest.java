//package com.importExpress.test;
//
//
//import com.cbt.FtpUtil.ContinueFTP2;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.ConvolveOp;
//import java.awt.image.Kernel;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class uploadImageTest {
//    /**
//     * @param originalFile  原文件
//     * @param resizedFile  压缩目标文件
//     * @param quality  压缩质量（越高质量越好）
//     * @param scale  缩放比例;  1等大.
//     * @throws IOException
//     */
//    public static void resize(File originalFile, File resizedFile,double scale, float quality,int width,int hight) throws IOException {
//        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//        Image i = ii.getImage();
//        int iWidth = (int) (width * scale);
//        int iHeight = (int) (width * scale);
////        int iWidth = (int) (i.getWidth(null)*scale);
////        int iHeight = (int) (i.getHeight(null)*scale);
//        //在这你可以自定义 返回图片的大小 iWidth iHeight
//        Image resizedImage = i.getScaledInstance(iWidth,iHeight, Image.SCALE_SMOOTH);
//        // 获取图片中的所有像素
//        Image temp = new ImageIcon(resizedImage).getImage();
//        // 创建缓冲
//        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
//                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        // 复制图片到缓冲流中
//        Graphics g = bufferedImage.createGraphics();
//        // 清除背景并开始画图
//        g.setColor(Color.white);
//        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//        g.drawImage(temp, 0, 0, null);
//        g.dispose();
//        // 柔和图片.
//        float softenFactor =0.05f;
//        float[] softenArray = { 0, softenFactor, 0, softenFactor,
//                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
//        Kernel kernel = new Kernel(3, 3, softenArray);
//        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        bufferedImage = cOp.filter(bufferedImage, null);
//        FileOutputStream out = new FileOutputStream(resizedFile);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
//        param.setQuality(quality, true);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(bufferedImage);
//        bufferedImage.flush();
//        out.close();
//    }
//    public static void main(String[] args) {
//        String imgPath = "I:/5.jpg";
//        //压缩图片
//        try {
//            resize(new File(imgPath), new File(imgPath), 1.00, 0.9f,640,640);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String imgName = imgPath.split("/")[1];
//        ContinueFTP2 f1=new ContinueFTP2("104.247.194.50", "importweb", "importftp@123", "21",
//                "/stock_picture/"+imgName,imgPath);
//        f1.start();
//    }
//}
