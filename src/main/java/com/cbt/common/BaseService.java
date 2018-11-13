package com.cbt.common;

import org.apache.ibatis.annotations.Param;

public interface BaseService <T>{
	public T getById(Integer id);
	public Integer delById(@Param("id") Integer id);
	public Integer update(@Param("t") T t);
	public Integer add(@Param("t") T t);
}
