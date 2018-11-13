package com.cbt.service;

import com.cbt.bean.UserGradeBean;

import java.util.List;

public interface UserGradeService {
	
	/**获取所有等级，操作user_grade表
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	public List<UserGradeBean> getGrades();
	
	
	/**修改用户等级，操作user表
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @param uid
	 * @return  
	 */
	public int updateUserGrade(int gid, int uid);



	/**所有等级折扣，操作grade_discount
	 * @date 2016年11月14日
	 * @author abc
	 * @return
	 */
	public List<UserGradeBean> getGradeDiscount();




	/**修改折扣，操作grade_discount表
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @param discount
	 * @return
	 */
	public int updateGradeDiscount(int gid, double discount, int valid);



	/**折扣表是否存在，操作grade_discount表
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @return
	 */
	public int isExsis(int gid);

	/**添加，操作grade_discount表
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @param discount
	 * @return
	 */
	public int addDicount(int gid, double discount);
	

}
