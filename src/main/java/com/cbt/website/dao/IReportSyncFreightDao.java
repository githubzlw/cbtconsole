package com.cbt.website.dao;

import java.util.List;

/**
* @ClassName: IReportSyncFreightDao 
* @Description: 报表同步运费
* @author lyb
 */
public interface IReportSyncFreightDao {

	public List<String> getExpressno(String flag);
	
	public List<Object[]> getFreightByExpressno(List<String> list, String flag);
	
	public int updateWeightFreight(List<Object[]> list);
	
}
