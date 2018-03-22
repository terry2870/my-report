package com.myreport.bean;

/**
 * 用户组表的对象<br />
 * @author huangping <br />
 * 创建日期 2014-03-17
 */
public class SysUserGroupBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private Integer groupId; // 用户组ID
	private String groupName; // 用户组名称
	private String groupInfo; // 用户组描述
	private Integer createUserId; // 创建者ID
	private String createDate; // 创建日期
	private String status; // 状态


	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupInfo() {
		return this.groupInfo;
	}

	public void setGroupInfo(String groupInfo) {
		this.groupInfo = groupInfo;
	}

	public Integer getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
