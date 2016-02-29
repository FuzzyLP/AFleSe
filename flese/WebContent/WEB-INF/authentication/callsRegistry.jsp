<%@page import="storeHouse.RegistryStoreHouse"%>
<%@page import="storeHouse.SessionStoreHouse"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="constants.KConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>
<jsp:include page="../commonHtmlHead.jsp" />

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	SessionStoreHouse sessionStoreHouse = JspsUtils.getSessionStoreHouse(requestStoreHouse);
	// ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
	String [] registryEntries = new String[0];
	if (sessionStoreHouse != null) {
		registryEntries = sessionStoreHouse.getRegistryStoreHouse();
	}
%>

<body>
<h3>FleSe User Session Registry</h3>

<table id='registry' class='registryTable'>
<% for (int i=0; i<registryEntries.length; i++) { %>
	<%=registryEntries[i] %>
<% } %>
</table>

<script type="text/javascript">
//	writeHtmlInContainer('registry', '', false);
</script>
</body>