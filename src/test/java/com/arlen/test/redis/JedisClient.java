package com.arlen.test.redis;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

@Component
public class JedisClient {

	private final static Logger logger = LoggerFactory.getLogger(JedisClient.class);
	
	/**
	 * jedisPool/sentinelPool/shardedPool
	 */
	@Resource(name = "sentinelPool")
	private Pool<Jedis> pool;
	
	// 或者直接在Spring配置工厂bean，无需每次手动调用
	private final Jedis getJedis() {
		try {
			return pool.getResource();
		} catch (Exception e) {
			logger.error("Get jedis failed, fatal error: ", e);
			throw new JedisException(e);
		}
	}
	
	// 或者用动态代理，将jedis作为最后触发方法的参数，代理内做连接的获取与释放
	public boolean zadd(final String key, final double score, final String member) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Long result = jedis.zadd(key, score, member);
			return result > 0;
		} catch (Exception e) {
			logger.error("Jedis error: zadd failed.", e);
			return false;
		} finally {
			// 关闭Jedis实例
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public Set<String> zrange(final String key, final long start, final long end) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Set<String> result = jedis.zrange(key, start, end);
			return result;
		} catch (Exception e) {
			logger.error("Jedis error: zrangeByScore failed.", e);
			return new HashSet<String>(0);
		} finally {
			// 关闭Jedis实例
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	// ...
}
