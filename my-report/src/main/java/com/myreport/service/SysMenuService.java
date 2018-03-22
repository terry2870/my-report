package com.myreport.service;

import java.util.List;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysMenuBean;
import com.myreport.bean.SysUserBean;

/**
 * @author huangping
 * 2014-03-11
 */
public interface SysMenuService {

	/**
	 * 按照权限，查询菜单
	 * @param sessionUser
	 * @return
	 * @throws Exception
	 */
	public List<SysMenuBean> queryAll(SysUserBean sessionUser) throws Exception;
	
	/**
	 * 新增或修改
	 * @param opera
	 * @param returnInfo
	 * @param menu
	 * @throws Exception
	 */
	public void editMenu(OperaBean opera, ReturnInfoBean returnInfo, SysMenuBean menu) throws Exception;
	
	/**
	 * 删除
	 * @param opera
	 * @param returnInfo
	 * @param menuIds
	 * @throws Exception
	 */
	public void delete(OperaBean opera, ReturnInfoBean returnInfo, String[] menuIds) throws Exception;
	
}

