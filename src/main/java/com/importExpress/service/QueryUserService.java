package com.importExpress.service;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.GoodsReview;

import java.util.List;
import java.util.Map;


public interface QueryUserService {

	List<String> queryUser();

	Map<String, Object> queryMarkedList(Integer rows, String orderby);

	void updateMarkedById(Long id, Integer marked);

	Map<String, Object> createStandardGoodsForm();

	Map<String, Object> queryStandardGoodsFormCreatetime();

	Map<String, Object> queryStandardGoodsFormList(Integer page, Integer rows, Integer flag, Integer bmFlag, Integer valid);

	List<String> queryUserByPrice(Integer price);

	String createStaticizeForm(Integer flag);

	AuthInfo queryAuthInfo(Integer authId);

    String queryAvailable(String email);

    long updateAvailable(String email, Double available);

    JsonResult resetPwd(String admName, String oldPwd, String newPwd);

    long updateAuthInfo(AuthInfo authInfo, String url, String urlFlag, String colorFlag);

    void updateNeedOffShelfData();

    List<String> queryGoodsWeightNoSyn();

    EasyUiJsonResult queryUserList(Integer page, Integer rows, Integer userType, String startDate, String endDate);

    Map<String,Object> queryUserOtherInfo(Integer id, Integer userType);

    Map<String, Object> updateNeedoffshellEditFlag(String pids);

    Map<String,String> queryUserListCsv(Integer userType, String startDate, String endDate);

    EasyUiJsonResult queryGoodsReviewList(Integer page, Integer rows, String goodsPid, String reviewRemark, Integer type,
                                     Integer reviewFlag, String startDate, String endDate);

    GoodsReview queryGoodsReviewById(Integer id);

    void updateNeedoffshelfByPid(String pid, String noShelfInfo);
}
