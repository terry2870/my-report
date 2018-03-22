package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;

/**
 * 
 * @author huangping<br />
 * 2013-1-16
 */
public interface SysRegionDao extends BaseDAO<SysRegionBean> {

	/**
	 * 批量添加
	 * @param list
	 * @throws Exception
	 */
	public void batchAdd(List<SysRegionBean> list) throws Exception;
	
	/**
	 * 根据地区编号，查询地区信息
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	public SysRegionBean queryRegionByRegionId(String regionId) throws Exception;
	
	/**
	 * 根据父节点编号，查询子节点（不递归）
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<SysRegionBean> queryRegionInfoByParentRegionId(long parentRegionId, SysUserBean user) throws Exception;

}


