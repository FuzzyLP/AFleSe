<%@page import="urls.ServerAndAppUrls"%>
<%@page import="auxiliar.Dates"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	// This sets the app path for all the application.
	ServerAndAppUrls.getAppUrl(request);
%>

<!DOCTYPE html>
<!-- beginning of commonHtmlHead -->
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FleSe: Flexible Searches in Databases</title>

<link rel="stylesheet" type="text/css" href="js_and_css/style.css" />
<link rel="stylesheet" type="text/css"
	href="js_and_css/jquery-ui-1.10.3.custom.css" />

<script type="text/javascript" src="js_and_css/ba-debug.js"></script>
<script type="text/javascript" src="js_and_css/ba-debug-init.js"></script>
<script type="text/javascript" src="js_and_css/jquery-1.11.0.js"></script>
<script type="text/javascript" src="js_and_css/jquery-ui-1.10.4.js"></script>

<script type="text/javascript" src="js_and_css/clientSoftware.js"></script>
<script type="text/javascript" src="js_and_css/highcharts-all-3.0.9.js"></script>
<script type="text/javascript"
	src="js_and_css/auxiliarJS.jsp?date=<%=Dates.getStringOfCurrentDate()%>"></script>
<script type="text/javascript"
	src="js_and_css/answersJS.jsp?date=<%=Dates.getStringOfCurrentDate()%>"></script>
<script type="text/javascript"
	src="js_and_css/fuzzificationsJS.jsp?date=<%=Dates.getStringOfCurrentDate()%>"></script>
<script type="text/javascript"
	src="js_and_css/filesJS.jsp?date=<%=Dates.getStringOfCurrentDate()%>"></script>
<script type="text/javascript"
	src="js_and_css/ontologiesJS.jsp?date=<%=Dates.getStringOfCurrentDate()%>"></script>
</head>
<!-- end of commonHtmlHead -->