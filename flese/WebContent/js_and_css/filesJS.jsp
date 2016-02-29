/*! * auxiliarJS Library v1 * auxiliar javascript code * Author: Victor
Pablos Ceruelo */ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/

<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="storeHouse.ResultsStoreHouse"%>
<%@page import="filesAndPaths.ProgramFileInfo"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%
	
 if (JspsUtils.getStringWithValueS().equals("N")) { %>
<script type="text/javascript">
<% } 
%>
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function removeFileAction (urlRemove, params) {
	var divId = "<%=KConstants.JspsDivsIds.auxAndInvisibleSection %>";
	
	loadAjaxIn(divId, urlRemove + params);
}

function changeSharingState (urlRemove, params) {
	var divId = "<%=KConstants.JspsDivsIds.auxAndInvisibleSection %>";
	
	loadAjaxIn(divId, urlRemove + params);
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function fileViewAction(fileViewContentsDivId, urlFileView, params, fileName) {
	
	var containerId = fileViewContentsDivId;
	
	loadAjaxInDialog(containerId, urlFileView + params, 'Contents of program file ' + fileName);
	
	//prevent the browser to follow the link
	return false;
}

function myFunction(val) {
    document.getElementById("demo").innerHTML = val + "Hello World";
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function insertiFrameWindowEvaluationOfJS(uploadFormTargetiFrameId) {
	
	// This does not work on google chrome: "src='#' " 
    	
	$('#' + uploadFormTargetiFrameId).load(function() {
		// document.getElementById('#' + submitiFrameId);
		var responseHtmlText = null;
		var iFrameWindow = getIframeWindow(this);
		if ((notNullNorUndefined(iFrameWindow)) && (notNullNorUndefined(iFrameWindow.document)) && (notNullNorUndefined(iFrameWindow.document.body))) {
			
			// Save html received in iFrameWindow
			responseHtmlText = iFrameWindow.document.body.innerHTML;
			
			// Empty iFrameWindow html text.
			if (notNullNorUndefined(responseHtmlText)) {
				iFrameWindow.document.body.innerHTML="";
				debug.info(responseHtmlText);
				if (typeof(executeAjaxLoadedPageJS) == "function") {
					executeAjaxLoadedPageJS(responseHtmlText);
				}
				if (typeof(window.parent.executeAjaxLoadedPageJS) == "function") {
					window.parent.executeAjaxLoadedPageJS(responseHtmlText);
				}
			}
			
			// Clear the content of the iframe.
			// this.contentDocument.location.href = '/images/loading.gif';
			// alert("responseText: " + responseHtmlText);
			
			// var container = getContainer(uploadStatusDivId);
			// container.style.visibility = 'visible';
			// container.innerHTML = responseHtmlText;
		}
		  
	});	
}

function fileUploadAutomaticSendActionOnChange(formId, uploadStatusDivId) {
	// alert("Upload Submit Action started ...");
	var uploadStatusDiv = getContainer(uploadStatusDivId);
	uploadStatusDiv.style.visibility = 'visible';
	uploadStatusDiv.innerHTML = "";
	uploadStatusDiv.innerHTML = loadingImageHtml(true);

	var form = document.getElementById(formId);
	form.submit();
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/


<% if (JspsUtils.getStringWithValueS().equals("N")) { %>
</script>
<% } %>