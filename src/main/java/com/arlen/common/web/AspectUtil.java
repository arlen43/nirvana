/**
 * 项目名: nirvana
 * 文件名：AspectUtil.java 
 * 版本信息： V1.0
 * 日期：2017年3月31日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.web;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 项目名称：nirvana <br>
 * 类名称：AspectUtil <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月31日 下午5:37:29 <br>
 * @version 1.0
 * @author arlen
 */
public class AspectUtil {

	private final static Logger logger = LoggerFactory.getLogger(AspectUtil.class);
	
	public static List<Object> findArgsByType(JoinPoint pjp, Class<?> clazz) {
		List<Object> needHandleParamList = new ArrayList<Object>();
		Signature signature = pjp.getSignature();
		
		Class<?>[] paramTypes = ((MethodSignature)signature).getMethod().getParameterTypes();
		if (null == paramTypes || paramTypes.length <= 0) {
			logger.warn("Find arguments by class {} in join point {}, arguments type is empty.", clazz.getName(), signature.getName());
			return needHandleParamList;
		}
		Object[] args = pjp.getArgs();
		if (null == args || args.length <= 0) {
			logger.warn("Find arguments by class {} in join point {}, arguments is empty.", clazz.getName(), signature.getName());
			return needHandleParamList;
		}
		
		for (int i = 0; i < paramTypes.length; i++) {
			if (clazz.isAssignableFrom(paramTypes[i])) {
				if (args[i] != null) {
					needHandleParamList.add(args[i]);
				} else {
					logger.warn("Found matched type '{}' on index {}, but argument is null", clazz.getName(), i);
				}
			}
		}
		return needHandleParamList;
	}
}
