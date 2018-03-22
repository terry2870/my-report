package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myreport.bean.OperaBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserRegionBean;
import com.myreport.constants.Constant;
import com.myreport.service.SysUserRegionService;
import com.myreport.service.SysUserService;
import com.mytools.beans.PageBean;

/**
 * 用户操作的控制类
 * @author huangping <br />
 * 2012-12-4
 */
@Controller
public class SysUserController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	SysUserService sysUserService;
	@Resource
	SysUserRegionService sysUserRegionService;
	
	/**
	 * 查询用户列表
	 * @param user 查询条件
	 * @param page 分页信息
	 * @param session session信息
	 * @param response response
	 */
	@RequestMapping("/jsp/queryAllSysUser.do")
	public void queryAllSysUser(SysUserBean user, PageEntity page, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			PageBean p = page.toPageBean();
			List<SysUserBean> list = sysUserService.queryAll(user, p, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
			json.put("rows", list == null ? "[]" : list);
			json.put("total", p.getTotalCount());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(json);
				out.close();
			}
		}
	}

	/**
	 * 删除用户
	 * @param userId 用户ID
	 * @param request request
	 * @param response response
	 */
	@RequestMapping("/jsp/deleteUser.do")
	public void deleteUser(String userIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysUserService.deleteUser(opera, returnInfo, userIds.split(","));
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
	 * 保存用户信息
	 * @param user 用户信息
	 * @param request request
	 * @param response response
	 */
	@RequestMapping("/jsp/saveUser.do")
	public void saveUser(SysUserBean user, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		user.setCreateUserId(opera.getUser().getUserId());
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysUserService.saveUser(opera, returnInfo, user);
			SysUserRegionBean r = new SysUserRegionBean();
			r.setUserId((Integer) returnInfo.getReturnObj());
			String regionIds = request.getParameter("userRegionHidden");
			if (!StringUtils.isEmpty(regionIds)) {
				r.setRegionIds(regionIds.split(","));
			}
			sysUserRegionService.saveUserRegion(opera, returnInfo, r);
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
	 * 修改用户密码
	 * @param user 用户信息
	 * @param request request
	 * @param response response
	 */
	@RequestMapping("/jsp/modifyPwd.do")
	public void modifyPwd(SysUserBean user, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysUserService.modifyPwd(opera, returnInfo, user);
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

