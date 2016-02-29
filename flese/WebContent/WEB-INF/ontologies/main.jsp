<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="storeHouse.RequestStoreHouse"%>

<%
	String ontologyUrlFieldId = KConstants.JspsDivsIds.ontologyUrlFieldId;
	String ontologyQueryResultsDivId = KConstants.JspsDivsIds.ontologyQueryResultsDivId;
	String queryUrl = KUrls.Ontologies.MainQuery.getUrl(true);
	String defaultServiceEndpoint = KConstants.Ontologies.defaultServiceEndpoint;
%>

<p style="font-family:arial;color:red;font-size:20px;align:center">
This is still under development !!!
</p>

<form action="#">

	Enter the ontology url <br> 
		<input type="text" name="<%=ontologyUrlFieldId%>" id="<%=ontologyUrlFieldId%>" value="<%=defaultServiceEndpoint%>"> 
		<input type="button" value="Process Ontology"
		onclick="return sendOntologyMainQuery('<%=ontologyUrlFieldId%>', '<%=queryUrl%>', '<%=ontologyQueryResultsDivId%>');">

</form>

<br>

<div id="<%=ontologyQueryResultsDivId%>"></div>

<script type="text/javascript">
	// loadAjaxIn('<%=KConstants.JspsDivsIds.ontologyStartDivId %>', '<%=KUrls.Files.UploadDiv.getUrl(true) %>');
</script>

<!-- END -->
