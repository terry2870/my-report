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

import com.myreport.bean.ReportInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.ReportInfoDao;
import com.myreport.enums.StatusEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * 报表的数据库操作<br />
 * 
 * @author huangping <br />
 *         创建日期 2014-04-02
 */
@Repository
public class ReportInfoDaoImpl implements ReportInfoDao {

	@Override
	public int insert(final ReportInfoBean reportInfo) throws Exception {
		final String sql = "INSERT INTO report_info (report_name, original_sql, execute_sql, query_conditions, status, create_user_id, group_id, database_id, sort_number, param_keys, table_params) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, reportInfo.getReportName());
				ps.setObject(2, reportInfo.getOriginalSql());
				ps.setObject(3, reportInfo.getExecuteSql());
				ps.setObject(4, reportInfo.getQueryConditions());
				ps.setObject(5, reportInfo.getStatus());
				ps.setObject(6, reportInfo.getCreateUserId());
				ps.setObject(7, reportInfo.getGroupId());
				ps.setObject(8, reportInfo.getDatabaseId());
				ps.setObject(9, reportInfo.getSortNumber());
				ps.setObject(10, reportInfo.getParamKeys());
				ps.setObject(11, reportInfo.getTableParams());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public int update(ReportInfoBean reportInfo) throws Exception {
		String sql = "UPDATE report_info SET report_name=?, original_sql=?, execute_sql=?, query_conditions=?, status=?, group_id=?, sort_number=?, param_keys=?, table_params=?, database_id=? WHERE report_id=?";
		Object[] obj = new Object[11];
		obj[0] = reportInfo.getReportName();
		obj[1] = reportInfo.getOriginalSql();
		obj[2] = reportInfo.getExecuteSql();
		obj[3] = reportInfo.getQueryConditions();
		obj[4] = reportInfo.getStatus();
		obj[5] = reportInfo.getGroupId();
		obj[6] = reportInfo.getSortNumber();
		obj[7] = reportInfo.getParamKeys();
		obj[8] = reportInfo.getTableParams();
		obj[9] = reportInfo.getDatabaseId();
		obj[10] = reportInfo.getReportId();
		jdbcTemplate.update(sql, obj);
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM report_info WHERE report_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public ReportInfoBean queryObjectById(Integer id) throws Exception {
		String sql = "SELECT report_id, report_name, original_sql, execute_sql, query_conditions, status, create_user_id, DATE_FORMAT(create_date, '%Y-%m-%d %T') create_date, group_id, database_id, sort_number, param_keys, table_params FROM report_info WHERE report_id=?";
		List<ReportInfoBean> list = jdbcTemplate.query(sql, new Object[] { id }, BeanPropertyRowMapper.newInstance(ReportInfoBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	@Override
	public List<ReportInfoBean> queryReportByReportGroupId(Integer reportGroupId) throws Exception {
		String sql = "SELECT * FROM report_info WHERE group_id=? AND status=? ORDER BY sort_number";
		return jdbcTemplate.query(sql, new Object[] { reportGroupId, StatusEnum.A.toString() }, BeanPropertyRowMapper.newInstance(ReportInfoBean.class));
	}

	@Override
	public List<ReportInfoBean> queryReportByDatabaseId(Integer databaseId) throws Exception {
		String sql = "SELECT * FROM report_info WHERE database_id=? ORDER BY sort_number";
		return jdbcTemplate.query(sql, new Object[] { databaseId }, BeanPropertyRowMapper.newInstance(ReportInfoBean.class));
	}

	@Override
	public List<ReportInfoBean> queryAll(ReportInfoBean reportInfo, PageBean page, Object... o) throws Exception {
		List<ReportInfoBean> list = null;
		String sql = "SELECT reportInfo.report_id, reportInfo.report_name, reportInfo.original_sql, reportInfo.execute_sql, reportInfo.query_conditions, reportInfo.status, reportInfo.create_user_id, DATE_FORMAT(reportInfo.create_date, '%Y-%m-%d %T') create_date, reportInfo.group_id, reportInfo.database_id, us.user_name create_user_name, " + StatusEnum.toSql("reportInfo.status") + " status_name, reportInfo.sort_number, reportInfo.param_keys, reportInfo.table_params FROM report_info reportInfo " + "LEFT JOIN sys_user us ON reportInfo.create_user_id=us.user_id " + "WHERE 1=1";
		List<Object> l = new ArrayList<Object>();
		if (o != null && o.length > 0) {
			SysUserBean sessionUser = (SysUserBean) o[0];
			if (!SessionUtil.isSuperManager(sessionUser.getLoginName())) {
				sql += " AND EXISTS (SELECT * FROM sys_user u WHERE u.user_id=reportInfo.create_user_id AND FIND_IN_SET(u.user_id, getChildUserIdBy(?)))";
				l.add(sessionUser.getUserId());
			}
		}
		if (reportInfo != null) {
			if (reportInfo.getReportId() != null) {
				sql += " AND reportInfo.report_id=?";
				l.add(reportInfo.getReportId().intValue());
			}
			if (!StringUtils.isEmpty(reportInfo.getReportName())) {
				sql += " AND INSTR(reportInfo.report_name, ?) > 0";
				l.add(reportInfo.getReportName());
			}
			if (!StringUtils.isEmpty(reportInfo.getStatus())) {
				sql += " AND reportInfo.status=?";
				l.add(reportInfo.getStatus());
			}
			if (!StringUtils.isEmpty(reportInfo.getQueryStartDate())) {
				sql += " AND reportInfo.create_date>=?";
				l.add(reportInfo.getQueryStartDate());
			}
			if (!StringUtils.isEmpty(reportInfo.getQueryEndDate())) {
				sql += " AND reportInfo.create_date<=?";
				l.add(reportInfo.getQueryEndDate());
			}
		}
		sql += " ORDER BY reportInfo.sort_number";
		Object[] obj = l.toArray();
		if (page != null && !page.isEmpty()) {
			page.setSql(sql);
			page.setParams(obj);
			page.setTotalCount(jdbcTemplate.queryForObject(mysqlDatabaseAbst.getTotalCountSql(sql), obj, Integer.class).intValue());
			list = jdbcTemplate.query(mysqlDatabaseAbst.getPageSQL(page), obj, BeanPropertyRowMapper.newInstance(ReportInfoBean.class));
		} else {
			list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(ReportInfoBean.class));
		}
		return list;
	}

}
