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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arlen.common.sequence.exception.SequenceGenerateException;

/** 
 * 项目名称：empty_sample <br>
 * 类名称：SequenceUtil <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月28日 下午4:55:01 <br>
 * @version 1.0
 * @author arlen
 */
public class SequenceUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SequenceUtil.class);
	
	private final static Map<Byte, BaseSequence> sequenceMap = new ConcurrentHashMap<Byte, BaseSequence>();
	
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
				sequenceMap.put(key, new BaseSequence(key));
			}
			return sequenceMap.get(key).getNextId();
		} catch (Exception e) {
			throw new SequenceGenerateException(e);
		}
	}
	
}
