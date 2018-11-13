package com.cbt.controller;

import com.cbt.dao.KeyWordSearchDao;
import com.cbt.dao.impl.KeyWordSearchDaoImpl;
import com.cbt.util.Cache;
import com.cbt.util.Utility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/cbt/keywords")
public class KeyWordsController {
	
	private static final Log LOG = LogFactory.getLog(KeyWordsController.class);
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response, String zc, String tyc, Map<String, Object> map) throws ServletException, IOException {
		try {
			String tbname=zc+"_"+Utility.format(new Date(), "yyyyMMddHHmmss");
			String time=Utility.format(new Date(), Utility.datePattern1);
			KeyWordSearchDao keyWordSearchDao=new KeyWordSearchDaoImpl();
			int updateResult=keyWordSearchDao.updateSimilarwords(zc, tyc, time);
			if(updateResult == 0){
				keyWordSearchDao.insertSimilarwords(zc, tyc, time);
			}
			List<Map<String,Object>> list=keyWordSearchDao.selectGoodsInfo(tyc);
			int tbresult=keyWordSearchDao.create(tbname);
			int updatekwsy=keyWordSearchDao.updateKwsynonymstb(zc, tbname);
			if(updatekwsy == 0){
				keyWordSearchDao.insertKwsynonymstb(zc, tbname);
			}
			keyWordSearchDao.insertNewTb(list, tbname);
			Cache.save(zc, list);
			map.put("code", "0");
			map.put("msg", "success");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "1");
			map.put("msg", "faild");
		}	
		request.setAttribute("map", map);
		request.getRequestDispatcher("/website/keywords.jsp").forward(request, response);
	}
}