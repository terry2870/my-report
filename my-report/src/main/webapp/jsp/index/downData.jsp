<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="t" uri="/my-tags" %>
<table id="downDataTable"></table>
<script>
	$(function() {
		var totalPageSize = 10;
		var MY_REPORT_reportId = <t:write name="MY_REPORT_reportId" />;
		var MY_REPORT_field = "<t:write name='MY_REPORT_field' />";
		var rowData = <t:write name="rowData" />;
		$.post("<t:path />/jsp/getDownData.do", $.extend(rowData, {
			MY_REPORT_reportId : MY_REPORT_reportId,
			MY_REPORT_field : MY_REPORT_field,
			page : 1,
			rows : totalPageSize
		}), function(data) {
			var columns = [[]];
			if (data && !$.isEmptyObject(data)) {
				for (var o in data.rows[0]) {
					columns[0].push({
						title : o,
						field : o,
						width : 100,
						align : "center"
					});
				}
				$("#downDataTable").myDatagrid({
					title : "下转详情",
					fit : true,
					fitColumns : false,
					nowrap : true,
					striped : true,
					cache : false,
					pagination : true,
					rownumbers : true,
					showFooter : true,
					singleSelect : true,
					data : data,
					columns : columns,
					pageSize : totalPageSize,
					pageList : [10],
					onLoadSuccess : function(json) {
						var pager = $("#downDataTable").datagrid("getPager");
						var pagerOptions = pager.pagination("options");
						pagerOptions.onSelectPage = function(pageNumber, pageSize) {
							$.post("<t:path />/jsp/getDownData.do", $.extend(rowData, {
								MY_REPORT_reportId : MY_REPORT_reportId,
								MY_REPORT_field : MY_REPORT_field,
								page : pageNumber,
								rows : pageSize
							}), function(d) {
								if (d && !$.isEmptyObject(d)) {
									$("#downDataTable").myDatagrid("loadData", d.rows);
									pager.pagination("refresh", {
										total : d.total,
										pageNumber : pageNumber
									});
								}
							}, "json");
						};
					}
				});
			}
		}, "json");
	});
</script>

