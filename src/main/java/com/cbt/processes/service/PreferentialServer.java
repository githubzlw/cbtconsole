package com.cbt.processes.service;

import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.Preferential;
import com.cbt.bean.PreferentialWeb;
import com.cbt.bean.SpiderBean;
import com.cbt.processes.dao.IPreferentialDao;
import com.cbt.processes.dao.ISpiderDao;
import com.cbt.processes.dao.PreferentialDao;
import com.cbt.processes.dao.SpiderDao;

import java.util.List;
import java.util.Map;

public class PreferentialServer implements IPreferentialServer {

	private IPreferentialDao dao = new PreferentialDao();
	
	@Override
	public int savePreferential(Preferential preferential,String url) {
		/*if(dao.getPreferential(preferential.getUserid(),url) > 0){
			return 2;
		}*/
		return dao.savePreferential(preferential,url);
	}

	@Override
	public int getPreferentialNum(int userid) {
		return dao.getPreferentialNum(userid);
	}

	@Override
	public List<PreferentialWeb> getPreferentials(int userid, int state, int page) {
		return dao.getPreferentials( userid,state,(page-1)*10,10);
	}
	@Override
	public List<PreferentialWeb> getDiscounts(int userid) {
		return dao.getDiscounts( userid);
	}
	
	
	@Override
	public int getPreferentials(int userid,int state) {
		return dao.getPreferentials( userid,state);
	}
	@Override
	public int savePai(int id, int gids, double prices) {
		return dao.savePai(id, gids, prices);
	}

	@Override
	public int delPaprestrain(int pid, String cancel_reason) {
		return dao.delPaprestrain(pid, cancel_reason);
	}

	@Override
	public List<PreferentialWeb> getPreferentials(String uid, int userid) {
		//查询该uid是否是该用户下的并且是有效uid
		int res = dao.getPauid(uid, userid);
		if(res < 1){
			return null;
		}
		List<PreferentialWeb> list = dao.getPreferentials(uid);
		return list;
	}

	@Override
	public int addPa_Googs_car(String uid,int userid,int type,String uCurrency,Map<String, Double> maphl) {
		//根据uid查询商品表信息
		List<SpiderBean> list = dao.getGoodsdata(uid,userid,type);
		if(list.size() == 0){
			return 0;
		}
		ISpiderDao sdao = new SpiderDao();
		//保存商品到用户购物车
		int res  = sdao.addGoogs_car(list,uCurrency,maphl);
		//将该uid状态设置为已链接&将优惠申请状态改为已处理
		if(res > 0){
			dao.upPauid(uid,type);
			dao.upPA(uid,type);
		}
		return res;
	}
	
	

	@Override
	public int savePostageD(PostageDiscounts pd) {
		return dao.savePostageD(pd);
	}

	@Override
	public PostageDiscounts getPostageD(int userid,String sessionid) {
		return dao.getPostageD(userid,sessionid);
	}

	@Override
	public int upPostageD(PostageDiscounts pd) {
		return dao.upPostageD(pd);
	}

	@Override
	public int savePreferential2(Preferential preferential, String url) {
		// TODO Auto-generated method stub
		return dao.savePreferential2(preferential, url);
	}

	@Override
	public int upPA(int pid) {
		return dao.upPA(pid);
	}
	
}
