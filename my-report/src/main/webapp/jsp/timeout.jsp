<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="t" uri="/my-tags" %>
<html>
<head>
	<title>session过期了</title>
	<jsp:include page="/jsp/common/includeJquery.jsp" flush="true" />
</head>
<body>
session过期了
<input type="button" id="btn" value="重新登录" />
<script>
	$("#btn").on("click", function() {
		window.top.location.href = "<t:path />/jsp/logout.jsp";
	});
</script>
</body>
</html>

