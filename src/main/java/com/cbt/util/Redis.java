package com.cbt.util;

import com.cbt.warehouse.ctrl.HotGoodsCtrl;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * 	Redis 连接池
 * @author Administrator
 *
 */
public class Redis {

	private static JedisPool pool = null;

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(Redis.class);

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	public static JedisPool getPool() {
		if (pool == null) {
			String redisIP = SysParamUtil.getParam("RedisIP");
			String redisPort = SysParamUtil.getParam("RedisPort");
			logger.info("Redis config:{} {}",redisIP,redisPort);
			JedisPoolConfig config = new JedisPoolConfig();
			//最大连接数
			config.setMaxTotal(Integer.parseInt(SysParamUtil.getParam("MaxTotal")));
			//最大空闲数
			config.setMaxIdle(Integer.parseInt(SysParamUtil.getParam("MaxIdle")));
			//最大等待毫秒数
			config.setMaxWaitMillis(Long.parseLong(SysParamUtil.getParam("MaxWaitMillis")));
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			// 在连接还回给pool时，是否提前进行validate操作
			config.setTestOnReturn(true);
			//如果一个连接300秒内没有任何的返回Jedis将关闭这个连接
			pool = new JedisPool(config, redisIP, Integer.parseInt(redisPort),Integer.parseInt(SysParamUtil.getParam("PoolTimeout")),SysParamUtil.getParam("RedisAuthPass"));
		}
		return pool;
	}
	
	
	
	/**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
    @SuppressWarnings("deprecation")
	public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
    
    
    /**
	  * 获取redis哈希内容
	  * @param key
	  * @param field
	  * @param value
	  * @return
	  */
	 @SuppressWarnings("deprecation")
	public static String hget(String key,String field){
	        String value = null;
	        
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            value = jedis.hget(key, field);
	        } catch (Exception e) {
	            //释放redis对象
	            pool.returnBrokenResource(jedis);
	            logger.error("error",e);
	        } finally {
	            //返还到连接池
	            returnResource(pool, jedis);
	        }
	        
	        return value;
	    }
	 
	 /**
	  * 设置redis哈希内容, 对哈希进行hset不会修改已经设置的key过期时间
	  * @param key
	  * @param field
	  * @param value
	  * @return
	  */
	 @SuppressWarnings("deprecation")
	public static Long hset(String key,String field,String value){
	        Long res = 0L;
	        
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            if (jedis.exists(key)) {					
	            	//设置内容
	            	res = jedis.hset(key, field, value);
				}else{
					res = jedis.hset(key, field, value);
					//设置过期时间:(时间单位:秒),2880*60=172800
					jedis.expire(key, Integer.parseInt(SysParamUtil.getParam("ExpiredTime")));
				}
	        } catch (Exception e) {
	            //释放redis对象
				e.printStackTrace();
	            pool.returnBrokenResource(jedis);
	            logger.error("error",e);
	        } finally {
	            //返还到连接池
	            returnResource(pool, jedis);
	        }
	        
	        return res;
	    }
	 
	 /**
	  * 删除redis哈希内容
	  * @param key
	  * @return
	  */
	 @SuppressWarnings("deprecation")
	public static Long hdel(String key){
	        Long res = 0L;
	        
	        JedisPool pool = null;
	        Jedis jedis = null;
	        try {
	            pool = getPool();
	            jedis = pool.getResource();
	            res = jedis.del(key);
	        } catch (Exception e) {
	            //释放redis对象
	            pool.returnBrokenResource(jedis);
	            logger.error("error",e);
	        } finally {
	            //返还到连接池
	            returnResource(pool, jedis);
	        }
	        return res;
	    }
	 
	 	/**
	 	 *  根据key获取redis的内容
	 	 * @param key
	 	 * @return
	 	 */
		@SuppressWarnings("deprecation")
		public static String get(String key) {
			String res = null;

			JedisPool pool = null;
	        Jedis jedis = null;
			try {
				pool = getPool();
				jedis = pool.getResource();
				res = jedis.get(key);
			} catch (Exception e) {
				// 释放redis对象
				pool.returnBrokenResource(jedis);
				logger.error("error",e);
			} finally {
				// 返还到连接池
				returnResource(pool, jedis);
			}
			return res;
		}
		
		/**
		 * 判断key是否存在
		 * @param key
		 * @return
		 */
		@SuppressWarnings("deprecation")
		public static Boolean exists(String key) {
			Boolean res = null;

			JedisPool pool = null;
	        Jedis jedis = null;
			try {
				pool = getPool();
				jedis = pool.getResource();
				res = jedis.exists(key);
			} catch (Exception e) {
				// 释放redis对象
				pool.returnBrokenResource(jedis);
				logger.error("error",e);
			} finally {
				// 返还到连接池
				returnResource(pool, jedis);
			}
			return res;
		}
}
