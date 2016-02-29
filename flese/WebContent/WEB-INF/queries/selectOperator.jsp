
<%@page import="prologConnector.moreInfo.PredMoreInfoInterface"%>
<%@page import="auxiliar.Conversors"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="constants.KUrls"%>
<%@page import="prologConnector.CiaoPrologQueryAnswer"%>
<%@page import="prologConnector.CiaoPrologProgramIntrospectionQuery"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="prologConnector.PredicateInfo"%>
<%@page import="prologConnector.ProgramIntrospection"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
	ProgramIntrospection programIntrospection = resultsStoreHouse.getCiaoPrologProgramIntrospection();
	
	String linesCounter = requestStoreHouse.getRequestParameter(KConstants.Request.linesCounterParam);
	// int lineIndex = Conversors.toInt(lineIndexString);
	String database = requestStoreHouse.getRequestParameter(KConstants.Request.databaseParam);
	String predicate = requestStoreHouse.getRequestParameter(KConstants.Request.predicateParam);
	
	String lineNumber = requestStoreHouse.getRequestParameter(KConstants.Request.lineNumberParam);
	String lineId = requestStoreHouse.getRequestParameter(KConstants.Request.lineIdParam);
	
	String [] type = {database, null};
	PredicateInfo predicateInfo = programIntrospection.getPredicateInfo(predicate);
	String [] [] predicateType = predicateInfo.getFullyDefinedTypes(type);
	
	PredicateInfo predicateInfoOperators = programIntrospection.getPredicateInfo("rfuzzy_compute_defined_operators");
	PredMoreInfoInterface predMoreInfo = predicateInfoOperators.getPredicateMoreInfoAs("defined_operators");
	String [][] operators = predMoreInfo.getOperatorsFor(predicateType);
	
	String [] neededType1 = {database, KConstants.PrologTypes.rfuzzy_enum_type};
	String [] neededType2 = {database, KConstants.PrologTypes.rfuzzy_integer_type};
	String [] neededType3 = {database, KConstants.PrologTypes.rfuzzy_string_type};
%>

<%
	if ((predicateInfo.hasType(neededType1, false)) || (predicateInfo.hasType(neededType2, false)) || (predicateInfo.hasType(neededType3, false))) {
%>
<select name="<%=lineId%>.<%=KConstants.Request.operatorParam %>"
	id="<%=lineId%>.<%=KConstants.Request.operatorParam %>">
	<%=JspsUtils.comboBoxDefaultValue() %>

	<% for (int i=0; i<operators.length; i++) {	%>
	<option value='<%=operators[i][0] %>'>
		<%= JspsUtils.getPrologNameInColloquialLanguage(operators[i][0])  %>
	</option>
	<% } %>
</select>
<script type="text/javascript">
		document.getElementById('<%= lineId %>.operatorDiv').style.display='inline';
	</script>
<% } else { %>
<script type="text/javascript">
		document.getElementById('<%= lineId %>.operatorDiv').style.display='none';
	</script>
<% } %>
