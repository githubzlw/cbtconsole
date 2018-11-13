package com.cbt.website.quartz;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;

public class UpdateHotGoodsPrice extends QuartzJobBean {
    private static final Log logger = LogFactory.getLog(UpdateHotGoodsPrice.class);
    private static final String ACCESS_URL = "https://www.import-express.com/popProducts/refreshHotJson";
    //private static final String ACCESS_URL = "http://127.0.0.1:8087/popProducts/refreshHotJson";

    public void testFun() throws Exception {
        System.out.println("UpdateHotGoodsPrice testFun begin...");
        logger.info("UpdateHotGoodsPrice testFun begin...");
    }


    public void updateHotGoodsJson() {
        System.out.println("updateHotGoodsJson begin...");
        logger.info("updateHotGoodsJson begin...");

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(ACCESS_URL).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String resultStr = response.body().string();
            if ("1".equals(resultStr)) {
                System.out.println("updateHotGoodsJson success !!!");
            } else {
                System.err.println("updateHotGoodsJson error :<:<:<");
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("updateHotGoodsJson error :" + e.getMessage());
        }
        System.out.println("updateHotGoodsJson end!!");
        logger.info("updateHotGoodsJson end!!");
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
