package com.cbt.warehouse.service;

import com.cbt.pojo.Syncdatainfo;

import java.util.List;

/**   
 * @Title : SyncdatainfoService.java 
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : 柒月
 * @date : 2016年11月18日
 * @version : V1.0   
 */
public interface SyncdatainfoService {
	
	int addSyncdatainfo(Syncdatainfo syncdatainfo);
	
	List<Syncdatainfo> getSyncdatainfoList();

}
