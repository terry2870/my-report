package com.myreport.bean;

public class SysUserGroupUserBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private Integer userId;
	private Integer groupId;
	
	public SysUserGroupUserBean() {
		
	}
	
	public SysUserGroupUserBean(Integer userId, Integer groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
}
