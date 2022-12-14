package com.importExpress.mapper;

import com.cbt.bean.SameGoodsDetails;
import com.cbt.pojo.ShareableOrder;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.importExpress.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface QueryUserMapper {

	List<String> queryDropShipUserId();

	List<String> queryRandomUser(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("haveOrder") boolean haveOrder);

	List<String> queryLoginUser(List<String> list);

	List<Map<String, Object>> queryUserName(List<String> userIdList);

	List<GoodsInfoSpiderPO> queryMarkedList(@Param("rows") Integer rows, @Param("orderby") String orderby);

	void updateMarkedById(@Param("id") Long id, @Param("marked") Integer marked);

	Long queryNoMarkedTotal();

	void createStandardGoodsForm();

	String queryCreatetime();

	List<StandardGoodsFormDataPO> queryStandardGoodsFormList(@Param("startBars") Integer startBars, @Param("rows") Integer rows, @Param("flag") Integer flag, @Param("bmFlag") Integer bmFlag, @Param("valid") Integer valid);

	/**
	 * 查询对标商品总数
	 * @param flag 0-默认值；1-优势商品；2-劣势商品；
	 * @param bmFlag 是否人为对标货源,1:是， 2：不是 ,0:默认值
	 * @param valid 在线状态：1-在线；2-软下架；
	 * @return
	 */
	Long queryStandardGoodsFormCount(@Param("flag") Integer flag, @Param("bmFlag") Integer bmFlag, @Param("valid") Integer valid);

	Long queryNoStandardGoodsFormCount();

	List<String> queryUserByPrice(@Param("price") Integer price);

	List<String> queryCarGoods();

	List<ItemStaticFile> queryStaticizeData();

	void insertStaticizeData(List<ItemStaticFile> list);

    void insertOrderShare(@Param("list") List<OrderShare> list,@Param("shopType") String shopType,@Param("orderNo") String orderNo);

	String queryStaticizeTime();

	AuthInfo queryAuthInfo(@Param("authId") Integer authId);

    String queryAvailable(@Param("email") String email);

    long updateAvailable(@Param("email") String email, @Param("available") Double available);

    long queryAdmUserByAdmNameAndPwd(@Param("admName") String admName, @Param("oldPwd") String oldPwd);

    void updatePasswordByAdmName(@Param("admName") String admName, @Param("newPwd") String newPwd);

    long insertAuthInfo(AuthInfo authInfo);

    long updateAuthInfo(AuthInfo authInfo);

    Integer queryOrderNoByModuleType(@Param("moduleType") int moduleType);

    List<String> queryBoughtGoods(@Param("startDate") String startDate);

    List<String> queryCarProducts(@Param("startDate") String startDate);

    void insertNeedoffshelfSoldPid(@Param("list") List<String> list, @Param("type") Integer type);

    void deleteNeedoffshelfSoldAll();

    List<String> queryIsEditProducts();

    List<String> queryInventoryProducts();

    List<String> queryGoodsWeightNoSyn();

    List<UserXlsBean> queryUserList(@Param("startBars") Integer startBars, @Param("rows") Integer rows, @Param("userType") Integer userType,
                                    @Param("startDate") String startDate, @Param("endDate") String endDate);

    Integer queryUserListCount(@Param("userType") Integer userType,
                               @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<String> queryUserAddressById(@Param("id") Integer id);

    List<String> queryUserExById(@Param("id") Integer id);

    String queryClickGoodsCount(@Param("id") Integer id);

    void updateNeedoffshellEditFlag(@Param("list") List<String> list);

    void updateNeedoffshelfByPid(@Param("pid") String pid, @Param("noShelfInfo") String noShelfInfo);

    List<HashMap<String,String>> queryCarGoodsCount(@Param("list") List<UserXlsBean> list);

    String querySearchKeywords(@Param("id") Integer id);

    Integer queryGoodsReviewListCount(@Param("goodsPid") String goodsPid, @Param("reviewRemark") String reviewRemark,
                                 @Param("type") Integer type, @Param("reviewFlag") Integer reviewFlag,
                                 @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<GoodsReview> queryGoodsReviewList(@Param("startBars") Integer startBars, @Param("rows") Integer rows,
                                      @Param("goodsPid") String goodsPid, @Param("reviewRemark") String reviewRemark,
                                      @Param("type") Integer type, @Param("reviewFlag") Integer reviewFlag,
                                      @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<OrderShare> queryOrderShareList(@Param("startBars") Integer startBars, @Param("rows") Integer rows,
                                           @Param("shopType") String shopType, @Param("orderNo") String orderNo);

    Integer queryOrderShareListCount(@Param("shopType") String shopType, @Param("orderNo") String orderNo);

    void insertOrderShare(List<OrderShare> list);

    GoodsReview queryGoodsReviewById(@Param("id") Integer id);

    List<String> queryAddCarList(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<String> querySearchProductList(@Param("startDate") String startDate, @Param("endDate") String endDate);

    String queryProductsStockverification1688data(@Param("pid") String pid);

    List<SameGoodsDetails> querySameGoodsDetails(@Param("pid") String pid);

    void insertNeedoffDownAll(@Param("list") List<String> list, @Param("reason") Integer reason, @Param("adminid") Integer adminid);

    List<Map<String,String>> queryUnsellablereasonMaster();
    @Select({"<script> SELECT a.share, a.orderNo,a.userid,a.paymentamount,a.creatime,b.name FROM paysuccessinfo a LEFT JOIN `user` b ON a.userid=b.id where a.sharechoice=1 <when test='orderNo != null'> and a.orderNo = #{orderNo} </when> " +
            "<when test='name != null'> and b.name = #{name} </when>" +
            "<when test='userid != 0'> and a.userid = #{userid} </when>" +
            "<when test='share != -1'> and a.share = #{share} </when>" +
            " LIMIT #{startBars},#{rows}</script>"})
    List<ShareableOrder> lookShareableOrder(@Param("startBars") int startBars, @Param("rows") Integer rows, @Param("orderNo") String orderNo,@Param("name")String name,@Param("userid")int userid,@Param("share")int share);
    @Select({"<script> SELECT COUNT(1) FROM paysuccessinfo a LEFT JOIN `user` b ON a.userid=b.id where a.sharechoice=1 <when test='orderNo != null'> and a.orderNo = #{orderNo} </when> " +
            "<when test='name != null'> and b.name = #{name} </when>" +
            "<when test='userid != 0'> and a.userid = #{userid} </when>" +
            "<when test='share != -1'> and a.share = #{share} </when>" +
            " </script>"})
    Integer lookShareableOrderCount(@Param("orderNo") String orderNo,@Param("name")String name,@Param("userid")int userid,@Param("share")int share);
    @Update("UPDATE paysuccessinfo SET share=1 WHERE orderno=#{orderNo}")
    void SetShareByOrderno(@Param("orderNo") String orderNo);

    List<String> querySalesShop(@Param("list") List<SameGoodsDetails> list);

    void insertLoginLog(@Param("userid") Integer userid, @Param("admid") Integer admid, @Param("site") Integer site);

    void updateUserCheckout(@Param("userid") Integer userid, @Param("type") Integer type);

    List<TimingWarningInfo> queryTimingWarningInfo(@Param("valid") Integer valid, @Param("day") Integer day);

    TimingWarningInfo queryQuotaData(@Param("id") Integer id);

    void udpateQuotaData(@Param("bean") TimingWarningInfo bean);

    List<Integer> queryAllCheckout(@Param("flag") int flag, @Param("updateTime") String updateTime);
}
