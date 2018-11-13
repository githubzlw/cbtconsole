package com.cbt.service.impl;

import com.cbt.service.InitCacheService;
import com.cbt.website.userAuth.Dao.AuthInfoDao;
import com.cbt.website.userAuth.impl.AuthInfoDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InitCacheServiceImpl implements InitCacheService{
	
	private static final Log LOG = LogFactory.getLog(InitCacheServiceImpl.class);
	
	@Override
	public void init() throws Exception {
//		KeyWordSearchDao keyWordSearchDao=new KeyWordSearchDaoImpl();
//		LOG.debug("----------------init save cache-----------------");
//		// TODO Auto-generated method stub
//		String sql="select * from kwsynonymstb";
//		List<Map<String, Object>> synonymsList=keyWordSearchDao.search(sql);
//		if(synonymsList != null && synonymsList.size()>0){
//			for(Map<String, Object> map : synonymsList){
//				String goodsIdTbSql="select * from "+map.get("tablename").toString();
//				List<Map<String, Object>> goodsIdList=keyWordSearchDao.search(goodsIdTbSql);
//				Cache.save(map.get("keywords").toString(), goodsIdList);
//			}
//		}
		
		//讲管理台菜单全部加载进缓存中
		AuthInfoDao authinfoDao = new AuthInfoDaoImpl();
		authinfoDao.saveAllAuth();
		
		//初始化
//		Redis.getJedis();
	}
	
}
