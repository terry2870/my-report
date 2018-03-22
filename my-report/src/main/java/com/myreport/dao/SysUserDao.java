package com.myreport.dao;

import java.util.List;

import com.myreport.bean.SysUserBean;

/**
 * @author huangping 2014-02-14
 */
public interface SysUserDao extends BaseDAO<SysUserBean> {

	/**
	 * 查询该角色下的所有用户
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<SysUserBean> queryUserByRoleId(Integer roleId) throws Exception;
	
	/**
	 * 根据登录名，查询用户
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public SysUserBean queryUserByLoginName(String loginName) throws Exception;
	
	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public SysUserBean login(SysUserBean user) throws Exception;
	
	/**
	 * 修改密码
	 * 
	 * @param user
	 *            用户信息
	 * @return 返回修改的行数
	 * @throws Exception
	 */
	public long modifyPwd(SysUserBean user) throws Exception;
	
	/**
	 * 更新用户最近登录时间
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public long updateLoginDate(SysUserBean user) throws Exception;

}
