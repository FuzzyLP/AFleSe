<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="constants.KConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
%>

<%	if (! JspsUtils.isAjax(requestStoreHouse)) { %>
<jsp:include page='../commonHtmlBody.jsp' />
<% } %>

<% 
	String containerId = KConstants.JspsDivsIds.mainSecDivId;
	String title1 = "<h3>FleSe Logs - Queries</h3>";
	String title2 = "<h3>FleSe Logs - Users Signed In</h3>";
	String textArea1 = "<textarea id=\"logs1\" cols=\"150\" rows=\"40\" name=\"logs1\" readonly=\"readonly\"></textarea>";
	String textArea2 = "<textarea id=\"logs2\" cols=\"150\" rows=\"40\" name=\"logs2\" readonly=\"readonly\"></textarea>";
%>
<script type="text/javascript">
	writeHtmlInContainer('<%=containerId%>', '<%=title1%><%=textArea1%><%=title2%><%=textArea2%>', true);
	
	writeHtmlInContainer('logs1', '<%=JspsUtils.getLogsQueries()%>', false);
	writeHtmlInContainer('logs2', '<%=JspsUtils.getLogsSignedUsers()%>', false);
</script>
