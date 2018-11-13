package com.cbt.dao;

import com.cbt.bean.CustomOnlineGoodsBean;
import com.cbt.bean.SameTypeGoodsBean;
import com.cbt.pojo.Admuser;
import com.cbt.website.util.JsonResult;

import java.util.List;

public interface SameTypeGoodsDao {
	/**
	 * 批量添加1688url
	 * 
	 * @Title batchAddUrl
	 * @Description 批量插入主图和同款商品
	 * @param mainUrl
	 * @param urls
	 * @param adminId
	 * @param typeFlag
	 * @param aveWeight
	 * @return 执行结果
	 * @return JsonResult
	 */
	public JsonResult batchAddUrl(String mainUrl, String urls, int adminId, int typeFlag, double aveWeight);

	/**
	 *
	 * @Title batchAddTypeUrl
	 * @Description 批量插入主图下的同款商品
	 * @param mainUrl
	 * @param urls
	 * @param adminId
	 * @param typeFlag
	 * @param aveWeight
	 * @return 执行结果
	 * @return JsonResult
	 */
	public JsonResult batchAddTypeUrl(String mainUrl, String urls, int adminId, int typeFlag, double aveWeight);

	/**
	 *
	 * @Title queryDealGoods
	 * @Description 查询所有已处理的数据的主图、规格图、橱窗图和详情图的数据
	 * @return 商品数据集合
	 * @return List<CustomGoodsPublish>
	 */
	public List<CustomOnlineGoodsBean>  queryDealGoods();



	/**
	 *
	 * @Title updateGoodsImg
	 * @Description 更新下载好的图片
	 * @param goods
	 * @return 是否更新成功
	 * @return boolean
	 */
	public boolean updateGoodsImg(CustomOnlineGoodsBean goods);

	/**
	 *
	 * @Title updateGoodsImgError
	 * @Description 下载图片失败更新pid标识
	 * @param goods
	 * @return
	 * @return boolean
	 */
	public boolean updateGoodsImgError(CustomOnlineGoodsBean goods);

	/**
	 *
	 * @Title queryGoodsByPid
	 * @Description 查询商品的所有信息
	 * @param pid
	 * @return 商品所有数据
	 * @return CustomOnlineGoodsBean
	 */
	public CustomOnlineGoodsBean queryGoodsByPid(String pid);

	/**
	 *
	 * @Title syncSingleGoodsToOnline
	 * @Description 单个商品到线上
	 * @param goods
	 * @return 是否同步成功
	 * @return boolean
	 */
	public boolean syncSingleGoodsToOnline(CustomOnlineGoodsBean goods);

	/**
	 *
	 * @Title updateSameTypeGoodsError
	 * @Description 更新同款商品关系表失败标识
	 * @param pid
	 * @return boolean
	 */
	public boolean updateSameTypeGoodsError(String pid);


	/**
	 *
	 * @Title queryForList
	 * @Description 根据条件查询数据
	 * @param type
	 * @param adminId
	 * @param start
	 * @param limitNum
	 * @return 数据集合
	 * @return List<SameTypeGoodsBean>
	 */
	public List<SameTypeGoodsBean> queryForList(int type, int adminId, int start, int limitNum);

	/**
	 *
	 * @Title queryForListCount
	 * @Description 查询结果集总数
	 * @param type
	 * @param adminId
	 * @return 总数
	 * @return int
	 */
	public int queryForListCount(int type, int adminId);

	/**
	 *
	 * @Title queryListByMainPid
	 * @Description 根据MainPid查询数据
	 * @param mainPid
	 * @return 数据集合
	 * @return List<SameTypeGoodsBean>
	 */
	public List<SameTypeGoodsBean> queryListByMainPid(String mainPid);


	/**
	 *
	 * @Title queryAllAdmin
	 * @Description 查询所有的管理员数据
	 * @return 数据集合
	 * @return List<Admuser>
	 */
	public List<Admuser> queryAllAdmin();

	/**
	 *
	 * @Title deleteGoodsByPid
	 * @Description 根据mainPid删除数据
	 * @param mainPid
	 * @return
	 * @return boolean
	 */
	public boolean deleteGoodsByMainPid(String mainPid);

	/**
	 *
	 * @Title deleteGoodsByPid
	 * @Description 根据mainPid和pid删除数据
	 * @param mainPid
	 * @param pid
	 * @return
	 * @return boolean
	 */
	public boolean deleteGoodsByPid(String mainPid, String pid);

	/**
	 *
	 * @Title queryNoDealGoods
	 * @Description 查询没有处理的商品信息
	 * @return id集合
	 * @return List<Integer>
	 */
	public List<Integer> queryNoDealGoods();

	/**
	 *
	 * @Title batchUpdateDlFlag
	 * @Description 批量更新正在处理的flag
	 * @param ids
	 * @return 更新成功数量
	 * @return int
	 */
	public int batchUpdateDlFlag(List<Integer> ids);


	/**
	 *
	 * @Title replaceGoodsMainPid
	 * @Description 替换主图pid
	 * @param newPid 新的pid
	 * @param oldPid 原始主图pid
	 * @return
	 * @return boolean
	 */
	public boolean replaceGoodsMainPid(String newPid, String oldPid);

	/**
	 *
	 * @Title useGoodsByState
	 * @Description 是否启用商品 0:不启用 1:启用
	 * @param state
	 * @param pids
	 * @return
	 * @return boolean
	 */
	public boolean useGoodsByState(int state, String pids);
}
