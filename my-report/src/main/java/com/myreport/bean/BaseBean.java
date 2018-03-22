package com.myreport.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 下午5:24:50
 * 类描述：
 */
public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String createUserName; // 创建者名称
	private String statusName; // 状态名称
	private String queryStartDate; // 查询开始日期
	private String queryEndDate; // 查询结束日期
	
	private String className = this.getClass().getName();

	public String getClassName() {
		return className;
	}
	
	public String getCreateUserName() {
		return this.createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getStatusName() {
		return this.statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getQueryStartDate() {
		return this.queryStartDate;
	}

	public void setQueryStartDate(String queryStartDate) {
		this.queryStartDate = queryStartDate;
	}

	public String getQueryEndDate() {
		return this.queryEndDate;
	}

	public void setQueryEndDate(String queryEndDate) {
		this.queryEndDate = queryEndDate;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
