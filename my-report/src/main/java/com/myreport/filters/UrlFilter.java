package com.myreport.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.myreport.bean.SysMenuBean;
import com.myreport.bean.SysUserBean;
import com.myreport.configs.ConfigFactory;
import com.myreport.constants.Constant;
import com.mytools.constants.MyToolsConstant;

/**
 * @author hp
 * 2014-02-14
 */
public class UrlFilter implements Filter {

	Logger log = Logger.getLogger(getClass());
	
	@Override
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		request.setCharacterEncoding(MyToolsConstant.PAGE_ENCODING);
		response.setCharacterEncoding(MyToolsConstant.PAGE_ENCODING);
		response.setContentType(MyToolsConstant.CONTENT_TYPE);
		String url = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (StringUtils.isEmpty(url)) {
			url = request.getRequestURI();
		}
		String lastName = url.substring(url.lastIndexOf("/") + 1);
		//第一层免过滤
		if (StringUtils.isEmpty(lastName) || ConfigFactory.getSysConfig().getFirstNoFilterList().contains(lastName)) {
			arg2.doFilter(arg0, arg1);
			return;
		}
		//过滤session失效的
		SysUserBean user = (SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION);
		if (user == null) {
			log.warn("session 过期，重新登录");
			response.sendRedirect(request.getContextPath() + "/jsp/timeout.jsp");
			return;
		}
		//第二层免过滤
		if (ConfigFactory.getSysConfig().getSecondNoFilterList().contains(lastName)) {
			arg2.doFilter(arg0, arg1);
			return;
		}
		//超级管理员
		if (ConfigFactory.getSysConfig().getSuperManagerList().contains(user.getLoginName())) {
			arg2.doFilter(arg0, arg1);
			return;
		}
		//按照权限过滤
		List<SysMenuBean> userMenu = (List<SysMenuBean>) request.getSession().getAttribute(Constant.USER_MENU_LIST);
		String[] arr = null;
		for (SysMenuBean menu : userMenu) {
			if (!StringUtils.isEmpty(menu.getMenuUrl()) && menu.getMenuUrl().indexOf(lastName) >= 0) {
				arg2.doFilter(arg0, arg1);
				return;
			}
			if (!StringUtils.isEmpty(menu.getExtraUrl())) {
				arr = menu.getExtraUrl().split(",");
				for (String s : arr) {
					if (s.equals(lastName)) {
						arg2.doFilter(arg0, arg1);
						return;
					}
				}
			}
		}
		log.warn("你没有权限访问【"+ url +"】");
		response.sendRedirect(request.getContextPath() + "/jsp/noPurview.jsp");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}


