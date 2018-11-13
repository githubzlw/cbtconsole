package com.cbt.service;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.website.userAuth.bean.Admuser;

import java.util.List;

public interface ProductOfflineService {


	/**
	 * 查询参数下类别和商品总数统计
	 * 
	 * @param queryBean
	 * @return
	 */
	public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean);

	
	/**
	 * 获取指定产品
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param type
	 *            0-本地 1-线上
	 * @return
	 */
	public CustomGoodsPublish getGoods(String pid, int type);


	/**
	 * 获取产品集合
	 * 
	 * @date 2017年3月21日
	 * @author abc
	 * @param catid
	 *            类别id
	 * @return
	 */
	public List<CustomGoodsBean> getGoodsListByCatid(String catid);


	/**
	 * 更新指定产品详情数据
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param bean
	 *            产品数据
	 * @return
	 */
	public int updateInfo(CustomGoodsBean bean);

	/**
	 * 批量更新产品详情数据
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param list
	 *            产品数据
	 * @return
	 */
	public int updateInfoList(List<CustomGoodsBean> list);

	/**
	 * 发布数据到线上
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param bean
	 * @return
	 */
	public int publish(CustomGoodsPublish bean);


	/**
	 * 更新本地产品状态
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param state
	 *            2-产品下架 3-发布失败 4-发布成功
	 * @param adminid
	 *            操作人id
	 * @return
	 */
	public int updateState(int state, String pid, int adminid);


	/**
	 * 更新线上产品状态
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param valid
	 *            0-下架 1-在线
	 * @return
	 */
	public int updateValid(int valid, String pid);


	/**
	 * 查询产品、翻译和对应ali商品的数据
	 * 
	 * @param queryBean
	 * @return
	 */
	public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean);

	/**
	 * 查询参数下的总数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int queryGoodsInfosCount(CustomGoodsQuery queryBean);

	/**
	 * 批量更新商品的翻译名称
	 * 
	 * @param user
	 * @param cgLst
	 * @return
	 */
	public void batchSaveEnName(Admuser user, List<CustomGoodsBean> cgLst);

	/**
	 * 根据pid查询本地的1688商品以及对于翻译和ali商品的数据
	 * 
	 * @param pid
	 * @return
	 */
	public CustomGoodsPublish queryGoodsDetails(String pid, int type);

	/**
	 * 保存编辑后的1688对应电商网站的详情信息
	 * 
	 * @param cgp
	 * @param adminId
	 * @param type
	 * @return
	 */
	public int saveEditDetalis(CustomGoodsPublish cgp, int adminId, int type);
	

	/**
	 * 
	 * @Title replaceDetalisImgToLocal 
	 * @Description 替换网络图片到本地图片数据
	 * @param cgp
	 * @param adminId
	 * @return int
	 */
	public int replaceDetalisImgToLocal(CustomGoodsPublish cgp, int adminId);

	/**
	 * 
	 * @Title batchDeletePids 
	 * @Description 批量删除
	 * @param pidLst
	 * @return boolean
	 */
	public boolean batchDeletePids(String[] pidLst);
	
	/**
	 * 
	 * @Title publishGoods 
	 * @Description 走流程发布到线上到28库，上架中的商品flag标识
	 * @param pid
	 * @param adminId
	 * @return boolean
	 */
	public boolean publishGoods(String pid, int adminId);

}
