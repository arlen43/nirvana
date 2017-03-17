/**
 * 项目名: nirvana
 * 文件名：GroupEnum.java 
 * 版本信息： V1.0
 * 日期：2017年3月15日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.mybatis.criteria;

/** 
 * 项目名称：nirvana <br>
 * 类名称：GroupEnum <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月15日 下午2:07:11 <br>
 * @version 1.0
 * @author arlen
 */
public enum GroupEnum {
	AND("and"),
	OR("or");
	
	private GroupEnum(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
}
