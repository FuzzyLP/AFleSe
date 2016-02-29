<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="storeHouse.RequestStoreHouse"%>

<%
	String container = KConstants.JspsDivsIds.mainSecDivId;
	String url = KUrls.Ontologies.Main.getUrl(true);
%>

<a href="#" title="Create program file from ontology"
	onclick='return loadAjaxIn("<%=container %>", "<%=url%>");'>Create
	program file from ontology</a>





<!-- END -->
