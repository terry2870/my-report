package com.myreport.bean;


/**
 * 历史密码对象
 * @author huangping<br />
 * 2014-02-14
 */
public class SysPwdHistoryBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	private Integer pwdHisId;
	private String loginPwd;
	private Integer userId;
	private String pwdCreateDate;
	private String createDate;
	public Integer getPwdHisId() {
		return pwdHisId;
	}
	public void setPwdHisId(Integer pwdHisId) {
		this.pwdHisId = pwdHisId;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPwdCreateDate() {
		return pwdCreateDate;
	}
	public void setPwdCreateDate(String pwdCreateDate) {
		this.pwdCreateDate = pwdCreateDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}


