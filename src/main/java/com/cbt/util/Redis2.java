//package com.cbt.util;
//
//import redis.clients.jedis.JedisPoolConfig;
//import redis.clients.jedis.JedisShardInfo;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPool;
//import redis.clients.util.Hashing;
//import redis.clients.util.Sharded;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 	Redis   负载均衡
// * @author Administrator
// *
// */
//public class Redis2 {
//
//	private static ShardedJedisPool pool = null;
//
//	/**
//	 * 构建redis连接池
//	 *
//	 * @param ip
//	 * @param port
//	 * @return JedisPool
//	 */
//	public static ShardedJedisPool getPool() {
//		if (pool == null) {
//			JedisPoolConfig config = new JedisPoolConfig();
//			// 最大连接数
//			config.setMaxTotal(500);
//			// 最大空闲数
//			config.setMaxIdle(30);
//			// 最大等待毫秒数
//			config.setMaxWaitMillis(1000 * 100);
//			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
//			config.setTestOnBorrow(true);
//
//			String hostA = "192.168.1.205";
//			int portA = 6379;
//			 String hostB = "192.168.1.140";
//			 int portB = 6379;
//			List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>(2);
//			JedisShardInfo infoA = new JedisShardInfo(hostA, portA);
//			 JedisShardInfo infoB = new JedisShardInfo(hostB, portB);
////			 infoA.setPassword("redis.360buy");
//			jdsInfoList.add(infoA);
//			 jdsInfoList.add(infoB);
//			pool = new ShardedJedisPool(config, jdsInfoList,
//					Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
//		}
//		return pool;
//	}
//
//	/**
//	 * 返还到连接池
//	 *
//	 * @param pool
//	 * @param rediss
//	 */
//	@SuppressWarnings("deprecation")
//	public static void returnResource(ShardedJedisPool pool, ShardedJedis redis) {
//		if (redis != null) {
//			pool.returnResource(redis);
//		}
//	}
//
//	/**
//	 * 获取redis哈希内容
//	 *
//	 * @param key
//	 * @param field
//	 * @param value
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public static String hget(String key, String field) {
//		String value = null;
//
//		ShardedJedisPool pool = null;
//		ShardedJedis jedis = null;
//		try {
//			pool = getPool();
//			jedis = pool.getResource();
//			value = jedis.hget(key, field);
//		} catch (Exception e) {
//			// 释放redis对象
//			pool.returnBrokenResource(jedis);
//			e.printStackTrace();
//		} finally {
//			// 返还到连接池
//			returnResource(pool, jedis);
//		}
//
//		return value;
//	}
//
//	/**
//	 * 设置redis哈希内容, 对哈希进行hset不会修改已经设置的key过期时间
//	 *
//	 * @param key
//	 * @param field
//	 * @param value
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public static Long hset(String key, String field, String value) {
//		Long res = 0L;
//
//		ShardedJedisPool pool = null;
//		ShardedJedis jedis = null;
//		try {
//			pool = getPool();
//			jedis = pool.getResource();
//			// 设置内容
//			res = jedis.hset(key, field, value);
//			// 设置过期时间
//			// jedis.expire("testhash", 500);
//		} catch (Exception e) {
//			// 释放redis对象
//			pool.returnBrokenResource(jedis);
//			e.printStackTrace();
//		} finally {
//			// 返还到连接池
//			returnResource(pool, jedis);
//		}
//
//		return res;
//	}
//
//	/**
//	 * 删除redis哈希内容
//	 *
//	 * @param key
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public static Long hdel(String key) {
//		Long res = 0L;
//
//		ShardedJedisPool pool = null;
//		ShardedJedis jedis = null;
//		try {
//			pool = getPool();
//			jedis = pool.getResource();
//			res = jedis.del(key);
//		} catch (Exception e) {
//			// 释放redis对象
//			pool.returnBrokenResource(jedis);
//			e.printStackTrace();
//		} finally {
//			// 返还到连接池
//			returnResource(pool, jedis);
//		}
//		return res;
//	}
//
//	@SuppressWarnings("deprecation")
//	public static String set(String key, String value) {
//		String res = null;
//
//		ShardedJedisPool pool = null;
//		ShardedJedis jedis = null;
//		try {
//			pool = getPool();
//			jedis = pool.getResource();
//			res = jedis.set(key, value);
//		} catch (Exception e) {
//			// 释放redis对象
//			pool.returnBrokenResource(jedis);
//			e.printStackTrace();
//		} finally {
//			// 返还到连接池
//			returnResource(pool, jedis);
//		}
//		return res;
//	}
//
//
//
//	@SuppressWarnings("deprecation")
//	public static String get(String key) {
//		String res = null;
//
//		ShardedJedisPool pool = null;
//		ShardedJedis jedis = null;
//		try {
//			pool = getPool();
//			jedis = pool.getResource();
//			res = jedis.get(key);
//		} catch (Exception e) {
//			// 释放redis对象
//			pool.returnBrokenResource(jedis);
//			e.printStackTrace();
//		} finally {
//			// 返还到连接池
//			returnResource(pool, jedis);
//		}
//		return res;
//	}
//
//
//}
