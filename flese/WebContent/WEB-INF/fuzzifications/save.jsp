<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="storeHouse.ResultsStoreHouse"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
	String [] msgs = resultsStoreHouse.getResultMessages();
	for (int i=0; i<msgs.length; i++) {
		out.println(msgs[i]);
	}
%>

<!-- END -->