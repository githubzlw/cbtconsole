package com.cbt.service;

import com.cbt.bean.Complain;
import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.bean.ComplainVO;
import com.cbt.pojo.page.Page;

import java.util.List;

public interface IComplainService{
	
	public Page<ComplainVO> searchComplainByParam(Complain t, String username, Page page, String admName,int check);

	public ComplainVO getComplainByCid(Integer cid);

	public Integer afterResponse(Integer complainid, Integer chatid, Integer cfid);

	public List<ComplainVO> getCommunicatingByCidBG(Integer complainId);

	public Integer responseCustomer(ComplainChat t, String dealAdmin, Integer dealAdminId, Integer complainid);
	
	public Integer closeComplain(Integer complainid);
	
	public List<ComplainFile> getImgsByCid(Integer complainId);
	
	/**录入关联产品
	 * @param id
	 * @param orderid
	 * @param goodsid
	 * @return
	 */
	int updateGoodsid(int id,String orderid,String goodsid);
	/**录入申诉事件号
	 * @param id
	 * @param disputeid
	 * @param merchantid
	 * @return
	 */
	int updateDisputeid(int id,String disputeid,String merchantid);
	
	/**获取申诉号对应的投诉
	 * @param disputeIdList
	 * @return
	 */
	List<ComplainVO> getComplainByDisputeId(List<String> disputeIdList);
	/**获取用户的投诉
	 * @param disputeIdList
	 * @return
	 */
	List<ComplainVO> getComplainByUserId(String userId);
}
