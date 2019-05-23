package com.cbt.customer.service;

import ceRong.tools.bean.DorpDwonBean;

import com.cbt.bean.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
public interface IPictureComparisonService {
	
	/**
	 * 方法描述:查看所有Aliexpress图片地址
	 * author:zlw
	 * date:2015年11月020日
	 * @return
	 */
	public List<GoodsFarBean> findByAliPicture(int maxC);
	/*
	插入商品替换日志
	 */
	public int insertChangeGoodsLog(String pUrl,String goodsType,String name,String price,String goodsCarId,String admuserid);
	
	/**
	 * 方法描述:取得lire搜索条件
	 * author:zlw
	 * date:2016年4月1日
	 * @return
	 */
	public List<GoodsFarBean> getLireSearchCondition();
	
	public List<GoodsFarBean> getImgFile();
	
	/**
	 * 方法描述:更新lire搜索条件flag=0
	 * author:zlw
	 * date:2016年4月5日
	 * @return
	 */
	public int updateLireConditionFlag(List<GoodsFarBean> beanList);
	
	public int updateImgFileUpload(List<GoodsFarBean> beanList);
	
	/**
	 * 方法描述:查看所有tb图片地址
	 * author:zlw
	 * date:2015年11月25日
	 * @return
	 */
	public List<GoodsFarBean> findByTbPicture(int maxC);
	
	public List<GoodsFarBean> findByTbStyUrl(int maxC);
	
	public List<GoodsFarBean> findByCpPicture(int maxC);
	
	public int getMaxCount();
	
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
	 * 方法描述:更新无效图片goodsdata_far[Valid]=9
	 * author:zlw
	 * date:2015年11月21日
	 * @return
	 */
	public int updateImgUrlValid(int id);

	public int updateStyle(int id, String style, String styJosn);

	/**
	 * 方法描述:更新相似度
	 * author:zlw
	 * date:2015年11月24日
	 * @return
	 */
	public int updateSimilarity(int pId, int difference, int flag);

	/**
	 * 方法描述:查看100条图片比较
	 * author:zlw
	 * date:2015年11月24日
	 * @return
	 */
	public List<GoodsCheckBean> findGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findLireData(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findGoodsDataCheckCount(int selled, String cid, String categoryId1);

	public int findCount(int selled, String cid, String categoryId1);

	public int findResoultCount();

	public List<GoodsCheckBean> findTbGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findYLGoodsDataCheck(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> getErrorInfo(String userId, String timeF, String timeE, int start, int valid, int pagesize);

	public List<GoodsCheckBean> findSamplInfo(int selled, String cid, String categoryId1, int start, int end);

	public List<GoodsCheckBean> findLireImgInfo(int selled, String cid, String categoryId1, int start, int end, int flag);

	public List<GoodsCheckBean> findImgDb(int selled, String cid, String categoryId1, int start, int end, int flag);

	/**
	 * 根据工厂ID查询核心商品
	 * @Title fingGoodsByShopId
	 * @Description TODO
	 * @param shop_id
	 * @param start
	 * @param end
	 * @return
	 * @return List<AliInfoDataBean>
	 */
	public List<AliInfoDataBean> fingGoodsByShopId(String shop_id, int start, int end);

	public List<GoodsCheckBean> findWinPic(String goodsPid);

	public String getSourceTbl(String aliPid);

	public List<GoodsCheckBean> findGoodsDataCheck1(String cid, String path, int start, int end);

	public List<GoodsCheckBean> findOrdersPurchaseInfo(int selled, String orderNo, String categoryId1, int start, int end);


	/**
	 * 方法描述:取得分类信息
	 * author:zlw
	 * date:2015年11月27日
	 * @return
	 */
	public List<CategoryBean> getCategoryInfo();

	public List<CategoryBean> getCategoryInfo1(String cid);

	/**
	 * 保存淘宝比较信息
	 * @param
	 *
	 */
	public int saveTbGood(TbGoodBean tbGoodBean);

	public int saveDbYlGood(TbGoodBean tbGoodBean);

	public int saveLireGood(TbGoodBean tbGoodBean);

    /**
     * 设置pid商品是否有效(上架或者下架)
     *
     * @param pid
     * @param adminName
     * @param adminId
     * @param type
     * @return
     */
    public int setGoodsValid(String pid, String adminName, int adminId, int type, String remark);
    
	public int insertLireSearchData(List<GoodsCheckBean> picIdList);

	public int saveChangeGoodData(ChangeGoodBean chg);

	public GoodsCheckBean getGoodsDataCheck(int pId, String taId);

	public GoodsCheckBean getChangeGoodData(List<Map<String, String>> listData);

	public int cancelChangeGoodData(String orderNo);

	public int updateCgdEmailFlag(String orderNo);

	public int insertOnlineChange(String orderNo);

	public int getUserIdByOrder(String orderNo);

	public List<ChangeGoodBean> findChangeGoodsInfo(String orderNo, int flag);

	public int updateChangeType(String pUrl, String goodsType, String name, String price, String goodsCarId);

	public int updateSourceFlag(String url, int flag);

	public int updateLireFlag(String pid);

	public int updateTbName(String goodsId, String tbName1, String tbName2, String tbName3, String tbName4);

	/**
	 * 查询淘宝辅图信息
	 */
	public List<GoodsCheckBean> findTaobaoFtInfo(int goodsDataId, String aliUrl, String orderNo);

	/**
	 * 查询本地lire搜索信息
	 */
	public List<GoodsCheckBean> findLireSearchData(String aliUrl, int goodsDataId, String orderNo, String flag);


	/**
	 * 查询1688相似图片信息
	 */
	public List<GoodsCheckBean> searchDownLoadInfo(List<CustomOrderBean> picIdList, String type, int goodsDataId, String aliUrl, String orderNo);

	/**
	 * 取得抓取csv数据
	 */
	public List<ceRong.tools.bean.GoodsCheckBean> getDownLoadCsvInfo(List<ceRong.tools.bean.CustomOrderBean> picIdList);

	public List<DorpDwonBean> getLargeIndexInfo();

	public int updateDownLoadInfo(List<CustomOrderBean> picIdList);

	public int updateAliFlag(TbGoodBean tbGoodBean);

	public int updateSourceAliFlag(TbGoodBean tbGoodBean);

	public int delForGoodsPid(TbGoodBean tbGoodBean);

	public int updateAliInfoFlag(TbGoodBean tbGoodBean);

	public int updateYlFlag(TbGoodBean tbGoodBean);

	public int updateRebidFlag(TbGoodBean tbGoodBean);

	public int validationDateUpdate(String flag);

	public List<GoodsCheckBean> getOneShopInFo(String flag);

	public int updatePolymerizationShopId(String sourceShopId, String sourceGoodsPid, String newShopId);

	public int delNotSoldCategory(String catid1);

	public List<Map<String, Object>> getNoSoldCategory();

	public int updateNoSoldCategoryToSold(List<String> lists);

	public void updateTime(int id, int flag);

	public Map<String, Object> selectupdateTime();

	
}
