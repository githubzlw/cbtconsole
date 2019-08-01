package com.cbt.website.util;

import com.cbt.util.GoodsInfoUtils;
import okhttp3.*;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UploadByOkHttp {
    private static final String ACCESS_URL_OLD = "http://104.247.194.50:3009/uploadImage";
    private static final String ACCESS_URL_NEW = "http://108.61.142.103:3009/uploadImage";
    private static final String TOKEN = "cerong2018jack";

    public static boolean doUpload(Map<String, String> uploadMap, int isKids) {
        boolean isSuccess = false;
        File originFile;

        for (String mapKey : uploadMap.keySet()) {

            originFile = new File(mapKey);
            if (originFile.exists()) {
                // 重试5次
                int count = 0;
                boolean isUpload = false;
                while (!isUpload && count < 5) {
                    count++;
                    isUpload = uploadFile(originFile, uploadMap.get(mapKey), isKids);
                    if (!isUpload) {
                        // 休眠5秒
                        try {
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (isKids > 0) {
                    // 维护kids的后，维护import
                    boolean isImport = uploadFile(originFile, uploadMap.get(mapKey), 0);
                    if (!isImport) {
                        isImport = uploadFile(originFile, uploadMap.get(mapKey), 0);
                    }
                    if (!isImport) {
                        System.err.println("originFile:" + originFile.getAbsolutePath() + ",upload import path:"
                                + uploadMap.get(mapKey) + "error");
                    }
                } else {
                    // 维护import的后，维护kids(主要)
                    isUpload = false;
                    int importCount = 0;
                    while (!isUpload && importCount < 5) {
                        importCount++;
                        isUpload = uploadFile(originFile, uploadMap.get(mapKey), 1);
                        if (!isUpload) {
                            // 休眠5秒
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (isUpload) {
                    isSuccess = true;
                } else {
                    isSuccess = false;
                    break;
                }

            } else {
                System.err.println("file:" + originFile.getAbsolutePath() + " is not exists :<:<:<:<");
                isSuccess = false;
                break;
            }
        }
        if (!isSuccess) {
            System.err.println("doUploadByMap error :<:<:<:<");
        }
        return isSuccess;
    }

    public static boolean uploadFile(File originFile, String destPath, int isKids) {
        boolean isUpload = false;
        String imageType = "image/jpeg";
        if (originFile.getName().endsWith(".png")) {
            imageType = "image/png";
        }
        String uploadFileName = originFile.getName();
        String[] destPathArr = destPath.split("@@@@");
        if (destPathArr.length == 2) {
            destPath = destPathArr[0];
            uploadFileName = destPathArr[1];
        }
        try {
            String accessUrl = ACCESS_URL_OLD;
            if (isKids > 0) {
                accessUrl = ACCESS_URL_NEW;
                destPath = destPath.replace(GoodsInfoUtils.SERVICE_LOCAL_IMPORT_PATH, GoodsInfoUtils.SERVICE_LOCAL_KIDS_PATH);
            }
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//					.addFormDataPart("image", originFile.getName(),
                    .addFormDataPart("userPhoto", uploadFileName,
                            RequestBody.create(MediaType.parse(imageType), originFile))
                    .addFormDataPart("token", TOKEN).addFormDataPart("destPath", destPath).build();
            Request request = new Request.Builder().url(accessUrl).post(formBody).build();
            // client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build();
            Response response = client.newCall(request).execute();
            String rs = response.body().string();
            System.out.println(rs);
            if (rs.contains("OK")) {
                isUpload = true;
            } else {
                isUpload = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isUpload = false;
        }
        return isUpload;

    }

    public static boolean uploadFileBatch(File originFile, String destPath, int isKids) {
        boolean isUpload = false;

        try {
            String accessUrl = ACCESS_URL_OLD;
            if (isKids > 0) {
                accessUrl = ACCESS_URL_NEW;
                destPath = destPath.replace(GoodsInfoUtils.SERVICE_LOCAL_IMPORT_PATH, GoodsInfoUtils.SERVICE_LOCAL_KIDS_PATH);
            }
            if (originFile.exists() && originFile.isDirectory()) {

                File[] childFiles = originFile.listFiles();

                String imageType;
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                multipartBodyBuilder.setType(MultipartBody.FORM);

                int count = 0;
                for (File chFile : childFiles) {
                    if (chFile.isFile()) {
                        imageType = "image/jpeg";
                        if (originFile.getName().endsWith(".png")) {
                            imageType = "image/png";
                        }
                        count++;
                        //System.out.println("upload file:[" + chFile.getAbsolutePath().replace("\\", "/") +  "]");
                        multipartBodyBuilder.addFormDataPart("userPhoto", chFile.getName(),
                                RequestBody.create(MediaType.parse(imageType), chFile));
                    }
                }
                System.out.println("total size:" + count + " to destPath:" + destPath);
                RequestBody formBody = multipartBodyBuilder.addFormDataPart("token", TOKEN)
                        .addFormDataPart("destPath", destPath).build();
                Request request = new Request.Builder().url(accessUrl).post(formBody).build();
                // client = new OkHttpClient();
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(600, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).build();
                Response response = client.newCall(request).execute();
                String rs = response.body().string();
                System.out.println(rs);
                if (rs.contains("OK")) {
                    isUpload = true;
                } else {
                    isUpload = false;
                }
            } else {
                System.err.println("file" + originFile.getAbsolutePath() + " is not exist or is not directory");
                isUpload = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            isUpload = false;
        }
        return isUpload;
    }


    public static void main(String[] args) {
		/*Map<String, String> uploadMap = new HashMap<String, String>();

		for (int i = 0; i < 20; i++) {
			String sourcePrePath = "E:/";
			String sourceFileName = "3155222823_1506463280.jpg";
			if (i < 10) {
				String preFilePath = "/usr/local/goodsimg/importcsvimg/test/36996699/";
				uploadMap.put(sourcePrePath + sourceFileName, preFilePath);
			} else {
				String preFilePath = "/usr/local/goodsimg/importcsvimg/test/36996699/desc/";
				uploadMap.put(sourcePrePath + sourceFileName, preFilePath);
			}
		}
		doUpload(uploadMap);*/

        // 批量上传测试
        File testFile = new File("G:/singleimg/singleimg135/571374457941");
        String filePath = "/usr/local/goodsimg/importcsvimg/test/789456";

        uploadFileBatch(testFile, filePath, 0);
    }

}
