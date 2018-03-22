<%@page import="com.myreport.enums.YesOrNoEnum"%>
<%@page import="com.myreport.enums.StatusEnum"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="t" uri="/my-tags"%>
<!doctype html>
<form name="reportInfoEditForm" id="reportInfoEditForm" method="post">
	<div id="columnMenu" class="easyui-menu" data-options="onClick:menuHandler" style="width:120px;">
		<div data-options="iconCls:'icon-edit'">修改</div>
		<div data-options="iconCls:'icon-remove'">删除</div>
	</div>
	<input type="hidden" name="reportId" id="reportId" />
	<input type="hidden" name="executeSql" id="executeSql" />
	<input type="hidden" name="queryConditions" id="queryConditions" />
	<input type="hidden" name="paramKeys" id="paramKeys" />
	<input type="hidden" name="tableParams" id="tableParams" />
	<table cellpadding="0" cellspacing="0" border="1" width="98%" align="center" style="margin-top:20px;table-layout:fixed">
		<tr>
			<td width="20%" align="right">报表名称：</td>
			<td width="80%">
				<input name="reportName" id="reportName" class="easyui-validatebox" data-options=" 
					required : true
				" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">数据库：</td>
			<td>
				<input name="databaseId" id="databaseId" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<tr>
			<td align="right">所属分组：</td>
			<td>
				<input name="groupId" id="groupId" />
				<span class='requriedInput'>*</span>
			</td>
		</tr>
		<%-- <tr>
			<td align="right">自适应网格列：</td>
			<td>
				<input name="fitColumns" id="fitColumns" />
				<span class='requriedInput'>*</span>
			</td>
		</tr> --%>
		<tr>
			<td align="right">状态：</td>
			<td>
				<input name="status" id="status" class="easyui-combobox" data-options="
					url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=StatusEnum',
					panelHeight : 50,
					editable : false,
					required : true,
					missingMessage : '状态必须选择！'
				" />
			</td>
		</tr>
		<tr>
			<td align="right">排序：</td>
			<td>
				<input name="sortNumber" id="sortNumber" class="easyui-numberbox" data-options="
					required : true,
					min : 0
				" />
			</td>
		</tr>
		<tr>
			<td align="right">sql语句：</td>
			<td><textarea name="originalSql" id="originalSql" rows="10" cols="90" class="easyui-validatebox" data-options="required : true"></textarea></td>
		</tr>
		<tr>
			<%-- <td align="right">报表头部：</td> --%>
			<td colspan="2">
				<table id="tableColumnsTable"></table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table id="paramsTable"></table>
			</td>
		</tr>
	</table>
</form>
<script>
	var lastColumnIndex = -1, lastFrozenColumnIndex = -1;
	function menuHandler(item) {
		var options = $("#reportInfoEditForm #tableColumnsTable").datagrid("options");
		var columns = options.columns;
		var frozenColumns = options.frozenColumns;
		if (!columns[0]) {
			columns = [[]];
		}
		if (!frozenColumns[0]) {
			frozenColumns = [[]];
		}
		var arr = lastColumnIndex >= 0 ? columns[0] : frozenColumns[0];
		var index = lastColumnIndex >= 0 ? lastColumnIndex : lastFrozenColumnIndex;
		if (item.iconCls == "icon-edit") {
			for (var i = 0; i < arr.length; i++) {
				if (i == index) {
					addColumn(arr[i], "edit");
					return;
				}
			}
		} else if (item.iconCls == "icon-remove") {
			for (var i = 0; i < arr.length; i++) {
				if (i == index) {
					arr.splice(i, 1);
					$("#reportInfoEditForm #tableColumnsTable").datagrid({
						columns : columns,
						frozenColumns : frozenColumns
					});
					return;
				}
			}
		}
	}
	function addColumn(item, type) {
		var div = $("<div>").appendTo($(document.body));
		$(div).myDialog({
			width : 400,
			height : 400,
			title : "列属性",
			href : "<t:path />/jsp/reportManage/reportInfo/reportInfoColumnEdit.jsp?obj=" + encodeURI($.toJSON($.extend(item, {
				type : type
			}))),
			modal : true,
			collapsible : true,
			cache : false,
			buttons : [{
				text : "确定",
				iconCls : "icon-save",
				handler : function() {
					if (!$("#addTableColumnsForm").form("validate")) {
						return;
					}
					var options = $("#reportInfoEditForm #tableColumnsTable").datagrid("options");
					var columns = options.columns;
					var frozenColumns = options.frozenColumns;
					if (!columns[0]) {
						columns = [[]];
					}
					if (!frozenColumns[0]) {
						frozenColumns = [[]];
					}
					var frozenAble = $("#addTableColumnsForm #frozenAble").combobox("getValue");
					var downAble = $("#addTableColumnsForm #downAble").combobox("getValue");
					var downSQL = $("#addTableColumnsForm #downSQL").val();
					var obj = {
						title : $("#addTableColumnsForm #columnTitle").val(),
						field : $("#addTableColumnsForm #columnName").val(),
						width : $("#addTableColumnsForm #columnWidth").val(),
						align : "center",
						sortable : $("#addTableColumnsForm #sortable").combobox("getValue") == "<%=YesOrNoEnum.YES.getValue()%>",
						frozenAble : frozenAble,
						downAble : downAble,
						downSQL : downSQL
					};
					var statisType = $("#addTableColumnsForm #statisType").combobox("getValues");
					obj.statisType = statisType;
					if (type == "add") {
						if (frozenAble == "<%=YesOrNoEnum.YES.getValue()%>") {
							frozenColumns[0].push(obj);
						} else {
							columns[0].push(obj);
						}
					} else if (type == "edit") {
						if (lastColumnIndex >= 0) {
							if (frozenAble == "<%=YesOrNoEnum.YES.getValue()%>") {
								columns[0].splice(lastColumnIndex, 1);
								frozenColumns[0].push(obj);
							} else {
								columns[0][lastColumnIndex] = obj;
							}
						} else {
							if (frozenAble == "<%=YesOrNoEnum.YES.getValue()%>") {
								frozenColumns[0][lastFrozenColumnIndex] = obj;
							} else {
								frozenColumns[0].splice(lastFrozenColumnIndex, 1);
								columns[0].push(obj);
							}
						}
					}
					$("#reportInfoEditForm #tableColumnsTable").datagrid({
						columns : columns,
						frozenColumns : frozenColumns
					});
					$(div).myDialog("close");
				}
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					$(div).dialog("close");
				}
			}]
		});
	}
	$(function() {
		$("#reportInfoEditForm input").attr("size", 30);
		var reportId = <t:write name='reportId' />;
		$.post("<t:path />/jsp/queryAllReportInfo.do?reportId=" + reportId, function(data) {
			if (!data || data.length == 0) {
				data = [{}];
			}
			var tableParams = data[0].tableParams;
			if (tableParams) {
				tableParams = $.parseJSON(tableParams);
			}
			/* $("#reportInfoEditForm #fitColumns").combobox({
				panelHeight : 100,
				editable : false,
				url : "<t:path />/jsp/noFilter.do?method=getEnumForSelect&className=YesOrNoEnum",
				value : tableParams.fitColumns
			}); */
			$("#reportInfoEditForm").form("load", {
				reportId : data[0].reportId ? data[0].reportId : "0",
				reportName : data[0].reportName,
				originalSql : data[0].originalSql,
				executeSql : data[0].executeSql,
				queryConditions : data[0].queryConditions,
				sortNumber : data[0].sortNumber,
				paramKeys : data[0].paramKeys,
				tableParams : data[0].tableParams
			});
			$("#reportInfoEditForm #status").combobox({
				url : '<t:path />/jsp/noFilter.do?method=queryEnumForSelect&className=StatusEnum',
				panelHeight : 50,
				editable : false,
				required : true,
				missingMessage : '状态必须选择！',
				value : data[0].status ? data[0].status : "<%=StatusEnum.A.toString()%>"
			});
			$("#reportInfoEditForm #groupId").myCombotree({
				required : true,
				editable : false,
				value : data[0].groupId,
				ajaxParam : {
					url : '<t:path />/jsp/queryAllReportGroup.do'
				},
				idField : "groupId",
				pidField : "parentGroupId",
				textField : "groupName",
				onClick : function(node) {
					var tree = $("#reportInfoEditForm #groupId").combotree("tree");
					if (!tree.tree("isLeaf", node.target)) {
						tree.tree("toggle", node.target);
						$("#reportInfoEditForm #groupId").combotree("showPanel");
					}
				},
				onLoadSuccess : function() {
					$("#reportInfoEditForm #groupId").combotree("tree").tree("expandAll");
				},
				animate : true,
				lines : true
			});
			$("#reportInfoEditForm #databaseId").combobox({
				required : true,
				url : '<t:path />/jsp/queryAllDatabaseInfo.do',
				valueField : 'databaseId',
				textField : 'databaseTitle',
				editable : false,
				value : data[0].databaseId,
				loadFilter : function(data) {
					return data.rows;
				}
			});
			$("#reportInfoEditForm #tableColumnsTable").myDatagrid({
				fit : false,
				fitColumns : false,
				title : "报表列头",
				columns : reportId == 0 ? [[]] : $.parseJSON(data[0].tableParams).columns,
				frozenColumns : reportId == 0 ? [[]] : $.parseJSON(data[0].tableParams).frozenColumns,
				cache : false,
				data : [[]],
				enableColumnMove : false,
				selectColumn : false,
				onHeaderContextMenu : function(e, field) {
					e.preventDefault();
					lastColumnIndex = -1;
					lastFrozenColumnIndex = -1;
					var columns = $("#reportInfoEditForm #tableColumnsTable").datagrid("getColumnFields");
					var frozenColumns = $("#reportInfoEditForm #tableColumnsTable").datagrid("getColumnFields", true);
					for (var i = 0; i < columns.length; i++) {
						if (field == columns[i]) {
							lastColumnIndex = i;
							break;
						}
					}
					for (var i = 0; i < frozenColumns.length; i++) {
						if (field == frozenColumns[i]) {
							lastFrozenColumnIndex = i;
							break;
						}
					}
					$("#columnMenu").menu("show", {
						left : e.clientX,
						top : e.clientY
					});
				},
				toolbar : [{
					iconCls : "icon-add",
					text : "新增",
					handler : function() {
						addColumn({}, "add");
					}
				}],
				onLoadSuccess : function() {
					$(this).myDatagrid("columnMoving");
				}
			});
			$("#reportInfoEditForm #paramsTable").datagrid({
				title : "查询条件",
				fit : false,
				fitColumns : true,
				nowrap : true,
				striped : true,
				singleSelect : true,
				columns : [[{
					title : "field",
					field : "fieldName",
					width : 100,
					align : "center"
				}, {
					title : "显示标签",
					field : "fieldLabel",
					width : 100,
					align : "center"
					/* editor : {
						type : "validatebox",
						options : {
							required : true
						}
					} */
				}, {
					title : "类型",
					field : "fieldTypeLabel",
					width : 100,
					align : "center"
					/*formatter:function(value, row){
						return row.label;
					},
					editor : {
						type : "combobox",
						options : {
							required : true,
							panelHeight : 100,
							editable : false,
							data : [
								{text : "普通文本",value : "1"},
								{text : "数字框",value : "2"},
								{text : "下拉框",value : "3"},
								{text : "日期",value : "4"}
							]
						}
					} */
				}, {
					title : "编辑",
					field : "more",
					width : 100,
					align : "center",
					formatter : function() {
						return "<a href='javascript:showMore();'>编辑</a>";
					}
				}
				/*, {
					title : "是否必填",
					field : "fieldRequired",
					width : 100,
					align : "center",
					formatter:function(value, row){
						return row.isRequiredLabel;
					},
					editor : {
						type : "combobox",
						options : {
							required : true,
							panelHeight : 100,
							editable : false,
							data : [
								{text : "是",value : "1"},
								{text : "否",value : "0"}
							]
						}
					}
				}, {
					title : "默认值",
					field : "fieldDefaultValue",
					width : 100,
					align : "center",
					editor : {
						type : "text"
					}
				}, {
					title : "日期格式",
					field : "fieldDateFormatter",
					width : 100,
					align : "center",
					editor : {
						type : "text"
					}
				}, {
					title : "下拉数据源",
					field : "fieldComboData",
					width : 100,
					align : "center",
					editor : {
						type : "text"
					}
				} */
				]],
				/* onEndEdit : function(rowIndex, rowData, changes) {
					var editors = $("#reportInfoEditForm #paramsTable").datagrid("getEditors", rowIndex);
					for (var i = 0; i < editors.length; i++) {
						if (editors[i].field == "fieldType") {
							var label = $(editors[i].target).combobox("getText");
							rowData.label = label;
						}
						if (editors[i].field == "fieldRequired") {
							var label = $(editors[i].target).combobox("getText");
							rowData.isRequiredLabel = label;
						}
					}
				},
				onClickRow : function(rowIndex, rowData) {
					$("#reportInfoEditForm #paramsTable").datagrid("beginEdit", rowIndex);
				},*/
				data : data[0].queryConditions ? $.parseJSON(data[0].queryConditions) : [],
				onLoadSuccess : function() {
					$(this).datagrid("enableDnd");
				}
			});
		});
	});
	function showMore() {
		var selectData = $("#reportInfoEditForm #paramsTable").datagrid("getSelected");
		var div = $("<div>").appendTo($(document.body));
		$(div).myDialog({
			width : 500,
			height : 400,
			title : "更多参数设置",
			href : "<t:path />/jsp/reportManage/reportInfo/reportInfoParamEdit.jsp?param=" + encodeURI($.toJSON(selectData)),
			modal : true,
			collapsible : true,
			cache : false,
			buttons : [{
				text : "刷新",
				iconCls : "icon-reload",
				handler : function() {
					$(div).dialog("refresh");
				}
			},  {
				text : "确定",
				iconCls : "icon-ok",
				handler : function() {
					if (!$("#queryParamForm").form("validate")) {
						return;
					}
					var data = {
						fieldName : $("#queryParamForm #fieldName").val(),
						fieldLabel : $("#queryParamForm #fieldLabel").val(),
						fieldType : $("#queryParamForm #fieldType").combobox("getValue"),
						fieldTypeLabel : $("#queryParamForm #fieldType").combobox("getText"),
						fieldDateFormatter : $("#queryParamForm #fieldDateFormatter").val(),
						fieldComboDataType : $("#queryParamForm #fieldComboDataType").combobox("getValue"),
						fieldComboData : $("#queryParamForm #fieldComboData").val(),
						fieldRequired : $("#queryParamForm #fieldRequired").combobox("getValue"),
						fieldDefaultValue : $("#queryParamForm #fieldDefaultValue").val(),
						fieldEditAble : $("#queryParamForm #fieldEditAble").combobox("getValue")
					};
					var index = $("#reportInfoEditForm #paramsTable").datagrid("getRowIndex", selectData);
					$("#reportInfoEditForm #paramsTable").datagrid("updateRow", {
						index : index,
						row : data
					});
					$(div).dialog("close");
				}
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					$(div).dialog("close");
				}
			}]
		});
	}
</script>