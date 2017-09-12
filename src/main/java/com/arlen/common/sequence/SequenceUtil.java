/**
 * 项目名: empty_sample
 * 文件名：SequenceUtil.java 
 * 版本信息： V1.0
 * 日期：2017年3月28日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.sequence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.arlen.common.sequence.dao.IBaseSequenceDao;
import com.arlen.common.spring.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arlen.common.sequence.exception.SequenceGenerateException;
import org.springframework.context.ApplicationContext;

/** 
 * 项目名称：empty_sample <br>
 * 类名称：SequenceUtil <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月28日 下午4:55:01 <br>
 *     重要说明：需在web.xml的spring context listener中配置为classpath*，以让spring扫到该bean<br>
 *     需配置事务，并开启注解事务，以让序列号操作可以使用事务。<br>
 *     需配置Mybatis，并扫描到config/mybatis/sequence
 * @version 1.0
 * @author arlen
 */
public class SequenceUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SequenceUtil.class);
	
	private final static Map<Byte, BaseSequence> sequenceMap = new ConcurrentHashMap<Byte, BaseSequence>();

	// 全局加锁，无并发问题
	private static IBaseSequenceService baseSequenceService;

	private SequenceUtil() {}
	
	/**
	 * 根据业务key获取对应的自增序列
	 * @param key
	 * @return 序列号
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 * @throws SequenceGenerateException
	 */
	public synchronized static int getNextId(Byte key) {
		try {
			if (!sequenceMap.containsKey(key)) {
				logger.info("Get sequence of key {}, create or reload a new sequence.", key);
				BaseSequence baseSequence = getBaseSequenceService().getNewBaseSequence(key);
				sequenceMap.put(key, baseSequence);
			}
			return getBaseSequenceService().getNextId(sequenceMap.get(key));
		} catch (Exception e) {
			throw new SequenceGenerateException(e);
		}
	}

	public static IBaseSequenceService getBaseSequenceService() {
		if (baseSequenceService == null) {
			ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
			if (applicationContext == null) {
				throw new SequenceGenerateException("Init sequence faild, not in web environment. Get WebApplication failed");
			}
			baseSequenceService = applicationContext.getBean(IBaseSequenceService.class);
			if (baseSequenceService == null) {
				throw new SequenceGenerateException("Init sequence faild, Bean IBaseSequenceService dose not init");
			}
		}
		return baseSequenceService;
	}

}
