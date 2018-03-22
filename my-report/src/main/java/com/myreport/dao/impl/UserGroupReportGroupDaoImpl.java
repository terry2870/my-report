package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.UserGroupReportGroupBean;
import com.myreport.dao.UserGroupReportGroupDao;
import com.mytools.beans.PageBean;

/**
 * 用户组与报表组关系的dao
 * @author ping.huang
 *
 */
@Repository
public class UserGroupReportGroupDaoImpl implements UserGroupReportGroupDao {

	@Override
	public int insert(UserGroupReportGroupBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(UserGroupReportGroupBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserGroupReportGroupBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserGroupReportGroupBean> queryAll(UserGroupReportGroupBean t, PageBean page, Object... o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void batchInsert(List<UserGroupReportGroupBean> list) throws Exception {
		String sql = "INSERT INTO user_group_report_group (user_group_id, report_group_id) VALUES (?, ?)";
		List<Object[]> l = new ArrayList<Object[]>();
		for (UserGroupReportGroupBean bean : list) {
			l.add(new Object[] { bean.getUserGroupId(), bean.getReportGroupId() });
		}
		jdbcTemplate.batchUpdate(sql, l);
	}
	
	@Override
	public void deleteUserGroupReportGroupByUserGroupId(Integer userGroupId) throws Exception {
		String sql = "DELETE FROM user_group_report_group WHERE user_group_id=?";
		jdbcTemplate.update(sql, userGroupId);
	}
	
	@Override
	public List<UserGroupReportGroupBean> queryUserGroupByReportGroupId(Integer reportGroupId) throws Exception {
		String sql = "SELECT * FROM user_group_report_group WHERE report_group_id=?";
		return jdbcTemplate.query(sql, new Object[] { reportGroupId }, BeanPropertyRowMapper.newInstance(UserGroupReportGroupBean.class));
	}
	
	@Override
	public List<UserGroupReportGroupBean> queryReportGroupByUserGroupId(Integer userGroupId) throws Exception {
		String sql = "SELECT * FROM user_group_report_group WHERE user_group_id=?";
		return jdbcTemplate.query(sql, new Object[] { userGroupId }, BeanPropertyRowMapper.newInstance(UserGroupReportGroupBean.class));
	}

}
