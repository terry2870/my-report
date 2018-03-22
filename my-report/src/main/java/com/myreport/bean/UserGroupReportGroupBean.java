package com.myreport.bean;

public class UserGroupReportGroupBean {

	private Integer userGroupId;
	private Integer reportGroupId;
	
	
	public UserGroupReportGroupBean() {
	}
	public UserGroupReportGroupBean(Integer userGroupId, Integer reportGroupId) {
		this.userGroupId = userGroupId;
		this.reportGroupId = reportGroupId;
	}
	public Integer getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}
	public Integer getReportGroupId() {
		return reportGroupId;
	}
	public void setReportGroupId(Integer reportGroupId) {
		this.reportGroupId = reportGroupId;
	}
	
}
