package com.arlen.test.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

public class JedisTest {
	private final static JedisPool pool;
	private final static JedisSentinelPool sentinelPool;
	private final static ShardedJedisPool shardedPool;
	static {
		// JedisPool
		pool = new JedisPool(new JedisPoolConfig(), "192.168.226.70", 6389, 1800, "123456");
		
		// JedisSentinelPool
		Set<String> sentinelSet = new HashSet<String>();
		sentinelSet.add("192.168.226.70:26379");
		sentinelSet.add("192.168.226.70:26389");
		sentinelSet.add("192.168.226.70:26399");
		sentinelPool = new JedisSentinelPool("mymaster", sentinelSet, new JedisPoolConfig(), 1800, "123456");
		
		// ShardedJedisPool
		List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>();
		shardInfoList.add(new JedisShardInfo("192.168.226.70", 6389));
		shardInfoList.add(new JedisShardInfo("192.168.226.68", 6379));
		shardedPool = new ShardedJedisPool(new JedisPoolConfig(), shardInfoList);
	}

	public static void main(String[] args) {
		try {
			test();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 在应用退出时需关闭连接池
			pool.destroy();
		}
	}
	
	public static void test() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			// 基本类型测试
			// String
			jedis.set("banana", "99");
			// 输出：99
			System.out.println(jedis.get("banana"));
			
			// Hash
			jedis.hset("student", "name", "daozhang");
			jedis.hset("student", "age", "99");
			// 输出：{age=99, name=daozhang}
			System.out.println(jedis.hgetAll("student"));
			
			// List
			jedis.lpush("books", "Think in java");
			jedis.lpush("books", "Effective java");
			jedis.rpush("books", "Code Complete");
			// 输出：[Effective java, Think in java, Code Complete]
			System.out.println(jedis.lrange("books", 0, 100));
			
			// Sort
			jedis.sadd("users", "lucy", "jack", "lucy");
			// 输出：[jack, lucy]
			System.out.println(jedis.smembers("users"));
			
			// ZSort
			jedis.zadd("bucket", 9.8, "apple");
			jedis.zadd("bucket", 2.1, "banana");
			jedis.zadd("bucket", 5.3, "lemon");
			// 输出：[banana, lemon, apple]
			System.out.println(jedis.zrangeByScore("bucket", 0, 100));
			
			// 事务测试
			// 事务开始
			Transaction t = jedis.multi();
			t.set("fool", "bar"); 
			Response<String> foolRes = t.get("fool");
			t.zadd("foo", 1, "barowitch"); t.zadd("foo", 0, "barinsky"); t.zadd("foo", 0, "barikoviev");
			// -1表示无穷大
			Response<Set<String>> fooRes = t.zrange("foo", 0, -1);
			// 事务提交
			t.exec();
			// 使用Response.get获取返回的值。注意在t.exec()前，Response中是没有值的，Jedis通过watcher
			// 输出：bar
			System.out.println(foolRes.get());
			// 输出：[barikoviev, barinsky, barowitch]
			System.out.println(fooRes.get());
			// 也可以最后一次获取返回结果。但里边除了你要的返回值之外，还有Redis的状态信息等
			// List<Object> allResults = t.exec(); 
			// Redis事务不支持事务嵌套，比如multi提交后再来一个multi或watch，那么redis会返回一个错误，继续等待第一个multi的exec
			// 如果非要实现嵌套事务，可以使用eval/LUA脚本
			
			// 管道测试
			Pipeline p = jedis.pipelined();
			p.set("fool", "bar"); 
			p.zadd("foo", 1, "barowitch");  p.zadd("foo", 0, "barinsky"); p.zadd("foo", 0, "barikoviev");
			Response<String> pipeFoolRes = p.get("fool");
			Response<Set<String>> pipeFooRes = p.zrange("foo", 0, -1);
			p.sync(); 
			// 输出：bar
			System.out.println(pipeFoolRes.get());
			// 输出：[barikoviev, barinsky, barowitch]
			System.out.println(pipeFooRes.get());
			
			
			// 发布订阅测试
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 监听订阅
					Jedis jedis = null;
					try {
						jedis = pool.getResource();
						MyListener listener = new MyListener();
						jedis.subscribe(listener, "chat");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (jedis != null) {
							jedis.close();
						}
					}
				}
			}).start();

			TimeUnit.SECONDS.sleep(3l);
			// 发布
			jedis.publish("chat", "Hello, every body");
			jedis.publish("chat", "I come from China");
			// 输出：channel: chat; subscribedChannels: 1
			// 过两秒输出：
			// channel: chat; message: Hello, every body
			// channel: chat; message: I come from China
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭Jedis实例
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 简单继承几个方法，打印出日志
	 */
	public static class MyListener extends JedisPubSub {

		@Override
		public void onMessage(String channel, String message) {
			System.out.println("channel: " + channel + "; message: "+ message);
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			System.out.println("pattern: " + pattern + "; channel: " + channel + "; message: "+ message);
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			System.out.println("channel: " + channel + "; subscribedChannels: " + subscribedChannels);
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			System.out.println("channel: " + channel + "; subscribedChannels: " + subscribedChannels);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			System.out.println("pattern: " + pattern + "; subscribedChannels: " + subscribedChannels);
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			System.out.println("pattern: " + pattern + "; subscribedChannels: " + subscribedChannels);
		}

		@Override
		public void onPong(String pattern) {
			System.out.println("pattern: " + pattern);
		}
		
	}
}