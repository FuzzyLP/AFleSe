<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="storeHouse.RequestStoreHouse"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);

	String urlList = KUrls.Files.ListMyFiles.getUrl(true);
	String msgsArray = JspsUtils.getResultMessagesInJS(resultsStoreHouse);
%>

<script type="text/javascript">
	// Update the files list.
	if (typeof(loadAjaxIn) == "function") {
		loadAjaxIn('<%=KConstants.JspsDivsIds.filesListDiv %>', '<%=urlList %>');
	}
	if (typeof(window.parent.loadAjaxIn) == "function") {
		window.parent.loadAjaxIn('<%=KConstants.JspsDivsIds.filesListDiv %>', '<%=urlList %>');
	}
	// Clean the status div.
	if (typeof(showMsgsArrayInDiv) == "function") {
		showMsgsArrayInDiv("<%=KConstants.JspsDivsIds.uploadStatusDivId%>", <%=msgsArray%>);
	}
	if (typeof(window.parent.showMsgsArrayInDiv) == "function") {
		window.parent.showMsgsArrayInDiv("<%=KConstants.JspsDivsIds.uploadStatusDivId%>", <%=msgsArray%>);
	}
	// Clean the messages div.
	if (typeof(showMsgsArray) == "function") {
		showMsgsArray(<%=JspsUtils.getEmptyArrayMessagesInJs()%>);
	}
	if (typeof(window.parent.showMsgsArray) == "function") {
		window.parent.showMsgsArray(<%=JspsUtils.getEmptyArrayMessagesInJs()%>);
	}	
</script>





<!-- END -->
