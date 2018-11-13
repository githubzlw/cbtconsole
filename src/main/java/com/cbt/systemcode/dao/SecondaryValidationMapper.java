package com.cbt.systemcode.dao;

import com.cbt.bean.SecondaryValidation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecondaryValidationMapper {

	/**
	 * 查询所有二次密码验证信息
	 * 
	 * @return
	 */
	List<SecondaryValidation> queryForList();

	/**
	 * 根据userid查询验证信息
	 * 
	 * @param userid
	 * @return
	 */
	SecondaryValidation queryByUserId(int userid);
	
	/**
	 * 根据userid查询是否存在验证信息
	 * @param userid
	 * @return
	 */
	int existsSecondaryValidation(int userid);

	/**
	 * 插入验证信息
	 * 
	 * @param secVali
	 * @return
	 */
	int insertSecondaryValidation(SecondaryValidation secVali);

	/**
	 * 根据userid修改验证信息
	 * 
	 * @param secVali
	 * @return
	 */
	int updateByUserId(SecondaryValidation secVali);

	/**
	 * 
	 * @param userid
	 * @return
	 */
	int deleteByUserId(int userid);
	
	/**
	 * 根据userid检查验证密码
	 * 
	 * @param userid
	 * @param password
	 * @return
	 */
	public int checkExistsPassword(@Param("userid") int userid, @Param("password") String password);

}
