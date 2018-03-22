<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ul id="roleMenuTree"></ul>
<script>
	$(function() {
		$("#roleMenuTree").myTree({
			animate : true,
			checkbox : true,
			lines : true,
			ajaxParam : {
				url : "<t:path />/jsp/queryAllMenu.do",
				cache : false
			},
			idField : "menuId",
			pidField : "parentMenuId",
			textField : "menuName",
			rootPid : 0,
			onClick : function(node) {
				if (!$("#roleMenuTree").myTree("isLeaf", node.target)) {
					$("#roleMenuTree").myTree("toggle", node.target);
				}
			},
			onLoadSuccess : function(node, data) {
				$("#roleMenuTree").myTree("expandAll");
				$.ajax({
					url : "<t:path />/jsp/queryRoleMenuByRoleId.do",
					data : {
						roleId : "<t:write name='roleId' />"
					},
					dataType : "json",
					type : "POST",
					success : function(json) {
						if (json && json != "") {
							$(json).each(function(i, item) {
								var t = $("#roleMenuTree").myTree("find", item.menuId);
								if (t) {
									$("#roleMenuTree").myTree("check", t.target);
								}
							});
						}
					}
				});
			}
		});
	});
	function saveRoleMenu(div) {
		var check = $("#roleMenuTree").myTree("getChecked");
		if (!check || check.length == 0) {
			$.messager.alert("提示", "请至少分配一个菜单！", "error");
			return;
		}
		var menuArr = [];
		$(check).each(function(i, item) {
			if ($("#roleMenuTree").myTree("isLeaf", item.target)) {
				menuArr.push(item.attributes.menuId);
			}
		});
		$.messager.progress({
			title : "正在执行",
			msg : "正在执行，请稍后...",
			interval : 500
		});
		$.ajax({
			url : "<t:path />/jsp/addRoleMenu.do",
			data : {
				menuIdStr : menuArr.join(","),
				roleId : "<t:write name='roleId' />"
			},
			dataType : "json",
			type : "POST",
			success : function(json) {
				$.messager.progress("close");
				if (json.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
					$(div).dialog("close");
					$.messager.show({
						title : "提示",
						msg : "分配菜单权限成功！"
					});
				} else {
					$.messager.alert("失败", json.returnInfo, "error");
				}
			}
		});
	}
</script>


