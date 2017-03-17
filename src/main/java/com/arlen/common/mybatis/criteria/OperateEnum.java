/**
 * 项目名: nirvana
 * 文件名：OperateEnum.java 
 * 版本信息： V1.0
 * 日期：2017年3月15日 
 */
package com.arlen.common.mybatis.criteria;

/** 
 * 项目名称：nirvana <br>
 * 类名称：OperateEnum <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月15日 上午10:01:57 <br>
 * @version 1.0
 * @author arlen
 */
public enum OperateEnum {

	/**
	 *  "eq", "and{0}EqualTo"
	 */
	EQUAL_TO("eq"),
	/**
	 *  "ne", "and{0}NotEqualTo"
	 */
	NOT_EQUAL_TO("ne"),
	/** 
	 * lt", "and{0}LessThan"
	 */
	LESS_THAN("lt"),
	/** 
	 * le", "and{0}LessThanOrEqualTo"
	 */
	LESS_THAN_OR_EQUAL_TO("le"),
	/** 
	 * gt", "and{0}GreaterThan"
	 */
	GREATER_THAN("gt"),
	/** 
	 * ge", "and{0}GreaterThanOrEqualTo"
	 */
	GREATER_THAN_OR_EUQAL_TO("ge"),
	/** 
	 * bw", "and{0}Like"
	 */
	BEGINS_WITH("bw"),
	/** 
	 * bn", "and{0}NotLike"
	 */
	NOT_BEGINS_WITH("bn"),
	/** 
	 * in", "and{0}In"
	 */
	IS_IN("in"),
	/** 
	 * ni", "and{0}NotIn"
	 */
	IS_NOT_IN("ni"),
	/** 
	 * ew", "and{0}Like"
	 */
	ENDS_WITH("ew"),
	/** 
	 * en", "and{0}NotLike"
	 */
	NOT_ENDS_WITH("en"),
	/** 
	 * cn", "and{0}Like"
	 */
	CONTAINS("cn"),
	/** 
	 * nc", "and{0}NotLike"
	 */
	NOT_CONTAINS("nc"),
	/** 
	 * nn", "and{0}IsNull"
	 */
	IS_NULL("nn"),
	/** 
	 * nu", "and{0}IsNotNull"
	 */
	IS_NOT_NULL("nu");
	
	private OperateEnum(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
	
}
