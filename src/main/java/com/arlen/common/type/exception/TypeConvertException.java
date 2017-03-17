/**
 * 项目名: empty_sample
 * 文件名：TypeConvertException.java 
 * 版本信息： V1.0
 * 日期：2017年2月24日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.type.exception;

/**
 * 项目名称：empty_sample <br>
 * 类名称：TypeConvertException <br>
 * 类描述：类型转换异常，包含要转换到的类型<br>
 * Copyright: Copyright (c) 2017 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2017年2月24日 上午10:45:32 <br>
 * 修改人：arlen<br>
 * 修改时间：2017年2月24日 上午10:45:32 <br>
 * 修改备注：<br>
 * 
 * @version 1.0
 * @author arlen
 */
public class TypeConvertException extends RuntimeException {

	private static final long serialVersionUID = 6045349885390688574L;

	private Class<?> type;

	public TypeConvertException(Class<?> type) {
		super();
		this.type = type;
	}

	public TypeConvertException(Class<?> type, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.type = type;
	}

	public TypeConvertException(Class<?> type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public TypeConvertException(Class<?> type, String message) {
		super(message);
		this.type = type;
	}

	public TypeConvertException(Class<?> type, Throwable cause) {
		super(cause);
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}
