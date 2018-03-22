package com.myreport.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRoleMenuBean;
import com.myreport.service.SysRoleMenuService;

/**
 * 角色权限菜单控制类
 * @author huangping <br />
 * 2013-08-15
 */
@Controller
public class SysRoleMenuController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	SysRoleMenuService sysRoleMenuService;
	
	@RequestMapping("/jsp/addRoleMenu.do")
	public void addRoleMenu(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			String roleId = request.getParameter("roleId");
			String[] menuIdArr = request.getParameter("menuIdStr").split(",");
			List<SysRoleMenuBean> list = new ArrayList<SysRoleMenuBean>();
			for (String s : menuIdArr) {
				list.add(new SysRoleMenuBean(new Integer(roleId), new Integer(s)));
			}
			sysRoleMenuService.addRoleMenu(opera, returnInfo, list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}
	
	@RequestMapping("/jsp/queryRoleMenuByRoleId.do")
	public void queryRoleMenuByRoleId(HttpServletRequest request, HttpServletResponse response, Integer roleId) {
		PrintWriter out = null;
		List<SysRoleMenuBean> list = null;
		try {
			out = response.getWriter();
			list = sysRoleMenuService.queryRoleMenuByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(JSON.toJSONString(list));
				out.close();
			}
		}
	}
}


