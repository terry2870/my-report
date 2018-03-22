package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysMenuBean;


/**
 * @author huangping
 * 2013-08-15
 */
public interface SysMenuDao extends BaseDAO<SysMenuBean> {

	/**
	 * 根据菜单名称，查询菜单（只查询与该菜单同一个节点下的菜单）
	 * @param menuName
	 *            菜单名称
	 * @param parentId
	 *            父节点ID
	 * @return 返回菜单对象
	 * @throws Exception
	 */
	public SysMenuBean queryMenuByMenuName(String menuName, long parentId) throws Exception;
	
	/**
	 * 查询该节点下的子菜单（不递归）
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public List<SysMenuBean> queryChildMenuById(long menuId) throws Exception;

}



