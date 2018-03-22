package com.myreport.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysRegionDao;
import com.myreport.utils.SessionUtil;
import com.mytools.beans.PageBean;

/**
 * @author ping.huang
 * 创建时间 ：2014年12月25日 上午10:20:47
 * 类描述：
 */
@Repository
public class SysRegionDaoImpl implements SysRegionDao {

	@Override
	public int insert(SysRegionBean t) throws Exception {
		String sql = "INSERT INTO sys_region (region_name, region_code, region_type, parent_region_id, sort_number) VALUES (?, ?, ?, ?, ?)";
		Object[] obj = new Object[5];
		obj[0] = t.getRegionName();
		obj[1] = t.getRegionCode();
		obj[2] = t.getRegionType();
		obj[3] = t.getParentRegionId();
		obj[4] = t.getSortNumber();
		jdbcTemplate.update(sql, obj);
		return 0;
	}

	@Override
	public int update(SysRegionBean t) throws Exception {
		String sql = "UPDATE sys_region SET region_name=?, region_code=?, region_type=?, sort_number=? WHERE region_id=?";
		Object[] obj = new Object[5];
		obj[0] = t.getRegionName();
		obj[1] = t.getRegionCode();
		obj[2] = t.getRegionType();
		obj[3] = t.getSortNumber();
		obj[4] = t.getRegionId();
		jdbcTemplate.update(sql, obj);
		return 0;
	}

	@Override
	public void delete(String[] ids) throws Exception {
		String sql = "DELETE FROM sys_region WHERE region_id=?";
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			list.add(new Object[] { ids[i] });
		}
		jdbcTemplate.batchUpdate(sql, list);
	}

	@Override
	public SysRegionBean queryObjectById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysRegionBean> queryAll(SysRegionBean t, PageBean page, Object... o) throws Exception {
		String sql = "";
		Object[] obj = null;
		SysUserBean user = new SysUserBean();
		if (o != null && o.length > 0) {
			user = (SysUserBean) o[0];
		}
		if (SessionUtil.isSuperManager(user.getLoginName())) {
			sql = "SELECT * FROM sys_region";
		} else {
			sql = "SELECT region_id FROM sys_user_region WHERE user_id=?";
			obj = new Object[1];
			obj[0] = user.getUserId();
			List<Integer> l = jdbcTemplate.queryForList(sql, obj, Integer.class);
			String regionIds = StringUtils.join(l, ",");
			sql = "SELECT * FROM sys_region WHERE FIND_IN_SET(region_id, getParentsRegionIdByRegionId(?))";
			//sql = "SELECT DISTINCT r.* FROM sys_region r connect by prior r.parent_region_id=r.region_id start with r.region_id in (select region_id from sys_user_region ur where ur.user_id=?)";
			obj = new Object[1];
			obj[0] = regionIds;
		}
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRegionBean.class));
	}
	
	/**
	 * 批量添加
	 * @param list
	 * @throws Exception
	 */
	@Override
	public void batchAdd(List<SysRegionBean> list) throws Exception {
		String sql = "INSERT INTO sys_region (region_name, region_code, region_type, parent_region_id) VALUES (?, ?, ?, ?)";
		List<Object[]> l = new ArrayList<Object[]>();
		for (SysRegionBean region : list) {
			l.add(new Object[] { region.getRegionName(),  region.getRegionCode(), region.getRegionType(), region.getParentRegionId()});
		}
		jdbcTemplate.batchUpdate(sql, l);
	}
	
	/**
	 * 根据地区编号，查询地区信息
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	@Override
	public SysRegionBean queryRegionByRegionId(String regionId) throws Exception {
		String sql = "SELECT * FROM sys_region WHERE region_code=?";
		Object[] obj = new Object[1];
		obj[0] = regionId;
		List<SysRegionBean> list = jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRegionBean.class));
		return list == null || list.size() == 0 ? null : list.get(0);
	}
	
	/**
	 * 根据父节点编号，查询子节点（不递归）
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysRegionBean> queryRegionInfoByParentRegionId(long parentRegionId, SysUserBean user) throws Exception {
		String sql = "";
		Object[] obj = null;
		if (SessionUtil.isSuperManager(user.getLoginName())) {
			sql = "SELECT * FROM sys_region WHERE parent_region_id=?";
			obj = new Object[1];
			obj[0] = parentRegionId;
		} else {
			sql = "SELECT t2.* FROM sys_user_region t1 INNER JOIN sys_region t2 ON t1.region_id=t2.region_id WHERE t2.parent_region_id=? AND t1.user_id=?";
			obj = new Object[2];
			obj[0] = parentRegionId;
			obj[1] = user.getUserId();
		}
		return jdbcTemplate.query(sql, obj, BeanPropertyRowMapper.newInstance(SysRegionBean.class));
	}
}
