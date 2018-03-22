<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<table align="center" id="userSearchTable" cellpadding="0" cellspacing="0" style="margin-top: 7px" width="100%" border="0">
	<tr>
		<td width="10%" align="right">登录名：</td>
		<td width="10%"><input type="text" id="loginName"  /></td>
		<td width="10%" align="right">用户名称：</td>
		<td width="10%"><input type="text" id="userName"  /></td>
		<td width="10%" align="right">所属角色：</td>
		<td width="10%"><input id="roleId" size="30" /></td>
		<td width="10%" align="right">状态：</td>
		<td width="10%"><input id="status" size="30" /></td>
		<td width="20%" align="center"><a id="btn" /></td>
	</tr>
</table>
<script type="text/javascript">
	$(function(){
		$("#userSearchTable #status").combobox({
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryEnumForSelect",
				className : "StatusEnum",
				firstValue : "",
				firstText : "请选择"
			},
			width : 100,
			value : "",
			panelHeight : 100,
			editable : false
		});
		$("#userSearchTable #roleId").combobox({
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryRoleForSelect",
				firstValue : "0",
				firstText : "请选择"
			},
			width : 100,
			value : 0,
			valueField : "roleId",
			textField : "roleName",
			editable : false
		});
		$("#userSearchTable #btn").linkbutton({
			text : "查询",
			iconCls : "icon-search"
		}).click(function() {
			searchUser();
		});
		$("#userSearchTable,#userSearchTable input[type='text']").keydown(function(e) {
			if (e.keyCode == 13) {
				searchUser();
			}
		});
		function searchUser() {
			$("#userListTable").datagrid("load", {
				loginName : $("#userSearchTable #loginName").val(),
				userName : $("#userSearchTable #userName").val(),
				roleId : $("#userSearchTable #roleId").combobox("getValue"),
				status : $("#userSearchTable #status").combobox("getValue")
			});
		}
	});
</script>

