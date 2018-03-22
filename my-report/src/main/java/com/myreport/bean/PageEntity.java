package com.myreport.bean;

import com.mytools.beans.PageBean;
import com.mytools.utils.DBUtil;

/**
 * @author hp
 * 2014-02-14
 */
public class PageEntity extends BaseBean {

	private static final long serialVersionUID = 1L;

	private int page;
	private int rows;
	private String sort;
	private String order;
	
	public PageBean toPageBean() {
		PageBean p = new PageBean();
		p.setPageSize(this.rows);
		p.setCurrentPage(this.page);
		p.setOrderBy(DBUtil.unFormatColumn(this.sort, true));
		p.setSort(this.order);
		return p;
	}
	
	public PageBean toPageBeanForReport() {
		PageBean p = new PageBean();
		p.setPageSize(this.rows);
		p.setCurrentPage(this.page);
		p.setOrderBy(this.sort);
		p.setSort(this.order);
		return p;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty() {
		if (this.page == 0 && this.rows == 0) {
			return true;
		} else {
			return false;
		}
	}
}


