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
import com.alibaba.fastjson.JSONObject;
import com.myreport.bean.OperaBean;
import com.myreport.bean.PageEntity;
import com.myreport.bean.ReportGroupBean;
import com.myreport.bean.ReturnInfoBean;
import com.myreport.bean.SysUserBean;
import com.myreport.bean.SysUserGroupBean;
import com.myreport.bean.UserGroupReportGroupBean;
import com.myreport.constants.Constant;
import com.myreport.service.SysUserGroupService;
import com.myreport.service.SysUserGroupUserService;
import com.myreport.service.UserGroupReportGroupService;
import com.mytools.beans.PageBean;

/**
 * 用户组表的控制对象<br />
 * @author huangping <br />
 * 创建日期 2014-03-17
 */
@Controller
public class SysUserGroupController extends BaseController {

	Logger log = Logger.getLogger(getClass());
	
	@Resource
	SysUserGroupService sysUserGroupService;
	@Resource
	SysUserGroupUserService sysUserGroupUserService;
	@Resource
	UserGroupReportGroupService userGroupReportGroupService;
	
	/**
	 * 查询所有的用户组
	 * @param userGroup
	 * @param page
	 * @param session
	 * @param response
	 */
	@RequestMapping("/jsp/queryAllSysUserGroup.do")
	public void queryAllSysUserGroup(SysUserGroupBean userGroup, PageEntity page, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			out = response.getWriter();
			PageBean p = page.toPageBean();
			List<SysUserGroupBean> list = sysUserGroupService.queryAll(userGroup, p, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
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
	
	/**
	 * 查询该用户组下的用户
	 * @param groupId
	 * @param session
	 * @param response
	 */
	@RequestMapping("/jsp/querySysUserGroupByGroupId.do")
	public void querySysUserGroupByGroupId(Integer groupId, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysUserBean> list = null;
		try {
			out = response.getWriter();
			list = sysUserGroupUserService.queryUserByGroupId(groupId);
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
	 * 查询可以供该用户组分配的用户
	 * @param groupId
	 * @param session
	 * @param response
	 */
	@RequestMapping("/jsp/queryForSelectUserByGroupId.do")
	public void queryForSelectUserByGroupId(Integer groupId, HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		List<SysUserBean> list = null;
		try {
			out = response.getWriter();
			list = sysUserGroupUserService.queryForSelectUserByGroupId(groupId, (SysUserBean) session.getAttribute(Constant.USER_SESSION));
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
	 * 添加用户组用户
	 * @param groupId
	 * @param userIds
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/addSysUserGroupUser.do")
	public void addSysUserGroupUser(Integer groupId, String userIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysUserGroupUserService.batchInsert(opera, returnInfo, groupId, userIds);
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
	 * 删除用户组
	 * @param groupIds
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/deleteSysUserGroup.do")
	public void deleteSysUserGroup(String groupIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysUserGroupService.deleteUserGroup(opera, returnInfo, groupIds.split(","));
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
	 * 新增或修改用户组
	 * @param request
	 * @param response
	 * @param userGroup
	 */
	@RequestMapping("/jsp/saveSysUserGroup.do")
	public void saveSysUserGroup(HttpServletRequest request, HttpServletResponse response, SysUserGroupBean userGroup) {
		PrintWriter out = null;
		OperaBean opera = getOpera(request);
		userGroup.setCreateUserId(opera.getUser().getUserId());
		ReturnInfoBean returnInfo = getReturnInfo();
		try {
			out = response.getWriter();
			sysUserGroupService.editUserGroup(opera, returnInfo, userGroup);
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
	 * 查询该用户所能看到的报表组
	 * @param session
	 * @param response
	 */
	@RequestMapping("/jsp/queryReportGroupByUserId.do")
	public void queryReportGroupByUserId(HttpSession session, HttpServletResponse response) {
		PrintWriter out = null;
		SysUserBean user = (SysUserBean) session.getAttribute(Constant.USER_SESSION);
		List<ReportGroupBean> list = null;
		try {
			out = response.getWriter();
			list = sysUserGroupService.queryReportGroupByUserId(user);
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
	 * 查询关联该用户组的报表组
	 * @param userGroupId
	 * @param response
	 */
	@RequestMapping("/jsp/queryReportGroupByUserGroupId.do")
	public void queryReportGroupByUserGroupId(Integer userGroupId, HttpServletResponse response) {
		PrintWriter out = null;
		List<UserGroupReportGroupBean> list = null;
		try {
			out = response.getWriter();
			list = userGroupReportGroupService.queryReportGroupByUserGroupId(userGroupId);
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
	 * 保存用户组与报表组关联
	 * @param userGroupId
	 * @param reportGroupIds
	 * @param request
	 * @param response
	 */
	@RequestMapping("/jsp/saveUserGroupReportGroup.do")
	public void saveUserGroupReportGroup(Integer userGroupId, String reportGroupIds, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		SysUserBean user = (SysUserBean) request.getSession().getAttribute(Constant.USER_SESSION);
		OperaBean opera = new OperaBean();
		opera.setUser(user);
		opera.setOperatorIp(request.getRemoteAddr());
		ReturnInfoBean returnInfo = new ReturnInfoBean();
		try {
			out = response.getWriter();
			userGroupReportGroupService.batchInsert(opera, returnInfo, userGroupId, reportGroupIds);
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

