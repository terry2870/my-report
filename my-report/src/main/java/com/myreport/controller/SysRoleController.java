package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myreport.bean.OperaBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRoleBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.service.SysRoleService;
import com.mytools.beans.PageBean;

/**
 * @author huangping
 * 2014-03-11
 */
@Controller
public class SysRoleController extends BaseController {
	
	Logger log = Logger.getLogger(getClass());

	@Resource
	SysRoleService sysRoleService;
	
	@RequestMapping("/jsp/queryAllRoles.do")
	public void queryAllRoles(SysRoleBean role, PageEntity page, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			PageBean p = page.toPageBean();
			List<SysRoleBean> list = sysRoleService.queryAll(role, p, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
			json.put("rows", list == null ? "[]" : list);
			json.put("total", p.getTotalCount());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(json.toString());
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/roleDelete.do")
	public void roleDelete(String roleIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysRoleService.deleteRoles(opera, returnInfo, roleIds.split(","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/roleSave.do")
	public void roleSave(HttpServletRequest request, HttpServletResponse response, SysRoleBean role) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		role.setCreateUserId(opera.getUser().getUserId());
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysRoleService.roleEdit(opera, returnInfo, role);
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

