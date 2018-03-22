package com.myreport.bean;

/**
 * 报表的对象<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public class ReportInfoBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private Integer reportId; // 报表ID
	private String reportName; // 报表名称
	private String originalSql; // 原始sql
	private String executeSql; // 执行的sql
	private String queryConditions; // 查询条件
	private String status; // 状态
	private Integer createUserId; // 创建者
	private String createDate; // 创建日期
	private Integer groupId; // 所属分组
	private Integer databaseId; // 数据库ID
	private Integer sortNumber; //排序字段
	private String paramKeys; //
	private String tableParams;

	private String createUserName; // 创建者名称
	private String statusName; // 状态名称
	private String queryStartDate; // 查询开始日期
	private String queryEndDate; // 查询结束日期

	public Integer getReportId() {
		return this.reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getOriginalSql() {
		return this.originalSql;
	}

	public void setOriginalSql(String originalSql) {
		this.originalSql = originalSql;
	}

	public String getExecuteSql() {
		return this.executeSql;
	}

	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}

	public String getQueryConditions() {
		return this.queryConditions;
	}

	public void setQueryConditions(String queryConditions) {
		this.queryConditions = queryConditions;
	}
	
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
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

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public String getParamKeys() {
		return paramKeys;
	}

	public void setParamKeys(String paramKeys) {
		this.paramKeys = paramKeys;
	}

	public String getTableParams() {
		return tableParams;
	}

	public void setTableParams(String tableParams) {
		this.tableParams = tableParams;
	}

}
