package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.GradeDiscountMapper;
import com.cbt.warehouse.pojo.GradeDiscountBean;
import com.cbt.warehouse.pojo.OrderBean;
import com.cbt.warehouse.pojo.UserGradeGrowthBean;
import com.cbt.warehouse.pojo.UserGradeGrowthLogBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GradeDiscountServiceImpl implements GradeDiscountService {
	@Autowired
	private GradeDiscountMapper  gradeDiscountMapper;
	@Override
	public Map<String, String> getDisount() {
		List<GradeDiscountBean> disount = gradeDiscountMapper.getDisount();
		Map<String, String> map  = new HashMap<String, String>();
		for(GradeDiscountBean bean:disount){
			map.put(bean.getGid()+"", bean.getDiscount()+"_"+bean.getGrade());
		}
		return map;
	}
	@Override
	public UserGradeGrowthBean getUserGradeGrowth(int userId) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getUserGradeGrowth(userId);
	}
	@Override
	public GradeDiscountBean getGradeDiscountByGrowthValue(int growthValue) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getGradeDiscountByGrowthValue(growthValue);
	}
	@Override
	public GradeDiscountBean getGradeDiscountByGid(int gid) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getGradeDiscountByGid(gid);
	}
	@Override
	public int insertUserGradeGrowth(UserGradeGrowthBean bean) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.insertUserGradeGrowth(bean);
	}
	@Override
	public int getCompleteOrderCount(int userId) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getCompleteOrderCount(userId);
	}
	@Override
	public int updateUserGradeGrowth(UserGradeGrowthBean bean) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.updateUserGradeGrowth(bean);
	}
	@Override
	public OrderBean getCompleteOrder(String orderNo) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getCompleteOrder(orderNo);
	}
	@Override
	public List<GradeDiscountBean> getAllGradeDiscount() {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getAllGradeDiscount();
	}
	@Override
	public int updateUserGrade(int grade, int userId) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.updateUserGrade(grade, userId);
	}
	@Override
	public int insertUserGradeGrowthLog(UserGradeGrowthLogBean bean) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.insertUserGradeGrowthLog(bean);
	}
	@Override
	public UserGradeGrowthLogBean getLogByUserIdAndOrderNo(int userId,
			String orderNo) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getLogByUserIdAndOrderNo(userId, orderNo);
	}
	@Override
	public UserGradeGrowthLogBean getLogByUserId(int userId) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getLogByUserId(userId);
	}
	@Override
	public Integer getTimesByUserIdAndGrade(int userId, int grade) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getTimesByUserIdAndGrade(userId, grade);
	}
	@Override
	public List<OrderBean> getOtherGradeDiscount(int userId) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getOtherGradeDiscount(userId);
	}
	@Override
	public int updateOtherOrderGradeDiscout(double grade_discount, String orderNo, int userId) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.updateOtherOrderGradeDiscout(grade_discount, orderNo, userId);
	}
	@Override
	public boolean isSpitOrder(int userId, String orderNo) {
		int count = getCountLikeOrderNo(userId, orderNo);
		if(count>1){
			return true;
		}
		return false;
	}
	@Override
	public int getCountLikeOrderNo(int userId, String orderNo) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getCountLikeOrderNo(userId, orderNo);
	}
	@Override
	public int getDeleteCountLikeOrderNo(int userId, String orderNo) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getDeleteCountLikeOrderNo(userId, orderNo);
	}
	@Override
	public int getCompleteOrderCountExcludeTOrder(int userId, String orderNo) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getCompleteOrderCountExcludeTOrder(userId, orderNo);
	}
	@Override
	public Double getTOrderNowTotalPayPrice(int userId, String orderNo) {
		// TODO Auto-generated method stub
		return gradeDiscountMapper.getTOrderNowTotalPayPrice(userId, orderNo);
	}
	

}
