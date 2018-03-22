package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRoleBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysRoleDao;
import com.myreport.dao.SysRoleMenuDao;
import com.myreport.dao.SysUserDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysRoleService;
import com.mytools.beans.PageBean;

/**
 * @author huangping
 * 2014-02-14
 */
@Service
public class SysRoleImpl implements SysRoleService {

	@Resource
	SysRoleDao sysRoleDao;
	
	@Resource
	SysUserDao sysUserDao;
	
	@Resource
	SysRoleMenuDao sysRoleMenuDao;

	@Override
	public List<SysRoleBean> queryAll(SysRoleBean t, PageBean page, SysUserBean sessionUser) throws Exception {
		return sysRoleDao.queryAll(t, page, sessionUser);
	}

	@Override
	public void deleteRoles(OperaBean opera, ReturnInfoBean returnInfo, String[] roleIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		Integer roleId = 0;
		String[] obj = null;
		for (String r : roleIds) {
			roleId = new Integer(r);
			List<SysUserBean> list = sysUserDao.queryUserByRoleId(roleId);
			if (list != null && list.size() > 0) {
				obj = new String[] { sysRoleDao.queryObjectById(roleId).getRoleName() };
				returnInfo.setReturnCode(ReturnCodeEnum.ROLE_HAVE_USER_BIND.toString(), obj);
				return;
			}
		}
		sysRoleMenuDao.delete(roleIds);
		sysRoleDao.delete(roleIds);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void roleEdit(OperaBean opera, ReturnInfoBean returnInfo, SysRoleBean t) throws Exception {
		SysRoleBean role = sysRoleDao.queryRoleByName(t.getRoleName());
		if (t.getRoleId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			if (role != null) {
				returnInfo.setReturnCode(ReturnCodeEnum.ROLE_NAME_EXISTS.toString());
				return;
			}
			sysRoleDao.insert(t);
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			if (role != null && t.getRoleId().intValue() != role.getRoleId().intValue()) {
				returnInfo.setReturnCode(ReturnCodeEnum.ROLE_NAME_EXISTS.toString());
				return;
			}
			sysRoleDao.update(t);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}
}


