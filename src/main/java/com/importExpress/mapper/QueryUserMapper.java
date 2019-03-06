package com.importExpress.mapper;

import com.cbt.website.userAuth.bean.AuthInfo;
import com.importExpress.pojo.GoodsInfoSpiderPO;
import com.importExpress.pojo.ItemStaticFile;
import com.importExpress.pojo.StandardGoodsFormDataPO;
import com.importExpress.pojo.UserXlsBean;
import org.apache.ibatis.annotations.Param;

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

    List<HashMap<String,String>> queryCarGoodsCount(@Param("list") List<UserXlsBean> list);

    String querySearchKeywords(@Param("id") Integer id);
}
