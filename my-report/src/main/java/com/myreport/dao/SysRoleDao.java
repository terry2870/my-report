package com.myreport.dao;

import com.myreport.bean.SysRoleBean;

/**
 * @author huangping
 * 2014-02-14
 */
public interface SysRoleDao extends BaseDAO<SysRoleBean> {

	/**
	 * 根据名称，查询角色信息
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	public SysRoleBean queryRoleByName(String roleName) throws Exception;

}


