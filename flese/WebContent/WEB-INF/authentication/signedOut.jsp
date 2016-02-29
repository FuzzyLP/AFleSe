<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>

<% 
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
%>

<%	if (! JspsUtils.isAjax(requestStoreHouse)) { %>
<jsp:include page='../commonHtmlBody.jsp' />
<% } %>
<%
	String msgsArray = JspsUtils.getResultMessagesInJS(resultsStoreHouse);
%>

<script type="text/javascript">	
	showMsgsArray(<%= msgsArray %>);
	loadAjaxIn('<%=KConstants.JspsDivsIds.mainSecDivId %>', "<%=KUrls.Auth.Providers.getUrl(true)%>");
</script>

