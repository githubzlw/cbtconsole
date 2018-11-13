package com.cbt.warehouse.service;

import com.cbt.pojo.page.Page;
import com.cbt.warehouse.dao.SupplierScoringMapper;
import com.cbt.warehouse.pojo.SupplierProductsBean;
import com.cbt.warehouse.pojo.SupplierScoringBean;
import com.cbt.warehouse.util.StringUtil;
import com.importExpress.pojo.QueAns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SupplierScoringServiceImpl implements SupplierScoringService {
	@Autowired
	private SupplierScoringMapper supplierScoringMapper;
	//供应商列表
	@Override
	public Page<SupplierScoringBean> queryList(int start , int pagesize, String shop_id, String level, String quality,
                                               String services, String authorized, boolean flag, String userid) {
		Page<SupplierScoringBean> page = new Page<SupplierScoringBean>(start, pagesize);
		int startindex = (start-1)*pagesize;//起始位置
		String qualitys="";
		String servicess="";
		if(StringUtil.isNotBlank(quality)){
			qualitys=String.valueOf(Integer.valueOf(quality)+1);
		}
		if(StringUtil.isNotBlank(services)){
			servicess=String.valueOf(Integer.valueOf(services)+1);
		}
		//查询总数
		int total = supplierScoringMapper.querySupplierRecord(shop_id,level,quality,qualitys,services,servicess,authorized,flag,userid);
		List<SupplierScoringBean> supplierScoringlist = supplierScoringMapper.querySupplierScoringByPage(startindex,pagesize,
				shop_id,level,quality,qualitys,services,servicess,authorized,flag,userid);
		for(SupplierScoringBean s:supplierScoringlist){
			String type="非精品店铺";
			String authorizedFlag="未授权";
//			SupplierScoringBean sl=supplierScoringMapper.getShoptype(s.getShopId());
			if(StringUtil.isNotBlank(s.getShopType()) && "1".equals(s.getShopType())){
				type="精品店铺";
			}
			if(StringUtil.isNotBlank(s.getAuthorizedFlag()) && "1".equals(s.getAuthorizedFlag())){
				authorizedFlag="已授权";
			}
			s.setType(type);
			s.setAuthorizedFlag(authorizedFlag);
		}
		double countPage = Math.ceil(total*1.0/pagesize);
		//封装数据
		page.setCurrentPage(start);
		page.setCountPage((int)countPage);//总页数
		page.setCountRecord(total);//总数
		page.setOnePageCount(pagesize);
		page.setList(supplierScoringlist);
		return page;

	}

	@Override
	public int getCooperatedCount() {
		return supplierScoringMapper.getCooperatedCount();
	}

	@Override
	public int getHighCount() {
		return supplierScoringMapper.getHighCount();
	}

	@Override
	public int getBlacklistCount() {
		return supplierScoringMapper.getBlacklistCount();
	}

	@Override
	public int getOrdinaryCount() {
		return supplierScoringMapper.getOrdinaryCount();
	}

	//根据shop_id查询该供应商下对应的产品
	@Override
	public List<SupplierProductsBean> queryProductsByShopId(String shop_id, String goodsPid, int userId) {
		return supplierScoringMapper.queryProductsByShopId(shop_id,goodsPid,userId);
	}
	//增加或者修改产品的信息
	@Override
	public void saveOrupdateProductScord(
			SupplierProductsBean supplierProductsBean) {
		supplierScoringMapper.saveOrupdateProductScord(supplierProductsBean);
	}

	@Override
	public int updateRemark(String id, String reamrk) {
		return supplierScoringMapper.updateRemark(id,reamrk);
	}

	//查询打过分的信息
	@Override
	public List<SupplierProductsBean> searchProductListByShopId(String shopId) {
		return supplierScoringMapper.searchProductListByShopId(shopId);
	}
	//查询供应商表中是否存在打过分的数据
	@Override
	public SupplierScoringBean searchOneScoringByShopId(String shopId) {
		return supplierScoringMapper.searchOneScoringByShopId(shopId);
	}

	@Override
	public List<SupplierProductsBean> getAllShopInfo(Map<String, String> map) {
		return supplierScoringMapper.getAllShopInfo(map);
	}

	@Override
	public List<SupplierProductsBean> getAllShopGoodsInfo(Map<String, String> map) {
		List<SupplierProductsBean> list=supplierScoringMapper.getAllShopGoodsInfo(map);
		for(SupplierProductsBean s:list){
			s.setGoodsImg("<a target='_blank' href='https://detail.1688.com/offer/"+s.getGoodsPid()+".html'><img src='"+(s.getRemotpath()+s.getCustom_main_image())+"'></img></a>");
		}
		return list;
	}

	@Override
	public List<SupplierProductsBean> getAllShopGoodsInfoList(Map<String, String> map) {
		List<SupplierProductsBean> list=supplierScoringMapper.getAllShopGoodsInfoList(map);
		for(SupplierProductsBean s:list){
			s.setGoodsImg("<a target='_blank' href='https://detail.1688.com/offer/"+s.getGoodsPid()+".html'><img src='"+(s.getRemotpath()+s.getCustom_main_image())+"'></img></a>");
		}
		return list;
	}

	//新增或更新供应商中的数据
	@Override
	public void insertOrupdateScoringScoring(SupplierScoringBean scoringBean) {
		supplierScoringMapper.insertOrupdateScoringScoring(scoringBean);
	}

	@Override
	public List<QueAns> lookQuestion(String pid) {
		return supplierScoringMapper.lookQuestion(pid);
	}

	//查询当前用户的打打分信息
	@Override
	public List<SupplierProductsBean> queryOneProductsUserId(String shop_id, String goodsPid) {
		// TODO Auto-generated method stub
		return supplierScoringMapper.queryOneProductsUserId(shop_id,goodsPid);
	}
	//chauxn
	@Override
	public SupplierProductsBean queryByProductScoreId(String goodsPid,
                                                      Integer userId) {
		return supplierScoringMapper.queryByProductScoreId(goodsPid,userId);
	}
	//查询该用户对该店铺打分信息
	@Override
	public SupplierProductsBean searchUserScoringByShopId(String shop_id,
                                                          Integer userId) {
		return supplierScoringMapper.searchUserScoringByShopId(shop_id,userId);
	}
	//保存或者修改店铺打分
	@Override
	public void saveOrupdateSupplierScoringScord(
            SupplierScoringBean supplierScoringBean, Integer id, String admName) {
		supplierScoringMapper.saveOrupdateSupplierScoringScord(supplierScoringBean,id,admName);
	}

	@Override
	public int saveSupplierProduct(Map<String, String> map) {
		return supplierScoringMapper.saveSupplierProduct(map);
	}

	@Override
	public int saveSupplierScoring(Map<String, String> map) {
		return supplierScoringMapper.saveSupplierScoring(map);
	}

	@Override
	public int updateSupplierScoring(Map<String, String> map) {
		return supplierScoringMapper.updateSupplierScoring(map);
	}

	@Override
	public List<SupplierScoringBean> getSupplierScoring(Map<String, String> map) {
		return supplierScoringMapper.getSupplierScoring(map);
	}

	@Override
	public List<SupplierProductsBean> getAllShopScoring(Map<String, String> map) {
		return supplierScoringMapper.getAllShopScoring(map);
	}

	//查询该供应商的所有打分信息
	@Override
	public List<SupplierProductsBean> querySupplierAllScoresSupplier(String shop_id) {
		// TODO Auto-generated method stub
		return supplierScoringMapper.querySupplierAllScoresSupplier(shop_id);
	}
	//修改库存
	@Override
	public void saveOrUpdateInven(SupplierScoringBean bean) {
		supplierScoringMapper.saveOrUpdateInven(bean);
		//修改ali_info_data下供应商的天数
		supplierScoringMapper.updateAliInfoDataDays(bean.getReturnDays(),bean.getShopId());
	}
}
