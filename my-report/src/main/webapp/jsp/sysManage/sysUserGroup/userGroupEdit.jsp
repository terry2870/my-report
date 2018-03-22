<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="userGroupEditForm" id="userGroupEditForm" method="post">
	<input type="hidden" name="groupId" id="groupId" value="<t:write name='groupId' />" />
	<table cellpadding="0" cellspacing="0" border="1" width="80%" align="center" style="margin-top:20px">
		<tr>
			<td width="30%" align="right">用户组名称：</td>
			<td width="70%">
				<input name="groupName" id="groupName" value="<t:write name='groupName' />" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">用户组描述：</td>
			<td width="70%">
				<input name="groupInfo" id="groupInfo" value="<t:write name='groupInfo' />" /> 
			</td>
		</tr>
		<tr>
			<td align="right">状态：</td>
			<td>
				<input name="status" id="status" class="easyui-combobox" data-options="
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
	</table>
</form>
<script>
	$(function() {
		$("#userGroupEditForm").form();
	});
</script>

