
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="java.util.ArrayList"%>

<% RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request); %>

<%	if (! JspsUtils.isAjax(requestStoreHouse)) { %>
<jsp:include page='commonHtmlBody.jsp' />
<% } %>

<%
ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
ArrayList<String> msgs = new ArrayList<String>();
if (KConstants.PathsMgmt.stateErrorConfigFile)
{
	msgs.add(KConstants.AppMsgs.exception1);
	msgs.add(KConstants.AppMsgs.exception4);
	msgs.add(KConstants.AppMsgs.exception5);
	KConstants.PathsMgmt.stateErrorConfigFile = false;
} else {
	if (KConstants.PathsMgmt.stateErrorConfigFile2)
	{
		msgs.add(KConstants.AppMsgs.exception1);
		msgs.add(KConstants.AppMsgs.exception6 + " " + KConstants.PathsMgmt.reasonError2);
		msgs.add(KConstants.AppMsgs.exception7);
		KConstants.PathsMgmt.stateErrorConfigFile2 = false;
		KConstants.PathsMgmt.reasonError2 = "";
	} else {
		msgs.add(KConstants.AppMsgs.exception1);
		msgs.add(KConstants.AppMsgs.exception2 + KConstants.Application.AppBugsEmail + KConstants.AppMsgs.exception3);
		msgs.add("");
		msgs.add(resultsStoreHouse.getExceptionMsg());
	}
}


String msgsArray = JspsUtils.getMessagesInJS(msgs);
%>



<script type="text/javascript">
if (typeof(clearMainSection) == "function") {
	clearMainSection();
}
if (typeof(window.parent.clearMainSection) == "function") {
	window.parent.clearMainSection();
}
if (typeof(clearMsgsSection) == "function") {
	clearMsgsSection();
}
if (typeof(window.parent.clearMsgsSection) == "function") {
	window.parent.clearMsgsSection();
}
if (typeof(showMsgsArray) == "function") {
	showMsgsArray(<%= msgsArray %>);
}
if (typeof(window.parent.showMsgsArray) == "function") {
	window.parent.showMsgsArray(<%= msgsArray %>);
}
if (typeof(window.close) == "function") {
	window.close()
}
</script>





<!--  END -->