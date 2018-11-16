package com.cbt.util;

import org.springframework.web.multipart.MultipartFile;

public abstract class FileUtil {
    
    /**
     * 鐢熸垚瀵煎嚭闄勪欢涓枃鍚嶃�傚簲瀵瑰鍑烘枃浠朵腑鏂囦贡鐮�
     * <p>
     * response.addHeader("Content-Disposition", "attachment; filename=" + cnName);
     * 
     * @param cnName
     * @param defaultName
     * @return
     */
    public static String genAttachmentFileName(String cnName, String defaultName) {
        try {
//            fileName = URLEncoder.encode(fileName, "UTF-8");
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
            /*
            if (fileName.length() > 150) {
                fileName = new String( fileName.getBytes("gb2312"), "ISO8859-1" );
            }
            */
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }
    /**
	 * 获取新文件名
	 * [规则：时间戳+4位随机数+原文件名+原文件扩展名]
	 * 修改不加原文件名，原文件名有可能有特殊字符和汉字
	 * @param
	 * @return
	 */
	public static String getNewFileName(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String fileName2 = fileName.split(",")[0];
		int lastIndexOf = fileName2.lastIndexOf(".");
		fileName = fileName2.substring(0,lastIndexOf);
		fileName = fileName + System.currentTimeMillis() + RandomUtil.getRandom4();
		return fileName +fileName2.substring(lastIndexOf);
	}
}