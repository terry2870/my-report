package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysRoleMenuBean;
import com.myreport.dao.SysRoleMenuDao;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:24:14
 * 类描述：
 */
@Repository
public class SysRoleMenuDaoImpl implements SysRoleMenuDao {

	@Override
	public int insert(SysRoleMenuBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
 	}
	
	/**
	 * 批量添加角色与菜单关系
	 * @param list
	 * @throws Exception
	 */
	@Override
	public void batchAddRoleMenu(List<SysRoleMenuBean> list) throws Exception {
		String sql = "INSERT INTO sys_role_menu (role_id, menu_id) VALUES (?, ?)";
		List<Object[]> l = new ArrayList<Object[]>();
		SysRoleMenuBean roleMenu = null;
		for (int i = 0; i < list.size(); i++) {
			roleMenu = list.get(i);
			l.add(new Object[] { roleMenu.getRoleId(), roleMenu.getMenuId() });
		}
		jdbcTemplate.batchUpdate(sql, l);
	}

	@Override
	public int update(SysRoleMenuBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_role_menu WHERE role_id=?";
		List<Object[]> l = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			l.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, l);
	}

	@Override
	public SysRoleMenuBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysRoleMenuBean> queryAll(SysRoleMenuBean t, PageBean page, Object... o) throws Exception {
		String sql = "SELECT * FROM sys_role_menu t1 WHERE t1.role_id=?";
		Object[] obj = new Object[1];
		obj[0] = t.getRoleId();
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRoleMenuBean.class));
	}
	
	/**
	 * 根据菜单ID，查询所有角色与菜单关联关系
	 * @param menuId 菜单ID
	 * @return 菜单与角色关联关系
	 * @throws Exception
	 */
	@Override
	public List<SysRoleMenuBean> queryRoleMenuByMenuId(long menuId) throws Exception {
		String sql = "SELECT * FROM sys_role_menu WHERE menu_id=?";
		Object[] obj = new Object[1];
		obj[0] = menuId;
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRoleMenuBean.class));
	}
}
