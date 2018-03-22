package com.myreport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.myreport.bean.DatabaseInfoBean;
import com.myreport.constants.Constant;
import com.myreport.dao.QueryReportDao;
import com.myreport.utils.DatabaseUtils;
import com.mytools.beans.PageBean;
import com.mytools.database.DatabaseAbst;
import com.mytools.enums.DatabaseTypeEnum;
import com.mytools.utils.SpringContextUtil;

/**
 * 
 * @author huangping<br />
 * 2013-9-22
 */
@Repository
public class QueryReportDaoImpl implements QueryReportDao {
	
	@Override
	public List<Map<String, Object>> queryDataListForReport(String sql, Object[] obj, PageBean page, Integer databaseId, String databaseType) throws Exception {
		List<Map<String, Object>> list = null;
		JdbcTemplate jdbc = SpringContextUtil.getBean(Constant.JDBCTEMPLATE_FOR_REPORT + databaseId, JdbcTemplate.class);
		DatabaseAbst abst = DatabaseUtils.getDatabaseAbst(databaseType);
		if (page != null) {
			page.setSql(sql);
			page.setParams(obj);
			if (obj == null) {
				page.setTotalCount(jdbc.queryForObject(abst.getTotalCountSql(sql), Integer.class));
				list = jdbc.queryForList(abst.getPageSQL(page));
			} else {
				page.setTotalCount(jdbc.queryForObject(abst.getTotalCountSql(sql), obj, Integer.class));
				list = jdbc.queryForList(abst.getPageSQL(page), obj);
			}
		} else {
			if (obj == null) {
				list = jdbc.queryForList(sql);
			} else {
				list = jdbc.queryForList(sql, obj);
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.myreport.dao.QueryReportDao#checkSql(java.lang.String, com.myreport.bean.DatabaseInfoBean)
	 */
	@Override
	public void checkSql(String sql, DatabaseInfoBean db) throws Exception {
		if (DatabaseTypeEnum.MYSQL.toString().equalsIgnoreCase(db.getDatabaseType())) {
			sql = sql.replace("?", "1");
			sql = "EXPLAIN " + sql;
		} else if (DatabaseTypeEnum.ORACLE.toString().equalsIgnoreCase(db.getDatabaseType())) {
			sql = sql.replace("?", ":1");
			sql = "EXPLAIN PLAN SET STATEMENT_ID='"+ sql.hashCode() +"' FOR " + sql;
		}
		JdbcTemplate jdbc = SpringContextUtil.getBean(Constant.JDBCTEMPLATE_FOR_REPORT + db.getDatabaseId(), JdbcTemplate.class);
		jdbc.execute(sql);
	}

	/* (non-Javadoc)
	 * @see com.myreport.dao.BaseDAO#insert(java.lang.Object)
	 */
	@Override
	public int insert(Object t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.myreport.dao.BaseDAO#update(java.lang.Object)
	 */
	@Override
	public int update(Object t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.myreport.dao.BaseDAO#delete(java.lang.String[])
	 */
	@Override
	public void delete(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.myreport.dao.BaseDAO#queryObjectById(java.lang.Integer)
	 */
	@Override
	public Object queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.myreport.dao.BaseDAO#queryAll(java.lang.Object, com.mytools.beans.PageBean, java.lang.Object[])
	 */
	@Override
	public List<Object> queryAll(Object t, PageBean page, Object... o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}


