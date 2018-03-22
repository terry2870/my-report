<%@page import="com.myreport.configs.ConfigFactory"%>
<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div>
	<form name="pwdForm" id="pwdForm" method="post">
		<input type="hidden" name="loginName" id="loginName" value="<t:write name="loginName" />" />
		<table cellpadding="0" cellspacing="0" border="1" width="80%" align="center" style="margin-top: 20px">
			<tr style="height:23px">
				<td width="30%" align="right">账号：</td>
				<td width="70%"><t:write name="loginName" /></td>
			</tr>
			<tr>
				<td align="right">原始密码：</td>
				<td><input type="password" name="oldPwd" id="oldPwd" size="20" class="easyui-validatebox" data-options="
					required : true,
					missingMessage : '请输入原始密码！'
				" /></td>
			</tr>
			<tr>
				<td align="right">新密码：</td>
				<td><input type="password" name="loginPwd" id="loginPwd" size="20" /></td>
			</tr>
			<tr>
				<td align="right">重复密码：</td>
				<td><input type="password" name="loginPwd2" id="loginPwd2" size="20" /></td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
	$(function() {
		$("#pwdForm").form({
			url : "<t:path/>/jsp/modifyPwd.do",
			onSubmit : function() {
				if (!$("#pwdForm").form("validate")) {
					return false;
				}
				return true;
			},
			success : function(data) {
				data = $.parseJSON(data);
				if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
					if ("<t:write name='flag' />" == "0") {
						$.messager.confirm("确认", "密码修改成功，是否立即登录平台？", function(flag) {
							if (flag) {
								$("#f1 #loginPwd").val($("#pwdForm #loginPwd").val());
								$("#f1").submit();
							}
						});
					} else {
						$.messager.alert("提示", "密码修改成功！", "info");
						$("#pwdForm").form("clear");
					}
				} else {
					$.messager.alert("失败", data.returnInfo, "error");
				}
			}
		});
		$("#pwdForm #loginPwd").validatebox({
			required : true,
			validType : "<%=ConfigFactory.getSysConfig().isCheckPwd()%>" == "true" ? "pwdComplexDegree" : undefined
		});
		$("#pwdForm #loginPwd2").validatebox({
			required : true,
			validType : "checkPwd['#pwdForm #loginPwd']"
		});
	});
</script>


