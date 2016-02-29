
<%@page import="filesAndPaths.ProgramFileInfo"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="constants.KUrls"%>
<%@page import="storeHouse.RequestStoreHouse"%>


<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
	ProgramFileInfo programFileInfo = resultsStoreHouse.getProgramFileInfo();
	
	if (programFileInfo != null) {
		
		String urlFileView = KUrls.Files.View.getUrl(true);
		String params = "&" + KConstants.Request.fileOwnerParam + "=" + programFileInfo.getFileOwner() +
				"&" + KConstants.Request.fileNameParam + "=" + programFileInfo.getFileName() + 
				"&" + KConstants.Request.mode + "=" + KConstants.Request.modeBasic;

		String urlListFuzzifications = KUrls.Fuzzifications.List.getUrl(true);
		String fuzzificationParams = "&" + KConstants.Request.fileOwnerParam + "=" + programFileInfo.getFileOwner() +
		"&" + KConstants.Request.fileNameParam + "=" + programFileInfo.getFileName() + 
		"&" + KConstants.Request.mode + "=" + KConstants.Request.modeBasic;

%>
Program file actions: &nbsp;
<a href='#'
	onclick='fileViewAction("<%=KConstants.JspsDivsIds.fileViewContentsDiv %>", "<%=urlFileView%>", "<%=params %>", "<%=programFileInfo.getFileName() %>");'
	title='view program file <%= programFileInfo.getFileName() %>'>View
</a>
&nbsp;and/or&nbsp;
<a href='#' 
	onclick='return personalizeProgramFile("<%=urlListFuzzifications%>", "<%= fuzzificationParams %>", "<%=programFileInfo.getFileName() %>");'
	title='personalize program file'>Personalize
</a>

<% } %>