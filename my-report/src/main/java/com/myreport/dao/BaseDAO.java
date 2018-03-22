package com.myreport.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mytools.beans.PageBean;
import com.mytools.database.DatabaseAbst;
import com.mytools.utils.SpringContextUtil;

/**
 * dao基础的接口
 * @author huangping<br />
 * 2014-02-14
 */
public abstract interface BaseDAO<T> {

	JdbcTemplate jdbcTemplate = SpringContextUtil.getBean("jdbcTemplate", JdbcTemplate.class);
	DatabaseAbst oracleDatabaseAbst = SpringContextUtil.getBean("oracleDatabaseAbst", DatabaseAbst.class);
	DatabaseAbst mysqlDatabaseAbst = SpringContextUtil.getBean("mysqlDatabaseAbst", DatabaseAbst.class);
	
	/**
	 * 新增数据
	 * @param t 数据对象
	 * @return 返回新增的行数
	 * @throws Exception
	 */
	public abstract int insert(T t) throws Exception;
	
	/**
	 * 修改数据
	 * @param t 数据对象
	 * @return 返回修改的行数
	 * @throws Exception
	 */
	public abstract int update(T t) throws Exception;
	
	/**
	 * 根据主键删除数据
	 * @param ids 主键数组
	 * @throws Exception
	 */
	public abstract void delete(String[] ids) throws Exception;
	
	/**
	 * 根据主键，查询对象详细信息
	 * @param id 主键ID
	 * @return 返回查询对象
	 * @throws Exception
	 */
	public abstract T queryObjectById(Integer id) throws Exception;
	
	/**
	 * 根据条件，查询列表(如果page==null则不分页)
	 * @param t 查询条件
	 * @param page 分页对象
	 * @param o 额外信息数组
	 * @return 查询数据
	 * @throws Exception
	 */
	public abstract List<T> queryAll(T t, PageBean page, Object... o) throws Exception;

}


