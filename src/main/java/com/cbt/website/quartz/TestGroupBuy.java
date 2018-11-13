package com.cbt.website.quartz;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.util.BigDecimalUtil;
import com.cbt.warehouse.dao.GroupBuyDao;
import com.cbt.warehouse.dao.GroupBuyDaoImpl;
import com.cbt.warehouse.pojo.GroupBuyManageBean;
import com.cbt.warehouse.pojo.UserCouponBean;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestGroupBuy {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TestGroupBuy.class);
    private static final SimpleDateFormat sdfNoHour = new SimpleDateFormat("yyyy-MM-dd");
    private static GroupBuyDao gbDao = new GroupBuyDaoImpl();

    public static void main(String[] args) {
        {
            if (gbDao == null) {
                gbDao = new GroupBuyDaoImpl();
            }
            //1.获取昨天团购活动已经到期的活动信息
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(calendar.DATE, -1);

            String yesToday = sdfNoHour.format(calendar.getTime());
            String beginTime = yesToday + " 00:00:00";
            String endTime = yesToday + " 23:59:59";

            System.out.println("yesToday:" + yesToday + ",GroupBuyJob begin...");
            LOG.info("yesToday:" + yesToday + ",GroupBuyJob begin...");

            List<GroupBuyManageBean> infos = gbDao.getIsEndGroupBuyInfos(beginTime, endTime);
            Map<Integer, GroupBuyManageBean> gbMap = new HashMap<Integer, GroupBuyManageBean>();
            if (infos.isEmpty()) {
                System.err.println("yesToday:" + yesToday + " no end group buy info---");
                LOG.info("yesToday:" + yesToday + " no end group buy info---");
            } else {
                String ids = "";
                for (GroupBuyManageBean gbMn : infos) {
                    if (gbMn.getIsOn() == 1) {
                        ids += "," + gbMn.getId();
                        gbMap.put(gbMn.getId(), gbMn);
                    }
                }
                if (ids.length() < 3) {
                    System.err.println("昨日无已经开启的团购活动数据");
                } else {
                    //2.查询符合条件的客户和下单时商品的金额
                    List<OrderDetailsBean> orderDetailsList = gbDao.queryOrderDetailsBuyGroupBuyIds(ids.substring(1));
                    if (orderDetailsList.isEmpty()) {
                        System.err.println("yesToday:" + yesToday + ",ids:" + ids.substring(1) + ";活动结束后无下单商品");
                    } else {

                        //2.1分组标准 先按照团购分组，再按照客户分组，进行每一个团购进行计算折扣金额
                        Map<Integer, List<OrderDetailsBean>> gbIdOdMap = new HashMap<Integer, List<OrderDetailsBean>>();
                        for (OrderDetailsBean odBean : orderDetailsList) {
                            if (gbIdOdMap.containsKey(odBean.getGroupBuyId())) {
                                gbIdOdMap.get(odBean.getGroupBuyId()).add(odBean);
                            } else {
                                List<OrderDetailsBean> tempList = new ArrayList<OrderDetailsBean>();
                                tempList.add(odBean);
                                gbIdOdMap.put(odBean.getGroupBuyId(), tempList);
                            }
                        }

                        List<UserCouponBean> couponList = new ArrayList<UserCouponBean>();
                        //循环遍历数据，每个团购ID进行折扣金额计算
                        Map<Integer, Double> userCpMap = new HashMap<Integer, Double>();//总金额累计
                        for (int gbId : gbIdOdMap.keySet()) {
                            /**
                             * 计算逻辑：
                             * 当人数在“起始虚拟人数”到“最终价格要求人数”之间时，每增加一个人，价格下降=10*最终价/(虚拟人数-1)
                             * 在团购结束后，所有参加的人员都获得返现，返现额 =该客户购买数量 *(该客户购买时价格-最终价格)
                             */
                            GroupBuyManageBean curGb = gbMap.get(gbId);
                            double curPrice = 0;
                            curPrice = curGb.getFinalPrice() * 10.0d / (curGb.getInitVirtualNum() + 1);

                            List<OrderDetailsBean> curOdList = gbIdOdMap.get(gbId);
                            for (OrderDetailsBean odb : curOdList) {
                                UserCouponBean ucBean = new UserCouponBean();
                                ucBean.setUserId(odb.getUserid());
                                ucBean.setCouponValue(BigDecimalUtil.truncateDouble(odb.getYourorder() * (Double.valueOf(odb.getGoodsprice()) - curPrice),2));
                                ucBean.setGoodsPid(odb.getGoods_pid());
                                ucBean.setOrderNo(odb.getOrderid());
                                ucBean.setGoodsId(odb.getGoodsid());
                                ucBean.setSourceFlag(0);
                                ucBean.setType(1);
                                couponList.add(ucBean);
                                //3.计算客户可得的折扣金额
                                if (userCpMap.containsKey(odb.getUserid())) {
                                    double tempVal = userCpMap.get(odb.getUserid()) + ucBean.getCouponValue();
                                    userCpMap.put(odb.getUserid(), tempVal);
                                } else {
                                    userCpMap.put(odb.getUserid(), ucBean.getCouponValue());
                                }
                            }

                        }
                        //4.折扣金额作为优惠券返还给客户，更新和插入数据
                        boolean isSuccess = gbDao.insertAndUpdateUserCoupon(couponList, userCpMap, ids.substring(1));
                        if (!isSuccess) {
                            gbDao.insertAndUpdateUserCoupon(couponList, userCpMap, ids.substring(1));
                        }
                        couponList.clear();
                        userCpMap.clear();
                        gbIdOdMap.clear();
                        orderDetailsList.clear();
                        if (isSuccess) {
                            System.err.println("yesToday:" + yesToday + ",ids:" + ids.substring(1) + ";活动结束,更新和插入数据成功！！");
                        } else {
                            System.err.println("yesToday:" + yesToday + ",ids:" + ids.substring(1) + ",活动结束，更新和插入数据失败:<:<");
                            LOG.info("yesToday:" + yesToday + ",ids:" + ids.substring(1) + ",活动结束，更新和插入数据失败:<:<");
                        }
                    }
                }
            }
            System.err.println("yesToday:" + yesToday + ",GroupBuyJob end...");
            LOG.info("yesToday:" + yesToday + ",GroupBuyJob end...");
        }
    }
}
