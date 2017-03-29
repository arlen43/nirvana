/**
 * 项目名: nirvana
 * 文件名：SpringContextUtil.java 
 * 版本信息： V1.0
 * 日期：2017年3月28日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.spring;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 项目名称：nirvana <br>
 * 类名称：SpringContextUtil <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2017 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月28日 下午8:06:09 <br>
 * 修改人：arlen<br>
 * 修改时间：2017年3月28日 下午8:06:09 <br>
 * 修改备注：<br>
 * 
 * @version 1.0
 * @author arlen
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}

	/**
	 * 获取applicationContext
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据id获取Bean
	 * 
	 * @param id
	 * @return Bean
	 */
	public static Object getBeanById(String id) {
		return applicationContext.getBean(id);
	}

	/**
	 * 根据class获取Bean
	 * 
	 * @param clazz
	 * @return Bean
	 */
	public static Object getBeanByClass(Class<?> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 根据class获取所有的Bean
	 * 
	 * @param clazz
	 * @return Map
	 */
	public static Map<String, ?> getBeansByClass(Class<?> clazz) {
		return applicationContext.getBeansOfType(clazz);
	}
}
