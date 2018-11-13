package com.cbt.dao;

import com.cbt.bean.UserGradeBean;

import java.util.List;

public interface UserGradeDao {
	
	/**获取用户等级
	 * @date 2016年11月14日
	 * @author abc
	 * @return  
	 */
	public List<UserGradeBean> getGrades();
	
	/**更新用户等级
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @param uid
	 * @return  
	 */
	public int updateGrade(int gid, int uid);

	/**所有等级折扣
	 * @date 2016年11月14日
	 * @author abc
	 * @return
	 */
	public List<UserGradeBean> getGradeDiscount();




	/**修改折扣
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @param discount
	 * @return
	 */
	public int updateGradeDiscount(int gid, double discount, int valid);

	/**折扣表是否存在
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @return
	 */
	public int isExsis(int gid);

	/**添加
	 * @date 2016年11月14日
	 * @author abc
	 * @param gid
	 * @param discount
	 * @return
	 */
	public int addDicount(int gid, double discount);
	
	
}
