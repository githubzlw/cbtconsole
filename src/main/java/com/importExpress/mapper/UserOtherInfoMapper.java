package com.importExpress.mapper;

import com.importExpress.pojo.UserOtherInfoBean;

import java.util.List;

public interface UserOtherInfoMapper {

    /**
     * 跟进查询条件分页查询
     *
     * @param userOtherInfo : 查询bean
     * @return
     */
    List<UserOtherInfoBean> queryForList(UserOtherInfoBean userOtherInfo);


    /**
     * 跟进查询条件查询总数
     *
     * @param userOtherInfo : 查询bean
     * @return
     */
    int queryForListCount(UserOtherInfoBean userOtherInfo);


     /**
     * 保存跟进信息
     * @param userOtherInfo : 信息bean
     * @return
     */
    int updateFollowInfo(UserOtherInfoBean userOtherInfo);

    /**
     * 插入跟进日志
     * @param userOtherInfo
     * @return
     */
    int insertFollowInfoByAdminId(UserOtherInfoBean userOtherInfo);


}
