/**
 * 项目名: nirvana
 * 文件名：CriteriaRule.java 
 * 版本信息： V1.0
 * 日期：2017年2月24日 
 */
package com.arlen.common.mybatis.criteria;

import java.util.List;

import com.arlen.common.verify.annotation.Length;
import com.arlen.common.verify.annotation.NotEmpty;

/** 
 * 项目名称：nirvana <br>
 * 类名称：CriteriaRule <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年2月24日 下午3:25:38 <br>
 * @version 1.0
 * @author arlen
 */
public class CriteriaFilter {
	/**
	 * 多个条件的组合方式：AND OR
	 */
	@Length(value=3, name="查询组合方式", options="and,or")
	private String groupOper;
	/**
	 * 查询条件
	 */
	@NotEmpty("查询条件")
	private List<CriteriaRule> ruleList;
	
	public boolean isValid() {
		return ruleList != null && ruleList.size() > 0;
	}
	
	public static class CriteriaRule {
		/**
		 * 查询操作<br>
		 * ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc', 'nu', 'nn']<br>
		 * ['equal','not equal', 'less', 'less or equal','greater','greater or equal', 'begins with',
		 *	'does not begin with','is in','is not in','ends with','does not end with','contains','does not contain', 
		 *  'is null','is not null'] 
		 */
		@Length(value=2,name="操作符",options="eq,ne,lt,le,gt,ge,bw,bn,in,ni,ew,en,cn,nc,nu,nn")
		private String operation;
		/**
		 * 查询字段
		 */
		@Length(value=256, name="查询字段")
		private String field;
		/**
		 * 字段值
		 */
		private String value;
		
		public String getOperation() {
			return operation;
		}
		
		public void setOperation(String operation) {
			this.operation = operation;
		}
		
		public String getField() {
			return field;
		}
		
		public void setField(String field) {
			this.field = field;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
	}

	public String getGroupOper() {
		return groupOper;
	}

	public void setGroupOper(String groupOper) {
		this.groupOper = groupOper;
	}

	public List<CriteriaRule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<CriteriaRule> ruleList) {
		this.ruleList = ruleList;
	}
	
}