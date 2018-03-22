package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRoleMenuBean;
import com.myreport.dao.SysRoleMenuDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysRoleMenuService;

/**
 * 角色权限菜单的实现类
 * @author huangping <br />
 * 2014-03-11
 */
@Service
public class SysRoleMenuImpl implements SysRoleMenuService {

	@Resource
	SysRoleMenuDao sysRoleMenuDao;
	
	@Override
	public void addRoleMenu(OperaBean opera, ReturnInfoBean returnInfo, List<SysRoleMenuBean> list) throws Exception {
		opera.setOperaType(ActionTypeEnum.ADD.toString());
		sysRoleMenuDao.delete(new String[] { String.valueOf(list.get(0).getRoleId()) });
		sysRoleMenuDao.batchAddRoleMenu(list);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public List<SysRoleMenuBean> queryRoleMenuByRoleId(Integer roleId) throws Exception {
		SysRoleMenuBean roleMenu = new SysRoleMenuBean();
		roleMenu.setRoleId(roleId);
		return sysRoleMenuDao.queryAll(roleMenu, null);
	}

	@Override
	public List<SysRoleMenuBean> queryRoleMenuByMenuId(Integer menuId) throws Exception {
		return sysRoleMenuDao.queryRoleMenuByMenuId(menuId);
	}

}

