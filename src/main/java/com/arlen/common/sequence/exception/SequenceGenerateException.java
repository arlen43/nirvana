/**
 * 项目名: nirvana
 * 文件名：SequenceGenerateException.java 
 * 版本信息： V1.0
 * 日期：2017年3月28日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.sequence.exception;

/** 
 * 项目名称：nirvana <br>
 * 类名称：SequenceGenerateException <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月28日 下午9:40:32 <br>
 * @version 1.0
 * @author arlen
 */
public class SequenceGenerateException extends RuntimeException {

	private static final long serialVersionUID = 8318097740455627621L;

	public SequenceGenerateException() {
		super();
	}

	public SequenceGenerateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SequenceGenerateException(String message, Throwable cause) {
		super(message, cause);
	}

	public SequenceGenerateException(String message) {
		super(message);
	}

	public SequenceGenerateException(Throwable cause) {
		super(cause);
	}
	
}
