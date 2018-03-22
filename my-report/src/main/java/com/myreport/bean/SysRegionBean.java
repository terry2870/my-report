package com.myreport.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 地区对象
 * @author huangping<br />
 * 2013-1-16
 */
public class SysRegionBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private Integer regionId;
	private String regionName;
	private String regionCode;
	private String regionType;
	private Integer parentRegionId;
	private Integer sortNumber;
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	
	public JSONObject toTreeJson() {
		JSONObject j = new JSONObject();
		j.put("text", this.regionName);
		j.put("attributes", this);
		j.put("id", this.regionId);
		return j;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public Integer getParentRegionId() {
		return parentRegionId;
	}
	public void setParentRegionId(Integer parentRegionId) {
		this.parentRegionId = parentRegionId;
	}
	public Integer getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

}


