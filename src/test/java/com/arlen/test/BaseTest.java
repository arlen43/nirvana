package com.arlen.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:config/spring/spring-*.xml" })
@ContextConfiguration(locations = { "classpath*:config/spring/spring-web.xml", "classpath*:config/spring/spring-redis.xml"})
public class BaseTest {
	
	public static void main(String[] args) throws InterruptedException {
		//sysn();
		List<Integer> l1 = Arrays.asList(new Integer[] {1,2,3});
		List<Integer> l2 = Arrays.asList(new Integer[] {1,2,3});
		List<Integer> l3 = new ArrayList<>(l1);
		System.out.println(l1.equals(l2));
		System.out.println(l1.equals(l3));
	}
	
	public static void sysn() {
		final ExecutorService exec = Executors.newFixedThreadPool(4);
		final String lock = "A";

		final Runnable add = new Runnable() {
			public void run() {
				System.out.println("Pre " + lock);
				synchronized (lock) {
					try {
						System.out.println(Thread.currentThread().getName() + " go in");
						// 1. 线程进入休眠状态，直至被唤醒或者时间到了，立马释放所有资源，也即其他线程可以进来了
						// wait(5000l);
						// 2. 线程处于挂起状态，但是不会释放任何资源
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						System.out.println("Post " + lock);
					}
				}
				
			}
		};
		for (int index = 0; index < 4; index++) {
			exec.submit(add);
		}
		exec.shutdown();
	}
	
	public static void reentrantLock() {
		final ExecutorService exec = Executors.newFixedThreadPool(4);
		final ReentrantLock lock = new ReentrantLock();
		final Condition con = lock.newCondition();

		final Runnable add = new Runnable() {
			public void run() {
				System.out.println("Pre " + lock);
				lock.lock();
				try {
					System.out.println(Thread.currentThread().getName() + " go in");
					// 1. 线程进入休眠状态，直至被唤醒或者时间到了，立马释放所有资源，也即其他线程可以进来了
					con.await(5000, TimeUnit.MILLISECONDS);
					// 2. 线程处于挂起状态，但是不会释放任何资源
					// Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("Post " + lock.toString());
					lock.unlock();
				}
			}
		};
		for (int index = 0; index < 4; index++) {
			exec.submit(add);
		}
		exec.shutdown();
	}
}
