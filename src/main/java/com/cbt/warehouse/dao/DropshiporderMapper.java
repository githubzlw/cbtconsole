package com.cbt.warehouse.dao;


import com.cbt.warehouse.pojo.Dropshiporder;
import com.cbt.warehouse.pojo.DropshiporderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DropshiporderMapper {
    int countByExample(DropshiporderExample example);

    int deleteByExample(DropshiporderExample example);

    int deleteByPrimaryKey(Integer orderid);

    int insert(Dropshiporder record);

    int insertSelective(Dropshiporder record);

    List<Dropshiporder> selectByExample(DropshiporderExample example);

    Dropshiporder selectByPrimaryKey(Integer orderid);

    int updateByExampleSelective(@Param("record") Dropshiporder record, @Param("example") DropshiporderExample example);

    int updateByExample(@Param("record") Dropshiporder record, @Param("example") DropshiporderExample example);

    int updateByPrimaryKeySelective(Dropshiporder record);

    int updateByPrimaryKey(Dropshiporder record);
    
    int updateDropshiporder(@Param("orderno") String orderno);


	public List<Map<String, Object>> getDropShipOrderInfo(@Param("orderNo") String orderNo, @Param("userId") int userId);

	/**
	 * ��ȡ�û��ܸ�����
	 * ylm
	 * @param orderNo
	 * 		�û�ID,����״̬
	 */
	Map<String, Object> getOrdersPay(String orderNo);

	/**
	 * 根据userId、parentOrderNo
	 * @param userId
	 * @param parentOrderNo
	 * @return
	 */
	List<Dropshiporder> selectByUserIdAndParentOrderNo(@Param("userId") Integer userId, @Param("parentOrderNo") String parentOrderNo);
}