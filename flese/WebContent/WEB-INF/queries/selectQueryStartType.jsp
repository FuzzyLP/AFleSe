
<%@page import="filesAndPaths.ProgramFileInfo"%>
<%@page import="prologConnector.PredicateInfo"%>
<%@page import="prologConnector.ProgramIntrospection"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="prologConnector.CiaoPrologTermInJava"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>
<%@page import="prologConnector.CiaoPrologQueryAnswer"%>
<%@page import="prologConnector.CiaoPrologProgramIntrospectionQuery"%>
<%@page import="storeHouse.RequestStoreHouse"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
	ProgramIntrospection programIntrospection = resultsStoreHouse.getCiaoPrologProgramIntrospection();	
	PredicateInfo [] predicatesInfos = programIntrospection.getPredicatesInfosByMoreInfoKey(KConstants.MoreInfoTypes.database);
	ProgramFileInfo programFileInfo = programIntrospection.getProgramFileInfo();
	
	String url1 = KUrls.Queries.SelectQuery.getUrl(true) + programFileInfo.getInfoForUrls() + 
	"&" + KConstants.Request.databaseParam + "=";
	String url2 = KUrls.Queries.Evaluate.getUrl(true) + programFileInfo.getInfoForUrls();
	String url3 = KUrls.Queries.ProgramFileActions.getUrl(true) + programFileInfo.getInfoForUrls();
	
	
	String urlListFuzzifications = KUrls.Fuzzifications.List.getUrl(true);
	String fuzzificationParams = "&" + KConstants.Request.fileOwnerParam + "=" + programFileInfo.getFileOwner() +
	"&" + KConstants.Request.fileNameParam + "=" + programFileInfo.getFileName() + 
	"&" + KConstants.Request.mode + "=" + KConstants.Request.modeBasic;
%>


<!-- <form id='queryForm' action='' method='POST' accept-charset='utf-8'>  -->
<!-- 
// action='"+ urlMappingFor('RunQueryRequest') + "&fileName="+fileName+"&fileOwner="+fileOwner + "' ";
// target='" + runQueryTargetiFrameId+ "'>";
 -->
<div id='<%=KConstants.JspsDivsIds.queryStartContainerId%>'
	class='queryStartContainerTable'>
	<div class='queryStartContainerTableRow'>
		<div class='queryStartContainerTableCell1'>Your query: I'm
			looking for a</div>
		<div class='queryStartContainerTableCell2'
			id='<%=KConstants.JspsDivsIds.chooseQueryStartTypeContainerId%>'>
			<select name="<%=KConstants.Request.databaseParam %>"
				id="<%=KConstants.Request.databaseParam %>"
				onchange="selectedQueryStartTypeChanged(this, '<%=url1 %>', '<%=url3 %>');">
				<%=JspsUtils.comboBoxDefaultValue()%>
				<%
	for (int i=0; i<predicatesInfos.length; i++) {
		String desc = predicatesInfos[i].getPredicateName();
%>
				<option id='<%=desc%>' title='<%=desc%>' value='<%=desc%>'><%=JspsUtils.getPrologNameInColloquialLanguage(desc)%></option>
				<%
	}
%>
			</select>
		</div>
	</div>
</div>

<!-- Initialize the query lines counter -->
<input type="hidden" name='<%=KConstants.Request.linesCounterParam %>'
	value="0" id='<%=KConstants.Request.linesCounterParam %>'>

<div id='<%=KConstants.JspsDivsIds.queryLinesContainerId%>'
	class='queryLinesContainerTable'></div>

<div id='<%=KConstants.JspsDivsIds.searchOrPersonalizeTableId%>'
	class='searchOrPersonalizeTable'>
	<div class='searchOrPersonalizeTableRow'>
		<div class='searchOrPersonalizeTableCell'>
			<input type='submit' value='Search'
				onclick="return evaluateQuery('<%=url2 %>');">
		</div>
		<!-- 
		<div class='searchOrPersonalizeTableCell'>&nbsp; or &nbsp;</div>
		<div class='searchOrPersonalizeTableCell'>
			<INPUT type='submit' value='Personalize Program File'
				onclick='return personalizeProgramFile("<%=urlListFuzzifications%>", "<%= fuzzificationParams %>", "<%=programFileInfo.getFileName() %>");'>
		</div>
		-->
	</div>
</div>
<!--  </form><br />  -->

<script type="text/javascript">
		selectedQueryStartTypeChanged("");		
	</script>
