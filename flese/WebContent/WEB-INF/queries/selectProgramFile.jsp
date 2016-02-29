<%@page import="auxiliar.LocalUserInfo"%>
<%@page import="constants.KConstants"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="java.util.Iterator"%>
<%@page import="managers.FilesManagerAux"%>
<%@page import="filesAndPaths.ProgramFileInfo"%>
<%@page import="java.util.*"%>

<div class="defaultTable">
	<div id="selectDatabaseContainerDiv" class="selectDatabaseTable">
		<%
		RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
		ResultsStoreHouse resultsStoreHouse = JspsUtils.getResultsStoreHouse(requestStoreHouse);
		ProgramFileInfo[] filesList = resultsStoreHouse.getFilesList();

		String urlSelectQueryStartType = KUrls.Queries.SelectQueryStartType.getUrl(true);
		String urlProgramFileActions = KUrls.Queries.ProgramFileActions.getUrl(true); 
		String urlListFuzzifications = KUrls.Fuzzifications.Update.getUrl(true);
		
		String updateUrl = KUrls.Fuzzifications.Update.getUrl(true);
		JspsUtils.getValue(updateUrl);

		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
	
		if (filesList.length == 0) {
	%>
		<div class="selectDatabaseTableRow">
			<div class="selectDatabaseTableCell">No configuration files available. Please
				upload one via your user options.</div>
		</div>
		<%
		} else {
	%>
		<div class="selectDatabaseTableRow">
			<div class="selectDatabaseTableCell1">Please, select a configuration file:</div>
			<div class="selectDatabaseTableCell2">
				<select name="<%=KConstants.Request.programParam %>"
					id="<%=KConstants.Request.programParam %>"
					onchange="selectedProgramDatabaseChanged(this, '<%=urlSelectQueryStartType %>', '<%=urlProgramFileActions %>', '<%=KConstants.JspsDivsIds.fuzzificationUpdateStatusDivId %>','<%=updateUrl%>')">
					<%=JspsUtils.comboBoxDefaultValue()%>
					<%
					ArrayList<String> descList = new ArrayList<String>();
					ArrayList<String> valueList = new ArrayList<String>();
		for (int i=0; i<filesList.length; i++) { 
			if ((filesList[i].getSharingState())||(filesList[i].getFileOwner()).equals(localUserInfo.getLocalUserName()))
			{
				String value = filesList[i].getInfoForUrls();
				String desc = filesList[i].getFileName() + " ( owned by " + filesList[i].getFileOwner() + " ) ";
				descList.add(desc);
				valueList.add(value);
			}
		}
		for (int j = 0 ; j < descList.size() ; j++)
		{
			
	%>
					<option id='<%=valueList.get(j)%>' title='<%=valueList.get(j)%>' value='<%=valueList.get(j)%>'><%=descList.get(j)%></option>
					<%
		}
	%>
				</select>
			</div>
			<div class='selectDatabaseTableCell3'
				id='<%=KConstants.JspsDivsIds.programFileActionsContainerId%>'>
			</div>
		</div>
		<%
		}
	%>
	</div>
</div>

<div id="<%=KConstants.JspsDivsIds.selectQueryDivId%>"
	class="defaultTable"></div>

<div id="<%=KConstants.JspsDivsIds.runQueryDivId%>" class="defaultTable">
</div>

<div id='<%=KConstants.JspsDivsIds.fileViewContentsDiv %>'
	class='filesListTable' style='display: none;'></div>


<!--  EOF -->