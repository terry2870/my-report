package com.myreport.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.myreport.bean.SysUserActionLogBean;
import com.myreport.dao.SysUserActionLogDao;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:25:31
 * 类描述：
 */
@Repository
public class SysUserActionLogDaoImpl implements SysUserActionLogDao {

	@Override
	public int insert(SysUserActionLogBean t) throws Exception {
		String sql = "INSERT INTO sys_user_action_log (user_id, user_ip, log_type, log_info, log_class) VALUES (?, ?, ?, ?, ?)";
		Object[] obj = new Object[5];
		obj[0] = t.getUserId();
		obj[1] = t.getUserIp();
		obj[2] = t.getLogType();
		obj[3] = t.getLogInfo();
		obj[4] = t.getLogClass();
		int[] type = new int[5];
		type[0] = Types.INTEGER;
		type[1] = Types.VARCHAR;
		type[2] = Types.VARCHAR;
		type[3] = Types.LONGVARCHAR;
		type[4] = Types.VARCHAR;
		return jdbcTemplate.update(sql, obj, type);
	}

	@Override
	public int update(SysUserActionLogBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public SysUserActionLogBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysUserActionLogBean> queryAll(SysUserActionLogBean t, PageBean page, Object... o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
