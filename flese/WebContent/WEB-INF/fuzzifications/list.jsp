<%@page import="programAnalysis.ProgramPartAnalysis"%>
<%@page import="constants.KUrls"%>
<%@page import="filesAndPaths.ProgramFileInfo"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.io.InputStreamReader"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
	ProgramFileInfo programFileInfo = resultsStoreHouse.getProgramFileInfo();
	ProgramPartAnalysis [][] fuzzifications = resultsStoreHouse.getProgramPartAnalysis();
	String mode = requestStoreHouse.getRequestParameter(KConstants.Request.mode);
	String urlEditFuzzification = KUrls.Fuzzifications.Edit.getUrl(true);
	String textMode = "";
	String textMode2 = "";
	if (mode.equals(KConstants.Request.modeAdvanced))
	{
		textMode = "Default";
		textMode2 = "If any user modifies a fuzzification, it is not possible to modify the default fuzzification anymore.";
	}
	if (fuzzifications.length != 0) {
%>
<center><br> Select the <strong> <%=textMode %> </strong> fuzzification you want to personalize: </center>
<div class='personalizationDivMainTable'>
	<div class='personalizationDivMainTableRow'>
		<div class='personalizationDivMainTableCell'>
			<div class='personalizationDivSelectFuzzificationTable'>
				<div class='personalizationDivSelectFuzzificationTableRow'>
					<div class='personalizationDivSelectFuzzificationTableCell'>
						I want to personalize how it is determined that a &nbsp;</div>
					<div class='personalizationDivSelectFuzzificationTableCell'>
								<select name="personalizationSelectComboBoxId"
							id="personalizationSelectComboBoxId"
							onchange="personalizationFunctionChanged(this, '<%=KConstants.JspsDivsIds.personalizationFunctionUnderModificationDivId %>', '<%= urlEditFuzzification %>');">
							<%=JspsUtils.comboBoxDefaultValue() %>
							<%
								for (int i=0; i<fuzzifications.length; i++) {
									if ((fuzzifications[i] != null) && (fuzzifications[i].length > 0)) {
										ProgramPartAnalysis fuzzification = fuzzifications[i][0];
										String desc = JspsUtils.getFromFuzzificationNameOf(fuzzification, KConstants.Fuzzifications.database, true) +
														" is " + 
														JspsUtils.getFromFuzzificationNameOf(fuzzification, KConstants.Fuzzifications.predDefined, true) +
														" from the value it has for " + 
														JspsUtils.getFromFuzzificationNameOf(fuzzification, KConstants.Fuzzifications.predNecessary, true);
										String id = "&" + KConstants.Request.fileOwnerParam + "=" + programFileInfo.getFileOwner() +
													"&" + KConstants.Request.fileNameParam + "=" + programFileInfo.getFileName() + 
													"&" + KConstants.Fuzzifications.predDefined + "=" + fuzzification.getPredDefined() +
													"&" + KConstants.Fuzzifications.predNecessary + "=" + fuzzification.getPredNecessary() +
													"&" + KConstants.Request.mode + "=" + mode;
										%>
										<option id='<%=desc%>' title='<%=desc%>' value='<%=id%>'><%= desc %></option>
										<%
										}
									}
									
									
							
%>
						</select>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class='personalizationDivMainTableRow'>
		<div class='personalizationDivMainTableCell'
			id='<%=KConstants.JspsDivsIds.personalizationFunctionUnderModificationDivId %>'>
			<%=textMode2%></div>
	</div>
</div>

<%
	} else {
%>
<div class='personalizationDivMainTable'>
	<div class='personalizationDivMainTableRow'>
		<div class='personalizationDivMainTableCell'>You cannot
			personalize this program file.</div>
	</div>
</div>

<% } %>
<script type="text/javascript">
function hide(){
	var earrings = document.getElementById('earringstd');
	earrings.style.visibility = 'hidden';
	}

	function show(){
	var earrings = document.getElementById('earringstd');
	earrings.style.visibility = 'visible';
	}
</script>



<!-- END -->