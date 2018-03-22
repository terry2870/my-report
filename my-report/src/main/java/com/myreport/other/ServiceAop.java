package com.myreport.other;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserActionLogBean;
import com.myreport.dao.SysUserActionLogDao;
import com.myreport.enums.ReturnCodeEnum;

/**
 * 拦截器。<br />
 * 拦截service中所有非查询类操作，并且记录用户操作信息，写数据库<br />
 * 拦截service中所有出现异常的情况，并且写日志文件
 * @author hp
 * 2014-03-11
 */
@Component
@Aspect
public class ServiceAop {

	@Resource
	SysUserActionLogDao sysUserActionLogDao;

	Logger log = Logger.getLogger(getClass());

	/**
	 * 拦截com.myreport.service该包下面的所有的类名以Service结尾，方法是void类型，第一个参数是OperaBean，第二个参数是ReturnInfoBean
	 * @param join
	 */
	@AfterReturning(value = "execution(public void com.myreport.service.*Service.*(com.myreport.bean.OperaBean, com.myreport.bean.ReturnInfoBean, ..))()")
	public void afterReturn(JoinPoint join) {
		Object[] args = join.getArgs();
		OperaBean opera = (OperaBean) args[0];
		ReturnInfoBean returnInfo = (ReturnInfoBean) args[1];
		Object[] obj = new Object[args.length - 2];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = args[i + 2];
		}
		JSONArray arr = new JSONArray();
		if (!StringUtils.isEmpty(opera.getLogInfo())) {
			arr.add(opera.getLogInfo());
		} else {
			if (obj != null && obj.length > 0) {
				for (int i = 0; i < obj.length; i++) {
					if (obj[i] instanceof Number || obj[i] instanceof String || obj[i] instanceof Boolean) {
						arr.add(obj[i].toString());
					} else if (obj[i] instanceof List<?>) {
						arr.add(obj[i]);
					} else if (obj[i] instanceof Object[]) {
						arr.add(obj[i]);
					} else if (obj[i] instanceof HttpServletRequest) {
						HttpServletRequest request = (HttpServletRequest) obj[i];
						arr.add(request.getParameterMap());
					} else {
						arr.add(obj);						
					}
				}
			}
		}
		SysUserActionLogBean logs = new SysUserActionLogBean();
		logs.setLogInfo(arr.toString());
		logs.setLogType(opera.getOperaType());
		logs.setUserId(opera.getUser() == null ? null : opera.getUser().getUserId());
		logs.setUserIp(opera.getOperatorIp());
		logs.setLogClass(join.toString());
		if (ReturnCodeEnum.CODE_OK.toString().equals(returnInfo.getReturnCode())) {
			try {
				sysUserActionLogDao.insert(logs);
			} catch (Exception e) {
				log.error("", e);
			}
		}
		log.info(JSON.toJSONString(logs));
	}

	/**
	 * 拦截com.myreport.service该包下面的类名以Service结尾，里面的所有方法，出现异常的情况
	 * @param join
	 * @param e
	 */
	@AfterThrowing(value = "execution(public * com.myreport.service.*Service.*(..))()", throwing = "e")
	public void afterThrowing(JoinPoint join, Exception e) {
		ReturnInfoBean returnInfo = null;
		Object[] args = join.getArgs();
		if (args != null && args.length > 1 && args[1] instanceof ReturnInfoBean) {
			returnInfo = (ReturnInfoBean) join.getArgs()[1];
			returnInfo.setReturnCode(ReturnCodeEnum.UNKNOWN_ERROR.toString());
			returnInfo.setReturnInfo(e.getMessage());
		}
	}
}

