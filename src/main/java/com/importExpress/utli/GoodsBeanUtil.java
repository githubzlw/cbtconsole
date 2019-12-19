package com.importExpress.utli;

import com.cbt.bean.CustomGoodsQuery;
import com.cbt.parse.service.StrUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress
 * @date:2019/11/22
 */
public class GoodsBeanUtil {


    public static CustomGoodsQuery genQueryBean(HttpServletRequest request) {
        CustomGoodsQuery queryBean = new CustomGoodsQuery();

        String strPage = request.getParameter("page");
        if (strPage == null || strPage.isEmpty() || !StrUtils.isMatch(strPage, "(\\d+)")) {
            strPage = "1";
        }
        queryBean.setCurrPage(Integer.valueOf(strPage));
        queryBean.setPage((queryBean.getCurrPage() - 1) * 50);
        queryBean.setLimitNum(50);

        String catid = request.getParameter("catid");
        if (StringUtils.isNotBlank(catid) && !"0".equals(catid)) {
            queryBean.setCatid(catid);
        }

        String sttime = request.getParameter("sttime");
        if (StringUtils.isNotBlank(sttime)) {
            queryBean.setSttime(sttime);
        }

        String valid = request.getParameter("valid");
        if (StringUtils.isNotBlank(valid)) {
            queryBean.setValid(Integer.parseInt(valid));
        } else {
            queryBean.setValid(-1);
        }

        String unsellableReason = request.getParameter("unsellableReason");
        if (StringUtils.isBlank(unsellableReason) || "-1".equals(unsellableReason)) {
            unsellableReason = null;
        }
        queryBean.setUnsellableReason(unsellableReason);
        String edtime = request.getParameter("edtime");
        if (StringUtils.isNotBlank(edtime)) {
            queryBean.setEdtime(edtime);
        }

        String strState = request.getParameter("state");
        if (strState == null || "".equals(strState)) {
            strState = "0";
        }
        queryBean.setState(Integer.valueOf(strState));

        String adminid = request.getParameter("adminid");
        if (adminid == null || adminid.isEmpty()) {
            adminid = "0";
        }
        queryBean.setAdminId(Integer.valueOf(adminid));

        String isEdited = request.getParameter("isEdited");
        if (StringUtils.isNotBlank(isEdited)) {
            queryBean.setIsEdited(Integer.valueOf(isEdited));
        }


        String isAbnormal = request.getParameter("isAbnormal");
        if (StringUtils.isNotBlank(isAbnormal)) {
            queryBean.setIsAbnormal(Integer.valueOf(isAbnormal));
        }

        String isBenchmark = request.getParameter("isBenchmark");// 对标参数0全部，1对标，2非对标
        if (isBenchmark == null || "".equals(isBenchmark)) {
            isBenchmark = "-1";
        }
        queryBean.setIsBenchmark(Integer.valueOf(isBenchmark));

        /**
         * 重量检查组合方式( 0 2 3 4 5 2*5 3*5); 0不是异常;2对于重量 比 类别平均重量 高30% 而且 运费占 总价格 占比超 35%的 ;
         * 3如果重量 比 类别平均重量低40%，请人为检查; 4重量数据为空的; 5对于所有的 运费占总免邮价格 60%以上的
         */
        String weightCheck = request.getParameter("weightCheck");
        if (weightCheck == null || "".equals(weightCheck)) {
            weightCheck = "-1";
        }
        queryBean.setWeightCheck(Integer.valueOf(weightCheck));

        String bmFlag = request.getParameter("bmFlag");
        if (bmFlag == null || "".equals(bmFlag)) {
            bmFlag = "0";
        }
        queryBean.setBmFlag(Integer.valueOf(bmFlag));

        String sourceProFlag = request.getParameter("sourceProFlag");
        if (sourceProFlag == null || "".equals(sourceProFlag)) {
            sourceProFlag = "0";
        }
        queryBean.setSourceProFlag(Integer.valueOf(sourceProFlag));

        String soldFlag = request.getParameter("soldFlag");
        if (StringUtils.isBlank(soldFlag)) {
            soldFlag = "-1";
        }
        queryBean.setSoldFlag(Integer.valueOf(soldFlag));

        String priorityFlag = request.getParameter("priorityFlag");
        if (priorityFlag == null || "".equals(priorityFlag)) {
            priorityFlag = "0";
        }
        queryBean.setPriorityFlag(Integer.valueOf(priorityFlag));

        String addCarFlag = request.getParameter("addCarFlag");
        if (addCarFlag == null || "".equals(addCarFlag)) {
            addCarFlag = "0";
        }
        queryBean.setAddCarFlag(Integer.valueOf(addCarFlag));

        String sourceUsedFlag = request.getParameter("sourceUsedFlag");
        if (sourceUsedFlag == null || "".equals(sourceUsedFlag)) {
            sourceUsedFlag = "-1";
        }
        queryBean.setSourceUsedFlag(Integer.valueOf(sourceUsedFlag));

        String ocrMatchFlag = request.getParameter("ocrMatchFlag");
        if (ocrMatchFlag == null || "".equals(ocrMatchFlag)) {
            ocrMatchFlag = "0";
        }
        queryBean.setOcrMatchFlag(Integer.valueOf(ocrMatchFlag));

        String infringingFlag = request.getParameter("infringingFlag");
        if (org.apache.commons.lang3.StringUtils.isBlank(infringingFlag)) {
            infringingFlag = "-1";
        }
        queryBean.setInfringingFlag(Integer.valueOf(infringingFlag));


        String aliWeightBegin = request.getParameter("aliWeightBegin");
        if (StringUtils.isNotBlank(aliWeightBegin) && !"0".equals(aliWeightBegin)) {
            queryBean.setAliWeightBegin(Double.valueOf(aliWeightBegin));
        }

        String aliWeightEnd = request.getParameter("aliWeightEnd");
        if (StringUtils.isNotBlank(aliWeightEnd) && !"0".equals(aliWeightEnd)) {
            queryBean.setAliWeightEnd(Double.valueOf(aliWeightEnd));
        }

        String onlineTime = request.getParameter("onlineTime");
        if (StringUtils.isNotBlank(onlineTime)) {
            queryBean.setOnlineTime(onlineTime);
        }

        String offlineTime = request.getParameter("offlineTime");
        if (StringUtils.isNotBlank(offlineTime)) {
            queryBean.setOfflineTime(offlineTime);
        }

        String editBeginTime = request.getParameter("editBeginTime");
        if (StringUtils.isNotBlank(editBeginTime)) {
            queryBean.setEditBeginTime(editBeginTime);
        }

        String editEndTime = request.getParameter("editEndTime");
        if (StringUtils.isNotBlank(editEndTime)) {
            queryBean.setEditEndTime(editEndTime);
        }

        String weight1688Begin = request.getParameter("weight1688Begin");
        if (StringUtils.isNotBlank(weight1688Begin) && !"0".equals(weight1688Begin)) {
            queryBean.setWeight1688Begin(Double.valueOf(weight1688Begin));
        }

        String weight1688End = request.getParameter("weight1688End");
        if (StringUtils.isNotBlank(weight1688End) && !"0".equals(weight1688End)) {
            queryBean.setWeight1688End(Double.valueOf(weight1688End));
        }

        String price1688Begin = request.getParameter("price1688Begin");
        if (StringUtils.isNotBlank(price1688Begin) && !"0".equals(price1688Begin)) {
            queryBean.setPrice1688Begin(Double.valueOf(price1688Begin));
        }

        String price1688End = request.getParameter("price1688End");
        if (StringUtils.isNotBlank(price1688End) && !"0".equals(price1688End)) {
            queryBean.setPrice1688End(Double.valueOf(price1688End));
        }

        String isSort = request.getParameter("isSort");
        if (StringUtils.isNotBlank(isSort)) {
            queryBean.setIsSort(Integer.valueOf(isSort));
        }

        String isComplain = request.getParameter("isComplain");
        if (StringUtils.isNotBlank(isComplain) && StringUtils.equals(isComplain, "1")) {
            queryBean.setIsComplain(Integer.valueOf(isComplain));
        }

        String fromFlag = request.getParameter("fromFlag");
        if (StringUtils.isNotBlank(fromFlag)) {
            queryBean.setFromFlag(Integer.valueOf(fromFlag));
        } else {
            queryBean.setFromFlag(-1);
        }


        String finalWeightBegin = request.getParameter("finalWeightBegin");
        if (StringUtils.isNotBlank(finalWeightBegin) && !"0".equals(finalWeightBegin)) {
            queryBean.setFinalWeightBegin(Double.valueOf(finalWeightBegin));
        }
        String finalWeightEnd = request.getParameter("finalWeightEnd");
        if (StringUtils.isNotBlank(finalWeightEnd) && !"0".equals(finalWeightEnd)) {
            queryBean.setFinalWeightEnd(Double.valueOf(finalWeightEnd));
        }
        String minPrice = request.getParameter("minPrice");
        if (StringUtils.isNotBlank(minPrice) && !"0".equals(minPrice)) {
            queryBean.setMinPrice(Double.valueOf(minPrice));
        }
        String maxPrice = request.getParameter("maxPrice");
        if (StringUtils.isNotBlank(maxPrice) && !"0".equals(maxPrice)) {
            queryBean.setMaxPrice(Double.valueOf(maxPrice));
        }

        String isSoldFlag = request.getParameter("isSoldFlag");
        if (StringUtils.isNotBlank(isSoldFlag)) {
            queryBean.setIsSoldFlag(Integer.valueOf(isSoldFlag));
        }

        String isWeigthZero = request.getParameter("isWeigthZero");
        if (StringUtils.isNotBlank(isWeigthZero)) {
            queryBean.setIsWeigthZero(Integer.valueOf(isWeigthZero));
        }

        String isWeigthCatid = request.getParameter("isWeigthCatid");
        if (StringUtils.isNotBlank(isWeigthCatid)) {
            queryBean.setIsWeigthCatid(Integer.valueOf(isWeigthCatid));
        }

		/*String qrCatid = request.getParameter("qrCatid");
		if(StringUtils.isNotBlank(qrCatid)){
			queryBean.setQrCatid(qrCatid);
		}*/
        String shopId = request.getParameter("shopId");
        if (StringUtils.isNotBlank(shopId)) {
            queryBean.setShopId(shopId);
        }
        String chKeyWord = request.getParameter("chKeyWord");
        if (StringUtils.isNotBlank(chKeyWord)) {
            queryBean.setChKeyWord(chKeyWord);
        }

        return queryBean;
    }
}
