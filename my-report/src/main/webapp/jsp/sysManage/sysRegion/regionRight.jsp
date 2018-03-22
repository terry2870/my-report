<%@page import="com.myreport.enums.ReturnCodeEnum"%>
<%@page import="com.myreport.enums.RegionTypeEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<form name="regionEditForm" id="regionEditForm" method="post">
	<input type="hidden" name="parentRegionId" id="parentRegionId" value="<t:write name='parentRegionId' />" />
	<input type="hidden" name="regionId" id="regionId" value="<t:write name='regionId' />" />
	<table cellpadding="0" cellspacing="0" border="0" width="80%" style="margin-top:10px">
		<tr>
			<td width="30%" align="right">地区名称：</td>
			<td width="70%">
				<input type="text" name="regionName" id="regionName" class="easyui-validatebox" data-options="
					required:true,
					validType : 'checkName',
					invalidMessage : '请输入正确的地区名，地区名不能输入形如（@#$）等特殊字符'
				" />
			</td>
		</tr>
		<tr>
			<td align="right">地区类型：</td>
			<td>
				<input name="regionType" id="regionType" class="easyui-combobox" data-options="
					url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=RegionTypeEnum',
					readonly : true,
					editable : false,
					required:true
				" />
			</td>
		</tr>
		<tr>
			<td align="right">排序：</td>
			<td>
				<input name="sortNumber" id="sortNumber" class="easyui-numberbox" data-options="min : 0,max : 1000,required : true" />
			</td>
		</tr>
		<tr style="padding-top:20px">
			<td align="center" colspan="2" id="buttons">
				<a id="addProvinceMenu" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addRegion('<%=RegionTypeEnum.PROVINCE.toString()%>');">增加省份</a>
				<a id="addCityMenu" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addRegion('<%=RegionTypeEnum.CITY.toString()%>');">增加地市</a>
				<a id="addCity2Menu" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addRegion('<%=RegionTypeEnum.CITY_2.toString()%>');">新增区/县</a>
				<a id="addTowmButton" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addRegion('<%=RegionTypeEnum.TOWN.toString()%>');">新增街道/乡镇</a>
				<a id="addCountryButton" class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="#" onclick="addRegion('<%=RegionTypeEnum.COUNTRY.toString()%>');">新增社区/村</a>
				<a id="deleteRegion" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" href="#" onclick="delRegion();">删除节点</a>
				<a id="saveRegion" class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="#" onclick="saveRegion();">保存</a>
			</td>
		</tr>
	</table>
</form>
<script>
	$(function() {
		$("#regionEditForm").form("load", {
			regionName : "<t:write name='regionName' />",
			regionType : "<t:write name='regionType' />",
			sortNumber : "<t:write name='sortNumber' />"
		});
		var regionType = "<t:write name='regionType' />";
		var regionId = "<t:write name='regionId' defaultValue='0' />";
		if (!regionType) {
			$("#saveRegion").hide();
		}
		if (regionId == 0) {
			$("#addCityMenu,#addCity2Menu,#addTowmButton,#addCountryButton,#addRegionButton,#deleteRegion").hide();
		}
		//$("#addCityMenu").hide();
		if (regionType == "<%=RegionTypeEnum.PROVINCE.toString()%>") {
			$("#addCity2Menu,#addTowmButton,#addCountryButton").hide();
		} else if (regionType == "<%=RegionTypeEnum.CITY.toString()%>") {
			$("#addCityMenu,#addTowmButton,#addCountryButton").hide();
		} else if (regionType == "<%=RegionTypeEnum.CITY_2.toString()%>") {
			$("#addCityMenu,#addCity2Menu,#addCountryButton").hide();
		} else if (regionType == "<%=RegionTypeEnum.TOWN.toString()%>") {
			$("#addCityMenu,#addCity2Menu,#addTowmButton").hide();
		} else if (regionType == "<%=RegionTypeEnum.COUNTRY.toString()%>") {
			$("#addCityMenu,#addCity2Menu,#addTowmButton,#addCountryButton").hide();
		}
	});
	function addRegion(regionType) {
		var treeNode = $("#regionLeft").myTree("getSelected");
		var data = {
			regionId : 0,
			regionType : regionType,
			parentRegionId : regionType == "<%=RegionTypeEnum.PROVINCE.toString()%>" ? 0 : treeNode.attributes.regionId
		};
		$("#regionRight").panel("refresh", "<t:path />/jsp/sysManage/sysRegion/regionRight.jsp?" + $.param(data, true));
	};
	function delRegion() {
		if ($("#regionId").val() == "") {
			window.top.$.messager.alert("错误", "请选择一个地区", "error");
			return;
		}
		window.top.$.messager.confirm("确认", "您确定要删除该地区吗？", function(flag) {
			if (flag) {
				window.top.$.messager.progress({
					title : "正在执行",
					msg : "正在执行，请稍后...",
					interval : 500,
					text : ""
				});
				$.ajax({
					url : "<t:path />/jsp/deleteSysregion.do",
					data : {
						regionIds : "<t:write name='regionId' />"
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						window.top.$.messager.progress("close");
						if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
							$('#regionLeft').myTree("reload");
							$.messager.show({
								title : "提示",
								msg : "删除地区成功！"
							});
							$("#regionEditForm").form("clear");
						} else {
							window.top.$.messager.alert("删除地区失败", data.returnInfo, "error");
						}
					}
				});
			}
		});
	};
	function saveRegion() {
		$("#regionEditForm").form("submit", {
			url : "<t:path />/jsp/saveSysRegion.do",
			onSubmit : function() {
				if (!$("#regionEditForm").form("validate")) {
					return false;
				}
				window.top.$.messager.progress({
					title : "正在执行",
					msg : "正在执行，请稍后...",
					interval : 500,
					text : ""
				});
				return true;
			},
			success : function(data) {
				window.top.$.messager.progress("close");
				if (data) {
					data = $.parseJSON(data);
					if (data.returnCode == "<%=ReturnCodeEnum.CODE_OK.toString()%>") {
						$("#regionLeft").myTree("reload");
						$.messager.show({
							title : "提示",
							msg : "保存成功！"
						});
						$("#regionEditForm").form("clear");
					} else {
						window.top.$.messager.alert("出错", data.returnInfo, "error");
					}
				} else {
					window.top.$.messager.alert("出错", data.returnInfo, "error");
				}
			}
		});
	};
</script>


