package com.cbt.service;

import com.cbt.bean.BusiessBean;

import java.util.List;
import java.util.Map;

public interface BusiessService {

	public int saveBusiess(Map<String, Object> params);

	public int deleteBusiess(int id);

	public int getBusiessCountPage(BusiessBean busiessBean);

	public List<BusiessBean> getBusiessPage(BusiessBean busiessBean, int pagenum);

	public int queryByChoiceCountPage(BusiessBean busiessBean, int type);

	public List<BusiessBean> queryByChoice(BusiessBean busiessBean, int pagenum, int type);
}
