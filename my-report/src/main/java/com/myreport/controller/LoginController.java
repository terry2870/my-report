package com.myreport.controller;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.enums.ReturnCodeEnum;
import com.myreport.service.SysMenuService;
import com.myreport.service.SysPwdHistoryService;
import com.myreport.service.SysUserService;

/**
 * 登录controller
 * @author hp
 * 2014-03-11
 */
@Controller
public class LoginController extends BaseController {
	
	Logger log = Logger.getLogger(getClass());
	
	@Resource
	SysUserService sysUserService;
	
	@Resource
	SysMenuService sysMenuService;
	
	@Resource
	SysPwdHistoryService sysPwdHistoryService;
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/login.do")
	public void login(SysUserBean user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			String sessionCheckCode = (String) request.getSession().getAttribute("checkCode");
			if (!sessionCheckCode.equals(user.getCheckCode())) {
				returnInfo.setReturnCode(ReturnCodeEnum.CHECK_CODE_ERROR.toString());
				return;
			}
			sysUserService.login(opera, returnInfo, user);
			if (!ReturnCodeEnum.CODE_OK.toString().equals(returnInfo.getReturnCode())) {
				return;
			}
			request.getSession().setAttribute(Constant.USER_SESSION, returnInfo.getReturnObj());
			request.getSession().setAttribute(Constant.USER_MENU_LIST, sysMenuService.queryAll((SysUserBean) returnInfo.getReturnObj()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}


}

