<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
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
	loadAjaxIn('<%=KConstants.JspsDivsIds.mainSecDivId %>', "<%=KUrls.Queries.SelectProgramFile.getUrl(true)%>");

	// Window onload trigger to load contents after page load.
	// $(window).load(function(){
		/*code goes here*/ 
	// });
</script>
