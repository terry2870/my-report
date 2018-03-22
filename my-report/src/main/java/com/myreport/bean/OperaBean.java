package com.myreport.bean;


/**
 * 操作着的实体对象
 * @author hp
 * 2014-03-11
 */
public class OperaBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	private String operatorIp;
	private SysUserBean user;
	private String operaType;
	private String logInfo;
	
	public OperaBean() {
		
	}
	
	public OperaBean(String operatorIp) {
		this.operatorIp = operatorIp;
	}
	
	public OperaBean(SysUserBean user) {
		this.user = user;
	}
	
	public OperaBean(String operatorIp, SysUserBean user) {
		this.operatorIp = operatorIp;
		this.user = user;
	}
	
	public SysUserBean getUser() {
		return user;
	}
	public void setUser(SysUserBean user) {
		this.user = user;
	}
	public String getOperatorIp() {
		return operatorIp;
	}
	public void setOperatorIp(String operatorIp) {
		this.operatorIp = operatorIp;
	}

	public String getOperaType() {
		return operaType;
	}

	public void setOperaType(String operaType) {
		this.operaType = operaType;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
}

