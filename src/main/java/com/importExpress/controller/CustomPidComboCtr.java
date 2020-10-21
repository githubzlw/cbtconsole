package com.importExpress.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.pojo.Admuser;
import com.cbt.service.CustomGoodsService;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.CustomPidComboBean;
import com.importExpress.pojo.CustomPidComboQuery;
import com.importExpress.service.CustomPidComboService;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.UserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.controller
 * @date:2020-10-19
 */
@RestController
@Slf4j
@RequestMapping("/customPidCombo")
public class CustomPidComboCtr {

    @Autowired
    private CustomPidComboService comboService;

    @Autowired
    private CustomGoodsService goodsService;

    @RequestMapping("/getList")
    public JsonResult getList(CustomPidComboQuery comboQuery) {
        if (comboQuery == null) {
            comboQuery = new CustomPidComboQuery();
        }
        if (comboQuery.getPage() == 0) {
            comboQuery.setPage(1);
        }
        if (comboQuery.getLimitNum() == 0) {
            comboQuery.setLimitNum(20);
        }
        comboQuery.setStartNum((comboQuery.getPage() - 1) * comboQuery.getLimitNum());

        if (StringUtils.isBlank(comboQuery.getPid())) {
            comboQuery.setPid(null);
        }
        if (StringUtils.isBlank(comboQuery.getUuid())) {
            comboQuery.setUuid(null);
        }
        if (StringUtils.isBlank(comboQuery.getCreate_time())) {
            comboQuery.setCreate_time(null);
        }
        if (StringUtils.isBlank(comboQuery.getUpdate_time())) {
            comboQuery.setUpdate_time(null);
        }
        try {
            List<CustomPidComboBean> rsList = new ArrayList<>();
            int count = comboService.queryForListCount(comboQuery);
            Map<String, List<CustomPidComboBean>> rsMap = new HashMap<>();
            if (count > 0) {
                rsList = comboService.queryForList(comboQuery);

                rsList = rsList.stream().skip(comboQuery.getStartNum()).limit(comboQuery.getLimitNum()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(rsList)) {
                    List<String> pidList = new ArrayList<>();
                    Map<String, CustomPidComboBean> mapBean = new HashMap<>();
                    for (CustomPidComboBean tempBean : rsList) {
                        pidList.add(tempBean.getPid());
                        mapBean.put(tempBean.getPid(), tempBean);
                    }

                    List<CustomGoodsPublish> pidInfos = goodsService.queryGoodsByPidList(pidList);
                    if (CollectionUtils.isNotEmpty(pidInfos)) {
                        pidInfos.forEach(e -> {
                            if (e.getShowMainImage().contains("http")) {
                                mapBean.get(e.getPid()).setImg_url(e.getShowMainImage());
                            } else {
                                mapBean.get(e.getPid()).setImg_url(e.getRemotpath() + e.getShowMainImage());
                            }
                            mapBean.get(e.getPid()).setValid(e.getValid());
                        });
                    }

                    List<CustomPidComboBean> mainList = rsList.stream().filter(e -> e.getIs_main() > 0).collect(Collectors.toList());
                    List<CustomPidComboBean> childList = rsList.stream().filter(e -> e.getIs_main() == 0).collect(Collectors.toList());


                    pidList.clear();
                    mapBean.clear();
                    rsList.clear();

                    rsMap.put("mainList", mainList);
                    rsMap.put("childList", childList);
                }
            }
            return JsonResult.success(rsMap, count);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getList,comboQuery[{}]", comboQuery, e);
            return JsonResult.error(e.getMessage());
        }
    }


    @RequestMapping("/insertCombo")
    public JsonResult insertCombo(CustomPidComboBean comboBean, HttpServletRequest request) {

        if (comboBean == null) {
            return JsonResult.error("获取数据异常");
        }
        if (StringUtils.isBlank(comboBean.getPid())) {
            return JsonResult.error("pid null");
        }
        if (comboBean.getIs_main() == 0 && comboBean.getDiv_price() == 0) {
            return JsonResult.error("获取价格异常");
        }

        try {

            Admuser userInfo = UserInfoUtils.getUserInfo(request);
            if (userInfo == null || userInfo.getId() == 0) {
                return JsonResult.error("请登录后操作");
            }

            CustomPidComboQuery comboQuery = new CustomPidComboQuery();
            comboQuery.setUuid(comboBean.getUuid());
            comboQuery.setPid(comboBean.getPid());


            List<CustomPidComboBean> beanList = comboService.queryForList(comboQuery);
            if (CollectionUtils.isNotEmpty(beanList)) {
                if (comboBean.getIs_main() == 1) {
                    long count = beanList.stream().filter(e -> e.getIs_main() > 0).count();
                    if (count > 0) {
                        return JsonResult.error("已经存在此PID");
                    }
                } else {
                    return JsonResult.error("已经存在此PID");
                }
            }

            comboBean.setAdmin_id(userInfo.getId());
            if (comboBean.getIs_main() == 1 && StringUtils.isBlank(comboBean.getUuid())) {
                String uuid = UUID.randomUUID().toString();
                comboBean.setUuid(uuid);
            }

            comboService.insertCustomPidCombo(comboBean);
            if (comboBean.getId() > 0) {
                insertMq(comboBean);
                return JsonResult.success(1);
            } else {
                return JsonResult.error("插入失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("insertInfo,comboBean[{}]", comboBean, e);
            return JsonResult.error(e.getMessage());
        }
    }


    @RequestMapping("/editInfo")
    public JsonResult editInfo(CustomPidComboBean comboBean, HttpServletRequest request) {

        if (comboBean == null) {
            return JsonResult.error("获取数据异常");
        }
        if (StringUtils.isBlank(comboBean.getPid())) {
            return JsonResult.error("pid null");
        }
        if (comboBean.getIs_main() == 0 && comboBean.getDiv_price() == 0) {
            return JsonResult.error("获取价格异常");
        }

        try {

            Admuser userInfo = UserInfoUtils.getUserInfo(request);
            if (userInfo == null || userInfo.getId() == 0) {
                return JsonResult.error("请登录后操作");
            }
            if (comboBean.getDiv_price() <= 0) {
                return JsonResult.error("获取优惠减去价格异常");
            }
            comboBean.setUpdate_admin(userInfo.getId());
            int count = comboService.updateCustomPidCombo(comboBean);
            if (count > 0) {
                updateMq(comboBean);
                return JsonResult.success(1);
            } else {
                return JsonResult.error("更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("editInfo,comboBean[{}]", comboBean, e);
            return JsonResult.error(e.getMessage());
        }
    }

    @RequestMapping("/deleteMainCombo")
    public JsonResult deleteMainCombo(CustomPidComboBean comboBean, HttpServletRequest request) {

        Admuser userInfo = UserInfoUtils.getUserInfo(request);
        if (userInfo == null || userInfo.getId() == 0) {
            return JsonResult.error("请登录后操作");
        }
        if (comboBean == null) {
            return JsonResult.error("获取数据异常");
        }
        if (StringUtils.isBlank(comboBean.getPid())) {
            return JsonResult.error("pid null");
        }
        if (StringUtils.isBlank(comboBean.getUuid())) {
            return JsonResult.error("uuid null");
        }
        try {

            int count = comboService.deleteCustomPidCombo(comboBean);
            if (count > 0) {
                deleteMq(comboBean);
                return JsonResult.success(1);
            } else {
                return JsonResult.error("删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("editInfo,comboBean[{}]", comboBean, e);
            return JsonResult.error(e.getMessage());
        }
    }


    private void insertMq(CustomPidComboBean comboBean) {
        String sql = "insert into custom_pid_combo(id,pid,is_main,uuid,div_price,admin_id)" +
                " values (" + comboBean.getId() + ",'" + comboBean.getPid() + "'," + comboBean.getIs_main() + ",'" + comboBean.getUuid() + "'," + comboBean.getDiv_price() + "," + comboBean.getAdmin_id() + ")";
        System.err.println(sql);
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

    private void updateMq(CustomPidComboBean comboBean) {
        String sql = "update custom_pid_combo set div_price = " + comboBean.getDiv_price() + " where pid = '" + comboBean.getPid() + "' and uuid = '" + comboBean.getUuid() + "'";
        System.err.println(sql);
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

    private void deleteMq(CustomPidComboBean comboBean) {
        String sql = "delete from custom_pid_combo where uuid = '" + comboBean.getUuid() + "'";
        if (comboBean.getIs_main() == 0) {
            sql += " and pid = '" + comboBean.getPid() + "' ";
        }
        System.err.println(sql);
        NotifyToCustomerUtil.sendSqlByMq(sql);
    }

}
