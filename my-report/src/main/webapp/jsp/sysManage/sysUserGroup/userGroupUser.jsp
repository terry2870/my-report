<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div fit="true" class="easyui-layout">
	<div region="west" title="待选择的用户" style="width: 300px;border-style: none">
		<table id="waitForSelectUser"></table>
	</div>
	<div region="center" style="padding-top:180px">
		<a id="toLeftBtn" /><br /><br /><br />
		<a id="toRightBtn" />
	</div>
	<div region="east" title="已选择的用户" style="width: 300px;border-style: none">
		<table id="selectedUser"></table>
	</div>
</div>
<script>
	$(function() {
		$("#toLeftBtn").linkbutton({
			text : "<<---",
			iconCls : "icon-remove",
		}).click(function() {
			var data = $("#selectedUser").datagrid("getSelections");
			if (!data || data.length == 0) {
				return;
			}
			var d = $.extend([], data);
			for (var i = 0; i < d.length; i++) {
				$("#selectedUser").datagrid("deleteRow", $("#selectedUser").datagrid("getRowIndex", d[i]));
				$("#waitForSelectUser").datagrid("appendRow", d[i]);
			}
		});
		$("#toRightBtn").linkbutton({
			text : "--->>",
			iconCls : "icon-add",
		}).click(function() {
			var data = $("#waitForSelectUser").datagrid("getSelections");
			if (!data || data.length == 0) {
				return;
			}
			var d = $.extend([], data);
			for (var i = 0; i < d.length; i++) {
				$("#waitForSelectUser").datagrid("deleteRow", $("#waitForSelectUser").datagrid("getRowIndex", d[i]));
				$("#selectedUser").datagrid("appendRow", d[i]);
			}
		});
		$("#waitForSelectUser").datagrid({
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/queryForSelectUserByGroupId.do",
			queryParams : {
				groupId : <t:write name='groupId' />
			},
			columns : [[{
				title : "登录名",
				field : "loginName",
				width : 80,
				align : "center",
				sortable : true
			}, {
				title : "用户名称",
				field : "userName",
				width : 100,
				align : "center",
				sortable : true
			}]],
			cache : false,
			rownumbers : true,
			idField : "userId",
			singleSelect : false
		});
		$("#selectedUser").datagrid({
			fit : true,
			fitColumns : true,
			nowrap : true,
			striped : true,
			url : "<t:path />/jsp/querySysUserGroupByGroupId.do",
			queryParams : {
				groupId : <t:write name='groupId' />
			},
			columns : [[{
				title : "登录名",
				field : "loginName",
				width : 80,
				align : "center",
				sortable : true
			}, {
				title : "用户名称",
				field : "userName",
				width : 100,
				align : "center",
				sortable : true
			}]],
			cache : false,
			rownumbers : true,
			idField : "userId",
			singleSelect : false
		});
	});
</script>

