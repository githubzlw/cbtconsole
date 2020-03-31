package com.cbt.website.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.util.GoodsInfoUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UploadByOkHttp {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(UploadByOkHttp.class);

    private static final String ACCESS_URL_OLD = "http://104.247.194.50:3009/uploadImage";
    private static final String ACCESS_URL_NEW = "http://108.61.142.103:3009/uploadImage";
    private static final String TOKEN = "cerong2018jack";
    private static final String DELETE_URL_NEW = "http://108.61.142.103:3008/image/delete";

    public static final String SERVICE_LOCAL_IMPORT_PATH = "/usr/local/goodsimg";
    /**
     * 客户投诉上传图片位置
     */
    public static final String SERVICE_LOCAL_SERVICE_REQUEST = "/usr/local/goodsimg/importcsvimg/servicerequest/";

    public static OkHttpClient initClient() {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS).writeTimeout(150, TimeUnit.SECONDS).build();
        return client;
    }

    public static boolean doUpload(Map<String, String> uploadMap, int isKids) {
        boolean isSuccess = false;
        File originFile;

        isKids = 0;
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
                    boolean isImport = uploadFile(originFile, uploadMap.get(mapKey), isKids);
                    if (!isImport) {
                        isImport = uploadFile(originFile, uploadMap.get(mapKey), isKids);
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
            logger.error("doUploadByMap error :<:<:<:<,map:" + uploadMap);
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
        try {
            String accessUrl = ACCESS_URL_OLD;
            if (isKids > 0) {
                accessUrl = ACCESS_URL_NEW;
                destPath = destPath.replace(GoodsInfoUtils.SERVICE_LOCAL_IMPORT_PATH, GoodsInfoUtils.SERVICE_LOCAL_KIDS_PATH);
            }
            System.err.println("originFile:" + originFile.getAbsolutePath() + "@" + destPath);
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//					.addFormDataPart("image", originFile.getName(),
                    .addFormDataPart("userPhoto", originFile.getName(),
                            RequestBody.create(MediaType.parse(imageType), originFile))
                    .addFormDataPart("token", TOKEN).addFormDataPart("destPath", destPath).build();
            Request request = new Request.Builder().url(accessUrl).post(formBody).build();
            // client = new OkHttpClient();
            OkHttpClient client = initClient();
            Response response = client.newCall(request).execute();
            String rs = response.body().string();
            System.out.println(rs);
            return checkResult(1, rs);
        } catch (Exception e) {
            e.printStackTrace();
            isUpload = false;
        }
        return isUpload;

    }

    /**
     * 上传kids
     *
     * @param originFile
     * @param destPath
     * @return
     */
    public static boolean uploadFileBatchNew(File originFile, String destPath) {
        boolean isUpload = false;

        String accessUrl = ACCESS_URL_NEW;
        if (!destPath.contains(GoodsInfoUtils.SERVICE_LOCAL_KIDS_PATH)) {
            isUpload = false;
            System.err.println("destPath:" + destPath + " not kids -----");
            return isUpload;
        }
        return uploadFileBatch(originFile, destPath, accessUrl);
    }


    /**
     * 上传import
     *
     * @param originFile
     * @param destPath
     * @return
     */
    public static boolean uploadFileBatchOld(File originFile, String destPath) {
        boolean isUpload = false;

        String accessUrl = ACCESS_URL_OLD;
        if (!destPath.contains(GoodsInfoUtils.SERVICE_LOCAL_IMPORT_PATH)) {
            isUpload = false;
            System.err.println("destPath:" + destPath + " not import -----");
            return isUpload;
        }
        return uploadFileBatch(originFile, destPath, accessUrl);
    }

    /**
     * 上传kids和import
     *
     * @param originFile
     * @param destPath
     * @return
     */
    public static boolean uploadFileBatchAll(File originFile, String destPath) {
        boolean isUpload = false;

        // KIDS配置
        String accessUrl = ACCESS_URL_NEW;
        if (!destPath.contains(GoodsInfoUtils.SERVICE_LOCAL_KIDS_PATH)) {
            isUpload = false;
            System.err.println("destPath:" + destPath + " not kids -----");
            return isUpload;
        }
        isUpload = uploadFileBatch(originFile, destPath, accessUrl);
        if (isUpload) {
            // import配置
            destPath = destPath.replace(GoodsInfoUtils.SERVICE_LOCAL_KIDS_PATH, GoodsInfoUtils.SERVICE_LOCAL_IMPORT_PATH);
            accessUrl = ACCESS_URL_OLD;
            isUpload = uploadFileBatch(originFile, destPath, accessUrl);
        }
        return isUpload;
    }


    public static boolean uploadFileBatch(File originFile, String destPath, String accessUrl) {
        boolean isUpload = false;
        try {
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
                        multipartBodyBuilder.addFormDataPart("userPhoto", chFile.getName(),
                                RequestBody.create(MediaType.parse(imageType), chFile));
                    }
                }
                System.out.println("total size:" + count + " to destPath:" + destPath);
                RequestBody formBody = multipartBodyBuilder.addFormDataPart("token", TOKEN)
                        .addFormDataPart("destPath", destPath).build();
                Request request = new Request.Builder().url(accessUrl).post(formBody).build();
                // client = new OkHttpClient();
                OkHttpClient client = initClient();
                Response response = client.newCall(request).execute();
                String rs = response.body().string();
                System.out.println(rs);
                return checkResult(count, rs);
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

    /**
     * 删除图片
     *
     * @param list : 图片本地路径集合
     * @return
     */
    public static boolean deleteRemoteImgByList(List<String> list) {

        boolean isUpload = false;
        if (list == null || list.size() == 0) {
            return isUpload;
        }
        try {
            MediaType contentType = MediaType.parse("application/json; charset=utf-8");

            JSONArray jarr = new JSONArray();
            for (String tempStr : list) {
                jarr.add(tempStr);
            }
            JSONObject json = new JSONObject();

            json.put("token", TOKEN);
            json.put("crud", "d");
            json.put("paths", jarr);
            RequestBody formBody = RequestBody.create(contentType, json.toString());

            Request request = new Request.Builder().url(DELETE_URL_NEW).post(formBody).build();
            // client = new OkHttpClient();
            OkHttpClient client = initClient();
            Response response = client.newCall(request).execute();
            String rs = response.body().string();
            if (!rs.contains("ERR") && (rs.contains("OK") || rs.contains("ok"))) {
                isUpload = true;
                System.out.println("delete result[" + rs + "]");
            } else {
                isUpload = false;
                System.err.println("delete result[" + rs + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            isUpload = false;
        }
        return isUpload;
    }


    public static boolean checkResult(int fileCount, String result) {
        boolean isUpload = false;
        if (!(result.contains("err") || result.contains("ERR")) && (result.contains("OK") || result.contains("ok"))) {
            isUpload = true;
            try {
                String rs = result;
                if ("\"".equals(rs.substring(0, 1))) {
                    rs = rs.substring(1);
                }
                if (rs.endsWith("\"")) {
                    rs = rs.substring(0, rs.length() - 1);
                }
                rs = rs.replace("\\", "");
                JSONObject json = JSONObject.parseObject(rs);

                String[] resultList = json.getString("result").split(",");
                int rsCount = 0;
                for (String tempStr : resultList) {
                    if (StringUtils.isNotBlank(tempStr) && tempStr.length() > 5) {
                        rsCount++;
                    }
                }
                if (rsCount > 0 && fileCount == rsCount) {
                    isUpload = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("result:" + result + ",fileCount:" + fileCount + ",checkResult error:", e);
            }
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

        uploadFileBatchOld(testFile, filePath);
    }

}
