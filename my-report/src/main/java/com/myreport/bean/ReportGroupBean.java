package com.myreport.bean;

/**
 * 分组信息的对象<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public class ReportGroupBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private Integer groupId; // 分组ID
	private String groupName; // 组名
	private Integer parentGroupId; // 父节点ID
	private Integer sortNumber; // 排序号
	private Integer createUserId; // 创建者
	private String createDate; // 创建日期
	private String status; // 状态

	private String statusName;
	private String createUserName; // 创建者名称
	private String queryStartDate; // 查询开始日期
	private String queryEndDate; // 查询结束日期
	private String searchType;//搜寻方式（-1：向上遍历；0-上下遍历；1：向下遍历）

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

	public Integer getParentGroupId() {
		return this.parentGroupId;
	}

	public void setParentGroupId(Integer parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public Integer getSortNumber() {
		return this.sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
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

	public String getCreateUserName() {
		return this.createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

}
