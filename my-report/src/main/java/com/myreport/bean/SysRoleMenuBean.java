package com.myreport.bean;


/**
 * @author huangping
 * 2013-08-15
 */
public class SysRoleMenuBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	public SysRoleMenuBean() {
		
	}
	public SysRoleMenuBean(Integer roleId, Integer menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
	}
	private Integer roleId;
	private Integer menuId;
	private String str;
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
}


