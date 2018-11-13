package com.cbt.fee.service;

import java.util.Map;

public interface IMisceServer {
	/**
	 * 获取运费
	 * @param zipcode
	 * @param productweight
	 * @param furniture
	 * @param insidedelivery
	 * @param lsad
	 * @return
	 */
	public Map<String, String>  getFreight(String zipcode, float productweight, float cubicfoot, int furniture, int insidedelivery, float productValue, float dutyrate, String zone);

	public String getZipResult(String zipcode);
	
}
