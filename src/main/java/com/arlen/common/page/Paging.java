package com.arlen.common.page;

import java.io.Serializable;
import java.util.List;

/** 
 * 项目名称：nirvana <br>
 * 类名称：Paging <br>
 * 类描述：分页，页码从1开始<br>
 * 创建人：arlen <br>
 * 创建时间：2016年8月11日 下午4:49:04 <br>
 * @version 1.0
 * @author arlen
 */
public class Paging implements Serializable {

	private static final long serialVersionUID = -8755772314949622163L;
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_CURRENT_PAGE = 1;
	
	/**
	 * 分页标识
	 */
	private boolean pageFlag = false;
	/**
	 * 当前页，即第几页<br>current page
	 */
	private int currentPage = DEFAULT_CURRENT_PAGE;
	/**
	 * 页面大小<br>page size
	 */
	private int pageRows = DEFAULT_PAGE_SIZE;
	/**
	 * 总页数
	 */
	private int totalPages = 0;
	/**
	 * 总行数，总记录条数
	 */
	private int totalRows = 0;
	/**
	 * 数据
	 */
	private List<?> records;
	/**
	 * 出错数据
	 */
	private String errMsg;
	
	public Paging() {
		
	}

	/**
	 * 分页，起始页为1，页面行数为10. 
	 *
	 * @param totalRows
	 */
	public Paging(int totalRows) {
		this.pageFlag = true;
		this.totalRows = totalRows;
	}

	/**
	 * 分页，起始页为1
	 *
	 * @param currentPage
	 * @param pageRows
	 * @param totalRows
	 */
	public Paging(int currentPage, int pageRows, int totalRows) {
		this.pageFlag = true;
		this.currentPage = currentPage;
		this.pageRows = pageRows;
		this.totalRows = totalRows;
	}

	public int getStartIndex() {
		return (currentPage - 1) * pageRows;
	}
	
	public int getEndIndex() {
		int startIndex = getStartIndex();
		return (startIndex + pageRows) < totalRows? (startIndex + pageRows): totalRows;
	}

	public int getTotalPages() {
		totalPages = (totalRows % pageRows) == 0? (totalRows / pageRows):  (totalRows / pageRows + 1);
		return totalPages;
	}

	public boolean hasNext() {
		return currentPage < getTotalPages();
	}
	
	public void next() {
		this.currentPage++;
	}
	
	public int getOffset() {
		return getStartIndex();
	}
	
	public int getLimit() {
		return this.pageRows;
	}

	// ////////////////////

	public boolean isPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(boolean pageFlag) {
		this.pageFlag = pageFlag;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageRows() {
		return pageRows;
	}

	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public List<?> getRecords() {
		return records;
	}

	public void setRecords(List<?> records) {
		this.records = records;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
