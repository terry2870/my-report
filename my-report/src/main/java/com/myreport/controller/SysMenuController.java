package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysMenuBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.service.SysMenuService;

/**
 * @author huangping
 * 2013-08-15
 */
@Controller
public class SysMenuController extends BaseController {
	
	Logger log = Logger.getLogger(getClass());

	@Resource
	SysMenuService sysMenuService;
	
	@RequestMapping("/jsp/queryAllMenu.do")
	public void queryAllMenu(HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		SysUserBean user = (SysUserBean) session.getAttribute(Constant.USER_SESSION);
		List<SysMenuBean> list = null;
		try {
			out = response.getWriter();
			list = sysMenuService.queryAll(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(JSON.toJSONString(list));
				out.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/jsp/queryUserSessionMenu.do")
	public void queryUserSessionMenu(HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysMenuBean> list = null;
		try {
			out = response.getWriter();
			list = (List<SysMenuBean>) session.getAttribute(Constant.USER_MENU_LIST);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(JSON.toJSONString(list));
				out.close();
			}
		}
	}
	
	/**
	 * 保存菜单
	 * @param response
	 * @param menu
	 */
	@RequestMapping("/jsp/menuSave.do")
	public void menuSave(HttpServletRequest request, HttpServletResponse response, SysMenuBean menu) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		opera.setUser((SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION));
		try {
			out = response.getWriter();
			sysMenuService.editMenu(opera, returnInfo, menu);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	/**
	 * 删除菜单
	 * @param response
	 * @param menuId
	 */
	@RequestMapping("/jsp/menuDelete.do")
	public void menuDelete(HttpServletRequest request, HttpServletResponse response, String menuIds) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		opera.setUser((SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION));
		try {
			out = response.getWriter();
			sysMenuService.delete(opera, returnInfo, menuIds.split(","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
}


