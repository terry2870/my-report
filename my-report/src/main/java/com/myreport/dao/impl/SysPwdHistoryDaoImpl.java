package com.myreport.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysPwdHistoryBean;
import com.myreport.configs.ConfigFactory;
import com.myreport.dao.SysPwdHistoryDao;
import com.mytools.beans.PageBean;
import com.mytools.utils.MD5Util;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:17:45
 * 类描述：
 */
@Repository
public class SysPwdHistoryDaoImpl implements SysPwdHistoryDao {

	@Override
	public void delete(String[] ids) throws Exception {
	}

	@Override
	public SysPwdHistoryBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询该用户的历史密码
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysPwdHistoryBean> queryPwdHistory(long userId) throws Exception {
		String sql = "SELECT * FROM sys_pwd_history WHERE user_id=? AND pwd_create_date >= DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -"+ ConfigFactory.getSysConfig().getPwdCheckMaxDate() +" DAY) ORDER BY pwd_create_date DESC";
		Object[] obj = new Object[1];
		obj[0] = userId;
		PageBean page = new PageBean();
		page.setSql(sql);
		page.setParams(obj);
		page.setPageSize(ConfigFactory.getSysConfig().getPwdRecordNum());
		page.setCurrentPage(1);
		return jdbcTemplate.query(mysqlDatabaseAbst.getPageSQL(page), obj, BeanPropertyRowMapper.newInstance(SysPwdHistoryBean.class));
	}

	@Override
	public int insert(SysPwdHistoryBean t) throws Exception {
		String sql = "INSERT INTO sys_pwd_history(login_pwd, user_id, pwd_create_date) VALUES (?, ?, CURRENT_TIMESTAMP)";
		Object[] obj = new Object[2];
		obj[0] = MD5Util.getMD5(t.getLoginPwd());
		obj[1] = t.getUserId();
		jdbcTemplate.update(sql, obj);
		return 0;
	}
	
	@Override
	public int update(SysPwdHistoryBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SysPwdHistoryBean> queryAll(SysPwdHistoryBean t, PageBean page, Object...o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
