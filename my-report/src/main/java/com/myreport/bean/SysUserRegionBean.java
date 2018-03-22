package com.myreport.bean;

/**
 * 用户与地区关系对象
 * @author huangping<br />
 * 2013-2-4
 */
public class SysUserRegionBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public SysUserRegionBean() {
	}
	public SysUserRegionBean(Integer userId, String regionId) {
		super();
		this.userId = userId;
		this.regionId = regionId;
	}
	private Integer userId;
	private String regionId;
	private String[] regionIds;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String[] getRegionIds() {
		return regionIds;
	}
	public void setRegionIds(String[] regionIds) {
		this.regionIds = regionIds;
	}

}

