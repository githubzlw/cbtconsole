package com.cbt.util;

import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Redis 连接池
 *
 * @author Administrator
 */
public class Redis {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(Redis.class);
    private static JedisPool pool = null;

    private static Jedis getJedis() {
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            logger.info("get redis connect  active:[{}] idle:[{}] waiter:[{}]",pool.getNumActive(),pool.getNumIdle(),pool.getNumWaiters());

            return jedis;
        } catch (Exception e) {
            logger.error("error", e);
            throw new RuntimeException("redis exception");
        }
    }

    /**
     * 构建redis连接池
     *
     * @return JedisPool
     */
    private synchronized static JedisPool getPool() {
        if (pool == null) {
            logger.info("redis pool init begin");
            String redisIP = SysParamUtil.getParam("RedisIP");
            String redisPort = SysParamUtil.getParam("RedisPort");
	        int intMaxTotal=Integer.parseInt(SysParamUtil.getParam("MaxTotal"));
	        int intMaxIdle=Integer.parseInt(SysParamUtil.getParam("MaxIdle"));
	        long lngMaxWaitMillis=Long.parseLong(SysParamUtil.getParam("MaxWaitMillis"));
	        int intPoolTimeout=Integer.parseInt(SysParamUtil.getParam("PoolTimeout"));
            logger.info("Redis config IP:[{}] PORT:[{}] intMaxTotal:[{}] intMaxIdle:[{}] lngMaxWaitMillis:[{}] intPoolTimeout:[{}]", redisIP, redisPort,intMaxTotal,intMaxIdle,lngMaxWaitMillis,intPoolTimeout);
            JedisPoolConfig config = new JedisPoolConfig();
            //最大连接数
            config.setMaxTotal(intMaxTotal);
            //最大空闲数
            config.setMaxIdle(intMaxIdle);
            config.setMinIdle(intMaxIdle);
            //最大等待毫秒数
            config.setMaxWaitMillis(lngMaxWaitMillis);
            // 在连接还回给pool时，是否提前进行validate操作
            config.setTestOnReturn(true);
            //如果一个连接300秒内没有任何的返回Jedis将关闭这个连接
            pool = new JedisPool(config, redisIP, Integer.parseInt(redisPort), intPoolTimeout, SysParamUtil.getParam("RedisAuthPass"));
            logger.info("redis pool init end");
        }
        return pool;
    }

    /**
     * 设置redis哈希内容, 对哈希进行hset不会修改已经设置的key过期时间
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static long hset(String key, String field, String value) {

        try (Jedis jedis = getJedis()) {
            if (jedis.exists(key)) {
                //设置内容
                return jedis.hset(key, field, value);
            } else {
                Long res = jedis.hset(key, field, value);
                //设置过期时间:(时间单位:秒),2880*60=172800
                jedis.expire(key, Integer.parseInt(SysParamUtil.getParam("ExpiredTime")));
                return res;
            }
        }
    }

    /**
     * 删除redis哈希内容
     *
     * @param key
     * @return
     */
    public static long hdel(String key) {

        try (Jedis jedis = getJedis()) {
            Long res = jedis.del(key);
            if (res == 0L) {
                logger.error("hdel result is 0,key:[{}]", key);
            }
            return res;
        }
    }

	/**
	 * 获取redis哈希内容
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public static String hget(String key, String field) {

		try (Jedis jedis = getJedis()) {
			return jedis.hget(key, field);
		}
	}
}
