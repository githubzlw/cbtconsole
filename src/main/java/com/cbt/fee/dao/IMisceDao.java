package com.cbt.fee.dao;

import com.cbt.bean.State;
import com.cbt.bean.StateZip;

import java.util.Map;

public interface IMisceDao {
	/**
	 * 根据州 获取州内邮编
	 * @param state
	 * @return
	 */
	public Map<String, StateZip> getStateZipMap(String state);
	/**
	 * 根据目的地获取运费
	 * @param destinationport
	 * @return
	 */
	public StateZip getStateZip(String destinationport);
	/**
	 * 根据州名 获取对应更准确邮编
	 * @param state
	 * @return
	 */
	public Map<String, State> getStateList(String state);
	
	/**
	 * 获取运价系数
	 * @param pcf
	 * @return
	 */
	public float getFreightClass(float pcf);
	
}
