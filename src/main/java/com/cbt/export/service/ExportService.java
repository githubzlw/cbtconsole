package com.cbt.export.service;

import java.util.List;

import com.cbt.export.pojo.ExportInfo;

public interface ExportService {
	/**获取列表
	 * @param start 起始
	 * @param limit 数量
	 * @return
	 */
	List<ExportInfo> getExport(int start,int limit,String order_no);
	
	/**数量
	 * @return
	 */
	int getExportCount();

}
