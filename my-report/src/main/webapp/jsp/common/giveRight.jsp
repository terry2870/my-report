<%@page import="com.myreport.utils.SessionUtil"%>
<%@page import="com.myreport.constants.Constant"%>
<%@page import="com.myreport.bean.SysUserBean"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
	SysUserBean user = (SysUserBean) session.getAttribute(Constant.USER_SESSION);
%>
<script>

	function showAddButton(menuId, parentDiv, target, checkEnable) {
		if ("<%=SessionUtil.isSuperManager(user.getLoginName())%>" == "true") {
			for (var i in checkEnable) {
				if (target) {
					if (checkEnable[i]()) {
						target.$(parentDiv +" #" + i).linkbutton("enable");
					} else {
						target.$(parentDiv +" #" + i).linkbutton("disable");
						target.$(parentDiv +" #" + i).find(".l-btn-text").addClass("whiteBlack");
					}
				} else {
					if (checkEnable[i]()) {
						$(parentDiv +" #" + i).linkbutton("enable");
					} else {
						$(parentDiv +" #" + i).linkbutton("disable");
						$(parentDiv +" #" + i).find(".l-btn-text").addClass("whiteBlack");
					}
				}
			}
		} else {
			$.ajax({
				url : "<t:path />/jsp/noFilter.do",
				data : {
					method : "queryUserMenu",
					menuId : menuId
				},
				type : "POST",
				dataType : "json",
				success : function(json) {
					if (json) {
						/* for (var i in checkShow) {
							if (target) {
								if (!checkShow[i]() || !contants(json, i)) {
									target.$(parentDiv +" #" + i).hide();
								}
							} else {
								if (!checkShow[i]() || !contants(json, i)) {
									$(parentDiv +" #" + i).hide();
								}
							}
						} */
						for (var i in checkEnable) {
							if (target) {
								if (checkEnable[i]() && contants(json, i)) {
									target.$(parentDiv +" #" + i).linkbutton("enable");
								} else {
									target.$(parentDiv + " #"+ i).find(".l-btn-text").addClass("whiteBlack");
									target.$(parentDiv + " #"+ i).linkbutton("disable");
								}
							} else {
								if (checkEnable[i]() && contants(json, i)) {
									$(parentDiv +" #" + i).linkbutton("enable");
								} else {
									$(parentDiv + " #"+ i).find(".l-btn-text").addClass("whiteBlack");
									$(parentDiv + " #"+ i).linkbutton("disable");
								}
							}
						}
					}
				}
			});
		}
	}
	function contants(arr, buttonId) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i].buttonId == buttonId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 给列表页面的按钮按权限显示
	 */
	function getPageListButton(menuId, parentDiv, target, check) {
		if ("<%=SessionUtil.isSuperManager(user.getLoginName())%>" == "true") {
			for (var i in check) {
				if (check[i]()) {
					target.$(parentDiv + " #"+ i).linkbutton("enable");
					target.$(parentDiv + " #"+ i).find(".l-btn-text").removeClass("whiteBlack");
				} else {
					target.$(parentDiv + " #"+ i).linkbutton("disable");
					target.$(parentDiv + " #"+ i).find(".l-btn-text").addClass("whiteBlack");
				}
			}
		} else {
			$.ajax({
				url : "<t:path />/jsp/noFilter.do",
				data : {
					method : "queryUserMenu",
					menuId : menuId
				},
				type : "POST",
				dataType : "json",
				success : function(json) {
					if (json) {
						for (var i in check) {
							if (check[i]() && contants(json, i)) {
								target.$(parentDiv +" #" + i).linkbutton("enable");
								target.$(parentDiv +" #" + i).find(".l-btn-text").removeClass("whiteBlack");
							} else {
								target.$(parentDiv +" #" + i).linkbutton("disable");
								target.$(parentDiv +" #" + i).find(".l-btn-text").addClass("whiteBlack");
							}
						}
					}
				}
			});
		}
	}
	
	/**
	 * 给新增修改页面的按钮，按照权限显示
	 */
	function getEditPageButton(menuId, check) {
		if ("<%=SessionUtil.isSuperManager(user.getLoginName())%>" == "true") {
			for (var i in check) {
				if (check[i]()) {
					$("#"+ i).linkbutton("enable");
				} else {
					$("#"+ i).linkbutton("disable");
					$("#"+ i).find(".l-btn-text").addClass("whiteBlack");
				}
			}
		} else {
			$.ajax({
				url : "<t:path />/jsp/noFilter.do",
				data : {
					method : "queryUserMenu",
					menuId : menuId
				},
				type : "POST",
				dataType : "json",
				success : function(json) {
					if (json) {
						for (var i in check) {
							if (check[i]() && contants(json, i)) {
								$("#" + i).linkbutton("enable");
							} else {
								$("#" + i).linkbutton("disable");
								$("#"+ i).find(".l-btn-text").addClass("whiteBlack");
							}
						}
					}
				}
			});
		}
	}
</script>



