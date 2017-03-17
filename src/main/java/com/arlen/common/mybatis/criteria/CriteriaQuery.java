/**
 * 项目名: nirvana
 * 文件名：CriteriaQuery.java 
 * 版本信息： V1.0
 * 日期：2017年2月28日 
 */
package com.arlen.common.mybatis.criteria;

import org.springframework.util.StringUtils;

import com.arlen.common.page.Paging;
import com.arlen.common.verify.annotation.NotNull;

/** 
 * 项目名称：nirvana <br>
 * 类名称：CriteriaQuery <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年2月28日 下午2:10:16 <br>
 * @version 1.0
 * @author arlen
 */
public class CriteriaQuery<T> extends Paging {

	private static final long serialVersionUID = 5293288385585876091L;

	/**
	 * 索引，如果指定排序方式，则此为排序字段
	 */
	private String index;
	
	/**
	 * 排序方式，asc desc
	 */
	private String sortType;
	
	/**
	 * 是否有查询条件
	 */
	private boolean hasSearch;
	
	/** 
	 * 查询条件
	 */
	private CriteriaFilter criteriaFilter;

	/**
	 * Mybatis Example类的类型
	 */
	@NotNull
	private Class<T> queryType;
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public CriteriaFilter getCriteriaFilter() {
		return criteriaFilter;
	}

	public void setCriteriaFilter(CriteriaFilter criteriaFilter) {
		this.criteriaFilter = criteriaFilter;
	}

	public Class<T> getQueryType() {
		return queryType;
	}

	public void setQueryType(Class<T> queryType) {
		this.queryType = queryType;
	}

	public boolean isHasSearch() {
		return hasSearch;
	}

	public void setHasSearch(boolean hasSearch) {
		this.hasSearch = hasSearch;
	}

	public String getSortClause() {
		if (hasSortClause()) {
			return index + " " + sortType;
		}
		return null;
	}

	public boolean hasSortClause() {
		return !StringUtils.isEmpty(index) && !StringUtils.isEmpty(sortType);
	}
	
}
