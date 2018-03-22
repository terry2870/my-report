package com.myreport.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myreport.bean.OperaBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysRegionBean;
import com.myreport.bean.SysUserBean;
import com.myreport.constants.Constant;
import com.myreport.service.SysRegionService;
import com.myreport.service.SysUserRegionService;

/**
 * 
 * @author huangping<br />
 * 2013-2-4
 */
@Controller
public class SysRegionController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	@Resource
	SysRegionService sysRegionService;
	@Resource
	SysUserRegionService sysUserRegionService;
	
	@RequestMapping("/jsp/queryAllRegion.do")
	public void queryAllRegion(String treeType, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysRegionBean> list = null;
		Object result = "";
		try {
			out = response.getWriter();
			SysUserBean user = (SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION);
			list = sysRegionService.queryAllRegion(user);
			if (list == null) {
				result = "[]";
			} else {
				if ("tree".equalsIgnoreCase(treeType)) {
					result = queryChildRegion(list, 0);
				} else if ("myTree".equalsIgnoreCase(treeType)) {
					result = JSON.toJSONString(list);
				}
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
	
	private JSONArray queryChildRegion(List<SysRegionBean> list, long regionId) {
		JSONArray arr = new JSONArray();
		if (list != null && list.size() > 0) {
			JSONObject j = null;
			SysRegionBean r = null;
			JSONArray child = null;
			for (int i = 0; i < list.size(); i++) {
				r = list.get(i);
				if (r.getParentRegionId() == regionId) {
					j = r.toTreeJson();
					child = queryChildRegion(list, r.getRegionId());
					if (child != null && child.size() > 0) {
						j.put("state", "closed");
						j.put("children", child);
					}
					arr.add(j);
				}
			}
		}
		return arr;
	}
	
	@RequestMapping("/jsp/queryUserRegion.do")
	public void queryUserRegion(Integer userId, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysRegionBean> list = null;
		try {
			out = response.getWriter();
			if (userId != null) {
				SysUserBean user = new SysUserBean();
				user.setUserId(userId);
				list = sysUserRegionService.queryUserRegion(user);
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
	
	@RequestMapping("/jsp/deleteSysregion.do")
	public void deleteSysregion(String regionIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysRegionService.deleteSysRegion(opera, returnInfo, regionIds.split(","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.print(returnInfo.toString());
				out.close();
			}
		}
	}

	@RequestMapping("/jsp/saveSysRegion.do")
	public void saveSysRegion(HttpServletRequest request, HttpServletResponse response, SysRegionBean sysRegionBean) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysRegionService.editSysRegion(opera, returnInfo, sysRegionBean);
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

