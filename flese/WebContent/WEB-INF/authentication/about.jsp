<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="java.util.ArrayList"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
%>

<%	if (! JspsUtils.isAjax(requestStoreHouse)) { %>
<jsp:include page='../commonHtmlBody.jsp' />
<% } %>

<h3>About FleSe</h3>

Main developer: Victor Pablos Ceruelo (vpablos@babel.ls.fi.upm.es)
<br />
Running since:
<%= JspsUtils.getRunningSince() %>

