package com.cbt.website.service;

import java.util.List;

public interface GoodsPriceHistoryservice {
	
	List<Object[]> seehistoryPrice(String url) throws Exception;

}
