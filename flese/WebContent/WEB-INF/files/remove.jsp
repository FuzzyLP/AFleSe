<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);

	String urlList = KUrls.Files.ListMyFiles.getUrl(true);
	String msgsArray = JspsUtils.getResultMessagesInJS(resultsStoreHouse);
%>


<script type="text/javascript">
	showMsgsArray(<%= msgsArray %>);
	loadAjaxIn('<%=KConstants.JspsDivsIds.filesListDiv %>', '<%=urlList %>');	
</script>



