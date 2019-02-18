package com.importExpress.service.impl;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.cbt.website.util.JsonResult;
import com.importExpress.mapper.QueryUserMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.QueryUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QueryUserServiceImpl implements QueryUserService {

	@Autowired
	private QueryUserMapper queryUserMapper;
	
	@Override
	public List<String> queryUser() {
		List<String> userIdList = new ArrayList<String>(); 
		List<String> resultList = new ArrayList<String>(); 
		//在线上查询
		DataSourceSelector.set("dataSource127hop"); 
		//查询购物车中有数据用户
		List<String> carGoodsList = queryUserMapper.queryCarGoods();
		DataSourceSelector.restore();
		
		//本地查询
		// dropship用户 有订单 且购物车有数据
		List<String> dpList = queryUserMapper.queryDropShipUserId();
		userIdList.addAll(getRandomList(removeAllNoList(carGoodsList, dpList, null, null), 1));
		
		List<String> randomtList = queryUserMapper.queryRandomUser(null, null, true);
		List<String> randomfList = queryUserMapper.queryRandomUser(null, null, false);
		
		//时间范围内用户
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //一周到两周
        calendar.add(Calendar.DATE, -7);
        String endDate = sdf.format(calendar.getTime()).toString() + " 23:59";
        calendar.add(Calendar.DATE, -7);
        String startDate = sdf.format(calendar.getTime()).toString() + " 00:00";
		queryRandom(userIdList, endDate, startDate, carGoodsList, randomtList, randomfList);
        //两周到一个月
        endDate = sdf.format(calendar.getTime()).toString() + " 23:59";
        calendar.add(Calendar.DATE, -16);
        startDate = sdf.format(calendar.getTime()).toString() + " 00:00";
        queryRandom(userIdList, endDate, startDate, carGoodsList, randomtList, randomfList);
        //一个月到三个月
        endDate = sdf.format(calendar.getTime()).toString() + " 23:59";
        calendar.add(Calendar.DATE, -60);
        startDate = sdf.format(calendar.getTime()).toString() + " 00:00";
        queryRandom(userIdList, endDate, startDate, carGoodsList, randomtList, randomfList);
        //三个月到半年
        endDate = sdf.format(calendar.getTime()).toString() + " 23:59";
        calendar.add(Calendar.DATE, -90);
        startDate = sdf.format(calendar.getTime()).toString() + " 00:00";
        queryRandom(userIdList, endDate, startDate, carGoodsList, randomtList, randomfList);
        //半年之前
        endDate = sdf.format(calendar.getTime()).toString() + " 23:59";
        queryRandom(userIdList, endDate, null, carGoodsList, randomtList, randomfList);
        //查询用户对于登陆信息
        if (null == userIdList || userIdList.size() < 1) {
			return resultList;
		}
        List<Map<String, Object>> userList = queryUserMapper.queryUserName(userIdList);
        Map<String, String> newUserMap = new HashMap<String, String>();
        if (null != userList && userList.size() > 0) {
			for (Map<String, Object> bean : userList) {
				String id = bean.get("id").toString();
				newUserMap.put(id, bean.get("name").toString());
			}
		}
        Map<String, String> timeMap = new HashMap<String, String>();
        for (String bean : carGoodsList) {
			String[] splArr = bean.split("@");
			if (null != splArr && splArr.length == 2) {
				timeMap.put(splArr[0], splArr[1].substring(0, 16));
			}
		}
        for (String id : userIdList) {
        	resultList.add("https://www.import-express.com/simulateLogin/login?userName=" + id 
        			+ "&password=" + newUserMap.get(id) + "&currency=USD"
        			+ "&time=" + timeMap.get(id));
		}
		return resultList;
	}
	
	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	/**
	 * 移除 dpList集合中 不在 carGoodsList集合中的数据
	 **/
	private List<String> removeAllNoList(List<String> carGoodsList, List<String> dpList, String endDate, String startDate) {
		//去除不在范围的内的userid
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		try {
			Date startD = null!=startDate?DF.parse(startDate):null;
			Date endD = null!=endDate?DF.parse(endDate):null;
			for (String bean : carGoodsList) {
				String[] splArr = bean.split("@");
				if (null != splArr && splArr.length == 2) {
					Date carD = DF.parse(splArr[1].substring(0, 16));
					if (null != startD && startD.getTime() > carD.getTime()) {
						continue;
					}
					if (null != endD && endD.getTime() < carD.getTime()) {
						continue;
					}
					list.add(splArr[0]);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//dpList 内满足的用户
		Iterator<String> iterator = dpList.iterator();
		while (iterator.hasNext()) {
			String str = (String) iterator.next();
			if (list.contains(str)) {
				result.add(str);
			}
		}
		return result;
	}
	
	@Override
	public List<String> queryUserByPrice(Integer price) {
		// 27
		List<String> priceList = queryUserMapper.queryUserByPrice(price);
		List<String> userIdList = getRandomList(priceList, 100);
		return userIdList;
	}

	private void queryRandom(List<String> userIdList, String endDate, String startDate, List<String> carGoodsList, List<String> randomtList, List<String> randomfList) {
		List<String> tList = removeAllNoList(carGoodsList, randomtList, endDate, startDate);
		List<String> fList = removeAllNoList(carGoodsList, randomfList, endDate, startDate);
        
		tList.removeAll(userIdList);
        List<String> newtList = getRandomList(tList, 3);
        userIdList.addAll(newtList);
        fList.removeAll(userIdList);
        userIdList.addAll(getRandomList(fList, 5 - newtList.size()));
	}
	
	 /**
     * @function:从数组中随机抽取若干不重复元素
     * 
     * @param list:被抽取集合
     * @param count:抽取元素的个数
     * @return:由抽取元素组成的新集合
     */
    public static List<String> getRandomList(List<String> list,int count){
        if(null == list || list.size() < 1){
            return list;
        }
        //list 去重复
        Set<String> set = new HashSet<String>(list);
        list = new ArrayList<>(set);
        if(null == list || list.size() <= count){
            return list;
        }
        List<String> newList = new ArrayList<String>();
        Random random= new Random();
        int temp=0;//接收产生的随机数
        for(int i=1;i<=count;){
            temp=random.nextInt(list.size());//将产生的随机数作为被抽数组的索引
            if(!(newList.contains(list.get(temp)))){
                newList.add(list.get(temp));
                i++;
            }
        }
        return newList;
    }

	@Override
	public Map<String, Object> queryMarkedList(Integer rows, String orderby) {
		Map<String, Object> result = new HashMap<String, Object>();
		//查询未处理数据
		List<GoodsInfoSpiderPO> list = queryUserMapper.queryMarkedList(rows, orderby);
		if (null != list && list.size() > 0) {
			result.put("date", list);
		}
		//查询剩余条数
		Long total = queryUserMapper.queryNoMarkedTotal();
		result.put("total", total);
		return result;
	}

	@Override
	public void updateMarkedById(Long id, Integer marked) {
		queryUserMapper.updateMarkedById(id, marked);
	}

	@Override
	public Map<String, Object> createStandardGoodsForm() {
		queryUserMapper.createStandardGoodsForm();
		return queryStandardGoodsFormCreatetime();
	}
	
	@Override
	public String createStaticizeForm(Integer flag) {
		if (null != flag && flag == 1) {
			//线上
			DataSourceSelector.set("dataSource127hop"); 
			List<ItemStaticFile> list = queryUserMapper.queryStaticizeData();
			DataSourceSelector.restore();
			if (null != list && list.size() > 0) {
				//27
				queryUserMapper.insertStaticizeData(list);
			}
		}
		return queryUserMapper.queryStaticizeTime();
	}

	@Override
	public Map<String, Object> queryStandardGoodsFormCreatetime() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createtime", queryUserMapper.queryCreatetime());
		map.put("total0", queryUserMapper.queryStandardGoodsFormCount(null, null, null));
		map.put("total1", queryUserMapper.queryStandardGoodsFormCount(1, null, null));
		map.put("total2", queryUserMapper.queryStandardGoodsFormCount(2, null, null));
		map.put("total3", queryUserMapper.queryNoStandardGoodsFormCount());
		
		map.put("total10", queryUserMapper.queryStandardGoodsFormCount(null, 1, 1));
		map.put("total11", queryUserMapper.queryStandardGoodsFormCount(1, 1, 1));
		map.put("total12", queryUserMapper.queryStandardGoodsFormCount(2, 1, 1));
		
		map.put("total20", queryUserMapper.queryStandardGoodsFormCount(null, 1, 2));
		map.put("total21", queryUserMapper.queryStandardGoodsFormCount(1, 1, 2));
		map.put("total22", queryUserMapper.queryStandardGoodsFormCount(2, 1, 2));
		
		map.put("total30", queryUserMapper.queryStandardGoodsFormCount(null, 2, 1));
		map.put("total31", queryUserMapper.queryStandardGoodsFormCount(1, 2, 1));
		map.put("total32", queryUserMapper.queryStandardGoodsFormCount(2, 2, 1));
		
		map.put("total40", queryUserMapper.queryStandardGoodsFormCount(null, 2, 2));
		map.put("total41", queryUserMapper.queryStandardGoodsFormCount(1, 2, 2));
		map.put("total42", queryUserMapper.queryStandardGoodsFormCount(2, 2, 2));
		
		return map;
	}

	@Override
	public Map<String, Object> queryStandardGoodsFormList(Integer page, Integer rows, Integer flag, Integer bmFlag,Integer valid) {
		List<StandardGoodsFormDataPO> list = queryUserMapper.queryStandardGoodsFormList((page - 1) * rows,rows, flag, bmFlag, valid);
		Long totalCount = queryUserMapper.queryStandardGoodsFormCount(flag, bmFlag, valid);
		
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("recordList", list);
        map.put("totalCount", totalCount);
        return map;
	}

	@Override
	public AuthInfo queryAuthInfo(Integer authId) {
		return queryUserMapper.queryAuthInfo(authId);
	}

    @Override
    public String queryAvailable(String email) {
        DataSourceSelector.set("dataSource127hop");
        String available = queryUserMapper.queryAvailable(email);
        DataSourceSelector.restore();
        return available;
    }

    @Override
    public long updateAvailable(String email, Double available) {
        DataSourceSelector.set("dataSource127hop");
        long count = queryUserMapper.updateAvailable(email, available);
        DataSourceSelector.restore();
        return count;
    }

    @Override
    public JsonResult resetPwd(String admName, String oldPwd, String newPwd) {
        JsonResult json = new JsonResult();
        //旧密码校验
        long count = queryUserMapper.queryAdmUserByAdmNameAndPwd(admName, oldPwd);
        if (count == 0) {
            json.setOk(false);
            json.setMessage("旧密码输入错误!");
            return json;
        }
        //新密码更新
        queryUserMapper.updatePasswordByAdmName(admName, newPwd);
        json.setOk(true);
        json.setMessage("密码修改成功,下次登录请使用新密码!");
        return json;
    }

    public long updateAuthInfo(AuthInfo authInfo, String url, String urlFlag, String colorFlag) {
        String urlParam = "";
        //入口显示方式
        if ("2".equals(urlFlag)){
            urlParam += "urlFlag=a";
        }
        //入口颜色
        if (StringUtils.isNotBlank(colorFlag)){
            if (StringUtils.isBlank(urlParam)){
                urlParam += "colorFlag=" + colorFlag.replace("#", "");
            } else {
                urlParam += "&colorFlag=" + colorFlag.replace("#", "");
            }
        }
        //拼接
        if (StringUtils.isNotBlank(urlParam)){
            if (url.indexOf("?") > 0){
                url += "&" + urlParam;
            } else {
                url += "?" + urlParam;
            }
        }
        authInfo.setUrl(url);
        if (0 == authInfo.getAuthId()){
            Integer maxOrderNo = queryUserMapper.queryOrderNoByModuleType(authInfo.getModuleType());
            authInfo.setOrderNo((maxOrderNo == null?0:maxOrderNo) + 1);
            return queryUserMapper.insertAuthInfo(authInfo);
        }
        return queryUserMapper.updateAuthInfo(authInfo);
    }

    @Override
    public void updateNeedOffShelfData() {
        //时间范围内用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //一周到两周
        calendar.add(Calendar.DATE, -360);
        String startDate = sdf.format(calendar.getTime()).toString() + " 00:00";

        //查询已卖过的27 type=1
        List<String> pidList = queryUserMapper.queryBoughtGoods(startDate);
        //查询加过购物车的27 type=2
        List<String> pidList2 = queryUserMapper.queryCarProducts(startDate);
        //查询人为编辑过的27 type=4
        List<String> pidList4 = queryUserMapper.queryIsEditProducts();
        //查询有库存的27 type=5
        List<String> pidList5 = queryUserMapper.queryInventoryProducts();
        //保存到28
        DataSourceSelector.set("dataSource28hop");
        //清空原表中数据
        queryUserMapper.deleteNeedoffshelfSoldAll();
        insertNeedoffshelfSoldPid(pidList, 1);
        insertNeedoffshelfSoldPid(pidList2, 2);
        insertNeedoffshelfSoldPid(pidList4, 4);
        insertNeedoffshelfSoldPid(pidList5, 5);
        DataSourceSelector.restore();
    }

    public void insertNeedoffshelfSoldPid(List<String> list, Integer type){
        if (list != null && list.size() > 0) {
            queryUserMapper.insertNeedoffshelfSoldPid(list, type);
        }
    }

    @Override
    public List<String> queryGoodsWeightNoSyn() {
        return queryUserMapper.queryGoodsWeightNoSyn();
    }

    @Override
    public EasyUiJsonResult queryUserList(Integer page, Integer rows, Integer userType) {
        List<UserBean> list = queryUserMapper.queryUserList((page - 1) * rows, rows, userType);
        Integer totalCount = queryUserMapper.queryUserListCount(userType);

        EasyUiJsonResult json = new EasyUiJsonResult();
        if (list != null && list.size() > 0) {
            for (UserBean bean : list) {
                bean.setType(userType.toString());
            }
            json.setSuccess(true);
            json.setRows(list);
            json.setTotal(totalCount);
        } else {
            json.setSuccess(false);
            json.setRows("");
            json.setTotal(0);

        }
        return json;
    }

    @Override
    public Map<String, Object> queryUserOtherInfo(Integer id, Integer userType) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userType", userType);
        /*A 所有 录入了地址 但没下单的客户
        B 所有 注册了，而且有Wechat 号 但没下单的客户
        */
        if (userType == 1){
            result.put("address", queryUserMapper.queryUserAddressById(id));
        } else if (userType == 2){
            result.put("userEx", queryUserMapper.queryUserExById(id));
        }
        //每个客户显示 尽量多个内容， 比如 搜索词，比如 查看的 产品页数量。 多次打开的 产品页是哪个，当前购物车中 产品数量， 是否已经被“购物车营销跟进过”
        //当前客户购物车产品数量
        String shopCarShowinfo = queryUserMapper.queryGoodsCarCount(id);
        if (StringUtils.isNotBlank(shopCarShowinfo)){
            result.put("goodsCarCount", shopCarShowinfo.split("guId").length - 1);
        } else {
            result.put("goodsCarCount", 0);
        }
        return result;
    }
}
