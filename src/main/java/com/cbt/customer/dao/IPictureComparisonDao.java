package com.cbt.customer.dao;

import ceRong.tools.bean.DorpDwonBean;

import com.cbt.bean.*;

import java.util.List;
import java.util.Map;

public interface IPictureComparisonDao {
	
	/**
	 * 方法描述:查看所有Aliexpress图片地址
	 * author:zlw
	 * date:2015年11月020日
	 * @return
	 */
	public List<GoodsFarBean> findByAliPicture(int maxC);
	
	public List<GoodsFarBean> getLireSearchCondition();
		/*
		插入商品替换日志
	 */
	public int insertChangeGoodsLog(String pUrl,String goodsType,String name,String price,String goodsCarId,String admuserid);
	public List<GoodsFarBean> getImgFile();
	
	public List<GoodsFarBean> findByTbPicture(int maxC);
	
	public List<GoodsFarBean> findByTbStyUrl(int maxC);
	
	public List<GoodsFarBean> findByCpPicture(int maxC);
	
	public int getMaxCount();
	
	public int updateLireConditionFlag(List<GoodsFarBean> beanList);
	
	public int updateImgFileUpload(List<GoodsFarBean> beanList);
	
	public int getTbMaxCount();
	
	public int getTbStyMaxCount();
	
	public int getCpMaxCount();
	
	public int getGoodsCheckCount(int selled, String similarityId, String categoryId1);

	public int getOrderPurchaseCount(int selled, String similarityId, String categoryId1);

	public int getTbgooddataCount();

	public int getYlbbGooddataCount(String userName, int flag);

	public int getErrorInfoCount(String userId,String timeF,String timeE,int valid);
	
	public int getLireImgCount(String userName, int flag);

	public int getFactoryCount(String cid, String shopId);

	public int getShelfCount();

	public int getTbCount1(String categoryId);

	public int getTbCount2(String categoryId);

	public int getTbCount3(String categoryId);

	public int getTbCount4(String categoryId);

	public int getTbCount5(String categoryId);

	public int getTbCount6(String categoryId);

	public int getTbCount7(String categoryId);

	public int getTbCount8(String categoryId);

	public int getTbCount9(String categoryId);

	public int getTbCount10(String categoryId);



	/**
	 * 方法描述:更新无效图片url
	 * author:zlw
	 * date:2015年11月21日
	 * @return
	 */
	public int updateImgUrlValid(int id);

	public int updateStyle(int id, String style, String styJosn);

	public int updateChangeType(String pUrl, String goodsType, String name, String price, String goodsCarId);

	public int updateSourceFlag(String url, int flag);

	public int updateLireFlag(String pid);


	public int updateTbName(String goodsId, String tbName1, String tbName2, String tbName3, String tbName4);

	/**
	 * 方法描述:更新相似度
	 * author:zlw
	 * date:2015年11月24日
	 * @return
	 */
	public int updateSimilarity(int pId, int difference, int flag);

	public int cancelChangeGoodData(String orderNo);

	public int updateCgdEmailFlag(String orderNo);

	public int insertOnlineChange(String orderNo);

	public int getUserIdByOrder(String orderNo);

	/**
	 * 方法描述:查看所有Aliexpress图片地址
	 * author:zlw
	 * date:2015年11月24日
	 * @return
	 */
	public List<GoodsCheckBean> findGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findLireData(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findOrdersPurchaseInfo(int selled, String orderNo, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findTaobaoFtInfo(int goodsDataId, String aliUrl, String orderNo);

	public List<GoodsCheckBean> findLireSearchData(String aliUrl, int goodDataId, String orderNo, String flag);
	/**
	 * 查询1688相似图片信息
	 */
	public List<GoodsCheckBean> searchDownLoadInfo(List<CustomOrderBean> picIdList, String type, int goodsDataId, String aliUrl, String orderNo);

	public List<ceRong.tools.bean.GoodsCheckBean> getDownLoadCsvInfo(List<ceRong.tools.bean.CustomOrderBean> picIdList);

	public List<DorpDwonBean> getLargeIndexInfo();

	public int updateDownLoadInfo(List<CustomOrderBean> picIdList);


	public List<GoodsCheckBean> findGoodsDataCheckCount(int selled, String cid, String categoryId1);

	public int findCount(int selled, String cid, String categoryId1);

	public int findResoultCount();

	public List<GoodsCheckBean> findTbGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findYLGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> getErrorInfo(String userId, String timeF, String timeE, int start, int end, int valid);

	public List<GoodsCheckBean> findSamplInfo(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findLireImgInfo(int selled, String cid, String categoryId1, int start, int end, int flag);

	public List<GoodsCheckBean> findImgDb(int selled, String cid, String categoryId1, int start, int end, int flag);

	public List<GoodsCheckBean> findWinPic(String goodsPid);

	public String getSourceTbl(String aliPid);

	public List<GoodsCheckBean> findGoodsDataCheck1(String cid, String path, int start, int end);
	/**
	 * 根据工厂ID查询核心商品
	 */
	public List<AliInfoDataBean> fingGoodsByShopId(String shop_id, int start, int end);
	public List<CategoryBean> getCategoryInfo();

	public List<CategoryBean> getCategoryInfo1(String cid);

	/**
	 * 保存 淘宝相关信息
	 *
	 */
	public int saveTbGood(TbGoodBean tbGoodBean);

	public int upDbYlGood(TbGoodBean tbGoodBean);

	public int upDbLireGood(TbGoodBean tbGoodBean);

	public int insertLireSearchData(List<GoodsCheckBean> picIdList);

	public int saveChangeGood(ChangeGoodBean chaGoodBean);

	/**
	 * 查询 淘宝相关信息
	 *
	 */
	public TbGoodBean getTbGood(String goodId);

	public TbGoodBean getDbYlGood(String goodId);

	public int getDbLireGood(TbGoodBean tbGoodBean);

	public GoodsCheckBean getGoodsDataCheck(int pId, String tbId);

	public GoodsCheckBean getChangeGoodData(List<Map<String, String>> listData);

	public List<ChangeGoodBean> findChangeGoodsInfo(String orderNo, int flag);

	public ChangeGoodBean getChangeGoodData(String orderNo, String chaGoodUrl);

	/**
	 * 修改 淘宝相关信息
	 *
	 */
	public int upTbGood(TbGoodBean tbGoodBean);

	public int saveDbYlGood(TbGoodBean tbGoodBean);

	public int saveDbLireGood(TbGoodBean tbGoodBean);

	public int upChangeGood(ChangeGoodBean chaGoodBean);

	public int updateAliFlag(TbGoodBean tbGoodBean);

	public int updateSourceAliFlag(TbGoodBean tbGoodBean);

	public int delForGoodsPid(TbGoodBean tbGoodBean);

	public int updateAliInfoFlag(TbGoodBean tbGoodBean);

	public int updateYlFlag(TbGoodBean tbGoodBean);

	public int updateRebidFlag(TbGoodBean tbGoodBean);

	public int validationDateUpdate(String flag);

	public int updatePolymerizationShopId(String sourceShopId, String sourceGoodsPid, String newShopId);

	public void insertDelFlag_New(Map<String, String> map);

	List<Map<String, String>> getDataForDel(String startid);

	/**获取历史价格
	 * @author user4
	 * @param ali_pid
	 * @return
	 */
	String getAliexpressHistory(String ali_pid);

	public List<GoodsCheckBean> getOneShopInFo(String cid);

	public int delNotSoldCategory(String catid1);

	public List<Map<String, Object>> getNoSoldCategory();

	public int updateNoSoldCategoryToSold(List<String> lists);

	public void updateTime(int id, int flag);

	public Map<String, Object> selectupdateTime();

}
