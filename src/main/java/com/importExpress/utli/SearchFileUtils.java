package com.importExpress.utli;

import com.cbt.FtpUtil.ContinueFTP2;
import com.cbt.parse.service.ImgDownload;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 2018/11/22 17:12 ly
 * 关键词专页 品类精研 等图片上传到本地及上传到图片服务器端 通用方法
 */
public class SearchFileUtils {

    //线上服务器地址
    public static String importexpressPath = "https://www.import-express.com";
    public static String kidsPath = "https://www.kidsproductwholesale.com";
    public static String petsPath = "https://www.lovelypetsupply.com";

    //上传及访问路径
    public static String IMAGEHOSTURL;// http://192.168.1.9/editimg/shopimgzip/research/
    public static String LOCALPATH;// /data/cbtconsole/cbtimg/editimg/shopimgzip/
    public static String LOCALPATHZIPIMG;// /data/cbtconsole/cbtimg/editimg/shopimgzip/research/
    public static String IMAGESEARCHURL;// http://img1.import-express.com/importcsvimg/stock_picture/researchimg/

    //ftp连接信息
    public static String ftpURL;
    public static String ftpPort;
    public static String ftpUserName;
    public static String ftpPassword;

    static {
        FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
        String localShowPath = ftpConfig.getLocalShowPath();
        String localDiskPath = ftpConfig.getLocalDiskPath();
        String remoteShowPath = ftpConfig.getRemoteShowPath();
        if (StringUtils.isNotBlank(localShowPath)) {
            IMAGEHOSTURL = (localShowPath.endsWith("/") ? localShowPath : localShowPath + "/") + "shopimgzip/research/";
        }
        if (StringUtils.isNotBlank(localDiskPath)) {
            LOCALPATH = (localDiskPath.endsWith("/") ? localDiskPath : localDiskPath + "/") + "shopimgzip/";
            LOCALPATHZIPIMG = LOCALPATH + "research/";
        }
        if (StringUtils.isNotBlank(remoteShowPath)) {
            IMAGESEARCHURL = ((remoteShowPath.endsWith("/") ? remoteShowPath : remoteShowPath + "/") + "stock_picture/researchimg/").replace("http:", "https:");
        }
        ftpURL = ftpConfig.getFtpURL();
        ftpPort = ftpConfig.getFtpPort();
        ftpUserName = ftpConfig.getFtpUserName();
        ftpPassword = ftpConfig.getFtpPassword();
    }


    /**
     * 获取图片宽度
     *
     * @param file 图片文件
     * @return 宽度
     */
    public static int[] getImgWidth(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = {0, 0};

        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            result[1] = src.getHeight(null);  // 得到源图高
            result[0] = src.getWidth(null);  // 得到源图宽
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

    /**
     * 图片压缩
     *
     * @param sourceImgUrl
     *            源图片地址
     * @param targetImgUrl
     *            目标图片地址
     * @param width
     *            压缩后图片宽度
     * @param height
     *            压缩后图片高度
     * @param rate
     *            压缩比例(最终宽高等于原宽高乘以压缩比例)
     * @param flag
     *            压缩类型 1-指定宽和高；2-根据指定宽等比压缩；3-根据比例等比压缩；4- 2和3结合到一起的压缩方式
     */
    public static boolean reduceImgOnlyWidth(String sourceImgUrl, String targetImgUrl, Integer width, Integer height, Double rate, Integer flag) {
        boolean is = false;
        FileOutputStream out = null;
        try {
            File srcfile = new File(sourceImgUrl);
            // 检查图片文件是否存在
            if (!srcfile.exists()) {
                System.out.println("文件不存在");
            }
            //原宽高
            int[] results = getImgWidth(srcfile);
            switch (flag) {
                case 1:
                    break;
                case 2:
                    height = (int) (width * results[1] / results[0]);
                    break;
                case 3:
                    width = (int) (results[0] * rate);
                    height = (int) (results[1] * rate);
                    break;
                case 4:
                    height = (int) (width * results[1] * rate / results[0]);
                    width = (int) (width * rate);
                    break;
            }
            // 开始读取文件并进行压缩
            Image src = javax.imageio.ImageIO.read(srcfile);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            out = new FileOutputStream(targetImgUrl);
            String fileSuffix = targetImgUrl.substring(targetImgUrl.lastIndexOf(".") + 1);
            ImageIO.write(tag, fileSuffix, new File(targetImgUrl));
            is = true;

        } catch (IOException ex) {
            ex.getStackTrace();
            System.out.println("sourceImgUrl:" + sourceImgUrl + ",targetImgUrl" + targetImgUrl);
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

    public static void outputImage(String srcPath, String destPath) {
        // 打开输入流
        try {
            FileInputStream fis = new FileInputStream(srcPath);
            // 打开输出流
            FileOutputStream fos = new FileOutputStream(destPath);

            // 读取和写入信息
            int len = 0;
            // 创建一个字节数组，当做缓冲区
            byte[] b = new byte[1024];
            while ((len = fis.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            // 关闭流  先开后关  后开先关
            fos.close(); // 后开先关
            fis.close(); // 先开后关
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据宽度检测图片是否需要压缩
     *
     * @param imgUrl
     *            : 图片全路径
     * @param width
     *            : 宽度大小
     * @return false 不需要压缩；true 需要压缩
     */
    public static boolean checkImgResolution(String imgUrl, int width) {
        boolean boo = false;
        try {
            File srcfile = new File(imgUrl);
            // 检查文件是否存在
            if (srcfile.exists()) {
                if (width > 0) {
                    int[] results = getImgWidth(srcfile);
                    return results[0] > width;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return boo;
    }
    /**
     * 获取文件大小
     *
     * @return 返回文件大小
     */
    public static Long getImgSize(String fileName) {
        File file = new File(LOCALPATHZIPIMG, fileName);
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0L;
    }


    /**
     * @param file
     *            上传上来的原文件
     * @param sid
     *            保存的文件夹名
     * @param width
     *            压缩后图片宽度
     * @param height
     *            压缩后图片高度
     * @param rate
     *            压缩比例(最终宽高等于原宽高乘以压缩比例)
     * @param flag
     *            压缩类型 0-不压缩；1-指定宽和高；2-根据指定宽等比压缩；3-根据比例等比压缩；4- 2和3结合到一起的压缩方式
     * @return 返回数组
     *            第一个是 保存在后台服务器本地路径 可 加 IMAGEHOSTURL 前缀进行访问
     *            第二个是 上传到图片服务器中访问地址
     *            第三个是 上传的原文件名
     */
    public static String[] comFileUpload(MultipartFile file, String sid, Integer width, Integer height, Double rate, Integer flag) throws Exception{//sid 保存的中间文件夹名
        if (!file.isEmpty()) {
            //接收到的文件名
            String originalName = file.getOriginalFilename();
            //文件后缀
            String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
            //保存到本地文件名(时间戳加随机数组成)
            String saveFileName = System.currentTimeMillis() + "_" + (String.valueOf(Math.random()).substring(2,7))  + fileSuffix;
            //最终本地保存路径
            String targetImgUrl = LOCALPATHZIPIMG + sid + "/" + saveFileName;
            File file2 = new File(LOCALPATHZIPIMG + sid);
            if (!file2.exists()) {
                file2.mkdirs();
            }

            if (flag == 0){
                //不需要压缩
                ImgDownload.writeImageToDisk(file.getBytes(), targetImgUrl);
            } else {
                //需要压缩
                //保存到本地的原文件路径
                String localFilePath = LOCALPATH + sid + "/" + saveFileName;
                // 文件流输出到本地服务器指定路径
                ImgDownload.writeImageToDisk(file.getBytes(), localFilePath);
                switch (flag) {
                    case 1:
                        reduceImgOnlyWidth(localFilePath, targetImgUrl, width, height, null, flag);
                        break;
                    case 2:
                        reduceImgOnlyWidth(localFilePath, targetImgUrl, width, null, null, flag);
                        break;
                    case 3:
                        reduceImgOnlyWidth(localFilePath, targetImgUrl, width, height, null, flag);
                        break;
                    case 4:
                        reduceImgOnlyWidth(localFilePath, targetImgUrl, width, null, null, flag);
                        break;
                }
            }
            // 支持断点续存上传图片
            ContinueFTP2 f1 = new ContinueFTP2(ftpURL, ftpUserName, ftpPassword, ftpPort, "/stock_picture/researchimg/AuthorizedFile/" + saveFileName, targetImgUrl);
            // 远程上传到图片服务器
            f1.start();
            String[] result = new String[3];
            result[0] = IMAGEHOSTURL + sid + "/" + saveFileName;
            result[1] = IMAGESEARCHURL + "AuthorizedFile/" + saveFileName;
            result[2] = originalName;
            return result;
        }
        return null;
    }

}
