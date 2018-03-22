package com.myreport.bean;


/**
 * @author hp
 * 2014-03-11
 */
public class SysUserActionLogBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	private Integer logId;
	private Integer userId;
	private String userIp;
	private String logType;
	private String logInfo;
	private String createDate;
	private String logClass;
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getLogInfo() {
		return logInfo;
	}
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getLogClass() {
		return logClass;
	}
	public void setLogClass(String logClass) {
		this.logClass = logClass;
	}

}

