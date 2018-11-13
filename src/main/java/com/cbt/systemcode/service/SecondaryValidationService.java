package com.cbt.systemcode.service;

import com.cbt.bean.SecondaryValidation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SecondaryValidationService {

	/**
	 * 查询所有二次密码验证信息
	 * 
	 * @return
	 */
	public List<SecondaryValidation> queryForList();

	/**
	 * 根据userid查询验证信息
	 * 
	 * @param userid
	 * @return
	 */
	public SecondaryValidation queryByUserId(int userid);

	/**
	 * 根据userid查询是否存在验证信息
	 * 
	 * @param userid
	 * @return
	 */
	public int existsSecondaryValidation(int userid);

	/**
	 * 插入验证信息
	 * 
	 * @param secVali
	 * @return
	 */
	public int insertSecondaryValidation(SecondaryValidation secVali);

	/**
	 * 根据userid修改验证信息
	 * 
	 * @param secVali
	 * @return
	 */
	public int updateByUserId(SecondaryValidation secVali);

	/**
	 * 根据userid删除验证信息
	 * 
	 * @param userid
	 * @return
	 */
	public int deleteByUserId(int userid);

	/**
	 * 根据userid检查验证密码
	 * 
	 * @param userid
	 * @param password
	 * @return
	 */
	boolean checkExistsPassword(int userid, String password);

}
