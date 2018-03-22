<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.mytools.extend.cache.CacheObject"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="com.mytools.utils.SpringContextUtil"%>
<%@page import="com.mytools.extend.cache.MyCache"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	MyCache myCache = SpringContextUtil.getBean(MyCache.class);
	for (Entry<String, CacheObject> entry : myCache.getCache().entrySet()) {
		out.println(entry.getKey() + "=" + JSON.toJSONString(entry.getValue().getValue()) + "<br />");
	}
%>
</body>
</html>