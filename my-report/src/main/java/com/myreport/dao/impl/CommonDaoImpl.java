package com.myreport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.myreport.dao.CommonDao;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:33:08
 * 类描述：
 */
@Repository
public class CommonDaoImpl implements CommonDao {

	/**
	 * 获取数据
	 * @param sql
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryDataList(String sql, Object[] obj) throws Exception {
		return queryDataList(sql, obj, null);
	}
	
	/**
	 * 获取数据
	 * @param sql
	 * @param obj
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryDataList(String sql, Object[] obj, PageBean page) throws Exception {
		List<Map<String, Object>> list = null;
		if (page != null) {
			page.setSql(sql);
			page.setParams(obj);
			page.setTotalCount(jdbcTemplate.queryForObject(mysqlDatabaseAbst.getTotalCountSql(sql), obj, Integer.class));
			list = jdbcTemplate.queryForList(mysqlDatabaseAbst.getPageSQL(page), obj);
		} else {
			list = jdbcTemplate.queryForList(sql, obj);
		}
		return list;
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
	 * @see com.myreport.dao.BaseDAO#getObjectById(java.lang.Integer)
	 */
	@Override
	public Object queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.myreport.dao.BaseDAO#getAll(java.lang.Object, com.mytools.beans.PageBean, java.lang.Object[])
	 */
	@Override
	public List<Object> queryAll(Object t, PageBean page, Object... o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
