package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupBean;
import com.myreport.dao.SysUserGroupDao;
import com.myreport.enums.StatusEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:28:15
 * 类描述：
 */
@Repository
public class SysUserGroupDaoImpl implements SysUserGroupDao {

	@Override
	public int insert(SysUserGroupBean userGroup) throws Exception {
		String sql = "INSERT INTO sys_user_group (group_name, group_info, create_user_id, status) VALUES (?, ?, ?, ?)";
		Object[] obj = new Object[4];
		obj[0] = userGroup.getGroupName();
		obj[1] = userGroup.getGroupInfo();
		obj[2] = userGroup.getCreateUserId();
		obj[3] = userGroup.getStatus();
		int result = jdbcTemplate.update(sql, obj);
		return result;
	}


	@Override
	public int update(SysUserGroupBean userGroup) throws Exception {
		String sql = "UPDATE sys_user_group SET group_name=?, group_info=?, status=? WHERE group_id=?";
		Object[] obj = new Object[4];
		obj[0] = userGroup.getGroupName();
		obj[1] = userGroup.getGroupInfo();
		obj[2] = userGroup.getStatus();
		obj[3] = userGroup.getGroupId();
		jdbcTemplate.update(sql, obj);
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_user_group WHERE group_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public SysUserGroupBean queryObjectById(Integer id) throws Exception {
		String sql = "SELECT group_id, group_name, group_info, create_user_id, DATE_FORMAT(create_date, '%Y-%m-%d %T') CREATE_DATE, status FROM sys_user_group WHERE group_id=?";
		List<SysUserGroupBean> list = jdbcTemplate.query(sql, new Object[] { id }, BeanPropertyRowMapper.newInstance(SysUserGroupBean.class));		return list == null || list.size() == 0 ? null : list.get(0);	}

	@Override
	public List<SysUserGroupBean> queryAll(SysUserGroupBean userGroup, PageBean page, Object... o) throws Exception {
		List<SysUserGroupBean> list = null;
		String sql = "SELECT userGroup.group_id, userGroup.group_name, userGroup.group_info, userGroup.create_user_id, DATE_FORMAT(userGroup.create_date, '%Y-%m-%d %T') create_date, userGroup.status, us.user_name create_user_name, "+ StatusEnum.toSql("userGroup.status") +" status_name FROM sys_user_group userGroup " +
				"LEFT JOIN sys_user us ON userGroup.create_user_id=us.user_id " +
				"WHERE 1=1";
		List<Object> l = new ArrayList<Object>();
		if (o != null && o.length > 0) {
			SysUserBean sessionUser = (SysUserBean) o[0];
			if (!SessionUtil.isSuperManager(sessionUser.getLoginName())) {
				sql += " AND EXISTS (SELECT * FROM sys_user u WHERE u.user_id=userGroup.create_user_id AND FIND_IN_SET(u.user_id, getChildUserIdBy(?)))";
				//sql += " AND EXISTS (SELECT * FROM sys_user u WHERE u.user_id=userGroup.create_user_id START WITH u.user_id=? CONNECT BY PRIOR u.user_id=u.create_user_id)";
				l.add(sessionUser.getUserId());
			}
		}
		if (userGroup != null) {
			if (!StringUtils.isEmpty(userGroup.getGroupName())) {
				sql += " AND INSTR(userGroup.group_name, ?) > 0";
				l.add(userGroup.getGroupName());
			}
			if (userGroup.getCreateUserId() != null && userGroup.getCreateUserId() != 0) {
				sql += " AND userGroup.create_user_id=?";
				l.add(userGroup.getCreateUserId());
			}
			if (!StringUtils.isEmpty(userGroup.getQueryStartDate())) {
				sql += " AND userGroup.create_date>=STR_TO_DATE(?, '%Y-%m-%d %T')";
				l.add(userGroup.getQueryStartDate());
			}
			if (!StringUtils.isEmpty(userGroup.getQueryEndDate())) {
				sql += " AND userGroup.create_date<=STR_TO_DATE(?, '%Y-%m-%d %T')";
				l.add(userGroup.getQueryEndDate());
			}
			if (!StringUtils.isEmpty(userGroup.getStatus())) {
				sql += " AND userGroup.status=?";
				l.add(userGroup.getStatus());
			}
		}
		sql += " ORDER BY userGroup.group_id";
		Object[] obj = l.toArray();
		if (page != null && !page.isEmpty()) {
			page.setSql(sql);
			page.setParams(obj);
			page.setTotalCount(jdbcTemplate.queryForObject(mysqlDatabaseAbst.getTotalCountSql(sql), obj, Integer.class).intValue());
			list = jdbcTemplate.query(mysqlDatabaseAbst.getPageSQL(page), obj, BeanPropertyRowMapper.newInstance(SysUserGroupBean.class));
		} else {
			list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysUserGroupBean.class));
		}
		return list;
	}
}
