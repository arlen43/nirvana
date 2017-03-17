/**
 * 项目名: nirvana
 * 文件名：CriteriaGeneratException.java 
 * 版本信息： V1.0
 * 日期：2017年2月27日 
 */
package com.arlen.common.mybatis.exception;

/** 
 * 项目名称：nirvana <br>
 * 类名称：CriteriaGeneratException <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年2月27日 下午3:37:42 <br>
 * @version 1.0
 * @author arlen
 */
public class CriteriaGeneratException extends RuntimeException {

	private static final long serialVersionUID = 912740981450160917L;

	public CriteriaGeneratException() {
		super();
	}

	public CriteriaGeneratException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CriteriaGeneratException(String message, Throwable cause) {
		super(message, cause);
	}

	public CriteriaGeneratException(String message) {
		super(message);
	}

	public CriteriaGeneratException(Throwable cause) {
		super(cause);
	}
	
}
