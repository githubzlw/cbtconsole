package com.importExpress.utli;

import com.cbt.parse.service.ImgDownload;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;


/**
 * 使用okhttp下载图片
 */
public class ImgDownByOkHttpUtils {

    public static boolean downFromImgServiceWithApache(String imgUrl, String fileName) {
        boolean isDown;
        InputStream inputStream = null;
        try {
            File downFile = new File(fileName);
            if (downFile.exists() && downFile.isFile()) {
                isDown = true;
                return isDown;
            } else if (!downFile.getParentFile().exists()) {
                downFile.getParentFile().mkdirs();
            }
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(400, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS).writeTimeout(200, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().addHeader("Connection", "close").addHeader("Accept", "*/*")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").get().url(imgUrl).build();
            Response response = client.newCall(request).execute();

            inputStream = response.body().byteStream();

            FileUtils.copyInputStreamToFile(inputStream, downFile);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("execute download[ " + imgUrl + "] error:---");
            System.err.println("--- " + e.getMessage());
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        File downFile = new File(fileName);

        if (downFile.exists()) {
            isDown = true;
        } else {
            if (downFile.exists()) {
                downFile.delete();
            }
            isDown = false;
        }
        if (!isDown) {
            System.err.println("down img [" + fileName + "],result:" + isDown);
        }
        return isDown;
    }


    public static boolean downFromImgServiceWithStream(String imgUrl, String fileName) {

        boolean isDown;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        ByteArrayOutputStream output = null;
        try {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(400, TimeUnit.SECONDS)
                    .readTimeout(200, TimeUnit.SECONDS).writeTimeout(200, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().addHeader("Connection", "close").addHeader("Accept", "*/*")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").get().url(imgUrl).build();
            Response response = client.newCall(request).execute();
            inputStream = response.body().byteStream();

            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();// 返回此抽象路径名父目录的抽象路径名；创建
            }

            fileOutputStream = new FileOutputStream(file);
            output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("execute download[ " + imgUrl + "] error:" + e.getMessage());
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        isDown = ImgDownload.checkDownFileByName(fileName);
        if (!isDown) {
            System.err.println("down img [" + fileName + "],result:" + isDown);
        }
        return isDown;
    }



    public static void main(String[] args) {
        downFromImgServiceWithApache("https://img.import-express.com/importcsvimg/shopimg3/574903499292/9193801437_713076455.220x220.jpg", "E:/data/test/11.jpg");
        downFromImgServiceWithStream("https://img.import-express.com/importcsvimg/shopimg3/574903499292/9193801437_713076455.220x220.jpg", "E:/data/test/22.jpg");
    }

}
