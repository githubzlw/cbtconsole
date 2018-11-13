package com.cbt.shopcar.service.impl;

import com.cbt.shopcar.dao.GoodsCarMapper;
import com.cbt.shopcar.pojo.GoodCar;
import com.cbt.shopcar.pojo.GoodsCar;
import com.cbt.shopcar.pojo.GoodsCarExample;
import com.cbt.shopcar.service.GoodCarService;
import com.importExpress.pojo.SpiderNewBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


@Service("goodCarService")
public class GoodCarServiceImpl implements GoodCarService {

	
	@Autowired
	private GoodsCarMapper goodsCarMapper;

	@Override
	public List<GoodsCar> selectByExample(GoodsCarExample example) {
		return goodsCarMapper.selectByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return goodsCarMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(GoodsCar record) {
		return goodsCarMapper.insert(record);
	}

	@Override
	public int insertSelective(GoodsCar record) {
		return goodsCarMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKey(GoodsCar record) {
		return goodsCarMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<GoodCar> getGoodCar(String sessionid, Integer userid,
			Integer preshopping) {
		return goodsCarMapper.getGoodCar(sessionid, userid, preshopping);
	}

    @Override
    public int updateGoodsCarPrice(int goodsId, int userId, double goodsPrice) {
        return goodsCarMapper.updateGoodsCarPrice(goodsId,userId,goodsPrice);
    }

	@Override
	public List<SpiderNewBean> querySpiderBeanByUserId(int userId) {
		return goodsCarMapper.querySpiderBeanByUserId(userId);
	}

	@Override
	public int deleteMarketingByUserId(int userId) {
		return goodsCarMapper.deleteMarketingByUserId(userId);
	}

	//修改购物车表数据，更新用户的redis数据
	public int upRedisData(int userid){
		  int res = 0;//0-失败，1-成功，
	        BufferedReader in = null;
	        try {
	            String urlNameString ="https://www.import-express.com/upRedisData/upShopCar?userid="+userid+"&pass=dylhfm";
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader
	            		(
	                    connection.getInputStream()));
	            String line;
	            if ((line = in.readLine()) != null) {
	            	res = Integer.parseInt(line);
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
		return res;
	}
}
