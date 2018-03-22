package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.ReportGroupDao;
import com.myreport.enums.StatusEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * 分组信息的数据库操作<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Repository
public class ReportGroupDaoImpl implements ReportGroupDao {

	@Override
	public int insert(ReportGroupBean reportGroup) throws Exception {
		String sql = "INSERT INTO report_group (group_name, parent_group_id, sort_number, create_user_id, status) VALUES (?, ?, ?, ?, ?)";
		Object[] obj = new Object[5];
		obj[0] = reportGroup.getGroupName();
		obj[1] = reportGroup.getParentGroupId();
		obj[2] = reportGroup.getSortNumber();
		obj[3] = reportGroup.getCreateUserId();
		obj[4] = reportGroup.getStatus();
		int result = jdbcTemplate.update(sql, obj);
		return result;
	}


	@Override
	public int update(ReportGroupBean reportGroup) throws Exception {
		String sql = "UPDATE report_group SET group_name=?, sort_number=?, status=? WHERE group_id=?";
		Object[] obj = new Object[4];
		obj[0] = reportGroup.getGroupName();
		obj[1] = reportGroup.getSortNumber();
		obj[2] = reportGroup.getStatus();
		obj[3] = reportGroup.getGroupId();
		jdbcTemplate.update(sql, obj);
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM report_group WHERE group_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public List<ReportGroupBean> queryChildGroupById(Integer groupId) throws Exception {
		String sql = "SELECT * FROM report_group WHERE parent_group_id=?";
		return jdbcTemplate.query(sql, new Object[] { groupId }, BeanPropertyRowMapper.newInstance(ReportGroupBean.class));
	}
	
	@Override
	public ReportGroupBean queryObjectById(Integer id) throws Exception {
		String sql = "SELECT group_id, group_name, parent_group_id, sort_number, create_user_id, DATE_FORMAT(create_date, '%Y-%m-%d %T') create_date, status FROM report_group WHERE group_id=?";
		List<ReportGroupBean> list = jdbcTemplate.query(sql, new Object[] { id }, BeanPropertyRowMapper.newInstance(ReportGroupBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}
	
	@Override
	public List<ReportGroupBean> queryAll(ReportGroupBean reportGroup, PageBean page, Object... o) throws Exception {
		String sql = "";
		Object[] obj = null;
		SysUserBean sessionUser = (SysUserBean) o[0];
		if (SessionUtil.isSuperManager(sessionUser.getLoginName())) {
			sql = "SELECT * FROM report_group WHERE status=? ORDER BY sort_number";
			obj = new Object[1];
			obj[0] = StatusEnum.A.toString();
		} else {
			sql = "SELECT group_id FROM report_group WHERE create_user_id=? AND status=?";
			obj = new Object[2];
			obj[0] = sessionUser.getUserId();
			obj[1] = StatusEnum.A.toString();
			List<Integer> l = jdbcTemplate.queryForList(sql, obj, Integer.class);
			sql = "SELECT DISTINCT * FROM report_group WHERE status='A' AND FIND_IN_SET(group_id, getParentsGroupIdByGroupId(?))";
			sql += " UNION ";
			sql += "SELECT DISTINCT * FROM report_group WHERE status='A' AND FIND_IN_SET(group_id, getChildrenGroupIdByGroupId(?))";
			obj = new Object[2];
			obj[0] = StringUtils.join(l, ",");
			obj[1] = obj[0];
//			sql = "select distinct menu.* from sys_menu menu connect by prior menu.parent_menu_id=menu.menu_id start with menu.menu_id in (select menu_id from sys_role_menu ur where ur.role_id=?) AND menu.status=? ORDER BY menu.sort_number";
			
		}
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(ReportGroupBean.class));
	}
	
	@Override
	public List<ReportGroupBean> queryUserReportGroup(SysUserBean user) throws Exception {
		List<ReportGroupBean> list = null;
		String sql = "";
		if (SessionUtil.isSuperManager(user.getLoginName())) {
			sql = "SELECT * FROM report_group WHERE status=? ORDER BY sort_number";
			list = jdbcTemplate.query(sql, new Object[] { StatusEnum.A.toString() }, BeanPropertyRowMapper.newInstance(ReportGroupBean.class));
		} else {
			sql = "SELECT group_id FROM report_group rg";
			sql += " WHERE EXISTS (SELECT 1 FROM user_group_report_group ugrg WHERE rg.group_id=ugrg.report_group_id AND EXISTS";
			sql += " (SELECT 1 FROM sys_user_group_user sugu WHERE ugrg.user_group_id=sugu.group_id AND sugu.user_id=?))";
			List<Integer> l = jdbcTemplate.queryForList(sql, new Object[] { user.getUserId() }, Integer.class);
			sql = "SELECT DISTINCT * FROM report_group WHERE status=?";
			sql += " AND FIND_IN_SET(group_id, getParentsGroupIdByGroupId(?)) ORDER BY sort_number";
			String param = StringUtils.join(l, ",");
			list = jdbcTemplate.query(sql, new Object[] { StatusEnum.A.toString(), param }, BeanPropertyRowMapper.newInstance(ReportGroupBean.class));
		}
		return list;
	}

}

