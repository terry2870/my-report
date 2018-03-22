package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysRoleBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysRoleDao;
import com.myreport.enums.StatusEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:22:50
 * 类描述：
 */
@Repository
public class SysRoleDaoImpl implements SysRoleDao {

	@Override
	public int insert(SysRoleBean t) throws Exception {
		String sql = "INSERT INTO sys_role (role_name, role_info, status, create_user_id) VALUES (?, ?, ? ,?)";
		Object[] obj = new Object[4];
		obj[0] = t.getRoleName();
		obj[1] = t.getRoleInfo();
		obj[2] = t.getStatus();
		obj[3] = t.getCreateUserId();
		return jdbcTemplate.update(sql, obj);
	}

	@Override
	public int update(SysRoleBean t) throws Exception {
		String sql = "UPDATE sys_role SET role_name=?, role_info=?, status=? WHERE role_id=?";
		Object[] obj = new Object[4];
		obj[0] = t.getRoleName();
		obj[1] = t.getRoleInfo();
		obj[2] = t.getStatus();
		obj[3] = t.getRoleId();
		return jdbcTemplate.update(sql, obj);
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_role WHERE role_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public SysRoleBean queryObjectById(Integer roleId) throws Exception {
		String sql = "SELECT * FROM sys_role WHERE role_id=?";
		Object[] obj = new Object[1];
		obj[0] = roleId;
		List<SysRoleBean> list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRoleBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 根据名称，查询角色信息
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	@Override
	public SysRoleBean queryRoleByName(String roleName) throws Exception {
		String sql = "SELECT * FROM sys_role WHERE role_name=?";
		Object[] obj = new Object[1];
		obj[0] = roleName;
		List<SysRoleBean> list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRoleBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}
	
	@Override
	public List<SysRoleBean> queryAll(SysRoleBean t, PageBean page, Object... o) throws Exception {
		List<SysRoleBean> list = null;
		String sql = "SELECT role.*, us.user_name create_user_name, "+ StatusEnum.toSql("role.status") +" status_name FROM sys_role role " +
				"LEFT JOIN sys_user us ON role.create_user_id=us.user_id " +
				"WHERE 1=1";
		List<Object> l = new ArrayList<Object>();
		if (o != null && o.length > 0) {
			SysUserBean sessionUser = (SysUserBean) o[0];
			if (!SessionUtil.isSuperManager(sessionUser.getLoginName())) {
				sql += " AND EXISTS (SELECT * FROM sys_user u WHERE role.create_user_id=u.user_id AND FIND_IN_SET(u.user_id, getChildUserIdBy(?)))";
				//sql += " AND EXISTS (SELECT * FROM sys_user u WHERE u.user_id=role.create_user_id START WITH u.user_id=? CONNECT BY PRIOR u.user_id=u.create_user_id)";
				l.add(sessionUser.getUserId());
			}
			sql += " OR role.role_id=? ";
			l.add(sessionUser.getRoleId());
		}
		if (t != null) {
			if (!StringUtils.isEmpty(t.getRoleName())) {
				sql += " AND INSTR(role.role_name, ?) > 0";
				l.add(t.getRoleName());
			}
			if (!StringUtils.isEmpty(t.getStatus())) {
				sql += " AND role.status=?";
				l.add(t.getStatus());
			}
			if (t.getCreateUserId() != null && t.getCreateUserId() != 0) {
				sql += " AND role.create_user_id=?";
				l.add(t.getCreateUserId());
			}
		}
		sql += " ORDER BY role.role_id";
		Object[] obj = l.toArray();
		if (page != null) {
			page.setSql(sql);
			page.setParams(obj);
			page.setTotalCount(jdbcTemplate.queryForObject(mysqlDatabaseAbst.getTotalCountSql(sql), obj, Integer.class));
			list = jdbcTemplate.query(mysqlDatabaseAbst.getPageSQL(page), obj, BeanPropertyRowMapper.newInstance(SysRoleBean.class));
		} else {
			list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRoleBean.class));
		}
		return list;
	}
}
