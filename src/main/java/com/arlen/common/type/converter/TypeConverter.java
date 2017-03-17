/**
 * 项目名: greenpass-api
 * 文件名：ITypeCast.java 
 * 版本信息： V1.0
 * 日期：2016年7月25日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.common.type.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arlen.common.type.exception.TypeConvertException;

/** 
 * 项目名称：greenpass-api <br>
 * 类名称：ITypeCast <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2016 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2016年7月25日 下午4:21:25 <br>
 * 修改人：arlen<br>
 * 修改时间：2016年7月25日 下午4:21:25 <br>
 * 修改备注：<br>
 * @version 1.0
 * @author arlen
 */
public abstract class TypeConverter<T> {
	
	protected final static Logger logger = LoggerFactory.getLogger(TypeConverter.class);
	
	/**
	 * cast(将String类型的值转化为对应的基本类型) 
	 * @param value
	 * @return 如果转换出错，打日志，并返回null
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public T convert(String value) {
		if (value == null || value.length() == 0) {
			logger.warn("Cast string value to other type, value is empty.");
			return null;
		}
		try {
			return doConvert(value);
		} catch (TypeConvertException e) {
			logger.error("Cast string value to '"+e.getType().getSimpleName()+"', input string '"+value+"' is invalid.", e);
		} catch (Exception e) {
			logger.error("Cast string value to other type error.", e);
		}
		return null;
	}
	
	protected abstract T doConvert(String value);
}
