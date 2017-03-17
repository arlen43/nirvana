/**
 * 项目名: empty_sample
 * 文件名：ReflectUtil.java 
 * 版本信息： V1.0
 * 日期：2017年2月20日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.reflect;

import org.springframework.util.StringUtils;

/** 
 * 项目名称：empty_sample <br>
 * 类名称：ReflectUtil <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2017 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2017年2月20日 上午10:34:47 <br>
 * 修改人：arlen<br>
 * 修改时间：2017年2月20日 上午10:34:47 <br>
 * 修改备注：<br>
 * @version 1.0
 * @author arlen
 */
public class ReflectUtil {

	private ReflectUtil() {}
	
	public static String getMethodByFieldName(String fieldName, String prefix, String suffix) {
		String methodName = getSuffixOfMethodByFieldName(fieldName);
		methodName = prefix + methodName + suffix;
		return methodName;
	}
	
	public static String getGetterByFieldName(String fieldName) {
		return getGetterOrSetterMethod(fieldName, "get");
	}
	
	public static String getSetterByFieldName(String fieldName) {
		return getGetterOrSetterMethod(fieldName, "set");
	}
	
	public static String getGetterOrSetterMethod(String fieldName, String prefix) {
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(prefix)) {
			return fieldName;
		}
		if (prefix.equalsIgnoreCase("get")) {
			return "get" + getSuffixOfMethodByFieldName(fieldName);
		} else if (prefix.equalsIgnoreCase("set")) {
			return "set" + getSuffixOfMethodByFieldName(fieldName);
		}
		return fieldName;
	}
	
	public static String getSuffixOfMethodByFieldName(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return fieldName;
		}
		String upper = fieldName.substring(0,1).toUpperCase();
		return upper + fieldName.substring(1);
	}
}
