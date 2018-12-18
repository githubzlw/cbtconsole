package com.cbt.customer.service;

import ceRong.tools.bean.DorpDwonBean;

import com.cbt.bean.*;
import com.cbt.customer.dao.IPictureComparisonDao;
import com.cbt.customer.dao.PictureComparisonDaoImpl;

import java.util.List;
import java.util.Map;

public class PictureComparisonServiceImpl implements IPictureComparisonService {

	IPictureComparisonDao dao = new PictureComparisonDaoImpl();
	
	@Override
	public List<GoodsFarBean> findByAliPicture(int maxC) {
		return dao.findByAliPicture(maxC);
	}
	
	@Override
	public List<GoodsFarBean> getLireSearchCondition() {
		return dao.getLireSearchCondition();
	}

	@Override
	public int insertChangeGoodsLog(String pUrl, String goodsType, String name, String price, String goodsCarId,String admuserid) {
		return dao.insertChangeGoodsLog(pUrl,goodsType,name,price,goodsCarId,admuserid);
	}

	@Override
	public List<GoodsFarBean> getImgFile() {
		return dao.getImgFile();
	}
	
	
	@Override
	public List<GoodsFarBean> findByTbPicture(int maxC) {
		return dao.findByTbPicture(maxC);
	}
	
	@Override
	public List<GoodsFarBean> findByTbStyUrl(int maxC) {
		return dao.findByTbStyUrl(maxC);
	}
	
	
	@Override
	public List<GoodsFarBean> findByCpPicture(int maxC) {
		return dao.findByCpPicture(maxC);
	}
	
	@Override
	public int getMaxCount(){
		return dao.getMaxCount();
	}
	
	@Override
	public int updateLireConditionFlag(List<GoodsFarBean> beanList){
		return dao.updateLireConditionFlag(beanList);
	}
	
	@Override
	public int updateImgFileUpload(List<GoodsFarBean> beanList){
		return dao.updateImgFileUpload(beanList);
	}
	
	
	@Override
	public int getTbMaxCount(){
		return dao.getTbMaxCount();
	}
	
	@Override
	public int getTbStyMaxCount(){
		return dao.getTbStyMaxCount();
	}
	
	
	@Override
	public int getCpMaxCount(){
		return dao.getCpMaxCount();
	}
	
	@Override
	public int getGoodsCheckCount(int selled,String similarityId,String categoryId1){
		return dao.getGoodsCheckCount(selled,similarityId,categoryId1);
	}
	
	@Override
	public int getOrderPurchaseCount(int selled,String similarityId,String categoryId1){
		return dao.getOrderPurchaseCount(selled,similarityId,categoryId1);
	}
	
	@Override
	public int getTbgooddataCount(){
		return dao.getTbgooddataCount();
	}
	
	@Override
	public int getYlbbGooddataCount(String userName,int flag){
		return dao.getYlbbGooddataCount(userName,flag);
	}
	
	@Override
	public int getErrorInfoCount(String userId,String timeF,String timeE){
		return dao.getErrorInfoCount(userId,timeF,timeE);
	}
	
	
	@Override
	public int getLireImgCount(String userName,int flag){
		return dao.getLireImgCount(userName,flag);
	}
	
	@Override
	public int getFactoryCount(String cid,String shopId){
		return dao.getFactoryCount(cid,shopId);
	}
	
	@Override
	public int getShelfCount(){
		return dao.getShelfCount();
	}
	
	@Override
	public int getTbCount1(String categoryId){
		return dao.getTbCount1(categoryId);
	}
	
	@Override
	public int getTbCount2(String categoryId){
		return dao.getTbCount2(categoryId);
	}
	@Override
	public int getTbCount3(String categoryId){
		return dao.getTbCount3(categoryId);
	}
	@Override
	public int getTbCount4(String categoryId){
		return dao.getTbCount4(categoryId);
	}
	@Override
	public int getTbCount5(String categoryId){
		return dao.getTbCount5(categoryId);
	}
	@Override
	public int getTbCount6(String categoryId){
		return dao.getTbCount6(categoryId);
	}
	@Override
	public int getTbCount7(String categoryId){
		return dao.getTbCount7(categoryId);
	}
	@Override
	public int getTbCount8(String categoryId){
		return dao.getTbCount8(categoryId);
	}
	@Override
	public int getTbCount9(String categoryId){
		return dao.getTbCount9(categoryId);
	}
	@Override
	public int getTbCount10(String categoryId){
		return dao.getTbCount10(categoryId);
	}
	
	
	@Override
	public int updateImgUrlValid(int id){
		return dao.updateImgUrlValid(id);
	}
	
	@Override
	public int updateStyle(int id,String style,String styJosn){
		return dao.updateStyle(id,style,styJosn);
	}
	
	@Override
	public int updateChangeType(String pUrl,String goodsType,String name,String price,String goodsCarId){
		return dao.updateChangeType(pUrl,goodsType,name,price,goodsCarId);
	}
	
	@Override
	public int updateSourceFlag(String url,int flag){
		return dao.updateSourceFlag(url,flag);
	}
	
	@Override
	public int updateLireFlag(String pid){
		return dao.updateLireFlag(pid);
	}
	
	
	@Override
	public int updateTbName(String goodsId,String tbName1,String tbName2,String tbName3,String tbName4){
		return dao.updateTbName(goodsId,tbName1,tbName2,tbName3,tbName4);
	}
	
	
	
	@Override
	public int updateSimilarity(int pId,int difference,int flag){
		return dao.updateSimilarity(pId,difference,flag);
	}
	
	@Override
	public int cancelChangeGoodData(String orderNo){
		return dao.cancelChangeGoodData(orderNo);
	}
	
	@Override
	public int updateCgdEmailFlag(String orderNo){
		return dao.updateCgdEmailFlag(orderNo);
	}
	
	@Override
	public int insertOnlineChange(String orderNo){
		return dao.insertOnlineChange(orderNo);
	}
	
	@Override
	public int getUserIdByOrder(String orderNo){
		return dao.getUserIdByOrder(orderNo);
	}
	
	
	@Override
	public List<GoodsCheckBean> findGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end) {
		return dao.findGoodsDataCheck(selled,cid,categoryId1,start,end);
	}
	
	@Override
	public List<GoodsCheckBean> findLireData(int selled, String cid, String categoryId1, int start, int end) {
		return dao.findLireData(selled,cid,categoryId1,start,end);
	}
	
	
	@Override
	public List<GoodsCheckBean> findOrdersPurchaseInfo(int selled, String orderNo, String categoryId1, int start, int end) {
		return dao.findOrdersPurchaseInfo(selled,orderNo,categoryId1,start,end);
	}
	
	@Override
	public List<GoodsCheckBean> findTaobaoFtInfo(int goodsDataId, String aliUrl, String orderNo) {
		return dao.findTaobaoFtInfo(goodsDataId,aliUrl,orderNo);
	}
	
	@Override
	public List<GoodsCheckBean> findLireSearchData(String aliUrl, int goodDataId, String orderNo, String flag) {
		return dao.findLireSearchData(aliUrl,goodDataId,orderNo,flag);
	}
	
	
	@Override
	public List<GoodsCheckBean> searchDownLoadInfo(List<CustomOrderBean> picIdList, String type, int goodsDataId, String aliUrl, String orderNo) {
		return dao.searchDownLoadInfo(picIdList,type,goodsDataId,aliUrl,orderNo);
	}
	
	@Override
	public List<ceRong.tools.bean.GoodsCheckBean> getDownLoadCsvInfo(List<ceRong.tools.bean.CustomOrderBean> picIdList) {
		return dao.getDownLoadCsvInfo(picIdList);
	}
	
	@Override
	public List<DorpDwonBean> getLargeIndexInfo() {
		return dao.getLargeIndexInfo();
	}
	
	
	@Override
	public int updateDownLoadInfo(List<CustomOrderBean> picIdList) {
		return dao.updateDownLoadInfo(picIdList);
	}
	
	@Override
	public List<GoodsCheckBean> findGoodsDataCheckCount(int selled, String cid, String categoryId1) {
		return dao.findGoodsDataCheckCount(selled,cid,categoryId1);
	}
	
	@Override
	public int findCount(int selled,String cid,String categoryId1) {
		return dao.findCount(selled,cid,categoryId1);
	}
	
	@Override
	public int findResoultCount() {
		return dao.findResoultCount();
	}
	
	@Override
	public List<GoodsCheckBean> findTbGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end) {
		return dao.findTbGoodsDataCheck(selled,cid,categoryId1,start,end);
	}
	
	@Override
	public List<GoodsCheckBean> findYLGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end) {
		return dao.findYLGoodsDataCheck(selled,cid,categoryId1,start,end);
	}
	
	@Override
	public List<GoodsCheckBean> getErrorInfo(String userId, String timeF, String timeE, int start, int end) {
		return dao.getErrorInfo(userId,timeF,timeE,start,end);
	}
	
	
	@Override
	public List<GoodsCheckBean> findSamplInfo(int selled, String cid, String categoryId1, int start, int end) {
		return dao.findSamplInfo(selled,cid,categoryId1,start,end);
	}
	
	@Override
	public List<GoodsCheckBean> findLireImgInfo(int selled, String cid, String categoryId1, int start, int end, int flag) {
		return dao.findLireImgInfo(selled,cid,categoryId1,start,end,flag);
	}
	
	
	
	
	/**
	 * 根据工厂ID查询核心商品
	 */
	@Override
	public List<AliInfoDataBean> fingGoodsByShopId(String shop_id, int start,int end) {
		
		return dao.fingGoodsByShopId(shop_id,start,end);
	}
	
	@Override
	public List<GoodsCheckBean> findWinPic(String goodsPid) {
		return dao.findWinPic(goodsPid);
	}
	
	
	
	@Override
	public String getSourceTbl(String aliPid) {
		return dao.getSourceTbl(aliPid);
	}
	
	
	@Override
	public List<GoodsCheckBean> findGoodsDataCheck1(String cid, String path, int start, int end) {
		return dao.findGoodsDataCheck1(cid,path,start,end);
	}
	
	@Override
	public GoodsCheckBean getGoodsDataCheck(int pId, String tbId) {
		return dao.getGoodsDataCheck(pId,tbId);
	}
	
	@Override
	public GoodsCheckBean getChangeGoodData(List<Map<String,String>> listData) {
		return dao.getChangeGoodData(listData);
	}
	
	@Override
	public List<ChangeGoodBean> findChangeGoodsInfo(String orderNo, int flag) {
		return dao.findChangeGoodsInfo(orderNo,flag);
	}
	
	@Override
	public List<CategoryBean> getCategoryInfo() {
		return dao.getCategoryInfo();
	}
	
	@Override
	public List<CategoryBean> getCategoryInfo1(String cid) {
		return dao.getCategoryInfo1(cid);
	}
	
	@Override
	public int saveTbGood(TbGoodBean tbGoodBean) {
		if(dao.getTbGood(tbGoodBean.getGoodId() ) != null){
			return dao.upTbGood(tbGoodBean);
		}
		return dao.saveTbGood(tbGoodBean);
	}
	
	@Override
	public int saveDbYlGood(TbGoodBean tbGoodBean) {
		if(dao.getDbYlGood(tbGoodBean.getGoodId() ) != null){
			return dao.upDbYlGood(tbGoodBean);
		}
		return dao.saveDbYlGood(tbGoodBean);
	}
	
	@Override
	public int saveLireGood(TbGoodBean tbGoodBean) {
		if(dao.getDbLireGood(tbGoodBean ) != 0){
			if(tbGoodBean.getDelFlag()!=10 && tbGoodBean.getDelFlag()!=6){
				return dao.upDbLireGood(tbGoodBean);
			}else{
				return 0;
			}
		}
		return dao.saveDbLireGood(tbGoodBean);
	}
	
	
	
	@Override
	public int updateAliFlag(TbGoodBean tbGoodBean) {
		return dao.updateAliFlag(tbGoodBean);
	}
	
	@Override
	public int updateSourceAliFlag(TbGoodBean tbGoodBean) {
		return dao.updateSourceAliFlag(tbGoodBean);
	}
	
	
	
	@Override
	public int updateAliInfoFlag(TbGoodBean tbGoodBean) {
		return dao.updateAliInfoFlag(tbGoodBean);
	}
	
	@Override
	public int updateYlFlag(TbGoodBean tbGoodBean) {
		return dao.updateYlFlag(tbGoodBean);
	}
	
	@Override
	public int updateRebidFlag(TbGoodBean tbGoodBean) {
		return dao.updateRebidFlag(tbGoodBean);
	}
	
	@Override
	public int validationDateUpdate(String flag) {
		return dao.validationDateUpdate(flag);
	}
	
	@Override
	public int updatePolymerizationShopId(String sourceShopId,String sourceGoodsPid,String newShopId) {
		return dao.updatePolymerizationShopId(sourceShopId,sourceGoodsPid,newShopId);
	}
	
	
	
	@Override
	public int delForGoodsPid(TbGoodBean tbGoodBean) {
		return dao.delForGoodsPid(tbGoodBean);
	}
	
	
	
	@Override
	public int insertLireSearchData(List<GoodsCheckBean> picIdList) {
//		if(dao.getTbGood(tbGoodBean.getGoodId() ) != null){
//			return dao.upTbGood(tbGoodBean);
//		}
		return dao.insertLireSearchData(picIdList);
	}
	
	
	
	@Override
	public int saveChangeGoodData(ChangeGoodBean chg) {
//		if(dao.getChangeGoodData(chg.getOrderno(),chg.getChagoodurl() ) != null){
//			return dao.upChangeGood(chg);
//		}
		return dao.saveChangeGood(chg);
	}
	
	
	
	@Override
	public List<GoodsCheckBean> getOneShopInFo(String flag) {
		return dao.getOneShopInFo(flag);
	}

	@Override
	public int delNotSoldCategory(String catid1) {
		return dao.delNotSoldCategory(catid1);
	}

	@Override
	public List<Map<String, Object>> getNoSoldCategory() {
		// TODO Auto-generated method stub
		return dao.getNoSoldCategory();
	}

	@Override
	public int updateNoSoldCategoryToSold(List<String> lists) {
		// TODO Auto-generated method stub
		return dao.updateNoSoldCategoryToSold(lists);
	}

	@Override
	public void updateTime(int id,int flag) {
		dao.updateTime(id,flag);
		
	}

	@Override
	public Map<String, Object> selectupdateTime() {
		// TODO Auto-generated method stub
		return dao.selectupdateTime();
	}

	
}
