<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div>
	<ul id="reportGroupUl"></ul>
</div>
<script>
	$(function() {
		$("#reportGroupUl").myTree({
			animate : true,
			lines : true,
			ajaxParam : {
				url : "<t:path />/jsp/queryReportGroupByUserId.do?searchType=0",
				cache : false
			},
			idField : "groupId",
			pidField : "parentGroupId",
			textField : "groupName",
			rootPid : 0,
			animate : true,
			checkbox : true,
			onClick : function(node) {
				if (!$("#reportGroupUl").myTree("isLeaf", node.target)) {
					$("#reportGroupUl").myTree("toggle", node.target);
				}
			},
			onLoadSuccess : function(node, data) {
				$("#reportGroupUl").myTree("expandAll");
				$.ajax({
					url : "<t:path />/jsp/queryReportGroupByUserGroupId.do?userGroupId=<t:write name='groupId' />",
					dataType : "json",
					type : "POST",
					success : function(json) {
						if (json && json != "") {
							$(json).each(function(i, item) {
								var t = $("#reportGroupUl").myTree("find", item.reportGroupId);
								if (t) {
									$("#reportGroupUl").myTree("check", t.target);
								}
							});
						}
					}
				});
			}
		});
	});
	function saveUserGroupReportGroup(div) {
		var check = $("#reportGroupUl").myTree("getChecked");
		var menuArr = [];
		$(check).each(function(i, item) {
			if ($("#reportGroupUl").myTree("isLeaf", item.target)) {
				menuArr.push(item.attributes.groupId);
			}
		});
		$.messager.progress({
			title : "正在执行",
			msg : "正在执行，请稍后...",
			interval : 500
		});
		$.ajax({
			url : "<t:path />/jsp/saveUserGroupReportGroup.do",
			data : {
				reportGroupIds : menuArr.join(","),
				userGroupId : "<t:write name='groupId' />"
			},
			dataType : "json",
			type : "POST",
			success : function(json) {
				$.messager.progress("close");
				if (json.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
					$(div).dialog("close");
					$.messager.show({
						title : "提示",
						msg : "关联报表组成功！"
					});
				} else {
					$.messager.alert("失败", json.returnInfo, "error");
				}
			}
		});
	}
</script>

