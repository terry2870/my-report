package com.myreport.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myreport.bean.EnumTypeBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.SysMenuBean;
import com.myreport.bean.SysRoleBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.enums.MenuTypeEnum;
import com.myreport.service.CommonService;
import com.myreport.service.SysRoleService;
import com.myreport.service.SysUserService;
import com.mytools.beans.PageBean;
import com.mytools.utils.MyToolsUtil;

/**
 * @author hp
 * 2014-02-14
 */
@Controller
@RequestMapping("/jsp/noFilter.do")
@SuppressWarnings("unchecked")
public class NoFilterController {

	Logger log = Logger.getLogger(getClass());

	@Resource
	SysRoleService sysRoleService;
	@Resource
	SysUserService sysUserService;
	@Resource
	CommonService commonService;

	@RequestMapping(params = "method=queryDataList")
	public void queryDataList(HttpServletRequest request, HttpServletResponse response, PageEntity page) {
		PrintWriter out = null;
		List<Map<String, Object>> list = null;
		JSONObject json = null;
		PageBean p = null;
		String result = "";
		if (page != null && !page.isEmpty()) {
			p = page.toPageBean();
		}
		try {
			out = response.getWriter();
			list = commonService.queryDataList(request, p);
			if (page != null && !page.isEmpty()) {
				json = new JSONObject();
				json.put("rows", list == null ? new ArrayList<Map<String, Object>>() : list);
				json.put("total", p.getTotalCount());
				result = json.toString();
			} else {
				result = list == null ? "[]" : JSON.toJSONString(list);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(result);
				out.close();
			}
		}
	}
	
	@RequestMapping(params = "method=queryRoleForSelect")
	public void queryRoleForSelect(HttpServletRequest request, HttpServletResponse response) {
		String firstText = request.getParameter("firstText");
		String firstValue = request.getParameter("firstValue");
		PrintWriter out = null;
		List<SysRoleBean> list = null;
		try {
			out = response.getWriter();
			list = sysRoleService.queryAll(null, null, (SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION));
			if (list == null) {
				list = new ArrayList<SysRoleBean>();
			}
			if (firstValue != null) {
				SysRoleBean role = new SysRoleBean();
				role.setRoleId(new Integer(firstValue));
				role.setRoleName(firstText);
				list.add(0, role);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(list == null ? "[]" : JSON.toJSONString(list));
				out.close();
			}
		}
	}

	@RequestMapping(params = "method=queryEnumForSelect")
	public void queryEnumForSelect(EnumTypeBean bean, HttpServletResponse response) {
		PrintWriter out = null;
		JSONArray arr = null;
		try {
			out = response.getWriter();
			String className = bean.getClassName();
			if (className.indexOf(".") < 0) {
				className = "com.myreport.enums." + className;
			}
			arr = (JSONArray) MyToolsUtil.executeJavaMethod(Class.forName(className), "toJson", null, null);
			if (bean.getFirstText() != null) {
				JSONObject json = new JSONObject();
				json.put("text", bean.getFirstText());
				json.put("value", bean.getFirstValue());
				arr.add(0, json);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(arr.toString());
				out.close();
			}
		}
	}

	@RequestMapping(params = "method=queryUserMenu")
	public void queryUserMenu(String menuId, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysMenuBean> list = new ArrayList<SysMenuBean>();
		try {
			out = response.getWriter();
			List<SysMenuBean> menuList = (List<SysMenuBean>) session.getAttribute(Constant.USER_MENU_LIST);
			for (SysMenuBean menu : menuList) {
				if (!MenuTypeEnum.BUTTON.toString().equals(menu.getMenuType())) {
					continue;
				}
				if (menu.getParentMenuId() != Long.parseLong(menuId)) {
					continue;
				}
				list.add(menu);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(list == null ? "[]" : JSON.toJSONString(list));
				out.close();
			}
		}
	}

	@RequestMapping(params = "method=queryChildUsers")
	public void queryChildUsers(String menuId, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysUserBean> list = null;
		try {
			out = response.getWriter();
			list = sysUserService.queryAll(null, null, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
			SysUserBean u = new SysUserBean();
			u.setUserId(null);
			u.setUserName("请选择");
			list.add(0, u);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(list == null ? "[]" : JSON.toJSONString(list));
				out.close();
			}
		}
	}
}


