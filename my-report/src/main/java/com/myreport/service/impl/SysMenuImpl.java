package com.myreport.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysMenuBean;
import com.myreport.bean.SysRoleMenuBean;
import com.myreport.bean.SysUserBean;
import com.myreport.dao.SysMenuDao;
import com.myreport.enums.ActionTypeEnum;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysMenuService;
import com.myreport.service.SysRoleMenuService;

/**
 * @author huangping
 * 2014-03-11
 */
@Service
public class SysMenuImpl implements SysMenuService {

	@Resource
	SysMenuDao sysMenuDao;
	
	@Resource
	SysRoleMenuService sysRoleMenuService;
	
	@Override
	public List<SysMenuBean> queryAll(SysUserBean sessionUser) throws Exception {
		return sysMenuDao.queryAll(null, null, sessionUser);
	}

	@Override
	public void editMenu(OperaBean opera, ReturnInfoBean returnInfo, SysMenuBean menu) throws Exception {
		SysMenuBean m = sysMenuDao.queryMenuByMenuName(menu.getMenuName(), menu.getParentMenuId());
		if (menu.getMenuId() == 0) {
			opera.setOperaType(ActionTypeEnum.ADD.toString());
			if (m != null) {
				returnInfo.setReturnCode(ReturnCodeEnum.MENU_NAME_EXISTS.toString());
				return;
			}
			sysMenuDao.insert(menu);
		} else {
			opera.setOperaType(ActionTypeEnum.MODIFY.toString());
			if (m != null && m.getMenuId().intValue() != menu.getMenuId().intValue()) {
				returnInfo.setReturnCode(ReturnCodeEnum.MENU_NAME_EXISTS.toString());
				return;
			}
			sysMenuDao.update(menu);
		}
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}

	@Override
	public void delete(OperaBean opera, ReturnInfoBean returnInfo, String[] menuIds) throws Exception {
		opera.setOperaType(ActionTypeEnum.DELETE.toString());
		for (String menuId : menuIds) {
			List<SysMenuBean> list = sysMenuDao.queryChildMenuById(Long.parseLong(menuId));
			if (list != null && list.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.MENU_HAVE_CHILD_MENU.toString());
				return;
			}
			List<SysRoleMenuBean> l = sysRoleMenuService.queryRoleMenuByMenuId(new Integer(menuId));
			if (l != null && l.size() > 0) {
				returnInfo.setReturnCode(ReturnCodeEnum.MENU_HAVE_ROLE_BIND.toString());
				return;
			}
		}
		sysMenuDao.delete(menuIds);
		returnInfo.setReturnCode(ReturnCodeEnum.CODE_OK.toString());
	}


}

