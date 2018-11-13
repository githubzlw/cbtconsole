package com.cbt.service;

import com.cbt.bean.Complain;
import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.bean.ComplainVO;
import com.cbt.pojo.page.Page;

import java.util.List;

public interface IComplainService{
	
	public Page<ComplainVO> searchComplainByParam(Complain t, String username, Page page, String admName);

	public ComplainVO getComplainByCid(Integer cid);

	public Integer afterResponse(Integer complainid, Integer chatid, Integer cfid);

	public List<ComplainVO> getCommunicatingByCidBG(Integer complainId);

	public Integer responseCustomer(ComplainChat t, String dealAdmin, Integer dealAdminId, Integer complainid);
	
	public Integer closeComplain(Integer complainid);
	
	public List<ComplainFile> getImgsByCid(Integer complainId);
}
