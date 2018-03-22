<%@page import="com.myreport.enums.StatisTypeEnum"%>
<%@page import="com.myreport.enums.YesOrNoEnum"%>
<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form id="addTableColumnsForm">
	<input type="hidden" name="type" id="type" />
	<table cellpadding="0" cellspacing="0" border="1" width="98%" align="center" style="margin-top:20px">
		<tr>
			<td width="30%" align="right">列标题：</td>
			<td width="70%">
				<input name="columnTitle" id="columnTitle" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">列属性名：</td>
			<td>
				<input name="columnName" id="columnName" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">列宽度：</td>
			<td>
				<input name="columnWidth" id="columnWidth" class="easyui-numberbox" data-options=" 
					required : true,
					min : 10,
					max : 1000
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">是否固定：</td>
			<td>
				<input name="frozenAble" id="frozenAble" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">是否可以排序：</td>
			<td>
				<input name="sortable" id="sortable" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">统计方式：</td>
			<td>
				<input name="statisType" id="statisType" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">是否可以下转：</td>
			<td>
				<input name="downAble" id="downAble" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr id="downSQLTR">
			<td align="right">下转SQL：</td>
			<td><textarea rows="3" cols="20" id="downSQL" name="downSQL"></textarea><span class='requriedInput'>*</span></td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		var obj = <t:write name="obj" />;
		if (obj.downAble == <%=YesOrNoEnum.YES.getValue()%>) {
			$("#addTableColumnsForm #downSQLTR").show();
		} else {
			$("#addTableColumnsForm #downSQLTR").hide();
		}
		$("#addTableColumnsForm").form("load", {
			type : obj.type,
			columnTitle : obj.title,
			columnName : obj.field,
			columnWidth : obj.width ? obj.width : 200,
			downSQL : obj.downSQL
		});
		$("#addTableColumnsForm #frozenAble").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryEnumForSelect",
				className : "YesOrNoEnum"
			},
			value : obj.frozenAble ? obj.frozenAble : <%=YesOrNoEnum.NO.getValue()%>
		});
		$("#addTableColumnsForm #sortable").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryEnumForSelect",
				className : "YesOrNoEnum"
			},
			value : obj.sortable == true ? "<%=YesOrNoEnum.YES.getValue()%>" : "<%=YesOrNoEnum.NO.getValue()%>"
		});
		$("#addTableColumnsForm #statisType").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			multiple : true,
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryEnumForSelect",
				className : "StatisTypeEnum"
			},
			onLoadSuccess : function() {
				var statisType = obj.statisType;
				if (statisType) {
					for (var i = 0; i < statisType.length; i++) {
						$(this).combobox("select", statisType[i]);
					}
				} else {
					$(this).combobox("select", '<%=StatisTypeEnum.NONE.getValue()%>');
				}
			}
		});
		$("#addTableColumnsForm #downAble").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do",
			queryParams : {
				method : "queryEnumForSelect",
				className : "YesOrNoEnum"
			},
			value : obj.downAble ? obj.downAble : <%=YesOrNoEnum.NO.getValue()%>,
			onSelect : function(record) {
				if (record.value == "<%=YesOrNoEnum.YES.getValue()%>") {
					$("#addTableColumnsForm #downSQLTR").show();
				} else {
					$("#addTableColumnsForm #downSQLTR").hide();
				}
			}
		});
		<%-- 
		alert(2);
		
		 --%>
		/* if ("<t:write name='type' />" == "edit") {
			$("#addTableColumnsForm #columnName").attr("readonly", "readonly");
		} */
	});
</script>
