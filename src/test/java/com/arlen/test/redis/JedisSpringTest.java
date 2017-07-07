package com.arlen.test.redis;

import javax.annotation.Resource;

import org.junit.Test;

import com.arlen.test.BaseTest;

public class JedisSpringTest extends BaseTest {

	@Resource
	private JedisClient jedisClient;
	
	@Test
	public void testJedis() {
		jedisClient.zadd("phones", 300, "apple");
		jedisClient.zadd("phones", 200, "xiaomi");
		jedisClient.zadd("phones", 320, "huawei");
		
		// 输出[xiaomi, apple, huawei]
		System.out.println(jedisClient.zrange("phones", 0, 100));
	}
}
