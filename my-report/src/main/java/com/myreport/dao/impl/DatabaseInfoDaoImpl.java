package com.myreport.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.myreport.bean.DatabaseInfoBean;
import com.myreport.dao.DatabaseInfoDao;
import com.myreport.enums.StatusEnum;
import com.mytools.beans.PageBean;
import com.mytools.database.DatabaseAbst;
import com.mytools.enums.DatabaseTypeEnum;

/**
 * 数据库表信息的数据库操作<br />
 * @author huangping <br />
 * 创建日期 2014-04-02
 */
@Repository
public class DatabaseInfoDaoImpl implements DatabaseInfoDao {

	Logger log = Logger.getLogger(getClass());
	
	@Override
	public int insert(final DatabaseInfoBean databaseInfo) throws Exception {
		final String sql = "INSERT INTO database_info (database_name, database_type, user_name, password, create_user_id, status, database_ip, database_port, database_title) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, databaseInfo.getDatabaseName());
				ps.setObject(2, databaseInfo.getDatabaseType());
				ps.setObject(3, databaseInfo.getUserName());
				ps.setObject(4, databaseInfo.getPassword());
				ps.setObject(5, databaseInfo.getCreateUserId());
				ps.setObject(6, databaseInfo.getStatus());
				ps.setObject(7, databaseInfo.getDatabaseIp());
				ps.setObject(8, databaseInfo.getDatabasePort());
				ps.setObject(9, databaseInfo.getDatabaseTitle());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}


	@Override
	public int update(DatabaseInfoBean databaseInfo) throws Exception {
		String sql = "UPDATE database_info SET database_name=?, database_type=?, user_name=?, password=?, status=?, database_ip=?, database_port=?, database_title=? WHERE database_id=?";
		Object[] obj = new Object[9];
		obj[0] = databaseInfo.getDatabaseName();
		obj[1] = databaseInfo.getDatabaseType();
		obj[2] = databaseInfo.getUserName();
		obj[3] = databaseInfo.getPassword();
		obj[4] = databaseInfo.getStatus();
		obj[5] = databaseInfo.getDatabaseIp();
		obj[6] = databaseInfo.getDatabasePort();
		obj[7] = databaseInfo.getDatabaseTitle();
		obj[8] = databaseInfo.getDatabaseId();
		jdbcTemplate.update(sql, obj);
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM database_info WHERE database_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public DatabaseInfoBean queryObjectById(Integer id) throws Exception {
		String sql = "SELECT database_id, database_name, database_type, user_name, password, create_user_id, DATE_FORMAT(create_date, '%Y-%m-%d %T') create_date, status, database_ip, database_port, database_title FROM database_info WHERE database_id=?";
		List<DatabaseInfoBean> list = jdbcTemplate.query(sql, new Object[] { id }, BeanPropertyRowMapper.newInstance(DatabaseInfoBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}

	@Override
	public List<DatabaseInfoBean> queryAll(DatabaseInfoBean databaseInfo, PageBean page, Object... o) throws Exception {
		List<DatabaseInfoBean> list = null;
		String sql = "SELECT databaseInfo.database_id, databaseInfo.database_name, databaseInfo.database_type, databaseInfo.user_name, databaseInfo.password, databaseInfo.create_user_id, DATE_FORMAT(databaseInfo.create_date, '%Y-%m-%d %T') create_date, databaseInfo.status, "+ StatusEnum.toSql("databaseInfo.status") +" status_name, us.user_name create_user_name, databaseInfo.database_ip, databaseInfo.database_port, databaseInfo.database_title FROM database_info databaseInfo " +
				"LEFT JOIN sys_user us ON databaseInfo.create_user_id=us.user_id " +
				"WHERE 1=1";
		List<Object> l = new ArrayList<Object>();
//		if (o != null && o.length > 0) {
//			SysUserBean sessionUser = (SysUserBean) o[0];
//			if (!SessionUtil.isSuperManager(sessionUser.getLoginName())) {
//				sql += " AND EXISTS (SELECT * FROM sys_user u WHERE u.user_id=databaseInfo.create_user_id AND FIND_IN_SET(u.user_id, getChildUserIdBy(?)))";
//				l.add(sessionUser.getUserId());
//			}
//		}
		if (databaseInfo != null) {
			if (!StringUtils.isEmpty(databaseInfo.getDatabaseType())) {
				sql += " AND databaseInfo.database_type=?";
				l.add(databaseInfo.getDatabaseType());
			}
			if (!StringUtils.isEmpty(databaseInfo.getDatabaseTitle())) {
				sql += " AND INSTR(databaseInfo.database_title, ?) > 0";
				l.add(databaseInfo.getDatabaseTitle());
			}
			if (!StringUtils.isEmpty(databaseInfo.getQueryStartDate())) {
				sql += " AND DATE_FORMAT(databaseInfo.create_date, '%Y-%m-%d')>=?";
				l.add(databaseInfo.getQueryStartDate());
			}
			if (!StringUtils.isEmpty(databaseInfo.getQueryEndDate())) {
				sql += " AND DATE_FORMAT(databaseInfo.create_date, '%Y-%m-%d')<=?";
				l.add(databaseInfo.getQueryEndDate());
			}
		}
		sql += " ORDER BY databaseInfo.database_id";
		Object[] obj = l.toArray();
		if (page != null && !page.isEmpty()) {
			page.setSql(sql);
			page.setParams(obj);
			page.setTotalCount(jdbcTemplate.queryForObject(mysqlDatabaseAbst.getTotalCountSql(sql), obj, Integer.class).intValue());
			list = jdbcTemplate.query(mysqlDatabaseAbst.getPageSQL(page), obj, BeanPropertyRowMapper.newInstance(DatabaseInfoBean.class));
		} else {
			list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(DatabaseInfoBean.class));
		}
		return list;
	}
	
	@Override
	public boolean checkConnect(DatabaseInfoBean bean) {
		DatabaseAbst abst = DatabaseTypeEnum.getDatabaseAbst(bean.getDatabaseType());
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stat = null;
		boolean result = false;
		try {
			Class.forName(abst.getDriverClassName());
			conn = DriverManager.getConnection(abst.getConnectUrl(bean.getDatabaseIp(), bean.getDatabasePort(), bean.getDatabaseName()), bean.getUserName(), bean.getPassword());
			stat = conn.prepareStatement(abst.getCheckSql());
			rs = stat.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (Exception e) {
			log.error("测试数据库失败["+ bean +"]", e);
			result = false;
		} finally {
			try {
				if (rs != null) {
					rs.getStatement().close();
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
		return result;
	}
	

}

