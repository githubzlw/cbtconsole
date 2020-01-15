package com.importExpress.utli;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取图片RGB
 *
 * @author luohao  2019/8/8
 */
public class ColorRGBUtil {
	private static final Log logger = LogFactory.getLog(ColorRGBUtil.class);

    private static ColorRGBUtil _instance = null;

    private ColorRGBUtil() {

    }

    public synchronized static ColorRGBUtil getInstance() {

        if (_instance == null) {
            _instance = new ColorRGBUtil();
        }
        return _instance;
    }

    /**
     * 判断图片底部是否有灰色（压缩受损图片）
     */
    public boolean checkImage(String filename) throws Exception {
    	boolean isSu;
    	isSu = getImagePixel(new File(filename)) > 0.8;
    	if(isSu){
    		logger.error(filename + ",检查灰图----");
    	}
        return isSu;
    }

    /**
     * 计算图片的灰色占比
     */
    public float getImagePixel(File file) throws Exception {
        int[] rgb = new int[3];

        BufferedImage bi;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.err.println("file:" + file.getAbsolutePath() + ",error:" + e.getMessage());
            throw e;
        }
        if(bi==null){
            throw new IllegalStateException("file is not image : "+file.getName());
        }
        int width = bi.getWidth();
        int height = bi.getHeight();

        // 灰色
        List<Integer> grey = new ArrayList<>(500);

        for (int i = width / 5; i < width * 4 / 5; i++) {
            for (int j = height * 4 / 5; j < height; j++) {
                int pixel = bi.getRGB(i, j);
                // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);

                if (rgb[0] == 128 && rgb[1] == 128 && rgb[2] == 128) {
                    grey.add(rgb[2]);
                }
            }
        }

        float result = (float) (grey.size() / (width * height * 0.6 * 0.2));
        //System.out.println(String.format("%s -- 灰色占比： %.2f", file.getName(), result));
        boolean isSu;
    	isSu = result > 0.8;
    	if(isSu){
    		logger.error(file.getAbsolutePath().replace("\\", "/") + ",检查灰图----");
    	}
        return result;
    }


    public static void main(String[] args) throws Exception {
    	ArrayList<String> logList = new ArrayList<String>();
        String filename;
        if (args.length==0){
            filename = "C:\\Users\\Administrator\\Desktop\\img5.csv";
        }else{
            filename = args[0];
        }
        List<String> files = Files.readAllLines(new File(filename).toPath());

        files.parallelStream().forEach( file -> {
            try {
                float result=ColorRGBUtil.getInstance().getImagePixel(new File(file));
                if(result>0.8){
                    System.err.println(String.format("ERR: file:[%s] is grey image: result:[%.2f]",file,result));
                    logList.add(String.format("ERR: file:[%s] is grey image: result:[%.2f]",file,result));
                }
            } catch (Exception e) {
                System.err.println(String.format("ERR: file:[%s] %s",file,e.getMessage()));
                logList.add(String.format("ERR: file:[%s] %s",file,e.getMessage()));
            }
        });
        
        System.out.println(1);
        
        FileUtils.writeLines(new File("C:\\Users\\Administrator\\Desktop\\img5_res.csv"), logList, true);
        
        
    }

}

