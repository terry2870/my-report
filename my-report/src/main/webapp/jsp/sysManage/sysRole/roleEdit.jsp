<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="roleEditForm" id="roleEditForm" method="post">
	<input type="hidden" name="roleId" id="roleId" value="<t:write name='roleId' />" />
	<table cellpadding="0" cellspacing="0" border="1" width="80%" align="center" style="margin-top:20px">
		<tr>
			<td width="30%" align="right">角色名：</td>
			<td width="70%">
				<input name="roleName" id="roleName" size="30" value="<t:write name='roleName' />" 
					class="easyui-validatebox" data-options="
						required : true,
						validType : 'checkName',
						missingMessage : '角色名不能为空',
						invalidMessage : '请输入正确的角色名，角色名不能输入形如（@#$）等特殊字符'
					" />
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">状态：</td>
			<td width="70%">
				<input name="status" id="status" size="30" class="easyui-combobox" data-options="
					url : '<t:path />/jsp/noFilter.do',
					queryParams : {
						method : 'queryEnumForSelect',
						className : 'StatusEnum'
					},
					panelHeight : 50,
					editable : false,
					required : true,
					value : '<t:write name='status' defaultValue='<%=StatusEnum.A.toString()%>' />',
					missingMessage : '状态必须选择！'
				" />
			</td>
		</tr>
		<tr>
			<td align="right">角色描述：</td>
			<td><input name="roleInfo" id="roleInfo" size="30" value="<t:write name='roleInfo' />" /></td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		$("#roleEditForm").form();
	});
</script>

