package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserRegionBean;
import com.myreport.dao.SysUserRegionDao;
import com.myreport.enums.RegionTypeEnum;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:30:43
 * 类描述：
 */
@Repository
public class SysUserRegionDaoImpl implements SysUserRegionDao {

	@Override
	public int insert(SysUserRegionBean t) throws Exception {
		String sql = "INSERT INTO sys_user_region(user_id, region_id) VALUES (?, ?)";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < t.getRegionIds().length; i++) {
			list.add(new Object[] { t.getUserId(), t.getRegionIds()[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
		return 0;
	}

	@Override
	public int update(SysUserRegionBean t) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_user_region WHERE user_id=?";
		Object[] obj = new Object[1];
		obj[0] = ids[0];
		jdbcTemplate.update(sql, obj);
	}

	@Override
	public SysUserRegionBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysUserRegionBean> queryAll(SysUserRegionBean t, PageBean page, Object... o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询该用户所属的地市
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysRegionBean> queryUserRegion(SysUserBean user) throws Exception {
		String sql = "";
		Object[] obj = null;
		if (SessionUtil.isSuperManager(user.getLoginName())) {
			sql = "SELECT * FROM sys_region";
		} else {
			sql = "SELECT t2.* FROM sys_user_region t1 INNER JOIN sys_region t2 ON t1.region_id=t2.region_id WHERE t1.user_id=? AND t2.region_type=?";
			obj = new Object[2];
			obj[0] = user.getUserId();
			obj[1] = RegionTypeEnum.CITY.toString();
		}
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRegionBean.class));
	}
}
