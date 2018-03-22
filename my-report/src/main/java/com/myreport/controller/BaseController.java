package com.myreport.controller;

import javax.servlet.http.HttpServletRequest;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;

/**
 * @author ping.huang
 * 创建时间：2015年1月23日 下午1:32:59
 * 类描述：
 */
public class BaseController {

	/**
	 * 获取操作者对象
	 * @param request
	 * @return
	 */
	public OperaBean getOpera(HttpServletRequest request) {
		SysUserBean user = (SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION);
		OperaBean opera = new OperaBean();
		opera.setUser(user);
		opera.setOperatorIp(request.getRemoteAddr());
		return opera;
	}
	
	/**
	 * 获取返回值对象
	 * @return
	 */
	public ReturnInfoBean getReturnInfo() {
		return new ReturnInfoBean();
	}
}
