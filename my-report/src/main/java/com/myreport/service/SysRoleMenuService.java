package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRoleMenuBean;

/**
 * 角色权限菜单的接口
 * @author huangping <br />
 * 2014-03-11
 */
public interface SysRoleMenuService {

	/**
	 * 新增角色菜单
	 * @param opera 操作对象
	 * @param returnInfo 返回对象
	 * @param list 角色菜单对象
	 * @throws Exception
	 */
	public void addRoleMenu(OperaBean opera, ReturnInfoBean returnInfo, List<SysRoleMenuBean> list) throws Exception;
	
	/**
	 * 查询该角色多能看到的菜单（只查询最后一层节点）
	 * @param roleId 角色ID
	 * @return 该角色的菜单
	 * @throws Exception
	 */
	public List<SysRoleMenuBean> queryRoleMenuByRoleId(Integer roleId) throws Exception;
	
	/**
	 * 根据菜单ID，查询所有角色与菜单关联关系
	 * @param menuId 菜单ID
	 * @return 菜单与角色关联关系
	 * @throws Exception
	 */
	public List<SysRoleMenuBean> queryRoleMenuByMenuId(Integer menuId) throws Exception;
}

