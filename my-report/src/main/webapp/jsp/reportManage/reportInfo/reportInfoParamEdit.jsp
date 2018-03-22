<%@page import="com.myreport.enums.FieldComboDataTypeEnum"%>
<%@page import="com.myreport.enums.FieldTypeEnum"%>
<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form id="queryParamForm">
	<table cellpadding="0" cellspacing="0" border="1" width="98%" align="center" style="margin-top:20px">
		<tr>
			<td width="30%" align="right">字段名称：</td>
			<td width="70%">
				<input name="fieldName" id="fieldName" readonly="readonly" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">显示标签：</td>
			<td>
				<input name="fieldLabel" id="fieldLabel" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">字段类型：</td>
			<td>
				<input name="fieldType" id="fieldType" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr id="fieldEditAbleTR">
			<td align="right">是否可以编辑：</td>
			<td>
				<input name="fieldEditAble" id="fieldEditAble" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr id="fieldDateFormatterTR">
			<td align="right">日期格式：</td>
			<td>
				<input name="fieldDateFormatter" id="fieldDateFormatter" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr id="fieldComboDataTypeTR">
			<td align="right">下拉数据来源：</td>
			<td>
				<input name="fieldComboDataType" id="fieldComboDataType" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr id="fieldComboDataTR">
			<td align="right">数据源：</td>
			<td>
				<textarea name="fieldComboData" id="fieldComboData" rows="5" cols="30"></textarea>
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">是否必填：</td>
			<td>
				<input name="fieldRequired" id="fieldRequired" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">默认值：</td>
			<td>
				<input name="fieldDefaultValue" id="fieldDefaultValue" />
			</td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		var data = <t:write name="param" />;
		$("#queryParamForm #fieldDateFormatterTR,#queryParamForm #fieldComboDataTypeTR,#queryParamForm #fieldComboDataTR").hide();
		$("#queryParamForm #fieldType").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=FieldTypeEnum",
			onSelect : function(record) {
				if (record.value == "<%=FieldTypeEnum.COMBO.getValue()%>") {
					$("#queryParamForm #fieldComboDataTypeTR,#queryParamForm #fieldComboDataTR,#queryParamForm #fieldEditAbleTR").show();
					$("#queryParamForm #fieldDateFormatterTR").hide();
					$("#queryParamForm #fieldEditAble").combobox("enableValidation");
					$("#queryParamForm #fieldComboDataType").combobox("enableValidation");
					$("#queryParamForm #fieldComboData").validatebox("enableValidation");
					$("#queryParamForm #fieldDateFormatter").validatebox("disableValidation");
				} else if (record.value == "<%=FieldTypeEnum.DATE.getValue()%>") {
					$("#queryParamForm #fieldComboDataTypeTR,#queryParamForm #fieldComboDataTR,#queryParamForm #fieldEditAbleTR").hide();
					$("#queryParamForm #fieldDateFormatterTR").show();
					$("#queryParamForm #fieldEditAble").combobox("disableValidation");
					$("#queryParamForm #fieldComboDataType").combobox("disableValidation");
					$("#queryParamForm #fieldComboData").validatebox("disableValidation");
					$("#queryParamForm #fieldDateFormatter").validatebox("enableValidation");
				} else {
					$("#queryParamForm #fieldComboDataTypeTR,#queryParamForm #fieldComboDataTR,#queryParamForm #fieldEditAbleTR").hide();
					$("#queryParamForm #fieldDateFormatterTR").hide();
					$("#queryParamForm #fieldComboDataType").combobox("disableValidation");
					$("#queryParamForm #fieldComboData").validatebox("disableValidation");
					$("#queryParamForm #fieldDateFormatter").validatebox("disableValidation");
					$("#queryParamForm #fieldEditAble").combobox("disableValidation");
				}
			},
			onLoadSuccess : function() {
				$(this).combobox("select", data.fieldType ? data.fieldType : "");
			}
		});
		$("#queryParamForm #fieldRequired").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=YesOrNoEnum",
			value : data.fieldRequired
		});
		$("#queryParamForm #fieldEditAble").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=YesOrNoEnum",
			value : data.fieldEditAble
		});
		$("#queryParamForm #fieldComboDataType").combobox({
			required : true,
			panelHeight : 100,
			editable : false,
			url : "<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=FieldComboDataTypeEnum",
			value : data.fieldComboDataType,
			onSelect : function(record) {
				if (record.value == "<%=FieldComboDataTypeEnum.SELF.getValue()%>") {
					$("#queryParamForm #fieldComboDataTR").tooltip({
						content : "<span>请输入数据源，形如（value1:text1,value2:text2,value3:text3）</span>"
					});
				} else if (record.value == "<%=FieldComboDataTypeEnum.SQL_RESULT.getValue()%>") {
					$("#queryParamForm #fieldComboDataTR").tooltip({
						content : "<span>请输入一个查询sql作为数据源。下拉框显示的文本字段为text，值为value</span>"
					});
				}
			}
		});
		$("#queryParamForm #fieldDateFormatter,#queryParamForm #fieldComboData").validatebox({
			required : true
		});
		$("#queryParamForm").form("load", {
			fieldName : data.fieldName,
			fieldLabel : data.fieldLabel,
			fieldDefaultValue : data.fieldDefaultValue,
			fieldComboData : data.fieldComboData,
			fieldDateFormatter : data.fieldDateFormatter
		});
	});
</script>
