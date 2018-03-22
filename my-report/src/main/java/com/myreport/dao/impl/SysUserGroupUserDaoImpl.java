package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupUserBean;
import com.myreport.dao.SysUserGroupUserDao;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:29:09
 * 类描述：
 */
@Repository
public class SysUserGroupUserDaoImpl implements SysUserGroupUserDao {

	@Override
	public int insert(SysUserGroupUserBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 批量添加用户与用户组关系
	 * @param list
	 * @throws Exception
	 */
	@Override
	public void batchInsert(List<SysUserGroupUserBean> list) throws Exception {
		String sql = "INSERT INTO sys_user_group_user (user_id, group_id) VALUES (?, ?)";
		List<Object[]> l = new ArrayList<Object[]>();
		for (SysUserGroupUserBean bean : list) {
			l.add(new Object[] { bean.getUserId(), bean.getGroupId() });
		}
		jdbcTemplate.batchUpdate(sql, l);
	}

	@Override
	public int update(SysUserGroupUserBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 根据组ID，删除用户与用户组关系
	 * @param groupId
	 * @throws Exception
	 */
	@Override
	public void deleteFromGroupId(Integer groupId) throws Exception {
		String sql = "DELETE FROM sys_user_group_user WHERE group_id=?";
		jdbcTemplate.update(sql, new Object[] { groupId });
	}

	@Override
	public SysUserGroupUserBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询该用户组下面的所有用户
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysUserBean> queryUserByGroupId(Integer groupId) throws Exception {
		String sql = "SELECT u.*";
		sql += " FROM sys_user_group_user g";
		sql += " LEFT JOIN sys_user u ON g.user_id=u.user_id";
		sql += " WHERE g.group_id=? ORDER BY g.user_id";
		return jdbcTemplate.query(sql, new Object[] { groupId }, BeanPropertyRowMapper.newInstance(SysUserBean.class));
	}
	
	/**
	 * 查询可以供该用户组分配的用户
	 * @param groupId
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysUserBean> queryForSelectUserByGroupId(Integer groupId, SysUserBean sessionUser) throws Exception {
		String sql = "SELECT u.* FROM sys_user u WHERE 1=1";
		List<Object> l = new ArrayList<Object>();
		sql += " AND NOT EXISTS (SELECT * FROM sys_user_group_user ugu WHERE group_id=? AND ugu.user_id=u.user_id)";
		l.add(groupId);
		if (!SessionUtil.isSuperManager(sessionUser.getLoginName())) {
			sql += " AND EXISTS (SELECT * FROM sys_user u2 WHERE u.create_user_id=u2.user_id AND FIND_IN_SET(u2.user_id, getChildUserIdBy(?)))";
			// sql += " START WITH u.user_id=? CONNECT BY PRIOR u.user_id=u.create_user_id";
			l.add(sessionUser.getUserId());
		}
		return jdbcTemplate.query(sql, l.toArray(), BeanPropertyRowMapper.newInstance(SysUserBean.class));
	}
	
	@Override
	public List<SysUserGroupUserBean> queryAll(SysUserGroupUserBean t, PageBean page, Object... o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
