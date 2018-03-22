package com.myreport.bean;

/**
 * 数据库表信息的对象<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
public class DatabaseInfoBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private Integer databaseId; // 主键
	private String databaseName; // 数据库名称
	private String databaseType; // 数据库类型
	private String userName; // 数据库用户名
	private String password; // 数据库用户密码
	private Integer createUserId; // 创建者
	private String createDate; // 创建日期
	private String status; // 状态
	private String databaseIp;
	private Integer databasePort;
	private String databaseTitle;

	private String statusName;
	private String createUserName; // 创建者名称
	private String queryStartDate; // 查询开始日期
	private String queryEndDate; // 查询结束日期

	public Integer getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseType() {
		return this.databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getDatabaseIp() {
		return databaseIp;
	}

	public void setDatabaseIp(String databaseIp) {
		this.databaseIp = databaseIp;
	}

	public Integer getDatabasePort() {
		return databasePort;
	}

	public void setDatabasePort(Integer databasePort) {
		this.databasePort = databasePort;
	}

	public String getDatabaseTitle() {
		return databaseTitle;
	}

	public void setDatabaseTitle(String databaseTitle) {
		this.databaseTitle = databaseTitle;
	}

}
