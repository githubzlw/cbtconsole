package com.cbt.test;

import com.cbt.util.DateFormatUtil;
import com.cbt.website.bean.PayCheckBean;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OnlineStatistics {
    private static IOrderwsDao orderwsDao = new OrderwsDao();

    public static void main(String[] args) {


        //System.err.println(DateFormatUtil.getWithDay(today.getTime()));
        List<String> rsSt = new ArrayList<>();
        PayCheckBean bean = new PayCheckBean();
        bean.setOrdersum(2);
        for (int i = 0; i < 30; i++) {
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(System.currentTimeMillis());
            today.add(Calendar.DATE, 0 - i);
            String todayStr = DateFormatUtil.getWithDay(today.getTime());
            int registerStatisticsNum = orderwsDao.statisticsMakeOrderNewUser(todayStr + " 00:00:00", todayStr + " 23:59:59", 0);
            //System.err.println(todayStr + "@" + registerStatisticsNum);

            bean.setDataStart(todayStr + " 00:00:00");
            bean.setDataEnd(todayStr + " 23:59:59");
            PaymentDaoImp dao = new PaymentDao();
            int totalPp = dao.queryPaymentStatisticsCount(bean);
            //dao.queryPaymentStatistics(bean);
            rsSt.add(todayStr + "@StatisticsNum:" + registerStatisticsNum + "@paymentNum:" + totalPp);
        }
        for (int k = rsSt.size() - 1; k >= 0; k--) {
            System.err.println(rsSt.get(k));
        }


    }
}
