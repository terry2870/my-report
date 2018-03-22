package com.myreport.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysUserDao;
import com.myreport.enums.StatusEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;
import com.mytools.utils.DateUtil;
import com.mytools.utils.MD5Util;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:26:33
 * 类描述：
 */
@Repository
public class SysUserDaoImpl implements SysUserDao {

	@Override
	public int insert(SysUserBean t) throws Exception {
		final String sql = "INSERT INTO sys_user (user_name, login_name, login_pwd, mobile, phone_number, address, email, role_id, status, create_user_id, time_out_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		final Object[] obj = new Object[11];
		obj[0] = t.getUserName();
		obj[1] = t.getLoginName();
		obj[2] = MD5Util.getMD5(t.getLoginPwd());
		obj[3] = t.getMobile();
		obj[4] = t.getPhoneNumber();
		obj[5] = t.getAddress();
		obj[6] = t.getEmail();
		obj[7] = t.getRoleId();
		obj[8] = t.getStatus();
		obj[9] = t.getCreateUserId();
		obj[10] = t.getTimeOutDate();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < obj.length; i++) {
					ps.setObject(i + 1, obj[i]);
				}
				return ps;
			}
		}, keyHolder);
		//jdbcTemplate.update(sql, obj);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int update(SysUserBean t) throws Exception {
		String sql = "UPDATE sys_user SET user_name=?, mobile=?, phone_number=?, address=?, email=?, role_id=?, status=? WHERE user_id=? ";
		Object[] obj = new Object[8];
		obj[0] = t.getUserName();
		obj[1] = t.getMobile();
		obj[2] = t.getPhoneNumber();
		obj[3] = t.getAddress();
		obj[4] = t.getEmail();
		obj[5] = t.getRoleId();
		obj[6] = t.getStatus();
		obj[7] = t.getUserId();
		return jdbcTemplate.update(sql, obj);
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_user WHERE user_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public SysUserBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysUserBean> queryAll(SysUserBean t, PageBean page, Object... o) throws Exception {
		String sql = "SELECT u.*, r.role_name, " + StatusEnum.toSql("u.status") + " status_name FROM sys_user u";
		sql += " LEFT JOIN sys_role r ON u.role_id=r.role_id";
		sql += " WHERE 1=1";
		List<SysUserBean> list = null;
		List<Object> l = new ArrayList<Object>();
		if (t != null) {
			if (!StringUtils.isEmpty(t.getLoginName())) {
				sql += " AND INSTR(u.login_Name, ?) > 0";
				l.add(t.getLoginName());
			}
			if (!StringUtils.isEmpty(t.getUserName())) {
				sql += " AND INSTR(u.user_Name, ?) > 0";
				l.add(t.getUserName());
			}
			if (!StringUtils.isEmpty(t.getStatus())) {
				sql += " AND u.status=?";
				l.add(t.getStatus());
			}
			if (t.getRoleId() != null && t.getRoleId() != 0) {
				sql += " AND u.role_id=?";
				l.add(t.getRoleId());
			}
			if (t.getCreateUserId() != null && t.getCreateUserId() != 0) {
				sql += " AND u.create_user_id=?";
				l.add(t.getCreateUserId());
			}
		}
		if (o != null && o.length > 0) {
			SysUserBean user = (SysUserBean) o[0];
			if (!SessionUtil.isSuperManager(user.getLoginName())) {
				sql += " AND EXISTS (SELECT * FROM sys_user u2 WHERE u.create_user_id=u2.user_id AND FIND_IN_SET(u2.user_id, getChildUserIdBy(?)))";
				// sql += " START WITH u.user_id=? CONNECT BY PRIOR u.user_id=u.create_user_id";
				l.add(user.getUserId());
			}
			sql += " OR u.user_id=?";
			l.add(user.getUserId());
		}
		sql += " ORDER BY u.user_id";
		Object[] obj = l.toArray();
		if (page != null) {
			page.setSql(sql);
			page.setParams(obj);
			page.setTotalCount(jdbcTemplate.queryForObject(mysqlDatabaseAbst.getTotalCountSql(sql), obj, Integer.class).intValue());
			list = jdbcTemplate.query(mysqlDatabaseAbst.getPageSQL(page), obj, BeanPropertyRowMapper.newInstance(SysUserBean.class));
		} else {
			list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysUserBean.class));
		}
		return list;
	}

	/**
	 * 查询该角色下的所有用户
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysUserBean> queryUserByRoleId(Integer roleId) throws Exception {
		String sql = "SELECT * FROM sys_user WHERE role_id=?";
		Object[] obj = new Object[1];
		obj[0] = roleId;
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysUserBean.class));
	}

	/**
	 * 根据登录名，查询用户
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	@Override
	public SysUserBean queryUserByLoginName(String loginName) throws Exception {
		String sql = "SELECT * FROM sys_user WHERE login_name=? AND status='"+ StatusEnum.A.toString() +"'";
		Object[] obj = new Object[1];
		obj[0] = loginName;
		List<SysUserBean> list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysUserBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public SysUserBean login(SysUserBean user) throws Exception {
		String sql = "SELECT * FROM sys_user WHERE login_name=? AND login_pwd=? AND status='"+ StatusEnum.A.toString() +"'";
		Object[] obj = new Object[2];
		obj[0] = user.getLoginName();
		obj[1] = MD5Util.getMD5(user.getLoginPwd());
		List<SysUserBean> list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysUserBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 *            用户信息
	 * @return 返回修改的行数
	 * @throws Exception
	 */
	@Override
	public long modifyPwd(SysUserBean user) throws Exception {
		String sql = "UPDATE sys_user SET login_pwd=?, time_out_date=? WHERE login_name=?";
		Object[] obj = new Object[3];
		obj[0] = MD5Util.getMD5(user.getLoginPwd());
		obj[1] = user.getTimeOutDate();
		obj[2] = user.getLoginName();
		return jdbcTemplate.update(sql, obj);
	}

	/**
	 * 更新用户最近登录时间
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public long updateLoginDate(SysUserBean user) throws Exception {
		String sql = "UPDATE sys_user SET last_login_date=? WHERE login_name=?";
		Object[] obj = new Object[2];
		obj[0] = DateUtil.getcurrentDateTime();
		obj[1] = user.getLoginName();
		return jdbcTemplate.update(sql, obj);
	}
}
