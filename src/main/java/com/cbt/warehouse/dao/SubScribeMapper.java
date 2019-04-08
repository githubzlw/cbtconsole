package com.cbt.warehouse.dao;

import com.cbt.bean.Subscribe;

import java.util.List;

public interface SubScribeMapper {
	public List<Subscribe> queryAllSubscribe(Subscribe sbean);
	public int queryAllSubscribeConut(Subscribe sbean);
}