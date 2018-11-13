package com.cbt.Specification.util;

import com.cbt.Specification.dao.SpecificationDao;
import com.cbt.Specification.traslate.TransApi;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class TraslateByBaidu {

	// 在平台申请的APP_ID 详见
	// http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
	//private static final String APP_ID = "20170304000041484";
	//private static final String SECURITY_KEY = "9pEBDGhtXtoyY0hBX2_d";
	
	private static final String APP_ID = "20170303000040259";
	private static final String SECURITY_KEY = "yoLYRLSD8CxoFxEQYVaD";

	public static void main(String[] args) {
		TransApi api = new TransApi(APP_ID, SECURITY_KEY);

		SpecificationDao spcDao = new SpecificationDao();
		List<String> transEnNames = spcDao.queryTranslationEnName();
		List<String> mappingEnNames = spcDao.queryMappingEnName();
		long begin = System.currentTimeMillis();
		for (String enName : transEnNames) {
			String chStr = getResult(api.getTransResult(enName, "en", "zh"));
			if (chStr != null && !"".equals(chStr)) {
				spcDao.updateSpecificationByEnName(chStr, enName);
			}
		}

		for (String enName : mappingEnNames) {
			String chStr = getResult(api.getTransResult(enName, "en", "zh"));
			if (chStr != null && !"".equals(chStr)) {
				spcDao.updateSpecificationByEnName(chStr, enName);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("begin datetime :" + DateFormatUtil.getWithMicroseconds(new Date(begin)));
		System.out.println("end datetime :" + DateFormatUtil.getWithMicroseconds(new Date(end)));
		System.out.println("consume :" + (end - begin) / (1000 * 60 * 60 * 1.0) + " hours");

	}

	private static String getResult(String transResultStr) {
		String transResult = "";
		try {
			// System.out.println(transResultStr);
			JSONObject objResult = new JSONObject(transResultStr);
			String trans_result = objResult.get("trans_result").toString();
			// System.out.println(trans_result);
			if (trans_result.length() > 0) {
				JSONObject transObj = new JSONObject(trans_result.substring(1, trans_result.length()));
				transResult = transObj.getString("dst");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transResult;
	}

}
