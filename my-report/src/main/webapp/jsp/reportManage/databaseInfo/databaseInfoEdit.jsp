<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="databaseInfoEditForm" id="databaseInfoEditForm" method="post">
	<input type="hidden" name="databaseId" id="databaseId" value="<t:write name='databaseId' />" />
	<table cellpadding="0" cellspacing="0" border="1" width="95%" align="center" style="margin-top:20px;line-height:25px;">
		<tr>
			<td width="30%" align="right">数据库类型：</td>
			<td width="70%">
				<input name="databaseType" id="databaseType" value="<t:write name='databaseType' />" class="easyui-combobox" data-options=" 
					required : true,
					url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=com.mytools.enums.DatabaseTypeEnum',
					panelHeight : 150,
					editable : false
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">数据库标题：</td>
			<td width="70%">
				<input name="databaseTitle" id="databaseTitle" value="<t:write name='databaseTitle' />" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">数据库IP：</td>
			<td width="70%">
				<input name="databaseIp" id="databaseIp" value="<t:write name='databaseIp' />" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">数据库端口：</td>
			<td width="70%">
				<input name="databasePort" id="databasePort" value="<t:write name='databasePort' />" class="easyui-numberbox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">数据库名：</td>
			<td width="70%">
				<input name="databaseName" id="databaseName" value="<t:write name='databaseName' />" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">数据库用户名：</td>
			<td width="70%">
				<input name="userName" id="userName" value="<t:write name='userName' />" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">数据库用户密码：</td>
			<td width="70%">
				<input name="password" id="password" type="password" value="<t:write name='password' />" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td width="30%" align="right">状态：</td>
			<td width="70%">
				<input name="status" id="status" class="easyui-combobox" data-options="
					url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=StatusEnum',
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
		$("#databaseInfoEditForm").form();
	});
</script>

