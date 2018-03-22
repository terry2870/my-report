package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysMenuBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysMenuDao;
import com.myreport.enums.StatusEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午9:42:18
 * 类描述：
 */
@Repository
public class SysMenuDaoImpl implements SysMenuDao {

	@Override
	public int insert(SysMenuBean t) throws Exception {
		String sql = "INSERT INTO sys_menu (menu_name, menu_url, parent_menu_id, sort_number, status, menu_type, icon_name, target, extra_url, button_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] obj = new Object[10];
		obj[0] = t.getMenuName();
		obj[1] = t.getMenuUrl();
		obj[2] = t.getParentMenuId();
		obj[3] = t.getSortNumber();
		obj[4] = t.getStatus();
		obj[5] = t.getMenuType();
		obj[6] = t.getIconName();
		obj[7] = t.getTarget();
		obj[8] = t.getExtraUrl();
		obj[9] = t.getButtonId();
		return jdbcTemplate.update(sql, obj);
	}

	@Override
	public int update(SysMenuBean t) throws Exception {
		String sql = "UPDATE sys_menu SET menu_name=?, menu_url=?, sort_number=?, status=?, icon_name=?, target=?, extra_url=?, button_id=? WHERE menu_id=?";
		Object[] obj = new Object[9];
		obj[0] = t.getMenuName();
		obj[1] = t.getMenuUrl();
		obj[2] = t.getSortNumber();
		obj[3] = t.getStatus();
		obj[4] = t.getIconName();
		obj[5] = t.getTarget();
		obj[6] = t.getExtraUrl();
		obj[7] = t.getButtonId();
		obj[8] = t.getMenuId();
		return jdbcTemplate.update(sql, obj);
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_menu WHERE menu_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public SysMenuBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据菜单名称，查询菜单（只查询与该菜单同一个节点下的菜单）
	 * @param menuName
	 *            菜单名称
	 * @param parentId
	 *            父节点ID
	 * @return 返回菜单对象
	 * @throws Exception
	 */
	@Override
	public SysMenuBean queryMenuByMenuName(String menuName, long parentId) throws Exception {
		String sql = "SELECT * FROM sys_menu WHERE menu_name=? AND parent_menu_id=?";
		Object[] obj = new Object[2];
		obj[0] = menuName;
		obj[1] = parentId;
		List<SysMenuBean> list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysMenuBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 查询该节点下的子菜单（不递归）
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysMenuBean> queryChildMenuById(long menuId) throws Exception {
		String sql = "SELECT * FROM sys_menu WHERE parent_menu_id=? ORDER BY sort_number";
		Object[] obj = new Object[1];
		obj[0] = menuId;
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysMenuBean.class));
	}

	@Override
	public List<SysMenuBean> queryAll(SysMenuBean t, PageBean page, Object... o) throws Exception {
		String sql = "";
		Object[] obj = null;
		SysUserBean sessionUser = (SysUserBean) o[0];
		if (SessionUtil.isSuperManager(sessionUser.getLoginName())) {
			sql = "SELECT * FROM sys_menu WHERE status=? ORDER BY sort_number";
			obj = new Object[1];
			obj[0] = StatusEnum.A.toString();
		} else {
			sql = "SELECT t1.menu_id FROM sys_role_menu t1 INNER JOIN sys_menu t2 ON t1.menu_id=t2.menu_id WHERE t1.role_id=? AND t2.status='"+ StatusEnum.A.toString() +"'";
			obj = new Object[1];
			obj[0] = sessionUser.getRoleId();
			List<Integer> l = jdbcTemplate.queryForList(sql, obj, Integer.class);
			String menuIds = StringUtils.join(l, ",");
			sql = "SELECT * FROM sys_menu WHERE status='"+ StatusEnum.A.toString() +"' AND FIND_IN_SET(menu_id, getParentsMenuIdByMenuId(?)) ORDER BY sort_number";
//			sql = "select distinct menu.* from sys_menu menu connect by prior menu.parent_menu_id=menu.menu_id start with menu.menu_id in (select menu_id from sys_role_menu ur where ur.role_id=?) AND menu.status=? ORDER BY menu.sort_number";
			obj = new Object[1];
			obj[0] = menuIds;
		}
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysMenuBean.class));
	}
}
