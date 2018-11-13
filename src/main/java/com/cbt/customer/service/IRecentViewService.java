package com.cbt.customer.service;

import com.cbt.bean.RecentViewBean;

import java.util.List;

public interface IRecentViewService {
	public int addRecode(RecentViewBean rvb);
	/**
	 * 方法描述:根据商品id查询一个商品对象
	 * author: lizhanjun
	 * date:2015年4月18日
	 * @return
	 */
	public RecentViewBean findByPid(String pid);
	
	/**
	 * 方法描述: 分页查询最近浏览信息（从最新浏览开始）
	 * author:
	 * date:2015年4月18日
	 * @return
	 */
	public List<RecentViewBean> findAll(int start, int page_size);
}
