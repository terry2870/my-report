package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysRoleMenuBean;

/**
 * 角色权限菜单数据库操作类
 * @author huangping
 * 2013-08-15
 */
public interface SysRoleMenuDao extends BaseDAO<SysRoleMenuBean> {

	/**
	 * 批量添加角色与菜单关系
	 * @param list
	 * @throws Exception
	 */
	public void batchAddRoleMenu(List<SysRoleMenuBean> list) throws Exception;
	
	/**
	 * 根据菜单ID，查询所有角色与菜单关联关系
	 * @param menuId 菜单ID
	 * @return 菜单与角色关联关系
	 * @throws Exception
	 */
	public List<SysRoleMenuBean> queryRoleMenuByMenuId(long menuId) throws Exception;

}



