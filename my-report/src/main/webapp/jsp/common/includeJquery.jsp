<%@ taglib prefix="t" uri="/my-tags" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<t:set beanName="jsPath" value="/jquery-web"/>

<script src="<t:write name='jsPath' />/jquery/jquery-1.11.2.min.js"></script>
<script src="<t:write name='jsPath' />/jquery/easyui/jquery.easyui.min.js"></script>
<script src="<t:write name='jsPath' />/jquery/easyui/easyui-lang-zh_CN.js"></script>
<script src="<t:write name='jsPath' />/jquery/easyui/easyui.default.js"></script>
<script src="<t:write name='jsPath' />/jquery/jquery.json.js"></script>
<script src="<t:write name='jsPath' />/myTags/datePicker/WdatePicker.js"></script>
<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myDialog.js"></script>
<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myDatagrid.js"></script>
<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myTabs.js"></script>
<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myAccordion.js"></script>
<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myTree.js"></script>
<script src="<t:write name='jsPath' />/myTags/myJqueryPlugins/js/easyui.hp.myCombotree.js"></script>
<link rel="stylesheet" type="text/css" href="<t:path />/css/style.css" />
<link rel="stylesheet" type="text/css" href="<t:path />/css/css.css" />
<link rel="stylesheet" type="text/css" href="<t:write name='jsPath' />/jquery/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<t:write name='jsPath' />/jquery/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<t:write name='jsPath' />/myTags/myJqueryPlugins/css/easyui.hp.myAccordion.css" />

<script>
	$.ajaxSetup({
		cache : false,
		dataType : "json",
		type : "POST",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest + "," + textStatus + "," + errorThrown);
		}
	});
</script>

