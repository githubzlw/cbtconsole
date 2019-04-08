package com.cbt.warehouse.service;

import com.cbt.bean.Subscribe;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SubScribeService {
	public List<Subscribe> queryAllSubscribe(Subscribe sbean);

	public int queryAllSubscribeConut(Subscribe sbean);
}